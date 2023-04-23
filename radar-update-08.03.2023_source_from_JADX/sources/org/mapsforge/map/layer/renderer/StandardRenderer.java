package org.mapsforge.map.layer.renderer;

import java.util.Collections;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Position;
import org.mapsforge.core.mapelements.SymbolContainer;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tag;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MapReadResult;
import org.mapsforge.map.datastore.PointOfInterest;
import org.mapsforge.map.datastore.Way;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.rendertheme.RenderContext;

public class StandardRenderer implements RenderCallback {
    private static final Byte DEFAULT_START_ZOOM_LEVEL = (byte) 12;
    private static final Tag TAG_NATURAL_WATER = new Tag("natural", "water");
    private static final byte ZOOM_MAX = 22;
    public final GraphicFactory graphicFactory;
    public final HillsRenderConfig hillsRenderConfig;
    public final MapDataStore mapDataStore;
    private final boolean renderLabels;

    public byte getZoomLevelMax() {
        return ZOOM_MAX;
    }

    public StandardRenderer(MapDataStore mapDataStore2, GraphicFactory graphicFactory2, boolean z) {
        this(mapDataStore2, graphicFactory2, z, (HillsRenderConfig) null);
    }

    public StandardRenderer(MapDataStore mapDataStore2, GraphicFactory graphicFactory2, boolean z, HillsRenderConfig hillsRenderConfig2) {
        this.mapDataStore = mapDataStore2;
        this.graphicFactory = graphicFactory2;
        this.renderLabels = z;
        this.hillsRenderConfig = hillsRenderConfig2;
    }

    public LatLong getStartPosition() {
        MapDataStore mapDataStore2 = this.mapDataStore;
        if (mapDataStore2 != null) {
            return mapDataStore2.startPosition();
        }
        return null;
    }

    public Byte getStartZoomLevel() {
        MapDataStore mapDataStore2 = this.mapDataStore;
        if (mapDataStore2 == null || mapDataStore2.startZoomLevel() == null) {
            return DEFAULT_START_ZOOM_LEVEL;
        }
        return this.mapDataStore.startZoomLevel();
    }

    public void renderArea(RenderContext renderContext, Paint paint, Paint paint2, int i, PolylineContainer polylineContainer) {
        renderContext.addToCurrentDrawingLayer(i, new ShapePaintContainer(polylineContainer, paint2));
        renderContext.addToCurrentDrawingLayer(i, new ShapePaintContainer(polylineContainer, paint));
    }

    public void renderAreaCaption(RenderContext renderContext, Display display, int i, String str, float f, float f2, Paint paint, Paint paint2, Position position, int i2, PolylineContainer polylineContainer) {
        if (this.renderLabels) {
            renderContext.labels.add(this.graphicFactory.createPointTextContainer(polylineContainer.getCenterAbsolute().offset((double) f, (double) f2), display, i, str, paint, paint2, (SymbolContainer) null, position, i2));
        }
    }

    public void renderAreaSymbol(RenderContext renderContext, Display display, int i, Bitmap bitmap, PolylineContainer polylineContainer) {
        if (this.renderLabels) {
            renderContext.labels.add(new SymbolContainer(polylineContainer.getCenterAbsolute(), display, i, bitmap));
        }
    }

    public void renderPointOfInterestCaption(RenderContext renderContext, Display display, int i, String str, float f, float f2, Paint paint, Paint paint2, Position position, int i2, PointOfInterest pointOfInterest) {
        RenderContext renderContext2 = renderContext;
        if (this.renderLabels) {
            renderContext2.labels.add(this.graphicFactory.createPointTextContainer(MercatorProjection.getPixelAbsolute(pointOfInterest.position, renderContext2.rendererJob.tile.mapSize).offset((double) f, (double) f2), display, i, str, paint, paint2, (SymbolContainer) null, position, i2));
        }
    }

    public void renderPointOfInterestCircle(RenderContext renderContext, float f, Paint paint, Paint paint2, int i, PointOfInterest pointOfInterest) {
        Point pixelRelativeToTile = MercatorProjection.getPixelRelativeToTile(pointOfInterest.position, renderContext.rendererJob.tile);
        renderContext.addToCurrentDrawingLayer(i, new ShapePaintContainer(new CircleContainer(pixelRelativeToTile, f), paint2));
        renderContext.addToCurrentDrawingLayer(i, new ShapePaintContainer(new CircleContainer(pixelRelativeToTile, f), paint));
    }

