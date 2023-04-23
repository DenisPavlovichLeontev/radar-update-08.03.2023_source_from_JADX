package org.mapsforge.map.view;

import java.util.logging.Logger;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.GraphicContext;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.FrameBufferModel;

public class FrameBufferHA extends FrameBuffer {
    private static final Logger LOGGER = Logger.getLogger(FrameBufferHA.class.getName());
    private boolean allowBitmapSwap = true;
    private final Object lmBitmapLock = new Object();
    private MapPosition lmMapPosition;

    public FrameBufferHA(FrameBufferModel frameBufferModel, DisplayModel displayModel, GraphicFactory graphicFactory) {
        super(frameBufferModel, displayModel, graphicFactory);
    }

    public void draw(GraphicContext graphicContext) {
        graphicContext.fillColor(this.displayModel.getBackgroundColor());
        swapBitmaps();
        if (this.odBitmap != null) {
            graphicContext.drawBitmap(this.odBitmap, this.matrix);
        }
    }

    public void frameFinished(MapPosition mapPosition) {
        freeLmBitmap(mapPosition);
    }

    private void freeLmBitmap(MapPosition mapPosition) {
        synchronized (this.lmBitmapLock) {
            this.lmMapPosition = mapPosition;
            this.allowBitmapSwap = true;
        }
    }

    public Bitmap getDrawingBitmap() {
        lockLmBitmap();
        return super.getDrawingBitmap();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:7|8|9|10) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0011 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void lockLmBitmap() {
        /*
            r3 = this;
            java.lang.Object r0 = r3.lmBitmapLock
            monitor-enter(r0)
            org.mapsforge.core.graphics.Bitmap r1 = r3.lmBitmap     // Catch:{ all -> 0x001d }
            if (r1 == 0) goto L_0x0018
            boolean r1 = r3.allowBitmapSwap     // Catch:{ all -> 0x001d }
            if (r1 == 0) goto L_0x0018
            java.lang.Object r1 = r3.lmBitmapLock     // Catch:{ InterruptedException -> 0x0011 }
            r1.wait()     // Catch:{ InterruptedException -> 0x0011 }
            goto L_0x0018
        L_0x0011:
            java.util.logging.Logger r1 = LOGGER     // Catch:{ all -> 0x001d }
            java.lang.String r2 = "FrameBufferHA interrupted"
            r1.warning(r2)     // Catch:{ all -> 0x001d }
        L_0x0018:
            r1 = 0
            r3.allowBitmapSwap = r1     // Catch:{ all -> 0x001d }
            monitor-exit(r0)     // Catch:{ all -> 0x001d }
            return
        L_0x001d:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x001d }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.view.FrameBufferHA.lockLmBitmap():void");
    }

    private void swapBitmaps() {
        synchronized (this.lmBitmapLock) {
            if (this.allowBitmapSwap) {
                Bitmap bitmap = this.odBitmap;
                this.odBitmap = this.lmBitmap;
                this.lmBitmap = bitmap;
                this.frameBufferModel.setMapPosition(this.lmMapPosition);
                this.allowBitmapSwap = false;
                this.lmBitmapLock.notify();
            }
        }
    }
}
