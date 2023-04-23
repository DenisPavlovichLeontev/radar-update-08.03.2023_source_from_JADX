package mil.nga.wkb.geom;

import java.util.ArrayList;
import java.util.List;

public class CompoundCurve extends Curve {
    private List<LineString> lineStrings;

    public CompoundCurve() {
        this(false, false);
    }

    public CompoundCurve(boolean z, boolean z2) {
        super(GeometryType.COMPOUNDCURVE, z, z2);
        this.lineStrings = new ArrayList();
    }

    public CompoundCurve(CompoundCurve compoundCurve) {
        this(compoundCurve.hasZ(), compoundCurve.hasM());
        for (LineString copy : compoundCurve.getLineStrings()) {
            addLineString((LineString) copy.copy());
        }
    }

    public List<LineString> getLineStrings() {
        return this.lineStrings;
    }

    public void setLineStrings(List<LineString> list) {
        this.lineStrings = list;
    }

    public void addLineString(LineString lineString) {
        this.lineStrings.add(lineString);
    }

    public int numLineStrings() {
        return this.lineStrings.size();
    }

    public Geometry copy() {
        return new CompoundCurve(this);
    }
}
