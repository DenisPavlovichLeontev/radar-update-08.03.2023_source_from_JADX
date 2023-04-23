package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class QuarticAuthalicProjection extends SineTangentSeriesProjection {
    public String toString() {
        return "Quartic Authalic";
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

    public QuarticAuthalicProjection() {
        super(2.0d, 2.0d, false);
    }
}
