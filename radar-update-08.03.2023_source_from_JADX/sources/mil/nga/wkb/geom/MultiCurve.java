package mil.nga.wkb.geom;

import mil.nga.wkb.geom.Curve;

public abstract class MultiCurve<T extends Curve> extends GeometryCollection<T> {
    protected MultiCurve(GeometryType geometryType, boolean z, boolean z2) {
        super(geometryType, z, z2);
    }
}
