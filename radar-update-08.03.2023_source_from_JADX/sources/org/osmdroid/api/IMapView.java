package org.osmdroid.api;

public interface IMapView {
    public static final String LOGTAG = "OsmDroid";

    IMapController getController();

    double getLatitudeSpanDouble();

    double getLongitudeSpanDouble();

    IGeoPoint getMapCenter();

    double getMaxZoomLevel();

    IProjection getProjection();

    @Deprecated
    int getZoomLevel();

    double getZoomLevelDouble();

    void setBackgroundColor(int i);
}
