package org.osmdroid.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import androidx.work.PeriodicWorkRequest;
import java.io.File;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.modules.SqlTileWriter;
import org.osmdroid.tileprovider.util.StorageUtils;

public class DefaultConfigurationProvider implements IConfigurationProvider {
    public static final String DEFAULT_USER_AGENT = "osmdroid";
    protected int animationSpeedDefault = 1000;
    protected int animationSpeedShort = 500;
    protected short cacheMapTileCount = 9;
    protected short cacheTileOvershoot = 0;
    protected boolean debugMapTileDownloader = false;
    protected boolean debugMapView = false;
    protected boolean debugMode = false;
    protected boolean debugTileProviders = false;
    protected boolean enforceTileSystemBounds = false;
    protected long expirationAdder = 0;
    protected Long expirationOverride = null;
    protected long gpsWaitTime = 20000;
    protected SimpleDateFormat httpHeaderDateTimeFormat = new SimpleDateFormat(OpenStreetMapTileProviderConstants.HTTP_EXPIRES_HEADER_FORMAT, Locale.US);
    protected Proxy httpProxy = null;
    protected boolean isMapViewHardwareAccelerated = true;
    private final Map<String, String> mAdditionalHttpRequestProperties = new HashMap();
    private String mNormalizedUserAgent;
    protected boolean mTileDownloaderFollowRedirects = true;
    protected long mTileGCBulkPauseInMillis = 500;
    protected int mTileGCBulkSize = 20;
    protected long mTileGCFrequencyInMillis = PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS;
    protected boolean mapViewRecycler = true;
    protected File osmdroidBasePath;
    protected File osmdroidTileCache;
    protected short tileDownloadMaxQueueSize = 40;
    protected short tileDownloadThreads = 2;
    protected long tileFileSystemCacheMaxBytes = 629145600;
    protected long tileFileSystemCacheTrimBytes = 524288000;
    protected short tileFileSystemMaxQueueSize = 40;
    protected short tileFileSystemThreads = 8;
    protected String userAgentHttpHeader = "User-Agent";
    protected String userAgentValue = DEFAULT_USER_AGENT;

    public long getGpsWaitTime() {
        return this.gpsWaitTime;
    }

    public void setGpsWaitTime(long j) {
        this.gpsWaitTime = j;
    }

    public boolean isDebugMode() {
        return this.debugMode;
    }

    public void setDebugMode(boolean z) {
        this.debugMode = z;
    }

    public boolean isDebugMapView() {
        return this.debugMapView;
    }

    public void setDebugMapView(boolean z) {
        this.debugMapView = z;
    }

    public boolean isDebugTileProviders() {
        return this.debugTileProviders;
    }

    public void setDebugTileProviders(boolean z) {
        this.debugTileProviders = z;
    }

    public boolean isDebugMapTileDownloader() {
        return this.debugMapTileDownloader;
    }

    public void setDebugMapTileDownloader(boolean z) {
        this.debugMapTileDownloader = z;
    }

    public boolean isMapViewHardwareAccelerated() {
        return this.isMapViewHardwareAccelerated;
    }

    public void setMapViewHardwareAccelerated(boolean z) {
        this.isMapViewHardwareAccelerated = z;
    }

    public String getUserAgentValue() {
        return this.userAgentValue;
    }

    public void setUserAgentValue(String str) {
        this.userAgentValue = str;
    }

    public Map<String, String> getAdditionalHttpRequestProperties() {
        return this.mAdditionalHttpRequestProperties;
    }

    public short getCacheMapTileCount() {
        return this.cacheMapTileCount;
    }

    public void setCacheMapTileCount(short s) {
        this.cacheMapTileCount = s;
    }

    public short getTileDownloadThreads() {
        return this.tileDownloadThreads;
    }

    public void setTileDownloadThreads(short s) {
        this.tileDownloadThreads = s;
    }

    public short getTileFileSystemThreads() {
        return this.tileFileSystemThreads;
    }

    public void setTileFileSystemThreads(short s) {
        this.tileFileSystemThreads = s;
    }

    public short getTileDownloadMaxQueueSize() {
        return this.tileDownloadMaxQueueSize;
    }

    public void setTileDownloadMaxQueueSize(short s) {
        this.tileDownloadMaxQueueSize = s;
    }

    public short getTileFileSystemMaxQueueSize() {
        return this.tileFileSystemMaxQueueSize;
    }

    public void setTileFileSystemMaxQueueSize(short s) {
        this.tileFileSystemMaxQueueSize = s;
    }

    public long getTileFileSystemCacheMaxBytes() {
        return this.tileFileSystemCacheMaxBytes;
    }

