package mil.nga.wkb.geom;

import java.util.List;

public class MultiLineString extends MultiCurve<LineString> {
    public MultiLineString() {
        this(false, false);
    }

    public MultiLineString(boolean z, boolean z2) {
        super(GeometryType.MULTILINESTRING, z, z2);
    }

    public MultiLineString(MultiLineString multiLineString) {
        this(multiLineString.hasZ(), multiLineString.hasM());
        for (LineString copy : multiLineString.getLineStrings()) {
            addLineString((LineString) copy.copy());
        }
    }

    public List<LineString> getLineStrings() {
        return getGeometries();
    }

    public void setLineStrings(List<LineString> list) {
        setGeometries(list);
    }

    public void addLineString(LineString lineString) {
        addGeometry(lineString);
    }

    public int numLineStrings() {
        return numGeometries();
    }

    public Geometry copy() {
        return new MultiLineString(this);
    }
}
