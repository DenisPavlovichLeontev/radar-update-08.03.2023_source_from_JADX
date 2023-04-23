package org.mapsforge.map.layer.queue;

import org.mapsforge.map.layer.queue.Job;

class QueueItem<T extends Job> {
    final T object;
    private double priority;

    QueueItem(T t) {
        this.object = t;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof QueueItem)) {
            return false;
        }
        return this.object.equals(((QueueItem) obj).object);
    }

    public int hashCode() {
        return this.object.hashCode();
    }

    /* access modifiers changed from: package-private */
    public double getPriority() {
        return this.priority;
    }

    /* access modifiers changed from: package-private */
    public void setPriority(double d) {
        if (d < 0.0d || Double.isNaN(d)) {
            throw new IllegalArgumentException("invalid priority: " + d);
        }
        this.priority = d;
    }
}
