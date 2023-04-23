package mil.nga.geopackage.user;

import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserCoreResult;
import mil.nga.geopackage.user.UserCoreRow;
import mil.nga.geopackage.user.UserTable;

public abstract class UserCoreConnection<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserCoreRow<TColumn, TTable>, TResult extends UserCoreResult<TColumn, TTable, TRow>> {
    public abstract TResult query(String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5);

    public abstract TResult query(String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5, String str6);

    public abstract TResult query(String str, String[] strArr, String[] strArr2, String str2, String[] strArr3, String str3, String str4, String str5);

    public abstract TResult query(String str, String[] strArr, String[] strArr2, String str2, String[] strArr3, String str3, String str4, String str5, String str6);

    public abstract TResult rawQuery(String str, String[] strArr);
}
