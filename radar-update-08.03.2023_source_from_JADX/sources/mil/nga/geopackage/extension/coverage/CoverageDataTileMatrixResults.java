package mil.nga.geopackage.extension.coverage;

import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.user.TileCursor;

public class CoverageDataTileMatrixResults {
    private TileMatrix tileMatrix;
    private TileCursor tileResults;

    public CoverageDataTileMatrixResults(TileMatrix tileMatrix2, TileCursor tileCursor) {
        this.tileMatrix = tileMatrix2;
        this.tileResults = tileCursor;
    }

    public TileMatrix getTileMatrix() {
        return this.tileMatrix;
    }

    public TileCursor getTileResults() {
        return this.tileResults;
    }
}
