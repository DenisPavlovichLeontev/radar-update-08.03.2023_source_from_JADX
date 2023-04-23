package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.MapTileRequestState;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public abstract class MapTileModuleProviderBase {
    private final ExecutorService mExecutor;
    protected final LinkedHashMap<Long, MapTileRequestState> mPending;
    protected final Object mQueueLockObject = new Object();
    protected final HashMap<Long, MapTileRequestState> mWorking;

    public abstract int getMaximumZoomLevel();

    public abstract int getMinimumZoomLevel();

    /* access modifiers changed from: protected */
    public abstract String getName();

    /* access modifiers changed from: protected */
    public abstract String getThreadGroupName();

    public abstract TileLoader getTileLoader();

    public abstract boolean getUsesDataConnection();

    public abstract void setTileSource(ITileSource iTileSource);

    public boolean isTileReachable(long j) {
        int zoom = MapTileIndex.getZoom(j);
        return zoom >= getMinimumZoomLevel() && zoom <= getMaximumZoomLevel();
    }

    public MapTileModuleProviderBase(int i, int i2) {
        if (i2 < i) {
            Log.w(IMapView.LOGTAG, "The pending queue size is smaller than the thread pool size. Automatically reducing the thread pool size.");
            i = i2;
        }
        this.mExecutor = Executors.newFixedThreadPool(i, new ConfigurablePriorityThreadFactory(5, getThreadGroupName()));
        this.mWorking = new HashMap<>();
        final int i3 = i2;
        this.mPending = new LinkedHashMap<Long, MapTileRequestState>(i2 + 2, 0.1f, true) {
            private static final long serialVersionUID = 6455337315681858866L;

            /* access modifiers changed from: protected */
            public boolean removeEldestEntry(Map.Entry<Long, MapTileRequestState> entry) {
                MapTileRequestState mapTileRequestState;
                if (size() <= i3) {
                    return false;
                }
                Iterator<Long> it = MapTileModuleProviderBase.this.mPending.keySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    long longValue = it.next().longValue();
                    if (!MapTileModuleProviderBase.this.mWorking.containsKey(Long.valueOf(longValue)) && (mapTileRequestState = MapTileModuleProviderBase.this.mPending.get(Long.valueOf(longValue))) != null) {
                        MapTileModuleProviderBase.this.removeTileFromQueues(longValue);
                        mapTileRequestState.getCallback().mapTileRequestFailedExceedsMaxQueueSize(mapTileRequestState);
                        break;
                    }
                }
                return false;
            }
        };
    }

    public void loadMapTileAsync(MapTileRequestState mapTileRequestState) {
        if (!this.mExecutor.isShutdown()) {
            synchronized (this.mQueueLockObject) {
                if (Configuration.getInstance().isDebugTileProviders()) {
                    Log.d(IMapView.LOGTAG, "MapTileModuleProviderBase.loadMaptileAsync() on provider: " + getName() + " for tile: " + MapTileIndex.toString(mapTileRequestState.getMapTile()));
                    if (this.mPending.containsKey(Long.valueOf(mapTileRequestState.getMapTile()))) {
                        Log.d(IMapView.LOGTAG, "MapTileModuleProviderBase.loadMaptileAsync() tile already exists in request queue for modular provider. Moving to front of queue.");
                    } else {
                        Log.d(IMapView.LOGTAG, "MapTileModuleProviderBase.loadMaptileAsync() adding tile to request queue for modular provider.");
                    }
                }
                this.mPending.put(Long.valueOf(mapTileRequestState.getMapTile()), mapTileRequestState);
            }
            try {
                this.mExecutor.execute(getTileLoader());
            } catch (RejectedExecutionException e) {
                Log.w(IMapView.LOGTAG, "RejectedExecutionException", e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void clearQueue() {
        synchronized (this.mQueueLockObject) {
            this.mPending.clear();
            this.mWorking.clear();
        }
    }

    public void detach() {
        clearQueue();
        this.mExecutor.shutdown();
    }

    /* access modifiers changed from: protected */
    public void removeTileFromQueues(long j) {
        synchronized (this.mQueueLockObject) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                Log.d(IMapView.LOGTAG, "MapTileModuleProviderBase.removeTileFromQueues() on provider: " + getName() + " for tile: " + MapTileIndex.toString(j));
            }
            this.mPending.remove(Long.valueOf(j));
            this.mWorking.remove(Long.valueOf(j));
        }
    }

    public abstract class TileLoader implements Runnable {
        public abstract Drawable loadTile(long j) throws CantContinueException;

        /* access modifiers changed from: protected */
        public void onTileLoaderInit() {
        }

        /* access modifiers changed from: protected */
        public void onTileLoaderShutdown() {
        }

        public TileLoader() {
        }

        public Drawable loadTileIfReachable(long j) throws CantContinueException {
            if (!MapTileModuleProviderBase.this.isTileReachable(j)) {
                return null;
            }
            return loadTile(j);
        }

        /* access modifiers changed from: protected */
        @Deprecated
        public Drawable loadTile(MapTileRequestState mapTileRequestState) throws CantContinueException {
            return loadTileIfReachable(mapTileRequestState.getMapTile());
        }

        /* access modifiers changed from: protected */
        public MapTileRequestState nextTile() {
            MapTileRequestState mapTileRequestState;
            synchronized (MapTileModuleProviderBase.this.mQueueLockObject) {
                mapTileRequestState = null;
                Long l = null;
                for (Long next : MapTileModuleProviderBase.this.mPending.keySet()) {
                    if (!MapTileModuleProviderBase.this.mWorking.containsKey(next)) {
                        if (Configuration.getInstance().isDebugTileProviders()) {
                            Log.d(IMapView.LOGTAG, "TileLoader.nextTile() on provider: " + MapTileModuleProviderBase.this.getName() + " found tile in working queue: " + MapTileIndex.toString(next.longValue()));
                        }
                        l = next;
                    }
                }
                if (l != null) {
                    if (Configuration.getInstance().isDebugTileProviders()) {
                        Log.d(IMapView.LOGTAG, "TileLoader.nextTile() on provider: " + MapTileModuleProviderBase.this.getName() + " adding tile to working queue: " + l);
                    }
                    MapTileModuleProviderBase.this.mWorking.put(l, MapTileModuleProviderBase.this.mPending.get(l));
                }
                if (l != null) {
                    mapTileRequestState = MapTileModuleProviderBase.this.mPending.get(l);
                }
            }
            return mapTileRequestState;
        }

        /* access modifiers changed from: protected */
        public void tileLoaded(MapTileRequestState mapTileRequestState, Drawable drawable) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                Log.d(IMapView.LOGTAG, "TileLoader.tileLoaded() on provider: " + MapTileModuleProviderBase.this.getName() + " with tile: " + MapTileIndex.toString(mapTileRequestState.getMapTile()));
            }
            MapTileModuleProviderBase.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            ExpirableBitmapDrawable.setState(drawable, -1);
            mapTileRequestState.getCallback().mapTileRequestCompleted(mapTileRequestState, drawable);
        }

        /* access modifiers changed from: protected */
        public void tileLoadedExpired(MapTileRequestState mapTileRequestState, Drawable drawable) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                Log.d(IMapView.LOGTAG, "TileLoader.tileLoadedExpired() on provider: " + MapTileModuleProviderBase.this.getName() + " with tile: " + MapTileIndex.toString(mapTileRequestState.getMapTile()));
            }
            MapTileModuleProviderBase.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            ExpirableBitmapDrawable.setState(drawable, -2);
            mapTileRequestState.getCallback().mapTileRequestExpiredTile(mapTileRequestState, drawable);
        }

        /* access modifiers changed from: protected */
        public void tileLoadedScaled(MapTileRequestState mapTileRequestState, Drawable drawable) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                Log.d(IMapView.LOGTAG, "TileLoader.tileLoadedScaled() on provider: " + MapTileModuleProviderBase.this.getName() + " with tile: " + MapTileIndex.toString(mapTileRequestState.getMapTile()));
            }
            MapTileModuleProviderBase.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            ExpirableBitmapDrawable.setState(drawable, -3);
            mapTileRequestState.getCallback().mapTileRequestExpiredTile(mapTileRequestState, drawable);
        }

        /* access modifiers changed from: protected */
        public void tileLoadedFailed(MapTileRequestState mapTileRequestState) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                Log.d(IMapView.LOGTAG, "TileLoader.tileLoadedFailed() on provider: " + MapTileModuleProviderBase.this.getName() + " with tile: " + MapTileIndex.toString(mapTileRequestState.getMapTile()));
            }
            MapTileModuleProviderBase.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            mapTileRequestState.getCallback().mapTileRequestFailed(mapTileRequestState);
        }

        public final void run() {
            onTileLoaderInit();
            while (true) {
                MapTileRequestState nextTile = nextTile();
                if (nextTile != null) {
                    if (Configuration.getInstance().isDebugTileProviders()) {
                        Log.d(IMapView.LOGTAG, "TileLoader.run() processing next tile: " + MapTileIndex.toString(nextTile.getMapTile()) + ", pending:" + MapTileModuleProviderBase.this.mPending.size() + ", working:" + MapTileModuleProviderBase.this.mWorking.size());
                    }
                    Drawable drawable = null;
                    try {
                        drawable = loadTileIfReachable(nextTile.getMapTile());
                    } catch (CantContinueException e) {
                        Log.i(IMapView.LOGTAG, "Tile loader can't continue: " + MapTileIndex.toString(nextTile.getMapTile()), e);
                        MapTileModuleProviderBase.this.clearQueue();
                    } catch (Throwable th) {
                        Log.i(IMapView.LOGTAG, "Error downloading tile: " + MapTileIndex.toString(nextTile.getMapTile()), th);
                    }
                    if (drawable == null) {
                        tileLoadedFailed(nextTile);
                    } else if (ExpirableBitmapDrawable.getState(drawable) == -2) {
                        tileLoadedExpired(nextTile, drawable);
                    } else if (ExpirableBitmapDrawable.getState(drawable) == -3) {
                        tileLoadedScaled(nextTile, drawable);
                    } else {
                        tileLoaded(nextTile, drawable);
                    }
                } else {
                    onTileLoaderShutdown();
                    return;
                }
            }
        }
    }
}
