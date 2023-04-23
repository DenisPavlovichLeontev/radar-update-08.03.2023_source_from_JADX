package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class Eckert1Projection extends Projection {

    /* renamed from: FC */
    private static final double f465FC = 0.9213177319235613d;

    /* renamed from: RP */
    private static final double f466RP = 0.3183098861837907d;

    public boolean hasInverse() {
        return true;
    }

    public String toString() {
        return "Eckert I";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f409x = d * f465FC * (1.0d - (Math.abs(d2) * f466RP));
        projCoordinate.f410y = d2 * f465FC;
        return projCoordinate;
    }

    public ProjCoordinate projectInverse(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = d2 / f465FC;
        projCoordinate.f409x = d / ((1.0d - (Math.abs(projCoordinate.f410y) * f466RP)) * f465FC);
        return projCoordinate;
    }
}
