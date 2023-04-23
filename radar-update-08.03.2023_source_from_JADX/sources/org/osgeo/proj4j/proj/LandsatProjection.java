package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.util.ProjectionMath;

public class LandsatProjection extends Projection {
    private static final double PI_HALFPI = 4.71238898038469d;
    private static final double TOL = 1.0E-7d;
    private static final double TWOPI_HALFPI = 7.853981633974483d;

    /* renamed from: a2 */
    private double f502a2;

    /* renamed from: a4 */
    private double f503a4;

    /* renamed from: b */
    private double f504b;

    /* renamed from: c1 */
    private double f505c1;

    /* renamed from: c3 */
    private double f506c3;

    /* renamed from: ca */
    private double f507ca;
    private double p22;

    /* renamed from: q */
    private double f508q;
    private double rlm;
    private double rlm2;

    /* renamed from: sa */
    private double f509sa;

    /* renamed from: t */
    private double f510t;

    /* renamed from: u */
    private double f511u;

    /* renamed from: w */
    private double f512w;

    /* renamed from: xj */
    private double f513xj;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Landsat";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        int i;
        double d3;
        double d4;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double d5 = -1.5707963267948966d;
        double d6 = d2 > 1.5707963267948966d ? 1.5707963267948966d : d2 < -1.5707963267948966d ? -1.5707963267948966d : d2;
        double d7 = 0.0d;
        double d8 = d6 >= 0.0d ? 1.5707963267948966d : PI_HALFPI;
        double tan = Math.tan(d6);
        int i2 = 0;
        double d9 = 0.0d;
        double d10 = 0.0d;
        while (true) {
            double cos = Math.cos(d + (this.p22 * d8));
            Math.abs(cos);
            double sin = d8 - (Math.sin(d8) * (cos < d7 ? d5 : 1.5707963267948966d));
            i = 50;
            double d11 = d8;
            while (true) {
                if (i <= 0) {
                    d3 = d8;
                    break;
                }
                double d12 = d + (this.p22 * d11);
                double cos2 = Math.cos(d12);
                if (Math.abs(cos2) < TOL) {
                    d12 -= TOL;
                }
                d10 = d12;
                d3 = d8;
                d9 = Math.atan((((this.one_es * tan) * this.f509sa) + (Math.sin(d10) * this.f507ca)) / cos2) + sin;
                if (Math.abs(Math.abs(d11) - Math.abs(d9)) < TOL) {
                    break;
                }
                i--;
                d11 = d9;
                d8 = d3;
            }
            if (i == 0 || (i2 = i2 + 1) >= 3) {
                break;
            }
            double d13 = this.rlm;
            if (d9 > d13 && d9 < this.rlm2) {
                break;
            }
            if (d9 <= d13) {
                d4 = TWOPI_HALFPI;
            } else if (d9 >= this.rlm2) {
                d5 = -1.5707963267948966d;
                d7 = 0.0d;
                d4 = 1.5707963267948966d;
            } else {
                d4 = d3;
            }
            d5 = -1.5707963267948966d;
            d7 = 0.0d;
        }
        if (i != 0) {
            double sin2 = Math.sin(d6);
            double log = Math.log(Math.tan((ProjectionMath.asin((((this.one_es * this.f507ca) * sin2) - ((this.f509sa * Math.cos(d6)) * Math.sin(d10))) / Math.sqrt(1.0d - ((this.f539es * sin2) * sin2))) * 0.5d) + 0.7853981633974483d));
            double sin3 = Math.sin(d9);
            double d14 = sin3 * sin3;
            double d15 = sin3;
            double cos3 = this.p22 * this.f509sa * Math.cos(d9) * Math.sqrt(((this.f510t * d14) + 1.0d) / (((this.f512w * d14) + 1.0d) * ((this.f508q * d14) + 1.0d)));
            double d16 = this.f513xj;
            double sqrt = Math.sqrt((d16 * d16) + (cos3 * cos3));
            double d17 = log;
            projCoordinate2.f409x = (((this.f504b * d9) + (this.f502a2 * Math.sin(2.0d * d9))) + (this.f503a4 * Math.sin(4.0d * d9))) - ((d17 * cos3) / sqrt);
            projCoordinate2.f410y = (this.f505c1 * d15) + (this.f506c3 * Math.sin(d9 * 3.0d)) + ((d17 * this.f513xj) / sqrt);
        } else {
            projCoordinate2.f410y = Double.POSITIVE_INFINITY;
            projCoordinate2.f409x = Double.POSITIVE_INFINITY;
        }
        return projCoordinate2;
    }

    private void seraz0(double d, double d2) {
        double d3 = 0.017453292519943295d * d;
        double sin = Math.sin(d3);
        double d4 = sin * sin;
        double cos = this.p22 * this.f509sa * Math.cos(d3) * Math.sqrt(((this.f510t * d4) + 1.0d) / (((this.f512w * d4) + 1.0d) * ((this.f508q * d4) + 1.0d)));
        double d5 = this.f508q;
        double d6 = (d5 * d4) + 1.0d;
        double sqrt = Math.sqrt(((d5 * d4) + 1.0d) / ((this.f512w * d4) + 1.0d)) * ((((this.f512w * d4) + 1.0d) / (d6 * d6)) - (this.p22 * this.f507ca));
        double d7 = this.f513xj;
        double d8 = cos * cos;
        double sqrt2 = Math.sqrt((d7 * d7) + d8);
        double d9 = this.f504b;
        double d10 = (d2 * ((this.f513xj * sqrt) - d8)) / sqrt2;
        this.f504b = d9 + d10;
        this.f502a2 += Math.cos(d3 + d3) * d10;
        this.f503a4 += d10 * Math.cos(4.0d * d3);
        double d11 = ((cos * d2) * (sqrt + this.f513xj)) / sqrt2;
        this.f505c1 += Math.cos(d3) * d11;
        this.f506c3 += d11 * Math.cos(d3 * 3.0d);
    }

    public void initialize() {
        super.initialize();
        this.projectionLongitude = 2.2492058070450924d - (((double) 120) * 0.025032610785576042d);
        this.p22 = 103.2669323d / 1440.0d;
        this.f509sa = Math.sin(1.729481662386221d);
        double cos = Math.cos(1.729481662386221d);
        this.f507ca = cos;
        if (Math.abs(cos) < 1.0E-9d) {
            this.f507ca = 1.0E-9d;
        }
        double d = this.f539es;
        double d2 = this.f507ca;
        double d3 = d * d2 * d2;
        double d4 = this.f539es;
        double d5 = this.f509sa;
        double d6 = d4 * d5 * d5;
        double d7 = (1.0d - d3) * this.rone_es;
        this.f512w = (d7 * d7) - 1.0d;
        this.f508q = this.rone_es * d6;
        this.f510t = d6 * (2.0d - this.f539es) * this.rone_es * this.rone_es;
        this.f511u = d3 * this.rone_es;
        this.f513xj = this.one_es * this.one_es * this.one_es;
        this.rlm = 1.6341348883592068d;
        this.rlm2 = 1.6341348883592068d + 6.283185307179586d;
        this.f506c3 = 0.0d;
        this.f505c1 = 0.0d;
        this.f504b = 0.0d;
        this.f503a4 = 0.0d;
        this.f502a2 = 0.0d;
        seraz0(0.0d, 1.0d);
        for (double d8 = 9.0d; d8 <= 81.0001d; d8 += 18.0d) {
            seraz0(d8, 4.0d);
        }
        for (double d9 = 18.0d; d9 <= 72.0001d; d9 += 18.0d) {
            seraz0(d9, 2.0d);
        }
        seraz0(90.0d, 1.0d);
        this.f502a2 /= 30.0d;
        this.f503a4 /= 60.0d;
        this.f504b /= 30.0d;
        this.f505c1 /= 15.0d;
        this.f506c3 /= 45.0d;
    }
}
