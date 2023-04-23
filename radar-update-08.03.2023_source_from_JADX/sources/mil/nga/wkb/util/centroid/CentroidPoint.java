package mil.nga.wkb.util.centroid;

import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.GeometryType;
import mil.nga.wkb.geom.MultiPoint;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.util.WkbException;

public class CentroidPoint {
    private int count = 0;
    private Point sum = new Point();

    public CentroidPoint() {
    }

    public CentroidPoint(Geometry geometry) {
        add(geometry);
    }

    /* renamed from: mil.nga.wkb.util.centroid.CentroidPoint$1 */
    static /* synthetic */ class C11921 {
        static final /* synthetic */ int[] $SwitchMap$mil$nga$wkb$geom$GeometryType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                mil.nga.wkb.geom.GeometryType[] r0 = mil.nga.wkb.geom.GeometryType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$mil$nga$wkb$geom$GeometryType = r0
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POINT     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTIPOINT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.GEOMETRYCOLLECTION     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.wkb.util.centroid.CentroidPoint.C11921.<clinit>():void");
        }
    }

    public void add(Geometry geometry) {
        GeometryType geometryType = geometry.getGeometryType();
        int i = C11921.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometryType.ordinal()];
        if (i == 1) {
            add((Point) geometry);
        } else if (i == 2) {
            for (Point add : ((MultiPoint) geometry).getPoints()) {
                add(add);
            }
        } else if (i == 3) {
            for (Geometry add2 : ((GeometryCollection) geometry).getGeometries()) {
                add(add2);
            }
        } else {
            throw new WkbException("Unsupported " + getClass().getSimpleName() + " Geometry Type: " + geometryType);
        }
    }

    private void add(Point point) {
        this.count++;
        Point point2 = this.sum;
        point2.setX(point2.getX() + point.getX());
        Point point3 = this.sum;
        point3.setY(point3.getY() + point.getY());
    }

    public Point getCentroid() {
        return new Point(this.sum.getX() / ((double) this.count), this.sum.getY() / ((double) this.count));
    }
}
