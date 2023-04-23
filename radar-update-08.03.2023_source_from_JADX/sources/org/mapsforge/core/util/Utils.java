package org.mapsforge.core.util;

public final class Utils {
    public static boolean equals(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    private Utils() {
        throw new IllegalStateException();
    }
}
