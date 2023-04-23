package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.util.ProjectionMath;

public class PolyconicProjection extends Projection {
    private static final double CONV = 1.0E-10d;
    private static final double ITOL = 1.0E-12d;
    private static final int I_ITER = 20;
    private static final int N_ITER = 10;
    private static final double TOL = 1.0E-10d;

    /* renamed from: en */
    private double[] f536en;
    private double ml0;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Polyconic (American)";
    }

    public PolyconicProjection() {
        this.minLatitude = ProjectionMath.degToRad(0.0d);
        this.maxLatitude = ProjectionMath.degToRad(80.0d);
        this.minLongitude = ProjectionMath.degToRad(-60.0d);
        this.maxLongitude = ProjectionMath.degToRad(60.0d);
        initialize();
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4 = d;
        ProjCoordinate projCoordinate2 = projCoordinate;
        if (this.spherical) {
            if (Math.abs(d2) <= 1.0E-10d) {
                projCoordinate2.f409x = d4;
                projCoordinate2.f410y = this.ml0;
            } else {
                double tan = 1.0d / Math.tan(d2);
                double sin = d4 * Math.sin(d2);
                projCoordinate2.f409x = Math.sin(sin) * tan;
                projCoordinate2.f410y = (d2 - this.projectionLatitude) + (tan * (1.0d - Math.cos(sin)));
            }
        } else if (Math.abs(d2) <= 1.0E-10d) {
            projCoordinate2.f409x = d4;
            projCoordinate2.f410y = -this.ml0;
        } else {
            double sin2 = Math.sin(d2);
            double cos = Math.cos(d2);
            if (Math.abs(cos) > 1.0E-10d) {
                d3 = ProjectionMath.msfn(sin2, cos, this.f539es) / sin2;
            } else {
                d3 = 0.0d;
            }
            double d5 = projCoordinate2.f409x * sin2;
            projCoordinate2.f409x = d5;
            projCoordinate2.f409x = Math.sin(d5) * d3;
            projCoordinate2.f410y = (ProjectionMath.mlfn(d2, sin2, cos, this.f536en) - this.ml0) + (d3 * (1.0d - Math.cos(d)));
        }
        return projCoordinate2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0074  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.osgeo.proj4j.ProjCoordinate projectInverse(double r34, double r36, org.osgeo.proj4j.ProjCoordinate r38) {
        /*
            r33 = this;
            r0 = r33
            r1 = r34
            r3 = r38
            boolean r4 = r0.spherical
            r5 = 0
            java.lang.String r7 = "I"
            r8 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            r10 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            if (r4 == 0) goto L_0x007a
            double r12 = r0.projectionLatitude
            double r12 = r12 + r36
            double r12 = java.lang.Math.abs(r12)
            int r4 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
            if (r4 > 0) goto L_0x0027
            r3.f409x = r1
            r3.f410y = r5
            goto L_0x0145
        L_0x0027:
            double r4 = r1 * r1
            double r12 = r36 * r36
            double r4 = r4 + r12
            r6 = 10
            r12 = r36
        L_0x0030:
            double r14 = java.lang.Math.tan(r12)
            double r16 = r12 * r14
            double r16 = r16 + r10
            double r16 = r16 * r36
            double r16 = r16 - r12
            r18 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r20 = r12 * r12
            double r20 = r20 + r4
            double r20 = r20 * r18
            double r20 = r20 * r14
            double r16 = r16 - r20
            double r18 = r12 - r36
            double r18 = r18 / r14
            double r18 = r18 - r10
            double r16 = r16 / r18
            double r12 = r12 - r16
            double r14 = java.lang.Math.abs(r16)
            int r14 = (r14 > r8 ? 1 : (r14 == r8 ? 0 : -1))
            if (r14 <= 0) goto L_0x005e
            int r6 = r6 + -1
            if (r6 > 0) goto L_0x0030
        L_0x005e:
            if (r6 == 0) goto L_0x0074
            double r4 = java.lang.Math.tan(r12)
            double r1 = r1 * r4
            double r1 = java.lang.Math.asin(r1)
            double r4 = java.lang.Math.sin(r12)
            double r1 = r1 / r4
            r3.f409x = r1
            r3.f410y = r12
            goto L_0x0145
        L_0x0074:
            org.osgeo.proj4j.ProjectionException r1 = new org.osgeo.proj4j.ProjectionException
            r1.<init>(r7)
            throw r1
        L_0x007a:
            double r12 = r0.ml0
            double r12 = r36 + r12
            double r14 = java.lang.Math.abs(r12)
            int r4 = (r14 > r8 ? 1 : (r14 == r8 ? 0 : -1))
            if (r4 > 0) goto L_0x008c
            r3.f409x = r1
            r3.f410y = r5
            goto L_0x0145
        L_0x008c:
            double r4 = r12 * r12
            double r8 = r1 * r1
            double r4 = r4 + r8
            r6 = 20
            r8 = r12
        L_0x0094:
            if (r6 <= 0) goto L_0x0120
            double r16 = java.lang.Math.sin(r8)
            double r18 = java.lang.Math.cos(r8)
            double r21 = r16 * r18
            double r14 = java.lang.Math.abs(r18)
            r23 = 4427486594234968593(0x3d719799812dea11, double:1.0E-12)
            int r14 = (r14 > r23 ? 1 : (r14 == r23 ? 0 : -1))
            if (r14 < 0) goto L_0x011a
            double r14 = r0.f539es
            double r14 = r14 * r16
            double r14 = r14 * r16
            double r14 = r10 - r14
            double r25 = java.lang.Math.sqrt(r14)
            double r14 = r16 * r25
            double r27 = r14 / r18
            double[] r14 = r0.f536en
            r20 = r14
            r14 = r8
            double r14 = org.osgeo.proj4j.util.ProjectionMath.mlfn(r14, r16, r18, r20)
            double r16 = r14 * r14
            double r16 = r16 + r4
            r36 = r4
            double r4 = r0.f539es
            double r4 = r10 / r4
            double r18 = r25 * r25
            double r18 = r18 * r25
            double r4 = r4 / r18
            double r18 = r14 + r14
            double r25 = r27 * r16
            double r18 = r18 + r25
            r25 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r29 = r12 * r25
            double r31 = r27 * r14
            double r31 = r31 + r10
            double r31 = r31 * r29
            double r18 = r18 - r31
            double r10 = r0.f539es
            double r10 = r10 * r21
            double r29 = r29 * r14
            double r16 = r16 - r29
            double r10 = r10 * r16
            double r10 = r10 / r27
            double r14 = r12 - r14
            double r14 = r14 * r25
            double r27 = r27 * r4
            r16 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r20 = r16 / r21
            double r27 = r27 - r20
            double r14 = r14 * r27
            double r10 = r10 + r14
            double r10 = r10 - r4
            double r10 = r10 - r4
            double r18 = r18 / r10
            double r8 = r8 + r18
            double r4 = java.lang.Math.abs(r18)
            int r4 = (r4 > r23 ? 1 : (r4 == r23 ? 0 : -1))
            if (r4 > 0) goto L_0x0112
            goto L_0x0120
        L_0x0112:
            int r6 = r6 + -1
            r4 = r36
            r10 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            goto L_0x0094
        L_0x011a:
            org.osgeo.proj4j.ProjectionException r1 = new org.osgeo.proj4j.ProjectionException
            r1.<init>(r7)
            throw r1
        L_0x0120:
            if (r6 == 0) goto L_0x0146
            double r4 = java.lang.Math.sin(r8)
            double r6 = java.lang.Math.tan(r8)
            double r1 = r1 * r6
            double r6 = r0.f539es
            double r6 = r6 * r4
            double r6 = r6 * r4
            r4 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r10 = r4 - r6
            double r4 = java.lang.Math.sqrt(r10)
            double r1 = r1 * r4
            double r1 = java.lang.Math.asin(r1)
            double r4 = java.lang.Math.sin(r8)
            double r1 = r1 / r4
            r3.f409x = r1
            r3.f410y = r8
        L_0x0145:
            return r3
        L_0x0146:
            org.osgeo.proj4j.ProjectionException r1 = new org.osgeo.proj4j.ProjectionException
            r1.<init>(r7)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.proj.PolyconicProjection.projectInverse(double, double, org.osgeo.proj4j.ProjCoordinate):org.osgeo.proj4j.ProjCoordinate");
    }

    public void initialize() {
        super.initialize();
        this.spherical = true;
        if (!this.spherical) {
            double[] enfn = ProjectionMath.enfn(this.f539es);
            this.f536en = enfn;
            if (enfn != null) {
                this.ml0 = ProjectionMath.mlfn(this.projectionLatitude, Math.sin(this.projectionLatitude), Math.cos(this.projectionLatitude), this.f536en);
                return;
            }
            throw new ProjectionException("E");
        }
        this.ml0 = -this.projectionLatitude;
    }
}
