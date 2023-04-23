package org.mapsforge.map.android.util;

import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.model.common.Observer;

public class MapViewPositionObserver implements Observer {
    private final IMapViewPosition observable;
    private final IMapViewPosition observer;

    public MapViewPositionObserver(IMapViewPosition iMapViewPosition, IMapViewPosition iMapViewPosition2) {
        this.observable = iMapViewPosition;
        this.observer = iMapViewPosition2;
        iMapViewPosition.addObserver(this);
    }

    public void onChange() {
        setCenter();
        setZoom();
    }

    /* access modifiers changed from: protected */
    public void setCenter() {
        if (!this.observable.getCenter().equals(this.observer.getCenter())) {
            this.observer.setCenter(this.observable.getCenter());
        }
    }

    /* access modifiers changed from: protected */
    public void setZoom() {
        if (this.observable.getZoomLevel() != this.observer.getZoomLevel()) {
            this.observer.setZoomLevel(this.observable.getZoomLevel());
        }
    }

    public void removeObserver() {
        this.observable.removeObserver(this);
    }
}
