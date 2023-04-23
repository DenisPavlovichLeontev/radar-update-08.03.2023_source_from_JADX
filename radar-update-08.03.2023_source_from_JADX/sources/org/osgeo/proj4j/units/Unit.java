package org.osgeo.proj4j.units;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;

public class Unit implements Serializable {
    public static final int ANGLE_UNIT = 0;
    public static final int AREA_UNIT = 2;
    public static final int LENGTH_UNIT = 1;
    public static final int VOLUME_UNIT = 3;
    public static final NumberFormat format;
    static final long serialVersionUID = -6704954923429734628L;
    public String abbreviation;
    public String name;
    public String plural;
    public double value;

    static {
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        format = numberInstance;
        numberInstance.setMaximumFractionDigits(2);
        numberInstance.setGroupingUsed(false);
    }

    public Unit(String str, String str2, String str3, double d) {
        this.name = str;
        this.plural = str2;
        this.abbreviation = str3;
        this.value = d;
    }

    public double toBase(double d) {
        return d * this.value;
    }

    public double fromBase(double d) {
        return d / this.value;
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
            StringBuilder sb = new StringBuilder();
            NumberFormat numberFormat = format;
            sb.append(numberFormat.format(d));
            sb.append("/");
            sb.append(numberFormat.format(d2));
            sb.append(" ");
            sb.append(this.abbreviation);
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        NumberFormat numberFormat2 = format;
        sb2.append(numberFormat2.format(d));
        sb2.append("/");
        sb2.append(numberFormat2.format(d2));
        return sb2.toString();
    }

    public String format(double d, double d2) {
        return format(d, d2, true);
    }

    public String toString() {
        return this.plural;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Unit) || ((Unit) obj).value != this.value) {
            return false;
        }
        return true;
    }
}
