package mil.nga.wkb.geom;

import java.util.ArrayList;
import java.util.List;
import mil.nga.wkb.geom.Curve;

public class CurvePolygon<T extends Curve> extends Surface {
    private List<T> rings;

    public CurvePolygon() {
        this(false, false);
    }

    public CurvePolygon(boolean z, boolean z2) {
        super(GeometryType.CURVEPOLYGON, z, z2);
        this.rings = new ArrayList();
    }

    public CurvePolygon(CurvePolygon<T> curvePolygon) {
        this(curvePolygon.hasZ(), curvePolygon.hasM());
        for (T copy : curvePolygon.getRings()) {
            addRing((Curve) copy.copy());
        }
    }

    protected CurvePolygon(GeometryType geometryType, boolean z, boolean z2) {
        super(geometryType, z, z2);
        this.rings = new ArrayList();
    }

    public List<T> getRings() {
        return this.rings;
    }

    public void setRings(List<T> list) {
        this.rings = list;
    }

    public void addRing(T t) {
        this.rings.add(t);
    }

    public int numRings() {
        return this.rings.size();
    }

    public Geometry copy() {
        return new CurvePolygon(this);
    }
}
