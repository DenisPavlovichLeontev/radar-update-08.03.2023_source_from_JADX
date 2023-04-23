package org.osgeo.proj4j;

public interface CoordinateTransform {
    CoordinateReferenceSystem getSourceCRS();

    CoordinateReferenceSystem getTargetCRS();

    ProjCoordinate transform(ProjCoordinate projCoordinate, ProjCoordinate projCoordinate2) throws Proj4jException;
}
