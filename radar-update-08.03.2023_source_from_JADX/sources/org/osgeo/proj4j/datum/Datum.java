package org.osgeo.proj4j.datum;

import org.osgeo.proj4j.ProjCoordinate;

public class Datum {
    public static final Datum CARTHAGE = new Datum("carthage", -263.0d, 6.0d, 431.0d, Ellipsoid.CLARKE_1880, "Carthage 1934 Tunisia");
    private static final double[] DEFAULT_TRANSFORM = {0.0d, 0.0d, 0.0d};
    public static final double ELLIPSOID_E2_TOLERANCE = 5.0E-11d;
    public static final Datum GGRS87 = new Datum("GGRS87", -199.87d, 74.79d, 246.62d, Ellipsoid.GRS80, "Greek_Geodetic_Reference_System_1987");
    public static final Datum HERMANNSKOGEL = new Datum("hermannskogel", 653.0d, -212.0d, 449.0d, Ellipsoid.BESSEL, "Hermannskogel");
    public static final Datum IRE65 = new Datum("ire65", 482.53d, -130.596d, 564.557d, -1.042d, -0.214d, -0.631d, 8.15d, Ellipsoid.MOD_AIRY, "Ireland 1965");
    public static final Datum NAD27 = new Datum("NAD27", "@conus,@alaska,@ntv2_0.gsb,@ntv1_can.dat", Ellipsoid.CLARKE_1866, "North_American_Datum_1927");
    public static final Datum NAD83 = new Datum("NAD83", 0.0d, 0.0d, 0.0d, Ellipsoid.GRS80, "North_American_Datum_1983");
    public static final Datum NZGD49 = new Datum("nzgd49", 59.47d, -5.04d, 187.44d, 0.47d, -0.1d, 1.024d, -4.5993d, Ellipsoid.INTERNATIONAL, "New Zealand Geodetic Datum 1949");
    public static final Datum OSEB36 = new Datum("OSGB36", 446.448d, -125.157d, 542.06d, 0.1502d, 0.247d, 0.8421d, -20.4894d, Ellipsoid.AIRY, "Airy 1830");
    public static final Datum POTSDAM = new Datum("potsdam", 606.0d, 23.0d, 413.0d, Ellipsoid.BESSEL, "Potsdam Rauenberg 1950 DHDN");
    public static final int TYPE_3PARAM = 2;
    public static final int TYPE_7PARAM = 3;
    public static final int TYPE_GRIDSHIFT = 4;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_WGS84 = 1;
    public static final Datum WGS84 = new Datum("WGS84", 0.0d, 0.0d, 0.0d, Ellipsoid.WGS84, "WGS84");
    private String code;
    private Ellipsoid ellipsoid;
    private String name;
    private double[] transform;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public Datum(String str, String str2, Ellipsoid ellipsoid2, String str3) {
        this(str, (double[]) null, ellipsoid2, str3);
        double[] dArr = null;
    }

    public Datum(String str, double d, double d2, double d3, Ellipsoid ellipsoid2, String str2) {
        this(str, new double[]{d, d2, d3}, ellipsoid2, str2);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public Datum(String str, double d, double d2, double d3, double d4, double d5, double d6, double d7, Ellipsoid ellipsoid2, String str2) {
        this(str, new double[]{d, d2, d3, d4, d5, d6, d7}, ellipsoid2, str2);
        String str3 = str;
    }

    public Datum(String str, double[] dArr, Ellipsoid ellipsoid2, String str2) {
        this.transform = DEFAULT_TRANSFORM;
        this.code = str;
        this.name = str2;
        this.ellipsoid = ellipsoid2;
        if (dArr != null) {
            this.transform = dArr;
        }
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return "[Datum-" + this.name + "]";
    }

    public Ellipsoid getEllipsoid() {
        return this.ellipsoid;
    }

    public double[] getTransformToWGS84() {
        return this.transform;
    }

    public int getTransformType() {
        double[] dArr = this.transform;
        if (dArr == null || isIdentity(dArr)) {
            return 1;
        }
        double[] dArr2 = this.transform;
        if (dArr2.length == 3) {
            return 2;
        }
        if (dArr2.length == 7) {
            return 3;
        }
        return 1;
    }

    private static boolean isIdentity(double[] dArr) {
        for (int i = 0; i < dArr.length; i++) {
            if (i == 6) {
                double d = dArr[i];
                if (!(d == 1.0d || d == 0.0d)) {
                    return false;
                }
            } else if (dArr[i] != 0.0d) {
                return false;
            }
        }
        return true;
    }

    public boolean hasTransformToWGS84() {
        int transformType = getTransformType();
        return transformType == 2 || transformType == 3;
    }

    public boolean isEqual(Datum datum) {
        if (getTransformType() != datum.getTransformType()) {
            return false;
        }
        if (this.ellipsoid.getEquatorRadius() != this.ellipsoid.getEquatorRadius() && Math.abs(this.ellipsoid.getEccentricitySquared() - datum.ellipsoid.getEccentricitySquared()) > 5.0E-11d) {
            return false;
        }
        if (getTransformType() != 2 && getTransformType() != 3) {
            return true;
        }
        int i = 0;
        while (true) {
            double[] dArr = this.transform;
            if (i >= dArr.length) {
                return true;
            }
            if (dArr[i] != datum.transform[i]) {
                return false;
            }
            i++;
        }
    }

    public void transformFromGeocentricToWgs84(ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double[] dArr = this.transform;
        if (dArr.length == 3) {
            projCoordinate2.f409x += this.transform[0];
            projCoordinate2.f410y += this.transform[1];
            projCoordinate2.f411z += this.transform[2];
        } else if (dArr.length == 7) {
            double d = dArr[0];
            double d2 = dArr[1];
            double d3 = dArr[2];
            double d4 = dArr[3];
            double d5 = dArr[4];
            double d6 = dArr[5];
            double d7 = dArr[6];
            double d8 = (((d6 * projCoordinate2.f409x) + projCoordinate2.f410y) - (projCoordinate2.f411z * d4)) * d7;
            double d9 = d7 * (((-d5) * projCoordinate2.f409x) + (d4 * projCoordinate2.f410y) + projCoordinate2.f411z);
            projCoordinate2.f409x = (((projCoordinate2.f409x - (projCoordinate2.f410y * d6)) + (projCoordinate2.f411z * d5)) * d7) + d;
            projCoordinate2.f410y = d8 + d2;
            projCoordinate2.f411z = d9 + d3;
        }
    }

    public void transformToGeocentricFromWgs84(ProjCoordinate projCoordinate) {
        ProjCoordinate projCoordinate2 = projCoordinate;
        double[] dArr = this.transform;
        if (dArr.length == 3) {
            projCoordinate2.f409x -= this.transform[0];
            projCoordinate2.f410y -= this.transform[1];
            projCoordinate2.f411z -= this.transform[2];
        } else if (dArr.length == 7) {
            double d = dArr[0];
            double d2 = dArr[1];
            double d3 = dArr[2];
            double d4 = dArr[3];
            double d5 = dArr[4];
            double d6 = dArr[5];
            double d7 = dArr[6];
            double d8 = d4;
            double d9 = (projCoordinate2.f409x - d) / d7;
            double d10 = (projCoordinate2.f410y - d2) / d7;
            double d11 = (projCoordinate2.f411z - d3) / d7;
            projCoordinate2.f409x = ((d6 * d10) + d9) - (d5 * d11);
            projCoordinate2.f410y = ((-d6) * d9) + d10 + (d8 * d11);
            projCoordinate2.f411z = ((d5 * d9) - (d8 * d10)) + d11;
        }
    }
}
