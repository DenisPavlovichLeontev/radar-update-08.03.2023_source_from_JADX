package org.mapsforge.map.layer.renderer;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.TileLayer;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.labels.LabelStore;
import org.mapsforge.map.layer.labels.TileBasedLabelStore;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.model.common.Observer;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture;

public class TileRendererLayer extends TileLayer<RendererJob> implements Observer {
    private final DatabaseRenderer databaseRenderer;
    private final GraphicFactory graphicFactory;
    private final MapDataStore mapDataStore;
    private MapWorkerPool mapWorkerPool;
    private RenderThemeFuture renderThemeFuture;
    private float textScale;
    private final TileBasedLabelStore tileBasedLabelStore;
    private XmlRenderTheme xmlRenderTheme;

    public TileRendererLayer(TileCache tileCache, MapDataStore mapDataStore2, IMapViewPosition iMapViewPosition, GraphicFactory graphicFactory2) {
        this(tileCache, mapDataStore2, iMapViewPosition, false, true, false, graphicFactory2);
    }

    public TileRendererLayer(TileCache tileCache, MapDataStore mapDataStore2, IMapViewPosition iMapViewPosition, boolean z, boolean z2, boolean z3, GraphicFactory graphicFactory2) {
        this(tileCache, mapDataStore2, iMapViewPosition, z, z2, z3, graphicFactory2, (HillsRenderConfig) null);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public TileRendererLayer(TileCache tileCache, MapDataStore mapDataStore2, IMapViewPosition iMapViewPosition, boolean z, boolean z2, boolean z3, GraphicFactory graphicFactory2, HillsRenderConfig hillsRenderConfig) {
        super(tileCache, iMapViewPosition, graphicFactory2.createMatrix(), z);
        TileCache tileCache2 = tileCache;
        IMapViewPosition iMapViewPosition2 = iMapViewPosition;
        boolean z4 = z;
        this.graphicFactory = graphicFactory2;
        this.mapDataStore = mapDataStore2;
        if (z3) {
            this.tileBasedLabelStore = new TileBasedLabelStore(tileCache.getCapacityFirstLevel());
        } else {
            this.tileBasedLabelStore = null;
        }
        this.databaseRenderer = new DatabaseRenderer(mapDataStore2, graphicFactory2, tileCache, this.tileBasedLabelStore, z2, z3, hillsRenderConfig);
        this.textScale = 1.0f;
    }

    public LabelStore getLabelStore() {
        return this.tileBasedLabelStore;
    }

    public MapDataStore getMapDataStore() {
        return this.mapDataStore;
    }

    public float getTextScale() {
        return this.textScale;
    }

    public void onDestroy() {
        RenderThemeFuture renderThemeFuture2 = this.renderThemeFuture;
        if (renderThemeFuture2 != null) {
            renderThemeFuture2.decrementRefCount();
        }
        this.mapDataStore.close();
        super.onDestroy();
    }

    public synchronized void setDisplayModel(DisplayModel displayModel) {
        super.setDisplayModel(displayModel);
        if (displayModel != null) {
            compileRenderTheme();
            if (this.mapWorkerPool == null) {
                this.mapWorkerPool = new MapWorkerPool(this.tileCache, this.jobQueue, this.databaseRenderer, this);
            }
            this.mapWorkerPool.start();
        } else {
            MapWorkerPool mapWorkerPool2 = this.mapWorkerPool;
            if (mapWorkerPool2 != null) {
                mapWorkerPool2.stop();
            }
        }
    }

    public void setTextScale(float f) {
        this.textScale = f;
    }

    public void setXmlRenderTheme(XmlRenderTheme xmlRenderTheme2) {
        this.xmlRenderTheme = xmlRenderTheme2;
        compileRenderTheme();
    }

    /* access modifiers changed from: protected */
    public void compileRenderTheme() {
        this.renderThemeFuture = new RenderThemeFuture(this.graphicFactory, this.xmlRenderTheme, this.displayModel);
        new Thread(this.renderThemeFuture).start();
    }

    public RenderThemeFuture getRenderThemeFuture() {
        return this.renderThemeFuture;
    }

    /* access modifiers changed from: protected */
    public RendererJob createJob(Tile tile) {
        return new RendererJob(tile, this.mapDataStore, this.renderThemeFuture, this.displayModel, this.textScale, this.isTransparent, false);
    }

    /* access modifiers changed from: protected */
    public boolean isTileStale(Tile tile, TileBitmap tileBitmap) {
        return this.mapDataStore.getDataTimestamp(tile) > tileBitmap.getTimestamp();
    }

    /* access modifiers changed from: protected */
    public void onAdd() {
        this.mapWorkerPool.start();
        if (this.tileCache != null) {
            this.tileCache.addObserver(this);
        }
        super.onAdd();
    }

    /* access modifiers changed from: protected */
    public void onRemove() {
        this.mapWorkerPool.stop();
        if (this.tileCache != null) {
            this.tileCache.removeObserver(this);
        }
        super.onRemove();
    }

    /* access modifiers changed from: protected */
    public void retrieveLabelsOnly(RendererJob rendererJob) {
        TileBasedLabelStore tileBasedLabelStore2;
        if (this.hasJobQueue && (tileBasedLabelStore2 = this.tileBasedLabelStore) != null && tileBasedLabelStore2.requiresTile(rendererJob.tile)) {
            rendererJob.setRetrieveLabelsOnly();
            this.jobQueue.add(rendererJob);
        }
    }

    public void onChange() {
        requestRedraw();
    }
}
