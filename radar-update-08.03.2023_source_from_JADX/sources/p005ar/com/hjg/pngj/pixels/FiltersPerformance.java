package p005ar.com.hjg.pngj.pixels;

import java.util.Arrays;
import kotlin.UByte;
import p005ar.com.hjg.pngj.FilterType;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngHelperInternal;
import p005ar.com.hjg.pngj.PngjExceptionInternal;

/* renamed from: ar.com.hjg.pngj.pixels.FiltersPerformance */
public class FiltersPerformance {
    public static final double[] FILTER_WEIGHTS_DEFAULT = {0.73d, 1.03d, 0.97d, 1.11d, 1.22d};
    private static final double LOG2NI = (-1.0d / Math.log(2.0d));
    private double[] absum = new double[5];
    private double[] cost = new double[5];
    private double[] entropy = new double[5];
    private double[] filter_weights = {-1.0d, -1.0d, -1.0d, -1.0d, -1.0d};
    private int[] histog = new int[256];
    private final ImageInfo iminfo;
    private boolean initdone = false;
    private int lastprefered = -1;
    private int lastrow = -1;
    private double memoryA = 0.7d;
    private double preferenceForNone = 1.0d;

    public FiltersPerformance(ImageInfo imageInfo) {
        this.iminfo = imageInfo;
    }

    private void init() {
        double[] dArr = this.filter_weights;
        if (dArr[0] < 0.0d) {
            System.arraycopy(FILTER_WEIGHTS_DEFAULT, 0, dArr, 0, 5);
            double d = this.filter_weights[0];
            if (this.iminfo.bitDepth == 16) {
                d = 1.2d;
            } else if (this.iminfo.alpha) {
                d = 0.8d;
            } else if (this.iminfo.indexed || this.iminfo.bitDepth < 8) {
                d = 0.4d;
            }
            this.filter_weights[0] = d / this.preferenceForNone;
        }
        Arrays.fill(this.cost, 1.0d);
        this.initdone = true;
    }

    public void updateFromFiltered(FilterType filterType, byte[] bArr, int i) {
        updateFromRawOrFiltered(filterType, bArr, (byte[]) null, (byte[]) null, i);
    }

    public void updateFromRaw(FilterType filterType, byte[] bArr, byte[] bArr2, int i) {
        updateFromRawOrFiltered(filterType, (byte[]) null, bArr, bArr2, i);
    }

    private void updateFromRawOrFiltered(FilterType filterType, byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
        if (!this.initdone) {
            init();
        }
        if (i != this.lastrow) {
            Arrays.fill(this.absum, Double.NaN);
            Arrays.fill(this.entropy, Double.NaN);
        }
        this.lastrow = i;
        if (bArr != null) {
            computeHistogram(bArr);
        } else {
            computeHistogramForFilter(filterType, bArr2, bArr3);
        }
        if (filterType == FilterType.FILTER_NONE) {
            this.entropy[filterType.val] = computeEntropyFromHistogram();
        } else {
            this.absum[filterType.val] = computeAbsFromHistogram();
        }
    }

    public FilterType getPreferred() {
        double d;
        double d2 = Double.MAX_VALUE;
        int i = 0;
        for (int i2 = 0; i2 < 5; i2++) {
            if (!Double.isNaN(this.absum[i2])) {
                d = this.absum[i2];
            } else if (!Double.isNaN(this.entropy[i2])) {
                d = (Math.pow(2.0d, this.entropy[i2]) - 1.0d) * 0.5d;
            }
            double d3 = d * this.filter_weights[i2];
            double[] dArr = this.cost;
            double d4 = dArr[i2];
            double d5 = this.memoryA;
            double d6 = (d4 * d5) + ((1.0d - d5) * d3);
            dArr[i2] = d6;
            if (d6 < d2) {
                i = i2;
                d2 = d6;
            }
        }
        this.lastprefered = i;
        return FilterType.getByVal(i);
    }

