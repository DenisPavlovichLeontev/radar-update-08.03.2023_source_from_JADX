package org.mapsforge.map.layer.renderer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.util.Parameters;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.queue.JobQueue;

public class MapWorkerPool implements Runnable {
    public static boolean DEBUG_TIMING = false;
    /* access modifiers changed from: private */
    public static final Logger LOGGER = Logger.getLogger(MapWorkerPool.class.getName());
    /* access modifiers changed from: private */
    public final AtomicInteger concurrentJobs = new AtomicInteger();
    /* access modifiers changed from: private */
    public final DatabaseRenderer databaseRenderer;
    /* access modifiers changed from: private */
    public boolean inShutdown;
    private boolean isRunning;
    /* access modifiers changed from: private */
    public final JobQueue<RendererJob> jobQueue;
    /* access modifiers changed from: private */
    public final Layer layer;
    private ExecutorService self;
    /* access modifiers changed from: private */
    public final TileCache tileCache;
    /* access modifiers changed from: private */
    public final AtomicLong totalExecutions = new AtomicLong();
    /* access modifiers changed from: private */
    public final AtomicLong totalTime = new AtomicLong();
    private ExecutorService workers;

    public MapWorkerPool(TileCache tileCache2, JobQueue<RendererJob> jobQueue2, DatabaseRenderer databaseRenderer2, Layer layer2) {
        this.tileCache = tileCache2;
        this.jobQueue = jobQueue2;
        this.databaseRenderer = databaseRenderer2;
        this.layer = layer2;
        this.inShutdown = false;
        this.isRunning = false;
    }

    public void run() {
        while (!this.inShutdown) {
            try {
                RendererJob rendererJob = this.jobQueue.get(Parameters.NUMBER_OF_THREADS);
                if (rendererJob != null) {
                    if (this.tileCache.containsKey(rendererJob)) {
                        if (!rendererJob.labelsOnly) {
                            this.jobQueue.remove(rendererJob);
                        }
                    }
                    this.workers.execute(new MapWorker(rendererJob));
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "MapWorkerPool interrupted", e);
                return;
            } catch (RejectedExecutionException e2) {
                LOGGER.log(Level.SEVERE, "MapWorkerPool rejected", e2);
                return;
            }
        }
    }

    public synchronized void start() {
        if (!this.isRunning) {
            this.inShutdown = false;
            this.self = Executors.newSingleThreadExecutor();
            this.workers = Executors.newFixedThreadPool(Parameters.NUMBER_OF_THREADS);
            this.self.execute(this);
            this.isRunning = true;
        }
    }

    public synchronized void stop() {
        if (this.isRunning) {
            this.inShutdown = true;
            this.jobQueue.interrupt();
            this.self.shutdown();
            this.workers.shutdown();
            try {
                if (!this.self.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                    this.self.shutdownNow();
                    if (!this.self.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                        LOGGER.warning("Shutdown self executor failed");
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Shutdown self executor interrupted", e);
            }
            try {
                if (!this.workers.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                    this.workers.shutdownNow();
                    if (!this.workers.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                        LOGGER.warning("Shutdown workers executor failed");
                    }
                }
            } catch (InterruptedException e2) {
                LOGGER.log(Level.SEVERE, "Shutdown workers executor interrupted", e2);
            }
            this.isRunning = false;
            return;
        }
        return;
    }

    class MapWorker implements Runnable {
        private final RendererJob rendererJob;

        MapWorker(RendererJob rendererJob2) {
            this.rendererJob = rendererJob2;
            rendererJob2.renderThemeFuture.incrementRefCount();
        }

        public void run() {
            long j;
            TileBitmap tileBitmap = null;
            try {
                if (MapWorkerPool.this.inShutdown) {
                    this.rendererJob.renderThemeFuture.decrementRefCount();
                    MapWorkerPool.this.jobQueue.remove(this.rendererJob);
                    return;
                }
                if (MapWorkerPool.DEBUG_TIMING) {
                    j = System.currentTimeMillis();
                    MapWorkerPool.LOGGER.info("ConcurrentJobs " + MapWorkerPool.this.concurrentJobs.incrementAndGet());
                } else {
                    j = 0;
                }
                tileBitmap = MapWorkerPool.this.databaseRenderer.executeJob(this.rendererJob);
                if (!MapWorkerPool.this.inShutdown) {
                    if (!this.rendererJob.labelsOnly && tileBitmap != null) {
                        MapWorkerPool.this.tileCache.put(this.rendererJob, tileBitmap);
                        MapWorkerPool.this.databaseRenderer.removeTileInProgress(this.rendererJob.tile);
                    }
                    MapWorkerPool.this.layer.requestRedraw();
                    if (MapWorkerPool.DEBUG_TIMING) {
                        long currentTimeMillis = System.currentTimeMillis();
                        long incrementAndGet = MapWorkerPool.this.totalExecutions.incrementAndGet();
                        long addAndGet = MapWorkerPool.this.totalTime.addAndGet(currentTimeMillis - j);
                        if (incrementAndGet % 10 == 0) {
                            MapWorkerPool.LOGGER.info("TIMING " + Long.toString(incrementAndGet) + " " + Double.toString((double) (addAndGet / incrementAndGet)));
                        }
                        MapWorkerPool.this.concurrentJobs.decrementAndGet();
                    }
                    this.rendererJob.renderThemeFuture.decrementRefCount();
                    MapWorkerPool.this.jobQueue.remove(this.rendererJob);
                    if (tileBitmap != null) {
                        tileBitmap.decrementRefCount();
                    }
                }
            } finally {
                this.rendererJob.renderThemeFuture.decrementRefCount();
                MapWorkerPool.this.jobQueue.remove(this.rendererJob);
                if (tileBitmap != null) {
                    tileBitmap.decrementRefCount();
                }
            }
        }
    }
}
