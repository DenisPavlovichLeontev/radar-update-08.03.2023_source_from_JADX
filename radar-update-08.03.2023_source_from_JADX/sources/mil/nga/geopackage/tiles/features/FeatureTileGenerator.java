package mil.nga.geopackage.tiles.features;

import android.content.Context;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.extension.link.FeatureTileTableLinker;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.tiles.TileGenerator;

public class FeatureTileGenerator extends TileGenerator {
    private final FeatureTiles featureTiles;
    private boolean linkTables = true;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FeatureTileGenerator(Context context, GeoPackage geoPackage, String str, FeatureTiles featureTiles2, int i, int i2, BoundingBox boundingBox, Projection projection) {
        super(context, geoPackage, str, i, i2, boundingBox, projection);
        this.featureTiles = featureTiles2;
    }

    public boolean isLinkTables() {
        return this.linkTables;
    }

    public void setLinkTables(boolean z) {
        this.linkTables = z;
    }

    /* access modifiers changed from: protected */
    public void preTileGeneration() {
        GeoPackage geoPackage = getGeoPackage();
        String tableName = this.featureTiles.getFeatureDao().getTableName();
        String tableName2 = getTableName();
        if (this.linkTables && geoPackage.isFeatureTable(tableName) && geoPackage.isTileTable(tableName2)) {
            new FeatureTileTableLinker(geoPackage).link(tableName, tableName2);
        }
    }

    /* access modifiers changed from: protected */
    public byte[] createTile(int i, long j, long j2) {
        return this.featureTiles.drawTileBytes((int) j, (int) j2, i);
    }
}
