package org.mapsforge.map.layer.cache;

import java.util.Map;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.util.WorkingSetCache;
import org.mapsforge.map.layer.queue.Job;

/* compiled from: InMemoryTileCache */
class BitmapLRUCache extends WorkingSetCache<Job, TileBitmap> {
    private static final long serialVersionUID = 1;

    public BitmapLRUCache(int i) {
        super(i);
    }

    /* access modifiers changed from: protected */
    public boolean removeEldestEntry(Map.Entry<Job, TileBitmap> entry) {
        if (size() <= this.capacity) {
            return false;
        }
        TileBitmap value = entry.getValue();
        if (value == null) {
            return true;
        }
        value.decrementRefCount();
        return true;
    }
}
