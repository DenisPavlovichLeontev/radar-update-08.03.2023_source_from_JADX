package org.mapsforge.map.layer.overlay;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Rectangle;
import org.mapsforge.map.layer.Layer;

public class Marker extends Layer {
    private Bitmap bitmap;
    private int horizontalOffset;
    private LatLong latLong;
    private int verticalOffset;

    public Marker(LatLong latLong2, Bitmap bitmap2, int i, int i2) {
        this.latLong = latLong2;
        this.bitmap = bitmap2;
        this.horizontalOffset = i;
        this.verticalOffset = i2;
    }

    public synchronized boolean contains(Point point, Point point2) {
        boolean contains;
        Point point3 = point;
        synchronized (this) {
            double max = ((double) Math.max(this.displayModel.getScaleFactor() * 20.0f, (float) this.bitmap.getWidth())) / 2.0d;
            double d = ((double) this.horizontalOffset) + (point3.f381x - max);
            double max2 = ((double) Math.max(this.displayModel.getScaleFactor() * 20.0f, (float) this.bitmap.getHeight())) / 2.0d;
            contains = new Rectangle(d, (point3.f382y - max2) + ((double) this.verticalOffset), ((double) this.horizontalOffset) + point3.f381x + max, ((double) this.verticalOffset) + point3.f382y + max2).contains(point2);
        }
        return contains;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0097, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void draw(org.mapsforge.core.model.BoundingBox r25, byte r26, org.mapsforge.core.graphics.Canvas r27, org.mapsforge.core.model.Point r28) {
        /*
            r24 = this;
            r1 = r24
            r0 = r28
            monitor-enter(r24)
            org.mapsforge.core.model.LatLong r2 = r1.latLong     // Catch:{ all -> 0x0098 }
            if (r2 == 0) goto L_0x0096
            org.mapsforge.core.graphics.Bitmap r2 = r1.bitmap     // Catch:{ all -> 0x0098 }
            if (r2 == 0) goto L_0x0096
            boolean r2 = r2.isDestroyed()     // Catch:{ all -> 0x0098 }
            if (r2 == 0) goto L_0x0015
            goto L_0x0096
        L_0x0015:
            org.mapsforge.map.model.DisplayModel r2 = r1.displayModel     // Catch:{ all -> 0x0098 }
            int r2 = r2.getTileSize()     // Catch:{ all -> 0x0098 }
            r3 = r26
            long r2 = org.mapsforge.core.util.MercatorProjection.getMapSize(r3, r2)     // Catch:{ all -> 0x0098 }
            org.mapsforge.core.model.LatLong r4 = r1.latLong     // Catch:{ all -> 0x0098 }
            double r4 = r4.longitude     // Catch:{ all -> 0x0098 }
            double r4 = org.mapsforge.core.util.MercatorProjection.longitudeToPixelX(r4, r2)     // Catch:{ all -> 0x0098 }
            org.mapsforge.core.model.LatLong r6 = r1.latLong     // Catch:{ all -> 0x0098 }
            double r6 = r6.latitude     // Catch:{ all -> 0x0098 }
            double r2 = org.mapsforge.core.util.MercatorProjection.latitudeToPixelY(r6, r2)     // Catch:{ all -> 0x0098 }
            org.mapsforge.core.graphics.Bitmap r6 = r1.bitmap     // Catch:{ all -> 0x0098 }
            int r6 = r6.getWidth()     // Catch:{ all -> 0x0098 }
            int r6 = r6 / 2
            org.mapsforge.core.graphics.Bitmap r7 = r1.bitmap     // Catch:{ all -> 0x0098 }
            int r7 = r7.getHeight()     // Catch:{ all -> 0x0098 }
            int r7 = r7 / 2
            double r8 = r0.f381x     // Catch:{ all -> 0x0098 }
            double r4 = r4 - r8
            double r8 = (double) r6     // Catch:{ all -> 0x0098 }
            double r4 = r4 - r8
            int r6 = r1.horizontalOffset     // Catch:{ all -> 0x0098 }
            double r8 = (double) r6     // Catch:{ all -> 0x0098 }
            double r4 = r4 + r8
            int r4 = (int) r4     // Catch:{ all -> 0x0098 }
            double r5 = r0.f382y     // Catch:{ all -> 0x0098 }
            double r2 = r2 - r5
            double r5 = (double) r7     // Catch:{ all -> 0x0098 }
            double r2 = r2 - r5
            int r0 = r1.verticalOffset     // Catch:{ all -> 0x0098 }
            double r5 = (double) r0     // Catch:{ all -> 0x0098 }
            double r2 = r2 + r5
            int r0 = (int) r2     // Catch:{ all -> 0x0098 }
            org.mapsforge.core.graphics.Bitmap r2 = r1.bitmap     // Catch:{ all -> 0x0098 }
            int r2 = r2.getWidth()     // Catch:{ all -> 0x0098 }
            int r2 = r2 + r4
            org.mapsforge.core.graphics.Bitmap r3 = r1.bitmap     // Catch:{ all -> 0x0098 }
            int r3 = r3.getHeight()     // Catch:{ all -> 0x0098 }
            int r3 = r3 + r0
            org.mapsforge.core.model.Rectangle r14 = new org.mapsforge.core.model.Rectangle     // Catch:{ all -> 0x0098 }
            double r6 = (double) r4     // Catch:{ all -> 0x0098 }
            double r8 = (double) r0     // Catch:{ all -> 0x0098 }
            double r10 = (double) r2     // Catch:{ all -> 0x0098 }
            double r12 = (double) r3     // Catch:{ all -> 0x0098 }
            r5 = r14
            r5.<init>(r6, r8, r10, r12)     // Catch:{ all -> 0x0098 }
            org.mapsforge.core.model.Rectangle r2 = new org.mapsforge.core.model.Rectangle     // Catch:{ all -> 0x0098 }
            r16 = 0
            r18 = 0
            int r3 = r27.getWidth()     // Catch:{ all -> 0x0098 }
            double r5 = (double) r3     // Catch:{ all -> 0x0098 }
            int r3 = r27.getHeight()     // Catch:{ all -> 0x0098 }
            double r7 = (double) r3     // Catch:{ all -> 0x0098 }
            r15 = r2
            r20 = r5
            r22 = r7
            r15.<init>(r16, r18, r20, r22)     // Catch:{ all -> 0x0098 }
            boolean r2 = r2.intersects(r14)     // Catch:{ all -> 0x0098 }
            if (r2 != 0) goto L_0x008d
            monitor-exit(r24)
            return
        L_0x008d:
            org.mapsforge.core.graphics.Bitmap r2 = r1.bitmap     // Catch:{ all -> 0x0098 }
            r3 = r27
            r3.drawBitmap((org.mapsforge.core.graphics.Bitmap) r2, (int) r4, (int) r0)     // Catch:{ all -> 0x0098 }
            monitor-exit(r24)
            return
        L_0x0096:
            monitor-exit(r24)
            return
        L_0x0098:
            r0 = move-exception
            monitor-exit(r24)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.overlay.Marker.draw(org.mapsforge.core.model.BoundingBox, byte, org.mapsforge.core.graphics.Canvas, org.mapsforge.core.model.Point):void");
    }

    public synchronized Bitmap getBitmap() {
        return this.bitmap;
    }

    public synchronized int getHorizontalOffset() {
        return this.horizontalOffset;
    }

    public synchronized LatLong getLatLong() {
        return this.latLong;
    }

    public synchronized LatLong getPosition() {
        return this.latLong;
    }

    public synchronized int getVerticalOffset() {
        return this.verticalOffset;
    }

    public synchronized void onDestroy() {
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null) {
            bitmap2.decrementRefCount();
        }
    }

    public synchronized void setBitmap(Bitmap bitmap2) {
        Bitmap bitmap3 = this.bitmap;
        if (bitmap3 == null || !bitmap3.equals(bitmap2)) {
            Bitmap bitmap4 = this.bitmap;
            if (bitmap4 != null) {
                bitmap4.decrementRefCount();
            }
            this.bitmap = bitmap2;
        }
    }

    public synchronized void setHorizontalOffset(int i) {
        this.horizontalOffset = i;
    }

    public synchronized void setLatLong(LatLong latLong2) {
        this.latLong = latLong2;
    }

    public synchronized void setVerticalOffset(int i) {
        this.verticalOffset = i;
    }
}
