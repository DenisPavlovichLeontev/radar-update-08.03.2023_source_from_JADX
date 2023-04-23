package org.osmdroid.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListPointL implements Iterable<PointL> {
    private final List<PointL> mList = new ArrayList();
    /* access modifiers changed from: private */
    public int mSize;

    public void clear() {
        this.mSize = 0;
    }

    public int size() {
        return this.mSize;
    }

    public PointL get(int i) {
        return this.mList.get(i);
    }

    public void add(long j, long j2) {
        PointL pointL;
        if (this.mSize >= this.mList.size()) {
            pointL = new PointL();
            this.mList.add(pointL);
        } else {
            pointL = this.mList.get(this.mSize);
        }
        this.mSize++;
        pointL.set(j, j2);
    }

    public Iterator<PointL> iterator() {
        return new Iterator<PointL>() {
            private int mIndex;

            public boolean hasNext() {
                return this.mIndex < ListPointL.this.mSize;
            }

            public PointL next() {
                ListPointL listPointL = ListPointL.this;
                int i = this.mIndex;
                this.mIndex = i + 1;
                return listPointL.get(i);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
