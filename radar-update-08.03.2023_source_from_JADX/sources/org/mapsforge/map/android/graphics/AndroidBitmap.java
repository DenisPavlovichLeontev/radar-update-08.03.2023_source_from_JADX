package org.mapsforge.map.android.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import org.mapsforge.core.graphics.Bitmap;

public class AndroidBitmap implements Bitmap {
    private static final AtomicInteger BITMAP_INSTANCES = null;
    private static final List<AndroidBitmap> BITMAP_LIST = null;
    private static final Logger LOGGER = Logger.getLogger(AndroidBitmap.class.getName());
    private static final Set<SoftReference<android.graphics.Bitmap>> REUSABLE_BITMAPS = new HashSet();
    protected android.graphics.Bitmap bitmap;
    private AtomicInteger refCount;

    protected static android.graphics.Bitmap createAndroidBitmap(int i, int i2, Bitmap.Config config) {
        return android.graphics.Bitmap.createBitmap(i, i2, config);
    }

    protected static final BitmapFactory.Options createBitmapFactoryOptions(Bitmap.Config config) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = config;
        return options;
    }

    protected AndroidBitmap() {
        this.refCount = new AtomicInteger();
    }

    public AndroidBitmap(android.graphics.Bitmap bitmap2) {
        this();
        if (!bitmap2.isRecycled()) {
            this.bitmap = bitmap2;
            return;
        }
        throw new IllegalArgumentException("bitmap is already recycled");
    }

    AndroidBitmap(int i, int i2, Bitmap.Config config) {
        this();
        android.graphics.Bitmap bitmapFromReusableSet = getBitmapFromReusableSet(i, i2, config);
        this.bitmap = bitmapFromReusableSet;
        if (bitmapFromReusableSet == null) {
            this.bitmap = createAndroidBitmap(i, i2, config);
        }
    }

    public void compress(OutputStream outputStream) throws IOException {
        if (!this.bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)) {
            throw new IOException("Failed to write bitmap to output stream");
        }
    }

    public void decrementRefCount() {
        if (this.refCount.decrementAndGet() < 0) {
            destroy();
        }
    }

    public int getHeight() {
        return this.bitmap.getHeight();
    }

    public int getWidth() {
        return this.bitmap.getWidth();
    }

    public void incrementRefCount() {
        this.refCount.incrementAndGet();
    }

    public boolean isDestroyed() {
        return this.bitmap == null;
    }

    public void scaleTo(int i, int i2) {
        if (getWidth() != i || getHeight() != i2) {
            android.graphics.Bitmap createScaledBitmap = android.graphics.Bitmap.createScaledBitmap(this.bitmap, i, i2, true);
            destroy();
            this.bitmap = createScaledBitmap;
        }
    }

    public void setBackgroundColor(int i) {
        this.bitmap.eraseColor(i);
    }

    public String toString() {
        android.graphics.Bitmap bitmap2 = this.bitmap;
        String str = bitmap2 != null ? bitmap2.hasAlpha() ? " has alpha" : " no alpha" : " is recycled";
        return super.toString() + " rC " + Integer.toString(this.refCount.get()) + str;
    }

    /* access modifiers changed from: protected */
    public final boolean canUseBitmap(android.graphics.Bitmap bitmap2, int i, int i2) {
        return bitmap2.getWidth() == i && bitmap2.getHeight() == i2;
    }

    /* access modifiers changed from: protected */
    public void destroy() {
        destroyBitmap();
    }

    /* access modifiers changed from: protected */
    public void destroyBitmap() {
        if (this.bitmap != null) {
            if (Build.VERSION.SDK_INT >= 11) {
                Set<SoftReference<android.graphics.Bitmap>> set = REUSABLE_BITMAPS;
                synchronized (set) {
                    set.add(new SoftReference(this.bitmap));
                }
            } else {
                this.bitmap.recycle();
            }
            this.bitmap = null;
        }
    }

    /* access modifiers changed from: protected */
    public final android.graphics.Bitmap getBitmapFromReusableSet(int i, int i2, Bitmap.Config config) {
        Set<SoftReference<android.graphics.Bitmap>> set = REUSABLE_BITMAPS;
        android.graphics.Bitmap bitmap2 = null;
        if (set != null && !set.isEmpty()) {
            synchronized (set) {
                Iterator<SoftReference<android.graphics.Bitmap>> it = set.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    android.graphics.Bitmap bitmap3 = (android.graphics.Bitmap) it.next().get();
                    if (bitmap3 == null || !bitmap3.isMutable()) {
                        it.remove();
                    } else if (canUseBitmap(bitmap3, i, i2)) {
                        it.remove();
                        bitmap2 = bitmap3;
                        break;
                    }
                }
            }
        }
        return bitmap2;
    }
}
