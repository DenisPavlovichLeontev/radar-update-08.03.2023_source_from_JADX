package org.mapsforge.map.layer.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.common.Observer;

public class TwoLevelTileCache implements TileCache {
    private final TileCache firstLevelTileCache;
    private final TileCache secondLevelTileCache;
    private final Set<Job> workingSet = Collections.synchronizedSet(new HashSet());

    public TwoLevelTileCache(TileCache tileCache, TileCache tileCache2) {
        this.firstLevelTileCache = tileCache;
        this.secondLevelTileCache = tileCache2;
    }

    public boolean containsKey(Job job) {
        return this.firstLevelTileCache.containsKey(job) || this.secondLevelTileCache.containsKey(job);
    }

    public void destroy() {
        this.firstLevelTileCache.destroy();
        this.secondLevelTileCache.destroy();
    }

    public TileBitmap get(Job job) {
        TileBitmap tileBitmap = this.firstLevelTileCache.get(job);
        if (tileBitmap != null) {
            return tileBitmap;
        }
        TileBitmap tileBitmap2 = this.secondLevelTileCache.get(job);
        if (tileBitmap2 == null) {
            return null;
        }
        this.firstLevelTileCache.put(job, tileBitmap2);
        return tileBitmap2;
    }

    public int getCapacity() {
        return Math.max(this.firstLevelTileCache.getCapacity(), this.secondLevelTileCache.getCapacity());
    }

    public int getCapacityFirstLevel() {
        return this.firstLevelTileCache.getCapacity();
    }

    public TileBitmap getImmediately(Job job) {
        return this.firstLevelTileCache.get(job);
    }

    public void purge() {
        this.firstLevelTileCache.purge();
        this.secondLevelTileCache.purge();
    }

    public void put(Job job, TileBitmap tileBitmap) {
        if (this.workingSet.contains(job)) {
            this.firstLevelTileCache.put(job, tileBitmap);
        }
        this.secondLevelTileCache.put(job, tileBitmap);
    }

    public void setWorkingSet(Set<Job> set) {
        TileBitmap tileBitmap;
        this.workingSet.clear();
        this.workingSet.addAll(set);
        this.firstLevelTileCache.setWorkingSet(this.workingSet);
        this.secondLevelTileCache.setWorkingSet(this.workingSet);
        synchronized (this.workingSet) {
            for (Job next : this.workingSet) {
                if (!this.firstLevelTileCache.containsKey(next) && this.secondLevelTileCache.containsKey(next) && (tileBitmap = this.secondLevelTileCache.get(next)) != null) {
                    this.firstLevelTileCache.put(next, tileBitmap);
                }
            }
        }
    }

    public void addObserver(Observer observer) {
        this.firstLevelTileCache.addObserver(observer);
        this.secondLevelTileCache.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        this.secondLevelTileCache.removeObserver(observer);
        this.firstLevelTileCache.removeObserver(observer);
    }
}
