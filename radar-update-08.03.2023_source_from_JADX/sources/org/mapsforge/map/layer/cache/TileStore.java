package org.mapsforge.map.layer.cache;

import java.io.File;
import java.util.Set;
import java.util.logging.Logger;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.common.Observer;

public class TileStore implements TileCache {
    private static final Logger LOGGER = Logger.getLogger(TileStore.class.getName());
    private final GraphicFactory graphicFactory;
    private final File rootDirectory;
    private final String suffix;

    public void addObserver(Observer observer) {
    }

    public void removeObserver(Observer observer) {
    }

    public void setWorkingSet(Set<Job> set) {
    }

    public TileStore(File file, String str, GraphicFactory graphicFactory2) {
        this.rootDirectory = file;
        this.graphicFactory = graphicFactory2;
        this.suffix = str;
        if (file == null || !file.isDirectory() || !file.canRead()) {
            throw new IllegalArgumentException("Root directory must be readable");
        }
    }

    public synchronized boolean containsKey(Job job) {
        return findFile(job) != null;
    }

    public synchronized void destroy() {
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x002f */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:18:0x0024=Splitter:B:18:0x0024, B:29:0x002f=Splitter:B:29:0x002f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized org.mapsforge.core.graphics.TileBitmap get(org.mapsforge.map.layer.queue.Job r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            java.io.File r0 = r4.findFile(r5)     // Catch:{ all -> 0x0034 }
            r1 = 0
            if (r0 != 0) goto L_0x000a
            monitor-exit(r4)
            return r1
        L_0x000a:
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ CorruptedInputStreamException -> 0x002e, IOException -> 0x0028, all -> 0x0023 }
            r2.<init>(r0)     // Catch:{ CorruptedInputStreamException -> 0x002e, IOException -> 0x0028, all -> 0x0023 }
            org.mapsforge.core.graphics.GraphicFactory r0 = r4.graphicFactory     // Catch:{ CorruptedInputStreamException -> 0x002f, IOException -> 0x0029, all -> 0x0020 }
            org.mapsforge.core.model.Tile r3 = r5.tile     // Catch:{ CorruptedInputStreamException -> 0x002f, IOException -> 0x0029, all -> 0x0020 }
            int r3 = r3.tileSize     // Catch:{ CorruptedInputStreamException -> 0x002f, IOException -> 0x0029, all -> 0x0020 }
            boolean r5 = r5.hasAlpha     // Catch:{ CorruptedInputStreamException -> 0x002f, IOException -> 0x0029, all -> 0x0020 }
            org.mapsforge.core.graphics.TileBitmap r5 = r0.createTileBitmap(r2, r3, r5)     // Catch:{ CorruptedInputStreamException -> 0x002f, IOException -> 0x0029, all -> 0x0020 }
            org.mapsforge.core.util.IOUtils.closeQuietly(r2)     // Catch:{ all -> 0x0034 }
            monitor-exit(r4)
            return r5
        L_0x0020:
            r5 = move-exception
            r1 = r2
            goto L_0x0024
        L_0x0023:
            r5 = move-exception
        L_0x0024:
            org.mapsforge.core.util.IOUtils.closeQuietly(r1)     // Catch:{ all -> 0x0034 }
            throw r5     // Catch:{ all -> 0x0034 }
        L_0x0028:
            r2 = r1
        L_0x0029:
            org.mapsforge.core.util.IOUtils.closeQuietly(r2)     // Catch:{ all -> 0x0034 }
            monitor-exit(r4)
            return r1
        L_0x002e:
            r2 = r1
        L_0x002f:
            org.mapsforge.core.util.IOUtils.closeQuietly(r2)     // Catch:{ all -> 0x0034 }
            monitor-exit(r4)
            return r1
        L_0x0034:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.cache.TileStore.get(org.mapsforge.map.layer.queue.Job):org.mapsforge.core.graphics.TileBitmap");
    }

    public synchronized int getCapacity() {
        return Integer.MAX_VALUE;
    }

    public synchronized int getCapacityFirstLevel() {
        return getCapacity();
    }

    public TileBitmap getImmediately(Job job) {
        return get(job);
    }

    public synchronized void purge() {
    }

    public synchronized void put(Job job, TileBitmap tileBitmap) {
    }

    /* access modifiers changed from: protected */
    public File findFile(Job job) {
        File file = new File(this.rootDirectory, Byte.toString(job.tile.zoomLevel));
        if (!file.isDirectory() || !file.canRead()) {
            Logger logger = LOGGER;
            logger.info("Failed to find directory " + file.getAbsolutePath());
            return null;
        }
        File file2 = new File(file, Long.toString((long) job.tile.tileX));
        if (!file2.isDirectory() || !file2.canRead()) {
            Logger logger2 = LOGGER;
            logger2.info("Failed to find directory " + file2.getAbsolutePath());
            return null;
        }
        File file3 = new File(file2, Long.toString((long) job.tile.tileY) + this.suffix);
        if (!file3.isFile() || !file3.canRead()) {
            Logger logger3 = LOGGER;
            logger3.info("Failed to find file " + file3.getAbsolutePath());
            return null;
        }
        Logger logger4 = LOGGER;
        logger4.info("Found file " + file3.getAbsolutePath());
        return file3;
    }
}
