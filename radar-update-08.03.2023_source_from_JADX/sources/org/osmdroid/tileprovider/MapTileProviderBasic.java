package org.osmdroid.tileprovider;

import android.content.Context;
import android.os.Build;
import org.osmdroid.tileprovider.modules.IFilesystemCache;
import org.osmdroid.tileprovider.modules.INetworkAvailablityCheck;
import org.osmdroid.tileprovider.modules.MapTileApproximater;
import org.osmdroid.tileprovider.modules.MapTileAssetsProvider;
import org.osmdroid.tileprovider.modules.MapTileDownloader;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileFileStorageProviderBase;
import org.osmdroid.tileprovider.modules.MapTileFilesystemProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.modules.MapTileSqlCacheProvider;
import org.osmdroid.tileprovider.modules.NetworkAvailabliltyCheck;
import org.osmdroid.tileprovider.modules.SqlTileWriter;
import org.osmdroid.tileprovider.modules.TileWriter;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.MapTileAreaBorderComputer;
import org.osmdroid.util.MapTileAreaZoomComputer;
import org.osmdroid.util.MapTileIndex;

public class MapTileProviderBasic extends MapTileProviderArray implements IMapTileProviderCallback {
    private final MapTileApproximater mApproximationProvider;
    private final MapTileDownloader mDownloaderProvider;
    private final INetworkAvailablityCheck mNetworkAvailabilityCheck;
    protected IFilesystemCache tileWriter;

    public MapTileProviderBasic(Context context) {
        this(context, TileSourceFactory.DEFAULT_TILE_SOURCE);
    }

    public MapTileProviderBasic(Context context, ITileSource iTileSource) {
        this(context, iTileSource, (IFilesystemCache) null);
    }

    public MapTileProviderBasic(Context context, ITileSource iTileSource, IFilesystemCache iFilesystemCache) {
        this(new SimpleRegisterReceiver(context), new NetworkAvailabliltyCheck(context), iTileSource, context, iFilesystemCache);
    }

    public MapTileProviderBasic(IRegisterReceiver iRegisterReceiver, INetworkAvailablityCheck iNetworkAvailablityCheck, ITileSource iTileSource, Context context, IFilesystemCache iFilesystemCache) {
        super(iTileSource, iRegisterReceiver);
        this.mNetworkAvailabilityCheck = iNetworkAvailablityCheck;
        if (iFilesystemCache != null) {
            this.tileWriter = iFilesystemCache;
        } else if (Build.VERSION.SDK_INT < 10) {
            this.tileWriter = new TileWriter();
        } else {
            this.tileWriter = new SqlTileWriter();
        }
        MapTileFileStorageProviderBase createAssetsProvider = createAssetsProvider(iRegisterReceiver, iTileSource, context);
        this.mTileProviderList.add(createAssetsProvider);
        MapTileFileStorageProviderBase mapTileFileStorageProviderBase = getMapTileFileStorageProviderBase(iRegisterReceiver, iTileSource, this.tileWriter);
        this.mTileProviderList.add(mapTileFileStorageProviderBase);
        MapTileFileStorageProviderBase createArchiveProvider = createArchiveProvider(iRegisterReceiver, iTileSource);
        this.mTileProviderList.add(createArchiveProvider);
        MapTileApproximater createApproximater = createApproximater(createAssetsProvider, mapTileFileStorageProviderBase, createArchiveProvider);
        this.mApproximationProvider = createApproximater;
        this.mTileProviderList.add(createApproximater);
        MapTileDownloader createDownloaderProvider = createDownloaderProvider(iNetworkAvailablityCheck, iTileSource);
        this.mDownloaderProvider = createDownloaderProvider;
        this.mTileProviderList.add(createDownloaderProvider);
        getTileCache().getProtectedTileComputers().add(new MapTileAreaZoomComputer(-1));
        getTileCache().getProtectedTileComputers().add(new MapTileAreaBorderComputer(1));
        getTileCache().setAutoEnsureCapacity(false);
        getTileCache().setStressedMemory(false);
        getTileCache().getPreCache().addProvider(createAssetsProvider);
        getTileCache().getPreCache().addProvider(mapTileFileStorageProviderBase);
        getTileCache().getPreCache().addProvider(createArchiveProvider);
        getTileCache().getPreCache().addProvider(createDownloaderProvider);
        getTileCache().getProtectedTileContainers().add(this);
        setOfflineFirst(true);
    }

