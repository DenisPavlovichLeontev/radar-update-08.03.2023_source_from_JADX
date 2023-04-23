package org.mapsforge.map.scalebar;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Cap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.FontFamily;
import org.mapsforge.core.graphics.FontStyle;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.model.MapViewDimension;
import org.mapsforge.map.scalebar.MapScaleBar;

public class DefaultMapScaleBar extends MapScaleBar {
    private static final int BITMAP_HEIGHT = 40;
    private static final int BITMAP_WIDTH = 120;
    private static final int DEFAULT_HORIZONTAL_MARGIN = 5;
    private static final int DEFAULT_VERTICAL_MARGIN = 0;
    private static final int SCALE_BAR_MARGIN = 10;
    private static final float STROKE_EXTERNAL = 4.0f;
    private static final float STROKE_INTERNAL = 2.0f;
    private static final int TEXT_MARGIN = 1;
    private final Paint paintScaleBar;
    private final Paint paintScaleBarStroke;
    private final Paint paintScaleText;
    private final Paint paintScaleTextStroke;
    private final float scale;
    private ScaleBarMode scaleBarMode;
    private DistanceUnitAdapter secondaryDistanceUnitAdapter;

    public enum ScaleBarMode {
        BOTH,
        SINGLE
    }

    public DefaultMapScaleBar(IMapViewPosition iMapViewPosition, MapViewDimension mapViewDimension, GraphicFactory graphicFactory, DisplayModel displayModel) {
        this(iMapViewPosition, mapViewDimension, graphicFactory, displayModel, displayModel.getScaleFactor());
    }

    public DefaultMapScaleBar(IMapViewPosition iMapViewPosition, MapViewDimension mapViewDimension, GraphicFactory graphicFactory, DisplayModel displayModel, float f) {
        super(iMapViewPosition, mapViewDimension, displayModel, graphicFactory, (int) (120.0f * f), (int) (40.0f * f));
        setMarginHorizontal((int) (5.0f * f));
        setMarginVertical((int) (f * 0.0f));
        this.scale = f;
        this.scaleBarMode = ScaleBarMode.BOTH;
        this.secondaryDistanceUnitAdapter = ImperialUnitAdapter.INSTANCE;
        this.paintScaleBar = createScaleBarPaint(Color.BLACK, STROKE_INTERNAL, Style.FILL);
        this.paintScaleBarStroke = createScaleBarPaint(Color.WHITE, STROKE_EXTERNAL, Style.STROKE);
        this.paintScaleText = createTextPaint(Color.BLACK, 0.0f, Style.FILL);
        this.paintScaleTextStroke = createTextPaint(Color.WHITE, STROKE_INTERNAL, Style.STROKE);
    }

    public DistanceUnitAdapter getSecondaryDistanceUnitAdapter() {
        return this.secondaryDistanceUnitAdapter;
    }

    public void setSecondaryDistanceUnitAdapter(DistanceUnitAdapter distanceUnitAdapter) {
        if (distanceUnitAdapter != null) {
            this.secondaryDistanceUnitAdapter = distanceUnitAdapter;
            this.redrawNeeded = true;
            return;
        }
        throw new IllegalArgumentException("adapter must not be null");
    }

    public ScaleBarMode getScaleBarMode() {
        return this.scaleBarMode;
    }

    public void setScaleBarMode(ScaleBarMode scaleBarMode2) {
        this.scaleBarMode = scaleBarMode2;
        this.redrawNeeded = true;
    }

    private Paint createScaleBarPaint(Color color, float f, Style style) {
        Paint createPaint = this.graphicFactory.createPaint();
        createPaint.setColor(color);
        createPaint.setStrokeWidth(f * this.scale);
        createPaint.setStyle(style);
        createPaint.setStrokeCap(Cap.SQUARE);
        return createPaint;
    }

    private Paint createTextPaint(Color color, float f, Style style) {
        Paint createPaint = this.graphicFactory.createPaint();
        createPaint.setColor(color);
        createPaint.setStrokeWidth(f * this.scale);
        createPaint.setStyle(style);
        createPaint.setTypeface(FontFamily.DEFAULT, FontStyle.BOLD);
        createPaint.setTextSize(this.scale * 12.0f);
        return createPaint;
    }

