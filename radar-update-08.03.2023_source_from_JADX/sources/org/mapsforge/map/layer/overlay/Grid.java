package org.mapsforge.map.layer.overlay;

import java.text.DecimalFormat;
import java.util.Map;
import kotlin.text.Typography;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.FontFamily;
import org.mapsforge.core.graphics.FontStyle;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.model.DisplayModel;

public class Grid extends Layer {
    private final Paint lineBack;
    private final Paint lineFront;
    private final Map<Byte, Double> spacingConfig;
    private final Paint textBack;
    private final Paint textFront;

    private static String convertCoordinate(double d) {
        StringBuilder sb = new StringBuilder();
        if (d < 0.0d) {
            sb.append('-');
            d = -d;
        }
        DecimalFormat decimalFormat = new DecimalFormat("00");
        int floor = (int) Math.floor(d);
        sb.append(decimalFormat.format((long) floor));
        sb.append(176);
        double d2 = (d - ((double) floor)) * 60.0d;
        int floor2 = (int) Math.floor(d2);
        sb.append(decimalFormat.format((long) floor2));
        sb.append(Typography.prime);
        sb.append(decimalFormat.format((d2 - ((double) floor2)) * 60.0d));
        sb.append(Typography.doublePrime);
        return sb.toString();
    }

    private static Paint createLineFront(GraphicFactory graphicFactory, DisplayModel displayModel) {
        Paint createPaint = graphicFactory.createPaint();
        createPaint.setColor(Color.BLUE);
        createPaint.setStrokeWidth(displayModel.getScaleFactor() * 2.0f);
        createPaint.setStyle(Style.STROKE);
        return createPaint;
    }

    private static Paint createLineBack(GraphicFactory graphicFactory, DisplayModel displayModel) {
        Paint createPaint = graphicFactory.createPaint();
        createPaint.setColor(Color.WHITE);
        createPaint.setStrokeWidth(displayModel.getScaleFactor() * 4.0f);
        createPaint.setStyle(Style.STROKE);
        return createPaint;
    }

    private static Paint createTextFront(GraphicFactory graphicFactory, DisplayModel displayModel) {
        Paint createPaint = graphicFactory.createPaint();
        createPaint.setColor(Color.BLUE);
        createPaint.setTypeface(FontFamily.DEFAULT, FontStyle.BOLD);
        createPaint.setTextSize(displayModel.getScaleFactor() * 12.0f);
        return createPaint;
    }

    private static Paint createTextBack(GraphicFactory graphicFactory, DisplayModel displayModel) {
        Paint createPaint = graphicFactory.createPaint();
        createPaint.setColor(Color.WHITE);
        createPaint.setTypeface(FontFamily.DEFAULT, FontStyle.BOLD);
        createPaint.setTextSize(displayModel.getScaleFactor() * 12.0f);
        createPaint.setStrokeWidth(displayModel.getScaleFactor() * 4.0f);
        createPaint.setStyle(Style.STROKE);
        return createPaint;
    }

    public Grid(GraphicFactory graphicFactory, DisplayModel displayModel, Map<Byte, Double> map) {
        this.displayModel = displayModel;
        this.spacingConfig = map;
        this.lineBack = createLineBack(graphicFactory, displayModel);
        this.lineFront = createLineFront(graphicFactory, displayModel);
        this.textBack = createTextBack(graphicFactory, displayModel);
        this.textFront = createTextFront(graphicFactory, displayModel);
    }

    public Grid(DisplayModel displayModel, Map<Byte, Double> map, Paint paint, Paint paint2, Paint paint3, Paint paint4) {
        this.displayModel = displayModel;
        this.spacingConfig = map;
        this.lineBack = paint;
        this.lineFront = paint2;
        this.textBack = paint3;
        this.textFront = paint4;
    }

