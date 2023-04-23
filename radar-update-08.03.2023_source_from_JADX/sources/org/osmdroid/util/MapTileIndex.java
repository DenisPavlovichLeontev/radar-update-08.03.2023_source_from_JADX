package org.osmdroid.util;

public class MapTileIndex {
    public static int mMaxZoomLevel = 29;
    private static int mModulo = (1 << 29);

    public static long getTileIndex(int i, int i2, int i3) {
        checkValues(i, i2, i3);
        long j = (long) i;
        int i4 = mMaxZoomLevel;
        return (j << (i4 * 2)) + (((long) i2) << i4) + ((long) i3);
    }

    public static int getZoom(long j) {
        return (int) (j >> (mMaxZoomLevel * 2));
    }

    public static int getX(long j) {
        return (int) ((j >> mMaxZoomLevel) % ((long) mModulo));
    }

    public static int getY(long j) {
        return (int) (j % ((long) mModulo));
    }

    public static String toString(int i, int i2, int i3) {
        return "/" + i + "/" + i2 + "/" + i3;
    }

    public static String toString(long j) {
        return toString(getZoom(j), getX(j), getY(j));
    }

    private static void checkValues(int i, int i2, int i3) {
        if (i < 0 || i > mMaxZoomLevel) {
            throwIllegalValue(i, i, "Zoom");
        }
        long j = (long) (1 << i);
        if (i2 < 0 || ((long) i2) >= j) {
            throwIllegalValue(i, i2, "X");
        }
        if (i3 < 0 || ((long) i3) >= j) {
            throwIllegalValue(i, i3, "Y");
        }
    }

    private static void throwIllegalValue(int i, int i2, String str) {
        throw new IllegalArgumentException("MapTileIndex: " + str + " (" + i2 + ") is too big (zoom=" + i + ")");
    }
}
