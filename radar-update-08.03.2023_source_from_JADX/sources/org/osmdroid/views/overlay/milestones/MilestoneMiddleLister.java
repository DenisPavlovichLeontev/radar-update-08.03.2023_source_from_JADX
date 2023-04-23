package org.osmdroid.views.overlay.milestones;

import org.osmdroid.util.Distance;

public class MilestoneMiddleLister extends MilestoneLister {
    private final double mMinimumSquaredPixelDistance;

    public MilestoneMiddleLister(double d) {
        this.mMinimumSquaredPixelDistance = d * d;
    }

    /* access modifiers changed from: protected */
    public void add(long j, long j2, long j3, long j4) {
        long j5 = j;
        long j6 = j3;
        long j7 = j4;
        if (Distance.getSquaredDistanceToPoint((double) j5, (double) j2, (double) j6, (double) j7) > this.mMinimumSquaredPixelDistance) {
            add(new MilestoneStep((j5 + j6) / 2, (j2 + j7) / 2, getOrientation(j, j2, j3, j4)));
        }
    }
}
