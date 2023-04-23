package mil.nga.geopackage.user;

import android.database.Cursor;
import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserCursor;
import mil.nga.geopackage.user.UserRow;
import mil.nga.geopackage.user.UserTable;

public abstract class UserWrapperConnection<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserRow<TColumn, TTable>, TResult extends UserCursor<TColumn, TTable, TRow>> extends UserConnection<TColumn, TTable, TRow, TResult> {
    /* access modifiers changed from: protected */
    public abstract TResult wrapCursor(Cursor cursor);

    protected UserWrapperConnection(GeoPackageConnection geoPackageConnection) {
        super(geoPackageConnection);
    }

    /* access modifiers changed from: protected */
    public TResult convertCursor(Cursor cursor) {
        return wrapCursor(cursor);
    }
}
