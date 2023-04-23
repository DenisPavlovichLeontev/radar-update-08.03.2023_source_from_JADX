package org.mapsforge.map.view;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.scalebar.MapScaleBar;
import org.mapsforge.map.util.MapViewProjection;

public interface MapView {
    void addLayer(Layer layer);

    void destroy();

    void destroyAll();

    BoundingBox getBoundingBox();

    Dimension getDimension();

    FpsCounter getFpsCounter();

    FrameBuffer getFrameBuffer();

    int getHeight();

    LayerManager getLayerManager();

    MapScaleBar getMapScaleBar();

    MapViewProjection getMapViewProjection();

    Model getModel();

    int getWidth();

    void repaint();

    void setCenter(LatLong latLong);

    void setMapScaleBar(MapScaleBar mapScaleBar);

    void setZoomLevel(byte b);

    void setZoomLevelMax(byte b);

    void setZoomLevelMin(byte b);
}
