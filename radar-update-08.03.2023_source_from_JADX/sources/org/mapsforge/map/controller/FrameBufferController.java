package org.mapsforge.map.controller;

import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.core.util.Parameters;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.model.common.Observer;
import org.mapsforge.map.view.FrameBuffer;

public final class FrameBufferController implements Observer {
    private static float maxAspectRatio = 2.0f;
    private final FrameBuffer frameBuffer;
    private Dimension lastMapViewDimension;
    private double lastOverdrawFactor;
    private final Model model;

    public static FrameBufferController create(FrameBuffer frameBuffer2, Model model2) {
        FrameBufferController frameBufferController = new FrameBufferController(frameBuffer2, model2);
        model2.frameBufferModel.addObserver(frameBufferController);
        model2.mapViewDimension.addObserver(frameBufferController);
        model2.mapViewPosition.addObserver(frameBufferController);
        model2.displayModel.addObserver(frameBufferController);
        return frameBufferController;
    }

    public static Dimension calculateFrameBufferDimension(Dimension dimension, double d) {
        int i = (int) (((double) dimension.width) * d);
        int i2 = (int) (((double) dimension.height) * d);
        if (Parameters.SQUARE_FRAME_BUFFER) {
            float f = ((float) dimension.width) / ((float) dimension.height);
            float f2 = maxAspectRatio;
            if (f < f2 && f > 1.0f / f2) {
                i = Math.max(i, i2);
                i2 = i;
            }
        }
        return new Dimension(i, i2);
    }

    private FrameBufferController(FrameBuffer frameBuffer2, Model model2) {
        this.frameBuffer = frameBuffer2;
        this.model = model2;
    }

    public void destroy() {
        this.model.displayModel.removeObserver(this);
        this.model.mapViewPosition.removeObserver(this);
        this.model.mapViewDimension.removeObserver(this);
        this.model.frameBufferModel.removeObserver(this);
    }

    public void onChange() {
        Dimension dimension = this.model.mapViewDimension.getDimension();
        if (dimension != null) {
            double overdrawFactor = this.model.frameBufferModel.getOverdrawFactor();
            if (dimensionChangeNeeded(dimension, overdrawFactor)) {
                Dimension calculateFrameBufferDimension = calculateFrameBufferDimension(dimension, overdrawFactor);
                if (!Parameters.SQUARE_FRAME_BUFFER || this.frameBuffer.getDimension() == null || calculateFrameBufferDimension.width > this.frameBuffer.getDimension().width || calculateFrameBufferDimension.height > this.frameBuffer.getDimension().height) {
                    this.frameBuffer.setDimension(calculateFrameBufferDimension);
                }
                this.lastMapViewDimension = dimension;
                this.lastOverdrawFactor = overdrawFactor;
            }
            synchronized (this.model.mapViewPosition) {
                synchronized (this.frameBuffer) {
                    MapPosition mapPosition = this.model.frameBufferModel.getMapPosition();
                    if (mapPosition != null) {
                        adjustFrameBufferMatrix(mapPosition, dimension, this.model.mapViewPosition.getScaleFactor(), this.model.mapViewPosition.getPivot());
                    }
                }
            }
        }
    }

    private void adjustFrameBufferMatrix(MapPosition mapPosition, Dimension dimension, double d, LatLong latLong) {
        double d2;
        MapPosition mapPosition2 = mapPosition;
        LatLong latLong2 = latLong;
        MapPosition mapPosition3 = this.model.mapViewPosition.getMapPosition();
        long mapSize = MercatorProjection.getMapSize(mapPosition2.zoomLevel, this.model.displayModel.getTileSize());
        Point pixel = MercatorProjection.getPixel(mapPosition2.latLong, mapSize);
        Point pixel2 = MercatorProjection.getPixel(mapPosition3.latLong, mapSize);
        double d3 = pixel.f381x - pixel2.f381x;
        double d4 = pixel.f382y - pixel2.f382y;
        double d5 = 0.0d;
        if (latLong2 != null) {
            Point pixel3 = MercatorProjection.getPixel(latLong2, mapSize);
            d5 = pixel3.f381x - pixel.f381x;
            d2 = pixel3.f382y - pixel.f382y;
        } else {
            d2 = 0.0d;
        }
        this.frameBuffer.adjustMatrix((float) d3, (float) d4, (float) (d / Math.pow(2.0d, (double) mapPosition2.zoomLevel)), dimension, (float) d5, (float) d2);
    }

    private boolean dimensionChangeNeeded(Dimension dimension, double d) {
        if (Double.compare(d, this.lastOverdrawFactor) == 0 && dimension.equals(this.lastMapViewDimension)) {
            return false;
        }
        return true;
    }
}
