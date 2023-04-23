package org.osmdroid.util;

import java.util.ArrayList;

public class PointReducer {
    public static ArrayList<GeoPoint> reduceWithTolerance(ArrayList<GeoPoint> arrayList, double d) {
        int i;
        int i2;
        int size = arrayList.size();
        if (d <= 0.0d || size < 3) {
            return arrayList;
        }
        boolean[] zArr = new boolean[size];
        int i3 = 1;
        while (true) {
            i = size - 1;
            if (i3 >= i) {
                break;
            }
            zArr[i3] = false;
            i3++;
        }
        zArr[i] = true;
        zArr[0] = true;
        douglasPeuckerReduction(arrayList, zArr, d, 0, i);
        ArrayList<GeoPoint> arrayList2 = new ArrayList<>(size);
        for (i2 = 0; i2 < size; i2++) {
            if (zArr[i2]) {
                arrayList2.add(arrayList.get(i2));
            }
        }
        return arrayList2;
    }

    private static void douglasPeuckerReduction(ArrayList<GeoPoint> arrayList, boolean[] zArr, double d, int i, int i2) {
        ArrayList<GeoPoint> arrayList2 = arrayList;
        int i3 = i;
        int i4 = i2;
        int i5 = i3 + 1;
        if (i4 > i5) {
            double d2 = 0.0d;
            GeoPoint geoPoint = arrayList.get(i3);
            GeoPoint geoPoint2 = arrayList.get(i4);
            int i6 = 0;
            while (i5 < i4) {
                double orthogonalDistance = orthogonalDistance(arrayList.get(i5), geoPoint, geoPoint2);
                if (orthogonalDistance > d2) {
                    i6 = i5;
                    d2 = orthogonalDistance;
                }
                i5++;
            }
            if (d2 > d) {
                zArr[i6] = true;
                ArrayList<GeoPoint> arrayList3 = arrayList;
                boolean[] zArr2 = zArr;
                double d3 = d;
                douglasPeuckerReduction(arrayList3, zArr2, d3, i, i6);
                douglasPeuckerReduction(arrayList3, zArr2, d3, i6, i2);
            }
        }
    }

    public static double orthogonalDistance(GeoPoint geoPoint, GeoPoint geoPoint2, GeoPoint geoPoint3) {
        return (Math.abs(((((((geoPoint2.getLatitude() * geoPoint3.getLongitude()) + (geoPoint3.getLatitude() * geoPoint.getLongitude())) + (geoPoint.getLatitude() * geoPoint2.getLongitude())) - (geoPoint3.getLatitude() * geoPoint2.getLongitude())) - (geoPoint.getLatitude() * geoPoint3.getLongitude())) - (geoPoint2.getLatitude() * geoPoint.getLongitude())) / 2.0d) / Math.hypot(geoPoint2.getLatitude() - geoPoint3.getLatitude(), geoPoint2.getLongitude() - geoPoint3.getLongitude())) * 2.0d;
    }
}
