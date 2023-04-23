package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import java.util.List;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

public class FolderOverlay extends Overlay {
    protected String mDescription;
    protected String mName;
    protected OverlayManager mOverlayManager;

    @Deprecated
    public FolderOverlay(Context context) {
        this();
    }

    public FolderOverlay() {
        this.mOverlayManager = new DefaultOverlayManager((TilesOverlay) null);
        this.mName = "";
        this.mDescription = "";
    }

    public void setName(String str) {
        this.mName = str;
    }

    public String getName() {
        return this.mName;
    }

    public void setDescription(String str) {
        this.mDescription = str;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public List<Overlay> getItems() {
        return this.mOverlayManager;
    }

    public boolean add(Overlay overlay) {
        boolean add = this.mOverlayManager.add(overlay);
        if (add) {
            recalculateBounds();
        }
        return add;
    }

    private void recalculateBounds() {
        double d = -1.7976931348623157E308d;
        double d2 = -1.7976931348623157E308d;
        double d3 = Double.MAX_VALUE;
        double d4 = Double.MAX_VALUE;
        for (Overlay bounds : this.mOverlayManager) {
            BoundingBox bounds2 = bounds.getBounds();
            d3 = Math.min(d3, bounds2.getLatSouth());
            d4 = Math.min(d4, bounds2.getLonWest());
            d = Math.max(d, bounds2.getLatNorth());
            d2 = Math.max(d2, bounds2.getLonEast());
        }
        if (d3 == Double.MAX_VALUE) {
            TileSystem tileSystem = MapView.getTileSystem();
            this.mBounds = new BoundingBox(tileSystem.getMaxLatitude(), tileSystem.getMaxLongitude(), tileSystem.getMinLatitude(), tileSystem.getMinLongitude());
            return;
        }
        this.mBounds = new BoundingBox(d, d2, d3, d4);
    }

    public boolean remove(Overlay overlay) {
        boolean remove = this.mOverlayManager.remove(overlay);
        if (remove) {
            recalculateBounds();
        }
        return remove;
    }

    public void draw(Canvas canvas, Projection projection) {
        this.mOverlayManager.onDraw(canvas, projection);
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
        if (!z) {
            this.mOverlayManager.onDraw(canvas, mapView);
        }
    }

    public boolean onSingleTapUp(MotionEvent motionEvent, MapView mapView) {
        if (isEnabled()) {
            return this.mOverlayManager.onSingleTapUp(motionEvent, mapView);
        }
        return false;
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent, MapView mapView) {
        if (isEnabled()) {
            return this.mOverlayManager.onSingleTapConfirmed(motionEvent, mapView);
        }
        return false;
    }

    public boolean onLongPress(MotionEvent motionEvent, MapView mapView) {
        if (isEnabled()) {
            return this.mOverlayManager.onLongPress(motionEvent, mapView);
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (isEnabled()) {
            return this.mOverlayManager.onTouchEvent(motionEvent, mapView);
        }
        return false;
    }

    public void closeAllInfoWindows() {
        for (Overlay overlay : this.mOverlayManager) {
            if (overlay instanceof FolderOverlay) {
                ((FolderOverlay) overlay).closeAllInfoWindows();
            } else if (overlay instanceof OverlayWithIW) {
                ((OverlayWithIW) overlay).closeInfoWindow();
            }
        }
    }

    public void onDetach(MapView mapView) {
        OverlayManager overlayManager = this.mOverlayManager;
        if (overlayManager != null) {
            overlayManager.onDetach(mapView);
        }
        this.mOverlayManager = null;
    }
}
