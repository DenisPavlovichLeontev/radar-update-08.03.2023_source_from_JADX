package org.osmdroid.views.overlay.simplefastpoint;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import org.osmdroid.util.GeoPoint;

public class LabelledGeoPoint extends GeoPoint {
    public static final Parcelable.Creator<LabelledGeoPoint> CREATOR = new Parcelable.Creator<LabelledGeoPoint>() {
        public LabelledGeoPoint createFromParcel(Parcel parcel) {
            return new LabelledGeoPoint(parcel);
        }

        public LabelledGeoPoint[] newArray(int i) {
            return new LabelledGeoPoint[i];
        }
    };
    String mLabel;

    public LabelledGeoPoint(double d, double d2) {
        super(d, d2);
    }

    public LabelledGeoPoint(double d, double d2, double d3) {
        super(d, d2, d3);
    }

    public LabelledGeoPoint(double d, double d2, double d3, String str) {
        super(d, d2, d3);
        this.mLabel = str;
    }

    public LabelledGeoPoint(Location location) {
        super(location);
    }

    public LabelledGeoPoint(GeoPoint geoPoint) {
        super(geoPoint);
    }

    public LabelledGeoPoint(double d, double d2, String str) {
        super(d, d2);
        this.mLabel = str;
    }

    public LabelledGeoPoint(LabelledGeoPoint labelledGeoPoint) {
        this(labelledGeoPoint.getLatitude(), labelledGeoPoint.getLongitude(), labelledGeoPoint.getAltitude(), labelledGeoPoint.getLabel());
    }

    public String getLabel() {
        return this.mLabel;
    }

    public void setLabel(String str) {
        this.mLabel = str;
    }

    public LabelledGeoPoint clone() {
        return new LabelledGeoPoint(getLatitude(), getLongitude(), getAltitude(), this.mLabel);
    }

    private LabelledGeoPoint(Parcel parcel) {
        super(parcel.readDouble(), parcel.readDouble(), parcel.readDouble());
        setLabel(parcel.readString());
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mLabel);
    }
}
