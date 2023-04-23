package org.mapsforge.map.android.layers;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.overlay.Circle;
import org.mapsforge.map.layer.overlay.Marker;

public class MyLocationOverlay extends Layer {
    private final Circle circle;
    private final Marker marker;

    public MyLocationOverlay(Marker marker2) {
        this(marker2, (Circle) null);
    }

    public MyLocationOverlay(Marker marker2, Circle circle2) {
        this.marker = marker2;
        this.circle = circle2;
    }

    public synchronized void draw(BoundingBox boundingBox, byte b, Canvas canvas, Point point) {
        Circle circle2 = this.circle;
        if (circle2 != null) {
            circle2.draw(boundingBox, b, canvas, point);
        }
        this.marker.draw(boundingBox, b, canvas, point);
    }

    /* access modifiers changed from: protected */
    public void onAdd() {
        Circle circle2 = this.circle;
        if (circle2 != null) {
            circle2.setDisplayModel(this.displayModel);
        }
        this.marker.setDisplayModel(this.displayModel);
    }

    public void onDestroy() {
        this.marker.onDestroy();
    }

    public void setPosition(double d, double d2, float f) {
        synchronized (this) {
            LatLong latLong = new LatLong(d, d2);
            this.marker.setLatLong(latLong);
            Circle circle2 = this.circle;
            if (circle2 != null) {
                circle2.setLatLong(latLong);
                this.circle.setRadius(f);
            }
            requestRedraw();
        }
    }
}
