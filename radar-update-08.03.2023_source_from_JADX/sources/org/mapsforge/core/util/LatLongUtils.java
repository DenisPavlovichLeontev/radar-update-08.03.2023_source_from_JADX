package org.mapsforge.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import kotlin.jvm.internal.ByteCompanionObject;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

public final class LatLongUtils {
    private static final double CONVERSION_FACTOR = 1000000.0d;
    private static final String DELIMITER = ",";
    public static final double EQUATORIAL_RADIUS = 6378137.0d;
    public static final double INVERSE_FLATTENING = 298.257223563d;
    public static final double LATITUDE_MAX = 90.0d;
    public static final double LATITUDE_MIN = -90.0d;
    public static final double LONGITUDE_MAX = 180.0d;
    public static final double LONGITUDE_MIN = -180.0d;
    public static final double POLAR_RADIUS = 6356752.3142d;

    public static int degreesToMicrodegrees(double d) {
        return (int) (d * 1000000.0d);
    }

    public static double latitudeDistance(int i) {
        return ((double) (i * 360)) / 4.007501668557849E7d;
    }

    public static double microdegreesToDegrees(int i) {
        return ((double) i) / 1000000.0d;
    }

    public static boolean contains(LatLong[] latLongArr, LatLong latLong) {
        int length = latLongArr.length - 1;
        boolean z = false;
        for (int i = 0; i < latLongArr.length; i++) {
            if ((latLongArr[i].latitude > latLong.latitude) != (latLongArr[length].latitude > latLong.latitude) && latLong.longitude < (((latLongArr[length].longitude - latLongArr[i].longitude) * (latLong.latitude - latLongArr[i].latitude)) / (latLongArr[length].latitude - latLongArr[i].latitude)) + latLongArr[i].longitude) {
                z = !z;
            }
            length = i;
        }
        return z;
    }