    public void setTileFileSystemCacheMaxBytes(long j) {
        this.tileFileSystemCacheMaxBytes = j;
    }

    public long getTileFileSystemCacheTrimBytes() {
        return this.tileFileSystemCacheTrimBytes;
    }

    public void setTileFileSystemCacheTrimBytes(long j) {
        this.tileFileSystemCacheTrimBytes = j;
    }

    public SimpleDateFormat getHttpHeaderDateTimeFormat() {
        return this.httpHeaderDateTimeFormat;
    }

    public void setHttpHeaderDateTimeFormat(SimpleDateFormat simpleDateFormat) {
        this.httpHeaderDateTimeFormat = simpleDateFormat;
    }

    public Proxy getHttpProxy() {
        return this.httpProxy;
    }

    public void setHttpProxy(Proxy proxy) {
        this.httpProxy = proxy;
    }

    public File getOsmdroidBasePath() {
        return getOsmdroidBasePath((Context) null);
    }

    public File getOsmdroidBasePath(Context context) {
        try {
            if (this.osmdroidBasePath == null) {
                StorageUtils.StorageInfo bestWritableStorage = StorageUtils.getBestWritableStorage(context);
                if (bestWritableStorage != null) {
                    File file = new File(bestWritableStorage.path, DEFAULT_USER_AGENT);
                    this.osmdroidBasePath = file;
                    file.mkdirs();
                } else if (!new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DEFAULT_USER_AGENT).mkdirs()) {
                    Log.e(IMapView.LOGTAG, "Directory not created");
                }
            }
        } catch (Exception e) {
            Log.d(IMapView.LOGTAG, "Unable to create base path at " + this.osmdroidBasePath, e);
        }
        if (this.osmdroidBasePath == null && context != null) {
            this.osmdroidBasePath = context.getFilesDir();
        }
        return this.osmdroidBasePath;
    }

    public void setOsmdroidBasePath(File file) {
        this.osmdroidBasePath = file;
    }

    public File getOsmdroidTileCache() {
        return getOsmdroidTileCache((Context) null);
    }

    public File getOsmdroidTileCache(Context context) {
        if (this.osmdroidTileCache == null) {
            this.osmdroidTileCache = new File(getOsmdroidBasePath(context), "tiles");
        }
        try {
            this.osmdroidTileCache.mkdirs();
        } catch (Exception e) {
            Log.d(IMapView.LOGTAG, "Unable to create tile cache path at " + this.osmdroidTileCache, e);
        }
        return this.osmdroidTileCache;
    }

    public void setOsmdroidTileCache(File file) {
        this.osmdroidTileCache = file;
    }

    public String getUserAgentHttpHeader() {
        return this.userAgentHttpHeader;
    }

    public void setUserAgentHttpHeader(String str) {
        this.userAgentHttpHeader = str;
    }

    public void load(Context context, SharedPreferences sharedPreferences) {
        this.mNormalizedUserAgent = computeNormalizedUserAgent(context);
        String string = sharedPreferences.getString("osmdroid.basePath", (String) null);
        if (string == null || !new File(string).exists()) {
            File osmdroidBasePath2 = getOsmdroidBasePath(context);
            File osmdroidTileCache2 = getOsmdroidTileCache(context);
            if (!osmdroidBasePath2.exists() || !StorageUtils.isWritable(osmdroidBasePath2)) {
                osmdroidBasePath2 = new File(context.getFilesDir(), DEFAULT_USER_AGENT);
                osmdroidTileCache2 = new File(osmdroidBasePath2, "tiles");
                osmdroidTileCache2.mkdirs();
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("osmdroid.basePath", osmdroidBasePath2.getAbsolutePath());
            edit.putString("osmdroid.cachePath", osmdroidTileCache2.getAbsolutePath());
            commit(edit);
            setOsmdroidBasePath(osmdroidBasePath2);
            setOsmdroidTileCache(osmdroidTileCache2);
            setUserAgentValue(context.getPackageName());
            save(context, sharedPreferences);
        } else {
            setOsmdroidBasePath(new File(sharedPreferences.getString("osmdroid.basePath", getOsmdroidBasePath(context).getAbsolutePath())));
            setOsmdroidTileCache(new File(sharedPreferences.getString("osmdroid.cachePath", getOsmdroidTileCache(context).getAbsolutePath())));
            setDebugMode(sharedPreferences.getBoolean("osmdroid.DebugMode", this.debugMode));
            setDebugMapTileDownloader(sharedPreferences.getBoolean("osmdroid.DebugDownloading", this.debugMapTileDownloader));
            setDebugMapView(sharedPreferences.getBoolean("osmdroid.DebugMapView", this.debugMapView));
            setDebugTileProviders(sharedPreferences.getBoolean("osmdroid.DebugTileProvider", this.debugTileProviders));
            setMapViewHardwareAccelerated(sharedPreferences.getBoolean("osmdroid.HardwareAcceleration", this.isMapViewHardwareAccelerated));
            setUserAgentValue(sharedPreferences.getString("osmdroid.userAgentValue", context.getPackageName()));
            load(sharedPreferences, this.mAdditionalHttpRequestProperties, "osmdroid.additionalHttpRequestProperty.");
            setGpsWaitTime(sharedPreferences.getLong("osmdroid.gpsWaitTime", this.gpsWaitTime));
            setTileDownloadThreads((short) sharedPreferences.getInt("osmdroid.tileDownloadThreads", this.tileDownloadThreads));
            setTileFileSystemThreads((short) sharedPreferences.getInt("osmdroid.tileFileSystemThreads", this.tileFileSystemThreads));
            setTileDownloadMaxQueueSize((short) sharedPreferences.getInt("osmdroid.tileDownloadMaxQueueSize", this.tileDownloadMaxQueueSize));
            setTileFileSystemMaxQueueSize((short) sharedPreferences.getInt("osmdroid.tileFileSystemMaxQueueSize", this.tileFileSystemMaxQueueSize));
            setExpirationExtendedDuration(sharedPreferences.getLong("osmdroid.ExpirationExtendedDuration", this.expirationAdder));
            setMapViewRecyclerFriendly(sharedPreferences.getBoolean("osmdroid.mapViewRecycler", this.mapViewRecycler));
            setAnimationSpeedDefault(sharedPreferences.getInt("osmdroid.ZoomSpeedDefault", this.animationSpeedDefault));
            setAnimationSpeedShort(sharedPreferences.getInt("osmdroid.animationSpeedShort", this.animationSpeedShort));
            setCacheMapTileOvershoot((short) sharedPreferences.getInt("osmdroid.cacheTileOvershoot", this.cacheTileOvershoot));
            setMapTileDownloaderFollowRedirects(sharedPreferences.getBoolean("osmdroid.TileDownloaderFollowRedirects", this.mTileDownloaderFollowRedirects));
            setEnforceTileSystemBounds(sharedPreferences.getBoolean("osmdroid.enforceTileSystemBounds", false));
            if (sharedPreferences.contains("osmdroid.ExpirationOverride")) {
                Long valueOf = Long.valueOf(sharedPreferences.getLong("osmdroid.ExpirationOverride", -1));
                this.expirationOverride = valueOf;
                if (valueOf != null && valueOf.longValue() == -1) {
                    this.expirationOverride = null;
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 9) {
            long j = 0;
            File file = new File(getOsmdroidTileCache().getAbsolutePath() + File.separator + SqlTileWriter.DATABASE_FILENAME);
            if (file.exists()) {
                j = file.length();
            }
            long freeSpace = getOsmdroidTileCache().getFreeSpace() + j;
            if (getTileFileSystemCacheMaxBytes() > freeSpace) {
                double d = (double) freeSpace;
                setTileFileSystemCacheMaxBytes((long) (0.95d * d));
                setTileFileSystemCacheTrimBytes((long) (d * 0.9d));
            }
        }
    }

    public void save(Context context, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("osmdroid.basePath", getOsmdroidBasePath().getAbsolutePath());
        edit.putString("osmdroid.cachePath", getOsmdroidTileCache().getAbsolutePath());
        edit.putBoolean("osmdroid.DebugMode", isDebugMode());
        edit.putBoolean("osmdroid.DebugDownloading", isDebugMapTileDownloader());
        edit.putBoolean("osmdroid.DebugMapView", isDebugMapView());
        edit.putBoolean("osmdroid.DebugTileProvider", isDebugTileProviders());
        edit.putBoolean("osmdroid.HardwareAcceleration", isMapViewHardwareAccelerated());
        edit.putBoolean("osmdroid.TileDownloaderFollowRedirects", isMapTileDownloaderFollowRedirects());
        edit.putString("osmdroid.userAgentValue", getUserAgentValue());
        save(sharedPreferences, edit, this.mAdditionalHttpRequestProperties, "osmdroid.additionalHttpRequestProperty.");
        edit.putLong("osmdroid.gpsWaitTime", this.gpsWaitTime);
        edit.putInt("osmdroid.cacheMapTileCount", this.cacheMapTileCount);
        edit.putInt("osmdroid.tileDownloadThreads", this.tileDownloadThreads);
        edit.putInt("osmdroid.tileFileSystemThreads", this.tileFileSystemThreads);
        edit.putInt("osmdroid.tileDownloadMaxQueueSize", this.tileDownloadMaxQueueSize);
        edit.putInt("osmdroid.tileFileSystemMaxQueueSize", this.tileFileSystemMaxQueueSize);
        edit.putLong("osmdroid.ExpirationExtendedDuration", this.expirationAdder);
        Long l = this.expirationOverride;
        if (l != null) {
            edit.putLong("osmdroid.ExpirationOverride", l.longValue());
        }
        edit.putInt("osmdroid.ZoomSpeedDefault", this.animationSpeedDefault);
        edit.putInt("osmdroid.animationSpeedShort", this.animationSpeedShort);
        edit.putBoolean("osmdroid.mapViewRecycler", this.mapViewRecycler);
        edit.putInt("osmdroid.cacheTileOvershoot", this.cacheTileOvershoot);
        edit.putBoolean("osmdroid.enforceTileSystemBounds", this.enforceTileSystemBounds);
        commit(edit);
    }

    private static void load(SharedPreferences sharedPreferences, Map<String, String> map, String str) {
        if (str != null && map != null) {
            map.clear();
            for (String next : sharedPreferences.getAll().keySet()) {
                if (next != null && next.startsWith(str)) {
                    map.put(next.substring(str.length()), sharedPreferences.getString(next, (String) null));
                }
            }
        }
    }

    private static void save(SharedPreferences sharedPreferences, SharedPreferences.Editor editor, Map<String, String> map, String str) {
        for (String next : sharedPreferences.getAll().keySet()) {
            if (next.startsWith(str)) {
                editor.remove(next);
            }
        }
        for (Map.Entry next2 : map.entrySet()) {
            editor.putString(str + ((String) next2.getKey()), (String) next2.getValue());
        }
    }

    private static void commit(SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= 9) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public long getExpirationExtendedDuration() {
        return this.expirationAdder;
    }

    public void setExpirationExtendedDuration(long j) {
        if (j < 0) {
            this.expirationAdder = 0;
        } else {
            this.expirationAdder = j;
        }
    }

    public void setExpirationOverrideDuration(Long l) {
        this.expirationOverride = l;
    }

    public Long getExpirationOverrideDuration() {
        return this.expirationOverride;
    }

    public void setAnimationSpeedDefault(int i) {
        this.animationSpeedDefault = i;
    }

    public int getAnimationSpeedDefault() {
        return this.animationSpeedDefault;
    }

    public void setAnimationSpeedShort(int i) {
        this.animationSpeedShort = i;
    }

    public int getAnimationSpeedShort() {
        return this.animationSpeedShort;
    }

    public boolean isMapViewRecyclerFriendly() {
        return this.mapViewRecycler;
    }

    public void setMapViewRecyclerFriendly(boolean z) {
        this.mapViewRecycler = z;
    }

    public void setCacheMapTileOvershoot(short s) {
        this.cacheTileOvershoot = s;
    }

    public short getCacheMapTileOvershoot() {
        return this.cacheTileOvershoot;
    }

    public long getTileGCFrequencyInMillis() {
        return this.mTileGCFrequencyInMillis;
    }

    public void setTileGCFrequencyInMillis(long j) {
        this.mTileGCFrequencyInMillis = j;
    }

    public int getTileGCBulkSize() {
        return this.mTileGCBulkSize;
    }

    public void setTileGCBulkSize(int i) {
        this.mTileGCBulkSize = i;
    }

    public long getTileGCBulkPauseInMillis() {
        return this.mTileGCBulkPauseInMillis;
    }

    public void setTileGCBulkPauseInMillis(long j) {
        this.mTileGCBulkPauseInMillis = j;
    }

    public void setMapTileDownloaderFollowRedirects(boolean z) {
        this.mTileDownloaderFollowRedirects = z;
    }

    public boolean isMapTileDownloaderFollowRedirects() {
        return this.mTileDownloaderFollowRedirects;
    }

    public String getNormalizedUserAgent() {
        return this.mNormalizedUserAgent;
    }

    public boolean isEnforceTileSystemBounds() {
        return this.enforceTileSystemBounds;
    }

    public void setEnforceTileSystemBounds(boolean z) {
        this.enforceTileSystemBounds = z;
    }

    private String computeNormalizedUserAgent(Context context) {
        if (context == null) {
            return null;
        }
        String packageName = context.getPackageName();
        try {
            int i = context.getPackageManager().getPackageInfo(packageName, 128).versionCode;
            return packageName + "/" + i;
        } catch (PackageManager.NameNotFoundException unused) {
            return packageName;
        }
    }
}