    /* access modifiers changed from: protected */
    public MapTileApproximater createApproximater(MapTileFileStorageProviderBase mapTileFileStorageProviderBase, MapTileFileStorageProviderBase mapTileFileStorageProviderBase2, MapTileFileStorageProviderBase mapTileFileStorageProviderBase3) {
        MapTileApproximater mapTileApproximater = new MapTileApproximater();
        mapTileApproximater.addProvider(mapTileFileStorageProviderBase);
        mapTileApproximater.addProvider(mapTileFileStorageProviderBase2);
        mapTileApproximater.addProvider(mapTileFileStorageProviderBase3);
        return mapTileApproximater;
    }

    /* access modifiers changed from: protected */
    public MapTileFileStorageProviderBase createArchiveProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource) {
        return new MapTileFileArchiveProvider(iRegisterReceiver, iTileSource);
    }

    /* access modifiers changed from: protected */
    public MapTileFileStorageProviderBase createAssetsProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource, Context context) {
        return new MapTileAssetsProvider(iRegisterReceiver, context.getAssets(), iTileSource);
    }

    public IFilesystemCache getTileWriter() {
        return this.tileWriter;
    }

    /* access modifiers changed from: protected */
    public MapTileDownloader createDownloaderProvider(INetworkAvailablityCheck iNetworkAvailablityCheck, ITileSource iTileSource) {
        return new MapTileDownloader(iTileSource, this.tileWriter, iNetworkAvailablityCheck);
    }

    public void detach() {
        IFilesystemCache iFilesystemCache = this.tileWriter;
        if (iFilesystemCache != null) {
            iFilesystemCache.onDetach();
        }
        this.tileWriter = null;
        super.detach();
    }

    /* access modifiers changed from: protected */
    public boolean isDowngradedMode(long j) {
        int zoom;
        INetworkAvailablityCheck iNetworkAvailablityCheck = this.mNetworkAvailabilityCheck;
        if ((iNetworkAvailablityCheck != null && !iNetworkAvailablityCheck.getNetworkAvailable()) || !useDataConnection()) {
            return true;
        }
        int i = -1;
        int i2 = -1;
        for (MapTileModuleProviderBase mapTileModuleProviderBase : this.mTileProviderList) {
            if (mapTileModuleProviderBase.getUsesDataConnection()) {
                int minimumZoomLevel = mapTileModuleProviderBase.getMinimumZoomLevel();
                if (i == -1 || i > minimumZoomLevel) {
                    i = minimumZoomLevel;
                }
                int maximumZoomLevel = mapTileModuleProviderBase.getMaximumZoomLevel();
                if (i2 == -1 || i2 < maximumZoomLevel) {
                    i2 = maximumZoomLevel;
                }
            }
        }
        if (i == -1 || i2 == -1 || (zoom = MapTileIndex.getZoom(j)) < i || zoom > i2) {
            return true;
        }
        return false;
    }

    public static MapTileFileStorageProviderBase getMapTileFileStorageProviderBase(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource, IFilesystemCache iFilesystemCache) {
        if (iFilesystemCache instanceof TileWriter) {
            return new MapTileFilesystemProvider(iRegisterReceiver, iTileSource);
        }
        return new MapTileSqlCacheProvider(iRegisterReceiver, iTileSource);
    }

    public boolean setOfflineFirst(boolean z) {
        int i = 0;
        int i2 = -1;
        int i3 = -1;
        for (MapTileModuleProviderBase mapTileModuleProviderBase : this.mTileProviderList) {
            if (i2 == -1 && mapTileModuleProviderBase == this.mDownloaderProvider) {
                i2 = i;
            }
            if (i3 == -1 && mapTileModuleProviderBase == this.mApproximationProvider) {
                i3 = i;
            }
            i++;
        }
        if (i2 == -1 || i3 == -1) {
            return false;
        }
        if (i3 < i2 && z) {
            return true;
        }
        if (i3 > i2 && !z) {
            return true;
        }
        this.mTileProviderList.set(i2, this.mApproximationProvider);
        this.mTileProviderList.set(i3, this.mDownloaderProvider);
        return true;
    }
}
