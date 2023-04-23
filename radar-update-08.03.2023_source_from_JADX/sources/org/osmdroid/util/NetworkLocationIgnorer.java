package org.osmdroid.util;

import org.osmdroid.config.Configuration;

public class NetworkLocationIgnorer {
    private long mLastGps = 0;

    public boolean shouldIgnore(String str, long j) {
        if (!"gps".equals(str)) {
            return j < this.mLastGps + Configuration.getInstance().getGpsWaitTime();
        }
        this.mLastGps = j;
        return false;
    }
}
