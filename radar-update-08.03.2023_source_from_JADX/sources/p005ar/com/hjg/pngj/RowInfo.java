package p005ar.com.hjg.pngj;

/* renamed from: ar.com.hjg.pngj.RowInfo */
class RowInfo {
    byte[] buf;
    int buflen;
    int bytesRow;
    int colsSubImg;

    /* renamed from: dX */
    int f117dX;

    /* renamed from: dY */
    int f118dY;
    public final Deinterlacer deinterlacer;
    public final ImageInfo imgInfo;
    public final boolean imode;

    /* renamed from: oX */
    int f119oX;

    /* renamed from: oY */
    int f120oY;
    int pass;
    int rowNreal;
    int rowNseq;
    int rowNsubImg;
    int rowsSubImg;

    public RowInfo(ImageInfo imageInfo, Deinterlacer deinterlacer2) {
        this.imgInfo = imageInfo;
        this.deinterlacer = deinterlacer2;
        this.imode = deinterlacer2 != null;
    }

    /* access modifiers changed from: package-private */
    public void update(int i) {
        this.rowNseq = i;
        if (this.imode) {
            this.pass = this.deinterlacer.getPass();
            this.f117dX = this.deinterlacer.f112dX;
            this.f118dY = this.deinterlacer.f113dY;
            this.f119oX = this.deinterlacer.f114oX;
            this.f120oY = this.deinterlacer.f115oY;
            this.rowNreal = this.deinterlacer.getCurrRowReal();
            this.rowNsubImg = this.deinterlacer.getCurrRowSubimg();
            this.rowsSubImg = this.deinterlacer.getRows();
            this.colsSubImg = this.deinterlacer.getCols();
            this.bytesRow = ((this.imgInfo.bitspPixel * this.colsSubImg) + 7) / 8;
            return;
        }
        this.pass = 1;
        this.f118dY = 1;
        this.f117dX = 1;
        this.f120oY = 0;
        this.f119oX = 0;
        this.rowNsubImg = i;
        this.rowNreal = i;
        this.rowsSubImg = this.imgInfo.rows;
        this.colsSubImg = this.imgInfo.cols;
        this.bytesRow = this.imgInfo.bytesPerRow;
    }

    /* access modifiers changed from: package-private */
    public void updateBuf(byte[] bArr, int i) {
        this.buf = bArr;
        this.buflen = i;
    }
}
