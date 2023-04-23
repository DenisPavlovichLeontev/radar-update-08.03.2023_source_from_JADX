package mil.nga.geopackage.user;

import android.content.ContentValues;
import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.p009db.GeoPackageDatabase;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserCursor;
import mil.nga.geopackage.user.UserRow;
import mil.nga.geopackage.user.UserTable;

public abstract class UserDao<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserRow<TColumn, TTable>, TResult extends UserCursor<TColumn, TTable, TRow>> extends UserCoreDao<TColumn, TTable, TRow, TResult> {

    /* renamed from: db */
    private final GeoPackageDatabase f357db;
    private boolean invalidRequery = true;
    private UserConnection userDb;

    protected UserDao(String str, GeoPackageConnection geoPackageConnection, UserConnection<TColumn, TTable, TRow, TResult> userConnection, TTable ttable) {
        super(str, geoPackageConnection, userConnection, ttable);
        this.f357db = geoPackageConnection.getDb();
        this.userDb = userConnection;
    }

    public GeoPackageDatabase getDatabaseConnection() {
        return this.f357db;
    }

    public boolean isInvalidRequery() {
        return this.invalidRequery;
    }

    public void setInvalidRequery(boolean z) {
        this.invalidRequery = z;
    }

    /* access modifiers changed from: protected */
    public TResult prepareResult(TResult tresult) {
        if (this.invalidRequery) {
            tresult.enableInvalidRequery(this);
        }
        return tresult;
    }

    public TRow queryForIdRow(long j) {
        TRow trow;
        UserCursor userCursor = (UserCursor) queryForId(j);
        if (userCursor.moveToNext()) {
            trow = userCursor.getRow();
            if (!trow.isValid() && userCursor.moveToNext()) {
                trow = userCursor.getRow();
            }
        } else {
            trow = null;
        }
        userCursor.close();
        return trow;
    }

    public TResult query(TResult tresult) {
        return this.userDb.query(tresult);
    }

    public TResult query(UserQuery userQuery) {
        return this.userDb.query(userQuery);
    }

    public int update(TRow trow) {
        ContentValues contentValues = trow.toContentValues();
        if (contentValues.size() > 0) {
            return this.f357db.update(getTableName(), contentValues, getPkWhere(trow.getId()), getPkWhereArgs(trow.getId()));
        }
        return 0;
    }

    public int update(ContentValues contentValues, String str, String[] strArr) {
        return this.f357db.update(getTableName(), contentValues, str, strArr);
    }

    public long insert(TRow trow) {
        long insertOrThrow = this.f357db.insertOrThrow(getTableName(), (String) null, trow.toContentValues());
        trow.setId(insertOrThrow);
        return insertOrThrow;
    }

    public long insert(ContentValues contentValues) {
        return this.f357db.insert(getTableName(), (String) null, contentValues);
    }

    public long insertOrThrow(ContentValues contentValues) {
        return this.f357db.insertOrThrow(getTableName(), (String) null, contentValues);
    }
}
