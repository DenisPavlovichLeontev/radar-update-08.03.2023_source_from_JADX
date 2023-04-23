package org.osmdroid.util;

public class TileSystemWebMercator extends TileSystem {
    public static final double MaxLatitude = 85.05112877980658d;
    public static final double MaxLongitude = 180.0d;
    public static final double MinLatitude = -85.05112877980658d;
    public static final double MinLongitude = -180.0d;

    public double getMaxLatitude() {
        return 85.05112877980658d;
    }

    public double getMaxLongitude() {
        return 180.0d;
    }

    public double getMinLatitude() {
        return -85.05112877980658d;
    }

    public double getMinLongitude() {
        return -180.0d;
    }

    public double getX01FromLongitude(double d) {
        return (d - getMinLongitude()) / (getMaxLongitude() - getMinLongitude());
    }

    public double getY01FromLatitude(double d) {
        double sin = Math.sin((d * 3.141592653589793d) / 180.0d);
        return 0.5d - (Math.log((sin + 1.0d) / (1.0d - sin)) / 12.566370614359172d);
    }

    public double getLongitudeFromX01(double d) {
        return getMinLongitude() + ((getMaxLongitude() - getMinLongitude()) * d);
    }

    public double getLatitudeFromY01(double d) {
        return 90.0d - ((Math.atan(Math.exp(((d - 0.5d) * 2.0d) * 3.141592653589793d)) * 360.0d) / 3.141592653589793d);
    }
}
