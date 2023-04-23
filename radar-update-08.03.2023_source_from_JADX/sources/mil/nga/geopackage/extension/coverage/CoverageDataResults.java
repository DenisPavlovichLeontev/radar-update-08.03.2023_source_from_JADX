package mil.nga.geopackage.extension.coverage;

import mil.nga.geopackage.tiles.matrix.TileMatrix;

public class CoverageDataResults {
    private int height;
    private final TileMatrix tileMatrix;
    private final Double[][] values;
    private int width;

    public CoverageDataResults(Double[][] dArr, TileMatrix tileMatrix2) {
        this.values = dArr;
        this.tileMatrix = tileMatrix2;
        this.height = dArr.length;
        this.width = dArr[0].length;
    }

    public Double[][] getValues() {
        return this.values;
    }

    public TileMatrix getTileMatrix() {
        return this.tileMatrix;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public Double getValue(int i, int i2) {
        return this.values[i][i2];
    }

    public long getZoomLevel() {
        return this.tileMatrix.getZoomLevel();
    }
}
