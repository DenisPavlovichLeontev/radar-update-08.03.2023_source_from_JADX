package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class Eckert5Projection extends Projection {
    private static final double RXF = 2.267508027238226d;
    private static final double RYF = 1.133754013619113d;

    /* renamed from: XF */
    private static final double f467XF = 0.4410127717245515d;

    /* renamed from: YF */
    private static final double f468YF = 0.882025543449103d;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Eckert V";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = (Math.cos(d2) + 1.0d) * f467XF * d;
        projCoordinate.f410y = d2 * f468YF;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d * RXF;
        double d4 = d2 * RYF;
        projCoordinate.f410y = d4;
        projCoordinate.f409x = d3 / (Math.cos(d4) + 1.0d);
        return projCoordinate;
    }
}
