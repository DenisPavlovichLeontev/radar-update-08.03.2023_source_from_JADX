package org.mapsforge.map.android.graphics;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;
import kotlin.jvm.internal.ByteCompanionObject;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Path;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.Rectangle;

class AndroidCanvas implements Canvas {
    private static final float[] INVERT_MATRIX = {-1.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, -1.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, -1.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    private final Paint bitmapPaint;
    android.graphics.Canvas canvas;
    private ColorFilter grayscaleFilter;
    private ColorFilter grayscaleInvertFilter;
    private HilshadingTemps hillshadingTemps;
    private ColorFilter invertFilter;

    AndroidCanvas() {
        Paint paint = new Paint();
        this.bitmapPaint = paint;
        this.hillshadingTemps = null;
        this.canvas = new android.graphics.Canvas();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        createFilters();
    }

    AndroidCanvas(android.graphics.Canvas canvas2) {
        this.bitmapPaint = new Paint();
        this.hillshadingTemps = null;
        this.canvas = canvas2;
        createFilters();
    }

    private void applyFilter(Filter filter) {
        if (filter != Filter.NONE) {
            int i = C13091.$SwitchMap$org$mapsforge$core$graphics$Filter[filter.ordinal()];
            if (i == 1) {
                this.bitmapPaint.setColorFilter(this.grayscaleFilter);
            } else if (i == 2) {
                this.bitmapPaint.setColorFilter(this.grayscaleInvertFilter);
            } else if (i == 3) {
                this.bitmapPaint.setColorFilter(this.invertFilter);
            }
        }
    }

    /* renamed from: org.mapsforge.map.android.graphics.AndroidCanvas$1 */
    static /* synthetic */ class C13091 {
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$core$graphics$Filter;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                org.mapsforge.core.graphics.Filter[] r0 = org.mapsforge.core.graphics.Filter.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$mapsforge$core$graphics$Filter = r0
                org.mapsforge.core.graphics.Filter r1 = org.mapsforge.core.graphics.Filter.GRAYSCALE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Filter     // Catch:{ NoSuchFieldError -> 0x001d }
                org.mapsforge.core.graphics.Filter r1 = org.mapsforge.core.graphics.Filter.GRAYSCALE_INVERT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$org$mapsforge$core$graphics$Filter     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.mapsforge.core.graphics.Filter r1 = org.mapsforge.core.graphics.Filter.INVERT     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.android.graphics.AndroidCanvas.C13091.<clinit>():void");
        }
    }

