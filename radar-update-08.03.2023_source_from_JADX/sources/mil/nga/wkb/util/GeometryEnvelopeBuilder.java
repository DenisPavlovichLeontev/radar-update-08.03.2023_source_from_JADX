package mil.nga.wkb.util;

import mil.nga.wkb.geom.CircularString;
import mil.nga.wkb.geom.CompoundCurve;
import mil.nga.wkb.geom.Curve;
import mil.nga.wkb.geom.CurvePolygon;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.GeometryEnvelope;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.MultiLineString;
import mil.nga.wkb.geom.MultiPoint;
import mil.nga.wkb.geom.MultiPolygon;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;
import mil.nga.wkb.geom.PolyhedralSurface;
import mil.nga.wkb.geom.TIN;
import mil.nga.wkb.geom.Triangle;

public class GeometryEnvelopeBuilder {
    public static GeometryEnvelope buildEnvelope(Geometry geometry) {
        GeometryEnvelope geometryEnvelope = new GeometryEnvelope();
        geometryEnvelope.setMinX(Double.MAX_VALUE);
        geometryEnvelope.setMaxX(-1.7976931348623157E308d);
        geometryEnvelope.setMinY(Double.MAX_VALUE);
        geometryEnvelope.setMaxY(-1.7976931348623157E308d);
        buildEnvelope(geometry, geometryEnvelope);
        return geometryEnvelope;
    }

