package org.mapsforge.map.layer.tilestore;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.TileLayer;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.IMapViewPosition;

public class TileStoreLayer extends TileLayer<Job> {
    /* access modifiers changed from: protected */
    public boolean isTileStale(Tile tile, TileBitmap tileBitmap) {
        return false;
    }

    public TileStoreLayer(TileCache tileCache, IMapViewPosition iMapViewPosition, GraphicFactory graphicFactory, boolean z) {
        super(tileCache, iMapViewPosition, graphicFactory.createMatrix(), z, false);
    }

    /* access modifiers changed from: protected */
    public Job createJob(Tile tile) {
        return new Job(tile, this.isTransparent);
    }
}
