package org.mapsforge.core.util;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;

public final class MercatorProjection {
    private static final int DUMMY_TILE_SIZE = 256;
    public static final double EARTH_CIRCUMFERENCE = 4.0075016686E7d;
    public static final double LATITUDE_MAX = 85.05112877980659d;
    public static final double LATITUDE_MIN = -85.05112877980659d;

    public static double longitudeToPixelX(double d, long j) {
        return ((d + 180.0d) / 360.0d) * ((double) j);
    }

    public static long tileToPixel(long j, int i) {
        return j * ((long) i);
    }

    public static double calculateGroundResolutionWithScaleFactor(double d, double d2, int i) {
        return (Math.cos(d * 0.017453292519943295d) * 4.0075016686E7d) / ((double) getMapSizeWithScaleFactor(d2, i));
    }

    public static double calculateGroundResolution(double d, long j) {
        return (Math.cos(d * 0.017453292519943295d) * 4.0075016686E7d) / ((double) j);
    }

    public static LatLong fromPixelsWithScaleFactor(double d, double d2, double d3, int i) {
        return new LatLong(pixelYToLatitudeWithScaleFactor(d2, d3, i), pixelXToLongitudeWithScaleFactor(d, d3, i));
    }

    public static LatLong fromPixels(double d, double d2, long j) {
        return new LatLong(pixelYToLatitude(d2, j), pixelXToLongitude(d, j));
    }

    public static long getMapSizeWithScaleFactor(double d, int i) {
        if (d >= 1.0d) {
            return (long) (((double) i) * Math.pow(2.0d, scaleFactorToZoomLevel(d)));
        }
        throw new IllegalArgumentException("scale factor must not < 1 " + d);
    }

    public static long getMapSize(byte b, int i) {
        if (b >= 0) {
            return ((long) i) << b;
        }
        throw new IllegalArgumentException("zoom level must not be negative: " + b);
    }

    public static Point getPixelWithScaleFactor(LatLong latLong, double d, int i) {
        return new Point(longitudeToPixelXWithScaleFactor(latLong.longitude, d, i), latitudeToPixelYWithScaleFactor(latLong.latitude, d, i));
    }

    public static Point getPixel(LatLong latLong, long j) {
        return new Point(longitudeToPixelX(latLong.longitude, j), latitudeToPixelY(latLong.latitude, j));
    }

    public static Point getPixelAbsolute(LatLong latLong, long j) {
        return getPixelRelative(latLong, j, 0.0d, 0.0d);
    }

    public static Point getPixelRelative(LatLong latLong, long j, double d, double d2) {
        return new Point(longitudeToPixelX(latLong.longitude, j) - d, latitudeToPixelY(latLong.latitude, j) - d2);
    }

    public static Point getPixelRelative(LatLong latLong, long j, Point point) {
        return getPixelRelative(latLong, j, point.f381x, point.f382y);
    }

    public static Point getPixelRelativeToTile(LatLong latLong, Tile tile) {
        return getPixelRelative(latLong, tile.mapSize, tile.getOrigin());
    }

    public static double latitudeToPixelYWithScaleFactor(double d, double d2, int i) {
        double sin = Math.sin(d * 0.017453292519943295d);
        long mapSizeWithScaleFactor = getMapSizeWithScaleFactor(d2, i);
        double log = 0.5d - (Math.log((sin + 1.0d) / (1.0d - sin)) / 12.566370614359172d);
        double d3 = (double) mapSizeWithScaleFactor;
        return Math.min(Math.max(0.0d, log * d3), d3);
    }

    public static double latitudeToPixelY(double d, byte b, int i) {
        double sin = Math.sin(d * 0.017453292519943295d);
        long mapSize = getMapSize(b, i);
        double log = 0.5d - (Math.log((sin + 1.0d) / (1.0d - sin)) / 12.566370614359172d);
        double d2 = (double) mapSize;
        return Math.min(Math.max(0.0d, log * d2), d2);
    }

    public static double latitudeToPixelY(double d, long j) {
        double sin = Math.sin(d * 0.017453292519943295d);
        double log = 0.5d - (Math.log((sin + 1.0d) / (1.0d - sin)) / 12.566370614359172d);
        double d2 = (double) j;
        return Math.min(Math.max(0.0d, log * d2), d2);
    }

    public static int latitudeToTileYWithScaleFactor(double d, double d2) {
        return pixelYToTileYWithScaleFactor(latitudeToPixelYWithScaleFactor(d, d2, 256), d2, 256);
    }

    public static int latitudeToTileY(double d, byte b) {
        return pixelYToTileY(latitudeToPixelY(d, b, 256), b, 256);
    }

