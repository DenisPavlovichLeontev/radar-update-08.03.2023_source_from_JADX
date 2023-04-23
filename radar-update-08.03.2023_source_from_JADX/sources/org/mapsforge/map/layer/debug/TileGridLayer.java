package org.mapsforge.map.layer.debug;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.model.DisplayModel;

public class TileGridLayer extends Layer {
    private final DisplayModel displayModel;
    private final Paint paintBack;
    private final Paint paintFront;

    private static Paint createPaintFront(GraphicFactory graphicFactory, DisplayModel displayModel2) {
        Paint createPaint = graphicFactory.createPaint();
        createPaint.setColor(Color.RED);
        createPaint.setStrokeWidth(displayModel2.getScaleFactor() * 2.0f);
        createPaint.setStyle(Style.STROKE);
        return createPaint;
    }

    private static Paint createPaintBack(GraphicFactory graphicFactory, DisplayModel displayModel2) {
        Paint createPaint = graphicFactory.createPaint();
        createPaint.setColor(Color.WHITE);
        createPaint.setStrokeWidth(displayModel2.getScaleFactor() * 4.0f);
        createPaint.setStyle(Style.STROKE);
        return createPaint;
    }

    public TileGridLayer(GraphicFactory graphicFactory, DisplayModel displayModel2) {
        this.displayModel = displayModel2;
        this.paintBack = createPaintBack(graphicFactory, displayModel2);
        this.paintFront = createPaintFront(graphicFactory, displayModel2);
    }

    public TileGridLayer(DisplayModel displayModel2, Paint paint, Paint paint2) {
        this.displayModel = displayModel2;
        this.paintBack = paint;
        this.paintFront = paint2;
    }

    public void draw(BoundingBox boundingBox, byte b, Canvas canvas, Point point) {
        int i;
        int i2;
        BoundingBox boundingBox2 = boundingBox;
        byte b2 = b;
        Point point2 = point;
        int tileSize = this.displayModel.getTileSize();
        int tileToPixel = (int) (((double) MercatorProjection.tileToPixel((long) MercatorProjection.longitudeToTileX(boundingBox2.minLongitude, b2), tileSize)) - point2.f381x);
        int tileToPixel2 = (int) (((double) MercatorProjection.tileToPixel((long) MercatorProjection.latitudeToTileY(boundingBox2.maxLatitude, b2), tileSize)) - point2.f382y);
        double d = (double) tileSize;
        int tileToPixel3 = (int) ((((double) MercatorProjection.tileToPixel((long) MercatorProjection.longitudeToTileX(boundingBox2.maxLongitude, b2), tileSize)) - point2.f381x) + d);
        int tileToPixel4 = (int) ((((double) MercatorProjection.tileToPixel((long) MercatorProjection.latitudeToTileY(boundingBox2.minLatitude, b2), tileSize)) - point2.f382y) + d);
        int i3 = tileToPixel;
        while (true) {
            i = tileToPixel3 + 1;
            if (i3 > i) {
                break;
            }
            canvas.drawLine(i3, tileToPixel2, i3, tileToPixel4, this.paintBack);
            i3 += tileSize;
        }
        int i4 = tileToPixel2;
        while (true) {
            i2 = tileToPixel4 + 1;
            if (i4 > i2) {
                break;
            }
            canvas.drawLine(tileToPixel, i4, tileToPixel3, i4, this.paintBack);
            i4 += tileSize;
        }
        for (int i5 = tileToPixel; i5 <= i; i5 += tileSize) {
            canvas.drawLine(i5, tileToPixel2, i5, tileToPixel4, this.paintFront);
        }
        while (tileToPixel2 <= i2) {
            canvas.drawLine(tileToPixel, tileToPixel2, tileToPixel3, tileToPixel2, this.paintFront);
            tileToPixel2 += tileSize;
        }
    }
}
