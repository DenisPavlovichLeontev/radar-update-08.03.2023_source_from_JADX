package org.mapsforge.map.model;

import org.mapsforge.core.model.Dimension;
import org.mapsforge.map.model.common.Observable;

public class MapViewDimension extends Observable {
    private Dimension dimension;

    public synchronized Dimension getDimension() {
        return this.dimension;
    }

    public void setDimension(Dimension dimension2) {
        synchronized (this) {
            this.dimension = dimension2;
        }
        notifyObservers();
    }
}
