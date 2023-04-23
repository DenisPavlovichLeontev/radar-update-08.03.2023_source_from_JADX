package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class OrthographicAzimuthalProjection extends AzimuthalProjection {
    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Orthographic Azimuthal";
    }

    public OrthographicAzimuthalProjection() {
        initialize();
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double cos = Math.cos(d2);
        double cos2 = Math.cos(d);
        int i = this.mode;
        if (i == 1) {
            cos2 = -cos2;
        } else if (i != 2) {
            if (i == 3) {
                projCoordinate.f410y = Math.sin(d2);
            } else if (i == 4) {
                projCoordinate.f410y = (this.cosphi0 * Math.sin(d2)) - ((this.sinphi0 * cos) * cos2);
            }
            projCoordinate.f409x = cos * Math.sin(d);
            return projCoordinate;
        }
        projCoordinate.f410y = cos2 * cos;
        projCoordinate.f409x = cos * Math.sin(d);
        return projCoordinate;
    }

    /* JADX WARNING: Removed duplicated region for block: B:43:0x00e2  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00f6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.osgeo.proj4j.ProjCoordinate projectInverse(double r24, double r26, org.osgeo.proj4j.ProjCoordinate r28) {
        /*
            r23 = this;
            r0 = r23
            r1 = r26
            r3 = r28
            double r4 = org.osgeo.proj4j.util.ProjectionMath.distance(r24, r26)
            r6 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            r9 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            if (r8 <= 0) goto L_0x0023
            double r11 = r4 - r6
            int r8 = (r11 > r9 ? 1 : (r11 == r9 ? 0 : -1))
            if (r8 > 0) goto L_0x001d
            r11 = r6
            goto L_0x0024
        L_0x001d:
            org.osgeo.proj4j.ProjectionException r1 = new org.osgeo.proj4j.ProjectionException
            r1.<init>()
            throw r1
        L_0x0023:
            r11 = r4
        L_0x0024:
            double r13 = r11 * r11
            double r13 = r6 - r13
            double r13 = java.lang.Math.sqrt(r13)
            double r15 = java.lang.Math.abs(r4)
            int r8 = (r15 > r9 ? 1 : (r15 == r9 ? 0 : -1))
            r9 = 4
            r10 = 3
            r17 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
            r19 = 0
            if (r8 > 0) goto L_0x0043
            double r4 = r0.projectionLatitude
            r3.f410y = r4
            goto L_0x00c8
        L_0x0043:
            int r8 = r0.mode
            r15 = 1
            if (r8 == r15) goto L_0x00cc
            r15 = 2
            if (r8 == r15) goto L_0x00c1
            if (r8 == r10) goto L_0x0094
            if (r8 == r9) goto L_0x0051
            goto L_0x00c8
        L_0x0051:
            double r9 = r0.sinphi0
            double r9 = r9 * r13
            double r1 = r1 * r11
            double r6 = r0.cosphi0
            double r1 = r1 * r6
            double r1 = r1 / r4
            double r9 = r9 + r1
            r3.f410y = r9
            double r1 = r0.sinphi0
            double r6 = r3.f410y
            double r1 = r1 * r6
            double r13 = r13 - r1
            double r1 = r13 * r4
            double r4 = r0.cosphi0
            double r11 = r11 * r4
            double r4 = r24 * r11
            double r6 = r3.f410y
            double r6 = java.lang.Math.abs(r6)
            r9 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r6 = (r6 > r9 ? 1 : (r6 == r9 ? 0 : -1))
            if (r6 < 0) goto L_0x0086
            double r6 = r3.f410y
            int r6 = (r6 > r19 ? 1 : (r6 == r19 ? 0 : -1))
            if (r6 >= 0) goto L_0x0081
            r6 = -4613618979930100456(0xbff921fb54442d18, double:-1.5707963267948966)
            goto L_0x0083
        L_0x0081:
            r6 = r17
        L_0x0083:
            r3.f410y = r6
            goto L_0x008e
        L_0x0086:
            double r6 = r3.f410y
            double r6 = java.lang.Math.asin(r6)
            r3.f410y = r6
        L_0x008e:
            r21 = r1
            r1 = r4
            r4 = r21
            goto L_0x00d4
        L_0x0094:
            double r1 = r1 * r11
            double r1 = r1 / r4
            r3.f410y = r1
            double r1 = r24 * r11
            double r4 = r4 * r13
            double r6 = r3.f410y
            double r6 = java.lang.Math.abs(r6)
            r9 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r6 = (r6 > r9 ? 1 : (r6 == r9 ? 0 : -1))
            if (r6 < 0) goto L_0x00b8
            double r6 = r3.f410y
            int r6 = (r6 > r19 ? 1 : (r6 == r19 ? 0 : -1))
            if (r6 >= 0) goto L_0x00b3
            r6 = -4613618979930100456(0xbff921fb54442d18, double:-1.5707963267948966)
            goto L_0x00b5
        L_0x00b3:
            r6 = r17
        L_0x00b5:
            r3.f410y = r6
            goto L_0x00d4
        L_0x00b8:
            double r6 = r3.f410y
            double r6 = java.lang.Math.asin(r6)
            r3.f410y = r6
            goto L_0x00d4
        L_0x00c1:
            double r4 = java.lang.Math.acos(r11)
            double r4 = -r4
            r3.f410y = r4
        L_0x00c8:
            r4 = r1
            r1 = r24
            goto L_0x00d4
        L_0x00cc:
            double r1 = -r1
            double r4 = java.lang.Math.acos(r11)
            r3.f410y = r4
            goto L_0x00c8
        L_0x00d4:
            int r6 = (r4 > r19 ? 1 : (r4 == r19 ? 0 : -1))
            if (r6 != 0) goto L_0x00f6
            int r6 = r0.mode
            r7 = 4
            if (r6 == r7) goto L_0x00e2
            int r6 = r0.mode
            r7 = 3
            if (r6 != r7) goto L_0x00f6
        L_0x00e2:
            int r4 = (r1 > r19 ? 1 : (r1 == r19 ? 0 : -1))
            if (r4 != 0) goto L_0x00e9
            r1 = r19
            goto L_0x00fb
        L_0x00e9:
            int r1 = (r1 > r19 ? 1 : (r1 == r19 ? 0 : -1))
            if (r1 >= 0) goto L_0x00f3
            r1 = -4613618979930100456(0xbff921fb54442d18, double:-1.5707963267948966)
            goto L_0x00fb
        L_0x00f3:
            r1 = r17
            goto L_0x00fb
        L_0x00f6:
            double r15 = java.lang.Math.atan2(r1, r4)
            r1 = r15
        L_0x00fb:
            r3.f409x = r1
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.proj.OrthographicAzimuthalProjection.projectInverse(double, double, org.osgeo.proj4j.ProjCoordinate):org.osgeo.proj4j.ProjCoordinate");
    }
}
