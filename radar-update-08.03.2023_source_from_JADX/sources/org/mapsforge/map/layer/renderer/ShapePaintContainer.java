package org.mapsforge.map.layer.renderer;

import org.mapsforge.core.graphics.Paint;

public class ShapePaintContainer {

    /* renamed from: dy */
    final float f386dy;
    final Paint paint;
    final ShapeContainer shapeContainer;

    public ShapePaintContainer(ShapeContainer shapeContainer2, Paint paint2) {
        this(shapeContainer2, paint2, 0.0f);
    }

    public ShapePaintContainer(ShapeContainer shapeContainer2, Paint paint2, float f) {
        this.shapeContainer = shapeContainer2;
        this.paint = paint2;
        this.f386dy = f;
    }
}
