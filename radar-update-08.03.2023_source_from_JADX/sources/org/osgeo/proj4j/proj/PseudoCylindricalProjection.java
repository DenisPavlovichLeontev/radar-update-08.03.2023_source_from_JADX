package org.osgeo.proj4j.proj;

public class PseudoCylindricalProjection extends CylindricalProjection {
    public boolean isRectilinear() {
        return false;
    }

    public String toString() {
        return "Pseudo Cylindrical";
    }
}
