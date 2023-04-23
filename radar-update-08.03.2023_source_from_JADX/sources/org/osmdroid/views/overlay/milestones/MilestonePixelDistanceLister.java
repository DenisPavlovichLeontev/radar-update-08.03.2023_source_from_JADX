package org.osmdroid.views.overlay.milestones;

import org.osmdroid.util.Distance;

public class MilestonePixelDistanceLister extends MilestoneLister {
    private double mDistance;
    private final double mNbPixelsInit;
    private final double mNbPixelsRecurrence;

    public MilestonePixelDistanceLister(double d, double d2) {
        this.mNbPixelsInit = d;
        this.mNbPixelsRecurrence = d2;
    }

    public void init() {
        super.init();
        this.mDistance = this.mNbPixelsRecurrence - this.mNbPixelsInit;
    }

    /* access modifiers changed from: protected */
    public void add(long j, long j2, long j3, long j4) {
        double d = (double) j;
        double d2 = (double) j2;
        double d3 = (double) j3;
        double d4 = d2;
        double sqrt = Math.sqrt(Distance.getSquaredDistanceToPoint(d, d2, d3, (double) j4));
        if (sqrt != 0.0d) {
            double orientation = getOrientation(j, j2, j3, j4);
            double d5 = d4;
            while (true) {
                double floor = Math.floor(this.mDistance / this.mNbPixelsRecurrence);
                double d6 = this.mNbPixelsRecurrence;
                double d7 = (floor * d6) + d6;
                double d8 = this.mDistance;
                double d9 = d7 - d8;
                if (sqrt < d9) {
                    this.mDistance = d8 + sqrt;
                    return;
                }
                this.mDistance = d8 + d9;
                double d10 = 0.017453292519943295d * orientation;
                d += Math.cos(d10) * d9;
                d5 += d9 * Math.sin(d10);
                add(new MilestoneStep((long) d, (long) d5, orientation, Double.valueOf(this.mDistance)));
                sqrt -= d9;
            }
        }
    }
}
