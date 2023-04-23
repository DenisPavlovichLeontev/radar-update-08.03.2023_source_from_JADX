package org.mapsforge.map.layer.download;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.TileLayer;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.download.tilesource.TileSource;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.model.common.Observer;

public class TileDownloadLayer extends TileLayer<DownloadJob> implements Observer {
    private static final int DOWNLOAD_THREADS_MAX = 8;
    private long cacheTimeToLive = 0;
    private final GraphicFactory graphicFactory;
    private boolean started;
    private final TileCache tileCache;
    private TileDownloadThread[] tileDownloadThreads;
    private final TileSource tileSource;

    public TileDownloadLayer(TileCache tileCache2, IMapViewPosition iMapViewPosition, TileSource tileSource2, GraphicFactory graphicFactory2) {
        super(tileCache2, iMapViewPosition, graphicFactory2.createMatrix(), tileSource2.hasAlpha());
        this.tileCache = tileCache2;
        this.tileSource = tileSource2;
        this.cacheTimeToLive = tileSource2.getDefaultTimeToLive();
        this.graphicFactory = graphicFactory2;
    }

    public void draw(BoundingBox boundingBox, byte b, Canvas canvas, Point point) {
        if (b >= this.tileSource.getZoomLevelMin() && b <= this.tileSource.getZoomLevelMax()) {
            super.draw(boundingBox, b, canvas, point);
        }
    }

    public long getCacheTimeToLive() {
        return this.cacheTimeToLive;
    }

    public void onDestroy() {
        for (TileDownloadThread finish : this.tileDownloadThreads) {
            finish.finish();
        }
        super.onDestroy();
    }

    public void onPause() {
        for (TileDownloadThread pause : this.tileDownloadThreads) {
            pause.pause();
        }
    }

    public void onResume() {
        if (!this.started) {
            start();
        }
        for (TileDownloadThread proceed : this.tileDownloadThreads) {
            proceed.proceed();
        }
    }

    public void setCacheTimeToLive(long j) {
        this.cacheTimeToLive = j;
    }

    public synchronized void setDisplayModel(DisplayModel displayModel) {
        super.setDisplayModel(displayModel);
        int min = Math.min(this.tileSource.getParallelRequestsLimit(), 8);
        int i = 0;
        if (this.displayModel != null) {
            this.tileDownloadThreads = new TileDownloadThread[min];
            while (i < min) {
                this.tileDownloadThreads[i] = new TileDownloadThread(this.tileCache, this.jobQueue, this, this.graphicFactory, this.displayModel);
                i++;
            }
        } else {
            TileDownloadThread[] tileDownloadThreadArr = this.tileDownloadThreads;
            if (tileDownloadThreadArr != null) {
                int length = tileDownloadThreadArr.length;
                while (i < length) {
                    tileDownloadThreadArr[i].finish();
                    i++;
                }
            }
        }
    }

    public void start() {
        for (TileDownloadThread start : this.tileDownloadThreads) {
            start.start();
        }
        this.started = true;
    }

    /* access modifiers changed from: protected */
    public DownloadJob createJob(Tile tile) {
        return new DownloadJob(tile, this.tileSource);
    }

    /* access modifiers changed from: protected */
    public boolean isTileStale(Tile tile, TileBitmap tileBitmap) {
        if (tileBitmap.isExpired()) {
            return true;
        }
        if (this.cacheTimeToLive == 0 || tileBitmap.getTimestamp() + this.cacheTimeToLive >= System.currentTimeMillis()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onAdd() {
        TileCache tileCache2 = this.tileCache;
        if (tileCache2 != null) {
            tileCache2.addObserver(this);
        }
        super.onAdd();
    }

    /* access modifiers changed from: protected */
    public void onRemove() {
        TileCache tileCache2 = this.tileCache;
        if (tileCache2 != null) {
            tileCache2.removeObserver(this);
        }
        super.onRemove();
    }

    public void onChange() {
        requestRedraw();
    }
}
