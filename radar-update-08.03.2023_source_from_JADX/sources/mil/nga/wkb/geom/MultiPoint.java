package mil.nga.wkb.geom;

import java.util.List;

public class MultiPoint extends GeometryCollection<Point> {
    public MultiPoint() {
        this(false, false);
    }

    public MultiPoint(boolean z, boolean z2) {
        super(GeometryType.MULTIPOINT, z, z2);
    }

    public MultiPoint(MultiPoint multiPoint) {
        this(multiPoint.hasZ(), multiPoint.hasM());
        for (Point copy : multiPoint.getPoints()) {
            addPoint((Point) copy.copy());
        }
    }

    public List<Point> getPoints() {
        return getGeometries();
    }

    public void setPoints(List<Point> list) {
        setGeometries(list);
    }

    public void addPoint(Point point) {
        addGeometry(point);
    }

    public int numPoints() {
        return numGeometries();
    }

    public Geometry copy() {
        return new MultiPoint(this);
    }
}
