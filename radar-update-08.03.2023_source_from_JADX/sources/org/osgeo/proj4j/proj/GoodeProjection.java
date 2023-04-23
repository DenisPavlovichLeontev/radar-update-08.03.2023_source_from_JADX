package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class GoodeProjection extends Projection {
    private static final double PHI_LIM = 0.7109307819790236d;
    private static final double Y_COR = 0.0528d;
    private MolleweideProjection moll = new MolleweideProjection();
    private SinusoidalProjection sinu = new SinusoidalProjection();

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Goode Homolosine";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        if (Math.abs(d2) <= PHI_LIM) {
            return this.sinu.project(d, d2, projCoordinate);
        }
        ProjCoordinate project = this.moll.project(d, d2, projCoordinate);
        project.f410y -= d2 >= 0.0d ? Y_COR : -0.0528d;
        return project;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        if (Math.abs(d2) <= PHI_LIM) {
            return this.sinu.projectInverse(d, d2, projCoordinate);
        }
        return this.moll.projectInverse(d, d2 + (d2 >= 0.0d ? Y_COR : -0.0528d), projCoordinate);
    }
}
