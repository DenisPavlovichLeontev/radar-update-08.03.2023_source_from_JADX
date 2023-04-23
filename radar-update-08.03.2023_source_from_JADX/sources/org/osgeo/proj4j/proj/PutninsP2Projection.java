package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.util.ProjectionMath;

public class PutninsP2Projection extends Projection {
    private static final double C_p = 0.6141848493043784d;
    private static final double C_x = 1.8949d;
    private static final double C_y = 1.71848d;
    private static final double EPS = 1.0E-10d;
    private static final int NITER = 10;
    private static final double PI_DIV_3 = 1.0471975511965976d;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Putnins P2";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double sin = Math.sin(d2) * C_p;
        double d3 = d2 * d2;
        projCoordinate2.f410y *= (d3 * ((0.0046292d * d3) + 0.00909953d)) + 0.615709d;
        int i = 10;
        while (i > 0) {
            double cos = Math.cos(d2);
            double sin2 = Math.sin(d2);
            double d4 = cos - 1.0d;
            double d5 = ((d2 + (sin2 * d4)) - sin) / (((cos * d4) + 1.0d) - (sin2 * sin2));
            projCoordinate2.f410y -= d5;
            if (Math.abs(d5) < 1.0E-10d) {
                break;
            }
            i--;
        }
        if (i == 0) {
            projCoordinate2.f410y = d2 < 0.0d ? -1.0471975511965976d : PI_DIV_3;
        }
        projCoordinate2.f409x = C_x * d * (Math.cos(d2) - 0.5d);
        projCoordinate2.f410y = Math.sin(d2) * C_y;
        return projCoordinate2;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = ProjectionMath.asin(d2 / C_y);
        double cos = Math.cos(projCoordinate.f410y);
        projCoordinate.f409x = d / ((cos - 0.5d) * C_x);
        projCoordinate.f410y = ProjectionMath.asin((projCoordinate.f410y + (Math.sin(projCoordinate.f410y) * (cos - 1.0d))) / C_p);
        return projCoordinate;
    }
}
