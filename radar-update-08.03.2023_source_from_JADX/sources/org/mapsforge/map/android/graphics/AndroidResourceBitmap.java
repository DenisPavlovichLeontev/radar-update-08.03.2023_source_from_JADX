package org.mapsforge.map.android.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Pair;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import org.mapsforge.core.graphics.GraphicUtils;
import org.mapsforge.core.graphics.ResourceBitmap;

public class AndroidResourceBitmap extends AndroidBitmap implements ResourceBitmap {
    protected static final Logger LOGGER = Logger.getLogger(AndroidResourceBitmap.class.getName());
    protected static final Map<Integer, Pair<Bitmap, Integer>> RESOURCE_BITMAPS = new HashMap();
    protected static Set<Integer> rBitmaps;
    protected static AtomicInteger rInstances;
    private final int hash;

    private static boolean removeBitmap(int i) {
        return false;
    }

    public static void clearResourceBitmaps() {
        Map<Integer, Pair<Bitmap, Integer>> map = RESOURCE_BITMAPS;
        synchronized (map) {
            for (Pair<Bitmap, Integer> pair : map.values()) {
                ((Bitmap) pair.first).recycle();
            }
            RESOURCE_BITMAPS.clear();
        }
    }

    private static Bitmap getResourceBitmap(InputStream inputStream, float f, int i, int i2, int i3, int i4) throws IOException {
        Map<Integer, Pair<Bitmap, Integer>> map = RESOURCE_BITMAPS;
        synchronized (map) {
            Pair pair = map.get(Integer.valueOf(i4));
            if (pair != null) {
                map.put(Integer.valueOf(i4), new Pair(pair.first, Integer.valueOf(((Integer) pair.second).intValue() + 1)));
                Bitmap bitmap = (Bitmap) pair.first;
                return bitmap;
            }
            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, (Rect) null, createBitmapFactoryOptions(AndroidGraphicFactory.TRANSPARENT_BITMAP));
            if (decodeStream != null) {
                float[] imageSize = GraphicUtils.imageSize((float) decodeStream.getWidth(), (float) decodeStream.getHeight(), f, i, i2, i3);
                if (!(((int) imageSize[0]) == decodeStream.getWidth() && ((int) imageSize[1]) == decodeStream.getHeight())) {
                    decodeStream = Bitmap.createScaledBitmap(decodeStream, (int) imageSize[0], (int) imageSize[1], true);
                }
                map.put(Integer.valueOf(i4), new Pair(decodeStream, 1));
                return decodeStream;
            }
            throw new IOException("BitmapFactory failed to decodeStream");
        }
    }

    protected AndroidResourceBitmap(int i) {
        this.hash = i;
    }

    AndroidResourceBitmap(InputStream inputStream, float f, int i, int i2, int i3, int i4) throws IOException {
        this(i4);
        this.bitmap = getResourceBitmap(inputStream, f, i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public void destroyBitmap() {
        if (this.bitmap != null) {
            if (removeBitmap(this.hash)) {
                this.bitmap.recycle();
            }
            this.bitmap = null;
        }
    }
}
