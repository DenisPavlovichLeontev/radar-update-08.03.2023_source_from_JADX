package org.osmdroid.util;

public class SpeechBalloonHelper {
    public static final int CORNER_BOTTOM = 8;
    public static final int CORNER_INSIDE = -1;
    public static final int CORNER_LEFT = 1;
    public static final int CORNER_NONE = 0;
    public static final int CORNER_RIGHT = 2;
    public static final int CORNER_TOP = 4;
    private PointL mPoint;
    private RectL mRect;
    private final PointL mTrianglePoint = new PointL();

    public int compute(RectL rectL, PointL pointL, double d, PointL pointL2, PointL pointL3) {
        RectL rectL2 = rectL;
        PointL pointL4 = pointL;
        this.mRect = rectL2;
        this.mPoint = pointL4;
        if (rectL2.contains(pointL4.f559x, this.mPoint.f560y)) {
            return -1;
        }
        double d2 = d;
        double computeAngle = MyMath.computeAngle(this.mRect.centerX(), this.mRect.centerY(), this.mPoint.f559x, this.mPoint.f560y);
        computeCirclePoint(this.mTrianglePoint, d2, computeAngle, false);
        int checkIntersection = checkIntersection(pointL2);
        computeCirclePoint(this.mTrianglePoint, d2, computeAngle, true);
        int checkIntersection2 = checkIntersection(pointL3);
        if (checkIntersection == checkIntersection2) {
            return 0;
        }
        return checkIntersection2 | checkIntersection;
    }

    private int checkIntersection(PointL pointL) {
        if (this.mPoint.f560y <= this.mRect.top && checkIntersectionY(this.mRect.top, pointL)) {
            return 4;
        }
        if (this.mPoint.f560y >= this.mRect.bottom && checkIntersectionY(this.mRect.bottom, pointL)) {
            return 8;
        }
        if (this.mPoint.f559x <= this.mRect.left && checkIntersectionX(this.mRect.left, pointL)) {
            return 1;
        }
        if (this.mPoint.f559x >= this.mRect.right && checkIntersectionX(this.mRect.right, pointL)) {
            return 2;
        }
        throw new IllegalArgumentException();
    }

    private boolean checkIntersectionX(long j, PointL pointL) {
        double d = (double) this.mPoint.f560y;
        double d2 = (double) this.mTrianglePoint.f559x;
        double d3 = (double) this.mTrianglePoint.f560y;
        double d4 = (double) j;
        double d5 = d4;
        double d6 = (double) this.mRect.top;
        double d7 = (double) this.mRect.bottom;
        return SegmentIntersection.intersection((double) this.mPoint.f559x, d, d2, d3, d5, d6, d4, d7, pointL);
    }

    private boolean checkIntersectionY(long j, PointL pointL) {
        double d = (double) j;
        double d2 = d;
        return SegmentIntersection.intersection((double) this.mPoint.f559x, (double) this.mPoint.f560y, (double) this.mTrianglePoint.f559x, (double) this.mTrianglePoint.f560y, (double) this.mRect.left, d, (double) this.mRect.right, d2, pointL);
    }

    private void computeCirclePoint(PointL pointL, double d, double d2, boolean z) {
        MyMath.computeCirclePoint(this.mRect.centerX(), this.mRect.centerY(), d, d2 + (((double) (z ? 1 : -1)) * 1.5707963267948966d), pointL);
    }
}
