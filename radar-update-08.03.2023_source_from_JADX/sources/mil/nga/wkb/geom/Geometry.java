package mil.nga.wkb.geom;

public abstract class Geometry {
    private final GeometryType geometryType;
    private final boolean hasM;
    private final boolean hasZ;

    public abstract Geometry copy();

    protected Geometry(GeometryType geometryType2, boolean z, boolean z2) {
        this.geometryType = geometryType2;
        this.hasZ = z;
        this.hasM = z2;
    }

    public GeometryType getGeometryType() {
        return this.geometryType;
    }

    public boolean hasZ() {
        return this.hasZ;
    }

    public boolean hasM() {
        return this.hasM;
    }

    public int getWkbCode() {
        int code = this.geometryType.getCode();
        if (this.hasZ) {
            code += 1000;
        }
        return this.hasM ? code + 2000 : code;
    }
}
