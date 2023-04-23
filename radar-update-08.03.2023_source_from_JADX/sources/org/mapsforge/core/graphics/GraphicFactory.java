package org.mapsforge.core.graphics;

import java.io.IOException;
import java.io.InputStream;
import org.mapsforge.core.mapelements.PointTextContainer;
import org.mapsforge.core.mapelements.SymbolContainer;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

public interface GraphicFactory {
    Bitmap createBitmap(int i, int i2);

    Bitmap createBitmap(int i, int i2, boolean z);

    Canvas createCanvas();

    int createColor(int i, int i2, int i3, int i4);

    int createColor(Color color);

    Matrix createMatrix();

    HillshadingBitmap createMonoBitmap(int i, int i2, byte[] bArr, int i3, BoundingBox boundingBox);

    Paint createPaint();

    Paint createPaint(Paint paint);

    Path createPath();

    PointTextContainer createPointTextContainer(Point point, Display display, int i, String str, Paint paint, Paint paint2, SymbolContainer symbolContainer, Position position, int i2);

    ResourceBitmap createResourceBitmap(InputStream inputStream, float f, int i, int i2, int i3, int i4) throws IOException;

    TileBitmap createTileBitmap(int i, boolean z);

    TileBitmap createTileBitmap(InputStream inputStream, int i, boolean z) throws IOException;

    InputStream platformSpecificSources(String str, String str2) throws IOException;

    ResourceBitmap renderSvg(InputStream inputStream, float f, int i, int i2, int i3, int i4) throws IOException;
}
