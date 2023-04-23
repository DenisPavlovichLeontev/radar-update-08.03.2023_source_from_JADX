package org.osmdroid.views.overlay.milestones;

public class MilestoneVertexLister extends MilestoneLister {
    private int mIndex;
    private double mLatestOrientation;
    private long mLatestX;
    private long mLatestY;

    public void init() {
        super.init();
        this.mIndex = 0;
    }

    /* access modifiers changed from: protected */
    public void add(long j, long j2, long j3, long j4) {
        this.mLatestOrientation = getOrientation(j, j2, j3, j4);
        int i = this.mIndex;
        this.mIndex = i + 1;
        innerAdd(j, j2, i);
        this.mLatestX = j3;
        this.mLatestY = j4;
    }

    public void end() {
        super.end();
        innerAdd(this.mLatestX, this.mLatestY, -this.mIndex);
    }

    private void innerAdd(long j, long j2, int i) {
        add(new MilestoneStep(j, j2, this.mLatestOrientation, Integer.valueOf(i)));
    }
}
