package p005ar.com.hjg.pngj;

/* renamed from: ar.com.hjg.pngj.Deinterlacer */
public class Deinterlacer {
    private int cols;
    private int currRowReal = -1;
    private int currRowSeq;
    private int currRowSubimg = -1;

    /* renamed from: dX */
    int f112dX;
    int dXsamples;

    /* renamed from: dY */
    int f113dY;
    private boolean ended = false;
    final ImageInfo imi;

    /* renamed from: oX */
    int f114oX;
    int oXsamples;

    /* renamed from: oY */
    int f115oY;
    private int pass;
    private int rows;
    int totalRows = 0;

    public Deinterlacer(ImageInfo imageInfo) {
        this.imi = imageInfo;
        this.pass = 0;
        this.currRowSeq = 0;
        setPass(1);
        setRow(0);
    }

    private void setRow(int i) {
        this.currRowSubimg = i;
        int i2 = (i * this.f113dY) + this.f115oY;
        this.currRowReal = i2;
        if (i2 < 0 || i2 >= this.imi.rows) {
            throw new PngjExceptionInternal("bad row - this should not happen");
        }
    }

    /* access modifiers changed from: package-private */
    public boolean nextRow() {
        int i;
        this.currRowSeq++;
        int i2 = this.rows;
        if (i2 == 0 || (i = this.currRowSubimg) >= i2 - 1) {
            int i3 = this.pass;
            if (i3 == 7) {
                this.ended = true;
                return false;
            }
            setPass(i3 + 1);
            if (this.rows == 0) {
                this.currRowSeq--;
                return nextRow();
            }
            setRow(0);
        } else {
            setRow(i + 1);
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isEnded() {
        return this.ended;
    }

    /* access modifiers changed from: package-private */
    public void setPass(int i) {
        int i2;
        int i3;
        if (this.pass != i) {
            this.pass = i;
            byte[] paramsForPass = paramsForPass(i);
            this.f112dX = paramsForPass[0];
            this.f113dY = paramsForPass[1];
            this.f114oX = paramsForPass[2];
            this.f115oY = paramsForPass[3];
            if (this.imi.rows > this.f115oY) {
                int i4 = this.imi.rows;
                int i5 = this.f113dY;
                i2 = (((i4 + i5) - 1) - this.f115oY) / i5;
            } else {
                i2 = 0;
            }
            this.rows = i2;
            if (this.imi.cols > this.f114oX) {
                int i6 = this.imi.cols;
                int i7 = this.f112dX;
                i3 = (((i6 + i7) - 1) - this.f114oX) / i7;
            } else {
                i3 = 0;
            }
            this.cols = i3;
            if (i3 == 0) {
                this.rows = 0;
            }
            this.dXsamples = this.f112dX * this.imi.channels;
            this.oXsamples = this.f114oX * this.imi.channels;
        }
    }

    static byte[] paramsForPass(int i) {
        switch (i) {
            case 1:
                return new byte[]{8, 8, 0, 0};
            case 2:
                return new byte[]{8, 8, 4, 0};
            case 3:
                return new byte[]{4, 8, 0, 4};
            case 4:
                return new byte[]{4, 4, 2, 0};
            case 5:
                return new byte[]{2, 4, 0, 2};
            case 6:
                return new byte[]{2, 2, 1, 0};
            case 7:
                return new byte[]{1, 2, 0, 1};
            default:
                throw new PngjExceptionInternal("bad interlace pass" + i);
        }
    }

    /* access modifiers changed from: package-private */
    public int getCurrRowSubimg() {
        return this.currRowSubimg;
    }

    /* access modifiers changed from: package-private */
    public int getCurrRowReal() {
        return this.currRowReal;
    }

    /* access modifiers changed from: package-private */
    public int getPass() {
        return this.pass;
    }

    /* access modifiers changed from: package-private */
    public int getRows() {
        return this.rows;
    }

    /* access modifiers changed from: package-private */
    public int getCols() {
        return this.cols;
    }

    public int getPixelsToRead() {
        return getCols();
    }

    public int getBytesToRead() {
        return ((this.imi.bitspPixel * getPixelsToRead()) + 7) / 8;
    }

    public int getdY() {
        return this.f113dY;
    }

    public int getdX() {
        return this.f112dX;
    }

    public int getoY() {
        return this.f115oY;
    }

    public int getoX() {
        return this.f114oX;
    }

    public int getTotalRows() {
        int i;
        if (this.totalRows == 0) {
            for (int i2 = 1; i2 <= 7; i2++) {
                byte[] paramsForPass = paramsForPass(i2);
                int i3 = 0;
                if (this.imi.rows > paramsForPass[3]) {
                    int i4 = this.imi.rows;
                    byte b = paramsForPass[1];
                    i = (((i4 + b) - 1) - paramsForPass[3]) / b;
                } else {
                    i = 0;
                }
                if (this.imi.cols > paramsForPass[2]) {
                    int i5 = this.imi.cols;
                    byte b2 = paramsForPass[0];
                    i3 = (((i5 + b2) - 1) - paramsForPass[2]) / b2;
                }
                if (i > 0 && i3 > 0) {
                    this.totalRows += i;
                }
            }
        }
        return this.totalRows;
    }

    public long getTotalRawBytes() {
        int i;
        long j = 0;
        for (int i2 = 1; i2 <= 7; i2++) {
            byte[] paramsForPass = paramsForPass(i2);
            int i3 = 0;
            if (this.imi.rows > paramsForPass[3]) {
                int i4 = this.imi.rows;
                byte b = paramsForPass[1];
                i = (((i4 + b) - 1) - paramsForPass[3]) / b;
            } else {
                i = 0;
            }
            if (this.imi.cols > paramsForPass[2]) {
                int i5 = this.imi.cols;
                byte b2 = paramsForPass[0];
                i3 = (((i5 + b2) - 1) - paramsForPass[2]) / b2;
            }
            int i6 = ((this.imi.bitspPixel * i3) + 7) / 8;
            if (i > 0 && i3 > 0) {
                j += ((long) i) * (((long) i6) + 1);
            }
        }
        return j;
    }

    public int getCurrRowSeq() {
        return this.currRowSeq;
    }
}
