package org.mapsforge.map.layer.renderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.mapelements.MapElementContainer;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.rendertheme.RenderContext;
import org.mapsforge.map.util.LayerUtil;

public class DirectRenderer extends StandardRenderer {
    private static final Logger LOGGER = Logger.getLogger(DirectRenderer.class.getName());
    private final boolean renderLabels;
    private final TileDependencies tileDependencies = new TileDependencies();
    private final List<TileRefresher> tileRefreshers = new ArrayList();

    public interface TileRefresher {
        void refresh(Tile tile);
    }

    public DirectRenderer(MapDataStore mapDataStore, GraphicFactory graphicFactory, boolean z, HillsRenderConfig hillsRenderConfig) {
        super(mapDataStore, graphicFactory, z, hillsRenderConfig);
        this.renderLabels = z;
    }

    public void addTileRefresher(TileRefresher tileRefresher) {
        this.tileRefreshers.add(tileRefresher);
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x00d3  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00db  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.mapsforge.core.graphics.TileBitmap executeJob(org.mapsforge.map.layer.renderer.RendererJob r7) {
        /*
            r6 = this;
            r0 = 0
            org.mapsforge.map.rendertheme.RenderContext r1 = new org.mapsforge.map.rendertheme.RenderContext     // Catch:{ Exception -> 0x00b5, all -> 0x00b3 }
            org.mapsforge.map.layer.renderer.CanvasRasterer r2 = new org.mapsforge.map.layer.renderer.CanvasRasterer     // Catch:{ Exception -> 0x00b5, all -> 0x00b3 }
            org.mapsforge.core.graphics.GraphicFactory r3 = r6.graphicFactory     // Catch:{ Exception -> 0x00b5, all -> 0x00b3 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x00b5, all -> 0x00b3 }
            r1.<init>(r7, r2)     // Catch:{ Exception -> 0x00b5, all -> 0x00b3 }
            boolean r2 = r6.renderBitmap(r1)     // Catch:{ Exception -> 0x00b1 }
            if (r2 == 0) goto L_0x00ad
            org.mapsforge.map.datastore.MapDataStore r2 = r6.mapDataStore     // Catch:{ Exception -> 0x00b1 }
            if (r2 == 0) goto L_0x0022
            org.mapsforge.map.datastore.MapDataStore r2 = r6.mapDataStore     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.core.model.Tile r3 = r7.tile     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.map.datastore.MapReadResult r2 = r2.readMapData(r3)     // Catch:{ Exception -> 0x00b1 }
            r6.processReadMapData(r1, r2)     // Catch:{ Exception -> 0x00b1 }
        L_0x0022:
            boolean r2 = r7.labelsOnly     // Catch:{ Exception -> 0x00b1 }
            if (r2 != 0) goto L_0x006a
            org.mapsforge.map.rendertheme.rule.RenderTheme r2 = r1.renderTheme     // Catch:{ Exception -> 0x00b1 }
            r2.matchHillShadings(r6, r1)     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.core.graphics.GraphicFactory r2 = r6.graphicFactory     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.core.model.Tile r3 = r7.tile     // Catch:{ Exception -> 0x00b1 }
            int r3 = r3.tileSize     // Catch:{ Exception -> 0x00b1 }
            boolean r4 = r7.hasAlpha     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.core.graphics.TileBitmap r2 = r2.createTileBitmap(r3, r4)     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.map.datastore.MapDataStore r3 = r7.mapDataStore     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.core.model.Tile r4 = r7.tile     // Catch:{ Exception -> 0x00b1 }
            long r3 = r3.getDataTimestamp(r4)     // Catch:{ Exception -> 0x00b1 }
            r2.setTimestamp(r3)     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.map.layer.renderer.CanvasRasterer r3 = r1.canvasRasterer     // Catch:{ Exception -> 0x00b1 }
            r3.setCanvasBitmap(r2)     // Catch:{ Exception -> 0x00b1 }
            boolean r3 = r7.hasAlpha     // Catch:{ Exception -> 0x00b1 }
            if (r3 != 0) goto L_0x0064
            org.mapsforge.map.model.DisplayModel r3 = r7.displayModel     // Catch:{ Exception -> 0x00b1 }
            int r3 = r3.getBackgroundColor()     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.map.rendertheme.rule.RenderTheme r4 = r1.renderTheme     // Catch:{ Exception -> 0x00b1 }
            int r4 = r4.getMapBackground()     // Catch:{ Exception -> 0x00b1 }
            if (r3 == r4) goto L_0x0064
            org.mapsforge.map.layer.renderer.CanvasRasterer r3 = r1.canvasRasterer     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.map.rendertheme.rule.RenderTheme r4 = r1.renderTheme     // Catch:{ Exception -> 0x00b1 }
            int r4 = r4.getMapBackground()     // Catch:{ Exception -> 0x00b1 }
            r3.fill(r4)     // Catch:{ Exception -> 0x00b1 }
        L_0x0064:
            org.mapsforge.map.layer.renderer.CanvasRasterer r3 = r1.canvasRasterer     // Catch:{ Exception -> 0x00b1 }
            r3.drawWays(r1)     // Catch:{ Exception -> 0x00b1 }
            goto L_0x006b
        L_0x006a:
            r2 = r0
        L_0x006b:
            boolean r3 = r6.renderLabels     // Catch:{ Exception -> 0x00b1 }
            if (r3 == 0) goto L_0x007a
            java.util.Set r3 = r6.processLabels(r1)     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.map.layer.renderer.CanvasRasterer r4 = r1.canvasRasterer     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.core.model.Tile r5 = r7.tile     // Catch:{ Exception -> 0x00b1 }
            r4.drawMapElements(r3, r5)     // Catch:{ Exception -> 0x00b1 }
        L_0x007a:
            boolean r3 = r7.labelsOnly     // Catch:{ Exception -> 0x00b1 }
            if (r3 != 0) goto L_0x00a9
            org.mapsforge.map.rendertheme.rule.RenderTheme r3 = r1.renderTheme     // Catch:{ Exception -> 0x00b1 }
            boolean r3 = r3.hasMapBackgroundOutside()     // Catch:{ Exception -> 0x00b1 }
            if (r3 == 0) goto L_0x00a9
            org.mapsforge.map.datastore.MapDataStore r3 = r6.mapDataStore     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.core.model.BoundingBox r3 = r3.boundingBox()     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.core.model.Tile r4 = r7.tile     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.core.model.Rectangle r3 = r3.getPositionRelativeToTile(r4)     // Catch:{ Exception -> 0x00b1 }
            boolean r7 = r7.hasAlpha     // Catch:{ Exception -> 0x00b1 }
            if (r7 != 0) goto L_0x00a2
            org.mapsforge.map.layer.renderer.CanvasRasterer r7 = r1.canvasRasterer     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.map.rendertheme.rule.RenderTheme r4 = r1.renderTheme     // Catch:{ Exception -> 0x00b1 }
            int r4 = r4.getMapBackgroundOutside()     // Catch:{ Exception -> 0x00b1 }
            r7.fillOutsideAreas((int) r4, (org.mapsforge.core.model.Rectangle) r3)     // Catch:{ Exception -> 0x00b1 }
            goto L_0x00a9
        L_0x00a2:
            org.mapsforge.map.layer.renderer.CanvasRasterer r7 = r1.canvasRasterer     // Catch:{ Exception -> 0x00b1 }
            org.mapsforge.core.graphics.Color r4 = org.mapsforge.core.graphics.Color.TRANSPARENT     // Catch:{ Exception -> 0x00b1 }
            r7.fillOutsideAreas((org.mapsforge.core.graphics.Color) r4, (org.mapsforge.core.model.Rectangle) r3)     // Catch:{ Exception -> 0x00b1 }
        L_0x00a9:
            r1.destroy()
            return r2
        L_0x00ad:
            r1.destroy()
            return r0
        L_0x00b1:
            r7 = move-exception
            goto L_0x00b7
        L_0x00b3:
            r7 = move-exception
            goto L_0x00d9
        L_0x00b5:
            r7 = move-exception
            r1 = r0
        L_0x00b7:
            java.util.logging.Logger r2 = LOGGER     // Catch:{ all -> 0x00d7 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d7 }
            r3.<init>()     // Catch:{ all -> 0x00d7 }
            java.lang.String r4 = "Exception: "
            r3.append(r4)     // Catch:{ all -> 0x00d7 }
            java.lang.String r7 = r7.getMessage()     // Catch:{ all -> 0x00d7 }
            r3.append(r7)     // Catch:{ all -> 0x00d7 }
            java.lang.String r7 = r3.toString()     // Catch:{ all -> 0x00d7 }
            r2.warning(r7)     // Catch:{ all -> 0x00d7 }
            if (r1 == 0) goto L_0x00d6
            r1.destroy()
        L_0x00d6:
            return r0
        L_0x00d7:
            r7 = move-exception
            r0 = r1
        L_0x00d9:
            if (r0 == 0) goto L_0x00de
            r0.destroy()
        L_0x00de:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.renderer.DirectRenderer.executeJob(org.mapsforge.map.layer.renderer.RendererJob):org.mapsforge.core.graphics.TileBitmap");
    }

    private Set<MapElementContainer> processLabels(RenderContext renderContext) {
        HashSet<MapElementContainer> hashSet;
        synchronized (this.tileDependencies) {
            hashSet = new HashSet<>();
            Set<Tile> neighbours = renderContext.rendererJob.tile.getNeighbours();
            for (Tile overlappingElements : neighbours) {
                hashSet.addAll(this.tileDependencies.getOverlappingElements(overlappingElements, renderContext.rendererJob.tile));
            }
            List<MapElementContainer> collisionFreeOrdered = LayerUtil.collisionFreeOrdered(renderContext.labels);
            Iterator<MapElementContainer> it = collisionFreeOrdered.iterator();
            while (it.hasNext()) {
                MapElementContainer next = it.next();
                Iterator it2 = hashSet.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        if (((MapElementContainer) it2.next()).clashesWith(next)) {
                            it.remove();
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            hashSet.addAll(collisionFreeOrdered);
            for (Tile next2 : neighbours) {
                Set<MapElementContainer> overlappingElements2 = this.tileDependencies.getOverlappingElements(renderContext.rendererJob.tile, next2);
                HashSet<MapElementContainer> hashSet2 = new HashSet<>();
                for (MapElementContainer mapElementContainer : hashSet) {
                    if (mapElementContainer.intersects(next2.getBoundaryAbsolute())) {
                        hashSet2.add(mapElementContainer);
                    }
                }
                if (!overlappingElements2.equals(hashSet2)) {
                    this.tileDependencies.removeTileData(renderContext.rendererJob.tile, next2);
                    for (MapElementContainer addOverlappingElement : hashSet2) {
                        this.tileDependencies.addOverlappingElement(renderContext.rendererJob.tile, next2, addOverlappingElement);
                    }
                    for (TileRefresher refresh : this.tileRefreshers) {
                        refresh.refresh(next2);
                    }
                }
            }
        }
        return hashSet;
    }
}
