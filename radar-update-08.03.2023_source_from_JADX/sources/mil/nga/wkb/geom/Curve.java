package mil.nga.wkb.geom;

public abstract class Curve extends Geometry {
    protected Curve(GeometryType geometryType, boolean z, boolean z2) {
        super(geometryType, z, z2);
    }
}
