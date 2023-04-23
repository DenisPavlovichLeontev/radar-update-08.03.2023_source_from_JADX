package mil.nga.wkb.util.sweep;

import mil.nga.wkb.geom.Point;

public class Segment {
    private Segment above;
    private Segment below;
    private int edge;
    private Point leftPoint;
    private Point rightPoint;
    private int ring;

    public Segment(int i, int i2, Point point, Point point2) {
        this.edge = i;
        this.ring = i2;
        this.leftPoint = point;
        this.rightPoint = point2;
    }

    public int getEdge() {
        return this.edge;
    }

    public int getRing() {
        return this.ring;
    }

    public Point getLeftPoint() {
        return this.leftPoint;
    }

    public Point getRightPoint() {
        return this.rightPoint;
    }

    public Segment getAbove() {
        return this.above;
    }

    public void setAbove(Segment segment) {
        this.above = segment;
    }

    public Segment getBelow() {
        return this.below;
    }

    public void setBelow(Segment segment) {
        this.below = segment;
    }
}
