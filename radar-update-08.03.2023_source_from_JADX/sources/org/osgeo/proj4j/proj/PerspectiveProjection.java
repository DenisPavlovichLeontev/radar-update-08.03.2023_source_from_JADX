package org.osgeo.proj4j.proj;

public class PerspectiveProjection extends Projection {
    private static final double EPS10 = 1.0E-10d;
    private static final int EQUIT = 2;
    private static final int N_POLE = 0;
    private static final int OBLIQ = 3;
    private static final int S_POLE = 1;

    /* renamed from: cg */
    private double f529cg;

    /* renamed from: cw */
    private double f530cw;

    /* renamed from: h */
    private double f531h;
    private double height;
    private int mode;

    /* renamed from: p */
    private double f532p;
    private double pcosph0;
    private double pfact;
    private double pn1;
    private double psinph0;

    /* renamed from: rp */
    private double f533rp;

    /* renamed from: sg */
    private double f534sg;

    /* renamed from: sw */
    private double f535sw;
    private int tilt;

    public boolean hasInverse() {
        return false;
    }

    public String toString() {
        return "Perspective";
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0074  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.osgeo.proj4j.ProjCoordinate project(double r17, double r19, org.osgeo.proj4j.ProjCoordinate r21) {
        /*
            r16 = this;
            r0 = r16
            r1 = r21
            double r2 = java.lang.Math.sin(r19)
            double r4 = java.lang.Math.cos(r19)
            double r6 = java.lang.Math.cos(r17)
            int r8 = r0.mode
            r9 = 3
            r10 = 2
            r11 = 1
            if (r8 == 0) goto L_0x0032
            if (r8 == r11) goto L_0x002e
            if (r8 == r10) goto L_0x0029
            if (r8 == r9) goto L_0x001e
            goto L_0x0034
        L_0x001e:
            double r12 = r0.psinph0
            double r12 = r12 * r2
            double r14 = r0.pcosph0
            double r14 = r14 * r4
            double r14 = r14 * r6
            double r12 = r12 + r14
            r1.f410y = r12
            goto L_0x0034
        L_0x0029:
            double r12 = r4 * r6
            r1.f410y = r12
            goto L_0x0034
        L_0x002e:
            double r12 = -r2
            r1.f410y = r12
            goto L_0x0034
        L_0x0032:
            r1.f410y = r2
        L_0x0034:
            double r12 = r0.pn1
            double r14 = r0.f532p
            double r9 = r1.f410y
            double r14 = r14 - r9
            double r12 = r12 / r14
            r1.f410y = r12
            double r8 = r1.f410y
            double r8 = r8 * r4
            double r12 = java.lang.Math.sin(r17)
            double r8 = r8 * r12
            r1.f409x = r8
            int r8 = r0.mode
            if (r8 == 0) goto L_0x0069
            if (r8 == r11) goto L_0x006a
            r9 = 2
            if (r8 == r9) goto L_0x0063
            r9 = 3
            if (r8 == r9) goto L_0x0055
            goto L_0x0070
        L_0x0055:
            double r8 = r1.f410y
            double r10 = r0.pcosph0
            double r10 = r10 * r2
            double r2 = r0.psinph0
            double r2 = r2 * r4
            double r2 = r2 * r6
            double r10 = r10 - r2
            double r8 = r8 * r10
            r1.f410y = r8
            goto L_0x0070
        L_0x0063:
            double r4 = r1.f410y
            double r4 = r4 * r2
            r1.f410y = r4
            goto L_0x0070
        L_0x0069:
            double r6 = -r6
        L_0x006a:
            double r2 = r1.f410y
            double r4 = r4 * r6
            double r2 = r2 * r4
            r1.f410y = r2
        L_0x0070:
            int r2 = r0.tilt
            if (r2 == 0) goto L_0x009f
            double r2 = r1.f410y
            double r4 = r0.f529cg
            double r2 = r2 * r4
            double r4 = r1.f409x
            double r6 = r0.f534sg
            double r4 = r4 * r6
            double r2 = r2 + r4
            r4 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r6 = r0.f535sw
            double r6 = r6 * r2
            double r8 = r0.f531h
            double r6 = r6 * r8
            double r8 = r0.f530cw
            double r6 = r6 + r8
            double r4 = r4 / r6
            double r6 = r1.f409x
            double r8 = r0.f529cg
            double r6 = r6 * r8
            double r8 = r1.f410y
            double r10 = r0.f534sg
            double r8 = r8 * r10
            double r6 = r6 - r8
            double r8 = r0.f530cw
            double r6 = r6 * r8
            double r6 = r6 * r4
            r1.f409x = r6
            double r2 = r2 * r4
            r1.f410y = r2
        L_0x009f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.proj.PerspectiveProjection.project(double, double, org.osgeo.proj4j.ProjCoordinate):org.osgeo.proj4j.ProjCoordinate");
    }

    public void initialize() {
        super.initialize();
        this.mode = 2;
        double d = this.f537a;
        this.height = d;
        this.tilt = 0;
        double d2 = d / this.f537a;
        this.pn1 = d2;
        double d3 = d2 + 1.0d;
        this.f532p = d3;
        this.f533rp = 1.0d / d3;
        double d4 = 1.0d / d2;
        this.f531h = d4;
        this.pfact = (d3 + 1.0d) * d4;
        this.f539es = 0.0d;
    }
}
