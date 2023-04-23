package p005ar.com.hjg.pngj.pixels;

import java.nio.ByteOrder;
import kotlin.UByte;
import kotlin.UShort;

/* renamed from: ar.com.hjg.pngj.pixels.DeflaterEstimatorLz4 */
public final class DeflaterEstimatorLz4 {
    static final int COPY_LENGTH = 8;
    static final int HASH_LOG = 12;
    static final int HASH_LOG_64K = 13;
    static final int HASH_LOG_HC = 15;
    static final int HASH_TABLE_SIZE = 4096;
    static final int HASH_TABLE_SIZE_64K = 8192;
    static final int HASH_TABLE_SIZE_HC = 32768;
    static final int LAST_LITERALS = 5;
    static final int LZ4_64K_LIMIT = 65547;
    static final int MAX_DISTANCE = 65536;
    static final int MEMORY_USAGE = 14;
    static final int MF_LIMIT = 12;
    static final int MIN_LENGTH = 13;
    static final int MIN_MATCH = 4;
    static final int ML_BITS = 4;
    static final int ML_MASK = 15;
    static final ByteOrder NATIVE_BYTE_ORDER = ByteOrder.nativeOrder();
    static final int NOT_COMPRESSIBLE_DETECTION_LEVEL = 6;
    static final int OPTIMAL_ML = 18;
    static final int RUN_BITS = 4;
    static final int RUN_MASK = 15;
    static final int SKIP_STRENGTH = Math.max(6, 2);

    static int hash(int i) {
        return (i * -1640531535) >>> 20;
    }

    static int hash64k(int i) {
        return (i * -1640531535) >>> 19;
    }

    public int compressEstim(byte[] bArr, int i, int i2) {
        if (i2 < 10) {
            return i2;
        }
        int i3 = ((i2 + 65546) - 1) / 65546;
        int i4 = i2 / i3;
        if (i4 >= 65546 || i4 * i3 > i2 || i3 < 1 || i4 < 1) {
            throw new RuntimeException("?? " + i2);
        }
        int i5 = 0;
        int i6 = i2;
        int i7 = 0;
        while (i6 > 0) {
            if (i6 > i4) {
                i6 = i4;
            }
            i5 += compress64k(bArr, i, i6);
            i += i6;
            i7 += i6;
            i6 = i2 - i7;
        }
        return i7 == i2 ? i5 : (int) (((((double) i5) / ((double) i7)) * ((double) i2)) + 0.5d);
    }

