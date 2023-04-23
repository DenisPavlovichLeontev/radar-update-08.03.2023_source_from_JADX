package p005ar.com.hjg.pngj;

/* renamed from: ar.com.hjg.pngj.ImageLineInt */
public class ImageLineInt implements IImageLine, IImageLineArray {
    protected FilterType filterType;
    public final ImageInfo imgInfo;
    protected final int[] scanline;
    protected final int size;

    public void endReadFromPngRaw() {
    }

    public ImageLineInt(ImageInfo imageInfo) {
        this(imageInfo, (int[]) null);
    }

    public ImageLineInt(ImageInfo imageInfo, int[] iArr) {
        this.filterType = FilterType.FILTER_UNKNOWN;
        this.imgInfo = imageInfo;
        this.filterType = FilterType.FILTER_UNKNOWN;
        int i = imageInfo.samplesPerRow;
        this.size = i;
        this.scanline = (iArr == null || iArr.length < i) ? new int[i] : iArr;
    }

    public FilterType getFilterType() {
        return this.filterType;
    }

    /* access modifiers changed from: protected */
    public void setFilterType(FilterType filterType2) {
        this.filterType = filterType2;
    }

    public String toString() {
        return " cols=" + this.imgInfo.cols + " bpc=" + this.imgInfo.bitDepth + " size=" + this.scanline.length;
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x00bf  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readFromPngRaw(byte[] r11, int r12, int r13, int r14) {
        /*
            r10 = this;
            r0 = 0
            byte r1 = r11[r0]
            ar.com.hjg.pngj.FilterType r1 = p005ar.com.hjg.pngj.FilterType.getByVal(r1)
            r10.setFilterType(r1)
            int r1 = r12 + -1
            int r2 = r14 + -1
            ar.com.hjg.pngj.ImageInfo r3 = r10.imgInfo
            int r3 = r3.channels
            int r2 = r2 * r3
            ar.com.hjg.pngj.ImageInfo r3 = r10.imgInfo
            int r3 = r3.bitDepth
            r4 = 8
            r5 = 1
            if (r3 != r4) goto L_0x004c
            if (r14 != r5) goto L_0x002e
        L_0x001e:
            int r12 = r10.size
            if (r0 >= r12) goto L_0x00ca
            int[] r12 = r10.scanline
            int r13 = r0 + 1
            byte r14 = r11[r13]
            r14 = r14 & 255(0xff, float:3.57E-43)
            r12[r0] = r14
            r0 = r13
            goto L_0x001e
        L_0x002e:
            ar.com.hjg.pngj.ImageInfo r12 = r10.imgInfo
            int r12 = r12.channels
            int r13 = r13 * r12
            r14 = r0
            r12 = r5
        L_0x0035:
            if (r12 > r1) goto L_0x00ca
            int[] r3 = r10.scanline
            byte r4 = r11[r12]
            r4 = r4 & 255(0xff, float:3.57E-43)
            r3[r13] = r4
            int r14 = r14 + r5
            ar.com.hjg.pngj.ImageInfo r3 = r10.imgInfo
            int r3 = r3.channels
            if (r14 != r3) goto L_0x0048
            int r13 = r13 + r2
            r14 = r0
        L_0x0048:
            int r12 = r12 + 1
            int r13 = r13 + r5
            goto L_0x0035
        L_0x004c:
            ar.com.hjg.pngj.ImageInfo r3 = r10.imgInfo
            int r3 = r3.bitDepth
            r6 = 16
            if (r3 != r6) goto L_0x0099
            if (r14 != r5) goto L_0x006f
        L_0x0056:
            int r12 = r10.size
            if (r0 >= r12) goto L_0x00ca
            int[] r12 = r10.scanline
            int r13 = r5 + 1
            byte r14 = r11[r5]
            r14 = r14 & 255(0xff, float:3.57E-43)
            int r14 = r14 << r4
            int r5 = r13 + 1
            byte r13 = r11[r13]
            r13 = r13 & 255(0xff, float:3.57E-43)
            r13 = r13 | r14
            r12[r0] = r13
            int r0 = r0 + 1
            goto L_0x0056
        L_0x006f:
            if (r13 == 0) goto L_0x0077
            ar.com.hjg.pngj.ImageInfo r12 = r10.imgInfo
            int r12 = r12.channels
            int r13 = r13 * r12
            goto L_0x0078
        L_0x0077:
            r13 = r0
        L_0x0078:
            r14 = r0
            r12 = r5
        L_0x007a:
            if (r12 > r1) goto L_0x00ca
            int[] r3 = r10.scanline
            int r6 = r12 + 1
            byte r12 = r11[r12]
            r12 = r12 & 255(0xff, float:3.57E-43)
            int r12 = r12 << r4
            byte r7 = r11[r6]
            r7 = r7 & 255(0xff, float:3.57E-43)
            r12 = r12 | r7
            r3[r13] = r12
            int r14 = r14 + r5
            ar.com.hjg.pngj.ImageInfo r12 = r10.imgInfo
            int r12 = r12.channels
            if (r14 != r12) goto L_0x0095
            int r13 = r13 + r2
            r14 = r0
        L_0x0095:
            int r12 = r6 + 1
            int r13 = r13 + r5
            goto L_0x007a
        L_0x0099:
            ar.com.hjg.pngj.ImageInfo r14 = r10.imgInfo
            int r14 = r14.bitDepth
            int r1 = p005ar.com.hjg.pngj.ImageLineHelper.getMaskForPackedFormats(r14)
            ar.com.hjg.pngj.ImageInfo r3 = r10.imgInfo
            int r3 = r3.channels
            int r13 = r13 * r3
            r4 = r0
            r3 = r5
        L_0x00a8:
            if (r3 >= r12) goto L_0x00ca
            int r6 = 8 - r14
            r7 = r1
        L_0x00ad:
            int[] r8 = r10.scanline
            byte r9 = r11[r3]
            r9 = r9 & r7
            int r9 = r9 >> r6
            r8[r13] = r9
            int r7 = r7 >> r14
            int r6 = r6 - r14
            int r13 = r13 + r5
            int r4 = r4 + r5
            ar.com.hjg.pngj.ImageInfo r8 = r10.imgInfo
            int r8 = r8.channels
            if (r4 != r8) goto L_0x00c1
            int r13 = r13 + r2
            r4 = r0
        L_0x00c1:
            if (r7 == 0) goto L_0x00c7
            int r8 = r10.size
            if (r13 < r8) goto L_0x00ad
        L_0x00c7:
            int r3 = r3 + 1
            goto L_0x00a8
        L_0x00ca:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.ImageLineInt.readFromPngRaw(byte[], int, int, int):void");
    }

    public void writeToPngRaw(byte[] bArr) {
        int i = 0;
        bArr[0] = (byte) this.filterType.val;
        if (this.imgInfo.bitDepth == 8) {
            while (i < this.size) {
                int i2 = i + 1;
                bArr[i2] = (byte) this.scanline[i];
                i = i2;
            }
            return;
        }
        int i3 = 1;
        if (this.imgInfo.bitDepth == 16) {
            while (i < this.size) {
                int i4 = i3 + 1;
                int i5 = this.scanline[i];
                bArr[i3] = (byte) (i5 >> 8);
                i3 = i4 + 1;
                bArr[i4] = (byte) (i5 & 255);
                i++;
            }
            return;
        }
        int i6 = this.imgInfo.bitDepth;
        int i7 = 8 - i6;
        int i8 = 0;
        int i9 = 0;
        int i10 = i7;
        while (true) {
            int i11 = this.size;
            if (i8 < i11) {
                i9 |= this.scanline[i8] << i10;
                i10 -= i6;
                if (i10 < 0 || i8 == i11 - 1) {
                    bArr[i3] = (byte) i9;
                    i9 = 0;
                    i3++;
                    i10 = i7;
                }
                i8++;
            } else {
                return;
            }
        }
    }

    public int getSize() {
        return this.size;
    }

    public int getElem(int i) {
        return this.scanline[i];
    }

    public int[] getScanline() {
        return this.scanline;
    }

    public ImageInfo getImageInfo() {
        return this.imgInfo;
    }

    public static IImageLineFactory<ImageLineInt> getFactory(ImageInfo imageInfo) {
        return new IImageLineFactory<ImageLineInt>() {
            public ImageLineInt createImageLine(ImageInfo imageInfo) {
                return new ImageLineInt(imageInfo);
            }
        };
    }
}
