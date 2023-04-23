package mil.nga.geopackage.core.srs;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;

public class SpatialReferenceSystemSqlMmDao extends BaseDaoImpl<SpatialReferenceSystemSqlMm, Integer> {
    public SpatialReferenceSystemSqlMmDao(ConnectionSource connectionSource, Class<SpatialReferenceSystemSqlMm> cls) throws SQLException {
        super(connectionSource, cls);
    }
}
