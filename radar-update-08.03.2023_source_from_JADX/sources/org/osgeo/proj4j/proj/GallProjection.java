package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class GallProjection extends Projection {
    private static final double RXF = 1.4142135623730951d;
    private static final double RYF = 0.585786437626905d;

    /* renamed from: XF */
    private static final double f483XF = 0.7071067811865476d;

    /* renamed from: YF */
    private static final double f484YF = 1.7071067811865475d;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Gall (Gall Stereographic)";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = d * f483XF;
        projCoordinate.f410y = Math.tan(d2 * 0.5d) * f484YF;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = d * RXF;
        projCoordinate.f410y = Math.atan(d2 * RYF) * 2.0d;
        return projCoordinate;
    }
}
