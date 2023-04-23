package org.osmdroid.util;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.constants.GeoConstants;
import org.osmdroid.views.util.constants.MathConstants;

public class GeoPoint implements IGeoPoint, MathConstants, GeoConstants, Parcelable, Serializable, Cloneable {
    public static final Parcelable.Creator<GeoPoint> CREATOR = new Parcelable.Creator<GeoPoint>() {
        public GeoPoint createFromParcel(Parcel parcel) {
            return new GeoPoint(parcel);
        }

        public GeoPoint[] newArray(int i) {
            return new GeoPoint[i];
        }
    };
    static final long serialVersionUID = 1;
    private double mAltitude;
    private double mLatitude;
    private double mLongitude;

    public int describeContents() {
        return 0;
    }

    @Deprecated
    public GeoPoint(int i, int i2) {
        this.mLatitude = ((double) i) / 1000000.0d;
        this.mLongitude = ((double) i2) / 1000000.0d;
    }

    @Deprecated
    public GeoPoint(int i, int i2, int i3) {
        this.mLatitude = ((double) i) / 1000000.0d;
        this.mLongitude = ((double) i2) / 1000000.0d;
        this.mAltitude = (double) i3;
    }

    public GeoPoint(double d, double d2) {
        this.mLatitude = d;
        this.mLongitude = d2;
    }

    public GeoPoint(double d, double d2, double d3) {
        this.mLatitude = d;
        this.mLongitude = d2;
        this.mAltitude = d3;
    }

    public GeoPoint(Location location) {
        this(location.getLatitude(), location.getLongitude(), location.getAltitude());
    }

    public GeoPoint(GeoPoint geoPoint) {
        this.mLatitude = geoPoint.mLatitude;
        this.mLongitude = geoPoint.mLongitude;
        this.mAltitude = geoPoint.mAltitude;
    }

    public GeoPoint(IGeoPoint iGeoPoint) {
        this.mLatitude = iGeoPoint.getLatitude();
        this.mLongitude = iGeoPoint.getLongitude();
    }

    public static GeoPoint fromDoubleString(String str, char c) {
        int indexOf = str.indexOf(c);
        int i = indexOf + 1;
        int indexOf2 = str.indexOf(c, i);
        if (indexOf2 == -1) {
            return new GeoPoint(Double.parseDouble(str.substring(0, indexOf)), Double.parseDouble(str.substring(i, str.length())));
        }
        return new GeoPoint(Double.parseDouble(str.substring(0, indexOf)), Double.parseDouble(str.substring(i, indexOf2)), Double.parseDouble(str.substring(indexOf2 + 1, str.length())));
    }

    public static GeoPoint fromInvertedDoubleString(String str, char c) {
        int indexOf = str.indexOf(c);
        int i = indexOf + 1;
        int indexOf2 = str.indexOf(c, i);
        if (indexOf2 == -1) {
            return new GeoPoint(Double.parseDouble(str.substring(i, str.length())), Double.parseDouble(str.substring(0, indexOf)));
        }
        return new GeoPoint(Double.parseDouble(str.substring(i, indexOf2)), Double.parseDouble(str.substring(0, indexOf)), Double.parseDouble(str.substring(indexOf2 + 1, str.length())));
    }

