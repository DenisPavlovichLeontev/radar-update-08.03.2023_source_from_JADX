package mil.nga.geopackage.extension.index;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.Collection;
import mil.nga.geopackage.GeoPackageException;

public class TableIndexDao extends BaseDaoImpl<TableIndex, String> {
    private GeometryIndexDao geometryIndexDao;

    public TableIndexDao(ConnectionSource connectionSource, Class<TableIndex> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public int deleteCascade(TableIndex tableIndex) throws SQLException {
        if (tableIndex == null) {
            return 0;
        }
        GeometryIndexDao geometryIndexDao2 = getGeometryIndexDao();
        if (geometryIndexDao2.isTableExists()) {
            DeleteBuilder deleteBuilder = geometryIndexDao2.deleteBuilder();
            deleteBuilder.where().mo19710eq("table_name", tableIndex.getTableName());
            geometryIndexDao2.delete(deleteBuilder.prepare());
        }
        return delete(tableIndex);
    }

    public int deleteCascade(Collection<TableIndex> collection) throws SQLException {
        int i = 0;
        if (collection != null) {
            for (TableIndex deleteCascade : collection) {
                i += deleteCascade(deleteCascade);
            }
        }
        return i;
    }

    public int deleteCascade(PreparedQuery<TableIndex> preparedQuery) throws SQLException {
        if (preparedQuery != null) {
            return deleteCascade((Collection<TableIndex>) query(preparedQuery));
        }
        return 0;
    }

    public int deleteByIdCascade(String str) throws SQLException {
        TableIndex tableIndex;
        if (str == null || (tableIndex = (TableIndex) queryForId(str)) == null) {
            return 0;
        }
        return deleteCascade(tableIndex);
    }

    public int deleteIdsCascade(Collection<String> collection) throws SQLException {
        int i = 0;
        if (collection != null) {
            for (String deleteByIdCascade : collection) {
                i += deleteByIdCascade(deleteByIdCascade);
            }
        }
        return i;
    }

    public void deleteTable(String str) {
        try {
            deleteByIdCascade(str);
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete table: " + str, e);
        }
    }

    private GeometryIndexDao getGeometryIndexDao() throws SQLException {
        if (this.geometryIndexDao == null) {
            this.geometryIndexDao = (GeometryIndexDao) DaoManager.createDao(this.connectionSource, GeometryIndex.class);
        }
        return this.geometryIndexDao;
    }

    public int deleteAllCascade() throws SQLException {
        getGeometryIndexDao().deleteAll();
        return deleteAll();
    }

    public int deleteAll() throws SQLException {
        if (isTableExists()) {
            return delete(deleteBuilder().prepare());
        }
        return 0;
    }
}
