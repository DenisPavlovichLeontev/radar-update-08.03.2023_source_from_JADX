package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class AiryProjection extends Projection {
    private static final double EPS = 1.0E-10d;
    private static final int EQUIT = 2;
    private static final int N_POLE = 0;
    private static final int OBLIQ = 3;
    private static final int S_POLE = 1;

    /* renamed from: Cb */
    private double f427Cb;
    private double cosph0;
    private int mode;
    private boolean no_cut = true;
    private double p_halfpi;
    private double sinph0;

    public String toString() {
        return "Airy";
    }

    public AiryProjection() {
        this.minLatitude = Math.toRadians(-60.0d);
        this.maxLatitude = Math.toRadians(60.0d);
        this.minLongitude = Math.toRadians(-90.0d);
        this.maxLongitude = Math.toRadians(90.0d);
        initialize();
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        double d5;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double sin = Math.sin(d);
        double cos = Math.cos(d);
        int i = this.mode;
        if (i == 0 || i == 1) {
            projCoordinate2.f410y = Math.abs(this.p_halfpi - d2);
            if (this.no_cut || d2 - 1.0E-10d <= 1.5707963267948966d) {
                double d6 = projCoordinate2.f410y * 0.5d;
                projCoordinate2.f410y = d6;
                if (d6 > 1.0E-10d) {
                    double tan = Math.tan(d2);
                    double log = ((Math.log(Math.cos(d2)) / tan) + (tan * this.f427Cb)) * -2.0d;
                    projCoordinate2.f409x = sin * log;
                    projCoordinate2.f410y = log * cos;
                    if (this.mode == 0) {
                        projCoordinate2.f410y = -projCoordinate2.f410y;
                    }
                } else {
                    projCoordinate2.f410y = 0.0d;
                    projCoordinate2.f409x = 0.0d;
                }
            } else {
                throw new ProjectionException("F");
            }
        } else if (i == 2 || i == 3) {
            double sin2 = Math.sin(d2);
            double cos2 = Math.cos(d2);
            double d7 = cos2 * cos;
            if (this.mode == 3) {
                d3 = sin2;
                d7 = (this.sinph0 * sin2) + (this.cosph0 * d7);
            } else {
                d3 = sin2;
            }
            if (this.no_cut || d7 >= -1.0E-10d) {
                double d8 = 1.0d - d7;
                if (Math.abs(d8) > 1.0E-10d) {
                    double d9 = (d7 + 1.0d) * 0.5d;
                    d5 = (-Math.log(d9)) / d8;
                    d4 = this.f427Cb / d9;
                } else {
                    d5 = 0.5d;
                    d4 = this.f427Cb;
                }
                double d10 = d5 - d4;
                projCoordinate2.f409x = d10 * cos2 * sin;
                if (this.mode == 3) {
                    projCoordinate2.f410y = d10 * ((this.cosph0 * d3) - ((this.sinph0 * cos2) * cos));
                } else {
                    projCoordinate2.f410y = d10 * d3;
                }
            } else {
                throw new ProjectionException("F");
            }
        }
        return projCoordinate2;
    }

    public void initialize() {
        super.initialize();
        this.no_cut = false;
        if (Math.abs(0.7853981633974483d) < 1.0E-10d) {
            this.f427Cb = -0.5d;
        } else {
            double tan = 1.0d / Math.tan(0.7853981633974483d);
            this.f427Cb = tan;
            this.f427Cb = tan * Math.log(Math.cos(0.7853981633974483d)) * tan;
        }
        if (Math.abs(Math.abs(this.projectionLatitude) - 1.5707963267948966d) < 1.0E-10d) {
            if (this.projectionLatitude < 0.0d) {
                this.p_halfpi = -1.5707963267948966d;
                this.mode = 1;
                return;
            }
            this.p_halfpi = 1.5707963267948966d;
            this.mode = 0;
        } else if (Math.abs(this.projectionLatitude) < 1.0E-10d) {
            this.mode = 2;
        } else {
            this.mode = 3;
            this.sinph0 = Math.sin(this.projectionLatitude);
            this.cosph0 = Math.cos(this.projectionLatitude);
        }
    }
}
