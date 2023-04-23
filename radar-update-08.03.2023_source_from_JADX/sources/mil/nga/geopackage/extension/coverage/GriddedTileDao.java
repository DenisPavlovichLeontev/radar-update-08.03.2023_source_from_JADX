package mil.nga.geopackage.extension.coverage;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;

public class GriddedTileDao extends BaseDaoImpl<GriddedTile, Long> {
    public GriddedTileDao(ConnectionSource connectionSource, Class<GriddedTile> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public List<GriddedTile> query(Contents contents) {
        return query(contents.getTableName());
    }

    public List<GriddedTile> query(String str) {
        try {
            return queryForEq(GriddedTile.COLUMN_TABLE_NAME, str);
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to query for Gridded Tile objects by Table Name: " + str, e);
        }
    }

    public GriddedTile query(String str, long j) {
        try {
            QueryBuilder queryBuilder = queryBuilder();
            queryBuilder.where().mo19710eq(GriddedTile.COLUMN_TABLE_NAME, str).and().mo19710eq(GriddedTile.COLUMN_TABLE_ID, Long.valueOf(j));
            return (GriddedTile) queryForFirst(queryBuilder.prepare());
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to query for Gridded Tile objects by Table Name: " + str + ", Tile Id: " + j, e);
        }
    }

    public int delete(Contents contents) {
        return delete(contents.getTableName());
    }

    public int delete(String str) {
        DeleteBuilder deleteBuilder = deleteBuilder();
        try {
            deleteBuilder.where().mo19710eq(GriddedTile.COLUMN_TABLE_NAME, str);
            return delete(deleteBuilder.prepare());
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete Gridded Tile by Table Name: " + str, e);
        }
    }
}
