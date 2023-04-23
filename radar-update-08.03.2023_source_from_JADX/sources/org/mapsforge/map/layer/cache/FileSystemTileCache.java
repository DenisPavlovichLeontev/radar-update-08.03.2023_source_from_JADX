package org.mapsforge.map.layer.cache;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.util.IOUtils;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.common.Observable;
import org.mapsforge.map.model.common.Observer;

public class FileSystemTileCache implements TileCache {
    static final String FILE_EXTENSION = ".tile";
    /* access modifiers changed from: private */
    public static final Logger LOGGER = Logger.getLogger(FileSystemTileCache.class.getName());
    /* access modifiers changed from: private */
    public final File cacheDirectory;
    private final GraphicFactory graphicFactory;
    /* access modifiers changed from: private */
    public final ReentrantReadWriteLock lock;
    /* access modifiers changed from: private */
    public FileWorkingSetCache<String> lruCache;
    private final Observable observable;
    private final boolean persistent;

    private class CacheDirectoryReader implements Runnable {
        private CacheDirectoryReader() {
        }

        public void run() {
            int i;
            File[] fileArr;
            File[] listFiles = FileSystemTileCache.this.cacheDirectory.listFiles();
            if (listFiles != null) {
                int length = listFiles.length;
                int i2 = 0;
                while (i2 < length) {
                    File file = listFiles[i2];
                    File[] listFiles2 = file.listFiles();
                    if (listFiles2 != null) {
                        int length2 = listFiles2.length;
                        int i3 = 0;
                        while (i3 < length2) {
                            File file2 = listFiles2[i3];
                            File[] listFiles3 = file2.listFiles();
                            if (listFiles3 != null) {
                                int length3 = listFiles3.length;
                                int i4 = 0;
                                while (i4 < length3) {
                                    File file3 = listFiles3[i4];
                                    if (!FileSystemTileCache.isValidFile(file3) || !file3.getName().endsWith(".tile")) {
                                        fileArr = listFiles;
                                        i = length;
                                    } else {
                                        fileArr = listFiles;
                                        i = length;
                                        String composeKey = Job.composeKey(file.getName(), file2.getName(), file3.getName().substring(0, file3.getName().lastIndexOf(".tile")));
                                        try {
                                            FileSystemTileCache.this.lock.writeLock().lock();
                                            if (FileSystemTileCache.this.lruCache.put(composeKey, file3) != null) {
                                                FileSystemTileCache.LOGGER.warning("overwriting cached entry: " + composeKey);
                                            }
                                        } finally {
                                            FileSystemTileCache.this.lock.writeLock().unlock();
                                        }
                                    }
                                    i4++;
                                    listFiles = fileArr;
                                    length = i;
                                }
                            }
                            i3++;
                            listFiles = listFiles;
                            length = length;
                        }
                    }
                    i2++;
                    listFiles = listFiles;
                    length = length;
                }
            }
        }
    }

    private static boolean isValidCacheDirectory(File file) {
        return file != null && (file.exists() || file.mkdirs()) && file.isDirectory() && file.canRead() && file.canWrite();
    }

    /* access modifiers changed from: private */
    public static boolean isValidFile(File file) {
        return file != null && file.isFile() && file.canRead();
    }

