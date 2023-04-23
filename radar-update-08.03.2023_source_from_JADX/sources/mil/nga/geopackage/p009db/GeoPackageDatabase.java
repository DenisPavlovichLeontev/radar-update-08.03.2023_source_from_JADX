package mil.nga.geopackage.p009db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/* renamed from: mil.nga.geopackage.db.GeoPackageDatabase */
public class GeoPackageDatabase {

    /* renamed from: db */
    private final SQLiteDatabase f337db;

    public GeoPackageDatabase(SQLiteDatabase sQLiteDatabase) {
        this.f337db = sQLiteDatabase;
    }

    public SQLiteDatabase getDb() {
        return this.f337db;
    }

    public void execSQL(String str) throws SQLException {
        this.f337db.execSQL(str);
    }

    public int delete(String str, String str2, String[] strArr) {
        return this.f337db.delete(CoreSQLUtils.quoteWrap(str), str2, strArr);
    }

    public Cursor rawQuery(String str, String[] strArr) {
        return this.f337db.rawQuery(str, strArr);
    }

    public void close() {
        this.f337db.close();
    }

    public Cursor query(String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5) {
        return this.f337db.query(CoreSQLUtils.quoteWrap(str), CoreSQLUtils.quoteWrap(strArr), str2, strArr2, str3, str4, str5);
    }

    public Cursor query(String str, String[] strArr, String[] strArr2, String str2, String[] strArr3, String str3, String str4, String str5) {
        String[] strArr4 = strArr2;
        return this.f337db.query(CoreSQLUtils.quoteWrap(str), CoreSQLUtils.buildColumnsAs(CoreSQLUtils.quoteWrap(strArr), strArr2), str2, strArr3, str3, str4, str5);
    }

    public Cursor query(String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5, String str6) {
        return this.f337db.query(CoreSQLUtils.quoteWrap(str), CoreSQLUtils.quoteWrap(strArr), str2, strArr2, str3, str4, str5, str6);
    }

    public Cursor query(String str, String[] strArr, String[] strArr2, String str2, String[] strArr3, String str3, String str4, String str5, String str6) {
        String[] strArr4 = strArr2;
        return this.f337db.query(CoreSQLUtils.quoteWrap(str), CoreSQLUtils.buildColumnsAs(CoreSQLUtils.quoteWrap(strArr), strArr2), str2, strArr3, str3, str4, str5, str6);
    }

    public int update(String str, ContentValues contentValues, String str2, String[] strArr) {
        return this.f337db.update(CoreSQLUtils.quoteWrap(str), SQLUtils.quoteWrap(contentValues), str2, strArr);
    }

    public long insertOrThrow(String str, String str2, ContentValues contentValues) throws SQLException {
        return this.f337db.insertOrThrow(CoreSQLUtils.quoteWrap(str), str2, SQLUtils.quoteWrap(contentValues));
    }

    public long insert(String str, String str2, ContentValues contentValues) {
        return this.f337db.insert(CoreSQLUtils.quoteWrap(str), str2, SQLUtils.quoteWrap(contentValues));
    }
}
