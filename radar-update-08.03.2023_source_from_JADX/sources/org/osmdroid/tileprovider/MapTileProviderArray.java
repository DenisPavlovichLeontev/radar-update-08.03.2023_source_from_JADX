package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.osmdroid.tileprovider.modules.IFilesystemCache;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileContainer;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.TileSystem;

public class MapTileProviderArray extends MapTileProviderBase implements MapTileContainer {
    private static final int WORKING_STATUS_FOUND = 1;
    private static final int WORKING_STATUS_STARTED = 0;
    private IRegisterReceiver mRegisterReceiver;
    protected final List<MapTileModuleProviderBase> mTileProviderList;
    private final Map<Long, Integer> mWorking;

    public IFilesystemCache getTileWriter() {
        return null;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public boolean isDowngradedMode() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isDowngradedMode(long j) {
        return false;
    }

    protected MapTileProviderArray(ITileSource iTileSource, IRegisterReceiver iRegisterReceiver) {
        this(iTileSource, iRegisterReceiver, new MapTileModuleProviderBase[0]);
    }

    public MapTileProviderArray(ITileSource iTileSource, IRegisterReceiver iRegisterReceiver, MapTileModuleProviderBase[] mapTileModuleProviderBaseArr) {
        super(iTileSource);
        this.mWorking = new HashMap();
        this.mRegisterReceiver = iRegisterReceiver;
        ArrayList arrayList = new ArrayList();
        this.mTileProviderList = arrayList;
        Collections.addAll(arrayList, mapTileModuleProviderBaseArr);
    }

    public void detach() {
        synchronized (this.mTileProviderList) {
            for (MapTileModuleProviderBase detach : this.mTileProviderList) {
                detach.detach();
            }
        }
        synchronized (this.mWorking) {
            this.mWorking.clear();
        }
        IRegisterReceiver iRegisterReceiver = this.mRegisterReceiver;
        if (iRegisterReceiver != null) {
            iRegisterReceiver.destroy();
            this.mRegisterReceiver = null;
        }
        super.detach();
    }

    public boolean contains(long j) {
        boolean containsKey;
        synchronized (this.mWorking) {
            containsKey = this.mWorking.containsKey(Long.valueOf(j));
        }
        return containsKey;
    }

    public Drawable getMapTile(long j) {
        Drawable mapTile = this.mTileCache.getMapTile(j);
        if (mapTile != null && (ExpirableBitmapDrawable.getState(mapTile) == -1 || isDowngradedMode(j))) {
            return mapTile;
        }
        synchronized (this.mWorking) {
            if (this.mWorking.containsKey(Long.valueOf(j))) {
                return mapTile;
            }
            this.mWorking.put(Long.valueOf(j), 0);
            runAsyncNextProvider(new MapTileRequestState(j, this.mTileProviderList, (IMapTileProviderCallback) this));
            return mapTile;
        }
    }

    private void remove(long j) {
        synchronized (this.mWorking) {
            this.mWorking.remove(Long.valueOf(j));
        }
    }

    public void mapTileRequestCompleted(MapTileRequestState mapTileRequestState, Drawable drawable) {
        super.mapTileRequestCompleted(mapTileRequestState, drawable);
        remove(mapTileRequestState.getMapTile());
    }

    public void mapTileRequestFailed(MapTileRequestState mapTileRequestState) {
        runAsyncNextProvider(mapTileRequestState);
    }

    public void mapTileRequestFailedExceedsMaxQueueSize(MapTileRequestState mapTileRequestState) {
        super.mapTileRequestFailed(mapTileRequestState);
        remove(mapTileRequestState.getMapTile());
    }

    public void mapTileRequestExpiredTile(MapTileRequestState mapTileRequestState, Drawable drawable) {
        super.mapTileRequestExpiredTile(mapTileRequestState, drawable);
        synchronized (this.mWorking) {
            this.mWorking.put(Long.valueOf(mapTileRequestState.getMapTile()), 1);
        }
        runAsyncNextProvider(mapTileRequestState);
    }

    public long getQueueSize() {
        long size;
        synchronized (this.mWorking) {
            size = (long) this.mWorking.size();
        }
        return size;
    }

    /* access modifiers changed from: protected */
    public MapTileModuleProviderBase findNextAppropriateProvider(MapTileRequestState mapTileRequestState) {
        MapTileModuleProviderBase nextProvider;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        while (true) {
            nextProvider = mapTileRequestState.getNextProvider();
            if (nextProvider != null) {
                boolean z4 = true;
                z = !getProviderExists(nextProvider);
                boolean z5 = !useDataConnection() && nextProvider.getUsesDataConnection();
                int zoom = MapTileIndex.getZoom(mapTileRequestState.getMapTile());
                if (zoom <= nextProvider.getMaximumZoomLevel() && zoom >= nextProvider.getMinimumZoomLevel()) {
                    z4 = false;
                }
                boolean z6 = z5;
                z3 = z4;
                z2 = z6;
            }
            if (nextProvider == null || (!z && !z2 && !z3)) {
                return nextProvider;
            }
        }
        return nextProvider;
    }

    private void runAsyncNextProvider(MapTileRequestState mapTileRequestState) {
        Integer num;
        MapTileModuleProviderBase findNextAppropriateProvider = findNextAppropriateProvider(mapTileRequestState);
        if (findNextAppropriateProvider != null) {
            findNextAppropriateProvider.loadMapTileAsync(mapTileRequestState);
            return;
        }
        synchronized (this.mWorking) {
            num = this.mWorking.get(Long.valueOf(mapTileRequestState.getMapTile()));
        }
        if (num != null && num.intValue() == 0) {
            super.mapTileRequestFailed(mapTileRequestState);
        }
        remove(mapTileRequestState.getMapTile());
    }

    public boolean getProviderExists(MapTileModuleProviderBase mapTileModuleProviderBase) {
        return this.mTileProviderList.contains(mapTileModuleProviderBase);
    }

    public int getMinimumZoomLevel() {
        int maximumZoomLevel = TileSystem.getMaximumZoomLevel();
        synchronized (this.mTileProviderList) {
            for (MapTileModuleProviderBase next : this.mTileProviderList) {
                if (next.getMinimumZoomLevel() < maximumZoomLevel) {
                    maximumZoomLevel = next.getMinimumZoomLevel();
                }
            }
        }
        return maximumZoomLevel;
    }

    public int getMaximumZoomLevel() {
        int i;
        synchronized (this.mTileProviderList) {
            i = 0;
            for (MapTileModuleProviderBase next : this.mTileProviderList) {
                if (next.getMaximumZoomLevel() > i) {
                    i = next.getMaximumZoomLevel();
                }
            }
        }
        return i;
    }

    public void setTileSource(ITileSource iTileSource) {
        super.setTileSource(iTileSource);
        synchronized (this.mTileProviderList) {
            for (MapTileModuleProviderBase tileSource : this.mTileProviderList) {
                tileSource.setTileSource(iTileSource);
                clearTileCache();
            }
        }
    }
}
