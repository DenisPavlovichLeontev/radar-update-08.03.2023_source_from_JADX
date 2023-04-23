package p005ar.com.hjg.pngj.pixels;

import java.util.LinkedList;
import p005ar.com.hjg.pngj.FilterType;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;

/* renamed from: ar.com.hjg.pngj.pixels.PixelsWriterMultiple */
public class PixelsWriterMultiple extends PixelsWriter {
    protected static final int HINT_MEMORY_DEFAULT_KB = 100;
    protected int bandNum;
    protected CompressorStream[] filterBank = new CompressorStream[6];
    protected byte[] filteredRowTmp;
    protected byte[][] filteredRows = new byte[5][];
    protected FiltersPerformance filtersPerf;
    protected int firstRowInThisBand;
    protected int hintMemoryKb;
    private int hintRowsPerBand;
    protected int lastRowInThisBand;
    protected int rowInBand;
    protected LinkedList<byte[]> rows;
    protected int rowsPerBand;
    protected int rowsPerBandCurrent;
    private boolean tryAdaptive;
    private boolean useLz4;

    public PixelsWriterMultiple(ImageInfo imageInfo) {
        super(imageInfo);
        this.rowsPerBand = 0;
        this.rowsPerBandCurrent = 0;
        this.rowInBand = -1;
        this.bandNum = -1;
        this.tryAdaptive = true;
        this.hintMemoryKb = 100;
        this.hintRowsPerBand = 1000;
        this.useLz4 = true;
        this.filtersPerf = new FiltersPerformance(imageInfo);
        this.rows = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            this.rows.add(new byte[this.buflen]);
        }
        this.filteredRowTmp = new byte[this.buflen];
    }

    /* access modifiers changed from: protected */
    public void filterAndWrite(byte[] bArr) {
        byte[] bArr2;
        if (!this.initdone) {
            init();
        }
        int i = 0;
        if (bArr == this.rows.get(0)) {
            setBandFromNewRown();
            byte[] bArr3 = this.rows.get(1);
            for (FilterType filterType : FilterType.getAllStandardNoneLast()) {
                if (this.currentRow != 0 || filterType == FilterType.FILTER_NONE || filterType == FilterType.FILTER_SUB) {
                    byte[] filterRowWithFilterType = filterRowWithFilterType(filterType, bArr, bArr3, this.filteredRows[filterType.val]);
                    this.filterBank[filterType.val].write(filterRowWithFilterType);
                    if (this.currentRow == 0 && filterType == FilterType.FILTER_SUB) {
                        this.filterBank[FilterType.FILTER_PAETH.val].write(filterRowWithFilterType);
                        this.filterBank[FilterType.FILTER_AVERAGE.val].write(filterRowWithFilterType);
                        this.filterBank[FilterType.FILTER_UP.val].write(filterRowWithFilterType);
                    }
                    if (this.tryAdaptive) {
                        this.filtersPerf.updateFromFiltered(filterType, filterRowWithFilterType, this.currentRow);
                    }
                }
            }
            this.filteredRows[0] = bArr;
            if (this.tryAdaptive) {
                this.filterBank[5].write(this.filteredRows[this.filtersPerf.getPreferred().val]);
            }
            if (this.currentRow == this.lastRowInThisBand) {
                byte[] firstBytes = this.filterBank[getBestCompressor()].getFirstBytes();
                int i2 = this.firstRowInThisBand;
                int i3 = this.lastRowInThisBand - i2;
                while (true) {
                    int i4 = this.lastRowInThisBand;
                    if (i2 > i4) {
                        break;
                    }
                    byte b = firstBytes[i];
                    if (i2 != i4) {
                        bArr2 = filterRowWithFilterType(FilterType.getByVal(b), this.rows.get(i3), this.rows.get(i3 + 1), this.filteredRowTmp);
                    } else {
                        bArr2 = this.filteredRows[b];
                    }
                    sendToCompressedStream(bArr2);
                    i2++;
                    i3--;
                    i++;
                }
            }
            if (this.rows.size() > this.rowsPerBandCurrent) {
                LinkedList<byte[]> linkedList = this.rows;
                linkedList.addFirst(linkedList.removeLast());
                return;
            }
            this.rows.addFirst(new byte[this.buflen]);
            return;
        }
        throw new RuntimeException("?");
    }

    public byte[] getRowb() {
        return this.rows.get(0);
    }

    private void setBandFromNewRown() {
        boolean z = false;
        boolean z2 = this.currentRow == 0 || this.currentRow > this.lastRowInThisBand;
        if (this.currentRow == 0) {
            this.bandNum = -1;
        }
        if (z2) {
            this.bandNum++;
            this.rowInBand = 0;
        } else {
            this.rowInBand++;
        }
        if (z2) {
            int i = this.currentRow;
            this.firstRowInThisBand = i;
            int i2 = this.rowsPerBand;
            this.lastRowInThisBand = (i + i2) - 1;
            if ((i + (i2 * 2)) - 1 >= this.imgInfo.rows) {
                this.lastRowInThisBand = this.imgInfo.rows - 1;
            }
            int i3 = (this.lastRowInThisBand + 1) - this.firstRowInThisBand;
            this.rowsPerBandCurrent = i3;
            if (i3 > 3 && (i3 >= 10 || this.imgInfo.bytesPerRow >= 64)) {
                z = true;
            }
            this.tryAdaptive = z;
            rebuildFiltersBank();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v9, resolved type: ar.com.hjg.pngj.pixels.CompressorStreamDeflater} */
    /* JADX WARNING: type inference failed for: r3v7, types: [ar.com.hjg.pngj.pixels.CompressorStreamLz4] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void rebuildFiltersBank() {
        /*
            r11 = this;
            int r0 = r11.rowsPerBandCurrent
            long r0 = (long) r0
            int r2 = r11.buflen
            long r2 = (long) r2
            long r0 = r0 * r2
            r2 = 0
        L_0x0008:
            r3 = 5
            if (r2 > r3) goto L_0x0047
            ar.com.hjg.pngj.pixels.CompressorStream[] r3 = r11.filterBank
            r3 = r3[r2]
            if (r3 == 0) goto L_0x001c
            long r4 = r3.totalbytes
            int r4 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r4 == 0) goto L_0x0018
            goto L_0x001c
        L_0x0018:
            r3.reset()
            goto L_0x003e
        L_0x001c:
            if (r3 == 0) goto L_0x0021
            r3.close()
        L_0x0021:
            boolean r3 = r11.useLz4
            if (r3 == 0) goto L_0x002e
            ar.com.hjg.pngj.pixels.CompressorStreamLz4 r3 = new ar.com.hjg.pngj.pixels.CompressorStreamLz4
            r4 = 0
            int r5 = r11.buflen
            r3.<init>(r4, r5, r0)
            goto L_0x003a
        L_0x002e:
            ar.com.hjg.pngj.pixels.CompressorStreamDeflater r3 = new ar.com.hjg.pngj.pixels.CompressorStreamDeflater
            r5 = 0
            int r6 = r11.buflen
            r9 = 4
            r10 = 0
            r4 = r3
            r7 = r0
            r4.<init>(r5, r6, r7, r9, r10)
        L_0x003a:
            ar.com.hjg.pngj.pixels.CompressorStream[] r4 = r11.filterBank
            r4[r2] = r3
        L_0x003e:
            int r4 = r11.rowsPerBandCurrent
            r5 = 1
            r3.setStoreFirstByte(r5, r4)
            int r2 = r2 + 1
            goto L_0x0008
        L_0x0047:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.pixels.PixelsWriterMultiple.rebuildFiltersBank():void");
    }

    private int computeInitialRowsPerBand() {
        int i = 1;
        int i2 = (int) (((((double) this.hintMemoryKb) * 1024.0d) / ((double) (this.imgInfo.bytesPerRow + 1))) - 5.0d);
        if (i2 >= 1) {
            i = i2;
        }
        int i3 = this.hintRowsPerBand;
        if (i3 > 0 && i > i3) {
            i = i3;
        }
        if (i > this.imgInfo.rows) {
            i = this.imgInfo.rows;
        }
        if (i > 2 && i > this.imgInfo.rows / 8) {
            int i4 = (this.imgInfo.rows + (i - 1)) / i;
            i = (this.imgInfo.rows + (i4 / 2)) / i4;
        }
        PngHelperInternal.debug("rows :" + i + "/" + this.imgInfo.rows);
        return i;
    }

    private int getBestCompressor() {
        int i = -1;
        double d = Double.MAX_VALUE;
        for (int i2 = this.tryAdaptive ? 5 : 4; i2 >= 0; i2--) {
            double compressionRatio = this.filterBank[i2].getCompressionRatio();
            if (compressionRatio <= d) {
                i = i2;
                d = compressionRatio;
            }
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public void initParams() {
        if (this.imgInfo.cols < 3 && !FilterType.isValidStandard(this.filterType)) {
            this.filterType = FilterType.FILTER_DEFAULT;
        }
        if (this.imgInfo.rows < 3 && !FilterType.isValidStandard(this.filterType)) {
            this.filterType = FilterType.FILTER_DEFAULT;
        }
        for (int i = 1; i <= 4; i++) {
            byte[] bArr = this.filteredRows[i];
            if (bArr == null || bArr.length < this.buflen) {
                this.filteredRows[i] = new byte[this.buflen];
            }
        }
        if (this.rowsPerBand == 0) {
            this.rowsPerBand = computeInitialRowsPerBand();
        }
    }

    public void close() {
        super.close();
        this.rows.clear();
        for (CompressorStream close : this.filterBank) {
            close.close();
        }
    }

    public void setHintMemoryKb(int i) {
        if (i <= 0) {
            i = 100;
        } else if (i > 10000) {
            i = 10000;
        }
        this.hintMemoryKb = i;
    }

    public void setHintRowsPerBand(int i) {
        this.hintRowsPerBand = i;
    }

    public void setUseLz4(boolean z) {
        this.useLz4 = z;
    }

    public FiltersPerformance getFiltersPerf() {
        return this.filtersPerf;
    }
}
