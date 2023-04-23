package p005ar.com.hjg.pngj.pixels;

import java.util.Arrays;
import okhttp3.internal.p014ws.RealWebSocket;
import p005ar.com.hjg.pngj.FilterType;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngjOutputException;

/* renamed from: ar.com.hjg.pngj.pixels.PixelsWriterDefault */
public class PixelsWriterDefault extends PixelsWriter {
    protected int adaptMaxSkip;
    protected int adaptNextRow = 0;
    protected double adaptSkipIncreaseFactor;
    protected int adaptSkipIncreaseSinceRow;
    protected FilterType curfilterType;
    protected FiltersPerformance filtersPerformance;
    protected byte[] rowb;
    protected byte[] rowbfilter;
    protected byte[] rowbprev;

    public PixelsWriterDefault(ImageInfo imageInfo) {
        super(imageInfo);
        this.filtersPerformance = new FiltersPerformance(imageInfo);
    }

    /* access modifiers changed from: protected */
    public void initParams() {
        super.initParams();
        byte[] bArr = this.rowb;
        if (bArr == null || bArr.length < this.buflen) {
            this.rowb = new byte[this.buflen];
        }
        byte[] bArr2 = this.rowbfilter;
        if (bArr2 == null || bArr2.length < this.buflen) {
            this.rowbfilter = new byte[this.buflen];
        }
        byte[] bArr3 = this.rowbprev;
        if (bArr3 == null || bArr3.length < this.buflen) {
            this.rowbprev = new byte[this.buflen];
        } else {
            Arrays.fill(this.rowbprev, (byte) 0);
        }
        if (this.imgInfo.cols < 3 && !FilterType.isValidStandard(this.filterType)) {
            this.filterType = FilterType.FILTER_DEFAULT;
        }
        if (this.imgInfo.rows < 3 && !FilterType.isValidStandard(this.filterType)) {
            this.filterType = FilterType.FILTER_DEFAULT;
        }
        if (this.imgInfo.getTotalPixels() <= RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE && !FilterType.isValidStandard(this.filterType)) {
            this.filterType = getDefaultFilter();
        }
        if (FilterType.isAdaptive(this.filterType)) {
            this.adaptNextRow = 0;
            if (this.filterType == FilterType.FILTER_ADAPTIVE_FAST) {
                this.adaptMaxSkip = 200;
                this.adaptSkipIncreaseSinceRow = 3;
                this.adaptSkipIncreaseFactor = 0.25d;
            } else if (this.filterType == FilterType.FILTER_ADAPTIVE_MEDIUM) {
                this.adaptMaxSkip = 8;
                this.adaptSkipIncreaseSinceRow = 32;
                this.adaptSkipIncreaseFactor = 0.0125d;
            } else if (this.filterType == FilterType.FILTER_ADAPTIVE_FULL) {
                this.adaptMaxSkip = 0;
                this.adaptSkipIncreaseSinceRow = 128;
                this.adaptSkipIncreaseFactor = 0.008333333333333333d;
            } else {
                throw new PngjOutputException("bad filter " + this.filterType);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void filterAndWrite(byte[] bArr) {
        if (bArr == this.rowb) {
            decideCurFilterType();
            sendToCompressedStream(filterRowWithFilterType(this.curfilterType, bArr, this.rowbprev, this.rowbfilter));
            byte[] bArr2 = this.rowb;
            this.rowb = this.rowbprev;
            this.rowbprev = bArr2;
            return;
        }
        throw new RuntimeException("??");
    }

    /* access modifiers changed from: protected */
    public void decideCurFilterType() {
        if (FilterType.isValidStandard(getFilterType())) {
            this.curfilterType = getFilterType();
        } else {
            int i = 0;
            if (getFilterType() == FilterType.FILTER_PRESERVE) {
                this.curfilterType = FilterType.getByVal(this.rowb[0]);
            } else if (getFilterType() == FilterType.FILTER_CYCLIC) {
                this.curfilterType = FilterType.getByVal(this.currentRow % 5);
            } else if (getFilterType() == FilterType.FILTER_DEFAULT) {
                setFilterType(getDefaultFilter());
                this.curfilterType = getFilterType();
            } else if (!FilterType.isAdaptive(getFilterType())) {
                throw new PngjOutputException("not implemented filter: " + getFilterType());
            } else if (this.currentRow == this.adaptNextRow) {
                for (FilterType updateFromRaw : FilterType.getAllStandard()) {
                    this.filtersPerformance.updateFromRaw(updateFromRaw, this.rowb, this.rowbprev, this.currentRow);
                }
                this.curfilterType = this.filtersPerformance.getPreferred();
                int round = this.currentRow >= this.adaptSkipIncreaseSinceRow ? (int) Math.round(((double) (this.currentRow - this.adaptSkipIncreaseSinceRow)) * this.adaptSkipIncreaseFactor) : 0;
                int i2 = this.adaptMaxSkip;
                if (round > i2) {
                    round = i2;
                }
                if (this.currentRow != 0) {
                    i = round;
                }
                this.adaptNextRow = this.currentRow + 1 + i;
            }
        }
        if (this.currentRow == 0 && this.curfilterType != FilterType.FILTER_NONE && this.curfilterType != FilterType.FILTER_SUB) {
            this.curfilterType = FilterType.FILTER_SUB;
        }
    }

    public byte[] getRowb() {
        if (!this.initdone) {
            init();
        }
        return this.rowb;
    }

    public void close() {
        super.close();
    }

    public void setPreferenceForNone(double d) {
        this.filtersPerformance.setPreferenceForNone(d);
    }

    public void tuneMemory(double d) {
        this.filtersPerformance.tuneMemory(d);
    }

    public void setFilterWeights(double[] dArr) {
        this.filtersPerformance.setFilterWeights(dArr);
    }
}
