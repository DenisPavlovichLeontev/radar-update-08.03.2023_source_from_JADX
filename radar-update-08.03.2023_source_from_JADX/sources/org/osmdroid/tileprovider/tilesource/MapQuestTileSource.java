package org.osmdroid.tileprovider.tilesource;

import android.content.Context;
import org.osmdroid.tileprovider.util.ManifestUtil;
import org.osmdroid.util.MapTileIndex;

public class MapQuestTileSource extends OnlineTileSourceBase {
    private static final String ACCESS_TOKEN = "MAPQUEST_ACCESS_TOKEN";
    private static final String MAPBOX_MAPID = "MAPQUEST_MAPID";
    private static final String[] mapBoxBaseUrl = {"http://api.tiles.mapbox.com/v4/"};
    private String accessToken;
    private String mapBoxMapId;

    public MapQuestTileSource(Context context) {
        super("MapQuest", 1, 19, 256, ".png", mapBoxBaseUrl, "MapQuest");
        this.mapBoxMapId = "mapquest.streets-mb";
        retrieveAccessToken(context);
        retrieveMapBoxMapId(context);
        this.mName = "MapQuest" + this.mapBoxMapId;
    }

    public MapQuestTileSource(String str, String str2) {
        super("MapQuest" + str, 1, 19, 256, ".png", mapBoxBaseUrl, "MapQuest");
        this.accessToken = str2;
        this.mapBoxMapId = str;
    }

    public MapQuestTileSource(String str, int i, int i2, int i3, String str2) {
        super(str, i, i2, i3, str2, mapBoxBaseUrl, "MapQuest");
        this.mapBoxMapId = "mapquest.streets-mb";
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MapQuestTileSource(java.lang.String r11, int r12, int r13, int r14, java.lang.String r15, java.lang.String r16, java.lang.String r17) {
        /*
            r10 = this;
            r0 = r16
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r2 = r11
            r1.append(r11)
            r1.append(r0)
            java.lang.String r3 = r1.toString()
            r1 = 1
            java.lang.String[] r8 = new java.lang.String[r1]
            r1 = 0
            r8[r1] = r17
            java.lang.String r9 = "MapQuest"
            r2 = r10
            r4 = r12
            r5 = r13
            r6 = r14
            r7 = r15
            r2.<init>(r3, r4, r5, r6, r7, r8, r9)
            r1 = r10
            r1.mapBoxMapId = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.tilesource.MapQuestTileSource.<init>(java.lang.String, int, int, int, java.lang.String, java.lang.String, java.lang.String):void");
    }

    public final void retrieveMapBoxMapId(Context context) {
        String retrieveKey = ManifestUtil.retrieveKey(context, MAPBOX_MAPID);
        if (retrieveKey != null && retrieveKey.length() > 0) {
            this.mapBoxMapId = retrieveKey;
        }
    }

    public final void retrieveAccessToken(Context context) {
        this.accessToken = ManifestUtil.retrieveKey(context, ACCESS_TOKEN);
    }

    public void setMapboxMapid(String str) {
        this.mapBoxMapId = str;
    }

    public String getMapBoxMapId() {
        return this.mapBoxMapId;
    }

    public String getTileURLString(long j) {
        return getBaseUrl() + getMapBoxMapId() + "/" + MapTileIndex.getZoom(j) + "/" + MapTileIndex.getX(j) + "/" + MapTileIndex.getY(j) + ".png" + "?access_token=" + getAccessToken();
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String str) {
        this.accessToken = str;
    }
}
