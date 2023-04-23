package org.osmdroid.util;

@Deprecated
public class MapTileListZoomComputer implements MapTileListComputer {
    private final int mZoomDelta;

    public MapTileListZoomComputer(int i) {
        this.mZoomDelta = i;
    }

    public int getZoomDelta() {
        return this.mZoomDelta;
    }

    public MapTileList computeFromSource(MapTileList mapTileList, MapTileList mapTileList2) {
        if (mapTileList2 == null) {
            mapTileList2 = new MapTileList();
        }
        for (int i = 0; i < mapTileList.getSize(); i++) {
            long j = mapTileList.get(i);
            int zoom = MapTileIndex.getZoom(j) + this.mZoomDelta;
            if (zoom >= 0 && zoom <= MapTileIndex.mMaxZoomLevel) {
                int x = MapTileIndex.getX(j);
                int y = MapTileIndex.getY(j);
                int i2 = this.mZoomDelta;
                if (i2 <= 0) {
                    mapTileList2.put(MapTileIndex.getTileIndex(zoom, x >> (-i2), y >> (-i2)));
                } else {
                    int i3 = 1 << i2;
                    int i4 = x << i2;
                    int i5 = y << i2;
                    for (int i6 = 0; i6 < i3; i6++) {
                        for (int i7 = 0; i7 < i3; i7++) {
                            mapTileList2.put(MapTileIndex.getTileIndex(zoom, i4 + i6, i5 + i7));
                        }
                    }
                }
            }
        }
        return mapTileList2;
    }
}
