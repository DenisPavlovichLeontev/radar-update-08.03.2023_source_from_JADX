package org.mapsforge.map.layer.labels;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.renderer.StandardRenderer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture;

public class MapDataStoreLabelStore implements LabelStore {
    final DisplayModel displayModel;
    final RenderThemeFuture renderThemeFuture;
    final StandardRenderer standardRenderer;
    final float textScale;

    public void clear() {
    }

    public int getVersion() {
        return 0;
    }

    public MapDataStoreLabelStore(MapDataStore mapDataStore, RenderThemeFuture renderThemeFuture2, float f, DisplayModel displayModel2, GraphicFactory graphicFactory) {
        this.textScale = f;
        this.renderThemeFuture = renderThemeFuture2;
        this.standardRenderer = new StandardRenderer(mapDataStore, graphicFactory, true);
        this.displayModel = displayModel2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x009a, code lost:
        return new java.util.ArrayList();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0094 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.util.List<org.mapsforge.core.mapelements.MapElementContainer> getVisibleItems(org.mapsforge.core.model.Tile r10, org.mapsforge.core.model.Tile r11) {
        /*
            r9 = this;
            monitor-enter(r9)
            org.mapsforge.map.layer.renderer.RendererJob r8 = new org.mapsforge.map.layer.renderer.RendererJob     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.layer.renderer.StandardRenderer r0 = r9.standardRenderer     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.datastore.MapDataStore r2 = r0.mapDataStore     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.rendertheme.rule.RenderThemeFuture r3 = r9.renderThemeFuture     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.model.DisplayModel r4 = r9.displayModel     // Catch:{ Exception -> 0x0094 }
            float r5 = r9.textScale     // Catch:{ Exception -> 0x0094 }
            r6 = 1
            r7 = 1
            r0 = r8
            r1 = r10
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.rendertheme.RenderContext r0 = new org.mapsforge.map.rendertheme.RenderContext     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.layer.renderer.CanvasRasterer r1 = new org.mapsforge.map.layer.renderer.CanvasRasterer     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.layer.renderer.StandardRenderer r2 = r9.standardRenderer     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.core.graphics.GraphicFactory r2 = r2.graphicFactory     // Catch:{ Exception -> 0x0094 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x0094 }
            r0.<init>(r8, r1)     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.layer.renderer.StandardRenderer r1 = r9.standardRenderer     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.datastore.MapDataStore r1 = r1.mapDataStore     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.datastore.MapReadResult r1 = r1.readLabels(r10, r11)     // Catch:{ Exception -> 0x0094 }
            if (r1 != 0) goto L_0x0033
            java.util.ArrayList r10 = new java.util.ArrayList     // Catch:{ Exception -> 0x0094 }
            r10.<init>()     // Catch:{ Exception -> 0x0094 }
            monitor-exit(r9)
            return r10
        L_0x0033:
            java.util.List<org.mapsforge.map.datastore.PointOfInterest> r2 = r1.pointOfInterests     // Catch:{ Exception -> 0x0094 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ Exception -> 0x0094 }
        L_0x0039:
            boolean r3 = r2.hasNext()     // Catch:{ Exception -> 0x0094 }
            if (r3 == 0) goto L_0x005a
            java.lang.Object r3 = r2.next()     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.datastore.PointOfInterest r3 = (org.mapsforge.map.datastore.PointOfInterest) r3     // Catch:{ Exception -> 0x0094 }
            byte r4 = r3.layer     // Catch:{ Exception -> 0x0094 }
            r0.setDrawingLayers(r4)     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.layer.renderer.RendererJob r4 = r0.rendererJob     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.rendertheme.rule.RenderThemeFuture r4 = r4.renderThemeFuture     // Catch:{ Exception -> 0x0094 }
            java.lang.Object r4 = r4.get()     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.rendertheme.rule.RenderTheme r4 = (org.mapsforge.map.rendertheme.rule.RenderTheme) r4     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.layer.renderer.StandardRenderer r5 = r9.standardRenderer     // Catch:{ Exception -> 0x0094 }
            r4.matchNode(r5, r0, r3)     // Catch:{ Exception -> 0x0094 }
            goto L_0x0039
        L_0x005a:
            java.util.List<org.mapsforge.map.datastore.Way> r1 = r1.ways     // Catch:{ Exception -> 0x0094 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ Exception -> 0x0094 }
        L_0x0060:
            boolean r2 = r1.hasNext()     // Catch:{ Exception -> 0x0094 }
            if (r2 == 0) goto L_0x008e
            java.lang.Object r2 = r1.next()     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.datastore.Way r2 = (org.mapsforge.map.datastore.Way) r2     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.layer.renderer.PolylineContainer r3 = new org.mapsforge.map.layer.renderer.PolylineContainer     // Catch:{ Exception -> 0x0094 }
            r3.<init>(r2, r10, r11)     // Catch:{ Exception -> 0x0094 }
            byte r2 = r3.getLayer()     // Catch:{ Exception -> 0x0094 }
            r0.setDrawingLayers(r2)     // Catch:{ Exception -> 0x0094 }
            boolean r2 = r3.isClosedWay()     // Catch:{ Exception -> 0x0094 }
            if (r2 == 0) goto L_0x0086
            org.mapsforge.map.rendertheme.rule.RenderTheme r2 = r0.renderTheme     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.layer.renderer.StandardRenderer r4 = r9.standardRenderer     // Catch:{ Exception -> 0x0094 }
            r2.matchClosedWay(r4, r0, r3)     // Catch:{ Exception -> 0x0094 }
            goto L_0x0060
        L_0x0086:
            org.mapsforge.map.rendertheme.rule.RenderTheme r2 = r0.renderTheme     // Catch:{ Exception -> 0x0094 }
            org.mapsforge.map.layer.renderer.StandardRenderer r4 = r9.standardRenderer     // Catch:{ Exception -> 0x0094 }
            r2.matchLinearWay(r4, r0, r3)     // Catch:{ Exception -> 0x0094 }
            goto L_0x0060
        L_0x008e:
            java.util.List<org.mapsforge.core.mapelements.MapElementContainer> r10 = r0.labels     // Catch:{ Exception -> 0x0094 }
            monitor-exit(r9)
            return r10
        L_0x0092:
            r10 = move-exception
            goto L_0x009b
        L_0x0094:
            java.util.ArrayList r10 = new java.util.ArrayList     // Catch:{ all -> 0x0092 }
            r10.<init>()     // Catch:{ all -> 0x0092 }
            monitor-exit(r9)
            return r10
        L_0x009b:
            monitor-exit(r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.labels.MapDataStoreLabelStore.getVisibleItems(org.mapsforge.core.model.Tile, org.mapsforge.core.model.Tile):java.util.List");
    }
}
