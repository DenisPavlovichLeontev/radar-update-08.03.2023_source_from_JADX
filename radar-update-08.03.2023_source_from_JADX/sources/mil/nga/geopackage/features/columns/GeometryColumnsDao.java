package mil.nga.geopackage.features.columns;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import mil.nga.geopackage.schema.TableColumnKey;

public class GeometryColumnsDao extends BaseDaoImpl<GeometryColumns, TableColumnKey> {
    public GeometryColumnsDao(ConnectionSource connectionSource, Class<GeometryColumns> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public List<String> getFeatureTables() throws SQLException {
        ArrayList arrayList = new ArrayList();
        for (GeometryColumns tableName : queryForAll()) {
            arrayList.add(tableName.getTableName());
        }
        return arrayList;
    }

    public GeometryColumns queryForId(TableColumnKey tableColumnKey) throws SQLException {
        if (tableColumnKey != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("table_name", tableColumnKey.getTableName());
            hashMap.put("column_name", tableColumnKey.getColumnName());
            List queryForFieldValues = queryForFieldValues(hashMap);
            if (!queryForFieldValues.isEmpty()) {
                if (queryForFieldValues.size() <= 1) {
                    return (GeometryColumns) queryForFieldValues.get(0);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("More than one ");
                Class<GeometryColumns> cls = GeometryColumns.class;
                sb.append("GeometryColumns");
                sb.append(" returned for key. Table Name: ");
                sb.append(tableColumnKey.getTableName());
                sb.append(", Column Name: ");
                sb.append(tableColumnKey.getColumnName());
                throw new SQLException(sb.toString());
            }
        }
        return null;
    }

    public TableColumnKey extractId(GeometryColumns geometryColumns) throws SQLException {
        return geometryColumns.getId();
    }

    public boolean idExists(TableColumnKey tableColumnKey) throws SQLException {
        return queryForId(tableColumnKey) != null;
    }

    public GeometryColumns queryForSameId(GeometryColumns geometryColumns) throws SQLException {
        return queryForId(geometryColumns.getId());
    }

    public int updateId(GeometryColumns geometryColumns, TableColumnKey tableColumnKey) throws SQLException {
        GeometryColumns queryForId = queryForId(geometryColumns.getId());
        if (queryForId == null || tableColumnKey == null) {
            return 0;
        }
        queryForId.setId(tableColumnKey);
        return update(queryForId);
    }

    public int delete(GeometryColumns geometryColumns) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        deleteBuilder.where().mo19710eq("table_name", geometryColumns.getTableName()).and().mo19710eq("column_name", geometryColumns.getColumnName());
        return delete(deleteBuilder.prepare());
    }

    public int deleteById(TableColumnKey tableColumnKey) throws SQLException {
        GeometryColumns queryForId;
        if (tableColumnKey == null || (queryForId = queryForId(tableColumnKey)) == null) {
            return 0;
        }
        return delete(queryForId);
    }

    public int deleteIds(Collection<TableColumnKey> collection) throws SQLException {
        int i = 0;
        if (collection != null) {
            for (TableColumnKey deleteById : collection) {
                i += deleteById(deleteById);
            }
        }
        return i;
    }

    public int update(GeometryColumns geometryColumns) throws SQLException {
        UpdateBuilder updateBuilder = updateBuilder();
        updateBuilder.updateColumnValue("geometry_type_name", geometryColumns.getGeometryTypeName());
        updateBuilder.updateColumnValue("srs_id", Long.valueOf(geometryColumns.getSrsId()));
        updateBuilder.updateColumnValue(GeometryColumns.COLUMN_Z, Byte.valueOf(geometryColumns.getZ()));
        updateBuilder.updateColumnValue(GeometryColumns.COLUMN_M, Byte.valueOf(geometryColumns.getM()));
        updateBuilder.where().mo19710eq("table_name", geometryColumns.getTableName()).and().mo19710eq("column_name", geometryColumns.getColumnName());
        return update(updateBuilder.prepare());
    }

    public GeometryColumns queryForTableName(String str) throws SQLException {
        if (str != null) {
            List queryForEq = queryForEq("table_name", str);
            if (!queryForEq.isEmpty()) {
                if (queryForEq.size() <= 1) {
                    return (GeometryColumns) queryForEq.get(0);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("More than one ");
                Class<GeometryColumns> cls = GeometryColumns.class;
                sb.append("GeometryColumns");
                sb.append(" returned for Table Name: ");
                sb.append(str);
                throw new SQLException(sb.toString());
            }
        }
        return null;
    }
}
