package org.osmdroid.util;

import android.graphics.Path;

public class PathBuilder implements PointAccepter {
    private boolean mFirst;
    private final PointL mLatestPoint = new PointL();
    private final Path mPath;

    public void end() {
    }

    public PathBuilder(Path path) {
        this.mPath = path;
    }

    public void init() {
        this.mFirst = true;
    }

    public void add(long j, long j2) {
        if (this.mFirst) {
            this.mFirst = false;
            this.mPath.moveTo((float) j, (float) j2);
            this.mLatestPoint.set(j, j2);
        } else if (this.mLatestPoint.f559x != j || this.mLatestPoint.f560y != j2) {
            this.mPath.lineTo((float) j, (float) j2);
            this.mLatestPoint.set(j, j2);
        }
    }
}
