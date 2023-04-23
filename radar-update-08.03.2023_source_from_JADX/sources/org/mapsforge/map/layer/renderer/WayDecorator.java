package org.mapsforge.map.layer.renderer;

import java.util.List;
import org.mapsforge.core.graphics.Display;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.mapelements.MapElementContainer;
import org.mapsforge.core.mapelements.WayTextContainer;
import org.mapsforge.core.model.LineSegment;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Rectangle;
import org.mapsforge.core.model.Tile;

final class WayDecorator {
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x008b, code lost:
        r14 = r38;
        r31 = r3;
        r2 = (int) (r9 - r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0094, code lost:
        if (((float) r2) >= r1) goto L_0x0099;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0096, code lost:
        r9 = r20;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0099, code lost:
        r9 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x009a, code lost:
        r8 = r8 + 1;
        r3 = r31;
        r6 = r10;
        r4 = r12;
        r2 = r20;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void renderSymbol(org.mapsforge.core.graphics.Bitmap r28, org.mapsforge.core.graphics.Display r29, int r30, float r31, boolean r32, boolean r33, float r34, float r35, boolean r36, org.mapsforge.core.model.Point[][] r37, java.util.List<org.mapsforge.core.mapelements.MapElementContainer> r38) {
        /*
            r0 = r31
            r1 = r35
            int r2 = (int) r1
            r3 = 0
            int r4 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            r5 = 0
            if (r4 != 0) goto L_0x000e
            r0 = r37[r5]
            goto L_0x0015
        L_0x000e:
            r4 = r37[r5]
            double r6 = (double) r0
            org.mapsforge.core.model.Point[] r0 = org.mapsforge.map.layer.renderer.RendererUtils.parallelPath(r4, r6)
        L_0x0015:
            r4 = r0[r5]
            double r6 = r4.f381x
            r4 = r0[r5]
            double r4 = r4.f382y
            r8 = 1
            r9 = r2
        L_0x001f:
            int r10 = r0.length
            if (r8 >= r10) goto L_0x00a4
            r10 = r0[r8]
            double r10 = r10.f381x
            r12 = r0[r8]
            double r12 = r12.f382y
            double r14 = r10 - r6
            double r16 = r12 - r4
            double r18 = r14 * r14
            double r20 = r16 * r16
            double r18 = r18 + r20
            r20 = r2
            r31 = r3
            double r2 = java.lang.Math.sqrt(r18)
            float r2 = (float) r2
            r3 = r31
        L_0x003f:
            float r9 = (float) r9
            float r18 = r2 - r9
            int r19 = (r18 > r1 ? 1 : (r18 == r1 ? 0 : -1))
            if (r19 <= 0) goto L_0x008b
            float r9 = r9 / r2
            r31 = r3
            double r2 = (double) r9
            double r14 = r14 * r2
            double r6 = r6 + r14
            double r16 = r16 * r2
            double r4 = r4 + r16
            if (r36 == 0) goto L_0x005c
            double r2 = r12 - r4
            double r14 = r10 - r6
            double r2 = java.lang.Math.atan2(r2, r14)
            float r3 = (float) r2
            goto L_0x005e
        L_0x005c:
            r3 = r31
        L_0x005e:
            org.mapsforge.core.model.Point r2 = new org.mapsforge.core.model.Point
            r2.<init>(r6, r4)
            org.mapsforge.core.mapelements.SymbolContainer r9 = new org.mapsforge.core.mapelements.SymbolContainer
            r21 = r9
            r22 = r2
            r23 = r29
            r24 = r30
            r25 = r28
            r26 = r3
            r27 = r32
            r21.<init>(r22, r23, r24, r25, r26, r27)
            r14 = r38
            r14.add(r9)
            if (r33 != 0) goto L_0x007e
            return
        L_0x007e:
            double r15 = r10 - r6
            double r21 = r12 - r4
            r2 = r34
            int r9 = (int) r2
            r14 = r15
            r2 = r18
            r16 = r21
            goto L_0x003f
        L_0x008b:
            r14 = r38
            r31 = r3
            float r9 = r9 - r2
            int r2 = (int) r9
            float r3 = (float) r2
            int r3 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1))
            if (r3 >= 0) goto L_0x0099
            r9 = r20
            goto L_0x009a
        L_0x0099:
            r9 = r2
        L_0x009a:
            int r8 = r8 + 1
            r3 = r31
            r6 = r10
            r4 = r12
            r2 = r20
            goto L_0x001f
        L_0x00a4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.renderer.WayDecorator.renderSymbol(org.mapsforge.core.graphics.Bitmap, org.mapsforge.core.graphics.Display, int, float, boolean, boolean, float, float, boolean, org.mapsforge.core.model.Point[][], java.util.List):void");
    }

    static void renderText(Tile tile, Tile tile2, String str, Display display, int i, float f, Paint paint, Paint paint2, boolean z, float f2, float f3, boolean z2, Point[][] pointArr, List<MapElementContainer> list) {
        Point[] pointArr2;
        Point[] pointArr3;
        Rectangle rectangle;
        int i2;
        LineSegment clipToRectangle;
        String str2 = str;
        float f4 = f;
        Paint paint3 = paint;
        Paint paint4 = paint2;
        float f5 = f3;
        List<MapElementContainer> list2 = list;
        int textWidth = (paint4 == null ? paint3.getTextWidth(str2) : paint4.getTextWidth(str2)) + ((int) f5);
        double textHeight = (double) (paint4 == null ? paint3.getTextHeight(str2) : paint4.getTextHeight(str2));
        Rectangle boundaryAbsolute = Tile.getBoundaryAbsolute(tile, tile2);
        int i3 = 0;
        if (f4 == 0.0f) {
            pointArr2 = pointArr[0];
        } else {
            pointArr2 = RendererUtils.parallelPath(pointArr[0], (double) f4);
        }
        Point[] pointArr4 = pointArr2;
        int i4 = 1;
        while (i4 < pointArr4.length) {
            LineSegment lineSegment = new LineSegment(pointArr4[i4 - 1], pointArr4[i4]);
            double length = lineSegment.length();
            int i5 = (int) (((double) i3) - length);
            if (i5 <= 0) {
                double d = (double) textWidth;
                if (length >= d && (clipToRectangle = lineSegment.clipToRectangle(boundaryAbsolute)) != null) {
                    double length2 = clipToRectangle.length();
                    if (length2 >= d) {
                        LineSegment subSegment = clipToRectangle.subSegment(((length2 - d) / 2.0d) + ((double) (f5 / 2.0f)), (double) (((float) textWidth) - f5));
                        if (subSegment.start.f381x <= subSegment.end.f381x) {
                            WayTextContainer wayTextContainer = r0;
                            i2 = i4;
                            rectangle = boundaryAbsolute;
                            pointArr3 = pointArr4;
                            WayTextContainer wayTextContainer2 = new WayTextContainer(subSegment.start, subSegment.end, display, i, str, paint, paint2, textHeight);
                            list2.add(wayTextContainer);
                        } else {
                            i2 = i4;
                            rectangle = boundaryAbsolute;
                            pointArr3 = pointArr4;
                            list2.add(new WayTextContainer(subSegment.end, subSegment.start, display, i, str, paint, paint2, textHeight));
                        }
                        if (z) {
                            i3 = (int) f2;
                            i4 = i2 + 1;
                            String str3 = str;
                            boundaryAbsolute = rectangle;
                            pointArr4 = pointArr3;
                        } else {
                            return;
                        }
                    }
                }
            }
            float f6 = f2;
            i3 = i5;
            i2 = i4;
            rectangle = boundaryAbsolute;
            pointArr3 = pointArr4;
            i4 = i2 + 1;
            String str32 = str;
            boundaryAbsolute = rectangle;
            pointArr4 = pointArr3;
        }
    }

    private WayDecorator() {
        throw new IllegalStateException();
    }
}
