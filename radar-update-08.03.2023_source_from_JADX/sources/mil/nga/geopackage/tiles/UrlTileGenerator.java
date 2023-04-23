package mil.nga.geopackage.tiles;

import android.content.Context;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.C1157R;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.projection.Projection;

public class UrlTileGenerator extends TileGenerator {
    private final String tileUrl;
    private boolean tms = false;
    private final boolean urlHasBoundingBox;
    private final boolean urlHasXYZ;

    /* access modifiers changed from: protected */
    public void preTileGeneration() {
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UrlTileGenerator(Context context, GeoPackage geoPackage, String str, String str2, int i, int i2, BoundingBox boundingBox, Projection projection) {
        super(context, geoPackage, str, i, i2, boundingBox, projection);
        String str3 = str2;
        try {
            this.tileUrl = URLDecoder.decode(str2, "UTF-8");
            boolean hasXYZ = hasXYZ(str2);
            this.urlHasXYZ = hasXYZ;
            boolean hasBoundingBox = hasBoundingBox(str2);
            this.urlHasBoundingBox = hasBoundingBox;
            if (!hasXYZ && !hasBoundingBox) {
                throw new GeoPackageException("URL does not contain x,y,z or bounding box variables: " + str2);
            }
        } catch (UnsupportedEncodingException e) {
            throw new GeoPackageException("Failed to decode tile url: " + str2, e);
        }
    }

    public boolean isTms() {
        return this.tms;
    }

    public void setTms(boolean z) {
        this.tms = z;
    }

    private boolean hasBoundingBox(String str) {
        return !replaceBoundingBox(str, this.boundingBox).equals(str);
    }

    private String replaceXYZ(String str, int i, long j, long j2) {
        return str.replaceAll(this.context.getString(C1157R.string.tile_generator_variable_z), String.valueOf(i)).replaceAll(this.context.getString(C1157R.string.tile_generator_variable_x), String.valueOf(j)).replaceAll(this.context.getString(C1157R.string.tile_generator_variable_y), String.valueOf(j2));
    }

    private boolean hasXYZ(String str) {
        return !replaceXYZ(str, 0, 0, 0).equals(str);
    }

    private String replaceBoundingBox(String str, int i, long j, long j2) {
        return replaceBoundingBox(str, TileBoundingBoxUtils.getProjectedBoundingBox(this.projection, j, j2, i));
    }

    private String replaceBoundingBox(String str, BoundingBox boundingBox) {
        return str.replaceAll(this.context.getString(C1157R.string.tile_generator_variable_min_lat), String.valueOf(boundingBox.getMinLatitude())).replaceAll(this.context.getString(C1157R.string.tile_generator_variable_max_lat), String.valueOf(boundingBox.getMaxLatitude())).replaceAll(this.context.getString(C1157R.string.tile_generator_variable_min_lon), String.valueOf(boundingBox.getMinLongitude())).replaceAll(this.context.getString(C1157R.string.tile_generator_variable_max_lon), String.valueOf(boundingBox.getMaxLongitude()));
    }

    /* JADX WARNING: type inference failed for: r0v8, types: [java.net.URLConnection] */
    /* JADX WARNING: type inference failed for: r0v19, types: [java.net.URLConnection] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x008a A[Catch:{ IOException -> 0x00cf }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0098 A[SYNTHETIC, Splitter:B:34:0x0098] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00fc  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] createTile(int r19, long r20, long r22) {
        /*
            r18 = this;
            r8 = r18
            r9 = r19
            r10 = r20
            r12 = r22
            java.lang.String r14 = ", y="
            java.lang.String r15 = ", x="
            java.lang.String r6 = ", z="
            java.lang.String r7 = "Failed to download tile. URL: "
            java.lang.String r2 = r8.tileUrl
            boolean r0 = r8.urlHasXYZ
            if (r0 == 0) goto L_0x0035
            boolean r0 = r8.tms
            if (r0 == 0) goto L_0x0023
            int r0 = (int) r12
            int r0 = mil.nga.geopackage.tiles.TileBoundingBoxUtils.getYAsOppositeTileFormat(r9, r0)
            long r0 = (long) r0
            r16 = r0
            goto L_0x0025
        L_0x0023:
            r16 = r12
        L_0x0025:
            r1 = r18
            r3 = r19
            r4 = r20
            r13 = r6
            r12 = r7
            r6 = r16
            java.lang.String r0 = r1.replaceXYZ(r2, r3, r4, r6)
            r2 = r0
            goto L_0x0037
        L_0x0035:
            r13 = r6
            r12 = r7
        L_0x0037:
            boolean r0 = r8.urlHasBoundingBox
            if (r0 == 0) goto L_0x0047
            r1 = r18
            r3 = r19
            r4 = r20
            r6 = r22
            java.lang.String r2 = r1.replaceBoundingBox(r2, r3, r4, r6)
        L_0x0047:
            java.net.URL r0 = new java.net.URL     // Catch:{ MalformedURLException -> 0x0100 }
            r0.<init>(r2)     // Catch:{ MalformedURLException -> 0x0100 }
            r1 = 0
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ IOException -> 0x00cf }
            r3 = r0
            java.net.HttpURLConnection r3 = (java.net.HttpURLConnection) r3     // Catch:{ IOException -> 0x00cf }
            r3.connect()     // Catch:{ IOException -> 0x00c7, all -> 0x00c4 }
            int r0 = r3.getResponseCode()     // Catch:{ IOException -> 0x00c7, all -> 0x00c4 }
            r1 = 301(0x12d, float:4.22E-43)
            if (r0 == r1) goto L_0x006a
            r1 = 302(0x12e, float:4.23E-43)
            if (r0 == r1) goto L_0x006a
            r1 = 303(0x12f, float:4.25E-43)
            if (r0 != r1) goto L_0x0068
            goto L_0x006a
        L_0x0068:
            r1 = r3
            goto L_0x0082
        L_0x006a:
            java.lang.String r0 = "Location"
            java.lang.String r0 = r3.getHeaderField(r0)     // Catch:{ IOException -> 0x00c7, all -> 0x00c4 }
            r3.disconnect()     // Catch:{ IOException -> 0x00c7, all -> 0x00c4 }
            java.net.URL r1 = new java.net.URL     // Catch:{ IOException -> 0x00c7, all -> 0x00c4 }
            r1.<init>(r0)     // Catch:{ IOException -> 0x00c7, all -> 0x00c4 }
            java.net.URLConnection r0 = r1.openConnection()     // Catch:{ IOException -> 0x00c7, all -> 0x00c4 }
            r1 = r0
            java.net.HttpURLConnection r1 = (java.net.HttpURLConnection) r1     // Catch:{ IOException -> 0x00c7, all -> 0x00c4 }
            r1.connect()     // Catch:{ IOException -> 0x00cf }
        L_0x0082:
            int r0 = r1.getResponseCode()     // Catch:{ IOException -> 0x00cf }
            r3 = 200(0xc8, float:2.8E-43)
            if (r0 != r3) goto L_0x0098
            java.io.InputStream r0 = r1.getInputStream()     // Catch:{ IOException -> 0x00cf }
            byte[] r0 = mil.nga.geopackage.p010io.GeoPackageIOUtils.streamBytes(r0)     // Catch:{ IOException -> 0x00cf }
            if (r1 == 0) goto L_0x0097
            r1.disconnect()
        L_0x0097:
            return r0
        L_0x0098:
            mil.nga.geopackage.GeoPackageException r0 = new mil.nga.geopackage.GeoPackageException     // Catch:{ IOException -> 0x00cf }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00cf }
            r3.<init>()     // Catch:{ IOException -> 0x00cf }
            r3.append(r12)     // Catch:{ IOException -> 0x00cf }
            r3.append(r2)     // Catch:{ IOException -> 0x00cf }
            r3.append(r13)     // Catch:{ IOException -> 0x00cf }
            r3.append(r9)     // Catch:{ IOException -> 0x00cf }
            r3.append(r15)     // Catch:{ IOException -> 0x00cf }
            r3.append(r10)     // Catch:{ IOException -> 0x00cf }
            r3.append(r14)     // Catch:{ IOException -> 0x00cf }
            r4 = r22
            r6 = r12
            r3.append(r4)     // Catch:{ IOException -> 0x00c2 }
            java.lang.String r3 = r3.toString()     // Catch:{ IOException -> 0x00c2 }
            r0.<init>((java.lang.String) r3)     // Catch:{ IOException -> 0x00c2 }
            throw r0     // Catch:{ IOException -> 0x00c2 }
        L_0x00c2:
            r0 = move-exception
            goto L_0x00d3
        L_0x00c4:
            r0 = move-exception
            r1 = r3
            goto L_0x00fa
        L_0x00c7:
            r0 = move-exception
            r4 = r22
            r6 = r12
            r1 = r3
            goto L_0x00d3
        L_0x00cd:
            r0 = move-exception
            goto L_0x00fa
        L_0x00cf:
            r0 = move-exception
            r4 = r22
            r6 = r12
        L_0x00d3:
            mil.nga.geopackage.GeoPackageException r3 = new mil.nga.geopackage.GeoPackageException     // Catch:{ all -> 0x00cd }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x00cd }
            r7.<init>()     // Catch:{ all -> 0x00cd }
            r7.append(r6)     // Catch:{ all -> 0x00cd }
            r7.append(r2)     // Catch:{ all -> 0x00cd }
            r7.append(r13)     // Catch:{ all -> 0x00cd }
            r7.append(r9)     // Catch:{ all -> 0x00cd }
            r7.append(r15)     // Catch:{ all -> 0x00cd }
            r7.append(r10)     // Catch:{ all -> 0x00cd }
            r7.append(r14)     // Catch:{ all -> 0x00cd }
            r7.append(r4)     // Catch:{ all -> 0x00cd }
            java.lang.String r2 = r7.toString()     // Catch:{ all -> 0x00cd }
            r3.<init>(r2, r0)     // Catch:{ all -> 0x00cd }
            throw r3     // Catch:{ all -> 0x00cd }
        L_0x00fa:
            if (r1 == 0) goto L_0x00ff
            r1.disconnect()
        L_0x00ff:
            throw r0
        L_0x0100:
            r0 = move-exception
            r4 = r22
            r6 = r12
            mil.nga.geopackage.GeoPackageException r1 = new mil.nga.geopackage.GeoPackageException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r6)
            r3.append(r2)
            r3.append(r13)
            r3.append(r9)
            r3.append(r15)
            r3.append(r10)
            r3.append(r14)
            r3.append(r4)
            java.lang.String r2 = r3.toString()
            r1.<init>(r2, r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.tiles.UrlTileGenerator.createTile(int, long, long):byte[]");
    }
}
