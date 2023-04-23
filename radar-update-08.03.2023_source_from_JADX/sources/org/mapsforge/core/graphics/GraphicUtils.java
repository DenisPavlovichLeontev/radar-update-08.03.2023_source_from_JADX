package org.mapsforge.core.graphics;

public final class GraphicUtils {
    public static int getAlpha(int i) {
        return (i >> 24) & 255;
    }

    public static float[] imageSize(float f, float f2, float f3, int i, int i2, int i3) {
        float f4 = f * f3;
        float f5 = f3 * f2;
        float f6 = f / f2;
        if (i != 0 && i2 != 0) {
            f4 = (float) i;
            f5 = (float) i2;
        } else if (i == 0 && i2 != 0) {
            f5 = (float) i2;
            f4 = f5 * f6;
        } else if (i != 0 && i2 == 0) {
            f4 = (float) i;
            f5 = f4 / f6;
        }
        if (i3 != 100) {
            float f7 = ((float) i3) / 100.0f;
            f4 *= f7;
            f5 *= f7;
        }
        return new float[]{f4, f5};
    }

    public static int filterColor(int i, Filter filter) {
        if (filter == Filter.NONE) {
            return i;
        }
        int i2 = i >>> 24;
        int i3 = (i >> 16) & 255;
        int i4 = (i >> 8) & 255;
        int i5 = i & 255;
        int i6 = C13081.$SwitchMap$org$mapsforge$core$graphics$Filter[filter.ordinal()];
        if (i6 == 1) {
            i3 = (int) ((((float) i3) * 0.213f) + (((float) i4) * 0.715f) + (((float) i5) * 0.072f));
        } else if (i6 != 2) {
            if (i6 == 3) {
                i3 = 255 - i3;
                i4 = 255 - i4;
                i5 = 255 - i5;
            }
            return i5 | (i2 << 24) | (i3 << 16) | (i4 << 8);
        } else {
            i3 = 255 - ((int) (((((float) i3) * 0.213f) + (((float) i4) * 0.715f)) + (((float) i5) * 0.072f)));
        }
        i5 = i3;
        i4 = i5;
        return i5 | (i2 << 24) | (i3 << 16) | (i4 << 8);
    }

    /* renamed from: org.mapsforge.core.graphics.GraphicUtils$1 */
    static /* synthetic */ class C13081 {
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$core$graphics$Filter;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                org.mapsforge.core.graphics.Filter[] r0 = org.mapsforge.core.graphics.Filter.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$mapsforge$core$graphics$Filter = r0
                org.mapsforge.core.graphics.Filter r1 = org.mapsforge.core.graphics.Filter.GRAYSCALE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Filter     // Catch:{ NoSuchFieldError -> 0x001d }
                org.mapsforge.core.graphics.Filter r1 = org.mapsforge.core.graphics.Filter.GRAYSCALE_INVERT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Filter     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.mapsforge.core.graphics.Filter r1 = org.mapsforge.core.graphics.Filter.INVERT     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.core.graphics.GraphicUtils.C13081.<clinit>():void");
        }
    }

    private GraphicUtils() {
    }
}
