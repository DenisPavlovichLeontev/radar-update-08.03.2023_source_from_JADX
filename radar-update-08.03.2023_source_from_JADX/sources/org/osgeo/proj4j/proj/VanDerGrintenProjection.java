package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;

public class VanDerGrintenProjection extends Projection {
    private static final double C2_27 = 0.07407407407407407d;
    private static final double HPISQ = 4.934802200544679d;
    private static final double PI4_3 = 4.188790204786391d;
    private static final double PISQ = 9.869604401089358d;
    private static final double THIRD = 0.3333333333333333d;
    private static final double TOL = 1.0E-10d;
    private static final double TPISQ = 19.739208802178716d;
    private static final double TWO_THRD = 0.6666666666666666d;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "van der Grinten (I)";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double abs = Math.abs(d2 / 1.5707963267948966d);
        if (abs - 1.0E-10d <= 1.0d) {
            if (abs > 1.0d) {
                abs = 1.0d;
            }
            if (Math.abs(d2) <= 1.0E-10d) {
                projCoordinate2.f409x = d3;
                projCoordinate2.f410y = 0.0d;
            } else {
                double d4 = 3.141592653589793d;
                if (Math.abs(d) <= 1.0E-10d || Math.abs(abs - 1.0d) < 1.0E-10d) {
                    projCoordinate2.f409x = 0.0d;
                    projCoordinate2.f410y = Math.tan(Math.asin(abs) * 0.5d) * 3.141592653589793d;
                    if (d2 < 0.0d) {
                        projCoordinate2.f410y = -projCoordinate2.f410y;
                    }
                } else {
                    double abs2 = Math.abs((3.141592653589793d / d3) - (d3 / 3.141592653589793d)) * 0.5d;
                    double d5 = abs2 * abs2;
                    double sqrt = Math.sqrt(1.0d - (abs * abs));
                    double d6 = sqrt / ((abs + sqrt) - 1.0d);
                    double d7 = ((2.0d / abs) - 1.0d) * d6;
                    double d8 = d7 * d7;
                    projCoordinate2.f409x = d6 - d8;
                    double d9 = d8 + d5;
                    String str = "F";
                    projCoordinate2.f409x = (((projCoordinate2.f409x * abs2) + Math.sqrt(((d5 * projCoordinate2.f409x) * projCoordinate2.f409x) - (((d6 * d6) - d8) * d9))) * 3.141592653589793d) / d9;
                    if (d3 < 0.0d) {
                        projCoordinate2.f409x = -projCoordinate2.f409x;
                    }
                    projCoordinate2.f410y = Math.abs(projCoordinate2.f409x / 3.141592653589793d);
                    projCoordinate2.f410y = 1.0d - (projCoordinate2.f410y * (projCoordinate2.f410y + (abs2 * 2.0d)));
                    if (projCoordinate2.f410y < -1.0E-10d) {
                        throw new ProjectionException(str);
                    } else if (projCoordinate2.f410y < 0.0d) {
                        projCoordinate2.f410y = 0.0d;
                    } else {
                        double sqrt2 = Math.sqrt(projCoordinate2.f410y);
                        if (d2 < 0.0d) {
                            d4 = -3.141592653589793d;
                        }
                        projCoordinate2.f410y = sqrt2 * d4;
                    }
                }
            }
            return projCoordinate2;
        }
        throw new ProjectionException("F");
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double d4 = d * d;
        double abs = Math.abs(d2);
        double d5 = 0.0d;
        if (abs < 1.0E-10d) {
            projCoordinate2.f410y = 0.0d;
            double d6 = (d4 * d4) + ((HPISQ + d4) * TPISQ);
            if (Math.abs(d) > 1.0E-10d) {
                d5 = (((d4 - PISQ) + Math.sqrt(d6)) * 0.5d) / d;
            }
            projCoordinate2.f409x = d5;
            return projCoordinate2;
        }
        double d7 = d2 * d2;
        double d8 = d4 + d7;
        double d9 = d8 * d8;
        double d10 = -3.141592653589793d * abs * (d8 + PISQ);
        double d11 = d9 + (((abs * d8) + ((d7 + ((abs + 1.5707963267948966d) * 3.141592653589793d)) * 3.141592653589793d)) * 6.283185307179586d);
        double d12 = abs * 3.141592653589793d;
        double d13 = (d10 + ((d8 - (d7 * 3.0d)) * PISQ)) / d11;
        double d14 = d13 * 0.3333333333333333d;
        double d15 = (d10 / d11) - (d14 * d13);
        double sqrt = Math.sqrt(-0.3333333333333333d * d15) * 2.0d;
        double d16 = (((((C2_27 * d13) * d13) * d13) + (((d12 * d12) - (d10 * d14)) / d11)) * 3.0d) / (d15 * sqrt);
        double abs2 = Math.abs(d16);
        if (abs2 - 1.0E-10d <= 1.0d) {
            if (abs2 > 1.0d) {
                d3 = d16 > 0.0d ? 0.0d : 3.141592653589793d;
            } else {
                d3 = Math.acos(d16);
            }
            projCoordinate2.f410y = ((sqrt * Math.cos((d3 * 0.3333333333333333d) + PI4_3)) - d14) * 3.141592653589793d;
            if (d2 < 0.0d) {
                projCoordinate2.f410y = -projCoordinate2.f410y;
            }
            double d17 = d9 + (((d4 - d7) + HPISQ) * TPISQ);
            if (Math.abs(d) > 1.0E-10d) {
                double d18 = d8 - PISQ;
                if (d17 > 0.0d) {
                    d5 = Math.sqrt(d17);
                }
                d5 = ((d18 + d5) * 0.5d) / d;
            }
            projCoordinate2.f409x = d5;
            return projCoordinate2;
        }
        throw new ProjectionException("I");
    }
}
