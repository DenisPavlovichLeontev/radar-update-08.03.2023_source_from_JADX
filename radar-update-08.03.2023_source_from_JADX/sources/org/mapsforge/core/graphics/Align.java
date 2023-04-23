package org.mapsforge.core.graphics;

public enum Align {
    CENTER,
    LEFT,
    RIGHT;

    public Align fromString(String str) {
        if ("center".equals(str)) {
            return CENTER;
        }
        if ("left".equals(str)) {
            return LEFT;
        }
        if ("right".equals(str)) {
            return RIGHT;
        }
        throw new IllegalArgumentException("Invalid value for Align: " + str);
    }
}
