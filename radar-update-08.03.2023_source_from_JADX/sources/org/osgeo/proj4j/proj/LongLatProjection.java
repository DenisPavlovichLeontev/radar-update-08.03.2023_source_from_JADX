package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.units.Units;

public class LongLatProjection extends Projection {
    public String toString() {
        return "LongLat";
    }

    public void initialize() {
        this.unit = Units.DEGREES;
        this.totalScale = 1.0d;
    }
}
