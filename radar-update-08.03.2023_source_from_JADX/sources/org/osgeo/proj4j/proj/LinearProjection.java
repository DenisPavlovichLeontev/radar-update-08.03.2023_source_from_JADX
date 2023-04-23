package org.osgeo.proj4j.proj;

import org.osgeo.proj4j.ProjCoordinate;

public class LinearProjection extends Projection {
    public boolean hasInverse() {
        return true;
    }

    public boolean isRectilinear() {
        return true;
    }

    public String toString() {
        return "Linear";
    }

    public ProjCoordinate project(ProjCoordinate projCoordinate, ProjCoordinate projCoordinate2) {
        projCoordinate2.f409x = projCoordinate.f409x;
        projCoordinate2.f410y = projCoordinate.f410y;
        return projCoordinate2;
    }

    public void transform(double[] dArr, int i, double[] dArr2, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = i2 + 1;
            int i6 = i + 1;
            dArr2[i2] = dArr[i];
            i2 = i5 + 1;
            i = i6 + 1;
            dArr2[i5] = dArr[i6];
        }
    }

    public ProjCoordinate inverseProject(ProjCoordinate projCoordinate, ProjCoordinate projCoordinate2) {
        projCoordinate2.f409x = projCoordinate.f409x;
        projCoordinate2.f410y = projCoordinate.f410y;
        return projCoordinate2;
    }

    public void inverseTransform(double[] dArr, int i, double[] dArr2, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = i2 + 1;
            int i6 = i + 1;
            dArr2[i2] = dArr[i];
            i2 = i5 + 1;
            i = i6 + 1;
            dArr2[i5] = dArr[i6];
        }
    }
}
