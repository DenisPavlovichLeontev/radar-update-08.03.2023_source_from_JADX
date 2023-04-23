package org.mapsforge.map.layer;

import java.util.ArrayList;
import java.util.List;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.model.DisplayModel;

public class GroupLayer extends Layer {
    public final List<Layer> layers = new ArrayList();

    public void draw(BoundingBox boundingBox, byte b, Canvas canvas, Point point) {
        for (Layer draw : this.layers) {
            draw.draw(boundingBox, b, canvas, point);
        }
    }

    public void onDestroy() {
        for (Layer onDestroy : this.layers) {
            onDestroy.onDestroy();
        }
    }

    public boolean onLongPress(LatLong latLong, Point point, Point point2) {
        for (int size = this.layers.size() - 1; size >= 0; size--) {
            if (this.layers.get(size).onLongPress(latLong, point, point2)) {
                return true;
            }
        }
        return false;
    }

    public boolean onTap(LatLong latLong, Point point, Point point2) {
        for (int size = this.layers.size() - 1; size >= 0; size--) {
            if (this.layers.get(size).onTap(latLong, point, point2)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void setDisplayModel(DisplayModel displayModel) {
        super.setDisplayModel(displayModel);
        for (Layer displayModel2 : this.layers) {
            displayModel2.setDisplayModel(displayModel);
        }
    }
}
