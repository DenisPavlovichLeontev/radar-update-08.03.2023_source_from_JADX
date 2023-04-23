package mil.nga.wkb.geom;

import java.util.ArrayList;
import java.util.List;
import mil.nga.wkb.geom.Geometry;

public class GeometryCollection<T extends Geometry> extends Geometry {
    private List<T> geometries;

    public GeometryCollection() {
        this(false, false);
    }

    public GeometryCollection(boolean z, boolean z2) {
        super(GeometryType.GEOMETRYCOLLECTION, z, z2);
        this.geometries = new ArrayList();
    }

    public GeometryCollection(GeometryCollection<T> geometryCollection) {
        this(geometryCollection.hasZ(), geometryCollection.hasM());
        for (T copy : geometryCollection.getGeometries()) {
            addGeometry(copy.copy());
        }
    }

    protected GeometryCollection(GeometryType geometryType, boolean z, boolean z2) {
        super(geometryType, z, z2);
        this.geometries = new ArrayList();
    }

    public List<T> getGeometries() {
        return this.geometries;
    }

    public void setGeometries(List<T> list) {
        this.geometries = list;
    }

    public void addGeometry(T t) {
        this.geometries.add(t);
    }

    public int numGeometries() {
        return this.geometries.size();
    }

    public Geometry copy() {
        return new GeometryCollection(this);
    }
}
