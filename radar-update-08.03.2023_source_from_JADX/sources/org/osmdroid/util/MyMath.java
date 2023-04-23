package org.osmdroid.util;

import org.osmdroid.views.util.constants.MathConstants;

public class MyMath implements MathConstants {
    public static double cleanPositiveAngle(double d) {
        while (d < 0.0d) {
            d += 360.0d;
        }
        while (d >= 360.0d) {
            d -= 360.0d;
        }
        return d;
    }

    public static int floorToInt(double d) {
        int i = (int) d;
        return ((double) i) <= d ? i : i - 1;
    }

    public static long floorToLong(double d) {
        long j = (long) d;
        return ((double) j) <= d ? j : j - 1;
    }

    public static int getNextSquareNumberAbove(float f) {
        int i = 1;
        int i2 = 0;
        int i3 = 1;
        while (((float) i) <= f) {
            i *= 2;
            i2 = i3;
            i3++;
        }
        return i2;
    }

    private MyMath() {
    }

    public static double gudermannInverse(double d) {
        return Math.log(Math.tan(((d * 0.017453292519943295d) / 2.0d) + 0.7853981633974483d));
    }

    public static double gudermann(double d) {
        return Math.atan(Math.sinh(d)) * 57.29577951308232d;
    }

    public static int mod(int i, int i2) {
        if (i > 0) {
            return i % i2;
        }
        while (i < 0) {
            i += i2;
        }
        return i;
    }

    public static double getAngleDifference(double d, double d2, Boolean bool) {
        double cleanPositiveAngle = cleanPositiveAngle(d2 - d);
        return bool != null ? bool.booleanValue() ? cleanPositiveAngle : cleanPositiveAngle - 360.0d : cleanPositiveAngle < 180.0d ? cleanPositiveAngle : cleanPositiveAngle - 360.0d;
    }

    public static double computeAngle(long j, long j2, long j3, long j4) {
        return Math.atan2((double) (j4 - j2), (double) (j3 - j));
    }

    public static void computeCirclePoint(long j, long j2, double d, double d2, PointL pointL) {
        pointL.f559x = j + ((long) (Math.cos(d2) * d));
        pointL.f560y = j2 + ((long) (d * Math.sin(d2)));
    }
}
