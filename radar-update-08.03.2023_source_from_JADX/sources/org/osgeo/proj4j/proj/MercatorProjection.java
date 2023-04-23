package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.util.ProjectionMath;

public class MercatorProjection extends CylindricalProjection {
    public int getEPSGCode() {
        return 9804;
    }

    public boolean hasInverse() {
        return true;
    }

    public boolean isRectilinear() {
        return true;
    }

    public String toString() {
        return "Mercator";
    }

    public MercatorProjection() {
        this.minLatitude = ProjectionMath.degToRad(-85.0d);
        this.maxLatitude = ProjectionMath.degToRad(85.0d);
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        if (this.spherical) {
            projCoordinate.f409x = this.scaleFactor * d;
            projCoordinate.f410y = this.scaleFactor * Math.log(Math.tan((d2 * 0.5d) + 0.7853981633974483d));
        } else {
            projCoordinate.f409x = this.scaleFactor * d;
            projCoordinate.f410y = (-this.scaleFactor) * Math.log(ProjectionMath.tsfn(d2, Math.sin(d2), this.f538e));
        }
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        if (this.spherical) {
            projCoordinate.f410y = 1.5707963267948966d - (Math.atan(Math.exp((-d2) / this.scaleFactor)) * 2.0d);
            projCoordinate.f409x = d / this.scaleFactor;
        } else {
            projCoordinate.f410y = ProjectionMath.phi2(Math.exp((-d2) / this.scaleFactor), this.f538e);
            projCoordinate.f409x = d / this.scaleFactor;
        }
        return projCoordinate;
    }
}
