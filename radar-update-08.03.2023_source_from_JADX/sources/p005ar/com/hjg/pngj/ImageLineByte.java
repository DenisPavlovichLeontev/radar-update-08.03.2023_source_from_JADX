package p005ar.com.hjg.pngj;

import kotlin.UByte;

/* renamed from: ar.com.hjg.pngj.ImageLineByte */
public class ImageLineByte implements IImageLine, IImageLineArray {
    protected FilterType filterType;
    public final ImageInfo imgInfo;
    final byte[] scanline;
    final byte[] scanline2;
    final int size;

    public void endReadFromPngRaw() {
    }

    public ImageLineByte(ImageInfo imageInfo) {
        this(imageInfo, (byte[]) null);
    }

    public ImageLineByte(ImageInfo imageInfo, byte[] bArr) {
        this.imgInfo = imageInfo;
        this.filterType = FilterType.FILTER_UNKNOWN;
        int i = imageInfo.samplesPerRow;
        this.size = i;
        this.scanline = (bArr == null || bArr.length < i) ? new byte[i] : bArr;
        this.scanline2 = imageInfo.bitDepth == 16 ? new byte[i] : null;
    }

    public static IImageLineFactory<ImageLineByte> getFactory(ImageInfo imageInfo) {
        return new IImageLineFactory<ImageLineByte>() {
            public ImageLineByte createImageLine(ImageInfo imageInfo) {
                return new ImageLineByte(imageInfo);
            }
        };
    }

    public FilterType getFilterUsed() {
        return this.filterType;
    }

    public byte[] getScanlineByte() {
        return this.scanline;
    }

    public byte[] getScanlineByte2() {
        return this.scanline2;
    }

