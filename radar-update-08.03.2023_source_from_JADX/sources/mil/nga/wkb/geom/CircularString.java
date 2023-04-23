package mil.nga.wkb.geom;

public class CircularString extends LineString {
    public CircularString() {
        this(false, false);
    }

    public CircularString(boolean z, boolean z2) {
        super(GeometryType.CIRCULARSTRING, z, z2);
    }

    public CircularString(CircularString circularString) {
        this(circularString.hasZ(), circularString.hasM());
        for (Point copy : circularString.getPoints()) {
            addPoint((Point) copy.copy());
        }
    }

    public Geometry copy() {
        return new CircularString(this);
    }
}
