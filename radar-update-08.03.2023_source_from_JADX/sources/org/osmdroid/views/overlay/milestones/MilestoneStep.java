package org.osmdroid.views.overlay.milestones;

public class MilestoneStep {
    private final Object mObject;
    private final double mOrientation;

    /* renamed from: mX */
    private final long f567mX;

    /* renamed from: mY */
    private final long f568mY;

    public MilestoneStep(long j, long j2, double d, Object obj) {
        this.f567mX = j;
        this.f568mY = j2;
        this.mOrientation = d;
        this.mObject = obj;
    }

    public MilestoneStep(long j, long j2, double d) {
        this(j, j2, d, (Object) null);
    }

    public long getX() {
        return this.f567mX;
    }

    public long getY() {
        return this.f568mY;
    }

    public double getOrientation() {
        return this.mOrientation;
    }

    public Object getObject() {
        return this.mObject;
    }

    public String toString() {
        return getClass().getSimpleName() + ":" + this.f567mX + "," + this.f568mY + "," + this.mOrientation + "," + this.mObject;
    }
}
