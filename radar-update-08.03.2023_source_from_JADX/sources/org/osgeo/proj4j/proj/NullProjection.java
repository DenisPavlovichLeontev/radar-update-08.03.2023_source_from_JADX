package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class NullProjection extends Projection {
    public boolean isRectilinear() {
        return true;
    }

    public String toString() {
        return "Null";
    }

    public NullProjection() {
        initialize();
    }

    public ProjCoordinate project(ProjCoordinate projCoordinate, ProjCoordinate projCoordinate2) {
        projCoordinate2.f409x = projCoordinate.f409x;
        projCoordinate2.f410y = projCoordinate.f410y;
        return projCoordinate2;
    }

    public ProjCoordinate projectInverse(ProjCoordinate projCoordinate, ProjCoordinate projCoordinate2) {
        projCoordinate2.f409x = projCoordinate.f409x;
        projCoordinate2.f410y = projCoordinate.f410y;
        return projCoordinate2;
    }
}