    public void renderPointOfInterestSymbol(RenderContext renderContext, Display display, int i, Bitmap bitmap, PointOfInterest pointOfInterest) {
        if (this.renderLabels) {
            renderContext.labels.add(new SymbolContainer(MercatorProjection.getPixelAbsolute(pointOfInterest.position, renderContext.rendererJob.tile.mapSize), display, i, bitmap));
        }
    }

    public void renderWay(RenderContext renderContext, Paint paint, float f, int i, PolylineContainer polylineContainer) {
        renderContext.addToCurrentDrawingLayer(i, new ShapePaintContainer(polylineContainer, paint, f));
    }

    public void renderWaySymbol(RenderContext renderContext, Display display, int i, Bitmap bitmap, float f, boolean z, boolean z2, float f2, float f3, boolean z3, PolylineContainer polylineContainer) {
        if (this.renderLabels) {
            WayDecorator.renderSymbol(bitmap, display, i, f, z, z2, f2, f3, z3, polylineContainer.getCoordinatesAbsolute(), renderContext.labels);
        }
    }

    public void renderWayText(RenderContext renderContext, Display display, int i, String str, float f, Paint paint, Paint paint2, boolean z, float f2, float f3, boolean z2, PolylineContainer polylineContainer) {
        if (this.renderLabels) {
            WayDecorator.renderText(polylineContainer.getUpperLeft(), polylineContainer.getLowerRight(), str, display, i, f, paint, paint2, z, f2, f3, z2, polylineContainer.getCoordinatesAbsolute(), renderContext.labels);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean renderBitmap(RenderContext renderContext) {
        return !renderContext.renderTheme.hasMapBackgroundOutside() || this.mapDataStore.supportsTile(renderContext.rendererJob.tile);
    }

    /* access modifiers changed from: protected */
    public void renderPointOfInterest(RenderContext renderContext, PointOfInterest pointOfInterest) {
        renderContext.setDrawingLayers(pointOfInterest.layer);
        renderContext.renderTheme.matchNode(this, renderContext, pointOfInterest);
    }

    /* access modifiers changed from: protected */
    public void renderWaterBackground(RenderContext renderContext) {
        renderContext.setDrawingLayers((byte) 0);
        Point[] tilePixelCoordinates = getTilePixelCoordinates(renderContext.rendererJob.tile.tileSize);
        Point origin = renderContext.rendererJob.tile.getOrigin();
        for (int i = 0; i < tilePixelCoordinates.length; i++) {
            tilePixelCoordinates[i] = tilePixelCoordinates[i].offset(origin.f381x, origin.f382y);
        }
        renderContext.renderTheme.matchClosedWay(this, renderContext, new PolylineContainer(tilePixelCoordinates, renderContext.rendererJob.tile, renderContext.rendererJob.tile, Collections.singletonList(TAG_NATURAL_WATER)));
    }

    /* access modifiers changed from: protected */
    public void renderWay(RenderContext renderContext, PolylineContainer polylineContainer) {
        renderContext.setDrawingLayers(polylineContainer.getLayer());
        if (polylineContainer.isClosedWay()) {
            renderContext.renderTheme.matchClosedWay(this, renderContext, polylineContainer);
        } else {
            renderContext.renderTheme.matchLinearWay(this, renderContext, polylineContainer);
        }
    }

    /* access modifiers changed from: protected */
    public void processReadMapData(RenderContext renderContext, MapReadResult mapReadResult) {
        if (mapReadResult != null) {
            for (PointOfInterest renderPointOfInterest : mapReadResult.pointOfInterests) {
                renderPointOfInterest(renderContext, renderPointOfInterest);
            }
            for (Way polylineContainer : mapReadResult.ways) {
                renderWay(renderContext, new PolylineContainer(polylineContainer, renderContext.rendererJob.tile, renderContext.rendererJob.tile));
            }
            if (mapReadResult.isWater) {
                renderWaterBackground(renderContext);
            }
        }
    }

    private static Point[] getTilePixelCoordinates(int i) {
        Point[] pointArr = new Point[5];
        pointArr[0] = new Point(0.0d, 0.0d);
        double d = (double) i;
        pointArr[1] = new Point(d, 0.0d);
        pointArr[2] = new Point(d, d);
        pointArr[3] = new Point(0.0d, d);
        pointArr[4] = pointArr[0];
        return pointArr;
    }
}
