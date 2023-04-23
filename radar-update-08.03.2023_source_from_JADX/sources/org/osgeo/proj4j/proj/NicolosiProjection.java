package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class NicolosiProjection extends Projection {
    private static final double EPS = 1.0E-10d;

    public String toString() {
        return "Nicolosi Globular";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d;
        double d4 = d2;
        ProjCoordinate projCoordinate2 = projCoordinate;
        if (Math.abs(d) < 1.0E-10d) {
            projCoordinate2.f409x = 0.0d;
            projCoordinate2.f410y = d4;
        } else if (Math.abs(d2) < 1.0E-10d) {
            projCoordinate2.f409x = d3;
            projCoordinate2.f410y = 0.0d;
        } else if (Math.abs(Math.abs(d) - 1.5707963267948966d) < 1.0E-10d) {
            projCoordinate2.f409x = d3 * Math.cos(d2);
            projCoordinate2.f410y = Math.sin(d2) * 1.5707963267948966d;
        } else if (Math.abs(Math.abs(d2) - 1.5707963267948966d) < 1.0E-10d) {
            projCoordinate2.f409x = 0.0d;
            projCoordinate2.f410y = d4;
        } else {
            double d5 = (1.5707963267948966d / d3) - (d3 / 1.5707963267948966d);
            double d6 = d4 / 1.5707963267948966d;
            double sin = Math.sin(d2);
            double d7 = (1.0d - (d6 * d6)) / (sin - d6);
            double d8 = d5 / d7;
            double d9 = d8 * d8;
            double d10 = ((d5 * sin) / d7) - (d5 * 0.5d);
            double d11 = d9 + 1.0d;
            double d12 = d10 / d11;
            double d13 = (1.0d / d9) + 1.0d;
            double d14 = ((sin / d9) + (0.5d * d7)) / d13;
            double cos = Math.cos(d2);
            double sqrt = Math.sqrt((d12 * d12) + ((cos * cos) / d11));
            if (d3 < 0.0d) {
                sqrt = -sqrt;
            }
            projCoordinate2.f409x = (d12 + sqrt) * 1.5707963267948966d;
            double sqrt2 = Math.sqrt((d14 * d14) - (((((sin * sin) / d9) + (d7 * sin)) - 1.0d) / d13));
            if (d4 >= 0.0d) {
                sqrt2 = -sqrt2;
            }
            projCoordinate2.f410y = (d14 + sqrt2) * 1.5707963267948966d;
        }
        return projCoordinate2;
    }
}
