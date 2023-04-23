package org.osgeo.proj4j;

import java.text.DecimalFormat;

public class ProjCoordinate {
    public static DecimalFormat DECIMAL_FORMAT = new DecimalFormat(DECIMAL_FORMAT_PATTERN);
    public static String DECIMAL_FORMAT_PATTERN = "0.0###############";

    /* renamed from: x */
    public double f409x;

    /* renamed from: y */
    public double f410y;

    /* renamed from: z */
    public double f411z;

    public ProjCoordinate() {
        this(0.0d, 0.0d);
    }

    public ProjCoordinate(double d, double d2, double d3) {
        this.f409x = d;
        this.f410y = d2;
        this.f411z = d3;
    }

    public ProjCoordinate(double d, double d2) {
        this.f409x = d;
        this.f410y = d2;
        this.f411z = Double.NaN;
    }

    public ProjCoordinate(String str) {
        if (str.startsWith("ProjCoordinate: ")) {
            String substring = str.substring(16).substring(1);
            String[] split = substring.substring(0, substring.length() - 2).split(" ");
            if (split.length == 2 || split.length == 3) {
                this.f409x = Double.parseDouble(split[0]);
                this.f410y = Double.parseDouble(split[0]);
                if (split.length == 3) {
                    this.f411z = Double.parseDouble(split[0]);
                    return;
                }
                return;
            }
            throw new IllegalArgumentException("The input string was not in the proper format.");
        }
        throw new IllegalArgumentException("The input string was not in the proper format.");
    }

    public void setValue(ProjCoordinate projCoordinate) {
        this.f409x = projCoordinate.f409x;
        this.f410y = projCoordinate.f410y;
        this.f411z = projCoordinate.f411z;
    }

    public void setValue(double d, double d2) {
        this.f409x = d;
        this.f410y = d2;
        this.f411z = Double.NaN;
    }

    public void setValue(double d, double d2, double d3) {
        this.f409x = d;
        this.f410y = d2;
        this.f411z = d3;
    }

    public void clearZ() {
        this.f411z = Double.NaN;
    }

    public boolean areXOrdinatesEqual(ProjCoordinate projCoordinate, double d) {
        return projCoordinate.f409x - this.f409x <= d;
    }

    public boolean areYOrdinatesEqual(ProjCoordinate projCoordinate, double d) {
        return projCoordinate.f410y - this.f410y <= d;
    }

    public boolean areZOrdinatesEqual(ProjCoordinate projCoordinate, double d) {
        return Double.isNaN(this.f411z) ? Double.isNaN(projCoordinate.f411z) : !Double.isNaN(projCoordinate.f411z) && projCoordinate.f411z - this.f411z <= d;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ProjCoordinate)) {
            return false;
        }
        ProjCoordinate projCoordinate = (ProjCoordinate) obj;
        if (this.f409x == projCoordinate.f409x && this.f410y == projCoordinate.f410y) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((629 + hashCode(this.f409x)) * 37) + hashCode(this.f410y);
    }

    private static int hashCode(double d) {
        long doubleToLongBits = Double.doubleToLongBits(d);
        return (int) (doubleToLongBits ^ (doubleToLongBits >>> 32));
    }

    public String toString() {
        return "ProjCoordinate[" + this.f409x + " " + this.f410y + " " + this.f411z + "]";
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(DECIMAL_FORMAT.format(this.f409x));
        sb.append(", ");
        sb.append(DECIMAL_FORMAT.format(this.f410y));
        if (!Double.isNaN(this.f411z)) {
            sb.append(", ");
            sb.append(this.f411z);
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean hasValidZOrdinate() {
        return !Double.isNaN(this.f411z);
    }

    public boolean hasValidXandYOrdinates() {
        if (!Double.isNaN(this.f409x) && !Double.isInfinite(this.f409x) && !Double.isNaN(this.f410y) && !Double.isInfinite(this.f410y)) {
            return true;
        }
        return false;
    }
}
