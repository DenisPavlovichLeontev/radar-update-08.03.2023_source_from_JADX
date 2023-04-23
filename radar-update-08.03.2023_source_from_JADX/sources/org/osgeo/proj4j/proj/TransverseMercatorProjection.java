package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.datum.Ellipsoid;
import org.osgeo.proj4j.util.ProjectionMath;

public class TransverseMercatorProjection extends CylindricalProjection {
    private static final double FC1 = 1.0d;
    private static final double FC2 = 0.5d;
    private static final double FC3 = 0.16666666666666666d;
    private static final double FC4 = 0.08333333333333333d;
    private static final double FC5 = 0.05d;
    private static final double FC6 = 0.03333333333333333d;
    private static final double FC7 = 0.023809523809523808d;
    private static final double FC8 = 0.017857142857142856d;

    /* renamed from: en */
    private double[] f553en;
    private double esp;
    private double ml0;
    private int utmZone = -1;

    public boolean hasInverse() {
        return true;
    }

    public boolean isRectilinear() {
        return false;
    }

    public TransverseMercatorProjection() {
        this.ellipsoid = Ellipsoid.GRS80;
        this.projectionLatitude = Math.toRadians(0.0d);
        this.projectionLongitude = Math.toRadians(0.0d);
        this.minLongitude = Math.toRadians(-90.0d);
        this.maxLongitude = Math.toRadians(90.0d);
        initialize();
    }

    public TransverseMercatorProjection(Ellipsoid ellipsoid, double d, double d2, double d3, double d4, double d5) {
        setEllipsoid(ellipsoid);
        this.projectionLongitude = d;
        this.projectionLatitude = d2;
        this.scaleFactor = d3;
        this.falseEasting = d4;
        this.falseNorthing = d5;
        initialize();
    }

    public Object clone() {
        TransverseMercatorProjection transverseMercatorProjection = (TransverseMercatorProjection) super.clone();
        double[] dArr = this.f553en;
        if (dArr != null) {
            transverseMercatorProjection.f553en = (double[]) dArr.clone();
        }
        return transverseMercatorProjection;
    }

    public void initialize() {
        super.initialize();
        if (this.spherical) {
            double d = this.scaleFactor;
            this.esp = d;
            this.ml0 = d * 0.5d;
            return;
        }
        this.f553en = ProjectionMath.enfn(this.f539es);
        this.ml0 = ProjectionMath.mlfn(this.projectionLatitude, Math.sin(this.projectionLatitude), Math.cos(this.projectionLatitude), this.f553en);
        this.esp = this.f539es / (FC1 - this.f539es);
    }

    public static int getRowFromNearestParallel(double d) {
        int radToDeg = (int) ProjectionMath.radToDeg(ProjectionMath.normalizeLatitude(d));
        if (radToDeg < -80 || radToDeg > 84) {
            return 0;
        }
        if (radToDeg > 80) {
            return 24;
        }
        return ((radToDeg + 80) / 8) + 3;
    }

    public static int getZoneFromNearestMeridian(double d) {
        int floor = ((int) Math.floor(((ProjectionMath.normalizeLongitude(d) + 3.141592653589793d) * 30.0d) / 3.141592653589793d)) + 1;
        if (floor < 1) {
            return 1;
        }
        if (floor > 60) {
            return 60;
        }
        return floor;
    }

