package mil.nga.geopackage.extension.link;

import java.util.ArrayList;
import java.util.List;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.features.user.FeatureDao;
import mil.nga.geopackage.tiles.user.TileDao;

public class FeatureTileTableLinker extends FeatureTileTableCoreLinker {
    private final GeoPackage geoPackage;

    public FeatureTileTableLinker(GeoPackage geoPackage2) {
        super(geoPackage2);
        this.geoPackage = geoPackage2;
    }

    public List<TileDao> getTileDaosForFeatureTable(String str) {
        ArrayList arrayList = new ArrayList();
        for (String next : getTileTablesForFeatureTable(str)) {
            if (this.geoPackage.isTileTable(next)) {
                arrayList.add(this.geoPackage.getTileDao(next));
            }
        }
        return arrayList;
    }

    public List<FeatureDao> getFeatureDaosForTileTable(String str) {
        ArrayList arrayList = new ArrayList();
        for (String next : getFeatureTablesForTileTable(str)) {
            if (this.geoPackage.isFeatureTable(next)) {
                arrayList.add(this.geoPackage.getFeatureDao(next));
            }
        }
        return arrayList;
    }
}
