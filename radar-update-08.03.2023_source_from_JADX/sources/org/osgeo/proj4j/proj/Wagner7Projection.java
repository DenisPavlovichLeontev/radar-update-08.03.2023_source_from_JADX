package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class Wagner7Projection extends Projection {
    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Wagner VII";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double sin = Math.sin(d2) * 0.9063077870366499d;
        projCoordinate.f410y = sin;
        double cos = Math.cos(Math.asin(sin));
        double d3 = d / 3.0d;
        projCoordinate.f409x = 2.66723d * cos * Math.sin(d3);
        double d4 = projCoordinate.f410y;
        double sqrt = 1.0d / Math.sqrt(((cos * Math.cos(d3)) + 1.0d) * 0.5d);
        projCoordinate.f410y = d4 * 1.24104d * sqrt;
        projCoordinate.f409x *= sqrt;
        return projCoordinate;
    }
}
