package mil.nga.wkb.util;

import java.util.List;
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

public class GeometryPrinter {
    public static String getGeometryString(Geometry geometry) {
        StringBuilder sb = new StringBuilder();
        switch (C11891.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometry.getGeometryType().ordinal()]) {
            case 1:
                addPointMessage(sb, (Point) geometry);
                break;
            case 2:
                addLineStringMessage(sb, (LineString) geometry);
                break;
            case 3:
                addPolygonMessage(sb, (Polygon) geometry);
                break;
            case 4:
                addMultiPointMessage(sb, (MultiPoint) geometry);
                break;
            case 5:
                addMultiLineStringMessage(sb, (MultiLineString) geometry);
                break;
            case 6:
                addMultiPolygonMessage(sb, (MultiPolygon) geometry);
                break;
            case 7:
                addLineStringMessage(sb, (CircularString) geometry);
                break;
            case 8:
                addCompoundCurveMessage(sb, (CompoundCurve) geometry);
                break;
            case 9:
                addCurvePolygonMessage(sb, (CurvePolygon) geometry);
                break;
            case 10:
                addPolyhedralSurfaceMessage(sb, (PolyhedralSurface) geometry);
                break;
            case 11:
                addPolyhedralSurfaceMessage(sb, (TIN) geometry);
                break;
            case 12:
                addPolygonMessage(sb, (Triangle) geometry);
                break;
            case 13:
                GeometryCollection geometryCollection = (GeometryCollection) geometry;
                sb.append("Geometries: " + geometryCollection.numGeometries());
                List geometries = geometryCollection.getGeometries();
                int i = 0;
                while (i < geometries.size()) {
                    Geometry geometry2 = (Geometry) geometries.get(i);
                    sb.append("\n\n");
                    StringBuilder sb2 = new StringBuilder();
                    Class<Geometry> cls = Geometry.class;
                    sb2.append("Geometry");
                    sb2.append(" ");
                    i++;
                    sb2.append(i);
                    sb.append(sb2.toString());
                    sb.append("\n");
                    sb.append(geometry2.getGeometryType().getName());
                    sb.append("\n");
                    sb.append(getGeometryString(geometry2));
                }
                break;
        }
        return sb.toString();
    }

    /* renamed from: mil.nga.wkb.util.GeometryPrinter$1 */
    static /* synthetic */ class C11891 {
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
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.wkb.util.GeometryPrinter.C11891.<clinit>():void");
        }
    }

    private static void addPointMessage(StringBuilder sb, Point point) {
        sb.append("Latitude: ");
        sb.append(point.getY());
        sb.append("\nLongitude: ");
        sb.append(point.getX());
    }

    private static void addMultiPointMessage(StringBuilder sb, MultiPoint multiPoint) {
        StringBuilder sb2 = new StringBuilder();
        Class<Point> cls = Point.class;
        sb2.append("Point");
        sb2.append("s: ");
        sb2.append(multiPoint.numPoints());
        sb.append(sb2.toString());
        List<Point> points = multiPoint.getPoints();
        int i = 0;
        while (i < points.size()) {
            sb.append("\n\n");
            StringBuilder sb3 = new StringBuilder();
            Class<Point> cls2 = Point.class;
            sb3.append("Point");
            sb3.append(" ");
            i++;
            sb3.append(i);
            sb.append(sb3.toString());
            sb.append("\n");
            addPointMessage(sb, points.get(i));
        }
    }

    private static void addLineStringMessage(StringBuilder sb, LineString lineString) {
        StringBuilder sb2 = new StringBuilder();
        Class<Point> cls = Point.class;
        sb2.append("Point");
        sb2.append("s: ");
        sb2.append(lineString.numPoints());
        sb.append(sb2.toString());
        for (Point addPointMessage : lineString.getPoints()) {
            sb.append("\n\n");
            addPointMessage(sb, addPointMessage);
        }
    }

    private static void addMultiLineStringMessage(StringBuilder sb, MultiLineString multiLineString) {
        StringBuilder sb2 = new StringBuilder();
        Class<LineString> cls = LineString.class;
        sb2.append("LineString");
        sb2.append("s: ");
        sb2.append(multiLineString.numLineStrings());
        sb.append(sb2.toString());
        List<LineString> lineStrings = multiLineString.getLineStrings();
        int i = 0;
        while (i < lineStrings.size()) {
            sb.append("\n\n");
            StringBuilder sb3 = new StringBuilder();
            Class<LineString> cls2 = LineString.class;
            sb3.append("LineString");
            sb3.append(" ");
            i++;
            sb3.append(i);
            sb.append(sb3.toString());
            sb.append("\n");
            addLineStringMessage(sb, lineStrings.get(i));
        }
    }

    private static void addPolygonMessage(StringBuilder sb, Polygon polygon) {
        sb.append("Rings: " + polygon.numRings());
        List rings = polygon.getRings();
        for (int i = 0; i < rings.size(); i++) {
            LineString lineString = (LineString) rings.get(i);
            sb.append("\n\n");
            if (i > 0) {
                sb.append("Hole " + i);
                sb.append("\n");
            }
            addLineStringMessage(sb, lineString);
        }
    }

    private static void addMultiPolygonMessage(StringBuilder sb, MultiPolygon multiPolygon) {
        StringBuilder sb2 = new StringBuilder();
        Class<Polygon> cls = Polygon.class;
        sb2.append("Polygon");
        sb2.append("s: ");
        sb2.append(multiPolygon.numPolygons());
        sb.append(sb2.toString());
        List<Polygon> polygons = multiPolygon.getPolygons();
        int i = 0;
        while (i < polygons.size()) {
            sb.append("\n\n");
            StringBuilder sb3 = new StringBuilder();
            Class<Polygon> cls2 = Polygon.class;
            sb3.append("Polygon");
            sb3.append(" ");
            i++;
            sb3.append(i);
            sb.append(sb3.toString());
            sb.append("\n");
            addPolygonMessage(sb, polygons.get(i));
        }
    }

    private static void addCompoundCurveMessage(StringBuilder sb, CompoundCurve compoundCurve) {
        StringBuilder sb2 = new StringBuilder();
        Class<LineString> cls = LineString.class;
        sb2.append("LineString");
        sb2.append("s: ");
        sb2.append(compoundCurve.numLineStrings());
        sb.append(sb2.toString());
        List<LineString> lineStrings = compoundCurve.getLineStrings();
        int i = 0;
        while (i < lineStrings.size()) {
            sb.append("\n\n");
            StringBuilder sb3 = new StringBuilder();
            Class<LineString> cls2 = LineString.class;
            sb3.append("LineString");
            sb3.append(" ");
            i++;
            sb3.append(i);
            sb.append(sb3.toString());
            sb.append("\n");
            addLineStringMessage(sb, lineStrings.get(i));
        }
    }

    private static void addCurvePolygonMessage(StringBuilder sb, CurvePolygon<Curve> curvePolygon) {
        sb.append("Rings: " + curvePolygon.numRings());
        List<Curve> rings = curvePolygon.getRings();
        for (int i = 0; i < rings.size(); i++) {
            Curve curve = rings.get(i);
            sb.append("\n\n");
            if (i > 0) {
                sb.append("Hole " + i);
                sb.append("\n");
            }
            sb.append(getGeometryString(curve));
        }
    }

    private static void addPolyhedralSurfaceMessage(StringBuilder sb, PolyhedralSurface polyhedralSurface) {
        StringBuilder sb2 = new StringBuilder();
        Class<Polygon> cls = Polygon.class;
        sb2.append("Polygon");
        sb2.append("s: ");
        sb2.append(polyhedralSurface.numPolygons());
        sb.append(sb2.toString());
        List<Polygon> polygons = polyhedralSurface.getPolygons();
        int i = 0;
        while (i < polygons.size()) {
            sb.append("\n\n");
            StringBuilder sb3 = new StringBuilder();
            Class<Polygon> cls2 = Polygon.class;
            sb3.append("Polygon");
            sb3.append(" ");
            i++;
            sb3.append(i);
            sb.append(sb3.toString());
            sb.append("\n");
            addPolygonMessage(sb, polygons.get(i));
        }
    }
}
