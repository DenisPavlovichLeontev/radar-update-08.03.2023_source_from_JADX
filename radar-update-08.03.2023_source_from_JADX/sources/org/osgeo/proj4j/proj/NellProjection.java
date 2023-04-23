package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.util.ProjectionMath;

public class NellProjection extends Projection {
    private static final double LOOP_TOL = 1.0E-7d;
    private static final int MAX_ITER = 10;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Nell";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d2;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double sin = Math.sin(d2) * 2.0d;
        double d4 = d3 * d3;
        projCoordinate2.f410y *= (d4 * ((-0.011412d * d4) - 48.1084416d)) + 1.00371d;
        for (int i = 10; i > 0; i--) {
            double d5 = projCoordinate2.f410y;
            double sin2 = ((Math.sin(d2) + d3) - sin) / (Math.cos(d2) + 1.0d);
            projCoordinate2.f410y = d5 - sin2;
            if (Math.abs(sin2) < LOOP_TOL) {
                break;
            }
        }
        projCoordinate2.f409x = 0.5d * d * (Math.cos(d2) + 1.0d);
        projCoordinate2.f410y = d3;
        return projCoordinate2;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = (d * 2.0d) / (Math.cos(d2) + 1.0d);
        projCoordinate.f410y = ProjectionMath.asin((d2 + Math.sin(d2)) * 0.5d);
        return projCoordinate;
    }
}
