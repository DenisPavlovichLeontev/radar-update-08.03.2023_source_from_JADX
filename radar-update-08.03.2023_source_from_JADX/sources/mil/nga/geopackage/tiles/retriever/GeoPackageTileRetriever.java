package mil.nga.geopackage.tiles.retriever;

import mil.nga.geopackage.projection.ProjectionFactory;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.geopackage.tiles.user.TileDao;

public class GeoPackageTileRetriever implements TileRetriever {
    private final TileCreator tileCreator;

    public GeoPackageTileRetriever(TileDao tileDao) {
        this(tileDao, (Integer) null, (Integer) null);
    }

    public GeoPackageTileRetriever(TileDao tileDao, Integer num, Integer num2) {
        tileDao.adjustTileMatrixLengths();
        this.tileCreator = new TileCreator(tileDao, num, num2, ProjectionFactory.getProjection(3857));
    }

    public boolean hasTile(int i, int i2, int i3) {
        return this.tileCreator.hasTile(TileBoundingBoxUtils.getWebMercatorBoundingBox((long) i, (long) i2, i3));
    }

    public GeoPackageTile getTile(int i, int i2, int i3) {
        return this.tileCreator.getTile(TileBoundingBoxUtils.getWebMercatorBoundingBox((long) i, (long) i2, i3));
    }
}
