package org.mapsforge.core.model;

import java.io.Serializable;

public class Point implements Comparable<Point>, Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: x */
    public final double f381x;

    /* renamed from: y */
    public final double f382y;

    public Point(double d, double d2) {
        this.f381x = d;
        this.f382y = d2;
    }

    public int compareTo(Point point) {
        double d = this.f381x;
        double d2 = point.f381x;
        if (d > d2) {
            return 1;
        }
        if (d < d2) {
            return -1;
        }
        double d3 = this.f382y;
        double d4 = point.f382y;
        if (d3 > d4) {
            return 1;
        }
        if (d3 < d4) {
            return -1;
        }
        return 0;
    }

    public double distance(Point point) {
        return Math.hypot(this.f381x - point.f381x, this.f382y - point.f382y);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Point)) {
            return false;
        }
        Point point = (Point) obj;
        return Double.doubleToLongBits(this.f381x) == Double.doubleToLongBits(point.f381x) && Double.doubleToLongBits(this.f382y) == Double.doubleToLongBits(point.f382y);
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.f381x);
        long doubleToLongBits2 = Double.doubleToLongBits(this.f382y);
        return ((((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 31) * 31) + ((int) ((doubleToLongBits2 >>> 32) ^ doubleToLongBits2));
    }

    public Point offset(double d, double d2) {
        return (0.0d == d && 0.0d == d2) ? this : new Point(this.f381x + d, this.f382y + d2);
    }

    public String toString() {
        return "x=" + this.f381x + ", y=" + this.f382y;
    }
}
