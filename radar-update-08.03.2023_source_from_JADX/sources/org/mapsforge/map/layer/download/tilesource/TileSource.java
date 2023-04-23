package org.mapsforge.map.layer.download.tilesource;

import java.net.MalformedURLException;
import java.net.URL;
import org.mapsforge.core.model.Tile;

public interface TileSource {
    String getAuthorization();

    long getDefaultTimeToLive();

    int getParallelRequestsLimit();

    String getReferer();

    URL getTileUrl(Tile tile) throws MalformedURLException;

    int getTimeoutConnect();

    int getTimeoutRead();

    String getUserAgent();

    byte getZoomLevelMax();

    byte getZoomLevelMin();

    boolean hasAlpha();

    boolean isFollowRedirects();
}
