package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class CollignonProjection extends Projection {
    private static final double FXC = 1.1283791670955126d;
    private static final double FYC = 1.772453850905516d;
    private static final double ONEEPS = 1.0000001d;

    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Collignon";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double sin = 1.0d - Math.sin(d2);
        projCoordinate.f410y = sin;
        if (sin <= 0.0d) {
            projCoordinate.f410y = 0.0d;
        } else {
            projCoordinate.f410y = Math.sqrt(projCoordinate.f410y);
        }
        projCoordinate.f409x = d * FXC * projCoordinate.f410y;
        projCoordinate.f410y = (1.0d - projCoordinate.f410y) * FYC;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = (d2 / FYC) - 1.0d;
        double d4 = 1.0d - (d3 * d3);
        projCoordinate.f410y = d4;
        if (Math.abs(d4) < 1.0d) {
            projCoordinate.f410y = Math.asin(d3);
        } else if (Math.abs(d3) <= ONEEPS) {
            projCoordinate.f410y = d3 < 0.0d ? -1.5707963267948966d : 1.5707963267948966d;
        } else {
            throw new ProjectionException("I");
        }
        double sin = 1.0d - Math.sin(d3);
        projCoordinate.f409x = sin;
        if (sin <= 0.0d) {
            projCoordinate.f409x = 0.0d;
        } else {
            projCoordinate.f409x = d / (Math.sqrt(projCoordinate.f409x) * FXC);
        }
        projCoordinate.f410y = d3;
        return projCoordinate;
    }
}
