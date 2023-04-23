package org.osmdroid.wms;

import android.util.Log;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.MapTileIndex;

public class WMSTileSource extends OnlineTileSourceBase {
    private static final double MAP_SIZE = 4.007501669578488E7d;
    protected static final int MAXX = 1;
    protected static final int MAXY = 3;
    protected static final int MINX = 0;
    protected static final int MINY = 2;
    private static final int ORIG_X = 0;
    private static final int ORIG_Y = 1;
    private static final double[] TILE_ORIGIN = {-2.003750834789244E7d, 2.003750834789244E7d};
    final String WMS_FORMAT_STRING = "%s&version=%s&request=GetMap&layers=%s&bbox=%f,%f,%f,%f&width=256&height=256&srs=%s&format=image/png&style=%s&transparent=true";
    private boolean forceHttp = false;
    private boolean forceHttps = false;
    private String layer = "";
    private String srs = "EPSG:900913";
    private String style = null;
    private String version = "1.1.0";

    public WMSTileSource(String str, String[] strArr, String str2, String str3, String str4, String str5, int i) {
        super(str, 0, 22, i, "png", strArr);
        Log.i(IMapView.LOGTAG, "WMS support is BETA. Please report any issues");
        this.layer = str2;
        this.version = str3;
        if (str4 != null) {
            this.srs = str4;
        }
        this.style = str5;
    }

    public static WMSTileSource createFrom(WMSEndpoint wMSEndpoint, WMSLayer wMSLayer) {
        String str = !wMSLayer.getSrs().isEmpty() ? wMSLayer.getSrs().get(0) : "EPSG:900913";
        if (wMSLayer.getStyles().isEmpty()) {
            return new WMSTileSource(wMSLayer.getName(), new String[]{wMSEndpoint.getBaseurl()}, wMSLayer.getName(), wMSEndpoint.getWmsVersion(), str, (String) null, wMSLayer.getPixelSize());
        }
        return new WMSTileSource(wMSLayer.getName(), new String[]{wMSEndpoint.getBaseurl()}, wMSLayer.getName(), wMSEndpoint.getWmsVersion(), str, wMSLayer.getStyles().get(0), wMSLayer.getPixelSize());
    }

    public static BoundingBox tile2boundingBox(int i, int i2, int i3) {
        return new BoundingBox(tile2lat(i2, i3), tile2lon(i + 1, i3), tile2lat(i2 + 1, i3), tile2lon(i, i3));
    }

    public static double tile2lon(int i, int i2) {
        return ((((double) i) / Math.pow(2.0d, (double) i2)) * 360.0d) - 180.0d;
    }

    public static double tile2lat(int i, int i2) {
        return Math.toDegrees(Math.atan(Math.sinh(3.141592653589793d - ((((double) i) * 6.283185307179586d) / Math.pow(2.0d, (double) i2)))));
    }

    public double[] getBoundingBox(int i, int i2, int i3) {
        double pow = MAP_SIZE / Math.pow(2.0d, (double) i3);
        double[] dArr = TILE_ORIGIN;
        double d = dArr[0];
        double d2 = (((double) i) * pow) + d;
        double d3 = d + (((double) (i + 1)) * pow);
        double d4 = dArr[1];
        double d5 = d4 - (((double) (i2 + 1)) * pow);
        double[] dArr2 = new double[4];
        dArr2[0] = d2;
        dArr2[2] = d5;
        dArr2[1] = d3;
        dArr2[3] = d4 - (((double) i2) * pow);
        return dArr2;
    }

    public boolean isForceHttps() {
        return this.forceHttps;
    }

    public void setForceHttps(boolean z) {
        this.forceHttps = z;
    }

    public boolean isForceHttp() {
        return this.forceHttp;
    }

    public void setForceHttp(boolean z) {
        this.forceHttp = z;
    }

    public String getTileURLString(long j) {
        String baseUrl = getBaseUrl();
        if (this.forceHttps) {
            baseUrl = baseUrl.replace("http://", "https://");
        }
        if (this.forceHttp) {
            baseUrl = baseUrl.replace("https://", "http://");
        }
        StringBuilder sb = new StringBuilder(baseUrl);
        if (!baseUrl.endsWith("&")) {
            sb.append("&");
        }
        sb.append("request=GetMap&width=");
        sb.append(getTileSizePixels());
        sb.append("&height=");
        sb.append(getTileSizePixels());
        sb.append("&version=");
        sb.append(this.version);
        sb.append("&layers=");
        sb.append(this.layer);
        sb.append("&bbox=");
        if (this.srs.equals("EPSG:900913")) {
            double[] boundingBox = getBoundingBox(MapTileIndex.getX(j), MapTileIndex.getY(j), MapTileIndex.getZoom(j));
            sb.append(boundingBox[0]);
            sb.append(",");
            sb.append(boundingBox[2]);
            sb.append(",");
            sb.append(boundingBox[1]);
            sb.append(",");
            sb.append(boundingBox[3]);
        } else {
            BoundingBox tile2boundingBox = tile2boundingBox(MapTileIndex.getX(j), MapTileIndex.getY(j), MapTileIndex.getZoom(j));
            sb.append(tile2boundingBox.getLonWest());
            sb.append(",");
            sb.append(tile2boundingBox.getLatSouth());
            sb.append(",");
            sb.append(tile2boundingBox.getLonEast());
            sb.append(",");
            sb.append(tile2boundingBox.getLatNorth());
        }
        sb.append("&srs=");
        sb.append(this.srs);
        sb.append("&format=image/png&transparent=true");
        if (this.style != null) {
            sb.append("&styles=");
            sb.append(this.style);
        }
        Log.i(IMapView.LOGTAG, sb.toString());
        return sb.toString();
    }
}
