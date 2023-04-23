package org.osmdroid.tileprovider.tilesource.bing;

import org.json.JSONArray;
import org.json.JSONObject;

public class ImageryMetaDataResource {
    private static final String COPYRIGHT = "copyright";
    private static final String IMAGE_HEIGHT = "imageHeight";
    private static final String IMAGE_URL = "imageUrl";
    private static final String IMAGE_URL_SUBDOMAINS = "imageUrlSubdomains";
    private static final String IMAGE_WIDTH = "imageWidth";
    private static final String ZOOM_MAX = "ZoomMax";
    private static final String ZOOM_MIN = "ZoomMin";
    public String copyright = "";
    public int m_imageHeight = 256;
    public String m_imageUrl;
    public String[] m_imageUrlSubdomains;
    public int m_imageWidth = 256;
    public boolean m_isInitialised = false;
    private int m_subdomainsCounter = 0;
    public int m_zoomMax = 22;
    public int m_zoomMin = 1;

    public static ImageryMetaDataResource getDefaultInstance() {
        return new ImageryMetaDataResource();
    }

    public static ImageryMetaDataResource getInstanceFromJSON(JSONObject jSONObject, JSONObject jSONObject2) throws Exception {
        ImageryMetaDataResource imageryMetaDataResource = new ImageryMetaDataResource();
        if (jSONObject != null) {
            imageryMetaDataResource.copyright = jSONObject2.getString(COPYRIGHT);
            if (jSONObject.has(IMAGE_HEIGHT)) {
                imageryMetaDataResource.m_imageHeight = jSONObject.getInt(IMAGE_HEIGHT);
            }
            if (jSONObject.has(IMAGE_WIDTH)) {
                imageryMetaDataResource.m_imageWidth = jSONObject.getInt(IMAGE_WIDTH);
            }
            if (jSONObject.has(ZOOM_MIN)) {
                imageryMetaDataResource.m_zoomMin = jSONObject.getInt(ZOOM_MIN);
            }
            if (jSONObject.has(ZOOM_MAX)) {
                imageryMetaDataResource.m_zoomMax = jSONObject.getInt(ZOOM_MAX);
            }
            String string = jSONObject.getString(IMAGE_URL);
            imageryMetaDataResource.m_imageUrl = string;
            if (string != null && string.matches(".*?\\{.*?\\}.*?")) {
                imageryMetaDataResource.m_imageUrl = imageryMetaDataResource.m_imageUrl.replaceAll("\\{.*?\\}", "%s");
            }
            JSONArray jSONArray = jSONObject.getJSONArray(IMAGE_URL_SUBDOMAINS);
            if (jSONArray != null && jSONArray.length() >= 1) {
                imageryMetaDataResource.m_imageUrlSubdomains = new String[jSONArray.length()];
                for (int i = 0; i < jSONArray.length(); i++) {
                    imageryMetaDataResource.m_imageUrlSubdomains[i] = jSONArray.getString(i);
                }
            }
            imageryMetaDataResource.m_isInitialised = true;
            return imageryMetaDataResource;
        }
        throw new Exception("JSON to parse is null");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001b, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getSubDomain() {
        /*
            r3 = this;
            monitor-enter(r3)
            java.lang.String[] r0 = r3.m_imageUrlSubdomains     // Catch:{ all -> 0x001f }
            if (r0 == 0) goto L_0x001c
            int r1 = r0.length     // Catch:{ all -> 0x001f }
            if (r1 > 0) goto L_0x0009
            goto L_0x001c
        L_0x0009:
            int r1 = r3.m_subdomainsCounter     // Catch:{ all -> 0x001f }
            r2 = r0[r1]     // Catch:{ all -> 0x001f }
            int r0 = r0.length     // Catch:{ all -> 0x001f }
            int r0 = r0 + -1
            if (r1 >= r0) goto L_0x0017
            int r1 = r1 + 1
            r3.m_subdomainsCounter = r1     // Catch:{ all -> 0x001f }
            goto L_0x001a
        L_0x0017:
            r0 = 0
            r3.m_subdomainsCounter = r0     // Catch:{ all -> 0x001f }
        L_0x001a:
            monitor-exit(r3)
            return r2
        L_0x001c:
            r0 = 0
            monitor-exit(r3)
            return r0
        L_0x001f:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.tilesource.bing.ImageryMetaDataResource.getSubDomain():java.lang.String");
    }
}
