package mil.nga.wkb.util.sweep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.Point;

public class EventQueue implements Iterable<Event> {
    private List<Event> events = new ArrayList();

    public EventQueue(LineString lineString) {
        addRing(lineString, 0);
        sort();
    }

    public EventQueue(List<LineString> list) {
        for (int i = 0; i < list.size(); i++) {
            addRing(list.get(i), i);
        }
        sort();
    }

    private void addRing(LineString lineString, int i) {
        EventType eventType;
        EventType eventType2;
        List<Point> points = lineString.getPoints();
        int i2 = 0;
        while (i2 < points.size()) {
            Point point = points.get(i2);
            int i3 = i2 + 1;
            Point point2 = points.get(i3 % points.size());
            if (SweepLine.xyOrder(point, point2) < 0) {
                eventType2 = EventType.LEFT;
                eventType = EventType.RIGHT;
            } else {
                eventType2 = EventType.RIGHT;
                eventType = EventType.LEFT;
            }
            Event event = new Event(i2, i, point, eventType2);
            Event event2 = new Event(i2, i, point2, eventType);
            this.events.add(event);
            this.events.add(event2);
            i2 = i3;
        }
    }

    private void sort() {
        Collections.sort(this.events);
    }

    public Iterator<Event> iterator() {
        return this.events.iterator();
    }
}
