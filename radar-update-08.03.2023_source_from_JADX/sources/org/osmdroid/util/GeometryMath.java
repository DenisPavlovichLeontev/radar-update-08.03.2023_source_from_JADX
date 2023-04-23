package org.osmdroid.util;

import android.graphics.Point;
import android.graphics.Rect;

public class GeometryMath {
    @Deprecated
    public static final double DEG2RAD = 0.017453292519943295d;
    @Deprecated
    public static final double RAD2DEG = 57.29577951308232d;

    public static final Rect getBoundingBoxForRotatatedRectangle(Rect rect, float f, Rect rect2) {
        return getBoundingBoxForRotatatedRectangle(rect, rect.centerX(), rect.centerY(), f, rect2);
    }

    public static final Rect getBoundingBoxForRotatatedRectangle(Rect rect, Point point, float f, Rect rect2) {
        return getBoundingBoxForRotatatedRectangle(rect, point.x, point.y, f, rect2);
    }

    public static final Rect getBoundingBoxForRotatatedRectangle(Rect rect, int i, int i2, float f, Rect rect2) {
        float f2;
        Rect rect3;
        Rect rect4 = rect;
        int i3 = i;
        int i4 = i2;
        if (rect2 == null) {
            rect3 = new Rect();
            f2 = f;
        } else {
            f2 = f;
            rect3 = rect2;
        }
        double d = ((double) f2) * 0.017453292519943295d;
        double sin = Math.sin(d);
        double cos = Math.cos(d);
        double d2 = (double) (rect4.left - i3);
        double d3 = (double) (rect4.top - i4);
        double d4 = (double) i3;
        double d5 = (d4 - (d2 * cos)) + (d3 * sin);
        double d6 = (double) i4;
        double d7 = (d6 - (d2 * sin)) - (d3 * cos);
        double d8 = (double) (rect4.right - i3);
        double d9 = d7;
        double d10 = (double) (rect4.top - i4);
        double d11 = (d4 - (d8 * cos)) + (d10 * sin);
        double d12 = (d6 - (d8 * sin)) - (d10 * cos);
        double d13 = (double) (rect4.left - i3);
        double d14 = d12;
        double d15 = (double) (rect4.bottom - i4);
        double d16 = (d4 - (d13 * cos)) + (d15 * sin);
        double d17 = (d6 - (d13 * sin)) - (d15 * cos);
        double d18 = (double) (rect4.right - i3);
        double d19 = (double) (rect4.bottom - i4);
        double d20 = (d6 - (d18 * sin)) - (d19 * cos);
        double d21 = (d4 - (d18 * cos)) + (d19 * sin);
        rect3.left = MyMath.floorToInt(Min4(d5, d11, d16, d21));
        double d22 = d9;
        rect3.top = MyMath.floorToInt(Min4(d22, d14, d17, d20));
        rect3.right = MyMath.floorToInt(Max4(d5, d11, d16, d21));
        rect3.bottom = MyMath.floorToInt(Max4(d22, d14, d17, d20));
        return rect3;
    }

    private static double Min4(double d, double d2, double d3, double d4) {
        return Math.floor(Math.min(Math.min(d, d2), Math.min(d3, d4)));
    }

    private static double Max4(double d, double d2, double d3, double d4) {
        return Math.ceil(Math.max(Math.max(d, d2), Math.max(d3, d4)));
    }
}
