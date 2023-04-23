package org.osgeo.proj4j;

import org.osgeo.proj4j.datum.Datum;
import org.osgeo.proj4j.proj.LongLatProjection;
import org.osgeo.proj4j.proj.Projection;
import org.osgeo.proj4j.units.Units;

public class CoordinateReferenceSystem {
    public static final CoordinateReferenceSystem CS_GEO = new CoordinateReferenceSystem("CS_GEO", (String[]) null, (Datum) null, (Projection) null);
    private Datum datum;
    private String name;
    private String[] params;
    private Projection proj;

    public CoordinateReferenceSystem(String str, String[] strArr, Datum datum2, Projection projection) {
        this.name = str;
        this.params = strArr;
        this.datum = datum2;
        this.proj = projection;
        if (str == null) {
            String name2 = projection != null ? projection.getName() : "null-proj";
            this.name = name2 + "-CS";
        }
    }

    public String getName() {
        return this.name;
    }

    public String[] getParameters() {
        return this.params;
    }

    public Datum getDatum() {
        return this.datum;
    }

    public Projection getProjection() {
        return this.proj;
    }

    public String getParameterString() {
        if (this.params == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (true) {
            String[] strArr = this.params;
            if (i >= strArr.length) {
                return stringBuffer.toString();
            }
            stringBuffer.append(strArr[i]);
            stringBuffer.append(" ");
            i++;
        }
    }

    public CoordinateReferenceSystem createGeographic() {
        Datum datum2 = getDatum();
        LongLatProjection longLatProjection = new LongLatProjection();
        longLatProjection.setEllipsoid(getProjection().getEllipsoid());
        longLatProjection.setUnits(Units.DEGREES);
        longLatProjection.initialize();
        return new CoordinateReferenceSystem("GEO-" + datum2.getCode(), (String[]) null, datum2, longLatProjection);
    }

    public String toString() {
        return this.name;
    }
}
