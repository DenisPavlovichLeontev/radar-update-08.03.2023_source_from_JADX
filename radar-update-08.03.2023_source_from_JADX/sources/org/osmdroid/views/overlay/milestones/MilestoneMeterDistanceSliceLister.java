package org.osmdroid.views.overlay.milestones;

import org.osmdroid.util.Distance;

public class MilestoneMeterDistanceSliceLister extends MilestoneLister {
    private double mDistance;
    private int mIndex;
    private double mNbMetersEnd;
    private double mNbMetersStart;
    private Step mStep;

    private enum Step {
        STEP_INIT,
        STEP_STARTED,
        STEP_ENDED
    }

    public void setMeterDistanceSlice(double d, double d2) {
        this.mNbMetersStart = d;
        this.mNbMetersEnd = d2;
    }

    public void init() {
        super.init();
        this.mDistance = 0.0d;
        this.mIndex = 0;
        this.mStep = Step.STEP_INIT;
    }

    /* access modifiers changed from: protected */
    public void add(long j, long j2, long j3, long j4) {
        double d;
        if (this.mStep != Step.STEP_ENDED) {
            int i = this.mIndex + 1;
            this.mIndex = i;
            double distance = getDistance(i);
            if (distance != 0.0d) {
                double d2 = (double) j;
                double d3 = (double) j2;
                double d4 = d3;
                double sqrt = Math.sqrt(Distance.getSquaredDistanceToPoint(d2, d3, (double) j3, (double) j4)) / distance;
                double orientation = getOrientation(j, j2, j3, j4);
                if (this.mStep == Step.STEP_INIT) {
                    double d5 = this.mNbMetersStart;
                    double d6 = this.mDistance;
                    double d7 = d5 - d6;
                    if (d7 > distance) {
                        this.mDistance = d6 + distance;
                        return;
                    }
                    this.mStep = Step.STEP_STARTED;
                    this.mDistance += d7;
                    double d8 = distance - d7;
                    double d9 = orientation * 0.017453292519943295d;
                    d2 += Math.cos(d9) * d7 * sqrt;
                    d = d4 + (d7 * Math.sin(d9) * sqrt);
                    double d10 = d8;
                    add(new MilestoneStep((long) d2, (long) d, orientation, (Object) null));
                    if (this.mNbMetersStart == this.mNbMetersEnd) {
                        this.mStep = Step.STEP_ENDED;
                        return;
                    }
                    distance = d10;
                } else {
                    d = d4;
                }
                if (this.mStep == Step.STEP_STARTED) {
                    double d11 = this.mNbMetersEnd;
                    double d12 = this.mDistance;
                    double d13 = d11 - d12;
                    if (d13 > distance) {
                        this.mDistance = d12 + distance;
                        add(new MilestoneStep(j3, j4, orientation, (Object) null));
                        return;
                    }
                    this.mStep = Step.STEP_ENDED;
                    double d14 = orientation * 0.017453292519943295d;
                    add(new MilestoneStep((long) (d2 + (Math.cos(d14) * d13 * sqrt)), (long) (d + (d13 * Math.sin(d14) * sqrt)), orientation, (Object) null));
                }
            }
        }
    }
}
