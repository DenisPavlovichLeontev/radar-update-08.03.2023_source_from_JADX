package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class McBrydeThomasFlatPolarSine1Projection extends SineTangentSeriesProjection {
    public String toString() {
        return "McBryde-Thomas Flat-Polar Sine (No. 1)";
    }

    public /* bridge */ /* synthetic */ boolean hasInverse() {
        return super.hasInverse();
    }

    public /* bridge */ /* synthetic */ ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        return super.project(d, d2, projCoordinate);
    }

    public /* bridge */ /* synthetic */ ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        return super.projectInverse(d, d2, projCoordinate);
    }

    public McBrydeThomasFlatPolarSine1Projection() {
        super(1.48875d, 1.36509d, false);
    }
}
