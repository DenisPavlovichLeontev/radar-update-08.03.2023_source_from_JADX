package mil.nga.wkb.geom;

public class TIN extends PolyhedralSurface {
    public TIN() {
        this(false, false);
    }

    public TIN(boolean z, boolean z2) {
        super(GeometryType.TIN, z, z2);
    }

    public TIN(TIN tin) {
        this(tin.hasZ(), tin.hasM());
        for (Polygon copy : tin.getPolygons()) {
            addPolygon((Polygon) copy.copy());
        }
    }

    public Geometry copy() {
        return new TIN(this);
    }
}
