package org.mapsforge.core.util;

import java.util.Set;

public class WorkingSetCache<K, V> extends LRUCache<K, V> {
    private static final long serialVersionUID = 1;

    public WorkingSetCache(int i) {
        super(i);
    }

    public void setWorkingSet(Set<K> set) {
        synchronized (set) {
            for (K k : set) {
                get(k);
            }
        }
    }
}
