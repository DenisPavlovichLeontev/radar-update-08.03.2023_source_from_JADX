package org.mapsforge.map.layer.overlay;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.layer.Layer;

public class Circle extends Layer {
    private final boolean keepAligned;
    private LatLong latLong;
    private Paint paintFill;
    private Paint paintStroke;
    private float radius;

    public Circle(LatLong latLong2, float f, Paint paint, Paint paint2) {
        this(latLong2, f, paint, paint2, false);
    }

    public Circle(LatLong latLong2, float f, Paint paint, Paint paint2, boolean z) {
        this.keepAligned = z;
        this.latLong = latLong2;
        setRadiusInternal(f);
        this.paintFill = paint;
        this.paintStroke = paint2;
    }

    public synchronized boolean contains(Point point, Point point2, double d, byte b) {
        return point.distance(point2) < ((double) Math.max(this.displayModel.getScaleFactor() * 10.0f, (float) getRadiusInPixels(d, b)));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x007c, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007e, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void draw(org.mapsforge.core.model.BoundingBox r18, byte r19, org.mapsforge.core.graphics.Canvas r20, org.mapsforge.core.model.Point r21) {
        /*
            r17 = this;
            r1 = r17
            r0 = r19
            r2 = r20
            r3 = r21
            monitor-enter(r17)
            org.mapsforge.core.model.LatLong r4 = r1.latLong     // Catch:{ all -> 0x007f }
            if (r4 == 0) goto L_0x007d
            org.mapsforge.core.graphics.Paint r5 = r1.paintStroke     // Catch:{ all -> 0x007f }
            if (r5 != 0) goto L_0x0016
            org.mapsforge.core.graphics.Paint r5 = r1.paintFill     // Catch:{ all -> 0x007f }
            if (r5 != 0) goto L_0x0016
            goto L_0x007d
        L_0x0016:
            double r4 = r4.latitude     // Catch:{ all -> 0x007f }
            org.mapsforge.core.model.LatLong r6 = r1.latLong     // Catch:{ all -> 0x007f }
            double r6 = r6.longitude     // Catch:{ all -> 0x007f }
            org.mapsforge.map.model.DisplayModel r8 = r1.displayModel     // Catch:{ all -> 0x007f }
            int r8 = r8.getTileSize()     // Catch:{ all -> 0x007f }
            long r8 = org.mapsforge.core.util.MercatorProjection.getMapSize(r0, r8)     // Catch:{ all -> 0x007f }
            double r6 = org.mapsforge.core.util.MercatorProjection.longitudeToPixelX(r6, r8)     // Catch:{ all -> 0x007f }
            double r10 = r3.f381x     // Catch:{ all -> 0x007f }
            double r6 = r6 - r10
            int r6 = (int) r6     // Catch:{ all -> 0x007f }
            double r7 = org.mapsforge.core.util.MercatorProjection.latitudeToPixelY(r4, r8)     // Catch:{ all -> 0x007f }
            double r9 = r3.f382y     // Catch:{ all -> 0x007f }
            double r7 = r7 - r9
            int r7 = (int) r7     // Catch:{ all -> 0x007f }
            int r0 = r1.getRadiusInPixels(r4, r0)     // Catch:{ all -> 0x007f }
            org.mapsforge.core.model.Rectangle r4 = new org.mapsforge.core.model.Rectangle     // Catch:{ all -> 0x007f }
            r9 = 0
            int r5 = r20.getWidth()     // Catch:{ all -> 0x007f }
            double r13 = (double) r5     // Catch:{ all -> 0x007f }
            int r5 = r20.getHeight()     // Catch:{ all -> 0x007f }
            double r11 = (double) r5     // Catch:{ all -> 0x007f }
            r8 = r4
            r15 = r11
            r11 = 0
            r8.<init>(r9, r11, r13, r15)     // Catch:{ all -> 0x007f }
            double r9 = (double) r6     // Catch:{ all -> 0x007f }
            double r11 = (double) r7     // Catch:{ all -> 0x007f }
            double r13 = (double) r0     // Catch:{ all -> 0x007f }
            r8 = r4
            boolean r4 = r8.intersectsCircle(r9, r11, r13)     // Catch:{ all -> 0x007f }
            if (r4 != 0) goto L_0x005b
            monitor-exit(r17)
            return
        L_0x005b:
            org.mapsforge.core.graphics.Paint r4 = r1.paintStroke     // Catch:{ all -> 0x007f }
            if (r4 == 0) goto L_0x006b
            boolean r5 = r1.keepAligned     // Catch:{ all -> 0x007f }
            if (r5 == 0) goto L_0x0066
            r4.setBitmapShaderShift(r3)     // Catch:{ all -> 0x007f }
        L_0x0066:
            org.mapsforge.core.graphics.Paint r4 = r1.paintStroke     // Catch:{ all -> 0x007f }
            r2.drawCircle(r6, r7, r0, r4)     // Catch:{ all -> 0x007f }
        L_0x006b:
            org.mapsforge.core.graphics.Paint r4 = r1.paintFill     // Catch:{ all -> 0x007f }
            if (r4 == 0) goto L_0x007b
            boolean r5 = r1.keepAligned     // Catch:{ all -> 0x007f }
            if (r5 == 0) goto L_0x0076
            r4.setBitmapShaderShift(r3)     // Catch:{ all -> 0x007f }
        L_0x0076:
            org.mapsforge.core.graphics.Paint r3 = r1.paintFill     // Catch:{ all -> 0x007f }
            r2.drawCircle(r6, r7, r0, r3)     // Catch:{ all -> 0x007f }
        L_0x007b:
            monitor-exit(r17)
            return
        L_0x007d:
            monitor-exit(r17)
            return
        L_0x007f:
            r0 = move-exception
            monitor-exit(r17)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.overlay.Circle.draw(org.mapsforge.core.model.BoundingBox, byte, org.mapsforge.core.graphics.Canvas, org.mapsforge.core.model.Point):void");
    }

    public synchronized Paint getPaintFill() {
        return this.paintFill;
    }

    public synchronized Paint getPaintStroke() {
        return this.paintStroke;
    }

    public synchronized LatLong getPosition() {
        return this.latLong;
    }

    public synchronized float getRadius() {
        return this.radius;
    }

    /* access modifiers changed from: protected */
    public int getRadiusInPixels(double d, byte b) {
        return (int) MercatorProjection.metersToPixels(this.radius, d, MercatorProjection.getMapSize(b, this.displayModel.getTileSize()));
    }

    public boolean isKeepAligned() {
        return this.keepAligned;
    }

    public synchronized void setLatLong(LatLong latLong2) {
        this.latLong = latLong2;
    }

    public synchronized void setPaintFill(Paint paint) {
        this.paintFill = paint;
    }

    public synchronized void setPaintStroke(Paint paint) {
        this.paintStroke = paint;
    }

    public synchronized void setRadius(float f) {
        setRadiusInternal(f);
    }

    private void setRadiusInternal(float f) {
        if (f < 0.0f || Float.isNaN(f)) {
            throw new IllegalArgumentException("invalid radius: " + f);
        }
        this.radius = f;
    }
}
