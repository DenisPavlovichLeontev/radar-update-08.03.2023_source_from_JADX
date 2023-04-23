package mil.nga.geopackage.features.columns;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import mil.nga.geopackage.schema.TableColumnKey;

public class GeometryColumnsSqlMmDao extends BaseDaoImpl<GeometryColumnsSqlMm, TableColumnKey> {
    public GeometryColumnsSqlMmDao(ConnectionSource connectionSource, Class<GeometryColumnsSqlMm> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public GeometryColumnsSqlMm queryForId(TableColumnKey tableColumnKey) throws SQLException {
        if (tableColumnKey != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("table_name", tableColumnKey.getTableName());
            hashMap.put("column_name", tableColumnKey.getColumnName());
            List queryForFieldValues = queryForFieldValues(hashMap);
            if (!queryForFieldValues.isEmpty()) {
                if (queryForFieldValues.size() <= 1) {
                    return (GeometryColumnsSqlMm) queryForFieldValues.get(0);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("More than one ");
                Class<GeometryColumnsSqlMm> cls = GeometryColumnsSqlMm.class;
                sb.append("GeometryColumnsSqlMm");
                sb.append(" returned for key. Table Name: ");
                sb.append(tableColumnKey.getTableName());
                sb.append(", Column Name: ");
                sb.append(tableColumnKey.getColumnName());
                throw new SQLException(sb.toString());
            }
        }
        return null;
    }

    public TableColumnKey extractId(GeometryColumnsSqlMm geometryColumnsSqlMm) throws SQLException {
        return geometryColumnsSqlMm.getId();
    }

    public boolean idExists(TableColumnKey tableColumnKey) throws SQLException {
        return queryForId(tableColumnKey) != null;
    }

    public GeometryColumnsSqlMm queryForSameId(GeometryColumnsSqlMm geometryColumnsSqlMm) throws SQLException {
        return queryForId(geometryColumnsSqlMm.getId());
    }

    public int updateId(GeometryColumnsSqlMm geometryColumnsSqlMm, TableColumnKey tableColumnKey) throws SQLException {
        GeometryColumnsSqlMm queryForId = queryForId(geometryColumnsSqlMm.getId());
        if (queryForId == null || tableColumnKey == null) {
            return 0;
        }
        queryForId.setId(tableColumnKey);
        return update(queryForId);
    }

    public int delete(GeometryColumnsSqlMm geometryColumnsSqlMm) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        deleteBuilder.where().mo19710eq("table_name", geometryColumnsSqlMm.getTableName()).and().mo19710eq("column_name", geometryColumnsSqlMm.getColumnName());
        return delete(deleteBuilder.prepare());
    }

    public int deleteById(TableColumnKey tableColumnKey) throws SQLException {
        GeometryColumnsSqlMm queryForId;
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

    public int update(GeometryColumnsSqlMm geometryColumnsSqlMm) throws SQLException {
        UpdateBuilder updateBuilder = updateBuilder();
        updateBuilder.updateColumnValue("geometry_type_name", geometryColumnsSqlMm.getGeometryTypeName());
        updateBuilder.updateColumnValue("srs_id", Long.valueOf(geometryColumnsSqlMm.getSrsId()));
        updateBuilder.where().mo19710eq("table_name", geometryColumnsSqlMm.getTableName()).and().mo19710eq("column_name", geometryColumnsSqlMm.getColumnName());
        return update(updateBuilder.prepare());
    }
}