    /* access modifiers changed from: protected */
    public void redraw(Canvas canvas) {
        MapScaleBar.ScaleBarLengthAndValue scaleBarLengthAndValue;
        canvas.fillColor(Color.TRANSPARENT);
        MapScaleBar.ScaleBarLengthAndValue calculateScaleBarLengthAndValue = calculateScaleBarLengthAndValue();
        if (this.scaleBarMode == ScaleBarMode.BOTH) {
            scaleBarLengthAndValue = calculateScaleBarLengthAndValue(this.secondaryDistanceUnitAdapter);
        } else {
            scaleBarLengthAndValue = new MapScaleBar.ScaleBarLengthAndValue(0, 0);
        }
        drawScaleBar(canvas, calculateScaleBarLengthAndValue.scaleBarLength, scaleBarLengthAndValue.scaleBarLength, this.paintScaleBarStroke, this.scale);
        drawScaleBar(canvas, calculateScaleBarLengthAndValue.scaleBarLength, scaleBarLengthAndValue.scaleBarLength, this.paintScaleBar, this.scale);
        Canvas canvas2 = canvas;
        String scaleText = this.distanceUnitAdapter.getScaleText(calculateScaleBarLengthAndValue.scaleBarValue);
        String scaleText2 = this.scaleBarMode == ScaleBarMode.BOTH ? this.secondaryDistanceUnitAdapter.getScaleText(scaleBarLengthAndValue.scaleBarValue) : "";
        drawScaleText(canvas2, scaleText, scaleText2, this.paintScaleTextStroke, this.scale);
        drawScaleText(canvas2, scaleText, scaleText2, this.paintScaleText, this.scale);
    }

