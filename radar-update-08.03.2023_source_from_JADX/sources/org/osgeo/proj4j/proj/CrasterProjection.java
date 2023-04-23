package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class CrasterProjection extends Projection {
    private static final double RXM = 1.0233267079464885d;
    private static final double RYM = 0.32573500793527993d;
    private static final double THIRD = 0.3333333333333333d;

    /* renamed from: XM */
    private static final double f457XM = 0.9772050238058398d;

    /* renamed from: YM */
    private static final double f458YM = 3.0699801238394655d;

    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Craster Parabolic (Putnins P4)";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d2 * 0.3333333333333333d;
        projCoordinate.f409x = d * f457XM * ((Math.cos(d3 + d3) * 2.0d) - 1.0d);
        projCoordinate.f410y = Math.sin(d3) * f458YM;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = Math.asin(d2 * RYM) * 3.0d;
        projCoordinate.f409x = (d * RXM) / ((Math.cos((projCoordinate.f410y + projCoordinate.f410y) * 0.3333333333333333d) * 2.0d) - 1.0d);
        return projCoordinate;
    }
}
