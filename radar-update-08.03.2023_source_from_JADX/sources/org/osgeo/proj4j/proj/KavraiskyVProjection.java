package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class KavraiskyVProjection extends SineTangentSeriesProjection {
    public String toString() {
        return "Kavraisky V";
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

    public KavraiskyVProjection() {
        super(1.50488d, 1.35439d, false);
    }
}
