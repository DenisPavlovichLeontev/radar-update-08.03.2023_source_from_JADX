package p005ar.com.hjg.pngj;

import kotlin.UByte;
import org.kxml2.wap.Wbxml;
import p005ar.com.hjg.pngj.chunks.PngChunkPLTE;
import p005ar.com.hjg.pngj.chunks.PngChunkTRNS;

/* renamed from: ar.com.hjg.pngj.ImageLineHelper */
public class ImageLineHelper {
    static int[][] DEPTH_UNPACK;
    static int[] DEPTH_UNPACK_1;
    static int[] DEPTH_UNPACK_2;
    static int[] DEPTH_UNPACK_4;

    public static int clampTo_0_255(int i) {
        if (i > 255) {
            return 255;
        }
        if (i < 0) {
            return 0;
        }
        return i;
    }

    public static int clampTo_0_65535(int i) {
        if (i > 65535) {
            return 65535;
        }
        if (i < 0) {
            return 0;
        }
        return i;
    }

    public static int clampTo_128_127(int i) {
        if (i > 127) {
            return 127;
        }
        if (i < -128) {
            return -128;
        }
        return i;
    }

    static int getMaskForPackedFormats(int i) {
        if (i == 4) {
            return 240;
        }
        if (i == 2) {
            return Wbxml.EXT_0;
        }
        return 128;
    }

    static int getMaskForPackedFormatsLs(int i) {
        if (i == 4) {
            return 15;
        }
        return i == 2 ? 3 : 1;
    }

    public static int interpol(int i, int i2, int i3, int i4, double d, double d2) {
        double d3 = 1.0d - d;
        return (int) ((((((double) i) * d3) + (((double) i2) * d)) * (1.0d - d2)) + (((((double) i3) * d3) + (((double) i4) * d)) * d2) + 0.5d);
    }

    public static byte scaleDown(int i, byte b) {
        return i < 8 ? (byte) (b >> (8 - i)) : b;
    }

    private static void initDepthScale() {
        DEPTH_UNPACK_1 = new int[2];
        for (int i = 0; i < 2; i++) {
            DEPTH_UNPACK_1[i] = i * 255;
        }
        DEPTH_UNPACK_2 = new int[4];
        for (int i2 = 0; i2 < 4; i2++) {
            DEPTH_UNPACK_2[i2] = (i2 * 255) / 3;
        }
        DEPTH_UNPACK_4 = new int[16];
        for (int i3 = 0; i3 < 16; i3++) {
            DEPTH_UNPACK_4[i3] = (i3 * 255) / 15;
        }
        DEPTH_UNPACK = new int[][]{null, DEPTH_UNPACK_1, DEPTH_UNPACK_2, null, DEPTH_UNPACK_4};
    }

    public static void scaleUp(IImageLineArray iImageLineArray) {
        if (!iImageLineArray.getImageInfo().indexed && iImageLineArray.getImageInfo().bitDepth < 8) {
            if (DEPTH_UNPACK_1 == null || DEPTH_UNPACK == null) {
                initDepthScale();
            }
            int[] iArr = DEPTH_UNPACK[iImageLineArray.getImageInfo().bitDepth];
            int i = 0;
            if (iImageLineArray instanceof ImageLineInt) {
                ImageLineInt imageLineInt = (ImageLineInt) iImageLineArray;
                while (i < imageLineInt.getSize()) {
                    imageLineInt.scanline[i] = iArr[imageLineInt.scanline[i]];
                    i++;
                }
            } else if (iImageLineArray instanceof ImageLineByte) {
                ImageLineByte imageLineByte = (ImageLineByte) iImageLineArray;
                while (i < imageLineByte.getSize()) {
                    imageLineByte.scanline[i] = (byte) iArr[imageLineByte.scanline[i]];
                    i++;
                }
            } else {
                throw new PngjException("not implemented");
            }
        }
    }

