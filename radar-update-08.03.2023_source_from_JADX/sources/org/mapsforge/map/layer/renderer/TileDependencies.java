package org.mapsforge.map.layer.renderer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.mapsforge.core.mapelements.MapElementContainer;
import org.mapsforge.core.model.Tile;

public class TileDependencies {
    Map<Tile, Map<Tile, Set<MapElementContainer>>> overlapData = new HashMap();
    Set<Tile> tilesInProgress = new HashSet();

    TileDependencies() {
    }

    /* access modifiers changed from: package-private */
    public void addOverlappingElement(Tile tile, Tile tile2, MapElementContainer mapElementContainer) {
        if (!this.overlapData.containsKey(tile)) {
            this.overlapData.put(tile, new HashMap());
        }
        if (!this.overlapData.get(tile).containsKey(tile2)) {
            this.overlapData.get(tile).put(tile2, new HashSet());
        }
        ((Set) this.overlapData.get(tile).get(tile2)).add(mapElementContainer);
    }

    /* access modifiers changed from: package-private */
    public Set<MapElementContainer> getOverlappingElements(Tile tile, Tile tile2) {
        if (!this.overlapData.containsKey(tile) || !this.overlapData.get(tile).containsKey(tile2)) {
            return new HashSet(0);
        }
        return (Set) this.overlapData.get(tile).get(tile2);
    }

    /* access modifiers changed from: package-private */
    public void removeTileData(Tile tile) {
        this.overlapData.remove(tile);
    }

    /* access modifiers changed from: package-private */
    public void removeTileData(Tile tile, Tile tile2) {
        if (this.overlapData.containsKey(tile)) {
            this.overlapData.get(tile).remove(tile2);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean isTileInProgress(Tile tile) {
        return this.tilesInProgress.contains(tile);
    }

    /* access modifiers changed from: package-private */
    public synchronized void addTileInProgress(Tile tile) {
        this.tilesInProgress.add(tile);
    }

    /* access modifiers changed from: package-private */
    public synchronized void removeTileInProgress(Tile tile) {
        this.tilesInProgress.remove(tile);
    }
}