    public static double longitudeToPixelXWithScaleFactor(double d, double d2, int i) {
        return ((d + 180.0d) / 360.0d) * ((double) getMapSizeWithScaleFactor(d2, i));
    }

    public static double longitudeToPixelX(double d, byte b, int i) {
        return ((d + 180.0d) / 360.0d) * ((double) getMapSize(b, i));
    }

    public static int longitudeToTileXWithScaleFactor(double d, double d2) {
        return pixelXToTileXWithScaleFactor(longitudeToPixelXWithScaleFactor(d, d2, 256), d2, 256);
    }

    public static int longitudeToTileX(double d, byte b) {
        return pixelXToTileX(longitudeToPixelX(d, b, 256), b, 256);
    }

    public static double metersToPixelsWithScaleFactor(float f, double d, double d2, int i) {
        return ((double) f) / calculateGroundResolutionWithScaleFactor(d, d2, i);
    }

    public static double metersToPixels(float f, double d, long j) {
        return ((double) f) / calculateGroundResolution(d, j);
    }

    public static double pixelXToLongitudeWithScaleFactor(double d, double d2, int i) {
        long mapSizeWithScaleFactor = getMapSizeWithScaleFactor(d2, i);
        if (d >= 0.0d) {
            double d3 = (double) mapSizeWithScaleFactor;
            if (d <= d3) {
                return ((d / d3) - 0.5d) * 360.0d;
            }
        }
        throw new IllegalArgumentException("invalid pixelX coordinate at scale " + d2 + ": " + d);
    }

    public static double pixelXToLongitude(double d, long j) {
        if (d >= 0.0d) {
            double d2 = (double) j;
            if (d <= d2) {
                return ((d / d2) - 0.5d) * 360.0d;
            }
        }
        throw new IllegalArgumentException("invalid pixelX coordinate " + j + ": " + d);
    }

    public static int pixelXToTileXWithScaleFactor(double d, double d2, int i) {
        return (int) Math.min(Math.max(d / ((double) i), 0.0d), d2 - 1.0d);
    }

    public static int pixelXToTileX(double d, byte b, int i) {
        return (int) Math.min(Math.max(d / ((double) i), 0.0d), Math.pow(2.0d, (double) b) - 1.0d);
    }

    public static double pixelYToLatitudeWithScaleFactor(double d, double d2, int i) {
        long mapSizeWithScaleFactor = getMapSizeWithScaleFactor(d2, i);
        if (d >= 0.0d) {
            double d3 = (double) mapSizeWithScaleFactor;
            if (d <= d3) {
                return 90.0d - ((Math.atan(Math.exp((-(0.5d - (d / d3))) * 6.283185307179586d)) * 360.0d) / 3.141592653589793d);
            }
        }
        throw new IllegalArgumentException("invalid pixelY coordinate at scale " + d2 + ": " + d);
    }

    public static double pixelYToLatitude(double d, long j) {
        if (d >= 0.0d) {
            double d2 = (double) j;
            if (d <= d2) {
                return 90.0d - ((Math.atan(Math.exp((-(0.5d - (d / d2))) * 6.283185307179586d)) * 360.0d) / 3.141592653589793d);
            }
        }
        throw new IllegalArgumentException("invalid pixelY coordinate " + j + ": " + d);
    }

    public static int pixelYToTileYWithScaleFactor(double d, double d2, int i) {
        return (int) Math.min(Math.max(d / ((double) i), 0.0d), d2 - 1.0d);
    }

    public static int pixelYToTileY(double d, byte b, int i) {
        return (int) Math.min(Math.max(d / ((double) i), 0.0d), Math.pow(2.0d, (double) b) - 1.0d);
    }

    public static double scaleFactorToZoomLevel(double d) {
        return Math.log(d) / Math.log(2.0d);
    }

    public static double tileXToLongitudeWithScaleFactor(long j, double d) {
        return pixelXToLongitudeWithScaleFactor((double) (j * 256), d, 256);
    }

    public static double tileXToLongitude(long j, byte b) {
        return pixelXToLongitude((double) (j * 256), getMapSize(b, 256));
    }

    public static double tileYToLatitudeWithScaleFactor(long j, double d) {
        return pixelYToLatitudeWithScaleFactor((double) (j * 256), d, 256);
    }

    public static double tileYToLatitude(long j, byte b) {
        return pixelYToLatitude((double) (j * 256), getMapSize(b, 256));
    }

    public static double zoomLevelToScaleFactor(byte b) {
        return Math.pow(2.0d, (double) b);
    }

    private MercatorProjection() {
        throw new IllegalStateException();
    }
}
