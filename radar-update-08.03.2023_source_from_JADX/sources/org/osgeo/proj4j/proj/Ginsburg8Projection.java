package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class Ginsburg8Projection extends Projection {
    private static final double C12 = 0.08333333333333333d;

    /* renamed from: Cl */
    private static final double f488Cl = 9.52426E-4d;

    /* renamed from: Cp */
    private static final double f489Cp = 0.162388d;

    public String toString() {
        return "Ginsburg VIII (TsNIIGAiK)";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d2 * d2;
        projCoordinate.f410y = d2 * ((C12 * d3) + 1.0d);
        projCoordinate.f409x = (1.0d - (d3 * f489Cp)) * d;
        double d4 = d * d;
        projCoordinate.f409x *= 0.87d - ((f488Cl * d4) * d4);
        return projCoordinate;
    }
}
