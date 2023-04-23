package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.util.ProjectionMath;

public class Wagner2Projection extends Projection {
    private static final double C_p1 = 0.88022d;
    private static final double C_p2 = 0.8855d;
    private static final double C_x = 0.92483d;
    private static final double C_y = 1.38725d;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Wagner II";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = ProjectionMath.asin(Math.sin(C_p2 * d2) * C_p1);
        projCoordinate.f409x = d * C_x * Math.cos(d2);
        projCoordinate.f410y = d2 * C_y;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = d2 / C_y;
        projCoordinate.f409x = d / (Math.cos(projCoordinate.f410y) * C_x);
        projCoordinate.f410y = ProjectionMath.asin(Math.sin(projCoordinate.f410y) / C_p1) / C_p2;
        return projCoordinate;
    }
}
