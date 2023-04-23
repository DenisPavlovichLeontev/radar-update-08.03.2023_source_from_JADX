package org.mapsforge.map.layer.hills;

import java.io.File;
import java.util.concurrent.ExecutionException;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.HillshadingBitmap;

public class HillsRenderConfig {
    private float maginuteScaleFactor = 1.0f;
    private ShadeTileSource tileSource;

    public HillsRenderConfig(ShadeTileSource shadeTileSource) {
        this.tileSource = shadeTileSource;
    }

    public HillsRenderConfig(File file, GraphicFactory graphicFactory, ShadeTileSource shadeTileSource, ShadingAlgorithm shadingAlgorithm) {
        shadeTileSource = shadeTileSource == null ? new MemoryCachingHgtReaderTileSource(file, shadingAlgorithm, graphicFactory) : shadeTileSource;
        this.tileSource = shadeTileSource;
        shadeTileSource.setDemFolder(file);
        this.tileSource.setShadingAlgorithm(shadingAlgorithm);
    }

    public HillsRenderConfig indexOnThread() {
        ShadeTileSource shadeTileSource = this.tileSource;
        if (shadeTileSource != null) {
            shadeTileSource.applyConfiguration(true);
        }
        return this;
    }

    public HillshadingBitmap getShadingTile(int i, int i2, double d, double d2) throws ExecutionException, InterruptedException {
        ShadeTileSource shadeTileSource = this.tileSource;
        if (shadeTileSource == null) {
            return null;
        }
        HillshadingBitmap hillshadingBitmap = shadeTileSource.getHillshadingBitmap(i, i2, d, d2);
        if (hillshadingBitmap != null || Math.abs(i2) <= 178) {
            return hillshadingBitmap;
        }
        return shadeTileSource.getHillshadingBitmap(i, i2 > 0 ? i2 - 180 : i2 + 180, d, d2);
    }

    public float getMaginuteScaleFactor() {
        return this.maginuteScaleFactor;
    }

    public void setMaginuteScaleFactor(float f) {
        this.maginuteScaleFactor = f;
    }

    public ShadeTileSource getTileSource() {
        return this.tileSource;
    }

    public void setTileSource(ShadeTileSource shadeTileSource) {
        this.tileSource = shadeTileSource;
    }
}
