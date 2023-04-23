package mil.nga.geopackage.p009db;

import android.database.Cursor;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

/* renamed from: mil.nga.geopackage.db.GeoPackageConnection */
public class GeoPackageConnection extends GeoPackageCoreConnection {
    private static final String NAME_COLUMN = "name";
    private final ConnectionSource connectionSource;

    /* renamed from: db */
    private final GeoPackageDatabase f336db;

    public GeoPackageConnection(GeoPackageDatabase geoPackageDatabase) {
        this.f336db = geoPackageDatabase;
        this.connectionSource = new AndroidConnectionSource(geoPackageDatabase.getDb());
    }

    public GeoPackageDatabase getDb() {
        return this.f336db;
    }

    public ConnectionSource getConnectionSource() {
        return this.connectionSource;
    }

    public void execSQL(String str) {
        this.f336db.execSQL(str);
    }

    public int delete(String str, String str2, String[] strArr) {
        return this.f336db.delete(str, str2, strArr);
    }

    public int count(String str, String str2, String[] strArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ");
        sb.append(CoreSQLUtils.quoteWrap(str));
        if (str2 != null) {
            sb.append(" where ");
            sb.append(str2);
        }
        return singleResultQuery(sb.toString(), strArr);
    }

    public Integer min(String str, String str2, String str3, String[] strArr) {
        if (count(str, str3, strArr) <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select min(");
        sb.append(CoreSQLUtils.quoteWrap(str2));
        sb.append(") from ");
        sb.append(CoreSQLUtils.quoteWrap(str));
        if (str3 != null) {
            sb.append(" where ");
            sb.append(str3);
        }
        return Integer.valueOf(singleResultQuery(sb.toString(), strArr));
    }

    public Integer max(String str, String str2, String str3, String[] strArr) {
        if (count(str, str3, strArr) <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select max(");
        sb.append(CoreSQLUtils.quoteWrap(str2));
        sb.append(") from ");
        sb.append(CoreSQLUtils.quoteWrap(str));
        if (str3 != null) {
            sb.append(" where ");
            sb.append(str3);
        }
        return Integer.valueOf(singleResultQuery(sb.toString(), strArr));
    }

    private int singleResultQuery(String str, String[] strArr) {
        Cursor rawQuery = this.f336db.rawQuery(str, strArr);
        try {
            int i = 0;
            if (rawQuery.moveToFirst()) {
                i = rawQuery.getInt(0);
            }
            return i;
        } finally {
            rawQuery.close();
        }
    }

    public void close() {
        this.connectionSource.closeQuietly();
        this.f336db.close();
    }

    public boolean columnExists(String str, String str2) {
        boolean z;
        Cursor rawQuery = rawQuery("PRAGMA table_info(" + CoreSQLUtils.quoteWrap(str) + ")", (String[]) null);
        try {
            int columnIndex = rawQuery.getColumnIndex("name");
            while (true) {
                if (rawQuery.moveToNext()) {
                    if (str2.equals(rawQuery.getString(columnIndex))) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            return z;
        } finally {
            rawQuery.close();
        }
    }

    public String querySingleStringResult(String str, String[] strArr) {
        Cursor rawQuery = this.f336db.rawQuery(str, strArr);
        try {
            return rawQuery.moveToFirst() ? rawQuery.getString(0) : null;
        } finally {
            rawQuery.close();
        }
    }

    public Integer querySingleIntResult(String str, String[] strArr) {
        Cursor rawQuery = this.f336db.rawQuery(str, strArr);
        try {
            return rawQuery.moveToFirst() ? Integer.valueOf(rawQuery.getInt(0)) : null;
        } finally {
            rawQuery.close();
        }
    }

    public Cursor rawQuery(String str, String[] strArr) {
        return this.f336db.rawQuery(str, strArr);
    }
}
