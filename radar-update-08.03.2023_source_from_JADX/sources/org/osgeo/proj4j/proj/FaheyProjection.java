package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class FaheyProjection extends Projection {
    private static final double TOL = 1.0E-6d;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Fahey";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double tan = Math.tan(d2 * 0.5d);
        projCoordinate.f409x = tan;
        projCoordinate.f410y = tan * 1.819152d;
        projCoordinate.f409x = d * 0.819152d * asqrt(1.0d - (projCoordinate.f409x * projCoordinate.f409x));
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = projCoordinate.f410y / 1.819152d;
        projCoordinate.f410y = d3;
        projCoordinate.f410y = Math.atan(d3) * 2.0d;
        double d4 = 1.0d - (d2 * d2);
        projCoordinate.f410y = d4;
        projCoordinate.f409x = Math.abs(d4) < TOL ? 0.0d : d / (Math.sqrt(d2) * 0.819152d);
        return projCoordinate;
    }

    private double asqrt(double d) {
        if (d <= 0.0d) {
            return 0.0d;
        }
        return Math.sqrt(d);
    }
}
