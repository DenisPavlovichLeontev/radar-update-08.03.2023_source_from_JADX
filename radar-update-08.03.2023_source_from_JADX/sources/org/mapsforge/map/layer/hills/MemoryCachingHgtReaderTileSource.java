package org.mapsforge.map.layer.hills;

import java.io.File;
import java.util.concurrent.ExecutionException;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.HillshadingBitmap;

public class MemoryCachingHgtReaderTileSource implements ShadeTileSource {
    private ShadingAlgorithm algorithm;
    private boolean configurationChangePending;
    private HgtCache currentCache;
    private File demFolder;
    private boolean enableInterpolationOverlap;
    private final GraphicFactory graphicsFactory;
    private int mainCacheSize;
    private int neighborCacheSize;

    public MemoryCachingHgtReaderTileSource(File file, ShadingAlgorithm shadingAlgorithm, GraphicFactory graphicFactory) {
        this(graphicFactory);
        this.demFolder = file;
        this.algorithm = shadingAlgorithm;
    }

    public MemoryCachingHgtReaderTileSource(GraphicFactory graphicFactory) {
        this.mainCacheSize = 4;
        this.neighborCacheSize = 4;
        this.enableInterpolationOverlap = true;
        this.configurationChangePending = true;
        this.graphicsFactory = graphicFactory;
    }

    public void applyConfiguration(boolean z) {
        HgtCache hgtCache = this.currentCache;
        HgtCache latestCache = latestCache();
        if (z && latestCache != null && latestCache != hgtCache) {
            latestCache.indexOnThread();
        }
    }

    private HgtCache latestCache() {
        HgtCache hgtCache = this.currentCache;
        if (hgtCache != null && !this.configurationChangePending) {
            return hgtCache;
        }
        if (this.demFolder == null || this.algorithm == null) {
            this.currentCache = null;
            return null;
        } else if (hgtCache != null && this.enableInterpolationOverlap == hgtCache.interpolatorOverlap && this.mainCacheSize == this.currentCache.mainCacheSize && this.neighborCacheSize == this.currentCache.neighborCacheSize && this.demFolder.equals(this.currentCache.demFolder) && this.algorithm.equals(this.currentCache.algorithm)) {
            return hgtCache;
        } else {
            HgtCache hgtCache2 = new HgtCache(this.demFolder, this.enableInterpolationOverlap, this.graphicsFactory, this.algorithm, this.mainCacheSize, this.neighborCacheSize);
            this.currentCache = hgtCache2;
            return hgtCache2;
        }
    }

    public void prepareOnThread() {
        HgtCache hgtCache = this.currentCache;
        if (hgtCache != null) {
            hgtCache.indexOnThread();
        }
    }

    public HillshadingBitmap getHillshadingBitmap(int i, int i2, double d, double d2) throws ExecutionException, InterruptedException {
        if (latestCache() == null) {
            return null;
        }
        return this.currentCache.getHillshadingBitmap(i, i2, d, d2);
    }

    public void setShadingAlgorithm(ShadingAlgorithm shadingAlgorithm) {
        this.algorithm = shadingAlgorithm;
    }

    public void setDemFolder(File file) {
        this.demFolder = file;
    }

    public void setMainCacheSize(int i) {
        this.mainCacheSize = i;
    }

    public void setNeighborCacheSize(int i) {
        this.neighborCacheSize = i;
    }

    public void setEnableInterpolationOverlap(boolean z) {
        this.enableInterpolationOverlap = z;
    }

    public int getMainCacheSize() {
        return this.mainCacheSize;
    }

    public int getNeighborCacheSize() {
        return this.neighborCacheSize;
    }

    public boolean isEnableInterpolationOverlap() {
        return this.enableInterpolationOverlap;
    }

    public File getDemFolder() {
        return this.demFolder;
    }

    public ShadingAlgorithm getAlgorithm() {
        return this.algorithm;
    }
}
