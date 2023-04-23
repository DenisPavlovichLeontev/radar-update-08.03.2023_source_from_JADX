package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.util.ProjectionMath;

public class PutninsP4Projection extends Projection {
    protected double C_x = 0.874038744d;
    protected double C_y = 3.883251825d;

    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Putnins P4";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double asin = ProjectionMath.asin(Math.sin(d2) * 0.883883476d);
        projCoordinate.f409x = this.C_x * d * Math.cos(asin);
        double d3 = asin * 0.333333333333333d;
        projCoordinate.f409x /= Math.cos(d3);
        projCoordinate.f410y = this.C_y * Math.sin(d3);
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = ProjectionMath.asin(d2 / this.C_y);
        projCoordinate.f409x = (d * Math.cos(projCoordinate.f410y)) / this.C_x;
        projCoordinate.f410y *= 3.0d;
        projCoordinate.f409x /= Math.cos(projCoordinate.f410y);
        projCoordinate.f410y = ProjectionMath.asin(Math.sin(projCoordinate.f410y) * 1.13137085d);
        return projCoordinate;
    }
}
