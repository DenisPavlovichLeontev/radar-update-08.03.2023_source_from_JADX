package org.mapsforge.core.graphics;

import org.mapsforge.core.model.BoundingBox;

public interface HillshadingBitmap extends Bitmap {
    BoundingBox getAreaRect();

    int getPadding();

    public enum Border {
        WEST(true),
        NORTH(false),
        EAST(true),
        SOUTH(false);
        
        public final boolean vertical;

        private Border(boolean z) {
            this.vertical = z;
        }
    }
}
