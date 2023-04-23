package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class Eckert6Projection extends PseudoCylindricalProjection {
    private static final double C_x;
    private static final double C_y;
    private static final double LOOP_TOL = 1.0E-7d;
    private static final int MAX_ITER = 8;

    /* renamed from: n */
    private static final double f469n = 2.5707963267948966d;

    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Eckert VI";
    }

    static {
        double sqrt = Math.sqrt(0.7779690592966855d);
        C_y = sqrt;
        C_x = sqrt / 2.0d;
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double sin = Math.sin(d2) * f469n;
        int i = 8;
        while (i > 0) {
            double sin2 = ((Math.sin(d2) + d2) - sin) / (Math.cos(d2) + 1.0d);
            d2 -= sin2;
            if (Math.abs(sin2) < LOOP_TOL) {
                break;
            }
            i--;
        }
        if (i != 0) {
            projCoordinate.f409x = C_x * d * (Math.cos(d2) + 1.0d);
            projCoordinate.f410y = C_y * d2;
            return projCoordinate;
        }
        throw new ProjectionException("F_ERROR");
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d2 / C_y;
        projCoordinate.f410y = Math.asin((Math.sin(d3) + d3) / f469n);
        projCoordinate.f409x = d / (C_x * (Math.cos(d3) + 1.0d));
        return projCoordinate;
    }
}
