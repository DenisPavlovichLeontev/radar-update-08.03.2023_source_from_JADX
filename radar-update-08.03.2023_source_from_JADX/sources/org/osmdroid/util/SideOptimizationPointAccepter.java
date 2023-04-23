package org.osmdroid.util;

public class SideOptimizationPointAccepter implements PointAccepter {
    private static final int STATUS_DIFFERENT = 0;
    private static final int STATUS_SAME_X = 1;
    private static final int STATUS_SAME_Y = 2;
    private boolean mFirst;
    private final PointL mLatestPoint = new PointL();
    private long mMax;
    private long mMin;
    private final PointAccepter mPointAccepter;
    private final PointL mStartPoint = new PointL();
    private int mStatus;

    public SideOptimizationPointAccepter(PointAccepter pointAccepter) {
        this.mPointAccepter = pointAccepter;
    }

    public void init() {
        this.mFirst = true;
        this.mStatus = 0;
        this.mPointAccepter.init();
    }

    public void add(long j, long j2) {
        if (this.mFirst) {
            this.mFirst = false;
            addToAccepter(j, j2);
            this.mLatestPoint.set(j, j2);
        } else if (this.mLatestPoint.f559x != j || this.mLatestPoint.f560y != j2) {
            if (this.mLatestPoint.f559x == j) {
                if (this.mStatus == 1) {
                    if (this.mMin > j2) {
                        this.mMin = j2;
                    }
                    if (this.mMax < j2) {
                        this.mMax = j2;
                    }
                } else {
                    flushSides();
                    this.mStatus = 1;
                    this.mStartPoint.set(this.mLatestPoint);
                    this.mMin = Math.min(j2, this.mLatestPoint.f560y);
                    this.mMax = Math.max(j2, this.mLatestPoint.f560y);
                }
            } else if (this.mLatestPoint.f560y != j2) {
                flushSides();
                addToAccepter(j, j2);
            } else if (this.mStatus == 2) {
                if (this.mMin > j) {
                    this.mMin = j;
                }
                if (this.mMax < j) {
                    this.mMax = j;
                }
            } else {
                flushSides();
                this.mStatus = 2;
                this.mStartPoint.set(this.mLatestPoint);
                this.mMin = Math.min(j, this.mLatestPoint.f559x);
                this.mMax = Math.max(j, this.mLatestPoint.f559x);
            }
            this.mLatestPoint.set(j, j2);
        }
    }

    public void end() {
        flushSides();
        this.mPointAccepter.end();
    }

    private void flushSides() {
        long j;
        long j2;
        long j3;
        long j4;
        int i = this.mStatus;
        if (i == 1) {
            long j5 = this.mStartPoint.f559x;
            if (this.mStartPoint.f560y <= this.mLatestPoint.f560y) {
                j2 = this.mStartPoint.f560y;
                j = this.mLatestPoint.f560y;
            } else {
                j2 = this.mLatestPoint.f560y;
                j = this.mStartPoint.f560y;
            }
            long j6 = this.mMin;
            if (j6 < j2) {
                addToAccepter(j5, j6);
            }
            long j7 = this.mMax;
            if (j7 > j) {
                addToAccepter(j5, j7);
            }
            addToAccepter(j5, this.mLatestPoint.f560y);
        } else if (i == 2) {
            long j8 = this.mStartPoint.f560y;
            if (this.mStartPoint.f559x <= this.mLatestPoint.f559x) {
                j4 = this.mStartPoint.f559x;
                j3 = this.mLatestPoint.f559x;
            } else {
                j4 = this.mLatestPoint.f559x;
                j3 = this.mStartPoint.f559x;
            }
            long j9 = this.mMin;
            if (j9 < j4) {
                addToAccepter(j9, j8);
            }
            long j10 = this.mMax;
            if (j10 > j3) {
                addToAccepter(j10, j8);
            }
            addToAccepter(this.mLatestPoint.f559x, j8);
        }
        this.mStatus = 0;
    }

    private void addToAccepter(long j, long j2) {
        this.mPointAccepter.add(j, j2);
    }
}
