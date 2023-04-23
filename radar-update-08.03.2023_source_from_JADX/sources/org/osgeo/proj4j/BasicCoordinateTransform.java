package org.osgeo.proj4j;

import org.osgeo.proj4j.datum.GeocentricConverter;

public class BasicCoordinateTransform implements CoordinateTransform {
    private boolean doDatumTransform;
    private boolean doForwardProjection = true;
    private boolean doInverseProjection = true;
    private ProjCoordinate geoCoord = new ProjCoordinate(0.0d, 0.0d);
    private CoordinateReferenceSystem srcCRS;
    private GeocentricConverter srcGeoConv;
    private CoordinateReferenceSystem tgtCRS;
    private GeocentricConverter tgtGeoConv;
    private boolean transformViaGeocentric;

    public BasicCoordinateTransform(CoordinateReferenceSystem coordinateReferenceSystem, CoordinateReferenceSystem coordinateReferenceSystem2) {
        boolean z = false;
        this.doDatumTransform = false;
        this.transformViaGeocentric = false;
        this.srcCRS = coordinateReferenceSystem;
        this.tgtCRS = coordinateReferenceSystem2;
        this.doInverseProjection = (coordinateReferenceSystem == null || coordinateReferenceSystem == CoordinateReferenceSystem.CS_GEO) ? false : true;
        boolean z2 = (coordinateReferenceSystem2 == null || coordinateReferenceSystem2 == CoordinateReferenceSystem.CS_GEO) ? false : true;
        this.doForwardProjection = z2;
        if (this.doInverseProjection && z2 && coordinateReferenceSystem.getDatum() != coordinateReferenceSystem2.getDatum()) {
            z = true;
        }
        this.doDatumTransform = z;
        if (z) {
            if (!coordinateReferenceSystem.getDatum().getEllipsoid().isEqual(coordinateReferenceSystem2.getDatum().getEllipsoid())) {
                this.transformViaGeocentric = true;
            }
            if (coordinateReferenceSystem.getDatum().hasTransformToWGS84() || coordinateReferenceSystem2.getDatum().hasTransformToWGS84()) {
                this.transformViaGeocentric = true;
            }
            if (this.transformViaGeocentric) {
                this.srcGeoConv = new GeocentricConverter(coordinateReferenceSystem.getDatum().getEllipsoid());
                this.tgtGeoConv = new GeocentricConverter(coordinateReferenceSystem2.getDatum().getEllipsoid());
            }
        }
    }

    public CoordinateReferenceSystem getSourceCRS() {
        return this.srcCRS;
    }

    public CoordinateReferenceSystem getTargetCRS() {
        return this.tgtCRS;
    }

    public ProjCoordinate transform(ProjCoordinate projCoordinate, ProjCoordinate projCoordinate2) throws Proj4jException {
        if (this.doInverseProjection) {
            this.srcCRS.getProjection().inverseProjectRadians(projCoordinate, this.geoCoord);
        } else {
            this.geoCoord.setValue(projCoordinate);
        }
        this.geoCoord.clearZ();
        if (this.doDatumTransform) {
            datumTransform(this.geoCoord);
        }
        if (this.doForwardProjection) {
            this.tgtCRS.getProjection().projectRadians(this.geoCoord, projCoordinate2);
        } else {
            projCoordinate2.setValue(this.geoCoord);
        }
        return projCoordinate2;
    }

    private void datumTransform(ProjCoordinate projCoordinate) {
        if (!this.srcCRS.getDatum().isEqual(this.tgtCRS.getDatum()) && this.transformViaGeocentric) {
            this.srcGeoConv.convertGeodeticToGeocentric(projCoordinate);
            if (this.srcCRS.getDatum().hasTransformToWGS84()) {
                this.srcCRS.getDatum().transformFromGeocentricToWgs84(projCoordinate);
            }
            if (this.tgtCRS.getDatum().hasTransformToWGS84()) {
                this.tgtCRS.getDatum().transformToGeocentricFromWgs84(projCoordinate);
            }
            this.tgtGeoConv.convertGeocentricToGeodetic(projCoordinate);
        }
    }
}
