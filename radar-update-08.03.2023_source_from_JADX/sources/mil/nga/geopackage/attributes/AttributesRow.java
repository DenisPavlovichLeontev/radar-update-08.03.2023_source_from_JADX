package mil.nga.geopackage.attributes;

import mil.nga.geopackage.user.UserRow;

public class AttributesRow extends UserRow<AttributesColumn, AttributesTable> {
    AttributesRow(AttributesTable attributesTable, int[] iArr, Object[] objArr) {
        super(attributesTable, iArr, objArr);
    }

    AttributesRow(AttributesTable attributesTable) {
        super(attributesTable);
    }

    public AttributesRow(AttributesRow attributesRow) {
        super(attributesRow);
    }
}
