package org.osgeo.proj4j;

public class CoordinateTransformFactory {
    public CoordinateTransform createTransform(CoordinateReferenceSystem coordinateReferenceSystem, CoordinateReferenceSystem coordinateReferenceSystem2) {
        return new BasicCoordinateTransform(coordinateReferenceSystem, coordinateReferenceSystem2);
    }
}