    public static void scaleDown(IImageLineArray iImageLineArray) {
        if (!iImageLineArray.getImageInfo().indexed && iImageLineArray.getImageInfo().bitDepth < 8) {
            boolean z = iImageLineArray instanceof ImageLineInt;
            if (z) {
                int i = 8 - iImageLineArray.getImageInfo().bitDepth;
                int i2 = 0;
                if (z) {
                    ImageLineInt imageLineInt = (ImageLineInt) iImageLineArray;
                    while (i2 < iImageLineArray.getSize()) {
                        imageLineInt.scanline[i2] = imageLineInt.scanline[i2] >> i;
                        i2++;
                    }
                } else if (iImageLineArray instanceof ImageLineByte) {
                    ImageLineByte imageLineByte = (ImageLineByte) iImageLineArray;
                    while (i2 < iImageLineArray.getSize()) {
                        imageLineByte.scanline[i2] = (byte) ((imageLineByte.scanline[i2] & UByte.MAX_VALUE) >> i);
                        i2++;
                    }
                }
            } else {
                throw new PngjException("not implemented");
            }
        }
    }

    public static byte scaleUp(int i, byte b) {
        return i < 8 ? (byte) DEPTH_UNPACK[i][b] : b;
    }

    public static int[] palette2rgb(ImageLineInt imageLineInt, PngChunkPLTE pngChunkPLTE, PngChunkTRNS pngChunkTRNS, int[] iArr) {
        return palette2rgb(imageLineInt, pngChunkPLTE, pngChunkTRNS, iArr, false);
    }

    static int[] lineToARGB32(ImageLineByte imageLineByte, PngChunkPLTE pngChunkPLTE, PngChunkTRNS pngChunkTRNS, int[] iArr) {
        byte b;
        byte b2;
        boolean z = imageLineByte.imgInfo.alpha;
        int i = imageLineByte.getImageInfo().cols;
        if (iArr == null || iArr.length < i) {
            iArr = new int[i];
        }
        if (imageLineByte.getImageInfo().indexed) {
            int length = pngChunkTRNS != null ? pngChunkTRNS.getPalletteAlpha().length : 0;
            for (int i2 = 0; i2 < i; i2++) {
                byte b3 = imageLineByte.scanline[i2] & UByte.MAX_VALUE;
                iArr[i2] = ((b3 < length ? pngChunkTRNS.getPalletteAlpha()[b3] : 255) << 24) | pngChunkPLTE.getEntry(b3);
            }
        } else {
            int i3 = -1;
            if (imageLineByte.imgInfo.greyscale) {
                if (pngChunkTRNS != null) {
                    i3 = pngChunkTRNS.getGray();
                }
                int i4 = 0;
                int i5 = 0;
                while (i4 < i) {
                    int i6 = i5 + 1;
                    byte b4 = imageLineByte.scanline[i5] & UByte.MAX_VALUE;
                    if (z) {
                        b2 = imageLineByte.scanline[i6] & UByte.MAX_VALUE;
                        i6++;
                    } else {
                        b2 = b4 != i3 ? (byte) 255 : 0;
                    }
                    iArr[i4] = (b4 << 16) | (b2 << 24) | b4 | (b4 << 8);
                    i4++;
                    i5 = i6;
                }
            } else {
                if (pngChunkTRNS != null) {
                    i3 = pngChunkTRNS.getRGB888();
                }
                int i7 = 0;
                int i8 = 0;
                while (i7 < i) {
                    int i9 = i8 + 1;
                    int i10 = i9 + 1;
                    int i11 = i10 + 1;
                    byte b5 = ((imageLineByte.scanline[i8] & UByte.MAX_VALUE) << 16) | ((imageLineByte.scanline[i9] & UByte.MAX_VALUE) << 8) | (imageLineByte.scanline[i10] & UByte.MAX_VALUE);
                    if (z) {
                        b = imageLineByte.scanline[i11] & UByte.MAX_VALUE;
                        i11++;
                    } else {
                        b = b5 != i3 ? (byte) 255 : 0;
                    }
                    iArr[i7] = b5 | (b << 24);
                    i7++;
                    i8 = i11;
                }
            }
        }
        return iArr;
    }

