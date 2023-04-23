package p005ar.com.hjg.pngj;

import java.util.Arrays;
import java.util.zip.Checksum;
import java.util.zip.Inflater;
import kotlin.UByte;

/* renamed from: ar.com.hjg.pngj.IdatSet */
public class IdatSet extends DeflatedChunksSet {
    protected final Deinterlacer deinterlacer;
    protected int[] filterUseStat;
    protected final ImageInfo imgInfo;
    protected byte[] rowUnfiltered;
    protected byte[] rowUnfilteredPrev;
    final RowInfo rowinfo;

    /* access modifiers changed from: protected */
    public void processDoneCallback() {
    }

    public IdatSet(String str, ImageInfo imageInfo, Deinterlacer deinterlacer2) {
        this(str, imageInfo, deinterlacer2, (Inflater) null, (byte[]) null);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public IdatSet(java.lang.String r8, p005ar.com.hjg.pngj.ImageInfo r9, p005ar.com.hjg.pngj.Deinterlacer r10, java.util.zip.Inflater r11, byte[] r12) {
        /*
            r7 = this;
            if (r10 == 0) goto L_0x0007
            int r0 = r10.getBytesToRead()
            goto L_0x0009
        L_0x0007:
            int r0 = r9.bytesPerRow
        L_0x0009:
            int r0 = r0 + 1
            r3 = r0
            int r0 = r9.bytesPerRow
            int r4 = r0 + 1
            r1 = r7
            r2 = r8
            r5 = r11
            r6 = r12
            r1.<init>(r2, r3, r4, r5, r6)
            r8 = 5
            int[] r8 = new int[r8]
            r7.filterUseStat = r8
            r7.imgInfo = r9
            r7.deinterlacer = r10
            ar.com.hjg.pngj.RowInfo r8 = new ar.com.hjg.pngj.RowInfo
            r8.<init>(r9, r10)
            r7.rowinfo = r8
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.IdatSet.<init>(java.lang.String, ar.com.hjg.pngj.ImageInfo, ar.com.hjg.pngj.Deinterlacer, java.util.zip.Inflater, byte[]):void");
    }

    public void unfilterRow() {
        unfilterRow(this.rowinfo.bytesRow);
    }

    /* access modifiers changed from: protected */
    public void unfilterRow(int i) {
        byte[] bArr = this.rowUnfiltered;
        if (bArr == null || bArr.length < this.row.length) {
            this.rowUnfiltered = new byte[this.row.length];
            this.rowUnfilteredPrev = new byte[this.row.length];
        }
        if (this.rowinfo.rowNsubImg == 0) {
            Arrays.fill(this.rowUnfiltered, (byte) 0);
        }
        byte[] bArr2 = this.rowUnfiltered;
        this.rowUnfiltered = this.rowUnfilteredPrev;
        this.rowUnfilteredPrev = bArr2;
        byte b = this.row[0];
        FilterType byVal = FilterType.getByVal(b);
        if (byVal != null) {
            int[] iArr = this.filterUseStat;
            iArr[b] = iArr[b] + 1;
            this.rowUnfiltered[0] = this.row[0];
            int i2 = C06771.$SwitchMap$ar$com$hjg$pngj$FilterType[byVal.ordinal()];
            if (i2 == 1) {
                unfilterRowNone(i);
            } else if (i2 == 2) {
                unfilterRowSub(i);
            } else if (i2 == 3) {
                unfilterRowUp(i);
            } else if (i2 == 4) {
                unfilterRowAverage(i);
            } else if (i2 == 5) {
                unfilterRowPaeth(i);
            } else {
                throw new PngjInputException("Filter type " + b + " not implemented");
            }
        } else {
            throw new PngjInputException("Filter type " + b + " invalid");
        }
    }

    /* renamed from: ar.com.hjg.pngj.IdatSet$1 */
    static /* synthetic */ class C06771 {
        static final /* synthetic */ int[] $SwitchMap$ar$com$hjg$pngj$FilterType;

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                ar.com.hjg.pngj.FilterType[] r0 = p005ar.com.hjg.pngj.FilterType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$ar$com$hjg$pngj$FilterType = r0
                ar.com.hjg.pngj.FilterType r1 = p005ar.com.hjg.pngj.FilterType.FILTER_NONE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$ar$com$hjg$pngj$FilterType     // Catch:{ NoSuchFieldError -> 0x001d }
                ar.com.hjg.pngj.FilterType r1 = p005ar.com.hjg.pngj.FilterType.FILTER_SUB     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$ar$com$hjg$pngj$FilterType     // Catch:{ NoSuchFieldError -> 0x0028 }
                ar.com.hjg.pngj.FilterType r1 = p005ar.com.hjg.pngj.FilterType.FILTER_UP     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$ar$com$hjg$pngj$FilterType     // Catch:{ NoSuchFieldError -> 0x0033 }
                ar.com.hjg.pngj.FilterType r1 = p005ar.com.hjg.pngj.FilterType.FILTER_AVERAGE     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$ar$com$hjg$pngj$FilterType     // Catch:{ NoSuchFieldError -> 0x003e }
                ar.com.hjg.pngj.FilterType r1 = p005ar.com.hjg.pngj.FilterType.FILTER_PAETH     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.IdatSet.C06771.<clinit>():void");
        }
    }

    private void unfilterRowAverage(int i) {
        int i2 = 1;
        int i3 = 1 - this.imgInfo.bytesPixel;
        while (i2 <= i) {
            this.rowUnfiltered[i2] = (byte) (this.row[i2] + (((i3 > 0 ? this.rowUnfiltered[i3] & UByte.MAX_VALUE : 0) + (this.rowUnfilteredPrev[i2] & UByte.MAX_VALUE)) / 2));
            i2++;
            i3++;
        }
    }

    private void unfilterRowNone(int i) {
        for (int i2 = 1; i2 <= i; i2++) {
            this.rowUnfiltered[i2] = this.row[i2];
        }
    }

    private void unfilterRowPaeth(int i) {
        int i2 = 1;
        int i3 = 1 - this.imgInfo.bytesPixel;
        while (i2 <= i) {
            byte b = 0;
            byte b2 = i3 > 0 ? this.rowUnfiltered[i3] & UByte.MAX_VALUE : 0;
            if (i3 > 0) {
                b = this.rowUnfilteredPrev[i3] & UByte.MAX_VALUE;
            }
            this.rowUnfiltered[i2] = (byte) (this.row[i2] + PngHelperInternal.filterPaethPredictor(b2, this.rowUnfilteredPrev[i2] & UByte.MAX_VALUE, b));
            i2++;
            i3++;
        }
    }

    private void unfilterRowSub(int i) {
        for (int i2 = 1; i2 <= this.imgInfo.bytesPixel; i2++) {
            this.rowUnfiltered[i2] = this.row[i2];
        }
        int i3 = this.imgInfo.bytesPixel + 1;
        int i4 = 1;
        while (i3 <= i) {
            this.rowUnfiltered[i3] = (byte) (this.row[i3] + this.rowUnfiltered[i4]);
            i3++;
            i4++;
        }
    }

    private void unfilterRowUp(int i) {
        for (int i2 = 1; i2 <= i; i2++) {
            this.rowUnfiltered[i2] = (byte) (this.row[i2] + this.rowUnfilteredPrev[i2]);
        }
    }

    /* access modifiers changed from: protected */
    public void preProcessRow() {
        super.preProcessRow();
        this.rowinfo.update(getRown());
        unfilterRow();
        RowInfo rowInfo = this.rowinfo;
        rowInfo.updateBuf(this.rowUnfiltered, rowInfo.bytesRow + 1);
    }

    /* access modifiers changed from: protected */
    public int processRowCallback() {
        return advanceToNextRow();
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int advanceToNextRow() {
        /*
            r3 = this;
            ar.com.hjg.pngj.Deinterlacer r0 = r3.deinterlacer
            r1 = 0
            if (r0 != 0) goto L_0x0019
            int r0 = r3.getRown()
            ar.com.hjg.pngj.ImageInfo r2 = r3.imgInfo
            int r2 = r2.rows
            int r2 = r2 + -1
            if (r0 < r2) goto L_0x0012
            goto L_0x0026
        L_0x0012:
            ar.com.hjg.pngj.ImageInfo r0 = r3.imgInfo
            int r0 = r0.bytesPerRow
        L_0x0016:
            int r1 = r0 + 1
            goto L_0x0026
        L_0x0019:
            boolean r0 = r0.nextRow()
            if (r0 == 0) goto L_0x0026
            ar.com.hjg.pngj.Deinterlacer r0 = r3.deinterlacer
            int r0 = r0.getBytesToRead()
            goto L_0x0016
        L_0x0026:
            boolean r0 = r3.isCallbackMode()
            if (r0 != 0) goto L_0x002f
            r3.prepareForNextRow(r1)
        L_0x002f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.IdatSet.advanceToNextRow():int");
    }

    public boolean isRowReady() {
        return !isWaitingForMoreInput();
    }

    public byte[] getUnfilteredRow() {
        return this.rowUnfiltered;
    }

    public Deinterlacer getDeinterlacer() {
        return this.deinterlacer;
    }

    /* access modifiers changed from: package-private */
    public void updateCrcs(Checksum... checksumArr) {
        for (Checksum checksum : checksumArr) {
            if (checksum != null) {
                checksum.update(getUnfilteredRow(), 1, getRowFilled() - 1);
            }
        }
    }

    public void close() {
        super.close();
        this.rowUnfiltered = null;
        this.rowUnfilteredPrev = null;
    }

    public int[] getFilterUseStat() {
        return this.filterUseStat;
    }
}
