package org.mapsforge.map.view;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.GraphicContext;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.FrameBufferModel;

public class FrameBuffer {
    static final boolean IS_TRANSPARENT = false;
    Dimension dimension;
    final DisplayModel displayModel;
    final FrameBufferModel frameBufferModel;
    final GraphicFactory graphicFactory;
    Bitmap lmBitmap;
    final Matrix matrix;
    Bitmap odBitmap;

    public FrameBuffer(FrameBufferModel frameBufferModel2, DisplayModel displayModel2, GraphicFactory graphicFactory2) {
        this.frameBufferModel = frameBufferModel2;
        this.displayModel = displayModel2;
        this.graphicFactory = graphicFactory2;
        this.matrix = graphicFactory2.createMatrix();
    }

    public synchronized void adjustMatrix(float f, float f2, float f3, Dimension dimension2, float f4, float f5) {
        if (this.dimension != null) {
            this.matrix.reset();
            centerFrameBufferToMapView(dimension2);
            if (f4 == 0.0f && f5 == 0.0f) {
                this.matrix.translate(f, f2);
            }
            scale(f3, f4, f5);
        }
    }

    /* access modifiers changed from: package-private */
    public void centerFrameBufferToMapView(Dimension dimension2) {
        this.matrix.translate(((float) (this.dimension.width - dimension2.width)) / -2.0f, ((float) (this.dimension.height - dimension2.height)) / -2.0f);
    }

    public synchronized void destroy() {
        destroyBitmaps();
    }

    private void destroyBitmaps() {
        Bitmap bitmap = this.odBitmap;
        if (bitmap != null) {
            bitmap.decrementRefCount();
            this.odBitmap = null;
        }
        Bitmap bitmap2 = this.lmBitmap;
        if (bitmap2 != null) {
            bitmap2.decrementRefCount();
            this.lmBitmap = null;
        }
    }

    public synchronized void draw(GraphicContext graphicContext) {
        graphicContext.fillColor(this.displayModel.getBackgroundColor());
        Bitmap bitmap = this.odBitmap;
        if (bitmap != null) {
            graphicContext.drawBitmap(bitmap, this.matrix);
        }
    }

    public void frameFinished(MapPosition mapPosition) {
        synchronized (this) {
            Bitmap bitmap = this.odBitmap;
            this.odBitmap = this.lmBitmap;
            this.lmBitmap = bitmap;
        }
        this.frameBufferModel.setMapPosition(mapPosition);
    }

    public synchronized Dimension getDimension() {
        return this.dimension;
    }

    public synchronized Bitmap getDrawingBitmap() {
        Bitmap bitmap = this.lmBitmap;
        if (bitmap != null) {
            bitmap.setBackgroundColor(this.displayModel.getBackgroundColor());
        }
        return this.lmBitmap;
    }

    /* access modifiers changed from: package-private */
    public void scale(float f, float f2, float f3) {
        if (f != 1.0f) {
            Point center = this.dimension.getCenter();
            this.matrix.scale(f, f, (float) (((double) f2) + center.f381x), (float) (((double) f3) + center.f382y));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0034, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void setDimension(org.mapsforge.core.model.Dimension r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            org.mapsforge.core.model.Dimension r0 = r4.dimension     // Catch:{ all -> 0x0035 }
            if (r0 == 0) goto L_0x000d
            boolean r0 = r0.equals(r5)     // Catch:{ all -> 0x0035 }
            if (r0 == 0) goto L_0x000d
            monitor-exit(r4)
            return
        L_0x000d:
            r4.dimension = r5     // Catch:{ all -> 0x0035 }
            r4.destroyBitmaps()     // Catch:{ all -> 0x0035 }
            int r0 = r5.width     // Catch:{ all -> 0x0035 }
            if (r0 <= 0) goto L_0x0033
            int r0 = r5.height     // Catch:{ all -> 0x0035 }
            if (r0 <= 0) goto L_0x0033
            org.mapsforge.core.graphics.GraphicFactory r0 = r4.graphicFactory     // Catch:{ all -> 0x0035 }
            int r1 = r5.width     // Catch:{ all -> 0x0035 }
            int r2 = r5.height     // Catch:{ all -> 0x0035 }
            r3 = 0
            org.mapsforge.core.graphics.Bitmap r0 = r0.createBitmap(r1, r2, r3)     // Catch:{ all -> 0x0035 }
            r4.odBitmap = r0     // Catch:{ all -> 0x0035 }
            org.mapsforge.core.graphics.GraphicFactory r0 = r4.graphicFactory     // Catch:{ all -> 0x0035 }
            int r1 = r5.width     // Catch:{ all -> 0x0035 }
            int r5 = r5.height     // Catch:{ all -> 0x0035 }
            org.mapsforge.core.graphics.Bitmap r5 = r0.createBitmap(r1, r5, r3)     // Catch:{ all -> 0x0035 }
            r4.lmBitmap = r5     // Catch:{ all -> 0x0035 }
        L_0x0033:
            monitor-exit(r4)
            return
        L_0x0035:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.view.FrameBuffer.setDimension(org.mapsforge.core.model.Dimension):void");
    }
}
