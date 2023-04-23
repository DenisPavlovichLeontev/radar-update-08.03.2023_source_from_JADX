package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class MolleweideProjection extends PseudoCylindricalProjection {
    private static final int MAX_ITER = 10;
    public static final int MOLLEWEIDE = 0;
    private static final double TOLERANCE = 1.0E-7d;
    public static final int WAGNER4 = 1;
    public static final int WAGNER5 = 2;

    /* renamed from: cp */
    private double f522cp;

    /* renamed from: cx */
    private double f523cx;

    /* renamed from: cy */
    private double f524cy;
    private int type;

    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return true;
    }

    public MolleweideProjection() {
        this(1.5707963267948966d);
    }

    public MolleweideProjection(int i) {
        this.type = i;
        if (i == 0) {
            init(1.5707963267948966d);
        } else if (i == 1) {
            init(1.0471975511965976d);
        } else if (i == 2) {
            init(1.5707963267948966d);
            this.f523cx = 0.90977d;
            this.f524cy = 1.65014d;
            this.f522cp = 3.00896d;
        }
    }

    public MolleweideProjection(double d) {
        this.type = 0;
        init(d);
    }

    public void init(double d) {
        double d2 = d + d;
        double sin = Math.sin(d);
        double sqrt = Math.sqrt((6.283185307179586d * sin) / (Math.sin(d2) + d2));
        this.f523cx = (2.0d * sqrt) / 3.141592653589793d;
        this.f524cy = sqrt / sin;
        this.f522cp = d2 + Math.sin(d2);
    }

    public MolleweideProjection(double d, double d2, double d3) {
        this.type = 0;
        this.f523cx = d;
        this.f524cy = d2;
        this.f522cp = d3;
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double sin = this.f522cp * Math.sin(d2);
        int i = 10;
        while (i != 0) {
            double sin2 = ((Math.sin(d2) + d2) - sin) / (Math.cos(d2) + 1.0d);
            d2 -= sin2;
            if (Math.abs(sin2) < TOLERANCE) {
                break;
            }
            i--;
        }
        double d3 = i == 0 ? d2 < 0.0d ? -1.5707963267948966d : 1.5707963267948966d : d2 * 0.5d;
        projCoordinate.f409x = this.f523cx * d * Math.cos(d3);
        projCoordinate.f410y = this.f524cy * Math.sin(d3);
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double asin = Math.asin(d2 / this.f524cy);
        double cos = d / (this.f523cx * Math.cos(asin));
        double d3 = asin + asin;
        double asin2 = Math.asin((d3 + Math.sin(d3)) / this.f522cp);
        projCoordinate.f409x = cos;
        projCoordinate.f410y = asin2;
        return projCoordinate;
    }

    public String toString() {
        int i = this.type;
        if (i != 1) {
            return i != 2 ? "Molleweide" : "Wagner V";
        }
        return "Wagner IV";
    }
}
