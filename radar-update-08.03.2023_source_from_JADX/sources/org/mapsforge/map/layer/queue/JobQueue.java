package org.mapsforge.map.layer.queue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.IMapViewPosition;

public class JobQueue<T extends Job> {
    private static final int QUEUE_CAPACITY = 128;
    private final List<T> assignedJobs = new LinkedList();
    private final DisplayModel displayModel;
    private boolean isInterrupted;
    private final IMapViewPosition mapViewPosition;
    private final List<QueueItem<T>> queueItems = new LinkedList();
    private boolean scheduleNeeded;

    public JobQueue(IMapViewPosition iMapViewPosition, DisplayModel displayModel2) {
        this.mapViewPosition = iMapViewPosition;
        this.displayModel = displayModel2;
    }

    public synchronized void add(T t) {
        if (!this.assignedJobs.contains(t)) {
            QueueItem queueItem = new QueueItem(t);
            if (!this.queueItems.contains(queueItem)) {
                this.queueItems.add(queueItem);
                this.scheduleNeeded = true;
                notifyWorkers();
            }
        }
    }

    public synchronized T get() throws InterruptedException {
        return get(Integer.MAX_VALUE);
    }

    public synchronized T get(int i) throws InterruptedException {
        do {
            if (!this.queueItems.isEmpty()) {
                if (this.assignedJobs.size() < i) {
                    if (this.scheduleNeeded) {
                        this.scheduleNeeded = false;
                        schedule(this.displayModel.getTileSize());
                    }
                    T t = this.queueItems.remove(0).object;
                    this.assignedJobs.add(t);
                    return t;
                }
            }
            wait(200);
        } while (!this.isInterrupted);
        this.isInterrupted = false;
        return null;
    }

    public synchronized void interrupt() {
        this.isInterrupted = true;
        notifyWorkers();
    }

    public synchronized void notifyWorkers() {
        notifyAll();
    }

    public synchronized void remove(T t) {
        this.assignedJobs.remove(t);
        notifyWorkers();
    }

    private void schedule(int i) {
        QueueItemScheduler.schedule(this.queueItems, this.mapViewPosition.getMapPosition(), i);
        Collections.sort(this.queueItems, QueueItemComparator.INSTANCE);
        trimToSize();
    }

    public synchronized int size() {
        return this.queueItems.size();
    }

    private void trimToSize() {
        int size = this.queueItems.size();
        while (size > 128) {
            size--;
            this.queueItems.remove(size);
        }
    }
}
