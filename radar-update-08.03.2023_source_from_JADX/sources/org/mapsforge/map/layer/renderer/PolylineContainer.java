package org.mapsforge.map.layer.renderer;

import java.util.List;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tag;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.datastore.Way;

public class PolylineContainer implements ShapeContainer {
    private Point center;
    private Point[][] coordinatesAbsolute;
    private Point[][] coordinatesRelativeToTile;
    private final boolean isClosedWay;
    private final byte layer;
    private final Tile lowerRight;
    private final List<Tag> tags;
    private final Tile upperLeft;
    private Way way;

    public PolylineContainer(Way way2, Tile tile, Tile tile2) {
        Point[][] pointArr = null;
        this.coordinatesAbsolute = null;
        this.coordinatesRelativeToTile = null;
        this.tags = way2.tags;
        this.upperLeft = tile;
        this.lowerRight = tile2;
        this.layer = way2.layer;
        this.way = way2;
        this.isClosedWay = LatLongUtils.isClosedWay(way2.latLongs[0]);
        if (this.way.labelPosition != null) {
            this.center = MercatorProjection.getPixelAbsolute(this.way.labelPosition, tile.mapSize);
        }
    }

    public PolylineContainer(Point[] pointArr, Tile tile, Tile tile2, List<Tag> list) {
        Point[][] pointArr2 = new Point[1][];
        this.coordinatesAbsolute = pointArr2;
        Point[][] pointArr3 = null;
        this.coordinatesRelativeToTile = null;
        Point[] pointArr4 = new Point[pointArr.length];
        pointArr2[0] = pointArr4;
        System.arraycopy(pointArr, 0, pointArr4, 0, pointArr.length);
        this.tags = list;
        this.upperLeft = tile;
        this.lowerRight = tile2;
        this.layer = 0;
        this.isClosedWay = pointArr[0].equals(pointArr[pointArr.length - 1]);
    }

    public Point getCenterAbsolute() {
        if (this.center == null) {
            this.center = GeometryUtils.calculateCenterOfBoundingBox(getCoordinatesAbsolute()[0]);
        }
        return this.center;
    }

    public Point[][] getCoordinatesAbsolute() {
        if (this.coordinatesAbsolute == null) {
            this.coordinatesAbsolute = new Point[this.way.latLongs.length][];
            for (int i = 0; i < this.way.latLongs.length; i++) {
                this.coordinatesAbsolute[i] = new Point[this.way.latLongs[i].length];
                for (int i2 = 0; i2 < this.way.latLongs[i].length; i2++) {
                    this.coordinatesAbsolute[i][i2] = MercatorProjection.getPixelAbsolute(this.way.latLongs[i][i2], this.upperLeft.mapSize);
                }
            }
            this.way = null;
        }
        return this.coordinatesAbsolute;
    }

    public Point[][] getCoordinatesRelativeToOrigin() {
        if (this.coordinatesRelativeToTile == null) {
            Point origin = this.upperLeft.getOrigin();
            this.coordinatesRelativeToTile = new Point[getCoordinatesAbsolute().length][];
            int i = 0;
            while (true) {
                Point[][] pointArr = this.coordinatesRelativeToTile;
                if (i >= pointArr.length) {
                    break;
                }
                pointArr[i] = new Point[this.coordinatesAbsolute[i].length];
                int i2 = 0;
                while (true) {
                    Point[] pointArr2 = this.coordinatesRelativeToTile[i];
                    if (i2 >= pointArr2.length) {
                        break;
                    }
                    pointArr2[i2] = this.coordinatesAbsolute[i][i2].offset(-origin.f381x, -origin.f382y);
                    i2++;
                }
                i++;
            }
        }
        return this.coordinatesRelativeToTile;
    }

    public byte getLayer() {
        return this.layer;
    }

    public ShapeType getShapeType() {
        return ShapeType.POLYLINE;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public boolean isClosedWay() {
        return this.isClosedWay;
    }

    public Tile getUpperLeft() {
        return this.upperLeft;
    }

    public Tile getLowerRight() {
        return this.lowerRight;
    }
}
