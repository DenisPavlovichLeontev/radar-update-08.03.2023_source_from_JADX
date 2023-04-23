package org.mapsforge.core.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final float LOAD_FACTOR = 0.6f;
    private static final long serialVersionUID = 1;
    public final int capacity;

    private static int calculateInitialCapacity(int i) {
        if (i >= 0) {
            return ((int) (((float) i) / LOAD_FACTOR)) + 2;
        }
        throw new IllegalArgumentException("capacity must not be negative: " + i);
    }

    public LRUCache(int i) {
        super(calculateInitialCapacity(i), LOAD_FACTOR, true);
        this.capacity = i;
    }

    /* access modifiers changed from: protected */
    public boolean removeEldestEntry(Map.Entry<K, V> entry) {
        return size() > this.capacity;
    }
}
