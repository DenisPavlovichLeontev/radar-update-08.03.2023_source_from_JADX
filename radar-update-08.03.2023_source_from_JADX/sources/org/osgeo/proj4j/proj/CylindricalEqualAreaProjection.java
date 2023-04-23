package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class CylindricalEqualAreaProjection extends Projection {
    private double[] apa;

    /* renamed from: qp */
    private double f459qp;
    private double trueScaleLatitude;

    public boolean hasInverse() {
        return true;
    }

    public boolean isRectilinear() {
        return true;
    }

    public CylindricalEqualAreaProjection() {
        this(0.0d, 0.0d, 0.0d);
    }

    public CylindricalEqualAreaProjection(double d, double d2, double d3) {
        this.projectionLatitude = d;
        this.projectionLongitude = d2;
        this.trueScaleLatitude = d3;
        initialize();
    }

    public void initialize() {
        super.initialize();
        double d = this.trueScaleLatitude;
        this.scaleFactor = Math.cos(d);
        if (this.f539es != 0.0d) {
            double sin = Math.sin(d);
            this.scaleFactor /= Math.sqrt(1.0d - ((this.f539es * sin) * sin));
            this.apa = ProjectionMath.authset(this.f539es);
            this.f459qp = ProjectionMath.qsfn(1.0d, this.f538e, this.one_es);
        }
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        if (this.spherical) {
            projCoordinate.f409x = this.scaleFactor * d;
            projCoordinate.f410y = Math.sin(d2) / this.scaleFactor;
        } else {
            projCoordinate.f409x = this.scaleFactor * d;
            projCoordinate.f410y = (ProjectionMath.qsfn(Math.sin(d2), this.f538e, this.one_es) * 0.5d) / this.scaleFactor;
        }
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        if (this.spherical) {
            double d3 = d2 * this.scaleFactor;
            double abs = Math.abs(d3);
            if (abs - 1.0E-10d <= 1.0d) {
                if (abs >= 1.0d) {
                    projCoordinate.f410y = d3 < 0.0d ? -1.5707963267948966d : 1.5707963267948966d;
                } else {
                    projCoordinate.f410y = Math.asin(d3);
                }
                projCoordinate.f409x = d / this.scaleFactor;
            } else {
                throw new ProjectionException();
            }
        } else {
            projCoordinate.f410y = ProjectionMath.authlat(Math.asin(((d2 * 2.0d) * this.scaleFactor) / this.f459qp), this.apa);
            projCoordinate.f409x = d / this.scaleFactor;
        }
        return projCoordinate;
    }
}
