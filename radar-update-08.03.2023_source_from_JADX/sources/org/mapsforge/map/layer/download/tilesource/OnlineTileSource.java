package org.mapsforge.map.layer.download.tilesource;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.net.MalformedURLException;
import java.net.URL;
import org.mapsforge.core.model.Tile;

public class OnlineTileSource extends AbstractTileSource {
    private boolean alpha = false;
    private String baseUrl = "/";
    private String extension = "png";
    private String name;
    private int parallelRequestsLimit = 8;
    private String protocol = "http";
    private int tileSize = 256;
    private byte zoomLevelMax = 18;
    private byte zoomLevelMin = 0;

    public OnlineTileSource(String[] strArr, int i) {
        super(strArr, i);
    }

    public boolean equals(Object obj) {
        if (super.equals(obj) && (obj instanceof OnlineTileSource) && this.baseUrl.equals(((OnlineTileSource) obj).baseUrl)) {
            return true;
        }
        return false;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public String getExtension() {
        return this.extension;
    }

    public String getName() {
        return this.name;
    }

    public int getParallelRequestsLimit() {
        return this.parallelRequestsLimit;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public int getTileSize() {
        return this.tileSize;
    }

    public URL getTileUrl(Tile tile) throws MalformedURLException {
        StringBuilder sb = new StringBuilder(32);
        sb.append(this.baseUrl);
        sb.append(tile.zoomLevel);
        sb.append('/');
        sb.append(tile.tileX);
        sb.append('/');
        sb.append(tile.tileY);
        sb.append('.');
        sb.append(this.extension);
        if (this.apiKey != null) {
            sb.append('?');
            sb.append(this.keyName);
            sb.append(SimpleComparison.EQUAL_TO_OPERATION);
            sb.append(this.apiKey);
        }
        return new URL(this.protocol, getHostName(), this.port, sb.toString());
    }

    public byte getZoomLevelMax() {
        return this.zoomLevelMax;
    }

    public byte getZoomLevelMin() {
        return this.zoomLevelMin;
    }

    public boolean hasAlpha() {
        return this.alpha;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + this.baseUrl.hashCode();
    }

    public OnlineTileSource setAlpha(boolean z) {
        this.alpha = z;
        return this;
    }

    public OnlineTileSource setBaseUrl(String str) {
        this.baseUrl = str;
        return this;
    }

    public OnlineTileSource setExtension(String str) {
        this.extension = str;
        return this;
    }

    public OnlineTileSource setName(String str) {
        this.name = str;
        return this;
    }

    public OnlineTileSource setParallelRequestsLimit(int i) {
        this.parallelRequestsLimit = i;
        return this;
    }

    public OnlineTileSource setProtocol(String str) {
        this.protocol = str;
        return this;
    }

    public OnlineTileSource setTileSize(int i) {
        this.tileSize = i;
        return this;
    }

    public OnlineTileSource setZoomLevelMax(byte b) {
        this.zoomLevelMax = b;
        return this;
    }

    public OnlineTileSource setZoomLevelMin(byte b) {
        this.zoomLevelMin = b;
        return this;
    }
}
