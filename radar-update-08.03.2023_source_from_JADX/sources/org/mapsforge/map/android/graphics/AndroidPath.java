package org.mapsforge.map.android.graphics;

import android.graphics.Path;
import org.mapsforge.core.graphics.FillRule;
import org.mapsforge.core.graphics.Path;

class AndroidPath implements Path {
    final android.graphics.Path path = new android.graphics.Path();

    AndroidPath() {
    }

    /* renamed from: org.mapsforge.map.android.graphics.AndroidPath$1 */
    static /* synthetic */ class C13121 {
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$core$graphics$FillRule;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                org.mapsforge.core.graphics.FillRule[] r0 = org.mapsforge.core.graphics.FillRule.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$mapsforge$core$graphics$FillRule = r0
                org.mapsforge.core.graphics.FillRule r1 = org.mapsforge.core.graphics.FillRule.EVEN_ODD     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$FillRule     // Catch:{ NoSuchFieldError -> 0x001d }
                org.mapsforge.core.graphics.FillRule r1 = org.mapsforge.core.graphics.FillRule.NON_ZERO     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.android.graphics.AndroidPath.C13121.<clinit>():void");
        }
    }

    private static Path.FillType getWindingRule(FillRule fillRule) {
        int i = C13121.$SwitchMap$org$mapsforge$core$graphics$FillRule[fillRule.ordinal()];
        if (i == 1) {
            return Path.FillType.EVEN_ODD;
        }
        if (i == 2) {
            return Path.FillType.WINDING;
        }
        throw new IllegalArgumentException("unknown fill rule:" + fillRule);
    }

    public void clear() {
        this.path.rewind();
    }

    public void close() {
        this.path.close();
    }

    public boolean isEmpty() {
        return this.path.isEmpty();
    }

    public void lineTo(float f, float f2) {
        this.path.lineTo(f, f2);
    }

    public void moveTo(float f, float f2) {
        this.path.moveTo(f, f2);
    }

    public void setFillRule(FillRule fillRule) {
        this.path.setFillType(getWindingRule(fillRule));
    }
}
