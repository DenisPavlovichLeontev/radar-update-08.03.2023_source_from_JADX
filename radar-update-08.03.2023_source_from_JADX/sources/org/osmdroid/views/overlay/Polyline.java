package org.osmdroid.views.overlay;

import android.graphics.Paint;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class Polyline extends PolyOverlayWithIW {
    protected OnClickListener mOnClickListener;

    public interface OnClickListener {
        boolean onClick(Polyline polyline, MapView mapView, GeoPoint geoPoint);
    }

    public Polyline() {
        this((MapView) null);
    }

    public Polyline(MapView mapView) {
        this(mapView, false);
    }

    public Polyline(MapView mapView, boolean z, boolean z2) {
        super(mapView, z, z2);
        this.mOutlinePaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mOutlinePaint.setStrokeWidth(10.0f);
        this.mOutlinePaint.setStyle(Paint.Style.STROKE);
        this.mOutlinePaint.setAntiAlias(true);
    }

    public Polyline(MapView mapView, boolean z) {
        this(mapView, z, false);
    }

    @Deprecated
    public ArrayList<GeoPoint> getPoints() {
        return new ArrayList<>(getActualPoints());
    }

    @Deprecated
    public int getColor() {
        return this.mOutlinePaint.getColor();
    }

    @Deprecated
    public float getWidth() {
        return this.mOutlinePaint.getStrokeWidth();
    }

    @Deprecated
    public Paint getPaint() {
        return getOutlinePaint();
    }

    @Deprecated
    public void setColor(int i) {
        this.mOutlinePaint.setColor(i);
    }

    @Deprecated
    public void setWidth(float f) {
        this.mOutlinePaint.setStrokeWidth(f);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public boolean onClickDefault(Polyline polyline, MapView mapView, GeoPoint geoPoint) {
        polyline.setInfoWindowLocation(geoPoint);
        polyline.showInfoWindow();
        return true;
    }

    public void onDetach(MapView mapView) {
        super.onDetach(mapView);
        this.mOnClickListener = null;
    }

    public double getDistance() {
        return this.mOutline.getDistance();
    }

    /* access modifiers changed from: protected */
    public boolean click(MapView mapView, GeoPoint geoPoint) {
        OnClickListener onClickListener = this.mOnClickListener;
        if (onClickListener == null) {
            return onClickDefault(this, mapView, geoPoint);
        }
        return onClickListener.onClick(this, mapView, geoPoint);
    }
}
