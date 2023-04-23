package mil.nga.geopackage.p009db;

import com.j256.ormlite.support.ConnectionSource;
import java.io.Closeable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageException;

/* renamed from: mil.nga.geopackage.db.GeoPackageCoreConnection */
public abstract class GeoPackageCoreConnection implements Closeable {
    public abstract void close();

    public abstract boolean columnExists(String str, String str2);

    public abstract int count(String str, String str2, String[] strArr);

    public abstract int delete(String str, String str2, String[] strArr);

    public abstract void execSQL(String str);

    public abstract ConnectionSource getConnectionSource();

    public abstract Integer max(String str, String str2, String str3, String[] strArr);

    public abstract Integer min(String str, String str2, String str3, String[] strArr);

    public abstract Integer querySingleIntResult(String str, String[] strArr);

    public abstract String querySingleStringResult(String str, String[] strArr);

    public boolean tableExists(String str) {
        return count("sqlite_master", "tbl_name = ?", new String[]{str}) > 0;
    }

    public void addColumn(String str, String str2, String str3) {
        execSQL("ALTER TABLE " + CoreSQLUtils.quoteWrap(str) + " ADD COLUMN " + CoreSQLUtils.quoteWrap(str2) + " " + str3 + ";");
    }

    public void setApplicationId() {
        setApplicationId(GeoPackageConstants.APPLICATION_ID);
    }

    public void setApplicationId(String str) {
        execSQL(String.format("PRAGMA application_id = %d;", new Object[]{Integer.valueOf(ByteBuffer.wrap(str.getBytes()).asIntBuffer().get())}));
    }

    public String getApplicationId() {
        Integer querySingleIntResult = querySingleIntResult("PRAGMA application_id", (String[]) null);
        if (querySingleIntResult == null) {
            return null;
        }
        try {
            return new String(ByteBuffer.allocate(4).putInt(querySingleIntResult.intValue()).array(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new GeoPackageException("Unexpected application id character encoding", e);
        }
    }

    public void setUserVersion() {
        setUserVersion(GeoPackageConstants.USER_VERSION);
    }

    public void setUserVersion(int i) {
        execSQL(String.format("PRAGMA user_version = %d;", new Object[]{Integer.valueOf(i)}));
    }

    public int getUserVersion() {
        Integer querySingleIntResult = querySingleIntResult("PRAGMA user_version", (String[]) null);
        if (querySingleIntResult != null) {
            return querySingleIntResult.intValue();
        }
        return -1;
    }
}
