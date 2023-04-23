package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class BipolarProjection extends Projection {
    private static final double Azab = 0.8165004367468637d;
    private static final double Azba = 1.8226184385618593d;
    private static final double C20 = 0.9396926207859084d;
    private static final double C45 = 0.7071067811865476d;
    private static final double EPS = 1.0E-10d;
    private static final double EPS10 = 1.0E-10d;

    /* renamed from: F */
    private static final double f435F = 1.8972474256746104d;
    private static final int NITER = 10;
    private static final double ONEEPS = 1.000000001d;
    private static final double R104 = 1.8151424220741028d;
    private static final double R110 = 1.9198621771937625d;
    private static final double S20 = -0.3420201433256687d;
    private static final double S45 = 0.7071067811865476d;

    /* renamed from: T */
    private static final double f436T = 1.27246578267089d;
    private static final double cAzc = 0.6969152303867837d;
    private static final double lamB = -0.3489497672625068d;

    /* renamed from: n */
    private static final double f437n = 0.6305584488127469d;
    private static final double rhoc = 1.2070912152156872d;
    private static final double sAzc = 0.7171535133114361d;
    private boolean noskew;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Bipolar Conic of Western Hemisphere";
    }

    public BipolarProjection() {
        this.minLatitude = Math.toRadians(-80.0d);
        this.maxLatitude = Math.toRadians(80.0d);
        this.projectionLongitude = Math.toRadians(-90.0d);
        this.minLongitude = Math.toRadians(-90.0d);
        this.maxLongitude = Math.toRadians(90.0d);
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        double d5;
        double d6;
        double acos;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double cos = Math.cos(d2);
        double sin = Math.sin(d2);
        double d7 = lamB - d;
        double cos2 = Math.cos(d7);
        double sin2 = Math.sin(d7);
        if (Math.abs(Math.abs(d2) - 1.5707963267948966d) < 1.0E-10d) {
            d4 = d2 < 0.0d ? 3.141592653589793d : 0.0d;
            d3 = Double.MAX_VALUE;
        } else {
            d3 = sin / cos;
            d4 = Math.atan2(sin2, (d3 - cos2) * 0.7071067811865476d);
        }
        boolean z = d4 > Azba;
        double d8 = -1.0d;
        if (z) {
            double d9 = d + R110;
            double cos3 = Math.cos(d9);
            double sin3 = Math.sin(d9);
            double d10 = (sin * S20) + (cos * C20 * cos3);
            if (Math.abs(d10) <= 1.0d) {
                d6 = Math.acos(d10);
            } else if (Math.abs(d10) <= ONEEPS) {
                d6 = d10 < 0.0d ? -1.0d : 1.0d;
            } else {
                throw new ProjectionException("F");
            }
            if (d3 != Double.MAX_VALUE) {
                d4 = Math.atan2(sin3, (d3 * C20) - (cos3 * S20));
            }
            d5 = Azab;
            projCoordinate2.f410y = rhoc;
        } else {
            double d11 = (sin + (cos * cos2)) * 0.7071067811865476d;
            if (Math.abs(d11) <= 1.0d) {
                acos = Math.acos(d11);
            } else if (Math.abs(d11) <= ONEEPS) {
                acos = d11 < 0.0d ? -1.0d : 1.0d;
            } else {
                throw new ProjectionException("F");
            }
            projCoordinate2.f410y = -1.2070912152156872d;
            d5 = Azba;
        }
        if (d6 >= 0.0d) {
            double pow = Math.pow(Math.tan(d6 * 0.5d), f437n);
            double d12 = f435F * pow;
            double d13 = 0.5d * (R104 - d6);
            if (d13 >= 0.0d) {
                double pow2 = (pow + Math.pow(d13, f437n)) / f436T;
                if (Math.abs(pow2) <= 1.0d) {
                    d8 = Math.acos(pow2);
                } else if (Math.abs(pow2) > ONEEPS) {
                    throw new ProjectionException("F");
                } else if (pow2 >= 0.0d) {
                    d8 = 1.0d;
                }
                double d14 = (d5 - d4) * f437n;
                if (Math.abs(d14) < d8) {
                    d12 /= Math.cos(d8 + (z ? d14 : -d14));
                }
                projCoordinate2.f409x = Math.sin(d14) * d12;
                double d15 = projCoordinate2.f410y;
                if (z) {
                    d12 = -d12;
                }
                projCoordinate2.f410y = d15 + (d12 * Math.cos(d14));
                if (this.noskew) {
                    double d16 = projCoordinate2.f409x;
                    projCoordinate2.f409x = ((-projCoordinate2.f409x) * cAzc) - (projCoordinate2.f410y * sAzc);
                    projCoordinate2.f410y = ((-projCoordinate2.f410y) * cAzc) + (d16 * sAzc);
                }
                return projCoordinate2;
            }
            throw new ProjectionException("F");
        }
        throw new ProjectionException("F");
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        double d5;
        double d6;
        double d7 = d;
        double d8 = d2;
        ProjCoordinate projCoordinate2 = projCoordinate;
        if (this.noskew) {
            projCoordinate2.f409x = ((-d7) * cAzc) + (d8 * sAzc);
            projCoordinate2.f410y = ((-d8) * cAzc) - (d7 * sAzc);
        }
        boolean z = d7 < 0.0d;
        double d9 = 0.7071067811865476d;
        if (z) {
            projCoordinate2.f410y = rhoc - d8;
            d9 = S20;
            d4 = C20;
            d3 = Azab;
        } else {
            projCoordinate2.f410y += rhoc;
            d3 = Azba;
            d4 = 0.7071067811865476d;
        }
        double distance = ProjectionMath.distance(d, d2);
        double atan2 = Math.atan2(d, d2);
        double abs = Math.abs(atan2);
        int i = 10;
        double d10 = 0.0d;
        double d11 = distance;
        while (true) {
            if (i <= 0) {
                d5 = d4;
                break;
            }
            double atan = Math.atan(Math.pow(d11 / f435F, 1.585895806935677d)) * 2.0d;
            d5 = d4;
            d10 = atan;
            double acos = Math.acos((Math.pow(Math.tan(atan * 0.5d), f437n) + Math.pow(Math.tan((R104 - atan) * 0.5d), f437n)) / f436T);
            if (abs < acos) {
                d6 = Math.cos(acos + (z ? atan2 : -atan2)) * distance;
            } else {
                d6 = d11;
            }
            if (Math.abs(d11 - d6) < 1.0E-10d) {
                break;
            }
            i--;
            d11 = d6;
            d4 = d5;
            ProjCoordinate projCoordinate3 = projCoordinate;
        }
        double d12 = d10;
        if (i != 0) {
            double d13 = d3 - (atan2 / f437n);
            ProjCoordinate projCoordinate4 = projCoordinate;
            projCoordinate4.f410y = Math.asin((Math.cos(d12) * d9) + (d5 * Math.sin(d12) * Math.cos(d13)));
            projCoordinate4.f409x = Math.atan2(Math.sin(d13), (d5 / Math.tan(d12)) - (d9 * Math.cos(d13)));
            if (z) {
                projCoordinate4.f409x -= R110;
            } else {
                projCoordinate4.f409x = lamB - projCoordinate4.f409x;
            }
            return projCoordinate4;
        }
        throw new ProjectionException("I");
    }

    public void initialize() {
        super.initialize();
    }
}
