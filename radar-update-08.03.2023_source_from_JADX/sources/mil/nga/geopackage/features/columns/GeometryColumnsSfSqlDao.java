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

public class GeometryColumnsSfSqlDao extends BaseDaoImpl<GeometryColumnsSfSql, TableColumnKey> {
    public GeometryColumnsSfSqlDao(ConnectionSource connectionSource, Class<GeometryColumnsSfSql> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public GeometryColumnsSfSql queryForId(TableColumnKey tableColumnKey) throws SQLException {
        if (tableColumnKey != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("f_table_name", tableColumnKey.getTableName());
            hashMap.put("f_geometry_column", tableColumnKey.getColumnName());
            List queryForFieldValues = queryForFieldValues(hashMap);
            if (!queryForFieldValues.isEmpty()) {
                if (queryForFieldValues.size() <= 1) {
                    return (GeometryColumnsSfSql) queryForFieldValues.get(0);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("More than one ");
                Class<GeometryColumnsSfSql> cls = GeometryColumnsSfSql.class;
                sb.append("GeometryColumnsSfSql");
                sb.append(" returned for key. Table Name: ");
                sb.append(tableColumnKey.getTableName());
                sb.append(", Column Name: ");
                sb.append(tableColumnKey.getColumnName());
                throw new SQLException(sb.toString());
            }
        }
        return null;
    }

    public TableColumnKey extractId(GeometryColumnsSfSql geometryColumnsSfSql) throws SQLException {
        return geometryColumnsSfSql.getId();
    }

    public boolean idExists(TableColumnKey tableColumnKey) throws SQLException {
        return queryForId(tableColumnKey) != null;
    }

    public GeometryColumnsSfSql queryForSameId(GeometryColumnsSfSql geometryColumnsSfSql) throws SQLException {
        return queryForId(geometryColumnsSfSql.getId());
    }

    public int updateId(GeometryColumnsSfSql geometryColumnsSfSql, TableColumnKey tableColumnKey) throws SQLException {
        GeometryColumnsSfSql queryForId = queryForId(geometryColumnsSfSql.getId());
        if (queryForId == null || tableColumnKey == null) {
            return 0;
        }
        queryForId.setId(tableColumnKey);
        return update(queryForId);
    }

    public int delete(GeometryColumnsSfSql geometryColumnsSfSql) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        deleteBuilder.where().mo19710eq("f_table_name", geometryColumnsSfSql.getFTableName()).and().mo19710eq("f_geometry_column", geometryColumnsSfSql.getFGeometryColumn());
        return delete(deleteBuilder.prepare());
    }

    public int deleteById(TableColumnKey tableColumnKey) throws SQLException {
        GeometryColumnsSfSql queryForId;
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

    public int update(GeometryColumnsSfSql geometryColumnsSfSql) throws SQLException {
        UpdateBuilder updateBuilder = updateBuilder();
        updateBuilder.updateColumnValue(GeometryColumnsSfSql.COLUMN_GEOMETRY_TYPE, Integer.valueOf(geometryColumnsSfSql.getGeometryTypeCode()));
        updateBuilder.updateColumnValue(GeometryColumnsSfSql.COLUMN_COORD_DIMENSION, Byte.valueOf(geometryColumnsSfSql.getCoordDimension()));
        updateBuilder.updateColumnValue("srid", Long.valueOf(geometryColumnsSfSql.getSrid()));
        updateBuilder.where().mo19710eq("f_table_name", geometryColumnsSfSql.getFTableName()).and().mo19710eq("f_geometry_column", geometryColumnsSfSql.getFGeometryColumn());
        return update(updateBuilder.prepare());
    }
}
