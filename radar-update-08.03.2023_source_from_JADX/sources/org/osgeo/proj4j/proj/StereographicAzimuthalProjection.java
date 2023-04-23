package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class StereographicAzimuthalProjection extends AzimuthalProjection {
    private static final double TOL = 1.0E-8d;
    private double akm1;

    public boolean hasInverse() {
        return true;
    }

    public boolean isConformal() {
        return true;
    }

    public String toString() {
        return "Stereographic Azimuthal";
    }

    public StereographicAzimuthalProjection() {
        this(Math.toRadians(90.0d), Math.toRadians(0.0d));
    }

    public StereographicAzimuthalProjection(double d, double d2) {
        super(d, d2);
        initialize();
    }

    public void setupUPS(int i) {
        this.projectionLatitude = i == 2 ? -1.5707963267948966d : 1.5707963267948966d;
        this.projectionLongitude = 0.0d;
        this.scaleFactor = 0.994d;
        this.falseEasting = 2000000.0d;
        this.falseNorthing = 2000000.0d;
        this.trueScaleLatitude = 1.5707963267948966d;
        initialize();
    }

    public void initialize() {
        super.initialize();
        double abs = Math.abs(this.projectionLatitude);
        if (Math.abs(abs - 1.5707963267948966d) < 1.0E-10d) {
            this.mode = this.projectionLatitude < 0.0d ? 2 : 1;
        } else {
            this.mode = abs > 1.0E-10d ? 4 : 3;
        }
        this.trueScaleLatitude = Math.abs(this.trueScaleLatitude);
        if (!this.spherical) {
            int i = this.mode;
            if (i == 1 || i == 2) {
                if (Math.abs(this.trueScaleLatitude - 1.5707963267948966d) < 1.0E-10d) {
                    this.akm1 = (this.scaleFactor * 2.0d) / Math.sqrt(Math.pow(this.f538e + 1.0d, this.f538e + 1.0d) * Math.pow(1.0d - this.f538e, 1.0d - this.f538e));
                    return;
                }
                double cos = Math.cos(this.trueScaleLatitude);
                double d = this.trueScaleLatitude;
                double sin = Math.sin(this.trueScaleLatitude);
                this.akm1 = cos / ProjectionMath.tsfn(d, sin, this.f538e);
                double d2 = sin * this.f538e;
                this.akm1 /= Math.sqrt(1.0d - (d2 * d2));
            } else if (i == 3) {
                this.akm1 = this.scaleFactor * 2.0d;
            } else if (i == 4) {
                double sin2 = Math.sin(this.projectionLatitude);
                double atan = (Math.atan(ssfn(this.projectionLatitude, sin2, this.f538e)) * 2.0d) - 1.5707963267948966d;
                double d3 = sin2 * this.f538e;
                this.akm1 = ((this.scaleFactor * 2.0d) * Math.cos(this.projectionLatitude)) / Math.sqrt(1.0d - (d3 * d3));
                this.sinphi0 = Math.sin(atan);
                this.cosphi0 = Math.cos(atan);
            }
        } else {
            int i2 = this.mode;
            if (i2 == 1 || i2 == 2) {
                this.akm1 = Math.abs(this.trueScaleLatitude - 1.5707963267948966d) >= 1.0E-10d ? Math.cos(this.trueScaleLatitude) / Math.tan(0.7853981633974483d - (this.trueScaleLatitude * 0.5d)) : this.scaleFactor * 2.0d;
                return;
            }
            if (i2 != 3) {
                if (i2 == 4) {
                    this.sinphi0 = Math.sin(this.projectionLatitude);
                    this.cosphi0 = Math.cos(this.projectionLatitude);
                } else {
                    return;
                }
            }
            this.akm1 = this.scaleFactor * 2.0d;
        }
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9 = d2;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double cos = Math.cos(d);
        double sin = Math.sin(d);
        double sin2 = Math.sin(d2);
        if (this.spherical) {
            double cos2 = Math.cos(d2);
            int i = this.mode;
            if (i == 1) {
                cos = -cos;
                d8 = -d9;
            } else if (i == 2) {
                d8 = d9;
            } else if (i == 3) {
                projCoordinate2.f410y = (cos * cos2) + 1.0d;
                if (projCoordinate2.f410y > 1.0E-10d) {
                    double d10 = this.akm1 / projCoordinate2.f410y;
                    projCoordinate2.f410y = d10;
                    projCoordinate2.f409x = d10 * cos2 * sin;
                    projCoordinate2.f410y *= sin2;
                } else {
                    throw new ProjectionException();
                }
            } else if (i == 4) {
                projCoordinate2.f410y = (this.sinphi0 * sin2) + 1.0d + (this.cosphi0 * cos2 * cos);
                if (projCoordinate2.f410y > 1.0E-10d) {
                    double d11 = this.akm1 / projCoordinate2.f410y;
                    projCoordinate2.f410y = d11;
                    projCoordinate2.f409x = d11 * cos2 * sin;
                    projCoordinate2.f410y *= (this.cosphi0 * sin2) - ((this.sinphi0 * cos2) * cos);
                } else {
                    throw new ProjectionException();
                }
            }
            if (Math.abs(d8 - 1.5707963267948966d) >= TOL) {
                double tan = this.akm1 * Math.tan((d8 * 0.5d) + 0.7853981633974483d);
                projCoordinate2.f410y = tan;
                projCoordinate2.f409x = sin * tan;
                projCoordinate2.f410y *= cos;
            } else {
                throw new ProjectionException();
            }
        } else {
            double d12 = 0.0d;
            if (this.mode == 4 || this.mode == 3) {
                d3 = sin;
                d4 = sin2;
                double atan = (Math.atan(ssfn(d2, sin2, this.f538e)) * 2.0d) - 1.5707963267948966d;
                double sin3 = Math.sin(atan);
                double cos3 = Math.cos(atan);
                d5 = sin3;
                d12 = cos3;
            } else {
                d4 = sin2;
                d3 = sin;
                d5 = 0.0d;
            }
            int i2 = this.mode;
            if (i2 == 1) {
                d6 = d4;
                d7 = d9;
            } else if (i2 != 2) {
                if (i2 == 3) {
                    double d13 = (this.akm1 * 2.0d) / ((cos * d12) + 1.0d);
                    projCoordinate2.f410y = d5 * d13;
                    projCoordinate2.f409x = d13 * d12;
                } else if (i2 == 4) {
                    double d14 = this.akm1 / (this.cosphi0 * (((this.sinphi0 * d5) + 1.0d) + ((this.cosphi0 * d12) * cos)));
                    projCoordinate2.f410y = ((this.cosphi0 * d5) - ((this.sinphi0 * d12) * cos)) * d14;
                    projCoordinate2.f409x = d14 * d12;
                }
                projCoordinate2.f409x *= d3;
            } else {
                cos = -cos;
                d7 = -d9;
                d6 = -d4;
            }
            projCoordinate2.f409x = this.akm1 * ProjectionMath.tsfn(d7, d6, this.f538e);
            projCoordinate2.f410y = (-projCoordinate2.f409x) * cos;
            projCoordinate2.f409x *= d3;
        }
        return projCoordinate2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:57:0x015d  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x01b5 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.osgeo.proj4j.ProjCoordinate projectInverse(double r28, double r30, org.osgeo.proj4j.ProjCoordinate r32) {
        /*
            r27 = this;
            r0 = r27
            r1 = r28
            r3 = r30
            r5 = r32
            boolean r6 = r0.spherical
            r7 = 1
            r10 = 4611686018427387904(0x4000000000000000, double:2.0)
            r12 = 2
            r13 = 0
            if (r6 == 0) goto L_0x00d8
            double r15 = org.osgeo.proj4j.util.ProjectionMath.distance(r28, r30)
            double r8 = r0.akm1
            double r8 = r15 / r8
            double r8 = java.lang.Math.atan(r8)
            double r8 = r8 * r10
            double r10 = java.lang.Math.sin(r8)
            double r8 = java.lang.Math.cos(r8)
            r5.f409x = r13
            int r6 = r0.mode
            if (r6 == r7) goto L_0x00a5
            if (r6 == r12) goto L_0x00a6
            r7 = 3
            if (r6 == r7) goto L_0x0078
            r7 = 4
            if (r6 == r7) goto L_0x0037
            goto L_0x00d7
        L_0x0037:
            double r6 = java.lang.Math.abs(r15)
            r17 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            int r6 = (r6 > r17 ? 1 : (r6 == r17 ? 0 : -1))
            if (r6 > 0) goto L_0x0049
            double r3 = r0.projectionLatitude
            r5.f410y = r3
            goto L_0x0058
        L_0x0049:
            double r6 = r0.sinphi0
            double r6 = r6 * r8
            double r3 = r3 * r10
            double r13 = r0.cosphi0
            double r3 = r3 * r13
            double r3 = r3 / r15
            double r6 = r6 + r3
            double r3 = java.lang.Math.asin(r6)
            r5.f410y = r3
        L_0x0058:
            double r3 = r0.sinphi0
            double r6 = r5.f410y
            double r6 = java.lang.Math.sin(r6)
            double r3 = r3 * r6
            double r8 = r8 - r3
            r3 = 0
            int r6 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r6 != 0) goto L_0x006c
            int r3 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r3 == 0) goto L_0x00d7
        L_0x006c:
            double r1 = r1 * r10
            double r3 = r0.cosphi0
            double r1 = r1 * r3
            double r8 = r8 * r15
            double r1 = java.lang.Math.atan2(r1, r8)
            r5.f409x = r1
            goto L_0x00d7
        L_0x0078:
            double r6 = java.lang.Math.abs(r15)
            r12 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            int r6 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1))
            if (r6 > 0) goto L_0x008a
            r6 = 0
            r5.f410y = r6
            goto L_0x0094
        L_0x008a:
            r6 = 0
            double r3 = r3 * r10
            double r3 = r3 / r15
            double r3 = java.lang.Math.asin(r3)
            r5.f410y = r3
        L_0x0094:
            int r3 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
            if (r3 != 0) goto L_0x009c
            int r3 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r3 == 0) goto L_0x00d7
        L_0x009c:
            double r1 = r1 * r10
            double r8 = r8 * r15
            double r1 = java.lang.Math.atan2(r1, r8)
            r5.f409x = r1
            goto L_0x00d7
        L_0x00a5:
            double r3 = -r3
        L_0x00a6:
            double r6 = java.lang.Math.abs(r15)
            r10 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            int r6 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
            if (r6 > 0) goto L_0x00ba
            double r6 = r0.projectionLatitude
            r5.f410y = r6
        L_0x00b7:
            r6 = 0
            goto L_0x00c6
        L_0x00ba:
            int r6 = r0.mode
            if (r6 != r12) goto L_0x00bf
            double r8 = -r8
        L_0x00bf:
            double r6 = java.lang.Math.asin(r8)
            r5.f410y = r6
            goto L_0x00b7
        L_0x00c6:
            int r8 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r8 != 0) goto L_0x00d1
            int r8 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1))
            if (r8 != 0) goto L_0x00d1
            r13 = 0
            goto L_0x00d5
        L_0x00d1:
            double r13 = java.lang.Math.atan2(r1, r3)
        L_0x00d5:
            r5.f409x = r13
        L_0x00d7:
            return r5
        L_0x00d8:
            double r8 = org.osgeo.proj4j.util.ProjectionMath.distance(r28, r30)
            int r6 = r0.mode
            if (r6 == r7) goto L_0x0137
            if (r6 == r12) goto L_0x0138
            double r6 = r0.cosphi0
            double r6 = r6 * r8
            double r12 = r0.akm1
            double r6 = java.lang.Math.atan2(r6, r12)
            double r6 = r6 * r10
            double r12 = java.lang.Math.cos(r6)
            double r6 = java.lang.Math.sin(r6)
            r19 = 0
            int r14 = (r8 > r19 ? 1 : (r8 == r19 ? 0 : -1))
            if (r14 > 0) goto L_0x0102
            double r10 = r0.sinphi0
            double r10 = r10 * r12
            double r10 = java.lang.Math.asin(r10)
            goto L_0x0113
        L_0x0102:
            double r10 = r0.sinphi0
            double r10 = r10 * r12
            double r23 = r3 * r6
            double r14 = r0.cosphi0
            double r23 = r23 * r14
            double r23 = r23 / r8
            double r10 = r10 + r23
            double r10 = java.lang.Math.asin(r10)
        L_0x0113:
            r14 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
            double r23 = r10 + r14
            r14 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r23 = r23 * r14
            double r23 = java.lang.Math.tan(r23)
            double r1 = r1 * r6
            double r14 = r0.cosphi0
            double r8 = r8 * r14
            double r8 = r8 * r12
            double r12 = r0.sinphi0
            double r3 = r3 * r12
            double r3 = r3 * r6
            double r8 = r8 - r3
            double r3 = r0.f538e
            r6 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r3 = r3 * r6
            r13 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
            goto L_0x0157
        L_0x0137:
            double r3 = -r3
        L_0x0138:
            double r6 = -r8
            double r8 = r0.akm1
            double r23 = r6 / r8
            double r6 = java.lang.Math.atan(r23)
            r8 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r6 = r6 * r8
            r8 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
            double r10 = r8 - r6
            r13 = -4613618979930100456(0xbff921fb54442d18, double:-1.5707963267948966)
            r6 = -4620693217682128896(0xbfe0000000000000, double:-0.5)
            double r8 = r0.f538e
            double r6 = r6 * r8
            r8 = r3
            r3 = r6
        L_0x0157:
            r6 = 8
        L_0x0159:
            int r7 = r6 + -1
            if (r6 == 0) goto L_0x01b5
            r28 = r7
            double r6 = r0.f538e
            double r21 = java.lang.Math.sin(r10)
            double r6 = r6 * r21
            r21 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r25 = r6 + r21
            double r21 = r21 - r6
            double r6 = r25 / r21
            double r6 = java.lang.Math.pow(r6, r3)
            double r6 = r6 * r23
            double r6 = java.lang.Math.atan(r6)
            r21 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r6 = r6 * r21
            double r6 = r6 - r13
            r5.f410y = r6
            double r6 = r5.f410y
            double r10 = r10 - r6
            double r6 = java.lang.Math.abs(r10)
            r10 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            int r6 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
            if (r6 >= 0) goto L_0x01ad
            int r3 = r0.mode
            r6 = 2
            if (r3 != r6) goto L_0x019a
            double r3 = r5.f410y
            double r3 = -r3
            r5.f410y = r3
        L_0x019a:
            r15 = 0
            int r3 = (r1 > r15 ? 1 : (r1 == r15 ? 0 : -1))
            if (r3 != 0) goto L_0x01a6
            int r3 = (r8 > r15 ? 1 : (r8 == r15 ? 0 : -1))
            if (r3 != 0) goto L_0x01a6
            r13 = r15
            goto L_0x01aa
        L_0x01a6:
            double r13 = java.lang.Math.atan2(r1, r8)
        L_0x01aa:
            r5.f409x = r13
            return r5
        L_0x01ad:
            r15 = 0
            double r6 = r5.f410y
            r10 = r6
            r6 = r28
            goto L_0x0159
        L_0x01b5:
            org.osgeo.proj4j.ConvergenceFailureException r1 = new org.osgeo.proj4j.ConvergenceFailureException
            java.lang.String r2 = "Iteration didn't converge"
            r1.<init>(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.proj.StereographicAzimuthalProjection.projectInverse(double, double, org.osgeo.proj4j.ProjCoordinate):org.osgeo.proj4j.ProjCoordinate");
    }

    private double ssfn(double d, double d2, double d3) {
        double d4 = d2 * d3;
        return Math.tan((d + 1.5707963267948966d) * 0.5d) * Math.pow((1.0d - d4) / (d4 + 1.0d), d3 * 0.5d);
    }
}
