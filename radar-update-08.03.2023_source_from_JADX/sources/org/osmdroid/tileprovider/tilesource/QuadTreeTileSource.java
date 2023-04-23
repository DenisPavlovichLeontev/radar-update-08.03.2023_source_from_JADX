package org.osmdroid.tileprovider.tilesource;

import org.osmdroid.util.MapTileIndex;

public class QuadTreeTileSource extends OnlineTileSourceBase {
    public QuadTreeTileSource(String str, int i, int i2, int i3, String str2, String[] strArr) {
        super(str, i, i2, i3, str2, strArr);
    }

    public String getTileURLString(long j) {
        return getBaseUrl() + quadTree(j) + this.mImageFilenameEnding;
    }

    /* access modifiers changed from: protected */
    public String quadTree(long j) {
        StringBuilder sb = new StringBuilder();
        for (int zoom = MapTileIndex.getZoom(j); zoom > 0; zoom += -1) {
            int i = 0;
            int i2 = 1 << (zoom - 1);
            if ((MapTileIndex.getX(j) & i2) != 0) {
                i = 1;
            }
            if ((i2 & MapTileIndex.getY(j)) != 0) {
                i += 2;
            }
            sb.append("" + i);
        }
        return sb.toString();
    }
}
