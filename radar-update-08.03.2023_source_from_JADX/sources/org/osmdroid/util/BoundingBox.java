package org.osmdroid.util;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.List;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

public class BoundingBox implements Parcelable, Serializable {
    public static final Parcelable.Creator<BoundingBox> CREATOR = new Parcelable.Creator<BoundingBox>() {
        public BoundingBox createFromParcel(Parcel parcel) {
            return BoundingBox.readFromParcel(parcel);
        }

        public BoundingBox[] newArray(int i) {
            return new BoundingBox[i];
        }
    };
    static final long serialVersionUID = 2;
    private double mLatNorth;
    private double mLatSouth;
    private double mLonEast;
    private double mLonWest;

    public int describeContents() {
        return 0;
    }

    public BoundingBox(double d, double d2, double d3, double d4) {
        set(d, d2, d3, d4);
    }

    public BoundingBox() {
    }

    public void set(double d, double d2, double d3, double d4) {
        this.mLatNorth = d;
        this.mLonEast = d2;
        this.mLatSouth = d3;
        this.mLonWest = d4;
        if (Configuration.getInstance().isEnforceTileSystemBounds()) {
            TileSystem tileSystem = MapView.getTileSystem();
            if (!tileSystem.isValidLatitude(d)) {
                throw new IllegalArgumentException("north must be in " + tileSystem.toStringLatitudeSpan());
            } else if (!tileSystem.isValidLatitude(d3)) {
                throw new IllegalArgumentException("south must be in " + tileSystem.toStringLatitudeSpan());
            } else if (!tileSystem.isValidLongitude(d4)) {
                throw new IllegalArgumentException("west must be in " + tileSystem.toStringLongitudeSpan());
            } else if (!tileSystem.isValidLongitude(d2)) {
                throw new IllegalArgumentException("east must be in " + tileSystem.toStringLongitudeSpan());
            }
        }
    }

    public BoundingBox clone() {
        return new BoundingBox(this.mLatNorth, this.mLonEast, this.mLatSouth, this.mLonWest);
    }

    public BoundingBox concat(BoundingBox boundingBox) {
        return new BoundingBox(Math.max(this.mLatNorth, boundingBox.getLatNorth()), Math.max(this.mLonEast, boundingBox.getLonEast()), Math.min(this.mLatSouth, boundingBox.getLatSouth()), Math.min(this.mLonWest, boundingBox.getLonWest()));
    }

    @Deprecated
    public GeoPoint getCenter() {
        return new GeoPoint((this.mLatNorth + this.mLatSouth) / 2.0d, (this.mLonEast + this.mLonWest) / 2.0d);
    }

    public GeoPoint getCenterWithDateLine() {
        return new GeoPoint(getCenterLatitude(), getCenterLongitude());
    }

    public double getDiagonalLengthInMeters() {
        return new GeoPoint(this.mLatNorth, this.mLonWest).distanceToAsDouble(new GeoPoint(this.mLatSouth, this.mLonEast));
    }

    public double getLatNorth() {
        return this.mLatNorth;
    }

    public double getLatSouth() {
        return this.mLatSouth;
    }

    public double getCenterLatitude() {
        return (this.mLatNorth + this.mLatSouth) / 2.0d;
    }

    public double getCenterLongitude() {
        return getCenterLongitude(this.mLonWest, this.mLonEast);
    }

    public static double getCenterLongitude(double d, double d2) {
        double d3 = (d2 + d) / 2.0d;
        if (d2 < d) {
            d3 += 180.0d;
        }
        return MapView.getTileSystem().cleanLongitude(d3);
    }

    public double getActualNorth() {
        return Math.max(this.mLatNorth, this.mLatSouth);
    }

    public double getActualSouth() {
        return Math.min(this.mLatNorth, this.mLatSouth);
    }

    public double getLonEast() {
        return this.mLonEast;
    }

    public double getLonWest() {
        return this.mLonWest;
    }

    public double getLatitudeSpan() {
        return Math.abs(this.mLatNorth - this.mLatSouth);
    }

    @Deprecated
    public double getLongitudeSpan() {
        return Math.abs(this.mLonEast - this.mLonWest);
    }

