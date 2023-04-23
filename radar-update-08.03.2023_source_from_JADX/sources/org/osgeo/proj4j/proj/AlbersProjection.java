package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class AlbersProjection extends Projection {
    private static final double EPS10 = 1.0E-10d;
    private static final double EPSILON = 1.0E-7d;
    private static final int N_ITER = 15;
    private static final double TOL = 1.0E-10d;
    private static final double TOL7 = 1.0E-7d;

    /* renamed from: c */
    private double f428c;

    /* renamed from: dd */
    private double f429dd;

    /* renamed from: ec */
    private double f430ec;

    /* renamed from: en */
    private double[] f431en;

    /* renamed from: n */
    private double f432n;

    /* renamed from: n2 */
    private double f433n2;
    private double phi1;
    private double phi2;
    private double rho0;

    public int getEPSGCode() {
        return 9822;
    }

    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Albers Equal Area";
    }

    public AlbersProjection() {
        this.minLatitude = Math.toRadians(0.0d);
        this.maxLatitude = Math.toRadians(80.0d);
        this.projectionLatitude1 = ProjectionMath.degToRad(45.5d);
        this.projectionLatitude2 = ProjectionMath.degToRad(29.5d);
        initialize();
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x004e A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static double phi1_(double r17, double r19, double r21) {
        /*
            r0 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r2 = r17 * r0
            double r2 = java.lang.Math.asin(r2)
            r4 = 4502148214488346440(0x3e7ad7f29abcaf48, double:1.0E-7)
            int r4 = (r19 > r4 ? 1 : (r19 == r4 ? 0 : -1))
            if (r4 >= 0) goto L_0x0012
            return r2
        L_0x0012:
            r4 = 15
        L_0x0014:
            double r5 = java.lang.Math.sin(r2)
            double r7 = java.lang.Math.cos(r2)
            double r9 = r19 * r5
            double r11 = r9 * r9
            r13 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r11 = r13 - r11
            double r15 = r11 * r0
            double r15 = r15 * r11
            double r15 = r15 / r7
            double r7 = r17 / r21
            double r5 = r5 / r11
            double r7 = r7 - r5
            double r5 = r0 / r19
            double r11 = r13 - r9
            double r9 = r9 + r13
            double r11 = r11 / r9
            double r9 = java.lang.Math.log(r11)
            double r5 = r5 * r9
            double r7 = r7 + r5
            double r15 = r15 * r7
            double r2 = r2 + r15
            double r5 = java.lang.Math.abs(r15)
            r7 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 <= 0) goto L_0x004b
            int r4 = r4 + -1
            if (r4 != 0) goto L_0x0014
        L_0x004b:
            if (r4 == 0) goto L_0x004e
            goto L_0x0053
        L_0x004e:
            r2 = 9218868437227405311(0x7fefffffffffffff, double:1.7976931348623157E308)
        L_0x0053:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.proj.AlbersProjection.phi1_(double, double, double):double");
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        double d5 = this.f428c;
        if (!this.spherical) {
            d4 = this.f432n;
            d3 = ProjectionMath.qsfn(Math.sin(d2), this.f538e, this.one_es);
        } else {
            d4 = this.f433n2;
            d3 = Math.sin(d2);
        }
        double d6 = d5 - (d4 * d3);
        if (d6 >= 0.0d) {
            double sqrt = this.f429dd * Math.sqrt(d6);
            double d7 = d * this.f432n;
            projCoordinate.f409x = Math.sin(d7) * sqrt;
            projCoordinate.f410y = this.rho0 - (sqrt * Math.cos(d7));
            return projCoordinate;
        }
        throw new ProjectionException("F");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0060, code lost:
        if (r15 < 0.0d) goto L_0x0080;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x007e, code lost:
        if (r6 < 0.0d) goto L_0x0080;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0086, code lost:
        r11 = 1.5707963267948966d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.osgeo.proj4j.ProjCoordinate projectInverse(double r22, double r24, org.osgeo.proj4j.ProjCoordinate r26) {
        /*
            r21 = this;
            r0 = r21
            r1 = r22
            r3 = r26
            double r4 = r0.rho0
            double r4 = r4 - r24
            double r6 = org.osgeo.proj4j.util.ProjectionMath.distance(r1, r4)
            r8 = 0
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 == 0) goto L_0x0098
            double r11 = r0.f432n
            int r10 = (r11 > r8 ? 1 : (r11 == r8 ? 0 : -1))
            if (r10 >= 0) goto L_0x001d
            double r6 = -r6
            double r1 = -r1
            double r4 = -r4
        L_0x001d:
            double r10 = r0.f429dd
            double r6 = r6 / r10
            boolean r10 = r0.spherical
            if (r10 != 0) goto L_0x0063
            double r10 = r0.f428c
            double r6 = r6 * r6
            double r10 = r10 - r6
            double r6 = r0.f432n
            double r15 = r10 / r6
            double r6 = r0.f430ec
            double r10 = java.lang.Math.abs(r15)
            double r6 = r6 - r10
            double r6 = java.lang.Math.abs(r6)
            r10 = 4502148214488346440(0x3e7ad7f29abcaf48, double:1.0E-7)
            int r6 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
            if (r6 <= 0) goto L_0x005e
            double r6 = r0.f538e
            double r8 = r0.one_es
            r17 = r6
            r19 = r8
            double r6 = phi1_(r15, r17, r19)
            r8 = 9218868437227405311(0x7fefffffffffffff, double:1.7976931348623157E308)
            int r8 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r8 == 0) goto L_0x0056
            goto L_0x008c
        L_0x0056:
            org.osgeo.proj4j.ProjectionException r1 = new org.osgeo.proj4j.ProjectionException
            java.lang.String r2 = "I"
            r1.<init>(r2)
            throw r1
        L_0x005e:
            int r6 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x0086
            goto L_0x0080
        L_0x0063:
            double r10 = r0.f428c
            double r15 = r6 * r6
            double r10 = r10 - r15
            double r13 = r0.f433n2
            double r10 = r10 / r13
            r3.f410y = r10
            double r10 = java.lang.Math.abs(r10)
            r12 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r10 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r10 > 0) goto L_0x007c
            double r6 = java.lang.Math.asin(r6)
            goto L_0x008c
        L_0x007c:
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x0086
        L_0x0080:
            r11 = -4613618979930100456(0xbff921fb54442d18, double:-1.5707963267948966)
            goto L_0x008b
        L_0x0086:
            r11 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
        L_0x008b:
            r6 = r11
        L_0x008c:
            double r1 = java.lang.Math.atan2(r1, r4)
            double r4 = r0.f432n
            double r1 = r1 / r4
            r3.f409x = r1
            r3.f410y = r6
            goto L_0x00ad
        L_0x0098:
            r3.f409x = r8
            double r1 = r0.f432n
            int r1 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r1 <= 0) goto L_0x00a6
            r11 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
            goto L_0x00ab
        L_0x00a6:
            r11 = -4613618979930100456(0xbff921fb54442d18, double:-1.5707963267948966)
        L_0x00ab:
            r3.f410y = r11
        L_0x00ad:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.proj.AlbersProjection.projectInverse(double, double, org.osgeo.proj4j.ProjCoordinate):org.osgeo.proj4j.ProjCoordinate");
    }

    public void initialize() {
        super.initialize();
        this.phi1 = this.projectionLatitude1;
        double d = this.projectionLatitude2;
        this.phi2 = d;
        if (Math.abs(this.phi1 + d) >= 1.0E-10d) {
            double sin = Math.sin(this.phi1);
            this.f432n = sin;
            double cos = Math.cos(this.phi1);
            boolean z = Math.abs(this.phi1 - this.phi2) >= 1.0E-10d;
            if (!this.spherical) {
                double[] enfn = ProjectionMath.enfn(this.f539es);
                this.f431en = enfn;
                if (enfn != null) {
                    double d2 = sin;
                    double msfn = ProjectionMath.msfn(d2, cos, this.f539es);
                    double qsfn = ProjectionMath.qsfn(d2, this.f538e, this.one_es);
                    if (z) {
                        double sin2 = Math.sin(this.phi2);
                        double msfn2 = ProjectionMath.msfn(sin2, Math.cos(this.phi2), this.f539es);
                        this.f432n = ((msfn * msfn) - (msfn2 * msfn2)) / (ProjectionMath.qsfn(sin2, this.f538e, this.one_es) - qsfn);
                    }
                    this.f430ec = 1.0d - (((this.one_es * 0.5d) * Math.log((1.0d - this.f538e) / (this.f538e + 1.0d))) / this.f538e);
                    double d3 = this.f432n;
                    double d4 = (qsfn * d3) + (msfn * msfn);
                    this.f428c = d4;
                    double d5 = 1.0d / d3;
                    this.f429dd = d5;
                    this.rho0 = d5 * Math.sqrt(d4 - (d3 * ProjectionMath.qsfn(Math.sin(this.projectionLatitude), this.f538e, this.one_es)));
                    return;
                }
                throw new ProjectionException("0");
            }
            if (z) {
                this.f432n = (this.f432n + Math.sin(this.phi2)) * 0.5d;
            }
            double d6 = this.f432n;
            double d7 = d6 + d6;
            this.f433n2 = d7;
            double d8 = (cos * cos) + (sin * d7);
            this.f428c = d8;
            double d9 = 1.0d / d6;
            this.f429dd = d9;
            this.rho0 = d9 * Math.sqrt(d8 - (d7 * Math.sin(this.projectionLatitude)));
            return;
        }
        throw new ProjectionException("-21");
    }
}
