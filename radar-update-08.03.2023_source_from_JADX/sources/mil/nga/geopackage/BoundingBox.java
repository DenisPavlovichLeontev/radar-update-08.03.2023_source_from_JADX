package mil.nga.geopackage;

import mil.nga.geopackage.projection.ProjectionConstants;
import mil.nga.wkb.geom.GeometryEnvelope;

public class BoundingBox {
    private double maxLatitude;
    private double maxLongitude;
    private double minLatitude;
    private double minLongitude;

    public BoundingBox() {
        this(-ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH, -ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT, ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH, ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT);
    }

    public BoundingBox(double d, double d2, double d3, double d4) {
        this.minLongitude = d;
        this.minLatitude = d2;
        this.maxLongitude = d3;
        this.maxLatitude = d4;
    }

    public BoundingBox(BoundingBox boundingBox) {
        this(boundingBox.getMinLongitude(), boundingBox.getMinLatitude(), boundingBox.getMaxLongitude(), boundingBox.getMaxLatitude());
    }

    public BoundingBox(GeometryEnvelope geometryEnvelope) {
        this(geometryEnvelope.getMinX(), geometryEnvelope.getMinY(), geometryEnvelope.getMaxX(), geometryEnvelope.getMaxY());
    }

    public double getMinLongitude() {
        return this.minLongitude;
    }

    public void setMinLongitude(double d) {
        this.minLongitude = d;
    }

    public double getMaxLongitude() {
        return this.maxLongitude;
    }

    public void setMaxLongitude(double d) {
        this.maxLongitude = d;
    }

    public double getMinLatitude() {
        return this.minLatitude;
    }

    public void setMinLatitude(double d) {
        this.minLatitude = d;
    }

    public double getMaxLatitude() {
        return this.maxLatitude;
    }

    public void setMaxLatitude(double d) {
        this.maxLatitude = d;
    }

    public GeometryEnvelope buildEnvelope() {
        GeometryEnvelope geometryEnvelope = new GeometryEnvelope();
        geometryEnvelope.setMinX(this.minLongitude);
        geometryEnvelope.setMaxX(this.maxLongitude);
        geometryEnvelope.setMinY(this.minLatitude);
        geometryEnvelope.setMaxY(this.maxLatitude);
        return geometryEnvelope;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002c  */
    /* JADX WARNING: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public mil.nga.geopackage.BoundingBox complementary(double r9) {
        /*
            r8 = this;
            double r0 = r8.maxLongitude
            int r2 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1))
            r3 = 0
            if (r2 <= 0) goto L_0x0016
            double r0 = r8.minLongitude
            double r4 = -r9
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 < 0) goto L_0x0029
            r0 = -4611686018427387904(0xc000000000000000, double:-2.0)
            double r9 = r9 * r0
            java.lang.Double r9 = java.lang.Double.valueOf(r9)
            goto L_0x002a
        L_0x0016:
            double r4 = r8.minLongitude
            double r6 = -r9
            int r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r2 >= 0) goto L_0x0029
            int r0 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1))
            if (r0 > 0) goto L_0x0029
            r0 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r9 = r9 * r0
            java.lang.Double r9 = java.lang.Double.valueOf(r9)
            goto L_0x002a
        L_0x0029:
            r9 = r3
        L_0x002a:
            if (r9 == 0) goto L_0x0049
            mil.nga.geopackage.BoundingBox r3 = new mil.nga.geopackage.BoundingBox
            r3.<init>((mil.nga.geopackage.BoundingBox) r8)
            double r0 = r3.getMinLongitude()
            double r4 = r9.doubleValue()
            double r0 = r0 + r4
            r3.setMinLongitude(r0)
            double r0 = r3.getMaxLongitude()
            double r9 = r9.doubleValue()
            double r0 = r0 + r9
            r3.setMaxLongitude(r0)
        L_0x0049:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.BoundingBox.complementary(double):mil.nga.geopackage.BoundingBox");
    }

    public BoundingBox complementaryWgs84() {
        return complementary(ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH);
    }

    public BoundingBox complementaryWebMercator() {
        return complementary(ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH);
    }

    public BoundingBox boundCoordinates(double d) {
        BoundingBox boundingBox = new BoundingBox(this);
        double d2 = 2.0d * d;
        boundingBox.setMinLongitude(((getMinLongitude() + d) % d2) - d);
        boundingBox.setMaxLongitude(((getMaxLongitude() + d) % d2) - d);
        return boundingBox;
    }

    public BoundingBox boundWgs84Coordinates() {
        return boundCoordinates(ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH);
    }

    public BoundingBox boundWebMercatorCoordinates() {
        return boundCoordinates(ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH);
    }

    public BoundingBox expandCoordinates(double d) {
        BoundingBox boundingBox = new BoundingBox(this);
        double minLongitude2 = getMinLongitude();
        double maxLongitude2 = getMaxLongitude();
        if (minLongitude2 > maxLongitude2) {
            boundingBox.setMaxLongitude(maxLongitude2 + (((double) ((((int) ((minLongitude2 - maxLongitude2) / (2.0d * d))) + 1) * 2)) * d));
        }
        return boundingBox;
    }

    public BoundingBox expandWgs84Coordinates() {
        return expandCoordinates(ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH);
    }

    public BoundingBox expandWebMercatorCoordinates() {
        return expandCoordinates(ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH);
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.maxLatitude);
        long doubleToLongBits2 = Double.doubleToLongBits(this.maxLongitude);
        int i = ((((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 31) * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
        long doubleToLongBits3 = Double.doubleToLongBits(this.minLatitude);
        int i2 = (i * 31) + ((int) (doubleToLongBits3 ^ (doubleToLongBits3 >>> 32)));
        long doubleToLongBits4 = Double.doubleToLongBits(this.minLongitude);
        return (i2 * 31) + ((int) ((doubleToLongBits4 >>> 32) ^ doubleToLongBits4));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BoundingBox boundingBox = (BoundingBox) obj;
        return Double.doubleToLongBits(this.maxLatitude) == Double.doubleToLongBits(boundingBox.maxLatitude) && Double.doubleToLongBits(this.maxLongitude) == Double.doubleToLongBits(boundingBox.maxLongitude) && Double.doubleToLongBits(this.minLatitude) == Double.doubleToLongBits(boundingBox.minLatitude) && Double.doubleToLongBits(this.minLongitude) == Double.doubleToLongBits(boundingBox.minLongitude);
    }
}