    private void createFilters() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        this.grayscaleFilter = new ColorMatrixColorFilter(colorMatrix);
        ColorMatrix colorMatrix2 = new ColorMatrix();
        colorMatrix2.setSaturation(0.0f);
        float[] fArr = INVERT_MATRIX;
        colorMatrix2.postConcat(new ColorMatrix(fArr));
        this.grayscaleInvertFilter = new ColorMatrixColorFilter(colorMatrix2);
        this.invertFilter = new ColorMatrixColorFilter(fArr);
    }

    public void destroy() {
        this.canvas = null;
    }

    public void drawBitmap(Bitmap bitmap, int i, int i2) {
        android.graphics.Bitmap bitmap2 = AndroidGraphicFactory.getBitmap(bitmap);
        if (AndroidGraphicFactory.MONO_ALPHA_BITMAP.equals(bitmap2.getConfig())) {
            this.canvas.drawColor(Color.argb(0, 0, 0, 0), PorterDuff.Mode.SRC);
        }
        this.canvas.drawBitmap(bitmap2, (float) i, (float) i2, this.bitmapPaint);
    }

    public void drawBitmap(Bitmap bitmap, int i, int i2, Filter filter) {
        applyFilter(filter);
        this.canvas.drawBitmap(AndroidGraphicFactory.getBitmap(bitmap), (float) i, (float) i2, this.bitmapPaint);
        if (filter != Filter.NONE) {
            this.bitmapPaint.setColorFilter((ColorFilter) null);
        }
    }

    public void drawBitmap(Bitmap bitmap, Matrix matrix) {
        this.canvas.drawBitmap(AndroidGraphicFactory.getBitmap(bitmap), AndroidGraphicFactory.getMatrix(matrix), this.bitmapPaint);
    }

    public void drawBitmap(Bitmap bitmap, Matrix matrix, Filter filter) {
        applyFilter(filter);
        this.canvas.drawBitmap(AndroidGraphicFactory.getBitmap(bitmap), AndroidGraphicFactory.getMatrix(matrix), this.bitmapPaint);
        if (filter != Filter.NONE) {
            this.bitmapPaint.setColorFilter((ColorFilter) null);
        }
    }

    public void drawBitmap(Bitmap bitmap, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.canvas.drawBitmap(AndroidGraphicFactory.getBitmap(bitmap), new Rect(i, i2, i3, i4), new Rect(i5, i6, i7, i8), this.bitmapPaint);
    }

    public void drawBitmap(Bitmap bitmap, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Filter filter) {
        applyFilter(filter);
        this.canvas.drawBitmap(AndroidGraphicFactory.getBitmap(bitmap), new Rect(i, i2, i3, i4), new Rect(i5, i6, i7, i8), this.bitmapPaint);
        if (filter != Filter.NONE) {
            this.bitmapPaint.setColorFilter((ColorFilter) null);
        }
    }

    public void drawCircle(int i, int i2, int i3, org.mapsforge.core.graphics.Paint paint) {
        if (!paint.isTransparent()) {
            this.canvas.drawCircle((float) i, (float) i2, (float) i3, AndroidGraphicFactory.getPaint(paint));
        }
    }

    public void drawLine(int i, int i2, int i3, int i4, org.mapsforge.core.graphics.Paint paint) {
        if (!paint.isTransparent()) {
            this.canvas.drawLine((float) i, (float) i2, (float) i3, (float) i4, AndroidGraphicFactory.getPaint(paint));
        }
    }

    public void drawPath(Path path, org.mapsforge.core.graphics.Paint paint) {
        if (!paint.isTransparent()) {
            this.canvas.drawPath(AndroidGraphicFactory.getPath(path), AndroidGraphicFactory.getPaint(paint));
        }
    }

    public void drawText(String str, int i, int i2, org.mapsforge.core.graphics.Paint paint) {
        if (str != null && !str.trim().isEmpty() && !paint.isTransparent()) {
            this.canvas.drawText(str, (float) i, (float) i2, AndroidGraphicFactory.getPaint(paint));
        }
    }

    public void drawTextRotated(String str, int i, int i2, int i3, int i4, org.mapsforge.core.graphics.Paint paint) {
        if (str != null && !str.trim().isEmpty() && !paint.isTransparent()) {
            android.graphics.Path path = new android.graphics.Path();
            path.moveTo((float) i, (float) i2);
            path.lineTo((float) i3, (float) i4);
            this.canvas.drawTextOnPath(str, path, 0.0f, 3.0f, AndroidGraphicFactory.getPaint(paint));
        }
    }

    public void fillColor(org.mapsforge.core.graphics.Color color) {
        fillColor(AndroidGraphicFactory.getColor(color));
    }

    public void fillColor(int i) {
        this.canvas.drawColor(i, ((i >> 24) & 255) == 0 ? PorterDuff.Mode.CLEAR : PorterDuff.Mode.SRC_OVER);
    }

    public Dimension getDimension() {
        return new Dimension(getWidth(), getHeight());
    }

    public int getHeight() {
        return this.canvas.getHeight();
    }

    public int getWidth() {
        return this.canvas.getWidth();
    }

    public boolean isAntiAlias() {
        return this.bitmapPaint.isAntiAlias();
    }

    public boolean isFilterBitmap() {
        return this.bitmapPaint.isFilterBitmap();
    }

    public void resetClip() {
        if (Build.VERSION.SDK_INT >= 26) {
            this.canvas.save();
            this.canvas.clipRect(0, 0, getWidth(), getHeight());
            this.canvas.restore();
            return;
        }
        this.canvas.clipRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), Region.Op.REPLACE);
    }

    public void setAntiAlias(boolean z) {
        this.bitmapPaint.setAntiAlias(z);
    }

    public void setBitmap(Bitmap bitmap) {
        this.canvas.setBitmap(AndroidGraphicFactory.getBitmap(bitmap));
    }

    public void setClip(int i, int i2, int i3, int i4) {
        if (Build.VERSION.SDK_INT >= 26) {
            this.canvas.save();
            this.canvas.clipRect(i, i2, i3 + i, i4 + i2);
            this.canvas.restore();
            return;
        }
        setClipInternal(i, i2, i3, i4, Region.Op.REPLACE);
    }

    public void setClipDifference(int i, int i2, int i3, int i4) {
        setClipInternal(i, i2, i3, i4, Region.Op.DIFFERENCE);
    }

    public void setClipInternal(int i, int i2, int i3, int i4, Region.Op op) {
        this.canvas.clipRect((float) i, (float) i2, (float) (i + i3), (float) (i2 + i4), op);
    }

    public void setFilterBitmap(boolean z) {
        this.bitmapPaint.setFilterBitmap(z);
    }

    public void shadeBitmap(Bitmap bitmap, Rectangle rectangle, Rectangle rectangle2, float f) {
        android.graphics.Bitmap bitmap2;
        android.graphics.Bitmap bitmap3;
        Rectangle rectangle3 = rectangle;
        Rectangle rectangle4 = rectangle2;
        this.canvas.save();
        if (this.hillshadingTemps == null) {
            this.hillshadingTemps = new HilshadingTemps((C13091) null);
        }
        HilshadingTemps hilshadingTemps = this.hillshadingTemps;
        Paint useAlphaPaint = hilshadingTemps.useAlphaPaint((int) (255.0f * f));
        if (bitmap == null) {
            if (rectangle4 != null) {
                if (Build.VERSION.SDK_INT >= 26) {
                    this.canvas.save();
                    this.canvas.clipRect((float) rectangle4.left, (float) rectangle4.top, (float) rectangle4.right, (float) rectangle4.bottom);
                    this.canvas.restore();
                } else {
                    this.canvas.clipRect((float) rectangle4.left, (float) rectangle4.top, (float) rectangle4.right, (float) rectangle4.bottom, Region.Op.REPLACE);
                }
            }
            this.canvas.drawBitmap(this.hillshadingTemps.useNeutralShadingPixel(), this.hillshadingTemps.useAsr(0, 0, 1, 1), this.hillshadingTemps.useAdr(0, 0, this.canvas.getWidth(), this.canvas.getHeight()), useAlphaPaint);
            this.canvas.restore();
            return;
        }
        android.graphics.Bitmap bitmap4 = AndroidGraphicFactory.getBitmap(bitmap);
        double width = rectangle2.getWidth() / rectangle.getWidth();
        double height = rectangle2.getHeight() / rectangle.getHeight();
        if (width >= 1.0d || height >= 1.0d) {
            Paint paint = useAlphaPaint;
            double min = Math.min(rectangle3.left, (rectangle3.left - Math.floor(rectangle3.left)) + 1.0d);
            double d = width * min;
            HilshadingTemps hilshadingTemps2 = hilshadingTemps;
            double d2 = min;
            double min2 = Math.min(((double) bitmap.getWidth()) - rectangle3.right, (Math.floor(rectangle3.right) + 2.0d) - rectangle3.right);
            double d3 = d;
            int ceil = (int) Math.ceil((width * min2) + d + ((rectangle3.right - rectangle3.left) * width));
            double min3 = Math.min(rectangle3.top, (rectangle3.top - Math.floor(rectangle3.top)) + 1.0d);
            double d4 = height * min3;
            android.graphics.Bitmap bitmap5 = bitmap4;
            double d5 = min2;
            double min4 = Math.min(((double) bitmap.getHeight()) - rectangle3.bottom, (Math.floor(rectangle3.bottom) + 2.0d) - rectangle3.bottom);
            double d6 = d4;
            int ceil2 = (int) Math.ceil((height * min4) + d4 + ((rectangle3.bottom - rectangle3.top) * height));
            int round = (int) Math.round(rectangle3.left - d2);
            int round2 = (int) Math.round(rectangle3.top - min3);
            int round3 = (int) Math.round(rectangle3.right + d5);
            int round4 = (int) Math.round(rectangle3.bottom + min4);
            android.graphics.Canvas useCanvas = hilshadingTemps2.useCanvas();
            if (round == 0 && round2 == 0) {
                round3++;
                bitmap3 = android.graphics.Bitmap.createBitmap(round3, round4, bitmap5.getConfig());
                useCanvas.setBitmap(bitmap3);
                bitmap2 = bitmap5;
                useCanvas.drawBitmap(bitmap2, 1.0f, 0.0f, (Paint) null);
                round++;
            } else {
                bitmap2 = bitmap5;
                bitmap3 = bitmap2;
            }
            HilshadingTemps hilshadingTemps3 = hilshadingTemps2;
            Rect useAsr = hilshadingTemps3.useAsr(round, round2, round3, round4);
            Rect useAdr = hilshadingTemps3.useAdr(0, 0, ceil, ceil2);
            android.graphics.Bitmap useScaleBitmap = hilshadingTemps3.useScaleBitmap(ceil, ceil2, bitmap2.getConfig());
            useCanvas.setBitmap(useScaleBitmap);
            useCanvas.drawBitmap(bitmap3, useAsr, useAdr, this.bitmapPaint);
            Rectangle rectangle5 = rectangle2;
            this.canvas.clipRect((float) rectangle5.left, (float) rectangle5.top, (float) rectangle5.right, (float) rectangle5.bottom);
            this.canvas.drawBitmap(useScaleBitmap, (float) ((int) Math.round(rectangle5.left - d3)), (float) ((int) Math.round(rectangle5.top - d6)), paint);
        } else {
            if (Build.VERSION.SDK_INT >= 26) {
                this.canvas.save();
                this.canvas.clipRect((float) rectangle4.left, (float) rectangle4.top, (float) rectangle4.right, (float) rectangle4.bottom);
                this.canvas.restore();
            } else {
                this.canvas.clipRect((float) rectangle4.left, (float) rectangle4.top, (float) rectangle4.right, (float) rectangle4.bottom, Region.Op.REPLACE);
            }
            android.graphics.Matrix useMatrix = hilshadingTemps.useMatrix();
            useMatrix.preTranslate((float) rectangle4.left, (float) rectangle4.top);
            useMatrix.preScale((float) width, (float) height);
            useMatrix.preTranslate((float) (-rectangle3.left), (float) (-rectangle3.top));
            this.canvas.drawBitmap(bitmap4, useMatrix, useAlphaPaint);
        }
        this.canvas.restore();
    }

    private static class HilshadingTemps {
        private final Rect adr;
        private final Rect asr;
        private android.graphics.Bitmap neutralShadingPixel;
        private android.graphics.Bitmap scaleTemp;
        private final Paint shadePaint;
        private android.graphics.Bitmap shiftTemp;
        private final android.graphics.Canvas tmpCanvas;
        private android.graphics.Matrix tmpMatrix;

        /* synthetic */ HilshadingTemps(C13091 r1) {
            this();
        }

        private HilshadingTemps() {
            this.asr = new Rect(0, 0, 0, 0);
            this.adr = new Rect(0, 0, 0, 0);
            this.tmpCanvas = new android.graphics.Canvas();
            this.neutralShadingPixel = AndroidGraphicFactory.INSTANCE.createMonoBitmap(1, 1, new byte[]{ByteCompanionObject.MAX_VALUE}, 0, (BoundingBox) null).bitmap;
            Paint paint = new Paint();
            this.shadePaint = paint;
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
        }

        /* access modifiers changed from: package-private */
        public Rect useAsr(int i, int i2, int i3, int i4) {
            this.asr.left = i;
            this.asr.top = i2;
            this.asr.right = i3;
            this.asr.bottom = i4;
            return this.asr;
        }

        /* access modifiers changed from: package-private */
        public Rect useAdr(int i, int i2, int i3, int i4) {
            this.adr.left = i;
            this.adr.top = i2;
            this.adr.right = i3;
            this.adr.bottom = i4;
            return this.adr;
        }

        /* access modifiers changed from: package-private */
        public android.graphics.Canvas useCanvas() {
            return this.tmpCanvas;
        }

        /* access modifiers changed from: package-private */
        public android.graphics.Bitmap useScaleBitmap(int i, int i2, Bitmap.Config config) {
            android.graphics.Bitmap internalUseBitmap = internalUseBitmap(this.scaleTemp, i, i2, config);
            this.scaleTemp = internalUseBitmap;
            return internalUseBitmap;
        }

        /* access modifiers changed from: package-private */
        public android.graphics.Bitmap useShiftBitmap(int i, int i2, Bitmap.Config config) {
            android.graphics.Bitmap internalUseBitmap = internalUseBitmap(this.shiftTemp, i, i2, config);
            this.shiftTemp = internalUseBitmap;
            return internalUseBitmap;
        }

        private android.graphics.Bitmap internalUseBitmap(android.graphics.Bitmap bitmap, int i, int i2, Bitmap.Config config) {
            if (bitmap == null) {
                android.graphics.Bitmap createBitmap = android.graphics.Bitmap.createBitmap(i, i2, config);
                this.tmpCanvas.setBitmap(createBitmap);
                return createBitmap;
            } else if (bitmap.getWidth() < i || bitmap.getHeight() < i2 || !bitmap.getConfig().equals(config)) {
                bitmap.recycle();
                android.graphics.Bitmap createBitmap2 = android.graphics.Bitmap.createBitmap(i, i2, config);
                this.tmpCanvas.setBitmap(createBitmap2);
                return createBitmap2;
            } else {
                this.tmpCanvas.setBitmap(bitmap);
                this.tmpCanvas.drawColor(Color.argb(0, 0, 0, 0), PorterDuff.Mode.SRC);
                return bitmap;
            }
        }

        public Paint useAlphaPaint(int i) {
            this.shadePaint.setAlpha(i);
            return this.shadePaint;
        }

        public android.graphics.Bitmap useNeutralShadingPixel() {
            return this.neutralShadingPixel;
        }

        public android.graphics.Matrix useMatrix() {
            if (this.tmpMatrix == null) {
                this.tmpMatrix = new android.graphics.Matrix();
            }
            this.tmpMatrix.reset();
            return this.tmpMatrix;
        }
    }
}
