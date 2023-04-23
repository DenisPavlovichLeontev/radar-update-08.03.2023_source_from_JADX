package org.osgeo.proj4j.parser;

import java.text.ParsePosition;
import org.osgeo.proj4j.units.AngleFormat;

public class ParameterUtil {
    public static final AngleFormat format = new AngleFormat(AngleFormat.ddmmssPattern, true);

    public static double parseAngle(String str) {
        return format.parse(str, (ParsePosition) null).doubleValue();
    }
}
