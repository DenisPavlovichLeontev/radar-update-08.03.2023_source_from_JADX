package org.osgeo.proj4j.units;

import java.text.ParseException;

public class DegreeUnit extends Unit {
    private static AngleFormat format = new AngleFormat(AngleFormat.ddmmssPattern, true);
    static final long serialVersionUID = -3212757578604686538L;

    public DegreeUnit() {
        super("degree", "degrees", "deg", 1.0d);
    }

    public double parse(String str) throws NumberFormatException {
        try {
            return format.parse(str).doubleValue();
        } catch (ParseException e) {
            throw new NumberFormatException(e.getMessage());
        }
    }

    public String format(double d) {
        return format.format(d) + " " + this.abbreviation;
    }

    public String format(double d, boolean z) {
        if (!z) {
            return format.format(d);
        }
        return format.format(d) + " " + this.abbreviation;
    }

    public String format(double d, double d2, boolean z) {
        if (z) {
            return format.format(d) + "/" + format.format(d2) + " " + this.abbreviation;
        }
        return format.format(d) + "/" + format.format(d2);
    }
}
