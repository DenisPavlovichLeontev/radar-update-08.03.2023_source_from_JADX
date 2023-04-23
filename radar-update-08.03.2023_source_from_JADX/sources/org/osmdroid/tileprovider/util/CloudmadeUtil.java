package org.osmdroid.tileprovider.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

public class CloudmadeUtil {
    private static final String CLOUDMADE_ID = "CLOUDMADE_ID";
    private static final String CLOUDMADE_KEY = "CLOUDMADE_KEY";
    private static final String CLOUDMADE_TOKEN = "CLOUDMADE_TOKEN";
    public static boolean DEBUGMODE = false;
    private static String mAndroidId = "android_id";
    private static String mKey = "";
    private static SharedPreferences.Editor mPreferenceEditor = null;
    private static String mToken = "";

    public static void retrieveCloudmadeKey(Context context) {
        mAndroidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
        mKey = ManifestUtil.retrieveKey(context, CLOUDMADE_KEY);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mPreferenceEditor = defaultSharedPreferences.edit();
        if (defaultSharedPreferences.getString(CLOUDMADE_ID, "").equals(mAndroidId)) {
            String string = defaultSharedPreferences.getString(CLOUDMADE_TOKEN, "");
            mToken = string;
            if (string.length() > 0) {
                mPreferenceEditor = null;
                return;
            }
            return;
        }
        mPreferenceEditor.putString(CLOUDMADE_ID, mAndroidId);
        mPreferenceEditor.commit();
    }

    public static String getCloudmadeKey() {
        return mKey;
    }

