package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.util.ProjectionMath;

public class EqualAreaAzimuthalProjection extends AzimuthalProjection {
    private double[] apa;
    private double cosb1;

    /* renamed from: dd */
    private double f470dd;
    private double mmf;

    /* renamed from: qp */
    private double f471qp;

    /* renamed from: rq */
    private double f472rq;
    private double sinb1;
    private double xmf;
    private double ymf;

    public boolean hasInverse() {
        return true;
    }

    public boolean isEqualArea() {
        return true;
    }

    public String toString() {
        return "Lambert Equal Area Azimuthal";
    }

    public EqualAreaAzimuthalProjection() {
        initialize();
    }

    public Object clone() {
        EqualAreaAzimuthalProjection equalAreaAzimuthalProjection = (EqualAreaAzimuthalProjection) super.clone();
        double[] dArr = this.apa;
        if (dArr != null) {
            equalAreaAzimuthalProjection.apa = (double[]) dArr.clone();
        }
        return equalAreaAzimuthalProjection;
    }

    public void initialize() {
        super.initialize();
        if (!this.spherical) {
            this.f471qp = ProjectionMath.qsfn(1.0d, this.f538e, this.one_es);
            this.mmf = 0.5d / (1.0d - this.f539es);
            this.apa = ProjectionMath.authset(this.f539es);
            int i = this.mode;
            if (i == 1 || i == 2) {
                this.f470dd = 1.0d;
            } else if (i == 3) {
                double sqrt = Math.sqrt(this.f471qp * 0.5d);
                this.f472rq = sqrt;
                this.f470dd = 1.0d / sqrt;
                this.xmf = 1.0d;
                this.ymf = this.f471qp * 0.5d;
            } else if (i == 4) {
                this.f472rq = Math.sqrt(this.f471qp * 0.5d);
                double sin = Math.sin(this.projectionLatitude);
                double qsfn = ProjectionMath.qsfn(sin, this.f538e, this.one_es) / this.f471qp;
                this.sinb1 = qsfn;
                this.cosb1 = Math.sqrt(1.0d - (qsfn * qsfn));
                double cos = Math.cos(this.projectionLatitude);
                double sqrt2 = Math.sqrt(1.0d - ((this.f539es * sin) * sin));
                double d = this.f472rq;
                double d2 = cos / ((sqrt2 * d) * this.cosb1);
                this.f470dd = d2;
                this.ymf = d / d2;
                this.xmf = d * d2;
            }
        } else if (this.mode == 4) {
            this.sinphi0 = Math.sin(this.projectionLatitude);
            this.cosphi0 = Math.cos(this.projectionLatitude);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:53:0x0172  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x01dd  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.osgeo.proj4j.ProjCoordinate project(double r26, double r28, org.osgeo.proj4j.ProjCoordinate r30) {
        /*
            r25 = this;
            r0 = r25
            r1 = r30
            boolean r2 = r0.spherical
            r3 = 1
            r4 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            r6 = 4
            r9 = 2
            r10 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r12 = 3
            if (r2 == 0) goto L_0x00ea
            double r13 = java.lang.Math.sin(r28)
            double r15 = java.lang.Math.cos(r28)
            double r7 = java.lang.Math.cos(r26)
            int r2 = r0.mode
            if (r2 == r3) goto L_0x00a5
            if (r2 == r9) goto L_0x00a6
            if (r2 == r12) goto L_0x006b
            if (r2 == r6) goto L_0x002b
            goto L_0x01dc
        L_0x002b:
            double r2 = r0.sinphi0
            double r2 = r2 * r13
            double r2 = r2 + r10
            double r9 = r0.cosphi0
            double r9 = r9 * r15
            double r9 = r9 * r7
            double r2 = r2 + r9
            r1.f410y = r2
            double r2 = r1.f410y
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 <= 0) goto L_0x0065
            double r2 = r1.f410y
            r4 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r2 = r4 / r2
            double r2 = java.lang.Math.sqrt(r2)
            r1.f410y = r2
            double r2 = r2 * r15
            double r4 = java.lang.Math.sin(r26)
            double r2 = r2 * r4
            r1.f409x = r2
            double r2 = r1.f410y
            int r4 = r0.mode
            if (r4 != r12) goto L_0x0057
            goto L_0x0060
        L_0x0057:
            double r4 = r0.cosphi0
            double r4 = r4 * r13
            double r9 = r0.sinphi0
            double r9 = r9 * r15
            double r9 = r9 * r7
            double r13 = r4 - r9
        L_0x0060:
            double r2 = r2 * r13
            r1.f410y = r2
            goto L_0x01dc
        L_0x0065:
            org.osgeo.proj4j.ProjectionException r1 = new org.osgeo.proj4j.ProjectionException
            r1.<init>()
            throw r1
        L_0x006b:
            double r2 = r15 * r7
            double r2 = r2 + r10
            r1.f410y = r2
            double r2 = r1.f410y
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 <= 0) goto L_0x009f
            double r2 = r1.f410y
            r4 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r2 = r4 / r2
            double r2 = java.lang.Math.sqrt(r2)
            r1.f410y = r2
            double r2 = r2 * r15
            double r4 = java.lang.Math.sin(r26)
            double r2 = r2 * r4
            r1.f409x = r2
            double r2 = r1.f410y
            int r4 = r0.mode
            if (r4 != r12) goto L_0x0091
            goto L_0x009a
        L_0x0091:
            double r4 = r0.cosphi0
            double r4 = r4 * r13
            double r9 = r0.sinphi0
            double r9 = r9 * r15
            double r9 = r9 * r7
            double r13 = r4 - r9
        L_0x009a:
            double r2 = r2 * r13
            r1.f410y = r2
            goto L_0x01dc
        L_0x009f:
            org.osgeo.proj4j.ProjectionException r1 = new org.osgeo.proj4j.ProjectionException
            r1.<init>()
            throw r1
        L_0x00a5:
            double r7 = -r7
        L_0x00a6:
            double r2 = r0.projectionLatitude
            double r2 = r28 + r2
            double r2 = java.lang.Math.abs(r2)
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 < 0) goto L_0x00e4
            r2 = 4605249457297304856(0x3fe921fb54442d18, double:0.7853981633974483)
            r4 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r4 = r4 * r28
            double r2 = r2 - r4
            r1.f410y = r2
            int r2 = r0.mode
            if (r2 != r9) goto L_0x00c9
            double r2 = r1.f410y
            double r2 = java.lang.Math.cos(r2)
            goto L_0x00cf
        L_0x00c9:
            double r2 = r1.f410y
            double r2 = java.lang.Math.sin(r2)
        L_0x00cf:
            r4 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r2 = r2 * r4
            r1.f410y = r2
            double r2 = r1.f410y
            double r4 = java.lang.Math.sin(r26)
            double r2 = r2 * r4
            r1.f409x = r2
            double r2 = r1.f410y
            double r2 = r2 * r7
            r1.f410y = r2
            goto L_0x01dc
        L_0x00e4:
            org.osgeo.proj4j.ProjectionException r1 = new org.osgeo.proj4j.ProjectionException
            r1.<init>()
            throw r1
        L_0x00ea:
            double r7 = java.lang.Math.cos(r26)
            double r13 = java.lang.Math.sin(r26)
            double r17 = java.lang.Math.sin(r28)
            double r4 = r0.f538e
            double r9 = r0.one_es
            r19 = r4
            r21 = r9
            double r4 = org.osgeo.proj4j.util.ProjectionMath.qsfn(r17, r19, r21)
            int r9 = r0.mode
            if (r9 == r6) goto L_0x0110
            int r9 = r0.mode
            if (r9 != r12) goto L_0x010b
            goto L_0x0110
        L_0x010b:
            r10 = 0
            r17 = 0
            goto L_0x0124
        L_0x0110:
            double r10 = r0.f471qp
            double r9 = r4 / r10
            double r17 = r9 * r9
            r19 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r17 = r19 - r17
            double r17 = java.lang.Math.sqrt(r17)
            r23 = r9
            r10 = r17
            r17 = r23
        L_0x0124:
            int r9 = r0.mode
            r19 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
            if (r9 == r3) goto L_0x015b
            r2 = 2
            if (r9 == r2) goto L_0x0153
            if (r9 == r12) goto L_0x014a
            if (r9 == r6) goto L_0x013a
            r21 = r13
            r12 = r4
            r3 = 0
            goto L_0x0165
        L_0x013a:
            r21 = r13
            double r12 = r0.sinb1
            double r12 = r12 * r17
            r19 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r12 = r12 + r19
            double r2 = r0.cosb1
            double r2 = r2 * r10
            double r2 = r2 * r7
            double r2 = r2 + r12
            goto L_0x0163
        L_0x014a:
            r21 = r13
            r19 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r2 = r10 * r7
            double r2 = r2 + r19
            goto L_0x0163
        L_0x0153:
            r21 = r13
            double r2 = r28 - r19
            double r12 = r0.f471qp
            double r4 = r4 + r12
            goto L_0x0163
        L_0x015b:
            r21 = r13
            double r2 = r28 + r19
            double r12 = r0.f471qp
            double r4 = r12 - r4
        L_0x0163:
            r12 = r4
            r3 = r2
        L_0x0165:
            double r19 = java.lang.Math.abs(r3)
            r15 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            int r2 = (r19 > r15 ? 1 : (r19 == r15 ? 0 : -1))
            if (r2 < 0) goto L_0x01dd
            int r5 = r0.mode
            r2 = 1
            if (r5 == r2) goto L_0x01bd
            r2 = 2
            if (r5 == r2) goto L_0x01bd
            r9 = 3
            if (r5 == r9) goto L_0x01a0
            if (r5 == r6) goto L_0x0180
            goto L_0x01dc
        L_0x0180:
            double r5 = r0.ymf
            r12 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r2 = r12 / r3
            double r2 = java.lang.Math.sqrt(r2)
            double r5 = r5 * r2
            double r12 = r0.cosb1
            double r12 = r12 * r17
            double r14 = r0.sinb1
            double r14 = r14 * r10
            double r14 = r14 * r7
            double r12 = r12 - r14
            double r5 = r5 * r12
            r1.f410y = r5
            double r4 = r0.xmf
            double r4 = r4 * r2
            double r4 = r4 * r10
            double r4 = r4 * r21
            r1.f409x = r4
            goto L_0x01dc
        L_0x01a0:
            double r7 = r7 * r10
            r2 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r7 = r7 + r2
            r2 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r7 = r2 / r7
            double r2 = java.lang.Math.sqrt(r7)
            double r17 = r17 * r2
            double r4 = r0.ymf
            double r4 = r4 * r17
            r1.f410y = r4
            double r4 = r0.xmf
            double r4 = r4 * r2
            double r4 = r4 * r10
            double r4 = r4 * r21
            r1.f409x = r4
            goto L_0x01dc
        L_0x01bd:
            r3 = 0
            int r5 = (r12 > r3 ? 1 : (r12 == r3 ? 0 : -1))
            if (r5 < 0) goto L_0x01d6
            double r3 = java.lang.Math.sqrt(r12)
            double r13 = r3 * r21
            r1.f409x = r13
            int r5 = r0.mode
            r2 = 2
            if (r5 != r2) goto L_0x01d1
            goto L_0x01d2
        L_0x01d1:
            double r3 = -r3
        L_0x01d2:
            double r7 = r7 * r3
            r1.f410y = r7
            goto L_0x01dc
        L_0x01d6:
            r2 = 0
            r1.f410y = r2
            r1.f409x = r2
        L_0x01dc:
            return r1
        L_0x01dd:
            org.osgeo.proj4j.ProjectionException r1 = new org.osgeo.proj4j.ProjectionException
            r1.<init>()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.proj.EqualAreaAzimuthalProjection.project(double, double, org.osgeo.proj4j.ProjCoordinate):org.osgeo.proj4j.ProjCoordinate");
    }

    /* JADX WARNING: Removed duplicated region for block: B:61:0x013a  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0141  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.osgeo.proj4j.ProjCoordinate projectInverse(double r22, double r24, org.osgeo.proj4j.ProjCoordinate r26) {
        /*
            r21 = this;
            r0 = r21
            r1 = r24
            r3 = r26
            boolean r4 = r0.spherical
            r5 = 4611686018427387904(0x4000000000000000, double:2.0)
            r7 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r9 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            r11 = 1
            r12 = 4
            r14 = 0
            if (r4 == 0) goto L_0x00cd
            double r19 = org.osgeo.proj4j.util.ProjectionMath.distance(r22, r24)
            double r9 = r9 * r19
            r3.f410y = r9
            int r4 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1))
            if (r4 > 0) goto L_0x00c7
            double r7 = r3.f410y
            double r7 = java.lang.Math.asin(r7)
            double r7 = r7 * r5
            r3.f410y = r7
            int r4 = r0.mode
            if (r4 == r12) goto L_0x0036
            int r4 = r0.mode
            r5 = 3
            if (r4 != r5) goto L_0x0033
            goto L_0x0036
        L_0x0033:
            r4 = r14
            r6 = r4
            goto L_0x0042
        L_0x0036:
            double r4 = r3.f410y
            double r4 = java.lang.Math.sin(r4)
            double r6 = r3.f410y
            double r6 = java.lang.Math.cos(r6)
        L_0x0042:
            int r8 = r0.mode
            r9 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
            if (r8 == r11) goto L_0x00aa
            r11 = 2
            if (r8 == r11) goto L_0x00a1
            r11 = 3
            if (r8 == r11) goto L_0x0084
            if (r8 == r12) goto L_0x0054
            goto L_0x00a6
        L_0x0054:
            double r8 = java.lang.Math.abs(r19)
            r10 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 > 0) goto L_0x0064
            double r1 = r0.projectionLatitude
            goto L_0x0072
        L_0x0064:
            double r8 = r0.sinphi0
            double r8 = r8 * r6
            double r1 = r1 * r4
            double r10 = r0.cosphi0
            double r1 = r1 * r10
            double r1 = r1 / r19
            double r8 = r8 + r1
            double r1 = java.lang.Math.asin(r8)
        L_0x0072:
            r3.f410y = r1
            double r1 = r0.cosphi0
            double r4 = r4 * r1
            double r1 = r22 * r4
            double r4 = r3.f410y
            double r4 = java.lang.Math.sin(r4)
            double r8 = r0.sinphi0
            double r4 = r4 * r8
            double r6 = r6 - r4
            goto L_0x009e
        L_0x0084:
            double r8 = java.lang.Math.abs(r19)
            r10 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 > 0) goto L_0x0093
            r1 = r14
            goto L_0x009a
        L_0x0093:
            double r1 = r1 * r4
            double r1 = r1 / r19
            double r1 = java.lang.Math.asin(r1)
        L_0x009a:
            r3.f410y = r1
            double r1 = r22 * r4
        L_0x009e:
            double r4 = r6 * r19
            goto L_0x00b1
        L_0x00a1:
            double r4 = r3.f410y
            double r4 = r4 - r9
            r3.f410y = r4
        L_0x00a6:
            r4 = r1
            r1 = r22
            goto L_0x00b1
        L_0x00aa:
            double r1 = -r1
            double r4 = r3.f410y
            double r9 = r9 - r4
            r3.f410y = r9
            goto L_0x00a6
        L_0x00b1:
            int r6 = (r4 > r14 ? 1 : (r4 == r14 ? 0 : -1))
            if (r6 != 0) goto L_0x00bf
            int r6 = r0.mode
            r7 = 3
            if (r6 == r7) goto L_0x00c3
            int r6 = r0.mode
            if (r6 != r12) goto L_0x00bf
            goto L_0x00c3
        L_0x00bf:
            double r14 = java.lang.Math.atan2(r1, r4)
        L_0x00c3:
            r3.f409x = r14
            goto L_0x015f
        L_0x00c7:
            org.osgeo.proj4j.ProjectionException r1 = new org.osgeo.proj4j.ProjectionException
            r1.<init>()
            throw r1
        L_0x00cd:
            int r4 = r0.mode
            if (r4 == r11) goto L_0x0130
            r11 = 2
            if (r4 == r11) goto L_0x0131
            r11 = 3
            if (r4 == r11) goto L_0x00de
            if (r4 == r12) goto L_0x00de
        L_0x00d9:
            r4 = r1
        L_0x00da:
            r1 = r22
            goto L_0x014d
        L_0x00de:
            double r7 = r0.f470dd
            double r12 = r22 / r7
            double r1 = r1 * r7
            double r7 = org.osgeo.proj4j.util.ProjectionMath.distance(r12, r1)
            r16 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            int r11 = (r7 > r16 ? 1 : (r7 == r16 ? 0 : -1))
            if (r11 >= 0) goto L_0x00f7
            r3.f409x = r14
            double r1 = r0.projectionLatitude
            r3.f410y = r1
            return r3
        L_0x00f7:
            double r9 = r9 * r7
            double r14 = r0.f472rq
            double r9 = r9 / r14
            double r9 = java.lang.Math.asin(r9)
            double r9 = r9 * r5
            double r5 = java.lang.Math.cos(r9)
            double r9 = java.lang.Math.sin(r9)
            double r11 = r12 * r9
            int r13 = r0.mode
            r4 = 4
            if (r13 != r4) goto L_0x0128
            double r13 = r0.sinb1
            double r15 = r5 * r13
            double r17 = r1 * r9
            r22 = r11
            double r11 = r0.cosb1
            double r17 = r17 * r11
            double r17 = r17 / r7
            double r15 = r15 + r17
            double r7 = r7 * r11
            double r7 = r7 * r5
            double r1 = r1 * r13
            double r1 = r1 * r9
            double r1 = r7 - r1
            r4 = r1
            r14 = r15
            goto L_0x00da
        L_0x0128:
            r22 = r11
            double r1 = r1 * r9
            double r14 = r1 / r7
            double r1 = r7 * r5
            goto L_0x00d9
        L_0x0130:
            double r1 = -r1
        L_0x0131:
            double r4 = r22 * r22
            double r9 = r1 * r1
            double r4 = r4 + r9
            int r6 = (r4 > r14 ? 1 : (r4 == r14 ? 0 : -1))
            if (r6 != 0) goto L_0x0141
            r3.f409x = r14
            double r1 = r0.projectionLatitude
            r3.f410y = r1
            return r3
        L_0x0141:
            double r9 = r0.f471qp
            double r4 = r4 / r9
            double r14 = r7 - r4
            int r4 = r0.mode
            r5 = 2
            if (r4 != r5) goto L_0x00d9
            double r14 = -r14
            goto L_0x00d9
        L_0x014d:
            double r1 = java.lang.Math.atan2(r1, r4)
            r3.f409x = r1
            double r1 = java.lang.Math.asin(r14)
            double[] r4 = r0.apa
            double r1 = org.osgeo.proj4j.util.ProjectionMath.authlat(r1, r4)
            r3.f410y = r1
        L_0x015f:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.proj.EqualAreaAzimuthalProjection.projectInverse(double, double, org.osgeo.proj4j.ProjCoordinate):org.osgeo.proj4j.ProjCoordinate");
    }
}
