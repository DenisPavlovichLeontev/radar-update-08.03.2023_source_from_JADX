package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class UrmaevFlatPolarSinusoidalProjection extends Projection {
    private static final double C_x = 0.8773826753d;

    /* renamed from: Cy */
    private static final double f554Cy = 1.139753528477d;
    private double C_y;

    /* renamed from: n */
    private double f555n = 0.8660254037844386d;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Urmaev Flat-Polar Sinusoidal";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = ProjectionMath.asin(this.f555n * Math.sin(d2));
        projCoordinate.f409x = d * C_x * Math.cos(d2);
        projCoordinate.f410y = this.C_y * d2;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d2 / this.C_y;
        projCoordinate.f410y = ProjectionMath.asin(Math.sin(d3) / this.f555n);
        projCoordinate.f409x = d / (Math.cos(d3) * C_x);
        return projCoordinate;
    }

    public void initialize() {
        super.initialize();
        double d = this.f555n;
        if (d <= 0.0d || d > 1.0d) {
            throw new ProjectionException("-40");
        }
        this.C_y = f554Cy / d;
    }

    public void setN(double d) {
        this.f555n = d;
    }

    public double getN() {
        return this.f555n;
    }
}
