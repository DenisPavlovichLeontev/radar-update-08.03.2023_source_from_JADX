package org.mapsforge.map.reader;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.reader.header.SubFileParameter;

class QueryParameters {
    long fromBaseTileX;
    long fromBaseTileY;
    long fromBlockX;
    long fromBlockY;
    int queryTileBitmask;
    int queryZoomLevel;
    long toBaseTileX;
    long toBaseTileY;
    long toBlockX;
    long toBlockY;
    boolean useTileBitmask;

    QueryParameters() {
    }

    public void calculateBaseTiles(Tile tile, SubFileParameter subFileParameter) {
        if (tile.zoomLevel < subFileParameter.baseZoomLevel) {
            int i = subFileParameter.baseZoomLevel - tile.zoomLevel;
            this.fromBaseTileX = (long) (tile.tileX << i);
            long j = (long) (tile.tileY << i);
            this.fromBaseTileY = j;
            long j2 = (long) (1 << i);
            this.toBaseTileX = (this.fromBaseTileX + j2) - 1;
            this.toBaseTileY = (j + j2) - 1;
            this.useTileBitmask = false;
        } else if (tile.zoomLevel > subFileParameter.baseZoomLevel) {
            int i2 = tile.zoomLevel - subFileParameter.baseZoomLevel;
            this.fromBaseTileX = (long) (tile.tileX >>> i2);
            long j3 = (long) (tile.tileY >>> i2);
            this.fromBaseTileY = j3;
            this.toBaseTileX = this.fromBaseTileX;
            this.toBaseTileY = j3;
            this.useTileBitmask = true;
            this.queryTileBitmask = QueryCalculations.calculateTileBitmask(tile, i2);
        } else {
            this.fromBaseTileX = (long) tile.tileX;
            long j4 = (long) tile.tileY;
            this.fromBaseTileY = j4;
            this.toBaseTileX = this.fromBaseTileX;
            this.toBaseTileY = j4;
            this.useTileBitmask = false;
        }
    }

    public void calculateBaseTiles(Tile tile, Tile tile2, SubFileParameter subFileParameter) {
        if (tile.zoomLevel < subFileParameter.baseZoomLevel) {
            int i = subFileParameter.baseZoomLevel - tile.zoomLevel;
            this.fromBaseTileX = (long) (tile.tileX << i);
            this.fromBaseTileY = (long) (tile.tileY << i);
            int i2 = 1 << i;
            this.toBaseTileX = (long) (((tile2.tileX << i) + i2) - 1);
            this.toBaseTileY = (long) (((tile2.tileY << i) + i2) - 1);
            this.useTileBitmask = false;
        } else if (tile.zoomLevel > subFileParameter.baseZoomLevel) {
            int i3 = tile.zoomLevel - subFileParameter.baseZoomLevel;
            this.fromBaseTileX = (long) (tile.tileX >>> i3);
            this.fromBaseTileY = (long) (tile.tileY >>> i3);
            this.toBaseTileX = (long) (tile2.tileX >>> i3);
            this.toBaseTileY = (long) (tile2.tileY >>> i3);
            this.useTileBitmask = true;
            this.queryTileBitmask = QueryCalculations.calculateTileBitmask(tile, tile2, i3);
        } else {
            this.fromBaseTileX = (long) tile.tileX;
            this.fromBaseTileY = (long) tile.tileY;
            this.toBaseTileX = (long) tile2.tileX;
            this.toBaseTileY = (long) tile2.tileY;
            this.useTileBitmask = false;
        }
    }

    public void calculateBlocks(SubFileParameter subFileParameter) {
        this.fromBlockX = Math.max(this.fromBaseTileX - subFileParameter.boundaryTileLeft, 0);
        this.fromBlockY = Math.max(this.fromBaseTileY - subFileParameter.boundaryTileTop, 0);
        this.toBlockX = Math.min(this.toBaseTileX - subFileParameter.boundaryTileLeft, subFileParameter.blocksWidth - 1);
        this.toBlockY = Math.min(this.toBaseTileY - subFileParameter.boundaryTileTop, subFileParameter.blocksHeight - 1);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof QueryParameters)) {
            return false;
        }
        QueryParameters queryParameters = (QueryParameters) obj;
        if (this.fromBaseTileX == queryParameters.fromBaseTileX && this.fromBlockX == queryParameters.fromBlockX && this.fromBaseTileY == queryParameters.fromBaseTileY && this.fromBlockY == queryParameters.fromBlockY && this.queryTileBitmask == queryParameters.queryTileBitmask && this.queryZoomLevel == queryParameters.queryZoomLevel && this.toBaseTileX == queryParameters.toBaseTileX && this.toBaseTileY == queryParameters.toBaseTileY && this.toBlockX == queryParameters.toBlockX && this.toBlockY == queryParameters.toBlockY && this.useTileBitmask == queryParameters.useTileBitmask) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long j = this.fromBaseTileX;
        long j2 = this.fromBaseTileY;
        long j3 = this.toBaseTileX;
        long j4 = this.toBaseTileY;
        long j5 = this.fromBlockX;
        long j6 = this.fromBlockY;
        long j7 = this.toBlockX;
        long j8 = this.toBlockY;
        return ((((((((((((((((((217 + ((int) (j ^ (j >>> 16)))) * 31) + ((int) (j2 ^ (j2 >>> 16)))) * 31) + ((int) (j3 ^ (j3 >>> 16)))) * 31) + ((int) (j4 ^ (j4 >>> 16)))) * 31) + ((int) (j5 ^ (j5 >>> 16)))) * 31) + ((int) (j6 ^ (j6 >>> 16)))) * 31) + ((int) (j7 ^ (j7 >>> 16)))) * 31) + ((int) (j8 ^ (j8 >>> 16)))) * 31) + this.queryZoomLevel) * 31) + this.queryTileBitmask;
    }
}
