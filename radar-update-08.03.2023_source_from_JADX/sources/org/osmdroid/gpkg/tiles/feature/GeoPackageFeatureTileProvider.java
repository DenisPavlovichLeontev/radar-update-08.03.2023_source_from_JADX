package org.osmdroid.gpkg.tiles.feature;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import mil.nga.geopackage.tiles.features.FeatureTiles;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.modules.IFilesystemCache;
import org.osmdroid.tileprovider.modules.SqlTileWriter;
import org.osmdroid.tileprovider.modules.TileWriter;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public class GeoPackageFeatureTileProvider extends MapTileProviderBase {
    protected FeatureTiles featureTiles = null;
    protected int minzoom = 0;
    protected IFilesystemCache tileWriter;

    public int getMaximumZoomLevel() {
        return 22;
    }

    public long getQueueSize() {
        return 0;
    }

    public GeoPackageFeatureTileProvider(ITileSource iTileSource) {
        super(iTileSource);
        Log.i(IMapView.LOGTAG, "Geopackage support is BETA. Please report any issues");
        if (Build.VERSION.SDK_INT < 10) {
            this.tileWriter = new TileWriter();
        } else {
            this.tileWriter = new SqlTileWriter();
        }
    }

    public Drawable getMapTile(long j) {
        Bitmap drawTile;
        FeatureTiles featureTiles2 = this.featureTiles;
        if (featureTiles2 == null || (drawTile = featureTiles2.drawTile(MapTileIndex.getX(j), MapTileIndex.getY(j), MapTileIndex.getZoom(j))) == null) {
            return null;
        }
        return new BitmapDrawable(drawTile);
    }

    public int getMinimumZoomLevel() {
        return this.minzoom;
    }

    public IFilesystemCache getTileWriter() {
        return this.tileWriter;
    }

    public void set(int i, FeatureTiles featureTiles2) {
        this.featureTiles = featureTiles2;
        this.minzoom = i;
    }

    public void detach() {
        super.detach();
        this.featureTiles = null;
    }
}
