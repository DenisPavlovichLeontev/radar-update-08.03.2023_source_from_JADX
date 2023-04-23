package mil.nga.geopackage.user;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p009db.CoreSQLUtils;
import mil.nga.geopackage.p009db.GeoPackageCoreConnection;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserCoreResult;
import mil.nga.geopackage.user.UserCoreRow;
import mil.nga.geopackage.user.UserTable;
import org.osgeo.proj4j.units.DegreeUnit;

public abstract class UserCoreDao<TColumn extends UserColumn, TTable extends UserTable<TColumn>, TRow extends UserCoreRow<TColumn, TTable>, TResult extends UserCoreResult<TColumn, TTable, TRow>> {
    private final String database;

    /* renamed from: db */
    private final GeoPackageCoreConnection f355db;
    protected Projection projection;
    private final TTable table;
    private final UserCoreConnection<TColumn, TTable, TRow, TResult> userDb;

    public abstract BoundingBox getBoundingBox();

    public abstract long insert(TRow trow);

    public abstract TRow newRow();

    /* access modifiers changed from: protected */
    public abstract TResult prepareResult(TResult tresult);

    public abstract int update(TRow trow);

    protected UserCoreDao(String str, GeoPackageCoreConnection geoPackageCoreConnection, UserCoreConnection<TColumn, TTable, TRow, TResult> userCoreConnection, TTable ttable) {
        this.database = str;
        this.f355db = geoPackageCoreConnection;
        this.userDb = userCoreConnection;
        this.table = ttable;
    }

    public String getDatabase() {
        return this.database;
    }

    public GeoPackageCoreConnection getDb() {
        return this.f355db;
    }

    public UserCoreConnection<TColumn, TTable, TRow, TResult> getUserDb() {
        return this.userDb;
    }

    public String getTableName() {
        return this.table.getTableName();
    }

    public TTable getTable() {
        return this.table;
    }

    public Projection getProjection() {
        return this.projection;
    }

    public void dropTable() {
        GeoPackageCoreConnection geoPackageCoreConnection = this.f355db;
        geoPackageCoreConnection.execSQL("DROP TABLE IF EXISTS " + CoreSQLUtils.quoteWrap(getTableName()));
    }

    public TResult queryForAll() {
        TResult query = this.userDb.query(getTableName(), this.table.getColumnNames(), (String) null, (String[]) null, (String) null, (String) null, (String) null);
        prepareResult(query);
        return query;
    }

    public TResult queryForAll(String[] strArr) {
        TResult query = this.userDb.query(getTableName(), this.table.getColumnNames(), strArr, (String) null, (String[]) null, (String) null, (String) null, (String) null);
        prepareResult(query);
        return query;
    }

    public TResult queryForEq(String str, Object obj) {
        return queryForEq(str, obj, (String) null, (String) null, (String) null);
    }

    public TResult queryForEq(String str, Object obj, String str2, String str3, String str4) {
        TResult query = this.userDb.query(getTableName(), this.table.getColumnNames(), buildWhere(str, obj), buildWhereArgs(obj), str2, str3, str4);
        prepareResult(query);
        return query;
    }

    public TResult queryForEq(String str, ColumnValue columnValue) {
        TResult query = this.userDb.query(getTableName(), this.table.getColumnNames(), buildWhere(str, columnValue), buildWhereArgs(columnValue), (String) null, (String) null, (String) null);
        prepareResult(query);
        return query;
    }

    public TResult queryForFieldValues(Map<String, Object> map) {
        TResult query = this.userDb.query(getTableName(), this.table.getColumnNames(), buildWhere(map.entrySet()), buildWhereArgs(map.values()), (String) null, (String) null, (String) null);
        prepareResult(query);
        return query;
    }

    public TResult queryForValueFieldValues(Map<String, ColumnValue> map) {
        TResult query = this.userDb.query(getTableName(), this.table.getColumnNames(), buildValueWhere(map.entrySet()), buildValueWhereArgs(map.values()), (String) null, (String) null, (String) null);
        prepareResult(query);
        return query;
    }

