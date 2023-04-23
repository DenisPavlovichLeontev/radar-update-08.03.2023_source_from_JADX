package org.osmdroid.util;

import android.graphics.Point;
import android.graphics.Rect;
import com.j256.ormlite.stmt.query.SimpleComparison;

public abstract class TileSystem {
    @Deprecated
    public static final double EarthRadius = 6378137.0d;
    @Deprecated
    public static final double MaxLatitude = 85.05112877980659d;
    @Deprecated
    public static final double MaxLongitude = 180.0d;
    @Deprecated
    public static final double MinLatitude = -85.05112877980659d;
    @Deprecated
    public static final double MinLongitude = -180.0d;
    private static int mMaxZoomLevel = 29;
    private static int mTileSize = 256;
    public static final int primaryKeyMaxZoomLevel = 29;
    @Deprecated
    public static final int projectionZoomLevel = 30;

    private int clipTile(int i, int i2) {
        if (i < 0) {
            return 0;
        }
        int i3 = 1 << i2;
        return i >= i3 ? i3 - 1 : i;
    }

    public abstract double getLatitudeFromY01(double d);

    public abstract double getLongitudeFromX01(double d);

    public abstract double getMaxLatitude();

    public abstract double getMaxLongitude();

    public abstract double getMinLatitude();

    public abstract double getMinLongitude();

    public abstract double getX01FromLongitude(double d);

    public abstract double getY01FromLatitude(double d);

    public static void setTileSize(int i) {
        mMaxZoomLevel = Math.min(29, (63 - ((int) ((Math.log((double) i) / Math.log(2.0d)) + 0.5d))) - 1);
        mTileSize = i;
    }

    public static int getTileSize() {
        return mTileSize;
    }

    public static int getMaximumZoomLevel() {
        return mMaxZoomLevel;
    }

    public static double getTileSize(double d) {
        return MapSize(d - ((double) getInputTileZoomLevel(d)));
    }

    public static int getInputTileZoomLevel(double d) {
        return MyMath.floorToInt(d);
    }

    @Deprecated
    public static int MapSize(int i) {
        return (int) Math.round(MapSize((double) i));
    }

    public static double MapSize(double d) {
        return ((double) getTileSize()) * getFactor(d);
    }

    public static double getFactor(double d) {
        return Math.pow(2.0d, d);
    }

    public static double GroundResolution(double d, int i) {
        return GroundResolution(d, (double) i);
    }

    public static double GroundResolution(double d, double d2) {
        return GroundResolutionMapSize(wrap(d, -90.0d, 90.0d, 180.0d), MapSize(d2));
    }

    public static double GroundResolutionMapSize(double d, double d2) {
        return (((Math.cos((Clip(d, -90.0d, 90.0d) * 3.141592653589793d) / 180.0d) * 2.0d) * 3.141592653589793d) * 6378137.0d) / d2;
    }

    public static double MapScale(double d, int i, int i2) {
        return (GroundResolution(d, i) * ((double) i2)) / 0.0254d;
    }

    @Deprecated
    public Point LatLongToPixelXY(double d, double d2, int i, Point point) {
        if (point == null) {
            point = new Point();
        }
        double MapSize = (double) MapSize(i);
        point.x = truncateToInt(getMercatorXFromLongitude(d2, MapSize, true));
        point.y = truncateToInt(getMercatorYFromLatitude(d, MapSize, true));
        return point;
    }

    @Deprecated
    public PointL LatLongToPixelXY(double d, double d2, double d3, PointL pointL) {
        return LatLongToPixelXYMapSize(wrap(d, -90.0d, 90.0d, 180.0d), wrap(d2, -180.0d, 180.0d, 360.0d), MapSize(d3), pointL);
    }

    @Deprecated
    public PointL LatLongToPixelXYMapSize(double d, double d2, double d3, PointL pointL) {
        return getMercatorFromGeo(d, d2, d3, pointL, true);
    }

    @Deprecated
    public GeoPoint PixelXYToLatLong(int i, int i2, int i3, GeoPoint geoPoint) {
        return getGeoFromMercator((long) i, (long) i2, (double) MapSize(i3), geoPoint, true, true);
    }

