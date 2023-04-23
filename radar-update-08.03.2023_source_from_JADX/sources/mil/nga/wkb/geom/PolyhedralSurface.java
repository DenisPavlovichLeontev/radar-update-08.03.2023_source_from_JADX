package mil.nga.wkb.geom;

import java.util.ArrayList;
import java.util.List;

public class PolyhedralSurface extends Surface {
    private List<Polygon> polygons;

    public PolyhedralSurface() {
        this(false, false);
    }

    public PolyhedralSurface(boolean z, boolean z2) {
        super(GeometryType.POLYHEDRALSURFACE, z, z2);
        this.polygons = new ArrayList();
    }

    public PolyhedralSurface(PolyhedralSurface polyhedralSurface) {
        this(polyhedralSurface.hasZ(), polyhedralSurface.hasM());
        for (Polygon copy : polyhedralSurface.getPolygons()) {
            addPolygon((Polygon) copy.copy());
        }
    }

    protected PolyhedralSurface(GeometryType geometryType, boolean z, boolean z2) {
        super(geometryType, z, z2);
        this.polygons = new ArrayList();
    }

    public List<Polygon> getPolygons() {
        return this.polygons;
    }

    public void setPolygons(List<Polygon> list) {
        this.polygons = list;
    }

    public void addPolygon(Polygon polygon) {
        this.polygons.add(polygon);
    }

    public int numPolygons() {
        return this.polygons.size();
    }

    public Geometry copy() {
        return new PolyhedralSurface(this);
    }
}
