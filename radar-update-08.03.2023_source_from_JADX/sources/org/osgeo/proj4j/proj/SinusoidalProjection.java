package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.util.ProjectionMath;

public class SinusoidalProjection extends PseudoCylindricalProjection {
    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Sinusoidal";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = d * Math.cos(d2);
        projCoordinate.f410y = d2;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = d / Math.cos(d2);
        projCoordinate.f410y = d2;
        return projCoordinate;
    }

    public double getWidth(double d) {
        return ProjectionMath.normalizeLongitude(3.141592653589793d) * Math.cos(d);
    }
}
