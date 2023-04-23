package org.mapsforge.map.view;

import java.util.logging.Logger;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.Dimension;

class FrameBufferBitmap {
    /* access modifiers changed from: private */
    public static final Logger LOGGER = Logger.getLogger(FrameBufferBitmap.class.getName());
    private Bitmap bitmap = null;
    private BitmapRequest bitmapRequest = null;
    private final Object bitmapRequestSync = new Object();
    private final Lock frameLock = new Lock();

    FrameBufferBitmap() {
    }

    private static class BitmapRequest {
        private final int color;
        private final Dimension dimension;
        private final GraphicFactory factory;
        private final boolean isTransparent;

        BitmapRequest(GraphicFactory graphicFactory, Dimension dimension2, int i, boolean z) {
            this.factory = graphicFactory;
            this.dimension = dimension2;
            this.color = i;
            this.isTransparent = z;
        }

        /* access modifiers changed from: package-private */
        public Bitmap create() {
            if (this.dimension.width <= 0 || this.dimension.height <= 0) {
                return null;
            }
            Bitmap createBitmap = this.factory.createBitmap(this.dimension.width, this.dimension.height, this.isTransparent);
            createBitmap.setBackgroundColor(this.color);
            return createBitmap;
        }
    }

    static class Lock {
        private boolean enabled = false;

        Lock() {
        }

        /* access modifiers changed from: package-private */
        public synchronized void disable() {
            this.enabled = false;
            notifyAll();
        }

        /* access modifiers changed from: package-private */
        public synchronized void enable() {
            this.enabled = true;
        }

        /* access modifiers changed from: package-private */
        public boolean isEnabled() {
            return this.enabled;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            org.mapsforge.map.view.FrameBufferBitmap.access$000().warning("Frame buffer interrupted");
         */
        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x000b */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized void waitDisabled() {
            /*
                r2 = this;
                monitor-enter(r2)
            L_0x0001:
                boolean r0 = r2.enabled     // Catch:{ InterruptedException -> 0x000b }
                if (r0 == 0) goto L_0x0014
                r2.wait()     // Catch:{ InterruptedException -> 0x000b }
                goto L_0x0001
            L_0x0009:
                r0 = move-exception
                goto L_0x0016
            L_0x000b:
                java.util.logging.Logger r0 = org.mapsforge.map.view.FrameBufferBitmap.LOGGER     // Catch:{ all -> 0x0009 }
                java.lang.String r1 = "Frame buffer interrupted"
                r0.warning(r1)     // Catch:{ all -> 0x0009 }
            L_0x0014:
                monitor-exit(r2)
                return
            L_0x0016:
                monitor-exit(r2)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.view.FrameBufferBitmap.Lock.waitDisabled():void");
        }
    }

    /* access modifiers changed from: package-private */
    public void create(GraphicFactory graphicFactory, Dimension dimension, int i, boolean z) {
        synchronized (this.bitmapRequestSync) {
            this.bitmapRequest = new BitmapRequest(graphicFactory, dimension, i, z);
        }
    }

    private void createBitmapIfRequested() {
        synchronized (this.bitmapRequestSync) {
            if (this.bitmapRequest != null) {
                destroyBitmap();
                this.bitmap = this.bitmapRequest.create();
                this.bitmapRequest = null;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void destroy() {
        synchronized (this.frameLock) {
            if (this.bitmap != null) {
                this.frameLock.waitDisabled();
                destroyBitmap();
            }
        }
    }

    private void destroyBitmap() {
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null) {
            bitmap2.decrementRefCount();
            this.bitmap = null;
        }
    }

    /* access modifiers changed from: package-private */
    public Bitmap lock() {
        Bitmap bitmap2;
        synchronized (this.frameLock) {
            createBitmapIfRequested();
            if (this.bitmap != null) {
                this.frameLock.enable();
            }
            bitmap2 = this.bitmap;
        }
        return bitmap2;
    }

    /* access modifiers changed from: package-private */
    public void release() {
        synchronized (this.frameLock) {
            this.frameLock.disable();
        }
    }

    static void swap(FrameBufferBitmap frameBufferBitmap, FrameBufferBitmap frameBufferBitmap2) {
        Bitmap bitmap2 = frameBufferBitmap.bitmap;
        frameBufferBitmap.bitmap = frameBufferBitmap2.bitmap;
        frameBufferBitmap2.bitmap = bitmap2;
        BitmapRequest bitmapRequest2 = frameBufferBitmap.bitmapRequest;
        frameBufferBitmap.bitmapRequest = frameBufferBitmap2.bitmapRequest;
        frameBufferBitmap2.bitmapRequest = bitmapRequest2;
    }
}