    private void drawScaleBar(Canvas canvas, int i, int i2, Paint paint, float f) {
        int i3 = i;
        int i4 = i2;
        int max = Math.max(i, i2);
        switch (C13321.f403x399227b0[this.scaleBarPosition.ordinal()]) {
            case 1:
                if (i4 == 0) {
                    float f2 = f * 10.0f;
                    Canvas canvas2 = canvas;
                    Paint paint2 = paint;
                    canvas2.drawLine(Math.round(((float) (canvas.getWidth() - max)) * 0.5f), Math.round(((float) canvas.getHeight()) - f2), Math.round(((float) (canvas.getWidth() + max)) * 0.5f), Math.round(((float) canvas.getHeight()) - f2), paint2);
                    canvas2.drawLine(Math.round(((float) (canvas.getWidth() - max)) * 0.5f), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round(((float) (canvas.getWidth() - max)) * 0.5f), Math.round(((float) canvas.getHeight()) - f2), paint2);
                    canvas2.drawLine(Math.round(((float) (canvas.getWidth() + max)) * 0.5f), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round(((float) (canvas.getWidth() + max)) * 0.5f), Math.round(((float) canvas.getHeight()) - f2), paint2);
                    return;
                }
                float f3 = f * STROKE_EXTERNAL * 0.5f;
                Canvas canvas3 = canvas;
                canvas3.drawLine(Math.round(f3), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round(((float) max) + f3), Math.round(((float) canvas.getHeight()) * 0.5f), paint);
                float f4 = f * 10.0f;
                canvas.drawLine(Math.round(f3), Math.round(f4), Math.round(f3), Math.round(((float) canvas.getHeight()) - f4), paint);
                float f5 = ((float) i3) + f3;
                Paint paint3 = paint;
                canvas3.drawLine(Math.round(f5), Math.round(f4), Math.round(f5), Math.round(((float) canvas.getHeight()) * 0.5f), paint3);
                float f6 = f3 + ((float) i4);
                canvas3.drawLine(Math.round(f6), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round(f6), Math.round(((float) canvas.getHeight()) - f4), paint3);
                return;
            case 2:
                if (i4 == 0) {
                    float f7 = f * STROKE_EXTERNAL * 0.5f;
                    float f8 = f * 10.0f;
                    float f9 = ((float) max) + f7;
                    Canvas canvas4 = canvas;
                    canvas4.drawLine(Math.round(f7), Math.round(((float) canvas.getHeight()) - f8), Math.round(f9), Math.round(((float) canvas.getHeight()) - f8), paint);
                    canvas.drawLine(Math.round(f7), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round(f7), Math.round(((float) canvas.getHeight()) - f8), paint);
                    canvas4.drawLine(Math.round(f9), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round(f9), Math.round(((float) canvas.getHeight()) - f8), paint);
                    return;
                }
                float f10 = f * STROKE_EXTERNAL * 0.5f;
                Canvas canvas5 = canvas;
                canvas5.drawLine(Math.round(f10), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round(((float) max) + f10), Math.round(((float) canvas.getHeight()) * 0.5f), paint);
                float f11 = f * 10.0f;
                canvas.drawLine(Math.round(f10), Math.round(f11), Math.round(f10), Math.round(((float) canvas.getHeight()) - f11), paint);
                float f12 = ((float) i3) + f10;
                Paint paint4 = paint;
                canvas5.drawLine(Math.round(f12), Math.round(f11), Math.round(f12), Math.round(((float) canvas.getHeight()) * 0.5f), paint4);
                float f13 = f10 + ((float) i4);
                canvas5.drawLine(Math.round(f13), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round(f13), Math.round(((float) canvas.getHeight()) - f11), paint4);
                return;
            case 3:
                if (i4 == 0) {
                    float f14 = f * STROKE_EXTERNAL * 0.5f;
                    float f15 = (float) max;
                    float f16 = f * 10.0f;
                    Canvas canvas6 = canvas;
                    Paint paint5 = paint;
                    canvas6.drawLine(Math.round((((float) canvas.getWidth()) - f14) - f15), Math.round(((float) canvas.getHeight()) - f16), Math.round(((float) canvas.getWidth()) - f14), Math.round(((float) canvas.getHeight()) - f16), paint5);
                    canvas6.drawLine(Math.round(((float) canvas.getWidth()) - f14), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round(((float) canvas.getWidth()) - f14), Math.round(((float) canvas.getHeight()) - f16), paint5);
                    canvas6.drawLine(Math.round((((float) canvas.getWidth()) - f14) - f15), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round((((float) canvas.getWidth()) - f14) - f15), Math.round(((float) canvas.getHeight()) - f16), paint5);
                    return;
                }
                float f17 = STROKE_EXTERNAL * f * 0.5f;
                Canvas canvas7 = canvas;
                Paint paint6 = paint;
                canvas7.drawLine(Math.round(((float) canvas.getWidth()) - f17), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round((((float) canvas.getWidth()) - f17) - ((float) max)), Math.round(((float) canvas.getHeight()) * 0.5f), paint6);
                float f18 = f * 10.0f;
                canvas7.drawLine(Math.round(((float) canvas.getWidth()) - f17), Math.round(f18), Math.round(((float) canvas.getWidth()) - f17), Math.round(((float) canvas.getHeight()) - f18), paint6);
                float f19 = (float) i3;
                canvas7.drawLine(Math.round((((float) canvas.getWidth()) - f17) - f19), Math.round(f18), Math.round((((float) canvas.getWidth()) - f17) - f19), Math.round(((float) canvas.getHeight()) * 0.5f), paint6);
                float f20 = (float) i4;
                canvas7.drawLine(Math.round((((float) canvas.getWidth()) - f17) - f20), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round((((float) canvas.getWidth()) - f17) - f20), Math.round(((float) canvas.getHeight()) - f18), paint6);
                return;
            case 4:
                if (i4 == 0) {
                    float f21 = f * 10.0f;
                    Canvas canvas8 = canvas;
                    Paint paint7 = paint;
                    canvas8.drawLine(Math.round(((float) (canvas.getWidth() - max)) * 0.5f), Math.round(f21), Math.round(((float) (canvas.getWidth() + max)) * 0.5f), Math.round(f21), paint7);
                    canvas8.drawLine(Math.round(((float) (canvas.getWidth() - max)) * 0.5f), Math.round(f21), Math.round(((float) (canvas.getWidth() - max)) * 0.5f), Math.round(((float) canvas.getHeight()) * 0.5f), paint7);
                    canvas8.drawLine(Math.round(((float) (canvas.getWidth() + max)) * 0.5f), Math.round(f21), Math.round(((float) (canvas.getWidth() + max)) * 0.5f), Math.round(((float) canvas.getHeight()) * 0.5f), paint7);
                    return;
                }
                float f22 = f * STROKE_EXTERNAL * 0.5f;
                Canvas canvas9 = canvas;
                canvas9.drawLine(Math.round(f22), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round(((float) max) + f22), Math.round(((float) canvas.getHeight()) * 0.5f), paint);
                float f23 = f * 10.0f;
                canvas.drawLine(Math.round(f22), Math.round(f23), Math.round(f22), Math.round(((float) canvas.getHeight()) - f23), paint);
                float f24 = ((float) i3) + f22;
                Paint paint8 = paint;
                canvas9.drawLine(Math.round(f24), Math.round(f23), Math.round(f24), Math.round(((float) canvas.getHeight()) * 0.5f), paint8);
                float f25 = f22 + ((float) i4);
                canvas9.drawLine(Math.round(f25), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round(f25), Math.round(((float) canvas.getHeight()) - f23), paint8);
                return;
            case 5:
                if (i4 == 0) {
                    float f26 = f * STROKE_EXTERNAL * 0.5f;
                    float f27 = f * 10.0f;
                    float f28 = ((float) max) + f26;
                    Canvas canvas10 = canvas;
                    canvas10.drawLine(Math.round(f26), Math.round(f27), Math.round(f28), Math.round(f27), paint);
                    canvas.drawLine(Math.round(f26), Math.round(f27), Math.round(f26), Math.round(((float) canvas.getHeight()) * 0.5f), paint);
                    canvas10.drawLine(Math.round(f28), Math.round(f27), Math.round(f28), Math.round(((float) canvas.getHeight()) * 0.5f), paint);
                    return;
                }
                float f29 = f * STROKE_EXTERNAL * 0.5f;
                Canvas canvas11 = canvas;
                canvas11.drawLine(Math.round(f29), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round(((float) max) + f29), Math.round(((float) canvas.getHeight()) * 0.5f), paint);
                float f30 = f * 10.0f;
                canvas.drawLine(Math.round(f29), Math.round(f30), Math.round(f29), Math.round(((float) canvas.getHeight()) - f30), paint);
                float f31 = ((float) i3) + f29;
                Paint paint9 = paint;
                canvas11.drawLine(Math.round(f31), Math.round(f30), Math.round(f31), Math.round(((float) canvas.getHeight()) * 0.5f), paint9);
                float f32 = f29 + ((float) i4);
                canvas11.drawLine(Math.round(f32), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round(f32), Math.round(((float) canvas.getHeight()) - f30), paint9);
                return;
            case 6:
                if (i4 == 0) {
                    float f33 = f * STROKE_EXTERNAL * 0.5f;
                    float f34 = (float) max;
                    float f35 = f * 10.0f;
                    Canvas canvas12 = canvas;
                    Paint paint10 = paint;
                    canvas12.drawLine(Math.round((((float) canvas.getWidth()) - f33) - f34), Math.round(f35), Math.round(((float) canvas.getWidth()) - f33), Math.round(f35), paint10);
                    canvas12.drawLine(Math.round(((float) canvas.getWidth()) - f33), Math.round(f35), Math.round(((float) canvas.getWidth()) - f33), Math.round(((float) canvas.getHeight()) * 0.5f), paint10);
                    canvas12.drawLine(Math.round((((float) canvas.getWidth()) - f33) - f34), Math.round(f35), Math.round((((float) canvas.getWidth()) - f33) - f34), Math.round(((float) canvas.getHeight()) * 0.5f), paint10);
                    return;
                }
                float f36 = STROKE_EXTERNAL * f * 0.5f;
                Canvas canvas13 = canvas;
                Paint paint11 = paint;
                canvas13.drawLine(Math.round(((float) canvas.getWidth()) - f36), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round((((float) canvas.getWidth()) - f36) - ((float) max)), Math.round(((float) canvas.getHeight()) * 0.5f), paint11);
                float f37 = f * 10.0f;
                canvas13.drawLine(Math.round(((float) canvas.getWidth()) - f36), Math.round(f37), Math.round(((float) canvas.getWidth()) - f36), Math.round(((float) canvas.getHeight()) - f37), paint11);
                float f38 = (float) i3;
                canvas13.drawLine(Math.round((((float) canvas.getWidth()) - f36) - f38), Math.round(f37), Math.round((((float) canvas.getWidth()) - f36) - f38), Math.round(((float) canvas.getHeight()) * 0.5f), paint11);
                float f39 = (float) i4;
                canvas13.drawLine(Math.round((((float) canvas.getWidth()) - f36) - f39), Math.round(((float) canvas.getHeight()) * 0.5f), Math.round((((float) canvas.getWidth()) - f36) - f39), Math.round(((float) canvas.getHeight()) - f37), paint11);
                return;
            default:
                return;
        }
    }

