package org.osmdroid.gpkg.tiles.raster;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.GeoPackageManager;
import mil.nga.geopackage.factory.GeoPackageFactory;
import mil.nga.geopackage.tiles.retriever.GeoPackageTile;
import mil.nga.geopackage.tiles.retriever.GeoPackageTileRetriever;
import mil.nga.geopackage.tiles.user.TileDao;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.modules.IFilesystemCache;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;

public class GeoPackageMapTileModuleProvider extends MapTileModuleProviderBase {
    protected GeopackageRasterTileSource currentTileSource;
    protected GeoPackageManager manager;
    protected Set<GeoPackage> tileSources = new HashSet();
    private final TileSystem tileSystem = MapView.getTileSystem();
    protected IFilesystemCache tileWriter = null;

    /* access modifiers changed from: protected */
    public String getName() {
        return "Geopackage";
    }

    public boolean getUsesDataConnection() {
        return false;
    }

    public GeoPackageMapTileModuleProvider(File[] fileArr, Context context, IFilesystemCache iFilesystemCache) {
        super(Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
        Log.i(IMapView.LOGTAG, "Geopackage support is BETA. Please report any issues");
        this.tileWriter = iFilesystemCache;
        this.manager = GeoPackageFactory.getManager(context);
        for (int i = 0; i < fileArr.length; i++) {
            try {
                this.manager.importGeoPackage(fileArr[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<String> databases = this.manager.databases();
        for (int i2 = 0; i2 < databases.size(); i2++) {
            this.tileSources.add(this.manager.open(databases.get(i2)));
        }
    }

    public Drawable getMapTile(long j) {
        BitmapDrawable bitmapDrawable;
        byte[] bArr;
        String database = this.currentTileSource.getDatabase();
        String tableDao = this.currentTileSource.getTableDao();
        GeoPackage open = this.manager.open(database);
        GeoPackageTile tile = new GeoPackageTileRetriever(open.getTileDao(tableDao)).getTile(MapTileIndex.getX(j), MapTileIndex.getY(j), MapTileIndex.getZoom(j));
        if (tile == null || tile.data == null || (bArr = tile.data) == null) {
            bitmapDrawable = null;
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outHeight = 256;
            options.outWidth = 256;
            bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options));
        }
        open.close();
        return bitmapDrawable;
    }

    public List<GeopackageRasterTileSource> getTileSources() {
        GeoPackageMapTileModuleProvider geoPackageMapTileModuleProvider = this;
        ArrayList arrayList = new ArrayList();
        List<String> databases = geoPackageMapTileModuleProvider.manager.databases();
        int i = 0;
        while (i < databases.size()) {
            GeoPackage open = geoPackageMapTileModuleProvider.manager.open(databases.get(i));
            List<String> tileTables = open.getTileTables();
            int i2 = 0;
            while (i2 < tileTables.size()) {
                TileDao tileDao = open.getTileDao(tileTables.get(i2));
                BoundingBox transform = tileDao.getProjection().getTransformation(4326).transform(tileDao.getBoundingBox());
                int i3 = i;
                arrayList = arrayList;
                arrayList.add(new GeopackageRasterTileSource(databases.get(i3), tileTables.get(i2), (int) tileDao.getMinZoom(), (int) tileDao.getMaxZoom(), new org.osmdroid.util.BoundingBox(Math.min(geoPackageMapTileModuleProvider.tileSystem.getMaxLatitude(), transform.getMaxLatitude()), transform.getMaxLongitude(), Math.max(geoPackageMapTileModuleProvider.tileSystem.getMinLatitude(), transform.getMinLatitude()), transform.getMinLongitude())));
                i2++;
                geoPackageMapTileModuleProvider = this;
                i = i3;
            }
            open.close();
            i++;
            geoPackageMapTileModuleProvider = this;
        }
        return arrayList;
    }

    public List<GeopackageRasterTileSource> getTileSources(String str) {
        GeoPackageMapTileModuleProvider geoPackageMapTileModuleProvider = this;
        ArrayList arrayList = new ArrayList();
        GeoPackage open = geoPackageMapTileModuleProvider.manager.open(str);
        List<String> tileTables = open.getTileTables();
        int i = 0;
        while (i < tileTables.size()) {
            TileDao tileDao = open.getTileDao(tileTables.get(i));
            BoundingBox transform = tileDao.getProjection().getTransformation(4326).transform(tileDao.getBoundingBox());
            arrayList = arrayList;
            arrayList.add(new GeopackageRasterTileSource(str, tileTables.get(i), (int) tileDao.getMinZoom(), (int) tileDao.getMaxZoom(), new org.osmdroid.util.BoundingBox(Math.min(geoPackageMapTileModuleProvider.tileSystem.getMaxLatitude(), transform.getMaxLatitude()), transform.getMaxLongitude(), Math.max(geoPackageMapTileModuleProvider.tileSystem.getMinLatitude(), transform.getMinLatitude()), transform.getMinLongitude())));
            i++;
            geoPackageMapTileModuleProvider = this;
        }
        open.close();
        return arrayList;
    }

    public void detach() {
        Set<GeoPackage> set = this.tileSources;
        if (set != null) {
            for (GeoPackage close : set) {
                close.close();
            }
            this.tileSources.clear();
        }
        this.manager = null;
    }

    protected class TileLoader extends MapTileModuleProviderBase.TileLoader {
        protected TileLoader() {
            super();
        }

        public Drawable loadTile(long j) {
            try {
                return GeoPackageMapTileModuleProvider.this.getMapTile(j);
            } catch (Throwable th) {
                Log.e(IMapView.LOGTAG, "Error loading tile", th);
                return null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public String getThreadGroupName() {
        return getName();
    }

    public TileLoader getTileLoader() {
        return new TileLoader();
    }

    public int getMinimumZoomLevel() {
        GeopackageRasterTileSource geopackageRasterTileSource = this.currentTileSource;
        if (geopackageRasterTileSource != null) {
            return geopackageRasterTileSource.getMinimumZoomLevel();
        }
        return 0;
    }

    public int getMaximumZoomLevel() {
        GeopackageRasterTileSource geopackageRasterTileSource = this.currentTileSource;
        if (geopackageRasterTileSource != null) {
            return geopackageRasterTileSource.getMaximumZoomLevel();
        }
        return 22;
    }

    public void setTileSource(ITileSource iTileSource) {
        if (iTileSource instanceof GeopackageRasterTileSource) {
            this.currentTileSource = (GeopackageRasterTileSource) iTileSource;
        }
    }
}
