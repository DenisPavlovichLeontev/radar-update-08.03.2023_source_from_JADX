package org.mapsforge.map.layer;

import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;

public class TilePosition {
    public final Point point;
    public final Tile tile;

    public TilePosition(Tile tile2, Point point2) {
        this.tile = tile2;
        this.point = point2;
    }
}
