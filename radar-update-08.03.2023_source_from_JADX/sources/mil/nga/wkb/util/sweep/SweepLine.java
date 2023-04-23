package mil.nga.wkb.util.sweep;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.Point;

public class SweepLine {
    private SegmentComparator comparator = new SegmentComparator();
    private List<LineString> rings;
    private Map<Integer, Map<Integer, Segment>> segments = new HashMap();
    private TreeSet<Segment> tree = new TreeSet<>(this.comparator);

    private class SegmentComparator implements Comparator<Segment> {

        /* renamed from: x */
        private double f365x;

        private SegmentComparator() {
        }

        public void setX(double d) {
            this.f365x = d;
        }

        public int compare(Segment segment, Segment segment2) {
            double access$000 = SweepLine.this.yValueAtX(segment, this.f365x);
            double access$0002 = SweepLine.this.yValueAtX(segment2, this.f365x);
            if (access$000 >= access$0002) {
                if (access$0002 < access$000) {
                    return 1;
                }
                if (segment.getRing() >= segment2.getRing()) {
                    if (segment2.getRing() < segment.getRing()) {
                        return 1;
                    }
                    if (segment.getEdge() >= segment2.getEdge()) {
                        if (segment2.getEdge() < segment.getEdge()) {
                            return 1;
                        }
                        return 0;
                    }
                }
            }
            return -1;
        }
    }

    public SweepLine(List<LineString> list) {
        this.rings = list;
    }

    public Segment add(Event event) {
        Segment createSegment = createSegment(event);
        this.comparator.setX(event.getPoint().getX());
        this.tree.add(createSegment);
        Segment higher = this.tree.higher(createSegment);
        Segment lower = this.tree.lower(createSegment);
        if (higher != null) {
            createSegment.setAbove(higher);
            higher.setBelow(createSegment);
        }
        if (lower != null) {
            createSegment.setBelow(lower);
            lower.setAbove(createSegment);
        }
        Map map = this.segments.get(Integer.valueOf(createSegment.getRing()));
        if (map == null) {
            map = new HashMap();
            this.segments.put(Integer.valueOf(createSegment.getRing()), map);
        }
        map.put(Integer.valueOf(createSegment.getEdge()), createSegment);
        return createSegment;
    }

    private Segment createSegment(Event event) {
        int edge = event.getEdge();
        int ring = event.getRing();
        List<Point> points = this.rings.get(ring).getPoints();
        Point point = points.get(edge);
        Point point2 = points.get((edge + 1) % points.size());
        if (xyOrder(point, point2) >= 0) {
            Point point3 = point;
            point = point2;
            point2 = point3;
        }
        return new Segment(edge, ring, point, point2);
    }

    public Segment find(Event event) {
        return (Segment) this.segments.get(Integer.valueOf(event.getRing())).get(Integer.valueOf(event.getEdge()));
    }

    public boolean intersect(Segment segment, Segment segment2) {
        if (segment == null || segment2 == null) {
            return false;
        }
        int ring = segment.getRing();
        boolean z = ring == segment2.getRing();
        if (z) {
            int edge = segment.getEdge();
            int edge2 = segment2.getEdge();
            int numPoints = this.rings.get(ring).numPoints();
            z = (edge + 1) % numPoints == edge2 || edge == (edge2 + 1) % numPoints;
        }
        if (z || isLeft(segment, segment2.getLeftPoint()) * isLeft(segment, segment2.getRightPoint()) > 0.0d || isLeft(segment2, segment.getLeftPoint()) * isLeft(segment2, segment.getRightPoint()) > 0.0d) {
            return false;
        }
        return true;
    }

    public void remove(Segment segment) {
        boolean remove = this.tree.remove(segment);
        if (!remove) {
            this.comparator.setX(segment.getLeftPoint().getX());
            remove = this.tree.remove(segment);
        }
        if (remove) {
            Segment above = segment.getAbove();
            Segment below = segment.getBelow();
            if (above != null) {
                above.setBelow(below);
            }
            if (below != null) {
                below.setAbove(above);
            }
            this.segments.get(Integer.valueOf(segment.getRing())).remove(Integer.valueOf(segment.getEdge()));
        }
    }

    /* access modifiers changed from: private */
    public double yValueAtX(Segment segment, double d) {
        Point leftPoint = segment.getLeftPoint();
        Point rightPoint = segment.getRightPoint();
        double y = (rightPoint.getY() - leftPoint.getY()) / (rightPoint.getX() - leftPoint.getX());
        return (y * d) + (leftPoint.getY() - (leftPoint.getX() * y));
    }

    public static int xyOrder(Point point, Point point2) {
        if (point.getX() <= point2.getX()) {
            if (point.getX() < point2.getX()) {
                return -1;
            }
            if (point.getY() <= point2.getY()) {
                if (point.getY() < point2.getY()) {
                    return -1;
                }
                return 0;
            }
        }
        return 1;
    }

    private static double isLeft(Segment segment, Point point) {
        return isLeft(segment.getLeftPoint(), segment.getRightPoint(), point);
    }

    private static double isLeft(Point point, Point point2, Point point3) {
        return ((point2.getX() - point.getX()) * (point3.getY() - point.getY())) - ((point3.getX() - point.getX()) * (point2.getY() - point.getY()));
    }
}
