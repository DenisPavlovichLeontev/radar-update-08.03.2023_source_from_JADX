package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import java.util.concurrent.atomic.AtomicReference;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.tileprovider.MapTileRequestState;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.TileSystem;
import org.osmdroid.util.UrlBackoff;

public class MapTileDownloader extends MapTileModuleProviderBase {
    /* access modifiers changed from: private */
    public final IFilesystemCache mFilesystemCache;
    /* access modifiers changed from: private */
    public final INetworkAvailablityCheck mNetworkAvailablityCheck;
    /* access modifiers changed from: private */
    public TileDownloader mTileDownloader;
    private final TileLoader mTileLoader;
    /* access modifiers changed from: private */
    public final AtomicReference<OnlineTileSourceBase> mTileSource;
    /* access modifiers changed from: private */
    public final UrlBackoff mUrlBackoff;

    /* access modifiers changed from: protected */
    public String getName() {
        return "Online Tile Download Provider";
    }

    /* access modifiers changed from: protected */
    public String getThreadGroupName() {
        return "downloader";
    }

    public boolean getUsesDataConnection() {
        return true;
    }

    public MapTileDownloader(ITileSource iTileSource) {
        this(iTileSource, (IFilesystemCache) null, (INetworkAvailablityCheck) null);
    }

    public MapTileDownloader(ITileSource iTileSource, IFilesystemCache iFilesystemCache) {
        this(iTileSource, iFilesystemCache, (INetworkAvailablityCheck) null);
    }

    public MapTileDownloader(ITileSource iTileSource, IFilesystemCache iFilesystemCache, INetworkAvailablityCheck iNetworkAvailablityCheck) {
        this(iTileSource, iFilesystemCache, iNetworkAvailablityCheck, Configuration.getInstance().getTileDownloadThreads(), Configuration.getInstance().getTileDownloadMaxQueueSize());
    }

    public MapTileDownloader(ITileSource iTileSource, IFilesystemCache iFilesystemCache, INetworkAvailablityCheck iNetworkAvailablityCheck, int i, int i2) {
        super(i, i2);
        this.mTileSource = new AtomicReference<>();
        this.mTileLoader = new TileLoader();
        this.mUrlBackoff = new UrlBackoff();
        this.mTileDownloader = new TileDownloader();
        this.mFilesystemCache = iFilesystemCache;
        this.mNetworkAvailablityCheck = iNetworkAvailablityCheck;
        setTileSource(iTileSource);
    }

    public ITileSource getTileSource() {
        return this.mTileSource.get();
    }

    public TileLoader getTileLoader() {
        return this.mTileLoader;
    }

    public void detach() {
        super.detach();
        IFilesystemCache iFilesystemCache = this.mFilesystemCache;
        if (iFilesystemCache != null) {
            iFilesystemCache.onDetach();
        }
    }

    public int getMinimumZoomLevel() {
        OnlineTileSourceBase onlineTileSourceBase = this.mTileSource.get();
        if (onlineTileSourceBase != null) {
            return onlineTileSourceBase.getMinimumZoomLevel();
        }
        return 0;
    }

    public int getMaximumZoomLevel() {
        OnlineTileSourceBase onlineTileSourceBase = this.mTileSource.get();
        if (onlineTileSourceBase != null) {
            return onlineTileSourceBase.getMaximumZoomLevel();
        }
        return TileSystem.getMaximumZoomLevel();
    }

    public void setTileSource(ITileSource iTileSource) {
        if (iTileSource instanceof OnlineTileSourceBase) {
            this.mTileSource.set((OnlineTileSourceBase) iTileSource);
        } else {
            this.mTileSource.set((Object) null);
        }
    }

    protected class TileLoader extends MapTileModuleProviderBase.TileLoader {
        protected TileLoader() {
            super();
        }

        /* access modifiers changed from: protected */
        public Drawable downloadTile(long j, int i, String str) throws CantContinueException {
            OnlineTileSourceBase onlineTileSourceBase = (OnlineTileSourceBase) MapTileDownloader.this.mTileSource.get();
            if (onlineTileSourceBase == null) {
                return null;
            }
            try {
                onlineTileSourceBase.acquire();
                try {
                    return MapTileDownloader.this.mTileDownloader.downloadTile(j, i, str, MapTileDownloader.this.mFilesystemCache, onlineTileSourceBase);
                } finally {
                    onlineTileSourceBase.release();
                }
            } catch (InterruptedException unused) {
                return null;
            }
        }

        public Drawable loadTile(long j) throws CantContinueException {
            OnlineTileSourceBase onlineTileSourceBase = (OnlineTileSourceBase) MapTileDownloader.this.mTileSource.get();
            if (onlineTileSourceBase == null) {
                return null;
            }
            if (MapTileDownloader.this.mNetworkAvailablityCheck == null || MapTileDownloader.this.mNetworkAvailablityCheck.getNetworkAvailable()) {
                String tileURLString = onlineTileSourceBase.getTileURLString(j);
                if (TextUtils.isEmpty(tileURLString) || MapTileDownloader.this.mUrlBackoff.shouldWait(tileURLString)) {
                    return null;
                }
                Drawable downloadTile = downloadTile(j, 0, tileURLString);
                if (downloadTile == null) {
                    MapTileDownloader.this.mUrlBackoff.next(tileURLString);
                } else {
                    MapTileDownloader.this.mUrlBackoff.remove(tileURLString);
                }
                return downloadTile;
            }
            if (Configuration.getInstance().isDebugMode()) {
                Log.d(IMapView.LOGTAG, "Skipping " + MapTileDownloader.this.getName() + " due to NetworkAvailabliltyCheck.");
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void tileLoaded(MapTileRequestState mapTileRequestState, Drawable drawable) {
            MapTileDownloader.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            mapTileRequestState.getCallback().mapTileRequestCompleted(mapTileRequestState, (Drawable) null);
            BitmapPool.getInstance().asyncRecycle(drawable);
        }
    }

    public void setTileDownloader(TileDownloader tileDownloader) {
        this.mTileDownloader = tileDownloader;
    }
}
