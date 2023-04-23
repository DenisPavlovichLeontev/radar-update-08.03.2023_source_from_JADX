package org.mapsforge.map.controller;

import org.mapsforge.map.model.Model;
import org.mapsforge.map.model.common.Observer;
import org.mapsforge.map.view.MapView;

public final class MapViewController implements Observer {
    private final MapView mapView;

    public static MapViewController create(MapView mapView2, Model model) {
        MapViewController mapViewController = new MapViewController(mapView2);
        model.mapViewPosition.addObserver(mapViewController);
        return mapViewController;
    }

    private MapViewController(MapView mapView2) {
        this.mapView = mapView2;
    }

    public void onChange() {
        this.mapView.repaint();
    }
}
