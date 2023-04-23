package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class Eckert2Projection extends Projection {
    private static final double C13 = 0.3333333333333333d;
    private static final double FXC = 0.46065886596178063d;
    private static final double FYC = 1.4472025091165353d;
    private static final double ONEEPS = 1.0000001d;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Eckert II";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d * FXC;
        double sqrt = Math.sqrt(4.0d - (Math.sin(Math.abs(d2)) * 3.0d));
        projCoordinate.f410y = sqrt;
        projCoordinate.f409x = d3 * sqrt;
        projCoordinate.f410y = (2.0d - projCoordinate.f410y) * FYC;
        if (d2 < 0.0d) {
            projCoordinate.f410y = -projCoordinate.f410y;
        }
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double abs = 2.0d - (Math.abs(d2) / FYC);
        projCoordinate.f410y = abs;
        projCoordinate.f409x = d / (abs * FXC);
        projCoordinate.f410y = (4.0d - (projCoordinate.f410y * projCoordinate.f410y)) * 0.3333333333333333d;
        if (Math.abs(projCoordinate.f410y) < 1.0d) {
            projCoordinate.f410y = Math.asin(projCoordinate.f410y);
        } else if (Math.abs(projCoordinate.f410y) <= ONEEPS) {
            projCoordinate.f410y = projCoordinate.f410y < 0.0d ? -1.5707963267948966d : 1.5707963267948966d;
        } else {
            throw new ProjectionException("I");
        }
        if (d2 < 0.0d) {
            projCoordinate.f410y = -projCoordinate.f410y;
        }
        return projCoordinate;
    }
}
