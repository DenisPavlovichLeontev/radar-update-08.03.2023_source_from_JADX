package org.mapsforge.map.layer.renderer;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.HillshadingBitmap;
import org.mapsforge.core.model.Rectangle;

public class HillshadingContainer implements ShapeContainer {
    public final Bitmap bitmap;
    public final Rectangle hillsRect;
    public final float magnitude;
    public final Rectangle tileRect;

    public HillshadingContainer(HillshadingBitmap hillshadingBitmap, float f, Rectangle rectangle, Rectangle rectangle2) {
        this.magnitude = f;
        this.bitmap = hillshadingBitmap;
        this.hillsRect = rectangle;
        this.tileRect = rectangle2;
    }

    public ShapeType getShapeType() {
        return ShapeType.HILLSHADING;
    }

    public String toString() {
        return "[Hillshading:" + this.magnitude + "#" + System.identityHashCode(this.bitmap) + "\n @# " + this.hillsRect + "\n -> " + this.tileRect + "\n]";
    }
}
