package org.mapsforge.map.layer.overlay;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.util.MapViewProjection;

public class Polyline extends Layer {
    private static final byte STROKE_MIN_ZOOM = 12;
    private BoundingBox boundingBox;
    private final GraphicFactory graphicFactory;
    private final boolean keepAligned;
    private final List<LatLong> latLongs;
    private Paint paintStroke;
    private double strokeIncrease;

    public Polyline(Paint paint, GraphicFactory graphicFactory2) {
        this(paint, graphicFactory2, false);
    }

    public Polyline(Paint paint, GraphicFactory graphicFactory2, boolean z) {
        this.latLongs = new CopyOnWriteArrayList();
        this.strokeIncrease = 1.0d;
        this.keepAligned = z;
        this.paintStroke = paint;
        this.graphicFactory = graphicFactory2;
    }

    public synchronized void addPoint(LatLong latLong) {
        this.latLongs.add(latLong);
        updatePoints();
    }

    public synchronized void addPoints(List<LatLong> list) {
        this.latLongs.addAll(list);
        updatePoints();
    }

    public synchronized void clear() {
        this.latLongs.clear();
        updatePoints();
    }

    public synchronized boolean contains(Point point, MapViewProjection mapViewProjection) {
        Point point2 = point;
        MapViewProjection mapViewProjection2 = mapViewProjection;
        synchronized (this) {
            double max = (double) Math.max(this.displayModel.getScaleFactor() * 10.0f, this.paintStroke.getStrokeWidth() / 2.0f);
            Point point3 = null;
            int i = 0;
            while (i < this.latLongs.size() - 1) {
                if (i == 0) {
                    point3 = mapViewProjection2.toPixels(this.latLongs.get(i));
                }
                int i2 = i + 1;
                Point pixels = mapViewProjection2.toPixels(this.latLongs.get(i2));
                int i3 = i2;
                Point point4 = pixels;
                if (LatLongUtils.distanceSegmentPoint(point3.f381x, point3.f382y, pixels.f381x, pixels.f382y, point2.f381x, point2.f382y) <= max) {
                    return true;
                }
                i = i3;
                point3 = point4;
            }
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00b1, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void draw(org.mapsforge.core.model.BoundingBox r10, byte r11, org.mapsforge.core.graphics.Canvas r12, org.mapsforge.core.model.Point r13) {
        /*
            r9 = this;
            monitor-enter(r9)
            java.util.List<org.mapsforge.core.model.LatLong> r0 = r9.latLongs     // Catch:{ all -> 0x00b2 }
            boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x00b2 }
            if (r0 != 0) goto L_0x00b0
            org.mapsforge.core.graphics.Paint r0 = r9.paintStroke     // Catch:{ all -> 0x00b2 }
            if (r0 != 0) goto L_0x000f
            goto L_0x00b0
        L_0x000f:
            org.mapsforge.core.model.BoundingBox r0 = r9.boundingBox     // Catch:{ all -> 0x00b2 }
            if (r0 == 0) goto L_0x001b
            boolean r10 = r0.intersects(r10)     // Catch:{ all -> 0x00b2 }
            if (r10 != 0) goto L_0x001b
            monitor-exit(r9)
            return
        L_0x001b:
            java.util.List<org.mapsforge.core.model.LatLong> r10 = r9.latLongs     // Catch:{ all -> 0x00b2 }
            java.util.Iterator r10 = r10.iterator()     // Catch:{ all -> 0x00b2 }
            boolean r0 = r10.hasNext()     // Catch:{ all -> 0x00b2 }
            if (r0 != 0) goto L_0x0029
            monitor-exit(r9)
            return
        L_0x0029:
            java.lang.Object r0 = r10.next()     // Catch:{ all -> 0x00b2 }
            org.mapsforge.core.model.LatLong r0 = (org.mapsforge.core.model.LatLong) r0     // Catch:{ all -> 0x00b2 }
            org.mapsforge.map.model.DisplayModel r1 = r9.displayModel     // Catch:{ all -> 0x00b2 }
            int r1 = r1.getTileSize()     // Catch:{ all -> 0x00b2 }
            long r1 = org.mapsforge.core.util.MercatorProjection.getMapSize(r11, r1)     // Catch:{ all -> 0x00b2 }
            double r3 = r0.longitude     // Catch:{ all -> 0x00b2 }
            double r3 = org.mapsforge.core.util.MercatorProjection.longitudeToPixelX(r3, r1)     // Catch:{ all -> 0x00b2 }
            double r5 = r13.f381x     // Catch:{ all -> 0x00b2 }
            double r3 = r3 - r5
            float r3 = (float) r3     // Catch:{ all -> 0x00b2 }
            double r4 = r0.latitude     // Catch:{ all -> 0x00b2 }
            double r4 = org.mapsforge.core.util.MercatorProjection.latitudeToPixelY(r4, r1)     // Catch:{ all -> 0x00b2 }
            double r6 = r13.f382y     // Catch:{ all -> 0x00b2 }
            double r4 = r4 - r6
            float r0 = (float) r4     // Catch:{ all -> 0x00b2 }
            org.mapsforge.core.graphics.GraphicFactory r4 = r9.graphicFactory     // Catch:{ all -> 0x00b2 }
            org.mapsforge.core.graphics.Path r4 = r4.createPath()     // Catch:{ all -> 0x00b2 }
            r4.moveTo(r3, r0)     // Catch:{ all -> 0x00b2 }
        L_0x0056:
            boolean r0 = r10.hasNext()     // Catch:{ all -> 0x00b2 }
            if (r0 == 0) goto L_0x007a
            java.lang.Object r0 = r10.next()     // Catch:{ all -> 0x00b2 }
            org.mapsforge.core.model.LatLong r0 = (org.mapsforge.core.model.LatLong) r0     // Catch:{ all -> 0x00b2 }
            double r5 = r0.longitude     // Catch:{ all -> 0x00b2 }
            double r5 = org.mapsforge.core.util.MercatorProjection.longitudeToPixelX(r5, r1)     // Catch:{ all -> 0x00b2 }
            double r7 = r13.f381x     // Catch:{ all -> 0x00b2 }
            double r5 = r5 - r7
            float r3 = (float) r5     // Catch:{ all -> 0x00b2 }
            double r5 = r0.latitude     // Catch:{ all -> 0x00b2 }
            double r5 = org.mapsforge.core.util.MercatorProjection.latitudeToPixelY(r5, r1)     // Catch:{ all -> 0x00b2 }
            double r7 = r13.f382y     // Catch:{ all -> 0x00b2 }
            double r5 = r5 - r7
            float r0 = (float) r5     // Catch:{ all -> 0x00b2 }
            r4.lineTo(r3, r0)     // Catch:{ all -> 0x00b2 }
            goto L_0x0056
        L_0x007a:
            boolean r10 = r9.keepAligned     // Catch:{ all -> 0x00b2 }
            if (r10 == 0) goto L_0x0083
            org.mapsforge.core.graphics.Paint r10 = r9.paintStroke     // Catch:{ all -> 0x00b2 }
            r10.setBitmapShaderShift(r13)     // Catch:{ all -> 0x00b2 }
        L_0x0083:
            org.mapsforge.core.graphics.Paint r10 = r9.paintStroke     // Catch:{ all -> 0x00b2 }
            float r10 = r10.getStrokeWidth()     // Catch:{ all -> 0x00b2 }
            double r0 = r9.strokeIncrease     // Catch:{ all -> 0x00b2 }
            r2 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r13 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r13 <= 0) goto L_0x00a4
            int r11 = r11 + -12
            r13 = 0
            int r11 = java.lang.Math.max(r11, r13)     // Catch:{ all -> 0x00b2 }
            double r2 = (double) r11     // Catch:{ all -> 0x00b2 }
            double r0 = java.lang.Math.pow(r0, r2)     // Catch:{ all -> 0x00b2 }
            float r11 = (float) r0     // Catch:{ all -> 0x00b2 }
            org.mapsforge.core.graphics.Paint r13 = r9.paintStroke     // Catch:{ all -> 0x00b2 }
            float r11 = r11 * r10
            r13.setStrokeWidth(r11)     // Catch:{ all -> 0x00b2 }
        L_0x00a4:
            org.mapsforge.core.graphics.Paint r11 = r9.paintStroke     // Catch:{ all -> 0x00b2 }
            r12.drawPath(r4, r11)     // Catch:{ all -> 0x00b2 }
            org.mapsforge.core.graphics.Paint r11 = r9.paintStroke     // Catch:{ all -> 0x00b2 }
            r11.setStrokeWidth(r10)     // Catch:{ all -> 0x00b2 }
            monitor-exit(r9)
            return
        L_0x00b0:
            monitor-exit(r9)
            return
        L_0x00b2:
            r10 = move-exception
            monitor-exit(r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.overlay.Polyline.draw(org.mapsforge.core.model.BoundingBox, byte, org.mapsforge.core.graphics.Canvas, org.mapsforge.core.model.Point):void");
    }

    public List<LatLong> getLatLongs() {
        return this.latLongs;
    }

    public synchronized Paint getPaintStroke() {
        return this.paintStroke;
    }

    public synchronized double getStrokeIncrease() {
        return this.strokeIncrease;
    }

    public boolean isKeepAligned() {
        return this.keepAligned;
    }

    public synchronized void setPaintStroke(Paint paint) {
        this.paintStroke = paint;
    }

    public synchronized void setPoints(List<LatLong> list) {
        this.latLongs.clear();
        this.latLongs.addAll(list);
        updatePoints();
    }

    public synchronized void setStrokeIncrease(double d) {
        this.strokeIncrease = d;
    }

    private void updatePoints() {
        this.boundingBox = this.latLongs.isEmpty() ? null : new BoundingBox(this.latLongs);
    }
}
