package mil.nga.geopackage.tiles.matrixset;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TileMatrixSetDao extends BaseDaoImpl<TileMatrixSet, String> {
    public TileMatrixSetDao(ConnectionSource connectionSource, Class<TileMatrixSet> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public List<String> getTileTables() throws SQLException {
        ArrayList arrayList = new ArrayList();
        for (TileMatrixSet tableName : queryForAll()) {
            arrayList.add(tableName.getTableName());
        }
        return arrayList;
    }
}
