package org.mapsforge.map.model;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.model.common.Observer;
import org.mapsforge.map.model.common.PreferencesFacade;

public interface IMapViewPosition {
    void addObserver(Observer observer);

    void animateTo(LatLong latLong);

    boolean animationInProgress();

    void destroy();

    LatLong getCenter();

    BoundingBox getMapLimit();

    MapPosition getMapPosition();

    LatLong getPivot();

    double getScaleFactor();

    byte getZoomLevel();

    byte getZoomLevelMax();

    byte getZoomLevelMin();

    void init(PreferencesFacade preferencesFacade);

    void moveCenter(double d, double d2);

    void moveCenter(double d, double d2, boolean z);

    void moveCenterAndZoom(double d, double d2, byte b);

    void removeObserver(Observer observer);

    void save(PreferencesFacade preferencesFacade);

    void setCenter(LatLong latLong);

    void setMapLimit(BoundingBox boundingBox);

    void setMapPosition(MapPosition mapPosition);

    void setMapPosition(MapPosition mapPosition, boolean z);

    void setPivot(LatLong latLong);

    void setScaleFactorAdjustment(double d);

    void setZoomLevel(byte b);

    void setZoomLevel(byte b, boolean z);

    void setZoomLevelMax(byte b);

    void setZoomLevelMin(byte b);

    void zoom(byte b);

    void zoomIn();

    void zoomOut();
}