    public void draw(BoundingBox boundingBox, byte b, Canvas canvas, Point point) {
        BoundingBox boundingBox2 = boundingBox;
        Canvas canvas2 = canvas;
        Point point2 = point;
        if (this.spacingConfig.containsKey(Byte.valueOf(b))) {
            double doubleValue = this.spacingConfig.get(Byte.valueOf(b)).doubleValue();
            double floor = doubleValue * Math.floor(boundingBox2.minLongitude / doubleValue);
            double ceil = doubleValue * Math.ceil(boundingBox2.maxLongitude / doubleValue);
            double floor2 = doubleValue * Math.floor(boundingBox2.minLatitude / doubleValue);
            double ceil2 = doubleValue * Math.ceil(boundingBox2.maxLatitude / doubleValue);
            long mapSize = MercatorProjection.getMapSize(b, this.displayModel.getTileSize());
            double d = floor2;
            int latitudeToPixelY = (int) (MercatorProjection.latitudeToPixelY(floor2, mapSize) - point2.f382y);
            double d2 = doubleValue;
            int latitudeToPixelY2 = (int) (MercatorProjection.latitudeToPixelY(ceil2, mapSize) - point2.f382y);
            double d3 = floor;
            int longitudeToPixelX = (int) (MercatorProjection.longitudeToPixelX(floor, mapSize) - point2.f381x);
            int longitudeToPixelX2 = (int) (MercatorProjection.longitudeToPixelX(ceil, mapSize) - point2.f381x);
            double d4 = d;
            while (d4 <= ceil2) {
                int latitudeToPixelY3 = (int) (MercatorProjection.latitudeToPixelY(d4, mapSize) - point2.f382y);
                canvas.drawLine(longitudeToPixelX, latitudeToPixelY3, longitudeToPixelX2, latitudeToPixelY3, this.lineBack);
                d4 += d2;
                mapSize = mapSize;
                ceil2 = ceil2;
                Canvas canvas3 = canvas;
                point2 = point;
            }
            long j = mapSize;
            double d5 = ceil2;
            double d6 = d3;
            while (d6 <= ceil) {
                int longitudeToPixelX3 = (int) (MercatorProjection.longitudeToPixelX(d6, j) - point.f381x);
                canvas.drawLine(longitudeToPixelX3, latitudeToPixelY, longitudeToPixelX3, latitudeToPixelY2, this.lineBack);
                d6 += d2;
                j = j;
                ceil = ceil;
            }
            double d7 = ceil;
            long j2 = j;
            Point point3 = point;
            for (double d8 = d; d8 <= d5; d8 += d2) {
                int latitudeToPixelY4 = (int) (MercatorProjection.latitudeToPixelY(d8, j2) - point3.f382y);
                canvas.drawLine(longitudeToPixelX, latitudeToPixelY4, longitudeToPixelX2, latitudeToPixelY4, this.lineFront);
            }
            for (double d9 = d3; d9 <= d7; d9 += d2) {
                int longitudeToPixelX4 = (int) (MercatorProjection.longitudeToPixelX(d9, j2) - point3.f381x);
                canvas.drawLine(longitudeToPixelX4, latitudeToPixelY, longitudeToPixelX4, latitudeToPixelY2, this.lineFront);
            }
            for (double d10 = d; d10 <= d5; d10 += d2) {
                String convertCoordinate = convertCoordinate(d10);
                int width = (canvas.getWidth() - this.textFront.getTextWidth(convertCoordinate)) / 2;
                int latitudeToPixelY5 = ((int) (MercatorProjection.latitudeToPixelY(d10, j2) - point3.f382y)) + (this.textFront.getTextHeight(convertCoordinate) / 2);
                Canvas canvas4 = canvas;
                canvas4.drawText(convertCoordinate, width, latitudeToPixelY5, this.textBack);
                canvas4.drawText(convertCoordinate, width, latitudeToPixelY5, this.textFront);
            }
            Canvas canvas5 = canvas;
            for (double d11 = d3; d11 <= d7; d11 += d2) {
                String convertCoordinate2 = convertCoordinate(d11);
                int longitudeToPixelX5 = ((int) (MercatorProjection.longitudeToPixelX(d11, j2) - point3.f381x)) - (this.textFront.getTextWidth(convertCoordinate2) / 2);
                int height = (canvas.getHeight() + this.textFront.getTextHeight(convertCoordinate2)) / 2;
                canvas5.drawText(convertCoordinate2, longitudeToPixelX5, height, this.textBack);
                canvas5.drawText(convertCoordinate2, longitudeToPixelX5, height, this.textFront);
            }
        }
    }
}
