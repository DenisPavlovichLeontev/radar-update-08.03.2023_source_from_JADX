package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.util.ProjectionMath;

public class EquidistantConicProjection extends ConicProjection {
    private double eccentricity = 0.822719d;
    private double eccentricity2;
    private double eccentricity4;
    private double eccentricity6;

    /* renamed from: f */
    private double f479f;

    /* renamed from: n */
    private double f480n;
    private boolean northPole;
    private double radius;
    private double rho0;
    private double standardLatitude1;
    private double standardLatitude2;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Equidistant Conic";
    }

    public EquidistantConicProjection() {
        double d = 0.822719d * 0.822719d;
        this.eccentricity2 = d;
        double d2 = d * d;
        this.eccentricity4 = d2;
        this.eccentricity6 = d * d2;
        this.radius = 1.0d;
        this.minLatitude = ProjectionMath.degToRad(10.0d);
        this.maxLatitude = ProjectionMath.degToRad(70.0d);
        this.minLongitude = ProjectionMath.degToRad(-90.0d);
        this.maxLongitude = ProjectionMath.degToRad(90.0d);
        this.standardLatitude1 = Math.toDegrees(60.0d);
        this.standardLatitude2 = Math.toDegrees(20.0d);
        initialize(ProjectionMath.degToRad(0.0d), ProjectionMath.degToRad(37.5d), this.standardLatitude1, this.standardLatitude2);
    }

    public ProjCoordinate project(ProjCoordinate projCoordinate, ProjCoordinate projCoordinate2) {
        double normalizeLongitude = ProjectionMath.normalizeLongitude(projCoordinate.f409x - this.projectionLongitude);
        double d = projCoordinate.f410y;
        double pow = Math.pow((1.0d - (this.eccentricity * Math.sin(d))) / ((this.eccentricity * Math.sin(d)) + 1.0d), this.eccentricity * 0.5d);
        double tan = Math.tan(0.7853981633974483d - (d * 0.5d));
        double d2 = 0.0d;
        if (tan != 0.0d) {
            d2 = Math.pow(tan / pow, this.f480n);
        }
        double d3 = this.radius * this.f479f * d2;
        double d4 = this.f480n * normalizeLongitude;
        projCoordinate2.f409x = Math.sin(d4) * d3;
        projCoordinate2.f410y = this.rho0 - (d3 * Math.cos(d4));
        return projCoordinate2;
    }

    public ProjCoordinate inverseProject(ProjCoordinate projCoordinate, ProjCoordinate projCoordinate2) {
        ProjCoordinate projCoordinate3 = projCoordinate;
        ProjCoordinate projCoordinate4 = projCoordinate2;
        projCoordinate4.f409x = (Math.atan(projCoordinate3.f409x / (this.rho0 - projCoordinate3.f410y)) / this.f480n) + this.projectionLongitude;
        double sqrt = Math.sqrt((projCoordinate3.f409x * projCoordinate3.f409x) + ((this.rho0 - projCoordinate3.f410y) * (this.rho0 - projCoordinate3.f410y)));
        double d = this.f480n;
        double d2 = 0.0d;
        if (d < 0.0d) {
            sqrt = -sqrt;
        }
        double d3 = 1.0d;
        double pow = Math.pow(sqrt / (this.radius * this.f479f), 1.0d / d);
        double atan = 1.5707963267948966d - (Math.atan(pow) * 2.0d);
        int i = 0;
        double d4 = 1.0d;
        while (i < 100 && d4 > 1.0E-8d) {
            d2 = 1.5707963267948966d - (Math.atan(Math.pow((d3 - (this.eccentricity * Math.sin(atan))) / ((this.eccentricity * Math.sin(atan)) + d3), this.eccentricity * 0.5d) * pow) * 2.0d);
            i++;
            d4 = Math.abs(Math.abs(atan) - Math.abs(d2));
            atan = d2;
            d3 = 1.0d;
        }
        projCoordinate4.f410y = d2;
        return projCoordinate4;
    }

    private void initialize(double d, double d2, double d3, double d4) {
        super.initialize();
        boolean z = d2 > 0.0d;
        this.northPole = z;
        this.projectionLatitude = z ? 1.5707963267948966d : -1.5707963267948966d;
        double tan = Math.tan(0.7853981633974483d - (d3 * 0.5d)) / Math.pow((1.0d - (this.eccentricity * Math.sin(d3))) / ((this.eccentricity * Math.sin(d3)) + 1.0d), this.eccentricity * 0.5d);
        double cos = Math.cos(d3) / Math.sqrt(1.0d - (this.eccentricity2 * Math.pow(Math.sin(d3), 2.0d)));
        double tan2 = Math.tan(0.7853981633974483d - (d4 * 0.5d)) / Math.pow((1.0d - (this.eccentricity * Math.sin(d4))) / ((this.eccentricity * Math.sin(d4)) + 1.0d), this.eccentricity * 0.5d);
        double cos2 = Math.cos(d4) / Math.sqrt(1.0d - (this.eccentricity2 * Math.pow(Math.sin(d4), 2.0d)));
        double tan3 = Math.tan(0.7853981633974483d - (d2 * 0.5d)) / Math.pow((1.0d - (this.eccentricity * Math.sin(d2))) / ((this.eccentricity * Math.sin(d2)) + 1.0d), this.eccentricity * 0.5d);
        if (d3 != d4) {
            this.f480n = (Math.log(cos) - Math.log(cos2)) / (Math.log(tan) - Math.log(tan2));
        } else {
            this.f480n = Math.sin(d3);
        }
        double d5 = this.f480n;
        this.f479f = cos / (d5 * Math.pow(tan, d5));
        this.projectionLongitude = d;
        this.rho0 = this.radius * this.f479f * Math.pow(tan3, this.f480n);
    }
}
