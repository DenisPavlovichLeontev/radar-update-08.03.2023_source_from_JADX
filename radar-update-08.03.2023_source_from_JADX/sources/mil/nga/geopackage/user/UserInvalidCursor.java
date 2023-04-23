package mil.nga.geopackage.user;

import android.database.Cursor;
import android.util.Log;
import com.j256.ormlite.misc.IOUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import mil.nga.geopackage.p009db.CoreSQLUtils;
import mil.nga.geopackage.p009db.GeoPackageDataType;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserCursor;
import mil.nga.geopackage.user.UserDao;
import mil.nga.geopackage.user.UserRow;
import mil.nga.geopackage.user.UserTable;

public abstract class UserInvalidCursor<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserRow<TColumn, TTable>, TCursor extends UserCursor<TColumn, TTable, TRow>, TUserDao extends UserDao<TColumn, TTable, TRow, TCursor>> implements UserCoreResult<TColumn, TTable, TRow> {
    private static final int CHUNK_SIZE = 1048576;
    private List<TColumn> blobColumns;
    private int currentPosition = -1;
    private TCursor cursor;
    private TUserDao dao;
    private List<Integer> invalidPositions;

    protected UserInvalidCursor(TUserDao tuserdao, TCursor tcursor, List<Integer> list, List<TColumn> list2) {
        this.dao = tuserdao;
        this.cursor = tcursor;
        this.invalidPositions = list;
        this.blobColumns = list2;
    }

    public boolean moveToNext() {
        int i = this.currentPosition + 1;
        this.currentPosition = i;
        if (i >= this.invalidPositions.size()) {
            return false;
        }
        return this.cursor.moveToPosition(this.invalidPositions.get(this.currentPosition).intValue());
    }

    public TRow getRow() {
        TRow row = this.cursor.getRow();
        if (row.hasId()) {
            for (TColumn readBlobValue : this.blobColumns) {
                readBlobValue(row, readBlobValue);
            }
        }
        return row;
    }

    private void readBlobValue(UserRow userRow, UserColumn userColumn) {
        Cursor rawQuery;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bArr = {0};
            int i = 1;
            while (bArr.length > 0) {
                if (i > 1) {
                    byteArrayOutputStream.write(bArr);
                }
                bArr = new byte[0];
                rawQuery = this.dao.getDatabaseConnection().getDb().rawQuery("select substr(" + CoreSQLUtils.quoteWrap(userColumn.getName()) + ", " + i + ", " + 1048576 + ") from " + CoreSQLUtils.quoteWrap(this.dao.getTableName()) + " where " + CoreSQLUtils.quoteWrap(userRow.getPkColumn().getName()) + " = " + userRow.getId(), (String[]) null);
                if (rawQuery.moveToNext()) {
                    bArr = rawQuery.getBlob(0);
                }
                rawQuery.close();
                i += 1048576;
            }
            userRow.setValue(userColumn.getIndex(), (Object) byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            try {
                Log.e("UserInvalidCursor", "Failed to read large blob value. Table: " + this.dao.getTableName() + ", Column: " + userColumn.getName() + ", Position: " + getPosition(), e);
            } catch (Throwable th) {
                IOUtils.closeQuietly(byteArrayOutputStream);
                throw th;
            }
        } catch (Throwable th2) {
            rawQuery.close();
            throw th2;
        }
        IOUtils.closeQuietly(byteArrayOutputStream);
    }

    public void close() {
        this.cursor.close();
    }

    public int getPosition() {
        return this.cursor.getPosition();
    }

    public TRow getRow(int[] iArr, Object[] objArr) {
        return (UserRow) this.cursor.getRow(iArr, objArr);
    }

    public Object getValue(TColumn tcolumn) {
        return this.cursor.getValue(tcolumn);
    }

    public Object getValue(int i, GeoPackageDataType geoPackageDataType) {
        return this.cursor.getValue(i, geoPackageDataType);
    }

    public TTable getTable() {
        return this.cursor.getTable();
    }

    public int getCount() {
        return this.invalidPositions.size();
    }

    public boolean moveToFirst() {
        this.currentPosition = -1;
        return moveToNext();
    }

    public boolean moveToPosition(int i) {
        if (i >= this.invalidPositions.size()) {
            return false;
        }
        this.currentPosition = i;
        return this.cursor.moveToPosition(this.invalidPositions.get(i).intValue());
    }

    public int getType(int i) {
        return this.cursor.getType(i);
    }

    public int getColumnIndex(String str) {
        return this.cursor.getColumnIndex(str);
    }

    public String getString(int i) {
        return this.cursor.getString(i);
    }

    public long getLong(int i) {
        return this.cursor.getLong(i);
    }

    public int getInt(int i) {
        return this.cursor.getInt(i);
    }

    public short getShort(int i) {
        return this.cursor.getShort(i);
    }

    public double getDouble(int i) {
        return this.cursor.getDouble(i);
    }

    public float getFloat(int i) {
        return this.cursor.getFloat(i);
    }

    public byte[] getBlob(int i) {
        return this.cursor.getBlob(i);
    }

    public boolean wasNull() {
        return this.cursor.wasNull();
    }
}
