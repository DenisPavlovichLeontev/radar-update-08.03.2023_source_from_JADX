package org.mapsforge.map.layer;

import java.util.HashSet;
import java.util.List;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.Parameters;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.layer.queue.JobQueue;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.util.LayerUtil;

public abstract class TileLayer<T extends Job> extends Layer {
    protected final boolean hasJobQueue;
    protected final boolean isTransparent;
    protected JobQueue<T> jobQueue;
    private final IMapViewPosition mapViewPosition;
    private final Matrix matrix;
    protected final TileCache tileCache;

    /* access modifiers changed from: protected */
    public abstract T createJob(Tile tile);

    /* access modifiers changed from: protected */
    public abstract boolean isTileStale(Tile tile, TileBitmap tileBitmap);

    /* access modifiers changed from: protected */
    public void retrieveLabelsOnly(T t) {
    }

    public TileLayer(TileCache tileCache2, IMapViewPosition iMapViewPosition, Matrix matrix2, boolean z) {
        this(tileCache2, iMapViewPosition, matrix2, z, true);
    }

    public TileLayer(TileCache tileCache2, IMapViewPosition iMapViewPosition, Matrix matrix2, boolean z, boolean z2) {
        if (tileCache2 == null) {
            throw new IllegalArgumentException("tileCache must not be null");
        } else if (iMapViewPosition != null) {
            this.hasJobQueue = z2;
            this.tileCache = tileCache2;
            this.mapViewPosition = iMapViewPosition;
            this.matrix = matrix2;
            this.isTransparent = z;
        } else {
            throw new IllegalArgumentException("mapViewPosition must not be null");
        }
    }

    public void draw(BoundingBox boundingBox, byte b, Canvas canvas, Point point) {
        List<TilePosition> tilePositions = LayerUtil.getTilePositions(boundingBox, b, point, this.displayModel.getTileSize());
        canvas.resetClip();
        if (!this.isTransparent) {
            canvas.fillColor(this.displayModel.getBackgroundColor());
        }
        HashSet hashSet = new HashSet();
        for (TilePosition tilePosition : tilePositions) {
            hashSet.add(createJob(tilePosition.tile));
        }
        this.tileCache.setWorkingSet(hashSet);
        for (int size = tilePositions.size() - 1; size >= 0; size--) {
            TilePosition tilePosition2 = tilePositions.get(size);
            Point point2 = tilePosition2.point;
            Tile tile = tilePosition2.tile;
            Job createJob = createJob(tile);
            TileBitmap immediately = this.tileCache.getImmediately(createJob);
            if (immediately == null) {
                if (this.hasJobQueue && !this.tileCache.containsKey(createJob)) {
                    this.jobQueue.add(createJob);
                }
                if (Parameters.PARENT_TILES_RENDERING != Parameters.ParentTilesRendering.OFF) {
                    drawParentTileBitmap(canvas, point2, tile);
                }
            } else {
                if (isTileStale(tile, immediately) && this.hasJobQueue && !this.tileCache.containsKey(createJob)) {
                    this.jobQueue.add(createJob);
                }
                retrieveLabelsOnly(createJob);
                canvas.drawBitmap(immediately, (int) Math.round(point2.f381x), (int) Math.round(point2.f382y), this.displayModel.getFilter());
                immediately.decrementRefCount();
            }
        }
        if (this.hasJobQueue) {
            this.jobQueue.notifyWorkers();
        }
    }

    public synchronized void setDisplayModel(DisplayModel displayModel) {
        super.setDisplayModel(displayModel);
        if (displayModel == null || !this.hasJobQueue) {
            this.jobQueue = null;
        } else {
            this.jobQueue = new JobQueue<>(this.mapViewPosition, this.displayModel);
        }
    }

    private void drawParentTileBitmap(Canvas canvas, Point point, Tile tile) {
        TileBitmap immediately;
        Canvas canvas2 = canvas;
        Point point2 = point;
        Tile tile2 = tile;
        Tile cachedParentTile = getCachedParentTile(tile2, 4);
        if (cachedParentTile != null && (immediately = this.tileCache.getImmediately(createJob(cachedParentTile))) != null) {
            int tileSize = this.displayModel.getTileSize();
            long shiftX = (long) (tile2.getShiftX(cachedParentTile) * tileSize);
            long shiftY = (long) (tile2.getShiftY(cachedParentTile) * tileSize);
            float pow = (float) Math.pow(2.0d, (double) ((byte) (tile2.zoomLevel - cachedParentTile.zoomLevel)));
            int round = (int) Math.round(point2.f381x);
            int round2 = (int) Math.round(point2.f382y);
            if (Parameters.PARENT_TILES_RENDERING == Parameters.ParentTilesRendering.SPEED) {
                boolean isAntiAlias = canvas.isAntiAlias();
                boolean isFilterBitmap = canvas.isFilterBitmap();
                canvas2.setAntiAlias(false);
                canvas2.setFilterBitmap(false);
                boolean z = isAntiAlias;
                boolean z2 = isFilterBitmap;
                long j = (long) tileSize;
                canvas.drawBitmap(immediately, (int) (((float) shiftX) / pow), (int) (((float) shiftY) / pow), (int) (((float) (shiftX + j)) / pow), (int) (((float) (shiftY + j)) / pow), round, round2, round + tileSize, round2 + tileSize, this.displayModel.getFilter());
                canvas2.setAntiAlias(z);
                canvas2.setFilterBitmap(z2);
            } else {
                this.matrix.reset();
                this.matrix.translate((float) (((long) round) - shiftX), (float) (((long) round2) - shiftY));
                this.matrix.scale(pow, pow);
                canvas2.setClip(round, round2, this.displayModel.getTileSize(), this.displayModel.getTileSize());
                canvas2.drawBitmap((Bitmap) immediately, this.matrix, this.displayModel.getFilter());
                canvas.resetClip();
            }
            immediately.decrementRefCount();
        }
    }

    private Tile getCachedParentTile(Tile tile, int i) {
        Tile parent;
        if (i == 0 || (parent = tile.getParent()) == null) {
            return null;
        }
        if (this.tileCache.containsKey(createJob(parent))) {
            return parent;
        }
        return getCachedParentTile(parent, i - 1);
    }

    public TileCache getTileCache() {
        return this.tileCache;
    }
}
