package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.ProjectionException;
import org.osgeo.proj4j.datum.Ellipsoid;
import org.osgeo.proj4j.util.ProjectionMath;

public class ObliqueMercatorProjection extends CylindricalProjection {
    private static final double TOL = 1.0E-7d;
    private double Gamma;

    /* renamed from: al */
    private double f525al;

    /* renamed from: bl */
    private double f526bl;
    private double cosgam;
    private double cosrot;

    /* renamed from: el */
    private double f527el;
    private boolean ellips;
    private double lam1;
    private double lam2;
    private double lamc;
    private double phi1;
    private double phi2;
    private boolean rot;
    private double singam;
    private double sinrot;
    private double u_0;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Oblique Mercator";
    }

    public ObliqueMercatorProjection() {
        this.ellipsoid = Ellipsoid.WGS84;
        this.projectionLatitude = Math.toRadians(0.0d);
        this.projectionLongitude = Math.toRadians(0.0d);
        this.minLongitude = Math.toRadians(-60.0d);
        this.maxLongitude = Math.toRadians(60.0d);
        this.minLatitude = Math.toRadians(-80.0d);
        this.maxLatitude = Math.toRadians(80.0d);
        this.alpha = Math.toRadians(-45.0d);
        initialize();
    }

    public ObliqueMercatorProjection(Ellipsoid ellipsoid, double d, double d2, double d3, double d4, double d5, double d6) {
        setEllipsoid(ellipsoid);
        this.lamc = d;
        this.projectionLatitude = d2;
        this.alpha = d3;
        this.scaleFactor = d4;
        this.falseEasting = d5;
        this.falseNorthing = d6;
        initialize();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x008c, code lost:
        if (java.lang.Math.abs(java.lang.Math.abs(r0.phi2) - 1.5707963267948966d) > TOL) goto L_0x008e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initialize() {
        /*
            r25 = this;
            r0 = r25
            super.initialize()
            r1 = 1
            r0.rot = r1
            double r2 = r0.lonc
            r0.lamc = r2
            double r2 = r0.alpha
            boolean r2 = java.lang.Double.isNaN(r2)
            r2 = r2 ^ r1
            r3 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
            r5 = 4502148214488346440(0x3e7ad7f29abcaf48, double:1.0E-7)
            if (r2 == 0) goto L_0x0050
            double r7 = r0.alpha
            double r7 = java.lang.Math.abs(r7)
            int r7 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r7 <= 0) goto L_0x0048
            double r7 = r0.projectionLatitude
            double r7 = java.lang.Math.abs(r7)
            double r7 = r7 - r3
            double r7 = java.lang.Math.abs(r7)
            int r7 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r7 <= 0) goto L_0x0048
            double r7 = r0.alpha
            double r7 = java.lang.Math.abs(r7)
            double r7 = r7 - r3
            double r7 = java.lang.Math.abs(r7)
            int r5 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r5 <= 0) goto L_0x0048
            goto L_0x008e
        L_0x0048:
            org.osgeo.proj4j.ProjectionException r1 = new org.osgeo.proj4j.ProjectionException
            java.lang.String r2 = "Obl 1"
            r1.<init>(r2)
            throw r1
        L_0x0050:
            double r7 = r0.phi1
            double r9 = r0.phi2
            double r7 = r7 - r9
            double r7 = java.lang.Math.abs(r7)
            int r7 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r7 <= 0) goto L_0x0282
            double r7 = r0.phi1
            double r7 = java.lang.Math.abs(r7)
            int r9 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r9 <= 0) goto L_0x0282
            double r7 = r7 - r3
            double r7 = java.lang.Math.abs(r7)
            int r7 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r7 <= 0) goto L_0x0282
            double r7 = r0.projectionLatitude
            double r7 = java.lang.Math.abs(r7)
            double r7 = r7 - r3
            double r7 = java.lang.Math.abs(r7)
            int r7 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r7 <= 0) goto L_0x0282
            double r7 = r0.phi2
            double r7 = java.lang.Math.abs(r7)
            double r7 = r7 - r3
            double r7 = java.lang.Math.abs(r7)
            int r5 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r5 <= 0) goto L_0x0282
        L_0x008e:
            double r5 = r0.f539es
            r7 = 0
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 != 0) goto L_0x0097
            goto L_0x0098
        L_0x0097:
            r1 = 0
        L_0x0098:
            r0.spherical = r1
            r5 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            if (r1 == 0) goto L_0x00a0
            r9 = r5
            goto L_0x00a6
        L_0x00a0:
            double r9 = r0.one_es
            double r9 = java.lang.Math.sqrt(r9)
        L_0x00a6:
            double r11 = r0.projectionLatitude
            double r11 = java.lang.Math.abs(r11)
            r13 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            int r1 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r1 <= 0) goto L_0x0145
            double r13 = r0.projectionLatitude
            double r17 = java.lang.Math.sin(r13)
            double r13 = r0.projectionLatitude
            double r13 = java.lang.Math.cos(r13)
            boolean r1 = r0.spherical
            if (r1 != 0) goto L_0x00f2
            double r11 = r0.f539es
            double r11 = r11 * r17
            double r11 = r11 * r17
            double r11 = r5 - r11
            double r3 = r13 * r13
            r0.f526bl = r3
            double r3 = r0.f539es
            double r7 = r0.f526bl
            double r3 = r3 * r7
            double r3 = r3 * r7
            double r7 = r0.one_es
            double r3 = r3 / r7
            double r3 = r3 + r5
            double r3 = java.lang.Math.sqrt(r3)
            r0.f526bl = r3
            double r7 = r0.scaleFactor
            double r3 = r3 * r7
            double r3 = r3 * r9
            double r3 = r3 / r11
            r0.f525al = r3
            double r3 = r0.f526bl
            double r3 = r3 * r9
            double r7 = java.lang.Math.sqrt(r11)
            double r13 = r13 * r7
            double r3 = r3 / r13
            goto L_0x00fa
        L_0x00f2:
            r0.f526bl = r5
            double r3 = r0.scaleFactor
            r0.f525al = r3
            double r3 = r5 / r13
        L_0x00fa:
            double r7 = r3 * r3
            double r7 = r7 - r5
            r23 = 0
            int r1 = (r7 > r23 ? 1 : (r7 == r23 ? 0 : -1))
            if (r1 > 0) goto L_0x0106
            r7 = r23
            goto L_0x0111
        L_0x0106:
            double r7 = java.lang.Math.sqrt(r7)
            double r9 = r0.projectionLatitude
            int r1 = (r9 > r23 ? 1 : (r9 == r23 ? 0 : -1))
            if (r1 >= 0) goto L_0x0111
            double r7 = -r7
        L_0x0111:
            double r7 = r7 + r3
            r0.f527el = r7
            boolean r1 = r0.spherical
            if (r1 != 0) goto L_0x012f
            double r9 = r0.f527el
            double r11 = r0.projectionLatitude
            double r13 = r0.f538e
            r15 = r11
            r19 = r13
            double r11 = org.osgeo.proj4j.util.ProjectionMath.tsfn(r15, r17, r19)
            double r13 = r0.f526bl
            double r11 = java.lang.Math.pow(r11, r13)
            double r9 = r9 * r11
            r0.f527el = r9
            goto L_0x0151
        L_0x012f:
            double r9 = r0.f527el
            double r11 = r0.projectionLatitude
            r13 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
            double r11 = r13 - r11
            r13 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r11 = r11 * r13
            double r11 = java.lang.Math.tan(r11)
            double r9 = r9 * r11
            r0.f527el = r9
            goto L_0x0151
        L_0x0145:
            double r3 = r5 / r9
            r0.f526bl = r3
            double r3 = r0.scaleFactor
            r0.f525al = r3
            r0.f527el = r5
            r3 = r5
            r7 = r3
        L_0x0151:
            if (r2 == 0) goto L_0x0179
            double r1 = r0.alpha
            double r1 = java.lang.Math.sin(r1)
            double r1 = r1 / r3
            double r1 = java.lang.Math.asin(r1)
            r0.Gamma = r1
            double r9 = r0.lamc
            double r11 = r5 / r7
            double r7 = r7 - r11
            r11 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r7 = r7 * r11
            double r1 = java.lang.Math.tan(r1)
            double r7 = r7 * r1
            double r1 = java.lang.Math.asin(r7)
            double r7 = r0.f526bl
            double r1 = r1 / r7
            double r9 = r9 - r1
            r0.projectionLongitude = r9
            goto L_0x023b
        L_0x0179:
            boolean r1 = r0.spherical
            if (r1 != 0) goto L_0x01a2
            double r7 = r0.phi1
            double r9 = java.lang.Math.sin(r7)
            double r11 = r0.f538e
            double r1 = org.osgeo.proj4j.util.ProjectionMath.tsfn(r7, r9, r11)
            double r7 = r0.f526bl
            double r1 = java.lang.Math.pow(r1, r7)
            double r7 = r0.phi2
            double r9 = java.lang.Math.sin(r7)
            double r11 = r0.f538e
            double r7 = org.osgeo.proj4j.util.ProjectionMath.tsfn(r7, r9, r11)
            double r9 = r0.f526bl
            double r7 = java.lang.Math.pow(r7, r9)
            goto L_0x01ba
        L_0x01a2:
            double r1 = r0.phi1
            r7 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
            double r1 = r7 - r1
            r9 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r1 = r1 * r9
            double r1 = java.lang.Math.tan(r1)
            double r11 = r0.phi2
            double r7 = r7 - r11
            double r7 = r7 * r9
            double r7 = java.lang.Math.tan(r7)
        L_0x01ba:
            double r9 = r0.f527el
            double r11 = r9 / r1
            double r13 = r7 - r1
            double r15 = r7 + r1
            double r13 = r13 / r15
            double r9 = r9 * r9
            double r7 = r7 * r1
            double r1 = r9 - r7
            double r9 = r9 + r7
            double r1 = r1 / r9
            double r7 = r0.lam1
            double r9 = r0.lam2
            double r15 = r7 - r9
            r17 = -4609115380302729960(0xc00921fb54442d18, double:-3.141592653589793)
            int r17 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1))
            r18 = 4618760256179416344(0x401921fb54442d18, double:6.283185307179586)
            if (r17 >= 0) goto L_0x01e2
            double r9 = r9 - r18
            r0.lam2 = r9
            goto L_0x01ef
        L_0x01e2:
            r21 = 4614256656552045848(0x400921fb54442d18, double:3.141592653589793)
            int r15 = (r15 > r21 ? 1 : (r15 == r21 ? 0 : -1))
            if (r15 <= 0) goto L_0x01ef
            double r9 = r9 + r18
            r0.lam2 = r9
        L_0x01ef:
            double r9 = r0.lam2
            double r15 = r7 + r9
            r17 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r15 = r15 * r17
            double r5 = r0.f526bl
            double r5 = r5 * r17
            double r7 = r7 - r9
            double r5 = r5 * r7
            double r5 = java.lang.Math.tan(r5)
            double r1 = r1 * r5
            double r1 = r1 / r13
            double r1 = java.lang.Math.atan(r1)
            double r5 = r0.f526bl
            double r1 = r1 / r5
            double r15 = r15 - r1
            double r1 = org.osgeo.proj4j.util.ProjectionMath.normalizeLongitude(r15)
            r0.projectionLongitude = r1
            r1 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r5 = r0.f526bl
            double r7 = r0.lam1
            double r9 = r0.projectionLongitude
            double r7 = r7 - r9
            double r7 = org.osgeo.proj4j.util.ProjectionMath.normalizeLongitude(r7)
            double r5 = r5 * r7
            double r5 = java.lang.Math.sin(r5)
            double r5 = r5 * r1
            r1 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r7 = r1 / r11
            double r11 = r11 - r7
            double r5 = r5 / r11
            double r1 = java.lang.Math.atan(r5)
            r0.Gamma = r1
            double r1 = java.lang.Math.sin(r1)
            double r1 = r1 * r3
            double r1 = java.lang.Math.asin(r1)
            r0.alpha = r1
        L_0x023b:
            double r1 = r0.Gamma
            double r1 = java.lang.Math.sin(r1)
            r0.singam = r1
            double r1 = r0.Gamma
            double r1 = java.lang.Math.cos(r1)
            r0.cosgam = r1
            double r1 = r0.alpha
            double r5 = java.lang.Math.sin(r1)
            r0.sinrot = r5
            double r1 = java.lang.Math.cos(r1)
            r0.cosrot = r1
            double r1 = r0.f525al
            double r3 = r3 * r3
            r5 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r3 = r3 - r5
            double r3 = java.lang.Math.sqrt(r3)
            double r5 = r0.cosrot
            double r3 = r3 / r5
            double r3 = java.lang.Math.atan(r3)
            double r1 = r1 * r3
            double r3 = r0.f526bl
            double r1 = r1 / r3
            double r1 = java.lang.Math.abs(r1)
            r0.u_0 = r1
            double r1 = r0.projectionLatitude
            r3 = 0
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 >= 0) goto L_0x0281
            double r1 = r0.u_0
            double r1 = -r1
            r0.u_0 = r1
        L_0x0281:
            return
        L_0x0282:
            org.osgeo.proj4j.ProjectionException r1 = new org.osgeo.proj4j.ProjectionException
            java.lang.String r2 = "Obl 2"
            r1.<init>(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.proj.ObliqueMercatorProjection.initialize():void");
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        double d5;
        double d6;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double sin = Math.sin(this.f526bl * d);
        if (Math.abs(Math.abs(d2) - 1.5707963267948966d) <= 1.0E-10d) {
            d4 = d2 < 0.0d ? -this.singam : this.singam;
            d3 = (this.f525al * d2) / this.f526bl;
        } else {
            double d7 = this.f527el;
            if (!this.spherical) {
                d5 = d7;
                d6 = Math.pow(ProjectionMath.tsfn(d2, Math.sin(d2), this.f538e), this.f526bl);
            } else {
                d5 = d7;
                d6 = Math.tan((1.5707963267948966d - d2) * 0.5d);
            }
            double d8 = d5 / d6;
            double d9 = 1.0d / d8;
            double d10 = (d8 - d9) * 0.5d;
            d4 = (((this.singam * d10) - (this.cosgam * sin)) * 2.0d) / (d8 + d9);
            double cos = Math.cos(this.f526bl * d);
            if (Math.abs(cos) >= TOL) {
                double atan = this.f525al * Math.atan(((d10 * this.cosgam) + (sin * this.singam)) / cos);
                double d11 = this.f526bl;
                double d12 = atan / d11;
                d3 = cos < 0.0d ? d12 + ((this.f525al * 3.141592653589793d) / d11) : d12;
            } else {
                d3 = this.f525al * this.f526bl * d;
            }
        }
        if (Math.abs(Math.abs(d4) - 1.0d) > 1.0E-10d) {
            double log = ((this.f525al * 0.5d) * Math.log((1.0d - d4) / (d4 + 1.0d))) / this.f526bl;
            double d13 = d3 - this.u_0;
            if (!this.rot) {
                projCoordinate2.f409x = d13;
                projCoordinate2.f410y = log;
            } else {
                projCoordinate2.f409x = (this.cosrot * log) + (this.sinrot * d13);
                projCoordinate2.f410y = (d13 * this.cosrot) - (log * this.sinrot);
            }
            return projCoordinate2;
        }
        throw new ProjectionException("Obl 3");
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        double d3;
        double d4;
        ProjCoordinate projCoordinate2 = projCoordinate;
        if (!this.rot) {
            d4 = d;
            d3 = d2;
        } else {
            double d5 = this.cosrot;
            double d6 = this.sinrot;
            d3 = (d * d5) - (d2 * d6);
            d4 = (d5 * d2) + (d6 * d);
        }
        double d7 = d4 + this.u_0;
        double exp = Math.exp(((-this.f526bl) * d3) / this.f525al);
        double d8 = 1.0d / exp;
        double d9 = (exp - d8) * 0.5d;
        double sin = Math.sin((this.f526bl * d7) / this.f525al);
        double d10 = (((this.cosgam * sin) + (this.singam * d9)) * 2.0d) / (exp + d8);
        double d11 = 1.5707963267948966d;
        if (Math.abs(Math.abs(d10) - 1.0d) < 1.0E-10d) {
            projCoordinate2.f409x = 0.0d;
            if (d10 < 0.0d) {
                d11 = -1.5707963267948966d;
            }
            projCoordinate2.f410y = d11;
        } else {
            projCoordinate2.f410y = this.f527el / Math.sqrt((d10 + 1.0d) / (1.0d - d10));
            if (!this.spherical) {
                projCoordinate2.f410y = ProjectionMath.phi2(Math.pow(projCoordinate2.f410y, 1.0d / this.f526bl), this.f538e);
            } else {
                projCoordinate2.f410y = 1.5707963267948966d - (Math.atan(projCoordinate2.f410y) * 2.0d);
            }
            projCoordinate2.f409x = (-Math.atan2((d9 * this.cosgam) - (sin * this.singam), Math.cos((this.f526bl * d7) / this.f525al))) / this.f526bl;
        }
        return projCoordinate2;
    }
}
