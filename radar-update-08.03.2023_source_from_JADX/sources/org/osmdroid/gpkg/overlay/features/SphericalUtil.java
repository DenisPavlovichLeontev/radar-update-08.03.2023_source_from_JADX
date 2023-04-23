package org.osmdroid.gpkg.overlay.features;

import java.util.List;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

class SphericalUtil {
    static final double EARTH_RADIUS = 6371009.0d;

    static double clamp(double d, double d2, double d3) {
        return d < d2 ? d2 : d > d3 ? d3 : d;
    }

    static double mod(double d, double d2) {
        return ((d % d2) + d2) % d2;
    }

    static double wrap(double d, double d2, double d3) {
        return (d < d2 || d >= d3) ? mod(d - d2, d3 - d2) + d2 : d;
    }

    static double mercator(double d) {
        return Math.log(Math.tan((d * 0.5d) + 0.7853981633974483d));
    }

    static double inverseMercator(double d) {
        return (Math.atan(Math.exp(d)) * 2.0d) - 1.5707963267948966d;
    }

    static double hav(double d) {
        double sin = Math.sin(d * 0.5d);
        return sin * sin;
    }

    static double arcHav(double d) {
        return Math.asin(Math.sqrt(d)) * 2.0d;
    }

    static double sinFromHav(double d) {
        return Math.sqrt(d * (1.0d - d)) * 2.0d;
    }

    static double havFromSin(double d) {
        double d2 = d * d;
        return (d2 / (Math.sqrt(1.0d - d2) + 1.0d)) * 0.5d;
    }

    static double sinSumFromHav(double d, double d2) {
        double sqrt = Math.sqrt((1.0d - d) * d);
        double sqrt2 = Math.sqrt((1.0d - d2) * d2);
        return ((sqrt + sqrt2) - (((sqrt * d2) + (sqrt2 * d)) * 2.0d)) * 2.0d;
    }

    static double havDistance(double d, double d2, double d3) {
        return hav(d - d2) + (hav(d3) * Math.cos(d) * Math.cos(d2));
    }

    private SphericalUtil() {
    }

    public static double computeHeading(IGeoPoint iGeoPoint, IGeoPoint iGeoPoint2) {
        double radians = Math.toRadians(iGeoPoint.getLatitude());
        double radians2 = Math.toRadians(iGeoPoint.getLongitude());
        double radians3 = Math.toRadians(iGeoPoint2.getLatitude());
        double radians4 = Math.toRadians(iGeoPoint2.getLongitude()) - radians2;
        return wrap(Math.toDegrees(Math.atan2(Math.sin(radians4) * Math.cos(radians3), (Math.cos(radians) * Math.sin(radians3)) - ((Math.sin(radians) * Math.cos(radians3)) * Math.cos(radians4)))), -180.0d, 180.0d);
    }

    public static IGeoPoint computeOffset(IGeoPoint iGeoPoint, double d, double d2) {
        double d3 = d / EARTH_RADIUS;
        double radians = Math.toRadians(d2);
        double radians2 = Math.toRadians(iGeoPoint.getLatitude());
        double radians3 = Math.toRadians(iGeoPoint.getLongitude());
        double cos = Math.cos(d3);
        double sin = Math.sin(d3);
        double sin2 = Math.sin(radians2);
        double cos2 = sin * Math.cos(radians2);
        double cos3 = (cos * sin2) + (Math.cos(radians) * cos2);
        return new GeoPoint(Math.toDegrees(Math.asin(cos3)), Math.toDegrees(radians3 + Math.atan2(cos2 * Math.sin(radians), cos - (sin2 * cos3))));
    }

    public static IGeoPoint computeOffsetOrigin(IGeoPoint iGeoPoint, double d, double d2) {
        double radians = Math.toRadians(d2);
        double d3 = d / EARTH_RADIUS;
        double cos = Math.cos(d3);
        double sin = Math.sin(d3) * Math.cos(radians);
        double sin2 = Math.sin(d3) * Math.sin(radians);
        double sin3 = Math.sin(Math.toRadians(iGeoPoint.getLatitude()));
        double d4 = cos * cos;
        double d5 = sin * sin;
        double d6 = ((d5 * d4) + (d4 * d4)) - ((d4 * sin3) * sin3);
        if (d6 < 0.0d) {
            return null;
        }
        double d7 = sin * sin3;
        double d8 = d4 + d5;
        double sqrt = (d7 + Math.sqrt(d6)) / d8;
        double d9 = (sin3 - (sin * sqrt)) / cos;
        double atan2 = Math.atan2(d9, sqrt);
        if (atan2 < -1.5707963267948966d || atan2 > 1.5707963267948966d) {
            atan2 = Math.atan2(d9, (d7 - Math.sqrt(d6)) / d8);
        }
        if (atan2 < -1.5707963267948966d || atan2 > 1.5707963267948966d) {
            return null;
        }
        return new GeoPoint(Math.toDegrees(atan2), Math.toDegrees(Math.toRadians(iGeoPoint.getLongitude()) - Math.atan2(sin2, (cos * Math.cos(atan2)) - (sin * Math.sin(atan2)))));
    }

