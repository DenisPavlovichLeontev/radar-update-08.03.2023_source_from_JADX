package mil.nga.wkb.util.sweep;

import mil.nga.wkb.geom.Point;

public class Event implements Comparable<Event> {
    private int edge;
    private Point point;
    private int ring;
    private EventType type;

    public Event(int i, int i2, Point point2, EventType eventType) {
        this.edge = i;
        this.ring = i2;
        this.point = point2;
        this.type = eventType;
    }

    public int getEdge() {
        return this.edge;
    }

    public int getRing() {
        return this.ring;
    }

    public Point getPoint() {
        return this.point;
    }

    public EventType getType() {
        return this.type;
    }

    public int compareTo(Event event) {
        return SweepLine.xyOrder(this.point, event.point);
    }
}
