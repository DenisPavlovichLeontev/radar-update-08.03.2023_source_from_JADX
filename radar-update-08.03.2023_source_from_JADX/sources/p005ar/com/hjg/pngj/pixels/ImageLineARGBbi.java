package p005ar.com.hjg.pngj.pixels;

import java.awt.image.BufferedImage;
import p005ar.com.hjg.pngj.IImageLine;
import p005ar.com.hjg.pngj.IImageLineFactory;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.ImageLineByte;

/* renamed from: ar.com.hjg.pngj.pixels.ImageLineARGBbi */
public class ImageLineARGBbi implements IImageLine {
    private boolean bgrOrder;
    private byte[] bytes;
    private boolean hasAlpha;
    private final BufferedImage image;
    public final ImageInfo imgInfo;
    private int rowLength;
    private int rowNumber = -1;

    public ImageLineARGBbi(ImageInfo imageInfo, BufferedImage bufferedImage, byte[] bArr) {
        this.imgInfo = imageInfo;
        this.image = bufferedImage;
        this.bytes = bArr;
        boolean hasAlpha2 = bufferedImage.getColorModel().hasAlpha();
        this.hasAlpha = hasAlpha2;
        if (hasAlpha2) {
            this.rowLength = bufferedImage.getWidth() * 4;
        } else {
            this.rowLength = bufferedImage.getWidth() * 3;
        }
        this.bgrOrder = bufferedImage.getSampleModel().getBandOffsets()[0] != 0;
    }

    public static IImageLineFactory<ImageLineByte> getFactory(ImageInfo imageInfo) {
        return new IImageLineFactory<ImageLineByte>() {
            public ImageLineByte createImageLine(ImageInfo imageInfo) {
                return new ImageLineByte(imageInfo);
            }
        };
    }

    public void readFromPngRaw(byte[] bArr, int i, int i2, int i3) {
        throw new RuntimeException("not implemented");
    }

    public void writeToPngRaw(byte[] bArr) {
        if (this.imgInfo.bytesPerRow == this.rowLength) {
            int i = this.rowNumber;
            if (i < 0 || i >= this.imgInfo.rows) {
                throw new RuntimeException("???");
            }
            int i2 = this.rowLength * this.rowNumber;
            int i3 = 1;
            if (this.hasAlpha) {
                if (this.bgrOrder) {
                    while (i3 <= this.rowLength) {
                        byte[] bArr2 = this.bytes;
                        int i4 = i2 + 1;
                        byte b = bArr2[i2];
                        int i5 = i4 + 1;
                        byte b2 = bArr2[i4];
                        int i6 = i5 + 1;
                        byte b3 = bArr2[i5];
                        int i7 = i6 + 1;
                        byte b4 = bArr2[i6];
                        int i8 = i3 + 1;
                        bArr[i3] = b4;
                        int i9 = i8 + 1;
                        bArr[i8] = b3;
                        int i10 = i9 + 1;
                        bArr[i9] = b2;
                        bArr[i10] = b;
                        i3 = i10 + 1;
                        i2 = i7;
                    }
                    return;
                }
                while (i3 <= this.rowLength) {
                    int i11 = i3 + 1;
                    byte[] bArr3 = this.bytes;
                    int i12 = i2 + 1;
                    bArr[i3] = bArr3[i2];
                    int i13 = i11 + 1;
                    int i14 = i12 + 1;
                    bArr[i11] = bArr3[i12];
                    int i15 = i13 + 1;
                    int i16 = i14 + 1;
                    bArr[i13] = bArr3[i14];
                    i3 = i15 + 1;
                    i2 = i16 + 1;
                    bArr[i15] = bArr3[i16];
                }
            } else if (this.bgrOrder) {
                while (i3 <= this.rowLength) {
                    byte[] bArr4 = this.bytes;
                    int i17 = i2 + 1;
                    byte b5 = bArr4[i2];
                    int i18 = i17 + 1;
                    byte b6 = bArr4[i17];
                    int i19 = i18 + 1;
                    byte b7 = bArr4[i18];
                    int i20 = i3 + 1;
                    bArr[i3] = b7;
                    int i21 = i20 + 1;
                    bArr[i20] = b6;
                    i3 = i21 + 1;
                    bArr[i21] = b5;
                    i2 = i19;
                }
            } else {
                while (i3 <= this.rowLength) {
                    int i22 = i3 + 1;
                    byte[] bArr5 = this.bytes;
                    int i23 = i2 + 1;
                    bArr[i3] = bArr5[i2];
                    int i24 = i22 + 1;
                    int i25 = i23 + 1;
                    bArr[i22] = bArr5[i23];
                    int i26 = i25 + 1;
                    bArr[i24] = bArr5[i25];
                    i3 = i24 + 1;
                    i2 = i26;
                }
            }
        } else {
            throw new RuntimeException("??");
        }
    }

    public void endReadFromPngRaw() {
        throw new RuntimeException("not implemented");
    }

    public int getRowNumber() {
        return this.rowNumber;
    }

    public void setRowNumber(int i) {
        this.rowNumber = i;
    }
}
