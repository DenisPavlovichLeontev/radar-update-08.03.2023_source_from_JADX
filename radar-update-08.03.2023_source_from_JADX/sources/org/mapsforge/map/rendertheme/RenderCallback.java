package org.mapsforge.map.rendertheme;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Position;
import org.mapsforge.map.datastore.PointOfInterest;
import org.mapsforge.map.layer.renderer.PolylineContainer;

public interface RenderCallback {
    void renderArea(RenderContext renderContext, Paint paint, Paint paint2, int i, PolylineContainer polylineContainer);

    void renderAreaCaption(RenderContext renderContext, Display display, int i, String str, float f, float f2, Paint paint, Paint paint2, Position position, int i2, PolylineContainer polylineContainer);

    void renderAreaSymbol(RenderContext renderContext, Display display, int i, Bitmap bitmap, PolylineContainer polylineContainer);

    void renderPointOfInterestCaption(RenderContext renderContext, Display display, int i, String str, float f, float f2, Paint paint, Paint paint2, Position position, int i2, PointOfInterest pointOfInterest);

    void renderPointOfInterestCircle(RenderContext renderContext, float f, Paint paint, Paint paint2, int i, PointOfInterest pointOfInterest);

    void renderPointOfInterestSymbol(RenderContext renderContext, Display display, int i, Bitmap bitmap, PointOfInterest pointOfInterest);

    void renderWay(RenderContext renderContext, Paint paint, float f, int i, PolylineContainer polylineContainer);

    void renderWaySymbol(RenderContext renderContext, Display display, int i, Bitmap bitmap, float f, boolean z, boolean z2, float f2, float f3, boolean z3, PolylineContainer polylineContainer);

    void renderWayText(RenderContext renderContext, Display display, int i, String str, float f, Paint paint, Paint paint2, boolean z, float f2, float f3, boolean z2, PolylineContainer polylineContainer);
}
