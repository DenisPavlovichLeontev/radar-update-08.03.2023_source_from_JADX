package org.osgeo.proj4j.util;

import org.osgeo.proj4j.InvalidValueException;

public class ProjectionMath {
    private static final double C00 = 1.0d;
    private static final double C02 = 0.25d;
    private static final double C04 = 0.046875d;
    private static final double C06 = 0.01953125d;
    private static final double C08 = 0.01068115234375d;
    private static final double C22 = 0.75d;
    private static final double C44 = 0.46875d;
    private static final double C46 = 0.013020833333333334d;
    private static final double C48 = 0.007120768229166667d;
    private static final double C66 = 0.3645833333333333d;
    private static final double C68 = 0.005696614583333333d;
    private static final double C88 = 0.3076171875d;
    public static final double DTR = 0.017453292519943295d;
    public static final double EPS10 = 1.0E-10d;
    public static final double FORTPI = 0.7853981633974483d;
    public static final double HALFPI = 1.5707963267948966d;
    private static final int MAX_ITER = 10;
    public static final double MILLION = 1000000.0d;
    private static final int N_ITER = 15;
    private static final double P00 = 0.3333333333333333d;
    private static final double P01 = 0.17222222222222222d;
    private static final double P02 = 0.10257936507936508d;
    private static final double P10 = 0.06388888888888888d;
    private static final double P11 = 0.0664021164021164d;
    private static final double P20 = 0.016415012942191543d;

    /* renamed from: PI */
    public static final double f556PI = 3.141592653589793d;
    public static final double QUARTERPI = 0.7853981633974483d;
    public static final double RTD = 57.29577951308232d;
    public static final double SECONDS_TO_RAD = 4.84813681109536E-6d;
    public static final double TWOPI = 6.283185307179586d;

    public static double[] authset(double d) {
        double[] dArr = new double[3];
        double d2 = 0.3333333333333333d * d;
        dArr[0] = d2;
        double d3 = d * d;
        double d4 = d2 + (P01 * d3);
        dArr[0] = d4;
        double d5 = P10 * d3;
        dArr[1] = d5;
        double d6 = d3 * d;
        dArr[0] = d4 + (P02 * d6);
        dArr[1] = d5 + (P11 * d6);
        dArr[2] = d6 * P20;
        return dArr;
    }

    public static double cross(double d, double d2, double d3, double d4) {
        return (d * d4) - (d3 * d2);
    }

    public static double degToRad(double d) {
        return (d * 3.141592653589793d) / 180.0d;
    }

    public static double dmsToDeg(double d, double d2, double d3) {
        return d >= 0.0d ? d + (d2 / 60.0d) + (d3 / 3600.0d) : (d - (d2 / 60.0d)) - (d3 / 3600.0d);
    }

    public static double dmsToRad(double d, double d2, double d3) {
        return ((d >= 0.0d ? (d + (d2 / 60.0d)) + (d3 / 3600.0d) : (d - (d2 / 60.0d)) - (d3 / 3600.0d)) * 3.141592653589793d) / 180.0d;
    }

    public static double[] enfn(double d) {
        double d2 = ((((C08 * d) + C06) * d) + C04) * d;
        double d3 = d * d;
        double d4 = d3 * d;
        return new double[]{C00 - ((C02 + d2) * d), (C22 - d2) * d, (C44 - (((C48 * d) + C46) * d)) * d3, (C66 - (C68 * d)) * d4, d4 * d * C88};
    }

    public static double radToDeg(double d) {
        return (d * 180.0d) / 3.141592653589793d;
    }

    public static boolean sameSigns(double d, double d2) {
        return ((d > 0.0d ? 1 : (d == 0.0d ? 0 : -1)) < 0) == ((d2 > 0.0d ? 1 : (d2 == 0.0d ? 0 : -1)) < 0);
    }

    public static boolean sameSigns(int i, int i2) {
        return (i < 0) == (i2 < 0);
    }

    public static double sind(double d) {
        return Math.sin(d * 0.017453292519943295d);
    }

    public static double cosd(double d) {
        return Math.cos(d * 0.017453292519943295d);
    }

    public static double tand(double d) {
        return Math.tan(d * 0.017453292519943295d);
    }

    public static double asind(double d) {
        return Math.asin(d) * 57.29577951308232d;
    }

    public static double acosd(double d) {
        return Math.acos(d) * 57.29577951308232d;
    }

    public static double atand(double d) {
        return Math.atan(d) * 57.29577951308232d;
    }

    public static double atan2d(double d, double d2) {
        return Math.atan2(d, d2) * 57.29577951308232d;
    }

    public static double asin(double d) {
        if (Math.abs(d) > C00) {
            return d < 0.0d ? -1.5707963267948966d : 1.5707963267948966d;
        }
        return Math.asin(d);
    }