    private static boolean deleteDirectory(File file) {
        String[] list;
        if (file == null) {
            return false;
        }
        if (file.isDirectory() && (list = file.list()) != null) {
            for (String file2 : list) {
                if (!deleteDirectory(new File(file, file2))) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    public FileSystemTileCache(int i, File file, GraphicFactory graphicFactory2) {
        this(i, file, graphicFactory2, false);
    }

    public FileSystemTileCache(int i, File file, GraphicFactory graphicFactory2, boolean z) {
        this.observable = new Observable();
        this.persistent = z;
        this.lruCache = new FileWorkingSetCache<>(i);
        this.lock = new ReentrantReadWriteLock();
        if (isValidCacheDirectory(file)) {
            this.cacheDirectory = file;
            if (z) {
                new Thread(new CacheDirectoryReader()).start();
            }
        } else {
            this.cacheDirectory = null;
        }
        this.graphicFactory = graphicFactory2;
    }

    public boolean containsKey(Job job) {
        try {
            this.lock.readLock().lock();
            return this.lruCache.containsKey(job.getKey());
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void destroy() {
        if (!this.persistent) {
            purge();
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:16:0x0046=Splitter:B:16:0x0046, B:22:0x005a=Splitter:B:22:0x005a} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.mapsforge.core.graphics.TileBitmap get(org.mapsforge.map.layer.queue.Job r10) {
        /*
            r9 = this;
            java.util.concurrent.locks.ReentrantReadWriteLock r0 = r9.lock     // Catch:{ all -> 0x008f }
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r0.readLock()     // Catch:{ all -> 0x008f }
            r0.lock()     // Catch:{ all -> 0x008f }
            org.mapsforge.map.layer.cache.FileWorkingSetCache<java.lang.String> r0 = r9.lruCache     // Catch:{ all -> 0x008f }
            java.lang.String r1 = r10.getKey()     // Catch:{ all -> 0x008f }
            java.lang.Object r0 = r0.get(r1)     // Catch:{ all -> 0x008f }
            java.io.File r0 = (java.io.File) r0     // Catch:{ all -> 0x008f }
            java.util.concurrent.locks.ReentrantReadWriteLock r1 = r9.lock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r1 = r1.readLock()
            r1.unlock()
            r1 = 0
            if (r0 != 0) goto L_0x0022
            return r1
        L_0x0022:
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ CorruptedInputStreamException -> 0x0058, IOException -> 0x0044, all -> 0x0042 }
            r2.<init>(r0)     // Catch:{ CorruptedInputStreamException -> 0x0058, IOException -> 0x0044, all -> 0x0042 }
            org.mapsforge.core.graphics.GraphicFactory r3 = r9.graphicFactory     // Catch:{ CorruptedInputStreamException -> 0x0040, IOException -> 0x003e }
            org.mapsforge.core.model.Tile r4 = r10.tile     // Catch:{ CorruptedInputStreamException -> 0x0040, IOException -> 0x003e }
            int r4 = r4.tileSize     // Catch:{ CorruptedInputStreamException -> 0x0040, IOException -> 0x003e }
            boolean r5 = r10.hasAlpha     // Catch:{ CorruptedInputStreamException -> 0x0040, IOException -> 0x003e }
            org.mapsforge.core.graphics.TileBitmap r3 = r3.createTileBitmap(r2, r4, r5)     // Catch:{ CorruptedInputStreamException -> 0x0040, IOException -> 0x003e }
            long r4 = r0.lastModified()     // Catch:{ CorruptedInputStreamException -> 0x0040, IOException -> 0x003e }
            r3.setTimestamp(r4)     // Catch:{ CorruptedInputStreamException -> 0x0040, IOException -> 0x003e }
            org.mapsforge.core.util.IOUtils.closeQuietly(r2)
            return r3
        L_0x003e:
            r0 = move-exception
            goto L_0x0046
        L_0x0040:
            r3 = move-exception
            goto L_0x005a
        L_0x0042:
            r10 = move-exception
            goto L_0x008b
        L_0x0044:
            r0 = move-exception
            r2 = r1
        L_0x0046:
            r9.remove(r10)     // Catch:{ all -> 0x0089 }
            java.util.logging.Logger r10 = LOGGER     // Catch:{ all -> 0x0089 }
            java.util.logging.Level r3 = java.util.logging.Level.SEVERE     // Catch:{ all -> 0x0089 }
            java.lang.String r4 = r0.getMessage()     // Catch:{ all -> 0x0089 }
            r10.log(r3, r4, r0)     // Catch:{ all -> 0x0089 }
            org.mapsforge.core.util.IOUtils.closeQuietly(r2)
            return r1
        L_0x0058:
            r3 = move-exception
            r2 = r1
        L_0x005a:
            r9.remove(r10)     // Catch:{ all -> 0x0089 }
            java.util.logging.Logger r4 = LOGGER     // Catch:{ all -> 0x0089 }
            java.util.logging.Level r5 = java.util.logging.Level.WARNING     // Catch:{ all -> 0x0089 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0089 }
            r6.<init>()     // Catch:{ all -> 0x0089 }
            java.lang.String r7 = "input stream from file system cache invalid "
            r6.append(r7)     // Catch:{ all -> 0x0089 }
            java.lang.String r10 = r10.getKey()     // Catch:{ all -> 0x0089 }
            r6.append(r10)     // Catch:{ all -> 0x0089 }
            java.lang.String r10 = " "
            r6.append(r10)     // Catch:{ all -> 0x0089 }
            long r7 = r0.length()     // Catch:{ all -> 0x0089 }
            r6.append(r7)     // Catch:{ all -> 0x0089 }
            java.lang.String r10 = r6.toString()     // Catch:{ all -> 0x0089 }
            r4.log(r5, r10, r3)     // Catch:{ all -> 0x0089 }
            org.mapsforge.core.util.IOUtils.closeQuietly(r2)
            return r1
        L_0x0089:
            r10 = move-exception
            r1 = r2
        L_0x008b:
            org.mapsforge.core.util.IOUtils.closeQuietly(r1)
            throw r10
        L_0x008f:
            r10 = move-exception
            java.util.concurrent.locks.ReentrantReadWriteLock r0 = r9.lock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r0.readLock()
            r0.unlock()
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.layer.cache.FileSystemTileCache.get(org.mapsforge.map.layer.queue.Job):org.mapsforge.core.graphics.TileBitmap");
    }

    public int getCapacity() {
        try {
            this.lock.readLock().lock();
            return this.lruCache.capacity;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public int getCapacityFirstLevel() {
        return getCapacity();
    }

    public TileBitmap getImmediately(Job job) {
        return get(job);
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    /* JADX INFO: finally extract failed */
    public void purge() {
        try {
            this.lock.writeLock().lock();
            this.lruCache.clear();
            this.lock.writeLock().unlock();
            deleteDirectory(this.cacheDirectory);
        } catch (Throwable th) {
            this.lock.writeLock().unlock();
            throw th;
        }
    }

    public void put(Job job, TileBitmap tileBitmap) {
        if (job == null) {
            throw new IllegalArgumentException("key must not be null");
        } else if (tileBitmap == null) {
            throw new IllegalArgumentException("bitmap must not be null");
        } else if (getCapacity() != 0) {
            storeData(job, tileBitmap);
            this.observable.notifyObservers();
        }
    }

    public void setWorkingSet(Set<Job> set) {
        HashSet hashSet = new HashSet();
        synchronized (set) {
            for (Job key : set) {
                hashSet.add(key.getKey());
            }
        }
        this.lruCache.setWorkingSet(hashSet);
    }

    public void addObserver(Observer observer) {
        this.observable.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        this.observable.removeObserver(observer);
    }

    private File getOutputFile(Job job) {
        String str = this.cacheDirectory + File.separator + job.getKey();
        if (!isValidCacheDirectory(new File(str.substring(0, str.lastIndexOf(File.separatorChar))))) {
            return null;
        }
        return new File(str + ".tile");
    }

    private void remove(Job job) {
        try {
            this.lock.writeLock().lock();
            this.lruCache.remove(job.getKey());
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    private void storeData(Job job, TileBitmap tileBitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            File outputFile = getOutputFile(job);
            if (outputFile == null) {
                IOUtils.closeQuietly((Closeable) null);
                return;
            }
            FileOutputStream fileOutputStream2 = new FileOutputStream(outputFile);
            try {
                tileBitmap.compress(fileOutputStream2);
                this.lock.writeLock().lock();
                if (this.lruCache.put(job.getKey(), outputFile) != null) {
                    Logger logger = LOGGER;
                    logger.warning("overwriting cached entry: " + job.getKey());
                }
                this.lock.writeLock().unlock();
                IOUtils.closeQuietly(fileOutputStream2);
            } catch (Exception e) {
                e = e;
                fileOutputStream = fileOutputStream2;
                try {
                    LOGGER.log(Level.SEVERE, "Disabling filesystem cache", e);
                    destroy();
                    this.lock.writeLock().lock();
                    this.lruCache = new FileWorkingSetCache<>(0);
                    this.lock.writeLock().unlock();
                    IOUtils.closeQuietly(fileOutputStream);
                } catch (Throwable th) {
                    th = th;
                    IOUtils.closeQuietly(fileOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fileOutputStream2;
                IOUtils.closeQuietly(fileOutputStream);
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            LOGGER.log(Level.SEVERE, "Disabling filesystem cache", e);
            destroy();
            this.lock.writeLock().lock();
            this.lruCache = new FileWorkingSetCache<>(0);
            this.lock.writeLock().unlock();
            IOUtils.closeQuietly(fileOutputStream);
        }
    }
}