    /* renamed from: org.mapsforge.map.scalebar.DefaultMapScaleBar$1 */
    static /* synthetic */ class C13321 {

        /* renamed from: $SwitchMap$org$mapsforge$map$scalebar$MapScaleBar$ScaleBarPosition */
        static final /* synthetic */ int[] f403x399227b0;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition[] r0 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f403x399227b0 = r0
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition r1 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.BOTTOM_CENTER     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f403x399227b0     // Catch:{ NoSuchFieldError -> 0x001d }
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition r1 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.BOTTOM_LEFT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f403x399227b0     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition r1 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.BOTTOM_RIGHT     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f403x399227b0     // Catch:{ NoSuchFieldError -> 0x0033 }
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition r1 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.TOP_CENTER     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = f403x399227b0     // Catch:{ NoSuchFieldError -> 0x003e }
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition r1 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.TOP_LEFT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = f403x399227b0     // Catch:{ NoSuchFieldError -> 0x0049 }
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition r1 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.TOP_RIGHT     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.scalebar.DefaultMapScaleBar.C13321.<clinit>():void");
        }
    }

    private void drawScaleText(Canvas canvas, String str, String str2, Paint paint, float f) {
        switch (C13321.f403x399227b0[this.scaleBarPosition.ordinal()]) {
            case 1:
                if (str2.length() == 0) {
                    canvas.drawText(str, Math.round(((float) (canvas.getWidth() - this.paintScaleTextStroke.getTextWidth(str))) * 0.5f), Math.round(((((float) canvas.getHeight()) - (10.0f * f)) - ((STROKE_EXTERNAL * f) * 0.5f)) - (f * 1.0f)), paint);
                    return;
                }
                float f2 = STROKE_EXTERNAL * f;
                float f3 = f * 1.0f;
                float f4 = f2 + f3;
                float f5 = f2 * 0.5f;
                canvas.drawText(str, Math.round(f4), Math.round(((((float) canvas.getHeight()) * 0.5f) - f5) - f3), paint);
                canvas.drawText(str2, Math.round(f4), Math.round((((float) canvas.getHeight()) * 0.5f) + f5 + f3 + ((float) this.paintScaleTextStroke.getTextHeight(str2))), paint);
                return;
            case 2:
                if (str2.length() == 0) {
                    float f6 = STROKE_EXTERNAL * f;
                    float f7 = 1.0f * f;
                    canvas.drawText(str, Math.round(f6 + f7), Math.round(((((float) canvas.getHeight()) - (f * 10.0f)) - (f6 * 0.5f)) - f7), paint);
                    return;
                }
                float f8 = STROKE_EXTERNAL * f;
                float f9 = f * 1.0f;
                float f10 = f8 + f9;
                float f11 = f8 * 0.5f;
                canvas.drawText(str, Math.round(f10), Math.round(((((float) canvas.getHeight()) * 0.5f) - f11) - f9), paint);
                canvas.drawText(str2, Math.round(f10), Math.round((((float) canvas.getHeight()) * 0.5f) + f11 + f9 + ((float) this.paintScaleTextStroke.getTextHeight(str2))), paint);
                return;
            case 3:
                if (str2.length() == 0) {
                    float f12 = STROKE_EXTERNAL * f;
                    float f13 = 1.0f * f;
                    canvas.drawText(str, Math.round(((((float) canvas.getWidth()) - f12) - f13) - ((float) this.paintScaleTextStroke.getTextWidth(str))), Math.round(((((float) canvas.getHeight()) - (f * 10.0f)) - (f12 * 0.5f)) - f13), paint);
                    return;
                }
                float f14 = STROKE_EXTERNAL * f;
                float f15 = f * 1.0f;
                float f16 = f14 * 0.5f;
                canvas.drawText(str, Math.round(((((float) canvas.getWidth()) - f14) - f15) - ((float) this.paintScaleTextStroke.getTextWidth(str))), Math.round(((((float) canvas.getHeight()) * 0.5f) - f16) - f15), paint);
                canvas.drawText(str2, Math.round(((((float) canvas.getWidth()) - f14) - f15) - ((float) this.paintScaleTextStroke.getTextWidth(str2))), Math.round((((float) canvas.getHeight()) * 0.5f) + f16 + f15 + ((float) this.paintScaleTextStroke.getTextHeight(str2))), paint);
                return;
            case 4:
                if (str2.length() == 0) {
                    canvas.drawText(str, Math.round(((float) (canvas.getWidth() - this.paintScaleTextStroke.getTextWidth(str))) * 0.5f), Math.round((10.0f * f) + (STROKE_EXTERNAL * f * 0.5f) + (f * 1.0f) + ((float) this.paintScaleTextStroke.getTextHeight(str))), paint);
                    return;
                }
                float f17 = STROKE_EXTERNAL * f;
                float f18 = f * 1.0f;
                float f19 = f17 + f18;
                float f20 = f17 * 0.5f;
                canvas.drawText(str, Math.round(f19), Math.round(((((float) canvas.getHeight()) * 0.5f) - f20) - f18), paint);
                canvas.drawText(str2, Math.round(f19), Math.round((((float) canvas.getHeight()) * 0.5f) + f20 + f18 + ((float) this.paintScaleTextStroke.getTextHeight(str2))), paint);
                return;
            case 5:
                if (str2.length() == 0) {
                    float f21 = STROKE_EXTERNAL * f;
                    float f22 = 1.0f * f;
                    canvas.drawText(str, Math.round(f21 + f22), Math.round((f * 10.0f) + (f21 * 0.5f) + f22 + ((float) this.paintScaleTextStroke.getTextHeight(str))), paint);
                    return;
                }
                float f23 = STROKE_EXTERNAL * f;
                float f24 = f * 1.0f;
                float f25 = f23 + f24;
                float f26 = f23 * 0.5f;
                canvas.drawText(str, Math.round(f25), Math.round(((((float) canvas.getHeight()) * 0.5f) - f26) - f24), paint);
                canvas.drawText(str2, Math.round(f25), Math.round((((float) canvas.getHeight()) * 0.5f) + f26 + f24 + ((float) this.paintScaleTextStroke.getTextHeight(str2))), paint);
                return;
            case 6:
                if (str2.length() == 0) {
                    float f27 = STROKE_EXTERNAL * f;
                    float f28 = 1.0f * f;
                    canvas.drawText(str, Math.round(((((float) canvas.getWidth()) - f27) - f28) - ((float) this.paintScaleTextStroke.getTextWidth(str))), Math.round((f * 10.0f) + (f27 * 0.5f) + f28 + ((float) this.paintScaleTextStroke.getTextHeight(str))), paint);
                    return;
                }
                float f29 = STROKE_EXTERNAL * f;
                float f30 = f * 1.0f;
                float f31 = f29 * 0.5f;
                canvas.drawText(str, Math.round(((((float) canvas.getWidth()) - f29) - f30) - ((float) this.paintScaleTextStroke.getTextWidth(str))), Math.round(((((float) canvas.getHeight()) * 0.5f) - f31) - f30), paint);
                canvas.drawText(str2, Math.round(((((float) canvas.getWidth()) - f29) - f30) - ((float) this.paintScaleTextStroke.getTextWidth(str2))), Math.round((((float) canvas.getHeight()) * 0.5f) + f31 + f30 + ((float) this.paintScaleTextStroke.getTextHeight(str2))), paint);
                return;
            default:
                return;
        }
    }
}
