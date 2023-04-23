package mil.nga.geopackage.schema.columns;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import mil.nga.geopackage.schema.TableColumnKey;

public class DataColumnsDao extends BaseDaoImpl<DataColumns, TableColumnKey> {
    public DataColumnsDao(ConnectionSource connectionSource, Class<DataColumns> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public DataColumns queryForId(TableColumnKey tableColumnKey) throws SQLException {
        if (tableColumnKey != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("table_name", tableColumnKey.getTableName());
            hashMap.put("column_name", tableColumnKey.getColumnName());
            List queryForFieldValues = queryForFieldValues(hashMap);
            if (!queryForFieldValues.isEmpty()) {
                if (queryForFieldValues.size() <= 1) {
                    return (DataColumns) queryForFieldValues.get(0);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("More than one ");
                Class<DataColumns> cls = DataColumns.class;
                sb.append("DataColumns");
                sb.append(" returned for key. Table Name: ");
                sb.append(tableColumnKey.getTableName());
                sb.append(", Column Name: ");
                sb.append(tableColumnKey.getColumnName());
                throw new SQLException(sb.toString());
            }
        }
        return null;
    }

    public TableColumnKey extractId(DataColumns dataColumns) throws SQLException {
        return dataColumns.getId();
    }

    public boolean idExists(TableColumnKey tableColumnKey) throws SQLException {
        return queryForId(tableColumnKey) != null;
    }

    public DataColumns queryForSameId(DataColumns dataColumns) throws SQLException {
        return queryForId(dataColumns.getId());
    }

    public int updateId(DataColumns dataColumns, TableColumnKey tableColumnKey) throws SQLException {
        DataColumns queryForId = queryForId(dataColumns.getId());
        if (queryForId == null || tableColumnKey == null) {
            return 0;
        }
        queryForId.setId(tableColumnKey);
        return update(queryForId);
    }

    public int delete(DataColumns dataColumns) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        deleteBuilder.where().mo19710eq("table_name", dataColumns.getTableName()).and().mo19710eq("column_name", dataColumns.getColumnName());
        return delete(deleteBuilder.prepare());
    }

    public int deleteById(TableColumnKey tableColumnKey) throws SQLException {
        DataColumns queryForId;
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

    public int update(DataColumns dataColumns) throws SQLException {
        UpdateBuilder updateBuilder = updateBuilder();
        updateBuilder.updateColumnValue("name", dataColumns.getName());
        updateBuilder.updateColumnValue("title", dataColumns.getTitle());
        updateBuilder.updateColumnValue("description", dataColumns.getDescription());
        updateBuilder.updateColumnValue("mime_type", dataColumns.getMimeType());
        updateBuilder.updateColumnValue("constraint_name", dataColumns.getConstraintName());
        updateBuilder.where().mo19710eq("table_name", dataColumns.getTableName()).and().mo19710eq("column_name", dataColumns.getColumnName());
        return update(updateBuilder.prepare());
    }

    public List<DataColumns> queryByConstraintName(String str) throws SQLException {
        return queryForEq("constraint_name", str);
    }

    public DataColumns getDataColumn(String str, String str2) throws SQLException {
        return queryForId(new TableColumnKey(str, str2));
    }
}
