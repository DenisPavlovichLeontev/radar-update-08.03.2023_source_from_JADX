package org.osmdroid.tileprovider.tilesource;

import android.util.Log;
import com.j256.ormlite.stmt.query.SimpleComparison;
import java.net.HttpURLConnection;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.config.DefaultConfigurationProvider;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;

public class TileSourcePolicy {
    public static final int FLAG_NO_BULK = 1;
    public static final int FLAG_NO_PREVENTIVE = 2;
    public static final int FLAG_USER_AGENT_MEANINGFUL = 4;
    public static final int FLAG_USER_AGENT_NORMALIZED = 8;
    private final int mFlags;
    private final int mMaxConcurrent;

    public TileSourcePolicy() {
        this(0, 0);
    }

    public TileSourcePolicy(int i, int i2) {
        this.mMaxConcurrent = i;
        this.mFlags = i2;
    }

    public int getMaxConcurrent() {
        return this.mMaxConcurrent;
    }

    public boolean acceptsBulkDownload() {
        return (this.mFlags & 1) == 0;
    }

    private boolean acceptsMeaninglessUserAgent() {
        return (this.mFlags & 4) == 0;
    }

    public boolean normalizesUserAgent() {
        return (this.mFlags & 8) != 0;
    }

    public boolean acceptsPreventive() {
        return (this.mFlags & 2) == 0;
    }

    public boolean acceptsUserAgent(String str) {
        if (acceptsMeaninglessUserAgent()) {
            return true;
        }
        if (str == null || str.trim().length() <= 0 || str.equals(DefaultConfigurationProvider.DEFAULT_USER_AGENT)) {
            return false;
        }
        return true;
    }

    public Long getHttpExpiresTime(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        try {
            return Long.valueOf(Configuration.getInstance().getHttpHeaderDateTimeFormat().parse(str).getTime());
        } catch (Exception e) {
            if (!Configuration.getInstance().isDebugMapTileDownloader()) {
                return null;
            }
            Log.d(IMapView.LOGTAG, "Unable to parse expiration tag for tile, server returned " + str, e);
            return null;
        }
    }

    public Long getHttpCacheControlDuration(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        try {
            for (String str2 : str.split(", ")) {
                if (str2.indexOf("max-age=") == 0) {
                    return Long.valueOf(str2.substring(8));
                }
            }
            return null;
        } catch (Exception e) {
            if (!Configuration.getInstance().isDebugMapTileDownloader()) {
                return null;
            }
            Log.d(IMapView.LOGTAG, "Unable to parse cache control tag for tile, server returned " + str, e);
            return null;
        }
    }

    public long computeExpirationTime(String str, String str2, long j) {
        Long expirationOverrideDuration = Configuration.getInstance().getExpirationOverrideDuration();
        if (expirationOverrideDuration != null) {
            return j + expirationOverrideDuration.longValue();
        }
        long expirationExtendedDuration = Configuration.getInstance().getExpirationExtendedDuration();
        Long httpCacheControlDuration = getHttpCacheControlDuration(str2);
        if (httpCacheControlDuration != null) {
            return j + (httpCacheControlDuration.longValue() * 1000) + expirationExtendedDuration;
        }
        Long httpExpiresTime = getHttpExpiresTime(str);
        return httpExpiresTime != null ? httpExpiresTime.longValue() + expirationExtendedDuration : j + 604800000 + expirationExtendedDuration;
    }

    public long computeExpirationTime(HttpURLConnection httpURLConnection, long j) {
        String headerField = httpURLConnection.getHeaderField(OpenStreetMapTileProviderConstants.HTTP_EXPIRES_HEADER);
        String headerField2 = httpURLConnection.getHeaderField(OpenStreetMapTileProviderConstants.HTTP_CACHECONTROL_HEADER);
        long computeExpirationTime = computeExpirationTime(headerField, headerField2, j);
        if (Configuration.getInstance().isDebugMapTileDownloader()) {
            Log.d(IMapView.LOGTAG, "computeExpirationTime('" + headerField + "','" + headerField2 + "'," + j + SimpleComparison.EQUAL_TO_OPERATION + computeExpirationTime);
        }
        return computeExpirationTime;
    }
}
