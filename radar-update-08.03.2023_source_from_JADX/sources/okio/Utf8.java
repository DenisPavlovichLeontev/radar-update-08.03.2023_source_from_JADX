package okio;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import org.kxml2.wap.Wbxml;

@Metadata(mo19893d1 = {"\u0000D\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\u001a\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0001H\b\u001a\u0011\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0007H\b\u001a4\u0010\u0010\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\bø\u0001\u0000\u001a4\u0010\u0017\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\bø\u0001\u0000\u001a4\u0010\u0018\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\bø\u0001\u0000\u001a4\u0010\u0019\u001a\u00020\u0016*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00160\u0015H\bø\u0001\u0000\u001a4\u0010\u001a\u001a\u00020\u0016*\u00020\u001b2\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00160\u0015H\bø\u0001\u0000\u001a4\u0010\u001c\u001a\u00020\u0016*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\bø\u0001\u0000\u001a%\u0010\u001d\u001a\u00020\u001e*\u00020\u001b2\b\b\u0002\u0010\u0012\u001a\u00020\u00012\b\b\u0002\u0010\u0013\u001a\u00020\u0001H\u0007¢\u0006\u0002\b\u001f\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\tXT¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\u0002\u0007\n\u0005\b20\u0001¨\u0006 "}, mo19894d2 = {"HIGH_SURROGATE_HEADER", "", "LOG_SURROGATE_HEADER", "MASK_2BYTES", "MASK_3BYTES", "MASK_4BYTES", "REPLACEMENT_BYTE", "", "REPLACEMENT_CHARACTER", "", "REPLACEMENT_CODE_POINT", "isIsoControl", "", "codePoint", "isUtf8Continuation", "byte", "process2Utf8Bytes", "", "beginIndex", "endIndex", "yield", "Lkotlin/Function1;", "", "process3Utf8Bytes", "process4Utf8Bytes", "processUtf16Chars", "processUtf8Bytes", "", "processUtf8CodePoints", "utf8Size", "", "size", "okio"}, mo19895k = 2, mo19896mv = {1, 5, 1}, mo19898xi = 48)
/* compiled from: Utf8.kt */
public final class Utf8 {
    public static final int HIGH_SURROGATE_HEADER = 55232;
    public static final int LOG_SURROGATE_HEADER = 56320;
    public static final int MASK_2BYTES = 3968;
    public static final int MASK_3BYTES = -123008;
    public static final int MASK_4BYTES = 3678080;
    public static final byte REPLACEMENT_BYTE = 63;
    public static final char REPLACEMENT_CHARACTER = '�';
    public static final int REPLACEMENT_CODE_POINT = 65533;

    public static final boolean isIsoControl(int i) {
        if (i >= 0 && i <= 31) {
            return true;
        }
        return 127 <= i && i <= 159;
    }

    public static final boolean isUtf8Continuation(byte b) {
        return (b & 192) == 128;
    }

    public static final long size(String str) {
        Intrinsics.checkNotNullParameter(str, "<this>");
        return size$default(str, 0, 0, 3, (Object) null);
    }

    public static final long size(String str, int i) {
        Intrinsics.checkNotNullParameter(str, "<this>");
        return size$default(str, i, 0, 2, (Object) null);
    }

    public static /* synthetic */ long size$default(String str, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        return size(str, i, i2);
    }

    public static final long size(String str, int i, int i2) {
        int i3;
        char c;
        Intrinsics.checkNotNullParameter(str, "<this>");
        boolean z = true;
        if (i >= 0) {
            if (i2 >= i) {
                if (i2 > str.length()) {
                    z = false;
                }
                if (z) {
                    long j = 0;
                    while (i < i2) {
                        char charAt = str.charAt(i);
                        if (charAt < 128) {
                            j++;
                        } else {
                            if (charAt < 2048) {
                                i3 = 2;
                            } else if (charAt < 55296 || charAt > 57343) {
                                i3 = 3;
                            } else {
                                int i4 = i + 1;
                                if (i4 < i2) {
                                    c = str.charAt(i4);
                                } else {
                                    c = 0;
                                }
                                if (charAt > 56319 || c < 56320 || c > 57343) {
                                    j++;
                                    i = i4;
                                } else {
                                    j += (long) 4;
                                    i += 2;
                                }
                            }
                            j += (long) i3;
                        }
                        i++;
                    }
                    return j;
                }
                throw new IllegalArgumentException(("endIndex > string.length: " + i2 + " > " + str.length()).toString());
            }
            throw new IllegalArgumentException(("endIndex < beginIndex: " + i2 + " < " + i).toString());
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("beginIndex < 0: ", Integer.valueOf(i)).toString());
    }

