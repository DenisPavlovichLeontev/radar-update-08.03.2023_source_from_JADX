package okio.internal;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.Utf8;
import org.kxml2.wap.Wbxml;

@Metadata(mo19893d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0012\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\u001e\u0010\u0003\u001a\u00020\u0002*\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005¨\u0006\u0007"}, mo19894d2 = {"commonAsUtf8ToByteArray", "", "", "commonToUtf8String", "beginIndex", "", "endIndex", "okio"}, mo19895k = 2, mo19896mv = {1, 5, 1}, mo19898xi = 48)
/* compiled from: -Utf8.kt */
public final class _Utf8Kt {
    public static /* synthetic */ String commonToUtf8String$default(byte[] bArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = bArr.length;
        }
        return commonToUtf8String(bArr, i, i2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0098, code lost:
        if (((r0[r5] & 192) == 128) == false) goto L_0x0049;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x011c, code lost:
        if (((r0[r5] & 192) == 128) == false) goto L_0x0121;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final java.lang.String commonToUtf8String(byte[] r16, int r17, int r18) {
        /*
            r0 = r16
            r1 = r17
            r2 = r18
            java.lang.String r3 = "<this>"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r0, r3)
            if (r1 < 0) goto L_0x01c1
            int r3 = r0.length
            if (r2 > r3) goto L_0x01c1
            if (r1 > r2) goto L_0x01c1
            int r3 = r2 - r1
            char[] r3 = new char[r3]
            r4 = 0
            r5 = r4
        L_0x0018:
            if (r1 >= r2) goto L_0x01bc
            byte r6 = r0[r1]
            if (r6 < 0) goto L_0x0034
            char r6 = (char) r6
            int r7 = r5 + 1
            r3[r5] = r6
            int r1 = r1 + 1
        L_0x0025:
            r5 = r7
            if (r1 >= r2) goto L_0x0018
            byte r6 = r0[r1]
            if (r6 < 0) goto L_0x0018
            int r1 = r1 + 1
            char r6 = (char) r6
            int r7 = r5 + 1
            r3[r5] = r6
            goto L_0x0025
        L_0x0034:
            int r7 = r6 >> 5
            r8 = -2
            r10 = 128(0x80, float:1.794E-43)
            r11 = 65533(0xfffd, float:9.1831E-41)
            if (r7 != r8) goto L_0x0075
            int r7 = r1 + 1
            if (r2 > r7) goto L_0x004c
            char r6 = (char) r11
            int r7 = r5 + 1
            r3[r5] = r6
        L_0x0047:
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
        L_0x0049:
            r5 = r7
        L_0x004a:
            r9 = 1
            goto L_0x0073
        L_0x004c:
            byte r7 = r0[r7]
            r8 = r7 & 192(0xc0, float:2.69E-43)
            if (r8 != r10) goto L_0x0054
            r8 = 1
            goto L_0x0055
        L_0x0054:
            r8 = r4
        L_0x0055:
            if (r8 != 0) goto L_0x005d
            char r6 = (char) r11
            int r7 = r5 + 1
            r3[r5] = r6
            goto L_0x0047
        L_0x005d:
            r7 = r7 ^ 3968(0xf80, float:5.56E-42)
            int r6 = r6 << 6
            r6 = r6 ^ r7
            if (r6 >= r10) goto L_0x006a
            char r6 = (char) r11
            int r7 = r5 + 1
            r3[r5] = r6
            goto L_0x006f
        L_0x006a:
            char r6 = (char) r6
            int r7 = r5 + 1
            r3[r5] = r6
        L_0x006f:
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
        L_0x0071:
            r5 = r7
        L_0x0072:
            r9 = 2
        L_0x0073:
            int r1 = r1 + r9
            goto L_0x0018
        L_0x0075:
            int r7 = r6 >> 4
            r13 = 57343(0xdfff, float:8.0355E-41)
            r14 = 55296(0xd800, float:7.7486E-41)
            r15 = 3
            if (r7 != r8) goto L_0x00f1
            int r7 = r1 + 2
            if (r2 > r7) goto L_0x009b
            char r6 = (char) r11
            int r7 = r5 + 1
            r3[r5] = r6
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            int r5 = r1 + 1
            if (r2 <= r5) goto L_0x0049
            byte r5 = r0[r5]
            r5 = r5 & 192(0xc0, float:2.69E-43)
            if (r5 != r10) goto L_0x0097
            r5 = 1
            goto L_0x0098
        L_0x0097:
            r5 = r4
        L_0x0098:
            if (r5 != 0) goto L_0x0071
            goto L_0x0049
        L_0x009b:
            int r8 = r1 + 1
            byte r8 = r0[r8]
            r9 = r8 & 192(0xc0, float:2.69E-43)
            if (r9 != r10) goto L_0x00a5
            r9 = 1
            goto L_0x00a6
        L_0x00a5:
            r9 = r4
        L_0x00a6:
            if (r9 != 0) goto L_0x00b0
            char r6 = (char) r11
            int r7 = r5 + 1
            r3[r5] = r6
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            goto L_0x0049
        L_0x00b0:
            byte r7 = r0[r7]
            r9 = r7 & 192(0xc0, float:2.69E-43)
            if (r9 != r10) goto L_0x00b8
            r9 = 1
            goto L_0x00b9
        L_0x00b8:
            r9 = r4
        L_0x00b9:
            if (r9 != 0) goto L_0x00c3
            char r6 = (char) r11
            int r7 = r5 + 1
            r3[r5] = r6
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            goto L_0x0071
        L_0x00c3:
            r9 = -123008(0xfffffffffffe1f80, float:NaN)
            r7 = r7 ^ r9
            int r8 = r8 << 6
            r7 = r7 ^ r8
            int r6 = r6 << 12
            r6 = r6 ^ r7
            r7 = 2048(0x800, float:2.87E-42)
            if (r6 >= r7) goto L_0x00d9
            char r6 = (char) r11
            int r7 = r5 + 1
            r3[r5] = r6
        L_0x00d6:
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            goto L_0x00ee
        L_0x00d9:
            if (r14 > r6) goto L_0x00df
            if (r6 > r13) goto L_0x00df
            r12 = 1
            goto L_0x00e0
        L_0x00df:
            r12 = r4
        L_0x00e0:
            if (r12 == 0) goto L_0x00e8
            char r6 = (char) r11
            int r7 = r5 + 1
            r3[r5] = r6
            goto L_0x00d6
        L_0x00e8:
            char r6 = (char) r6
            int r7 = r5 + 1
            r3[r5] = r6
            goto L_0x00d6
        L_0x00ee:
            r5 = r7
        L_0x00ef:
            r9 = r15
            goto L_0x0073
        L_0x00f1:
            int r7 = r6 >> 3
            if (r7 != r8) goto L_0x01b3
            int r7 = r1 + 3
            if (r2 > r7) goto L_0x0127
            int r6 = r5 + 1
            r3[r5] = r11
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            int r5 = r1 + 1
            if (r2 <= r5) goto L_0x0124
            byte r5 = r0[r5]
            r5 = r5 & 192(0xc0, float:2.69E-43)
            if (r5 != r10) goto L_0x010b
            r5 = 1
            goto L_0x010c
        L_0x010b:
            r5 = r4
        L_0x010c:
            if (r5 != 0) goto L_0x010f
            goto L_0x0124
        L_0x010f:
            int r5 = r1 + 2
            if (r2 <= r5) goto L_0x0121
            byte r5 = r0[r5]
            r5 = r5 & 192(0xc0, float:2.69E-43)
            if (r5 != r10) goto L_0x011b
            r12 = 1
            goto L_0x011c
        L_0x011b:
            r12 = r4
        L_0x011c:
            if (r12 != 0) goto L_0x011f
            goto L_0x0121
        L_0x011f:
            r5 = r6
            goto L_0x00ef
        L_0x0121:
            r5 = r6
            goto L_0x0072
        L_0x0124:
            r5 = r6
            goto L_0x004a
        L_0x0127:
            int r8 = r1 + 1
            byte r8 = r0[r8]
            r9 = r8 & 192(0xc0, float:2.69E-43)
            if (r9 != r10) goto L_0x0131
            r9 = 1
            goto L_0x0132
        L_0x0131:
            r9 = r4
        L_0x0132:
            if (r9 != 0) goto L_0x013b
            int r6 = r5 + 1
            r3[r5] = r11
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            goto L_0x0124
        L_0x013b:
            int r9 = r1 + 2
            byte r9 = r0[r9]
            r12 = r9 & 192(0xc0, float:2.69E-43)
            if (r12 != r10) goto L_0x0145
            r12 = 1
            goto L_0x0146
        L_0x0145:
            r12 = r4
        L_0x0146:
            if (r12 != 0) goto L_0x014f
            int r6 = r5 + 1
            r3[r5] = r11
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            goto L_0x0121
        L_0x014f:
            byte r7 = r0[r7]
            r12 = r7 & 192(0xc0, float:2.69E-43)
            if (r12 != r10) goto L_0x0157
            r10 = 1
            goto L_0x0158
        L_0x0157:
            r10 = r4
        L_0x0158:
            if (r10 != 0) goto L_0x0161
            int r6 = r5 + 1
            r3[r5] = r11
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            goto L_0x011f
        L_0x0161:
            r10 = 3678080(0x381f80, float:5.154088E-39)
            r7 = r7 ^ r10
            int r9 = r9 << 6
            r7 = r7 ^ r9
            int r8 = r8 << 12
            r7 = r7 ^ r8
            int r6 = r6 << 18
            r6 = r6 ^ r7
            r7 = 1114111(0x10ffff, float:1.561202E-39)
            if (r6 <= r7) goto L_0x017a
            int r6 = r5 + 1
            r3[r5] = r11
        L_0x0177:
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            goto L_0x01af
        L_0x017a:
            if (r14 > r6) goto L_0x0180
            if (r6 > r13) goto L_0x0180
            r12 = 1
            goto L_0x0181
        L_0x0180:
            r12 = r4
        L_0x0181:
            if (r12 == 0) goto L_0x0188
            int r6 = r5 + 1
            r3[r5] = r11
            goto L_0x0177
        L_0x0188:
            r7 = 65536(0x10000, float:9.18355E-41)
            if (r6 >= r7) goto L_0x0191
            int r6 = r5 + 1
            r3[r5] = r11
            goto L_0x0177
        L_0x0191:
            if (r6 == r11) goto L_0x01aa
            int r7 = r6 >>> 10
            r8 = 55232(0xd7c0, float:7.7397E-41)
            int r7 = r7 + r8
            char r7 = (char) r7
            int r8 = r5 + 1
            r3[r5] = r7
            r5 = r6 & 1023(0x3ff, float:1.434E-42)
            r6 = 56320(0xdc00, float:7.8921E-41)
            int r5 = r5 + r6
            char r5 = (char) r5
            int r6 = r8 + 1
            r3[r8] = r5
            goto L_0x0177
        L_0x01aa:
            int r6 = r5 + 1
            r3[r5] = r11
            goto L_0x0177
        L_0x01af:
            r9 = 4
            r5 = r6
            goto L_0x0073
        L_0x01b3:
            int r6 = r5 + 1
            r3[r5] = r11
            int r1 = r1 + 1
            r5 = r6
            goto L_0x0018
        L_0x01bc:
            java.lang.String r0 = kotlin.text.StringsKt.concatToString(r3, r4, r5)
            return r0
        L_0x01c1:
            java.lang.ArrayIndexOutOfBoundsException r3 = new java.lang.ArrayIndexOutOfBoundsException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "size="
            r4.append(r5)
            int r0 = r0.length
            r4.append(r0)
            java.lang.String r0 = " beginIndex="
            r4.append(r0)
            r4.append(r1)
            java.lang.String r0 = " endIndex="
            r4.append(r0)
            r4.append(r2)
            java.lang.String r0 = r4.toString()
            r3.<init>(r0)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal._Utf8Kt.commonToUtf8String(byte[], int, int):java.lang.String");
    }

    public static final byte[] commonAsUtf8ToByteArray(String str) {
        int i;
        int i2;
        int i3;
        int i4;
        Intrinsics.checkNotNullParameter(str, "<this>");
        byte[] bArr = new byte[(str.length() * 4)];
        int length = str.length();
        if (length > 0) {
            int i5 = 0;
            while (true) {
                int i6 = i + 1;
                char charAt = str.charAt(i);
                if (Intrinsics.compare((int) charAt, 128) >= 0) {
                    int length2 = str.length();
                    int i7 = i;
                    while (i < length2) {
                        char charAt2 = str.charAt(i);
                        if (Intrinsics.compare((int) charAt2, 128) < 0) {
                            int i8 = i7 + 1;
                            bArr[i7] = (byte) charAt2;
                            i++;
                            while (true) {
                                i7 = i8;
                                if (i >= length2 || Intrinsics.compare((int) str.charAt(i), 128) >= 0) {
                                    break;
                                }
                                i8 = i7 + 1;
                                bArr[i7] = (byte) str.charAt(i);
                                i++;
                            }
                        } else {
                            if (Intrinsics.compare((int) charAt2, 2048) < 0) {
                                int i9 = i7 + 1;
                                bArr[i7] = (byte) ((charAt2 >> 6) | Wbxml.EXT_0);
                                i2 = i9 + 1;
                                bArr[i9] = (byte) ((charAt2 & '?') | 128);
                            } else {
                                boolean z = true;
                                if (!(55296 <= charAt2 && charAt2 <= 57343)) {
                                    int i10 = i7 + 1;
                                    bArr[i7] = (byte) ((charAt2 >> 12) | 224);
                                    int i11 = i10 + 1;
                                    bArr[i10] = (byte) (((charAt2 >> 6) & 63) | 128);
                                    i2 = i11 + 1;
                                    bArr[i11] = (byte) ((charAt2 & '?') | 128);
                                } else {
                                    if (Intrinsics.compare((int) charAt2, 56319) <= 0 && length2 > (i3 = i + 1)) {
                                        char charAt3 = str.charAt(i3);
                                        if (56320 > charAt3 || charAt3 > 57343) {
                                            z = false;
                                        }
                                        if (z) {
                                            int charAt4 = ((charAt2 << 10) + str.charAt(i3)) - 56613888;
                                            int i12 = i7 + 1;
                                            bArr[i7] = (byte) ((charAt4 >> 18) | 240);
                                            int i13 = i12 + 1;
                                            bArr[i12] = (byte) (((charAt4 >> 12) & 63) | 128);
                                            int i14 = i13 + 1;
                                            bArr[i13] = (byte) (((charAt4 >> 6) & 63) | 128);
                                            i2 = i14 + 1;
                                            bArr[i14] = (byte) ((charAt4 & 63) | 128);
                                            i4 = i + 2;
                                            i7 = i2;
                                        }
                                    }
                                    i2 = i7 + 1;
                                    bArr[i7] = Utf8.REPLACEMENT_BYTE;
                                }
                            }
                            i4 = i + 1;
                            i7 = i2;
                        }
                    }
                    byte[] copyOf = Arrays.copyOf(bArr, i7);
                    Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(this, newSize)");
                    return copyOf;
                }
                bArr[i] = (byte) charAt;
                if (i6 >= length) {
                    break;
                }
                i5 = i6;
            }
        }
        byte[] copyOf2 = Arrays.copyOf(bArr, str.length());
        Intrinsics.checkNotNullExpressionValue(copyOf2, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf2;
    }
}
