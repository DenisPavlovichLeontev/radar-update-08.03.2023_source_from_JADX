package mil.nga.geopackage.attributes;

import java.util.List;
import mil.nga.geopackage.user.UserInvalidCursor;

public class AttributesInvalidCursor extends UserInvalidCursor<AttributesColumn, AttributesTable, AttributesRow, AttributesCursor, AttributesDao> {
    public AttributesInvalidCursor(AttributesDao attributesDao, AttributesCursor attributesCursor, List<Integer> list, List<AttributesColumn> list2) {
        super(attributesDao, attributesCursor, list, list2);
    }
}
