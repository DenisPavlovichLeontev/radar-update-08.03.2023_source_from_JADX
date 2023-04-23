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
import org.osmdroid.tileprovider.util.Counters;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.TileSystem;

public class MapTileSqlCacheProvider extends MapTileFileStorageProviderBase {
    private static final String[] columns = {DatabaseFileArchive.COLUMN_TILE, SqlTileWriter.COLUMN_EXPIRES};
    /* access modifiers changed from: private */
    public final AtomicReference<ITileSource> mTileSource;
    /* access modifiers changed from: private */
    public SqlTileWriter mWriter;

    /* access modifiers changed from: protected */
    public String getName() {
        return "SQL Cache Archive Provider";
    }

    /* access modifiers changed from: protected */
    public String getThreadGroupName() {
        return "sqlcache";
    }

    public boolean getUsesDataConnection() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onMediaMounted() {
    }

    @Deprecated
    public MapTileSqlCacheProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource, long j) {
        this(iRegisterReceiver, iTileSource);
    }

    public MapTileSqlCacheProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource) {
        super(iRegisterReceiver, Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
        this.mTileSource = new AtomicReference<>();
        setTileSource(iTileSource);
        this.mWriter = new SqlTileWriter();
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

    /* access modifiers changed from: protected */
    public void onMediaUnmounted() {
        SqlTileWriter sqlTileWriter = this.mWriter;
        if (sqlTileWriter != null) {
            sqlTileWriter.onDetach();
        }
        this.mWriter = new SqlTileWriter();
    }

    public void setTileSource(ITileSource iTileSource) {
        this.mTileSource.set(iTileSource);
    }

    public void detach() {
        SqlTileWriter sqlTileWriter = this.mWriter;
        if (sqlTileWriter != null) {
            sqlTileWriter.onDetach();
        }
        this.mWriter = null;
        super.detach();
    }

    public boolean hasTile(long j) {
        ITileSource iTileSource = this.mTileSource.get();
        if (iTileSource == null || this.mWriter.getExpirationTimestamp(iTileSource, j) == null) {
            return false;
        }
        return true;
    }

    protected class TileLoader extends MapTileModuleProviderBase.TileLoader {
        protected TileLoader() {
            super();
        }

        public Drawable loadTile(long j) throws CantContinueException {
            ITileSource iTileSource = (ITileSource) MapTileSqlCacheProvider.this.mTileSource.get();
            if (iTileSource == null) {
                return null;
            }
            if (MapTileSqlCacheProvider.this.mWriter != null) {
                try {
                    Drawable loadTile = MapTileSqlCacheProvider.this.mWriter.loadTile(iTileSource, j);
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
            } else {
                Log.d(IMapView.LOGTAG, "TileLoader failed to load tile due to mWriter being null (map shutdown?)");
                return null;
            }
        }
    }
}
