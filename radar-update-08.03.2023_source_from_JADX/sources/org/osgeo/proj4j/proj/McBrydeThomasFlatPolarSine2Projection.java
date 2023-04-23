package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.util.ProjectionMath;

public class McBrydeThomasFlatPolarSine2Projection extends Projection {

    /* renamed from: C1 */
    private static final double f519C1 = 0.45503d;
    private static final double C1_2 = 0.3333333333333333d;

    /* renamed from: C2 */
    private static final double f520C2 = 1.36509d;

    /* renamed from: C3 */
    private static final double f521C3 = 1.41546d;
    private static final double C_x = 0.22248d;
    private static final double C_y = 1.44492d;
    private static final double LOOP_TOL = 1.0E-7d;
    private static final int MAX_ITER = 10;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "McBryde-Thomas Flat-Pole Sine (No. 2)";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double sin = Math.sin(d2) * f521C3;
        for (int i = 10; i > 0; i--) {
            double d3 = d2 / f520C2;
            double d4 = projCoordinate2.f410y;
            double sin2 = (((Math.sin(d3) * f519C1) + Math.sin(d2)) - sin) / ((Math.cos(d3) * 0.3333333333333333d) + Math.cos(d2));
            projCoordinate2.f410y = d4 - sin2;
            if (Math.abs(sin2) < LOOP_TOL) {
                break;
            }
        }
        double d5 = d2 / f520C2;
        projCoordinate2.f409x = C_x * d * (((Math.cos(d2) * 3.0d) / Math.cos(d5)) + 1.0d);
        projCoordinate2.f410y = Math.sin(d5) * C_y;
        return projCoordinate2;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double asin = ProjectionMath.asin(d2 / C_y);
        projCoordinate.f410y = f520C2 * asin;
        projCoordinate.f409x = d / ((((Math.cos(projCoordinate.f410y) * 3.0d) / Math.cos(asin)) + 1.0d) * C_x);
        projCoordinate.f410y = ProjectionMath.asin(((Math.sin(asin) * f519C1) + Math.sin(projCoordinate.f410y)) / f521C3);
        return projCoordinate;
    }
}
