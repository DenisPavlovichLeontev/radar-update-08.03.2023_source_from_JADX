package mil.nga.geopackage.tiles.retriever;

import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.user.TileDao;
import mil.nga.geopackage.tiles.user.TileRow;

public class GoogleAPIGeoPackageTileRetriever implements TileRetriever {
    private final TileDao tileDao;

    public GoogleAPIGeoPackageTileRetriever(TileDao tileDao2) {
        this.tileDao = tileDao2;
    }

    public boolean hasTile(int i, int i2, int i3) {
        return retrieveTileRow(i, i2, i3) != null;
    }

    public GeoPackageTile getTile(int i, int i2, int i3) {
        TileRow retrieveTileRow = retrieveTileRow(i, i2, i3);
        if (retrieveTileRow == null) {
            return null;
        }
        TileMatrix tileMatrix = this.tileDao.getTileMatrix((long) i3);
        return new GeoPackageTile((int) tileMatrix.getTileWidth(), (int) tileMatrix.getTileHeight(), retrieveTileRow.getTileData());
    }

    private TileRow retrieveTileRow(int i, int i2, int i3) {
        return this.tileDao.queryForTile((long) i, (long) i2, (long) i3);
    }
}
