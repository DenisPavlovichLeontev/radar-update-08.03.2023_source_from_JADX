package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.util.concurrent.atomic.AtomicInteger;
import org.osmdroid.api.IMapView;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.util.constants.OverlayConstants;

public abstract class Overlay implements OverlayConstants {
    protected static final float SHADOW_X_SKEW = -0.9f;
    protected static final float SHADOW_Y_SCALE = 0.5f;
    private static final Rect mRect = new Rect();
    private static AtomicInteger sOrdinal = new AtomicInteger();
    protected BoundingBox mBounds;
    private boolean mEnabled = true;
    private final TileSystem tileSystem;

    public interface Snappable {
        boolean onSnapToItem(int i, int i2, Point point, IMapView iMapView);
    }

    public void draw(Canvas canvas, Projection projection) {
    }

    public void onDetach(MapView mapView) {
    }

    public boolean onDoubleTap(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    public boolean onDoubleTapEvent(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    public boolean onDown(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, MapView mapView) {
        return false;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent, MapView mapView) {
        return false;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent, MapView mapView) {
        return false;
    }

    public boolean onLongPress(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, MapView mapView) {
        return false;
    }

    public void onShowPress(MotionEvent motionEvent, MapView mapView) {
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    public boolean onSingleTapUp(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    @Deprecated
    public Overlay(Context context) {
        TileSystem tileSystem2 = MapView.getTileSystem();
        this.tileSystem = tileSystem2;
        this.mBounds = new BoundingBox(tileSystem2.getMaxLatitude(), tileSystem2.getMaxLongitude(), tileSystem2.getMinLatitude(), tileSystem2.getMinLongitude());
    }

    public Overlay() {
        TileSystem tileSystem2 = MapView.getTileSystem();
        this.tileSystem = tileSystem2;
        this.mBounds = new BoundingBox(tileSystem2.getMaxLatitude(), tileSystem2.getMaxLongitude(), tileSystem2.getMinLatitude(), tileSystem2.getMinLongitude());
    }

    public BoundingBox getBounds() {
        return this.mBounds;
    }

    public void setEnabled(boolean z) {
        this.mEnabled = z;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    protected static final int getSafeMenuId() {
        return sOrdinal.getAndIncrement();
    }

    protected static final int getSafeMenuIdSequence(int i) {
        return sOrdinal.getAndAdd(i);
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
        if (!z) {
            draw(canvas, mapView.getProjection());
        }
    }

    protected static synchronized void drawAt(Canvas canvas, Drawable drawable, int i, int i2, boolean z, float f) {
        synchronized (Overlay.class) {
            canvas.save();
            canvas.rotate(-f, (float) i, (float) i2);
            Rect rect = mRect;
            drawable.copyBounds(rect);
            drawable.setBounds(rect.left + i, rect.top + i2, rect.right + i, rect.bottom + i2);
            drawable.draw(canvas);
            drawable.setBounds(rect);
            canvas.restore();
        }
    }
}
