package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class LagrangeProjection extends Projection {
    private static final double TOL = 1.0E-10d;

    /* renamed from: a1 */
    private double f495a1;
    private double hrw;
    private double phi1;

    /* renamed from: rw */
    private double f496rw = 1.4d;

    public boolean hasInverse() {
        return false;
    }

    public boolean isConformal() {
        return true;
    }

    public String toString() {
        return "Lagrange";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = 2.0d;
        if (Math.abs(Math.abs(d2) - 1.5707963267948966d) < 1.0E-10d) {
            projCoordinate.f409x = 0.0d;
            if (d2 < 0.0d) {
                d3 = -2.0d;
            }
            projCoordinate.f410y = d3;
        } else {
            double sin = Math.sin(d2);
            double pow = this.f495a1 * Math.pow((sin + 1.0d) / (1.0d - sin), this.hrw);
            double d4 = 1.0d / pow;
            double d5 = d * this.f496rw;
            double cos = ((pow + d4) * 0.5d) + Math.cos(d5);
            if (cos >= 1.0E-10d) {
                projCoordinate.f409x = (Math.sin(d5) * 2.0d) / cos;
                projCoordinate.f410y = (pow - d4) / cos;
            } else {
                throw new ProjectionException();
            }
        }
        return projCoordinate;
    }

    public void setW(double d) {
        this.f496rw = d;
    }

    public double getW() {
        return this.f496rw;
    }

    public void initialize() {
        super.initialize();
        double d = this.f496rw;
        if (d > 0.0d) {
            double d2 = 1.0d / d;
            this.f496rw = d2;
            this.hrw = d2 * 0.5d;
            double d3 = this.projectionLatitude1;
            this.phi1 = d3;
            double sin = Math.sin(d3);
            this.phi1 = sin;
            if (Math.abs(Math.abs(sin) - 1.0d) >= 1.0E-10d) {
                double d4 = this.phi1;
                this.f495a1 = Math.pow((1.0d - d4) / (d4 + 1.0d), this.hrw);
                return;
            }
            throw new ProjectionException("-22");
        }
        throw new ProjectionException("-27");
    }
}
