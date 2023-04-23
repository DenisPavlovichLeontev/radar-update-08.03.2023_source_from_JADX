package org.osmdroid.views.overlay;

public class OverlayLayoutParams {
    public static final int BOTTOM = 16;
    public static final int CENTER_HORIZONTAL = 4;
    public static final int CENTER_VERTICAL = 32;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 8;

    public static int getMaskedValue(int i, int i2, int[] iArr) {
        for (int i3 : iArr) {
            if ((i & i3) == i3) {
                return i3;
            }
        }
        return i2;
    }
}
