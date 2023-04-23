package org.osmdroid.gpkg.overlay.features;

import java.util.ArrayList;
import java.util.List;

public class MultiPolylineMarkers {
    private List<PolylineMarkers> polylineMarkers = new ArrayList();

    public void add(PolylineMarkers polylineMarkers2) {
        this.polylineMarkers.add(polylineMarkers2);
    }

    public List<PolylineMarkers> getPolylineMarkers() {
        return this.polylineMarkers;
    }

    public void setPolylineMarkers(List<PolylineMarkers> list) {
        this.polylineMarkers = list;
    }

    public void setVisible(boolean z) {
        for (PolylineMarkers visible : this.polylineMarkers) {
            visible.setVisible(z);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x0007 A[LOOP:0: B:1:0x0007->B:4:0x0017, LOOP_START, PHI: r1 
      PHI: (r1v1 boolean) = (r1v0 boolean), (r1v5 boolean) binds: [B:0:0x0000, B:4:0x0017] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isValid() {
        /*
            r3 = this;
            java.util.List<org.osmdroid.gpkg.overlay.features.PolylineMarkers> r0 = r3.polylineMarkers
            java.util.Iterator r0 = r0.iterator()
            r1 = 1
        L_0x0007:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0019
            java.lang.Object r1 = r0.next()
            org.osmdroid.gpkg.overlay.features.PolylineMarkers r1 = (org.osmdroid.gpkg.overlay.features.PolylineMarkers) r1
            boolean r1 = r1.isValid()
            if (r1 != 0) goto L_0x0007
        L_0x0019:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.gpkg.overlay.features.MultiPolylineMarkers.isValid():boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x0007 A[LOOP:0: B:1:0x0007->B:4:0x0017, LOOP_START, PHI: r1 
      PHI: (r1v1 boolean) = (r1v0 boolean), (r1v5 boolean) binds: [B:0:0x0000, B:4:0x0017] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isDeleted() {
        /*
            r3 = this;
            java.util.List<org.osmdroid.gpkg.overlay.features.PolylineMarkers> r0 = r3.polylineMarkers
            java.util.Iterator r0 = r0.iterator()
            r1 = 1
        L_0x0007:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x0019
            java.lang.Object r1 = r0.next()
            org.osmdroid.gpkg.overlay.features.PolylineMarkers r1 = (org.osmdroid.gpkg.overlay.features.PolylineMarkers) r1
            boolean r1 = r1.isDeleted()
            if (r1 != 0) goto L_0x0007
        L_0x0019:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.gpkg.overlay.features.MultiPolylineMarkers.isDeleted():boolean");
    }
}
