package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

public class CopyrightOverlay extends Overlay {
    protected boolean alignBottom = true;
    protected boolean alignRight = false;

    /* renamed from: dm */
    final DisplayMetrics f564dm;
    private String mCopyrightNotice;
    private Paint paint;
    int xOffset = 10;
    int yOffset = 10;

    public CopyrightOverlay(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.f564dm = displayMetrics;
        Paint paint2 = new Paint();
        this.paint = paint2;
        paint2.setAntiAlias(true);
        this.paint.setTextSize(displayMetrics.density * 12.0f);
    }

    public void setTextSize(int i) {
        this.paint.setTextSize(this.f564dm.density * ((float) i));
    }

    public void setTextColor(int i) {
        this.paint.setColor(i);
    }

    public void setAlignBottom(boolean z) {
        this.alignBottom = z;
    }

    public void setAlignRight(boolean z) {
        this.alignRight = z;
    }

    public void setOffset(int i, int i2) {
        this.xOffset = i;
        this.yOffset = i2;
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
        setCopyrightNotice(mapView.getTileProvider().getTileSource().getCopyrightNotice());
        draw(canvas, mapView.getProjection());
    }

    public void draw(Canvas canvas, Projection projection) {
        float f;
        float f2;
        String str = this.mCopyrightNotice;
        if (str != null && str.length() != 0) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            if (this.alignRight) {
                f = (float) (width - this.xOffset);
                this.paint.setTextAlign(Paint.Align.RIGHT);
            } else {
                f = (float) this.xOffset;
                this.paint.setTextAlign(Paint.Align.LEFT);
            }
            if (this.alignBottom) {
                f2 = (float) (height - this.yOffset);
            } else {
                f2 = this.paint.getTextSize() + ((float) this.yOffset);
            }
            projection.save(canvas, false, false);
            canvas.drawText(this.mCopyrightNotice, f, f2, this.paint);
            projection.restore(canvas, false);
        }
    }

    public void setCopyrightNotice(String str) {
        this.mCopyrightNotice = str;
    }
}
