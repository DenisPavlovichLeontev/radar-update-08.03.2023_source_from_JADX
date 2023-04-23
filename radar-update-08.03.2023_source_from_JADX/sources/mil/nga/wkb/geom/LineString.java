package mil.nga.wkb.geom;

import java.util.ArrayList;
import java.util.List;

public class LineString extends Curve {
    private List<Point> points;

    public LineString() {
        this(false, false);
    }

    public LineString(boolean z, boolean z2) {
        super(GeometryType.LINESTRING, z, z2);
        this.points = new ArrayList();
    }

    public LineString(LineString lineString) {
        this(lineString.hasZ(), lineString.hasM());
        for (Point copy : lineString.getPoints()) {
            addPoint((Point) copy.copy());
        }
    }

    protected LineString(GeometryType geometryType, boolean z, boolean z2) {
        super(geometryType, z, z2);
        this.points = new ArrayList();
    }

    public List<Point> getPoints() {
        return this.points;
    }

    public void setPoints(List<Point> list) {
        this.points = list;
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }

    public int numPoints() {
        return this.points.size();
    }

    public Geometry copy() {
        return new LineString(this);
    }
}
