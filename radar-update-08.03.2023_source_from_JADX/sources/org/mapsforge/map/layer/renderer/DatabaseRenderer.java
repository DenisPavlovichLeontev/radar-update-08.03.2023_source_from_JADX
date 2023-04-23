package org.mapsforge.map.layer.renderer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.mapelements.MapElementContainer;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.labels.TileBasedLabelStore;
import org.mapsforge.map.rendertheme.RenderContext;
import org.mapsforge.map.util.LayerUtil;

public class DatabaseRenderer extends StandardRenderer {
    private static final Logger LOGGER = Logger.getLogger(DatabaseRenderer.class.getName());
    private final TileBasedLabelStore labelStore;
    private final boolean renderLabels;
    private final TileCache tileCache;
    private final TileDependencies tileDependencies;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DatabaseRenderer(MapDataStore mapDataStore, GraphicFactory graphicFactory, TileCache tileCache2, TileBasedLabelStore tileBasedLabelStore, boolean z, boolean z2, HillsRenderConfig hillsRenderConfig) {
        super(mapDataStore, graphicFactory, z || z2, hillsRenderConfig);
        this.tileCache = tileCache2;
        this.labelStore = tileBasedLabelStore;
        this.renderLabels = z;
        if (!z) {
            this.tileDependencies = null;
        } else {
            this.tileDependencies = new TileDependencies();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:45:0x00e2  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00ea  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.mapsforge.core.graphics.TileBitmap executeJob(org.mapsforge.map.layer.renderer.RendererJob r7) {
        /*
            r6 = this;
            r0 = 0
            org.mapsforge.map.rendertheme.RenderContext r1 = new org.mapsforge.map.rendertheme.RenderContext     // Catch:{ Exception -> 0x00c4, all -> 0x00c2 }
            org.mapsforge.map.layer.renderer.CanvasRasterer r2 = new org.mapsforge.map.layer.renderer.CanvasRasterer     // Catch:{ Exception -> 0x00c4, all -> 0x00c2 }
            org.mapsforge.core.graphics.GraphicFactory r3 = r6.graphicFactory     // Catch:{ Exception -> 0x00c4, all -> 0x00c2 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x00c4, all -> 0x00c2 }
            r1.<init>(r7, r2)     // Catch:{ Exception -> 0x00c4, all -> 0x00c2 }
            boolean r2 = r6.renderBitmap(r1)     // Catch:{ Exception -> 0x00c0 }
            if (r2 == 0) goto L_0x00b8
            org.mapsforge.map.datastore.MapDataStore r2 = r6.mapDataStore     // Catch:{ Exception -> 0x00c0 }
            if (r2 == 0) goto L_0x0022
            org.mapsforge.map.datastore.MapDataStore r2 = r6.mapDataStore     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.core.model.Tile r3 = r7.tile     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.map.datastore.MapReadResult r2 = r2.readMapData(r3)     // Catch:{ Exception -> 0x00c0 }
            r6.processReadMapData(r1, r2)     // Catch:{ Exception -> 0x00c0 }
        L_0x0022:
            boolean r2 = r7.labelsOnly     // Catch:{ Exception -> 0x00c0 }
            if (r2 != 0) goto L_0x006a
            org.mapsforge.map.rendertheme.rule.RenderTheme r2 = r1.renderTheme     // Catch:{ Exception -> 0x00c0 }
            r2.matchHillShadings(r6, r1)     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.core.graphics.GraphicFactory r2 = r6.graphicFactory     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.core.model.Tile r3 = r7.tile     // Catch:{ Exception -> 0x00c0 }
            int r3 = r3.tileSize     // Catch:{ Exception -> 0x00c0 }
            boolean r4 = r7.hasAlpha     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.core.graphics.TileBitmap r2 = r2.createTileBitmap(r3, r4)     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.map.datastore.MapDataStore r3 = r7.mapDataStore     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.core.model.Tile r4 = r7.tile     // Catch:{ Exception -> 0x00c0 }
            long r3 = r3.getDataTimestamp(r4)     // Catch:{ Exception -> 0x00c0 }
            r2.setTimestamp(r3)     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.map.layer.renderer.CanvasRasterer r3 = r1.canvasRasterer     // Catch:{ Exception -> 0x00c0 }
            r3.setCanvasBitmap(r2)     // Catch:{ Exception -> 0x00c0 }
            boolean r3 = r7.hasAlpha     // Catch:{ Exception -> 0x00c0 }
            if (r3 != 0) goto L_0x0064
            org.mapsforge.map.model.DisplayModel r3 = r7.displayModel     // Catch:{ Exception -> 0x00c0 }
            int r3 = r3.getBackgroundColor()     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.map.rendertheme.rule.RenderTheme r4 = r1.renderTheme     // Catch:{ Exception -> 0x00c0 }
            int r4 = r4.getMapBackground()     // Catch:{ Exception -> 0x00c0 }
            if (r3 == r4) goto L_0x0064
            org.mapsforge.map.layer.renderer.CanvasRasterer r3 = r1.canvasRasterer     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.map.rendertheme.rule.RenderTheme r4 = r1.renderTheme     // Catch:{ Exception -> 0x00c0 }
            int r4 = r4.getMapBackground()     // Catch:{ Exception -> 0x00c0 }
            r3.fill(r4)     // Catch:{ Exception -> 0x00c0 }
        L_0x0064:
            org.mapsforge.map.layer.renderer.CanvasRasterer r3 = r1.canvasRasterer     // Catch:{ Exception -> 0x00c0 }
            r3.drawWays(r1)     // Catch:{ Exception -> 0x00c0 }
            goto L_0x006b
        L_0x006a:
            r2 = r0
        L_0x006b:
            boolean r3 = r6.renderLabels     // Catch:{ Exception -> 0x00c0 }
            if (r3 == 0) goto L_0x007a
            java.util.Set r3 = r6.processLabels(r1)     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.map.layer.renderer.CanvasRasterer r4 = r1.canvasRasterer     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.core.model.Tile r5 = r7.tile     // Catch:{ Exception -> 0x00c0 }
            r4.drawMapElements(r3, r5)     // Catch:{ Exception -> 0x00c0 }
        L_0x007a:
            org.mapsforge.map.layer.labels.TileBasedLabelStore r3 = r6.labelStore     // Catch:{ Exception -> 0x00c0 }
            if (r3 == 0) goto L_0x0085
            org.mapsforge.core.model.Tile r4 = r7.tile     // Catch:{ Exception -> 0x00c0 }
            java.util.List<org.mapsforge.core.mapelements.MapElementContainer> r5 = r1.labels     // Catch:{ Exception -> 0x00c0 }
            r3.storeMapItems(r4, r5)     // Catch:{ Exception -> 0x00c0 }
        L_0x0085:
            boolean r3 = r7.labelsOnly     // Catch:{ Exception -> 0x00c0 }
            if (r3 != 0) goto L_0x00b4
            org.mapsforge.map.rendertheme.rule.RenderTheme r3 = r1.renderTheme     // Catch:{ Exception -> 0x00c0 }
            boolean r3 = r3.hasMapBackgroundOutside()     // Catch:{ Exception -> 0x00c0 }
            if (r3 == 0) goto L_0x00b4
            org.mapsforge.map.datastore.MapDataStore r3 = r6.mapDataStore     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.core.model.BoundingBox r3 = r3.boundingBox()     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.core.model.Tile r4 = r7.tile     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.core.model.Rectangle r3 = r3.getPositionRelativeToTile(r4)     // Catch:{ Exception -> 0x00c0 }
            boolean r7 = r7.hasAlpha     // Catch:{ Exception -> 0x00c0 }
            if (r7 != 0) goto L_0x00ad
            org.mapsforge.map.layer.renderer.CanvasRasterer r7 = r1.canvasRasterer     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.map.rendertheme.rule.RenderTheme r4 = r1.renderTheme     // Catch:{ Exception -> 0x00c0 }
            int r4 = r4.getMapBackgroundOutside()     // Catch:{ Exception -> 0x00c0 }
            r7.fillOutsideAreas((int) r4, (org.mapsforge.core.model.Rectangle) r3)     // Catch:{ Exception -> 0x00c0 }
            goto L_0x00b4
        L_0x00ad:
            org.mapsforge.map.layer.renderer.CanvasRasterer r7 = r1.canvasRasterer     // Catch:{ Exception -> 0x00c0 }
            org.mapsforge.core.graphics.Color r4 = org.mapsforge.core.graphics.Color.TRANSPARENT     // Catch:{ Exception -> 0x00c0 }
            r7.fillOutsideAreas((org.mapsforge.core.graphics.Color) r4, (org.mapsforge.core.model.Rectangle) r3)     // Catch:{ Exception -> 0x00c0 }
        L_0x00b4:
            r1.destroy()
            return r2
        L_0x00b8:
            org.mapsforge.core.graphics.TileBitmap r7 = r6.createBackgroundBitmap(r1)     // Catch:{ Exception -> 0x00c0 }
            r1.destroy()
            return r7
        L_0x00c0:
            r7 = move-exception
            goto L_0x00c6
        L_0x00c2:
            r7 = move-exception
            goto L_0x00e8
        L_0x00c4:
            r7 = move-exception
            r1 = r0
        L_0x00c6:
            java.util.logging.Logger r2 = LOGGER     // Catch:{ all -> 0x00e6 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e6 }
            r3.<init>()     // Catch:{ all -> 0x00e6 }
            java.lang.String r4 = "Exception: "
            r3.append(r4)     // Catch:{ all -> 0x00e6 }
            java.lang.String r7 = r7.getMessage()     // Catch:{ all -> 0x00e6 }
            r3.append(r7)     // Catch:{ all -> 0x00e6 }
            java.lang.String r7 = r3.toString()     // Catch:{ all -> 0x00e6 }
            r2.warning(r7)     // Catch:{ all -> 0x00e6 }
            if (r1 == 0) goto L_0x00e5
            r1.destroy()
        L_0x00e5:
            return r0
        L_0x00e6:
            r7 = move-exception
            r0 = r1
        L_0x00e8:
            if (r0 == 0) goto L_0x00ed
            r0.destroy()
        L_0x00ed:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.renderer.DatabaseRenderer.executeJob(org.mapsforge.map.layer.renderer.RendererJob):org.mapsforge.core.graphics.TileBitmap");
    }

    public MapDataStore getMapDatabase() {
        return this.mapDataStore;
    }

    /* access modifiers changed from: package-private */
    public void removeTileInProgress(Tile tile) {
        TileDependencies tileDependencies2 = this.tileDependencies;
        if (tileDependencies2 != null) {
            tileDependencies2.removeTileInProgress(tile);
        }
    }

    private TileBitmap createBackgroundBitmap(RenderContext renderContext) {
        TileBitmap createTileBitmap = this.graphicFactory.createTileBitmap(renderContext.rendererJob.tile.tileSize, renderContext.rendererJob.hasAlpha);
        renderContext.canvasRasterer.setCanvasBitmap(createTileBitmap);
        if (!renderContext.rendererJob.hasAlpha) {
            renderContext.canvasRasterer.fill(renderContext.renderTheme.getMapBackgroundOutside());
        }
        return createTileBitmap;
    }

    private Set<MapElementContainer> processLabels(RenderContext renderContext) {
        HashSet<MapElementContainer> hashSet = new HashSet<>();
        synchronized (this.tileDependencies) {
            Set<Tile> neighbours = renderContext.rendererJob.tile.getNeighbours();
            Iterator<Tile> it = neighbours.iterator();
            HashSet hashSet2 = new HashSet();
            this.tileDependencies.addTileInProgress(renderContext.rendererJob.tile);
            while (it.hasNext()) {
                Tile next = it.next();
                if (!this.tileDependencies.isTileInProgress(next)) {
                    if (!this.tileCache.containsKey(renderContext.rendererJob.otherTile(next))) {
                        this.tileDependencies.removeTileData(next);
                    }
                }
                hashSet.addAll(this.tileDependencies.getOverlappingElements(next, renderContext.rendererJob.tile));
                for (MapElementContainer next2 : renderContext.labels) {
                    if (next2.intersects(next.getBoundaryAbsolute())) {
                        hashSet2.add(next2);
                    }
                }
                it.remove();
            }
            renderContext.labels.removeAll(hashSet2);
            List<MapElementContainer> collisionFreeOrdered = LayerUtil.collisionFreeOrdered(renderContext.labels);
            Iterator<MapElementContainer> it2 = collisionFreeOrdered.iterator();
            while (it2.hasNext()) {
                MapElementContainer next3 = it2.next();
                Iterator it3 = hashSet.iterator();
                while (true) {
                    if (it3.hasNext()) {
                        if (((MapElementContainer) it3.next()).clashesWith(next3)) {
                            it2.remove();
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            hashSet.addAll(collisionFreeOrdered);
            for (Tile next4 : neighbours) {
                this.tileDependencies.removeTileData(renderContext.rendererJob.tile, next4);
                for (MapElementContainer mapElementContainer : hashSet) {
                    if (mapElementContainer.intersects(next4.getBoundaryAbsolute())) {
                        this.tileDependencies.addOverlappingElement(renderContext.rendererJob.tile, next4, mapElementContainer);
                    }
                }
            }
        }
        return hashSet;
    }
}
