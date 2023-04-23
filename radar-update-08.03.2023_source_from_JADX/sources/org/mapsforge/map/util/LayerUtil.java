package org.mapsforge.map.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.mapsforge.core.mapelements.MapElementContainer;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.layer.TilePosition;

public final class LayerUtil {
    public static List<TilePosition> getTilePositions(BoundingBox boundingBox, byte b, Point point, int i) {
        int longitudeToTileX = MercatorProjection.longitudeToTileX(boundingBox.minLongitude, b);
        int latitudeToTileY = MercatorProjection.latitudeToTileY(boundingBox.maxLatitude, b);
        int longitudeToTileX2 = MercatorProjection.longitudeToTileX(boundingBox.maxLongitude, b);
        int latitudeToTileY2 = MercatorProjection.latitudeToTileY(boundingBox.minLatitude, b);
        ArrayList arrayList = new ArrayList(((longitudeToTileX2 - longitudeToTileX) + 1) * ((latitudeToTileY2 - latitudeToTileY) + 1));
        while (latitudeToTileY <= latitudeToTileY2) {
            for (int i2 = longitudeToTileX; i2 <= longitudeToTileX2; i2++) {
                arrayList.add(new TilePosition(new Tile(i2, latitudeToTileY, b, i), new Point(((double) MercatorProjection.tileToPixel((long) i2, i)) - point.f381x, ((double) MercatorProjection.tileToPixel((long) latitudeToTileY, i)) - point.f382y)));
            }
            latitudeToTileY++;
        }
        return arrayList;
    }

    public static Tile getUpperLeft(BoundingBox boundingBox, byte b, int i) {
        return new Tile(MercatorProjection.longitudeToTileX(boundingBox.minLongitude, b), MercatorProjection.latitudeToTileY(boundingBox.maxLatitude, b), b, i);
    }

    public static Tile getLowerRight(BoundingBox boundingBox, byte b, int i) {
        return new Tile(MercatorProjection.longitudeToTileX(boundingBox.maxLongitude, b), MercatorProjection.latitudeToTileY(boundingBox.minLatitude, b), b, i);
    }

    public static Set<Tile> getTiles(Tile tile, Tile tile2) {
        HashSet hashSet = new HashSet();
        for (int i = tile.tileY; i <= tile2.tileY; i++) {
            for (int i2 = tile.tileX; i2 <= tile2.tileX; i2++) {
                hashSet.add(new Tile(i2, i, tile.zoomLevel, tile.tileSize));
            }
        }
        return hashSet;
    }

    public static Set<Tile> getTiles(BoundingBox boundingBox, byte b, int i) {
        int longitudeToTileX = MercatorProjection.longitudeToTileX(boundingBox.minLongitude, b);
        int longitudeToTileX2 = MercatorProjection.longitudeToTileX(boundingBox.maxLongitude, b);
        int latitudeToTileY = MercatorProjection.latitudeToTileY(boundingBox.minLatitude, b);
        HashSet hashSet = new HashSet();
        for (int latitudeToTileY2 = MercatorProjection.latitudeToTileY(boundingBox.maxLatitude, b); latitudeToTileY2 <= latitudeToTileY; latitudeToTileY2++) {
            for (int i2 = longitudeToTileX; i2 <= longitudeToTileX2; i2++) {
                hashSet.add(new Tile(i2, latitudeToTileY2, b, i));
            }
        }
        return hashSet;
    }

    public static List<MapElementContainer> collisionFreeOrdered(List<MapElementContainer> list) {
        Collections.sort(list, Collections.reverseOrder());
        LinkedList linkedList = new LinkedList();
        for (MapElementContainer next : list) {
            boolean z = true;
            Iterator it = linkedList.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (((MapElementContainer) it.next()).clashesWith(next)) {
                        z = false;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (z) {
                linkedList.add(next);
            }
        }
        return linkedList;
    }

    private LayerUtil() {
        throw new IllegalStateException();
    }
}
