package org.mapsforge.core.graphics;

public enum FontStyle {
    BOLD,
    BOLD_ITALIC,
    ITALIC,
    NORMAL;

    public static FontStyle fromString(String str) {
        if ("bold".equals(str)) {
            return BOLD;
        }
        if ("bold_italic".equals(str)) {
            return BOLD_ITALIC;
        }
        if ("italic".equals(str)) {
            return ITALIC;
        }
        if ("normal".equals(str)) {
            return NORMAL;
        }
        throw new IllegalArgumentException("Invalid value for FontStyle: " + str);
    }
}
