package mil.nga.geopackage.user;

import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserCursor;
import mil.nga.geopackage.user.UserRow;
import mil.nga.geopackage.user.UserTable;

public abstract class UserTableReader<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserRow<TColumn, TTable>, TResult extends UserCursor<TColumn, TTable, TRow>> extends UserCoreTableReader<TColumn, TTable, TRow, TResult> {
    protected UserTableReader(String str) {
        super(str);
    }
}