    public TResult queryForId(long j) {
        TResult query = this.userDb.query(getTableName(), this.table.getColumnNames(), getPkWhere(j), getPkWhereArgs(j), (String) null, (String) null, (String) null);
        prepareResult(query);
        return query;
    }

    public TRow queryForIdRow(long j) {
        UserCoreResult queryForId = queryForId(j);
        TRow row = queryForId.moveToNext() ? queryForId.getRow() : null;
        queryForId.close();
        return row;
    }

    public TResult query(String str, String[] strArr) {
        TResult query = this.userDb.query(getTableName(), this.table.getColumnNames(), str, strArr, (String) null, (String) null, (String) null);
        prepareResult(query);
        return query;
    }

    public TResult query(String str, String[] strArr, String str2, String str3, String str4) {
        TResult query = this.userDb.query(getTableName(), this.table.getColumnNames(), str, strArr, str2, str3, str4);
        prepareResult(query);
        return query;
    }

    public TResult query(String str, String[] strArr, String str2, String str3, String str4, String str5) {
        TResult query = this.userDb.query(getTableName(), this.table.getColumnNames(), str, strArr, str2, str3, str4, str5);
        prepareResult(query);
        return query;
    }

    public int delete(TRow trow) {
        return deleteById(trow.getId());
    }

    public int deleteById(long j) {
        return this.f355db.delete(getTableName(), getPkWhere(j), getPkWhereArgs(j));
    }

    public int delete(String str, String[] strArr) {
        return this.f355db.delete(getTableName(), str, strArr);
    }

    public long create(TRow trow) {
        return insert(trow);
    }

    /* access modifiers changed from: protected */
    public String getPkWhere(long j) {
        return buildWhere(this.table.getPkColumn().getName(), (Object) Long.valueOf(j));
    }

    /* access modifiers changed from: protected */
    public String[] getPkWhereArgs(long j) {
        return buildWhereArgs((Object) Long.valueOf(j));
    }

