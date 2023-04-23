package mil.nga.geopackage.features.user;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p009db.GeoPackageDataType;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.wkb.geom.GeometryType;

public class FeatureColumn extends UserColumn {
    private final GeometryType geometryType;

    public static FeatureColumn createPrimaryKeyColumn(int i, String str) {
        return new FeatureColumn(i, str, GeoPackageDataType.INTEGER, (Long) null, true, (Object) null, true, (GeometryType) null);
    }

    public static FeatureColumn createGeometryColumn(int i, String str, GeometryType geometryType2, boolean z, Object obj) {
        if (geometryType2 != null) {
            return new FeatureColumn(i, str, GeoPackageDataType.BLOB, (Long) null, z, obj, false, geometryType2);
        }
        throw new GeoPackageException("Geometry Type is required to create geometry column: " + str);
    }

    public static FeatureColumn createColumn(int i, String str, GeoPackageDataType geoPackageDataType, boolean z, Object obj) {
        return createColumn(i, str, geoPackageDataType, (Long) null, z, obj);
    }

    public static FeatureColumn createColumn(int i, String str, GeoPackageDataType geoPackageDataType, Long l, boolean z, Object obj) {
        return new FeatureColumn(i, str, geoPackageDataType, l, z, obj, false, (GeometryType) null);
    }

    FeatureColumn(int i, String str, GeoPackageDataType geoPackageDataType, Long l, boolean z, Object obj, boolean z2, GeometryType geometryType2) {
        super(i, str, geoPackageDataType, l, z, obj, z2);
        this.geometryType = geometryType2;
        if (geoPackageDataType == null) {
            throw new GeoPackageException("Data Type is required to create column: " + str);
        }
    }

    public boolean isGeometry() {
        return this.geometryType != null;
    }

    public GeometryType getGeometryType() {
        return this.geometryType;
    }

    public String getTypeName() {
        if (isGeometry()) {
            return this.geometryType.name();
        }
        return super.getTypeName();
    }
}