    static byte[] lineToRGBA8888(ImageLineByte imageLineByte, PngChunkPLTE pngChunkPLTE, PngChunkTRNS pngChunkTRNS, byte[] bArr) {
        byte b;
        boolean z = imageLineByte.imgInfo.alpha;
        int i = imageLineByte.imgInfo.cols;
        int i2 = i * 4;
        if (bArr == null || bArr.length < i2) {
            bArr = new byte[i2];
        }
        if (imageLineByte.imgInfo.indexed) {
            int length = pngChunkTRNS != null ? pngChunkTRNS.getPalletteAlpha().length : 0;
            int i3 = 0;
            for (int i4 = 0; i4 < i; i4++) {
                int i5 = 255;
                byte b2 = imageLineByte.scanline[i4] & UByte.MAX_VALUE;
                int entry = pngChunkPLTE.getEntry(b2);
                int i6 = i3 + 1;
                bArr[i3] = (byte) ((entry >> 16) & 255);
                int i7 = i6 + 1;
                bArr[i6] = (byte) ((entry >> 8) & 255);
                int i8 = i7 + 1;
                bArr[i7] = (byte) (entry & 255);
                i3 = i8 + 1;
                if (b2 < length) {
                    i5 = pngChunkTRNS.getPalletteAlpha()[b2];
                }
                bArr[i8] = (byte) i5;
            }
        } else if (imageLineByte.imgInfo.greyscale) {
            int gray = pngChunkTRNS != null ? pngChunkTRNS.getGray() : -1;
            int i9 = 0;
            int i10 = 0;
            while (i9 < i2) {
                int i11 = i10 + 1;
                byte b3 = imageLineByte.scanline[i10];
                int i12 = i9 + 1;
                bArr[i9] = b3;
                int i13 = i12 + 1;
                bArr[i12] = b3;
                int i14 = i13 + 1;
                bArr[i13] = b3;
                i9 = i14 + 1;
                if (z) {
                    b = imageLineByte.scanline[i11];
                    i11++;
                } else {
                    b = (b3 & UByte.MAX_VALUE) == gray ? (byte) 0 : -1;
                }
                bArr[i14] = b;
                i10 = i11;
            }
        } else if (z) {
            System.arraycopy(imageLineByte.scanline, 0, bArr, 0, i2);
        } else {
            int i15 = 0;
            int i16 = 0;
            while (i15 < i2) {
                int i17 = i15 + 1;
                int i18 = i16 + 1;
                bArr[i15] = imageLineByte.scanline[i16];
                int i19 = i17 + 1;
                int i20 = i18 + 1;
                bArr[i17] = imageLineByte.scanline[i18];
                int i21 = i19 + 1;
                int i22 = i20 + 1;
                bArr[i19] = imageLineByte.scanline[i20];
                i15 = i21 + 1;
                bArr[i21] = -1;
                if (pngChunkTRNS != null && bArr[i15 - 3] == ((byte) pngChunkTRNS.getRGB()[0]) && bArr[i15 - 2] == ((byte) pngChunkTRNS.getRGB()[1])) {
                    int i23 = i15 - 1;
                    if (bArr[i23] == ((byte) pngChunkTRNS.getRGB()[2])) {
                        bArr[i23] = 0;
                    }
                }
                i16 = i22;
            }
        }
        return bArr;
    }

