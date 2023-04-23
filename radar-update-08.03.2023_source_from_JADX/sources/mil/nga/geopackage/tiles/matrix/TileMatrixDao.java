package mil.nga.geopackage.tiles.matrix;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class TileMatrixDao extends BaseDaoImpl<TileMatrix, TileMatrixKey> {
    public TileMatrixDao(ConnectionSource connectionSource, Class<TileMatrix> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public TileMatrix queryForId(TileMatrixKey tileMatrixKey) throws SQLException {
        if (tileMatrixKey != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("table_name", tileMatrixKey.getTableName());
            hashMap.put("zoom_level", Long.valueOf(tileMatrixKey.getZoomLevel()));
            List queryForFieldValues = queryForFieldValues(hashMap);
            if (!queryForFieldValues.isEmpty()) {
                if (queryForFieldValues.size() <= 1) {
                    return (TileMatrix) queryForFieldValues.get(0);
                }
                throw new SQLException("More than one TileMatrix returned for key. Table Name: " + tileMatrixKey.getTableName() + ", Zoom Level: " + tileMatrixKey.getZoomLevel());
            }
        }
        return null;
    }

    public TileMatrixKey extractId(TileMatrix tileMatrix) throws SQLException {
        return tileMatrix.getId();
    }

    public boolean idExists(TileMatrixKey tileMatrixKey) throws SQLException {
        return queryForId(tileMatrixKey) != null;
    }

    public TileMatrix queryForSameId(TileMatrix tileMatrix) throws SQLException {
        return queryForId(tileMatrix.getId());
    }

    public int updateId(TileMatrix tileMatrix, TileMatrixKey tileMatrixKey) throws SQLException {
        TileMatrix queryForId = queryForId(tileMatrix.getId());
        if (queryForId == null || tileMatrixKey == null) {
            return 0;
        }
        queryForId.setId(tileMatrixKey);
        return update(queryForId);
    }

    public int delete(TileMatrix tileMatrix) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        deleteBuilder.where().mo19710eq("table_name", tileMatrix.getTableName()).and().mo19710eq("zoom_level", Long.valueOf(tileMatrix.getZoomLevel()));
        return delete(deleteBuilder.prepare());
    }

    public int deleteById(TileMatrixKey tileMatrixKey) throws SQLException {
        TileMatrix queryForId;
        if (tileMatrixKey == null || (queryForId = queryForId(tileMatrixKey)) == null) {
            return 0;
        }
        return delete(queryForId);
    }

    public int deleteIds(Collection<TileMatrixKey> collection) throws SQLException {
        int i = 0;
        if (collection != null) {
            for (TileMatrixKey deleteById : collection) {
                i += deleteById(deleteById);
            }
        }
        return i;
    }

    public int update(TileMatrix tileMatrix) throws SQLException {
        UpdateBuilder updateBuilder = updateBuilder();
        updateBuilder.updateColumnValue(TileMatrix.COLUMN_MATRIX_WIDTH, Long.valueOf(tileMatrix.getMatrixWidth()));
        updateBuilder.updateColumnValue(TileMatrix.COLUMN_MATRIX_HEIGHT, Long.valueOf(tileMatrix.getMatrixHeight()));
        updateBuilder.updateColumnValue(TileMatrix.COLUMN_TILE_WIDTH, Long.valueOf(tileMatrix.getTileWidth()));
        updateBuilder.updateColumnValue(TileMatrix.COLUMN_TILE_HEIGHT, Long.valueOf(tileMatrix.getTileHeight()));
        updateBuilder.updateColumnValue(TileMatrix.COLUMN_PIXEL_X_SIZE, Double.valueOf(tileMatrix.getPixelXSize()));
        updateBuilder.updateColumnValue(TileMatrix.COLUMN_PIXEL_Y_SIZE, Double.valueOf(tileMatrix.getPixelYSize()));
        updateBuilder.where().mo19710eq("table_name", tileMatrix.getTableName()).and().mo19710eq("zoom_level", Long.valueOf(tileMatrix.getZoomLevel()));
        return update(updateBuilder.prepare());
    }
}
