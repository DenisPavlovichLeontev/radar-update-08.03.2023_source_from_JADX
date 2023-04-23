package org.osmdroid.views.overlay.milestones;

import android.graphics.Canvas;
import android.graphics.Paint;
import org.osmdroid.views.overlay.LineDrawer;

public class MilestoneLineDisplayer extends MilestoneDisplayer {
    /* access modifiers changed from: private */
    public boolean mFirst = true;
    private final LineDrawer mLineDrawer;
    private long mPreviousX;
    private long mPreviousY;

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas, Object obj) {
    }

    public MilestoneLineDisplayer(Paint paint) {
        super(0.0d, false);
        C13921 r0 = new LineDrawer(256) {
            public void flush() {
                super.flush();
                boolean unused = MilestoneLineDisplayer.this.mFirst = true;
            }
        };
        this.mLineDrawer = r0;
        r0.setPaint(paint);
    }

    public void drawBegin(Canvas canvas) {
        this.mLineDrawer.init();
        this.mLineDrawer.setCanvas(canvas);
        this.mFirst = true;
    }

    public void draw(Canvas canvas, MilestoneStep milestoneStep) {
        long x = milestoneStep.getX();
        long y = milestoneStep.getY();
        if (this.mFirst) {
            this.mFirst = false;
        } else {
            long j = this.mPreviousX;
            if (!(j == x && this.mPreviousY == y)) {
                this.mLineDrawer.add(j, this.mPreviousY);
                this.mLineDrawer.add(x, y);
            }
        }
        this.mPreviousX = x;
        this.mPreviousY = y;
    }

    public void drawEnd(Canvas canvas) {
        this.mLineDrawer.end();
    }
}