    @Deprecated
    public static GeoPoint fromIntString(String str) {
        int indexOf = str.indexOf(44);
        int i = indexOf + 1;
        int indexOf2 = str.indexOf(44, i);
        if (indexOf2 == -1) {
            return new GeoPoint(Integer.parseInt(str.substring(0, indexOf)), Integer.parseInt(str.substring(i, str.length())));
        }
        return new GeoPoint(Integer.parseInt(str.substring(0, indexOf)), Integer.parseInt(str.substring(i, indexOf2)), Integer.parseInt(str.substring(indexOf2 + 1, str.length())));
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getAltitude() {
        return this.mAltitude;
    }

    public void setLatitude(double d) {
        this.mLatitude = d;
    }

    public void setLongitude(double d) {
        this.mLongitude = d;
    }

    public void setAltitude(double d) {
        this.mAltitude = d;
    }

    public void setCoords(double d, double d2) {
        this.mLatitude = d;
        this.mLongitude = d2;
    }

    public GeoPoint clone() {
        return new GeoPoint(this.mLatitude, this.mLongitude, this.mAltitude);
    }

    public String toIntString() {
        return ((int) (this.mLatitude * 1000000.0d)) + "," + ((int) (this.mLongitude * 1000000.0d)) + "," + ((int) this.mAltitude);
    }

    public String toString() {
        return this.mLatitude + "," + this.mLongitude + "," + this.mAltitude;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        GeoPoint geoPoint = (GeoPoint) obj;
        if (geoPoint.mLatitude == this.mLatitude && geoPoint.mLongitude == this.mLongitude && geoPoint.mAltitude == this.mAltitude) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((int) (this.mLatitude * 1.0E-6d)) * 17) + ((int) (this.mLongitude * 1.0E-6d))) * 37) + ((int) this.mAltitude);
    }

    private GeoPoint(Parcel parcel) {
        this.mLatitude = parcel.readDouble();
        this.mLongitude = parcel.readDouble();
        this.mAltitude = parcel.readDouble();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.mLatitude);
        parcel.writeDouble(this.mLongitude);
        parcel.writeDouble(this.mAltitude);
    }

    public double distanceToAsDouble(IGeoPoint iGeoPoint) {
        double latitude = getLatitude() * 0.017453292519943295d;
        double latitude2 = iGeoPoint.getLatitude() * 0.017453292519943295d;
        return Math.asin(Math.min(1.0d, Math.sqrt(Math.pow(Math.sin((latitude2 - latitude) / 2.0d), 2.0d) + (Math.cos(latitude) * Math.cos(latitude2) * Math.pow(Math.sin(((iGeoPoint.getLongitude() * 0.017453292519943295d) - (getLongitude() * 0.017453292519943295d)) / 2.0d), 2.0d))))) * 1.2756274E7d;
    }

    public double bearingTo(IGeoPoint iGeoPoint) {
        double radians = Math.toRadians(this.mLatitude);
        double radians2 = Math.toRadians(this.mLongitude);
        double radians3 = Math.toRadians(iGeoPoint.getLatitude());
        double radians4 = Math.toRadians(iGeoPoint.getLongitude()) - radians2;
        return (Math.toDegrees(Math.atan2(Math.sin(radians4) * Math.cos(radians3), (Math.cos(radians) * Math.sin(radians3)) - ((Math.sin(radians) * Math.cos(radians3)) * Math.cos(radians4)))) + 360.0d) % 360.0d;
    }

    public GeoPoint destinationPoint(double d, double d2) {
        double d3 = d / 6378137.0d;
        double d4 = d2 * 0.017453292519943295d;
        double latitude = getLatitude() * 0.017453292519943295d;
        double asin = Math.asin((Math.sin(latitude) * Math.cos(d3)) + (Math.cos(latitude) * Math.sin(d3) * Math.cos(d4)));
        return new GeoPoint(asin / 0.017453292519943295d, ((getLongitude() * 0.017453292519943295d) + Math.atan2((Math.sin(d4) * Math.sin(d3)) * Math.cos(latitude), Math.cos(d3) - (Math.sin(latitude) * Math.sin(asin)))) / 0.017453292519943295d);
    }

    public static GeoPoint fromCenterBetween(GeoPoint geoPoint, GeoPoint geoPoint2) {
        return new GeoPoint((geoPoint.getLatitude() + geoPoint2.getLatitude()) / 2.0d, (geoPoint.getLongitude() + geoPoint2.getLongitude()) / 2.0d);
    }

    public String toDoubleString() {
        return this.mLatitude + "," + this.mLongitude + "," + this.mAltitude;
    }

    public String toInvertedDoubleString() {
        return this.mLongitude + "," + this.mLatitude + "," + this.mAltitude;
    }

    @Deprecated
    public int getLatitudeE6() {
        return (int) (getLatitude() * 1000000.0d);
    }

    @Deprecated
    public int getLongitudeE6() {
        return (int) (getLongitude() * 1000000.0d);
    }
}
