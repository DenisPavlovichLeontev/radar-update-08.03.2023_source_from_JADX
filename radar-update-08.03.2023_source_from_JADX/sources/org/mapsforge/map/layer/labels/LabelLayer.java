package org.mapsforge.map.layer.labels;

import java.util.Collections;
import java.util.List;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.mapelements.MapElementContainer;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.util.LayerUtil;

public class LabelLayer extends Layer {
    protected List<MapElementContainer> elementsToDraw;
    protected final LabelStore labelStore;
    protected int lastLabelStoreVersion = -1;
    protected Tile lowerRight;
    protected final Matrix matrix;
    protected Tile upperLeft;

    public LabelLayer(GraphicFactory graphicFactory, LabelStore labelStore2) {
        this.labelStore = labelStore2;
        this.matrix = graphicFactory.createMatrix();
    }

    public void draw(BoundingBox boundingBox, byte b, Canvas canvas, Point point) {
        Tile upperLeft2 = LayerUtil.getUpperLeft(boundingBox, b, this.displayModel.getTileSize());
        Tile lowerRight2 = LayerUtil.getLowerRight(boundingBox, b, this.displayModel.getTileSize());
        if (!upperLeft2.equals(this.upperLeft) || !lowerRight2.equals(this.lowerRight) || this.lastLabelStoreVersion != this.labelStore.getVersion()) {
            this.upperLeft = upperLeft2;
            this.lowerRight = lowerRight2;
            this.lastLabelStoreVersion = this.labelStore.getVersion();
            List<MapElementContainer> collisionFreeOrdered = LayerUtil.collisionFreeOrdered(this.labelStore.getVisibleItems(this.upperLeft, this.lowerRight));
            this.elementsToDraw = collisionFreeOrdered;
            Collections.sort(collisionFreeOrdered);
        }
        draw(canvas, point);
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas, Point point) {
        for (MapElementContainer draw : this.elementsToDraw) {
            draw.draw(canvas, point, this.matrix, this.displayModel.getFilter());
        }
    }
}
