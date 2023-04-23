package p005ar.com.hjg.pngj;

import java.util.ArrayList;
import java.util.List;
import p005ar.com.hjg.pngj.IImageLine;

/* renamed from: ar.com.hjg.pngj.ImageLineSetDefault */
public abstract class ImageLineSetDefault<T extends IImageLine> implements IImageLineSet<T> {
    protected int currentRow = -1;
    protected T imageLine;
    protected List<T> imageLines;
    protected final ImageInfo imgInfo;
    private final int nlines;
    private final int offset;
    private final boolean singleCursor;
    private final int step;

    /* access modifiers changed from: protected */
    public abstract T createImageLine();

    public ImageLineSetDefault(ImageInfo imageInfo, boolean z, int i, int i2, int i3) {
        this.imgInfo = imageInfo;
        this.singleCursor = z;
        if (z) {
            this.nlines = 1;
            this.offset = 0;
            this.step = 1;
        } else {
            this.nlines = imageInfo.rows;
            this.offset = 0;
            this.step = 1;
        }
        createImageLines();
    }

    private void createImageLines() {
        if (this.singleCursor) {
            this.imageLine = createImageLine();
            return;
        }
        this.imageLines = new ArrayList();
        for (int i = 0; i < this.nlines; i++) {
            this.imageLines.add(createImageLine());
        }
    }

    public T getImageLine(int i) {
        this.currentRow = i;
        if (this.singleCursor) {
            return this.imageLine;
        }
        return (IImageLine) this.imageLines.get(imageRowToMatrixRowStrict(i));
    }

    public boolean hasImageLine(int i) {
        if (this.singleCursor) {
            if (this.currentRow == i) {
                return true;
            }
        } else if (imageRowToMatrixRowStrict(i) >= 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return this.nlines;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0013  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int imageRowToMatrixRowStrict(int r4) {
        /*
            r3 = this;
            int r0 = r3.offset
            int r4 = r4 - r0
            r0 = -1
            if (r4 < 0) goto L_0x000e
            int r1 = r3.step
            int r2 = r4 % r1
            if (r2 != 0) goto L_0x000e
            int r4 = r4 / r1
            goto L_0x000f
        L_0x000e:
            r4 = r0
        L_0x000f:
            int r1 = r3.nlines
            if (r4 >= r1) goto L_0x0014
            r0 = r4
        L_0x0014:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.ImageLineSetDefault.imageRowToMatrixRowStrict(int):int");
    }

    public int matrixRowToImageRow(int i) {
        return (i * this.step) + this.offset;
    }

    public int imageRowToMatrixRow(int i) {
        int i2 = (i - this.offset) / this.step;
        if (i2 < 0) {
            return 0;
        }
        int i3 = this.nlines;
        return i2 < i3 ? i2 : i3 - 1;
    }

    public static IImageLineSetFactory<ImageLineInt> getFactoryInt() {
        return new IImageLineSetFactory<ImageLineInt>() {
            public IImageLineSet<ImageLineInt> create(ImageInfo imageInfo, boolean z, int i, int i2, int i3) {
                return new ImageLineSetDefault<ImageLineInt>(imageInfo, z, i, i2, i3) {
                    /* access modifiers changed from: protected */
                    public ImageLineInt createImageLine() {
                        return new ImageLineInt(this.imgInfo);
                    }
                };
            }
        };
    }

    public static IImageLineSetFactory<ImageLineByte> getFactoryByte() {
        return new IImageLineSetFactory<ImageLineByte>() {
            public IImageLineSet<ImageLineByte> create(ImageInfo imageInfo, boolean z, int i, int i2, int i3) {
                return new ImageLineSetDefault<ImageLineByte>(imageInfo, z, i, i2, i3) {
                    /* access modifiers changed from: protected */
                    public ImageLineByte createImageLine() {
                        return new ImageLineByte(this.imgInfo);
                    }
                };
            }
        };
    }
}
