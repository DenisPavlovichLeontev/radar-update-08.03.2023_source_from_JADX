package p005ar.com.hjg.pngj.pixels;

import java.io.OutputStream;
import kotlin.UByte;
import okhttp3.internal.p014ws.RealWebSocket;
import p005ar.com.hjg.pngj.FilterType;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjOutputException;

/* renamed from: ar.com.hjg.pngj.pixels.PixelsWriter */
public abstract class PixelsWriter {
    protected final int buflen;
    protected final int bytesPixel;
    protected final int bytesRow;
    private CompressorStream compressorStream;
    protected int currentRow;
    protected int deflaterCompLevel = 6;
    protected int deflaterStrategy = 0;
    protected FilterType filterType;
    private int[] filtersUsed = new int[5];
    protected final ImageInfo imgInfo;
    protected boolean initdone = false;

    /* renamed from: os */
    private OutputStream f145os;

    /* access modifiers changed from: protected */
    public abstract void filterAndWrite(byte[] bArr);

    public abstract byte[] getRowb();

    public PixelsWriter(ImageInfo imageInfo) {
        this.imgInfo = imageInfo;
        int i = imageInfo.bytesPerRow;
        this.bytesRow = i;
        this.buflen = i + 1;
        this.bytesPixel = imageInfo.bytesPixel;
        this.currentRow = -1;
        this.filterType = FilterType.FILTER_DEFAULT;
    }

    public final void processRow(byte[] bArr) {
        if (!this.initdone) {
            init();
        }
        this.currentRow++;
        filterAndWrite(bArr);
    }

    /* access modifiers changed from: protected */
    public void sendToCompressedStream(byte[] bArr) {
        this.compressorStream.write(bArr, 0, bArr.length);
        int[] iArr = this.filtersUsed;
        byte b = bArr[0];
        iArr[b] = iArr[b] + 1;
    }

    /* access modifiers changed from: protected */
    public final byte[] filterRowWithFilterType(FilterType filterType2, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        int i;
        int i2;
        int i3;
        if (filterType2 == FilterType.FILTER_NONE) {
            bArr3 = bArr;
        }
        bArr3[0] = (byte) filterType2.val;
        int i4 = C06961.$SwitchMap$ar$com$hjg$pngj$FilterType[filterType2.ordinal()];
        if (i4 != 1) {
            if (i4 == 2) {
                int i5 = 1;
                while (true) {
                    i = this.bytesPixel;
                    if (i5 > i) {
                        break;
                    }
                    bArr3[i5] = (byte) PngHelperInternal.filterRowPaeth(bArr[i5], 0, bArr2[i5] & UByte.MAX_VALUE, 0);
                    i5++;
                }
                int i6 = i + 1;
                int i7 = 1;
                while (i6 <= this.bytesRow) {
                    bArr3[i6] = (byte) PngHelperInternal.filterRowPaeth(bArr[i6], bArr[i7] & UByte.MAX_VALUE, bArr2[i6] & UByte.MAX_VALUE, bArr2[i7] & UByte.MAX_VALUE);
                    i6++;
                    i7++;
                }
            } else if (i4 == 3) {
                int i8 = 1;
                while (true) {
                    i2 = this.bytesPixel;
                    if (i8 > i2) {
                        break;
                    }
                    bArr3[i8] = bArr[i8];
                    i8++;
                }
                int i9 = i2 + 1;
                int i10 = 1;
                while (i9 <= this.bytesRow) {
                    bArr3[i9] = (byte) (bArr[i9] - bArr[i10]);
                    i9++;
                    i10++;
                }
            } else if (i4 == 4) {
                int i11 = 1;
                while (true) {
                    i3 = this.bytesPixel;
                    if (i11 > i3) {
                        break;
                    }
                    bArr3[i11] = (byte) (bArr[i11] - ((bArr2[i11] & UByte.MAX_VALUE) / 2));
                    i11++;
                }
                int i12 = i3 + 1;
                int i13 = 1;
                while (i12 <= this.bytesRow) {
                    bArr3[i12] = (byte) (bArr[i12] - (((bArr2[i12] & UByte.MAX_VALUE) + (bArr[i13] & UByte.MAX_VALUE)) / 2));
                    i12++;
                    i13++;
                }
            } else if (i4 == 5) {
                for (int i14 = 1; i14 <= this.bytesRow; i14++) {
                    bArr3[i14] = (byte) (bArr[i14] - bArr2[i14]);
                }
            } else {
                throw new PngjOutputException("Filter type not recognized: " + filterType2);
            }
        }
        return bArr3;
    }

