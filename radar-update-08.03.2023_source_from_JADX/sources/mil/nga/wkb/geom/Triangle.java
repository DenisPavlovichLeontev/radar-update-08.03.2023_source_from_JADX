package mil.nga.wkb.geom;

public class Triangle extends Polygon {
    public Triangle() {
        this(false, false);
    }

    public Triangle(boolean z, boolean z2) {
        super(GeometryType.TRIANGLE, z, z2);
    }

    public Triangle(Triangle triangle) {
        this(triangle.hasZ(), triangle.hasM());
        for (LineString copy : triangle.getRings()) {
            addRing((LineString) copy.copy());
        }
    }

    public Geometry copy() {
        return new Triangle(this);
    }
}
