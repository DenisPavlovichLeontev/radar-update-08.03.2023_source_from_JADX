package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class AitoffProjection extends PseudoCylindricalProjection {
    protected static final int AITOFF = 0;
    protected static final int WINKEL = 1;
    private double cosphi1 = 0.0d;
    private boolean winkel = false;

    public boolean hasInverse() {
        return false;
    }

    public AitoffProjection() {
    }

    public AitoffProjection(int i, double d) {
        boolean z = false;
        this.projectionLatitude = d;
        this.winkel = i == 1 ? true : z;
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d * 0.5d;
        double acos = Math.acos(Math.cos(d2) * Math.cos(d3));
        if (acos != 0.0d) {
            double cos = 2.0d * acos * Math.cos(d2) * Math.sin(d3);
            double sin = 1.0d / Math.sin(acos);
            projCoordinate.f410y = sin;
            projCoordinate.f409x = cos * sin;
            projCoordinate.f410y *= acos * Math.sin(d2);
        } else {
            projCoordinate.f410y = 0.0d;
            projCoordinate.f409x = 0.0d;
        }
        if (this.winkel) {
            projCoordinate.f409x = (projCoordinate.f409x + (d * this.cosphi1)) * 0.5d;
            projCoordinate.f410y = (projCoordinate.f410y + d2) * 0.5d;
        }
        return projCoordinate;
    }

    public void initialize() {
        super.initialize();
        if (this.winkel) {
            this.cosphi1 = 0.6366197723675814d;
        }
    }

    public String toString() {
        return this.winkel ? "Winkel Tripel" : "Aitoff";
    }
}
