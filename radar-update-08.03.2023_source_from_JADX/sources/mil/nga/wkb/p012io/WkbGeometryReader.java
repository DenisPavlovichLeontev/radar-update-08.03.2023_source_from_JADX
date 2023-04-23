package mil.nga.wkb.p012io;

import java.nio.ByteOrder;
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

/* renamed from: mil.nga.wkb.io.WkbGeometryReader */
public class WkbGeometryReader {
    public static Geometry readGeometry(ByteReader byteReader) {
        return readGeometry(byteReader, (Class) null);
    }

    public static <T extends Geometry> T readGeometry(ByteReader byteReader, Class<T> cls) {
        T t;
        ByteOrder byteOrder = byteReader.readByte() == 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        ByteOrder byteOrder2 = byteReader.getByteOrder();
        byteReader.setByteOrder(byteOrder);
        int readInt = byteReader.readInt();
        int i = readInt % 1000;
        int i2 = readInt / 1000;
        boolean z = false;
        boolean z2 = true;
        if (i2 == 1) {
            z2 = false;
            z = true;
        } else if (i2 != 2) {
            if (i2 != 3) {
                z2 = false;
            } else {
                z = true;
            }
        }
        GeometryType fromCode = GeometryType.fromCode(i);
        switch (C11851.$SwitchMap$mil$nga$wkb$geom$GeometryType[fromCode.ordinal()]) {
            case 1:
                throw new WkbException("Unexpected Geometry Type of " + fromCode.name() + " which is abstract");
            case 2:
                t = readPoint(byteReader, z, z2);
                break;
            case 3:
                t = readLineString(byteReader, z, z2);
                break;
            case 4:
                t = readPolygon(byteReader, z, z2);
                break;
            case 5:
                t = readMultiPoint(byteReader, z, z2);
                break;
            case 6:
                t = readMultiLineString(byteReader, z, z2);
                break;
            case 7:
                t = readMultiPolygon(byteReader, z, z2);
                break;
            case 8:
                t = readGeometryCollection(byteReader, z, z2);
                break;
            case 9:
                t = readCircularString(byteReader, z, z2);
                break;
            case 10:
                t = readCompoundCurve(byteReader, z, z2);
                break;
            case 11:
                t = readCurvePolygon(byteReader, z, z2);
                break;
            case 12:
                throw new WkbException("Unexpected Geometry Type of " + fromCode.name() + " which is abstract");
            case 13:
                throw new WkbException("Unexpected Geometry Type of " + fromCode.name() + " which is abstract");
            case 14:
                throw new WkbException("Unexpected Geometry Type of " + fromCode.name() + " which is abstract");
            case 15:
                throw new WkbException("Unexpected Geometry Type of " + fromCode.name() + " which is abstract");
            case 16:
                t = readPolyhedralSurface(byteReader, z, z2);
                break;
            case 17:
                t = readTIN(byteReader, z, z2);
                break;
            case 18:
                t = readTriangle(byteReader, z, z2);
                break;
            default:
                throw new WkbException("Geometry Type not supported: " + fromCode);
        }
        if (cls == null || t == null || cls.isAssignableFrom(t.getClass())) {
            byteReader.setByteOrder(byteOrder2);
            return t;
        }
        throw new WkbException("Unexpected Geometry Type. Expected: " + cls.getSimpleName() + ", Actual: " + t.getClass().getSimpleName());
    }

