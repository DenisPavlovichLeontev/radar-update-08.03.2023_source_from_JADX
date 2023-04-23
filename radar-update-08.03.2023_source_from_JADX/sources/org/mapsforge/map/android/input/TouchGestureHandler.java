package org.mapsforge.map.android.input;

import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.Scroller;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.model.IMapViewPosition;

public class TouchGestureHandler extends GestureDetector.SimpleOnGestureListener implements ScaleGestureDetector.OnScaleGestureListener, Runnable {
    private boolean doubleTapEnabled = true;
    private int flingLastX;
    private int flingLastY;
    private final Scroller flinger;
    private float focusX;
    private float focusY;
    private final Handler handler = new Handler();
    private boolean isInDoubleTap;
    private boolean isInScale;
    private final MapView mapView;
    private LatLong pivot;
    private boolean scaleEnabled = true;
    private float scaleFactorCumulative;

    public TouchGestureHandler(MapView mapView2) {
        this.mapView = mapView2;
        this.flinger = new Scroller(mapView2.getContext());
    }

    public void destroy() {
        this.handler.removeCallbacksAndMessages((Object) null);
    }

    public boolean isDoubleTapEnabled() {
        return this.doubleTapEnabled;
    }

    public boolean isScaleEnabled() {
        return this.scaleEnabled;
    }

    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        if (!this.scaleEnabled) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.isInDoubleTap = true;
        } else if (actionMasked == 1 && this.isInDoubleTap) {
            IMapViewPosition iMapViewPosition = this.mapView.getModel().mapViewPosition;
            if (this.doubleTapEnabled && iMapViewPosition.getZoomLevel() < iMapViewPosition.getZoomLevelMax()) {
                Point center = this.mapView.getModel().mapViewDimension.getDimension().getCenter();
                double d = (double) 1;
                double x = (center.f381x - ((double) motionEvent.getX())) / Math.pow(2.0d, d);
                double y = (center.f382y - ((double) motionEvent.getY())) / Math.pow(2.0d, d);
                LatLong fromPixels = this.mapView.getMapViewProjection().fromPixels((double) motionEvent.getX(), (double) motionEvent.getY());
                if (fromPixels != null) {
                    this.mapView.onMoveEvent();
                    this.mapView.onZoomEvent();
                    iMapViewPosition.setPivot(fromPixels);
                    iMapViewPosition.moveCenterAndZoom(x, y, (byte) 1);
                }
            }
            this.isInDoubleTap = false;
            return true;
        }
        return false;
    }

    public boolean onDown(MotionEvent motionEvent) {
        this.isInScale = false;
        this.flinger.forceFinished(true);
        return true;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.isInScale || motionEvent.getPointerCount() != 1 || motionEvent2.getPointerCount() != 1) {
            return false;
        }
        this.flinger.fling(0, 0, (int) (-f), (int) (-f2), Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.flingLastY = 0;
        this.flingLastX = 0;
        this.handler.removeCallbacksAndMessages((Object) null);
        this.handler.post(this);
        return true;
    }

    public void onLongPress(MotionEvent motionEvent) {
        if (!this.isInScale && !this.isInDoubleTap) {
            Point point = new Point((double) motionEvent.getX(), (double) motionEvent.getY());
            LatLong fromPixels = this.mapView.getMapViewProjection().fromPixels(point.f381x, point.f382y);
            if (fromPixels != null) {
                int size = this.mapView.getLayerManager().getLayers().size() - 1;
                while (size >= 0) {
                    Layer layer = this.mapView.getLayerManager().getLayers().get(size);
                    if (!layer.onLongPress(fromPixels, this.mapView.getMapViewProjection().toPixels(layer.getPosition()), point)) {
                        size--;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        this.scaleFactorCumulative *= scaleGestureDetector.getScaleFactor();
        this.mapView.getModel().mapViewPosition.setPivot(this.pivot);
        this.mapView.getModel().mapViewPosition.setScaleFactorAdjustment((double) this.scaleFactorCumulative);
        return true;
    }

    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        if (!this.scaleEnabled) {
            return false;
        }
        this.isInScale = true;
        this.scaleFactorCumulative = 1.0f;
        if (this.isInDoubleTap) {
            this.mapView.onZoomEvent();
            this.pivot = null;
        } else {
            this.mapView.onMoveEvent();
            this.mapView.onZoomEvent();
            this.focusX = scaleGestureDetector.getFocusX();
            this.focusY = scaleGestureDetector.getFocusY();
            this.pivot = this.mapView.getMapViewProjection().fromPixels((double) this.focusX, (double) this.focusY);
        }
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        long j;
        double d;
        double log = Math.log((double) this.scaleFactorCumulative) / Math.log(2.0d);
        double d2 = 0.0d;
        if (Math.abs(log) > 1.0d) {
            j = Math.round(log < 0.0d ? Math.floor(log) : Math.ceil(log));
        } else {
            j = Math.round(log);
        }
        byte b = (byte) ((int) j);
        IMapViewPosition iMapViewPosition = this.mapView.getModel().mapViewPosition;
        if (b == 0 || this.pivot == null) {
            iMapViewPosition.zoom(b);
        } else {
            Point center = this.mapView.getModel().mapViewDimension.getDimension().getCenter();
            if (b > 0) {
                int i = 1;
                d = 0.0d;
                while (i <= b && iMapViewPosition.getZoomLevel() + i <= iMapViewPosition.getZoomLevelMax()) {
                    double d3 = (double) i;
                    double pow = d2 + ((center.f381x - ((double) this.focusX)) / Math.pow(2.0d, d3));
                    d += (center.f382y - ((double) this.focusY)) / Math.pow(2.0d, d3);
                    i++;
                    d2 = pow;
                }
            } else {
                int i2 = -1;
                double d4 = 0.0d;
                while (i2 >= b && iMapViewPosition.getZoomLevel() + i2 >= iMapViewPosition.getZoomLevelMin()) {
                    double d5 = (double) (i2 + 1);
                    double pow2 = d2 - ((center.f381x - ((double) this.focusX)) / Math.pow(2.0d, d5));
                    d4 = d - ((center.f382y - ((double) this.focusY)) / Math.pow(2.0d, d5));
                    i2--;
                    d2 = pow2;
                }
            }
            iMapViewPosition.setPivot(this.pivot);
            iMapViewPosition.moveCenterAndZoom(d2, d, b);
        }
        this.isInDoubleTap = false;
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.isInScale || motionEvent.getPointerCount() != 1 || motionEvent2.getPointerCount() != 1) {
            return false;
        }
        this.mapView.onMoveEvent();
        this.mapView.getModel().mapViewPosition.moveCenter((double) (-f), (double) (-f2), false);
        return true;
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        Point point = new Point((double) motionEvent.getX(), (double) motionEvent.getY());
        LatLong fromPixels = this.mapView.getMapViewProjection().fromPixels(point.f381x, point.f382y);
        if (fromPixels == null) {
            return false;
        }
        for (int size = this.mapView.getLayerManager().getLayers().size() - 1; size >= 0; size--) {
            Layer layer = this.mapView.getLayerManager().getLayers().get(size);
            if (layer.onTap(fromPixels, this.mapView.getMapViewProjection().toPixels(layer.getPosition()), point)) {
                return true;
            }
        }
        return false;
    }

    public void run() {
        boolean z = !this.flinger.isFinished() && this.flinger.computeScrollOffset();
        this.mapView.getModel().mapViewPosition.moveCenter((double) (this.flingLastX - this.flinger.getCurrX()), (double) (this.flingLastY - this.flinger.getCurrY()));
        this.flingLastX = this.flinger.getCurrX();
        this.flingLastY = this.flinger.getCurrY();
        if (z) {
            this.handler.post(this);
        }
    }

    public void setDoubleTapEnabled(boolean z) {
        this.doubleTapEnabled = z;
    }

    public void setScaleEnabled(boolean z) {
        this.scaleEnabled = z;
    }
}