    public static double acos(double d) {
        if (Math.abs(d) > C00) {
            return d < 0.0d ? 3.141592653589793d : 0.0d;
        }
        return Math.acos(d);
    }

    public static double sqrt(double d) {
        if (d < 0.0d) {
            return 0.0d;
        }
        return Math.sqrt(d);
    }

    public static double distance(double d, double d2) {
        return Math.sqrt((d * d) + (d2 * d2));
    }

    public static double hypot(double d, double d2) {
        if (d < 0.0d) {
            d = -d;
        } else if (d == 0.0d) {
            return d2 < 0.0d ? -d2 : d2;
        }
        if (d2 < 0.0d) {
            d2 = -d2;
        } else if (d2 == 0.0d) {
            return d;
        }
        if (d < d2) {
            double d3 = d / d2;
            return d2 * Math.sqrt((d3 * d3) + C00);
        }
        double d4 = d2 / d;
        return d * Math.sqrt((d4 * d4) + C00);
    }

    public static double atan2(double d, double d2) {
        return Math.atan2(d, d2);
    }

    public static double trunc(double d) {
        return d < 0.0d ? Math.ceil(d) : Math.floor(d);
    }

    public static double frac(double d) {
        return d - trunc(d);
    }

    public static double normalizeLatitude(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new InvalidValueException("Infinite latitude");
        }
        while (d > 1.5707963267948966d) {
            d -= 3.141592653589793d;
        }
        while (d < -1.5707963267948966d) {
            d += 3.141592653589793d;
        }
        return d;
    }

    public static double normalizeLongitude(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new InvalidValueException("Infinite longitude");
        }
        while (d > 3.141592653589793d) {
            d -= 6.283185307179586d;
        }
        while (d < -3.141592653589793d) {
            d += 6.283185307179586d;
        }
        return d;
    }

    public static double normalizeAngle(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new InvalidValueException("Infinite angle");
        }
        while (d > 6.283185307179586d) {
            d -= 6.283185307179586d;
        }
        while (d < 0.0d) {
            d += 6.283185307179586d;
        }
        return d;
    }

    public static double greatCircleDistance(double d, double d2, double d3, double d4) {
        double sin = Math.sin((d4 - d2) / 2.0d);
        double sin2 = Math.sin((d3 - d) / 2.0d);
        return Math.asin(Math.sqrt((sin * sin) + (Math.cos(d2) * Math.cos(d4) * sin2 * sin2))) * 2.0d;
    }

    public static double sphericalAzimuth(double d, double d2, double d3, double d4) {
        double d5 = d4 - d2;
        double cos = Math.cos(d3);
        return Math.atan2(Math.sin(d5) * cos, (Math.cos(d) * Math.sin(d3)) - ((Math.sin(d) * cos) * Math.cos(d5)));
    }

    public static double takeSign(double d, double d2) {
        double abs = Math.abs(d);
        return d2 < 0.0d ? -abs : abs;
    }

    public static int takeSign(int i, int i2) {
        int abs = Math.abs(i);
        return i2 < 0 ? -abs : abs;
    }

    public static double longitudeDistance(double d, double d2) {
        return Math.min(Math.abs(d - d2), (d < 0.0d ? d + 3.141592653589793d : 3.141592653589793d - d) + (d2 < 0.0d ? d2 + 3.141592653589793d : 3.141592653589793d - d2));
    }

    public static double geocentricLatitude(double d, double d2) {
        double d3 = C00 - d2;
        return Math.atan(d3 * d3 * Math.tan(d));
    }

    public static double geographicLatitude(double d, double d2) {
        double d3 = C00 - d2;
        return Math.atan(Math.tan(d) / (d3 * d3));
    }

    public static double tsfn(double d, double d2, double d3) {
        double d4 = d2 * d3;
        return Math.tan((1.5707963267948966d - d) * 0.5d) / Math.pow((C00 - d4) / (d4 + C00), d3 * 0.5d);
    }

    public static double msfn(double d, double d2, double d3) {
        return d2 / Math.sqrt(C00 - ((d3 * d) * d));
    }

    /* JADX WARNING: Removed duplicated region for block: B:6:0x0041 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0042  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static double phi2(double r15, double r17) {
        /*
            r0 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r0 = r0 * r17
            double r2 = java.lang.Math.atan(r15)
            r4 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r2 = r2 * r4
            r6 = 4609753056924675352(0x3ff921fb54442d18, double:1.5707963267948966)
            double r2 = r6 - r2
            r8 = 15
        L_0x0014:
            double r9 = java.lang.Math.sin(r2)
            double r9 = r9 * r17
            r11 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r13 = r11 - r9
            double r9 = r9 + r11
            double r13 = r13 / r9
            double r9 = java.lang.Math.pow(r13, r0)
            double r9 = r9 * r15
            double r9 = java.lang.Math.atan(r9)
            double r9 = r9 * r4
            double r9 = r6 - r9
            double r9 = r9 - r2
            double r2 = r2 + r9
            double r9 = java.lang.Math.abs(r9)
            r11 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            int r9 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r9 <= 0) goto L_0x003f
            int r8 = r8 + -1
            if (r8 != 0) goto L_0x0014
        L_0x003f:
            if (r8 <= 0) goto L_0x0042
            return r2
        L_0x0042:
            org.osgeo.proj4j.ConvergenceFailureException r0 = new org.osgeo.proj4j.ConvergenceFailureException
            java.lang.String r1 = "Computation of phi2 failed to converage after 15 iterations"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.util.ProjectionMath.phi2(double, double):double");
    }

    public static double mlfn(double d, double d2, double d3, double[] dArr) {
        double d4 = d3 * d2;
        double d5 = d2 * d2;
        return (dArr[0] * d) - (d4 * (dArr[1] + (d5 * (dArr[2] + ((dArr[3] + (dArr[4] * d5)) * d5)))));
    }

    public static double inv_mlfn(double d, double d2, double[] dArr) {
        double d3 = C00 / (C00 - d2);
        double d4 = d;
        for (int i = 10; i != 0; i--) {
            double sin = Math.sin(d4);
            double d5 = C00 - ((d2 * sin) * sin);
            double mlfn = (mlfn(d4, sin, Math.cos(d4), dArr) - d) * d5 * Math.sqrt(d5) * d3;
            d4 -= mlfn;
            if (Math.abs(mlfn) < 1.0E-11d) {
                return d4;
            }
        }
        return d4;
    }

    public static double authlat(double d, double[] dArr) {
        double d2 = d + d;
        double d3 = d2 + d2;
        return d + (dArr[0] * Math.sin(d2)) + (dArr[1] * Math.sin(d3)) + (dArr[2] * Math.sin(d3 + d2));
    }

    public static double qsfn(double d, double d2, double d3) {
        if (d2 < 1.0E-7d) {
            return d + d;
        }
        double d4 = d2 * d;
        return d3 * ((d / (C00 - (d4 * d4))) - ((0.5d / d2) * Math.log((C00 - d4) / (d4 + C00))));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0041, code lost:
        if (r12 <= 5.0d) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0044, code lost:
        r4 = 10.0d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0030, code lost:
        if (r12 < 7.0d) goto L_0x0045;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static double niceNumber(double r12, boolean r14) {
        /*
            double r0 = java.lang.Math.log(r12)
            r2 = 4621819117588971520(0x4024000000000000, double:10.0)
            double r4 = java.lang.Math.log(r2)
            double r0 = r0 / r4
            double r0 = java.lang.Math.floor(r0)
            int r0 = (int) r0
            double r0 = (double) r0
            double r4 = java.lang.Math.pow(r2, r0)
            double r12 = r12 / r4
            r4 = 4617315517961601024(0x4014000000000000, double:5.0)
            r6 = 4611686018427387904(0x4000000000000000, double:2.0)
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            if (r14 == 0) goto L_0x0033
            r10 = 4609434218613702656(0x3ff8000000000000, double:1.5)
            int r14 = (r12 > r10 ? 1 : (r12 == r10 ? 0 : -1))
            if (r14 >= 0) goto L_0x0025
            goto L_0x0037
        L_0x0025:
            r8 = 4613937818241073152(0x4008000000000000, double:3.0)
            int r14 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
            if (r14 >= 0) goto L_0x002c
            goto L_0x003d
        L_0x002c:
            r6 = 4619567317775286272(0x401c000000000000, double:7.0)
            int r12 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1))
            if (r12 >= 0) goto L_0x0044
            goto L_0x0045
        L_0x0033:
            int r14 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
            if (r14 > 0) goto L_0x0039
        L_0x0037:
            r4 = r8
            goto L_0x0045
        L_0x0039:
            int r14 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1))
            if (r14 > 0) goto L_0x003f
        L_0x003d:
            r4 = r6
            goto L_0x0045
        L_0x003f:
            int r12 = (r12 > r4 ? 1 : (r12 == r4 ? 0 : -1))
            if (r12 > 0) goto L_0x0044
            goto L_0x0045
        L_0x0044:
            r4 = r2
        L_0x0045:
            double r12 = java.lang.Math.pow(r2, r0)
            double r4 = r4 * r12
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osgeo.proj4j.util.ProjectionMath.niceNumber(double, boolean):double");
    }
}
