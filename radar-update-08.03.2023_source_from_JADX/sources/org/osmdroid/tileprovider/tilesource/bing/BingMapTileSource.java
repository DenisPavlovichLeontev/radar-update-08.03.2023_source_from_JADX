package org.osmdroid.tileprovider.tilesource.bing;

import android.content.Context;
import android.util.Log;
import java.util.Locale;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.tilesource.IStyledTileSource;
import org.osmdroid.tileprovider.tilesource.QuadTreeTileSource;
import org.osmdroid.tileprovider.util.ManifestUtil;

public class BingMapTileSource extends QuadTreeTileSource implements IStyledTileSource<String> {
    private static final String BASE_URL_PATTERN = "https://dev.virtualearth.net/REST/V1/Imagery/Metadata/%s?mapVersion=v1&output=json&uriScheme=https&key=%s";
    private static final String BING_KEY = "BING_KEY";
    private static final String FILENAME_ENDING = ".jpeg";
    public static final String IMAGERYSET_AERIAL = "Aerial";
    public static final String IMAGERYSET_AERIALWITHLABELS = "AerialWithLabels";
    public static final String IMAGERYSET_ROAD = "Road";
    private static String mBingMapKey = "";
    private String mBaseUrl;
    private ImageryMetaDataResource mImageryData = ImageryMetaDataResource.getDefaultInstance();
    private String mLocale;
    private String mStyle = IMAGERYSET_ROAD;
    private String mUrl;

    public BingMapTileSource(String str) {
        super("BingMaps", 0, 19, 256, FILENAME_ENDING, (String[]) null);
        this.mLocale = str;
        if (str == null) {
            this.mLocale = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
        }
    }

    public static void retrieveBingKey(Context context) {
        mBingMapKey = ManifestUtil.retrieveKey(context, BING_KEY);
    }

    public static String getBingKey() {
        return mBingMapKey;
    }

    public static void setBingKey(String str) {
        mBingMapKey = str;
    }

    public String getBaseUrl() {
        if (!this.mImageryData.m_isInitialised) {
            initMetaData();
        }
        return this.mBaseUrl;
    }

    public String getTileURLString(long j) {
        if (!this.mImageryData.m_isInitialised) {
            initMetaData();
        }
        return String.format(this.mUrl, new Object[]{quadTree(j)});
    }

    public int getMinimumZoomLevel() {
        return this.mImageryData.m_zoomMin;
    }

    public int getMaximumZoomLevel() {
        return this.mImageryData.m_zoomMax;
    }

    public int getTileSizePixels() {
        return this.mImageryData.m_imageHeight;
    }

    public String pathBase() {
        return this.mName + this.mStyle;
    }

    public String getCopyrightNotice() {
        return this.mImageryData.copyright;
    }

    public void setStyle(String str) {
        if (!str.equals(this.mStyle)) {
            synchronized (this.mStyle) {
                this.mUrl = null;
                this.mBaseUrl = null;
                this.mImageryData.m_isInitialised = false;
            }
        }
        this.mStyle = str;
        this.mName = pathBase();
    }

    public String getStyle() {
        return this.mStyle;
    }