    @Deprecated
    public GeoPoint PixelXYToLatLong(int i, int i2, double d, GeoPoint geoPoint) {
        return getGeoFromMercator((long) i, (long) i2, MapSize(d), geoPoint, true, true);
    }

    public GeoPoint PixelXYToLatLongWithoutWrap(int i, int i2, double d, GeoPoint geoPoint) {
        return PixelXYToLatLongMapSizeWithoutWrap(i, i2, MapSize(d), geoPoint);
    }

    public double getX01FromLongitude(double d, boolean z) {
        if (z) {
            d = Clip(d, getMinLongitude(), getMaxLongitude());
        }
        double x01FromLongitude = getX01FromLongitude(d);
        return z ? Clip(x01FromLongitude, 0.0d, 1.0d) : x01FromLongitude;
    }

    public double getY01FromLatitude(double d, boolean z) {
        if (z) {
            d = Clip(d, getMinLatitude(), getMaxLatitude());
        }
        double y01FromLatitude = getY01FromLatitude(d);
        return z ? Clip(y01FromLatitude, 0.0d, 1.0d) : y01FromLatitude;
    }

    @Deprecated
    public GeoPoint PixelXYToLatLongMapSize(int i, int i2, double d, GeoPoint geoPoint, boolean z, boolean z2) {
        return getGeoFromMercator((long) i, (long) i2, d, geoPoint, z, z2);
    }

    public GeoPoint PixelXYToLatLongMapSizeWithoutWrap(int i, int i2, double d, GeoPoint geoPoint) {
        if (geoPoint == null) {
            geoPoint = new GeoPoint(0.0d, 0.0d);
        }
        geoPoint.setLatitude(90.0d - ((Math.atan(Math.exp(((-(0.5d - (((double) i2) / d))) * 2.0d) * 3.141592653589793d)) * 360.0d) / 3.141592653589793d));
        geoPoint.setLongitude(((((double) i) / d) - 0.5d) * 360.0d);
        return geoPoint;
    }

    public static double Clip(double d, double d2, double d3) {
        return Math.min(Math.max(d, d2), d3);
    }

    @Deprecated
    public Point PixelXYToTileXY(int i, int i2, Point point) {
        return PixelXYToTileXY(i, i2, (double) getTileSize(), point);
    }

    @Deprecated
    public Point PixelXYToTileXY(int i, int i2, double d, Point point) {
        if (point == null) {
            point = new Point();
        }
        point.x = getTileFromMercator((long) i, d);
        point.y = getTileFromMercator((long) i2, d);
        return point;
    }

    @Deprecated
    public Rect PixelXYToTileXY(Rect rect, double d, Rect rect2) {
        if (rect2 == null) {
            rect2 = new Rect();
        }
        rect2.left = getTileFromMercator((long) rect.left, d);
        rect2.top = getTileFromMercator((long) rect.top, d);
        rect2.right = getTileFromMercator((long) rect.right, d);
        rect2.bottom = getTileFromMercator((long) rect.bottom, d);
        return rect2;
    }

    @Deprecated
    public Point TileXYToPixelXY(int i, int i2, Point point) {
        if (point == null) {
            point = new Point();
        }
        double tileSize = (double) getTileSize();
        point.x = truncateToInt(getMercatorFromTile(i, tileSize));
        point.y = truncateToInt(getMercatorFromTile(i2, tileSize));
        return point;
    }

    @Deprecated
    public PointL TileXYToPixelXY(int i, int i2, double d, PointL pointL) {
        if (pointL == null) {
            pointL = new PointL();
        }
        pointL.f559x = getMercatorFromTile(i, d);
        pointL.f560y = getMercatorFromTile(i2, d);
        return pointL;
    }

    public static String TileXYToQuadKey(int i, int i2, int i3) {
        char[] cArr = new char[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            char c = '0';
            int i5 = 1 << i4;
            if ((i & i5) != 0) {
                c = (char) 49;
            }
            if ((i5 & i2) != 0) {
                c = (char) (((char) (c + 1)) + 1);
            }
            cArr[(i3 - i4) - 1] = c;
        }
        return new String(cArr);
    }