    public final void computeHistogramForFilter(FilterType filterType, byte[] bArr, byte[] bArr2) {
        Arrays.fill(this.histog, 0);
        int i = this.iminfo.bytesPerRow;
        int i2 = C06931.$SwitchMap$ar$com$hjg$pngj$FilterType[filterType.ordinal()];
        if (i2 == 1) {
            for (int i3 = 1; i3 <= i; i3++) {
                int[] iArr = this.histog;
                byte b = bArr[i3] & UByte.MAX_VALUE;
                iArr[b] = iArr[b] + 1;
            }
        } else if (i2 == 2) {
            for (int i4 = 1; i4 <= i; i4++) {
                int[] iArr2 = this.histog;
                int filterRowPaeth = PngHelperInternal.filterRowPaeth(bArr[i4], 0, bArr2[i4] & UByte.MAX_VALUE, 0);
                iArr2[filterRowPaeth] = iArr2[filterRowPaeth] + 1;
            }
            int i5 = this.iminfo.bytesPixel + 1;
            int i6 = 1;
            while (i5 <= i) {
                int[] iArr3 = this.histog;
                int filterRowPaeth2 = PngHelperInternal.filterRowPaeth(bArr[i5], bArr[i6] & UByte.MAX_VALUE, bArr2[i5] & UByte.MAX_VALUE, bArr2[i6] & UByte.MAX_VALUE);
                iArr3[filterRowPaeth2] = iArr3[filterRowPaeth2] + 1;
                i5++;
                i6++;
            }
        } else if (i2 == 3) {
            for (int i7 = 1; i7 <= this.iminfo.bytesPixel; i7++) {
                int[] iArr4 = this.histog;
                byte b2 = bArr[i7] & UByte.MAX_VALUE;
                iArr4[b2] = iArr4[b2] + 1;
            }
            int i8 = this.iminfo.bytesPixel + 1;
            int i9 = 1;
            while (i8 <= i) {
                int[] iArr5 = this.histog;
                int i10 = (bArr[i8] - bArr[i9]) & 255;
                iArr5[i10] = iArr5[i10] + 1;
                i8++;
                i9++;
            }
        } else if (i2 == 4) {
            for (int i11 = 1; i11 <= this.iminfo.bytesPerRow; i11++) {
                int[] iArr6 = this.histog;
                int i12 = (bArr[i11] - bArr2[i11]) & 255;
                iArr6[i12] = iArr6[i12] + 1;
            }
        } else if (i2 == 5) {
            for (int i13 = 1; i13 <= this.iminfo.bytesPixel; i13++) {
                int[] iArr7 = this.histog;
                int i14 = ((bArr[i13] & UByte.MAX_VALUE) - ((bArr2[i13] & UByte.MAX_VALUE) / 2)) & 255;
                iArr7[i14] = iArr7[i14] + 1;
            }
            int i15 = this.iminfo.bytesPixel + 1;
            int i16 = 1;
            while (i15 <= i) {
                int[] iArr8 = this.histog;
                int i17 = ((bArr[i15] & UByte.MAX_VALUE) - (((bArr2[i15] & UByte.MAX_VALUE) + (bArr[i16] & UByte.MAX_VALUE)) / 2)) & 255;
                iArr8[i17] = iArr8[i17] + 1;
                i15++;
                i16++;
            }
        } else {
            throw new PngjExceptionInternal("Bad filter:" + filterType);
        }
    }

    /* renamed from: ar.com.hjg.pngj.pixels.FiltersPerformance$1 */
    static /* synthetic */ class C06931 {
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
                ar.com.hjg.pngj.FilterType r1 = p005ar.com.hjg.pngj.FilterType.FILTER_UP     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$ar$com$hjg$pngj$FilterType     // Catch:{ NoSuchFieldError -> 0x003e }
                ar.com.hjg.pngj.FilterType r1 = p005ar.com.hjg.pngj.FilterType.FILTER_AVERAGE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p005ar.com.hjg.pngj.pixels.FiltersPerformance.C06931.<clinit>():void");
        }
    }

    public void computeHistogram(byte[] bArr) {
        Arrays.fill(this.histog, 0);
        for (int i = 1; i < this.iminfo.bytesPerRow; i++) {
            int[] iArr = this.histog;
            byte b = bArr[i] & UByte.MAX_VALUE;
            iArr[b] = iArr[b] + 1;
        }
    }

    public double computeAbsFromHistogram() {
        int i;
        int i2 = 0;
        int i3 = 1;
        while (true) {
            if (i3 >= 128) {
                break;
            }
            i2 += this.histog[i3] * i3;
            i3++;
        }
        int i4 = 128;
        for (i = 128; i > 0; i--) {
            i2 += this.histog[i4] * i;
            i4++;
        }
        return ((double) i2) / ((double) this.iminfo.bytesPerRow);
    }

    public final double computeEntropyFromHistogram() {
        double d = 1.0d / ((double) this.iminfo.bytesPerRow);
        double log = Math.log(d);
        double d2 = 0.0d;
        for (int i : this.histog) {
            if (i > 0) {
                double d3 = (double) i;
                d2 += (Math.log(d3) + log) * d3;
            }
        }
        double d4 = d2 * d * LOG2NI;
        if (d4 < 0.0d) {
            return 0.0d;
        }
        return d4;
    }

    public void setPreferenceForNone(double d) {
        this.preferenceForNone = d;
    }

    public void tuneMemory(double d) {
        if (d == 0.0d) {
            this.memoryA = 0.0d;
        } else {
            this.memoryA = Math.pow(this.memoryA, 1.0d / d);
        }
    }

    public void setFilterWeights(double[] dArr) {
        System.arraycopy(dArr, 0, this.filter_weights, 0, 5);
    }
}
