package org.osmdroid.views.overlay.simplefastpoint;

import java.util.Iterator;
import java.util.List;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;

public final class SimplePointTheme implements SimpleFastPointOverlay.PointAdapter {
    private boolean mLabelled;
    private final List<IGeoPoint> mPoints;
    private boolean mStyled;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SimplePointTheme(java.util.List<org.osmdroid.api.IGeoPoint> r5) {
        /*
            r4 = this;
            int r0 = r5.size()
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0012
            java.lang.Object r0 = r5.get(r2)
            boolean r0 = r0 instanceof org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint
            if (r0 == 0) goto L_0x0012
            r0 = r1
            goto L_0x0013
        L_0x0012:
            r0 = r2
        L_0x0013:
            int r3 = r5.size()
            if (r3 == 0) goto L_0x0022
            java.lang.Object r3 = r5.get(r2)
            boolean r3 = r3 instanceof org.osmdroid.views.overlay.simplefastpoint.StyledLabelledGeoPoint
            if (r3 == 0) goto L_0x0022
            goto L_0x0023
        L_0x0022:
            r1 = r2
        L_0x0023:
            r4.<init>(r5, r0, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme.<init>(java.util.List):void");
    }

    public SimplePointTheme(List<IGeoPoint> list, boolean z) {
        this(list, z, false);
    }

    public SimplePointTheme(List<IGeoPoint> list, boolean z, boolean z2) {
        this.mPoints = list;
        this.mLabelled = z;
        this.mStyled = z2;
    }

    public int size() {
        return this.mPoints.size();
    }

    public IGeoPoint get(int i) {
        return this.mPoints.get(i);
    }

    public boolean isLabelled() {
        return this.mLabelled;
    }

    public boolean isStyled() {
        return this.mStyled;
    }

    public Iterator<IGeoPoint> iterator() {
        return this.mPoints.iterator();
    }
}
