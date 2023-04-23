package mil.nga.geopackage.user;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p009db.CoreSQLUtils;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserCoreResult;
import mil.nga.geopackage.user.UserCoreRow;
import mil.nga.geopackage.user.UserTable;

public abstract class UserCoreTableReader<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserCoreRow<TColumn, TTable>, TResult extends UserCoreResult<TColumn, TTable, TRow>> {
    private static final String CID = "cid";
    private static final String DFLT_VALUE = "dflt_value";
    private static final String NAME = "name";
    private static final String NOT_NULL = "notnull";

    /* renamed from: PK */
    private static final String f356PK = "pk";
    private static final String TYPE = "type";
    private static final Logger logger = Logger.getLogger(UserCoreTableReader.class.getName());
    private final String tableName;

    /* access modifiers changed from: protected */
    public abstract TColumn createColumn(TResult tresult, int i, String str, String str2, Long l, boolean z, int i2, boolean z2);

    /* access modifiers changed from: protected */
    public abstract TTable createTable(String str, List<TColumn> list);

    protected UserCoreTableReader(String str) {
        this.tableName = str;
    }

    /* JADX INFO: finally extract failed */
    public TTable readTable(UserCoreConnection<TColumn, TTable, TRow, TResult> userCoreConnection) {
        Long l;
        String str;
        int indexOf;
        Long l2;
        ArrayList arrayList = new ArrayList();
        TResult rawQuery = userCoreConnection.rawQuery("PRAGMA table_info(" + CoreSQLUtils.quoteWrap(this.tableName) + ")", (String[]) null);
        while (rawQuery.moveToNext()) {
            try {
                int i = rawQuery.getInt(rawQuery.getColumnIndex(CID));
                String string = rawQuery.getString(rawQuery.getColumnIndex("name"));
                String string2 = rawQuery.getString(rawQuery.getColumnIndex("type"));
                boolean z = rawQuery.getInt(rawQuery.getColumnIndex(NOT_NULL)) == 1;
                boolean z2 = rawQuery.getInt(rawQuery.getColumnIndex(f356PK)) == 1;
                if (string2 != null && string2.endsWith(")") && (indexOf = string2.indexOf("(")) > -1) {
                    String substring = string2.substring(indexOf + 1, string2.length() - 1);
                    if (!substring.isEmpty()) {
                        try {
                            l2 = Long.valueOf(substring);
                            try {
                                string2 = string2.substring(0, indexOf);
                            } catch (NumberFormatException e) {
                                e = e;
                                logger.log(Level.WARNING, "Failed to parse type max from type: " + string2, e);
                                l = l2;
                                str = string2;
                                arrayList.add(createColumn(rawQuery, i, string, str, l, z, rawQuery.getColumnIndex(DFLT_VALUE), z2));
                            }
                        } catch (NumberFormatException e2) {
                            e = e2;
                            l2 = null;
                            logger.log(Level.WARNING, "Failed to parse type max from type: " + string2, e);
                            l = l2;
                            str = string2;
                            arrayList.add(createColumn(rawQuery, i, string, str, l, z, rawQuery.getColumnIndex(DFLT_VALUE), z2));
                        }
                        l = l2;
                        str = string2;
                        arrayList.add(createColumn(rawQuery, i, string, str, l, z, rawQuery.getColumnIndex(DFLT_VALUE), z2));
                    }
                }
                str = string2;
                l = null;
                arrayList.add(createColumn(rawQuery, i, string, str, l, z, rawQuery.getColumnIndex(DFLT_VALUE), z2));
            } catch (Throwable th) {
                rawQuery.close();
                throw th;
            }
        }
        rawQuery.close();
        if (!arrayList.isEmpty()) {
            return createTable(this.tableName, arrayList);
        }
        throw new GeoPackageException("Table does not exist: " + this.tableName);
    }
}