    public void setLatNorth(double d) {
        this.mLatNorth = d;
    }

    public void setLatSouth(double d) {
        this.mLatSouth = d;
    }

    public void setLonEast(double d) {
        this.mLonEast = d;
    }

    public void setLonWest(double d) {
        this.mLonWest = d;
    }

    public double getLongitudeSpanWithDateLine() {
        double d = this.mLonEast;
        double d2 = this.mLonWest;
        return d > d2 ? d - d2 : (d - d2) + 360.0d;
    }

    /* renamed from: getRelativePositionOfGeoPointInBoundingBoxWithLinearInterpolation */
    public PointF mo29187x94d7c798(double d, double d2, PointF pointF) {
        if (pointF == null) {
            pointF = new PointF();
        }
        pointF.set(1.0f - ((float) ((this.mLonEast - d2) / getLongitudeSpan())), (float) ((this.mLatNorth - d) / getLatitudeSpan()));
        return pointF;
    }

    /* renamed from: getRelativePositionOfGeoPointInBoundingBoxWithExactGudermannInterpolation */
    public PointF mo29186x3b33f3a5(double d, double d2, PointF pointF) {
        if (pointF == null) {
            pointF = new PointF();
        }
        pointF.set(1.0f - ((float) ((this.mLonEast - d2) / getLongitudeSpan())), (float) ((MyMath.gudermannInverse(this.mLatNorth) - MyMath.gudermannInverse(d)) / (MyMath.gudermannInverse(this.mLatNorth) - MyMath.gudermannInverse(this.mLatSouth))));
        return pointF;
    }

    public GeoPoint getGeoPointOfRelativePositionWithLinearInterpolation(float f, float f2) {
        TileSystem tileSystem = MapView.getTileSystem();
        return new GeoPoint(tileSystem.cleanLatitude(this.mLatNorth - (getLatitudeSpan() * ((double) f2))), tileSystem.cleanLongitude(this.mLonWest + (getLongitudeSpan() * ((double) f))));
    }

    public GeoPoint getGeoPointOfRelativePositionWithExactGudermannInterpolation(float f, float f2) {
        TileSystem tileSystem = MapView.getTileSystem();
        double gudermannInverse = MyMath.gudermannInverse(this.mLatNorth);
        double gudermannInverse2 = MyMath.gudermannInverse(this.mLatSouth);
        return new GeoPoint(tileSystem.cleanLatitude(MyMath.gudermann(gudermannInverse2 + (((double) (1.0f - f2)) * (gudermannInverse - gudermannInverse2)))), tileSystem.cleanLongitude(this.mLonWest + (getLongitudeSpan() * ((double) f))));
    }

    public BoundingBox increaseByScale(float f) {
        float f2 = f;
        if (f2 > 0.0f) {
            TileSystem tileSystem = MapView.getTileSystem();
            double centerLatitude = getCenterLatitude();
            double d = (double) f2;
            double latitudeSpan = (getLatitudeSpan() / 2.0d) * d;
            double cleanLatitude = tileSystem.cleanLatitude(centerLatitude + latitudeSpan);
            double cleanLatitude2 = tileSystem.cleanLatitude(centerLatitude - latitudeSpan);
            double centerLongitude = getCenterLongitude();
            double longitudeSpanWithDateLine = (getLongitudeSpanWithDateLine() / 2.0d) * d;
            return new BoundingBox(cleanLatitude, tileSystem.cleanLongitude(centerLongitude + longitudeSpanWithDateLine), cleanLatitude2, tileSystem.cleanLongitude(centerLongitude - longitudeSpanWithDateLine));
        }
        throw new IllegalArgumentException("pBoundingboxPaddingRelativeScale must be positive");
    }

    public String toString() {
        return new StringBuffer().append("N:").append(this.mLatNorth).append("; E:").append(this.mLonEast).append("; S:").append(this.mLatSouth).append("; W:").append(this.mLonWest).toString();
    }

    public GeoPoint bringToBoundingBox(double d, double d2) {
        return new GeoPoint(Math.max(this.mLatSouth, Math.min(this.mLatNorth, d)), Math.max(this.mLonWest, Math.min(this.mLonEast, d2)));
    }