    static byte[] lineToRGB888(ImageLineByte imageLineByte, PngChunkPLTE pngChunkPLTE, byte[] bArr) {
        boolean z = imageLineByte.imgInfo.alpha;
        int i = imageLineByte.imgInfo.cols;
        int i2 = i * 3;
        if (bArr == null || bArr.length < i2) {
            bArr = new byte[i2];
        }
        int[] iArr = new int[3];
        int i3 = 0;
        if (imageLineByte.imgInfo.indexed) {
            int i4 = 0;
            int i5 = 0;
            while (i4 < i) {
                pngChunkPLTE.getEntryRgb(imageLineByte.scanline[i4] & UByte.MAX_VALUE, iArr);
                int i6 = i5 + 1;
                bArr[i5] = (byte) iArr[0];
                int i7 = i6 + 1;
                bArr[i6] = (byte) iArr[1];
                bArr[i7] = (byte) iArr[2];
                i4++;
                i5 = i7 + 1;
            }
        } else if (imageLineByte.imgInfo.greyscale) {
            int i8 = 0;
            while (i3 < i2) {
                int i9 = i8 + 1;
                byte b = imageLineByte.scanline[i8];
                int i10 = i3 + 1;
                bArr[i3] = b;
                int i11 = i10 + 1;
                bArr[i10] = b;
                i3 = i11 + 1;
                bArr[i11] = b;
                i8 = z ? i9 + 1 : i9;
            }
        } else if (!z) {
            System.arraycopy(imageLineByte.scanline, 0, bArr, 0, i2);
        } else {
            int i12 = 0;
            while (i3 < i2) {
                int i13 = i3 + 1;
                int i14 = i12 + 1;
                bArr[i3] = imageLineByte.scanline[i12];
                int i15 = i13 + 1;
                int i16 = i14 + 1;
                bArr[i13] = imageLineByte.scanline[i14];
                i3 = i15 + 1;
                bArr[i15] = imageLineByte.scanline[i16];
                i12 = i16 + 1 + 1;
            }
        }
        return bArr;
    }

    public static int[] palette2rgba(ImageLineInt imageLineInt, PngChunkPLTE pngChunkPLTE, PngChunkTRNS pngChunkTRNS, int[] iArr) {
        return palette2rgb(imageLineInt, pngChunkPLTE, pngChunkTRNS, iArr, true);
    }

    public static int[] palette2rgb(ImageLineInt imageLineInt, PngChunkPLTE pngChunkPLTE, int[] iArr) {
        return palette2rgb(imageLineInt, pngChunkPLTE, (PngChunkTRNS) null, iArr, false);
    }

    private static int[] palette2rgb(IImageLine iImageLine, PngChunkPLTE pngChunkPLTE, PngChunkTRNS pngChunkTRNS, int[] iArr, boolean z) {
        boolean z2 = true;
        boolean z3 = pngChunkTRNS != null;
        int i = z3 ? 4 : 3;
        ImageLineInt imageLineInt = iImageLine instanceof ImageLineInt ? iImageLine : null;
        ImageLineInt imageLineInt2 = imageLineInt;
        if (!(iImageLine instanceof ImageLineByte)) {
            iImageLine = null;
        }
        ImageLineByte imageLineByte = (ImageLineByte) iImageLine;
        ImageLineByte imageLineByte2 = imageLineByte;
        if (imageLineByte == null) {
            z2 = false;
        }
        int i2 = (imageLineInt != null ? imageLineInt.imgInfo : imageLineByte.imgInfo).cols;
        int i3 = i2 * i;
        if (iArr == null || iArr.length < i3) {
            iArr = new int[i3];
        }
        int length = pngChunkTRNS != null ? pngChunkTRNS.getPalletteAlpha().length : 0;
        for (int i4 = 0; i4 < i2; i4++) {
            int i5 = 255;
            int i6 = z2 ? imageLineByte.scanline[i4] & UByte.MAX_VALUE : imageLineInt.scanline[i4];
            int i7 = i4 * i;
            pngChunkPLTE.getEntryRgb(i6, iArr, i7);
            if (z3) {
                if (i6 < length) {
                    i5 = pngChunkTRNS.getPalletteAlpha()[i6];
                }
                iArr[i7 + 3] = i5;
            }
        }
        return iArr;
    }

