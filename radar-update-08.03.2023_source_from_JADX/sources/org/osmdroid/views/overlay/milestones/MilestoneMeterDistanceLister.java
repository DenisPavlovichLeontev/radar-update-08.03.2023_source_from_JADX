package org.osmdroid.views.overlay.milestones;

import org.osmdroid.util.Distance;

public class MilestoneMeterDistanceLister extends MilestoneLister {
    private double mDistance;
    private int mIndex;
    private final double[] mMilestoneMeters;
    private int mMilestoneMetersIndex;
    private final double mNbMetersRecurrence;
    private double mNeededForNext;
    private double mSideEffectLastEpsilon;
    private boolean mSideEffectLastFlag;
    private double mSideEffectLastOrientation;
    private long mSideEffectLastX;
    private long mSideEffectLastY;

    public MilestoneMeterDistanceLister(double d) {
        this.mSideEffectLastEpsilon = 1.0E-5d;
        this.mNbMetersRecurrence = d;
        this.mMilestoneMeters = null;
    }

    public MilestoneMeterDistanceLister(double[] dArr) {
        this.mSideEffectLastEpsilon = 1.0E-5d;
        this.mNbMetersRecurrence = 0.0d;
        this.mMilestoneMeters = dArr;
    }

    public void init() {
        super.init();
        this.mDistance = 0.0d;
        this.mIndex = 0;
        if (this.mMilestoneMeters != null) {
            this.mMilestoneMetersIndex = 0;
        }
        this.mNeededForNext = getNewNeededForNext();
        this.mSideEffectLastFlag = false;
    }

    /* access modifiers changed from: protected */
    public void add(long j, long j2, long j3, long j4) {
        long j5 = j3;
        long j6 = j4;
        this.mSideEffectLastFlag = false;
        if (this.mNeededForNext != -1.0d) {
            int i = this.mIndex + 1;
            this.mIndex = i;
            double distance = getDistance(i);
            if (distance != 0.0d) {
                double d = (double) j;
                double d2 = (double) j2;
                double sqrt = Math.sqrt(Distance.getSquaredDistanceToPoint(d, d2, (double) j5, (double) j6)) / distance;
                double orientation = getOrientation(j, j2, j3, j4);
                while (true) {
                    double d3 = this.mNeededForNext;
                    if (distance < d3) {
                        this.mDistance += distance;
                        this.mNeededForNext = d3 - distance;
                        this.mSideEffectLastFlag = true;
                        this.mSideEffectLastX = j5;
                        this.mSideEffectLastY = j6;
                        this.mSideEffectLastOrientation = orientation;
                        return;
                    }
                    this.mDistance += d3;
                    double d4 = distance - d3;
                    double d5 = 0.017453292519943295d * orientation;
                    double cos = d + (d3 * Math.cos(d5) * sqrt);
                    double sin = d2 + (this.mNeededForNext * Math.sin(d5) * sqrt);
                    double d6 = cos;
                    d2 = sin;
                    add((long) cos, (long) sin, orientation);
                    double newNeededForNext = getNewNeededForNext();
                    this.mNeededForNext = newNeededForNext;
                    if (newNeededForNext != -1.0d) {
                        distance = d4;
                        d = d6;
                        j5 = j3;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    private double getNewNeededForNext() {
        double d;
        double[] dArr = this.mMilestoneMeters;
        if (dArr == null) {
            return this.mNbMetersRecurrence;
        }
        int i = this.mMilestoneMetersIndex;
        if (i >= dArr.length) {
            return -1.0d;
        }
        if (i == 0) {
            d = 0.0d;
        } else {
            d = dArr[i - 1];
        }
        this.mMilestoneMetersIndex = i + 1;
        double d2 = dArr[i] - d;
        if (d2 >= 0.0d) {
            return d2;
        }
        throw new IllegalArgumentException();
    }

    public void end() {
        if (this.mSideEffectLastFlag && this.mNeededForNext < this.mSideEffectLastEpsilon) {
            add(this.mSideEffectLastX, this.mSideEffectLastY, this.mSideEffectLastOrientation);
        }
        super.end();
    }

    public void setSideEffectLastEpsilon(double d) {
        this.mSideEffectLastEpsilon = d;
    }

    private void add(long j, long j2, double d) {
        add(new MilestoneStep(j, j2, d, Double.valueOf(this.mDistance)));
    }
}