    public static boolean contains(List<LatLong> list, LatLong latLong) {
        int size = list.size() - 1;
        boolean z = false;
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).latitude > latLong.latitude) != (list.get(size).latitude > latLong.latitude) && latLong.longitude < (((list.get(size).longitude - list.get(i).longitude) * (latLong.latitude - list.get(i).latitude)) / (list.get(size).latitude - list.get(i).latitude)) + list.get(i).longitude) {
                z = !z;
            }
            size = i;
        }
        return z;
    }

    public static LatLong destinationPoint(LatLong latLong, double d, float f) {
        double radians = Math.toRadians((double) f);
        double d2 = d / 6378137.0d;
        double radians2 = Math.toRadians(latLong.latitude);
        double radians3 = Math.toRadians(latLong.longitude);
        double asin = Math.asin((Math.sin(radians2) * Math.cos(d2)) + (Math.cos(radians2) * Math.sin(d2) * Math.cos(radians)));
        return new LatLong(Math.toDegrees(asin), Math.toDegrees(radians3 + Math.atan2(Math.sin(radians) * Math.sin(d2) * Math.cos(radians2), Math.cos(d2) - (Math.sin(radians2) * Math.sin(asin)))));
    }

    public static double distance(LatLong latLong, LatLong latLong2) {
        return Math.hypot(latLong.longitude - latLong2.longitude, latLong.latitude - latLong2.latitude);
    }

    public static double distanceSegmentPoint(double d, double d2, double d3, double d4, double d5, double d6) {
        Point nearestSegmentPoint = nearestSegmentPoint(d, d2, d3, d4, d5, d6);
        return Math.hypot(nearestSegmentPoint.f381x - d5, nearestSegmentPoint.f382y - d6);
    }

    public static LatLong fromString(String str) {
        double[] parseCoordinateString = parseCoordinateString(str, 2);
        return new LatLong(parseCoordinateString[0], parseCoordinateString[1]);
    }

    public static boolean isClosedWay(LatLong[] latLongArr) {
        return latLongArr[0].distance(latLongArr[latLongArr.length - 1]) < 1.0E-9d;
    }

    public static double longitudeDistance(int i, double d) {
        return ((double) (i * 360)) / (Math.cos(Math.toRadians(d)) * 4.007501668557849E7d);
    }

    public static Point nearestSegmentPoint(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d;
        double d8 = d2;
        double d9 = d3;
        double d10 = d4;
        double d11 = d9 - d7;
        double d12 = d10 - d8;
        double d13 = (d11 * d11) + (d12 * d12);
        if (d13 == 0.0d) {
            return new Point(d7, d8);
        }
        double d14 = (((d5 - d7) * d11) + ((d6 - d8) * d12)) / d13;
        if (d14 < 0.0d) {
            return new Point(d7, d8);
        }
        if (d14 > 1.0d) {
            return new Point(d9, d10);
        }
        return new Point(d7 + (d11 * d14), d8 + (d14 * d12));
    }

    public static double[] parseCoordinateString(String str, int i) {
        boolean z = true;
        StringTokenizer stringTokenizer = new StringTokenizer(str, DELIMITER, true);
        ArrayList arrayList = new ArrayList(i);
        while (stringTokenizer.hasMoreTokens()) {
            String nextToken = stringTokenizer.nextToken();
            z = !z;
            if (!z) {
                arrayList.add(nextToken);
            }
        }
        if (z) {
            throw new IllegalArgumentException("invalid coordinate delimiter: " + str);
        } else if (arrayList.size() == i) {
            double[] dArr = new double[i];
            for (int i2 = 0; i2 < i; i2++) {
                dArr[i2] = Double.parseDouble((String) arrayList.get(i2));
            }
            return dArr;
        } else {
            throw new IllegalArgumentException("invalid number of coordinate values: " + str);
        }
    }

    public static double sphericalDistance(LatLong latLong, LatLong latLong2) {
        double radians = Math.toRadians(latLong2.latitude - latLong.latitude);
        double d = radians / 2.0d;
        double radians2 = Math.toRadians(latLong2.longitude - latLong.longitude) / 2.0d;
        double sin = (Math.sin(d) * Math.sin(d)) + (Math.cos(Math.toRadians(latLong.latitude)) * Math.cos(Math.toRadians(latLong2.latitude)) * Math.sin(radians2) * Math.sin(radians2));
        return Math.atan2(Math.sqrt(sin), Math.sqrt(1.0d - sin)) * 2.0d * 6378137.0d;
    }

    public static double validateLatitude(double d) {
        if (!Double.isNaN(d) && d >= -90.0d && d <= 90.0d) {
            return d;
        }
        throw new IllegalArgumentException("invalid latitude: " + d);
    }

    public static double validateLongitude(double d) {
        if (!Double.isNaN(d) && d >= -180.0d && d <= 180.0d) {
            return d;
        }
        throw new IllegalArgumentException("invalid longitude: " + d);
    }

    public static double vincentyDistance(LatLong latLong, LatLong latLong2) {
        double sqrt;
        double d;
        double atan2;
        double d2;
        double d3;
        double d4;
        double d5;
        double radians = Math.toRadians(latLong2.getLongitude() - latLong.getLongitude());
        double atan = Math.atan(Math.tan(Math.toRadians(latLong.getLatitude())) * 0.9966471893352525d);
        double atan3 = Math.atan(0.9966471893352525d * Math.tan(Math.toRadians(latLong2.getLatitude())));
        double sin = Math.sin(atan);
        double cos = Math.cos(atan);
        double sin2 = Math.sin(atan3);
        double cos2 = Math.cos(atan3);
        double d6 = 100.0d;
        double d7 = radians;
        while (true) {
            double sin3 = Math.sin(d7);
            double cos3 = Math.cos(d7);
            double d8 = cos2 * sin3;
            double d9 = (cos * sin2) - ((sin * cos2) * cos3);
            double d10 = d6;
            sqrt = Math.sqrt((d8 * d8) + (d9 * d9));
            if (sqrt != 0.0d) {
                double d11 = cos * cos2;
                double d12 = cos;
                d = (sin * sin2) + (cos3 * d11);
                atan2 = Math.atan2(sqrt, d);
                double d13 = (d11 * sin3) / sqrt;
                d2 = 1.0d - (d13 * d13);
                double d14 = cos2;
                d3 = d2 != 0.0d ? d - (((sin * 2.0d) * sin2) / d2) : 0.0d;
                double d15 = 2.0955066654671753E-4d * d2 * (((4.0d - (3.0d * d2)) * 0.0033528106647474805d) + 4.0d);
                d4 = ((d3 * 2.0d) * d3) - 4.0d;
                double d16 = radians + ((1.0d - d15) * 0.0033528106647474805d * d13 * (atan2 + (d15 * sqrt * (d3 + (d15 * d * d4)))));
                if (Math.abs(d16 - d7) <= 1.0E-12d) {
                    d5 = d10;
                    break;
                }
                d5 = d10 - 1.0d;
                if (d5 <= 0.0d) {
                    break;
                }
                d6 = d5;
                cos = d12;
                cos2 = d14;
                d7 = d16;
            } else {
                return 0.0d;
            }
        }
        if (d5 == 0.0d) {
            return 0.0d;
        }
        double pow = (d2 * (Math.pow(6378137.0d, 2.0d) - Math.pow(6356752.3142d, 2.0d))) / Math.pow(6356752.3142d, 2.0d);
        double d17 = (pow / 1024.0d) * ((pow * (((74.0d - (47.0d * pow)) * pow) - 0.03125d)) + 256.0d);
        return (((pow / 16384.0d) * (((((320.0d - (175.0d * pow)) * pow) - 0.005859375d) * pow) + 4096.0d)) + 1.0d) * 6356752.3142d * (atan2 - ((d17 * sqrt) * (d3 + ((d17 / 4.0d) * ((d * d4) - ((((d17 / 6.0d) * d3) * (((sqrt * 4.0d) * sqrt) - 1.5d)) * (((4.0d * d3) * d3) - 1.5d)))))));
    }

    public static byte zoomForBounds(Dimension dimension, BoundingBox boundingBox, int i) {
        long mapSize = MercatorProjection.getMapSize((byte) 0, i);
        double floor = Math.floor(Math.min((-Math.log(Math.abs(MercatorProjection.longitudeToPixelX(boundingBox.maxLongitude, mapSize) - MercatorProjection.longitudeToPixelX(boundingBox.minLongitude, mapSize)) / ((double) dimension.width))) / Math.log(2.0d), (-Math.log(Math.abs(MercatorProjection.latitudeToPixelY(boundingBox.maxLatitude, mapSize) - MercatorProjection.latitudeToPixelY(boundingBox.minLatitude, mapSize)) / ((double) dimension.height))) / Math.log(2.0d)));
        if (floor < 0.0d) {
            return 0;
        }
        return floor > 127.0d ? ByteCompanionObject.MAX_VALUE : (byte) ((int) floor);
    }

    private LatLongUtils() {
        throw new IllegalStateException();
    }
}
