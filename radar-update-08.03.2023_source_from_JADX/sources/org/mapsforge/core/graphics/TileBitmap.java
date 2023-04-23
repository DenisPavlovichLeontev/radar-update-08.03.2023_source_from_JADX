package org.mapsforge.core.graphics;

public interface TileBitmap extends Bitmap {
    long getTimestamp();

    boolean isExpired();

    void setExpiration(long j);

    void setTimestamp(long j);
}
