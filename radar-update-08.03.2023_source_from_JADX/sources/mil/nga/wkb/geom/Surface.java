package mil.nga.wkb.geom;

public abstract class Surface extends Geometry {
    protected Surface(GeometryType geometryType, boolean z, boolean z2) {
        super(geometryType, z, z2);
    }
}
