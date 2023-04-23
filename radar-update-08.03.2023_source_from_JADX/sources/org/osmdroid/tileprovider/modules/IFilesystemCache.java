package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;

public interface IFilesystemCache {
    boolean exists(ITileSource iTileSource, long j);

    Long getExpirationTimestamp(ITileSource iTileSource, long j);

    Drawable loadTile(ITileSource iTileSource, long j) throws Exception;

    void onDetach();

    boolean remove(ITileSource iTileSource, long j);

    boolean saveFile(ITileSource iTileSource, long j, InputStream inputStream, Long l);
}
