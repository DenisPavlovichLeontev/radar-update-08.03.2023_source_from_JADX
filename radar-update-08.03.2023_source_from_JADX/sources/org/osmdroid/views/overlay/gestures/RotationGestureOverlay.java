package org.osmdroid.views.overlay.gestures;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.IOverlayMenuProvider;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.gestures.RotationGestureDetector;

public class RotationGestureOverlay extends Overlay implements RotationGestureDetector.RotationListener, IOverlayMenuProvider {
    private static final int MENU_ENABLED = getSafeMenuId();
    private static final int MENU_ROTATE_CCW = getSafeMenuId();
    private static final int MENU_ROTATE_CW = getSafeMenuId();
    private static final boolean SHOW_ROTATE_MENU_ITEMS = false;
    float currentAngle;
    final long deltaTime;
    private MapView mMapView;
    private boolean mOptionsMenuEnabled;
    private final RotationGestureDetector mRotationDetector;
    long timeLastSet;

    @Deprecated
    public RotationGestureOverlay(Context context, MapView mapView) {
        this(mapView);
    }

    public RotationGestureOverlay(MapView mapView) {
        this.mOptionsMenuEnabled = true;
        this.timeLastSet = 0;
        this.deltaTime = 25;
        this.currentAngle = 0.0f;
        this.mMapView = mapView;
        this.mRotationDetector = new RotationGestureDetector(this);
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        this.mRotationDetector.onTouch(motionEvent);
        return super.onTouchEvent(motionEvent, mapView);
    }

    public void onRotate(float f) {
        this.currentAngle += f;
        if (System.currentTimeMillis() - 25 > this.timeLastSet) {
            this.timeLastSet = System.currentTimeMillis();
            MapView mapView = this.mMapView;
            mapView.setMapOrientation(mapView.getMapOrientation() + this.currentAngle);
        }
    }

    public void onDetach(MapView mapView) {
        this.mMapView = null;
    }

    public boolean isOptionsMenuEnabled() {
        return this.mOptionsMenuEnabled;
    }

    public boolean onCreateOptionsMenu(Menu menu, int i, MapView mapView) {
        menu.add(0, MENU_ENABLED + i, 0, "Enable rotation").setIcon(17301569);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem, int i, MapView mapView) {
        if (menuItem.getItemId() == MENU_ENABLED + i) {
            if (isEnabled()) {
                this.mMapView.setMapOrientation(0.0f);
                setEnabled(false);
            } else {
                setEnabled(true);
                return true;
            }
        } else if (menuItem.getItemId() == MENU_ROTATE_CCW + i) {
            MapView mapView2 = this.mMapView;
            mapView2.setMapOrientation(mapView2.getMapOrientation() - 10.0f);
        } else if (menuItem.getItemId() == MENU_ROTATE_CW + i) {
            MapView mapView3 = this.mMapView;
            mapView3.setMapOrientation(mapView3.getMapOrientation() + 10.0f);
        }
        return false;
    }

    public boolean onPrepareOptionsMenu(Menu menu, int i, MapView mapView) {
        menu.findItem(MENU_ENABLED + i).setTitle(isEnabled() ? "Disable rotation" : "Enable rotation");
        return false;
    }

    public void setOptionsMenuEnabled(boolean z) {
        this.mOptionsMenuEnabled = z;
    }

    public void setEnabled(boolean z) {
        this.mRotationDetector.setEnabled(z);
        super.setEnabled(z);
    }
}
