package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class TranverseCentralCylindricalProjection extends CylindricalProjection {
    public boolean isRectilinear() {
        return false;
    }

    public String toString() {
        return "Transverse Central Cylindrical";
    }

    public TranverseCentralCylindricalProjection() {
        this.minLongitude = ProjectionMath.degToRad(-60.0d);
        this.maxLongitude = ProjectionMath.degToRad(60.0d);
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double cos = Math.cos(d2) * Math.sin(d);
        double d3 = 1.0d - (cos * cos);
        if (d3 >= 1.0E-10d) {
            projCoordinate.f409x = cos / Math.sqrt(d3);
            projCoordinate.f410y = Math.atan2(Math.tan(d2), Math.cos(d));
            return projCoordinate;
        }
        throw new ProjectionException("F");
    }
}
