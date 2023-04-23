package org.mapsforge.map.layer.labels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.mapsforge.core.mapelements.MapElementContainer;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.WorkingSetCache;
import org.mapsforge.map.util.LayerUtil;

public class TileBasedLabelStore extends WorkingSetCache<Tile, List<MapElementContainer>> implements LabelStore {
    private static final long serialVersionUID = 1;
    private Set<Tile> lastVisibleTileSet = new HashSet();
    private int version;

    public TileBasedLabelStore(int i) {
        super(i);
    }

    public void destroy() {
        clear();
    }

    public synchronized void storeMapItems(Tile tile, List<MapElementContainer> list) {
        put(tile, LayerUtil.collisionFreeOrdered(list));
        this.version++;
    }

    public int getVersion() {
        return this.version;
    }

    public synchronized List<MapElementContainer> getVisibleItems(Tile tile, Tile tile2) {
        return getVisibleItems(LayerUtil.getTiles(tile, tile2));
    }

    private synchronized List<MapElementContainer> getVisibleItems(Set<Tile> set) {
        ArrayList arrayList;
        this.lastVisibleTileSet = set;
        arrayList = new ArrayList();
        for (Tile next : this.lastVisibleTileSet) {
            if (containsKey(next)) {
                arrayList.addAll((Collection) get(next));
            }
        }
        return arrayList;
    }

    public synchronized boolean requiresTile(Tile tile) {
        return this.lastVisibleTileSet.contains(tile) && !containsKey(tile);
    }

    /* access modifiers changed from: protected */
    public boolean removeEldestEntry(Map.Entry<Tile, List<MapElementContainer>> entry) {
        return size() > this.capacity;
    }
}
