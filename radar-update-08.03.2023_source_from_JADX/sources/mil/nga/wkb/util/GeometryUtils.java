package mil.nga.wkb.util;

import java.util.ArrayList;
import java.util.List;
import mil.nga.wkb.geom.CircularString;
import mil.nga.wkb.geom.CompoundCurve;
import mil.nga.wkb.geom.Curve;
import mil.nga.wkb.geom.CurvePolygon;
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
import mil.nga.wkb.util.centroid.CentroidCurve;
import mil.nga.wkb.util.centroid.CentroidPoint;
import mil.nga.wkb.util.centroid.CentroidSurface;

public class GeometryUtils {
    public static final double DEFAULT_EPSILON = 1.0E-15d;

    public static int getDimension(Geometry geometry) {
        GeometryType geometryType = geometry.getGeometryType();
        switch (C11901.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometryType.ordinal()]) {
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
            case 5:
            case 6:
                return 1;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                return 2;
            case 13:
                int i = -1;
                for (Geometry dimension : ((GeometryCollection) geometry).getGeometries()) {
                    i = Math.max(i, getDimension(dimension));
                }
                return i;
            default:
                throw new WkbException("Unsupported Geometry Type: " + geometryType);
        }
    }

    public static double distance(Point point, Point point2) {
        double x = point.getX() - point2.getX();
        double y = point.getY() - point2.getY();
        return Math.sqrt((x * x) + (y * y));
    }

    public static Point getCentroid(Geometry geometry) {
        int dimension = getDimension(geometry);
        if (dimension == 0) {
            return new CentroidPoint(geometry).getCentroid();
        }
        if (dimension == 1) {
            return new CentroidCurve(geometry).getCentroid();
        }
        if (dimension != 2) {
            return null;
        }
        return new CentroidSurface(geometry).getCentroid();
    }

    public static void minimizeGeometry(Geometry geometry, double d) {
        switch (geometry.getGeometryType()) {
            case LINESTRING:
                minimize((LineString) geometry, d);
                return;
            case MULTILINESTRING:
                minimize((MultiLineString) geometry, d);
                return;
            case CIRCULARSTRING:
                minimize((LineString) (CircularString) geometry, d);
                return;
            case COMPOUNDCURVE:
                minimize((CompoundCurve) geometry, d);
                return;
            case POLYGON:
                minimize((Polygon) geometry, d);
                return;
            case CURVEPOLYGON:
                minimize((CurvePolygon<Curve>) (CurvePolygon) geometry, d);
                return;
            case MULTIPOLYGON:
                minimize((MultiPolygon) geometry, d);
                return;
            case POLYHEDRALSURFACE:
                minimize((PolyhedralSurface) geometry, d);
                return;
            case TIN:
                minimize((PolyhedralSurface) (TIN) geometry, d);
                return;
            case TRIANGLE:
                minimize((Polygon) (Triangle) geometry, d);
                return;
            case GEOMETRYCOLLECTION:
                for (Geometry minimizeGeometry : ((GeometryCollection) geometry).getGeometries()) {
                    minimizeGeometry(minimizeGeometry, d);
                }
                return;
            default:
                return;
        }
    }

    private static void minimize(LineString lineString, double d) {
        List<Point> points = lineString.getPoints();
        if (points.size() > 1) {
            Point point = points.get(0);
            for (int i = 1; i < points.size(); i++) {
                Point point2 = points.get(i);
                if (point.getX() < point2.getX()) {
                    double d2 = 2.0d * d;
                    if (point2.getX() - point.getX() > (point.getX() - point2.getX()) + d2) {
                        point2.setX(point2.getX() - d2);
                    }
                } else if (point.getX() > point2.getX()) {
                    double d3 = 2.0d * d;
                    if (point.getX() - point2.getX() > (point2.getX() - point.getX()) + d3) {
                        point2.setX(point2.getX() + d3);
                    }
                }
            }
        }
    }

    private static void minimize(MultiLineString multiLineString, double d) {
        for (LineString minimize : multiLineString.getLineStrings()) {
            minimize(minimize, d);
        }
    }

    private static void minimize(Polygon polygon, double d) {
        for (LineString minimize : polygon.getRings()) {
            minimize(minimize, d);
        }
    }

    private static void minimize(MultiPolygon multiPolygon, double d) {
        for (Polygon minimize : multiPolygon.getPolygons()) {
            minimize(minimize, d);
        }
    }

    private static void minimize(CompoundCurve compoundCurve, double d) {
        for (LineString minimize : compoundCurve.getLineStrings()) {
            minimize(minimize, d);
        }
    }

    private static void minimize(CurvePolygon<Curve> curvePolygon, double d) {
        for (Curve minimizeGeometry : curvePolygon.getRings()) {
            minimizeGeometry(minimizeGeometry, d);
        }
    }

    private static void minimize(PolyhedralSurface polyhedralSurface, double d) {
        for (Polygon minimize : polyhedralSurface.getPolygons()) {
            minimize(minimize, d);
        }
    }

    public static void normalizeGeometry(Geometry geometry, double d) {
        switch (C11901.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometry.getGeometryType().ordinal()]) {
            case 1:
                normalize((Point) geometry, d);
                return;
            case 2:
                normalize((MultiPoint) geometry, d);
                return;
            case 3:
                normalize((LineString) geometry, d);
                return;
            case 4:
                normalize((MultiLineString) geometry, d);
                return;
            case 5:
                normalize((LineString) (CircularString) geometry, d);
                return;
            case 6:
                normalize((CompoundCurve) geometry, d);
                return;
            case 7:
                normalize((Polygon) geometry, d);
                return;
            case 8:
                normalize((CurvePolygon<Curve>) (CurvePolygon) geometry, d);
                return;
            case 9:
                normalize((MultiPolygon) geometry, d);
                return;
            case 10:
                normalize((PolyhedralSurface) geometry, d);
                return;
            case 11:
                normalize((PolyhedralSurface) (TIN) geometry, d);
                return;
            case 12:
                normalize((Polygon) (Triangle) geometry, d);
                return;
            case 13:
                for (Geometry normalizeGeometry : ((GeometryCollection) geometry).getGeometries()) {
                    normalizeGeometry(normalizeGeometry, d);
                }
                return;
            default:
                return;
        }
    }

    private static void normalize(Point point, double d) {
        if (point.getX() < (-d)) {
            point.setX(point.getX() + (d * 2.0d));
        } else if (point.getX() > d) {
            point.setX(point.getX() - (d * 2.0d));
        }
    }

    private static void normalize(MultiPoint multiPoint, double d) {
        for (Point normalize : multiPoint.getPoints()) {
            normalize(normalize, d);
        }
    }

    private static void normalize(LineString lineString, double d) {
        for (Point normalize : lineString.getPoints()) {
            normalize(normalize, d);
        }
    }

    private static void normalize(MultiLineString multiLineString, double d) {
        for (LineString normalize : multiLineString.getLineStrings()) {
            normalize(normalize, d);
        }
    }

    private static void normalize(Polygon polygon, double d) {
        for (LineString normalize : polygon.getRings()) {
            normalize(normalize, d);
        }
    }

    private static void normalize(MultiPolygon multiPolygon, double d) {
        for (Polygon normalize : multiPolygon.getPolygons()) {
            normalize(normalize, d);
        }
    }

    private static void normalize(CompoundCurve compoundCurve, double d) {
        for (LineString normalize : compoundCurve.getLineStrings()) {
            normalize(normalize, d);
        }
    }

    private static void normalize(CurvePolygon<Curve> curvePolygon, double d) {
        for (Curve normalizeGeometry : curvePolygon.getRings()) {
            normalizeGeometry(normalizeGeometry, d);
        }
    }

    private static void normalize(PolyhedralSurface polyhedralSurface, double d) {
        for (Polygon normalize : polyhedralSurface.getPolygons()) {
            normalize(normalize, d);
        }
    }

    public static List<Point> simplifyPoints(List<Point> list, double d) {
        return simplifyPoints(list, d, 0, list.size() - 1);
    }

    private static List<Point> simplifyPoints(List<Point> list, double d, int i, int i2) {
        Point point = list.get(i);
        Point point2 = list.get(i2);
        double d2 = 0.0d;
        int i3 = 0;
        for (int i4 = i + 1; i4 < i2; i4++) {
            double perpendicularDistance = perpendicularDistance(list.get(i4), point, point2);
            if (perpendicularDistance > d2) {
                i3 = i4;
                d2 = perpendicularDistance;
            }
        }
        if (d2 > d) {
            List<Point> simplifyPoints = simplifyPoints(list, d, i, i3);
            List<Point> simplifyPoints2 = simplifyPoints(list, d, i3, i2);
            List<Point> subList = simplifyPoints.subList(0, simplifyPoints.size() - 1);
            subList.addAll(simplifyPoints2);
            return subList;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(point);
        arrayList.add(point2);
        return arrayList;
    }

    public static double perpendicularDistance(Point point, Point point2, Point point3) {
        double x = point.getX();
        double y = point.getY();
        double x2 = point2.getX();
        double y2 = point2.getY();
        double x3 = point3.getX();
        double y3 = point3.getY();
        double d = x3 - x2;
        double d2 = y3 - y2;
        double d3 = ((x - x2) * d) + ((y - y2) * d2);
        double d4 = (d * d) + (d2 * d2);
        if (d3 > 0.0d) {
            if (d4 <= d3) {
                x2 = x3;
                y2 = y3;
            } else {
                double d5 = d3 / d4;
                x2 += d * d5;
                y2 += d5 * d2;
            }
        }
        return Math.sqrt(Math.pow(x2 - x, 2.0d) + Math.pow(y2 - y, 2.0d));
    }

    public static boolean pointInPolygon(Point point, Polygon polygon) {
        return pointInPolygon(point, polygon, 1.0E-15d);
    }

    public static boolean pointInPolygon(Point point, Polygon polygon, double d) {
        List rings = polygon.getRings();
        if (rings.isEmpty()) {
            return false;
        }
        boolean pointInPolygon = pointInPolygon(point, (LineString) rings.get(0), d);
        if (pointInPolygon) {
            for (int i = 1; i < rings.size(); i++) {
                if (pointInPolygon(point, (LineString) rings.get(i), d)) {
                    return false;
                }
            }
        }
        return pointInPolygon;
    }

    public static boolean pointInPolygon(Point point, LineString lineString) {
        return pointInPolygon(point, lineString, 1.0E-15d);
    }

    public static boolean pointInPolygon(Point point, LineString lineString, double d) {
        return pointInPolygon(point, lineString.getPoints(), d);
    }

    public static boolean pointInPolygon(Point point, List<Point> list) {
        return pointInPolygon(point, list, 1.0E-15d);
    }

    public static boolean pointInPolygon(Point point, List<Point> list, double d) {
        boolean z;
        int i;
        int i2;
        List<Point> list2 = list;
        boolean z2 = true;
        int size = list.size() - 1;
        if (closedPolygon(list)) {
            i2 = 1;
            i = 0;
            z = false;
        } else {
            i = size;
            i2 = 0;
            z = false;
        }
        while (true) {
            if (i2 >= list.size()) {
                z2 = z;
                break;
            }
            Point point2 = list2.get(i2);
            Point point3 = list2.get(i);
            if (Math.abs(point2.getX() - point.getX()) <= d && Math.abs(point2.getY() - point.getY()) <= d) {
                break;
            }
            if ((point2.getY() > point.getY()) != (point3.getY() > point.getY()) && point.getX() < (((point3.getX() - point2.getX()) * (point.getY() - point2.getY())) / (point3.getY() - point2.getY())) + point2.getX()) {
                z = !z;
            }
            i = i2;
            i2++;
        }
        return !z2 ? pointOnPolygonEdge(point, list) : z2;
    }

    public static boolean pointOnPolygonEdge(Point point, Polygon polygon) {
        return pointOnPolygonEdge(point, polygon, 1.0E-15d);
    }

    public static boolean pointOnPolygonEdge(Point point, Polygon polygon, double d) {
        if (polygon.numRings() <= 0 || !pointOnPolygonEdge(point, (LineString) polygon.getRings().get(0), d)) {
            return false;
        }
        return true;
    }

    public static boolean pointOnPolygonEdge(Point point, LineString lineString) {
        return pointOnPolygonEdge(point, lineString, 1.0E-15d);
    }

    public static boolean pointOnPolygonEdge(Point point, LineString lineString, double d) {
        return pointOnPolygonEdge(point, lineString.getPoints(), d);
    }

    public static boolean pointOnPolygonEdge(Point point, List<Point> list) {
        return pointOnPolygonEdge(point, list, 1.0E-15d);
    }

    public static boolean pointOnPolygonEdge(Point point, List<Point> list, double d) {
        return pointOnPath(point, list, d, !closedPolygon(list));
    }

    public static boolean closedPolygon(Polygon polygon) {
        if (polygon.numRings() <= 0 || !closedPolygon((LineString) polygon.getRings().get(0))) {
            return false;
        }
        return true;
    }

    public static boolean closedPolygon(LineString lineString) {
        return closedPolygon(lineString.getPoints());
    }

    public static boolean closedPolygon(List<Point> list) {
        if (list.isEmpty()) {
            return false;
        }
        Point point = list.get(0);
        Point point2 = list.get(list.size() - 1);
        if (point.getX() == point2.getX() && point.getY() == point2.getY()) {
            return true;
        }
        return false;
    }

    public static boolean pointOnLine(Point point, LineString lineString) {
        return pointOnLine(point, lineString, 1.0E-15d);
    }

    public static boolean pointOnLine(Point point, LineString lineString, double d) {
        return pointOnLine(point, lineString.getPoints(), d);
    }

    public static boolean pointOnLine(Point point, List<Point> list) {
        return pointOnLine(point, list, 1.0E-15d);
    }

    public static boolean pointOnLine(Point point, List<Point> list, double d) {
        return pointOnPath(point, list, d, false);
    }

    public static boolean pointOnPath(Point point, Point point2, Point point3) {
        return pointOnPath(point, point2, point3, 1.0E-15d);
    }

    public static boolean pointOnPath(Point point, Point point2, Point point3, double d) {
        double x = point3.getX() - point2.getX();
        double y = point3.getY() - point2.getY();
        double x2 = point.getX() - point2.getX();
        double y2 = point.getY() - point2.getY();
        double d2 = (x2 * x) + (y2 * y);
        if (d2 < 0.0d) {
            return false;
        }
        double d3 = (x2 * x2) + (y2 * y2);
        double d4 = (x * x) + (y * y);
        if (d3 > d4 || Math.abs((d2 * d2) - (d3 * d4)) > d) {
            return false;
        }
        return true;
    }

    private static boolean pointOnPath(Point point, List<Point> list, double d, boolean z) {
        int i;
        int i2;
        int size = list.size() - 1;
        if (!z) {
            i2 = 1;
            i = 0;
        } else {
            i = size;
            i2 = 0;
        }
        while (i2 < list.size()) {
            if (pointOnPath(point, list.get(i2), list.get(i), d)) {
                return true;
            }
            int i3 = i2;
            i2++;
            i = i3;
        }
        return false;
    }
}
