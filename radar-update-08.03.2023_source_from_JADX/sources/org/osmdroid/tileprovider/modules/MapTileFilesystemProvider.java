package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.util.concurrent.atomic.AtomicReference;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.Counters;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.TileSystem;

public class MapTileFilesystemProvider extends MapTileFileStorageProviderBase {
    /* access modifiers changed from: private */
    public final AtomicReference<ITileSource> mTileSource;
    /* access modifiers changed from: private */
    public final TileWriter mWriter;

    /* access modifiers changed from: protected */
    public String getName() {
        return "File System Cache Provider";
    }

    /* access modifiers changed from: protected */
    public String getThreadGroupName() {
        return "filesystem";
    }

    public boolean getUsesDataConnection() {
        return false;
    }

    public MapTileFilesystemProvider(IRegisterReceiver iRegisterReceiver) {
        this(iRegisterReceiver, TileSourceFactory.DEFAULT_TILE_SOURCE);
    }

    public MapTileFilesystemProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource) {
        this(iRegisterReceiver, iTileSource, Configuration.getInstance().getExpirationExtendedDuration() + 604800000);
    }

    public MapTileFilesystemProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource, long j) {
        this(iRegisterReceiver, iTileSource, j, Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
    }

    public MapTileFilesystemProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource, long j, int i, int i2) {
        super(iRegisterReceiver, i, i2);
        TileWriter tileWriter = new TileWriter();
        this.mWriter = tileWriter;
        this.mTileSource = new AtomicReference<>();
        setTileSource(iTileSource);
        tileWriter.setMaximumCachedFileAge(j);
    }

    public TileLoader getTileLoader() {
        return new TileLoader();
    }

    public int getMinimumZoomLevel() {
        ITileSource iTileSource = this.mTileSource.get();
        if (iTileSource != null) {
            return iTileSource.getMinimumZoomLevel();
        }
        return 0;
    }

    public int getMaximumZoomLevel() {
        ITileSource iTileSource = this.mTileSource.get();
        if (iTileSource != null) {
            return iTileSource.getMaximumZoomLevel();
        }
        return TileSystem.getMaximumZoomLevel();
    }

    public void setTileSource(ITileSource iTileSource) {
        this.mTileSource.set(iTileSource);
    }

    protected class TileLoader extends MapTileModuleProviderBase.TileLoader {
        protected TileLoader() {
            super();
        }

        public Drawable loadTile(long j) throws CantContinueException {
            ITileSource iTileSource = (ITileSource) MapTileFilesystemProvider.this.mTileSource.get();
            if (iTileSource == null) {
                return null;
            }
            try {
                Drawable loadTile = MapTileFilesystemProvider.this.mWriter.loadTile(iTileSource, j);
                if (loadTile == null) {
                    Counters.fileCacheMiss++;
                } else {
                    Counters.fileCacheHit++;
                }
                return loadTile;
            } catch (BitmapTileSourceBase.LowMemoryException e) {
                Log.w(IMapView.LOGTAG, "LowMemoryException downloading MapTile: " + MapTileIndex.toString(j) + " : " + e);
                Counters.fileCacheOOM = Counters.fileCacheOOM + 1;
                throw new CantContinueException((Throwable) e);
            } catch (Throwable th) {
                Log.e(IMapView.LOGTAG, "Error loading tile", th);
                return null;
            }
        }
    }
}
