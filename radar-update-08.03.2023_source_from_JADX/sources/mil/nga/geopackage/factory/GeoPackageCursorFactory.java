package mil.nga.geopackage.factory;

import android.database.sqlite.SQLiteDatabase;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p009db.CoreSQLUtils;

class GeoPackageCursorFactory implements SQLiteDatabase.CursorFactory {
    private final Map<String, GeoPackageCursorWrapper> tableCursors = Collections.synchronizedMap(new HashMap());

    GeoPackageCursorFactory() {
    }

    public void registerTable(String str, GeoPackageCursorWrapper geoPackageCursorWrapper) {
        GeoPackageCursorWrapper geoPackageCursorWrapper2 = this.tableCursors.get(str);
        if (geoPackageCursorWrapper2 == null) {
            this.tableCursors.put(str, geoPackageCursorWrapper);
            String quoteWrap = CoreSQLUtils.quoteWrap(str);
            this.tableCursors.put(quoteWrap, geoPackageCursorWrapper);
            int indexOf = str.indexOf(32);
            if (indexOf > 0) {
                this.tableCursors.put(str.substring(0, indexOf), geoPackageCursorWrapper);
                this.tableCursors.put(quoteWrap.substring(0, quoteWrap.indexOf(32)), geoPackageCursorWrapper);
            }
        } else if (!geoPackageCursorWrapper2.getClass().equals(geoPackageCursorWrapper.getClass())) {
            throw new GeoPackageException("Table '" + str + "' was already registered for cursor wrapper '" + geoPackageCursorWrapper2.getClass().getSimpleName() + "' and can not be registered for '" + geoPackageCursorWrapper.getClass().getSimpleName() + "'");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0007, code lost:
        r2 = r0.tableCursors.get(r3);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.database.Cursor newCursor(android.database.sqlite.SQLiteDatabase r1, android.database.sqlite.SQLiteCursorDriver r2, java.lang.String r3, android.database.sqlite.SQLiteQuery r4) {
        /*
            r0 = this;
            android.database.sqlite.SQLiteCursor r1 = new android.database.sqlite.SQLiteCursor
            r1.<init>(r2, r3, r4)
            if (r3 == 0) goto L_0x0015
            java.util.Map<java.lang.String, mil.nga.geopackage.factory.GeoPackageCursorWrapper> r2 = r0.tableCursors
            java.lang.Object r2 = r2.get(r3)
            mil.nga.geopackage.factory.GeoPackageCursorWrapper r2 = (mil.nga.geopackage.factory.GeoPackageCursorWrapper) r2
            if (r2 == 0) goto L_0x0015
            android.database.Cursor r1 = r2.wrapCursor(r1)
        L_0x0015:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.factory.GeoPackageCursorFactory.newCursor(android.database.sqlite.SQLiteDatabase, android.database.sqlite.SQLiteCursorDriver, java.lang.String, android.database.sqlite.SQLiteQuery):android.database.Cursor");
    }
}
