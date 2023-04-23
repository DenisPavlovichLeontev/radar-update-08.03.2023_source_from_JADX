package mil.nga.geopackage.tiles.user;

import android.database.Cursor;
import java.util.List;
import mil.nga.geopackage.user.UserCursor;
import mil.nga.geopackage.user.UserDao;
import mil.nga.geopackage.user.UserInvalidCursor;

public class TileCursor extends UserCursor<TileColumn, TileTable, TileRow> {
    public TileCursor(TileTable tileTable, Cursor cursor) {
        super(tileTable, cursor);
    }

    public TileRow getRow(int[] iArr, Object[] objArr) {
        return new TileRow((TileTable) getTable(), iArr, objArr);
    }

    public void enableInvalidRequery(TileDao tileDao) {
        super.enableInvalidRequery(tileDao);
    }

    /* access modifiers changed from: protected */
    public UserInvalidCursor<TileColumn, TileTable, TileRow, ? extends UserCursor<TileColumn, TileTable, TileRow>, ? extends UserDao<TileColumn, TileTable, TileRow, ? extends UserCursor<TileColumn, TileTable, TileRow>>> createInvalidCursor(UserDao userDao, UserCursor userCursor, List<Integer> list, List<TileColumn> list2) {
        return new TileInvalidCursor((TileDao) userDao, (TileCursor) userCursor, list, list2);
    }
}
