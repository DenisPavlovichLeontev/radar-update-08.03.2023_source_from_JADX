package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;

public class TileDownloader {
    private boolean compatibilitySocketFactorySet;

    public Drawable downloadTile(long j, IFilesystemCache iFilesystemCache, OnlineTileSourceBase onlineTileSourceBase) throws CantContinueException {
        return downloadTile(j, 0, onlineTileSourceBase.getTileURLString(j), iFilesystemCache, onlineTileSourceBase);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v0, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v1, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v1, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v2, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v2, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v3, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v6, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v6, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v7, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v8, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v9, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v9, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v10, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v10, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v11, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v12, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v12, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v13, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v14, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v13, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v13, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v12, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v14, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v15, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v13, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v15, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v16, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v15, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v16, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v18, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v19, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v15, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v20, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v19, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v21, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v22, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v16, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v22, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v23, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v20, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v23, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v21, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v24, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v25, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v17, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v25, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v26, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v22, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v27, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v26, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v30, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v27, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v18, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v28, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v29, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v19, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v32, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v33, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v34, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v28, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v31, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v32, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v20, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v21, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v22, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v23, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v24, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v25, resolved type: java.io.ByteArrayInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v50, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v52, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v53, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v47, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v47, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v35, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v50, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v52, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v40, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v61, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v53, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v55, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v43, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v57, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v59, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v68, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v60, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v58, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v63, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v61, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v71, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v65, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v64, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v66, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v65, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v66, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v72, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v67, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v67, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v68, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v68, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v69, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v70, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v71, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v72, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v73, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v74, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v75, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v76, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v77, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v78, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v79, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v80, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v81, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v82, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v83, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v84, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v85, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v72, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v73, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v74, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v75, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v76, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v77, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v78, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v79, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v80, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v81, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v82, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v83, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v84, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v85, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v86, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v75, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v78, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v79, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v80, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v83, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v84, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v49, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v50, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v51, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v52, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v53, resolved type: java.lang.String} */
    /* JADX WARNING: type inference failed for: r10v0, types: [java.net.HttpURLConnection, java.lang.Throwable, java.io.Closeable, android.graphics.drawable.Drawable] */
    /* JADX WARNING: type inference failed for: r7v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r3v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r1v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r10v1 */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: type inference failed for: r7v10, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r3v11, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r2v11, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r1v12, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r10v2 */
    /* JADX WARNING: type inference failed for: r3v33, types: [java.io.OutputStream, java.io.Closeable, java.io.BufferedOutputStream] */
    /* JADX WARNING: type inference failed for: r7v34 */
    /* JADX WARNING: type inference failed for: r3v54 */
    /* JADX WARNING: type inference failed for: r2v46 */
    /* JADX WARNING: type inference failed for: r1v46 */
    /* JADX WARNING: type inference failed for: r7v36 */
    /* JADX WARNING: type inference failed for: r3v56 */
    /* JADX WARNING: type inference failed for: r2v48 */
    /* JADX WARNING: type inference failed for: r7v38 */
    /* JADX WARNING: type inference failed for: r3v58 */
    /* JADX WARNING: type inference failed for: r2v50 */
    /* JADX WARNING: type inference failed for: r1v48 */
    /* JADX WARNING: type inference failed for: r7v39 */
    /* JADX WARNING: type inference failed for: r3v59 */
    /* JADX WARNING: type inference failed for: r2v51 */
    /* JADX WARNING: type inference failed for: r1v49 */
    /* JADX WARNING: type inference failed for: r7v42 */
    /* JADX WARNING: type inference failed for: r3v62 */
    /* JADX WARNING: type inference failed for: r2v54 */
    /* JADX WARNING: type inference failed for: r1v52 */
    /* JADX WARNING: type inference failed for: r7v45 */
    /* JADX WARNING: type inference failed for: r3v65 */
    /* JADX WARNING: type inference failed for: r2v57 */
    /* JADX WARNING: type inference failed for: r1v55 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:180:0x0372=Splitter:B:180:0x0372, B:173:0x033d=Splitter:B:173:0x033d, B:196:0x03ff=Splitter:B:196:0x03ff} */
    /* JADX WARNING: Unknown variable types count: 9 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.drawable.Drawable downloadTile(long r21, int r23, java.lang.String r24, org.osmdroid.tileprovider.modules.IFilesystemCache r25, org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase r26) throws org.osmdroid.tileprovider.modules.CantContinueException {
        /*
            r20 = this;
            r8 = r20
            r1 = r23
            r2 = r24
            java.lang.String r3 = "https://"
            java.lang.String r9 = " : "
            r10 = 0
            r4 = 3
            if (r1 <= r4) goto L_0x000f
            return r10
        L_0x000f:
            org.osmdroid.tileprovider.tilesource.TileSourcePolicy r4 = r26.getTileSourcePolicy()
            boolean r4 = r4.normalizesUserAgent()
            if (r4 == 0) goto L_0x0022
            org.osmdroid.config.IConfigurationProvider r4 = org.osmdroid.config.Configuration.getInstance()
            java.lang.String r4 = r4.getNormalizedUserAgent()
            goto L_0x0023
        L_0x0022:
            r4 = r10
        L_0x0023:
            if (r4 != 0) goto L_0x002d
            org.osmdroid.config.IConfigurationProvider r4 = org.osmdroid.config.Configuration.getInstance()
            java.lang.String r4 = r4.getUserAgentValue()
        L_0x002d:
            org.osmdroid.tileprovider.tilesource.TileSourcePolicy r5 = r26.getTileSourcePolicy()
            boolean r5 = r5.acceptsUserAgent(r4)
            java.lang.String r11 = "OsmDroid"
            if (r5 != 0) goto L_0x004e
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Please configure a relevant user agent; current value is: "
            r1.append(r2)
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            android.util.Log.e(r11, r1)
            return r10
        L_0x004e:
            r12 = 1
            org.osmdroid.config.IConfigurationProvider r5 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            boolean r5 = r5.isDebugMode()     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            if (r5 == 0) goto L_0x006d
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            r5.<init>()     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            java.lang.String r6 = "Downloading Maptile from url: "
            r5.append(r6)     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            r5.append(r2)     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            java.lang.String r5 = r5.toString()     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            android.util.Log.d(r11, r5)     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
        L_0x006d:
            boolean r5 = android.text.TextUtils.isEmpty(r24)     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            if (r5 != 0) goto L_0x0325
            int r5 = android.os.Build.VERSION.SDK_INT     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            r6 = 20
            if (r5 >= r6) goto L_0x008b
            boolean r5 = r8.compatibilitySocketFactorySet     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            if (r5 != 0) goto L_0x008b
            org.osmdroid.tileprovider.modules.TileDownloader$CompatibilitySocketFactory r5 = new org.osmdroid.tileprovider.modules.TileDownloader$CompatibilitySocketFactory     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            javax.net.ssl.SSLSocketFactory r6 = javax.net.ssl.HttpsURLConnection.getDefaultSSLSocketFactory()     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            r5.<init>(r6)     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(r5)     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            r8.compatibilitySocketFactorySet = r12     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
        L_0x008b:
            org.osmdroid.config.IConfigurationProvider r5 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            java.net.Proxy r5 = r5.getHttpProxy()     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            if (r5 == 0) goto L_0x00a9
            java.net.URL r5 = new java.net.URL     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            r5.<init>(r2)     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            org.osmdroid.config.IConfigurationProvider r6 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            java.net.Proxy r6 = r6.getHttpProxy()     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            java.net.URLConnection r5 = r5.openConnection(r6)     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            java.net.HttpURLConnection r5 = (java.net.HttpURLConnection) r5     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            goto L_0x00b4
        L_0x00a9:
            java.net.URL r5 = new java.net.URL     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            r5.<init>(r2)     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            java.net.URLConnection r5 = r5.openConnection()     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
            java.net.HttpURLConnection r5 = (java.net.HttpURLConnection) r5     // Catch:{ UnknownHostException -> 0x03f8, LowMemoryException -> 0x03c5, FileNotFoundException -> 0x0396, IOException -> 0x036b, all -> 0x0336 }
        L_0x00b4:
            r13 = r5
            r13.setUseCaches(r12)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            org.osmdroid.config.IConfigurationProvider r5 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r5 = r5.getUserAgentHttpHeader()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r13.setRequestProperty(r5, r4)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            org.osmdroid.config.IConfigurationProvider r4 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.util.Map r4 = r4.getAdditionalHttpRequestProperties()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.util.Set r4 = r4.entrySet()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.util.Iterator r4 = r4.iterator()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
        L_0x00d3:
            boolean r5 = r4.hasNext()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            if (r5 == 0) goto L_0x00ef
            java.lang.Object r5 = r4.next()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.util.Map$Entry r5 = (java.util.Map.Entry) r5     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.Object r6 = r5.getKey()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.Object r5 = r5.getValue()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r13.setRequestProperty(r6, r5)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            goto L_0x00d3
        L_0x00ef:
            r13.connect()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            int r4 = r13.getResponseCode()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r5 = 200(0xc8, float:2.8E-43)
            if (r4 == r5) goto L_0x0207
            int r4 = r13.getResponseCode()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r5 = 301(0x12d, float:4.22E-43)
            java.lang.String r6 = " HTTP response: "
            if (r4 == r5) goto L_0x0112
            r5 = 302(0x12e, float:4.23E-43)
            if (r4 == r5) goto L_0x0112
            r5 = 307(0x133, float:4.3E-43)
            if (r4 == r5) goto L_0x0112
            r5 = 308(0x134, float:4.32E-43)
            if (r4 == r5) goto L_0x0112
            goto L_0x01bf
        L_0x0112:
            org.osmdroid.config.IConfigurationProvider r4 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            boolean r4 = r4.isMapTileDownloaderFollowRedirects()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            if (r4 == 0) goto L_0x01bf
            java.lang.String r4 = "Location"
            java.lang.String r4 = r13.getHeaderField(r4)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            if (r4 == 0) goto L_0x0207
            java.lang.String r5 = "/"
            boolean r5 = r4.startsWith(r5)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            if (r5 == 0) goto L_0x0176
            java.net.URL r5 = new java.net.URL     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r5.<init>(r2)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            int r7 = r5.getPort()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r14 = r24.toLowerCase()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            boolean r14 = r14.startsWith(r3)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r15 = -1
            if (r7 != r15) goto L_0x0151
            java.lang.String r2 = r24.toLowerCase()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r7 = "http://"
            boolean r2 = r2.startsWith(r7)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            if (r2 == 0) goto L_0x014f
            r7 = 80
            goto L_0x0151
        L_0x014f:
            r7 = 443(0x1bb, float:6.21E-43)
        L_0x0151:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r2.<init>()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            if (r14 == 0) goto L_0x0159
            goto L_0x015b
        L_0x0159:
            java.lang.String r3 = "http"
        L_0x015b:
            r2.append(r3)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r3 = r5.getHost()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r2.append(r3)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r3 = ":"
            r2.append(r3)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r2.append(r7)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r2.append(r4)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r2 = r2.toString()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r5 = r2
            goto L_0x0177
        L_0x0176:
            r5 = r4
        L_0x0177:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r2.<init>()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r3 = "Http redirect for MapTile: "
            r2.append(r3)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r3 = org.osmdroid.util.MapTileIndex.toString(r21)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r2.append(r3)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r2.append(r6)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r3 = r13.getResponseMessage()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r2.append(r3)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r3 = " to url "
            r2.append(r3)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r2.append(r5)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r2 = r2.toString()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            android.util.Log.i(r11, r2)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            int r4 = r1 + 1
            r1 = r20
            r2 = r21
            r6 = r25
            r7 = r26
            android.graphics.drawable.Drawable r1 = r1.downloadTile(r2, r4, r5, r6, r7)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r10)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r10)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r10)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r10)
            r13.disconnect()     // Catch:{ Exception -> 0x01be }
        L_0x01be:
            return r1
        L_0x01bf:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r1.<init>()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r3 = "Problem downloading MapTile: "
            r1.append(r3)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r3 = org.osmdroid.util.MapTileIndex.toString(r21)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r1.append(r3)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r1.append(r6)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r3 = r13.getResponseMessage()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r1.append(r3)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r1 = r1.toString()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            android.util.Log.w(r11, r1)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            org.osmdroid.config.IConfigurationProvider r1 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            boolean r1 = r1.isDebugMapTileDownloader()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            if (r1 == 0) goto L_0x01ee
            android.util.Log.d(r11, r2)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
        L_0x01ee:
            int r1 = org.osmdroid.tileprovider.util.Counters.tileDownloadErrors     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            int r1 = r1 + r12
            org.osmdroid.tileprovider.util.Counters.tileDownloadErrors = r1     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.io.InputStream r1 = r13.getErrorStream()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r1)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r10)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r10)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r10)
            r13.disconnect()     // Catch:{ Exception -> 0x0206 }
        L_0x0206:
            return r10
        L_0x0207:
            java.lang.String r1 = "Content-Type"
            java.lang.String r1 = r13.getHeaderField(r1)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            org.osmdroid.config.IConfigurationProvider r3 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            boolean r3 = r3.isDebugMapTileDownloader()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            if (r3 == 0) goto L_0x022e
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r3.<init>()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r3.append(r2)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r4 = " success, mime is "
            r3.append(r4)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r3.append(r1)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r3 = r3.toString()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            android.util.Log.d(r11, r3)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
        L_0x022e:
            if (r1 == 0) goto L_0x0253
            java.lang.String r3 = r1.toLowerCase()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r4 = "image"
            boolean r3 = r3.contains(r4)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            if (r3 != 0) goto L_0x0253
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r3.<init>()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r3.append(r2)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r2 = " success, however the mime type does not appear to be an image "
            r3.append(r2)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            r3.append(r1)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.lang.String r1 = r3.toString()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            android.util.Log.w(r11, r1)     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
        L_0x0253:
            java.io.InputStream r1 = r13.getInputStream()     // Catch:{ UnknownHostException -> 0x031d, LowMemoryException -> 0x0316, FileNotFoundException -> 0x030e, IOException -> 0x0306, all -> 0x02ff }
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ UnknownHostException -> 0x02fb, LowMemoryException -> 0x02f3, FileNotFoundException -> 0x02ef, IOException -> 0x02eb, all -> 0x02e7 }
            r2.<init>()     // Catch:{ UnknownHostException -> 0x02fb, LowMemoryException -> 0x02f3, FileNotFoundException -> 0x02ef, IOException -> 0x02eb, all -> 0x02e7 }
            java.io.BufferedOutputStream r3 = new java.io.BufferedOutputStream     // Catch:{ UnknownHostException -> 0x02e2, LowMemoryException -> 0x02df, FileNotFoundException -> 0x02da, IOException -> 0x02d6, all -> 0x02d2 }
            r4 = 8192(0x2000, float:1.14794E-41)
            r3.<init>(r2, r4)     // Catch:{ UnknownHostException -> 0x02e2, LowMemoryException -> 0x02df, FileNotFoundException -> 0x02da, IOException -> 0x02d6, all -> 0x02d2 }
            org.osmdroid.tileprovider.tilesource.TileSourcePolicy r4 = r26.getTileSourcePolicy()     // Catch:{ UnknownHostException -> 0x02cd, LowMemoryException -> 0x02ca, FileNotFoundException -> 0x02c5, IOException -> 0x02c0, all -> 0x02bb }
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ UnknownHostException -> 0x02cd, LowMemoryException -> 0x02ca, FileNotFoundException -> 0x02c5, IOException -> 0x02c0, all -> 0x02bb }
            long r4 = r4.computeExpirationTime(r13, r5)     // Catch:{ UnknownHostException -> 0x02cd, LowMemoryException -> 0x02ca, FileNotFoundException -> 0x02c5, IOException -> 0x02c0, all -> 0x02bb }
            org.osmdroid.tileprovider.util.StreamUtils.copy(r1, r3)     // Catch:{ UnknownHostException -> 0x02cd, LowMemoryException -> 0x02ca, FileNotFoundException -> 0x02c5, IOException -> 0x02c0, all -> 0x02bb }
            r3.flush()     // Catch:{ UnknownHostException -> 0x02cd, LowMemoryException -> 0x02ca, FileNotFoundException -> 0x02c5, IOException -> 0x02c0, all -> 0x02bb }
            byte[] r6 = r2.toByteArray()     // Catch:{ UnknownHostException -> 0x02cd, LowMemoryException -> 0x02ca, FileNotFoundException -> 0x02c5, IOException -> 0x02c0, all -> 0x02bb }
            java.io.ByteArrayInputStream r7 = new java.io.ByteArrayInputStream     // Catch:{ UnknownHostException -> 0x02cd, LowMemoryException -> 0x02ca, FileNotFoundException -> 0x02c5, IOException -> 0x02c0, all -> 0x02bb }
            r7.<init>(r6)     // Catch:{ UnknownHostException -> 0x02cd, LowMemoryException -> 0x02ca, FileNotFoundException -> 0x02c5, IOException -> 0x02c0, all -> 0x02bb }
            if (r25 == 0) goto L_0x0292
            java.lang.Long r19 = java.lang.Long.valueOf(r4)     // Catch:{ UnknownHostException -> 0x02b7, LowMemoryException -> 0x02b4, FileNotFoundException -> 0x02b0, IOException -> 0x02ac, all -> 0x02a8 }
            r14 = r25
            r15 = r26
            r16 = r21
            r18 = r7
            r14.saveFile(r15, r16, r18, r19)     // Catch:{ UnknownHostException -> 0x02b7, LowMemoryException -> 0x02b4, FileNotFoundException -> 0x02b0, IOException -> 0x02ac, all -> 0x02a8 }
            r7.reset()     // Catch:{ UnknownHostException -> 0x02b7, LowMemoryException -> 0x02b4, FileNotFoundException -> 0x02b0, IOException -> 0x02ac, all -> 0x02a8 }
        L_0x0292:
            r4 = r26
            android.graphics.drawable.Drawable r4 = r4.getDrawable((java.io.InputStream) r7)     // Catch:{ UnknownHostException -> 0x02b7, LowMemoryException -> 0x02b4, FileNotFoundException -> 0x02b0, IOException -> 0x02ac, all -> 0x02a8 }
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r1)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r3)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r7)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r2)
            r13.disconnect()     // Catch:{ Exception -> 0x02a7 }
        L_0x02a7:
            return r4
        L_0x02a8:
            r0 = move-exception
            r4 = r0
            goto L_0x033d
        L_0x02ac:
            r0 = move-exception
            r4 = r0
            goto L_0x0372
        L_0x02b0:
            r0 = move-exception
            r4 = r0
            goto L_0x039d
        L_0x02b4:
            r0 = move-exception
            goto L_0x02f7
        L_0x02b7:
            r0 = move-exception
            r4 = r0
            goto L_0x03ff
        L_0x02bb:
            r0 = move-exception
            r4 = r0
            r7 = r10
            goto L_0x033d
        L_0x02c0:
            r0 = move-exception
            r4 = r0
            r7 = r10
            goto L_0x0372
        L_0x02c5:
            r0 = move-exception
            r4 = r0
            r7 = r10
            goto L_0x039d
        L_0x02ca:
            r0 = move-exception
            r7 = r10
            goto L_0x02f7
        L_0x02cd:
            r0 = move-exception
            r4 = r0
            r7 = r10
            goto L_0x03ff
        L_0x02d2:
            r0 = move-exception
            r4 = r0
            r3 = r10
            goto L_0x0304
        L_0x02d6:
            r0 = move-exception
            r4 = r0
            r3 = r10
            goto L_0x030b
        L_0x02da:
            r0 = move-exception
            r4 = r0
            r3 = r10
            goto L_0x0313
        L_0x02df:
            r0 = move-exception
            r3 = r10
            goto L_0x02f6
        L_0x02e2:
            r0 = move-exception
            r4 = r0
            r3 = r10
            goto L_0x0322
        L_0x02e7:
            r0 = move-exception
            r4 = r0
            r2 = r10
            goto L_0x0303
        L_0x02eb:
            r0 = move-exception
            r4 = r0
            r2 = r10
            goto L_0x030a
        L_0x02ef:
            r0 = move-exception
            r4 = r0
            r2 = r10
            goto L_0x0312
        L_0x02f3:
            r0 = move-exception
            r2 = r10
            r3 = r2
        L_0x02f6:
            r7 = r3
        L_0x02f7:
            r10 = r1
            r1 = r0
            goto L_0x03cb
        L_0x02fb:
            r0 = move-exception
            r4 = r0
            r2 = r10
            goto L_0x0321
        L_0x02ff:
            r0 = move-exception
            r4 = r0
            r1 = r10
            r2 = r1
        L_0x0303:
            r3 = r2
        L_0x0304:
            r7 = r3
            goto L_0x033d
        L_0x0306:
            r0 = move-exception
            r4 = r0
            r1 = r10
            r2 = r1
        L_0x030a:
            r3 = r2
        L_0x030b:
            r7 = r3
            goto L_0x0372
        L_0x030e:
            r0 = move-exception
            r4 = r0
            r1 = r10
            r2 = r1
        L_0x0312:
            r3 = r2
        L_0x0313:
            r7 = r3
            goto L_0x039d
        L_0x0316:
            r0 = move-exception
            r1 = r0
            r2 = r10
            r3 = r2
            r7 = r3
            goto L_0x03cb
        L_0x031d:
            r0 = move-exception
            r4 = r0
            r1 = r10
            r2 = r1
        L_0x0321:
            r3 = r2
        L_0x0322:
            r7 = r3
            goto L_0x03ff
        L_0x0325:
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r10)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r10)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r10)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r10)
            r10.disconnect()     // Catch:{ Exception -> 0x0335 }
            throw r10     // Catch:{ Exception -> 0x0335 }
        L_0x0335:
            return r10
        L_0x0336:
            r0 = move-exception
            r4 = r0
            r1 = r10
            r2 = r1
            r3 = r2
            r7 = r3
            r13 = r7
        L_0x033d:
            int r5 = org.osmdroid.tileprovider.util.Counters.tileDownloadErrors     // Catch:{ all -> 0x03c1 }
            int r5 = r5 + r12
            org.osmdroid.tileprovider.util.Counters.tileDownloadErrors = r5     // Catch:{ all -> 0x03c1 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x03c1 }
            r5.<init>()     // Catch:{ all -> 0x03c1 }
            java.lang.String r6 = "Error downloading MapTile: "
            r5.append(r6)     // Catch:{ all -> 0x03c1 }
            java.lang.String r6 = org.osmdroid.util.MapTileIndex.toString(r21)     // Catch:{ all -> 0x03c1 }
            r5.append(r6)     // Catch:{ all -> 0x03c1 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x03c1 }
            android.util.Log.e(r11, r5, r4)     // Catch:{ all -> 0x03c1 }
        L_0x035a:
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r1)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r3)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r7)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r2)
            r13.disconnect()     // Catch:{ Exception -> 0x0424 }
            goto L_0x0424
        L_0x036b:
            r0 = move-exception
            r4 = r0
            r1 = r10
            r2 = r1
            r3 = r2
            r7 = r3
            r13 = r7
        L_0x0372:
            int r5 = org.osmdroid.tileprovider.util.Counters.tileDownloadErrors     // Catch:{ all -> 0x03c1 }
            int r5 = r5 + r12
            org.osmdroid.tileprovider.util.Counters.tileDownloadErrors = r5     // Catch:{ all -> 0x03c1 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x03c1 }
            r5.<init>()     // Catch:{ all -> 0x03c1 }
            java.lang.String r6 = "IOException downloading MapTile: "
            r5.append(r6)     // Catch:{ all -> 0x03c1 }
            java.lang.String r6 = org.osmdroid.util.MapTileIndex.toString(r21)     // Catch:{ all -> 0x03c1 }
            r5.append(r6)     // Catch:{ all -> 0x03c1 }
            r5.append(r9)     // Catch:{ all -> 0x03c1 }
            r5.append(r4)     // Catch:{ all -> 0x03c1 }
            java.lang.String r4 = r5.toString()     // Catch:{ all -> 0x03c1 }
            android.util.Log.w(r11, r4)     // Catch:{ all -> 0x03c1 }
            goto L_0x035a
        L_0x0396:
            r0 = move-exception
            r4 = r0
            r1 = r10
            r2 = r1
            r3 = r2
            r7 = r3
            r13 = r7
        L_0x039d:
            int r5 = org.osmdroid.tileprovider.util.Counters.tileDownloadErrors     // Catch:{ all -> 0x03c1 }
            int r5 = r5 + r12
            org.osmdroid.tileprovider.util.Counters.tileDownloadErrors = r5     // Catch:{ all -> 0x03c1 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x03c1 }
            r5.<init>()     // Catch:{ all -> 0x03c1 }
            java.lang.String r6 = "Tile not found: "
            r5.append(r6)     // Catch:{ all -> 0x03c1 }
            java.lang.String r6 = org.osmdroid.util.MapTileIndex.toString(r21)     // Catch:{ all -> 0x03c1 }
            r5.append(r6)     // Catch:{ all -> 0x03c1 }
            r5.append(r9)     // Catch:{ all -> 0x03c1 }
            r5.append(r4)     // Catch:{ all -> 0x03c1 }
            java.lang.String r4 = r5.toString()     // Catch:{ all -> 0x03c1 }
            android.util.Log.w(r11, r4)     // Catch:{ all -> 0x03c1 }
            goto L_0x035a
        L_0x03c1:
            r0 = move-exception
            r4 = r0
            goto L_0x0425
        L_0x03c5:
            r0 = move-exception
            r1 = r0
            r2 = r10
            r3 = r2
            r7 = r3
            r13 = r7
        L_0x03cb:
            int r4 = org.osmdroid.tileprovider.util.Counters.countOOM     // Catch:{ all -> 0x03f4 }
            int r4 = r4 + r12
            org.osmdroid.tileprovider.util.Counters.countOOM = r4     // Catch:{ all -> 0x03f4 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x03f4 }
            r4.<init>()     // Catch:{ all -> 0x03f4 }
            java.lang.String r5 = "LowMemoryException downloading MapTile: "
            r4.append(r5)     // Catch:{ all -> 0x03f4 }
            java.lang.String r5 = org.osmdroid.util.MapTileIndex.toString(r21)     // Catch:{ all -> 0x03f4 }
            r4.append(r5)     // Catch:{ all -> 0x03f4 }
            r4.append(r9)     // Catch:{ all -> 0x03f4 }
            r4.append(r1)     // Catch:{ all -> 0x03f4 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x03f4 }
            android.util.Log.w(r11, r4)     // Catch:{ all -> 0x03f4 }
            org.osmdroid.tileprovider.modules.CantContinueException r4 = new org.osmdroid.tileprovider.modules.CantContinueException     // Catch:{ all -> 0x03f4 }
            r4.<init>((java.lang.Throwable) r1)     // Catch:{ all -> 0x03f4 }
            throw r4     // Catch:{ all -> 0x03f4 }
        L_0x03f4:
            r0 = move-exception
            r4 = r0
            r1 = r10
            goto L_0x0425
        L_0x03f8:
            r0 = move-exception
            r4 = r0
            r1 = r10
            r2 = r1
            r3 = r2
            r7 = r3
            r13 = r7
        L_0x03ff:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x03c1 }
            r5.<init>()     // Catch:{ all -> 0x03c1 }
            java.lang.String r6 = "UnknownHostException downloading MapTile: "
            r5.append(r6)     // Catch:{ all -> 0x03c1 }
            java.lang.String r6 = org.osmdroid.util.MapTileIndex.toString(r21)     // Catch:{ all -> 0x03c1 }
            r5.append(r6)     // Catch:{ all -> 0x03c1 }
            r5.append(r9)     // Catch:{ all -> 0x03c1 }
            r5.append(r4)     // Catch:{ all -> 0x03c1 }
            java.lang.String r4 = r5.toString()     // Catch:{ all -> 0x03c1 }
            android.util.Log.w(r11, r4)     // Catch:{ all -> 0x03c1 }
            int r4 = org.osmdroid.tileprovider.util.Counters.tileDownloadErrors     // Catch:{ all -> 0x03c1 }
            int r4 = r4 + r12
            org.osmdroid.tileprovider.util.Counters.tileDownloadErrors = r4     // Catch:{ all -> 0x03c1 }
            goto L_0x035a
        L_0x0424:
            return r10
        L_0x0425:
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r1)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r3)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r7)
            org.osmdroid.tileprovider.util.StreamUtils.closeStream(r2)
            r13.disconnect()     // Catch:{ Exception -> 0x0434 }
        L_0x0434:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.TileDownloader.downloadTile(long, int, java.lang.String, org.osmdroid.tileprovider.modules.IFilesystemCache, org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase):android.graphics.drawable.Drawable");
    }

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    private static class CompatibilitySocketFactory extends SSLSocketFactory {
        SSLSocketFactory sslSocketFactory;

        CompatibilitySocketFactory(SSLSocketFactory sSLSocketFactory) {
            this.sslSocketFactory = sSLSocketFactory;
        }

        public String[] getDefaultCipherSuites() {
            return this.sslSocketFactory.getDefaultCipherSuites();
        }

        public String[] getSupportedCipherSuites() {
            return this.sslSocketFactory.getSupportedCipherSuites();
        }

        public Socket createSocket() throws IOException {
            return upgradeTlsAndRemoveSsl((SSLSocket) this.sslSocketFactory.createSocket());
        }

        public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
            return upgradeTlsAndRemoveSsl((SSLSocket) this.sslSocketFactory.createSocket(socket, str, i, z));
        }

        public Socket createSocket(String str, int i) throws IOException, UnknownHostException {
            return upgradeTlsAndRemoveSsl((SSLSocket) this.sslSocketFactory.createSocket(str, i));
        }

        public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException, UnknownHostException {
            return upgradeTlsAndRemoveSsl((SSLSocket) this.sslSocketFactory.createSocket(str, i, inetAddress, i2));
        }

        public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
            return upgradeTlsAndRemoveSsl((SSLSocket) this.sslSocketFactory.createSocket(inetAddress, i));
        }

        public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
            return upgradeTlsAndRemoveSsl((SSLSocket) this.sslSocketFactory.createSocket(inetAddress, i, inetAddress2, i2));
        }

        private SSLSocket upgradeTlsAndRemoveSsl(SSLSocket sSLSocket) {
            String[] supportedProtocols = sSLSocket.getSupportedProtocols();
            String[] enabledProtocols = sSLSocket.getEnabledProtocols();
            if (Arrays.binarySearch(supportedProtocols, "TLSv1.2") >= 0) {
                enabledProtocols = new String[]{"TLSv1.2"};
            } else {
                int binarySearch = Arrays.binarySearch(enabledProtocols, "SSLv3");
                if (binarySearch >= 0) {
                    int length = enabledProtocols.length - 1;
                    String[] strArr = new String[length];
                    System.arraycopy(enabledProtocols, 0, strArr, 0, binarySearch);
                    if (length > binarySearch) {
                        System.arraycopy(enabledProtocols, binarySearch + 1, strArr, binarySearch, length - binarySearch);
                    }
                    enabledProtocols = strArr;
                }
            }
            sSLSocket.setEnabledProtocols(enabledProtocols);
            return sSLSocket;
        }
    }
}
