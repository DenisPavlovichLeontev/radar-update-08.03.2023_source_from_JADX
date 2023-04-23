package org.mapsforge.map.layer.cache;

import java.util.Set;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.common.ObservableInterface;

public interface TileCache extends ObservableInterface {
    boolean containsKey(Job job);

    void destroy();

    TileBitmap get(Job job);

    int getCapacity();

    int getCapacityFirstLevel();

    TileBitmap getImmediately(Job job);

    void purge();

    void put(Job job, TileBitmap tileBitmap);

    void setWorkingSet(Set<Job> set);
}
