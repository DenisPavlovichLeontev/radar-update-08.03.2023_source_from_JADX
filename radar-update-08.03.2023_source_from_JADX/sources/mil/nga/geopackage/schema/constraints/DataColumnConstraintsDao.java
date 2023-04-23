package mil.nga.geopackage.schema.constraints;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.schema.columns.DataColumns;
import mil.nga.geopackage.schema.columns.DataColumnsDao;

public class DataColumnConstraintsDao extends BaseDaoImpl<DataColumnConstraints, Void> {
    private DataColumnsDao dataColumnsDao;

    public DataColumnConstraintsDao(ConnectionSource connectionSource, Class<DataColumnConstraints> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public int deleteCascade(DataColumnConstraints dataColumnConstraints) throws SQLException {
        if (dataColumnConstraints == null) {
            return 0;
        }
        List<DataColumnConstraints> queryByConstraintName = queryByConstraintName(dataColumnConstraints.getConstraintName());
        if (queryByConstraintName.size() == 1) {
            DataColumnConstraints dataColumnConstraints2 = queryByConstraintName.get(0);
            if (dataColumnConstraints2.getConstraintName().equals(dataColumnConstraints.getConstraintName()) && dataColumnConstraints2.getConstraintType().equals(dataColumnConstraints.getConstraintType()) && (dataColumnConstraints2.getValue() != null ? dataColumnConstraints2.getValue().equals(dataColumnConstraints.getValue()) : dataColumnConstraints.getValue() == null)) {
                DataColumnsDao dataColumnsDao2 = getDataColumnsDao();
                List<DataColumns> queryByConstraintName2 = dataColumnsDao2.queryByConstraintName(dataColumnConstraints.getConstraintName());
                if (!queryByConstraintName2.isEmpty()) {
                    dataColumnsDao2.delete(queryByConstraintName2);
                }
            }
        }
        return delete(dataColumnConstraints);
    }

    public int deleteCascade(Collection<DataColumnConstraints> collection) throws SQLException {
        int i = 0;
        if (collection != null) {
            for (DataColumnConstraints deleteCascade : collection) {
                i += deleteCascade(deleteCascade);
            }
        }
        return i;
    }

    public int deleteCascade(PreparedQuery<DataColumnConstraints> preparedQuery) throws SQLException {
        if (preparedQuery != null) {
            return deleteCascade((Collection<DataColumnConstraints>) query(preparedQuery));
        }
        return 0;
    }

    private DataColumnsDao getDataColumnsDao() throws SQLException {
        if (this.dataColumnsDao == null) {
            this.dataColumnsDao = (DataColumnsDao) DaoManager.createDao(this.connectionSource, DataColumns.class);
        }
        return this.dataColumnsDao;
    }

    public int update(DataColumnConstraints dataColumnConstraints) throws SQLException {
        UpdateBuilder updateBuilder = updateBuilder();
        updateBuilder.updateColumnValue("min", dataColumnConstraints.getMin());
        updateBuilder.updateColumnValue(DataColumnConstraints.COLUMN_MIN_IS_INCLUSIVE, dataColumnConstraints.getMinIsInclusive());
        updateBuilder.updateColumnValue("max", dataColumnConstraints.getMax());
        updateBuilder.updateColumnValue(DataColumnConstraints.COLUMN_MAX_IS_INCLUSIVE, dataColumnConstraints.getMaxIsInclusive());
        updateBuilder.updateColumnValue("description", dataColumnConstraints.getDescription());
        setUniqueWhere(updateBuilder.where(), dataColumnConstraints.getConstraintName(), dataColumnConstraints.getConstraintType(), dataColumnConstraints.getValue());
        return update(updateBuilder.prepare());
    }

    public int delete(DataColumnConstraints dataColumnConstraints) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        setUniqueWhere(deleteBuilder.where(), dataColumnConstraints.getConstraintName(), dataColumnConstraints.getConstraintType(), dataColumnConstraints.getValue());
        return deleteBuilder.delete();
    }

    public List<DataColumnConstraints> queryByConstraintName(String str) throws SQLException {
        return queryForEq("constraint_name", str);
    }

    public DataColumnConstraints queryByUnique(String str, DataColumnConstraintType dataColumnConstraintType, String str2) throws SQLException {
        QueryBuilder queryBuilder = queryBuilder();
        setUniqueWhere(queryBuilder.where(), str, dataColumnConstraintType, str2);
        List query = queryBuilder.query();
        if (query.isEmpty()) {
            return null;
        }
        if (query.size() <= 1) {
            return (DataColumnConstraints) query.get(0);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("More than one ");
        Class<DataColumnConstraints> cls = DataColumnConstraints.class;
        sb.append("DataColumnConstraints");
        sb.append(" was found for unique constraint. Name: ");
        sb.append(str);
        sb.append(", Type: ");
        sb.append(dataColumnConstraintType);
        sb.append(", Value: ");
        sb.append(str2);
        throw new GeoPackageException(sb.toString());
    }

    private void setUniqueWhere(Where<DataColumnConstraints, Void> where, String str, DataColumnConstraintType dataColumnConstraintType, String str2) throws SQLException {
        where.mo19710eq("constraint_name", str).and().mo19710eq(DataColumnConstraints.COLUMN_CONSTRAINT_TYPE, dataColumnConstraintType.getValue());
        if (str2 == null) {
            where.and().isNull(DataColumnConstraints.COLUMN_VALUE);
        } else {
            where.and().mo19710eq(DataColumnConstraints.COLUMN_VALUE, str2);
        }
    }
}
