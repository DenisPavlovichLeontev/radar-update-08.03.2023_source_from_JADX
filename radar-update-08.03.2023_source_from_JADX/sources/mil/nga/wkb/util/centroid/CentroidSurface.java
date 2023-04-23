package mil.nga.wkb.util.centroid;

import java.util.List;
import mil.nga.wkb.geom.CompoundCurve;
import mil.nga.wkb.geom.Curve;
import mil.nga.wkb.geom.CurvePolygon;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.GeometryType;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.MultiPolygon;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;
import mil.nga.wkb.geom.PolyhedralSurface;
import mil.nga.wkb.util.WkbException;

public class CentroidSurface {
    private double area = 0.0d;
    private Point base;
    private Point sum = new Point();

    public CentroidSurface() {
    }

    public CentroidSurface(Geometry geometry) {
        add(geometry);
    }

    /* renamed from: mil.nga.wkb.util.centroid.CentroidSurface$1 */
    static /* synthetic */ class C11931 {
        static final /* synthetic */ int[] $SwitchMap$mil$nga$wkb$geom$GeometryType;

        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|(3:25|26|28)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0084 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0090 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                mil.nga.wkb.geom.GeometryType[] r0 = mil.nga.wkb.geom.GeometryType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$mil$nga$wkb$geom$GeometryType = r0
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POLYGON     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.TRIANGLE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTIPOLYGON     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0033 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.CURVEPOLYGON     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x003e }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POLYHEDRALSURFACE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0049 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.TIN     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0054 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.GEOMETRYCOLLECTION     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0060 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POINT     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x006c }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTIPOINT     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0078 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.LINESTRING     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0084 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.CIRCULARSTRING     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0090 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTILINESTRING     // Catch:{ NoSuchFieldError -> 0x0090 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0090 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0090 }
            L_0x0090:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x009c }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.COMPOUNDCURVE     // Catch:{ NoSuchFieldError -> 0x009c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009c }
                r2 = 13
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x009c }
            L_0x009c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.wkb.util.centroid.CentroidSurface.C11931.<clinit>():void");
        }
    }

    public void add(Geometry geometry) {
        GeometryType geometryType = geometry.getGeometryType();
        switch (C11931.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometryType.ordinal()]) {
            case 1:
            case 2:
                add((Polygon) geometry);
                return;
            case 3:
                add(((MultiPolygon) geometry).getPolygons());
                return;
            case 4:
                add((CurvePolygon<Curve>) (CurvePolygon) geometry);
                return;
            case 5:
            case 6:
                add(((PolyhedralSurface) geometry).getPolygons());
                return;
            case 7:
                for (Geometry add : ((GeometryCollection) geometry).getGeometries()) {
                    add(add);
                }
                return;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                return;
            default:
                throw new WkbException("Unsupported " + getClass().getSimpleName() + " Geometry Type: " + geometryType);
        }
    }

    private void add(List<Polygon> list) {
        for (Polygon add : list) {
            add(add);
        }
    }

    private void add(Polygon polygon) {
        List rings = polygon.getRings();
        add((LineString) rings.get(0));
        for (int i = 1; i < rings.size(); i++) {
            addHole((LineString) rings.get(i));
        }
    }

    private void add(CurvePolygon<Curve> curvePolygon) {
        List<Curve> rings = curvePolygon.getRings();
        Curve curve = rings.get(0);
        GeometryType geometryType = curve.getGeometryType();
        int i = C11931.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometryType.ordinal()];
        if (i == 10 || i == 11) {
            add((LineString) curve);
        } else if (i == 13) {
            for (LineString add : ((CompoundCurve) curve).getLineStrings()) {
                add(add);
            }
        } else {
            throw new WkbException("Unexpected Curve Type: " + geometryType);
        }
        for (int i2 = 1; i2 < rings.size(); i2++) {
            Curve curve2 = rings.get(i2);
            GeometryType geometryType2 = curve2.getGeometryType();
            int i3 = C11931.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometryType2.ordinal()];
            if (i3 == 10 || i3 == 11) {
                addHole((LineString) curve2);
            } else if (i3 == 13) {
                for (LineString addHole : ((CompoundCurve) curve2).getLineStrings()) {
                    addHole(addHole);
                }
            } else {
                throw new WkbException("Unexpected Curve Type: " + geometryType2);
            }
        }
    }

    private void add(LineString lineString) {
        add(true, lineString);
    }

    private void addHole(LineString lineString) {
        add(false, lineString);
    }

    private void add(boolean z, LineString lineString) {
        List<Point> points = lineString.getPoints();
        int i = 0;
        Point point = points.get(0);
        if (this.base == null) {
            this.base = point;
        }
        while (i < points.size() - 1) {
            i++;
            addTriangle(z, this.base, points.get(i), points.get(i));
        }
        Point point2 = points.get(points.size() - 1);
        if (point.getX() != point2.getX() || point.getY() != point2.getY()) {
            addTriangle(z, this.base, point2, point);
        }
    }

    private void addTriangle(boolean z, Point point, Point point2, Point point3) {
        double d = z ? 1.0d : -1.0d;
        Point centroid3 = centroid3(point, point2, point3);
        double area2 = area2(point, point2, point3);
        Point point4 = this.sum;
        double d2 = d * area2;
        point4.setX(point4.getX() + (centroid3.getX() * d2));
        Point point5 = this.sum;
        point5.setY(point5.getY() + (centroid3.getY() * d2));
        this.area += d2;
    }

    private Point centroid3(Point point, Point point2, Point point3) {
        return new Point(point.getX() + point2.getX() + point3.getX(), point.getY() + point2.getY() + point3.getY());
    }

    private static double area2(Point point, Point point2, Point point3) {
        return ((point2.getX() - point.getX()) * (point3.getY() - point.getY())) - ((point3.getX() - point.getX()) * (point2.getY() - point.getY()));
    }

    public Point getCentroid() {
        return new Point((this.sum.getX() / 3.0d) / this.area, (this.sum.getY() / 3.0d) / this.area);
    }
}
