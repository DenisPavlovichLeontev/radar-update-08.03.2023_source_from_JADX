package org.mapsforge.map.layer.labels;

import java.util.List;
import org.mapsforge.core.mapelements.MapElementContainer;
import org.mapsforge.core.model.Tile;

public interface LabelStore {
    void clear();

    int getVersion();

    List<MapElementContainer> getVisibleItems(Tile tile, Tile tile2);
}
