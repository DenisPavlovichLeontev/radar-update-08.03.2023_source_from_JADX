package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class ObliqueStereographicAlternativeProjection extends GaussProjection {

    /* renamed from: R2 */
    private double f528R2;
    private double cosc0;
    private double sinc0;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Oblique Stereographic Alternative";
    }

    public ProjCoordinate OLDproject(double d, double d2, ProjCoordinate projCoordinate) {
        super.project(d, d2, projCoordinate);
        double d3 = projCoordinate.f409x;
        double d4 = projCoordinate.f410y;
        double sin = Math.sin(d4);
        double cos = Math.cos(d4);
        double cos2 = Math.cos(d3);
        double d5 = (this.scaleFactor * this.f528R2) / (((this.sinc0 * sin) + 1.0d) + ((this.cosc0 * cos) * cos2));
        projCoordinate.f409x = d5 * cos * Math.sin(d3);
        projCoordinate.f410y = d5 * ((this.cosc0 * sin) - ((this.sinc0 * cos) * cos2));
        return projCoordinate;
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        super.project(d, d2, projCoordinate);
        double d3 = projCoordinate.f409x;
        double d4 = projCoordinate.f410y;
        double sin = Math.sin(d4);
        double cos = Math.cos(d4);
        double cos2 = Math.cos(d3);
        double d5 = (this.scaleFactor * this.f528R2) / (((this.sinc0 * sin) + 1.0d) + ((this.cosc0 * cos) * cos2));
        projCoordinate.f409x = d5 * cos * Math.sin(d3);
        projCoordinate.f410y = d5 * ((this.cosc0 * sin) - ((this.sinc0 * cos) * cos2));
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        double d5 = d / this.scaleFactor;
        double d6 = d2 / this.scaleFactor;
        double sqrt = Math.sqrt((d5 * d5) + (d6 * d6));
        if (sqrt != 0.0d) {
            double atan2 = Math.atan2(sqrt, this.f528R2) * 2.0d;
            double sin = Math.sin(atan2);
            double cos = Math.cos(atan2);
            d3 = Math.asin((this.sinc0 * cos) + (((d6 * sin) * this.cosc0) / sqrt));
            d4 = Math.atan2(d5 * sin, ((sqrt * this.cosc0) * cos) - ((this.sinc0 * d6) * sin));
        } else {
            d3 = this.phic0;
            d4 = 0.0d;
        }
        return super.projectInverse(d4, d3, projCoordinate);
    }

    public void initialize() {
        super.initialize();
        this.sinc0 = Math.sin(this.phic0);
        this.cosc0 = Math.cos(this.phic0);
        this.f528R2 = this.f487rc * 2.0d;
    }
}