    public String buildWhere(Set<Map.Entry<String, Object>> set) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry next : set) {
            if (sb.length() > 0) {
                sb.append(" AND ");
            }
            sb.append(buildWhere((String) next.getKey(), next.getValue()));
        }
        return sb.toString();
    }

    public String buildValueWhere(Set<Map.Entry<String, ColumnValue>> set) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry next : set) {
            if (sb.length() > 0) {
                sb.append(" AND ");
            }
            sb.append(buildWhere((String) next.getKey(), (ColumnValue) next.getValue()));
        }
        return sb.toString();
    }

    public String buildWhere(String str, Object obj) {
        return buildWhere(str, obj, SimpleComparison.EQUAL_TO_OPERATION);
    }

    public String buildWhere(String str, Object obj, String str2) {
        String str3;
        StringBuilder sb = new StringBuilder();
        sb.append(CoreSQLUtils.quoteWrap(str));
        sb.append(" ");
        if (obj != null) {
            str3 = str2 + " ?";
        } else {
            str3 = "IS NULL";
        }
        sb.append(str3);
        return sb.toString();
    }

    public String buildWhere(String str, ColumnValue columnValue) {
        if (columnValue == null) {
            return buildWhere(str, (Object) null, SimpleComparison.EQUAL_TO_OPERATION);
        }
        if (columnValue.getValue() == null || columnValue.getTolerance() == null) {
            return buildWhere(str, columnValue.getValue());
        }
        if (columnValue.getValue() instanceof Number) {
            String quoteWrap = CoreSQLUtils.quoteWrap(str);
            return quoteWrap + " >= ? AND " + quoteWrap + " <= ?";
        }
        throw new GeoPackageException("Field value is not a number and can not use a tolerance, Field: " + str + ", Value: " + columnValue);
    }

    public String[] buildWhereArgs(Collection<Object> collection) {
        ArrayList arrayList = new ArrayList();
        for (Object next : collection) {
            if (next != null) {
                arrayList.add(next.toString());
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    public String[] buildWhereArgs(Object[] objArr) {
        ArrayList arrayList = new ArrayList();
        for (Object obj : objArr) {
            if (obj != null) {
                arrayList.add(obj.toString());
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    public String[] buildValueWhereArgs(Collection<ColumnValue> collection) {
        ArrayList arrayList = new ArrayList();
        for (ColumnValue next : collection) {
            if (!(next == null || next.getValue() == null)) {
                if (next.getTolerance() != null) {
                    String[] valueToleranceRange = getValueToleranceRange(next);
                    arrayList.add(valueToleranceRange[0]);
                    arrayList.add(valueToleranceRange[1]);
                } else {
                    arrayList.add(next.getValue().toString());
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    public String[] buildWhereArgs(Object obj) {
        if (obj == null) {
            return null;
        }
        return new String[]{obj.toString()};
    }

    public String[] buildWhereArgs(ColumnValue columnValue) {
        if (columnValue == null) {
            return null;
        }
        if (columnValue.getValue() == null || columnValue.getTolerance() == null) {
            return buildWhereArgs(columnValue.getValue());
        }
        return getValueToleranceRange(columnValue);
    }

    public int count() {
        return count((String) null, (String[]) null);
    }

    public int count(String str, String[] strArr) {
        return this.f355db.count(getTableName(), str, strArr);
    }

    public Integer min(String str, String str2, String[] strArr) {
        return this.f355db.min(getTableName(), str, str2, strArr);
    }

    public Integer max(String str, String str2, String[] strArr) {
        return this.f355db.max(getTableName(), str, str2, strArr);
    }

    public int getZoomLevel() {
        Projection projection2 = getProjection();
        if (projection2 != null) {
            BoundingBox boundingBox = getBoundingBox();
            if (boundingBox == null) {
                return 0;
            }
            if (projection2.getUnit() instanceof DegreeUnit) {
                boundingBox = TileBoundingBoxUtils.boundDegreesBoundingBoxWithWebMercatorLimits(boundingBox);
            }
            return TileBoundingBoxUtils.getZoomLevel(projection2.getTransformation(3857).transform(boundingBox));
        }
        throw new GeoPackageException("No projection was set which is required to determine the zoom level");
    }

    public String[] buildColumnsAsNull(List<TColumn> list) {
        return buildColumnsAs(list, "null");
    }

    public String[] buildColumnsAs(List<TColumn> list, String str) {
        return buildColumnsAs(buildColumnsArray(list), str);
    }

    public String[] buildColumnsAsNull(String[] strArr) {
        return buildColumnsAs(strArr, "null");
    }

    public String[] buildColumnsAs(String[] strArr, String str) {
        String[] strArr2 = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr2[i] = str;
        }
        return buildColumnsAs(strArr, strArr2);
    }

    public String[] buildColumnsAs(List<TColumn> list, String[] strArr) {
        return buildColumnsAs(buildColumnsArray(list), strArr);
    }

    public String[] buildColumnsAs(String[] strArr, String[] strArr2) {
        HashMap hashMap = new HashMap();
        for (int i = 0; i < strArr.length; i++) {
            hashMap.put(strArr[i], strArr2[i]);
        }
        return buildColumnsAs(hashMap);
    }

    public String[] buildColumnsAs(Map<String, String> map) {
        String[] columnNames = this.table.getColumnNames();
        String[] strArr = new String[columnNames.length];
        for (int i = 0; i < columnNames.length; i++) {
            strArr[i] = map.get(columnNames[i]);
        }
        return strArr;
    }

    private String[] buildColumnsArray(List<TColumn> list) {
        String[] strArr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strArr[i] = ((UserColumn) list.get(i)).getName();
        }
        return strArr;
    }

    private String[] getValueToleranceRange(ColumnValue columnValue) {
        double doubleValue = ((Number) columnValue.getValue()).doubleValue();
        double doubleValue2 = columnValue.getTolerance().doubleValue();
        return new String[]{Double.toString(doubleValue - doubleValue2), Double.toString(doubleValue + doubleValue2)};
    }
}
