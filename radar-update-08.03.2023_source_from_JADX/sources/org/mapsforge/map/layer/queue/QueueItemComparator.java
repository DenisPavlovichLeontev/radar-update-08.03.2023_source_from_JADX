package org.mapsforge.map.layer.queue;

import java.io.Serializable;
import java.util.Comparator;

final class QueueItemComparator implements Comparator<QueueItem<?>>, Serializable {
    static final QueueItemComparator INSTANCE = new QueueItemComparator();
    private static final long serialVersionUID = 1;

    private QueueItemComparator() {
    }

    public int compare(QueueItem<?> queueItem, QueueItem<?> queueItem2) {
        if (queueItem.getPriority() < queueItem2.getPriority()) {
            return -1;
        }
        return queueItem.getPriority() > queueItem2.getPriority() ? 1 : 0;
    }
}
