package org.mapsforge.map.util;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public final class MapPositionUtil {
    public static BoundingBox getBoundingBox(MapPosition mapPosition, Dimension dimension, int i) {
        MapPosition mapPosition2 = mapPosition;
        Dimension dimension2 = dimension;
        long mapSize = MercatorProjection.getMapSize(mapPosition2.zoomLevel, i);
        double longitudeToPixelX = MercatorProjection.longitudeToPixelX(mapPosition2.latLong.longitude, mapSize);
        double latitudeToPixelY = MercatorProjection.latitudeToPixelY(mapPosition2.latLong.latitude, mapSize);
        double d = (double) (dimension2.width / 2);
        double max = Math.max(0.0d, longitudeToPixelX - d);
        double d2 = (double) (dimension2.height / 2);
        double d3 = (double) mapSize;
        return new BoundingBox(MercatorProjection.pixelYToLatitude(Math.min(d3, latitudeToPixelY + d2), mapSize), MercatorProjection.pixelXToLongitude(max, mapSize), MercatorProjection.pixelYToLatitude(Math.max(0.0d, latitudeToPixelY - d2), mapSize), MercatorProjection.pixelXToLongitude(Math.min(d3, longitudeToPixelX + d), mapSize));
    }

    public static Point getTopLeftPoint(MapPosition mapPosition, Dimension dimension, int i) {
        LatLong latLong = mapPosition.latLong;
        long mapSize = MercatorProjection.getMapSize(mapPosition.zoomLevel, i);
        return new Point((double) (((long) ((double) Math.round(MercatorProjection.longitudeToPixelX(latLong.longitude, mapSize)))) - ((long) (dimension.width / 2))), (double) (((long) ((double) Math.round(MercatorProjection.latitudeToPixelY(latLong.latitude, mapSize)))) - ((long) (dimension.height / 2))));
    }

    private MapPositionUtil() {
        throw new IllegalStateException();
    }
}
