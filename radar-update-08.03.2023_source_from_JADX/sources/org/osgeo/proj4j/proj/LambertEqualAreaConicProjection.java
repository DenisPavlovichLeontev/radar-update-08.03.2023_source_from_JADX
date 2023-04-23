package org.osgeo.proj4j.proj;

public class LambertEqualAreaConicProjection extends AlbersProjection {
    public String toString() {
        return "Lambert Equal Area Conic";
    }

    public LambertEqualAreaConicProjection() {
        this(false);
    }

    public LambertEqualAreaConicProjection(boolean z) {
        this.minLatitude = Math.toRadians(0.0d);
        this.maxLatitude = Math.toRadians(90.0d);
        this.projectionLatitude1 = z ? -0.7853981633974483d : 0.7853981633974483d;
        this.projectionLatitude2 = z ? -1.5707963267948966d : 1.5707963267948966d;
        initialize();
    }
}
