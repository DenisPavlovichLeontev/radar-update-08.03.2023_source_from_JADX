package org.mapsforge.core.graphics;

public enum Display {
    NEVER,
    ALWAYS,
    IFSPACE;

    public static Display fromString(String str) {
        if ("never".equals(str)) {
            return NEVER;
        }
        if ("always".equals(str)) {
            return ALWAYS;
        }
        if ("ifspace".equals(str)) {
            return IFSPACE;
        }
        throw new IllegalArgumentException("Invalid value for Display: " + str);
    }
}
