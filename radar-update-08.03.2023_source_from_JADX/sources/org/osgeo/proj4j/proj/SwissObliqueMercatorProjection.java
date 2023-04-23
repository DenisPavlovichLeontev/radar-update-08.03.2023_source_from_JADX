package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class SwissObliqueMercatorProjection extends Projection {
    private static final int NITER = 6;

    /* renamed from: K */
    private double f550K;

    /* renamed from: c */
    private double f551c;
    private double cosp0;
    private double hlf_e;

    /* renamed from: kR */
    private double f552kR;
    private double phi0;
    private double sinp0;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Swiss Oblique Mercator";
    }

    public void initialize() {
        super.initialize();
        this.phi0 = this.projectionLatitude;
        this.hlf_e = this.f538e * 0.5d;
        double cos = Math.cos(this.phi0);
        double d = cos * cos;
        this.f551c = Math.sqrt((this.f539es * d * d * this.rone_es) + 1.0d);
        double sin = Math.sin(this.phi0);
        double d2 = sin / this.f551c;
        this.sinp0 = d2;
        double asin = Math.asin(d2);
        this.cosp0 = Math.cos(asin);
        double d3 = sin * this.f538e;
        this.f550K = Math.log(Math.tan((asin * 0.5d) + 0.7853981633974483d)) - (this.f551c * (Math.log(Math.tan((this.phi0 * 0.5d) + 0.7853981633974483d)) - (this.hlf_e * Math.log((d3 + 1.0d) / (1.0d - d3)))));
        this.f552kR = (this.scaleFactor * Math.sqrt(this.one_es)) / (1.0d - (d3 * d3));
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double sin = this.f538e * Math.sin(d2);
        double atan = (Math.atan(Math.exp((this.f551c * (Math.log(Math.tan((d2 * 0.5d) + 0.7853981633974483d)) - (this.hlf_e * Math.log((sin + 1.0d) / (1.0d - sin))))) + this.f550K)) * 2.0d) - 1.5707963267948966d;
        double d3 = this.f551c * d;
        double cos = Math.cos(atan);
        double asin = Math.asin((this.cosp0 * Math.sin(atan)) - ((this.sinp0 * cos) * Math.cos(d3)));
        projCoordinate2.f409x = this.f552kR * Math.asin((cos * Math.sin(d3)) / Math.cos(asin));
        projCoordinate2.f410y = this.f552kR * Math.log(Math.tan((asin * 0.5d) + 0.7853981633974483d));
        return projCoordinate2;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double d3 = 0.7853981633974483d;
        double atan = (Math.atan(Math.exp(d2 / this.f552kR)) - 0.7853981633974483d) * 2.0d;
        double d4 = d / this.f552kR;
        double cos = Math.cos(atan);
        double asin = Math.asin((this.cosp0 * Math.sin(atan)) + (this.sinp0 * cos * Math.cos(d4)));
        double asin2 = Math.asin((cos * Math.sin(d4)) / Math.cos(asin));
        double log = (this.f550K - Math.log(Math.tan((asin * 0.5d) + 0.7853981633974483d))) / this.f551c;
        int i = 6;
        while (i != 0) {
            double sin = this.f538e * Math.sin(asin);
            double log2 = ((Math.log(Math.tan((asin * 0.5d) + d3)) + log) - (this.hlf_e * Math.log((sin + 1.0d) / (1.0d - sin)))) * (1.0d - (sin * sin)) * Math.cos(asin) * this.rone_es;
            asin -= log2;
            if (Math.abs(log2) < 1.0E-10d) {
                break;
            }
            i--;
            d3 = 0.7853981633974483d;
        }
        if (i != 0) {
            projCoordinate2.f409x = asin2 / this.f551c;
            projCoordinate2.f410y = asin;
            return projCoordinate2;
        }
        throw new ProjectionException("I_ERROR");
    }
}
