package org.osmdroid.views.overlay.advancedpolyline;

import android.graphics.Paint;
import org.osmdroid.views.overlay.PaintList;

public class MonochromaticPaintList implements PaintList {
    private final Paint mPaint;

    public Paint getPaint(int i, float f, float f2, float f3, float f4) {
        return null;
    }

    public MonochromaticPaintList(Paint paint) {
        this.mPaint = paint;
    }

    public Paint getPaint() {
        return this.mPaint;
    }
}
