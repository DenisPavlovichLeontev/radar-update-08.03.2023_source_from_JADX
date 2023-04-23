package mil.nga.geopackage.attributes;

import mil.nga.geopackage.p009db.GeoPackageDataType;
import mil.nga.geopackage.user.UserColumn;

public class AttributesColumn extends UserColumn {
    public static AttributesColumn createPrimaryKeyColumn(int i, String str) {
        return new AttributesColumn(i, str, GeoPackageDataType.INTEGER, (Long) null, true, (Object) null, true);
    }

    public static AttributesColumn createColumn(int i, String str, GeoPackageDataType geoPackageDataType, boolean z, Object obj) {
        return createColumn(i, str, geoPackageDataType, (Long) null, z, obj);
    }

    public static AttributesColumn createColumn(int i, String str, GeoPackageDataType geoPackageDataType, Long l, boolean z, Object obj) {
        return new AttributesColumn(i, str, geoPackageDataType, l, z, obj, false);
    }

    AttributesColumn(int i, String str, GeoPackageDataType geoPackageDataType, Long l, boolean z, Object obj, boolean z2) {
        super(i, str, geoPackageDataType, l, z, obj, z2);
    }
}
