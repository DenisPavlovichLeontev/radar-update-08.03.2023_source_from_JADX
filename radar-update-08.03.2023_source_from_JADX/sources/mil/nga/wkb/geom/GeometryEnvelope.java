package mil.nga.wkb.geom;

public class GeometryEnvelope {
    private boolean hasM;
    private boolean hasZ;
    private Double maxM;
    private double maxX;
    private double maxY;
    private Double maxZ;
    private Double minM;
    private double minX;
    private double minY;
    private Double minZ;

    public GeometryEnvelope() {
        this.hasZ = false;
        this.hasM = false;
    }

    public GeometryEnvelope(boolean z, boolean z2) {
        this.hasZ = z;
        this.hasM = z2;
    }

    public boolean hasZ() {
        return this.hasZ;
    }

    public boolean hasM() {
        return this.hasM;
    }

    public double getMinX() {
        return this.minX;
    }

    public void setMinX(double d) {
        this.minX = d;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public void setMaxX(double d) {
        this.maxX = d;
    }

    public double getMinY() {
        return this.minY;
    }

    public void setMinY(double d) {
        this.minY = d;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public void setMaxY(double d) {
        this.maxY = d;
    }

    public boolean isHasZ() {
        return this.hasZ;
    }

    public void setHasZ(boolean z) {
        this.hasZ = z;
    }

    public Double getMinZ() {
        return this.minZ;
    }

    public void setMinZ(Double d) {
        this.minZ = d;
    }

    public Double getMaxZ() {
        return this.maxZ;
    }

    public void setMaxZ(Double d) {
        this.maxZ = d;
    }

    public boolean isHasM() {
        return this.hasM;
    }

    public void setHasM(boolean z) {
        this.hasM = z;
    }

    public Double getMinM() {
        return this.minM;
    }

    public void setMinM(Double d) {
        this.minM = d;
    }

    public Double getMaxM() {
        return this.maxM;
    }

    public void setMaxM(Double d) {
        this.maxM = d;
    }
}
