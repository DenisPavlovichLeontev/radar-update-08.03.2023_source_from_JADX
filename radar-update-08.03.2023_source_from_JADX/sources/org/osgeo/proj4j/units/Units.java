package org.osgeo.proj4j.units;

import mil.nga.geopackage.features.columns.GeometryColumns;

public class Units {
    public static final Unit ARC_MINUTES = new Unit("arc minute", "arc minutes", "min", 0.016666666666666666d);
    public static final Unit ARC_SECONDS = new Unit("arc second", "arc seconds", "sec", 2.777777777777778E-4d);
    public static final Unit CENTIMETRES;
    public static final Unit CHAINS = new Unit("chain", "chains", "ch", 20.1168d);
    public static final Unit DECIMETRES;
    public static final Unit DEGREES;
    public static final Unit FATHOMS = new Unit("fathom", "fathoms", "fath", 1.8288d);
    public static final Unit FEET;
    public static final Unit INCHES;
    public static final Unit KILOMETRES;
    public static final Unit LINKS = new Unit("link", "links", "link", 0.201168d);
    public static final Unit METRES;
    public static final Unit MILES;
    public static final Unit MILLIMETRES;
    public static final Unit NAUTICAL_MILES;
    public static final Unit POINTS = new Unit("point", "points", "point", 3.5145980351459805E-4d);
    public static final Unit RADIANS = new Unit("radian", "radians", "rad", Math.toDegrees(1.0d));
    public static final Unit US_CHAINS = new Unit("U.S. chain", "U.S. chains", "us-ch", 20.11684023368047d);
    public static final Unit US_FEET;
    public static final Unit US_INCHES;
    public static final Unit US_MILES;
    public static final Unit US_YARDS;
    public static final Unit YARDS;
    public static Unit[] units;

    static {
        DegreeUnit degreeUnit = new DegreeUnit();
        DEGREES = degreeUnit;
        Unit unit = new Unit("kilometre", "kilometres", "km", 1000.0d);
        KILOMETRES = unit;
        Unit unit2 = new Unit("metre", "metres", GeometryColumns.COLUMN_M, 1.0d);
        METRES = unit2;
        Unit unit3 = new Unit("decimetre", "decimetres", "dm", 0.1d);
        DECIMETRES = unit3;
        Unit unit4 = new Unit("centimetre", "centimetres", "cm", 0.01d);
        CENTIMETRES = unit4;
        Unit unit5 = new Unit("millimetre", "millimetres", "mm", 0.001d);
        MILLIMETRES = unit5;
        Unit unit6 = new Unit("nautical mile", "nautical miles", "kmi", 1852.0d);
        NAUTICAL_MILES = unit6;
        Unit unit7 = new Unit("mile", "miles", "mi", 1609.344d);
        MILES = unit7;
        Unit unit8 = new Unit("yard", "yards", "yd", 0.9144d);
        YARDS = unit8;
        Unit unit9 = new Unit("foot", "feet", "ft", 0.3048d);
        FEET = unit9;
        Unit unit10 = new Unit("inch", "inches", "in", 0.0254d);
        INCHES = unit10;
        Unit unit11 = new Unit("U.S. mile", "U.S. miles", "us-mi", 1609.347218694437d);
        US_MILES = unit11;
        Unit unit12 = new Unit("U.S. yard", "U.S. yards", "us-yd", 0.914401828803658d);
        US_YARDS = unit12;
        Unit unit13 = new Unit("U.S. foot", "U.S. feet", "us-ft", 0.304800609601219d);
        US_FEET = unit13;
        Unit unit14 = new Unit("U.S. inch", "U.S. inches", "us-in", 0.025400050800101603d);
        US_INCHES = unit14;
        units = new Unit[]{degreeUnit, unit, unit2, unit3, unit4, unit5, unit7, unit8, unit9, unit10, unit11, unit12, unit13, unit14, unit6};
    }

    public static Unit findUnits(String str) {
        int i = 0;
        while (true) {
            Unit[] unitArr = units;
            if (i >= unitArr.length) {
                return METRES;
            }
            if (!str.equals(unitArr[i].name) && !str.equals(units[i].plural) && !str.equals(units[i].abbreviation)) {
                i++;
            }
        }
        return units[i];
    }

    public static double convert(double d, Unit unit, Unit unit2) {
        return unit == unit2 ? d : unit2.fromBase(unit.toBase(d));
    }
}
