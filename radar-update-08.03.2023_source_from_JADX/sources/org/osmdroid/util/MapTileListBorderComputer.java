package org.osmdroid.util;

@Deprecated
public class MapTileListBorderComputer implements MapTileListComputer {
    private final int mBorder;
    private final boolean mIncludeAll;

    public MapTileListBorderComputer(int i, boolean z) {
        this.mBorder = i;
        this.mIncludeAll = z;
    }

    public int getBorder() {
        return this.mBorder;
    }

    public boolean isIncludeAll() {
        return this.mIncludeAll;
    }

    public MapTileList computeFromSource(MapTileList mapTileList, MapTileList mapTileList2) {
        if (mapTileList2 == null) {
            mapTileList2 = new MapTileList();
        }
        for (int i = 0; i < mapTileList.getSize(); i++) {
            long j = mapTileList.get(i);
            int zoom = MapTileIndex.getZoom(j);
            int x = MapTileIndex.getX(j);
            int y = MapTileIndex.getY(j);
            int i2 = 1 << zoom;
            int i3 = -this.mBorder;
            while (true) {
                int i4 = this.mBorder;
                if (i3 > i4) {
                    break;
                }
                for (int i5 = -i4; i5 <= this.mBorder; i5++) {
                    int i6 = x + i3;
                    int i7 = y + i5;
                    while (i6 < 0) {
                        i6 += i2;
                    }
                    while (i7 < 0) {
                        i7 += i2;
                    }
                    while (i6 >= i2) {
                        i6 -= i2;
                    }
                    while (i7 >= i2) {
                        i7 -= i2;
                    }
                    long tileIndex = MapTileIndex.getTileIndex(zoom, i6, i7);
                    if (!mapTileList2.contains(tileIndex) && (!mapTileList.contains(tileIndex) || this.mIncludeAll)) {
                        mapTileList2.put(tileIndex);
                    }
                }
                i3++;
            }
        }
        return mapTileList2;
    }
}
