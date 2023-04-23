package mil.nga.geopackage.extension.link;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(daoClass = FeatureTileLinkDao.class, tableName = "nga_feature_tile_link")
public class FeatureTileLink {
    public static final String COLUMN_FEATURE_TABLE_NAME = "feature_table_name";
    public static final String COLUMN_TILE_TABLE_NAME = "tile_table_name";
    public static final String TABLE_NAME = "nga_feature_tile_link";
    @DatabaseField(canBeNull = false, columnName = "feature_table_name", uniqueCombo = true)
    private String featureTableName;
    @DatabaseField(canBeNull = false, columnName = "tile_table_name", uniqueCombo = true)
    private String tileTableName;

    public FeatureTileLink() {
    }

    public FeatureTileLink(FeatureTileLink featureTileLink) {
        this.featureTableName = featureTileLink.featureTableName;
        this.tileTableName = featureTileLink.tileTableName;
    }

    public FeatureTileLinkKey getId() {
        return new FeatureTileLinkKey(this.featureTableName, this.tileTableName);
    }

    public void setId(FeatureTileLinkKey featureTileLinkKey) {
        this.featureTableName = featureTileLinkKey.getFeatureTableName();
        this.tileTableName = featureTileLinkKey.getTileTableName();
    }

    public String getFeatureTableName() {
        return this.featureTableName;
    }

    public void setFeatureTableName(String str) {
        this.featureTableName = str;
    }

    public String getTileTableName() {
        return this.tileTableName;
    }

    public void setTileTableName(String str) {
        this.tileTableName = str;
    }
}
