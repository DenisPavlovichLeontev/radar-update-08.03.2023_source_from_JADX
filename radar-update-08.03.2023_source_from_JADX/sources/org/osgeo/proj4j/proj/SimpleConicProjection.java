package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class SimpleConicProjection extends ConicProjection {
    private static final double EPS = 1.0E-10d;
    private static final double EPS10 = 1.0E-10d;
    public static final int EULER = 0;
    public static final int MURD1 = 1;
    public static final int MURD2 = 2;
    public static final int MURD3 = 3;
    public static final int PCONIC = 4;
    public static final int TISSOT = 5;
    public static final int VITK1 = 6;

    /* renamed from: c1 */
    private double f547c1;

    /* renamed from: c2 */
    private double f548c2;

    /* renamed from: n */
    private double f549n;
    private double rho_0;
    private double rho_c;
    private double sig;
    private int type;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Simple Conic";
    }

    public SimpleConicProjection() {
        this(0);
    }

    public SimpleConicProjection(int i) {
        this.type = i;
        this.minLatitude = Math.toRadians(0.0d);
        this.maxLatitude = Math.toRadians(80.0d);
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        int i = this.type;
        if (i == 2) {
            d3 = this.rho_c + Math.tan(this.sig - d2);
        } else if (i != 4) {
            d3 = this.rho_c - d2;
        } else {
            d3 = this.f548c2 * (this.f547c1 - Math.tan(d2 - this.sig));
        }
        double d4 = d * this.f549n;
        projCoordinate.f409x = Math.sin(d4) * d3;
        projCoordinate.f410y = this.rho_0 - (d3 * Math.cos(d4));
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = this.rho_0 - d2;
        projCoordinate.f410y = d3;
        double distance = ProjectionMath.distance(d, d3);
        if (this.f549n < 0.0d) {
            distance = -distance;
            projCoordinate.f409x = -d;
            projCoordinate.f410y = -d2;
        }
        projCoordinate.f409x = Math.atan2(d, d2) / this.f549n;
        int i = this.type;
        if (i == 2) {
            projCoordinate.f410y = this.sig - Math.atan(distance - this.rho_c);
        } else if (i != 4) {
            projCoordinate.f410y = this.rho_c - distance;
        } else {
            projCoordinate.f410y = Math.atan(this.f547c1 - (distance / this.f548c2)) + this.sig;
        }
        return projCoordinate;
    }

    public void initialize() {
        super.initialize();
        double radians = Math.toRadians(30.0d);
        double radians2 = Math.toRadians(60.0d);
        double d = (radians2 - radians) * 0.5d;
        this.sig = (radians2 + radians) * 0.5d;
        int i = (Math.abs(d) < 1.0E-10d || Math.abs(this.sig) < 1.0E-10d) ? -42 : 0;
        if (i == 0) {
            switch (this.type) {
                case 0:
                    this.f549n = (Math.sin(this.sig) * Math.sin(d)) / d;
                    double d2 = d * 0.5d;
                    double tan = (d2 / (Math.tan(d2) * Math.tan(this.sig))) + this.sig;
                    this.rho_c = tan;
                    this.rho_0 = tan - this.projectionLatitude;
                    return;
                case 1:
                    double sin = (Math.sin(d) / (d * Math.tan(this.sig))) + this.sig;
                    this.rho_c = sin;
                    this.rho_0 = sin - this.projectionLatitude;
                    this.f549n = Math.sin(this.sig);
                    return;
                case 2:
                    double sqrt = Math.sqrt(Math.cos(d));
                    double tan2 = sqrt / Math.tan(this.sig);
                    this.rho_c = tan2;
                    this.rho_0 = tan2 + Math.tan(this.sig - this.projectionLatitude);
                    this.f549n = Math.sin(this.sig) * sqrt;
                    return;
                case 3:
                    double tan3 = (d / (Math.tan(this.sig) * Math.tan(d))) + this.sig;
                    this.rho_c = tan3;
                    this.rho_0 = tan3 - this.projectionLatitude;
                    this.f549n = ((Math.sin(this.sig) * Math.sin(d)) * Math.tan(d)) / (d * d);
                    return;
                case 4:
                    this.f549n = Math.sin(this.sig);
                    this.f548c2 = Math.cos(d);
                    this.f547c1 = 1.0d / Math.tan(this.sig);
                    double d3 = this.projectionLatitude - this.sig;
                    if (Math.abs(d3) - 1.0E-10d < 1.5707963267948966d) {
                        this.rho_0 = this.f548c2 * (this.f547c1 - Math.tan(d3));
                        this.maxLatitude = Math.toRadians(60.0d);
                        return;
                    }
                    throw new ProjectionException("-43");
                case 5:
                    this.f549n = Math.sin(this.sig);
                    double cos = Math.cos(d);
                    double d4 = this.f549n;
                    double d5 = (d4 / cos) + (cos / d4);
                    this.rho_c = d5;
                    this.rho_0 = Math.sqrt((d5 - (Math.sin(this.projectionLatitude) * 2.0d)) / this.f549n);
                    return;
                case 6:
                    double tan4 = Math.tan(d);
                    this.f549n = (Math.sin(this.sig) * tan4) / d;
                    double tan5 = (d / (tan4 * Math.tan(this.sig))) + this.sig;
                    this.rho_c = tan5;
                    this.rho_0 = tan5 - this.projectionLatitude;
                    return;
                default:
                    return;
            }
        } else {
            throw new ProjectionException("Error " + i);
        }
    }
}
