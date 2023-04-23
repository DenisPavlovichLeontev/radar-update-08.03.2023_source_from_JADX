package org.mapsforge.map.layer.download.tilesource;

import java.net.MalformedURLException;
import java.net.URL;
import org.mapsforge.core.model.Tile;

public class OpenStreetMapMapnik extends AbstractTileSource {
    public static final OpenStreetMapMapnik INSTANCE = new OpenStreetMapMapnik(new String[]{"a.tile.openstreetmap.org", "b.tile.openstreetmap.org", "c.tile.openstreetmap.org"}, 443);
    private static final int PARALLEL_REQUESTS_LIMIT = 8;
    private static final String PROTOCOL = "https";
    private static final int ZOOM_LEVEL_MAX = 18;
    private static final int ZOOM_LEVEL_MIN = 0;

    public int getParallelRequestsLimit() {
        return 8;
    }

    public byte getZoomLevelMax() {
        return 18;
    }

    public byte getZoomLevelMin() {
        return 0;
    }

    public boolean hasAlpha() {
        return false;
    }

    public OpenStreetMapMapnik(String[] strArr, int i) {
        super(strArr, i);
        this.defaultTimeToLive = 8279000;
    }

    public URL getTileUrl(Tile tile) throws MalformedURLException {
        String hostName = getHostName();
        int i = this.port;
        return new URL(PROTOCOL, hostName, i, "/" + tile.zoomLevel + '/' + tile.tileX + '/' + tile.tileY + ".png");
    }
}
