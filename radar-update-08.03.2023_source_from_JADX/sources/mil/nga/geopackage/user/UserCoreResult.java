package mil.nga.geopackage.user;

import mil.nga.geopackage.p009db.GeoPackageDataType;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserCoreRow;
import mil.nga.geopackage.user.UserTable;

public interface UserCoreResult<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserCoreRow<TColumn, TTable>> {
    void close();

    byte[] getBlob(int i);

    int getColumnIndex(String str);

    int getCount();

    double getDouble(int i);

    float getFloat(int i);

    int getInt(int i);

    long getLong(int i);

    int getPosition();

    TRow getRow();

    TRow getRow(int[] iArr, Object[] objArr);

    short getShort(int i);

    String getString(int i);

    TTable getTable();

    int getType(int i);

    Object getValue(int i, GeoPackageDataType geoPackageDataType);

    Object getValue(TColumn tcolumn);

    boolean moveToFirst();

    boolean moveToNext();

    boolean moveToPosition(int i);

    boolean wasNull();
}
