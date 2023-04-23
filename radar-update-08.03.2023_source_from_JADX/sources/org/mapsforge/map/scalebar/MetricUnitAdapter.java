package org.mapsforge.map.scalebar;

import com.google.android.gms.common.util.GmsVersion;
import kotlin.time.DurationKt;

public final class MetricUnitAdapter implements DistanceUnitAdapter {
    public static final MetricUnitAdapter INSTANCE = new MetricUnitAdapter();
    private static final int ONE_KILOMETER = 1000;
    private static final int[] SCALE_BAR_VALUES = {10000000, GmsVersion.VERSION_LONGHORN, 2000000, DurationKt.NANOS_IN_MILLIS, 500000, 200000, 100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50, 20, 10, 5, 2, 1};

    public double getMeterRatio() {
        return 1.0d;
    }

    private MetricUnitAdapter() {
    }

    public int[] getScaleBarValues() {
        return SCALE_BAR_VALUES;
    }

    public String getScaleText(int i) {
        if (i < 1000) {
            return i + " m";
        }
        return (i / 1000) + " km";
    }
}
