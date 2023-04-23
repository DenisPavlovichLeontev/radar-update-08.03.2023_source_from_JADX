package org.mapsforge.map.scalebar;

public final class ImperialUnitAdapter implements DistanceUnitAdapter {
    public static final ImperialUnitAdapter INSTANCE = new ImperialUnitAdapter();
    private static final double METER_FOOT_RATIO = 0.3048d;
    private static final int ONE_MILE = 5280;
    private static final int[] SCALE_BAR_VALUES = {26400000, 10560000, 5280000, 2640000, 1056000, 528000, 264000, 105600, 52800, 26400, 10560, ONE_MILE, 2000, 1000, 500, 200, 100, 50, 20, 10, 5, 2, 1};

    public double getMeterRatio() {
        return METER_FOOT_RATIO;
    }

    private ImperialUnitAdapter() {
    }

    public int[] getScaleBarValues() {
        return SCALE_BAR_VALUES;
    }

    public String getScaleText(int i) {
        if (i < ONE_MILE) {
            return i + " ft";
        }
        return (i / ONE_MILE) + " mi";
    }
}
