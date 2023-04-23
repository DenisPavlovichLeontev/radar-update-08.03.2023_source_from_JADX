package org.mapsforge.map.layer.overlay;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.layer.Layer;

public class Polygon extends Layer {
    private BoundingBox boundingBox;
    private final GraphicFactory graphicFactory;
    private final boolean keepAligned;
    private final List<LatLong> latLongs;
    private Paint paintFill;
    private Paint paintStroke;

    public Polygon(Paint paint, Paint paint2, GraphicFactory graphicFactory2) {
        this(paint, paint2, graphicFactory2, false);
    }

    public Polygon(Paint paint, Paint paint2, GraphicFactory graphicFactory2, boolean z) {
        this.latLongs = new CopyOnWriteArrayList();
        this.keepAligned = z;
        this.paintFill = paint;
        this.paintStroke = paint2;
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

    public synchronized boolean contains(LatLong latLong) {
        return LatLongUtils.contains(this.latLongs, latLong);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0098, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x009a, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void draw(org.mapsforge.core.model.BoundingBox r9, byte r10, org.mapsforge.core.graphics.Canvas r11, org.mapsforge.core.model.Point r12) {
        /*
            r8 = this;
            monitor-enter(r8)
            java.util.List<org.mapsforge.core.model.LatLong> r0 = r8.latLongs     // Catch:{ all -> 0x009b }
            int r0 = r0.size()     // Catch:{ all -> 0x009b }
            r1 = 2
            if (r0 < r1) goto L_0x0099
            org.mapsforge.core.graphics.Paint r0 = r8.paintStroke     // Catch:{ all -> 0x009b }
            if (r0 != 0) goto L_0x0014
            org.mapsforge.core.graphics.Paint r0 = r8.paintFill     // Catch:{ all -> 0x009b }
            if (r0 != 0) goto L_0x0014
            goto L_0x0099
        L_0x0014:
            org.mapsforge.core.model.BoundingBox r0 = r8.boundingBox     // Catch:{ all -> 0x009b }
            if (r0 == 0) goto L_0x0020
            boolean r9 = r0.intersects(r9)     // Catch:{ all -> 0x009b }
            if (r9 != 0) goto L_0x0020
            monitor-exit(r8)
            return
        L_0x0020:
            java.util.List<org.mapsforge.core.model.LatLong> r9 = r8.latLongs     // Catch:{ all -> 0x009b }
            java.util.Iterator r9 = r9.iterator()     // Catch:{ all -> 0x009b }
            org.mapsforge.core.graphics.GraphicFactory r0 = r8.graphicFactory     // Catch:{ all -> 0x009b }
            org.mapsforge.core.graphics.Path r0 = r0.createPath()     // Catch:{ all -> 0x009b }
            java.lang.Object r1 = r9.next()     // Catch:{ all -> 0x009b }
            org.mapsforge.core.model.LatLong r1 = (org.mapsforge.core.model.LatLong) r1     // Catch:{ all -> 0x009b }
            org.mapsforge.map.model.DisplayModel r2 = r8.displayModel     // Catch:{ all -> 0x009b }
            int r2 = r2.getTileSize()     // Catch:{ all -> 0x009b }
            long r2 = org.mapsforge.core.util.MercatorProjection.getMapSize(r10, r2)     // Catch:{ all -> 0x009b }
            double r4 = r1.longitude     // Catch:{ all -> 0x009b }
            double r4 = org.mapsforge.core.util.MercatorProjection.longitudeToPixelX(r4, r2)     // Catch:{ all -> 0x009b }
            double r6 = r12.f381x     // Catch:{ all -> 0x009b }
            double r4 = r4 - r6
            float r10 = (float) r4     // Catch:{ all -> 0x009b }
            double r4 = r1.latitude     // Catch:{ all -> 0x009b }
            double r4 = org.mapsforge.core.util.MercatorProjection.latitudeToPixelY(r4, r2)     // Catch:{ all -> 0x009b }
            double r6 = r12.f382y     // Catch:{ all -> 0x009b }
            double r4 = r4 - r6
            float r1 = (float) r4     // Catch:{ all -> 0x009b }
            r0.moveTo(r10, r1)     // Catch:{ all -> 0x009b }
        L_0x0053:
            boolean r10 = r9.hasNext()     // Catch:{ all -> 0x009b }
            if (r10 == 0) goto L_0x0077
            java.lang.Object r10 = r9.next()     // Catch:{ all -> 0x009b }
            org.mapsforge.core.model.LatLong r10 = (org.mapsforge.core.model.LatLong) r10     // Catch:{ all -> 0x009b }
            double r4 = r10.longitude     // Catch:{ all -> 0x009b }
            double r4 = org.mapsforge.core.util.MercatorProjection.longitudeToPixelX(r4, r2)     // Catch:{ all -> 0x009b }
            double r6 = r12.f381x     // Catch:{ all -> 0x009b }
            double r4 = r4 - r6
            float r1 = (float) r4     // Catch:{ all -> 0x009b }
            double r4 = r10.latitude     // Catch:{ all -> 0x009b }
            double r4 = org.mapsforge.core.util.MercatorProjection.latitudeToPixelY(r4, r2)     // Catch:{ all -> 0x009b }
            double r6 = r12.f382y     // Catch:{ all -> 0x009b }
            double r4 = r4 - r6
            float r10 = (float) r4     // Catch:{ all -> 0x009b }
            r0.lineTo(r1, r10)     // Catch:{ all -> 0x009b }
            goto L_0x0053
        L_0x0077:
            org.mapsforge.core.graphics.Paint r9 = r8.paintStroke     // Catch:{ all -> 0x009b }
            if (r9 == 0) goto L_0x0087
            boolean r10 = r8.keepAligned     // Catch:{ all -> 0x009b }
            if (r10 == 0) goto L_0x0082
            r9.setBitmapShaderShift(r12)     // Catch:{ all -> 0x009b }
        L_0x0082:
            org.mapsforge.core.graphics.Paint r9 = r8.paintStroke     // Catch:{ all -> 0x009b }
            r11.drawPath(r0, r9)     // Catch:{ all -> 0x009b }
        L_0x0087:
            org.mapsforge.core.graphics.Paint r9 = r8.paintFill     // Catch:{ all -> 0x009b }
            if (r9 == 0) goto L_0x0097
            boolean r10 = r8.keepAligned     // Catch:{ all -> 0x009b }
            if (r10 == 0) goto L_0x0092
            r9.setBitmapShaderShift(r12)     // Catch:{ all -> 0x009b }
        L_0x0092:
            org.mapsforge.core.graphics.Paint r9 = r8.paintFill     // Catch:{ all -> 0x009b }
            r11.drawPath(r0, r9)     // Catch:{ all -> 0x009b }
        L_0x0097:
            monitor-exit(r8)
            return
        L_0x0099:
            monitor-exit(r8)
            return
        L_0x009b:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.overlay.Polygon.draw(org.mapsforge.core.model.BoundingBox, byte, org.mapsforge.core.graphics.Canvas, org.mapsforge.core.model.Point):void");
    }

    public List<LatLong> getLatLongs() {
        return this.latLongs;
    }

    public synchronized Paint getPaintFill() {
        return this.paintFill;
    }

    public synchronized Paint getPaintStroke() {
        return this.paintStroke;
    }

    public boolean isKeepAligned() {
        return this.keepAligned;
    }

    public synchronized void setPaintFill(Paint paint) {
        this.paintFill = paint;
    }

    public synchronized void setPaintStroke(Paint paint) {
        this.paintStroke = paint;
    }

    public synchronized void setPoints(List<LatLong> list) {
        this.latLongs.clear();
        this.latLongs.addAll(list);
        updatePoints();
    }

    private void updatePoints() {
        this.boundingBox = this.latLongs.isEmpty() ? null : new BoundingBox(this.latLongs);
    }
}
