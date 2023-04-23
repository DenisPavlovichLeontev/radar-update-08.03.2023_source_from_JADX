package org.mapsforge.map.layer.hills;

import java.io.File;
import java.util.concurrent.ExecutionException;
import org.mapsforge.core.graphics.HillshadingBitmap;

public interface ShadeTileSource {
    void applyConfiguration(boolean z);

    HillshadingBitmap getHillshadingBitmap(int i, int i2, double d, double d2) throws ExecutionException, InterruptedException;

    void prepareOnThread();

    void setDemFolder(File file);

    void setShadingAlgorithm(ShadingAlgorithm shadingAlgorithm);
}
