package org.osgeo.proj4j;

import org.osgeo.proj4j.proj.Projection;

public class ProjectionException extends Proj4jException {
    public static String ERR_17 = "non-convergent inverse meridinal dist";

    public ProjectionException() {
    }

    public ProjectionException(String str) {
        super(str);
    }

    public ProjectionException(Projection projection, String str) {
        this(projection.toString() + ": " + str);
    }
}
