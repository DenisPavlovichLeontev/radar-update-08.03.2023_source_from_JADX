package org.osmdroid.tileprovider.tilesource;

import android.content.Context;
import org.osmdroid.tileprovider.util.ManifestUtil;
import org.osmdroid.util.MapTileIndex;

public class MapBoxTileSource extends OnlineTileSourceBase {
    private static final String ACCESS_TOKEN = "MAPBOX_ACCESS_TOKEN";
    private static final String MAPBOX_MAPID = "MAPBOX_MAPID";
    private static final String[] mapBoxBaseUrl = {"https://api.mapbox.com/styles/v1/mapbox/"};
    private String accessToken;
    private String mapBoxMapId;

    public MapBoxTileSource() {
        super("mapbox", 1, 19, 256, ".png", mapBoxBaseUrl);
        this.mapBoxMapId = "";
    }

    public MapBoxTileSource(Context context) {
        super("mapbox", 1, 19, 256, ".png", mapBoxBaseUrl);
        this.mapBoxMapId = "";
        retrieveAccessToken(context);
        retrieveMapBoxMapId(context);
        this.mName = "mapbox" + this.mapBoxMapId;
    }

    public MapBoxTileSource(String str, String str2) {
        super("mapbox", 1, 19, 256, ".png", mapBoxBaseUrl);
        this.accessToken = str2;
        this.mapBoxMapId = str;
        this.mName = "mapbox" + this.mapBoxMapId;
    }

    public MapBoxTileSource(String str, int i, int i2, int i3, String str2) {
        super(str, i, i2, i3, str2, mapBoxBaseUrl);
        this.mapBoxMapId = "";
    }

    public MapBoxTileSource(String str, int i, int i2, int i3, String str2, String str3, String str4) {
        super(str, i, i2, i3, str2, new String[]{str4});
        this.mapBoxMapId = "";
    }

    public final void retrieveMapBoxMapId(Context context) {
        this.mapBoxMapId = ManifestUtil.retrieveKey(context, MAPBOX_MAPID);
    }

    public final void retrieveAccessToken(Context context) {
        this.accessToken = ManifestUtil.retrieveKey(context, ACCESS_TOKEN);
    }

    public void setMapboxMapid(String str) {
        this.mapBoxMapId = str;
        this.mName = "mapbox" + this.mapBoxMapId;
    }

    public String getMapBoxMapId() {
        return this.mapBoxMapId;
    }

    public String getTileURLString(long j) {
        return getBaseUrl() + getMapBoxMapId() + "/tiles/" + MapTileIndex.getZoom(j) + "/" + MapTileIndex.getX(j) + "/" + MapTileIndex.getY(j) + "?access_token=" + getAccessToken();
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String str) {
        this.accessToken = str;
    }
}
