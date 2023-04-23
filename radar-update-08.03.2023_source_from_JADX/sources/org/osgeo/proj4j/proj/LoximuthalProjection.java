package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class LoximuthalProjection extends PseudoCylindricalProjection {
    private static final double EPS = 1.0E-8d;

    /* renamed from: FC */
    private static final double f514FC = 0.9213177319235613d;

    /* renamed from: RP */
    private static final double f515RP = 0.3183098861837907d;
    private double cosphi1;
    private double phi1;
    private double tanphi1 = Math.tan((this.phi1 * 0.5d) + 0.7853981633974483d);

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Loximuthal";
    }

    public LoximuthalProjection() {
        double radians = Math.toRadians(40.0d);
        this.phi1 = radians;
        this.cosphi1 = Math.cos(radians);
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4 = d2 - this.phi1;
        if (d4 < EPS) {
            d3 = d * this.cosphi1;
        } else {
            double d5 = (d2 * 0.5d) + 0.7853981633974483d;
            d3 = (Math.abs(d5) < EPS || Math.abs(Math.abs(d5) - 1.5707963267948966d) < EPS) ? 0.0d : (d * d4) / Math.log(Math.tan(d5) / this.tanphi1);
        }
        projCoordinate.f409x = d3;
        projCoordinate.f410y = d4;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4 = this.phi1 + d2;
        if (Math.abs(d2) < EPS) {
            d2 = this.cosphi1;
        } else {
            double d5 = (0.5d * d2) + 0.7853981633974483d;
            if (Math.abs(d5) < EPS || Math.abs(Math.abs(d) - 1.5707963267948966d) < EPS) {
                d3 = 0.0d;
                projCoordinate.f409x = d3;
                projCoordinate.f410y = d4;
                return projCoordinate;
            }
            d *= Math.log(Math.tan(d5) / this.tanphi1);
        }
        d3 = d / d2;
        projCoordinate.f409x = d3;
        projCoordinate.f410y = d4;
        return projCoordinate;
    }
}
