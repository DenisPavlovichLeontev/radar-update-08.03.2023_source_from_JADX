package org.mapsforge.map.reader;

import org.mapsforge.core.model.Tile;

final class QueryCalculations {
    static int calculateTileBitmask(Tile tile, int i) {
        if (i == 1) {
            return getFirstLevelTileBitmask(tile);
        }
        int i2 = i - 2;
        long j = (long) (tile.tileX >>> i2);
        long j2 = (long) (tile.tileY >>> i2);
        long j3 = j2 >>> 1;
        long j4 = (j >>> 1) % 2;
        int i3 = (j4 > 0 ? 1 : (j4 == 0 ? 0 : -1));
        if (i3 == 0 && j3 % 2 == 0) {
            return getSecondLevelTileBitmaskUpperLeft(j, j2);
        }
        if (j4 == 1 && j3 % 2 == 0) {
            return getSecondLevelTileBitmaskUpperRight(j, j2);
        }
        if (i3 == 0 && j3 % 2 == 1) {
            return getSecondLevelTileBitmaskLowerLeft(j, j2);
        }
        return getSecondLevelTileBitmaskLowerRight(j, j2);
    }

    static int calculateTileBitmask(Tile tile, Tile tile2, int i) {
        int i2 = 0;
        for (int i3 = tile.tileX; i3 <= tile2.tileX; i3++) {
            for (int i4 = tile.tileY; i4 <= tile2.tileY; i4++) {
                i2 |= calculateTileBitmask(new Tile(i3, i4, tile.zoomLevel, tile.tileSize), i);
            }
        }
        return i2;
    }

    private static int getFirstLevelTileBitmask(Tile tile) {
        if (tile.tileX % 2 == 0 && tile.tileY % 2 == 0) {
            return 52224;
        }
        if (tile.tileX % 2 == 1 && tile.tileY % 2 == 0) {
            return 13056;
        }
        return (tile.tileX % 2 == 0 && tile.tileY % 2 == 1) ? 204 : 51;
    }

    private static int getSecondLevelTileBitmaskLowerLeft(long j, long j2) {
        long j3 = j % 2;
        int i = (j3 > 0 ? 1 : (j3 == 0 ? 0 : -1));
        if (i == 0 && j2 % 2 == 0) {
            return 128;
        }
        if (j3 == 1 && j2 % 2 == 0) {
            return 64;
        }
        return (i == 0 && j2 % 2 == 1) ? 8 : 4;
    }

    private static int getSecondLevelTileBitmaskLowerRight(long j, long j2) {
        long j3 = j % 2;
        int i = (j3 > 0 ? 1 : (j3 == 0 ? 0 : -1));
        if (i == 0 && j2 % 2 == 0) {
            return 32;
        }
        if (j3 == 1 && j2 % 2 == 0) {
            return 16;
        }
        return (i == 0 && j2 % 2 == 1) ? 2 : 1;
    }

    private static int getSecondLevelTileBitmaskUpperLeft(long j, long j2) {
        long j3 = j % 2;
        int i = (j3 > 0 ? 1 : (j3 == 0 ? 0 : -1));
        if (i == 0 && j2 % 2 == 0) {
            return 32768;
        }
        if (j3 == 1 && j2 % 2 == 0) {
            return 16384;
        }
        return (i == 0 && j2 % 2 == 1) ? 2048 : 1024;
    }

    private static int getSecondLevelTileBitmaskUpperRight(long j, long j2) {
        long j3 = j % 2;
        int i = (j3 > 0 ? 1 : (j3 == 0 ? 0 : -1));
        if (i == 0 && j2 % 2 == 0) {
            return 8192;
        }
        if (j3 == 1 && j2 % 2 == 0) {
            return 4096;
        }
        return (i == 0 && j2 % 2 == 1) ? 512 : 256;
    }

    private QueryCalculations() {
        throw new IllegalStateException();
    }
}
