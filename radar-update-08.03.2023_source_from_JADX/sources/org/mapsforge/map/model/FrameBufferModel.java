package org.mapsforge.map.model;

import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.model.common.Observable;

public class FrameBufferModel extends Observable {
    private Dimension dimension;
    private MapPosition mapPosition;
    private double overdrawFactor = 1.2d;

    public synchronized Dimension getDimension() {
        return this.dimension;
    }

    public synchronized MapPosition getMapPosition() {
        return this.mapPosition;
    }

    public synchronized double getOverdrawFactor() {
        return this.overdrawFactor;
    }

    public void setDimension(Dimension dimension2) {
        synchronized (this) {
            this.dimension = dimension2;
        }
        notifyObservers();
    }

    public void setMapPosition(MapPosition mapPosition2) {
        synchronized (this) {
            this.mapPosition = mapPosition2;
        }
        notifyObservers();
    }

    public void setOverdrawFactor(double d) {
        if (d > 0.0d) {
            synchronized (this) {
                this.overdrawFactor = d;
            }
            notifyObservers();
            return;
        }
        throw new IllegalArgumentException("overdrawFactor must be > 0: " + d);
    }
}
