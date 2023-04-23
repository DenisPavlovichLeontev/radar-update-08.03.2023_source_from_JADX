package org.mapsforge.map.layer.download;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.queue.JobQueue;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.util.PausableThread;

class TileDownloadThread extends PausableThread {
    private static final Logger LOGGER = Logger.getLogger(TileDownloadThread.class.getName());
    private final DisplayModel displayModel;
    private final GraphicFactory graphicFactory;
    private JobQueue<DownloadJob> jobQueue;
    private final Layer layer;
    private final TileCache tileCache;

    /* access modifiers changed from: protected */
    public boolean hasWork() {
        return true;
    }

    TileDownloadThread(TileCache tileCache2, JobQueue<DownloadJob> jobQueue2, Layer layer2, GraphicFactory graphicFactory2, DisplayModel displayModel2) {
        this.tileCache = tileCache2;
        this.jobQueue = jobQueue2;
        this.layer = layer2;
        this.graphicFactory = graphicFactory2;
        this.displayModel = displayModel2;
    }

    public void setJobQueue(JobQueue<DownloadJob> jobQueue2) {
        this.jobQueue = jobQueue2;
    }

    /* access modifiers changed from: protected */
    public void doWork() throws InterruptedException {
        DownloadJob downloadJob = this.jobQueue.get();
        try {
            if (!this.tileCache.containsKey(downloadJob)) {
                downloadTile(downloadJob);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (Throwable th) {
            this.jobQueue.remove(downloadJob);
            throw th;
        }
        this.jobQueue.remove(downloadJob);
    }

    /* access modifiers changed from: protected */
    public PausableThread.ThreadPriority getThreadPriority() {
        return PausableThread.ThreadPriority.BELOW_NORMAL;
    }

    private void downloadTile(DownloadJob downloadJob) throws IOException {
        TileBitmap downloadImage = new TileDownloader(downloadJob, this.graphicFactory).downloadImage();
        if (!isInterrupted() && downloadImage != null) {
            downloadImage.scaleTo(this.displayModel.getTileSize(), this.displayModel.getTileSize());
            this.tileCache.put(downloadJob, downloadImage);
            this.layer.requestRedraw();
        }
    }
}