    public static void setCloudmadeKey(String str) {
        mKey = str;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v7, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v12, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v9, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v10, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v23, resolved type: java.io.InputStreamReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v11, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v24, resolved type: java.io.InputStreamReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v18, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v27, resolved type: java.io.InputStreamReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v28, resolved type: java.io.InputStreamReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v29, resolved type: java.io.InputStreamReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v30, resolved type: java.io.InputStreamReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v31, resolved type: java.io.InputStreamReader} */
    /* JADX WARNING: type inference failed for: r3v0, types: [java.io.InputStreamReader] */
    /* JADX WARNING: type inference failed for: r2v5, types: [java.net.HttpURLConnection] */
    /* JADX WARNING: type inference failed for: r3v1 */
    /* JADX WARNING: type inference failed for: r2v6, types: [java.net.HttpURLConnection] */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r3v8, types: [java.io.InputStreamReader] */
    /* JADX WARNING: type inference failed for: r2v12 */
    /* JADX WARNING: type inference failed for: r2v13 */
    /* JADX WARNING: type inference failed for: r2v15 */
    /* JADX WARNING: type inference failed for: r2v17 */
    /* JADX WARNING: type inference failed for: r3v26 */
    /* JADX WARNING: type inference failed for: r2v19 */
    /* JADX WARNING: type inference failed for: r2v20 */
    /* JADX WARNING: type inference failed for: r2v21 */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:76|(2:78|79)|(2:82|83)|(2:86|87)|88|89) */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x012c, code lost:
        if (r3 == null) goto L_0x0177;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0163, code lost:
        if (r3 != 0) goto L_0x012e;
     */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Missing exception handler attribute for start block: B:88:0x0176 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x015b A[SYNTHETIC, Splitter:B:68:0x015b] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0160 A[SYNTHETIC, Splitter:B:72:0x0160] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0169 A[SYNTHETIC, Splitter:B:78:0x0169] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x016e A[SYNTHETIC, Splitter:B:82:0x016e] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0173 A[SYNTHETIC, Splitter:B:86:0x0173] */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getCloudmadeToken() {
        /*
            java.lang.String r0 = mToken
            int r0 = r0.length()
            if (r0 != 0) goto L_0x017c
            java.lang.String r0 = mToken
            monitor-enter(r0)
            java.lang.String r1 = mToken     // Catch:{ all -> 0x0179 }
            int r1 = r1.length()     // Catch:{ all -> 0x0179 }
            if (r1 != 0) goto L_0x0177
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0179 }
            r1.<init>()     // Catch:{ all -> 0x0179 }
            java.lang.String r2 = "https://auth.cloudmade.com/token/"
            r1.append(r2)     // Catch:{ all -> 0x0179 }
            java.lang.String r2 = mKey     // Catch:{ all -> 0x0179 }
            r1.append(r2)     // Catch:{ all -> 0x0179 }
            java.lang.String r2 = "?userid="
            r1.append(r2)     // Catch:{ all -> 0x0179 }
            java.lang.String r2 = mAndroidId     // Catch:{ all -> 0x0179 }
            r1.append(r2)     // Catch:{ all -> 0x0179 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0179 }
            r2 = 0
            java.net.URL r3 = new java.net.URL     // Catch:{ IOException -> 0x013f, all -> 0x013a }
            r3.<init>(r1)     // Catch:{ IOException -> 0x013f, all -> 0x013a }
            java.net.URLConnection r1 = r3.openConnection()     // Catch:{ IOException -> 0x013f, all -> 0x013a }
            java.net.HttpURLConnection r1 = (java.net.HttpURLConnection) r1     // Catch:{ IOException -> 0x013f, all -> 0x013a }
            r3 = 1
            r1.setDoOutput(r3)     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.lang.String r3 = "POST"
            r1.setRequestMethod(r3)     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.lang.String r3 = "Content-Type"
            java.lang.String r4 = "application/x-www-form-urlencoded"
            r1.setRequestProperty(r3, r4)     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            org.osmdroid.config.IConfigurationProvider r3 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.lang.String r3 = r3.getUserAgentHttpHeader()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            org.osmdroid.config.IConfigurationProvider r4 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.lang.String r4 = r4.getUserAgentValue()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            r1.setRequestProperty(r3, r4)     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            org.osmdroid.config.IConfigurationProvider r3 = org.osmdroid.config.Configuration.getInstance()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.util.Map r3 = r3.getAdditionalHttpRequestProperties()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.util.Set r3 = r3.entrySet()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
        L_0x006f:
            boolean r4 = r3.hasNext()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            if (r4 == 0) goto L_0x008b
            java.lang.Object r4 = r3.next()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.util.Map$Entry r4 = (java.util.Map.Entry) r4     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.lang.Object r5 = r4.getKey()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.lang.Object r4 = r4.getValue()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            r1.setRequestProperty(r5, r4)     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            goto L_0x006f
        L_0x008b:
            r1.connect()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            boolean r3 = DEBUGMODE     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            if (r3 == 0) goto L_0x00ac
            java.lang.String r3 = "OsmDroid"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            r4.<init>()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.lang.String r5 = "Response from Cloudmade auth: "
            r4.append(r5)     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.lang.String r5 = r1.getResponseMessage()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            r4.append(r5)     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            android.util.Log.d(r3, r4)     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
        L_0x00ac:
            int r3 = r1.getResponseCode()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            r4 = 200(0xc8, float:2.8E-43)
            if (r3 != r4) goto L_0x0121
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.io.InputStream r4 = r1.getInputStream()     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.lang.String r5 = "UTF-8"
            r3.<init>(r4, r5)     // Catch:{ IOException -> 0x0136, all -> 0x0132 }
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ IOException -> 0x011b, all -> 0x0115 }
            r5 = 8192(0x2000, float:1.14794E-41)
            r4.<init>(r3, r5)     // Catch:{ IOException -> 0x011b, all -> 0x0115 }
            java.lang.String r5 = r4.readLine()     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            boolean r6 = DEBUGMODE     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            if (r6 == 0) goto L_0x00e4
            java.lang.String r6 = "OsmDroid"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            r7.<init>()     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            java.lang.String r8 = "First line from Cloudmade auth: "
            r7.append(r8)     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            r7.append(r5)     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            java.lang.String r7 = r7.toString()     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            android.util.Log.d(r6, r7)     // Catch:{ IOException -> 0x0110, all -> 0x010a }
        L_0x00e4:
            java.lang.String r5 = r5.trim()     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            mToken = r5     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            int r5 = r5.length()     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            if (r5 <= 0) goto L_0x0101
            android.content.SharedPreferences$Editor r5 = mPreferenceEditor     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            java.lang.String r6 = "CLOUDMADE_TOKEN"
            java.lang.String r7 = mToken     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            r5.putString(r6, r7)     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            android.content.SharedPreferences$Editor r5 = mPreferenceEditor     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            r5.commit()     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            mPreferenceEditor = r2     // Catch:{ IOException -> 0x0110, all -> 0x010a }
            goto L_0x0108
        L_0x0101:
            java.lang.String r2 = "OsmDroid"
            java.lang.String r5 = "No authorization token received from Cloudmade"
            android.util.Log.e(r2, r5)     // Catch:{ IOException -> 0x0110, all -> 0x010a }
        L_0x0108:
            r2 = r4
            goto L_0x0122
        L_0x010a:
            r2 = move-exception
            r9 = r2
            r2 = r1
            r1 = r9
            goto L_0x0167
        L_0x0110:
            r2 = move-exception
            r9 = r2
            r2 = r1
            r1 = r9
            goto L_0x0143
        L_0x0115:
            r4 = move-exception
            r9 = r2
            r2 = r1
            r1 = r4
            r4 = r9
            goto L_0x0167
        L_0x011b:
            r4 = move-exception
            r9 = r2
            r2 = r1
            r1 = r4
            r4 = r9
            goto L_0x0143
        L_0x0121:
            r3 = r2
        L_0x0122:
            if (r1 == 0) goto L_0x0127
            r1.disconnect()     // Catch:{ Exception -> 0x0127 }
        L_0x0127:
            if (r2 == 0) goto L_0x012c
            r2.close()     // Catch:{ Exception -> 0x012c }
        L_0x012c:
            if (r3 == 0) goto L_0x0177
        L_0x012e:
            r3.close()     // Catch:{ Exception -> 0x0177 }
            goto L_0x0177
        L_0x0132:
            r3 = move-exception
            r4 = r2
            r2 = r1
            goto L_0x013c
        L_0x0136:
            r3 = move-exception
            r4 = r2
            r2 = r1
            goto L_0x0141
        L_0x013a:
            r3 = move-exception
            r4 = r2
        L_0x013c:
            r1 = r3
            r3 = r4
            goto L_0x0167
        L_0x013f:
            r3 = move-exception
            r4 = r2
        L_0x0141:
            r1 = r3
            r3 = r4
        L_0x0143:
            java.lang.String r5 = "OsmDroid"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0166 }
            r6.<init>()     // Catch:{ all -> 0x0166 }
            java.lang.String r7 = "No authorization token received from Cloudmade: "
            r6.append(r7)     // Catch:{ all -> 0x0166 }
            r6.append(r1)     // Catch:{ all -> 0x0166 }
            java.lang.String r1 = r6.toString()     // Catch:{ all -> 0x0166 }
            android.util.Log.e(r5, r1)     // Catch:{ all -> 0x0166 }
            if (r2 == 0) goto L_0x015e
            r2.disconnect()     // Catch:{ Exception -> 0x015e }
        L_0x015e:
            if (r4 == 0) goto L_0x0163
            r4.close()     // Catch:{ Exception -> 0x0163 }
        L_0x0163:
            if (r3 == 0) goto L_0x0177
            goto L_0x012e
        L_0x0166:
            r1 = move-exception
        L_0x0167:
            if (r2 == 0) goto L_0x016c
            r2.disconnect()     // Catch:{ Exception -> 0x016c }
        L_0x016c:
            if (r4 == 0) goto L_0x0171
            r4.close()     // Catch:{ Exception -> 0x0171 }
        L_0x0171:
            if (r3 == 0) goto L_0x0176
            r3.close()     // Catch:{ Exception -> 0x0176 }
        L_0x0176:
            throw r1     // Catch:{ all -> 0x0179 }
        L_0x0177:
            monitor-exit(r0)     // Catch:{ all -> 0x0179 }
            goto L_0x017c
        L_0x0179:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0179 }
            throw r1
        L_0x017c:
            java.lang.String r0 = mToken
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.util.CloudmadeUtil.getCloudmadeToken():java.lang.String");
    }
}
