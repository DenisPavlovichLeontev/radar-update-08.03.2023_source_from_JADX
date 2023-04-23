package org.mapsforge.map.layer.renderer;

import org.mapsforge.core.model.Point;

class RendererUtils {
    static Point[] parallelPath(Point[] pointArr, double d) {
        Point[] pointArr2 = pointArr;
        int length = pointArr2.length - 1;
        Point[] pointArr3 = new Point[length];
        Point[] pointArr4 = new Point[pointArr2.length];
        int i = 0;
        while (i < length) {
            int i2 = i + 1;
            double d2 = pointArr2[i2].f381x - pointArr2[i].f381x;
            double d3 = pointArr2[i2].f382y - pointArr2[i].f382y;
            double sqrt = Math.sqrt((d2 * d2) + (d3 * d3));
            if (sqrt == 0.0d) {
                pointArr3[i] = new Point(0.0d, 0.0d);
            } else {
                pointArr3[i] = new Point(d2 / sqrt, d3 / sqrt);
            }
            i = i2;
        }
        pointArr4[0] = new Point(pointArr2[0].f381x - (pointArr3[0].f382y * d), pointArr2[0].f382y + (pointArr3[0].f381x * d));
        for (int i3 = 1; i3 < length; i3++) {
            int i4 = i3 - 1;
            double d4 = d / (((pointArr3[i3].f381x * pointArr3[i4].f381x) + 1.0d) + (pointArr3[i3].f382y * pointArr3[i4].f382y));
            pointArr4[i3] = new Point(pointArr2[i3].f381x - ((pointArr3[i3].f382y + pointArr3[i4].f382y) * d4), pointArr2[i3].f382y + (d4 * (pointArr3[i3].f381x + pointArr3[i4].f381x)));
        }
        int i5 = length - 1;
        pointArr4[length] = new Point(pointArr2[length].f381x - (pointArr3[i5].f382y * d), pointArr2[length].f382y + (pointArr3[i5].f381x * d));
        return pointArr4;
    }

    private RendererUtils() {
        throw new IllegalStateException();
    }
}
