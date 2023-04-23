package org.osmdroid.util;

import java.util.ArrayList;
import java.util.List;

public class ListPointAccepter implements PointAccepter {
    private boolean mFirst;
    private final PointL mLatestPoint = new PointL();
    private final List<Long> mList = new ArrayList();
    private final boolean mRemoveConsecutiveDuplicates;

    public void end() {
    }

    public ListPointAccepter(boolean z) {
        this.mRemoveConsecutiveDuplicates = z;
    }

    public List<Long> getList() {
        return this.mList;
    }

    public void init() {
        this.mList.clear();
        this.mFirst = true;
    }

    public void add(long j, long j2) {
        if (!this.mRemoveConsecutiveDuplicates) {
            this.mList.add(Long.valueOf(j));
            this.mList.add(Long.valueOf(j2));
        } else if (this.mFirst) {
            this.mFirst = false;
            this.mList.add(Long.valueOf(j));
            this.mList.add(Long.valueOf(j2));
            this.mLatestPoint.set(j, j2);
        } else if (this.mLatestPoint.f559x != j || this.mLatestPoint.f560y != j2) {
            this.mList.add(Long.valueOf(j));
            this.mList.add(Long.valueOf(j2));
            this.mLatestPoint.set(j, j2);
        }
    }
}
