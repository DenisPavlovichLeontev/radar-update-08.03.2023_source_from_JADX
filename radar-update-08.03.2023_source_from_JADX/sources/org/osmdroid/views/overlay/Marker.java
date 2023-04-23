package org.osmdroid.views.overlay;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.MotionEvent;
import androidx.core.view.ViewCompat;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.RectL;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapViewRepository;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class Marker extends OverlayWithIW {
    public static final float ANCHOR_BOTTOM = 1.0f;
    public static final float ANCHOR_CENTER = 0.5f;
    public static final float ANCHOR_LEFT = 0.0f;
    public static final float ANCHOR_RIGHT = 1.0f;
    public static final float ANCHOR_TOP = 0.0f;
    protected float mAlpha;
    protected float mAnchorU;
    protected float mAnchorV;
    protected float mBearing;
    private boolean mDisplayed;
    protected float mDragOffsetY;
    protected boolean mDraggable;
    protected boolean mFlat;
    protected float mIWAnchorU;
    protected float mIWAnchorV;
    protected Drawable mIcon;
    protected Drawable mImage;
    protected boolean mIsDragged;
    private MapViewRepository mMapViewRepository;
    protected OnMarkerClickListener mOnMarkerClickListener;
    protected OnMarkerDragListener mOnMarkerDragListener;
    private final Rect mOrientedMarkerRect;
    private Paint mPaint;
    protected boolean mPanToView;
    protected GeoPoint mPosition;
    protected Point mPositionPixels;
    private final Rect mRect;
    protected Resources mResources;
    protected int mTextLabelBackgroundColor;
    protected int mTextLabelFontSize;
    protected int mTextLabelForegroundColor;

    public interface OnMarkerClickListener {
        boolean onMarkerClick(Marker marker, MapView mapView);
    }

    public interface OnMarkerDragListener {
        void onMarkerDrag(Marker marker);

        void onMarkerDragEnd(Marker marker);

        void onMarkerDragStart(Marker marker);
    }

    @Deprecated
    public static void cleanDefaults() {
    }

    public Marker(MapView mapView) {
        this(mapView, mapView.getContext());
    }

    public Marker(MapView mapView, Context context) {
        this.mTextLabelBackgroundColor = -1;
        this.mTextLabelForegroundColor = ViewCompat.MEASURED_STATE_MASK;
        this.mTextLabelFontSize = 24;
        this.mRect = new Rect();
        this.mOrientedMarkerRect = new Rect();
        this.mMapViewRepository = mapView.getRepository();
        this.mResources = mapView.getContext().getResources();
        this.mBearing = 0.0f;
        this.mAlpha = 1.0f;
        this.mPosition = new GeoPoint(0.0d, 0.0d);
        this.mAnchorU = 0.5f;
        this.mAnchorV = 0.5f;
        this.mIWAnchorU = 0.5f;
        this.mIWAnchorV = 0.0f;
        this.mDraggable = false;
        this.mIsDragged = false;
        this.mPositionPixels = new Point();
        this.mPanToView = true;
        this.mDragOffsetY = 0.0f;
        this.mFlat = false;
        this.mOnMarkerClickListener = null;
        this.mOnMarkerDragListener = null;
        setDefaultIcon();
        setInfoWindow(this.mMapViewRepository.getDefaultMarkerInfoWindow());
    }

    public void setIcon(Drawable drawable) {
        if (drawable != null) {
            this.mIcon = drawable;
        } else {
            setDefaultIcon();
        }
    }

    public void setDefaultIcon() {
        this.mIcon = this.mMapViewRepository.getDefaultMarkerIcon();
        setAnchor(0.5f, 1.0f);
    }

    public void setTextIcon(String str) {
        Paint paint = new Paint();
        paint.setColor(this.mTextLabelBackgroundColor);
        Paint paint2 = new Paint();
        paint2.setTextSize((float) this.mTextLabelFontSize);
        paint2.setColor(this.mTextLabelForegroundColor);
        paint2.setAntiAlias(true);
        paint2.setTypeface(Typeface.DEFAULT_BOLD);
        paint2.setTextAlign(Paint.Align.LEFT);
        float f = (float) ((int) ((-paint2.ascent()) + 0.5f));
        Bitmap createBitmap = Bitmap.createBitmap((int) (paint2.measureText(str) + 0.5f), (int) (paint2.descent() + f + 0.5f), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawPaint(paint);
        canvas.drawText(str, 0.0f, f, paint2);
        this.mIcon = new BitmapDrawable(this.mResources, createBitmap);
        setAnchor(0.5f, 0.5f);
    }

    public Drawable getIcon() {
        return this.mIcon;
    }

    public GeoPoint getPosition() {
        return this.mPosition;
    }

    public void setPosition(GeoPoint geoPoint) {
        this.mPosition = geoPoint.clone();
        if (isInfoWindowShown()) {
            closeInfoWindow();
            showInfoWindow();
        }
        this.mBounds = new BoundingBox(geoPoint.getLatitude(), geoPoint.getLongitude(), geoPoint.getLatitude(), geoPoint.getLongitude());
    }

    public float getRotation() {
        return this.mBearing;
    }

    public void setRotation(float f) {
        this.mBearing = f;
    }

    public void setAnchor(float f, float f2) {
        this.mAnchorU = f;
        this.mAnchorV = f2;
    }

    public void setInfoWindowAnchor(float f, float f2) {
        this.mIWAnchorU = f;
        this.mIWAnchorV = f2;
    }

    public void setAlpha(float f) {
        this.mAlpha = f;
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public void setDraggable(boolean z) {
        this.mDraggable = z;
    }

    public boolean isDraggable() {
        return this.mDraggable;
    }

    public void setFlat(boolean z) {
        this.mFlat = z;
    }

    public boolean isFlat() {
        return this.mFlat;
    }

    public void remove(MapView mapView) {
        mapView.getOverlays().remove(this);
    }

    public void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        this.mOnMarkerClickListener = onMarkerClickListener;
    }

    public void setOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener) {
        this.mOnMarkerDragListener = onMarkerDragListener;
    }

    public void setImage(Drawable drawable) {
        this.mImage = drawable;
    }

    public Drawable getImage() {
        return this.mImage;
    }

    public void setDragOffset(float f) {
        this.mDragOffsetY = f;
    }

    public float getDragOffset() {
        return this.mDragOffsetY;
    }

    public void setInfoWindow(MarkerInfoWindow markerInfoWindow) {
        this.mInfoWindow = markerInfoWindow;
    }

    public void setPanToView(boolean z) {
        this.mPanToView = z;
    }

    public void showInfoWindow() {
        if (this.mInfoWindow != null) {
            int intrinsicWidth = this.mIcon.getIntrinsicWidth();
            int intrinsicHeight = this.mIcon.getIntrinsicHeight();
            int i = (int) (((float) intrinsicWidth) * (this.mIWAnchorU - this.mAnchorU));
            int i2 = (int) (((float) intrinsicHeight) * (this.mIWAnchorV - this.mAnchorV));
            float f = this.mBearing;
            if (f == 0.0f) {
                this.mInfoWindow.open(this, this.mPosition, i, i2);
                return;
            }
            double d = (((double) (-f)) * 3.141592653589793d) / 180.0d;
            double cos = Math.cos(d);
            double sin = Math.sin(d);
            long j = (long) i;
            long j2 = j;
            long j3 = (long) i2;
            long j4 = j;
            double d2 = cos;
            int rotatedX = (int) RectL.getRotatedX(j2, j3, 0, 0, d2, sin);
            this.mInfoWindow.open(this, this.mPosition, rotatedX, (int) RectL.getRotatedY(j4, j3, 0, 0, d2, sin));
        }
    }

    public boolean isInfoWindowShown() {
        if (!(this.mInfoWindow instanceof MarkerInfoWindow)) {
            return super.isInfoWindowOpen();
        }
        MarkerInfoWindow markerInfoWindow = (MarkerInfoWindow) this.mInfoWindow;
        return markerInfoWindow != null && markerInfoWindow.isOpen() && markerInfoWindow.getMarkerReference() == this;
    }

    public void draw(Canvas canvas, Projection projection) {
        if (this.mIcon != null && isEnabled()) {
            projection.toPixels(this.mPosition, this.mPositionPixels);
            drawAt(canvas, this.mPositionPixels.x, this.mPositionPixels.y, this.mFlat ? -this.mBearing : (-projection.getOrientation()) - this.mBearing);
            if (isInfoWindowShown()) {
                this.mInfoWindow.draw();
            }
        }
    }

    public void onDetach(MapView mapView) {
        BitmapPool.getInstance().asyncRecycle(this.mIcon);
        this.mIcon = null;
        BitmapPool.getInstance().asyncRecycle(this.mImage);
        this.mOnMarkerClickListener = null;
        this.mOnMarkerDragListener = null;
        this.mResources = null;
        setRelatedObject((Object) null);
        if (isInfoWindowShown()) {
            closeInfoWindow();
        }
        this.mMapViewRepository = null;
        setInfoWindow((MarkerInfoWindow) null);
        onDestroy();
        super.onDetach(mapView);
    }

    public boolean hitTest(MotionEvent motionEvent, MapView mapView) {
        return this.mIcon != null && this.mDisplayed && this.mOrientedMarkerRect.contains((int) motionEvent.getX(), (int) motionEvent.getY());
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent, MapView mapView) {
        boolean hitTest = hitTest(motionEvent, mapView);
        if (!hitTest) {
            return hitTest;
        }
        OnMarkerClickListener onMarkerClickListener = this.mOnMarkerClickListener;
        if (onMarkerClickListener == null) {
            return onMarkerClickDefault(this, mapView);
        }
        return onMarkerClickListener.onMarkerClick(this, mapView);
    }

    public void moveToEventPosition(MotionEvent motionEvent, MapView mapView) {
        setPosition((GeoPoint) mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) (motionEvent.getY() - TypedValue.applyDimension(5, this.mDragOffsetY, mapView.getContext().getResources().getDisplayMetrics()))));
        mapView.invalidate();
    }

    public boolean onLongPress(MotionEvent motionEvent, MapView mapView) {
        boolean hitTest = hitTest(motionEvent, mapView);
        if (hitTest && this.mDraggable) {
            this.mIsDragged = true;
            closeInfoWindow();
            OnMarkerDragListener onMarkerDragListener = this.mOnMarkerDragListener;
            if (onMarkerDragListener != null) {
                onMarkerDragListener.onMarkerDragStart(this);
            }
            moveToEventPosition(motionEvent, mapView);
        }
        return hitTest;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.mDraggable && this.mIsDragged) {
            if (motionEvent.getAction() == 1) {
                this.mIsDragged = false;
                OnMarkerDragListener onMarkerDragListener = this.mOnMarkerDragListener;
                if (onMarkerDragListener != null) {
                    onMarkerDragListener.onMarkerDragEnd(this);
                }
                return true;
            } else if (motionEvent.getAction() == 2) {
                moveToEventPosition(motionEvent, mapView);
                OnMarkerDragListener onMarkerDragListener2 = this.mOnMarkerDragListener;
                if (onMarkerDragListener2 != null) {
                    onMarkerDragListener2.onMarkerDrag(this);
                }
                return true;
            }
        }
        return false;
    }

    public void setVisible(boolean z) {
        if (z) {
            setAlpha(1.0f);
        } else {
            setAlpha(0.0f);
        }
    }

    /* access modifiers changed from: protected */
    public boolean onMarkerClickDefault(Marker marker, MapView mapView) {
        marker.showInfoWindow();
        if (!marker.mPanToView) {
            return true;
        }
        mapView.getController().animateTo(marker.getPosition());
        return true;
    }

    public int getTextLabelBackgroundColor() {
        return this.mTextLabelBackgroundColor;
    }

    public void setTextLabelBackgroundColor(int i) {
        this.mTextLabelBackgroundColor = i;
    }

    public int getTextLabelForegroundColor() {
        return this.mTextLabelForegroundColor;
    }

    public void setTextLabelForegroundColor(int i) {
        this.mTextLabelForegroundColor = i;
    }

    public int getTextLabelFontSize() {
        return this.mTextLabelFontSize;
    }

    public void setTextLabelFontSize(int i) {
        this.mTextLabelFontSize = i;
    }

    public boolean isDisplayed() {
        return this.mDisplayed;
    }

    /* access modifiers changed from: protected */
    public void drawAt(Canvas canvas, int i, int i2, float f) {
        int intrinsicWidth = this.mIcon.getIntrinsicWidth();
        int intrinsicHeight = this.mIcon.getIntrinsicHeight();
        int round = i - Math.round(((float) intrinsicWidth) * this.mAnchorU);
        int round2 = i2 - Math.round(((float) intrinsicHeight) * this.mAnchorV);
        this.mRect.set(round, round2, intrinsicWidth + round, intrinsicHeight + round2);
        RectL.getBounds(this.mRect, i, i2, (double) f, this.mOrientedMarkerRect);
        boolean intersects = Rect.intersects(this.mOrientedMarkerRect, canvas.getClipBounds());
        this.mDisplayed = intersects;
        if (intersects && this.mAlpha != 0.0f) {
            int i3 = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
            if (i3 != 0) {
                canvas.save();
                canvas.rotate(f, (float) i, (float) i2);
            }
            this.mIcon.setAlpha((int) (this.mAlpha * 255.0f));
            this.mIcon.setBounds(this.mRect);
            this.mIcon.draw(canvas);
            if (i3 != 0) {
                canvas.restore();
            }
        }
    }
}
