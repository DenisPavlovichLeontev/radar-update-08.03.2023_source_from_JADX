package org.osmdroid.util;

public class MapTileList implements MapTileContainer {
    private int mSize;
    private long[] mTileIndices;

    public void clear() {
        this.mSize = 0;
    }

    public int getSize() {
        return this.mSize;
    }

    public long get(int i) {
        return this.mTileIndices[i];
    }

    public void put(long j) {
        ensureCapacity(this.mSize + 1);
        long[] jArr = this.mTileIndices;
        int i = this.mSize;
        this.mSize = i + 1;
        jArr[i] = j;
    }

    public void put(int i, int i2, int i3, int i4, int i5) {
        int i6 = 1 << i;
        int i7 = (i4 - i2) + 1 + (i4 < i2 ? i6 : 0);
        int i8 = (i5 - i3) + 1 + (i5 < i3 ? i6 : 0);
        ensureCapacity(getSize() + (i7 * i8));
        for (int i9 = 0; i9 < i7; i9++) {
            for (int i10 = 0; i10 < i8; i10++) {
                put(MapTileIndex.getTileIndex(i, (i2 + i9) % i6, (i3 + i10) % i6));
            }
        }
    }

    public void put(int i) {
        int i2 = (1 << i) - 1;
        put(i, 0, 0, i2, i2);
    }

    public void ensureCapacity(int i) {
        if (i != 0) {
            long[] jArr = this.mTileIndices;
            if (jArr == null || jArr.length < i) {
                synchronized (this) {
                    long[] jArr2 = new long[i];
                    long[] jArr3 = this.mTileIndices;
                    if (jArr3 != null) {
                        System.arraycopy(jArr3, 0, jArr2, 0, jArr3.length);
                    }
                    this.mTileIndices = jArr2;
                }
            }
        }
    }

    public boolean contains(long j) {
        if (this.mTileIndices == null) {
            return false;
        }
        for (int i = 0; i < this.mSize; i++) {
            if (this.mTileIndices[i] == j) {
                return true;
            }
        }
        return false;
    }

    public long[] toArray() {
        int i = this.mSize;
        long[] jArr = new long[i];
        long[] jArr2 = this.mTileIndices;
        if (jArr2 != null) {
            System.arraycopy(jArr2, 0, jArr, 0, i);
        }
        return jArr;
    }
}
