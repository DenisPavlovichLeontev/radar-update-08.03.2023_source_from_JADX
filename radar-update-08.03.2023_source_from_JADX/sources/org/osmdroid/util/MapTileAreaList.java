package org.osmdroid.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapTileAreaList implements MapTileContainer, IterableWithSize<Long> {
    /* access modifiers changed from: private */
    public final List<MapTileArea> mList = new ArrayList();

    public List<MapTileArea> getList() {
        return this.mList;
    }

    public int size() {
        int i = 0;
        for (MapTileArea size : this.mList) {
            i += size.size();
        }
        return i;
    }

    public Iterator<Long> iterator() {
        return new Iterator<Long>() {
            private Iterator<Long> mCurrent;
            private int mIndex;

            public boolean hasNext() {
                Iterator<Long> current = getCurrent();
                return current != null && current.hasNext();
            }

            public Long next() {
                long longValue = getCurrent().next().longValue();
                if (!getCurrent().hasNext()) {
                    this.mCurrent = null;
                }
                return Long.valueOf(longValue);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            private Iterator<Long> getCurrent() {
                Iterator<Long> it = this.mCurrent;
                if (it != null) {
                    return it;
                }
                if (this.mIndex >= MapTileAreaList.this.mList.size()) {
                    return null;
                }
                List access$000 = MapTileAreaList.this.mList;
                int i = this.mIndex;
                this.mIndex = i + 1;
                Iterator<Long> it2 = ((MapTileArea) access$000.get(i)).iterator();
                this.mCurrent = it2;
                return it2;
            }
        };
    }

    public boolean contains(long j) {
        for (MapTileArea contains : this.mList) {
            if (contains.contains(j)) {
                return true;
            }
        }
        return false;
    }
}
