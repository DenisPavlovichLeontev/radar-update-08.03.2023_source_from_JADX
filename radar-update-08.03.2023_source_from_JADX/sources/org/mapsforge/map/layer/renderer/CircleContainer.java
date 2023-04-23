package org.mapsforge.map.layer.renderer;

import org.mapsforge.core.model.Point;

class CircleContainer implements ShapeContainer {
    final Point point;
    final float radius;

    CircleContainer(Point point2, float f) {
        this.point = point2;
        this.radius = f;
    }

    public ShapeType getShapeType() {
        return ShapeType.CIRCLE;
    }
}
