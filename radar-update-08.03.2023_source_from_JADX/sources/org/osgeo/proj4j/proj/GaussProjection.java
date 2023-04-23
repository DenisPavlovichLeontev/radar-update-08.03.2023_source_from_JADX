package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class GaussProjection extends Projection {
    private static final double DEL_TOL = 1.0E-14d;
    private static final int MAX_ITER = 20;

    /* renamed from: C */
    private double f485C;

    /* renamed from: K */
    private double f486K;
    protected double phic0;
    private double ratexp;

    /* renamed from: rc */
    protected double f487rc;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Gauss";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = (Math.atan((this.f486K * Math.pow(Math.tan((0.5d * d2) + 0.7853981633974483d), this.f485C)) * srat(this.f538e * Math.sin(d2), this.ratexp)) * 2.0d) - 1.5707963267948966d;
        projCoordinate.f409x = this.f485C * d;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double d3 = d / this.f485C;
        double pow = Math.pow(Math.tan((0.5d * d2) + 0.7853981633974483d) / this.f486K, 1.0d / this.f485C);
        int i = 20;
        double d4 = d2;
        while (true) {
            if (i <= 0) {
                break;
            }
            double atan = (Math.atan(srat(this.f538e * Math.sin(d4), this.f538e * -0.5d) * pow) * 2.0d) - 1.5707963267948966d;
            if (Math.abs(atan - d4) < DEL_TOL) {
                d4 = atan;
                break;
            }
            i--;
            d4 = atan;
        }
        if (i > 0) {
            projCoordinate2.f409x = d3;
            projCoordinate2.f410y = d4;
            return projCoordinate2;
        }
        throw new ProjectionException(this, ProjectionException.ERR_17);
    }

    public void initialize() {
        super.initialize();
        double sin = Math.sin(this.projectionLatitude);
        double cos = Math.cos(this.projectionLatitude);
        double d = cos * cos;
        this.f487rc = Math.sqrt(1.0d - this.f539es) / (1.0d - ((this.f539es * sin) * sin));
        double sqrt = Math.sqrt((((this.f539es * d) * d) / (1.0d - this.f539es)) + 1.0d);
        this.f485C = sqrt;
        this.phic0 = Math.asin(sin / sqrt);
        this.ratexp = this.f485C * 0.5d * this.f538e;
        this.f486K = Math.tan((this.phic0 * 0.5d) + 0.7853981633974483d) / (Math.pow(Math.tan((this.projectionLatitude * 0.5d) + 0.7853981633974483d), this.f485C) * srat(this.f538e * sin, this.ratexp));
    }

    private static double srat(double d, double d2) {
        return Math.pow((1.0d - d) / (d + 1.0d), d2);
    }
}
