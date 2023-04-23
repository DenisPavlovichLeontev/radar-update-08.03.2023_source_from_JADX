package org.mapsforge.map.model;

public class FixedTileSizeDisplayModel extends DisplayModel {
    private final int tileSize;

    public FixedTileSizeDisplayModel(int i) {
        this.tileSize = i;
    }

    public int getTileSize() {
        return this.tileSize;
    }
}
