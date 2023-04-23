package org.mapsforge.map.layer.overlay;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

public class FixedPixelCircle extends Circle {
    private boolean scaleRadius;

    public FixedPixelCircle(LatLong latLong, float f, Paint paint, Paint paint2) {
        this(latLong, f, paint, paint2, false);
    }

    public FixedPixelCircle(LatLong latLong, float f, Paint paint, Paint paint2, boolean z) {
        super(latLong, f, paint, paint2, z);
        this.scaleRadius = true;
    }

    public synchronized boolean contains(Point point, Point point2) {
        return point.distance(point2) < ((double) Math.max(this.displayModel.getScaleFactor() * 10.0f, getRadius() * (this.scaleRadius ? this.displayModel.getScaleFactor() : 1.0f)));
    }

    /* access modifiers changed from: protected */
    public int getRadiusInPixels(double d, byte b) {
        return (int) (getRadius() * (this.scaleRadius ? this.displayModel.getScaleFactor() : 1.0f));
    }

    public boolean isScaleRadius() {
        return this.scaleRadius;
    }

    public void setScaleRadius(boolean z) {
        this.scaleRadius = z;
    }
}
