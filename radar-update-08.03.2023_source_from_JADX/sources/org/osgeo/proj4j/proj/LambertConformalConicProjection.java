package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.datum.Ellipsoid;
import org.osgeo.proj4j.util.ProjectionMath;

public class LambertConformalConicProjection extends ConicProjection {

    /* renamed from: c */
    private double f500c;

    /* renamed from: n */
    private double f501n;
    private double rho0;

    public boolean hasInverse() {
        return true;
    }

    public boolean isConformal() {
        return true;
    }

    public String toString() {
        return "Lambert Conformal Conic";
    }

    public LambertConformalConicProjection() {
        this.minLatitude = Math.toRadians(0.0d);
        this.maxLatitude = Math.toRadians(80.0d);
        this.projectionLatitude = 0.7853981633974483d;
        this.projectionLatitude1 = 0.0d;
        this.projectionLatitude2 = 0.0d;
        initialize();
    }

    public LambertConformalConicProjection(Ellipsoid ellipsoid, double d, double d2, double d3, double d4, double d5, double d6) {
        setEllipsoid(ellipsoid);
        this.projectionLongitude = d;
        this.projectionLatitude = d4;
        this.scaleFactor = 1.0d;
        this.falseEasting = d5;
        this.falseNorthing = d6;
        this.projectionLatitude1 = d2;
        this.projectionLatitude2 = d3;
        initialize();
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        double d5;
        if (Math.abs(Math.abs(d2) - 1.5707963267948966d) < 1.0E-10d) {
            d3 = 0.0d;
        } else {
            double d6 = this.f500c;
            if (this.spherical) {
                d4 = Math.tan((d2 * 0.5d) + 0.7853981633974483d);
                d5 = -this.f501n;
            } else {
                d4 = ProjectionMath.tsfn(d2, Math.sin(d2), this.f538e);
                d5 = this.f501n;
            }
            d3 = Math.pow(d4, d5) * d6;
        }
        double d7 = this.scaleFactor;
        double d8 = d * this.f501n;
        projCoordinate.f409x = d7 * Math.sin(d8) * d3;
        projCoordinate.f410y = this.scaleFactor * (this.rho0 - (d3 * Math.cos(d8)));
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double d3 = d / this.scaleFactor;
        double d4 = this.rho0 - (d2 / this.scaleFactor);
        double distance = ProjectionMath.distance(d3, d4);
        if (distance != 0.0d) {
            if (this.f501n < 0.0d) {
                distance = -distance;
                d3 = -d3;
                d4 = -d4;
            }
            if (this.spherical) {
                projCoordinate2.f410y = (Math.atan(Math.pow(this.f500c / distance, 1.0d / this.f501n)) * 2.0d) - 1.5707963267948966d;
            } else {
                projCoordinate2.f410y = ProjectionMath.phi2(Math.pow(distance / this.f500c, 1.0d / this.f501n), this.f538e);
            }
            projCoordinate2.f409x = Math.atan2(d3, d4) / this.f501n;
        } else {
            projCoordinate2.f409x = 0.0d;
            projCoordinate2.f410y = this.f501n > 0.0d ? 1.5707963267948966d : -1.5707963267948966d;
        }
        return projCoordinate2;
    }

    public void initialize() {
        super.initialize();
        if (this.projectionLatitude1 == 0.0d) {
            double d = this.projectionLatitude;
            this.projectionLatitude2 = d;
            this.projectionLatitude1 = d;
        }
        if (Math.abs(this.projectionLatitude1 + this.projectionLatitude2) >= 1.0E-10d) {
            double sin = Math.sin(this.projectionLatitude1);
            this.f501n = sin;
            double cos = Math.cos(this.projectionLatitude1);
            boolean z = true;
            boolean z2 = Math.abs(this.projectionLatitude1 - this.projectionLatitude2) >= 1.0E-10d;
            if (this.f539es != 0.0d) {
                z = false;
            }
            this.spherical = z;
            if (!this.spherical) {
                double msfn = ProjectionMath.msfn(sin, cos, this.f539es);
                double tsfn = ProjectionMath.tsfn(this.projectionLatitude1, sin, this.f538e);
                if (z2) {
                    double sin2 = Math.sin(this.projectionLatitude2);
                    double log = Math.log(msfn / ProjectionMath.msfn(sin2, Math.cos(this.projectionLatitude2), this.f539es));
                    this.f501n = log;
                    this.f501n = log / Math.log(tsfn / ProjectionMath.tsfn(this.projectionLatitude2, sin2, this.f538e));
                }
                double pow = (msfn * Math.pow(tsfn, -this.f501n)) / this.f501n;
                this.rho0 = pow;
                this.f500c = pow;
                this.rho0 = pow * (Math.abs(Math.abs(this.projectionLatitude) - 1.5707963267948966d) < 1.0E-10d ? 0.0d : Math.pow(ProjectionMath.tsfn(this.projectionLatitude, Math.sin(this.projectionLatitude), this.f538e), this.f501n));
                return;
            }
            if (z2) {
                this.f501n = Math.log(cos / Math.cos(this.projectionLatitude2)) / Math.log(Math.tan((this.projectionLatitude2 * 0.5d) + 0.7853981633974483d) / Math.tan((this.projectionLatitude1 * 0.5d) + 0.7853981633974483d));
            }
            this.f500c = (cos * Math.pow(Math.tan((this.projectionLatitude1 * 0.5d) + 0.7853981633974483d), this.f501n)) / this.f501n;
            this.rho0 = Math.abs(Math.abs(this.projectionLatitude) - 1.5707963267948966d) < 1.0E-10d ? 0.0d : this.f500c * Math.pow(Math.tan((this.projectionLatitude * 0.5d) + 0.7853981633974483d), -this.f501n);
            return;
        }
        throw new ProjectionException();
    }
}
