package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class McBrydeThomasFlatPolarQuarticProjection extends PseudoCylindricalProjection {

    /* renamed from: C */
    private static final double f517C = 1.7071067811865475d;
    private static final double EPS = 1.0E-7d;
    private static final double FXC = 0.3124597141037825d;
    private static final double FYC = 1.874758284622695d;
    private static final int NITER = 20;
    private static final double ONETOL = 1.000001d;

    /* renamed from: RC */
    private static final double f518RC = 0.585786437626905d;
    private static final double RXC = 3.2004125807650623d;
    private static final double RYC = 0.533402096794177d;

    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "McBryde-Thomas Flat-Polar Quartic";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double sin = Math.sin(d2) * f517C;
        for (int i = 20; i > 0; i--) {
            double d3 = projCoordinate2.f410y;
            double d4 = d2 * 0.5d;
            double sin2 = ((Math.sin(d4) + Math.sin(d2)) - sin) / ((Math.cos(d4) * 0.5d) + Math.cos(d2));
            projCoordinate2.f410y = d3 - sin2;
            if (Math.abs(sin2) < EPS) {
                break;
            }
        }
        double d5 = d2 * 0.5d;
        projCoordinate2.f409x = FXC * d * (((Math.cos(d2) * 2.0d) / Math.cos(d5)) + 1.0d);
        projCoordinate2.f410y = Math.sin(d5) * FYC;
        return projCoordinate2;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double d5 = RYC * d2;
        if (Math.abs(d5) <= 1.0d) {
            d3 = Math.asin(d5) * 2.0d;
        } else if (Math.abs(d5) > ONETOL) {
            throw new ProjectionException("I");
        } else if (d5 < 0.0d) {
            d5 = -1.0d;
            d3 = -3.141592653589793d;
        } else {
            d3 = 3.141592653589793d;
            d5 = 1.0d;
        }
        projCoordinate2.f409x = (RXC * d) / (((Math.cos(d3) * 2.0d) / Math.cos(0.5d * d3)) + 1.0d);
        double sin = (d5 + Math.sin(d3)) * f518RC;
        if (Math.abs(sin) <= 1.0d) {
            d4 = Math.asin(sin);
        } else if (Math.abs(sin) <= ONETOL) {
            d4 = sin < 0.0d ? -1.5707963267948966d : 1.5707963267948966d;
        } else {
            throw new ProjectionException("I");
        }
        projCoordinate2.f410y = d4;
        return projCoordinate2;
    }
}
