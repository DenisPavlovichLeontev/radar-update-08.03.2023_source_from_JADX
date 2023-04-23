package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class FoucautSinusoidalProjection extends Projection {
    private static final double LOOP_TOL = 1.0E-7d;
    private static final int MAX_ITER = 10;

    /* renamed from: n */
    private double f481n;

    /* renamed from: n1 */
    private double f482n1;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Foucaut Sinusoidal";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double cos = Math.cos(d2);
        projCoordinate.f409x = (d * cos) / (this.f481n + (this.f482n1 * cos));
        projCoordinate.f410y = (this.f481n * d2) + (this.f482n1 * Math.sin(d2));
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d2;
        ProjCoordinate projCoordinate2 = projCoordinate;
        if (this.f481n != 0.0d) {
            projCoordinate2.f410y = d3;
            int i = 10;
            while (i > 0) {
                double d4 = projCoordinate2.f410y;
                double sin = (((this.f481n * projCoordinate2.f410y) + (this.f482n1 * Math.sin(projCoordinate2.f410y))) - d3) / (this.f481n + (this.f482n1 * Math.cos(projCoordinate2.f410y)));
                projCoordinate2.f410y = d4 - sin;
                if (Math.abs(sin) < LOOP_TOL) {
                    break;
                }
                i--;
            }
            if (i == 0) {
                projCoordinate2.f410y = d3 < 0.0d ? -1.5707963267948966d : 1.5707963267948966d;
            }
        } else {
            projCoordinate2.f410y = ProjectionMath.asin(d2);
        }
        double cos = Math.cos(projCoordinate2.f410y);
        projCoordinate2.f409x = ((this.f481n + (this.f482n1 * cos)) * d) / cos;
        return projCoordinate2;
    }

    public void initialize() {
        super.initialize();
        double d = this.f481n;
        if (d < 0.0d || d > 1.0d) {
            throw new ProjectionException("-99");
        }
        this.f482n1 = 1.0d - d;
    }
}
