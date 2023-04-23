package mil.nga.wkb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.wkb.geom.CircularString;
import mil.nga.wkb.geom.CompoundCurve;
import mil.nga.wkb.geom.Curve;
import mil.nga.wkb.geom.CurvePolygon;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.MultiLineString;
import mil.nga.wkb.geom.MultiPoint;
import mil.nga.wkb.geom.MultiPolygon;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;
import mil.nga.wkb.geom.PolyhedralSurface;
import mil.nga.wkb.geom.TIN;
import mil.nga.wkb.geom.Triangle;

public class GeometryJSONCompatible {
    public static Object getJSONCompatibleGeometry(Geometry geometry) {
        HashMap hashMap = new HashMap();
        Object jSONCompatibleGeometryObject = getJSONCompatibleGeometryObject(geometry);
        if (jSONCompatibleGeometryObject != null) {
            hashMap.put(geometry.getGeometryType().getName(), jSONCompatibleGeometryObject);
        }
        return hashMap;
    }

    /* renamed from: mil.nga.wkb.util.GeometryJSONCompatible$1 */
    static /* synthetic */ class C11881 {
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
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.CURVEPOLYGON     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0078 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POLYHEDRALSURFACE     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0084 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.TIN     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0090 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.TRIANGLE     // Catch:{ NoSuchFieldError -> 0x0090 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0090 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0090 }
            L_0x0090:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x009c }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.GEOMETRYCOLLECTION     // Catch:{ NoSuchFieldError -> 0x009c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009c }
                r2 = 13
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x009c }
            L_0x009c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.wkb.util.GeometryJSONCompatible.C11881.<clinit>():void");
        }
    }

    private static Object getJSONCompatibleGeometryObject(Geometry geometry) {
        switch (C11881.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometry.getGeometryType().ordinal()]) {
            case 1:
                return getPoint((Point) geometry);
            case 2:
                return getLineString((LineString) geometry);
            case 3:
                return getPolygon((Polygon) geometry);
            case 4:
                return getMultiPoint((MultiPoint) geometry);
            case 5:
                return getMultiLineString((MultiLineString) geometry);
            case 6:
                return getMultiPolygon((MultiPolygon) geometry);
            case 7:
                return getLineString((CircularString) geometry);
            case 8:
                return getCompoundCurve((CompoundCurve) geometry);
            case 9:
                return getCurvePolygon((CurvePolygon) geometry);
            case 10:
                return getPolyhedralSurface((PolyhedralSurface) geometry);
            case 11:
                return getPolyhedralSurface((TIN) geometry);
            case 12:
                return getPolygon((Triangle) geometry);
            case 13:
                ArrayList arrayList = new ArrayList();
                List geometries = ((GeometryCollection) geometry).getGeometries();
                for (int i = 0; i < geometries.size(); i++) {
                    arrayList.add(getJSONCompatibleGeometry((Geometry) geometries.get(i)));
                }
                return arrayList;
            default:
                return null;
        }
    }

    private static Object getPoint(Point point) {
        HashMap hashMap = new HashMap();
        hashMap.put("x", Double.valueOf(point.getX()));
        hashMap.put("y", Double.valueOf(point.getY()));
        if (point.hasZ()) {
            hashMap.put(GeometryColumns.COLUMN_Z, point.getZ());
        }
        if (point.hasM()) {
            hashMap.put(GeometryColumns.COLUMN_M, point.getM());
        }
        return hashMap;
    }

    private static Object getMultiPoint(MultiPoint multiPoint) {
        ArrayList arrayList = new ArrayList();
        List<Point> points = multiPoint.getPoints();
        for (int i = 0; i < points.size(); i++) {
            arrayList.add(getPoint(points.get(i)));
        }
        return arrayList;
    }

    private static Object getLineString(LineString lineString) {
        ArrayList arrayList = new ArrayList();
        for (Point point : lineString.getPoints()) {
            arrayList.add(getPoint(point));
        }
        return arrayList;
    }

    private static Object getMultiLineString(MultiLineString multiLineString) {
        ArrayList arrayList = new ArrayList();
        List<LineString> lineStrings = multiLineString.getLineStrings();
        for (int i = 0; i < lineStrings.size(); i++) {
            arrayList.add(getLineString(lineStrings.get(i)));
        }
        return arrayList;
    }

    private static Object getPolygon(Polygon polygon) {
        ArrayList arrayList = new ArrayList();
        List rings = polygon.getRings();
        for (int i = 0; i < rings.size(); i++) {
            arrayList.add(getLineString((LineString) rings.get(i)));
        }
        return arrayList;
    }

    private static Object getMultiPolygon(MultiPolygon multiPolygon) {
        ArrayList arrayList = new ArrayList();
        List<Polygon> polygons = multiPolygon.getPolygons();
        for (int i = 0; i < polygons.size(); i++) {
            arrayList.add(getPolygon(polygons.get(i)));
        }
        return arrayList;
    }

    private static Object getCompoundCurve(CompoundCurve compoundCurve) {
        ArrayList arrayList = new ArrayList();
        List<LineString> lineStrings = compoundCurve.getLineStrings();
        for (int i = 0; i < lineStrings.size(); i++) {
            arrayList.add(getLineString(lineStrings.get(i)));
        }
        return arrayList;
    }

    private static Object getCurvePolygon(CurvePolygon<Curve> curvePolygon) {
        ArrayList arrayList = new ArrayList();
        List<Curve> rings = curvePolygon.getRings();
        for (int i = 0; i < rings.size(); i++) {
            arrayList.add(getJSONCompatibleGeometryObject(rings.get(i)));
        }
        return arrayList;
    }

    private static Object getPolyhedralSurface(PolyhedralSurface polyhedralSurface) {
        ArrayList arrayList = new ArrayList();
        List<Polygon> polygons = polyhedralSurface.getPolygons();
        for (int i = 0; i < polygons.size(); i++) {
            arrayList.add(getPolygon(polygons.get(i)));
        }
        return arrayList;
    }
}