    public static IGeoPoint interpolate(IGeoPoint iGeoPoint, IGeoPoint iGeoPoint2, double d) {
        double radians = Math.toRadians(iGeoPoint.getLatitude());
        double radians2 = Math.toRadians(iGeoPoint.getLongitude());
        double radians3 = Math.toRadians(iGeoPoint2.getLatitude());
        double radians4 = Math.toRadians(iGeoPoint2.getLongitude());
        double cos = Math.cos(radians);
        double cos2 = Math.cos(radians3);
        double computeAngleBetween = computeAngleBetween(iGeoPoint, iGeoPoint2);
        double sin = Math.sin(computeAngleBetween);
        if (sin < 1.0E-6d) {
            return iGeoPoint;
        }
        double sin2 = Math.sin((1.0d - d) * computeAngleBetween) / sin;
        double sin3 = Math.sin(computeAngleBetween * d) / sin;
        double d2 = cos * sin2;
        double d3 = cos2 * sin3;
        double cos3 = (Math.cos(radians2) * d2) + (Math.cos(radians4) * d3);
        double sin4 = (d2 * Math.sin(radians2)) + (d3 * Math.sin(radians4));
        return new GeoPoint(Math.toDegrees(Math.atan2((sin2 * Math.sin(radians)) + (sin3 * Math.sin(radians3)), Math.sqrt((cos3 * cos3) + (sin4 * sin4)))), Math.toDegrees(Math.atan2(sin4, cos3)));
    }

    private static double distanceRadians(double d, double d2, double d3, double d4) {
        return arcHav(havDistance(d, d3, d2 - d4));
    }

    static double computeAngleBetween(IGeoPoint iGeoPoint, IGeoPoint iGeoPoint2) {
        return distanceRadians(Math.toRadians(iGeoPoint.getLatitude()), Math.toRadians(iGeoPoint.getLongitude()), Math.toRadians(iGeoPoint2.getLatitude()), Math.toRadians(iGeoPoint2.getLongitude()));
    }

    public static double computeDistanceBetween(IGeoPoint iGeoPoint, IGeoPoint iGeoPoint2) {
        return computeAngleBetween(iGeoPoint, iGeoPoint2) * EARTH_RADIUS;
    }

    public static double computeLength(List<IGeoPoint> list) {
        double d = 0.0d;
        if (list.size() < 2) {
            return 0.0d;
        }
        IGeoPoint iGeoPoint = list.get(0);
        double radians = Math.toRadians(iGeoPoint.getLatitude());
        double radians2 = Math.toRadians(iGeoPoint.getLongitude());
        for (IGeoPoint next : list) {
            double radians3 = Math.toRadians(next.getLatitude());
            double radians4 = Math.toRadians(next.getLongitude());
            d += distanceRadians(radians, radians2, radians3, radians4);
            radians = radians3;
            radians2 = radians4;
        }
        return d * EARTH_RADIUS;
    }

    public static double computeArea(List<IGeoPoint> list) {
        return Math.abs(computeSignedArea(list));
    }

    public static double computeSignedArea(List<IGeoPoint> list) {
        return computeSignedArea(list, EARTH_RADIUS);
    }

    static double computeSignedArea(List<IGeoPoint> list, double d) {
        int size = list.size();
        double d2 = 0.0d;
        if (size < 3) {
            return 0.0d;
        }
        IGeoPoint iGeoPoint = list.get(size - 1);
        double tan = Math.tan((1.5707963267948966d - Math.toRadians(iGeoPoint.getLatitude())) / 2.0d);
        double radians = Math.toRadians(iGeoPoint.getLongitude());
        double d3 = tan;
        double d4 = radians;
        for (IGeoPoint next : list) {
            double tan2 = Math.tan((1.5707963267948966d - Math.toRadians(next.getLatitude())) / 2.0d);
            double radians2 = Math.toRadians(next.getLongitude());
            d2 += polarTriangleArea(tan2, radians2, d3, d4);
            d3 = tan2;
            d4 = radians2;
        }
        return d2 * d * d;
    }

    private static double polarTriangleArea(double d, double d2, double d3, double d4) {
        double d5 = d2 - d4;
        double d6 = d * d3;
        return Math.atan2(Math.sin(d5) * d6, (d6 * Math.cos(d5)) + 1.0d) * 2.0d;
    }
}
