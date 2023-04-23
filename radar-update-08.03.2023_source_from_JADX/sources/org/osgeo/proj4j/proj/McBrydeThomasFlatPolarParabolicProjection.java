package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class McBrydeThomasFlatPolarParabolicProjection extends Projection {
    private static final double C13 = 0.3333333333333333d;
    private static final double C23 = 0.6666666666666666d;

    /* renamed from: CS */
    private static final double f516CS = 0.9525793444156804d;
    private static final double FXC = 0.9258200997725514d;
    private static final double FYC = 3.401680257083045d;
    private static final double ONEEPS = 1.0000001d;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "McBride-Thomas Flat-Polar Parabolic";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = Math.asin(Math.sin(d2) * f516CS);
        projCoordinate.f409x = d * FXC * ((Math.cos(C23 * d2) * 2.0d) - 1.0d);
        projCoordinate.f410y = Math.sin(d2 * 0.3333333333333333d) * FYC;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        projCoordinate2.f410y = d2 / FYC;
        if (Math.abs(projCoordinate2.f410y) < 1.0d) {
            projCoordinate2.f410y = Math.asin(projCoordinate2.f410y);
        } else if (Math.abs(projCoordinate2.f410y) <= ONEEPS) {
            projCoordinate2.f410y = projCoordinate2.f410y < 0.0d ? -1.5707963267948966d : 1.5707963267948966d;
        } else {
            throw new ProjectionException("I");
        }
        double d3 = projCoordinate2.f410y * 3.0d;
        projCoordinate2.f410y = d3;
        projCoordinate2.f409x = d / (((Math.cos(d3 * C23) * 2.0d) - 1.0d) * FXC);
        double sin = Math.sin(projCoordinate2.f410y) / f516CS;
        projCoordinate2.f410y = sin;
        if (Math.abs(sin) < 1.0d) {
            projCoordinate2.f410y = Math.asin(projCoordinate2.f410y);
        } else if (Math.abs(projCoordinate2.f410y) <= ONEEPS) {
            projCoordinate2.f410y = projCoordinate2.f410y < 0.0d ? -1.5707963267948966d : 1.5707963267948966d;
        } else {
            throw new ProjectionException("I");
        }
        return projCoordinate2;
    }
}