    /* renamed from: mil.nga.wkb.util.GeometryEnvelopeBuilder$1 */
    static /* synthetic */ class C11871 {
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
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.wkb.util.GeometryEnvelopeBuilder.C11871.<clinit>():void");
        }
    }

    public static void buildEnvelope(Geometry geometry, GeometryEnvelope geometryEnvelope) {
        switch (C11871.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometry.getGeometryType().ordinal()]) {
            case 1:
                addPoint(geometryEnvelope, (Point) geometry);
                return;
            case 2:
                addLineString(geometryEnvelope, (LineString) geometry);
                return;
            case 3:
                addPolygon(geometryEnvelope, (Polygon) geometry);
                return;
            case 4:
                addMultiPoint(geometryEnvelope, (MultiPoint) geometry);
                return;
            case 5:
                addMultiLineString(geometryEnvelope, (MultiLineString) geometry);
                return;
            case 6:
                addMultiPolygon(geometryEnvelope, (MultiPolygon) geometry);
                return;
            case 7:
                addLineString(geometryEnvelope, (CircularString) geometry);
                return;
            case 8:
                addCompoundCurve(geometryEnvelope, (CompoundCurve) geometry);
                return;
            case 9:
                addCurvePolygon(geometryEnvelope, (CurvePolygon) geometry);
                return;
            case 10:
                addPolyhedralSurface(geometryEnvelope, (PolyhedralSurface) geometry);
                return;
            case 11:
                addPolyhedralSurface(geometryEnvelope, (TIN) geometry);
                return;
            case 12:
                addPolygon(geometryEnvelope, (Triangle) geometry);
                return;
            case 13:
                updateHasZandM(geometryEnvelope, geometry);
                for (Geometry buildEnvelope : ((GeometryCollection) geometry).getGeometries()) {
                    buildEnvelope(buildEnvelope, geometryEnvelope);
                }
                return;
            default:
                return;
        }
    }

    private static void updateHasZandM(GeometryEnvelope geometryEnvelope, Geometry geometry) {
        if (!geometryEnvelope.hasZ() && geometry.hasZ()) {
            geometryEnvelope.setHasZ(true);
        }
        if (!geometryEnvelope.hasM() && geometry.hasM()) {
            geometryEnvelope.setHasM(true);
        }
    }

    private static void addPoint(GeometryEnvelope geometryEnvelope, Point point) {
        Double m;
        Double z;
        updateHasZandM(geometryEnvelope, point);
        double x = point.getX();
        double y = point.getY();
        if (x < geometryEnvelope.getMinX()) {
            geometryEnvelope.setMinX(x);
        }
        if (x > geometryEnvelope.getMaxX()) {
            geometryEnvelope.setMaxX(x);
        }
        if (y < geometryEnvelope.getMinY()) {
            geometryEnvelope.setMinY(y);
        }
        if (y > geometryEnvelope.getMaxY()) {
            geometryEnvelope.setMaxY(y);
        }
        if (point.hasZ() && (z = point.getZ()) != null) {
            if (geometryEnvelope.getMinZ() == null || z.doubleValue() < geometryEnvelope.getMinZ().doubleValue()) {
                geometryEnvelope.setMinZ(z);
            }
            if (geometryEnvelope.getMaxZ() == null || z.doubleValue() > geometryEnvelope.getMaxZ().doubleValue()) {
                geometryEnvelope.setMaxZ(z);
            }
        }
        if (point.hasM() && (m = point.getM()) != null) {
            if (geometryEnvelope.getMinM() == null || m.doubleValue() < geometryEnvelope.getMinM().doubleValue()) {
                geometryEnvelope.setMinM(m);
            }
            if (geometryEnvelope.getMaxM() == null || m.doubleValue() > geometryEnvelope.getMaxM().doubleValue()) {
                geometryEnvelope.setMaxM(m);
            }
        }
    }

    private static void addMultiPoint(GeometryEnvelope geometryEnvelope, MultiPoint multiPoint) {
        updateHasZandM(geometryEnvelope, multiPoint);
        for (Point addPoint : multiPoint.getPoints()) {
            addPoint(geometryEnvelope, addPoint);
        }
    }

    private static void addLineString(GeometryEnvelope geometryEnvelope, LineString lineString) {
        updateHasZandM(geometryEnvelope, lineString);
        for (Point addPoint : lineString.getPoints()) {
            addPoint(geometryEnvelope, addPoint);
        }
    }

    private static void addMultiLineString(GeometryEnvelope geometryEnvelope, MultiLineString multiLineString) {
        updateHasZandM(geometryEnvelope, multiLineString);
        for (LineString addLineString : multiLineString.getLineStrings()) {
            addLineString(geometryEnvelope, addLineString);
        }
    }

    private static void addPolygon(GeometryEnvelope geometryEnvelope, Polygon polygon) {
        updateHasZandM(geometryEnvelope, polygon);
        for (LineString addLineString : polygon.getRings()) {
            addLineString(geometryEnvelope, addLineString);
        }
    }

    private static void addMultiPolygon(GeometryEnvelope geometryEnvelope, MultiPolygon multiPolygon) {
        updateHasZandM(geometryEnvelope, multiPolygon);
        for (Polygon addPolygon : multiPolygon.getPolygons()) {
            addPolygon(geometryEnvelope, addPolygon);
        }
    }

    private static void addCompoundCurve(GeometryEnvelope geometryEnvelope, CompoundCurve compoundCurve) {
        updateHasZandM(geometryEnvelope, compoundCurve);
        for (LineString addLineString : compoundCurve.getLineStrings()) {
            addLineString(geometryEnvelope, addLineString);
        }
    }

    private static void addCurvePolygon(GeometryEnvelope geometryEnvelope, CurvePolygon<Curve> curvePolygon) {
        updateHasZandM(geometryEnvelope, curvePolygon);
        for (Curve buildEnvelope : curvePolygon.getRings()) {
            buildEnvelope(buildEnvelope, geometryEnvelope);
        }
    }

    private static void addPolyhedralSurface(GeometryEnvelope geometryEnvelope, PolyhedralSurface polyhedralSurface) {
        updateHasZandM(geometryEnvelope, polyhedralSurface);
        for (Polygon addPolygon : polyhedralSurface.getPolygons()) {
            addPolygon(geometryEnvelope, addPolygon);
        }
    }
}