    public ImageryMetaDataResource initMetaData() {
        ImageryMetaDataResource metaData;
        if (!this.mImageryData.m_isInitialised) {
            synchronized (this) {
                if (!this.mImageryData.m_isInitialised && (metaData = getMetaData()) != null) {
                    this.mImageryData = metaData;
                    updateBaseUrl();
                }
            }
        }
        return this.mImageryData;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v0, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v0, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v0, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v2, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v3, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v3, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v1, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v8, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v4, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v7, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v5, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v12, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v9, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v8, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v9, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v9, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v10, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v10, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v4, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v11, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v5, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v12, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v6, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v15, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v16, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v9, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v20, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v17, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v38, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v32, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v33, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v34, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v39, resolved type: java.io.InputStream} */
    /* JADX WARNING: type inference failed for: r2v2, types: [java.net.HttpURLConnection] */
    /* JADX WARNING: type inference failed for: r3v1, types: [java.net.HttpURLConnection] */
    /* JADX WARNING: type inference failed for: r2v7 */
    /* JADX WARNING: type inference failed for: r2v15 */
    /* JADX WARNING: type inference failed for: r2v16 */
    /* JADX WARNING: type inference failed for: r2v18 */
    /* JADX WARNING: type inference failed for: r2v21 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x0195 A[SYNTHETIC, Splitter:B:100:0x0195] */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x019f A[SYNTHETIC, Splitter:B:105:0x019f] */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x014f A[SYNTHETIC, Splitter:B:67:0x014f] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0159 A[SYNTHETIC, Splitter:B:72:0x0159] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0163 A[SYNTHETIC, Splitter:B:77:0x0163] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x016d A[SYNTHETIC, Splitter:B:82:0x016d] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0181 A[SYNTHETIC, Splitter:B:90:0x0181] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x018b A[SYNTHETIC, Splitter:B:95:0x018b] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.osmdroid.tileprovider.tilesource.bing.ImageryMetaDataResource getMetaData() {
        /*
            r10 = this;
            java.lang.String r0 = "end getMetaData"
            java.lang.String r1 = "OsmDroid"
            java.lang.String r2 = "getMetaData"
            android.util.Log.d(r1, r2)
            r2 = 0
            java.net.URL r3 = new java.net.URL     // Catch:{ Exception -> 0x0143, all -> 0x013d }
            java.lang.String r4 = "https://dev.virtualearth.net/REST/V1/Imagery/Metadata/%s?mapVersion=v1&output=json&uriScheme=https&key=%s"
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x0143, all -> 0x013d }
            r6 = 0
            java.lang.String r7 = r10.mStyle     // Catch:{ Exception -> 0x0143, all -> 0x013d }
            r5[r6] = r7     // Catch:{ Exception -> 0x0143, all -> 0x013d }
            r6 = 1
            java.lang.String r7 = mBingMapKey     // Catch:{ Exception -> 0x0143, all -> 0x013d }
            r5[r6] = r7     // Catch:{ Exception -> 0x0143, all -> 0x013d }
            java.lang.String r4 = java.lang.String.format(r4, r5)     // Catch:{ Exception -> 0x0143, all -> 0x013d }
            r3.<init>(r4)     // Catch:{ Exception -> 0x0143, all -> 0x013d }
            java.net.URLConnection r3 = r3.openConnection()     // Catch:{ Exception -> 0x0143, all -> 0x013d }
            java.net.HttpURLConnection r3 = (java.net.HttpURLConnection) r3     // Catch:{ Exception -> 0x0143, all -> 0x013d }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            r4.<init>()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.String r5 = "make request "
            r4.append(r5)     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.net.URL r5 = r3.getURL()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            r4.append(r5)     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            android.util.Log.d(r1, r4)     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            org.osmdroid.config.IConfigurationProvider r4 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.String r4 = r4.getUserAgentHttpHeader()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            org.osmdroid.config.IConfigurationProvider r5 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.String r5 = r5.getUserAgentValue()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            r3.setRequestProperty(r4, r5)     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            org.osmdroid.config.IConfigurationProvider r4 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.util.Map r4 = r4.getAdditionalHttpRequestProperties()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.util.Set r4 = r4.entrySet()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.util.Iterator r4 = r4.iterator()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
        L_0x006b:
            boolean r5 = r4.hasNext()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            if (r5 == 0) goto L_0x0087
            java.lang.Object r5 = r4.next()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.util.Map$Entry r5 = (java.util.Map.Entry) r5     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.Object r6 = r5.getKey()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.Object r5 = r5.getValue()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            r3.setRequestProperty(r6, r5)     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            goto L_0x006b
        L_0x0087:
            r3.connect()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            int r4 = r3.getResponseCode()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            r5 = 200(0xc8, float:2.8E-43)
            if (r4 == r5) goto L_0x00be
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            r4.<init>()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.String r5 = "Cannot get response for url "
            r4.append(r5)     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.net.URL r5 = r3.getURL()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            r4.append(r5)     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.String r5 = " "
            r4.append(r5)     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.String r5 = r3.getResponseMessage()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            r4.append(r5)     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            android.util.Log.e(r1, r4)     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            r4 = r2
            r5 = r4
            r6 = r5
            goto L_0x00df
        L_0x00be:
            java.io.InputStream r4 = r3.getInputStream()     // Catch:{ Exception -> 0x013a, all -> 0x0135 }
            java.io.ByteArrayOutputStream r5 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x012e, all -> 0x0127 }
            r5.<init>()     // Catch:{ Exception -> 0x012e, all -> 0x0127 }
            java.io.BufferedOutputStream r6 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x0120, all -> 0x0119 }
            r7 = 8192(0x2000, float:1.14794E-41)
            r6.<init>(r5, r7)     // Catch:{ Exception -> 0x0120, all -> 0x0119 }
            org.osmdroid.tileprovider.util.StreamUtils.copy(r4, r6)     // Catch:{ Exception -> 0x0113, all -> 0x010d }
            r6.flush()     // Catch:{ Exception -> 0x0113, all -> 0x010d }
            java.lang.String r7 = r5.toString()     // Catch:{ Exception -> 0x0113, all -> 0x010d }
            org.osmdroid.tileprovider.tilesource.bing.ImageryMetaDataResource r2 = org.osmdroid.tileprovider.tilesource.bing.ImageryMetaData.getInstanceFromJSON(r7)     // Catch:{ Exception -> 0x0113, all -> 0x010d }
            r9 = r4
            r4 = r2
            r2 = r9
        L_0x00df:
            if (r3 == 0) goto L_0x00e9
            r3.disconnect()     // Catch:{ Exception -> 0x00e5 }
            goto L_0x00e9
        L_0x00e5:
            r3 = move-exception
            android.util.Log.d(r1, r0, r3)
        L_0x00e9:
            if (r2 == 0) goto L_0x00f3
            r2.close()     // Catch:{ Exception -> 0x00ef }
            goto L_0x00f3
        L_0x00ef:
            r2 = move-exception
            android.util.Log.d(r1, r0, r2)
        L_0x00f3:
            if (r5 == 0) goto L_0x00fd
            r5.close()     // Catch:{ Exception -> 0x00f9 }
            goto L_0x00fd
        L_0x00f9:
            r2 = move-exception
            android.util.Log.d(r1, r0, r2)
        L_0x00fd:
            if (r6 == 0) goto L_0x0107
            r6.close()     // Catch:{ Exception -> 0x0103 }
            goto L_0x0107
        L_0x0103:
            r2 = move-exception
            android.util.Log.d(r1, r0, r2)
        L_0x0107:
            android.util.Log.d(r1, r0)
            r2 = r4
            goto L_0x0178
        L_0x010d:
            r2 = move-exception
            r9 = r3
            r3 = r2
            r2 = r9
            goto L_0x017f
        L_0x0113:
            r7 = move-exception
            r9 = r5
            r5 = r4
            r4 = r7
            r7 = r6
            goto L_0x0125
        L_0x0119:
            r6 = move-exception
            r9 = r6
            r6 = r2
            r2 = r3
            r3 = r9
            goto L_0x017f
        L_0x0120:
            r6 = move-exception
            r7 = r2
            r9 = r5
            r5 = r4
            r4 = r6
        L_0x0125:
            r6 = r9
            goto L_0x0148
        L_0x0127:
            r5 = move-exception
            r6 = r2
            r2 = r3
            r3 = r5
            r5 = r6
            goto L_0x017f
        L_0x012e:
            r5 = move-exception
            r6 = r2
            r7 = r6
            r9 = r5
            r5 = r4
            r4 = r9
            goto L_0x0148
        L_0x0135:
            r4 = move-exception
            r5 = r2
            r6 = r5
            r2 = r3
            goto L_0x0140
        L_0x013a:
            r4 = move-exception
            r5 = r2
            goto L_0x0146
        L_0x013d:
            r4 = move-exception
            r5 = r2
            r6 = r5
        L_0x0140:
            r3 = r4
            r4 = r6
            goto L_0x017f
        L_0x0143:
            r4 = move-exception
            r3 = r2
            r5 = r3
        L_0x0146:
            r6 = r5
            r7 = r6
        L_0x0148:
            java.lang.String r8 = "Error getting imagery meta data"
            android.util.Log.e(r1, r8, r4)     // Catch:{ all -> 0x0179 }
            if (r3 == 0) goto L_0x0157
            r3.disconnect()     // Catch:{ Exception -> 0x0153 }
            goto L_0x0157
        L_0x0153:
            r3 = move-exception
            android.util.Log.d(r1, r0, r3)
        L_0x0157:
            if (r5 == 0) goto L_0x0161
            r5.close()     // Catch:{ Exception -> 0x015d }
            goto L_0x0161
        L_0x015d:
            r3 = move-exception
            android.util.Log.d(r1, r0, r3)
        L_0x0161:
            if (r6 == 0) goto L_0x016b
            r6.close()     // Catch:{ Exception -> 0x0167 }
            goto L_0x016b
        L_0x0167:
            r3 = move-exception
            android.util.Log.d(r1, r0, r3)
        L_0x016b:
            if (r7 == 0) goto L_0x0175
            r7.close()     // Catch:{ Exception -> 0x0171 }
            goto L_0x0175
        L_0x0171:
            r3 = move-exception
            android.util.Log.d(r1, r0, r3)
        L_0x0175:
            android.util.Log.d(r1, r0)
        L_0x0178:
            return r2
        L_0x0179:
            r4 = move-exception
            r2 = r3
            r3 = r4
            r4 = r5
            r5 = r6
            r6 = r7
        L_0x017f:
            if (r2 == 0) goto L_0x0189
            r2.disconnect()     // Catch:{ Exception -> 0x0185 }
            goto L_0x0189
        L_0x0185:
            r2 = move-exception
            android.util.Log.d(r1, r0, r2)
        L_0x0189:
            if (r4 == 0) goto L_0x0193
            r4.close()     // Catch:{ Exception -> 0x018f }
            goto L_0x0193
        L_0x018f:
            r2 = move-exception
            android.util.Log.d(r1, r0, r2)
        L_0x0193:
            if (r5 == 0) goto L_0x019d
            r5.close()     // Catch:{ Exception -> 0x0199 }
            goto L_0x019d
        L_0x0199:
            r2 = move-exception
            android.util.Log.d(r1, r0, r2)
        L_0x019d:
            if (r6 == 0) goto L_0x01a7
            r6.close()     // Catch:{ Exception -> 0x01a3 }
            goto L_0x01a7
        L_0x01a3:
            r2 = move-exception
            android.util.Log.d(r1, r0, r2)
        L_0x01a7:
            android.util.Log.d(r1, r0)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource.getMetaData():org.osmdroid.tileprovider.tilesource.bing.ImageryMetaDataResource");
    }

    /* access modifiers changed from: protected */
    public void updateBaseUrl() {
        Log.d(IMapView.LOGTAG, "updateBaseUrl");
        String subDomain = this.mImageryData.getSubDomain();
        int lastIndexOf = this.mImageryData.m_imageUrl.lastIndexOf("/");
        if (lastIndexOf > 0) {
            this.mBaseUrl = this.mImageryData.m_imageUrl.substring(0, lastIndexOf);
        } else {
            this.mBaseUrl = this.mImageryData.m_imageUrl;
        }
        this.mUrl = this.mImageryData.m_imageUrl;
        if (subDomain != null) {
            this.mBaseUrl = String.format(this.mBaseUrl, new Object[]{subDomain});
            this.mUrl = String.format(this.mUrl, new Object[]{subDomain, "%s", this.mLocale});
        }
        Log.d(IMapView.LOGTAG, "updated url = " + this.mUrl);
        Log.d(IMapView.LOGTAG, "end updateBaseUrl");
    }
}
