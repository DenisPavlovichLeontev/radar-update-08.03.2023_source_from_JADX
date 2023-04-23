package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class HatanoProjection extends Projection {

    /* renamed from: CN */
    private static final double f493CN = 2.67595d;

    /* renamed from: CS */
    private static final double f494CS = 2.43763d;
    private static final double EPS = 1.0E-7d;
    private static final double FXC = 0.85d;
    private static final double FYCN = 1.75859d;
    private static final double FYCS = 1.93052d;
    private static final int NITER = 20;
    private static final double ONETOL = 1.000001d;
    private static final double RCN = 0.3736990601468637d;
    private static final double RCS = 0.4102345310814193d;
    private static final double RXC = 1.1764705882352942d;
    private static final double RYCN = 0.5686373742600607d;
    private static final double RYCS = 0.5179951515653813d;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Hatano Asymmetrical Equal Area";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double sin = Math.sin(d2) * (d2 < 0.0d ? f494CS : f493CN);
        double d3 = d2;
        for (int i = 20; i > 0; i--) {
            double sin2 = ((Math.sin(d3) + d3) - sin) / (Math.cos(d3) + 1.0d);
            d3 -= sin2;
            if (Math.abs(sin2) < EPS) {
                break;
            }
        }
        double d4 = d3 * 0.5d;
        projCoordinate2.f409x = FXC * d * Math.cos(d4);
        projCoordinate2.f410y = Math.sin(d4) * (d4 < 0.0d ? FYCS : FYCN);
        return projCoordinate2;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        ProjCoordinate projCoordinate2 = projCoordinate;
        int i = (d2 > 0.0d ? 1 : (d2 == 0.0d ? 0 : -1));
        double d4 = (i < 0 ? RYCS : RYCN) * d2;
        if (Math.abs(d4) <= 1.0d) {
            d3 = Math.asin(d4);
        } else if (Math.abs(d4) <= ONETOL) {
            d3 = d4 > 0.0d ? 1.5707963267948966d : -1.5707963267948966d;
        } else {
            throw new ProjectionException("I");
        }
        projCoordinate2.f409x = (RXC * d) / Math.cos(d3);
        double d5 = d3 + d3;
        projCoordinate2.f410y = (d5 + Math.sin(d5)) * (i < 0 ? RCS : RCN);
        if (Math.abs(projCoordinate2.f410y) <= 1.0d) {
            projCoordinate2.f410y = Math.asin(projCoordinate2.f410y);
        } else if (Math.abs(projCoordinate2.f410y) <= ONETOL) {
            projCoordinate2.f410y = projCoordinate2.f410y > 0.0d ? 1.5707963267948966d : -1.5707963267948966d;
        } else {
            throw new ProjectionException("I");
        }
        return projCoordinate2;
    }
}
