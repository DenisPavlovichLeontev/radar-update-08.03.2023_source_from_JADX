package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class NellHProjection extends Projection {
    private static final double EPS = 1.0E-7d;
    private static final int NITER = 9;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Nell-Hammer";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = d * 0.5d * (Math.cos(d2) + 1.0d);
        projCoordinate.f410y = (d2 - Math.tan(0.5d * d2)) * 2.0d;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double d3 = d2 * 0.5d;
        int i = 9;
        while (i > 0) {
            double cos = Math.cos(d3);
            double d4 = projCoordinate2.f410y;
            double tan = ((d2 - Math.tan(d2 / 2.0d)) - d3) / (1.0d - (0.5d / (cos * cos)));
            projCoordinate2.f410y = d4 - tan;
            if (Math.abs(tan) < EPS) {
                break;
            }
            i--;
        }
        if (i == 0) {
            projCoordinate2.f410y = d3 < 0.0d ? -1.5707963267948966d : 1.5707963267948966d;
            projCoordinate2.f409x = d * 2.0d;
        } else {
            projCoordinate2.f409x = (d * 2.0d) / (Math.cos(d2) + 1.0d);
        }
        return projCoordinate2;
    }
}
