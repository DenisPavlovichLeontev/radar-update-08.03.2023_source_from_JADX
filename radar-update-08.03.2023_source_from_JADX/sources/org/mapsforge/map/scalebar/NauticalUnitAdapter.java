package org.mapsforge.map.scalebar;

public final class NauticalUnitAdapter implements DistanceUnitAdapter {
    public static final NauticalUnitAdapter INSTANCE = new NauticalUnitAdapter();
    private static final int ONE_MILE = 1852;
    private static final int[] SCALE_BAR_VALUES = {9260000, 3704000, 1852000, 926000, 370400, 185200, 92600, 37040, 18520, 9260, 3704, ONE_MILE, 926, 500, 200, 100, 50, 20, 10, 5, 2, 1};

    public double getMeterRatio() {
        return 1.0d;
    }

    private NauticalUnitAdapter() {
    }

    public int[] getScaleBarValues() {
        return SCALE_BAR_VALUES;
    }

    public String getScaleText(int i) {
        if (i < 926) {
            return i + " m";
        } else if (i == 926) {
            return "0.5 nmi";
        } else {
            return (i / ONE_MILE) + " nmi";
        }
    }
}
