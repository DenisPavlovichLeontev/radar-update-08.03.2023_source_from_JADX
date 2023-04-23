package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class LarriveeProjection extends Projection {
    private static final double SIXTH = 0.16666666666666666d;

    public String toString() {
        return "Larrivee";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = d * 0.5d * (Math.sqrt(Math.cos(d2)) + 1.0d);
        projCoordinate.f410y = d2 / (Math.cos(0.5d * d2) * Math.cos(d * SIXTH));
        return projCoordinate;
    }
}
