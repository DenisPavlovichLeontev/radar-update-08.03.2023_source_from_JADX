package org.mapsforge.map.layer.cache;

import java.util.Set;
import java.util.logging.Logger;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.common.Observable;
import org.mapsforge.map.model.common.Observer;

public class InMemoryTileCache implements TileCache {
    private static final Logger LOGGER = Logger.getLogger(InMemoryTileCache.class.getName());
    private BitmapLRUCache lruCache;
    private Observable observable = new Observable();

    public InMemoryTileCache(int i) {
        this.lruCache = new BitmapLRUCache(i);
    }

    public synchronized boolean containsKey(Job job) {
        return this.lruCache.containsKey(job);
    }

    public synchronized void destroy() {
        purge();
    }

    public synchronized TileBitmap get(Job job) {
        TileBitmap tileBitmap;
        tileBitmap = (TileBitmap) this.lruCache.get(job);
        if (tileBitmap != null) {
            tileBitmap.incrementRefCount();
        }
        return tileBitmap;
    }

    public synchronized int getCapacity() {
        return this.lruCache.capacity;
    }

    public int getCapacityFirstLevel() {
        return getCapacity();
    }

    public TileBitmap getImmediately(Job job) {
        return get(job);
    }

    public void purge() {
        for (TileBitmap decrementRefCount : this.lruCache.values()) {
            decrementRefCount.decrementRefCount();
        }
        this.lruCache.clear();
    }

    public synchronized void put(Job job, TileBitmap tileBitmap) {
        if (job == null) {
            throw new IllegalArgumentException("key must not be null");
        } else if (tileBitmap != null) {
            TileBitmap tileBitmap2 = (TileBitmap) this.lruCache.get(job);
            if (tileBitmap2 != null) {
                tileBitmap2.decrementRefCount();
            }
            if (this.lruCache.put(job, tileBitmap) != null) {
                Logger logger = LOGGER;
                logger.warning("overwriting cached entry: " + job);
            }
            tileBitmap.incrementRefCount();
            this.observable.notifyObservers();
        } else {
            throw new IllegalArgumentException("bitmap must not be null");
        }
    }

    public synchronized void setCapacity(int i) {
        BitmapLRUCache bitmapLRUCache = new BitmapLRUCache(i);
        bitmapLRUCache.putAll(this.lruCache);
        this.lruCache = bitmapLRUCache;
    }

    public synchronized void setWorkingSet(Set<Job> set) {
        this.lruCache.setWorkingSet(set);
    }

    public void addObserver(Observer observer) {
        this.observable.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        this.observable.removeObserver(observer);
    }
}
