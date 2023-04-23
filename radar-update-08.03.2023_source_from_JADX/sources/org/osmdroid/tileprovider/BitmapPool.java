package org.osmdroid.tileprovider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.osmdroid.api.IMapView;
import org.osmdroid.tileprovider.modules.ConfigurablePriorityThreadFactory;

public class BitmapPool {
    private static final BitmapPool sInstance = new BitmapPool();
    private final ExecutorService mExecutor = Executors.newFixedThreadPool(1, new ConfigurablePriorityThreadFactory(1, getClass().getName()));
    private final LinkedList<Bitmap> mPool = new LinkedList<>();

    private BitmapPool() {
    }

    public static BitmapPool getInstance() {
        return sInstance;
    }

    public void returnDrawableToPool(ReusableBitmapDrawable reusableBitmapDrawable) {
        Bitmap tryRecycle = reusableBitmapDrawable.tryRecycle();
        if (tryRecycle != null && !tryRecycle.isRecycled() && tryRecycle.isMutable() && tryRecycle.getConfig() != null) {
            synchronized (this.mPool) {
                this.mPool.addLast(tryRecycle);
            }
        } else if (tryRecycle != null) {
            Log.d(IMapView.LOGTAG, "Rejected bitmap from being added to BitmapPool.");
        }
    }

    @Deprecated
    public void applyReusableOptions(BitmapFactory.Options options) {
        if (Build.VERSION.SDK_INT >= 11) {
            options.inBitmap = null;
            options.inSampleSize = 1;
            options.inMutable = true;
        }
    }

    public void applyReusableOptions(BitmapFactory.Options options, int i, int i2) {
        if (Build.VERSION.SDK_INT >= 11) {
            options.inBitmap = obtainSizedBitmapFromPool(i, i2);
            options.inSampleSize = 1;
            options.inMutable = true;
        }
    }

    @Deprecated
    public Bitmap obtainBitmapFromPool() {
        synchronized (this.mPool) {
            if (this.mPool.isEmpty()) {
                return null;
            }
            Bitmap removeFirst = this.mPool.removeFirst();
            if (!removeFirst.isRecycled()) {
                return removeFirst;
            }
            Bitmap obtainBitmapFromPool = obtainBitmapFromPool();
            return obtainBitmapFromPool;
        }
    }

    public Bitmap obtainSizedBitmapFromPool(int i, int i2) {
        synchronized (this.mPool) {
            if (this.mPool.isEmpty()) {
                return null;
            }
            Iterator it = this.mPool.iterator();
            while (it.hasNext()) {
                Bitmap bitmap = (Bitmap) it.next();
                if (bitmap.isRecycled()) {
                    this.mPool.remove(bitmap);
                    Bitmap obtainSizedBitmapFromPool = obtainSizedBitmapFromPool(i, i2);
                    return obtainSizedBitmapFromPool;
                } else if (bitmap.getWidth() == i && bitmap.getHeight() == i2) {
                    this.mPool.remove(bitmap);
                    return bitmap;
                }
            }
            return null;
        }
    }

    public void clearBitmapPool() {
        synchronized (sInstance.mPool) {
            while (true) {
                BitmapPool bitmapPool = sInstance;
                if (!bitmapPool.mPool.isEmpty()) {
                    bitmapPool.mPool.remove().recycle();
                }
            }
        }
    }

    public void asyncRecycle(final Drawable drawable) {
        if (drawable != null) {
            this.mExecutor.execute(new Runnable() {
                public void run() {
                    BitmapPool.this.syncRecycle(drawable);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void syncRecycle(Drawable drawable) {
        Bitmap bitmap;
        if (drawable != null) {
            if (Build.VERSION.SDK_INT <= 10 && (drawable instanceof BitmapDrawable) && (bitmap = ((BitmapDrawable) drawable).getBitmap()) != null) {
                bitmap.recycle();
            }
            if (drawable instanceof ReusableBitmapDrawable) {
                returnDrawableToPool((ReusableBitmapDrawable) drawable);
            }
        }
    }
}
