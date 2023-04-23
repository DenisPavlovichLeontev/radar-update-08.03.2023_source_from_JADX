package org.mapsforge.core.graphics;

public enum FontFamily {
    DEFAULT,
    MONOSPACE,
    SANS_SERIF,
    SERIF;

    public static FontFamily fromString(String str) {
        if ("default".equals(str)) {
            return DEFAULT;
        }
        if ("monospace".equals(str)) {
            return MONOSPACE;
        }
        if ("sans_serif".equals(str)) {
            return SANS_SERIF;
        }
        if ("serif".equals(str)) {
            return SERIF;
        }
        throw new IllegalArgumentException("Invalid value for FontFamily: " + str);
    }
}
