package org.osmdroid.util;

public class SegmentClipper implements PointAccepter {
    private final long[] cornerX = new long[4];
    private final long[] cornerY = new long[4];
    private int mCurrentSegmentIndex;
    private boolean mFirstPoint;
    private IntegerAccepter mIntegerAccepter;
    private final PointL mOptimIntersection = new PointL();
    private final PointL mOptimIntersection1 = new PointL();
    private final PointL mOptimIntersection2 = new PointL();
    private boolean mPathMode;
    private final PointL mPoint0 = new PointL();
    private final PointL mPoint1 = new PointL();
    private PointAccepter mPointAccepter;
    private long mXMax;
    private long mXMin;
    private long mYMax;
    private long mYMin;

    private static long clip(long j, long j2, long j3) {
        return j <= j2 ? j2 : j >= j3 ? j3 : j;
    }

    public void set(long j, long j2, long j3, long j4, PointAccepter pointAccepter, IntegerAccepter integerAccepter, boolean z) {
        this.mXMin = j;
        this.mYMin = j2;
        this.mXMax = j3;
        this.mYMax = j4;
        long[] jArr = this.cornerX;
        jArr[1] = j;
        jArr[0] = j;
        jArr[3] = j3;
        jArr[2] = j3;
        long[] jArr2 = this.cornerY;
        jArr2[2] = j2;
        jArr2[0] = j2;
        jArr2[3] = j4;
        jArr2[1] = j4;
        this.mPointAccepter = pointAccepter;
        this.mIntegerAccepter = integerAccepter;
        this.mPathMode = z;
    }

    public void set(long j, long j2, long j3, long j4, PointAccepter pointAccepter, boolean z) {
        set(j, j2, j3, j4, pointAccepter, (IntegerAccepter) null, z);
    }

    public void init() {
        this.mFirstPoint = true;
        IntegerAccepter integerAccepter = this.mIntegerAccepter;
        if (integerAccepter != null) {
            integerAccepter.init();
        }
        this.mPointAccepter.init();
    }

    public void add(long j, long j2) {
        this.mPoint1.set(j, j2);
        if (this.mFirstPoint) {
            this.mFirstPoint = false;
            this.mCurrentSegmentIndex = 0;
        } else {
            clip(this.mPoint0.f559x, this.mPoint0.f560y, this.mPoint1.f559x, this.mPoint1.f560y);
            this.mCurrentSegmentIndex++;
        }
        this.mPoint0.set(this.mPoint1);
    }

    public void end() {
        IntegerAccepter integerAccepter = this.mIntegerAccepter;
        if (integerAccepter != null) {
            integerAccepter.end();
        }
        this.mPointAccepter.end();
    }

