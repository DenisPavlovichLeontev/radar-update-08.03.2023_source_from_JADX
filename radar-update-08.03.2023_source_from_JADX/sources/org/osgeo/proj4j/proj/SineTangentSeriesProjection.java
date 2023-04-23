package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.util.ProjectionMath;

class SineTangentSeriesProjection extends ConicProjection {
    private double C_p;
    private double C_x;
    private double C_y;
    private boolean tan_mode;

    public boolean hasInverse() {
        return true;
    }

    protected SineTangentSeriesProjection(double d, double d2, boolean z) {
        this.f539es = 0.0d;
        this.C_x = d2 / d;
        this.C_y = d;
        this.C_p = 1.0d / d2;
        this.tan_mode = z;
        initialize();
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = this.C_x * d * Math.cos(d2);
        projCoordinate.f410y = this.C_y;
        double d3 = d2 * this.C_p;
        double cos = Math.cos(d3);
        if (this.tan_mode) {
            projCoordinate.f409x *= cos * cos;
            projCoordinate.f410y *= Math.tan(d3);
        } else {
            projCoordinate.f409x /= cos;
            projCoordinate.f410y *= Math.sin(d3);
        }
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d2 / this.C_y;
        double atan = this.tan_mode ? Math.atan(d3) : ProjectionMath.asin(d3);
        projCoordinate.f410y = atan;
        double cos = Math.cos(atan);
        projCoordinate.f410y /= this.C_p;
        projCoordinate.f409x = d / (this.C_x * Math.cos(projCoordinate.f410y));
        if (this.tan_mode) {
            projCoordinate.f409x /= cos * cos;
        } else {
            projCoordinate.f409x *= cos;
        }
        return projCoordinate;
    }
}
