package mil.nga.wkb.geom;

import java.util.List;

public class MultiPolygon extends MultiSurface<Polygon> {
    public MultiPolygon() {
        this(false, false);
    }

    public MultiPolygon(boolean z, boolean z2) {
        super(GeometryType.MULTIPOLYGON, z, z2);
    }

    public MultiPolygon(MultiPolygon multiPolygon) {
        this(multiPolygon.hasZ(), multiPolygon.hasM());
        for (Polygon copy : multiPolygon.getPolygons()) {
            addPolygon((Polygon) copy.copy());
        }
    }

    public List<Polygon> getPolygons() {
        return getGeometries();
    }

    public void setPolygons(List<Polygon> list) {
        setGeometries(list);
    }

    public void addPolygon(Polygon polygon) {
        addGeometry(polygon);
    }

    public int numPolygons() {
        return numGeometries();
    }

    public Geometry copy() {
        return new MultiPolygon(this);
    }
}
