package org.osmdroid.tileprovider.tilesource;

import android.graphics.drawable.Drawable;
import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;

public interface ITileSource {
    String getCopyrightNotice();

    Drawable getDrawable(InputStream inputStream) throws BitmapTileSourceBase.LowMemoryException;

    Drawable getDrawable(String str) throws BitmapTileSourceBase.LowMemoryException;

    int getMaximumZoomLevel();

    int getMinimumZoomLevel();

    String getTileRelativeFilenameString(long j);

    int getTileSizePixels();

    String name();

    @Deprecated
    int ordinal();
}
