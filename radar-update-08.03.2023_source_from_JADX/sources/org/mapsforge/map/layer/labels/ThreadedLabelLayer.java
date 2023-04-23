package org.mapsforge.map.layer.labels;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.mapelements.MapElementContainer;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.util.LayerUtil;

public class ThreadedLabelLayer extends LabelLayer {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<?> future;
    Tile requestedLowerRight;
    Tile requestedUpperLeft;

    public ThreadedLabelLayer(GraphicFactory graphicFactory, LabelStore labelStore) {
        super(graphicFactory, labelStore);
    }

    public void draw(BoundingBox boundingBox, byte b, Canvas canvas, Point point) {
        Tile upperLeft = LayerUtil.getUpperLeft(boundingBox, b, this.displayModel.getTileSize());
        Tile lowerRight = LayerUtil.getLowerRight(boundingBox, b, this.displayModel.getTileSize());
        if (!upperLeft.equals(this.upperLeft) || !lowerRight.equals(this.lowerRight) || this.lastLabelStoreVersion != this.labelStore.getVersion()) {
            getData(upperLeft, lowerRight);
        }
        if (this.upperLeft != null && Tile.tileAreasOverlap(this.upperLeft, this.lowerRight, upperLeft, lowerRight)) {
            draw(canvas, point);
        }
    }

    /* access modifiers changed from: protected */
    public void getData(final Tile tile, final Tile tile2) {
        if (!tile.equals(this.requestedUpperLeft) || !tile2.equals(this.requestedLowerRight)) {
            this.requestedUpperLeft = tile;
            this.requestedLowerRight = tile2;
            Future<?> future2 = this.future;
            if (future2 != null) {
                future2.cancel(false);
            }
            this.future = this.executorService.submit(new Runnable() {
                public void run() {
                    List<MapElementContainer> visibleItems = ThreadedLabelLayer.this.labelStore.getVisibleItems(tile, tile2);
                    ThreadedLabelLayer.this.elementsToDraw = LayerUtil.collisionFreeOrdered(visibleItems);
                    Collections.sort(ThreadedLabelLayer.this.elementsToDraw);
                    ThreadedLabelLayer.this.upperLeft = tile;
                    ThreadedLabelLayer.this.lowerRight = tile2;
                    ThreadedLabelLayer threadedLabelLayer = ThreadedLabelLayer.this;
                    threadedLabelLayer.lastLabelStoreVersion = threadedLabelLayer.labelStore.getVersion();
                    ThreadedLabelLayer.this.requestRedraw();
                }
            });
        }
    }

    public void onDestroy() {
        this.executorService.shutdownNow();
    }
}
