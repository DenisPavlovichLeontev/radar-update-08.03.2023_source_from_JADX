package org.mapsforge.core.graphics;

public enum Cap {
    BUTT,
    ROUND,
    SQUARE;

    public static Cap fromString(String str) {
        if ("butt".equals(str)) {
            return BUTT;
        }
        if ("round".equals(str)) {
            return ROUND;
        }
        if ("square".equals(str)) {
            return SQUARE;
        }
        throw new IllegalArgumentException("Invalid value for Align: " + str);
    }
}
