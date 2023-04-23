package mil.nga.wkb.geom;

import java.util.Locale;

public enum GeometryType {
    GEOMETRY(0),
    POINT(1),
    LINESTRING(2),
    POLYGON(3),
    MULTIPOINT(4),
    MULTILINESTRING(5),
    MULTIPOLYGON(6),
    GEOMETRYCOLLECTION(7),
    CIRCULARSTRING(8),
    COMPOUNDCURVE(9),
    CURVEPOLYGON(10),
    MULTICURVE(11),
    MULTISURFACE(12),
    CURVE(13),
    SURFACE(14),
    POLYHEDRALSURFACE(15),
    TIN(16),
    TRIANGLE(17);
    
    private final int code;

    private GeometryType(int i) {
        this.code = i;
    }

    public String getName() {
        return name();
    }

    public int getCode() {
        return this.code;
    }

    public static GeometryType fromCode(int i) {
        switch (i) {
            case 0:
                return GEOMETRY;
            case 1:
                return POINT;
            case 2:
                return LINESTRING;
            case 3:
                return POLYGON;
            case 4:
                return MULTIPOINT;
            case 5:
                return MULTILINESTRING;
            case 6:
                return MULTIPOLYGON;
            case 7:
                return GEOMETRYCOLLECTION;
            case 8:
                return CIRCULARSTRING;
            case 9:
                return COMPOUNDCURVE;
            case 10:
                return CURVEPOLYGON;
            case 11:
                return MULTICURVE;
            case 12:
                return MULTISURFACE;
            case 13:
                return CURVE;
            case 14:
                return SURFACE;
            case 15:
                return POLYHEDRALSURFACE;
            case 16:
                return TIN;
            case 17:
                return TRIANGLE;
            default:
                return null;
        }
    }

    public static GeometryType fromName(String str) {
        return valueOf(str.toUpperCase(Locale.US));
    }
}
