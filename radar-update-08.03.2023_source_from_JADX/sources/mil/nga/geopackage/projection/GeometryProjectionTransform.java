package mil.nga.geopackage.projection;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.wkb.geom.CircularString;
import mil.nga.wkb.geom.CompoundCurve;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.GeometryType;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.MultiLineString;
import mil.nga.wkb.geom.MultiPoint;
import mil.nga.wkb.geom.MultiPolygon;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;
import mil.nga.wkb.geom.PolyhedralSurface;
import mil.nga.wkb.geom.TIN;
import mil.nga.wkb.geom.Triangle;
import org.osgeo.proj4j.ProjCoordinate;

public class GeometryProjectionTransform {
    private final ProjectionTransform transform;

    public GeometryProjectionTransform(ProjectionTransform projectionTransform) {
        this.transform = projectionTransform;
    }

    /* renamed from: mil.nga.geopackage.projection.GeometryProjectionTransform$1 */
    static /* synthetic */ class C11751 {
        static final /* synthetic */ int[] $SwitchMap$mil$nga$wkb$geom$GeometryType;

        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|26) */
        /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0084 */
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
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POINT     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.LINESTRING     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POLYGON     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0033 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTIPOINT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x003e }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTILINESTRING     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0049 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTIPOLYGON     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0054 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.CIRCULARSTRING     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0060 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.COMPOUNDCURVE     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x006c }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POLYHEDRALSURFACE     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0078 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.TIN     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0084 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.TRIANGLE     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0090 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.GEOMETRYCOLLECTION     // Catch:{ NoSuchFieldError -> 0x0090 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0090 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0090 }
            L_0x0090:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.projection.GeometryProjectionTransform.C11751.<clinit>():void");
        }
    }

    public Geometry transform(Geometry geometry) {
        GeometryType geometryType = geometry.getGeometryType();
        switch (C11751.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometryType.ordinal()]) {
            case 1:
                return transform((Point) geometry);
            case 2:
                return transform((LineString) geometry);
            case 3:
                return transform((Polygon) geometry);
            case 4:
                return transform((MultiPoint) geometry);
            case 5:
                return transform((MultiLineString) geometry);
            case 6:
                return transform((MultiPolygon) geometry);
            case 7:
                return transform((CircularString) geometry);
            case 8:
                return transform((CompoundCurve) geometry);
            case 9:
                return transform((PolyhedralSurface) geometry);
            case 10:
                return transform((TIN) geometry);
            case 11:
                return transform((Triangle) geometry);
            case 12:
                return transform((GeometryCollection<Geometry>) (GeometryCollection) geometry);
            default:
                throw new GeoPackageException("Unsupported Geometry Type: " + geometryType);
        }
    }

    public Point transform(Point point) {
        ProjCoordinate projCoordinate;
        if (point.hasZ()) {
            projCoordinate = new ProjCoordinate(point.getX(), point.getY(), point.getZ() != null ? point.getZ().doubleValue() : Double.NaN);
        } else {
            projCoordinate = new ProjCoordinate(point.getX(), point.getY());
        }
        ProjCoordinate transform2 = this.transform.transform(projCoordinate);
        Point point2 = new Point(point.hasZ(), point.hasM(), transform2.f409x, transform2.f410y);
        if (point.hasZ()) {
            if (Double.isNaN(transform2.f411z)) {
                point2.setZ(point.getZ());
            } else {
                point2.setZ(Double.valueOf(transform2.f411z));
            }
        }
        if (point.hasM()) {
            point2.setM(point.getM());
        }
        return point2;
    }

    public LineString transform(LineString lineString) {
        LineString lineString2 = new LineString(lineString.hasZ(), lineString.hasM());
        for (Point transform2 : lineString.getPoints()) {
            lineString2.addPoint(transform(transform2));
        }
        return lineString2;
    }

    public Polygon transform(Polygon polygon) {
        Polygon polygon2 = new Polygon(polygon.hasZ(), polygon.hasM());
        for (LineString transform2 : polygon.getRings()) {
            polygon2.addRing(transform(transform2));
        }
        return polygon2;
    }

    public MultiPoint transform(MultiPoint multiPoint) {
        MultiPoint multiPoint2 = new MultiPoint(multiPoint.hasZ(), multiPoint.hasM());
        for (Point transform2 : multiPoint.getPoints()) {
            multiPoint2.addPoint(transform(transform2));
        }
        return multiPoint2;
    }

    public MultiLineString transform(MultiLineString multiLineString) {
        MultiLineString multiLineString2 = new MultiLineString(multiLineString.hasZ(), multiLineString.hasM());
        for (LineString transform2 : multiLineString.getLineStrings()) {
            multiLineString2.addLineString(transform(transform2));
        }
        return multiLineString2;
    }

    public MultiPolygon transform(MultiPolygon multiPolygon) {
        MultiPolygon multiPolygon2 = new MultiPolygon(multiPolygon.hasZ(), multiPolygon.hasM());
        for (Polygon transform2 : multiPolygon.getPolygons()) {
            multiPolygon2.addPolygon(transform(transform2));
        }
        return multiPolygon2;
    }

    public CircularString transform(CircularString circularString) {
        CircularString circularString2 = new CircularString(circularString.hasZ(), circularString.hasM());
        for (Point transform2 : circularString.getPoints()) {
            circularString2.addPoint(transform(transform2));
        }
        return circularString2;
    }

    public CompoundCurve transform(CompoundCurve compoundCurve) {
        CompoundCurve compoundCurve2 = new CompoundCurve(compoundCurve.hasZ(), compoundCurve.hasM());
        for (LineString transform2 : compoundCurve.getLineStrings()) {
            compoundCurve2.addLineString(transform(transform2));
        }
        return compoundCurve2;
    }

    public PolyhedralSurface transform(PolyhedralSurface polyhedralSurface) {
        PolyhedralSurface polyhedralSurface2 = new PolyhedralSurface(polyhedralSurface.hasZ(), polyhedralSurface.hasM());
        for (Polygon transform2 : polyhedralSurface.getPolygons()) {
            polyhedralSurface2.addPolygon(transform(transform2));
        }
        return polyhedralSurface2;
    }

    public TIN transform(TIN tin) {
        TIN tin2 = new TIN(tin.hasZ(), tin.hasM());
        for (Polygon transform2 : tin.getPolygons()) {
            tin2.addPolygon(transform(transform2));
        }
        return tin2;
    }

    public Triangle transform(Triangle triangle) {
        Triangle triangle2 = new Triangle(triangle.hasZ(), triangle.hasM());
        for (LineString transform2 : triangle.getRings()) {
            triangle2.addRing(transform(transform2));
        }
        return triangle2;
    }

    public GeometryCollection<Geometry> transform(GeometryCollection<Geometry> geometryCollection) {
        GeometryCollection<Geometry> geometryCollection2 = new GeometryCollection<>(geometryCollection.hasZ(), geometryCollection.hasM());
        for (Geometry transform2 : geometryCollection.getGeometries()) {
            geometryCollection2.addGeometry(transform(transform2));
        }
        return geometryCollection2;
    }
}
