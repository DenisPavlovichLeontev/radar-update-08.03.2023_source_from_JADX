package org.osmdroid.config;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.Map;

public interface IConfigurationProvider {
    Map<String, String> getAdditionalHttpRequestProperties();

    int getAnimationSpeedDefault();

    int getAnimationSpeedShort();

    short getCacheMapTileCount();

    short getCacheMapTileOvershoot();

    long getExpirationExtendedDuration();

    Long getExpirationOverrideDuration();

    long getGpsWaitTime();

    SimpleDateFormat getHttpHeaderDateTimeFormat();

    Proxy getHttpProxy();

    String getNormalizedUserAgent();

    File getOsmdroidBasePath();

    File getOsmdroidBasePath(Context context);

    File getOsmdroidTileCache();

    File getOsmdroidTileCache(Context context);

    short getTileDownloadMaxQueueSize();

    short getTileDownloadThreads();

    long getTileFileSystemCacheMaxBytes();

    long getTileFileSystemCacheTrimBytes();

    short getTileFileSystemMaxQueueSize();

    short getTileFileSystemThreads();

    long getTileGCBulkPauseInMillis();

    int getTileGCBulkSize();

    long getTileGCFrequencyInMillis();

    String getUserAgentHttpHeader();

    String getUserAgentValue();

    boolean isDebugMapTileDownloader();

    boolean isDebugMapView();

    boolean isDebugMode();

    boolean isDebugTileProviders();

    boolean isEnforceTileSystemBounds();

    boolean isMapTileDownloaderFollowRedirects();

    boolean isMapViewHardwareAccelerated();

    boolean isMapViewRecyclerFriendly();

    void load(Context context, SharedPreferences sharedPreferences);

    void save(Context context, SharedPreferences sharedPreferences);

    void setAnimationSpeedDefault(int i);

    void setAnimationSpeedShort(int i);

    void setCacheMapTileCount(short s);

    void setCacheMapTileOvershoot(short s);

    void setDebugMapTileDownloader(boolean z);

    void setDebugMapView(boolean z);

    void setDebugMode(boolean z);

    void setDebugTileProviders(boolean z);

    void setEnforceTileSystemBounds(boolean z);

    void setExpirationExtendedDuration(long j);

    void setExpirationOverrideDuration(Long l);

    void setGpsWaitTime(long j);

    void setHttpHeaderDateTimeFormat(SimpleDateFormat simpleDateFormat);

    void setHttpProxy(Proxy proxy);

    void setMapTileDownloaderFollowRedirects(boolean z);

    void setMapViewHardwareAccelerated(boolean z);

    void setMapViewRecyclerFriendly(boolean z);

    void setOsmdroidBasePath(File file);

    void setOsmdroidTileCache(File file);

    void setTileDownloadMaxQueueSize(short s);

    void setTileDownloadThreads(short s);

    void setTileFileSystemCacheMaxBytes(long j);

    void setTileFileSystemCacheTrimBytes(long j);

    void setTileFileSystemMaxQueueSize(short s);

    void setTileFileSystemThreads(short s);

    void setTileGCBulkPauseInMillis(long j);

    void setTileGCBulkSize(int i);

    void setTileGCFrequencyInMillis(long j);

    void setUserAgentHttpHeader(String str);

    void setUserAgentValue(String str);
}
