package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class BonneProjection extends Projection {
    private double am1;
    private double cphi1;

    /* renamed from: en */
    private double[] f438en;

    /* renamed from: m1 */
    private double f439m1;
    private double phi1;

    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Bonne";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        if (this.spherical) {
            double d3 = (this.cphi1 + this.phi1) - d2;
            if (Math.abs(d3) > 1.0E-10d) {
                double cos = (Math.cos(d2) * d) / d3;
                projCoordinate2.f409x = Math.sin(cos) * d3;
                projCoordinate2.f410y = this.cphi1 - (d3 * Math.cos(cos));
            } else {
                projCoordinate2.f410y = 0.0d;
                projCoordinate2.f409x = 0.0d;
            }
        } else {
            double d4 = this.am1 + this.f439m1;
            double sin = Math.sin(d2);
            double cos2 = Math.cos(d2);
            double mlfn = d4 - ProjectionMath.mlfn(d2, sin, cos2, this.f438en);
            double sqrt = (cos2 * d) / (Math.sqrt(1.0d - ((this.f539es * sin) * sin)) * mlfn);
            projCoordinate2.f409x = Math.sin(sqrt) * mlfn;
            projCoordinate2.f410y = this.am1 - (mlfn * Math.cos(sqrt));
        }
        return projCoordinate2;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d;
        ProjCoordinate projCoordinate2 = projCoordinate;
        if (this.spherical) {
            double d4 = this.cphi1 - d2;
            projCoordinate2.f410y = d4;
            double distance = ProjectionMath.distance(d3, d4);
            projCoordinate2.f410y = (this.cphi1 + this.phi1) - distance;
            if (Math.abs(projCoordinate2.f410y) > 1.5707963267948966d) {
                throw new ProjectionException("I");
            } else if (Math.abs(Math.abs(projCoordinate2.f410y) - 1.5707963267948966d) <= 1.0E-10d) {
                projCoordinate2.f409x = 0.0d;
            } else {
                projCoordinate2.f409x = (distance * Math.atan2(d, d2)) / Math.cos(projCoordinate2.f410y);
            }
        } else {
            double d5 = this.am1 - d2;
            projCoordinate2.f410y = d5;
            double distance2 = ProjectionMath.distance(d3, d5);
            projCoordinate2.f410y = ProjectionMath.inv_mlfn((this.am1 + this.f439m1) - distance2, this.f539es, this.f438en);
            double abs = Math.abs(projCoordinate2.f410y);
            if (abs < 1.5707963267948966d) {
                double sin = Math.sin(projCoordinate2.f410y);
                projCoordinate2.f409x = ((distance2 * Math.atan2(d, d2)) * Math.sqrt(1.0d - ((this.f539es * sin) * sin))) / Math.cos(projCoordinate2.f410y);
            } else if (Math.abs(abs - 1.5707963267948966d) <= 1.0E-10d) {
                projCoordinate2.f409x = 0.0d;
            } else {
                throw new ProjectionException("I");
            }
        }
        return projCoordinate2;
    }

    public void initialize() {
        super.initialize();
        this.phi1 = 1.5707963267948966d;
        if (Math.abs(1.5707963267948966d) < 1.0E-10d) {
            throw new ProjectionException("-23");
        } else if (!this.spherical) {
            this.f438en = ProjectionMath.enfn(this.f539es);
            double d = this.phi1;
            double sin = Math.sin(d);
            this.am1 = sin;
            double cos = Math.cos(this.phi1);
            this.f439m1 = ProjectionMath.mlfn(d, sin, cos, this.f438en);
            double d2 = this.f539es;
            double d3 = this.am1;
            this.am1 = cos / (Math.sqrt(1.0d - ((d2 * d3) * d3)) * this.am1);
        } else if (Math.abs(this.phi1) + 1.0E-10d >= 1.5707963267948966d) {
            this.cphi1 = 0.0d;
        } else {
            this.cphi1 = 1.0d / Math.tan(this.phi1);
        }
    }
}
