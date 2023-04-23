package org.osmdroid.gpkg.tiles.raster;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import java.io.File;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.tiles.user.TileDao;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.IMapTileProviderCallback;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.modules.IFilesystemCache;
import org.osmdroid.tileprovider.modules.INetworkAvailablityCheck;
import org.osmdroid.tileprovider.modules.NetworkAvailabliltyCheck;
import org.osmdroid.tileprovider.modules.SqlTileWriter;
import org.osmdroid.tileprovider.modules.TileWriter;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;

public class GeoPackageProvider extends MapTileProviderArray implements IMapTileProviderCallback {
    protected GeoPackageMapTileModuleProvider geopackage;
    protected IFilesystemCache tileWriter;

    public GeoPackageProvider(File[] fileArr, Context context) {
        this(new SimpleRegisterReceiver(context), new NetworkAvailabliltyCheck(context), TileSourceFactory.DEFAULT_TILE_SOURCE, context, (IFilesystemCache) null, fileArr);
    }

    public GeoPackageProvider(IRegisterReceiver iRegisterReceiver, INetworkAvailablityCheck iNetworkAvailablityCheck, ITileSource iTileSource, Context context, IFilesystemCache iFilesystemCache, File[] fileArr) {
        super(iTileSource, iRegisterReceiver);
        Log.i(IMapView.LOGTAG, "Geopackage support is BETA. Please report any issues");
        if (iFilesystemCache != null) {
            this.tileWriter = iFilesystemCache;
        } else if (Build.VERSION.SDK_INT < 10) {
            this.tileWriter = new TileWriter();
        } else {
            this.tileWriter = new SqlTileWriter();
        }
        this.mTileProviderList.add(MapTileProviderBasic.getMapTileFileStorageProviderBase(iRegisterReceiver, iTileSource, this.tileWriter));
        this.geopackage = new GeoPackageMapTileModuleProvider(fileArr, context, this.tileWriter);
        this.mTileProviderList.add(this.geopackage);
    }

    public GeoPackageMapTileModuleProvider geoPackageMapTileModuleProvider() {
        return this.geopackage;
    }

    public IFilesystemCache getTileWriter() {
        return this.tileWriter;
    }

    public void detach() {
        IFilesystemCache iFilesystemCache = this.tileWriter;
        if (iFilesystemCache != null) {
            iFilesystemCache.onDetach();
        }
        this.tileWriter = null;
        this.geopackage.detach();
        super.detach();
    }

    public GeopackageRasterTileSource getTileSource(String str, String str2) {
        for (GeoPackage next : this.geopackage.tileSources) {
            if (next.getName().equalsIgnoreCase(str) && next.getTileTables().contains(str2)) {
                TileDao tileDao = next.getTileDao(str2);
                BoundingBox transform = tileDao.getProjection().getTransformation(tileDao.getProjection()).transform(tileDao.getBoundingBox());
                return new GeopackageRasterTileSource(str, str2, (int) tileDao.getMinZoom(), (int) tileDao.getMaxZoom(), new org.osmdroid.util.BoundingBox(transform.getMaxLatitude(), transform.getMaxLongitude(), transform.getMinLatitude(), transform.getMinLongitude()));
            }
        }
        return null;
    }

    public void setTileSource(ITileSource iTileSource) {
        super.setTileSource(iTileSource);
        this.geopackage.setTileSource(iTileSource);
    }
}
