package org.mapsforge.map.layer.download.tilesource;

import java.util.Arrays;
import java.util.Random;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.modules.DatabaseFileArchive;

public abstract class AbstractTileSource implements TileSource {
    private static final int TIMEOUT_CONNECT = 5000;
    private static final int TIMEOUT_READ = 10000;
    protected String apiKey;
    protected String authorization;
    protected long defaultTimeToLive = OpenStreetMapTileProviderConstants.ONE_DAY;
    protected boolean followRedirects = true;
    protected final String[] hostNames;
    protected String keyName = DatabaseFileArchive.COLUMN_KEY;
    protected final int port;
    protected final Random random = new Random();
    protected String referer;
    protected int timeoutConnect = TIMEOUT_CONNECT;
    protected int timeoutRead = TIMEOUT_READ;
    protected String userAgent;

    protected AbstractTileSource(String[] strArr, int i) {
        if (strArr == null || strArr.length == 0) {
            throw new IllegalArgumentException("no host names specified");
        } else if (i < 0 || i > 65535) {
            throw new IllegalArgumentException("invalid port number: " + i);
        } else {
            int length = strArr.length;
            int i2 = 0;
            while (i2 < length) {
                if (!strArr[i2].isEmpty()) {
                    i2++;
                } else {
                    throw new IllegalArgumentException("empty host name in host name list");
                }
            }
            this.hostNames = strArr;
            this.port = i;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractTileSource)) {
            return false;
        }
        AbstractTileSource abstractTileSource = (AbstractTileSource) obj;
        return Arrays.equals(this.hostNames, abstractTileSource.hostNames) && this.port == abstractTileSource.port;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getAuthorization() {
        return this.authorization;
    }

    public long getDefaultTimeToLive() {
        return this.defaultTimeToLive;
    }

    /* access modifiers changed from: protected */
    public String getHostName() {
        String[] strArr = this.hostNames;
        return strArr[this.random.nextInt(strArr.length)];
    }

    public String getKeyName() {
        return this.keyName;
    }

    public String getReferer() {
        return this.referer;
    }

    public int getTimeoutConnect() {
        return this.timeoutConnect;
    }

    public int getTimeoutRead() {
        return this.timeoutRead;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public int hashCode() {
        return ((Arrays.hashCode(this.hostNames) + 31) * 31) + this.port;
    }

    public boolean isFollowRedirects() {
        return this.followRedirects;
    }

    public void setApiKey(String str) {
        this.apiKey = str;
    }

    public void setAuthorization(String str) {
        this.authorization = str;
    }

    public void setFollowRedirects(boolean z) {
        this.followRedirects = z;
    }

    public void setKeyName(String str) {
        this.keyName = str;
    }

    public void setReferer(String str) {
        this.referer = str;
    }

    public void setTimeoutConnect(int i) {
        this.timeoutConnect = i;
    }

    public void setTimeoutRead(int i) {
        this.timeoutRead = i;
    }

    public void setUserAgent(String str) {
        this.userAgent = str;
    }
}