    public void clip(long j, long j2, long j3, long j4) {
        SegmentClipper segmentClipper;
        SegmentClipper segmentClipper2;
        SegmentClipper segmentClipper3;
        int i;
        long j5 = j2;
        long j6 = j3;
        long j7 = j4;
        if (!this.mPathMode && isOnTheSameSideOut(j, j2, j3, j4)) {
            return;
        }
        if (isInClipArea(j, j2)) {
            if (isInClipArea(j6, j7)) {
                nextVertex(j, j2);
                nextVertex(j6, j7);
            } else if (intersection(j, j2, j3, j4)) {
                nextVertex(j, j2);
                nextVertex(this.mOptimIntersection.f559x, this.mOptimIntersection.f560y);
                if (this.mPathMode) {
                    nextVertex(clipX(j6), clipY(j7));
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot find expected mOptimIntersection for ");
                RectL rectL = r8;
                StringBuilder sb2 = sb;
                RectL rectL2 = new RectL(j, j2, j3, j4);
                sb2.append(rectL);
                throw new RuntimeException(sb2.toString());
            }
        } else if (!isInClipArea(j6, j7)) {
            int i2 = 0;
            long j8 = this.mXMin;
            if (intersection(j, j2, j3, j4, j8, this.mYMin, j8, this.mYMax)) {
                this.mOptimIntersection1.set(this.mOptimIntersection);
                i2 = 1;
            }
            long j9 = this.mXMax;
            if (intersection(j, j2, j3, j4, j9, this.mYMin, j9, this.mYMax)) {
                int i3 = i2 + 1;
                segmentClipper = this;
                (i2 == 0 ? segmentClipper.mOptimIntersection1 : segmentClipper.mOptimIntersection2).set(segmentClipper.mOptimIntersection);
                i2 = i3;
            } else {
                segmentClipper = this;
            }
            long j10 = segmentClipper.mXMin;
            long j11 = segmentClipper.mYMin;
            if (intersection(j, j2, j3, j4, j10, j11, segmentClipper.mXMax, j11)) {
                int i4 = i2 + 1;
                segmentClipper2 = this;
                (i2 == 0 ? segmentClipper2.mOptimIntersection1 : segmentClipper2.mOptimIntersection2).set(segmentClipper2.mOptimIntersection);
                i2 = i4;
            } else {
                segmentClipper2 = this;
            }
            long j12 = segmentClipper2.mXMin;
            long j13 = segmentClipper2.mYMax;
            if (intersection(j, j2, j3, j4, j12, j13, segmentClipper2.mXMax, j13)) {
                i = i2 + 1;
                segmentClipper3 = this;
                (i2 == 0 ? segmentClipper3.mOptimIntersection1 : segmentClipper3.mOptimIntersection2).set(segmentClipper3.mOptimIntersection);
            } else {
                segmentClipper3 = this;
                i = i2;
            }
            if (i == 2) {
                double d = (double) j;
                long j14 = j2;
                double d2 = (double) j14;
                long j15 = j14;
                int i5 = (Distance.getSquaredDistanceToPoint((double) segmentClipper3.mOptimIntersection1.f559x, (double) segmentClipper3.mOptimIntersection1.f560y, d, d2) > Distance.getSquaredDistanceToPoint((double) segmentClipper3.mOptimIntersection2.f559x, (double) segmentClipper3.mOptimIntersection2.f560y, d, d2) ? 1 : (Distance.getSquaredDistanceToPoint((double) segmentClipper3.mOptimIntersection1.f559x, (double) segmentClipper3.mOptimIntersection1.f560y, d, d2) == Distance.getSquaredDistanceToPoint((double) segmentClipper3.mOptimIntersection2.f559x, (double) segmentClipper3.mOptimIntersection2.f560y, d, d2) ? 0 : -1));
                PointL pointL = i5 < 0 ? segmentClipper3.mOptimIntersection1 : segmentClipper3.mOptimIntersection2;
                PointL pointL2 = i5 < 0 ? segmentClipper3.mOptimIntersection2 : segmentClipper3.mOptimIntersection1;
                if (segmentClipper3.mPathMode) {
                    segmentClipper3.nextVertex(clipX(j), segmentClipper3.clipY(j15));
                }
                segmentClipper3.nextVertex(pointL.f559x, pointL.f560y);
                segmentClipper3.nextVertex(pointL2.f559x, pointL2.f560y);
                if (segmentClipper3.mPathMode) {
                    segmentClipper3.nextVertex(segmentClipper3.clipX(j3), segmentClipper3.clipY(j4));
                    return;
                }
                return;
            }
            long j16 = j;
            long j17 = j2;
            long j18 = j3;
            long j19 = j4;
            if (i == 1) {
                if (segmentClipper3.mPathMode) {
                    segmentClipper3.nextVertex(clipX(j), segmentClipper3.clipY(j17));
                    segmentClipper3.nextVertex(segmentClipper3.mOptimIntersection1.f559x, segmentClipper3.mOptimIntersection1.f560y);
                    segmentClipper3.nextVertex(segmentClipper3.clipX(j18), segmentClipper3.clipY(j19));
                }
            } else if (i != 0) {
                throw new RuntimeException("Impossible mOptimIntersection count (" + i + ")");
            } else if (segmentClipper3.mPathMode) {
                segmentClipper3.nextVertex(clipX(j), segmentClipper3.clipY(j17));
                int closestCorner = getClosestCorner(j, j2, j3, j4);
                segmentClipper3.nextVertex(segmentClipper3.cornerX[closestCorner], segmentClipper3.cornerY[closestCorner]);
                segmentClipper3.nextVertex(segmentClipper3.clipX(j18), segmentClipper3.clipY(j19));
            }
        } else if (intersection(j, j2, j3, j4)) {
            if (this.mPathMode) {
                nextVertex(clipX(j), clipY(j5));
            }
            nextVertex(this.mOptimIntersection.f559x, this.mOptimIntersection.f560y);
            nextVertex(j6, j7);
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Cannot find expected mOptimIntersection for ");
            RectL rectL3 = r8;
            StringBuilder sb4 = sb3;
            RectL rectL4 = new RectL(j, j2, j3, j4);
            sb4.append(rectL3);
            throw new RuntimeException(sb4.toString());
        }
    }

    public boolean isInClipArea(long j, long j2) {
        return j > this.mXMin && j < this.mXMax && j2 > this.mYMin && j2 < this.mYMax;
    }

    private long clipX(long j) {
        return clip(j, this.mXMin, this.mXMax);
    }

    private long clipY(long j) {
        return clip(j, this.mYMin, this.mYMax);
    }

    private void nextVertex(long j, long j2) {
        IntegerAccepter integerAccepter = this.mIntegerAccepter;
        if (integerAccepter != null) {
            integerAccepter.add(this.mCurrentSegmentIndex);
        }
        this.mPointAccepter.add(j, j2);
    }

    private boolean intersection(long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8) {
        return SegmentIntersection.intersection((double) j, (double) j2, (double) j3, (double) j4, (double) j5, (double) j6, (double) j7, (double) j8, this.mOptimIntersection);
    }

    private boolean intersection(long j, long j2, long j3, long j4) {
        long j5 = this.mXMin;
        if (!intersection(j, j2, j3, j4, j5, this.mYMin, j5, this.mYMax)) {
            long j6 = this.mXMax;
            if (!intersection(j, j2, j3, j4, j6, this.mYMin, j6, this.mYMax)) {
                long j7 = this.mXMin;
                long j8 = this.mYMin;
                if (!intersection(j, j2, j3, j4, j7, j8, this.mXMax, j8)) {
                    long j9 = this.mXMin;
                    long j10 = this.mYMax;
                    return intersection(j, j2, j3, j4, j9, j10, this.mXMax, j10);
                }
            }
        }
    }

    private int getClosestCorner(long j, long j2, long j3, long j4) {
        SegmentClipper segmentClipper = this;
        int i = 0;
        double d = Double.MAX_VALUE;
        int i2 = 0;
        while (true) {
            long[] jArr = segmentClipper.cornerX;
            if (i >= jArr.length) {
                return i2;
            }
            int i3 = i;
            int i4 = i2;
            double d2 = d;
            double squaredDistanceToSegment = Distance.getSquaredDistanceToSegment((double) jArr[i], (double) segmentClipper.cornerY[i], (double) j, (double) j2, (double) j3, (double) j4);
            if (d2 > squaredDistanceToSegment) {
                d = squaredDistanceToSegment;
                i2 = i3;
            } else {
                i2 = i4;
                d = d2;
            }
            segmentClipper = this;
            i = i3 + 1;
        }
    }

    private boolean isOnTheSameSideOut(long j, long j2, long j3, long j4) {
        long j5 = this.mXMin;
        if (j >= j5 || j3 >= j5) {
            long j6 = this.mXMax;
            if (j <= j6 || j3 <= j6) {
                long j7 = this.mYMin;
                if (j2 >= j7 || j4 >= j7) {
                    long j8 = this.mYMax;
                    if (j2 <= j8 || j4 <= j8) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
