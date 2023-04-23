package com.online.radar.kaf;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class PaidUtil {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    PaidUtil() {
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Can't wrap try/catch for region: R(20:0|1|2|3|4|5|6|7|(1:9)|10|11|12|13|14|15|35|36|37|(3:38|39|(3:40|41|(7:43|44|45|71|72|162|81)(16:160|82|83|(2:85|(3:86|87|(8:89|90|116|117|118|120|166|130)(2:164|131)))|(1:133)|(1:135)|(1:137)|(1:139)|(1:141)|(1:143)|(1:145)|146|147|148|149|169)))|(1:(1:24))) */
    /* JADX WARNING: Code restructure failed: missing block: B:154:0x047d, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:155:0x047e, code lost:
        r16 = r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x006a */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x020c A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00e9 A[SYNTHETIC, Splitter:B:43:0x00e9] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String[] getKaf(float r38, float r39, android.content.Context r40) {
        /*
            r1 = r38
            r2 = r39
            java.lang.String r3 = "category_tariff"
            java.lang.String r4 = ""
            java.lang.String r5 = "service_levels"
            r6 = 9
            java.lang.String[] r7 = new java.lang.String[r6]
            java.lang.String r0 = "tariffs.json"
            r8 = r40
            java.io.FileInputStream r0 = r8.openFileInput(r0)     // Catch:{ FileNotFoundException -> 0x0485 }
            java.io.InputStreamReader r8 = new java.io.InputStreamReader
            java.nio.charset.Charset r9 = java.nio.charset.StandardCharsets.UTF_8
            r8.<init>(r0, r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            r10 = 0
            java.io.BufferedReader r11 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0060, all -> 0x0055 }
            r11.<init>(r8)     // Catch:{ IOException -> 0x0060, all -> 0x0055 }
            java.lang.String r0 = r11.readLine()     // Catch:{ all -> 0x0049 }
        L_0x002c:
            if (r0 == 0) goto L_0x003b
            r9.append(r0)     // Catch:{ all -> 0x0049 }
            r0 = 10
            r9.append(r0)     // Catch:{ all -> 0x0049 }
            java.lang.String r0 = r11.readLine()     // Catch:{ all -> 0x0049 }
            goto L_0x002c
        L_0x003b:
            r11.close()     // Catch:{ IOException -> 0x0060, all -> 0x0055 }
            java.lang.String r0 = r9.toString()
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{ JSONException -> 0x006a }
            r8.<init>((java.lang.String) r0)     // Catch:{ JSONException -> 0x006a }
        L_0x0047:
            r10 = r8
            goto L_0x006a
        L_0x0049:
            r0 = move-exception
            r8 = r0
            r11.close()     // Catch:{ all -> 0x004f }
            goto L_0x0054
        L_0x004f:
            r0 = move-exception
            r11 = r0
            r8.addSuppressed(r11)     // Catch:{ IOException -> 0x0060, all -> 0x0055 }
        L_0x0054:
            throw r8     // Catch:{ IOException -> 0x0060, all -> 0x0055 }
        L_0x0055:
            r0 = move-exception
            java.lang.String r1 = r9.toString()
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x005f }
            r2.<init>((java.lang.String) r1)     // Catch:{ JSONException -> 0x005f }
        L_0x005f:
            throw r0
        L_0x0060:
            java.lang.String r0 = r9.toString()
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{ JSONException -> 0x006a }
            r8.<init>((java.lang.String) r0)     // Catch:{ JSONException -> 0x006a }
            goto L_0x0047
        L_0x006a:
            java.util.concurrent.atomic.AtomicInteger r0 = new java.util.concurrent.atomic.AtomicInteger     // Catch:{ Exception -> 0x047d }
            r0.<init>()     // Catch:{ Exception -> 0x047d }
            java.util.concurrent.atomic.AtomicInteger r8 = new java.util.concurrent.atomic.AtomicInteger     // Catch:{ Exception -> 0x047d }
            r8.<init>()     // Catch:{ Exception -> 0x047d }
            java.util.concurrent.atomic.AtomicInteger r9 = new java.util.concurrent.atomic.AtomicInteger     // Catch:{ Exception -> 0x047d }
            r9.<init>()     // Catch:{ Exception -> 0x047d }
            java.util.concurrent.atomic.AtomicInteger r11 = new java.util.concurrent.atomic.AtomicInteger     // Catch:{ Exception -> 0x047d }
            r11.<init>()     // Catch:{ Exception -> 0x047d }
            java.util.concurrent.atomic.AtomicInteger r12 = new java.util.concurrent.atomic.AtomicInteger     // Catch:{ Exception -> 0x047d }
            r12.<init>()     // Catch:{ Exception -> 0x047d }
            java.util.concurrent.atomic.AtomicInteger r13 = new java.util.concurrent.atomic.AtomicInteger     // Catch:{ Exception -> 0x047d }
            r13.<init>()     // Catch:{ Exception -> 0x047d }
            java.util.concurrent.atomic.AtomicInteger r14 = new java.util.concurrent.atomic.AtomicInteger     // Catch:{ Exception -> 0x047d }
            r14.<init>()     // Catch:{ Exception -> 0x047d }
            java.util.concurrent.atomic.AtomicInteger r15 = new java.util.concurrent.atomic.AtomicInteger     // Catch:{ Exception -> 0x047d }
            r15.<init>()     // Catch:{ Exception -> 0x047d }
            java.util.concurrent.atomic.AtomicReference r6 = new java.util.concurrent.atomic.AtomicReference     // Catch:{ Exception -> 0x047d }
            r6.<init>()     // Catch:{ Exception -> 0x047d }
            r16 = r7
            java.util.concurrent.atomic.AtomicReference r7 = new java.util.concurrent.atomic.AtomicReference     // Catch:{ Exception -> 0x0479 }
            r7.<init>(r4)     // Catch:{ Exception -> 0x0479 }
            r17 = r4
            java.lang.Thread r4 = new java.lang.Thread     // Catch:{ Exception -> 0x0479 }
            r18 = r5
            com.online.radar.kaf.PaidUtil$1 r5 = new com.online.radar.kaf.PaidUtil$1     // Catch:{ Exception -> 0x0479 }
            r5.<init>(r7, r1, r2)     // Catch:{ Exception -> 0x0479 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0479 }
            r4.start()     // Catch:{ Exception -> 0x0479 }
            r4.join()     // Catch:{ Exception -> 0x0479 }
            r5 = 0
        L_0x00b3:
            java.lang.Object r19 = r7.get()     // Catch:{ Exception -> 0x0479 }
            r4 = r19
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r4 = r10.getJSONObject(r4)     // Catch:{ Exception -> 0x0479 }
            org.json.JSONArray r4 = r4.getJSONArray(r3)     // Catch:{ Exception -> 0x0479 }
            int r4 = r4.length()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r1 = "vip"
            java.lang.String r2 = "comfortplus"
            r19 = r6
            java.lang.String r6 = "business"
            r20 = r0
            java.lang.String r0 = "econom"
            r21 = r8
            java.lang.String r8 = "express"
            r22 = -1
            r23 = 6
            r24 = 2
            r25 = 7
            r26 = 4
            r27 = 3
            r28 = 5
            r29 = 1
            if (r5 >= r4) goto L_0x020c
            java.lang.Object r4 = r7.get()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r4 = r10.getJSONObject(r4)     // Catch:{ Exception -> 0x0479 }
            org.json.JSONArray r4 = r4.getJSONArray(r3)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r4 = r4.getString(r5)     // Catch:{ Exception -> 0x0479 }
            int r30 = r4.hashCode()     // Catch:{ Exception -> 0x0479 }
            switch(r30) {
                case -1308979344: goto L_0x0148;
                case -1308578405: goto L_0x013f;
                case -1146830912: goto L_0x0136;
                case -1105744538: goto L_0x012d;
                case 116765: goto L_0x0124;
                case 94431164: goto L_0x0119;
                case 914213287: goto L_0x010e;
                case 1064546156: goto L_0x0103;
                default: goto L_0x0102;
            }     // Catch:{ Exception -> 0x0479 }
        L_0x0102:
            goto L_0x0150
        L_0x0103:
            java.lang.String r0 = "minivan"
            boolean r0 = r4.equals(r0)     // Catch:{ Exception -> 0x0479 }
            if (r0 == 0) goto L_0x0150
            r22 = r23
            goto L_0x0150
        L_0x010e:
            java.lang.String r0 = "child_tariff"
            boolean r0 = r4.equals(r0)     // Catch:{ Exception -> 0x0479 }
            if (r0 == 0) goto L_0x0150
            r22 = r24
            goto L_0x0150
        L_0x0119:
            java.lang.String r0 = "cargo"
            boolean r0 = r4.equals(r0)     // Catch:{ Exception -> 0x0479 }
            if (r0 == 0) goto L_0x0150
            r22 = r25
            goto L_0x0150
        L_0x0124:
            boolean r0 = r4.equals(r1)     // Catch:{ Exception -> 0x0479 }
            if (r0 == 0) goto L_0x0150
            r22 = r26
            goto L_0x0150
        L_0x012d:
            boolean r0 = r4.equals(r2)     // Catch:{ Exception -> 0x0479 }
            if (r0 == 0) goto L_0x0150
            r22 = r27
            goto L_0x0150
        L_0x0136:
            boolean r0 = r4.equals(r6)     // Catch:{ Exception -> 0x0479 }
            if (r0 == 0) goto L_0x0150
            r22 = r29
            goto L_0x0150
        L_0x013f:
            boolean r0 = r4.equals(r0)     // Catch:{ Exception -> 0x0479 }
            if (r0 == 0) goto L_0x0150
            r22 = 0
            goto L_0x0150
        L_0x0148:
            boolean r0 = r4.equals(r8)     // Catch:{ Exception -> 0x0479 }
            if (r0 == 0) goto L_0x0150
            r22 = r28
        L_0x0150:
            switch(r22) {
                case 0: goto L_0x01e9;
                case 1: goto L_0x01d1;
                case 2: goto L_0x01bd;
                case 3: goto L_0x01a9;
                case 4: goto L_0x0195;
                case 5: goto L_0x0181;
                case 6: goto L_0x016d;
                case 7: goto L_0x0159;
                default: goto L_0x0153;
            }     // Catch:{ Exception -> 0x0479 }
        L_0x0153:
            r1 = r20
            r4 = r21
            goto L_0x0200
        L_0x0159:
            java.lang.Object r0 = r7.get()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r0 = r10.getJSONObject(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r1 = "priceCargo"
            int r0 = r0.getInt(r1)     // Catch:{ Exception -> 0x0479 }
            r15.set(r0)     // Catch:{ Exception -> 0x0479 }
            goto L_0x0153
        L_0x016d:
            java.lang.Object r0 = r7.get()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r0 = r10.getJSONObject(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r1 = "priceMinivan"
            int r0 = r0.getInt(r1)     // Catch:{ Exception -> 0x0479 }
            r13.set(r0)     // Catch:{ Exception -> 0x0479 }
            goto L_0x0153
        L_0x0181:
            java.lang.Object r0 = r7.get()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r0 = r10.getJSONObject(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r1 = "priceExpress"
            int r0 = r0.getInt(r1)     // Catch:{ Exception -> 0x0479 }
            r14.set(r0)     // Catch:{ Exception -> 0x0479 }
            goto L_0x0153
        L_0x0195:
            java.lang.Object r0 = r7.get()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r0 = r10.getJSONObject(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r1 = "priceVip"
            int r0 = r0.getInt(r1)     // Catch:{ Exception -> 0x0479 }
            r11.set(r0)     // Catch:{ Exception -> 0x0479 }
            goto L_0x0153
        L_0x01a9:
            java.lang.Object r0 = r7.get()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r0 = r10.getJSONObject(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r1 = "priceComfortPlus"
            int r0 = r0.getInt(r1)     // Catch:{ Exception -> 0x0479 }
            r12.set(r0)     // Catch:{ Exception -> 0x0479 }
            goto L_0x0153
        L_0x01bd:
            java.lang.Object r0 = r7.get()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r0 = r10.getJSONObject(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r1 = "priceChildren"
            int r0 = r0.getInt(r1)     // Catch:{ Exception -> 0x0479 }
            r9.set(r0)     // Catch:{ Exception -> 0x0479 }
            goto L_0x0153
        L_0x01d1:
            java.lang.Object r0 = r7.get()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r0 = r10.getJSONObject(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r1 = "priceComfort"
            int r0 = r0.getInt(r1)     // Catch:{ Exception -> 0x0479 }
            r4 = r21
            r4.set(r0)     // Catch:{ Exception -> 0x0479 }
            r1 = r20
            goto L_0x0200
        L_0x01e9:
            r4 = r21
            java.lang.Object r0 = r7.get()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r0 = r10.getJSONObject(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r1 = "priceEconom"
            int r0 = r0.getInt(r1)     // Catch:{ Exception -> 0x0479 }
            r1 = r20
            r1.set(r0)     // Catch:{ Exception -> 0x0479 }
        L_0x0200:
            int r5 = r5 + 1
            r2 = r39
            r0 = r1
            r8 = r4
            r6 = r19
            r1 = r38
            goto L_0x00b3
        L_0x020c:
            r3 = r20
            r4 = r21
            java.lang.Thread r5 = new java.lang.Thread     // Catch:{ Exception -> 0x0479 }
            com.online.radar.kaf.PaidUtil$2 r7 = new com.online.radar.kaf.PaidUtil$2     // Catch:{ Exception -> 0x0479 }
            r20 = r3
            r21 = r4
            r10 = r19
            r3 = r1
            r4 = r2
            r1 = r38
            r2 = r39
            r7.<init>(r10, r2, r1)     // Catch:{ Exception -> 0x0479 }
            r5.<init>(r7)     // Catch:{ Exception -> 0x0479 }
            r5.start()     // Catch:{ Exception -> 0x0479 }
            r5.join()     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x0479 }
            java.lang.Object r2 = r10.get()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ Exception -> 0x0479 }
            r1.<init>((java.lang.String) r2)     // Catch:{ Exception -> 0x0479 }
            r2 = r18
            boolean r5 = r1.has(r2)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r7 = "0"
            r10 = r7
            r18 = r10
            r19 = r18
            r30 = r19
            r31 = r30
            r32 = r31
            r33 = r32
            r34 = r33
            if (r5 == 0) goto L_0x0441
            r5 = 0
        L_0x0251:
            org.json.JSONArray r35 = r1.getJSONArray(r2)     // Catch:{ Exception -> 0x0479 }
            r38 = r10
            int r10 = r35.length()     // Catch:{ Exception -> 0x0479 }
            if (r5 >= r10) goto L_0x043f
            org.json.JSONArray r10 = r1.getJSONArray(r2)     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r10 = r10.getJSONObject(r5)     // Catch:{ Exception -> 0x0479 }
            r35 = r12
            java.lang.String r12 = "class"
            java.lang.String r10 = r10.getString(r12)     // Catch:{ Exception -> 0x0479 }
            int r12 = r10.hashCode()     // Catch:{ Exception -> 0x0479 }
            switch(r12) {
                case -1308979344: goto L_0x02b9;
                case -1308578405: goto L_0x02b1;
                case -1146830912: goto L_0x02a8;
                case -1105744538: goto L_0x029f;
                case 116765: goto L_0x0296;
                case 94431164: goto L_0x028b;
                case 914213287: goto L_0x0280;
                case 1064546156: goto L_0x0275;
                default: goto L_0x0274;
            }     // Catch:{ Exception -> 0x0479 }
        L_0x0274:
            goto L_0x02c2
        L_0x0275:
            java.lang.String r12 = "minivan"
            boolean r10 = r10.equals(r12)     // Catch:{ Exception -> 0x0479 }
            if (r10 == 0) goto L_0x02c2
            r10 = r26
            goto L_0x02c4
        L_0x0280:
            java.lang.String r12 = "child_tariff"
            boolean r10 = r10.equals(r12)     // Catch:{ Exception -> 0x0479 }
            if (r10 == 0) goto L_0x02c2
            r10 = r28
            goto L_0x02c4
        L_0x028b:
            java.lang.String r12 = "cargo"
            boolean r10 = r10.equals(r12)     // Catch:{ Exception -> 0x0479 }
            if (r10 == 0) goto L_0x02c2
            r10 = r25
            goto L_0x02c4
        L_0x0296:
            boolean r10 = r10.equals(r3)     // Catch:{ Exception -> 0x0479 }
            if (r10 == 0) goto L_0x02c2
            r10 = r27
            goto L_0x02c4
        L_0x029f:
            boolean r10 = r10.equals(r4)     // Catch:{ Exception -> 0x0479 }
            if (r10 == 0) goto L_0x02c2
            r10 = r24
            goto L_0x02c4
        L_0x02a8:
            boolean r10 = r10.equals(r6)     // Catch:{ Exception -> 0x0479 }
            if (r10 == 0) goto L_0x02c2
            r10 = r29
            goto L_0x02c4
        L_0x02b1:
            boolean r10 = r10.equals(r0)     // Catch:{ Exception -> 0x0479 }
            if (r10 == 0) goto L_0x02c2
            r10 = 0
            goto L_0x02c4
        L_0x02b9:
            boolean r10 = r10.equals(r8)     // Catch:{ Exception -> 0x0479 }
            if (r10 == 0) goto L_0x02c2
            r10 = r23
            goto L_0x02c4
        L_0x02c2:
            r10 = r22
        L_0x02c4:
            java.lang.String r12 = "\\d$"
            r36 = r0
            java.lang.String r0 = "[^0-9]"
            r37 = r3
            java.lang.String r3 = "price"
            switch(r10) {
                case 0: goto L_0x0407;
                case 1: goto L_0x03dc;
                case 2: goto L_0x03b1;
                case 3: goto L_0x0386;
                case 4: goto L_0x035b;
                case 5: goto L_0x0330;
                case 6: goto L_0x0306;
                case 7: goto L_0x02d9;
                default: goto L_0x02d1;
            }
        L_0x02d1:
            r10 = r17
            r17 = r4
        L_0x02d5:
            r0 = r38
            goto L_0x0430
        L_0x02d9:
            org.json.JSONArray r10 = r1.getJSONArray(r2)     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r10 = r10.getJSONObject(r5)     // Catch:{ Exception -> 0x0479 }
            java.lang.Object r3 = r10.get(r3)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0479 }
            r10 = r17
            java.lang.String r0 = r3.replaceAll(r0, r10)     // Catch:{ Exception -> 0x0479 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x0479 }
            int r3 = r15.get()     // Catch:{ Exception -> 0x0479 }
            int r0 = r0 - r3
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r34 = r0.replaceAll(r12, r7)     // Catch:{ Exception -> 0x0479 }
            r0 = r38
            r17 = r4
            goto L_0x0430
        L_0x0306:
            r10 = r17
            r17 = r4
            org.json.JSONArray r4 = r1.getJSONArray(r2)     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r4 = r4.getJSONObject(r5)     // Catch:{ Exception -> 0x0479 }
            java.lang.Object r3 = r4.get(r3)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = r3.replaceAll(r0, r10)     // Catch:{ Exception -> 0x0479 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x0479 }
            int r3 = r14.get()     // Catch:{ Exception -> 0x0479 }
            int r0 = r0 - r3
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r32 = r0.replaceAll(r12, r7)     // Catch:{ Exception -> 0x0479 }
            goto L_0x02d5
        L_0x0330:
            r10 = r17
            r17 = r4
            org.json.JSONArray r4 = r1.getJSONArray(r2)     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r4 = r4.getJSONObject(r5)     // Catch:{ Exception -> 0x0479 }
            java.lang.Object r3 = r4.get(r3)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = r3.replaceAll(r0, r10)     // Catch:{ Exception -> 0x0479 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x0479 }
            int r3 = r9.get()     // Catch:{ Exception -> 0x0479 }
            int r0 = r0 - r3
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r18 = r0.replaceAll(r12, r7)     // Catch:{ Exception -> 0x0479 }
            goto L_0x02d5
        L_0x035b:
            r10 = r17
            r17 = r4
            org.json.JSONArray r4 = r1.getJSONArray(r2)     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r4 = r4.getJSONObject(r5)     // Catch:{ Exception -> 0x0479 }
            java.lang.Object r3 = r4.get(r3)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = r3.replaceAll(r0, r10)     // Catch:{ Exception -> 0x0479 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x0479 }
            int r3 = r13.get()     // Catch:{ Exception -> 0x0479 }
            int r0 = r0 - r3
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r33 = r0.replaceAll(r12, r7)     // Catch:{ Exception -> 0x0479 }
            goto L_0x02d5
        L_0x0386:
            r10 = r17
            r17 = r4
            org.json.JSONArray r4 = r1.getJSONArray(r2)     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r4 = r4.getJSONObject(r5)     // Catch:{ Exception -> 0x0479 }
            java.lang.Object r3 = r4.get(r3)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = r3.replaceAll(r0, r10)     // Catch:{ Exception -> 0x0479 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x0479 }
            int r3 = r11.get()     // Catch:{ Exception -> 0x0479 }
            int r0 = r0 - r3
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r30 = r0.replaceAll(r12, r7)     // Catch:{ Exception -> 0x0479 }
            goto L_0x02d5
        L_0x03b1:
            r10 = r17
            r17 = r4
            org.json.JSONArray r4 = r1.getJSONArray(r2)     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r4 = r4.getJSONObject(r5)     // Catch:{ Exception -> 0x0479 }
            java.lang.Object r3 = r4.get(r3)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = r3.replaceAll(r0, r10)     // Catch:{ Exception -> 0x0479 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x0479 }
            int r3 = r35.get()     // Catch:{ Exception -> 0x0479 }
            int r0 = r0 - r3
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r19 = r0.replaceAll(r12, r7)     // Catch:{ Exception -> 0x0479 }
            goto L_0x02d5
        L_0x03dc:
            r10 = r17
            r17 = r4
            org.json.JSONArray r4 = r1.getJSONArray(r2)     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r4 = r4.getJSONObject(r5)     // Catch:{ Exception -> 0x0479 }
            java.lang.Object r3 = r4.get(r3)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = r3.replaceAll(r0, r10)     // Catch:{ Exception -> 0x0479 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x0479 }
            int r3 = r21.get()     // Catch:{ Exception -> 0x0479 }
            int r0 = r0 - r3
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r31 = r0.replaceAll(r12, r7)     // Catch:{ Exception -> 0x0479 }
            goto L_0x02d5
        L_0x0407:
            r10 = r17
            r17 = r4
            org.json.JSONArray r4 = r1.getJSONArray(r2)     // Catch:{ Exception -> 0x0479 }
            org.json.JSONObject r4 = r4.getJSONObject(r5)     // Catch:{ Exception -> 0x0479 }
            java.lang.Object r3 = r4.get(r3)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = r3.replaceAll(r0, r10)     // Catch:{ Exception -> 0x0479 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x0479 }
            int r3 = r20.get()     // Catch:{ Exception -> 0x0479 }
            int r0 = r0 - r3
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x0479 }
            java.lang.String r0 = r0.replaceAll(r12, r7)     // Catch:{ Exception -> 0x0479 }
        L_0x0430:
            int r5 = r5 + 1
            r4 = r17
            r12 = r35
            r3 = r37
            r17 = r10
            r10 = r0
            r0 = r36
            goto L_0x0251
        L_0x043f:
            r10 = r38
        L_0x0441:
            if (r10 != 0) goto L_0x0444
            r10 = r7
        L_0x0444:
            if (r18 != 0) goto L_0x0448
            r18 = r7
        L_0x0448:
            if (r30 != 0) goto L_0x044c
            r30 = r7
        L_0x044c:
            if (r19 != 0) goto L_0x0450
            r19 = r7
        L_0x0450:
            if (r31 != 0) goto L_0x0454
            r31 = r7
        L_0x0454:
            if (r32 != 0) goto L_0x0458
            r32 = r7
        L_0x0458:
            if (r33 != 0) goto L_0x045c
            r33 = r7
        L_0x045c:
            r1 = 9
            java.lang.String[] r1 = new java.lang.String[r1]     // Catch:{ Exception -> 0x0479 }
            r0 = 0
            r1[r0] = r10     // Catch:{ Exception -> 0x0476 }
            r1[r29] = r31     // Catch:{ Exception -> 0x0476 }
            r1[r24] = r18     // Catch:{ Exception -> 0x0476 }
            r1[r27] = r30     // Catch:{ Exception -> 0x0476 }
            r1[r26] = r19     // Catch:{ Exception -> 0x0476 }
            r1[r28] = r7     // Catch:{ Exception -> 0x0476 }
            r1[r23] = r32     // Catch:{ Exception -> 0x0476 }
            r1[r25] = r33     // Catch:{ Exception -> 0x0476 }
            r0 = 8
            r1[r0] = r34     // Catch:{ Exception -> 0x0476 }
            goto L_0x0484
        L_0x0476:
            r0 = move-exception
            r7 = r1
            goto L_0x0480
        L_0x0479:
            r0 = move-exception
            r7 = r16
            goto L_0x0480
        L_0x047d:
            r0 = move-exception
            r16 = r7
        L_0x0480:
            r0.printStackTrace()
            r1 = r7
        L_0x0484:
            return r1
        L_0x0485:
            r0 = move-exception
            java.lang.RuntimeException r1 = new java.lang.RuntimeException
            r1.<init>(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.online.radar.kaf.PaidUtil.getKaf(float, float, android.content.Context):java.lang.String[]");
    }

    public static JSONArray getZoneInfo(String str) {
        Response execute;
        OkHttpClient client = OkHttpclient.getInstance().getClient();
        try {
            execute = client.newCall(new Request.Builder().url("https://ya-authproxy.taxi.yandex.ru/3.0/zoneinfo").post(RequestBody.create("{\n    \"zone_name\": \"" + str + "\",\n    \"size_hint\": 400,\n    \"skin_version\": 7,\n    \"options\": true\n}", JSON)).build()).execute();
            JSONObject jSONObject = new JSONObject(execute.body().string());
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < jSONObject.getJSONArray("zone_modes").length(); i++) {
                if (jSONObject.getJSONArray("zone_modes").getJSONObject(i).getString("mode").equals("default")) {
                    jSONArray.put((Object) jSONObject.getJSONArray("zone_modes").getJSONObject(i));
                }
            }
            if (execute != null) {
                execute.close();
            }
            return jSONArray;
        } catch (IOException | JSONException unused) {
            return null;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static String getCityName(float f, float f2) {
        Response execute;
        OkHttpClient client = OkHttpclient.getInstance().getClient();
        try {
            execute = client.newCall(new Request.Builder().url("https://ya-authproxy.taxi.yandex.ru/integration/turboapp/v1/suggest").post(RequestBody.create("{\n    \"type\": \"a\",\n    \"state\": {\n        \"accuracy\": 0,\n        \"location\": [\n            " + f2 + ",\n            " + f + "\n        ]\n    },\n    \"position\": [\n        " + f2 + ",\n        " + f + "\n    ],\n    \"prev_log\": \"{\\\"suggest_reqid\\\":\\\"1677800599876404-3778868811-qupzjqvmi5hbp23w\\\",\\\"user_params\\\":{\\\"request\\\":\\\"санкт\\\",\\\"ll\\\":\\\"87.135048,53.756901\\\",\\\"spn\\\":\\\"0.0563049,0.0563049\\\",\\\"ull\\\":\\\"87.135048,53.756901\\\",\\\"lang\\\":\\\"ru\\\"},\\\"server_reqid\\\":\\\"1677800599876404-3778868811-qupzjqvmi5hbp23w\\\",\\\"pos\\\":0,\\\"type\\\":\\\"toponym\\\",\\\"where\\\":{\\\"name\\\":\\\"Россия, Санкт-Петербург\\\",\\\"source_id\\\":\\\"53000093\\\",\\\"title\\\":\\\"Санкт-Петербург\\\"},\\\"uri\\\":\\\"ymapsbm1://geo?data=Cgg1MzAwMDA5MxIr0KDQvtGB0YHQuNGPLCDQodCw0L3QutGCLdCf0LXRgtC10YDQsdGD0YDQsyIKDW%2BG8kEVfcFvQg%3D%3D\\\",\\\"method\\\":\\\"suggest.geosuggest\\\"}\",\n    \"action\": \"finalize\",\n    \"sticky\": true\n}", JSON)).build()).execute();
            JSONObject jSONObject = null;
            if (execute != null) {
                jSONObject = new JSONObject(execute.body().string());
            }
            if (jSONObject.has("nearest_zone")) {
                String string = jSONObject.getString("nearest_zone");
                if (execute != null) {
                    execute.close();
                }
                return string;
            } else if (execute == null) {
                return "";
            } else {
                execute.close();
                return "";
            }
        } catch (IOException | JSONException unused) {
            return "";
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static String getApiRouteYandex(float f, float f2) {
        Response execute;
        OkHttpClient client = OkHttpclient.getInstance().getClient();
        Request.Builder builder = new Request.Builder();
        try {
            execute = client.newCall(builder.url("https://taxi-routeinfo.taxi.yandex.net/taxi_info?clid=ak220627&apikey=oIeqOjPdTjenlPLEinleoQhLUvyqMKDkoJzmTHa&rll=" + f2 + "," + f + "~" + f2 + "," + f + "&class=econom,vip,business,express,minivan,comfortplus,courier,cargo").get().build()).execute();
            JSONObject jSONObject = new JSONObject(execute.body().string());
            Log.e("e", "getCityName: " + jSONObject.getString("nearest_zone"));
            String string = jSONObject.getString("nearest_zone");
            if (execute != null) {
                execute.close();
            }
            return string;
        } catch (IOException | JSONException unused) {
            return "";
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static String getPaid(String str, String str2) throws IOException {
        Response execute = OkHttpclient.getInstance().getClient().newCall(new Request.Builder().url(str).post(RequestBody.create(str2, JSON)).build()).execute();
        if (execute != null) {
            try {
                String string = execute.body().string();
                if (execute != null) {
                    execute.close();
                }
                return string;
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        } else {
            if (execute != null) {
                execute.close();
            }
            return "";
        }
        throw th;
    }

    public static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            } catch (IOException unused) {
            }
        }
        byteArrayOutputStream.close();
        inputStream.close();
        return byteArrayOutputStream.toString();
    }
}
