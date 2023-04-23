package org.osmdroid.util;

import android.graphics.Rect;
import java.util.Iterator;

public class MapTileArea implements MapTileContainer, IterableWithSize<Long> {
    private int mHeight;
    /* access modifiers changed from: private */
    public int mLeft;
    /* access modifiers changed from: private */
    public int mMapTileUpperBound;
    /* access modifiers changed from: private */
    public int mTop;
    /* access modifiers changed from: private */
    public int mWidth;
    /* access modifiers changed from: private */
    public int mZoom;

    public MapTileArea set(int i, int i2, int i3, int i4, int i5) {
        this.mZoom = i;
        this.mMapTileUpperBound = 1 << i;
        this.mWidth = computeSize(i2, i4);
        this.mHeight = computeSize(i3, i5);
        this.mLeft = cleanValue(i2);
        this.mTop = cleanValue(i3);
        return this;
    }

    public MapTileArea set(int i, Rect rect) {
        return set(i, rect.left, rect.top, rect.right, rect.bottom);
    }

    public MapTileArea set(MapTileArea mapTileArea) {
        if (mapTileArea.size() == 0) {
            return reset();
        }
        return set(mapTileArea.mZoom, mapTileArea.mLeft, mapTileArea.mTop, mapTileArea.getRight(), mapTileArea.getBottom());
    }

    public MapTileArea reset() {
        this.mWidth = 0;
        return this;
    }

    public int getZoom() {
        return this.mZoom;
    }

    public int getLeft() {
        return this.mLeft;
    }

    public int getTop() {
        return this.mTop;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getRight() {
        return (this.mLeft + this.mWidth) % this.mMapTileUpperBound;
    }

    public int getBottom() {
        return (this.mTop + this.mHeight) % this.mMapTileUpperBound;
    }

    public int size() {
        return this.mWidth * this.mHeight;
    }

    public Iterator<Long> iterator() {
        return new Iterator<Long>() {
            private int mIndex;

            public boolean hasNext() {
                return this.mIndex < MapTileArea.this.size();
            }

            public Long next() {
                if (!hasNext()) {
                    return null;
                }
                int access$000 = MapTileArea.this.mLeft + (this.mIndex % MapTileArea.this.mWidth);
                int access$200 = MapTileArea.this.mTop + (this.mIndex / MapTileArea.this.mWidth);
                this.mIndex++;
                while (access$000 >= MapTileArea.this.mMapTileUpperBound) {
                    access$000 -= MapTileArea.this.mMapTileUpperBound;
                }
                while (access$200 >= MapTileArea.this.mMapTileUpperBound) {
                    access$200 -= MapTileArea.this.mMapTileUpperBound;
                }
                return Long.valueOf(MapTileIndex.getTileIndex(MapTileArea.this.mZoom, access$000, access$200));
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public boolean contains(long j) {
        if (MapTileIndex.getZoom(j) == this.mZoom && contains(MapTileIndex.getX(j), this.mLeft, this.mWidth)) {
            return contains(MapTileIndex.getY(j), this.mTop, this.mHeight);
        }
        return false;
    }

    private boolean contains(int i, int i2, int i3) {
        while (i < i2) {
            i += this.mMapTileUpperBound;
        }
        return i < i2 + i3;
    }

    private int cleanValue(int i) {
        while (i < 0) {
            i += this.mMapTileUpperBound;
        }
        while (true) {
            int i2 = this.mMapTileUpperBound;
            if (i < i2) {
                return i;
            }
            i -= i2;
        }
    }

    private int computeSize(int i, int i2) {
        while (i > i2) {
            i2 += this.mMapTileUpperBound;
        }
        return Math.min(this.mMapTileUpperBound, (i2 - i) + 1);
    }

    public String toString() {
        if (this.mWidth == 0) {
            return "MapTileArea:empty";
        }
        return "MapTileArea:zoom=" + this.mZoom + ",left=" + this.mLeft + ",top=" + this.mTop + ",width=" + this.mWidth + ",height=" + this.mHeight;
    }
}
