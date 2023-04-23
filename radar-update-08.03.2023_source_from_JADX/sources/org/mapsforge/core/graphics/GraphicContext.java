package org.mapsforge.core.graphics;

import org.mapsforge.core.model.Rectangle;

public interface GraphicContext {
    void drawBitmap(Bitmap bitmap, int i, int i2);

    void drawBitmap(Bitmap bitmap, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    void drawBitmap(Bitmap bitmap, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Filter filter);

    void drawBitmap(Bitmap bitmap, int i, int i2, Filter filter);

    void drawBitmap(Bitmap bitmap, Matrix matrix);

    void drawBitmap(Bitmap bitmap, Matrix matrix, Filter filter);

    void drawCircle(int i, int i2, int i3, Paint paint);

    void drawLine(int i, int i2, int i3, int i4, Paint paint);

    void drawPath(Path path, Paint paint);

    void drawText(String str, int i, int i2, Paint paint);

    void drawTextRotated(String str, int i, int i2, int i3, int i4, Paint paint);

    void fillColor(int i);

    void fillColor(Color color);

    boolean isAntiAlias();

    boolean isFilterBitmap();

    void resetClip();

    void setAntiAlias(boolean z);

    void setClip(int i, int i2, int i3, int i4);

    void setClipDifference(int i, int i2, int i3, int i4);

    void setFilterBitmap(boolean z);

    void shadeBitmap(Bitmap bitmap, Rectangle rectangle, Rectangle rectangle2, float f);
}
