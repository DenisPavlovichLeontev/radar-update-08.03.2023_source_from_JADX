package mil.nga.geopackage.core.srs;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;

public class SpatialReferenceSystemSfSqlDao extends BaseDaoImpl<SpatialReferenceSystemSfSql, Integer> {
    public SpatialReferenceSystemSfSqlDao(ConnectionSource connectionSource, Class<SpatialReferenceSystemSfSql> cls) throws SQLException {
        super(connectionSource, cls);
    }
}
