package org.osmdroid.tileprovider.tilesource;

import org.osmdroid.util.MapTileIndex;

public abstract class TMSOnlineTileSourceBase extends OnlineTileSourceBase {
    public TMSOnlineTileSourceBase(String str, int i, int i2, int i3, String str2, String[] strArr) {
        super(str, i, i2, i3, str2, strArr);
    }

    public String getTileRelativeFilenameString(long j) {
        return pathBase() + '/' + MapTileIndex.getZoom(j) + '/' + MapTileIndex.getX(j) + '/' + (((1 << MapTileIndex.getZoom(j)) - MapTileIndex.getY(j)) - 1) + imageFilenameEnding();
    }
}
