package org.mapsforge.map.layer.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.GraphicUtils;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.Path;
import org.mapsforge.core.mapelements.MapElementContainer;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Rectangle;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.rendertheme.RenderContext;

public class CanvasRasterer {
    private final Canvas canvas;
    private final Path path;
    private final Matrix symbolMatrix;

    public CanvasRasterer(GraphicFactory graphicFactory) {
        this.canvas = graphicFactory.createCanvas();
        this.path = graphicFactory.createPath();
        this.symbolMatrix = graphicFactory.createMatrix();
    }

    public void destroy() {
        this.canvas.destroy();
    }

    /* access modifiers changed from: package-private */
    public void drawWays(RenderContext renderContext) {
        int size = renderContext.ways.get(0).size();
        int size2 = renderContext.ways.size();
        for (int i = 0; i < size2; i++) {
            List list = renderContext.ways.get(i);
            for (int i2 = 0; i2 < size; i2++) {
                List list2 = (List) list.get(i2);
                for (int size3 = list2.size() - 1; size3 >= 0; size3--) {
                    drawShapePaintContainer((ShapePaintContainer) list2.get(size3));
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void drawMapElements(Set<MapElementContainer> set, Tile tile) {
        ArrayList<MapElementContainer> arrayList = new ArrayList<>(set);
        Collections.sort(arrayList);
        for (MapElementContainer draw : arrayList) {
            draw.draw(this.canvas, tile.getOrigin(), this.symbolMatrix, Filter.NONE);
        }
    }

    /* access modifiers changed from: package-private */
    public void fill(int i) {
        if (GraphicUtils.getAlpha(i) > 0) {
            this.canvas.fillColor(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void fillOutsideAreas(Color color, Rectangle rectangle) {
        this.canvas.setClipDifference((int) rectangle.left, (int) rectangle.top, (int) rectangle.getWidth(), (int) rectangle.getHeight());
        this.canvas.fillColor(color);
        this.canvas.resetClip();
    }

    /* access modifiers changed from: package-private */
    public void fillOutsideAreas(int i, Rectangle rectangle) {
        this.canvas.setClipDifference((int) rectangle.left, (int) rectangle.top, (int) rectangle.getWidth(), (int) rectangle.getHeight());
        this.canvas.fillColor(i);
        this.canvas.resetClip();
    }

    /* access modifiers changed from: package-private */
    public void setCanvasBitmap(Bitmap bitmap) {
        this.canvas.setBitmap(bitmap);
    }

    private void drawCircleContainer(ShapePaintContainer shapePaintContainer) {
        CircleContainer circleContainer = (CircleContainer) shapePaintContainer.shapeContainer;
        Point point = circleContainer.point;
        this.canvas.drawCircle((int) point.f381x, (int) point.f382y, (int) circleContainer.radius, shapePaintContainer.paint);
    }

    private void drawHillshading(HillshadingContainer hillshadingContainer) {
        this.canvas.shadeBitmap(hillshadingContainer.bitmap, hillshadingContainer.hillsRect, hillshadingContainer.tileRect, hillshadingContainer.magnitude);
    }

    private void drawPath(ShapePaintContainer shapePaintContainer, Point[][] pointArr, float f) {
        this.path.clear();
        int length = pointArr.length;
        for (int i = 0; i < length; i++) {
            Point[] pointArr2 = pointArr[i];
            if (f != 0.0f) {
                pointArr2 = RendererUtils.parallelPath(pointArr2, (double) f);
            }
            if (pointArr2.length >= 2) {
                Point point = pointArr2[0];
                this.path.moveTo((float) point.f381x, (float) point.f382y);
                for (int i2 = 1; i2 < pointArr2.length; i2++) {
                    Point point2 = pointArr2[i2];
                    this.path.lineTo((float) ((int) point2.f381x), (float) ((int) point2.f382y));
                }
            }
        }
        this.canvas.drawPath(this.path, shapePaintContainer.paint);
    }

    private void drawShapePaintContainer(ShapePaintContainer shapePaintContainer) {
        ShapeContainer shapeContainer = shapePaintContainer.shapeContainer;
        int i = C13271.$SwitchMap$org$mapsforge$map$layer$renderer$ShapeType[shapeContainer.getShapeType().ordinal()];
        if (i == 1) {
            drawCircleContainer(shapePaintContainer);
        } else if (i == 2) {
            drawHillshading((HillshadingContainer) shapeContainer);
        } else if (i == 3) {
            drawPath(shapePaintContainer, ((PolylineContainer) shapeContainer).getCoordinatesRelativeToOrigin(), shapePaintContainer.f386dy);
        }
    }

    /* renamed from: org.mapsforge.map.layer.renderer.CanvasRasterer$1 */
    static /* synthetic */ class C13271 {
        static final /* synthetic */ int[] $SwitchMap$org$mapsforge$map$layer$renderer$ShapeType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                org.mapsforge.map.layer.renderer.ShapeType[] r0 = org.mapsforge.map.layer.renderer.ShapeType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$mapsforge$map$layer$renderer$ShapeType = r0
                org.mapsforge.map.layer.renderer.ShapeType r1 = org.mapsforge.map.layer.renderer.ShapeType.CIRCLE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$org$mapsforge$map$layer$renderer$ShapeType     // Catch:{ NoSuchFieldError -> 0x001d }
                org.mapsforge.map.layer.renderer.ShapeType r1 = org.mapsforge.map.layer.renderer.ShapeType.HILLSHADING     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$org$mapsforge$map$layer$renderer$ShapeType     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.mapsforge.map.layer.renderer.ShapeType r1 = org.mapsforge.map.layer.renderer.ShapeType.POLYLINE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.renderer.CanvasRasterer.C13271.<clinit>():void");
        }
    }
}
