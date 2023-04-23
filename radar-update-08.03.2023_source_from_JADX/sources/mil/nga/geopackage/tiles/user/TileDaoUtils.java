package mil.nga.geopackage.tiles.user;

import java.util.List;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

public class TileDaoUtils {
    public static void adjustTileMatrixLengths(TileMatrixSet tileMatrixSet, List<TileMatrix> list) {
        double maxX = tileMatrixSet.getMaxX() - tileMatrixSet.getMinX();
        double maxY = tileMatrixSet.getMaxY() - tileMatrixSet.getMinY();
        for (TileMatrix next : list) {
            int pixelYSize = (int) (maxY / (next.getPixelYSize() * ((double) next.getTileHeight())));
            long pixelXSize = (long) ((int) (maxX / (next.getPixelXSize() * ((double) next.getTileWidth()))));
            if (pixelXSize > next.getMatrixWidth()) {
                next.setMatrixWidth(pixelXSize);
            }
            long j = (long) pixelYSize;
            if (j > next.getMatrixHeight()) {
                next.setMatrixHeight(j);
            }
        }
    }

    public static Long getZoomLevel(double[] dArr, double[] dArr2, List<TileMatrix> list, double d) {
        return getZoomLevel(dArr, dArr2, list, d, true);
    }

    public static Long getZoomLevel(double[] dArr, double[] dArr2, List<TileMatrix> list, double d, double d2) {
        return getZoomLevel(dArr, dArr2, list, d, d2, true);
    }

    public static Long getClosestZoomLevel(double[] dArr, double[] dArr2, List<TileMatrix> list, double d) {
        return getZoomLevel(dArr, dArr2, list, d, false);
    }

    public static Long getClosestZoomLevel(double[] dArr, double[] dArr2, List<TileMatrix> list, double d, double d2) {
        return getZoomLevel(dArr, dArr2, list, d, d2, false);
    }

    private static Long getZoomLevel(double[] dArr, double[] dArr2, List<TileMatrix> list, double d, boolean z) {
        return getZoomLevel(dArr, dArr2, list, d, d, z);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004b, code lost:
        if (r12 < getMinLength(r8)) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001d, code lost:
        if (r10 < getMinLength(r7)) goto L_0x001f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x004e  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0072 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0079  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x007b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Long getZoomLevel(double[] r7, double[] r8, java.util.List<mil.nga.geopackage.tiles.matrix.TileMatrix> r9, double r10, double r12, boolean r14) {
        /*
            int r0 = java.util.Arrays.binarySearch(r7, r10)
            r1 = -1
            if (r0 >= 0) goto L_0x000a
            int r0 = r0 + 1
            int r0 = r0 * r1
        L_0x000a:
            int r2 = java.util.Arrays.binarySearch(r8, r12)
            if (r2 >= 0) goto L_0x0013
            int r2 = r2 + 1
            int r2 = r2 * r1
        L_0x0013:
            if (r0 != 0) goto L_0x0021
            if (r14 == 0) goto L_0x0041
            double r3 = getMinLength(r7)
            int r7 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1))
            if (r7 >= 0) goto L_0x0041
        L_0x001f:
            r0 = r1
            goto L_0x0041
        L_0x0021:
            int r3 = r7.length
            if (r0 != r3) goto L_0x0032
            if (r14 == 0) goto L_0x002f
            double r3 = getMaxLength(r7)
            int r7 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1))
            if (r7 < 0) goto L_0x002f
            goto L_0x001f
        L_0x002f:
            int r0 = r0 + -1
            goto L_0x0041
        L_0x0032:
            int r3 = r0 + -1
            r3 = r7[r3]
            double r3 = r10 - r3
            r5 = r7[r0]
            double r5 = r5 - r10
            int r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r7 >= 0) goto L_0x0041
            int r0 = r0 + -1
        L_0x0041:
            if (r2 != 0) goto L_0x004e
            if (r14 == 0) goto L_0x006f
            double r7 = getMinLength(r8)
            int r7 = (r12 > r7 ? 1 : (r12 == r7 ? 0 : -1))
            if (r7 >= 0) goto L_0x006f
            goto L_0x0070
        L_0x004e:
            int r7 = r8.length
            if (r2 != r7) goto L_0x005f
            if (r14 == 0) goto L_0x005c
            double r7 = getMaxLength(r8)
            int r7 = (r12 > r7 ? 1 : (r12 == r7 ? 0 : -1))
            if (r7 < 0) goto L_0x005c
            goto L_0x0070
        L_0x005c:
            int r1 = r2 + -1
            goto L_0x0070
        L_0x005f:
            int r7 = r2 + -1
            r10 = r8[r7]
            double r10 = r12 - r10
            r7 = r8[r2]
            double r7 = r7 - r12
            int r7 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1))
            if (r7 >= 0) goto L_0x006f
            int r1 = r2 + -1
            goto L_0x0070
        L_0x006f:
            r1 = r2
        L_0x0070:
            if (r0 >= 0) goto L_0x0077
            if (r1 < 0) goto L_0x0075
            goto L_0x0077
        L_0x0075:
            r7 = 0
            goto L_0x0097
        L_0x0077:
            if (r0 >= 0) goto L_0x007b
            r0 = r1
            goto L_0x0082
        L_0x007b:
            if (r1 >= 0) goto L_0x007e
            goto L_0x0082
        L_0x007e:
            int r0 = java.lang.Math.min(r0, r1)
        L_0x0082:
            int r7 = r9.size()
            int r7 = r7 - r0
            int r7 = r7 + -1
            java.lang.Object r7 = r9.get(r7)
            mil.nga.geopackage.tiles.matrix.TileMatrix r7 = (mil.nga.geopackage.tiles.matrix.TileMatrix) r7
            long r7 = r7.getZoomLevel()
            java.lang.Long r7 = java.lang.Long.valueOf(r7)
        L_0x0097:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.tiles.user.TileDaoUtils.getZoomLevel(double[], double[], java.util.List, double, double, boolean):java.lang.Long");
    }

    public static double getMaxLength(double[] dArr, double[] dArr2) {
        return Math.min(getMaxLength(dArr), getMaxLength(dArr2));
    }

    public static double getMinLength(double[] dArr, double[] dArr2) {
        return Math.max(getMinLength(dArr), getMinLength(dArr2));
    }

    private static double getMaxLength(double[] dArr) {
        return dArr[dArr.length - 1] / 0.51d;
    }

    private static double getMinLength(double[] dArr) {
        return dArr[0] * 0.51d;
    }
}
