package org.mapsforge.map.android.rotation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;

public class SmoothCanvas extends Canvas {
    Canvas delegate;
    private final Paint smooth = new Paint(2);

    public void setBitmap(Bitmap bitmap) {
        this.delegate.setBitmap(bitmap);
    }

    public boolean isOpaque() {
        return this.delegate.isOpaque();
    }

    public int getWidth() {
        return this.delegate.getWidth();
    }

    public int getHeight() {
        return this.delegate.getHeight();
    }

    public int save() {
        return this.delegate.save();
    }

    public int saveLayer(RectF rectF, Paint paint, int i) {
        return this.delegate.saveLayer(rectF, paint, i);
    }

    public int saveLayer(float f, float f2, float f3, float f4, Paint paint, int i) {
        return this.delegate.saveLayer(f, f2, f3, f4, paint, i);
    }

    public int saveLayerAlpha(RectF rectF, int i, int i2) {
        return this.delegate.saveLayerAlpha(rectF, i, i2);
    }

    public int saveLayerAlpha(float f, float f2, float f3, float f4, int i, int i2) {
        return this.delegate.saveLayerAlpha(f, f2, f3, f4, i, i2);
    }

    public void restore() {
        this.delegate.restore();
    }

    public int getSaveCount() {
        return this.delegate.getSaveCount();
    }

    public void restoreToCount(int i) {
        this.delegate.restoreToCount(i);
    }

    public void translate(float f, float f2) {
        this.delegate.translate(f, f2);
    }

    public void scale(float f, float f2) {
        this.delegate.scale(f, f2);
    }

    public void rotate(float f) {
        this.delegate.rotate(f);
    }

    public void skew(float f, float f2) {
        this.delegate.skew(f, f2);
    }

    public void concat(Matrix matrix) {
        this.delegate.concat(matrix);
    }

    public void setMatrix(Matrix matrix) {
        this.delegate.setMatrix(matrix);
    }

    public void getMatrix(Matrix matrix) {
        this.delegate.getMatrix(matrix);
    }

    public boolean clipRect(RectF rectF, Region.Op op) {
        return this.delegate.clipRect(rectF, op);
    }

    public boolean clipRect(Rect rect, Region.Op op) {
        return this.delegate.clipRect(rect, op);
    }

    public boolean clipRect(RectF rectF) {
        return this.delegate.clipRect(rectF);
    }

    public boolean clipRect(Rect rect) {
        return this.delegate.clipRect(rect);
    }

    public boolean clipRect(float f, float f2, float f3, float f4, Region.Op op) {
        return this.delegate.clipRect(f, f2, f3, f4, op);
    }

    public boolean clipRect(float f, float f2, float f3, float f4) {
        return this.delegate.clipRect(f, f2, f3, f4);
    }

    public boolean clipRect(int i, int i2, int i3, int i4) {
        return this.delegate.clipRect(i, i2, i3, i4);
    }

    public boolean clipPath(Path path, Region.Op op) {
        return this.delegate.clipPath(path, op);
    }

    public boolean clipPath(Path path) {
        return this.delegate.clipPath(path);
    }

    public DrawFilter getDrawFilter() {
        return this.delegate.getDrawFilter();
    }

    public void setDrawFilter(DrawFilter drawFilter) {
        this.delegate.setDrawFilter(drawFilter);
    }

    public boolean quickReject(RectF rectF, Canvas.EdgeType edgeType) {
        return this.delegate.quickReject(rectF, edgeType);
    }

    public boolean quickReject(Path path, Canvas.EdgeType edgeType) {
        return this.delegate.quickReject(path, edgeType);
    }

    public boolean quickReject(float f, float f2, float f3, float f4, Canvas.EdgeType edgeType) {
        return this.delegate.quickReject(f, f2, f3, f4, edgeType);
    }

    public boolean getClipBounds(Rect rect) {
        return this.delegate.getClipBounds(rect);
    }

    public void drawRGB(int i, int i2, int i3) {
        this.delegate.drawRGB(i, i2, i3);
    }

    public void drawARGB(int i, int i2, int i3, int i4) {
        this.delegate.drawARGB(i, i2, i3, i4);
    }

    public void drawColor(int i) {
        this.delegate.drawColor(i);
    }

    public void drawColor(int i, PorterDuff.Mode mode) {
        this.delegate.drawColor(i, mode);
    }

    public void drawPaint(Paint paint) {
        this.delegate.drawPaint(paint);
    }

    public void drawPoints(float[] fArr, int i, int i2, Paint paint) {
        this.delegate.drawPoints(fArr, i, i2, paint);
    }

    public void drawPoints(float[] fArr, Paint paint) {
        this.delegate.drawPoints(fArr, paint);
    }

    public void drawPoint(float f, float f2, Paint paint) {
        this.delegate.drawPoint(f, f2, paint);
    }

    public void drawLine(float f, float f2, float f3, float f4, Paint paint) {
        this.delegate.drawLine(f, f2, f3, f4, paint);
    }

