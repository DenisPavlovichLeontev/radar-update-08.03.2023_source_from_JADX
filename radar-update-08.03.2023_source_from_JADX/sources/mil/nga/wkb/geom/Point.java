package mil.nga.wkb.geom;

public class Point extends Geometry {

    /* renamed from: m */
    private Double f360m;

    /* renamed from: x */
    private double f361x;

    /* renamed from: y */
    private double f362y;

    /* renamed from: z */
    private Double f363z;

    public Point(double d, double d2) {
        this(false, false, d, d2);
    }

    public Point() {
        this(0.0d, 0.0d);
    }

    public Point(boolean z, boolean z2, double d, double d2) {
        super(GeometryType.POINT, z, z2);
        this.f361x = d;
        this.f362y = d2;
    }

    public Point(Point point) {
        this(point.hasZ(), point.hasM(), point.getX(), point.getY());
        setZ(point.getZ());
        setM(point.getM());
    }

    public double getX() {
        return this.f361x;
    }

    public void setX(double d) {
        this.f361x = d;
    }

    public double getY() {
        return this.f362y;
    }

    public void setY(double d) {
        this.f362y = d;
    }

    public Double getZ() {
        return this.f363z;
    }

    public void setZ(Double d) {
        this.f363z = d;
    }

    public Double getM() {
        return this.f360m;
    }

    public void setM(Double d) {
        this.f360m = d;
    }

    public Geometry copy() {
        return new Point(this);
    }
}
