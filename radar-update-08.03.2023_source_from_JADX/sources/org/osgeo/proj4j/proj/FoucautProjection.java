package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class FoucautProjection extends SineTangentSeriesProjection {
    public String toString() {
        return "Foucaut";
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

    public FoucautProjection() {
        super(2.0d, 2.0d, true);
    }
}
