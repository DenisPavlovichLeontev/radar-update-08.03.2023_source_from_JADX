package org.osmdroid.views.overlay;

import android.graphics.Paint;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class Polygon extends PolyOverlayWithIW {
    protected OnClickListener mOnClickListener;

    public interface OnClickListener {
        boolean onClick(Polygon polygon, MapView mapView, GeoPoint geoPoint);
    }

    public Polygon() {
        this((MapView) null);
    }

    public Polygon(MapView mapView) {
        super(mapView, true, true);
        this.mFillPaint = new Paint();
        this.mFillPaint.setColor(0);
        this.mFillPaint.setStyle(Paint.Style.FILL);
        this.mOutlinePaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mOutlinePaint.setStrokeWidth(10.0f);
        this.mOutlinePaint.setStyle(Paint.Style.STROKE);
        this.mOutlinePaint.setAntiAlias(true);
    }

    @Deprecated
    public int getFillColor() {
        return this.mFillPaint.getColor();
    }

    @Deprecated
    public int getStrokeColor() {
        return this.mOutlinePaint.getColor();
    }

    @Deprecated
    public float getStrokeWidth() {
        return this.mOutlinePaint.getStrokeWidth();
    }

    public Paint getFillPaint() {
        return super.getFillPaint();
    }

    @Deprecated
    public List<GeoPoint> getPoints() {
        return getActualPoints();
    }

    @Deprecated
    public void setFillColor(int i) {
        this.mFillPaint.setColor(i);
    }

    @Deprecated
    public void setStrokeColor(int i) {
        this.mOutlinePaint.setColor(i);
    }

    @Deprecated
    public void setStrokeWidth(float f) {
        this.mOutlinePaint.setStrokeWidth(f);
    }

    public void setHoles(List<? extends List<GeoPoint>> list) {
        this.mHoles = new ArrayList(list.size());
        for (List points : list) {
            LinearRing linearRing = new LinearRing(this.mPath);
            linearRing.setGeodesic(this.mOutline.isGeodesic());
            linearRing.setPoints(points);
            this.mHoles.add(linearRing);
        }
    }

    public List<List<GeoPoint>> getHoles() {
        ArrayList arrayList = new ArrayList(this.mHoles.size());
        for (LinearRing points : this.mHoles) {
            arrayList.add(points.getPoints());
        }
        return arrayList;
    }

    public static ArrayList<GeoPoint> pointsAsCircle(GeoPoint geoPoint, double d) {
        ArrayList<GeoPoint> arrayList = new ArrayList<>(60);
        for (int i = 0; i < 360; i += 6) {
            arrayList.add(geoPoint.destinationPoint(d, (double) i));
        }
        return arrayList;
    }

    public static ArrayList<IGeoPoint> pointsAsRect(BoundingBox boundingBox) {
        ArrayList<IGeoPoint> arrayList = new ArrayList<>(4);
        arrayList.add(new GeoPoint(boundingBox.getLatNorth(), boundingBox.getLonWest()));
        arrayList.add(new GeoPoint(boundingBox.getLatNorth(), boundingBox.getLonEast()));
        arrayList.add(new GeoPoint(boundingBox.getLatSouth(), boundingBox.getLonEast()));
        arrayList.add(new GeoPoint(boundingBox.getLatSouth(), boundingBox.getLonWest()));
        return arrayList;
    }

    public static ArrayList<IGeoPoint> pointsAsRect(GeoPoint geoPoint, double d, double d2) {
        ArrayList<IGeoPoint> arrayList = new ArrayList<>(4);
        GeoPoint destinationPoint = geoPoint.destinationPoint(d * 0.5d, 90.0d);
        GeoPoint destinationPoint2 = geoPoint.destinationPoint(d2 * 0.5d, 180.0d);
        double longitude = (geoPoint.getLongitude() * 2.0d) - destinationPoint.getLongitude();
        double latitude = (geoPoint.getLatitude() * 2.0d) - destinationPoint2.getLatitude();
        arrayList.add(new GeoPoint(destinationPoint2.getLatitude(), destinationPoint.getLongitude()));
        arrayList.add(new GeoPoint(destinationPoint2.getLatitude(), longitude));
        arrayList.add(new GeoPoint(latitude, longitude));
        arrayList.add(new GeoPoint(latitude, destinationPoint.getLongitude()));
        return arrayList;
    }

    public void onDetach(MapView mapView) {
        super.onDetach(mapView);
        this.mOnClickListener = null;
    }

    public boolean onClickDefault(Polygon polygon, MapView mapView, GeoPoint geoPoint) {
        polygon.setInfoWindowLocation(geoPoint);
        polygon.showInfoWindow();
        return true;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
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
