package org.mapsforge.map.layer.debug;

import java.util.List;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.FontFamily;
import org.mapsforge.core.graphics.FontStyle;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.TilePosition;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.util.LayerUtil;

public class TileCoordinatesLayer extends Layer {
    private final DisplayModel displayModel;
    private boolean drawSimple;
    private final Paint paintBack;
    private final Paint paintFront;
    private final StringBuilder stringBuilder = new StringBuilder();

    private static Paint createPaintFront(GraphicFactory graphicFactory, DisplayModel displayModel2) {
        Paint createPaint = graphicFactory.createPaint();
        createPaint.setColor(Color.RED);
        createPaint.setTypeface(FontFamily.DEFAULT, FontStyle.BOLD);
        createPaint.setTextSize(displayModel2.getScaleFactor() * 12.0f);
        return createPaint;
    }

    private static Paint createPaintBack(GraphicFactory graphicFactory, DisplayModel displayModel2) {
        Paint createPaint = graphicFactory.createPaint();
        createPaint.setColor(Color.WHITE);
        createPaint.setTypeface(FontFamily.DEFAULT, FontStyle.BOLD);
        createPaint.setTextSize(displayModel2.getScaleFactor() * 12.0f);
        createPaint.setStrokeWidth(displayModel2.getScaleFactor() * 2.0f);
        createPaint.setStyle(Style.STROKE);
        return createPaint;
    }

    public TileCoordinatesLayer(GraphicFactory graphicFactory, DisplayModel displayModel2) {
        this.displayModel = displayModel2;
        this.paintBack = createPaintBack(graphicFactory, displayModel2);
        this.paintFront = createPaintFront(graphicFactory, displayModel2);
    }

    public TileCoordinatesLayer(DisplayModel displayModel2, Paint paint, Paint paint2) {
        this.displayModel = displayModel2;
        this.paintBack = paint;
        this.paintFront = paint2;
    }

    public void draw(BoundingBox boundingBox, byte b, Canvas canvas, Point point) {
        List<TilePosition> tilePositions = LayerUtil.getTilePositions(boundingBox, b, point, this.displayModel.getTileSize());
        for (int size = tilePositions.size() - 1; size >= 0; size--) {
            drawTileCoordinates(tilePositions.get(size), canvas);
        }
    }

    private void drawTileCoordinates(TilePosition tilePosition, Canvas canvas) {
        Tile tile = tilePosition.tile;
        if (this.drawSimple) {
            this.stringBuilder.setLength(0);
            StringBuilder sb = this.stringBuilder;
            sb.append(tile.zoomLevel);
            sb.append(" / ");
            sb.append(tile.tileX);
            sb.append(" / ");
            sb.append(tile.tileY);
            String sb2 = this.stringBuilder.toString();
            int textWidth = (int) (tilePosition.point.f381x + ((double) ((tile.tileSize - this.paintBack.getTextWidth(sb2)) / 2)));
            int textHeight = (int) (tilePosition.point.f382y + ((double) ((tile.tileSize + this.paintBack.getTextHeight(sb2)) / 2)));
            canvas.drawText(sb2, textWidth, textHeight, this.paintBack);
            canvas.drawText(sb2, textWidth, textHeight, this.paintFront);
            return;
        }
        int scaleFactor = (int) (tilePosition.point.f381x + ((double) (this.displayModel.getScaleFactor() * 8.0f)));
        int scaleFactor2 = (int) (tilePosition.point.f382y + ((double) (this.displayModel.getScaleFactor() * 24.0f)));
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("X: ");
        this.stringBuilder.append(tile.tileX);
        String sb3 = this.stringBuilder.toString();
        canvas.drawText(sb3, scaleFactor, scaleFactor2, this.paintBack);
        canvas.drawText(sb3, scaleFactor, scaleFactor2, this.paintFront);
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("Y: ");
        this.stringBuilder.append(tile.tileY);
        String sb4 = this.stringBuilder.toString();
        float f = (float) scaleFactor2;
        canvas.drawText(sb4, scaleFactor, (int) ((this.displayModel.getScaleFactor() * 24.0f) + f), this.paintBack);
        canvas.drawText(sb4, scaleFactor, (int) ((this.displayModel.getScaleFactor() * 24.0f) + f), this.paintFront);
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("Z: ");
        this.stringBuilder.append(tile.zoomLevel);
        String sb5 = this.stringBuilder.toString();
        canvas.drawText(sb5, scaleFactor, (int) ((this.displayModel.getScaleFactor() * 48.0f) + f), this.paintBack);
        canvas.drawText(sb5, scaleFactor, (int) (f + (this.displayModel.getScaleFactor() * 48.0f)), this.paintFront);
    }

    public boolean isDrawSimple() {
        return this.drawSimple;
    }

    public void setDrawSimple(boolean z) {
        this.drawSimple = z;
    }
}
