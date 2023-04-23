package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class RectangularPolyconicProjection extends Projection {
    private static final double EPS = 1.0E-9d;
    private double fxa;
    private double fxb;
    private boolean mode;
    private double phi0;
    private double phi1;

    public String toString() {
        return "Rectangular Polyconic";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        if (this.mode) {
            d = Math.tan(d * this.fxb);
            d3 = this.fxa;
        } else {
            d3 = 0.5d;
        }
        double d4 = d * d3;
        if (Math.abs(d2) < EPS) {
            projCoordinate.f409x = d4 + d4;
            projCoordinate.f410y = -this.phi0;
        } else {
            projCoordinate.f410y = 1.0d / Math.tan(d2);
            double atan = Math.atan(d4 * Math.sin(d2)) * 2.0d;
            projCoordinate.f409x = Math.sin(atan) * projCoordinate.f410y;
            projCoordinate.f410y = (d2 - this.phi0) + ((1.0d - Math.cos(atan)) * projCoordinate.f410y);
        }
        return projCoordinate;
    }

    public void initialize() {
        super.initialize();
    }
}
