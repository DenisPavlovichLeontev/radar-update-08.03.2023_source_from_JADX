package mil.nga.wkb.p012io;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Iterator;
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
import mil.nga.wkb.util.WkbException;

/* renamed from: mil.nga.wkb.io.WkbGeometryWriter */
public class WkbGeometryWriter {
    public static void writeGeometry(ByteWriter byteWriter, Geometry geometry) throws IOException {
        byteWriter.writeByte(byteWriter.getByteOrder() == ByteOrder.BIG_ENDIAN ? (byte) 0 : 1);
        byteWriter.writeInt(geometry.getWkbCode());
        GeometryType geometryType = geometry.getGeometryType();
        switch (C11861.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometryType.ordinal()]) {
            case 1:
                throw new WkbException("Unexpected Geometry Type of " + geometryType.name() + " which is abstract");
            case 2:
                writePoint(byteWriter, (Point) geometry);
                return;
            case 3:
                writeLineString(byteWriter, (LineString) geometry);
                return;
            case 4:
                writePolygon(byteWriter, (Polygon) geometry);
                return;
            case 5:
                writeMultiPoint(byteWriter, (MultiPoint) geometry);
                return;
            case 6:
                writeMultiLineString(byteWriter, (MultiLineString) geometry);
                return;
            case 7:
                writeMultiPolygon(byteWriter, (MultiPolygon) geometry);
                return;
            case 8:
                writeGeometryCollection(byteWriter, (GeometryCollection) geometry);
                return;
            case 9:
                writeCircularString(byteWriter, (CircularString) geometry);
                return;
            case 10:
                writeCompoundCurve(byteWriter, (CompoundCurve) geometry);
                return;
            case 11:
                writeCurvePolygon(byteWriter, (CurvePolygon) geometry);
                return;
            case 12:
                throw new WkbException("Unexpected Geometry Type of " + geometryType.name() + " which is abstract");
            case 13:
                throw new WkbException("Unexpected Geometry Type of " + geometryType.name() + " which is abstract");
            case 14:
                throw new WkbException("Unexpected Geometry Type of " + geometryType.name() + " which is abstract");
            case 15:
                throw new WkbException("Unexpected Geometry Type of " + geometryType.name() + " which is abstract");
            case 16:
                writePolyhedralSurface(byteWriter, (PolyhedralSurface) geometry);
                return;
            case 17:
                writeTIN(byteWriter, (TIN) geometry);
                return;
            case 18:
                writeTriangle(byteWriter, (Triangle) geometry);
                return;
            default:
                throw new WkbException("Geometry Type not supported: " + geometryType);
        }
    }

    /* renamed from: mil.nga.wkb.io.WkbGeometryWriter$1 */
    static /* synthetic */ class C11861 {
        static final /* synthetic */ int[] $SwitchMap$mil$nga$wkb$geom$GeometryType;

        /* JADX WARNING: Can't wrap try/catch for region: R(36:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|(3:35|36|38)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(38:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|38) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0084 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0090 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x009c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x00a8 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x00b4 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00c0 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x00cc */
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
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.GEOMETRY     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POINT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.LINESTRING     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0033 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POLYGON     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x003e }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTIPOINT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0049 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTILINESTRING     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0054 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTIPOLYGON     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0060 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.GEOMETRYCOLLECTION     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x006c }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.CIRCULARSTRING     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0078 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.COMPOUNDCURVE     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0084 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.CURVEPOLYGON     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0090 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTICURVE     // Catch:{ NoSuchFieldError -> 0x0090 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0090 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0090 }
            L_0x0090:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x009c }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTISURFACE     // Catch:{ NoSuchFieldError -> 0x009c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009c }
                r2 = 13
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x009c }
            L_0x009c:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x00a8 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.CURVE     // Catch:{ NoSuchFieldError -> 0x00a8 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00a8 }
                r2 = 14
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00a8 }
            L_0x00a8:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x00b4 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.SURFACE     // Catch:{ NoSuchFieldError -> 0x00b4 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00b4 }
                r2 = 15
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00b4 }
            L_0x00b4:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x00c0 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POLYHEDRALSURFACE     // Catch:{ NoSuchFieldError -> 0x00c0 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00c0 }
                r2 = 16
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00c0 }
            L_0x00c0:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x00cc }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.TIN     // Catch:{ NoSuchFieldError -> 0x00cc }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00cc }
                r2 = 17
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00cc }
            L_0x00cc:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x00d8 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.TRIANGLE     // Catch:{ NoSuchFieldError -> 0x00d8 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00d8 }
                r2 = 18
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00d8 }
            L_0x00d8:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.wkb.p012io.WkbGeometryWriter.C11861.<clinit>():void");
        }
    }

    public static void writePoint(ByteWriter byteWriter, Point point) throws IOException {
        byteWriter.writeDouble(point.getX());
        byteWriter.writeDouble(point.getY());
        if (point.hasZ()) {
            byteWriter.writeDouble(point.getZ().doubleValue());
        }
        if (point.hasM()) {
            byteWriter.writeDouble(point.getM().doubleValue());
        }
    }

    public static void writeLineString(ByteWriter byteWriter, LineString lineString) throws IOException {
        byteWriter.writeInt(lineString.numPoints());
        for (Point writePoint : lineString.getPoints()) {
            writePoint(byteWriter, writePoint);
        }
    }

    public static void writePolygon(ByteWriter byteWriter, Polygon polygon) throws IOException {
        byteWriter.writeInt(polygon.numRings());
        for (LineString writeLineString : polygon.getRings()) {
            writeLineString(byteWriter, writeLineString);
        }
    }

    public static void writeMultiPoint(ByteWriter byteWriter, MultiPoint multiPoint) throws IOException {
        byteWriter.writeInt(multiPoint.numPoints());
        for (Point writeGeometry : multiPoint.getPoints()) {
            writeGeometry(byteWriter, writeGeometry);
        }
    }

    public static void writeMultiLineString(ByteWriter byteWriter, MultiLineString multiLineString) throws IOException {
        byteWriter.writeInt(multiLineString.numLineStrings());
        for (LineString writeGeometry : multiLineString.getLineStrings()) {
            writeGeometry(byteWriter, writeGeometry);
        }
    }

    public static void writeMultiPolygon(ByteWriter byteWriter, MultiPolygon multiPolygon) throws IOException {
        byteWriter.writeInt(multiPolygon.numPolygons());
        for (Polygon writeGeometry : multiPolygon.getPolygons()) {
            writeGeometry(byteWriter, writeGeometry);
        }
    }

    public static void writeGeometryCollection(ByteWriter byteWriter, GeometryCollection<?> geometryCollection) throws IOException {
        byteWriter.writeInt(geometryCollection.numGeometries());
        Iterator<?> it = geometryCollection.getGeometries().iterator();
        while (it.hasNext()) {
            writeGeometry(byteWriter, (Geometry) it.next());
        }
    }

    public static void writeCircularString(ByteWriter byteWriter, CircularString circularString) throws IOException {
        byteWriter.writeInt(circularString.numPoints());
        for (Point writePoint : circularString.getPoints()) {
            writePoint(byteWriter, writePoint);
        }
    }

    public static void writeCompoundCurve(ByteWriter byteWriter, CompoundCurve compoundCurve) throws IOException {
        byteWriter.writeInt(compoundCurve.numLineStrings());
        for (LineString writeGeometry : compoundCurve.getLineStrings()) {
            writeGeometry(byteWriter, writeGeometry);
        }
    }

    public static void writeCurvePolygon(ByteWriter byteWriter, CurvePolygon<?> curvePolygon) throws IOException {
        byteWriter.writeInt(curvePolygon.numRings());
        Iterator<?> it = curvePolygon.getRings().iterator();
        while (it.hasNext()) {
            writeGeometry(byteWriter, (Curve) it.next());
        }
    }

    public static void writePolyhedralSurface(ByteWriter byteWriter, PolyhedralSurface polyhedralSurface) throws IOException {
        byteWriter.writeInt(polyhedralSurface.numPolygons());
        for (Polygon writeGeometry : polyhedralSurface.getPolygons()) {
            writeGeometry(byteWriter, writeGeometry);
        }
    }

    public static void writeTIN(ByteWriter byteWriter, TIN tin) throws IOException {
        byteWriter.writeInt(tin.numPolygons());
        for (Polygon writeGeometry : tin.getPolygons()) {
            writeGeometry(byteWriter, writeGeometry);
        }
    }

    public static void writeTriangle(ByteWriter byteWriter, Triangle triangle) throws IOException {
        byteWriter.writeInt(triangle.numRings());
        for (LineString writeLineString : triangle.getRings()) {
            writeLineString(byteWriter, writeLineString);
        }
    }
}