    public String toString() {
        return " cols=" + this.imgInfo.cols + " bpc=" + this.imgInfo.bitDepth + " size=" + this.scanline.length;
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readFromPngRaw(byte[] r11, int r12, int r13, int r14) {
        /*
            r10 = this;
            r0 = 0
            byte r1 = r11[r0]
            ar.com.hjg.pngj.FilterType r1 = p005ar.com.hjg.pngj.FilterType.getByVal(r1)
            r10.filterType = r1
            int r1 = r12 + -1
            int r2 = r14 + -1
            ar.com.hjg.pngj.ImageInfo r3 = r10.imgInfo
            int r3 = r3.channels
            int r2 = r2 * r3
            ar.com.hjg.pngj.ImageInfo r3 = r10.imgInfo
            int r3 = r3.bitDepth
            r4 = 8
            r5 = 1
            if (r3 != r4) goto L_0x0040
            if (r14 != r5) goto L_0x0024
            byte[] r12 = r10.scanline
            java.lang.System.arraycopy(r11, r5, r12, r0, r1)
            goto L_0x00be
        L_0x0024:
            ar.com.hjg.pngj.ImageInfo r12 = r10.imgInfo
            int r12 = r12.channels
            int r13 = r13 * r12
            r14 = r0
            r12 = r5
        L_0x002b:
            if (r12 > r1) goto L_0x00be
            byte[] r3 = r10.scanline
            byte r4 = r11[r12]
            r3[r13] = r4
            int r14 = r14 + r5
            ar.com.hjg.pngj.ImageInfo r3 = r10.imgInfo
            int r3 = r3.channels
            if (r14 != r3) goto L_0x003c
            int r13 = r13 + r2
            r14 = r0
        L_0x003c:
            int r12 = r12 + 1
            int r13 = r13 + r5
            goto L_0x002b
        L_0x0040:
            ar.com.hjg.pngj.ImageInfo r3 = r10.imgInfo
            int r3 = r3.bitDepth
            r4 = 16
            if (r3 != r4) goto L_0x008c
            if (r14 != r5) goto L_0x0063
        L_0x004a:
            ar.com.hjg.pngj.ImageInfo r12 = r10.imgInfo
            int r12 = r12.samplesPerRow
            if (r0 >= r12) goto L_0x00be
            byte[] r12 = r10.scanline
            int r13 = r5 + 1
            byte r14 = r11[r5]
            r12[r0] = r14
            byte[] r12 = r10.scanline2
            int r5 = r13 + 1
            byte r13 = r11[r13]
            r12[r0] = r13
            int r0 = r0 + 1
            goto L_0x004a
        L_0x0063:
            if (r13 == 0) goto L_0x006b
            ar.com.hjg.pngj.ImageInfo r12 = r10.imgInfo
            int r12 = r12.channels
            int r13 = r13 * r12
            goto L_0x006c
        L_0x006b:
            r13 = r0
        L_0x006c:
            r14 = r0
            r12 = r5
        L_0x006e:
            if (r12 > r1) goto L_0x00be
            byte[] r3 = r10.scanline
            int r4 = r12 + 1
            byte r12 = r11[r12]
            r3[r13] = r12
            byte[] r12 = r10.scanline2
            int r3 = r4 + 1
            byte r4 = r11[r4]
            r12[r13] = r4
            int r14 = r14 + r5
            ar.com.hjg.pngj.ImageInfo r12 = r10.imgInfo
            int r12 = r12.channels
            if (r14 != r12) goto L_0x0089
            int r13 = r13 + r2
            r14 = r0
        L_0x0089:
            int r13 = r13 + r5
            r12 = r3
            goto L_0x006e
        L_0x008c:
            ar.com.hjg.pngj.ImageInfo r14 = r10.imgInfo
            int r14 = r14.bitDepth
            int r1 = p005ar.com.hjg.pngj.ImageLineHelper.getMaskForPackedFormats(r14)
            ar.com.hjg.pngj.ImageInfo r3 = r10.imgInfo
            int r3 = r3.channels
            int r13 = r13 * r3
            r4 = r0
            r3 = r5
        L_0x009b:
            if (r3 >= r12) goto L_0x00be
            int r6 = 8 - r14
            r7 = r1
        L_0x00a0:
            byte[] r8 = r10.scanline
            byte r9 = r11[r3]
            r9 = r9 & r7
            int r9 = r9 >> r6
            byte r9 = (byte) r9
            r8[r13] = r9
            int r7 = r7 >> r14
            int r6 = r6 - r14
            int r13 = r13 + r5
            int r4 = r4 + r5
            ar.com.hjg.pngj.ImageInfo r8 = r10.imgInfo
            int r8 = r8.channels
            if (r4 != r8) goto L_0x00b5
            int r13 = r13 + r2
            r4 = r0
        L_0x00b5:
            if (r7 == 0) goto L_0x00bb
            int r8 = r10.size
            if (r13 < r8) goto L_0x00a0
        L_0x00bb:
            int r3 = r3 + 1
            goto L_0x009b
        L_0x00be:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.ImageLineByte.readFromPngRaw(byte[], int, int, int):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v0, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v6, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeToPngRaw(byte[] r10) {
        /*
            r9 = this;
            ar.com.hjg.pngj.FilterType r0 = r9.filterType
            int r0 = r0.val
            byte r0 = (byte) r0
            r1 = 0
            r10[r1] = r0
            ar.com.hjg.pngj.ImageInfo r0 = r9.imgInfo
            int r0 = r0.bitDepth
            r2 = 8
            r3 = 1
            if (r0 != r2) goto L_0x0026
            byte[] r0 = r9.scanline
            int r2 = r9.size
            java.lang.System.arraycopy(r0, r1, r10, r3, r2)
        L_0x0018:
            int r0 = r9.size
            if (r1 >= r0) goto L_0x006a
            int r0 = r1 + 1
            byte[] r2 = r9.scanline
            byte r1 = r2[r1]
            r10[r0] = r1
            r1 = r0
            goto L_0x0018
        L_0x0026:
            ar.com.hjg.pngj.ImageInfo r0 = r9.imgInfo
            int r0 = r0.bitDepth
            r2 = 16
            if (r0 != r2) goto L_0x0045
        L_0x002e:
            int r0 = r9.size
            if (r1 >= r0) goto L_0x006a
            int r0 = r3 + 1
            byte[] r2 = r9.scanline
            byte r2 = r2[r1]
            r10[r3] = r2
            int r3 = r0 + 1
            byte[] r2 = r9.scanline2
            byte r2 = r2[r1]
            r10[r0] = r2
            int r1 = r1 + 1
            goto L_0x002e
        L_0x0045:
            ar.com.hjg.pngj.ImageInfo r0 = r9.imgInfo
            int r0 = r0.bitDepth
            int r2 = 8 - r0
            r4 = r1
            r5 = r4
            r6 = r2
        L_0x004e:
            int r7 = r9.size
            if (r4 >= r7) goto L_0x006a
            byte[] r8 = r9.scanline
            byte r8 = r8[r4]
            int r8 = r8 << r6
            r5 = r5 | r8
            int r6 = r6 - r0
            if (r6 < 0) goto L_0x005f
            int r7 = r7 + -1
            if (r4 != r7) goto L_0x0067
        L_0x005f:
            int r6 = r3 + 1
            byte r5 = (byte) r5
            r10[r3] = r5
            r5 = r1
            r3 = r6
            r6 = r2
        L_0x0067:
            int r4 = r4 + 1
            goto L_0x004e
        L_0x006a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.ImageLineByte.writeToPngRaw(byte[]):void");
    }

    public int getSize() {
        return this.size;
    }

    public int getElem(int i) {
        byte[] bArr = this.scanline2;
        if (bArr == null) {
            return this.scanline[i] & UByte.MAX_VALUE;
        }
        return (bArr[i] & UByte.MAX_VALUE) | ((this.scanline[i] & UByte.MAX_VALUE) << 8);
    }

    public byte[] getScanline() {
        return this.scanline;
    }

    public ImageInfo getImageInfo() {
        return this.imgInfo;
    }

    public FilterType getFilterType() {
        return this.filterType;
    }
}
