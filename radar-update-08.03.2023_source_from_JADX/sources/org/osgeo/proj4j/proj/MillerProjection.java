package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class MillerProjection extends CylindricalProjection {
    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Miller Cylindrical";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = d;
        projCoordinate.f410y = Math.log(Math.tan((d2 * 0.4d) + 0.7853981633974483d)) * 1.25d;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = d;
        projCoordinate.f410y = (Math.atan(Math.exp(d2 * 0.8d)) - 0.7853981633974483d) * 2.5d;
        return projCoordinate;
    }
}
