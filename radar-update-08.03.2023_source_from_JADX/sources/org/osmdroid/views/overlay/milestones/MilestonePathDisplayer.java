package org.osmdroid.views.overlay.milestones;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class MilestonePathDisplayer extends MilestoneDisplayer {
    private final Paint mPaint;
    private final Path mPath;

    public MilestonePathDisplayer(double d, boolean z, Path path, Paint paint) {
        super(d, z);
        this.mPath = path;
        this.mPaint = paint;
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas, Object obj) {
        canvas.drawPath(this.mPath, this.mPaint);
    }
}
