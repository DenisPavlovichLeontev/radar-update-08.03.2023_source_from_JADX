package org.osmdroid.util;

public class PointL {

    /* renamed from: x */
    public long f559x;

    /* renamed from: y */
    public long f560y;

    public PointL() {
    }

    public PointL(long j, long j2) {
        this.f559x = j;
        this.f560y = j2;
    }

    public PointL(PointL pointL) {
        set(pointL);
    }

    public void set(PointL pointL) {
        this.f559x = pointL.f559x;
        this.f560y = pointL.f560y;
    }

    public void set(long j, long j2) {
        this.f559x = j;
        this.f560y = j2;
    }

    public final void offset(long j, long j2) {
        this.f559x += j;
        this.f560y += j2;
    }

    public String toString() {
        return "PointL(" + this.f559x + ", " + this.f560y + ")";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PointL)) {
            return false;
        }
        PointL pointL = (PointL) obj;
        if (this.f559x == pointL.f559x && this.f560y == pointL.f560y) {
            return true;
        }
        return false;
    }
}
