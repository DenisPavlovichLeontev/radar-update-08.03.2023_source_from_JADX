package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class GnomonicAzimuthalProjection extends AzimuthalProjection {
    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Gnomonic Azimuthal";
    }

    public GnomonicAzimuthalProjection() {
        this(Math.toRadians(90.0d), Math.toRadians(0.0d));
    }

    public GnomonicAzimuthalProjection(double d, double d2) {
        super(d, d2);
        this.minLatitude = Math.toRadians(0.0d);
        this.maxLatitude = Math.toRadians(90.0d);
        initialize();
    }

    public void initialize() {
        super.initialize();
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double sin = Math.sin(d2);
        double cos = Math.cos(d2);
        double cos2 = Math.cos(d);
        int i = this.mode;
        if (i == 1) {
            projCoordinate2.f410y = sin;
        } else if (i == 2) {
            projCoordinate2.f410y = -sin;
        } else if (i == 3) {
            projCoordinate2.f410y = cos * cos2;
        } else if (i == 4) {
            projCoordinate2.f410y = (this.sinphi0 * sin) + (this.cosphi0 * cos * cos2);
        }
        if (Math.abs(projCoordinate2.f410y) > 1.0E-10d) {
            double d3 = 1.0d / projCoordinate2.f410y;
            projCoordinate2.f410y = d3;
            projCoordinate2.f409x = d3 * cos * Math.sin(d);
            int i2 = this.mode;
            if (i2 == 1) {
                cos2 = -cos2;
            } else if (i2 != 2) {
                if (i2 == 3) {
                    projCoordinate2.f410y *= sin;
                } else if (i2 == 4) {
                    projCoordinate2.f410y *= (this.cosphi0 * sin) - ((this.sinphi0 * cos) * cos2);
                }
                return projCoordinate2;
            }
            projCoordinate2.f410y *= cos * cos2;
            return projCoordinate2;
        }
        throw new ProjectionException();
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        double d5 = d2;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double distance = ProjectionMath.distance(d, d2);
        double atan = Math.atan(distance);
        projCoordinate2.f410y = atan;
        double sin = Math.sin(atan);
        double sqrt = Math.sqrt(1.0d - (sin * sin));
        if (Math.abs(distance) <= 1.0E-10d) {
            projCoordinate2.f410y = this.projectionLatitude;
            projCoordinate2.f409x = 0.0d;
        } else {
            int i = this.mode;
            if (i == 1) {
                projCoordinate2.f410y = 1.5707963267948966d - projCoordinate2.f410y;
                d5 = -d5;
            } else if (i != 2) {
                if (i == 3) {
                    projCoordinate2.f410y = (d5 * sin) / distance;
                    if (Math.abs(projCoordinate2.f410y) >= 1.0d) {
                        projCoordinate2.f410y = projCoordinate2.f410y > 0.0d ? 1.5707963267948966d : -1.5707963267948966d;
                    } else {
                        projCoordinate2.f410y = Math.asin(projCoordinate2.f410y);
                    }
                    d4 = sqrt * distance;
                } else if (i == 4) {
                    projCoordinate2.f410y = (this.sinphi0 * sqrt) + (((d5 * sin) * this.cosphi0) / distance);
                    if (Math.abs(projCoordinate2.f410y) >= 1.0d) {
                        projCoordinate2.f410y = projCoordinate2.f410y > 0.0d ? 1.5707963267948966d : -1.5707963267948966d;
                    } else {
                        projCoordinate2.f410y = Math.asin(projCoordinate2.f410y);
                    }
                    d4 = (sqrt - (this.sinphi0 * Math.sin(projCoordinate2.f410y))) * distance;
                    sin *= this.cosphi0;
                }
                d3 = d * sin;
                projCoordinate2.f409x = Math.atan2(d3, d4);
            } else {
                projCoordinate2.f410y -= 1.5707963267948966d;
            }
            d3 = d;
            projCoordinate2.f409x = Math.atan2(d3, d4);
        }
        return projCoordinate2;
    }
}
