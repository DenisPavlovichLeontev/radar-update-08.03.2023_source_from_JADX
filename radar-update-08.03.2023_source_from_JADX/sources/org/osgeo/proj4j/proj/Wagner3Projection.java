package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class Wagner3Projection extends PseudoCylindricalProjection {
    private static final double TWOTHIRD = 0.6666666666666666d;
    private double C_x;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Wagner III";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = this.C_x * d * Math.cos(TWOTHIRD * d2);
        projCoordinate.f410y = d2;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = d2;
        projCoordinate.f409x = d / (this.C_x * Math.cos(projCoordinate.f410y * TWOTHIRD));
        return projCoordinate;
    }

    public void initialize() {
        super.initialize();
        this.C_x = Math.cos(this.trueScaleLatitude) / Math.cos((this.trueScaleLatitude * 2.0d) / 3.0d);
        this.f539es = 0.0d;
    }
}
