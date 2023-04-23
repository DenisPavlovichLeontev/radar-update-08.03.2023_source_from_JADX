package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class CentralCylindricalProjection extends CylindricalProjection {
    private static final double EPS10 = 1.0E-10d;

    /* renamed from: ap */
    private double f456ap;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Central Cylindrical";
    }

    public CentralCylindricalProjection() {
        this.minLatitude = Math.toRadians(-80.0d);
        this.maxLatitude = Math.toRadians(80.0d);
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        if (Math.abs(Math.abs(d2) - 1.5707963267948966d) > 1.0E-10d) {
            projCoordinate.f409x = d;
            projCoordinate.f410y = Math.tan(d2);
            return projCoordinate;
        }
        throw new ProjectionException("F");
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = Math.atan(d2);
        projCoordinate.f409x = d;
        return projCoordinate;
    }
}
