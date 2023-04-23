package org.mapsforge.map.controller;

import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.model.common.Observer;

public final class LayerManagerController implements Observer {
    private final LayerManager layerManager;

    public static LayerManagerController create(LayerManager layerManager2, Model model) {
        LayerManagerController layerManagerController = new LayerManagerController(layerManager2);
        model.mapViewDimension.addObserver(layerManagerController);
        model.mapViewPosition.addObserver(layerManagerController);
        return layerManagerController;
    }

    private LayerManagerController(LayerManager layerManager2) {
        this.layerManager = layerManager2;
    }

    public void onChange() {
        this.layerManager.redrawLayers();
    }
}
