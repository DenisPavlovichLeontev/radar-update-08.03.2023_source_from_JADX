package org.osmdroid.views.overlay.milestones;

import android.graphics.Canvas;

public abstract class MilestoneDisplayer {
    private final boolean mFollowTrajectory;
    private final double mInitialOrientation;

    /* access modifiers changed from: protected */
    public abstract void draw(Canvas canvas, Object obj);

    public void drawBegin(Canvas canvas) {
    }

    public void drawEnd(Canvas canvas) {
    }

    public MilestoneDisplayer(double d, boolean z) {
        this.mInitialOrientation = d;
        this.mFollowTrajectory = z;
    }

    public void draw(Canvas canvas, MilestoneStep milestoneStep) {
        double d = this.mInitialOrientation;
        double orientation = this.mFollowTrajectory ? milestoneStep.getOrientation() : 0.0d;
        canvas.save();
        canvas.rotate((float) (d + orientation), (float) milestoneStep.getX(), (float) milestoneStep.getY());
        canvas.translate((float) milestoneStep.getX(), (float) milestoneStep.getY());
        draw(canvas, milestoneStep.getObject());
        canvas.restore();
    }
}