    public static BoundingBox fromGeoPoints(List<? extends IGeoPoint> list) {
        double d = -1.7976931348623157E308d;
        double d2 = -1.7976931348623157E308d;
        double d3 = Double.MAX_VALUE;
        double d4 = Double.MAX_VALUE;
        for (IGeoPoint iGeoPoint : list) {
            double latitude = iGeoPoint.getLatitude();
            double longitude = iGeoPoint.getLongitude();
            d3 = Math.min(d3, latitude);
            d4 = Math.min(d4, longitude);
            d = Math.max(d, latitude);
            d2 = Math.max(d2, longitude);
        }
        return new BoundingBox(d, d2, d3, d4);
    }

    public boolean contains(IGeoPoint iGeoPoint) {
        return contains(iGeoPoint.getLatitude(), iGeoPoint.getLongitude());
    }

    public boolean contains(double d, double d2) {
        double d3 = this.mLatNorth;
        double d4 = this.mLatSouth;
        boolean z = d3 < d4 || (d < d3 && d > d4);
        double d5 = this.mLonEast;
        double d6 = this.mLonWest;
        boolean z2 = d5 >= d6 ? !(d2 >= d5 || d2 <= d6) : !(d2 > d5 || d2 < d6);
        if (!z || !z2) {
            return false;
        }
        return true;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.mLatNorth);
        parcel.writeDouble(this.mLonEast);
        parcel.writeDouble(this.mLatSouth);
        parcel.writeDouble(this.mLonWest);
    }

    /* access modifiers changed from: private */
    public static BoundingBox readFromParcel(Parcel parcel) {
        return new BoundingBox(parcel.readDouble(), parcel.readDouble(), parcel.readDouble(), parcel.readDouble());
    }

    @Deprecated
    public int getLatitudeSpanE6() {
        return (int) (getLatitudeSpan() * 1000000.0d);
    }

    @Deprecated
    public int getLongitudeSpanE6() {
        return (int) (getLongitudeSpan() * 1000000.0d);
    }

    public boolean overlaps(BoundingBox boundingBox, double d) {
        BoundingBox boundingBox2 = boundingBox;
        if (d < 3.0d) {
            return true;
        }
        double d2 = boundingBox2.mLatSouth;
        double d3 = this.mLatNorth;
        boolean z = d2 <= d3 && d2 >= this.mLatSouth;
        double d4 = this.mLonWest;
        double d5 = boundingBox2.mLonWest;
        boolean z2 = d4 >= d5 && d4 <= boundingBox2.mLonEast;
        double d6 = this.mLonEast;
        double d7 = d2;
        if (d6 >= d5 && d4 <= boundingBox2.mLonEast) {
            z2 = true;
        }
        if (d4 <= d5 && d6 >= boundingBox2.mLonEast && d3 >= boundingBox2.mLatNorth && this.mLatSouth <= d7) {
            return true;
        }
        if (d3 >= d7 && d3 <= this.mLatSouth) {
            z = true;
        }
        double d8 = this.mLatSouth;
        if (d8 >= d7 && d8 <= d8) {
            z = true;
        }
        if (d4 > d6) {
            double d9 = boundingBox2.mLonEast;
            if (d6 <= d9 && d5 >= d4) {
                z2 = true;
            }
            if (d4 >= d9 && d6 <= d9) {
                z2 = (d9 <= d6 || d5 <= d6) ? d9 >= d4 || d5 >= d4 : false;
            }
            if (d4 >= d9 && d6 >= d9) {
                z2 = true;
            }
        }
        if (!z || !z2) {
            return false;
        }
        return true;
    }

    public static BoundingBox fromGeoPointsSafe(List<GeoPoint> list) {
        try {
            return fromGeoPoints(list);
        } catch (IllegalArgumentException unused) {
            TileSystem tileSystem = MapView.getTileSystem();
            return new BoundingBox(tileSystem.getMaxLatitude(), tileSystem.getMaxLongitude(), tileSystem.getMinLatitude(), tileSystem.getMinLongitude());
        }
    }
}
