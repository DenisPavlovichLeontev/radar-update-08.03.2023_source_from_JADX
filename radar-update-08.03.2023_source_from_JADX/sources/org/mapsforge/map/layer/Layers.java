package org.mapsforge.map.layer;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.CopyOnWriteArrayList;
import org.mapsforge.map.model.DisplayModel;

public class Layers implements Iterable<Layer>, RandomAccess {
    private final DisplayModel displayModel;
    private final List<Layer> layersList = new CopyOnWriteArrayList();
    private final Redrawer redrawer;

    private static void checkIsNull(Collection<Layer> collection) {
        if (collection != null) {
            for (Layer checkIsNull : collection) {
                checkIsNull(checkIsNull);
            }
            return;
        }
        throw new IllegalArgumentException("layers must not be null");
    }

    private static void checkIsNull(Layer layer) {
        if (layer == null) {
            throw new IllegalArgumentException("layer must not be null");
        }
    }

    Layers(Redrawer redrawer2, DisplayModel displayModel2) {
        this.redrawer = redrawer2;
        this.displayModel = displayModel2;
    }

    public synchronized void add(int i, Layer layer) {
        add(i, layer, true);
    }

    public synchronized void add(int i, Layer layer, boolean z) {
        checkIsNull(layer);
        layer.setDisplayModel(this.displayModel);
        this.layersList.add(i, layer);
        layer.assign(this.redrawer);
        if (z) {
            this.redrawer.redrawLayers();
        }
    }

    public synchronized void add(Layer layer) {
        add(layer, true);
    }

    public synchronized void add(Layer layer, boolean z) {
        checkIsNull(layer);
        layer.setDisplayModel(this.displayModel);
        this.layersList.add(layer);
        layer.assign(this.redrawer);
        if (z) {
            this.redrawer.redrawLayers();
        }
    }

    public synchronized void addAll(Collection<Layer> collection) {
        addAll(collection, true);
    }

    public synchronized void addAll(Collection<Layer> collection, boolean z) {
        checkIsNull(collection);
        for (Layer displayModel2 : collection) {
            displayModel2.setDisplayModel(this.displayModel);
        }
        this.layersList.addAll(collection);
        for (Layer assign : collection) {
            assign.assign(this.redrawer);
        }
        if (z) {
            this.redrawer.redrawLayers();
        }
    }

    public synchronized void addAll(int i, Collection<Layer> collection) {
        addAll(i, collection, true);
    }

    public synchronized void addAll(int i, Collection<Layer> collection, boolean z) {
        checkIsNull(collection);
        this.layersList.addAll(i, collection);
        for (Layer next : collection) {
            next.setDisplayModel(this.displayModel);
            next.assign(this.redrawer);
        }
        if (z) {
            this.redrawer.redrawLayers();
        }
    }

    public synchronized void clear() {
        clear(true);
    }

    public synchronized void clear(boolean z) {
        for (Layer unassign : this.layersList) {
            unassign.unassign();
        }
        this.layersList.clear();
        if (z) {
            this.redrawer.redrawLayers();
        }
    }

    public synchronized boolean contains(Layer layer) {
        checkIsNull(layer);
        return this.layersList.contains(layer);
    }

    public synchronized Layer get(int i) {
        return this.layersList.get(i);
    }

    public synchronized int indexOf(Layer layer) {
        checkIsNull(layer);
        return this.layersList.indexOf(layer);
    }

    public synchronized boolean isEmpty() {
        return this.layersList.isEmpty();
    }

    public synchronized Iterator<Layer> iterator() {
        return this.layersList.iterator();
    }

    public synchronized Layer remove(int i) {
        return remove(i, true);
    }

    public synchronized Layer remove(int i, boolean z) {
        Layer remove;
        remove = this.layersList.remove(i);
        remove.unassign();
        if (z) {
            this.redrawer.redrawLayers();
        }
        return remove;
    }

    public synchronized boolean remove(Layer layer) {
        return remove(layer, true);
    }

    public synchronized boolean remove(Layer layer, boolean z) {
        checkIsNull(layer);
        if (!this.layersList.remove(layer)) {
            return false;
        }
        layer.unassign();
        if (z) {
            this.redrawer.redrawLayers();
        }
        return true;
    }

    public synchronized int size() {
        return this.layersList.size();
    }
}
