package org.mapsforge.core.model;

import java.io.Serializable;
import java.util.List;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.core.util.MercatorProjection;

public class BoundingBox implements Serializable {
    private static final long serialVersionUID = 1;
    public final double maxLatitude;
    public final double maxLongitude;
    public final double minLatitude;
    public final double minLongitude;

    public static BoundingBox fromString(String str) {
        double[] parseCoordinateString = LatLongUtils.parseCoordinateString(str, 4);
        return new BoundingBox(parseCoordinateString[0], parseCoordinateString[1], parseCoordinateString[2], parseCoordinateString[3]);
    }

    public BoundingBox(double d, double d2, double d3, double d4) {
        LatLongUtils.validateLatitude(d);
        LatLongUtils.validateLongitude(d2);
        LatLongUtils.validateLatitude(d3);
        LatLongUtils.validateLongitude(d4);
        if (d > d3) {
            throw new IllegalArgumentException("invalid latitude range: " + d + ' ' + d3);
        } else if (d2 <= d4) {
            this.minLatitude = d;
            this.minLongitude = d2;
            this.maxLatitude = d3;
            this.maxLongitude = d4;
        } else {
            throw new IllegalArgumentException("invalid longitude range: " + d2 + ' ' + d4);
        }
    }

    public BoundingBox(List<LatLong> list) {
        double d = Double.NEGATIVE_INFINITY;
        double d2 = Double.POSITIVE_INFINITY;
        double d3 = Double.POSITIVE_INFINITY;
        double d4 = Double.NEGATIVE_INFINITY;
        for (LatLong next : list) {
            double d5 = next.latitude;
            double d6 = next.longitude;
            d2 = Math.min(d2, d5);
            d3 = Math.min(d3, d6);
            d = Math.max(d, d5);
            d4 = Math.max(d4, d6);
        }
        this.minLatitude = d2;
        this.minLongitude = d3;
        this.maxLatitude = d;
        this.maxLongitude = d4;
    }

    public boolean contains(double d, double d2) {
        return this.minLatitude <= d && this.maxLatitude >= d && this.minLongitude <= d2 && this.maxLongitude >= d2;
    }