    public static final void processUtf8Bytes(String str, int i, int i2, Function1<? super Byte, Unit> function1) {
        int i3;
        Intrinsics.checkNotNullParameter(str, "<this>");
        Intrinsics.checkNotNullParameter(function1, "yield");
        while (i < i2) {
            char charAt = str.charAt(i);
            if (Intrinsics.compare((int) charAt, 128) < 0) {
                function1.invoke(Byte.valueOf((byte) charAt));
                i++;
                while (i < i2 && Intrinsics.compare((int) str.charAt(i), 128) < 0) {
                    function1.invoke(Byte.valueOf((byte) str.charAt(i)));
                    i++;
                }
            } else {
                if (Intrinsics.compare((int) charAt, 2048) < 0) {
                    function1.invoke(Byte.valueOf((byte) ((charAt >> 6) | Wbxml.EXT_0)));
                    function1.invoke(Byte.valueOf((byte) ((charAt & '?') | 128)));
                } else {
                    boolean z = false;
                    if (!(55296 <= charAt && charAt <= 57343)) {
                        function1.invoke(Byte.valueOf((byte) ((charAt >> 12) | 224)));
                        function1.invoke(Byte.valueOf((byte) (((charAt >> 6) & 63) | 128)));
                        function1.invoke(Byte.valueOf((byte) ((charAt & '?') | 128)));
                    } else {
                        if (Intrinsics.compare((int) charAt, 56319) <= 0 && i2 > (i3 = i + 1)) {
                            char charAt2 = str.charAt(i3);
                            if (56320 <= charAt2 && charAt2 <= 57343) {
                                z = true;
                            }
                            if (z) {
                                int charAt3 = ((charAt << 10) + str.charAt(i3)) - 56613888;
                                function1.invoke(Byte.valueOf((byte) ((charAt3 >> 18) | 240)));
                                function1.invoke(Byte.valueOf((byte) (((charAt3 >> 12) & 63) | 128)));
                                function1.invoke(Byte.valueOf((byte) (((charAt3 >> 6) & 63) | 128)));
                                function1.invoke(Byte.valueOf((byte) ((charAt3 & 63) | 128)));
                                i += 2;
                            }
                        }
                        function1.invoke(Byte.valueOf(REPLACEMENT_BYTE));
                    }
                }
                i++;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0091, code lost:
        if (r9 == false) goto L_0x004a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0114, code lost:
        if (r9 == false) goto L_0x006b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void processUtf8CodePoints(byte[] r16, int r17, int r18, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> r19) {
        /*
            r0 = r16
            r1 = r18
            r2 = r19
            java.lang.String r3 = "<this>"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r0, r3)
            java.lang.String r3 = "yield"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r3)
            r3 = r17
        L_0x0012:
            if (r3 >= r1) goto L_0x0199
            byte r4 = r0[r3]
            if (r4 < 0) goto L_0x0031
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r2.invoke(r4)
            int r3 = r3 + 1
        L_0x0021:
            if (r3 >= r1) goto L_0x0012
            byte r4 = r0[r3]
            if (r4 < 0) goto L_0x0012
            int r3 = r3 + 1
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r2.invoke(r4)
            goto L_0x0021
        L_0x0031:
            int r5 = r4 >> 5
            r6 = -2
            r8 = 128(0x80, float:1.794E-43)
            r9 = 0
            r10 = 65533(0xfffd, float:9.1831E-41)
            r11 = 1
            if (r5 != r6) goto L_0x006e
            int r5 = r3 + 1
            if (r1 > r5) goto L_0x004c
        L_0x0041:
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
        L_0x004a:
            r7 = r11
            goto L_0x006c
        L_0x004c:
            byte r5 = r0[r5]
            r6 = r5 & 192(0xc0, float:2.69E-43)
            if (r6 != r8) goto L_0x0053
            r9 = r11
        L_0x0053:
            if (r9 != 0) goto L_0x0056
            goto L_0x0041
        L_0x0056:
            r5 = r5 ^ 3968(0xf80, float:5.56E-42)
            int r4 = r4 << 6
            r4 = r4 ^ r5
            if (r4 >= r8) goto L_0x0062
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)
            goto L_0x0066
        L_0x0062:
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
        L_0x0066:
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
        L_0x006b:
            r7 = 2
        L_0x006c:
            int r3 = r3 + r7
            goto L_0x0012
        L_0x006e:
            int r5 = r4 >> 4
            r12 = 57343(0xdfff, float:8.0355E-41)
            r13 = 55296(0xd800, float:7.7486E-41)
            r14 = 3
            if (r5 != r6) goto L_0x00e7
            int r5 = r3 + 2
            if (r1 > r5) goto L_0x0094
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            int r4 = r3 + 1
            if (r1 <= r4) goto L_0x004a
            byte r4 = r0[r4]
            r4 = r4 & 192(0xc0, float:2.69E-43)
            if (r4 != r8) goto L_0x0091
            r9 = r11
        L_0x0091:
            if (r9 != 0) goto L_0x006b
            goto L_0x004a
        L_0x0094:
            int r6 = r3 + 1
            byte r6 = r0[r6]
            r15 = r6 & 192(0xc0, float:2.69E-43)
            if (r15 != r8) goto L_0x009e
            r15 = r11
            goto L_0x009f
        L_0x009e:
            r15 = r9
        L_0x009f:
            if (r15 != 0) goto L_0x00ab
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x004a
        L_0x00ab:
            byte r5 = r0[r5]
            r15 = r5 & 192(0xc0, float:2.69E-43)
            if (r15 != r8) goto L_0x00b3
            r8 = r11
            goto L_0x00b4
        L_0x00b3:
            r8 = r9
        L_0x00b4:
            if (r8 != 0) goto L_0x00c0
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x006b
        L_0x00c0:
            r7 = -123008(0xfffffffffffe1f80, float:NaN)
            r5 = r5 ^ r7
            int r6 = r6 << 6
            r5 = r5 ^ r6
            int r4 = r4 << 12
            r4 = r4 ^ r5
            r5 = 2048(0x800, float:2.87E-42)
            if (r4 >= r5) goto L_0x00d8
        L_0x00ce:
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)
        L_0x00d2:
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x00e5
        L_0x00d8:
            if (r13 > r4) goto L_0x00dd
            if (r4 > r12) goto L_0x00dd
            r9 = r11
        L_0x00dd:
            if (r9 == 0) goto L_0x00e0
            goto L_0x00ce
        L_0x00e0:
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            goto L_0x00d2
        L_0x00e5:
            r7 = r14
            goto L_0x006c
        L_0x00e7:
            int r5 = r4 >> 3
            if (r5 != r6) goto L_0x018e
            int r5 = r3 + 3
            if (r1 > r5) goto L_0x0118
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            int r4 = r3 + 1
            if (r1 <= r4) goto L_0x004a
            byte r4 = r0[r4]
            r4 = r4 & 192(0xc0, float:2.69E-43)
            if (r4 != r8) goto L_0x0104
            r4 = r11
            goto L_0x0105
        L_0x0104:
            r4 = r9
        L_0x0105:
            if (r4 != 0) goto L_0x0109
            goto L_0x004a
        L_0x0109:
            int r4 = r3 + 2
            if (r1 <= r4) goto L_0x006b
            byte r4 = r0[r4]
            r4 = r4 & 192(0xc0, float:2.69E-43)
            if (r4 != r8) goto L_0x0114
            r9 = r11
        L_0x0114:
            if (r9 != 0) goto L_0x00e5
            goto L_0x006b
        L_0x0118:
            int r6 = r3 + 1
            byte r6 = r0[r6]
            r15 = r6 & 192(0xc0, float:2.69E-43)
            if (r15 != r8) goto L_0x0122
            r15 = r11
            goto L_0x0123
        L_0x0122:
            r15 = r9
        L_0x0123:
            if (r15 != 0) goto L_0x0130
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x004a
        L_0x0130:
            int r15 = r3 + 2
            byte r15 = r0[r15]
            r7 = r15 & 192(0xc0, float:2.69E-43)
            if (r7 != r8) goto L_0x013a
            r7 = r11
            goto L_0x013b
        L_0x013a:
            r7 = r9
        L_0x013b:
            if (r7 != 0) goto L_0x0148
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x006b
        L_0x0148:
            byte r5 = r0[r5]
            r7 = r5 & 192(0xc0, float:2.69E-43)
            if (r7 != r8) goto L_0x0150
            r7 = r11
            goto L_0x0151
        L_0x0150:
            r7 = r9
        L_0x0151:
            if (r7 != 0) goto L_0x015d
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x00e5
        L_0x015d:
            r7 = 3678080(0x381f80, float:5.154088E-39)
            r5 = r5 ^ r7
            int r7 = r15 << 6
            r5 = r5 ^ r7
            int r6 = r6 << 12
            r5 = r5 ^ r6
            int r4 = r4 << 18
            r4 = r4 ^ r5
            r5 = 1114111(0x10ffff, float:1.561202E-39)
            if (r4 <= r5) goto L_0x0179
        L_0x016f:
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)
        L_0x0173:
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x018b
        L_0x0179:
            if (r13 > r4) goto L_0x017e
            if (r4 > r12) goto L_0x017e
            r9 = r11
        L_0x017e:
            if (r9 == 0) goto L_0x0181
            goto L_0x016f
        L_0x0181:
            r5 = 65536(0x10000, float:9.18355E-41)
            if (r4 >= r5) goto L_0x0186
            goto L_0x016f
        L_0x0186:
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            goto L_0x0173
        L_0x018b:
            r7 = 4
            goto L_0x006c
        L_0x018e:
            java.lang.Integer r4 = java.lang.Integer.valueOf(r10)
            r2.invoke(r4)
            int r3 = r3 + 1
            goto L_0x0012
        L_0x0199:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Utf8.processUtf8CodePoints(byte[], int, int, kotlin.jvm.functions.Function1):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0093, code lost:
        if (r9 == false) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0116, code lost:
        if (r9 == false) goto L_0x006c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void processUtf16Chars(byte[] r16, int r17, int r18, kotlin.jvm.functions.Function1<? super java.lang.Character, kotlin.Unit> r19) {
        /*
            r0 = r16
            r1 = r18
            r2 = r19
            java.lang.String r3 = "<this>"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r0, r3)
            java.lang.String r3 = "yield"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r3)
            r3 = r17
        L_0x0012:
            if (r3 >= r1) goto L_0x01b5
            byte r4 = r0[r3]
            if (r4 < 0) goto L_0x0033
            char r4 = (char) r4
            java.lang.Character r4 = java.lang.Character.valueOf(r4)
            r2.invoke(r4)
            int r3 = r3 + 1
        L_0x0022:
            if (r3 >= r1) goto L_0x0012
            byte r4 = r0[r3]
            if (r4 < 0) goto L_0x0012
            int r3 = r3 + 1
            char r4 = (char) r4
            java.lang.Character r4 = java.lang.Character.valueOf(r4)
            r2.invoke(r4)
            goto L_0x0022
        L_0x0033:
            int r5 = r4 >> 5
            r6 = -2
            r8 = 128(0x80, float:1.794E-43)
            r9 = 0
            r10 = 65533(0xfffd, float:9.1831E-41)
            r11 = 1
            if (r5 != r6) goto L_0x006f
            int r5 = r3 + 1
            if (r1 > r5) goto L_0x004f
        L_0x0043:
            char r4 = (char) r10
            java.lang.Character r4 = java.lang.Character.valueOf(r4)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
        L_0x004d:
            r7 = r11
            goto L_0x006d
        L_0x004f:
            byte r5 = r0[r5]
            r6 = r5 & 192(0xc0, float:2.69E-43)
            if (r6 != r8) goto L_0x0056
            r9 = r11
        L_0x0056:
            if (r9 != 0) goto L_0x0059
            goto L_0x0043
        L_0x0059:
            r5 = r5 ^ 3968(0xf80, float:5.56E-42)
            int r4 = r4 << 6
            r4 = r4 ^ r5
            if (r4 >= r8) goto L_0x0062
            char r4 = (char) r10
            goto L_0x0063
        L_0x0062:
            char r4 = (char) r4
        L_0x0063:
            java.lang.Character r4 = java.lang.Character.valueOf(r4)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
        L_0x006c:
            r7 = 2
        L_0x006d:
            int r3 = r3 + r7
            goto L_0x0012
        L_0x006f:
            int r5 = r4 >> 4
            r12 = 57343(0xdfff, float:8.0355E-41)
            r13 = 55296(0xd800, float:7.7486E-41)
            r14 = 3
            if (r5 != r6) goto L_0x00e9
            int r5 = r3 + 2
            if (r1 > r5) goto L_0x0096
            char r4 = (char) r10
            java.lang.Character r4 = java.lang.Character.valueOf(r4)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            int r4 = r3 + 1
            if (r1 <= r4) goto L_0x004d
            byte r4 = r0[r4]
            r4 = r4 & 192(0xc0, float:2.69E-43)
            if (r4 != r8) goto L_0x0093
            r9 = r11
        L_0x0093:
            if (r9 != 0) goto L_0x006c
            goto L_0x004d
        L_0x0096:
            int r6 = r3 + 1
            byte r6 = r0[r6]
            r15 = r6 & 192(0xc0, float:2.69E-43)
            if (r15 != r8) goto L_0x00a0
            r15 = r11
            goto L_0x00a1
        L_0x00a0:
            r15 = r9
        L_0x00a1:
            if (r15 != 0) goto L_0x00ae
            char r4 = (char) r10
            java.lang.Character r4 = java.lang.Character.valueOf(r4)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x004d
        L_0x00ae:
            byte r5 = r0[r5]
            r15 = r5 & 192(0xc0, float:2.69E-43)
            if (r15 != r8) goto L_0x00b6
            r8 = r11
            goto L_0x00b7
        L_0x00b6:
            r8 = r9
        L_0x00b7:
            if (r8 != 0) goto L_0x00c4
            char r4 = (char) r10
            java.lang.Character r4 = java.lang.Character.valueOf(r4)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x006c
        L_0x00c4:
            r7 = -123008(0xfffffffffffe1f80, float:NaN)
            r5 = r5 ^ r7
            int r6 = r6 << 6
            r5 = r5 ^ r6
            int r4 = r4 << 12
            r4 = r4 ^ r5
            r5 = 2048(0x800, float:2.87E-42)
            if (r4 >= r5) goto L_0x00dd
        L_0x00d2:
            char r4 = (char) r10
        L_0x00d3:
            java.lang.Character r4 = java.lang.Character.valueOf(r4)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x00e7
        L_0x00dd:
            if (r13 > r4) goto L_0x00e2
            if (r4 > r12) goto L_0x00e2
            r9 = r11
        L_0x00e2:
            if (r9 == 0) goto L_0x00e5
            goto L_0x00d2
        L_0x00e5:
            char r4 = (char) r4
            goto L_0x00d3
        L_0x00e7:
            r7 = r14
            goto L_0x006d
        L_0x00e9:
            int r5 = r4 >> 3
            if (r5 != r6) goto L_0x01aa
            int r5 = r3 + 3
            if (r1 > r5) goto L_0x011a
            java.lang.Character r4 = java.lang.Character.valueOf(r10)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            int r4 = r3 + 1
            if (r1 <= r4) goto L_0x004d
            byte r4 = r0[r4]
            r4 = r4 & 192(0xc0, float:2.69E-43)
            if (r4 != r8) goto L_0x0106
            r4 = r11
            goto L_0x0107
        L_0x0106:
            r4 = r9
        L_0x0107:
            if (r4 != 0) goto L_0x010b
            goto L_0x004d
        L_0x010b:
            int r4 = r3 + 2
            if (r1 <= r4) goto L_0x006c
            byte r4 = r0[r4]
            r4 = r4 & 192(0xc0, float:2.69E-43)
            if (r4 != r8) goto L_0x0116
            r9 = r11
        L_0x0116:
            if (r9 != 0) goto L_0x00e7
            goto L_0x006c
        L_0x011a:
            int r6 = r3 + 1
            byte r6 = r0[r6]
            r15 = r6 & 192(0xc0, float:2.69E-43)
            if (r15 != r8) goto L_0x0124
            r15 = r11
            goto L_0x0125
        L_0x0124:
            r15 = r9
        L_0x0125:
            if (r15 != 0) goto L_0x0132
            java.lang.Character r4 = java.lang.Character.valueOf(r10)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x004d
        L_0x0132:
            int r15 = r3 + 2
            byte r15 = r0[r15]
            r7 = r15 & 192(0xc0, float:2.69E-43)
            if (r7 != r8) goto L_0x013c
            r7 = r11
            goto L_0x013d
        L_0x013c:
            r7 = r9
        L_0x013d:
            if (r7 != 0) goto L_0x014a
            java.lang.Character r4 = java.lang.Character.valueOf(r10)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x006c
        L_0x014a:
            byte r5 = r0[r5]
            r7 = r5 & 192(0xc0, float:2.69E-43)
            if (r7 != r8) goto L_0x0152
            r7 = r11
            goto L_0x0153
        L_0x0152:
            r7 = r9
        L_0x0153:
            if (r7 != 0) goto L_0x015f
            java.lang.Character r4 = java.lang.Character.valueOf(r10)
            r2.invoke(r4)
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x00e7
        L_0x015f:
            r7 = 3678080(0x381f80, float:5.154088E-39)
            r5 = r5 ^ r7
            int r7 = r15 << 6
            r5 = r5 ^ r7
            int r6 = r6 << 12
            r5 = r5 ^ r6
            int r4 = r4 << 18
            r4 = r4 ^ r5
            r5 = 1114111(0x10ffff, float:1.561202E-39)
            if (r4 <= r5) goto L_0x017b
        L_0x0171:
            java.lang.Character r4 = java.lang.Character.valueOf(r10)
            r2.invoke(r4)
        L_0x0178:
            kotlin.Unit r4 = kotlin.Unit.INSTANCE
            goto L_0x01a7
        L_0x017b:
            if (r13 > r4) goto L_0x0180
            if (r4 > r12) goto L_0x0180
            r9 = r11
        L_0x0180:
            if (r9 == 0) goto L_0x0183
            goto L_0x0171
        L_0x0183:
            r5 = 65536(0x10000, float:9.18355E-41)
            if (r4 >= r5) goto L_0x0188
            goto L_0x0171
        L_0x0188:
            if (r4 == r10) goto L_0x0171
            int r5 = r4 >>> 10
            r6 = 55232(0xd7c0, float:7.7397E-41)
            int r5 = r5 + r6
            char r5 = (char) r5
            java.lang.Character r5 = java.lang.Character.valueOf(r5)
            r2.invoke(r5)
            r4 = r4 & 1023(0x3ff, float:1.434E-42)
            r5 = 56320(0xdc00, float:7.8921E-41)
            int r4 = r4 + r5
            char r4 = (char) r4
            java.lang.Character r4 = java.lang.Character.valueOf(r4)
            r2.invoke(r4)
            goto L_0x0178
        L_0x01a7:
            r7 = 4
            goto L_0x006d
        L_0x01aa:
            java.lang.Character r4 = java.lang.Character.valueOf(r10)
            r2.invoke(r4)
            int r3 = r3 + 1
            goto L_0x0012
        L_0x01b5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Utf8.processUtf16Chars(byte[], int, int, kotlin.jvm.functions.Function1):void");
    }

    public static final int process2Utf8Bytes(byte[] bArr, int i, int i2, Function1<? super Integer, Unit> function1) {
        Intrinsics.checkNotNullParameter(bArr, "<this>");
        Intrinsics.checkNotNullParameter(function1, "yield");
        int i3 = i + 1;
        Integer valueOf = Integer.valueOf(REPLACEMENT_CODE_POINT);
        if (i2 <= i3) {
            function1.invoke(valueOf);
            return 1;
        }
        byte b = bArr[i];
        byte b2 = bArr[i3];
        if (!((b2 & 192) == 128)) {
            function1.invoke(valueOf);
            return 1;
        }
        byte b3 = (b2 ^ ByteCompanionObject.MIN_VALUE) ^ (b << 6);
        if (b3 < 128) {
            function1.invoke(valueOf);
            return 2;
        }
        function1.invoke(Integer.valueOf(b3));
        return 2;
    }

    public static final int process3Utf8Bytes(byte[] bArr, int i, int i2, Function1<? super Integer, Unit> function1) {
        Intrinsics.checkNotNullParameter(bArr, "<this>");
        Intrinsics.checkNotNullParameter(function1, "yield");
        int i3 = i + 2;
        boolean z = false;
        Integer valueOf = Integer.valueOf(REPLACEMENT_CODE_POINT);
        if (i2 <= i3) {
            function1.invoke(valueOf);
            int i4 = i + 1;
            if (i2 > i4) {
                if ((bArr[i4] & 192) == 128) {
                    z = true;
                }
                return !z ? 1 : 2;
            }
        }
        byte b = bArr[i];
        byte b2 = bArr[i + 1];
        if (!((b2 & 192) == 128)) {
            function1.invoke(valueOf);
            return 1;
        }
        byte b3 = bArr[i3];
        if (!((b3 & 192) == 128)) {
            function1.invoke(valueOf);
            return 2;
        }
        byte b4 = ((b3 ^ ByteCompanionObject.MIN_VALUE) ^ (b2 << 6)) ^ (b << 12);
        if (b4 < 2048) {
            function1.invoke(valueOf);
            return 3;
        }
        if (55296 <= b4 && b4 <= 57343) {
            z = true;
        }
        if (z) {
            function1.invoke(valueOf);
            return 3;
        }
        function1.invoke(Integer.valueOf(b4));
        return 3;
    }

    public static final int process4Utf8Bytes(byte[] bArr, int i, int i2, Function1<? super Integer, Unit> function1) {
        Intrinsics.checkNotNullParameter(bArr, "<this>");
        Intrinsics.checkNotNullParameter(function1, "yield");
        int i3 = i + 3;
        boolean z = false;
        Integer valueOf = Integer.valueOf(REPLACEMENT_CODE_POINT);
        if (i2 <= i3) {
            function1.invoke(valueOf);
            int i4 = i + 1;
            if (i2 > i4) {
                if ((bArr[i4] & 192) == 128) {
                    int i5 = i + 2;
                    if (i2 > i5) {
                        if ((bArr[i5] & 192) == 128) {
                            z = true;
                        }
                        return !z ? 2 : 3;
                    }
                }
            }
            return 1;
        }
        byte b = bArr[i];
        byte b2 = bArr[i + 1];
        if (!((b2 & 192) == 128)) {
            function1.invoke(valueOf);
            return 1;
        }
        byte b3 = bArr[i + 2];
        if (!((b3 & 192) == 128)) {
            function1.invoke(valueOf);
            return 2;
        }
        byte b4 = bArr[i3];
        if (!((b4 & 192) == 128)) {
            function1.invoke(valueOf);
            return 3;
        }
        byte b5 = (((b4 ^ ByteCompanionObject.MIN_VALUE) ^ (b3 << 6)) ^ (b2 << 12)) ^ (b << 18);
        if (b5 > 1114111) {
            function1.invoke(valueOf);
            return 4;
        }
        if (55296 <= b5 && b5 <= 57343) {
            z = true;
        }
        if (z) {
            function1.invoke(valueOf);
            return 4;
        } else if (b5 < 65536) {
            function1.invoke(valueOf);
            return 4;
        } else {
            function1.invoke(Integer.valueOf(b5));
            return 4;
        }
    }
}
