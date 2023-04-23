package org.mapsforge.map.layer.renderer;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture;

public class RendererJob extends Job {
    public final DisplayModel displayModel;
    private final int hashCodeValue;
    public boolean labelsOnly;
    public final MapDataStore mapDataStore;
    public final RenderThemeFuture renderThemeFuture;
    public final float textScale;

    public RendererJob(Tile tile, MapDataStore mapDataStore2, RenderThemeFuture renderThemeFuture2, DisplayModel displayModel2, float f, boolean z, boolean z2) {
        super(tile, z);
        if (mapDataStore2 == null) {
            throw new IllegalArgumentException("mapFile must not be null");
        } else if (f <= 0.0f || Float.isNaN(f)) {
            throw new IllegalArgumentException("invalid textScale: " + f);
        } else {
            this.labelsOnly = z2;
            this.displayModel = displayModel2;
            this.mapDataStore = mapDataStore2;
            this.renderThemeFuture = renderThemeFuture2;
            this.textScale = f;
            this.hashCodeValue = calculateHashCode();
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || !(obj instanceof RendererJob)) {
            return false;
        }
        RendererJob rendererJob = (RendererJob) obj;
        if (!this.mapDataStore.equals(rendererJob.mapDataStore) || Float.floatToIntBits(this.textScale) != Float.floatToIntBits(rendererJob.textScale)) {
            return false;
        }
        RenderThemeFuture renderThemeFuture2 = this.renderThemeFuture;
        if (renderThemeFuture2 != null || rendererJob.renderThemeFuture == null) {
            return (renderThemeFuture2 == null || renderThemeFuture2.equals(rendererJob.renderThemeFuture)) && this.labelsOnly == rendererJob.labelsOnly && this.displayModel.equals(rendererJob.displayModel);
        }
        return false;
    }

    public int hashCode() {
        return this.hashCodeValue;
    }

    public RendererJob otherTile(Tile tile) {
        return new RendererJob(tile, this.mapDataStore, this.renderThemeFuture, this.displayModel, this.textScale, this.hasAlpha, this.labelsOnly);
    }

    public void setRetrieveLabelsOnly() {
        this.labelsOnly = true;
    }

    private int calculateHashCode() {
        int hashCode = (((super.hashCode() * 31) + this.mapDataStore.hashCode()) * 31) + Float.floatToIntBits(this.textScale);
        RenderThemeFuture renderThemeFuture2 = this.renderThemeFuture;
        return renderThemeFuture2 != null ? (hashCode * 31) + renderThemeFuture2.hashCode() : hashCode;
    }
}
