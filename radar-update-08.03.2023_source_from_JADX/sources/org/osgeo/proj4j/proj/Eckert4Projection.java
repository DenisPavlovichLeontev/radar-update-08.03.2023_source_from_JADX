package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.util.ProjectionMath;

public class Eckert4Projection extends Projection {
    private static final double C_p = 3.5707963267948966d;
    private static final double C_x = 0.4222382003157712d;
    private static final double C_y = 1.3265004281770023d;
    private static final double EPS = 1.0E-7d;
    private static final double RC_p = 0.2800495767557787d;
    private static final double RC_y = 0.7538633073600218d;
    private final int NITER = 6;

    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Eckert IV";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double sin = Math.sin(d2) * C_p;
        double d3 = d2 * d2;
        double d4 = ((d3 * ((0.00826809d * d3) + 0.0218849d)) + 0.895168d) * d2;
        int i = 6;
        while (i > 0) {
            double cos = Math.cos(d4);
            double sin2 = Math.sin(d4);
            double d5 = 2.0d + cos;
            double d6 = (((sin2 * d5) + d4) - sin) / (((cos * d5) + 1.0d) - (sin2 * sin2));
            d4 -= d6;
            if (Math.abs(d6) < EPS) {
                break;
            }
            i--;
        }
        double d7 = C_y;
        if (i == 0) {
            projCoordinate2.f409x = d * C_x;
            if (d4 < 0.0d) {
                d7 = -1.3265004281770023d;
            }
            projCoordinate2.f410y = d7;
        } else {
            projCoordinate2.f409x = C_x * d * (Math.cos(d4) + 1.0d);
            projCoordinate2.f410y = Math.sin(d4) * C_y;
        }
        return projCoordinate2;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = ProjectionMath.asin(d2 / C_y);
        double cos = Math.cos(projCoordinate.f410y);
        projCoordinate.f409x = d / ((1.0d + cos) * C_x);
        projCoordinate.f410y = ProjectionMath.asin((projCoordinate.f410y + (Math.sin(projCoordinate.f410y) * (cos + 2.0d))) / C_p);
        return projCoordinate;
    }
}
