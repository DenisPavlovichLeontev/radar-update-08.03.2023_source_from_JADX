package org.osmdroid.gpkg.tiles.raster;

import android.util.Log;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBox;

public class GeopackageRasterTileSource extends XYTileSource {
    private BoundingBox bounds;
    private String database;
    private String tableDao;

    public GeopackageRasterTileSource(String str, String str2, int i, int i2, BoundingBox boundingBox) {
        super(str + ":" + str2, i, i2, 256, "png", new String[]{""});
        Log.i(IMapView.LOGTAG, "Geopackage support is BETA. Please report any issues");
        this.database = str;
        this.tableDao = str2;
        this.bounds = boundingBox;
    }

    public BoundingBox getBounds() {
        return this.bounds;
    }

    public void setBounds(BoundingBox boundingBox) {
        this.bounds = boundingBox;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String str) {
        this.database = str;
    }

    public String getTableDao() {
        return this.tableDao;
    }

    public void setTableDao(String str) {
        this.tableDao = str;
    }
}
