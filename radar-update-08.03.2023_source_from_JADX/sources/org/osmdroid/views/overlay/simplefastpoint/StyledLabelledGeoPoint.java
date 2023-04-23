package org.osmdroid.views.overlay.simplefastpoint;

import android.graphics.Paint;
import android.location.Location;
import org.osmdroid.util.GeoPoint;

public class StyledLabelledGeoPoint extends LabelledGeoPoint {
    Paint mPointStyle;
    Paint mTextStyle;

    public StyledLabelledGeoPoint(double d, double d2) {
        super(d, d2);
    }

    public StyledLabelledGeoPoint(double d, double d2, double d3) {
        super(d, d2, d3);
    }

    public StyledLabelledGeoPoint(double d, double d2, double d3, String str) {
        super(d, d2, d3, str);
    }

    public StyledLabelledGeoPoint(Location location) {
        super(location);
    }

    public StyledLabelledGeoPoint(GeoPoint geoPoint) {
        super(geoPoint);
    }

    public StyledLabelledGeoPoint(double d, double d2, String str) {
        super(d, d2, str);
    }

    public StyledLabelledGeoPoint(double d, double d2, String str, Paint paint, Paint paint2) {
        super(d, d2, str);
        this.mPointStyle = paint;
        this.mTextStyle = paint2;
    }

    public StyledLabelledGeoPoint(double d, double d2, double d3, String str, Paint paint, Paint paint2) {
        super(d, d2, d3, str);
        this.mPointStyle = paint;
        this.mTextStyle = paint2;
    }

    public StyledLabelledGeoPoint(LabelledGeoPoint labelledGeoPoint) {
        super(labelledGeoPoint);
    }

    public Paint getPointStyle() {
        return this.mPointStyle;
    }

    public void setPointStyle(Paint paint) {
        this.mPointStyle = paint;
    }

    public Paint getTextStyle() {
        return this.mTextStyle;
    }

    public void setTextStyle(Paint paint) {
        this.mTextStyle = paint;
    }

    public StyledLabelledGeoPoint clone() {
        return new StyledLabelledGeoPoint(getLatitude(), getLongitude(), getAltitude(), this.mLabel, this.mPointStyle, this.mTextStyle);
    }
}
