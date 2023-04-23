package mil.nga.geopackage.attributes;

import java.util.List;
import mil.nga.geopackage.p009db.GeoPackageDataType;
import mil.nga.geopackage.user.UserTableReader;

public class AttributesTableReader extends UserTableReader<AttributesColumn, AttributesTable, AttributesRow, AttributesCursor> {
    public AttributesTableReader(String str) {
        super(str);
    }

    /* access modifiers changed from: protected */
    public AttributesTable createTable(String str, List<AttributesColumn> list) {
        return new AttributesTable(str, list);
    }

    /* access modifiers changed from: protected */
    public AttributesColumn createColumn(AttributesCursor attributesCursor, int i, String str, String str2, Long l, boolean z, int i2, boolean z2) {
        GeoPackageDataType fromName = GeoPackageDataType.fromName(str2);
        AttributesCursor attributesCursor2 = attributesCursor;
        return new AttributesColumn(i, str, fromName, l, z, attributesCursor.getValue(i2, fromName), z2);
    }
}
