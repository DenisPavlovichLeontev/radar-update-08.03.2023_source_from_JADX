package org.osmdroid.views.overlay.milestones;

import android.graphics.Canvas;
import org.osmdroid.util.PointAccepter;

public class MilestoneManager implements PointAccepter {
    private final MilestoneDisplayer mDisplayer;
    private final MilestoneLister mLister;

    public MilestoneManager(MilestoneLister milestoneLister, MilestoneDisplayer milestoneDisplayer) {
        this.mLister = milestoneLister;
        this.mDisplayer = milestoneDisplayer;
    }

    public void draw(Canvas canvas) {
        this.mDisplayer.drawBegin(canvas);
        for (MilestoneStep draw : this.mLister.getMilestones()) {
            this.mDisplayer.draw(canvas, draw);
        }
        this.mDisplayer.drawEnd(canvas);
    }

    public void init() {
        this.mLister.init();
    }

    public void add(long j, long j2) {
        this.mLister.add(j, j2);
    }

    public void end() {
        this.mLister.end();
    }

    public void setDistances(double[] dArr) {
        this.mLister.setDistances(dArr);
    }
}
