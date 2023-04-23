package org.osmdroid.views.overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import org.osmdroid.util.IntegerAccepter;
import org.osmdroid.util.LineBuilder;
import org.osmdroid.views.overlay.advancedpolyline.MonochromaticPaintList;

public class LineDrawer extends LineBuilder {
    private Canvas mCanvas;
    private IntegerAccepter mIntegerAccepter;
    private PaintList mPaintList;

    public LineDrawer(int i) {
        super(i);
    }

    public void setCanvas(Canvas canvas) {
        this.mCanvas = canvas;
    }

    public void setPaint(Paint paint) {
        setPaint((PaintList) new MonochromaticPaintList(paint));
    }

    public void setPaint(PaintList paintList) {
        this.mPaintList = paintList;
    }

    public void setIntegerAccepter(IntegerAccepter integerAccepter) {
        this.mIntegerAccepter = integerAccepter;
    }

    public void flush() {
        int size = getSize() / 4;
        if (size == 0) {
            additionalFlush();
            return;
        }
        float[] lines = getLines();
        Paint paint = this.mPaintList.getPaint();
        if (paint != null) {
            int compact = compact(lines, size * 4);
            if (compact > 0) {
                this.mCanvas.drawLines(lines, 0, compact, paint);
            }
            additionalFlush();
            return;
        }
        for (int i = 0; i < size * 4; i += 4) {
            float f = lines[i];
            float f2 = lines[i + 1];
            float f3 = lines[i + 2];
            float f4 = lines[i + 3];
            if (f != f3 || f2 != f4) {
                int value = this.mIntegerAccepter.getValue(i / 2);
                this.mCanvas.drawLine(f, f2, f3, f4, this.mPaintList.getPaint(value, f, f2, f3, f4));
            }
        }
        additionalFlush();
    }

    private void additionalFlush() {
        IntegerAccepter integerAccepter = this.mIntegerAccepter;
        if (integerAccepter != null) {
            integerAccepter.flush();
        }
    }

    private static int compact(float[] fArr, int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3 += 4) {
            float f = fArr[i3];
            float f2 = fArr[i3 + 1];
            float f3 = fArr[i3 + 2];
            float f4 = fArr[i3 + 3];
            if (f != f3 || f2 != f4) {
                if (i3 != i2) {
                    System.arraycopy(fArr, i3, fArr, i2, 4);
                }
                i2 += 4;
            }
        }
        return i2;
    }
}
