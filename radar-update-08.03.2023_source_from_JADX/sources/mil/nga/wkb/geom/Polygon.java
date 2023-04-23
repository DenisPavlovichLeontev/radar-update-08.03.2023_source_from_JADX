package mil.nga.wkb.geom;

public class Polygon extends CurvePolygon<LineString> {
    public Polygon() {
        this(false, false);
    }

    public Polygon(boolean z, boolean z2) {
        super(GeometryType.POLYGON, z, z2);
    }

    public Polygon(Polygon polygon) {
        this(polygon.hasZ(), polygon.hasM());
        for (LineString copy : polygon.getRings()) {
            addRing((LineString) copy.copy());
        }
    }

    protected Polygon(GeometryType geometryType, boolean z, boolean z2) {
        super(geometryType, z, z2);
    }

    public Geometry copy() {
        return new Polygon(this);
    }
}
