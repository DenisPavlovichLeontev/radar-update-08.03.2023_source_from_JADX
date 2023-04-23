package mil.nga.geopackage.user;

import android.database.Cursor;
import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.p009db.GeoPackageDatabase;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserCursor;
import mil.nga.geopackage.user.UserRow;
import mil.nga.geopackage.user.UserTable;

public abstract class UserConnection<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserRow<TColumn, TTable>, TResult extends UserCursor<TColumn, TTable, TRow>> extends UserCoreConnection<TColumn, TTable, TRow, TResult> {
    protected final GeoPackageDatabase database;

    protected UserConnection(GeoPackageConnection geoPackageConnection) {
        this.database = geoPackageConnection.getDb();
    }

    public TResult rawQuery(String str, String[] strArr) {
        return query(new UserQuery(str, strArr));
    }

    public TResult query(String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5) {
        return query(new UserQuery(str, strArr, str2, strArr2, str3, str4, str5));
    }

    public TResult query(String str, String[] strArr, String[] strArr2, String str2, String[] strArr3, String str3, String str4, String str5) {
        return query(new UserQuery(str, strArr, strArr2, str2, strArr3, str3, str4, str5));
    }

    public TResult query(String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5, String str6) {
        return query(new UserQuery(str, strArr, str2, strArr2, str3, str4, str5, str6));
    }

    public TResult query(String str, String[] strArr, String[] strArr2, String str2, String[] strArr3, String str3, String str4, String str5, String str6) {
        return query(new UserQuery(str, strArr, strArr2, str2, strArr3, str3, str4, str5, str6));
    }

    public TResult query(TResult tresult) {
        return query(tresult.getQuery());
    }

    public TResult query(UserQuery userQuery) {
        Cursor cursor;
        String[] selectionArgs = userQuery.getSelectionArgs();
        String sql = userQuery.getSql();
        if (sql != null) {
            cursor = this.database.rawQuery(sql, selectionArgs);
        } else {
            String table = userQuery.getTable();
            String[] columns = userQuery.getColumns();
            String selection = userQuery.getSelection();
            String groupBy = userQuery.getGroupBy();
            String having = userQuery.getHaving();
            String orderBy = userQuery.getOrderBy();
            String[] columnsAs = userQuery.getColumnsAs();
            String limit = userQuery.getLimit();
            if (columnsAs != null && limit != null) {
                cursor = this.database.query(table, columns, columnsAs, selection, selectionArgs, groupBy, having, orderBy, limit);
            } else if (columnsAs != null) {
                cursor = this.database.query(table, columns, columnsAs, selection, selectionArgs, groupBy, having, orderBy);
            } else if (limit != null) {
                cursor = this.database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            } else {
                cursor = this.database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
            }
        }
        return handleCursor(cursor, userQuery);
    }

    private TResult handleCursor(Cursor cursor, UserQuery userQuery) {
        TResult convertCursor = convertCursor(cursor);
        convertCursor.setQuery(userQuery);
        return convertCursor;
    }

    /* access modifiers changed from: protected */
    public TResult convertCursor(Cursor cursor) {
        return (UserCursor) cursor;
    }
}
