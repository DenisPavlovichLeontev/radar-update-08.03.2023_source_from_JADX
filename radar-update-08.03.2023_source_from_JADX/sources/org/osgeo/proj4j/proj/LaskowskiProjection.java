package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class LaskowskiProjection extends Projection {
    private static final double a10 = 0.975534d;
    private static final double a12 = -0.119161d;
    private static final double a14 = -0.0547009d;
    private static final double a32 = -0.0143059d;
    private static final double b01 = 1.00384d;
    private static final double b03 = 0.0998909d;
    private static final double b05 = -0.0491032d;
    private static final double b21 = 0.0802894d;
    private static final double b23 = -0.02855d;
    private static final double b41 = 1.99025E-4d;

    public String toString() {
        return "Laskowski";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d * d;
        double d4 = d2 * d2;
        projCoordinate.f409x = d * ((((a32 * d3) + a12 + (a14 * d4)) * d4) + a10);
        projCoordinate.f410y = d2 * ((d3 * ((b23 * d4) + b21 + (b41 * d3))) + b01 + (d4 * ((b05 * d4) + b03)));
        return projCoordinate;
    }
}
