package org.mapsforge.map.layer.hills;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.logging.Logger;
import org.mapsforge.map.layer.hills.HgtCache;

public class SimpleShadingAlgorithm implements ShadingAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(SimpleShadingAlgorithm.class.getName());
    public final double linearity;
    private byte[] lookup;
    private int lookupOffset;
    public final double scale;

    public SimpleShadingAlgorithm() {
        this(0.1d, 0.666d);
    }

    public SimpleShadingAlgorithm(double d, double d2) {
        this.linearity = Math.min(1.0d, Math.max(0.0d, d));
        this.scale = Math.max(0.0d, d2);
    }

    private static short readNext(ByteBuffer byteBuffer, short s) throws IOException {
        short s2 = byteBuffer.getShort();
        return s2 == Short.MIN_VALUE ? s : s2;
    }

    /* access modifiers changed from: protected */
    public double exaggerate(double d) {
        double max = Math.max(-128.0d, Math.min(128.0d, d * this.scale));
        double d2 = this.linearity;
        return (Math.sin(Math.sin(Math.sin((max * 1.5707963267948966d) / 128.0d) * 1.5707963267948966d) * 1.5707963267948966d) * 128.0d * (1.0d - d2)) + (max * d2);
    }

    public int getAxisLenght(HgtCache.HgtFileInfo hgtFileInfo) {
        long size = hgtFileInfo.getSize();
        int ceil = (int) Math.ceil(Math.sqrt((double) (size / 2)));
        if (((long) (ceil * ceil * 2)) != size) {
            return 0;
        }
        return ceil - 1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x005c A[SYNTHETIC, Splitter:B:28:0x005c] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0066 A[SYNTHETIC, Splitter:B:33:0x0066] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0073 A[SYNTHETIC, Splitter:B:41:0x0073] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x007d A[SYNTHETIC, Splitter:B:46:0x007d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.mapsforge.map.layer.hills.ShadingAlgorithm.RawShadingResult transformToByteBuffer(org.mapsforge.map.layer.hills.HgtCache.HgtFileInfo r12, int r13) {
        /*
            r11 = this;
            int r0 = r11.getAxisLenght(r12)
            int r1 = r0 + 1
            r2 = 0
            java.io.File r12 = r12.getFile()     // Catch:{ IOException -> 0x004c, all -> 0x0049 }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ IOException -> 0x004c, all -> 0x0049 }
            r3.<init>(r12)     // Catch:{ IOException -> 0x004c, all -> 0x0049 }
            java.nio.channels.FileChannel r10 = r3.getChannel()     // Catch:{ IOException -> 0x0046, all -> 0x0044 }
            java.nio.channels.FileChannel$MapMode r5 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch:{ IOException -> 0x0042 }
            r6 = 0
            long r8 = r12.length()     // Catch:{ IOException -> 0x0042 }
            r4 = r10
            java.nio.MappedByteBuffer r12 = r4.map(r5, r6, r8)     // Catch:{ IOException -> 0x0042 }
            java.nio.ByteOrder r4 = java.nio.ByteOrder.BIG_ENDIAN     // Catch:{ IOException -> 0x0042 }
            r12.order(r4)     // Catch:{ IOException -> 0x0042 }
            byte[] r12 = r11.convert(r12, r0, r1, r13)     // Catch:{ IOException -> 0x0042 }
            org.mapsforge.map.layer.hills.ShadingAlgorithm$RawShadingResult r1 = new org.mapsforge.map.layer.hills.ShadingAlgorithm$RawShadingResult     // Catch:{ IOException -> 0x0042 }
            r1.<init>(r12, r0, r0, r13)     // Catch:{ IOException -> 0x0042 }
            if (r10 == 0) goto L_0x0039
            r10.close()     // Catch:{ IOException -> 0x0035 }
            goto L_0x0039
        L_0x0035:
            r12 = move-exception
            r12.printStackTrace()
        L_0x0039:
            r3.close()     // Catch:{ IOException -> 0x003d }
            goto L_0x0041
        L_0x003d:
            r12 = move-exception
            r12.printStackTrace()
        L_0x0041:
            return r1
        L_0x0042:
            r12 = move-exception
            goto L_0x004f
        L_0x0044:
            r12 = move-exception
            goto L_0x0071
        L_0x0046:
            r12 = move-exception
            r10 = r2
            goto L_0x004f
        L_0x0049:
            r12 = move-exception
            r3 = r2
            goto L_0x0071
        L_0x004c:
            r12 = move-exception
            r3 = r2
            r10 = r3
        L_0x004f:
            java.util.logging.Logger r13 = LOGGER     // Catch:{ all -> 0x006f }
            java.util.logging.Level r0 = java.util.logging.Level.SEVERE     // Catch:{ all -> 0x006f }
            java.lang.String r1 = r12.getMessage()     // Catch:{ all -> 0x006f }
            r13.log(r0, r1, r12)     // Catch:{ all -> 0x006f }
            if (r10 == 0) goto L_0x0064
            r10.close()     // Catch:{ IOException -> 0x0060 }
            goto L_0x0064
        L_0x0060:
            r12 = move-exception
            r12.printStackTrace()
        L_0x0064:
            if (r3 == 0) goto L_0x006e
            r3.close()     // Catch:{ IOException -> 0x006a }
            goto L_0x006e
        L_0x006a:
            r12 = move-exception
            r12.printStackTrace()
        L_0x006e:
            return r2
        L_0x006f:
            r12 = move-exception
            r2 = r10
        L_0x0071:
            if (r2 == 0) goto L_0x007b
            r2.close()     // Catch:{ IOException -> 0x0077 }
            goto L_0x007b
        L_0x0077:
            r13 = move-exception
            r13.printStackTrace()
        L_0x007b:
            if (r3 == 0) goto L_0x0085
            r3.close()     // Catch:{ IOException -> 0x0081 }
            goto L_0x0085
        L_0x0081:
            r13 = move-exception
            r13.printStackTrace()
        L_0x0085:
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.hills.SimpleShadingAlgorithm.transformToByteBuffer(org.mapsforge.map.layer.hills.HgtCache$HgtFileInfo, int):org.mapsforge.map.layer.hills.ShadingAlgorithm$RawShadingResult");
    }

    private byte[] convert(MappedByteBuffer mappedByteBuffer, int i, int i2, int i3) throws IOException {
        int i4;
        MappedByteBuffer mappedByteBuffer2 = mappedByteBuffer;
        int i5 = i;
        int i6 = i2;
        short[] sArr = new short[i6];
        int i7 = i3 * 2;
        int i8 = i5 + i7;
        byte[] bArr = new byte[(i8 * i8)];
        byte[] bArr2 = this.lookup;
        if (bArr2 == null) {
            fillLookup();
            bArr2 = this.lookup;
        }
        int i9 = (i8 * i3) + i3;
        int i10 = 0;
        short s = 0;
        int i11 = 0;
        while (i10 < i6) {
            s = readNext(mappedByteBuffer2, s);
            sArr[i4] = s;
            i10++;
            i11 = i4 + 1;
        }
        for (int i12 = 1; i12 <= i5; i12++) {
            if (i4 >= i6) {
                i4 = 0;
            }
            short s2 = sArr[i4];
            short readNext = readNext(mappedByteBuffer2, s2);
            sArr[i4] = readNext;
            i4++;
            int i13 = 1;
            while (i13 <= i5) {
                short s3 = sArr[i4];
                short readNext2 = readNext(mappedByteBuffer2, s3);
                sArr[i4] = readNext2;
                bArr[i9] = (byte) (Math.min(255, Math.max(0, exaggerate(bArr2, -((readNext2 - s3) + (readNext - s2))) + exaggerate(bArr2, -((s3 - s2) + (readNext2 - readNext))) + 127)) & 255);
                i13++;
                i9++;
                readNext = readNext2;
                i4++;
                s2 = s3;
            }
            i9 += i7;
        }
        return bArr;
    }

    private byte exaggerate(byte[] bArr, int i) {
        return bArr[Math.max(0, Math.min(bArr.length - 1, i + this.lookupOffset))];
    }

    private void fillLookup() {
        int i = 0;
        while (i > -1024) {
            double round = (double) Math.round(exaggerate((double) i));
            if (round <= -128.0d || round >= 127.0d) {
                break;
            }
            i--;
        }
        int i2 = 0;
        while (i2 < 1024) {
            double round2 = (double) Math.round(exaggerate((double) i2));
            if (round2 <= -128.0d || round2 >= 127.0d) {
                break;
            }
            i2++;
        }
        int i3 = (i2 + 1) - i;
        byte[] bArr = new byte[i3];
        int i4 = i;
        for (int i5 = 0; i5 < i3; i5++) {
            bArr[i5] = (byte) ((int) Math.round(exaggerate((double) i4)));
            i4++;
        }
        this.lookup = bArr;
        this.lookupOffset = -i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SimpleShadingAlgorithm simpleShadingAlgorithm = (SimpleShadingAlgorithm) obj;
        if (Double.compare(simpleShadingAlgorithm.linearity, this.linearity) != 0) {
            return false;
        }
        if (Double.compare(simpleShadingAlgorithm.scale, this.scale) == 0) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.linearity);
        long doubleToLongBits2 = Double.doubleToLongBits(this.scale);
        return (((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) * 31) + ((int) ((doubleToLongBits2 >>> 32) ^ doubleToLongBits2));
    }

    public String toString() {
        return "SimpleShadingAlgorithm{linearity=" + this.linearity + ", scale=" + this.scale + '}';
    }
}
