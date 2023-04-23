package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class TransverseCylindricalEqualArea extends Projection {
    private double rk0;

    public boolean hasInverse() {
        return true;
    }

    public boolean isRectilinear() {
        return false;
    }

    public String toString() {
        return "Transverse Cylindrical Equal Area";
    }

    public TransverseCylindricalEqualArea() {
        initialize();
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = this.rk0 * Math.cos(d2) * Math.sin(d);
        projCoordinate.f410y = this.scaleFactor * (Math.atan2(Math.tan(d2), Math.cos(d)) - this.projectionLatitude);
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = (this.rk0 * d2) + this.projectionLatitude;
        projCoordinate.f409x *= this.scaleFactor;
        double sqrt = Math.sqrt(1.0d - (d * d));
        projCoordinate.f410y = Math.asin(Math.sin(d2) * sqrt);
        projCoordinate.f409x = Math.atan2(d, sqrt * Math.cos(d2));
        return projCoordinate;
    }

    public void initialize() {
        super.initialize();
        this.rk0 = 1.0d / this.scaleFactor;
    }
}
