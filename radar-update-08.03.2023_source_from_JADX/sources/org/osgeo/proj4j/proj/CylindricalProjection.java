package org.osgeo.proj4j.proj;

public abstract class CylindricalProjection extends Projection {
    public boolean isRectilinear() {
        return true;
    }

    public String toString() {
        return "Cylindrical";
    }
}