    /* renamed from: mil.nga.wkb.io.WkbGeometryReader$1 */
    static /* synthetic */ class C11851 {
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
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.wkb.p012io.WkbGeometryReader.C11851.<clinit>():void");
        }
    }

    public static Point readPoint(ByteReader byteReader, boolean z, boolean z2) {
        Point point = new Point(z, z2, byteReader.readDouble(), byteReader.readDouble());
        if (z) {
            point.setZ(Double.valueOf(byteReader.readDouble()));
        }
        if (z2) {
            point.setM(Double.valueOf(byteReader.readDouble()));
        }
        return point;
    }

    public static LineString readLineString(ByteReader byteReader, boolean z, boolean z2) {
        LineString lineString = new LineString(z, z2);
        int readInt = byteReader.readInt();
        for (int i = 0; i < readInt; i++) {
            lineString.addPoint(readPoint(byteReader, z, z2));
        }
        return lineString;
    }

    public static Polygon readPolygon(ByteReader byteReader, boolean z, boolean z2) {
        Polygon polygon = new Polygon(z, z2);
        int readInt = byteReader.readInt();
        for (int i = 0; i < readInt; i++) {
            polygon.addRing(readLineString(byteReader, z, z2));
        }
        return polygon;
    }

    public static MultiPoint readMultiPoint(ByteReader byteReader, boolean z, boolean z2) {
        MultiPoint multiPoint = new MultiPoint(z, z2);
        int readInt = byteReader.readInt();
        for (int i = 0; i < readInt; i++) {
            multiPoint.addPoint((Point) readGeometry(byteReader, Point.class));
        }
        return multiPoint;
    }

    public static MultiLineString readMultiLineString(ByteReader byteReader, boolean z, boolean z2) {
        MultiLineString multiLineString = new MultiLineString(z, z2);
        int readInt = byteReader.readInt();
        for (int i = 0; i < readInt; i++) {
            multiLineString.addLineString((LineString) readGeometry(byteReader, LineString.class));
        }
        return multiLineString;
    }

    public static MultiPolygon readMultiPolygon(ByteReader byteReader, boolean z, boolean z2) {
        MultiPolygon multiPolygon = new MultiPolygon(z, z2);
        int readInt = byteReader.readInt();
        for (int i = 0; i < readInt; i++) {
            multiPolygon.addPolygon((Polygon) readGeometry(byteReader, Polygon.class));
        }
        return multiPolygon;
    }

    public static GeometryCollection<Geometry> readGeometryCollection(ByteReader byteReader, boolean z, boolean z2) {
        GeometryCollection<Geometry> geometryCollection = new GeometryCollection<>(z, z2);
        int readInt = byteReader.readInt();
        for (int i = 0; i < readInt; i++) {
            geometryCollection.addGeometry(readGeometry(byteReader, Geometry.class));
        }
        return geometryCollection;
    }

    public static CircularString readCircularString(ByteReader byteReader, boolean z, boolean z2) {
        CircularString circularString = new CircularString(z, z2);
        int readInt = byteReader.readInt();
        for (int i = 0; i < readInt; i++) {
            circularString.addPoint(readPoint(byteReader, z, z2));
        }
        return circularString;
    }

    public static CompoundCurve readCompoundCurve(ByteReader byteReader, boolean z, boolean z2) {
        CompoundCurve compoundCurve = new CompoundCurve(z, z2);
        int readInt = byteReader.readInt();
        for (int i = 0; i < readInt; i++) {
            compoundCurve.addLineString((LineString) readGeometry(byteReader, LineString.class));
        }
        return compoundCurve;
    }

    public static CurvePolygon<Curve> readCurvePolygon(ByteReader byteReader, boolean z, boolean z2) {
        CurvePolygon<Curve> curvePolygon = new CurvePolygon<>(z, z2);
        int readInt = byteReader.readInt();
        for (int i = 0; i < readInt; i++) {
            curvePolygon.addRing((Curve) readGeometry(byteReader, Curve.class));
        }
        return curvePolygon;
    }

    public static PolyhedralSurface readPolyhedralSurface(ByteReader byteReader, boolean z, boolean z2) {
        PolyhedralSurface polyhedralSurface = new PolyhedralSurface(z, z2);
        int readInt = byteReader.readInt();
        for (int i = 0; i < readInt; i++) {
            polyhedralSurface.addPolygon((Polygon) readGeometry(byteReader, Polygon.class));
        }
        return polyhedralSurface;
    }

    public static TIN readTIN(ByteReader byteReader, boolean z, boolean z2) {
        TIN tin = new TIN(z, z2);
        int readInt = byteReader.readInt();
        for (int i = 0; i < readInt; i++) {
            tin.addPolygon((Polygon) readGeometry(byteReader, Polygon.class));
        }
        return tin;
    }

    public static Triangle readTriangle(ByteReader byteReader, boolean z, boolean z2) {
        Triangle triangle = new Triangle(z, z2);
        int readInt = byteReader.readInt();
        for (int i = 0; i < readInt; i++) {
            triangle.addRing(readLineString(byteReader, z, z2));
        }
        return triangle;
    }
}
