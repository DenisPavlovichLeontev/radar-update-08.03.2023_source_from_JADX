package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class DenoyerProjection extends Projection {

    /* renamed from: C0 */
    public static final double f460C0 = 0.95d;

    /* renamed from: C1 */
    public static final double f461C1 = -0.08333333333333333d;

    /* renamed from: C3 */
    public static final double f462C3 = 0.0016666666666666666d;

    /* renamed from: D1 */
    public static final double f463D1 = 0.9d;

    /* renamed from: D5 */
    public static final double f464D5 = 0.03d;

    public boolean hasInverse() {
        return false;
    }

    public boolean parallelsAreParallel() {
        return true;
    }

    public String toString() {
        return "Denoyer Semi-elliptical";
    }

    public ProjCoordinate project(double d, double d2, ProjCoordinate projCoordinate) {
        projCoordinate.f410y = d2;
        projCoordinate.f409x = d;
        double abs = Math.abs(d);
        projCoordinate.f409x *= Math.cos(((abs * (((abs * abs) * 0.0016666666666666666d) - 53.333333333333336d)) + 0.95d) * d2 * ((0.03d * d2 * d2 * d2 * d2) + 0.9d));
        return projCoordinate;
    }
}