    public boolean contains(LatLong latLong) {
        return contains(latLong.latitude, latLong.longitude);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BoundingBox)) {
            return false;
        }
        BoundingBox boundingBox = (BoundingBox) obj;
        return Double.doubleToLongBits(this.maxLatitude) == Double.doubleToLongBits(boundingBox.maxLatitude) && Double.doubleToLongBits(this.maxLongitude) == Double.doubleToLongBits(boundingBox.maxLongitude) && Double.doubleToLongBits(this.minLatitude) == Double.doubleToLongBits(boundingBox.minLatitude) && Double.doubleToLongBits(this.minLongitude) == Double.doubleToLongBits(boundingBox.minLongitude);
    }

    public BoundingBox extendBoundingBox(BoundingBox boundingBox) {
        return new BoundingBox(Math.min(this.minLatitude, boundingBox.minLatitude), Math.min(this.minLongitude, boundingBox.minLongitude), Math.max(this.maxLatitude, boundingBox.maxLatitude), Math.max(this.maxLongitude, boundingBox.maxLongitude));
    }

    public BoundingBox extendCoordinates(double d, double d2) {
        double d3 = d;
        double d4 = d2;
        if (contains(d, d2)) {
            return this;
        }
        return new BoundingBox(Math.max(-85.05112877980659d, Math.min(this.minLatitude, d3)), Math.max(-180.0d, Math.min(this.minLongitude, d4)), Math.min(85.05112877980659d, Math.max(this.maxLatitude, d3)), Math.min(180.0d, Math.max(this.maxLongitude, d4)));
    }

    public BoundingBox extendCoordinates(LatLong latLong) {
        return extendCoordinates(latLong.latitude, latLong.longitude);
    }

    public BoundingBox extendDegrees(double d, double d2) {
        if (d == 0.0d && d2 == 0.0d) {
            return this;
        }
        if (d >= 0.0d && d2 >= 0.0d) {
            return new BoundingBox(Math.max(-85.05112877980659d, this.minLatitude - d), Math.max(-180.0d, this.minLongitude - d2), Math.min(85.05112877980659d, this.maxLatitude + d), Math.min(180.0d, this.maxLongitude + d2));
        }
        throw new IllegalArgumentException("BoundingBox extend operation does not accept negative values");
    }

    public BoundingBox extendMargin(float f) {
        float f2 = f;
        if (f2 == 1.0f) {
            return this;
        }
        if (f2 > 0.0f) {
            double d = (double) f2;
            double latitudeSpan = ((getLatitudeSpan() * d) - getLatitudeSpan()) * 0.5d;
            double longitudeSpan = ((getLongitudeSpan() * d) - getLongitudeSpan()) * 0.5d;
            return new BoundingBox(Math.max(-85.05112877980659d, this.minLatitude - latitudeSpan), Math.max(-180.0d, this.minLongitude - longitudeSpan), Math.min(85.05112877980659d, this.maxLatitude + latitudeSpan), Math.min(180.0d, this.maxLongitude + longitudeSpan));
        }
        throw new IllegalArgumentException("BoundingBox extend operation does not accept negative or zero values");
    }

    public BoundingBox extendMeters(int i) {
        int i2 = i;
        if (i2 == 0) {
            return this;
        }
        if (i2 >= 0) {
            double latitudeDistance = LatLongUtils.latitudeDistance(i);
            double longitudeDistance = LatLongUtils.longitudeDistance(i2, Math.max(Math.abs(this.minLatitude), Math.abs(this.maxLatitude)));
            return new BoundingBox(Math.max(-85.05112877980659d, this.minLatitude - latitudeDistance), Math.max(-180.0d, this.minLongitude - longitudeDistance), Math.min(85.05112877980659d, this.maxLatitude + latitudeDistance), Math.min(180.0d, this.maxLongitude + longitudeDistance));
        }
        throw new IllegalArgumentException("BoundingBox extend operation does not accept negative values");
    }

    public LatLong getCenterPoint() {
        return new LatLong(this.minLatitude + ((this.maxLatitude - this.minLatitude) / 2.0d), this.minLongitude + ((this.maxLongitude - this.minLongitude) / 2.0d));
    }

    public double getLatitudeSpan() {
        return this.maxLatitude - this.minLatitude;
    }

    public double getLongitudeSpan() {
        return this.maxLongitude - this.minLongitude;
    }

    public Rectangle getPositionRelativeToTile(Tile tile) {
        Point pixelRelativeToTile = MercatorProjection.getPixelRelativeToTile(new LatLong(this.maxLatitude, this.minLongitude), tile);
        Point pixelRelativeToTile2 = MercatorProjection.getPixelRelativeToTile(new LatLong(this.minLatitude, this.maxLongitude), tile);
        return new Rectangle(pixelRelativeToTile.f381x, pixelRelativeToTile.f382y, pixelRelativeToTile2.f381x, pixelRelativeToTile2.f382y);
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.maxLatitude);
        long doubleToLongBits2 = Double.doubleToLongBits(this.maxLongitude);
        int i = ((((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 31) * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
        long doubleToLongBits3 = Double.doubleToLongBits(this.minLatitude);
        int i2 = (i * 31) + ((int) (doubleToLongBits3 ^ (doubleToLongBits3 >>> 32)));
        long doubleToLongBits4 = Double.doubleToLongBits(this.minLongitude);
        return (i2 * 31) + ((int) ((doubleToLongBits4 >>> 32) ^ doubleToLongBits4));
    }

    public boolean intersects(BoundingBox boundingBox) {
        if (this == boundingBox) {
            return true;
        }
        return this.maxLatitude >= boundingBox.minLatitude && this.maxLongitude >= boundingBox.minLongitude && this.minLatitude <= boundingBox.maxLatitude && this.minLongitude <= boundingBox.maxLongitude;
    }

    public boolean intersectsArea(LatLong[][] latLongArr) {
        LatLong[][] latLongArr2 = latLongArr;
        int i = 0;
        if (latLongArr2.length == 0 || latLongArr2[0].length == 0) {
            return false;
        }
        for (LatLong[] latLongArr3 : latLongArr2) {
            for (LatLong contains : latLongArr2[r4]) {
                if (contains(contains)) {
                    return true;
                }
            }
        }
        double d = latLongArr2[0][0].latitude;
        double d2 = latLongArr2[0][0].longitude;
        double d3 = latLongArr2[0][0].latitude;
        double d4 = latLongArr2[0][0].longitude;
        int length = latLongArr2.length;
        double d5 = d;
        double d6 = d2;
        double d7 = d3;
        double d8 = d4;
        int i2 = 0;
        while (i2 < length) {
            LatLong[] latLongArr4 = latLongArr2[i2];
            int length2 = latLongArr4.length;
            double d9 = d6;
            double d10 = d7;
            double d11 = d8;
            int i3 = i;
            while (i3 < length2) {
                LatLong latLong = latLongArr4[i3];
                d5 = Math.min(d5, latLong.latitude);
                d10 = Math.max(d10, latLong.latitude);
                d9 = Math.min(d9, latLong.longitude);
                d11 = Math.max(d11, latLong.longitude);
                i3++;
                LatLong[][] latLongArr5 = latLongArr;
                length = length;
            }
            int i4 = length;
            i2++;
            latLongArr2 = latLongArr;
            d6 = d9;
            d7 = d10;
            d8 = d11;
            i = 0;
        }
        return intersects(new BoundingBox(d5, d6, d7, d8));
    }

    public String toString() {
        return "minLatitude=" + this.minLatitude + ", minLongitude=" + this.minLongitude + ", maxLatitude=" + this.maxLatitude + ", maxLongitude=" + this.maxLongitude;
    }
}