    public void drawLines(float[] fArr, int i, int i2, Paint paint) {
        this.delegate.drawLines(fArr, i, i2, paint);
    }

    public void drawLines(float[] fArr, Paint paint) {
        this.delegate.drawLines(fArr, paint);
    }

    public void drawRect(RectF rectF, Paint paint) {
        this.delegate.drawRect(rectF, paint);
    }

    public void drawRect(Rect rect, Paint paint) {
        this.delegate.drawRect(rect, paint);
    }

    public void drawRect(float f, float f2, float f3, float f4, Paint paint) {
        this.delegate.drawRect(f, f2, f3, f4, paint);
    }

    public void drawOval(RectF rectF, Paint paint) {
        this.delegate.drawOval(rectF, paint);
    }

    public void drawCircle(float f, float f2, float f3, Paint paint) {
        this.delegate.drawCircle(f, f2, f3, paint);
    }

    public void drawArc(RectF rectF, float f, float f2, boolean z, Paint paint) {
        this.delegate.drawArc(rectF, f, f2, z, paint);
    }

    public void drawRoundRect(RectF rectF, float f, float f2, Paint paint) {
        this.delegate.drawRoundRect(rectF, f, f2, paint);
    }

    public void drawPath(Path path, Paint paint) {
        this.delegate.drawPath(path, paint);
    }

    public void drawBitmap(Bitmap bitmap, float f, float f2, Paint paint) {
        if (paint == null) {
            paint = this.smooth;
        } else {
            paint.setFilterBitmap(true);
        }
        this.delegate.drawBitmap(bitmap, f, f2, paint);
    }

    public void drawBitmap(Bitmap bitmap, Rect rect, RectF rectF, Paint paint) {
        if (paint == null) {
            paint = this.smooth;
        } else {
            paint.setFilterBitmap(true);
        }
        this.delegate.drawBitmap(bitmap, rect, rectF, paint);
    }

    public void drawBitmap(Bitmap bitmap, Rect rect, Rect rect2, Paint paint) {
        if (paint == null) {
            paint = this.smooth;
        } else {
            paint.setFilterBitmap(true);
        }
        this.delegate.drawBitmap(bitmap, rect, rect2, paint);
    }

    public void drawBitmap(int[] iArr, int i, int i2, int i3, int i4, int i5, int i6, boolean z, Paint paint) {
        Paint paint2 = paint;
        if (paint2 == null) {
            paint2 = this.smooth;
        } else {
            paint2.setFilterBitmap(true);
        }
        this.delegate.drawBitmap(iArr, i, i2, i3, i4, i5, i6, z, paint2);
    }

    public void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint) {
        if (paint == null) {
            paint = this.smooth;
        } else {
            paint.setFilterBitmap(true);
        }
        this.delegate.drawBitmap(bitmap, matrix, paint);
    }

    public void drawBitmapMesh(Bitmap bitmap, int i, int i2, float[] fArr, int i3, int[] iArr, int i4, Paint paint) {
        this.delegate.drawBitmapMesh(bitmap, i, i2, fArr, i3, iArr, i4, paint);
    }

    public void drawVertices(Canvas.VertexMode vertexMode, int i, float[] fArr, int i2, float[] fArr2, int i3, int[] iArr, int i4, short[] sArr, int i5, int i6, Paint paint) {
        this.delegate.drawVertices(vertexMode, i, fArr, i2, fArr2, i3, iArr, i4, sArr, i5, i6, paint);
    }

    public void drawText(char[] cArr, int i, int i2, float f, float f2, Paint paint) {
        this.delegate.drawText(cArr, i, i2, f, f2, paint);
    }

    public void drawText(String str, float f, float f2, Paint paint) {
        this.delegate.drawText(str, f, f2, paint);
    }

    public void drawText(String str, int i, int i2, float f, float f2, Paint paint) {
        this.delegate.drawText(str, i, i2, f, f2, paint);
    }

    public void drawText(CharSequence charSequence, int i, int i2, float f, float f2, Paint paint) {
        this.delegate.drawText(charSequence, i, i2, f, f2, paint);
    }

    public void drawPosText(char[] cArr, int i, int i2, float[] fArr, Paint paint) {
        this.delegate.drawPosText(cArr, i, i2, fArr, paint);
    }

    public void drawPosText(String str, float[] fArr, Paint paint) {
        this.delegate.drawPosText(str, fArr, paint);
    }

    public void drawTextOnPath(char[] cArr, int i, int i2, Path path, float f, float f2, Paint paint) {
        this.delegate.drawTextOnPath(cArr, i, i2, path, f, f2, paint);
    }

    public void drawTextOnPath(String str, Path path, float f, float f2, Paint paint) {
        this.delegate.drawTextOnPath(str, path, f, f2, paint);
    }

    public void drawPicture(Picture picture) {
        this.delegate.drawPicture(picture);
    }

    public void drawPicture(Picture picture, RectF rectF) {
        this.delegate.drawPicture(picture, rectF);
    }

    public void drawPicture(Picture picture, Rect rect) {
        this.delegate.drawPicture(picture, rect);
    }
}
