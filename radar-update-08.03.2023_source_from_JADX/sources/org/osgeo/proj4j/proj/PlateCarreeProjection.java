package org.osgeo.proj4j.proj;

public class PlateCarreeProjection extends CylindricalProjection {
    public boolean hasInverse() {
        return true;
    }

    public boolean isRectilinear() {
        return true;
    }

    public String toString() {
        return "Plate Carrée";
    }
}
