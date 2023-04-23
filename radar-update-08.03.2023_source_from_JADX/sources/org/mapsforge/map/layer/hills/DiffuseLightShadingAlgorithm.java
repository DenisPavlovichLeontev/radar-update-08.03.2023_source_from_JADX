package org.mapsforge.map.layer.hills;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.logging.Logger;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.layer.hills.HgtCache;

public class DiffuseLightShadingAlgorithm implements ShadingAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(DiffuseLightShadingAlgorithm.class.getName());
    private static final double halfPi = 1.5707963267948966d;

    /* renamed from: a */
    private double f385a;
    private final double ast2;
    private final float heightAngle;
    private final double neutral;

    public DiffuseLightShadingAlgorithm() {
        this(50.0f);
    }

    public DiffuseLightShadingAlgorithm(float f) {
        this.heightAngle = f;
        double heightAngleToRelativeHeight = heightAngleToRelativeHeight(f);
        this.f385a = heightAngleToRelativeHeight;
        this.ast2 = Math.sqrt((heightAngleToRelativeHeight * heightAngleToRelativeHeight) + 2.0d);
        this.neutral = calculateRaw(0.0d, 0.0d);
    }

    static double heightAngleToRelativeHeight(float f) {
        return Math.tan((((double) f) / 180.0d) * 3.141592653589793d) * Math.sqrt(2.0d);
    }

    private static short readNext(ByteBuffer byteBuffer, short s) throws IOException {
        short s2 = byteBuffer.getShort();
        return s2 == Short.MIN_VALUE ? s : s2;
    }

    public double getLightHeight() {
        return this.f385a;
    }

    public int getAxisLenght(HgtCache.HgtFileInfo hgtFileInfo) {
        long size = hgtFileInfo.getSize();
        int ceil = (int) Math.ceil(Math.sqrt((double) (size / 2)));
        if (((long) (ceil * ceil * 2)) != size) {
            return 0;
        }
        return ceil - 1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0069 A[SYNTHETIC, Splitter:B:29:0x0069] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0074 A[SYNTHETIC, Splitter:B:34:0x0074] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0083 A[SYNTHETIC, Splitter:B:42:0x0083] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x008e A[SYNTHETIC, Splitter:B:47:0x008e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.mapsforge.map.layer.hills.ShadingAlgorithm.RawShadingResult transformToByteBuffer(org.mapsforge.map.layer.hills.HgtCache.HgtFileInfo r17, int r18) {
        /*
            r16 = this;
            int r0 = r16.getAxisLenght(r17)
            int r4 = r0 + 1
            r7 = 0
            java.io.File r1 = r17.getFile()     // Catch:{ IOException -> 0x0059, all -> 0x0055 }
            java.io.FileInputStream r8 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0059, all -> 0x0055 }
            r8.<init>(r1)     // Catch:{ IOException -> 0x0059, all -> 0x0055 }
            java.nio.channels.FileChannel r15 = r8.getChannel()     // Catch:{ IOException -> 0x0052, all -> 0x004f }
            java.nio.channels.FileChannel$MapMode r10 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch:{ IOException -> 0x004d }
            r11 = 0
            long r13 = r1.length()     // Catch:{ IOException -> 0x004d }
            r9 = r15
            java.nio.MappedByteBuffer r2 = r9.map(r10, r11, r13)     // Catch:{ IOException -> 0x004d }
            java.nio.ByteOrder r1 = java.nio.ByteOrder.BIG_ENDIAN     // Catch:{ IOException -> 0x004d }
            r2.order(r1)     // Catch:{ IOException -> 0x004d }
            r1 = r16
            r3 = r0
            r5 = r18
            r6 = r17
            byte[] r1 = r1.convert(r2, r3, r4, r5, r6)     // Catch:{ IOException -> 0x004d }
            org.mapsforge.map.layer.hills.ShadingAlgorithm$RawShadingResult r2 = new org.mapsforge.map.layer.hills.ShadingAlgorithm$RawShadingResult     // Catch:{ IOException -> 0x004d }
            r3 = r18
            r2.<init>(r1, r0, r0, r3)     // Catch:{ IOException -> 0x004d }
            if (r15 == 0) goto L_0x0043
            r15.close()     // Catch:{ IOException -> 0x003e }
            goto L_0x0043
        L_0x003e:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
        L_0x0043:
            r8.close()     // Catch:{ IOException -> 0x0047 }
            goto L_0x004c
        L_0x0047:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
        L_0x004c:
            return r2
        L_0x004d:
            r0 = move-exception
            goto L_0x005c
        L_0x004f:
            r0 = move-exception
            r1 = r0
            goto L_0x0081
        L_0x0052:
            r0 = move-exception
            r15 = r7
            goto L_0x005c
        L_0x0055:
            r0 = move-exception
            r1 = r0
            r8 = r7
            goto L_0x0081
        L_0x0059:
            r0 = move-exception
            r8 = r7
            r15 = r8
        L_0x005c:
            java.util.logging.Logger r1 = LOGGER     // Catch:{ all -> 0x007e }
            java.util.logging.Level r2 = java.util.logging.Level.SEVERE     // Catch:{ all -> 0x007e }
            java.lang.String r3 = r0.getMessage()     // Catch:{ all -> 0x007e }
            r1.log(r2, r3, r0)     // Catch:{ all -> 0x007e }
            if (r15 == 0) goto L_0x0072
            r15.close()     // Catch:{ IOException -> 0x006d }
            goto L_0x0072
        L_0x006d:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
        L_0x0072:
            if (r8 == 0) goto L_0x007d
            r8.close()     // Catch:{ IOException -> 0x0078 }
            goto L_0x007d
        L_0x0078:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()
        L_0x007d:
            return r7
        L_0x007e:
            r0 = move-exception
            r1 = r0
            r7 = r15
        L_0x0081:
            if (r7 == 0) goto L_0x008c
            r7.close()     // Catch:{ IOException -> 0x0087 }
            goto L_0x008c
        L_0x0087:
            r0 = move-exception
            r2 = r0
            r2.printStackTrace()
        L_0x008c:
            if (r8 == 0) goto L_0x0097
            r8.close()     // Catch:{ IOException -> 0x0092 }
            goto L_0x0097
        L_0x0092:
            r0 = move-exception
            r2 = r0
            r2.printStackTrace()
        L_0x0097:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.hills.DiffuseLightShadingAlgorithm.transformToByteBuffer(org.mapsforge.map.layer.hills.HgtCache$HgtFileInfo, int):org.mapsforge.map.layer.hills.ShadingAlgorithm$RawShadingResult");
    }

    private byte[] convert(MappedByteBuffer mappedByteBuffer, int i, int i2, int i3, HgtCache.HgtFileInfo hgtFileInfo) throws IOException {
        MappedByteBuffer mappedByteBuffer2 = mappedByteBuffer;
        int i4 = i;
        int i5 = i2;
        short[] sArr = new short[i5];
        int i6 = i3 * 2;
        int i7 = i4 + i6;
        byte[] bArr = new byte[(i7 * i7)];
        int i8 = (i7 * i3) + i3;
        int i9 = 0;
        short s = 0;
        int i10 = 0;
        while (i9 < i5) {
            s = readNext(mappedByteBuffer2, s);
            sArr[i10] = s;
            i9++;
            i10++;
        }
        long j = (long) (i4 * 170);
        double calculateGroundResolution = MercatorProjection.calculateGroundResolution(hgtFileInfo.southLat(), j);
        double d = (double) (i4 * 2);
        double d2 = calculateGroundResolution / d;
        double calculateGroundResolution2 = MercatorProjection.calculateGroundResolution(hgtFileInfo.northLat(), j) / d;
        int i11 = 1;
        while (i11 <= i4) {
            if (i10 >= i5) {
                i10 = 0;
            }
            short s2 = sArr[i10];
            short readNext = readNext(mappedByteBuffer2, s2);
            int i12 = i10 + 1;
            sArr[i10] = readNext;
            int i13 = i6;
            double d3 = d2;
            double d4 = (((double) i11) * d2) + (((double) (i4 - i11)) * calculateGroundResolution2);
            int i14 = i8;
            int i15 = 1;
            while (i15 <= i4) {
                short s3 = sArr[i12];
                short readNext2 = readNext(mappedByteBuffer2, s3);
                sArr[i12] = readNext2;
                bArr[i14] = (byte) (Math.min(255, Math.max(0, calculate(((double) (-((readNext2 - s3) + (readNext - s2)))) / d4, ((double) (-((s3 - s2) + (readNext2 - readNext)))) / d4) + 127)) & 255);
                i15++;
                mappedByteBuffer2 = mappedByteBuffer;
                i4 = i;
                i14++;
                s2 = s3;
                sArr = sArr;
                i12++;
                int i16 = i2;
                readNext = readNext2;
            }
            short[] sArr2 = sArr;
            i8 = i14 + i13;
            i11++;
            mappedByteBuffer2 = mappedByteBuffer;
            i4 = i;
            i5 = i2;
            i6 = i13;
            i10 = i12;
            d2 = d3;
        }
        return bArr;
    }

    /* access modifiers changed from: package-private */
    public int calculate(double d, double d2) {
        long round;
        double calculateRaw = calculateRaw(d, d2);
        double d3 = this.neutral;
        double d4 = calculateRaw - d3;
        if (d4 < 0.0d) {
            round = Math.round((d4 / d3) * 128.0d);
        } else if (d4 <= 0.0d) {
            return 0;
        } else {
            round = Math.round((d4 / (1.0d - d3)) * 127.0d);
        }
        return (int) round;
    }

    /* access modifiers changed from: package-private */
    public double calculateRaw(double d, double d2) {
        return Math.max(0.0d, ((d2 + d) + this.f385a) / (this.ast2 * Math.sqrt(((d * d) + (d2 * d2)) + 1.0d)));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (Double.compare(((DiffuseLightShadingAlgorithm) obj).f385a, this.f385a) == 0) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.f385a);
        return (int) (doubleToLongBits ^ (doubleToLongBits >>> 32));
    }

    public String toString() {
        return "DiffuseLightShadingAlgorithm{heightAngle=" + this.heightAngle + '}';
    }
}
