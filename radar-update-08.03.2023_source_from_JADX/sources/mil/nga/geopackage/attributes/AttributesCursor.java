package mil.nga.geopackage.attributes;

import android.database.Cursor;
import java.util.List;
import mil.nga.geopackage.user.UserCursor;
import mil.nga.geopackage.user.UserDao;
import mil.nga.geopackage.user.UserInvalidCursor;

public class AttributesCursor extends UserCursor<AttributesColumn, AttributesTable, AttributesRow> {
    public AttributesCursor(AttributesTable attributesTable, Cursor cursor) {
        super(attributesTable, cursor);
    }

    public AttributesRow getRow(int[] iArr, Object[] objArr) {
        return new AttributesRow((AttributesTable) getTable(), iArr, objArr);
    }

    public void enableInvalidRequery(AttributesDao attributesDao) {
        super.enableInvalidRequery(attributesDao);
    }

    /* access modifiers changed from: protected */
    public UserInvalidCursor<AttributesColumn, AttributesTable, AttributesRow, ? extends UserCursor<AttributesColumn, AttributesTable, AttributesRow>, ? extends UserDao<AttributesColumn, AttributesTable, AttributesRow, ? extends UserCursor<AttributesColumn, AttributesTable, AttributesRow>>> createInvalidCursor(UserDao userDao, UserCursor userCursor, List<Integer> list, List<AttributesColumn> list2) {
        return new AttributesInvalidCursor((AttributesDao) userDao, (AttributesCursor) userCursor, list, list2);
    }
}
