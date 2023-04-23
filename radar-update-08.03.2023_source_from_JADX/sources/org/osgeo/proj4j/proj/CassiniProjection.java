package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class CassiniProjection extends Projection {

    /* renamed from: C1 */
    private static final double f440C1 = 0.16666666666666666d;

    /* renamed from: C2 */
    private static final double f441C2 = 0.008333333333333333d;

    /* renamed from: C3 */
    private static final double f442C3 = 0.041666666666666664d;

    /* renamed from: C4 */
    private static final double f443C4 = 0.3333333333333333d;

    /* renamed from: C5 */
    private static final double f444C5 = 0.06666666666666667d;
    private static final double EPS10 = 1.0E-10d;

    /* renamed from: a1 */
    private double f445a1;

    /* renamed from: a2 */
    private double f446a2;

    /* renamed from: c */
    private double f447c;

    /* renamed from: d2 */
    private double f448d2;

    /* renamed from: dd */
    private double f449dd;

    /* renamed from: en */
    private double[] f450en;

    /* renamed from: m0 */
    private double f451m0;

    /* renamed from: n */
    private double f452n;

    /* renamed from: r */
    private double f453r;

    /* renamed from: t */
    private double f454t;

    /* renamed from: tn */
    private double f455tn;

    public int getEPSGCode() {
        return 9806;
    }

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Cassini";
    }

    public CassiniProjection() {
        this.projectionLatitude = Math.toRadians(0.0d);
        this.projectionLongitude = Math.toRadians(0.0d);
        this.minLongitude = Math.toRadians(-90.0d);
        this.maxLongitude = Math.toRadians(90.0d);
        initialize();
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        if (this.spherical) {
            projCoordinate2.f409x = Math.asin(Math.cos(d2) * Math.sin(d));
            projCoordinate2.f410y = Math.atan2(Math.tan(d2), Math.cos(d)) - this.projectionLatitude;
        } else {
            double sin = Math.sin(d2);
            this.f452n = sin;
            double cos = Math.cos(d2);
            this.f447c = cos;
            projCoordinate2.f410y = ProjectionMath.mlfn(d2, sin, cos, this.f450en);
            double d3 = this.f539es;
            double d4 = this.f452n;
            this.f452n = 1.0d / Math.sqrt(1.0d - ((d3 * d4) * d4));
            double tan = Math.tan(d2);
            this.f455tn = tan;
            this.f454t = tan * tan;
            double d5 = this.f447c;
            this.f445a1 = d * d5;
            double d6 = d5 * ((this.f539es * this.f447c) / (1.0d - this.f539es));
            this.f447c = d6;
            double d7 = this.f445a1;
            double d8 = d7 * d7;
            this.f446a2 = d8;
            double d9 = this.f452n * d7;
            double d10 = this.f454t;
            projCoordinate2.f409x = d9 * (1.0d - ((d8 * d10) * (f440C1 - ((((8.0d - d10) + (d6 * 8.0d)) * d8) * f441C2))));
            double d11 = projCoordinate2.f410y;
            double d12 = this.f451m0;
            double d13 = this.f452n * this.f455tn;
            double d14 = this.f446a2;
            projCoordinate2.f410y = d11 - (d12 - ((d13 * d14) * (((((5.0d - this.f454t) + (this.f447c * 6.0d)) * d14) * f442C3) + 0.5d)));
        }
        return projCoordinate2;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        if (this.spherical) {
            double d3 = d2 + this.projectionLatitude;
            this.f449dd = d3;
            projCoordinate2.f410y = Math.asin(Math.sin(d3) * Math.cos(d));
            projCoordinate2.f409x = Math.atan2(Math.tan(d), Math.cos(this.f449dd));
        } else {
            double inv_mlfn = ProjectionMath.inv_mlfn(this.f451m0 + d2, this.f539es, this.f450en);
            double tan = Math.tan(inv_mlfn);
            this.f455tn = tan;
            this.f454t = tan * tan;
            this.f452n = Math.sin(inv_mlfn);
            double d4 = this.f539es;
            double d5 = this.f452n;
            double d6 = 1.0d / (1.0d - ((d4 * d5) * d5));
            this.f453r = d6;
            this.f452n = Math.sqrt(d6);
            double d7 = this.f453r;
            double d8 = this.f452n;
            double d9 = d7 * (1.0d - this.f539es) * d8;
            this.f453r = d9;
            double d10 = d / d8;
            this.f449dd = d10;
            double d11 = d10 * d10;
            this.f448d2 = d11;
            projCoordinate2.f410y = inv_mlfn - ((((d8 * this.f455tn) / d9) * d11) * (0.5d - ((((this.f454t * 3.0d) + 1.0d) * d11) * f442C3)));
            double d12 = this.f449dd;
            double d13 = this.f454t;
            double d14 = this.f448d2;
            projCoordinate2.f409x = (d12 * (((d13 * d14) * (((((d13 * 3.0d) + 1.0d) * d14) * f444C5) - 13.333333333333334d)) + 1.0d)) / Math.cos(inv_mlfn);
        }
        return projCoordinate2;
    }

    public void initialize() {
        super.initialize();
        if (!this.spherical) {
            double[] enfn = ProjectionMath.enfn(this.f539es);
            this.f450en = enfn;
            if (enfn != null) {
                this.f451m0 = ProjectionMath.mlfn(this.projectionLatitude, Math.sin(this.projectionLatitude), Math.cos(this.projectionLatitude), this.f450en);
                return;
            }
            throw new ProjectionException();
        }
    }
}