    public int compressEstim(byte[] bArr) {
        return compressEstim(bArr, 0, bArr.length);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0040, code lost:
        r5 = commonBytesBackward(r13, r11, r6, r14, r7);
        r6 = r6 - r5;
        r11 = r11 - r5;
        r5 = r6 - r7;
        r8 = r8 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x004c, code lost:
        if (r5 < 15) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004e, code lost:
        if (r5 <= 15) goto L_0x0055;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0050, code lost:
        r8 = r8 + ((r5 - 15) / 255);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0055, code lost:
        r8 = r8 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0056, code lost:
        r8 = r8 + r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0057, code lost:
        r8 = r8 + 2;
        r6 = r6 + 4;
        r5 = commonBytes(r13, r11 + 4, r6, r1);
        r6 = r6 + r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0062, code lost:
        if (r5 < 15) goto L_0x006c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0064, code lost:
        if (r5 < 270) goto L_0x006b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0066, code lost:
        r8 = r8 + ((r5 - 15) / 255);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x006b, code lost:
        r8 = r8 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006c, code lost:
        if (r6 <= r2) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x006e, code lost:
        r14 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0070, code lost:
        r5 = r6 - 2;
        writeShort(r15, hash64k(readInt(r13, r5)), r5 - r14);
        r5 = hash64k(readInt(r13, r6));
        r11 = r14 + readShort(r15, r5);
        writeShort(r15, r5, r6 - r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0095, code lost:
        if (readIntEquals(r13, r6, r11) != false) goto L_0x009d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x009d, code lost:
        r8 = r8 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static int compress64k(byte[] r13, int r14, int r15) {
        /*
            int r0 = r14 + r15
            int r1 = r0 + -5
            int r2 = r0 + -12
            r3 = 270(0x10e, float:3.78E-43)
            r4 = 1
            r5 = 0
            r6 = 13
            if (r15 < r6) goto L_0x00a4
            r15 = 8192(0x2000, float:1.14794E-41)
            short[] r15 = new short[r15]
            int r6 = r14 + 1
            r7 = r14
            r8 = r5
        L_0x0016:
            int r5 = SKIP_STRENGTH
            int r5 = r4 << r5
            int r5 = r5 + 3
        L_0x001c:
            int r9 = r5 + 1
            int r10 = SKIP_STRENGTH
            int r5 = r5 >>> r10
            int r5 = r5 + r6
            if (r5 <= r2) goto L_0x0028
            r14 = r7
        L_0x0025:
            r5 = r8
            goto L_0x00a4
        L_0x0028:
            int r10 = readInt(r13, r6)
            int r10 = hash64k(r10)
            int r11 = readShort(r15, r10)
            int r11 = r11 + r14
            int r12 = r6 - r14
            writeShort(r15, r10, r12)
            boolean r10 = readIntEquals(r13, r11, r6)
            if (r10 == 0) goto L_0x00a0
            int r5 = commonBytesBackward(r13, r11, r6, r14, r7)
            int r6 = r6 - r5
            int r11 = r11 - r5
            int r5 = r6 - r7
            int r8 = r8 + 1
            r10 = 15
            if (r5 < r10) goto L_0x0056
            if (r5 <= r10) goto L_0x0055
            int r7 = r5 + -15
            int r7 = r7 / 255
            int r8 = r8 + r7
        L_0x0055:
            int r8 = r8 + r4
        L_0x0056:
            int r8 = r8 + r5
        L_0x0057:
            int r8 = r8 + 2
            int r6 = r6 + 4
            int r11 = r11 + 4
            int r5 = commonBytes(r13, r11, r6, r1)
            int r6 = r6 + r5
            if (r5 < r10) goto L_0x006c
            if (r5 < r3) goto L_0x006b
            int r5 = r5 + -15
            int r5 = r5 / 255
            int r8 = r8 + r5
        L_0x006b:
            int r8 = r8 + r4
        L_0x006c:
            if (r6 <= r2) goto L_0x0070
            r14 = r6
            goto L_0x0025
        L_0x0070:
            int r5 = r6 + -2
            int r7 = readInt(r13, r5)
            int r7 = hash64k(r7)
            int r5 = r5 - r14
            writeShort(r15, r7, r5)
            int r5 = readInt(r13, r6)
            int r5 = hash64k(r5)
            int r7 = readShort(r15, r5)
            int r11 = r14 + r7
            int r7 = r6 - r14
            writeShort(r15, r5, r7)
            boolean r5 = readIntEquals(r13, r6, r11)
            if (r5 != 0) goto L_0x009d
            int r5 = r6 + 1
            r7 = r6
            r6 = r5
            goto L_0x0016
        L_0x009d:
            int r8 = r8 + 1
            goto L_0x0057
        L_0x00a0:
            r6 = r5
            r5 = r9
            goto L_0x001c
        L_0x00a4:
            int r0 = r0 - r14
            if (r0 < r3) goto L_0x00ac
            int r13 = r0 + -15
            int r13 = r13 / 255
            int r5 = r5 + r13
        L_0x00ac:
            int r5 = r5 + r4
            int r5 = r5 + r0
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.pixels.DeflaterEstimatorLz4.compress64k(byte[], int, int):int");
    }

    static final int maxCompressedLength(int i) {
        if (i >= 0) {
            return i + (i / 255) + 16;
        }
        throw new IllegalArgumentException("length must be >= 0, got " + i);
    }

    static int readShortLittleEndian(byte[] bArr, int i) {
        return ((bArr[i + 1] & UByte.MAX_VALUE) << 8) | (bArr[i] & UByte.MAX_VALUE);
    }

    static boolean readIntEquals(byte[] bArr, int i, int i2) {
        return bArr[i] == bArr[i2] && bArr[i + 1] == bArr[i2 + 1] && bArr[i + 2] == bArr[i2 + 2] && bArr[i + 3] == bArr[i2 + 3];
    }

    static int commonBytes(byte[] bArr, int i, int i2, int i3) {
        int i4 = 0;
        while (i2 < i3) {
            int i5 = i + 1;
            int i6 = i2 + 1;
            if (bArr[i] != bArr[i2]) {
                break;
            }
            i4++;
            i = i5;
            i2 = i6;
        }
        return i4;
    }

    static int commonBytesBackward(byte[] bArr, int i, int i2, int i3, int i4) {
        int i5 = 0;
        while (i > i3 && i2 > i4) {
            i--;
            i2--;
            if (bArr[i] != bArr[i2]) {
                break;
            }
            i5++;
        }
        return i5;
    }

    static int readShort(short[] sArr, int i) {
        return sArr[i] & UShort.MAX_VALUE;
    }

    static byte readByte(byte[] bArr, int i) {
        return bArr[i];
    }

    static void checkRange(byte[] bArr, int i) {
        if (i < 0 || i >= bArr.length) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
    }

    static void checkRange(byte[] bArr, int i, int i2) {
        checkLength(i2);
        if (i2 > 0) {
            checkRange(bArr, i);
            checkRange(bArr, (i + i2) - 1);
        }
    }

    static void checkLength(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("lengths must be >= 0");
        }
    }

    static int readIntBE(byte[] bArr, int i) {
        return (bArr[i + 3] & UByte.MAX_VALUE) | ((bArr[i] & UByte.MAX_VALUE) << 24) | ((bArr[i + 1] & UByte.MAX_VALUE) << 16) | ((bArr[i + 2] & UByte.MAX_VALUE) << 8);
    }

    static int readIntLE(byte[] bArr, int i) {
        return ((bArr[i + 3] & UByte.MAX_VALUE) << 24) | (bArr[i] & UByte.MAX_VALUE) | ((bArr[i + 1] & UByte.MAX_VALUE) << 8) | ((bArr[i + 2] & UByte.MAX_VALUE) << 16);
    }

    static int readInt(byte[] bArr, int i) {
        if (NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
            return readIntBE(bArr, i);
        }
        return readIntLE(bArr, i);
    }

    static void writeShort(short[] sArr, int i, int i2) {
        sArr[i] = (short) i2;
    }
}
