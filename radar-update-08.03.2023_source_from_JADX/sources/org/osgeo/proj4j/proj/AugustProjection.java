package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class AugustProjection extends Projection {

    /* renamed from: M */
    private static final double f434M = 1.333333333333333d;

    public boolean hasInverse() {
        return false;
    }

    public boolean isConformal() {
        return true;
    }

    public String toString() {
        return "August Epicycloidal";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double tan = Math.tan(d2 * 0.5d);
        double sqrt = Math.sqrt(1.0d - (tan * tan));
        double d3 = 0.5d * d;
        double cos = (Math.cos(d3) * sqrt) + 1.0d;
        double sin = (Math.sin(d3) * sqrt) / cos;
        double d4 = tan / cos;
        double d5 = sin * f434M;
        double d6 = sin * sin;
        double d7 = d4 * d4;
        projCoordinate2.f409x = d5 * ((d6 + 3.0d) - (d7 * 3.0d));
        projCoordinate2.f410y = d4 * f434M * (((d6 * 3.0d) + 3.0d) - d7);
        return projCoordinate2;
    }
}
