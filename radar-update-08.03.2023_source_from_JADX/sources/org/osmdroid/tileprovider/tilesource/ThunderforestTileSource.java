package org.osmdroid.tileprovider.tilesource;

import android.content.Context;
import androidx.core.app.NotificationCompat;
import org.osmdroid.tileprovider.util.ManifestUtil;
import org.osmdroid.util.MapTileIndex;

public class ThunderforestTileSource extends OnlineTileSourceBase {
    public static final int CYCLE = 0;
    public static final int LANDSCAPE = 2;
    public static final int MOBILE_ATLAS = 7;
    public static final int NEIGHBOURHOOD = 8;
    public static final int OUTDOORS = 3;
    public static final int PIONEER = 6;
    public static final int SPINAL_MAP = 5;
    private static final String THUNDERFOREST_MAPID = "THUNDERFOREST_MAPID";
    public static final int TRANSPORT = 1;
    public static final int TRANSPORT_DARK = 4;
    private static final String[] baseUrl = {"https://a.tile.thunderforest.com/{map}/", "https://b.tile.thunderforest.com/{map}/", "https://c.tile.thunderforest.com/{map}/"};
    private static final String[] uiMap = {"CycleMap", "Transport", "Landscape", "Outdoors", "TransportDark", "Spinal", "Pioneer", "MobileAtlas", "Neighbourhood"};
    private static final String[] urlMap = {"cycle", NotificationCompat.CATEGORY_TRANSPORT, "landscape", "outdoors", "transport-dark", "spinal-map", "pioneer", "mobile-atlas", "neighbourhood"};
    private final int mMap;
    private final String mMapId;

    public static final String mapName(int i) {
        if (i < 0) {
            return "";
        }
        String[] strArr = uiMap;
        return i >= strArr.length ? "" : strArr[i];
    }

    public ThunderforestTileSource(Context context, int i) {
        super(uiMap[i], 0, 17, 256, ".png", baseUrl, "Maps © Thunderforest, Data © OpenStreetMap contributors.");
        this.mMap = i;
        this.mMapId = retrieveMapId(context);
    }

    public final String retrieveMapId(Context context) {
        return ManifestUtil.retrieveKey(context, THUNDERFOREST_MAPID);
    }

    public String getTileURLString(long j) {
        return getBaseUrl().replace("{map}", urlMap[this.mMap]) + MapTileIndex.getZoom(j) + "/" + MapTileIndex.getX(j) + "/" + MapTileIndex.getY(j) + ".png?" + "apikey=" + this.mMapId;
    }

    public static boolean haveMapId(Context context) {
        return !ManifestUtil.retrieveKey(context, THUNDERFOREST_MAPID).equals("");
    }
}
