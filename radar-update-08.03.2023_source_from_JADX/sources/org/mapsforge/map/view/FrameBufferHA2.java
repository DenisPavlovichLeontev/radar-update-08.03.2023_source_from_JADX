package org.mapsforge.map.view;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.GraphicContext;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.FrameBufferModel;
import org.mapsforge.map.view.FrameBufferBitmap;

public class FrameBufferHA2 extends FrameBuffer {
    private final FrameBufferBitmap.Lock allowSwap;
    private final FrameBufferBitmap lmBitmap = new FrameBufferBitmap();
    private MapPosition lmMapPosition;
    private final FrameBufferBitmap odBitmap = new FrameBufferBitmap();

    public FrameBufferHA2(FrameBufferModel frameBufferModel, DisplayModel displayModel, GraphicFactory graphicFactory) {
        super(frameBufferModel, displayModel, graphicFactory);
        FrameBufferBitmap.Lock lock = new FrameBufferBitmap.Lock();
        this.allowSwap = lock;
        lock.disable();
    }

    public void adjustMatrix(float f, float f2, float f3, Dimension dimension, float f4, float f5) {
        synchronized (this.matrix) {
            if (this.dimension != null) {
                this.matrix.reset();
                centerFrameBufferToMapView(dimension);
                if (f4 == 0.0f && f5 == 0.0f) {
                    this.matrix.translate(f, f2);
                }
                scale(f3, f4, f5);
            }
        }
    }

    public synchronized void destroy() {
        this.odBitmap.destroy();
        this.lmBitmap.destroy();
    }

    public void draw(GraphicContext graphicContext) {
        graphicContext.fillColor(this.displayModel.getBackgroundColor());
        swapBitmaps();
        synchronized (this.matrix) {
            Bitmap lock = this.odBitmap.lock();
            if (lock != null) {
                graphicContext.drawBitmap(lock, this.matrix);
            }
        }
        this.odBitmap.release();
    }

    public void frameFinished(MapPosition mapPosition) {
        synchronized (this.allowSwap) {
            this.lmMapPosition = mapPosition;
            this.lmBitmap.release();
            this.allowSwap.enable();
        }
    }

    public Bitmap getDrawingBitmap() {
        Bitmap lock;
        synchronized (this.allowSwap) {
            this.allowSwap.waitDisabled();
            lock = this.lmBitmap.lock();
            if (lock != null) {
                lock.setBackgroundColor(this.displayModel.getBackgroundColor());
            }
        }
        return lock;
    }

    public void setDimension(Dimension dimension) {
        synchronized (this.matrix) {
            if (this.dimension == null || !this.dimension.equals(dimension)) {
                this.dimension = dimension;
                synchronized (this.allowSwap) {
                    this.odBitmap.create(this.graphicFactory, dimension, this.displayModel.getBackgroundColor(), false);
                    this.lmBitmap.create(this.graphicFactory, dimension, this.displayModel.getBackgroundColor(), false);
                }
            }
        }
    }

    private void swapBitmaps() {
        synchronized (this.allowSwap) {
            if (this.allowSwap.isEnabled()) {
                FrameBufferBitmap.swap(this.odBitmap, this.lmBitmap);
                this.frameBufferModel.setMapPosition(this.lmMapPosition);
                this.allowSwap.disable();
            }
        }
    }
}
