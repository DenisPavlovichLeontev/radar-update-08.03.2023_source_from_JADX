package mil.nga.wkb.util.sweep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;
import mil.nga.wkb.util.GeometryUtils;

public class ShamosHoey {
    public static boolean simplePolygon(Polygon polygon) {
        return simplePolygon((List<LineString>) polygon.getRings());
    }

    public static boolean simplePolygonPoints(List<Point> list) {
        LineString lineString = new LineString();
        lineString.setPoints(list);
        return simplePolygon(lineString);
    }

    public static boolean simplePolygonRingPoints(List<List<Point>> list) {
        ArrayList arrayList = new ArrayList();
        for (List<Point> points : list) {
            LineString lineString = new LineString();
            lineString.setPoints(points);
            arrayList.add(lineString);
        }
        return simplePolygon((List<LineString>) arrayList);
    }

    public static boolean simplePolygon(LineString lineString) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(lineString);
        return simplePolygon((List<LineString>) arrayList);
    }

    public static boolean simplePolygon(List<LineString> list) {
        boolean z = !list.isEmpty();
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            if (i >= list.size()) {
                break;
            }
            LineString lineString = new LineString();
            lineString.setPoints(new ArrayList(list.get(i).getPoints()));
            arrayList.add(lineString);
            List<Point> points = lineString.getPoints();
            if (points.size() >= 3) {
                Point point = points.get(0);
                Point point2 = points.get(points.size() - 1);
                if (point.getX() == point2.getX() && point.getY() == point2.getY()) {
                    points.remove(points.size() - 1);
                }
            }
            if (points.size() < 3) {
                break;
            }
            if (i > 0) {
                Point point3 = points.get(0);
                if (!GeometryUtils.pointInPolygon(point3, list.get(0))) {
                    break;
                }
                int i2 = 1;
                while (true) {
                    if (i2 >= i) {
                        break;
                    }
                    List<Point> points2 = list.get(i2).getPoints();
                    if (GeometryUtils.pointInPolygon(point3, points2) || GeometryUtils.pointInPolygon(points2.get(0), points)) {
                        z = false;
                    } else {
                        i2++;
                    }
                }
                z = false;
                if (!z) {
                    break;
                }
            }
            i++;
        }
        z = false;
        if (z) {
            EventQueue eventQueue = new EventQueue((List<LineString>) arrayList);
            SweepLine sweepLine = new SweepLine(arrayList);
            Iterator<Event> it = eventQueue.iterator();
            while (it.hasNext()) {
                Event next = it.next();
                if (next.getType() == EventType.LEFT) {
                    Segment add = sweepLine.add(next);
                    if (sweepLine.intersect(add, add.getAbove()) || sweepLine.intersect(add, add.getBelow())) {
                        return false;
                    }
                } else {
                    Segment find = sweepLine.find(next);
                    if (sweepLine.intersect(find.getAbove(), find.getBelow())) {
                        return false;
                    }
                    sweepLine.remove(find);
                }
            }
        }
        return z;
    }
}