    public static String infoFirstLastPixels(ImageLineInt imageLineInt) {
        if (imageLineInt.imgInfo.channels == 1) {
            return String.format("first=(%d) last=(%d)", new Object[]{Integer.valueOf(imageLineInt.scanline[0]), Integer.valueOf(imageLineInt.scanline[imageLineInt.scanline.length - 1])});
        }
        return String.format("first=(%d %d %d) last=(%d %d %d)", new Object[]{Integer.valueOf(imageLineInt.scanline[0]), Integer.valueOf(imageLineInt.scanline[1]), Integer.valueOf(imageLineInt.scanline[2]), Integer.valueOf(imageLineInt.scanline[imageLineInt.scanline.length - imageLineInt.imgInfo.channels]), Integer.valueOf(imageLineInt.scanline[(imageLineInt.scanline.length - imageLineInt.imgInfo.channels) + 1]), Integer.valueOf(imageLineInt.scanline[(imageLineInt.scanline.length - imageLineInt.imgInfo.channels) + 2])});
    }

    public static int getPixelRGB8(IImageLine iImageLine, int i) {
        int i2;
        int i3;
        if (iImageLine instanceof ImageLineInt) {
            ImageLineInt imageLineInt = (ImageLineInt) iImageLine;
            int i4 = i * imageLineInt.imgInfo.channels;
            int[] scanline = imageLineInt.getScanline();
            i2 = (scanline[i4] << 16) | (scanline[i4 + 1] << 8);
            i3 = scanline[i4 + 2];
        } else if (iImageLine instanceof ImageLineByte) {
            ImageLineByte imageLineByte = (ImageLineByte) iImageLine;
            int i5 = i * imageLineByte.imgInfo.channels;
            byte[] scanline2 = imageLineByte.getScanline();
            i2 = ((scanline2[i5] & UByte.MAX_VALUE) << 16) | ((scanline2[i5 + 1] & UByte.MAX_VALUE) << 8);
            i3 = scanline2[i5 + 2] & UByte.MAX_VALUE;
        } else {
            throw new PngjException("Not supported " + iImageLine.getClass());
        }
        return i3 | i2;
    }

    public static int getPixelARGB8(IImageLine iImageLine, int i) {
        int i2;
        int i3;
        if (iImageLine instanceof ImageLineInt) {
            ImageLineInt imageLineInt = (ImageLineInt) iImageLine;
            int i4 = i * imageLineInt.imgInfo.channels;
            int[] scanline = imageLineInt.getScanline();
            i2 = (scanline[i4 + 3] << 24) | (scanline[i4] << 16) | (scanline[i4 + 1] << 8);
            i3 = scanline[i4 + 2];
        } else if (iImageLine instanceof ImageLineByte) {
            ImageLineByte imageLineByte = (ImageLineByte) iImageLine;
            int i5 = i * imageLineByte.imgInfo.channels;
            byte[] scanline2 = imageLineByte.getScanline();
            i2 = ((scanline2[i5 + 3] & UByte.MAX_VALUE) << 24) | ((scanline2[i5] & UByte.MAX_VALUE) << 16) | ((scanline2[i5 + 1] & UByte.MAX_VALUE) << 8);
            i3 = scanline2[i5 + 2] & UByte.MAX_VALUE;
        } else {
            throw new PngjException("Not supported " + iImageLine.getClass());
        }
        return i3 | i2;
    }

    public static void setPixelsRGB8(ImageLineInt imageLineInt, int[] iArr) {
        int i = 0;
        int i2 = 0;
        while (i < imageLineInt.imgInfo.cols) {
            int i3 = i2 + 1;
            imageLineInt.scanline[i2] = (iArr[i] >> 16) & 255;
            int i4 = i3 + 1;
            imageLineInt.scanline[i3] = (iArr[i] >> 8) & 255;
            imageLineInt.scanline[i4] = iArr[i] & 255;
            i++;
            i2 = i4 + 1;
        }
    }

