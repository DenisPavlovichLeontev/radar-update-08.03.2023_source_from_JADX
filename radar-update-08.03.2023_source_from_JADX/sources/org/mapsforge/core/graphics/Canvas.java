package org.mapsforge.core.graphics;

import org.mapsforge.core.model.Dimension;

public interface Canvas extends GraphicContext {
    void destroy();

    Dimension getDimension();

    int getHeight();

    int getWidth();

    void setBitmap(Bitmap bitmap);
}
