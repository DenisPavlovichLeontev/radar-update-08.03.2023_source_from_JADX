package org.osgeo.proj4j.datum;

import org.osgeo.proj4j.ProjCoordinate;

public class GeocentricConverter {

    /* renamed from: a */
    double f412a;

    /* renamed from: a2 */
    double f413a2;

    /* renamed from: b */
    double f414b;

    /* renamed from: b2 */
    double f415b2;

    /* renamed from: e2 */
    double f416e2;
    double ep2;

    public GeocentricConverter(Ellipsoid ellipsoid) {
        this(ellipsoid.getA(), ellipsoid.getB());
    }

    public GeocentricConverter(double d, double d2) {
        this.f412a = d;
        this.f414b = d2;
        double d3 = d * d;
        this.f413a2 = d3;
        double d4 = d2 * d2;
        this.f415b2 = d4;
        this.f416e2 = (d3 - d4) / d3;
        this.ep2 = (d3 - d4) / d4;
    }

    public void convertGeodeticToGeocentric(ProjCoordinate projCoordinate) {
        double d = projCoordinate.f409x;
        double d2 = projCoordinate.f410y;
        double d3 = projCoordinate.hasValidZOrdinate() ? projCoordinate.f411z : 0.0d;
        int i = (d2 > -1.5707963267948966d ? 1 : (d2 == -1.5707963267948966d ? 0 : -1));
        if (i >= 0 || d2 <= -1.5723671231216914d) {
            int i2 = (d2 > 1.5707963267948966d ? 1 : (d2 == 1.5707963267948966d ? 0 : -1));
            if (i2 > 0 && d2 < 1.5723671231216914d) {
                d2 = 1.5707963267948966d;
            } else if (i < 0 || i2 > 0) {
                throw new IllegalStateException("Latitude is out of range: " + d2);
            }
        } else {
            d2 = -1.5707963267948966d;
        }
        if (d > 3.141592653589793d) {
            d -= 6.283185307179586d;
        }
        double sin = Math.sin(d2);
        double cos = Math.cos(d2);
        double sqrt = this.f412a / Math.sqrt(1.0d - (this.f416e2 * (sin * sin)));
        double d4 = (sqrt + d3) * cos;
        double cos2 = Math.cos(d) * d4;
        double sin2 = d4 * Math.sin(d);
        projCoordinate.f409x = cos2;
        projCoordinate.f410y = sin2;
        projCoordinate.f411z = ((sqrt * (1.0d - this.f416e2)) + d3) * sin;
    }

    public void convertGeocentricToGeodetic(ProjCoordinate projCoordinate) {
        convertGeocentricToGeodeticIter(projCoordinate);
    }

    public void convertGeocentricToGeodeticIter(ProjCoordinate projCoordinate) {
        double d;
        double d2;
        double d3;
        double d4;
        ProjCoordinate projCoordinate2 = projCoordinate;
        double d5 = projCoordinate2.f409x;
        double d6 = projCoordinate2.f410y;
        double d7 = projCoordinate.hasValidZOrdinate() ? projCoordinate2.f411z : 0.0d;
        double d8 = (d5 * d5) + (d6 * d6);
        double sqrt = Math.sqrt(d8);
        double sqrt2 = Math.sqrt(d8 + (d7 * d7));
        double d9 = this.f412a;
        if (sqrt / d9 >= 1.0E-12d) {
            d = Math.atan2(d6, d5);
        } else if (sqrt2 / d9 >= 1.0E-12d) {
            d = 0.0d;
        } else {
            return;
        }
        double d10 = d7 / sqrt2;
        double d11 = sqrt / sqrt2;
        double d12 = this.f416e2;
        double sqrt3 = 1.0d / Math.sqrt(1.0d - (((d12 * (2.0d - d12)) * d11) * d11));
        double d13 = d;
        double d14 = (1.0d - this.f416e2) * d11 * sqrt3;
        double d15 = sqrt3 * d10;
        int i = 0;
        while (true) {
            i++;
            double d16 = d10;
            double d17 = d11;
            double sqrt4 = this.f412a / Math.sqrt(1.0d - ((this.f416e2 * d15) * d15));
            double d18 = (sqrt * d14) + (d7 * d15);
            double d19 = d7;
            double d20 = this.f416e2;
            d2 = d18 - ((1.0d - ((d20 * d15) * d15)) * sqrt4);
            double d21 = (d20 * sqrt4) / (sqrt4 + d2);
            double sqrt5 = 1.0d / Math.sqrt(1.0d - ((((2.0d - d21) * d21) * d17) * d17));
            d3 = (1.0d - d21) * d17 * sqrt5;
            d4 = sqrt5 * d16;
            double d22 = (d14 * d4) - (d15 * d3);
            if (d22 * d22 <= 1.0E-24d || i >= 30) {
                double atan = Math.atan(d4 / Math.abs(d3));
                ProjCoordinate projCoordinate3 = projCoordinate;
                projCoordinate3.f409x = d13;
                projCoordinate3.f410y = atan;
                projCoordinate3.f411z = d2;
            } else {
                d15 = d4;
                d14 = d3;
                d10 = d16;
                d11 = d17;
                d7 = d19;
                ProjCoordinate projCoordinate4 = projCoordinate;
            }
        }
        double atan2 = Math.atan(d4 / Math.abs(d3));
        ProjCoordinate projCoordinate32 = projCoordinate;
        projCoordinate32.f409x = d13;
        projCoordinate32.f410y = atan2;
        projCoordinate32.f411z = d2;
    }
}
