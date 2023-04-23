package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class PutninsP5Projection extends Projection {

    /* renamed from: C */
    private static final double f540C = 1.01346d;

    /* renamed from: D */
    private static final double f541D = 1.2158542d;

    /* renamed from: A */
    protected double f542A = 2.0d;

    /* renamed from: B */
    protected double f543B = 1.0d;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Putnins P5";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = d * f540C * (this.f542A - (this.f543B * Math.sqrt(((f541D * d2) * d2) + 1.0d)));
        projCoordinate.f410y = d2 * f540C;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = d2 / f540C;
        projCoordinate.f409x = d / ((this.f542A - (this.f543B * Math.sqrt(((projCoordinate.f410y * f541D) * projCoordinate.f410y) + 1.0d))) * f540C);
        return projCoordinate;
    }
}
