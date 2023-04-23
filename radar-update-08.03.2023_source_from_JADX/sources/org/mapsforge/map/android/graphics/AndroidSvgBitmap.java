package org.mapsforge.map.android.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.RectF;
import android.util.Pair;
import com.caverock.androidsvg.SVG;
import java.io.IOException;
import java.io.InputStream;
import org.mapsforge.core.graphics.GraphicUtils;

public class AndroidSvgBitmap extends AndroidResourceBitmap {
    public static float DEFAULT_SIZE = 400.0f;

    public static Bitmap getResourceBitmap(InputStream inputStream, float f, float f2, int i, int i2, int i3) throws IOException {
        try {
            Picture renderToPicture = SVG.getFromInputStream(inputStream).renderToPicture();
            float[] imageSize = GraphicUtils.imageSize((float) renderToPicture.getWidth(), (float) renderToPicture.getHeight(), (float) (((double) f) / Math.sqrt((double) (((float) (renderToPicture.getHeight() * renderToPicture.getWidth())) / f2))), i, i2, i3);
            Bitmap createBitmap = Bitmap.createBitmap((int) Math.ceil((double) imageSize[0]), (int) Math.ceil((double) imageSize[1]), AndroidGraphicFactory.TRANSPARENT_BITMAP);
            new Canvas(createBitmap).drawPicture(renderToPicture, new RectF(0.0f, 0.0f, imageSize[0], imageSize[1]));
            return createBitmap;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private static Bitmap getResourceBitmapImpl(InputStream inputStream, int i, float f, int i2, int i3, int i4) throws IOException {
        synchronized (RESOURCE_BITMAPS) {
            Pair pair = (Pair) RESOURCE_BITMAPS.get(Integer.valueOf(i));
            if (pair != null) {
                RESOURCE_BITMAPS.put(Integer.valueOf(i), new Pair(pair.first, Integer.valueOf(((Integer) pair.second).intValue() + 1)));
                Bitmap bitmap = (Bitmap) pair.first;
                return bitmap;
            }
            Bitmap bitmap2 = AndroidSvgBitmapStore.get(i);
            if (bitmap2 == null) {
                bitmap2 = getResourceBitmap(inputStream, f, DEFAULT_SIZE, i2, i3, i4);
                AndroidSvgBitmapStore.put(i, bitmap2);
            }
            RESOURCE_BITMAPS.put(Integer.valueOf(i), new Pair(bitmap2, 1));
            return bitmap2;
        }
    }

    public AndroidSvgBitmap(InputStream inputStream, int i, float f, int i2, int i3, int i4) throws IOException {
        super(i);
        this.bitmap = getResourceBitmapImpl(inputStream, i, f, i2, i3, i4);
    }
}
