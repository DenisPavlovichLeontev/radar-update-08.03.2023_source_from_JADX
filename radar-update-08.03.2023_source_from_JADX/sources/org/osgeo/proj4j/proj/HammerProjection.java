package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class HammerProjection extends PseudoCylindricalProjection {

    /* renamed from: m */
    private double f490m = 1.0d;

    /* renamed from: rm */
    private double f491rm;

    /* renamed from: w */
    private double f492w = 0.5d;

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Hammer & Eckert-Greifendorff";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double cos = Math.cos(d2);
        double d3 = d * this.f492w;
        double sqrt = Math.sqrt(2.0d / ((Math.cos(d3) * cos) + 1.0d));
        projCoordinate.f409x = this.f490m * sqrt * cos * Math.sin(d3);
        projCoordinate.f410y = this.f491rm * sqrt * Math.sin(d2);
        return projCoordinate;
    }

    public void initialize() {
        super.initialize();
        double abs = Math.abs(this.f492w);
        this.f492w = abs;
        if (abs > 0.0d) {
            this.f492w = 0.5d;
            double abs2 = Math.abs(this.f490m);
            this.f490m = abs2;
            if (abs2 > 0.0d) {
                this.f491rm = 1.0d / 1.0d;
                this.f490m = 1.0d / this.f492w;
                this.f539es = 0.0d;
                return;
            }
            throw new ProjectionException("-27");
        }
        throw new ProjectionException("-27");
    }

    public void setW(double d) {
        this.f492w = d;
    }

    public double getW() {
        return this.f492w;
    }

    public void setM(double d) {
        this.f490m = d;
    }

    public double getM() {
        return this.f490m;
    }
}