    /* renamed from: ar.com.hjg.pngj.pixels.PixelsWriter$1 */
    static /* synthetic */ class C06961 {
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
                ar.com.hjg.pngj.FilterType r1 = p005ar.com.hjg.pngj.FilterType.FILTER_PAETH     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$ar$com$hjg$pngj$FilterType     // Catch:{ NoSuchFieldError -> 0x0028 }
                ar.com.hjg.pngj.FilterType r1 = p005ar.com.hjg.pngj.FilterType.FILTER_SUB     // Catch:{ NoSuchFieldError -> 0x0028 }
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
                ar.com.hjg.pngj.FilterType r1 = p005ar.com.hjg.pngj.FilterType.FILTER_UP     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.pixels.PixelsWriter.C06961.<clinit>():void");
        }
    }

    /* access modifiers changed from: protected */
    public final void init() {
        if (!this.initdone) {
            initParams();
            this.initdone = true;
        }
    }

    /* access modifiers changed from: protected */
    public void initParams() {
        if (this.compressorStream == null) {
            this.compressorStream = new CompressorStreamDeflater(this.f145os, this.buflen, this.imgInfo.getTotalRawBytes(), this.deflaterCompLevel, this.deflaterStrategy);
        }
    }

    public void close() {
        CompressorStream compressorStream2 = this.compressorStream;
        if (compressorStream2 != null) {
            compressorStream2.close();
        }
    }

    public void setDeflaterStrategy(Integer num) {
        this.deflaterStrategy = num.intValue();
    }

    public void setDeflaterCompLevel(Integer num) {
        this.deflaterCompLevel = num.intValue();
    }

    public Integer getDeflaterCompLevel() {
        return Integer.valueOf(this.deflaterCompLevel);
    }

    public final void setOs(OutputStream outputStream) {
        this.f145os = outputStream;
    }

    public OutputStream getOs() {
        return this.f145os;
    }

    public final FilterType getFilterType() {
        return this.filterType;
    }

    public final void setFilterType(FilterType filterType2) {
        this.filterType = filterType2;
    }

    public double getCompression() {
        if (this.compressorStream.isDone()) {
            return this.compressorStream.getCompressionRatio();
        }
        return 1.0d;
    }

    public void setCompressorStream(CompressorStream compressorStream2) {
        this.compressorStream = compressorStream2;
    }

    public long getTotalBytesToWrite() {
        return this.imgInfo.getTotalRawBytes();
    }

    /* access modifiers changed from: protected */
    public FilterType getDefaultFilter() {
        if (this.imgInfo.indexed || this.imgInfo.bitDepth < 8) {
            return FilterType.FILTER_NONE;
        }
        if (this.imgInfo.getTotalPixels() < RealWebSocket.DEFAULT_MINIMUM_DEFLATE_SIZE) {
            return FilterType.FILTER_NONE;
        }
        if (this.imgInfo.rows == 1) {
            return FilterType.FILTER_SUB;
        }
        if (this.imgInfo.cols == 1) {
            return FilterType.FILTER_UP;
        }
        return FilterType.FILTER_PAETH;
    }

    public final String getFiltersUsed() {
        return String.format("%d,%d,%d,%d,%d", new Object[]{Integer.valueOf((int) (((((double) this.filtersUsed[0]) * 100.0d) / ((double) this.imgInfo.rows)) + 0.5d)), Integer.valueOf((int) (((((double) this.filtersUsed[1]) * 100.0d) / ((double) this.imgInfo.rows)) + 0.5d)), Integer.valueOf((int) (((((double) this.filtersUsed[2]) * 100.0d) / ((double) this.imgInfo.rows)) + 0.5d)), Integer.valueOf((int) (((((double) this.filtersUsed[3]) * 100.0d) / ((double) this.imgInfo.rows)) + 0.5d)), Integer.valueOf((int) (((((double) this.filtersUsed[4]) * 100.0d) / ((double) this.imgInfo.rows)) + 0.5d))});
    }
}
