package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.logging.Logger;
import org.mapsforge.core.graphics.GraphicFactory;

public class Hillshading {
    private static final Logger LOGGER = Logger.getLogger(Hillshading.class.getName());
    private boolean always;
    private final byte layer;
    private final int level;
    private final float magnitude;
    private final byte maxZoom;
    private final byte minZoom;

    public Hillshading(byte b, byte b2, short s, byte b3, boolean z, int i, GraphicFactory graphicFactory) {
        this.always = z;
        this.level = i;
        this.layer = b3;
        this.minZoom = b;
        this.maxZoom = b2;
        this.magnitude = (float) s;
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x0147  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0153  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x016d  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0176  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0195  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x01a2  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x01ba  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x01bc  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void render(org.mapsforge.map.rendertheme.RenderContext r63, org.mapsforge.map.layer.hills.HillsRenderConfig r64) {
        /*
            r62 = this;
            r1 = r62
            r2 = r63
            r3 = 0
            if (r64 != 0) goto L_0x0022
            boolean r0 = r1.always
            if (r0 == 0) goto L_0x0021
            byte r0 = r1.layer
            r2.setDrawingLayers(r0)
            org.mapsforge.map.layer.renderer.HillshadingContainer r0 = new org.mapsforge.map.layer.renderer.HillshadingContainer
            float r4 = r1.magnitude
            r0.<init>(r3, r4, r3, r3)
            int r4 = r1.level
            org.mapsforge.map.layer.renderer.ShapePaintContainer r5 = new org.mapsforge.map.layer.renderer.ShapePaintContainer
            r5.<init>(r0, r3)
            r2.addToCurrentDrawingLayer(r4, r5)
        L_0x0021:
            return
        L_0x0022:
            r0 = 0
            float r4 = r1.magnitude
            float r5 = r64.getMaginuteScaleFactor()
            float r4 = r4 * r5
            float r0 = java.lang.Math.max(r0, r4)
            r4 = 1132396544(0x437f0000, float:255.0)
            float r0 = java.lang.Math.min(r0, r4)
            float r11 = r0 / r4
            org.mapsforge.map.layer.renderer.RendererJob r0 = r2.rendererJob
            org.mapsforge.core.model.Tile r12 = r0.tile
            byte r0 = r12.zoomLevel
            byte r4 = r1.maxZoom
            if (r0 > r4) goto L_0x01fe
            byte r4 = r1.minZoom
            if (r0 >= r4) goto L_0x0046
            goto L_0x01fe
        L_0x0046:
            org.mapsforge.core.model.Point r13 = r12.getOrigin()
            double r4 = r13.f382y
            long r4 = (long) r4
            double r4 = (double) r4
            long r6 = r12.mapSize
            double r14 = org.mapsforge.core.util.MercatorProjection.pixelYToLatitude(r4, r6)
            double r4 = r13.f381x
            long r4 = (long) r4
            double r4 = (double) r4
            long r6 = r12.mapSize
            double r16 = org.mapsforge.core.util.MercatorProjection.pixelXToLongitude(r4, r6)
            double r4 = r13.f382y
            long r4 = (long) r4
            int r0 = r12.tileSize
            long r6 = (long) r0
            long r4 = r4 + r6
            double r4 = (double) r4
            long r6 = r12.mapSize
            double r18 = org.mapsforge.core.util.MercatorProjection.pixelYToLatitude(r4, r6)
            double r4 = r13.f381x
            long r4 = (long) r4
            int r0 = r12.tileSize
            long r6 = (long) r0
            long r4 = r4 + r6
            double r4 = (double) r4
            long r6 = r12.mapSize
            double r4 = org.mapsforge.core.util.MercatorProjection.pixelXToLongitude(r4, r6)
            double r6 = r14 - r18
            double r8 = r4 - r16
            int r0 = r12.tileSize
            r20 = r4
            double r3 = (double) r0
            double r22 = r3 / r6
            int r0 = r12.tileSize
            double r3 = (double) r0
            double r24 = r3 / r8
            int r0 = (r20 > r16 ? 1 : (r20 == r16 ? 0 : -1))
            if (r0 >= 0) goto L_0x0095
            long r3 = r12.mapSize
            double r3 = (double) r3
            double r4 = r20 + r3
            r20 = r4
        L_0x0095:
            double r3 = java.lang.Math.floor(r16)
            int r0 = (int) r3
        L_0x009a:
            r3 = r0
            double r9 = (double) r3
            int r0 = (r9 > r20 ? 1 : (r9 == r20 ? 0 : -1))
            if (r0 > 0) goto L_0x01fe
            double r4 = java.lang.Math.floor(r18)
            int r0 = (int) r4
            r5 = r0
        L_0x00a6:
            double r7 = (double) r5
            int r0 = (r7 > r14 ? 1 : (r7 == r14 ? 0 : -1))
            if (r0 > 0) goto L_0x01f5
            int r6 = r3 + 1
            int r4 = r5 + 1
            r2 = r4
            r4 = r64
            r26 = r11
            r11 = r6
            r6 = r3
            r27 = r7
            r7 = r22
            r29 = r9
            r9 = r24
            org.mapsforge.core.graphics.HillshadingBitmap r0 = r4.getShadingTile(r5, r6, r7, r9)     // Catch:{ Exception -> 0x00c3 }
            goto L_0x00d1
        L_0x00c3:
            r0 = move-exception
            r4 = r0
            java.util.logging.Logger r0 = LOGGER
            java.util.logging.Level r5 = java.util.logging.Level.SEVERE
            java.lang.String r6 = r4.getMessage()
            r0.log(r5, r6, r4)
            r0 = 0
        L_0x00d1:
            if (r0 != 0) goto L_0x00e1
            boolean r4 = r1.always
            if (r4 != 0) goto L_0x00e1
            r4 = r63
            r5 = r2
            r37 = r3
            r3 = r26
            r7 = 0
            goto L_0x01ed
        L_0x00e1:
            if (r0 == 0) goto L_0x00f4
            int r5 = r0.getPadding()
            int r6 = r0.getWidth()
            int r7 = r5 * 2
            int r6 = r6 - r7
            int r8 = r0.getHeight()
            int r8 = r8 - r7
            goto L_0x00f7
        L_0x00f4:
            r5 = 0
            r6 = 1
            r8 = 1
        L_0x00f7:
            double r9 = (double) r5
            r31 = r5
            double r4 = (double) r6
            double r32 = r9 + r4
            r34 = r0
            double r0 = (double) r8
            double r35 = r9 + r0
            int r7 = r12.tileSize
            r38 = r6
            double r6 = (double) r7
            r39 = r6
            int r6 = r12.tileSize
            double r6 = (double) r6
            r41 = r6
            double r6 = (double) r2
            int r43 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            r44 = 0
            if (r43 <= 0) goto L_0x0124
            double r6 = r6 - r14
            r43 = r2
            r37 = r3
            r46 = r4
            r2 = 1
            double r3 = (double) r2
            double r6 = r6 / r3
            double r6 = r6 * r0
            double r6 = r6 + r9
            r56 = r6
            goto L_0x0141
        L_0x0124:
            r43 = r2
            r37 = r3
            r46 = r4
            int r3 = (r14 > r6 ? 1 : (r14 == r6 ? 0 : -1))
            if (r3 <= 0) goto L_0x013f
            double r3 = r44 / r0
            double r6 = r6 + r3
            long r3 = r12.mapSize
            double r3 = org.mapsforge.core.util.MercatorProjection.latitudeToPixelY(r6, r3)
            double r5 = r13.f382y
            double r3 = r3 - r5
            r51 = r3
            r56 = r9
            goto L_0x0143
        L_0x013f:
            r56 = r9
        L_0x0141:
            r51 = r44
        L_0x0143:
            int r3 = (r27 > r18 ? 1 : (r27 == r18 ? 0 : -1))
            if (r3 >= 0) goto L_0x0153
            int r5 = r31 + r8
            double r3 = (double) r5
            double r5 = r18 - r27
            r2 = 1
            double r7 = (double) r2
            double r5 = r5 / r7
            double r5 = r5 * r0
            double r35 = r3 - r5
            goto L_0x0167
        L_0x0153:
            int r3 = (r18 > r27 ? 1 : (r18 == r27 ? 0 : -1))
            if (r3 >= 0) goto L_0x0167
            double r3 = r44 / r0
            double r7 = r27 + r3
            long r3 = r12.mapSize
            double r3 = org.mapsforge.core.util.MercatorProjection.latitudeToPixelY(r7, r3)
            double r5 = r13.f382y
            double r6 = r3 - r5
            r41 = r6
        L_0x0167:
            r60 = r35
            int r3 = (r29 > r16 ? 1 : (r29 == r16 ? 0 : -1))
            if (r3 >= 0) goto L_0x0176
            double r3 = r16 - r29
            r2 = 1
            double r5 = (double) r2
            double r3 = r3 / r5
            double r4 = r46 * r3
            double r9 = r9 + r4
            goto L_0x018c
        L_0x0176:
            int r3 = (r16 > r29 ? 1 : (r16 == r29 ? 0 : -1))
            if (r3 >= 0) goto L_0x018c
            double r3 = r44 / r46
            double r3 = r29 + r3
            long r5 = r12.mapSize
            double r3 = org.mapsforge.core.util.MercatorProjection.longitudeToPixelX(r3, r5)
            double r5 = r13.f381x
            double r3 = r3 - r5
            r49 = r3
            r54 = r9
            goto L_0x0190
        L_0x018c:
            r54 = r9
            r49 = r44
        L_0x0190:
            double r3 = (double) r11
            int r5 = (r3 > r20 ? 1 : (r3 == r20 ? 0 : -1))
            if (r5 <= 0) goto L_0x01a2
            int r5 = r31 + r38
            double r0 = (double) r5
            double r3 = r3 - r20
            r2 = 1
            double r5 = (double) r2
            double r3 = r3 / r5
            double r4 = r46 * r3
            double r32 = r0 - r4
            goto L_0x01b6
        L_0x01a2:
            int r2 = (r20 > r3 ? 1 : (r20 == r3 ? 0 : -1))
            if (r2 <= 0) goto L_0x01b6
            double r44 = r44 / r0
            double r3 = r3 + r44
            long r0 = r12.mapSize
            double r0 = org.mapsforge.core.util.MercatorProjection.longitudeToPixelX(r3, r0)
            double r2 = r13.f381x
            double r6 = r0 - r2
            r39 = r6
        L_0x01b6:
            r58 = r32
            if (r34 != 0) goto L_0x01bc
            r0 = 0
            goto L_0x01c3
        L_0x01bc:
            org.mapsforge.core.model.Rectangle r0 = new org.mapsforge.core.model.Rectangle
            r53 = r0
            r53.<init>(r54, r56, r58, r60)
        L_0x01c3:
            org.mapsforge.core.model.Rectangle r1 = new org.mapsforge.core.model.Rectangle
            r48 = r1
            r53 = r39
            r55 = r41
            r48.<init>(r49, r51, r53, r55)
            org.mapsforge.map.layer.renderer.HillshadingContainer r2 = new org.mapsforge.map.layer.renderer.HillshadingContainer
            r3 = r26
            r4 = r34
            r2.<init>(r4, r3, r0, r1)
            r1 = r62
            byte r0 = r1.layer
            r4 = r63
            r5 = r43
            r4.setDrawingLayers(r0)
            int r0 = r1.level
            org.mapsforge.map.layer.renderer.ShapePaintContainer r6 = new org.mapsforge.map.layer.renderer.ShapePaintContainer
            r7 = 0
            r6.<init>(r2, r7)
            r4.addToCurrentDrawingLayer(r0, r6)
        L_0x01ed:
            r11 = r3
            r2 = r4
            r9 = r29
            r3 = r37
            goto L_0x00a6
        L_0x01f5:
            r4 = r2
            r37 = r3
            r3 = r11
            r7 = 0
            int r0 = r37 + 1
            goto L_0x009a
        L_0x01fe:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.rendertheme.renderinstruction.Hillshading.render(org.mapsforge.map.rendertheme.RenderContext, org.mapsforge.map.layer.hills.HillsRenderConfig):void");
    }
}
