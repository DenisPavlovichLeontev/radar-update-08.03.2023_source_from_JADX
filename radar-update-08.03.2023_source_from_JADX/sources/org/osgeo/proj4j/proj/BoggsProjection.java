package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class BoggsProjection extends PseudoCylindricalProjection {
    private static final double EPS = 1.0E-7d;
    private static final double FXC = 2.00276d;
    private static final double FXC2 = 1.11072d;
    private static final double FYC = 0.49931d;
    private static final double FYC2 = 1.4142135623730951d;
    private static final int NITER = 20;
    private static final double ONETOL = 1.000001d;

    public boolean hasInverse() {
        return false;
    }

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Boggs Eumorphic";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        ProjCoordinate projCoordinate2 = projCoordinate;
        if (Math.abs(Math.abs(d2) - 1.5707963267948966d) < EPS) {
            projCoordinate2.f409x = 0.0d;
            d3 = d2;
        } else {
            double sin = Math.sin(d2) * 3.141592653589793d;
            double d4 = d2;
            for (int i = 20; i > 0; i--) {
                double sin2 = ((Math.sin(d4) + d4) - sin) / (Math.cos(d4) + 1.0d);
                d4 -= sin2;
                if (Math.abs(sin2) < EPS) {
                    break;
                }
            }
            d3 = d4 * 0.5d;
            projCoordinate2.f409x = (FXC * d) / ((1.0d / Math.cos(d2)) + (FXC2 / Math.cos(d3)));
        }
        projCoordinate2.f410y = (d2 + (Math.sin(d3) * FYC2)) * FYC;
        return projCoordinate2;
    }
}
