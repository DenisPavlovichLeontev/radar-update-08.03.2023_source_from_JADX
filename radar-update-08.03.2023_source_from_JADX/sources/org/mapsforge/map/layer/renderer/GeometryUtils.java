package org.mapsforge.map.layer.renderer;

import org.mapsforge.core.model.Point;

final class GeometryUtils {
    static Point calculateCenterOfBoundingBox(Point[] pointArr) {
        double d = pointArr[0].f381x;
        double d2 = pointArr[0].f381x;
        double d3 = pointArr[0].f382y;
        double d4 = pointArr[0].f382y;
        for (Point point : pointArr) {
            if (point.f381x < d) {
                d = point.f381x;
            } else if (point.f381x > d2) {
                d2 = point.f381x;
            }
            if (point.f382y < d3) {
                d3 = point.f382y;
            } else if (point.f382y > d4) {
                d4 = point.f382y;
            }
        }
        return new Point((d + d2) / 2.0d, (d4 + d3) / 2.0d);
    }

    private GeometryUtils() {
        throw new IllegalStateException();
    }
}
