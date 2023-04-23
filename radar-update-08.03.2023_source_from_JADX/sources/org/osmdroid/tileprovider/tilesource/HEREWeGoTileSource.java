package org.osmdroid.tileprovider.tilesource;

import android.content.Context;
import org.osmdroid.tileprovider.util.ManifestUtil;
import org.osmdroid.util.MapTileIndex;

public class HEREWeGoTileSource extends OnlineTileSourceBase {
    private static final String APPCODE = "HEREWEGO_APPCODE";
    private static final String COPYRIGHT = "© 1987 - 2019 HERE. All rights reserved.";
    private static final String HEREWEGO_APPID = "HEREWEGO_APPID";
    private static final String HEREWEGO_DOMAIN_OVERRIDE = "HEREWEGO_OVERRIDE";
    private static final String HEREWEGO_MAPID = "HEREWEGO_MAPID";
    private static final String[] mapBoxBaseUrl = {"http://1.{domain}/maptile/2.1/maptile/newest/", "http://2.{domain}/maptile/2.1/maptile/newest/", "http://3.{domain}/maptile/2.1/maptile/newest/", "http://4.{domain}/maptile/2.1/maptile/newest/"};
    private String appCode;
    private String appId;
    private String domainOverride;
    private String herewegoMapId;

    public HEREWeGoTileSource() {
        super("herewego", 1, 20, 256, ".png", mapBoxBaseUrl, COPYRIGHT);
        this.herewegoMapId = "hybrid.day";
        this.appId = "";
        this.appCode = "";
        this.domainOverride = "aerial.maps.cit.api.here.com";
    }

    public HEREWeGoTileSource(Context context) {
        super("herewego", 1, 20, 256, ".png", mapBoxBaseUrl, COPYRIGHT);
        this.herewegoMapId = "hybrid.day";
        this.appId = "";
        this.appCode = "";
        this.domainOverride = "aerial.maps.cit.api.here.com";
        retrieveAppId(context);
        retrieveMapBoxMapId(context);
        retrieveAppCode(context);
        retrieveDomainOverride(context);
        this.mName = "herewego" + this.herewegoMapId;
    }

    private void retrieveDomainOverride(Context context) {
        String retrieveKey = ManifestUtil.retrieveKey(context, HEREWEGO_DOMAIN_OVERRIDE);
        if (retrieveKey != null && retrieveKey.length() > 0) {
            this.domainOverride = retrieveKey;
        }
    }

    public void setDomainOverride(String str) {
        this.domainOverride = str;
    }

    public HEREWeGoTileSource(String str, String str2, String str3) {
        super("herewego" + str, 1, 20, 256, ".png", mapBoxBaseUrl, COPYRIGHT);
        this.domainOverride = "aerial.maps.cit.api.here.com";
        this.appId = str2;
        this.herewegoMapId = str;
        this.appCode = str3;
    }

    public HEREWeGoTileSource(String str, int i, int i2, int i3, String str2) {
        super(str, i, i2, i3, str2, mapBoxBaseUrl, COPYRIGHT);
        this.herewegoMapId = "hybrid.day";
        this.appId = "";
        this.appCode = "";
        this.domainOverride = "aerial.maps.cit.api.here.com";
    }

    public HEREWeGoTileSource(String str, int i, int i2, int i3, String str2, String str3, String str4) {
        super(str, i, i2, i3, str2, new String[]{str4}, "© 1987 - 2017 HERE. All rights reserved.");
        this.herewegoMapId = "hybrid.day";
        this.appId = "";
        this.appCode = "";
        this.domainOverride = "aerial.maps.cit.api.here.com";
    }

    public final void retrieveAppCode(Context context) {
        this.appCode = ManifestUtil.retrieveKey(context, APPCODE);
    }

    public final void retrieveMapBoxMapId(Context context) {
        this.herewegoMapId = ManifestUtil.retrieveKey(context, HEREWEGO_MAPID);
    }

    public final void retrieveAppId(Context context) {
        this.appId = ManifestUtil.retrieveKey(context, HEREWEGO_APPID);
    }

    public void setHereWeGoMapid(String str) {
        this.herewegoMapId = str;
        this.mName = "herewego" + this.herewegoMapId;
    }

    public String getHerewegoMapId() {
        return this.herewegoMapId;
    }

    public String getTileURLString(long j) {
        return getBaseUrl().replace("{domain}", this.domainOverride) + getHerewegoMapId() + "/" + MapTileIndex.getZoom(j) + "/" + MapTileIndex.getX(j) + "/" + MapTileIndex.getY(j) + "/" + getTileSizePixels() + "/png8?" + "app_id=" + getAppId() + "&app_code=" + getAppCode() + "&lg=pt-BR";
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String str) {
        this.appId = str;
    }

    public String getAppCode() {
        return this.appCode;
    }

    public void setAppCode(String str) {
        this.appCode = str;
    }
}
