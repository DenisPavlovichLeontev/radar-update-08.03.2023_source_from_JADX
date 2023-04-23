package org.osmdroid.views.overlay.advancedpolyline;

import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import org.osmdroid.views.overlay.PaintList;

public class PolychromaticPaintList implements PaintList {
    private final ColorMapping mColorMapping;
    private final Paint mPaint;
    private final boolean mUseGradient;

    public Paint getPaint() {
        return null;
    }

    public PolychromaticPaintList(Paint paint, ColorMapping colorMapping, boolean z) {
        this.mPaint = paint;
        this.mColorMapping = colorMapping;
        this.mUseGradient = z;
    }

    public Paint getPaint(int i, float f, float f2, float f3, float f4) {
        int colorForIndex = this.mColorMapping.getColorForIndex(i);
        if (this.mUseGradient) {
            int colorForIndex2 = this.mColorMapping.getColorForIndex(i + 1);
            if (colorForIndex != colorForIndex2) {
                this.mPaint.setShader(new LinearGradient(f, f2, f3, f4, colorForIndex, colorForIndex2, Shader.TileMode.CLAMP));
                return this.mPaint;
            }
            this.mPaint.setShader((Shader) null);
        }
        this.mPaint.setColor(colorForIndex);
        return this.mPaint;
    }
}
