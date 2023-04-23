package org.mapsforge.map.rendertheme.rule;

enum Element {
    ANY,
    NODE,
    WAY;

    static Element fromString(String str) {
        if ("any".equals(str)) {
            return ANY;
        }
        if ("node".equals(str)) {
            return NODE;
        }
        if ("way".equals(str)) {
            return WAY;
        }
        throw new IllegalArgumentException("Invalid value for Element: " + str);
    }
}
