package mil.nga.wkb.geom;

import mil.nga.wkb.geom.Surface;

public abstract class MultiSurface<T extends Surface> extends GeometryCollection<T> {
    protected MultiSurface(GeometryType geometryType, boolean z, boolean z2) {
        super(geometryType, z, z2);
    }
}
