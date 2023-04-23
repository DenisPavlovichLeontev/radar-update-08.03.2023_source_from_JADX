package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class EquidistantAzimuthalProjection extends AzimuthalProjection {
    private static final double TOL = 1.0E-8d;

    /* renamed from: G */
    private double f473G;

    /* renamed from: He */
    private double f474He;

    /* renamed from: M1 */
    private double f475M1;

    /* renamed from: Mp */
    private double f476Mp;

    /* renamed from: N1 */
    private double f477N1;
    private double cosphi0;

    /* renamed from: en */
    private double[] f478en;
    private int mode;
    private double sinphi0;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Equidistant Azimuthal";
    }

    public EquidistantAzimuthalProjection() {
        this(Math.toRadians(90.0d), Math.toRadians(0.0d));
    }

    public EquidistantAzimuthalProjection(double d, double d2) {
        super(d, d2);
        initialize();
    }

    public Object clone() {
        EquidistantAzimuthalProjection equidistantAzimuthalProjection = (EquidistantAzimuthalProjection) super.clone();
        double[] dArr = this.f478en;
        if (dArr != null) {
            equidistantAzimuthalProjection.f478en = (double[]) dArr.clone();
        }
        return equidistantAzimuthalProjection;
    }

    public void initialize() {
        super.initialize();
        if (Math.abs(Math.abs(this.projectionLatitude) - 1.5707963267948966d) < 1.0E-10d) {
            this.mode = this.projectionLatitude < 0.0d ? 2 : 1;
            this.sinphi0 = this.projectionLatitude < 0.0d ? -1.0d : 1.0d;
            this.cosphi0 = 0.0d;
        } else if (Math.abs(this.projectionLatitude) < 1.0E-10d) {
            this.mode = 3;
            this.sinphi0 = 0.0d;
            this.cosphi0 = 1.0d;
        } else {
            this.mode = 4;
            this.sinphi0 = Math.sin(this.projectionLatitude);
            this.cosphi0 = Math.cos(this.projectionLatitude);
        }
        if (!this.spherical) {
            double[] enfn = ProjectionMath.enfn(this.f539es);
            this.f478en = enfn;
            int i = this.mode;
            if (i == 1) {
                this.f476Mp = ProjectionMath.mlfn(1.5707963267948966d, 1.0d, 0.0d, enfn);
            } else if (i == 2) {
                this.f476Mp = ProjectionMath.mlfn(-1.5707963267948966d, -1.0d, 0.0d, enfn);
            } else if (i == 3 || i == 4) {
                double d = this.f539es;
                double d2 = this.sinphi0;
                this.f477N1 = 1.0d / Math.sqrt(1.0d - ((d * d2) * d2));
                double d3 = this.sinphi0;
                double sqrt = this.f538e / Math.sqrt(this.one_es);
                this.f473G = d3 * sqrt;
                this.f474He = sqrt * this.cosphi0;
            }
        }
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3 = d2;
        ProjCoordinate projCoordinate2 = projCoordinate;
        if (this.spherical) {
            double sin = Math.sin(d2);
            double cos = Math.cos(d2);
            double cos2 = Math.cos(d);
            int i = this.mode;
            if (i == 1) {
                d3 = -d3;
                cos2 = -cos2;
            } else if (i != 2) {
                if (i != 3 && i != 4) {
                    return projCoordinate2;
                }
                if (i == 3) {
                    projCoordinate2.f410y = cos * cos2;
                } else {
                    projCoordinate2.f410y = (this.sinphi0 * sin) + (this.cosphi0 * cos * cos2);
                }
                if (Math.abs(Math.abs(projCoordinate2.f410y) - 1.0d) >= TOL) {
                    projCoordinate2.f410y = Math.acos(projCoordinate2.f410y);
                    projCoordinate2.f410y /= Math.sin(projCoordinate2.f410y);
                    projCoordinate2.f409x = projCoordinate2.f410y * cos * Math.sin(d);
                    double d4 = projCoordinate2.f410y;
                    if (this.mode != 3) {
                        sin = (this.cosphi0 * sin) - ((this.sinphi0 * cos) * cos2);
                    }
                    projCoordinate2.f410y = d4 * sin;
                    return projCoordinate2;
                } else if (projCoordinate2.f410y >= 0.0d) {
                    projCoordinate2.f410y = 0.0d;
                    projCoordinate2.f409x = 0.0d;
                    return projCoordinate2;
                } else {
                    throw new ProjectionException();
                }
            }
            if (Math.abs(d3 - 1.5707963267948966d) >= 1.0E-10d) {
                double d5 = d3 + 1.5707963267948966d;
                projCoordinate2.f410y = d5;
                projCoordinate2.f409x = d5 * Math.sin(d);
                projCoordinate2.f410y *= cos2;
                return projCoordinate2;
            }
            throw new ProjectionException();
        }
        double cos3 = Math.cos(d);
        double cos4 = Math.cos(d2);
        double sin2 = Math.sin(d2);
        int i2 = this.mode;
        if (i2 == 1) {
            cos3 = -cos3;
        } else if (i2 != 2) {
            if (i2 != 3 && i2 != 4) {
                return projCoordinate2;
            }
            if (Math.abs(d) >= 1.0E-10d || Math.abs(d3 - this.projectionLatitude) >= 1.0E-10d) {
                double atan2 = Math.atan2((this.one_es * sin2) + (this.f539es * this.f477N1 * this.sinphi0 * Math.sqrt(1.0d - ((this.f539es * sin2) * sin2))), cos4);
                double cos5 = Math.cos(atan2);
                double sin3 = Math.sin(atan2);
                double atan22 = Math.atan2(Math.sin(d) * cos5, (this.cosphi0 * sin3) - ((this.sinphi0 * cos3) * cos5));
                double cos6 = Math.cos(atan22);
                double sin4 = Math.sin(atan22);
                double asin = ProjectionMath.asin(Math.abs(sin4) < TOL ? ((this.cosphi0 * sin3) - ((this.sinphi0 * cos3) * cos5)) / cos6 : (Math.sin(d) * cos5) / sin4);
                double d6 = this.f474He * cos6;
                double d7 = d6 * d6;
                double d8 = sin4;
                double d9 = this.f473G;
                double d10 = 7.0d * d7;
                double d11 = this.f477N1 * asin * ((asin * asin * ((((-d7) * (1.0d - d7)) / 6.0d) + (asin * ((((d9 * d6) * (1.0d - ((2.0d * d7) * d7))) / 8.0d) + (((((d7 * (4.0d - d10)) - (((3.0d * d9) * d9) * (1.0d - d10))) / 120.0d) - (((d9 * asin) * d6) / 48.0d)) * asin))))) + 1.0d);
                ProjCoordinate projCoordinate3 = projCoordinate;
                projCoordinate3.f409x = d11 * d8;
                projCoordinate3.f410y = d11 * cos6;
                return projCoordinate3;
            }
            projCoordinate2.f410y = 0.0d;
            projCoordinate2.f409x = 0.0d;
            return projCoordinate2;
        }
        double abs = Math.abs(this.f476Mp - ProjectionMath.mlfn(d2, sin2, cos4, this.f478en));
        projCoordinate2.f409x = Math.sin(d) * abs;
        projCoordinate2.f410y = abs * cos3;
        return projCoordinate2;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        double d5 = d;
        double d6 = d2;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double d7 = 0.0d;
        if (this.spherical) {
            double distance = ProjectionMath.distance(d, d2);
            if (distance > 3.141592653589793d) {
                if (distance - 1.0E-10d <= 3.141592653589793d) {
                    distance = 3.141592653589793d;
                } else {
                    throw new ProjectionException();
                }
            } else if (distance < 1.0E-10d) {
                projCoordinate2.f410y = this.projectionLatitude;
                projCoordinate2.f409x = 0.0d;
                return projCoordinate2;
            }
            int i = this.mode;
            if (i == 4 || i == 3) {
                double sin = Math.sin(distance);
                double cos = Math.cos(distance);
                if (this.mode == 3) {
                    projCoordinate2.f410y = ProjectionMath.asin((d6 * sin) / distance);
                    d4 = d5 * sin;
                    d3 = cos * distance;
                } else {
                    projCoordinate2.f410y = ProjectionMath.asin((this.sinphi0 * cos) + (((d6 * sin) * this.cosphi0) / distance));
                    d3 = (cos - (this.sinphi0 * Math.sin(projCoordinate2.f410y))) * distance;
                    d4 = d5 * sin * this.cosphi0;
                }
                if (d3 != 0.0d) {
                    d7 = Math.atan2(d4, d3);
                }
                projCoordinate2.f409x = d7;
            } else if (i == 1) {
                projCoordinate2.f410y = 1.5707963267948966d - distance;
                projCoordinate2.f409x = Math.atan2(d5, -d6);
            } else {
                projCoordinate2.f410y = distance - 1.5707963267948966d;
                projCoordinate2.f409x = Math.atan2(d, d2);
            }
        } else {
            double distance2 = ProjectionMath.distance(d, d2);
            if (distance2 < 1.0E-10d) {
                projCoordinate2.f410y = this.projectionLatitude;
                projCoordinate2.f409x = 0.0d;
                return projCoordinate2;
            }
            int i2 = this.mode;
            if (i2 == 4 || i2 == 3) {
                double atan2 = Math.atan2(d, d2);
                double cos2 = this.cosphi0 * Math.cos(atan2);
                double d8 = (this.f539es * cos2) / this.one_es;
                double d9 = (-d8) * cos2;
                double d10 = this.sinphi0;
                double d11 = d8 * (1.0d - d9) * 3.0d * d10;
                double d12 = distance2 / this.f477N1;
                double d13 = d12 * (1.0d - ((d12 * d12) * ((((d9 + 1.0d) * d9) / 6.0d) + (((((3.0d * d9) + 1.0d) * d11) * d12) / 24.0d))));
                double d14 = 1.0d - ((d13 * d13) * ((d9 / 2.0d) + ((d11 * d13) / 6.0d)));
                double asin = ProjectionMath.asin((d10 * Math.cos(d13)) + (cos2 * Math.sin(d13)));
                projCoordinate2.f409x = ProjectionMath.asin((Math.sin(atan2) * Math.sin(d13)) / Math.cos(asin));
                double abs = Math.abs(asin);
                if (abs < 1.0E-10d) {
                    projCoordinate2.f410y = 0.0d;
                } else if (Math.abs(abs - 1.5707963267948966d) < 0.0d) {
                    projCoordinate2.f410y = 1.5707963267948966d;
                } else {
                    projCoordinate2.f410y = Math.atan(((1.0d - (((this.f539es * d14) * this.sinphi0) / Math.sin(asin))) * Math.tan(asin)) / this.one_es);
                }
            } else {
                projCoordinate2.f410y = ProjectionMath.inv_mlfn(i2 == 1 ? this.f476Mp - distance2 : this.f476Mp + distance2, this.f539es, this.f478en);
                if (this.mode == 1) {
                    d6 = -d6;
                }
                projCoordinate2.f409x = Math.atan2(d5, d6);
            }
        }
        return projCoordinate2;
    }
}
