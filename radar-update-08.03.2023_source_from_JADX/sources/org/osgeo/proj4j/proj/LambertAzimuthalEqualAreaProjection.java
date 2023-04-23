package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class LambertAzimuthalEqualAreaProjection extends Projection {
    private static final int EQUIT = 2;
    private static final int N_POLE = 0;
    private static final int OBLIQ = 3;
    private static final int S_POLE = 1;
    private double[] apa;
    private double cosb1;
    private double cosph0;

    /* renamed from: dd */
    private double f497dd;
    private double mmf;
    private int mode;
    private double phi0;

    /* renamed from: qp */
    private double f498qp;

    /* renamed from: rq */
    private double f499rq;
    private double sinb1;
    private double sinph0;
    private double xmf;
    private double ymf;

    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Lambert Azimuthal Equal Area";
    }

    public LambertAzimuthalEqualAreaProjection() {
        this(false);
    }

    public LambertAzimuthalEqualAreaProjection(boolean z) {
        this.mode = 0;
    }

    public void initialize() {
        super.initialize();
        double d = this.projectionLatitude;
        this.phi0 = d;
        double abs = Math.abs(d);
        if (Math.abs(abs - 1.5707963267948966d) < 1.0E-10d) {
            this.mode = this.phi0 < 0.0d ? 1 : 0;
        } else if (Math.abs(abs) < 1.0E-10d) {
            this.mode = 2;
        } else {
            this.mode = 3;
        }
        if (!this.spherical) {
            this.f538e = Math.sqrt(this.f539es);
            this.f498qp = ProjectionMath.qsfn(1.0d, this.f538e, this.one_es);
            this.mmf = 0.5d / (1.0d - this.f539es);
            this.apa = ProjectionMath.authset(this.f539es);
            int i = this.mode;
            if (i == 0 || i == 1) {
                this.f497dd = 1.0d;
            } else if (i == 2) {
                double sqrt = Math.sqrt(this.f498qp * 0.5d);
                this.f499rq = sqrt;
                this.f497dd = 1.0d / sqrt;
                this.xmf = 1.0d;
                this.ymf = this.f498qp * 0.5d;
            } else if (i == 3) {
                this.f499rq = Math.sqrt(this.f498qp * 0.5d);
                double sin = Math.sin(this.phi0);
                double qsfn = ProjectionMath.qsfn(sin, this.f538e, this.one_es) / this.f498qp;
                this.sinb1 = qsfn;
                this.cosb1 = Math.sqrt(1.0d - (qsfn * qsfn));
                double cos = Math.cos(this.phi0);
                double sqrt2 = Math.sqrt(1.0d - ((this.f539es * sin) * sin));
                double d2 = this.f499rq;
                double d3 = cos / ((sqrt2 * d2) * this.cosb1);
                this.f497dd = d3;
                this.ymf = d2 / d3;
                this.xmf = d2 * d3;
            }
        } else if (this.mode == 3) {
            this.sinph0 = Math.sin(this.phi0);
            this.cosph0 = Math.cos(this.phi0);
        }
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        if (this.spherical) {
            project_s(d, d2, projCoordinate);
        } else {
            project_e(d, d2, projCoordinate);
        }
        return projCoordinate;
    }

    public void project_s(double d, double d2, ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double sin = Math.sin(d2);
        double cos = Math.cos(d2);
        double cos2 = Math.cos(d);
        int i = this.mode;
        if (i == 0) {
            cos2 = -cos2;
        } else if (i != 1) {
            if (i == 2 || i == 3) {
                if (i == 2) {
                    projCoordinate2.f410y = (cos * cos2) + 1.0d;
                } else {
                    projCoordinate2.f410y = (this.sinph0 * sin) + 1.0d + (this.cosph0 * cos * cos2);
                }
                if (projCoordinate2.f410y > 1.0E-10d) {
                    double sqrt = Math.sqrt(2.0d / projCoordinate2.f410y);
                    projCoordinate2.f410y = sqrt;
                    projCoordinate2.f409x = sqrt * cos * Math.sin(d);
                    double d3 = projCoordinate2.f410y;
                    if (this.mode != 2) {
                        sin = (this.cosph0 * sin) - ((this.sinph0 * cos) * cos2);
                    }
                    projCoordinate2.f410y = d3 * sin;
                    return;
                }
                throw new ProjectionException("F");
            }
            return;
        }
        if (Math.abs(d2 + this.phi0) >= 1.0E-10d) {
            projCoordinate2.f410y = 0.7853981633974483d - (0.5d * d2);
            projCoordinate2.f410y = (this.mode == 1 ? Math.cos(projCoordinate2.f410y) : Math.sin(projCoordinate2.f410y)) * 2.0d;
            projCoordinate2.f409x = projCoordinate2.f410y * Math.sin(d);
            projCoordinate2.f410y *= cos2;
            return;
        }
        throw new ProjectionException("F");
    }

    public void project_e(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        double d5;
        double d6;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double cos = Math.cos(d);
        double sin = Math.sin(d);
        double qsfn = ProjectionMath.qsfn(Math.sin(d2), this.f538e, this.one_es);
        int i = this.mode;
        if (i == 3 || i == 2) {
            double d7 = qsfn / this.f498qp;
            double d8 = d7;
            d4 = Math.sqrt(1.0d - (d7 * d7));
            d3 = d8;
        } else {
            d4 = 0.0d;
            d3 = 0.0d;
        }
        int i2 = this.mode;
        if (i2 == 0) {
            d5 = d2 + 1.5707963267948966d;
            qsfn = this.f498qp - qsfn;
        } else if (i2 != 1) {
            d5 = i2 != 2 ? i2 != 3 ? 0.0d : (this.sinb1 * d3) + 1.0d + (this.cosb1 * d4 * cos) : (d4 * cos) + 1.0d;
        } else {
            d5 = d2 - 1.5707963267948966d;
            qsfn += this.f498qp;
        }
        if (Math.abs(d5) >= 1.0E-10d) {
            int i3 = this.mode;
            if (i3 == 0 || i3 == 1) {
                if (qsfn >= 0.0d) {
                    double sqrt = Math.sqrt(qsfn);
                    projCoordinate2.f409x = sin * sqrt;
                    if (this.mode != 1) {
                        sqrt = -sqrt;
                    }
                    projCoordinate2.f410y = cos * sqrt;
                    return;
                }
                projCoordinate2.f410y = 0.0d;
                projCoordinate2.f409x = 0.0d;
            } else if (i3 == 2 || i3 == 3) {
                if (i3 == 3) {
                    double d9 = this.ymf;
                    double sqrt2 = Math.sqrt(2.0d / d5);
                    projCoordinate2.f410y = d9 * sqrt2 * ((this.cosb1 * d3) - ((this.sinb1 * d4) * cos));
                    d6 = sqrt2;
                } else {
                    d6 = Math.sqrt(2.0d / ((cos * d4) + 1.0d));
                    projCoordinate2.f410y = this.ymf * d3 * d6;
                }
                projCoordinate2.f409x = this.xmf * d6 * d4 * sin;
            }
        } else {
            throw new ProjectionException("F");
        }
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        if (this.spherical) {
            projectInverse_s(d, d2, projCoordinate);
        } else {
            projectInverse_e(d, d2, projCoordinate);
        }
        return projCoordinate;
    }

    public void projectInverse_s(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        int i;
        double d9 = d2;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double hypot = Math.hypot(d, d2);
        double d10 = 0.5d * hypot;
        if (d10 <= 1.0d) {
            double asin = Math.asin(d10) * 2.0d;
            int i2 = this.mode;
            if (i2 == 3 || i2 == 2) {
                d4 = Math.sin(asin);
                d3 = Math.cos(asin);
            } else {
                d4 = 0.0d;
                d3 = 0.0d;
            }
            int i3 = this.mode;
            if (i3 == 0) {
                d9 = -d9;
                asin = 1.5707963267948966d - asin;
            } else if (i3 != 1) {
                if (i3 == 2) {
                    d6 = Math.abs(hypot) <= 1.0E-10d ? 0.0d : Math.asin((d9 * d4) / hypot);
                    d8 = d * d4;
                } else if (i3 == 3) {
                    d6 = Math.abs(hypot) <= 1.0E-10d ? this.phi0 : Math.asin((this.sinph0 * d3) + (((d9 * d4) * this.cosph0) / hypot));
                    d8 = d * d4 * this.cosph0;
                    d3 -= Math.sin(d6) * this.sinph0;
                }
                d7 = hypot * d3;
                d5 = 0.0d;
                if (!(d7 == d5 && ((i = this.mode) == 2 || i == 3))) {
                    d5 = Math.atan2(d8, d7);
                }
                projCoordinate2.f409x = d5;
                projCoordinate2.f410y = d6;
                return;
            } else {
                asin -= 1.5707963267948966d;
            }
            d7 = d9;
            d5 = 0.0d;
            d8 = d;
            d5 = Math.atan2(d8, d7);
            projCoordinate2.f409x = d5;
            projCoordinate2.f410y = d6;
            return;
        }
        throw new ProjectionException("I_ERROR");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0077  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x007e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void projectInverse_e(double r20, double r22, org.osgeo.proj4j.ProjCoordinate r24) {
        /*
            r19 = this;
            r0 = r19
            r1 = r22
            r3 = r24
            int r4 = r0.mode
            r5 = 1
            r6 = 0
            if (r4 == 0) goto L_0x006d
            if (r4 == r5) goto L_0x006e
            r5 = 2
            r8 = 3
            if (r4 == r5) goto L_0x001a
            if (r4 == r8) goto L_0x001a
        L_0x0015:
            r4 = r1
        L_0x0016:
            r1 = r20
            goto L_0x0088
        L_0x001a:
            double r4 = r0.f497dd
            double r9 = r20 / r4
            double r1 = r1 * r4
            double r4 = java.lang.Math.hypot(r9, r1)
            r11 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            int r11 = (r4 > r11 ? 1 : (r4 == r11 ? 0 : -1))
            if (r11 >= 0) goto L_0x0033
            double r1 = r0.phi0
            r3.f409x = r6
            r3.f410y = r1
            return
        L_0x0033:
            r6 = 4611686018427387904(0x4000000000000000, double:2.0)
            r11 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r11 = r11 * r4
            double r13 = r0.f499rq
            double r11 = r11 / r13
            double r11 = java.lang.Math.asin(r11)
            double r11 = r11 * r6
            double r6 = java.lang.Math.cos(r11)
            double r11 = java.lang.Math.sin(r11)
            double r9 = r9 * r11
            int r13 = r0.mode
            if (r13 != r8) goto L_0x0066
            double r13 = r0.sinb1
            double r15 = r6 * r13
            double r17 = r1 * r11
            r20 = r9
            double r8 = r0.cosb1
            double r17 = r17 * r8
            double r17 = r17 / r4
            double r15 = r15 + r17
            double r4 = r4 * r8
            double r4 = r4 * r6
            double r1 = r1 * r13
            double r1 = r1 * r11
            double r1 = r4 - r1
            r4 = r1
            r6 = r15
            goto L_0x0016
        L_0x0066:
            r20 = r9
            double r1 = r1 * r11
            double r1 = r1 / r4
            double r4 = r4 * r6
            r6 = r1
            goto L_0x0016
        L_0x006d:
            double r1 = -r1
        L_0x006e:
            double r8 = r20 * r20
            double r10 = r1 * r1
            double r8 = r8 + r10
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 != 0) goto L_0x007e
            double r1 = r0.phi0
            r3.f409x = r6
            r3.f410y = r1
            return
        L_0x007e:
            r6 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r10 = r0.f498qp
            double r8 = r8 / r10
            double r6 = r6 - r8
            if (r4 != r5) goto L_0x0015
            double r6 = -r6
            goto L_0x0015
        L_0x0088:
            double r1 = java.lang.Math.atan2(r1, r4)
            double r4 = java.lang.Math.asin(r6)
            double[] r6 = r0.apa
            double r4 = org.osgeo.proj4j.util.ProjectionMath.authlat(r4, r6)
            r3.f409x = r1
            r3.f410y = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.proj.LambertAzimuthalEqualAreaProjection.projectInverse_e(double, double, org.osgeo.proj4j.ProjCoordinate):void");
    }
}