    public void setUTMZone(int i) {
        this.utmZone = i;
        this.projectionLongitude = (((((double) (i - 1)) + 0.5d) * 3.141592653589793d) / 30.0d) - 3.141592653589793d;
        double d = 0.0d;
        this.projectionLatitude = 0.0d;
        this.scaleFactor = 0.9996d;
        this.falseEasting = 500000.0d;
        if (this.isSouth) {
            d = 1.0E7d;
        }
        this.falseNorthing = d;
        initialize();
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double d3 = 0.0d;
        if (this.spherical) {
            double cos = Math.cos(d2);
            double sin = Math.sin(d) * cos;
            projCoordinate2.f409x = this.ml0 * this.scaleFactor * Math.log((sin + FC1) / (FC1 - sin));
            double acos = ProjectionMath.acos((cos * Math.cos(d)) / Math.sqrt(FC1 - (sin * sin)));
            if (d2 < 0.0d) {
                acos = -acos;
            }
            projCoordinate2.f410y = this.esp * (acos - this.projectionLatitude);
        } else {
            double sin2 = Math.sin(d2);
            double cos2 = Math.cos(d2);
            if (Math.abs(cos2) > 1.0E-10d) {
                d3 = sin2 / cos2;
            }
            double d4 = d3 * d3;
            double d5 = cos2 * d;
            double d6 = d5 * d5;
            double sqrt = d5 / Math.sqrt(FC1 - ((this.f539es * sin2) * sin2));
            double d7 = this.esp * cos2 * cos2;
            projCoordinate2.f409x = this.scaleFactor * sqrt * ((FC3 * d6 * ((FC1 - d4) + d7 + (FC5 * d6 * (((d4 - 18.0d) * d4) + 5.0d + ((14.0d - (d4 * 58.0d)) * d7) + (FC7 * d6 * (((((179.0d - d4) * d4) - 479.0d) * d4) + 61.0d)))))) + FC1);
            double d8 = this.scaleFactor;
            projCoordinate2.f410y = d8 * ((ProjectionMath.mlfn(d2, sin2, cos2, this.f553en) - this.ml0) + (sin2 * sqrt * d * 0.5d * ((FC4 * d6 * ((5.0d - d4) + (((4.0d * d7) + 9.0d) * d7) + (FC6 * d6 * (((d4 - 58.0d) * d4) + 61.0d + (d7 * (270.0d - (330.0d * d4))) + (d6 * FC8 * ((d4 * (((543.0d - d4) * d4) - 3111.0d)) + 1385.0d)))))) + FC1)));
        }
        return projCoordinate2;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double d3 = 0.0d;
        if (this.spherical) {
            double exp = Math.exp(d / this.scaleFactor);
            double d4 = (exp - (FC1 / exp)) * 0.5d;
            double cos = Math.cos(this.projectionLatitude + (d2 / this.scaleFactor));
            projCoordinate2.f410y = ProjectionMath.asin(Math.sqrt((FC1 - (cos * cos)) / ((d4 * d4) + FC1)));
            if (d2 < 0.0d) {
                projCoordinate2.f410y = -projCoordinate2.f410y;
            }
            projCoordinate2.f409x = Math.atan2(d4, cos);
        } else {
            projCoordinate2.f410y = ProjectionMath.inv_mlfn(this.ml0 + (d2 / this.scaleFactor), this.f539es, this.f553en);
            double d5 = 1.5707963267948966d;
            if (Math.abs(d2) >= 1.5707963267948966d) {
                if (d2 < 0.0d) {
                    d5 = -1.5707963267948966d;
                }
                projCoordinate2.f410y = d5;
                projCoordinate2.f409x = 0.0d;
            } else {
                double sin = Math.sin(projCoordinate2.f410y);
                double cos2 = Math.cos(projCoordinate2.f410y);
                if (Math.abs(cos2) > 1.0E-10d) {
                    d3 = sin / cos2;
                }
                double d6 = this.esp * cos2 * cos2;
                double d7 = FC1 - ((this.f539es * sin) * sin);
                double sqrt = (Math.sqrt(d7) * d) / this.scaleFactor;
                double d8 = d7 * d3;
                double d9 = d3 * d3;
                double d10 = sqrt * sqrt;
                projCoordinate2.f410y -= (((d8 * d10) / (FC1 - this.f539es)) * 0.5d) * (FC1 - ((FC4 * d10) * (((((3.0d - (9.0d * d6)) * d9) + 5.0d) + ((FC1 - (4.0d * d6)) * d6)) - ((FC6 * d10) * ((((((90.0d - (252.0d * d6)) + (45.0d * d9)) * d9) + 61.0d) + (46.0d * d6)) - ((FC8 * d10) * ((((((1574.0d * d9) + 4095.0d) * d9) + 3633.0d) * d9) + 1385.0d)))))));
                projCoordinate2.f409x = (sqrt * (FC1 - ((FC3 * d10) * ((((2.0d * d9) + FC1) + d6) - ((FC5 * d10) * (((((((24.0d * d9) + 28.0d) + (8.0d * d6)) * d9) + 5.0d) + (d6 * 6.0d)) - ((d10 * FC7) * ((d9 * ((((720.0d * d9) + 1320.0d) * d9) + 662.0d)) + 61.0d)))))))) / cos2;
            }
        }
        return projCoordinate2;
    }

    public String toString() {
        return this.utmZone >= 0 ? "Universal Tranverse Mercator" : "Transverse Mercator";
    }
}
