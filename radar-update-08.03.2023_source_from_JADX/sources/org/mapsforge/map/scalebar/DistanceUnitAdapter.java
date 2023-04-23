package org.mapsforge.map.scalebar;

public interface DistanceUnitAdapter {
    double getMeterRatio();

    int[] getScaleBarValues();

    String getScaleText(int i);
}
