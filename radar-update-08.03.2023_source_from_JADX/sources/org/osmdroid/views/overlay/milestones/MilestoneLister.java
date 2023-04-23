package org.osmdroid.views.overlay.milestones;

import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.PointAccepter;
import org.osmdroid.util.PointL;

public abstract class MilestoneLister implements PointAccepter {
    private double[] mDistances;
    private boolean mFirst;
    private final PointL mLatestPoint = new PointL();
    private final List<MilestoneStep> mMilestones = new ArrayList();

    /* access modifiers changed from: protected */
    public abstract void add(long j, long j2, long j3, long j4);

    public void end() {
    }

    public List<MilestoneStep> getMilestones() {
        return this.mMilestones;
    }

    public void setDistances(double[] dArr) {
        this.mDistances = dArr;
    }

    /* access modifiers changed from: protected */
    public double getDistance(int i) {
        return this.mDistances[i];
    }

    public void init() {
        this.mMilestones.clear();
        this.mFirst = true;
    }

    public void add(long j, long j2) {
        if (this.mFirst) {
            this.mFirst = false;
            this.mLatestPoint.set(j, j2);
            return;
        }
        add(this.mLatestPoint.f559x, this.mLatestPoint.f560y, j, j2);
        this.mLatestPoint.set(j, j2);
    }

    /* access modifiers changed from: protected */
    public void add(MilestoneStep milestoneStep) {
        this.mMilestones.add(milestoneStep);
    }

    public static double getOrientation(long j, long j2, long j3, long j4) {
        if (j == j3) {
            int i = (j2 > j4 ? 1 : (j2 == j4 ? 0 : -1));
            if (i == 0) {
                return 0.0d;
            }
            return i > 0 ? -90.0d : 90.0d;
        }
        double d = ((double) (j4 - j2)) / ((double) (j3 - j));
        int i2 = 0;
        boolean z = j3 < j;
        double atan = Math.atan(d) * 57.29577951308232d;
        if (z) {
            i2 = 180;
        }
        return atan + ((double) i2);
    }
}