    public static void setPixelRGB8(ImageLineInt imageLineInt, int i, int i2, int i3, int i4) {
        int i5 = i * imageLineInt.imgInfo.channels;
        int i6 = i5 + 1;
        imageLineInt.scanline[i5] = i2;
        imageLineInt.scanline[i6] = i3;
        imageLineInt.scanline[i6 + 1] = i4;
    }

    public static void setPixelRGB8(ImageLineInt imageLineInt, int i, int i2) {
        setPixelRGB8(imageLineInt, i, (i2 >> 16) & 255, (i2 >> 8) & 255, i2 & 255);
    }

    public static void setPixelsRGBA8(ImageLineInt imageLineInt, int[] iArr) {
        int i = 0;
        int i2 = 0;
        while (i < imageLineInt.imgInfo.cols) {
            int i3 = i2 + 1;
            imageLineInt.scanline[i2] = (iArr[i] >> 16) & 255;
            int i4 = i3 + 1;
            imageLineInt.scanline[i3] = (iArr[i] >> 8) & 255;
            int i5 = i4 + 1;
            imageLineInt.scanline[i4] = iArr[i] & 255;
            imageLineInt.scanline[i5] = (iArr[i] >> 24) & 255;
            i++;
            i2 = i5 + 1;
        }
    }

    public static void setPixelRGBA8(ImageLineInt imageLineInt, int i, int i2, int i3, int i4, int i5) {
        int i6 = i * imageLineInt.imgInfo.channels;
        int i7 = i6 + 1;
        imageLineInt.scanline[i6] = i2;
        int i8 = i7 + 1;
        imageLineInt.scanline[i7] = i3;
        imageLineInt.scanline[i8] = i4;
        imageLineInt.scanline[i8 + 1] = i5;
    }

    public static void setPixelRGBA8(ImageLineInt imageLineInt, int i, int i2) {
        setPixelRGBA8(imageLineInt, i, (i2 >> 16) & 255, (i2 >> 8) & 255, i2 & 255, (i2 >> 24) & 255);
    }

    public static void setValD(ImageLineInt imageLineInt, int i, double d) {
        imageLineInt.scanline[i] = double2int(imageLineInt, d);
    }

    public static double int2double(ImageLineInt imageLineInt, int i) {
        double d;
        double d2;
        if (imageLineInt.imgInfo.bitDepth == 16) {
            d = (double) i;
            d2 = 65535.0d;
        } else {
            d = (double) i;
            d2 = 255.0d;
        }
        return d / d2;
    }

    public static double int2doubleClamped(ImageLineInt imageLineInt, int i) {
        double d;
        double d2;
        if (imageLineInt.imgInfo.bitDepth == 16) {
            d = (double) i;
            d2 = 65535.0d;
        } else {
            d = (double) i;
            d2 = 255.0d;
        }
        double d3 = d / d2;
        if (d3 <= 0.0d) {
            return 0.0d;
        }
        if (d3 >= 1.0d) {
            return 1.0d;
        }
        return d3;
    }

    public static int double2int(ImageLineInt imageLineInt, double d) {
        if (d <= 0.0d) {
            d = 0.0d;
        } else if (d >= 1.0d) {
            d = 1.0d;
        }
        return (int) ((d * (imageLineInt.imgInfo.bitDepth == 16 ? 65535.0d : 255.0d)) + 0.5d);
    }

    public static int double2intClamped(ImageLineInt imageLineInt, double d) {
        if (d <= 0.0d) {
            d = 0.0d;
        } else if (d >= 1.0d) {
            d = 1.0d;
        }
        return (int) ((d * (imageLineInt.imgInfo.bitDepth == 16 ? 65535.0d : 255.0d)) + 0.5d);
    }
}
