package org.osmdroid.views.util;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import java.util.Iterator;
import java.util.List;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.PointL;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

@Deprecated
public class PathProjection {
    public static Path toPixels(Projection projection, List<? extends GeoPoint> list, Path path) {
        return toPixels(projection, list, path, true);
    }

    public static Path toPixels(Projection projection, List<? extends GeoPoint> list, Path path, boolean z) throws IllegalArgumentException {
        Path path2;
        PointF pointF;
        Projection projection2 = projection;
        if (list.size() >= 2) {
            if (path != null) {
                path2 = path;
            } else {
                path2 = new Path();
            }
            path2.incReserve(list.size());
            TileSystem tileSystem = MapView.getTileSystem();
            Iterator<? extends GeoPoint> it = list.iterator();
            boolean z2 = true;
            while (it.hasNext()) {
                GeoPoint geoPoint = (GeoPoint) it.next();
                Point point = new Point();
                double MapSize = TileSystem.MapSize(projection.getZoomLevel());
                PointL mercatorFromGeo = tileSystem.getMercatorFromGeo(geoPoint.getLatitude(), geoPoint.getLongitude(), MapSize, (PointL) null, true);
                point.x = projection2.getTileFromMercator(mercatorFromGeo.f559x);
                point.y = projection2.getTileFromMercator(mercatorFromGeo.f560y);
                PointL pointL = new PointL(projection2.getMercatorFromTile(point.x), projection2.getMercatorFromTile(point.y));
                PointL pointL2 = new PointL(projection2.getMercatorFromTile(point.x + TileSystem.getTileSize()), projection2.getMercatorFromTile(point.y + TileSystem.getTileSize()));
                Iterator<? extends GeoPoint> it2 = it;
                Point point2 = point;
                GeoPoint geoFromMercator = tileSystem.getGeoFromMercator(pointL.f559x, pointL.f560y, MapSize, (GeoPoint) null, true, true);
                PointL pointL3 = pointL2;
                GeoPoint geoFromMercator2 = tileSystem.getGeoFromMercator(pointL3.f559x, pointL3.f560y, MapSize, (GeoPoint) null, true, true);
                BoundingBox boundingBox = new BoundingBox(geoFromMercator.getLatitude(), geoFromMercator.getLongitude(), geoFromMercator2.getLatitude(), geoFromMercator2.getLongitude());
                if (!z || projection.getZoomLevel() >= 7.0d) {
                    pointF = boundingBox.mo29187x94d7c798(geoPoint.getLatitude(), geoPoint.getLongitude(), (PointF) null);
                } else {
                    pointF = boundingBox.mo29186x3b33f3a5(geoPoint.getLatitude(), geoPoint.getLongitude(), (PointF) null);
                }
                Rect screenRect = projection.getScreenRect();
                Point point3 = new Point(projection2.getTileFromMercator((long) screenRect.centerX()), projection2.getTileFromMercator((long) screenRect.centerY()));
                PointL pointL4 = new PointL(projection2.getMercatorFromTile(point3.x), projection2.getMercatorFromTile(point3.y));
                int i = point3.x - point2.x;
                int i2 = point3.y - point2.y;
                long tileSize = pointL4.f559x - ((long) (TileSystem.getTileSize() * i));
                long tileSize2 = pointL4.f560y - ((long) (TileSystem.getTileSize() * i2));
                long tileSize3 = tileSize + ((long) (pointF.x * ((float) TileSystem.getTileSize())));
                long tileSize4 = tileSize2 + ((long) (pointF.y * ((float) TileSystem.getTileSize())));
                if (z2) {
                    path2.moveTo((float) tileSize3, (float) tileSize4);
                } else {
                    path2.lineTo((float) tileSize3, (float) tileSize4);
                }
                z2 = false;
                it = it2;
            }
            return path2;
        }
        throw new IllegalArgumentException("List of GeoPoints needs to be at least 2.");
    }
}
