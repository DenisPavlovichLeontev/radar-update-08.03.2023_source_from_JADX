package mil.nga.geopackage.features.user;

import java.util.List;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.p009db.GeoPackageDataType;
import mil.nga.geopackage.user.UserTableReader;
import mil.nga.wkb.geom.GeometryType;

public class FeatureTableReader extends UserTableReader<FeatureColumn, FeatureTable, FeatureRow, FeatureCursor> {
    private GeometryColumns geometryColumns;

    public FeatureTableReader(GeometryColumns geometryColumns2) {
        super(geometryColumns2.getTableName());
        this.geometryColumns = geometryColumns2;
    }

    /* access modifiers changed from: protected */
    public FeatureTable createTable(String str, List<FeatureColumn> list) {
        return new FeatureTable(str, list);
    }

    /* access modifiers changed from: protected */
    public FeatureColumn createColumn(FeatureCursor featureCursor, int i, String str, String str2, Long l, boolean z, int i2, boolean z2) {
        GeometryType geometryType;
        GeoPackageDataType geoPackageDataType;
        String str3 = str;
        if (str.equals(this.geometryColumns.getColumnName())) {
            geometryType = GeometryType.fromName(str2);
            geoPackageDataType = GeoPackageDataType.BLOB;
        } else {
            geoPackageDataType = GeoPackageDataType.fromName(str2);
            geometryType = null;
        }
        FeatureCursor featureCursor2 = featureCursor;
        return new FeatureColumn(i, str, geoPackageDataType, l, z, featureCursor.getValue(i2, geoPackageDataType), z2, geometryType);
    }
}
