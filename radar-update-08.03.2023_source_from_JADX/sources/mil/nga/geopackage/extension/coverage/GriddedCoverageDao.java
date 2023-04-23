package mil.nga.geopackage.extension.coverage;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

public class GriddedCoverageDao extends BaseDaoImpl<GriddedCoverage, Long> {
    public GriddedCoverageDao(ConnectionSource connectionSource, Class<GriddedCoverage> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public GriddedCoverage query(TileMatrixSet tileMatrixSet) {
        return query(tileMatrixSet.getTableName());
    }

    public GriddedCoverage query(String str) {
        try {
            QueryBuilder queryBuilder = queryBuilder();
            queryBuilder.where().mo19710eq(GriddedCoverage.COLUMN_TILE_MATRIX_SET_NAME, str);
            return (GriddedCoverage) queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to query for Gridded Coverage by Tile Matrix Set Name: " + str, e);
        }
    }

    public int delete(TileMatrixSet tileMatrixSet) {
        return delete(tileMatrixSet.getTableName());
    }

    public int delete(String str) {
        DeleteBuilder deleteBuilder = deleteBuilder();
        try {
            deleteBuilder.where().mo19710eq(GriddedCoverage.COLUMN_TILE_MATRIX_SET_NAME, str);
            return delete(deleteBuilder.prepare());
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete Gridded Coverage by Tile Matrix Set Name: " + str, e);
        }
    }
}
