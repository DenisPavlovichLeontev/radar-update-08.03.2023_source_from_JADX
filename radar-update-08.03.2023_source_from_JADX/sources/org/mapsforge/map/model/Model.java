package org.mapsforge.map.model;

import org.mapsforge.core.util.Parameters;
import org.mapsforge.map.model.common.Persistable;
import org.mapsforge.map.model.common.PreferencesFacade;

public class Model implements Persistable {
    public final DisplayModel displayModel;
    public final FrameBufferModel frameBufferModel = new FrameBufferModel();
    public final MapViewDimension mapViewDimension = new MapViewDimension();
    public final IMapViewPosition mapViewPosition;

    public Model() {
        DisplayModel displayModel2 = new DisplayModel();
        this.displayModel = displayModel2;
        if (Parameters.MAP_VIEW_POSITION2) {
            this.mapViewPosition = new MapViewPosition2(displayModel2);
        } else {
            this.mapViewPosition = new MapViewPosition(displayModel2);
        }
    }

    public void init(PreferencesFacade preferencesFacade) {
        this.mapViewPosition.init(preferencesFacade);
    }

    public void save(PreferencesFacade preferencesFacade) {
        this.mapViewPosition.save(preferencesFacade);
    }
}
