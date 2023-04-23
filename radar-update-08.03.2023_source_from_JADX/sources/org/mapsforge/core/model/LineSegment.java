package org.mapsforge.core.model;

public final class LineSegment {
    private static int BOTTOM = 4;
    private static int INSIDE = 0;
    private static int LEFT = 1;
    private static int RIGHT = 2;
    private static int TOP = 8;
    public final Point end;
    public final Point start;

    public LineSegment(Point point, Point point2) {
        this.start = point;
        this.end = point2;
    }

    public LineSegment(Point point, Point point2, double d) {
        this.start = point;
        this.end = new LineSegment(point, point2).pointAlongLineSegment(d);
    }

    public LineSegment clipToRectangle(Rectangle rectangle) {
        double d;
        double d2;
        Point point = this.start;
        Point point2 = this.end;
        int code = code(rectangle, point);
        int code2 = code(rectangle, point2);
        while ((code | code2) != 0) {
            if ((code & code2) != 0) {
                return null;
            }
            int i = code != 0 ? code : code2;
            if ((TOP & i) != 0) {
                d2 = point.f381x + (((point2.f381x - point.f381x) * (rectangle.top - point.f382y)) / (point2.f382y - point.f382y));
                d = rectangle.top;
            } else if ((BOTTOM & i) != 0) {
                d2 = point.f381x + (((point2.f381x - point.f381x) * (rectangle.bottom - point.f382y)) / (point2.f382y - point.f382y));
                d = rectangle.bottom;
            } else if ((RIGHT & i) != 0) {
                d = (((point2.f382y - point.f382y) * (rectangle.right - point.f381x)) / (point2.f381x - point.f381x)) + point.f382y;
                d2 = rectangle.right;
            } else if ((LEFT & i) != 0) {
                d = (((point2.f382y - point.f382y) * (rectangle.left - point.f381x)) / (point2.f381x - point.f381x)) + point.f382y;
                d2 = rectangle.left;
            } else {
                throw new IllegalStateException("Should not get here");
            }
            if (i == code) {
                point = new Point(d2, d);
                code = code(rectangle, point);
            } else {
                point2 = new Point(d2, d);
                code2 = code(rectangle, point2);
            }
        }
        return new LineSegment(point, point2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LineSegment)) {
            return false;
        }
        LineSegment lineSegment = (LineSegment) obj;
        return lineSegment.start.equals(this.start) && lineSegment.end.equals(this.end);
    }

    public int hashCode() {
        return ((this.start.hashCode() + 31) * 31) + this.end.hashCode();
    }

    public boolean intersectsRectangle(Rectangle rectangle, boolean z) {
        int code = code(rectangle, this.start);
        int code2 = code(rectangle, this.end);
        if ((code | code2) == 0) {
            return true;
        }
        if ((code2 & code) != 0) {
            return false;
        }
        return z;
    }

    public double length() {
        return this.start.distance(this.end);
    }

    public Point pointAlongLineSegment(double d) {
        if (this.start.f381x != this.end.f381x) {
            double d2 = (this.end.f382y - this.start.f382y) / (this.end.f381x - this.start.f381x);
            double sqrt = Math.sqrt((d * d) / ((d2 * d2) + 1.0d));
            if (this.end.f381x < this.start.f381x) {
                sqrt *= -1.0d;
            }
            return new Point(this.start.f381x + sqrt, this.start.f382y + (d2 * sqrt));
        } else if (this.start.f382y > this.end.f382y) {
            return new Point(this.end.f381x, this.end.f382y + d);
        } else {
            return new Point(this.start.f381x, this.start.f382y + d);
        }
    }

    public LineSegment reverse() {
        return new LineSegment(this.end, this.start);
    }

    public LineSegment subSegment(double d, double d2) {
        return new LineSegment(pointAlongLineSegment(d), pointAlongLineSegment(d + d2));
    }

    public String toString() {
        return this.start + " " + this.end;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0021  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int code(org.mapsforge.core.model.Rectangle r5, org.mapsforge.core.model.Point r6) {
        /*
            int r0 = INSIDE
            double r1 = r6.f381x
            double r3 = r5.left
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 >= 0) goto L_0x000e
            int r1 = LEFT
        L_0x000c:
            r0 = r0 | r1
            goto L_0x0019
        L_0x000e:
            double r1 = r6.f381x
            double r3 = r5.right
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 <= 0) goto L_0x0019
            int r1 = RIGHT
            goto L_0x000c
        L_0x0019:
            double r1 = r6.f382y
            double r3 = r5.bottom
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 <= 0) goto L_0x0025
            int r5 = BOTTOM
        L_0x0023:
            r0 = r0 | r5
            goto L_0x0030
        L_0x0025:
            double r1 = r6.f382y
            double r5 = r5.top
            int r5 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r5 >= 0) goto L_0x0030
            int r5 = TOP
            goto L_0x0023
        L_0x0030:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.core.model.LineSegment.code(org.mapsforge.core.model.Rectangle, org.mapsforge.core.model.Point):int");
    }
}