    public static Point QuadKeyToTileXY(String str, Point point) {
        if (point == null) {
            point = new Point();
        }
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Invalid QuadKey: " + str);
        }
        int length = str.length();
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            int i4 = 1 << i3;
            switch (str.charAt((length - i3) - 1)) {
                case '0':
                    break;
                case '1':
                    i += i4;
                    continue;
                case '2':
                    break;
                case '3':
                    i += i4;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid QuadKey: " + str);
            }
            i2 += i4;
        }
        point.x = i;
        point.y = i2;
        return point;
    }

    public double getBoundingBoxZoom(BoundingBox boundingBox, int i, int i2) {
        double longitudeZoom = getLongitudeZoom(boundingBox.getLonEast(), boundingBox.getLonWest(), i);
        double latitudeZoom = getLatitudeZoom(boundingBox.getLatNorth(), boundingBox.getLatSouth(), i2);
        if (longitudeZoom == Double.MIN_VALUE) {
            return latitudeZoom;
        }
        if (latitudeZoom == Double.MIN_VALUE) {
            return longitudeZoom;
        }
        return Math.min(latitudeZoom, longitudeZoom);
    }

    public double getLongitudeZoom(double d, double d2, int i) {
        double x01FromLongitude = getX01FromLongitude(d, true) - getX01FromLongitude(d2, true);
        if (x01FromLongitude < 0.0d) {
            x01FromLongitude += 1.0d;
        }
        if (x01FromLongitude == 0.0d) {
            return Double.MIN_VALUE;
        }
        return Math.log((((double) i) / x01FromLongitude) / ((double) getTileSize())) / Math.log(2.0d);
    }

    public double getLatitudeZoom(double d, double d2, int i) {
        double y01FromLatitude = getY01FromLatitude(d2, true) - getY01FromLatitude(d, true);
        if (y01FromLatitude <= 0.0d) {
            return Double.MIN_VALUE;
        }
        return Math.log((((double) i) / y01FromLatitude) / ((double) getTileSize())) / Math.log(2.0d);
    }

    private static double wrap(double d, double d2, double d3, double d4) {
        if (d2 > d3) {
            throw new IllegalArgumentException("minValue must be smaller than maxValue: " + d2 + SimpleComparison.GREATER_THAN_OPERATION + d3);
        } else if (d4 <= (d3 - d2) + 1.0d) {
            while (d < d2) {
                d += d4;
            }
            while (d > d3) {
                d -= d4;
            }
            return d;
        } else {
            throw new IllegalArgumentException("interval must be equal or smaller than maxValue-minValue: min: " + d2 + " max:" + d3 + " int:" + d4);
        }
    }

    public long getMercatorYFromLatitude(double d, double d2, boolean z) {
        return getMercatorFromXY01(getY01FromLatitude(d, z), d2, z);
    }

    public long getMercatorXFromLongitude(double d, double d2, boolean z) {
        return getMercatorFromXY01(getX01FromLongitude(d, z), d2, z);
    }

    public long getMercatorFromXY01(double d, double d2, boolean z) {
        return ClipToLong(d * d2, d2, z);
    }

    public double getLatitudeFromY01(double d, boolean z) {
        if (z) {
            d = Clip(d, 0.0d, 1.0d);
        }
        double latitudeFromY01 = getLatitudeFromY01(d);
        return z ? Clip(latitudeFromY01, getMinLatitude(), getMaxLatitude()) : latitudeFromY01;
    }

    public double getLongitudeFromX01(double d, boolean z) {
        if (z) {
            d = Clip(d, 0.0d, 1.0d);
        }
        double longitudeFromX01 = getLongitudeFromX01(d);
        return z ? Clip(longitudeFromX01, getMinLongitude(), getMaxLongitude()) : longitudeFromX01;
    }

    public long getCleanMercator(long j, double d, boolean z) {
        return ClipToLong(z ? wrap((double) j, 0.0d, d, d) : (double) j, d, z);
    }

    public static long ClipToLong(double d, double d2, boolean z) {
        long floorToLong = MyMath.floorToLong(d);
        if (!z) {
            return floorToLong;
        }
        if (floorToLong <= 0) {
            return 0;
        }
        return ((double) floorToLong) >= d2 ? MyMath.floorToLong(d2 - 1.0d) : floorToLong;
    }

    @Deprecated
    public static long Clip(long j, long j2, long j3) {
        return Math.min(Math.max(j, j2), j3);
    }

    public static int truncateToInt(long j) {
        return (int) Math.max(Math.min(j, 2147483647L), -2147483648L);
    }

    public PointL getMercatorFromGeo(double d, double d2, double d3, PointL pointL, boolean z) {
        if (pointL == null) {
            pointL = new PointL();
        }
        double d4 = d3;
        boolean z2 = z;
        pointL.f559x = getMercatorXFromLongitude(d2, d4, z2);
        pointL.f560y = getMercatorYFromLatitude(d, d4, z2);
        return pointL;
    }

    public GeoPoint getGeoFromMercator(long j, long j2, double d, GeoPoint geoPoint, boolean z, boolean z2) {
        if (geoPoint == null) {
            geoPoint = new GeoPoint(0.0d, 0.0d);
        }
        double d2 = d;
        geoPoint.setLatitude(getLatitudeFromY01(getXY01FromMercator(j2, d2, z2), z2));
        geoPoint.setLongitude(getLongitudeFromX01(getXY01FromMercator(j, d2, z), z));
        return geoPoint;
    }

    public double getXY01FromMercator(long j, double d, boolean z) {
        double d2 = (double) j;
        return z ? Clip(d2 / d, 0.0d, 1.0d) : d2 / d;
    }

    public double getRandomLongitude(double d) {
        return (d * (getMaxLongitude() - getMinLongitude())) + getMinLongitude();
    }

    public double getRandomLatitude(double d, double d2) {
        return (d * (getMaxLatitude() - d2)) + d2;
    }

    public double getRandomLatitude(double d) {
        return getRandomLatitude(d, getMinLatitude());
    }

    public static int getTileFromMercator(long j, double d) {
        return MyMath.floorToInt(((double) j) / d);
    }

    public static Rect getTileFromMercator(RectL rectL, double d, Rect rect) {
        if (rect == null) {
            rect = new Rect();
        }
        rect.left = getTileFromMercator(rectL.left, d);
        rect.top = getTileFromMercator(rectL.top, d);
        rect.right = getTileFromMercator(rectL.right, d);
        rect.bottom = getTileFromMercator(rectL.bottom, d);
        return rect;
    }

    public static long getMercatorFromTile(int i, double d) {
        return Math.round(((double) i) * d);
    }

    public double cleanLongitude(double d) {
        while (d < -180.0d) {
            d += 360.0d;
        }
        while (d > 180.0d) {
            d -= 360.0d;
        }
        return Clip(d, getMinLongitude(), getMaxLongitude());
    }

    public double cleanLatitude(double d) {
        return Clip(d, getMinLatitude(), getMaxLatitude());
    }

    public boolean isValidLongitude(double d) {
        return d >= getMinLongitude() && d <= getMaxLongitude();
    }

    public boolean isValidLatitude(double d) {
        return d >= getMinLatitude() && d <= getMaxLatitude();
    }

    public String toStringLongitudeSpan() {
        return "[" + getMinLongitude() + "," + getMaxLongitude() + "]";
    }

    public String toStringLatitudeSpan() {
        return "[" + getMinLatitude() + "," + getMaxLatitude() + "]";
    }

    public int getTileXFromLongitude(double d, int i) {
        return clipTile((int) Math.floor(getX01FromLongitude(d) * ((double) (1 << i))), i);
    }

    public int getTileYFromLatitude(double d, int i) {
        return clipTile((int) Math.floor(getY01FromLatitude(d) * ((double) (1 << i))), i);
    }

    public double getLatitudeFromTileY(int i, int i2) {
        return getLatitudeFromY01(((double) clipTile(i, i2)) / ((double) (1 << i2)));
    }

    public double getLongitudeFromTileX(int i, int i2) {
        return getLongitudeFromX01(((double) clipTile(i, i2)) / ((double) (1 << i2)));
    }
}
