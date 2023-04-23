package org.mapsforge.map.android.graphics;

import org.mapsforge.core.graphics.HillshadingBitmap;
import org.mapsforge.core.model.BoundingBox;

public class AndroidHillshadingBitmap extends AndroidBitmap implements HillshadingBitmap {
    private final BoundingBox areaRect;
    private final int padding;

    public AndroidHillshadingBitmap(int i, int i2, int i3, BoundingBox boundingBox) {
        super(i, i2, AndroidGraphicFactory.MONO_ALPHA_BITMAP);
        this.padding = i3;
        this.areaRect = boundingBox;
    }

    public BoundingBox getAreaRect() {
        return this.areaRect;
    }

    public int getPadding() {
        return this.padding;
    }
}
