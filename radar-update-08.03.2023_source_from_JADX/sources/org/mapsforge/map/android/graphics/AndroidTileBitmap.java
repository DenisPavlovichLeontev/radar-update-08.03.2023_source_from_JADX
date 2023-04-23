package org.mapsforge.map.android.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import org.mapsforge.core.graphics.CorruptedInputStreamException;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.util.IOUtils;

public class AndroidTileBitmap extends AndroidBitmap implements TileBitmap {
    private static final Logger LOGGER = Logger.getLogger(AndroidTileBitmap.class.getName());
    private static Map<Integer, Set<SoftReference<Bitmap>>> reusableTileBitmaps = new HashMap();
    private static AtomicInteger tileInstances;
    private long expiration = 0;
    private long timestamp = System.currentTimeMillis();

    private static int composeHash(int i, boolean z) {
        return z ? i + 268435456 : i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0046, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.graphics.Bitmap getTileBitmapFromReusableSet(int r4, boolean r5) {
        /*
            int r4 = composeHash(r4, r5)
            java.util.Map<java.lang.Integer, java.util.Set<java.lang.ref.SoftReference<android.graphics.Bitmap>>> r0 = reusableTileBitmaps
            monitor-enter(r0)
            java.util.Map<java.lang.Integer, java.util.Set<java.lang.ref.SoftReference<android.graphics.Bitmap>>> r1 = reusableTileBitmaps     // Catch:{ all -> 0x0047 }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x0047 }
            java.lang.Object r4 = r1.get(r4)     // Catch:{ all -> 0x0047 }
            java.util.Set r4 = (java.util.Set) r4     // Catch:{ all -> 0x0047 }
            r1 = 0
            if (r4 != 0) goto L_0x0018
            monitor-exit(r0)     // Catch:{ all -> 0x0047 }
            return r1
        L_0x0018:
            java.util.Iterator r4 = r4.iterator()     // Catch:{ all -> 0x0047 }
        L_0x001c:
            boolean r2 = r4.hasNext()     // Catch:{ all -> 0x0047 }
            if (r2 == 0) goto L_0x0045
            java.lang.Object r2 = r4.next()     // Catch:{ all -> 0x0047 }
            java.lang.ref.SoftReference r2 = (java.lang.ref.SoftReference) r2     // Catch:{ all -> 0x0047 }
            java.lang.Object r2 = r2.get()     // Catch:{ all -> 0x0047 }
            android.graphics.Bitmap r2 = (android.graphics.Bitmap) r2     // Catch:{ all -> 0x0047 }
            if (r2 == 0) goto L_0x0041
            boolean r3 = r2.isMutable()     // Catch:{ all -> 0x0047 }
            if (r3 == 0) goto L_0x0041
            if (r5 == 0) goto L_0x003c
            r5 = 0
            r2.eraseColor(r5)     // Catch:{ all -> 0x0047 }
        L_0x003c:
            r4.remove()     // Catch:{ all -> 0x0047 }
            r1 = r2
            goto L_0x0045
        L_0x0041:
            r4.remove()     // Catch:{ all -> 0x0047 }
            goto L_0x001c
        L_0x0045:
            monitor-exit(r0)     // Catch:{ all -> 0x0047 }
            return r1
        L_0x0047:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0047 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.android.graphics.AndroidTileBitmap.getTileBitmapFromReusableSet(int, boolean):android.graphics.Bitmap");
    }

    AndroidTileBitmap(InputStream inputStream, int i, boolean z) {
        try {
            this.bitmap = BitmapFactory.decodeStream(inputStream, (Rect) null, createTileBitmapFactoryOptions(i, z));
            this.bitmap.getWidth();
        } catch (Exception e) {
            Logger logger = LOGGER;
            logger.info("TILEBITMAP ERROR " + e.toString());
            this.bitmap = null;
            IOUtils.closeQuietly(inputStream);
            destroy();
            throw new CorruptedInputStreamException("Corrupted bitmap input stream", e);
        }
    }

    AndroidTileBitmap(int i, boolean z) {
        if (Build.VERSION.SDK_INT >= 11) {
            this.bitmap = getTileBitmapFromReusableSet(i, z);
        }
        if (this.bitmap == null) {
            this.bitmap = AndroidBitmap.createAndroidBitmap(i, i, z ? AndroidGraphicFactory.TRANSPARENT_BITMAP : AndroidGraphicFactory.NON_TRANSPARENT_BITMAP);
        }
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public boolean isExpired() {
        long j = this.expiration;
        if (j != 0 && j <= System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    public void setExpiration(long j) {
        this.expiration = j;
    }

    public void setTimestamp(long j) {
        this.timestamp = j;
    }

    /* access modifiers changed from: protected */
    public void destroy() {
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void destroyBitmap() {
        if (this.bitmap != null) {
            if (Build.VERSION.SDK_INT >= 11) {
                int height = getHeight();
                synchronized (reusableTileBitmaps) {
                    int composeHash = composeHash(height, this.bitmap.hasAlpha());
                    if (!reusableTileBitmaps.containsKey(Integer.valueOf(composeHash))) {
                        reusableTileBitmaps.put(Integer.valueOf(composeHash), new HashSet());
                    }
                    reusableTileBitmaps.get(Integer.valueOf(composeHash)).add(new SoftReference(this.bitmap));
                }
            } else {
                this.bitmap.recycle();
            }
            this.bitmap = null;
        }
    }

    private BitmapFactory.Options createTileBitmapFactoryOptions(int i, boolean z) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (z) {
            options.inPreferredConfig = AndroidGraphicFactory.TRANSPARENT_BITMAP;
        } else {
            options.inPreferredConfig = AndroidGraphicFactory.NON_TRANSPARENT_BITMAP;
        }
        if (Build.VERSION.SDK_INT >= 11 && getTileBitmapFromReusableSet(i, z) != null) {
            options.inMutable = true;
            options.inSampleSize = 1;
            options.inBitmap = getTileBitmapFromReusableSet(i, z);
        }
        return options;
    }
}
