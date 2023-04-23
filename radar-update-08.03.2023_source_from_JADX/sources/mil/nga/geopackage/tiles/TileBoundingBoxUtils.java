package mil.nga.geopackage.tiles;

import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.projection.ProjectionConstants;
import mil.nga.geopackage.projection.ProjectionFactory;
import mil.nga.geopackage.projection.ProjectionTransform;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.wkb.geom.Point;

public class TileBoundingBoxUtils {
    private static Projection webMercator = ProjectionFactory.getProjection(3857);

    public static double tileHeightDegrees(int i) {
        return 180.0d / ((double) i);
    }

    public static double tileWidthDegrees(int i) {
        return 360.0d / ((double) i);
    }

    public static BoundingBox overlap(BoundingBox boundingBox, BoundingBox boundingBox2) {
        return overlap(boundingBox, boundingBox2, false);
    }

    public static BoundingBox overlap(BoundingBox boundingBox, BoundingBox boundingBox2, boolean z) {
        double max = Math.max(boundingBox.getMinLongitude(), boundingBox2.getMinLongitude());
        double min = Math.min(boundingBox.getMaxLongitude(), boundingBox2.getMaxLongitude());
        double max2 = Math.max(boundingBox.getMinLatitude(), boundingBox2.getMinLatitude());
        double min2 = Math.min(boundingBox.getMaxLatitude(), boundingBox2.getMaxLatitude());
        int i = (max > min ? 1 : (max == min ? 0 : -1));
        if ((i >= 0 || max2 >= min2) && (!z || i > 0 || max2 > min2)) {
            return null;
        }
        return new BoundingBox(max, max2, min, min2);
    }

    public static BoundingBox overlap(BoundingBox boundingBox, BoundingBox boundingBox2, double d) {
        return overlap(boundingBox, boundingBox2, d, false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static mil.nga.geopackage.BoundingBox overlap(mil.nga.geopackage.BoundingBox r6, mil.nga.geopackage.BoundingBox r7, double r8, boolean r10) {
        /*
            r0 = 0
            int r2 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r2 <= 0) goto L_0x0025
            double r2 = r6.getMinLongitude()
            double r4 = r7.getMaxLongitude()
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 <= 0) goto L_0x0016
            r2 = 4611686018427387904(0x4000000000000000, double:2.0)
        L_0x0014:
            double r8 = r8 * r2
            goto L_0x0026
        L_0x0016:
            double r2 = r6.getMaxLongitude()
            double r4 = r7.getMinLongitude()
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 >= 0) goto L_0x0025
            r2 = -4611686018427387904(0xc000000000000000, double:-2.0)
            goto L_0x0014
        L_0x0025:
            r8 = r0
        L_0x0026:
            int r0 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r0 == 0) goto L_0x0040
            mil.nga.geopackage.BoundingBox r0 = new mil.nga.geopackage.BoundingBox
            r0.<init>((mil.nga.geopackage.BoundingBox) r7)
            double r1 = r0.getMinLongitude()
            double r1 = r1 + r8
            r0.setMinLongitude(r1)
            double r1 = r0.getMaxLongitude()
            double r1 = r1 + r8
            r0.setMaxLongitude(r1)
            r7 = r0
        L_0x0040:
            mil.nga.geopackage.BoundingBox r6 = overlap((mil.nga.geopackage.BoundingBox) r6, (mil.nga.geopackage.BoundingBox) r7, (boolean) r10)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.tiles.TileBoundingBoxUtils.overlap(mil.nga.geopackage.BoundingBox, mil.nga.geopackage.BoundingBox, double, boolean):mil.nga.geopackage.BoundingBox");
    }

    public static boolean isPointInBoundingBox(Point point, BoundingBox boundingBox) {
        if (overlap(boundingBox, new BoundingBox(point.getX(), point.getY(), point.getX(), point.getY()), true) != null) {
            return true;
        }
        return false;
    }

    public static boolean isPointInBoundingBox(Point point, BoundingBox boundingBox, double d) {
        if (overlap(boundingBox, new BoundingBox(point.getX(), point.getY(), point.getX(), point.getY()), d, true) != null) {
            return true;
        }
        return false;
    }

    public static BoundingBox union(BoundingBox boundingBox, BoundingBox boundingBox2) {
        double min = Math.min(boundingBox.getMinLongitude(), boundingBox2.getMinLongitude());
        double max = Math.max(boundingBox.getMaxLongitude(), boundingBox2.getMaxLongitude());
        double min2 = Math.min(boundingBox.getMinLatitude(), boundingBox2.getMinLatitude());
        double max2 = Math.max(boundingBox.getMaxLatitude(), boundingBox2.getMaxLatitude());
        if (min >= max || min2 >= max2) {
            return null;
        }
        return new BoundingBox(min, min2, max, max2);
    }

    public static float getXPixel(long j, BoundingBox boundingBox, double d) {
        return (float) (((d - boundingBox.getMinLongitude()) / (boundingBox.getMaxLongitude() - boundingBox.getMinLongitude())) * ((double) j));
    }

    public static double getLongitudeFromPixel(long j, BoundingBox boundingBox, float f) {
        return (((double) (f / ((float) j))) * (boundingBox.getMaxLongitude() - boundingBox.getMinLongitude())) + boundingBox.getMinLongitude();
    }

    public static float getYPixel(long j, BoundingBox boundingBox, double d) {
        return (float) (((boundingBox.getMaxLatitude() - d) / (boundingBox.getMaxLatitude() - boundingBox.getMinLatitude())) * ((double) j));
    }

    public static double getLatitudeFromPixel(long j, BoundingBox boundingBox, float f) {
        double d = (double) (f / ((float) j));
        return boundingBox.getMaxLatitude() - (d * (boundingBox.getMaxLatitude() - boundingBox.getMinLatitude()));
    }

    public static BoundingBox getBoundingBox(int i, int i2, int i3) {
        int tilesPerSide = tilesPerSide(i3);
        double tileWidthDegrees = tileWidthDegrees(tilesPerSide);
        double tileHeightDegrees = tileHeightDegrees(tilesPerSide);
        double d = (((double) i) * tileWidthDegrees) - 0.02490234375d;
        double d2 = 90.0d - (((double) i2) * tileHeightDegrees);
        return new BoundingBox(d, d2 - tileHeightDegrees, d + tileWidthDegrees, d2);
    }

    public static BoundingBox getWebMercatorBoundingBox(long j, long j2, int i) {
        long j3 = j;
        long j4 = j2;
        double tileSizeWithZoom = tileSizeWithZoom(i);
        return new BoundingBox((ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH * -1.0d) + (((double) j3) * tileSizeWithZoom), ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH - (((double) (j4 + 1)) * tileSizeWithZoom), (ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH * -1.0d) + (((double) (j3 + 1)) * tileSizeWithZoom), ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH - (((double) j4) * tileSizeWithZoom));
    }

    public static BoundingBox getWebMercatorBoundingBox(TileGrid tileGrid, int i) {
        double tileSizeWithZoom = tileSizeWithZoom(i);
        return new BoundingBox((ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH * -1.0d) + (((double) tileGrid.getMinX()) * tileSizeWithZoom), ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH - (((double) (tileGrid.getMaxY() + 1)) * tileSizeWithZoom), (ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH * -1.0d) + (((double) (tileGrid.getMaxX() + 1)) * tileSizeWithZoom), ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH - (((double) tileGrid.getMinY()) * tileSizeWithZoom));
    }

    public static BoundingBox getProjectedBoundingBox(Long l, int i, int i2, int i3) {
        return getProjectedBoundingBox(ProjectionConstants.AUTHORITY_EPSG, l, i, i2, i3);
    }

    public static BoundingBox getProjectedBoundingBox(String str, Long l, int i, int i2, int i3) {
        BoundingBox webMercatorBoundingBox = getWebMercatorBoundingBox((long) i, (long) i2, i3);
        return l != null ? webMercator.getTransformation(str, l.longValue()).transform(webMercatorBoundingBox) : webMercatorBoundingBox;
    }

    public static BoundingBox getProjectedBoundingBox(Projection projection, long j, long j2, int i) {
        BoundingBox webMercatorBoundingBox = getWebMercatorBoundingBox(j, j2, i);
        return projection != null ? webMercator.getTransformation(projection).transform(webMercatorBoundingBox) : webMercatorBoundingBox;
    }

    public static BoundingBox getProjectedBoundingBox(Long l, TileGrid tileGrid, int i) {
        return getProjectedBoundingBox(ProjectionConstants.AUTHORITY_EPSG, l, tileGrid, i);
    }

    public static BoundingBox getProjectedBoundingBox(String str, Long l, TileGrid tileGrid, int i) {
        BoundingBox webMercatorBoundingBox = getWebMercatorBoundingBox(tileGrid, i);
        return l != null ? webMercator.getTransformation(str, l.longValue()).transform(webMercatorBoundingBox) : webMercatorBoundingBox;
    }

    public static BoundingBox getProjectedBoundingBox(Projection projection, TileGrid tileGrid, int i) {
        BoundingBox webMercatorBoundingBox = getWebMercatorBoundingBox(tileGrid, i);
        return projection != null ? webMercator.getTransformation(projection).transform(webMercatorBoundingBox) : webMercatorBoundingBox;
    }

    public static TileGrid getTileGridFromWGS84(Point point, int i) {
        return getTileGrid(point, i, ProjectionFactory.getProjection(4326));
    }

    public static TileGrid getTileGrid(Point point, int i, Projection projection) {
        Point transform = projection.getTransformation(3857).transform(point);
        return getTileGrid(new BoundingBox(transform.getX(), transform.getY(), transform.getX(), transform.getY()), i);
    }

    public static TileGrid getTileGrid(BoundingBox boundingBox, int i) {
        int tilesPerSide = tilesPerSide(i);
        double tileSize = tileSize(tilesPerSide);
        int i2 = tilesPerSide - 1;
        return new TileGrid((long) ((int) ((boundingBox.getMinLongitude() + ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH) / tileSize)), (long) ((int) (((boundingBox.getMaxLatitude() - ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH) * -1.0d) / tileSize)), (long) Math.min((int) (((boundingBox.getMaxLongitude() + ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH) / tileSize) - ProjectionConstants.WEB_MERCATOR_PRECISION), i2), (long) Math.min((int) ((((boundingBox.getMinLatitude() - ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH) * -1.0d) / tileSize) - ProjectionConstants.WEB_MERCATOR_PRECISION), i2));
    }

    public static BoundingBox toWebMercator(BoundingBox boundingBox) {
        double max = Math.max(boundingBox.getMinLatitude(), -85.05112877980659d);
        double min = Math.min(boundingBox.getMaxLatitude(), 85.0511287798066d);
        Point point = new Point(false, false, boundingBox.getMinLongitude(), max);
        Point point2 = new Point(false, false, boundingBox.getMaxLongitude(), min);
        ProjectionTransform transformation = ProjectionFactory.getProjection(4326).getTransformation(3857);
        Point transform = transformation.transform(point);
        Point transform2 = transformation.transform(point2);
        return new BoundingBox(transform.getX(), transform.getY(), transform2.getX(), transform2.getY());
    }

    public static double tileSize(int i) {
        return (ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH * 2.0d) / ((double) i);
    }

    public static double zoomLevelOfTileSize(double d) {
        return Math.log((ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH * 2.0d) / d) / Math.log(2.0d);
    }

    public static int tilesPerSide(int i) {
        return (int) Math.pow(2.0d, (double) i);
    }

    public static double tileSizeWithZoom(int i) {
        return tileSize(tilesPerSide(i));
    }

    public static double toleranceDistance(int i, int i2) {
        return tileSizeWithZoom(i) / ((double) i2);
    }

    public static double toleranceDistance(int i, int i2, int i3) {
        return toleranceDistance(i, Math.max(i2, i3));
    }

    public static int getYAsOppositeTileFormat(int i, int i2) {
        return (tilesPerSide(i) - i2) - 1;
    }

    public static int zoomFromTilesPerSide(int i) {
        return (int) (Math.log((double) i) / Math.log(2.0d));
    }

    public static TileGrid getTileGrid(BoundingBox boundingBox, long j, long j2, BoundingBox boundingBox2) {
        long j3;
        long j4;
        BoundingBox boundingBox3 = boundingBox;
        long j5 = j;
        long j6 = j2;
        long tileColumn = getTileColumn(boundingBox3, j5, boundingBox2.getMinLongitude());
        long tileColumn2 = getTileColumn(boundingBox3, j5, boundingBox2.getMaxLongitude());
        long j7 = 0;
        if (tileColumn < j5 && tileColumn2 >= 0) {
            if (tileColumn < 0) {
                tileColumn = 0;
            }
            if (tileColumn2 >= j5) {
                tileColumn2 = j5 - 1;
            }
        }
        long j8 = tileColumn;
        long j9 = tileColumn2;
        long tileRow = getTileRow(boundingBox3, j6, boundingBox2.getMinLatitude());
        long tileRow2 = getTileRow(boundingBox3, j6, boundingBox2.getMaxLatitude());
        if (tileRow2 >= j6 || tileRow < 0) {
            j3 = tileRow;
            j4 = tileRow2;
        } else {
            if (tileRow2 >= 0) {
                j7 = tileRow2;
            }
            j3 = tileRow >= j6 ? j6 - 1 : tileRow;
            j4 = j7;
        }
        return new TileGrid(j8, j4, j9, j3);
    }

    public static long getTileColumn(BoundingBox boundingBox, long j, double d) {
        double minLongitude = boundingBox.getMinLongitude();
        double maxLongitude = boundingBox.getMaxLongitude();
        if (d < minLongitude) {
            return -1;
        }
        if (d >= maxLongitude) {
            return j;
        }
        return (long) ((d - minLongitude) / ((boundingBox.getMaxLongitude() - boundingBox.getMinLongitude()) / ((double) j)));
    }

    public static long getTileRow(BoundingBox boundingBox, long j, double d) {
        double minLatitude = boundingBox.getMinLatitude();
        double maxLatitude = boundingBox.getMaxLatitude();
        if (d <= minLatitude) {
            return j;
        }
        if (d > maxLatitude) {
            return -1;
        }
        return (long) ((maxLatitude - d) / ((boundingBox.getMaxLatitude() - boundingBox.getMinLatitude()) / ((double) j)));
    }

    public static BoundingBox getBoundingBox(BoundingBox boundingBox, TileMatrix tileMatrix, long j, long j2) {
        return getBoundingBox(boundingBox, tileMatrix.getMatrixWidth(), tileMatrix.getMatrixHeight(), j, j2);
    }

    public static BoundingBox getBoundingBox(BoundingBox boundingBox, long j, long j2, long j3, long j4) {
        return getBoundingBox(boundingBox, j, j2, new TileGrid(j3, j4, j3, j4));
    }

    public static BoundingBox getBoundingBox(BoundingBox boundingBox, TileMatrix tileMatrix, TileGrid tileGrid) {
        return getBoundingBox(boundingBox, tileMatrix.getMatrixWidth(), tileMatrix.getMatrixHeight(), tileGrid);
    }

    public static BoundingBox getBoundingBox(BoundingBox boundingBox, long j, long j2, TileGrid tileGrid) {
        double minLongitude = boundingBox.getMinLongitude();
        double maxLongitude = (boundingBox.getMaxLongitude() - minLongitude) / ((double) j);
        double minX = minLongitude + (((double) tileGrid.getMinX()) * maxLongitude);
        double maxX = minLongitude + (maxLongitude * ((double) (tileGrid.getMaxX() + 1)));
        double minLatitude = boundingBox.getMinLatitude();
        double maxLatitude = boundingBox.getMaxLatitude();
        double d = (maxLatitude - minLatitude) / ((double) j2);
        return new BoundingBox(minX, maxLatitude - (d * ((double) (tileGrid.getMaxY() + 1))), maxX, maxLatitude - (((double) tileGrid.getMinY()) * d));
    }

    public static int getZoomLevel(BoundingBox boundingBox) {
        double d = ProjectionConstants.WEB_MERCATOR_HALF_WORLD_WIDTH * 2.0d;
        double maxLongitude = boundingBox.getMaxLongitude() - boundingBox.getMinLongitude();
        double maxLatitude = boundingBox.getMaxLatitude() - boundingBox.getMinLatitude();
        if (maxLongitude <= 0.0d) {
            maxLongitude = Double.MIN_VALUE;
        }
        if (maxLatitude <= 0.0d) {
            maxLatitude = Double.MIN_VALUE;
        }
        return zoomFromTilesPerSide(Math.max(Math.min((int) (d / maxLongitude), (int) (d / maxLatitude)), 1));
    }

    public static double getPixelXSize(BoundingBox boundingBox, long j, int i) {
        return ((boundingBox.getMaxLongitude() - boundingBox.getMinLongitude()) / ((double) j)) / ((double) i);
    }

    public static double getPixelYSize(BoundingBox boundingBox, long j, int i) {
        return ((boundingBox.getMaxLatitude() - boundingBox.getMinLatitude()) / ((double) j)) / ((double) i);
    }

    public static BoundingBox boundWgs84BoundingBoxWithWebMercatorLimits(BoundingBox boundingBox) {
        return boundDegreesBoundingBoxWithWebMercatorLimits(boundingBox);
    }

    public static BoundingBox boundDegreesBoundingBoxWithWebMercatorLimits(BoundingBox boundingBox) {
        BoundingBox boundingBox2 = new BoundingBox(boundingBox);
        if (boundingBox2.getMinLatitude() < -85.05112877980659d) {
            boundingBox2.setMinLatitude(-85.05112877980659d);
        }
        if (boundingBox2.getMaxLatitude() > 85.0511287798066d) {
            boundingBox2.setMaxLatitude(85.0511287798066d);
        }
        return boundingBox2;
    }

    public static TileGrid getTileGridWGS84(BoundingBox boundingBox, int i) {
        int tilesPerWGS84LatSide = tilesPerWGS84LatSide(i);
        int tilesPerWGS84LonSide = tilesPerWGS84LonSide(i);
        double tileSizeLatPerWGS84Side = tileSizeLatPerWGS84Side(tilesPerWGS84LatSide);
        double tileSizeLonPerWGS84Side = tileSizeLonPerWGS84Side(tilesPerWGS84LonSide);
        int minLongitude = (int) ((boundingBox.getMinLongitude() + ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH) / tileSizeLonPerWGS84Side);
        double maxLongitude = (boundingBox.getMaxLongitude() + ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH) / tileSizeLonPerWGS84Side;
        int i2 = (int) maxLongitude;
        if (maxLongitude % 1.0d == 0.0d) {
            i2--;
        }
        int min = Math.min(i2, tilesPerWGS84LonSide - 1);
        int maxLatitude = (int) (((boundingBox.getMaxLatitude() - ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT) * -1.0d) / tileSizeLatPerWGS84Side);
        double minLatitude = ((boundingBox.getMinLatitude() - ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT) * -1.0d) / tileSizeLatPerWGS84Side;
        int i3 = (int) minLatitude;
        if (minLatitude % 1.0d == 0.0d) {
            i3--;
        }
        return new TileGrid((long) minLongitude, (long) maxLatitude, (long) min, (long) Math.min(i3, tilesPerWGS84LatSide - 1));
    }

    public static BoundingBox getWGS84BoundingBox(TileGrid tileGrid, int i) {
        int tilesPerWGS84LatSide = tilesPerWGS84LatSide(i);
        int tilesPerWGS84LonSide = tilesPerWGS84LonSide(i);
        double tileSizeLatPerWGS84Side = tileSizeLatPerWGS84Side(tilesPerWGS84LatSide);
        double tileSizeLonPerWGS84Side = tileSizeLonPerWGS84Side(tilesPerWGS84LonSide);
        return new BoundingBox((ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH * -1.0d) + (((double) tileGrid.getMinX()) * tileSizeLonPerWGS84Side), ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT - (((double) (tileGrid.getMaxY() + 1)) * tileSizeLatPerWGS84Side), (ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH * -1.0d) + (((double) (tileGrid.getMaxX() + 1)) * tileSizeLonPerWGS84Side), ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT - (((double) tileGrid.getMinY()) * tileSizeLatPerWGS84Side));
    }

    public static int tilesPerWGS84LatSide(int i) {
        return tilesPerSide(i);
    }

    public static int tilesPerWGS84LonSide(int i) {
        return tilesPerSide(i) * 2;
    }

    public static double tileSizeLatPerWGS84Side(int i) {
        return (ProjectionConstants.WGS84_HALF_WORLD_LAT_HEIGHT * 2.0d) / ((double) i);
    }

    public static double tileSizeLonPerWGS84Side(int i) {
        return (ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH * 2.0d) / ((double) i);
    }

    public static TileGrid tileGridZoom(TileGrid tileGrid, int i, int i2) {
        int i3 = i2 - i;
        if (i3 > 0) {
            return tileGridZoomIncrease(tileGrid, i3);
        }
        return i3 < 0 ? tileGridZoomDecrease(tileGrid, Math.abs(i3)) : tileGrid;
    }

    public static TileGrid tileGridZoomIncrease(TileGrid tileGrid, int i) {
        return new TileGrid(tileGridMinZoomIncrease(tileGrid.getMinX(), i), tileGridMinZoomIncrease(tileGrid.getMinY(), i), tileGridMaxZoomIncrease(tileGrid.getMaxX(), i), tileGridMaxZoomIncrease(tileGrid.getMaxY(), i));
    }

    public static TileGrid tileGridZoomDecrease(TileGrid tileGrid, int i) {
        return new TileGrid(tileGridMinZoomDecrease(tileGrid.getMinX(), i), tileGridMinZoomDecrease(tileGrid.getMinY(), i), tileGridMaxZoomDecrease(tileGrid.getMaxX(), i), tileGridMaxZoomDecrease(tileGrid.getMaxY(), i));
    }

    public static long tileGridMinZoomIncrease(long j, int i) {
        return j * ((long) Math.pow(2.0d, (double) i));
    }

    public static long tileGridMaxZoomIncrease(long j, int i) {
        return ((j + 1) * ((long) Math.pow(2.0d, (double) i))) - 1;
    }

    public static long tileGridMinZoomDecrease(long j, int i) {
        return (long) Math.floor(((double) j) / Math.pow(2.0d, (double) i));
    }

    public static long tileGridMaxZoomDecrease(long j, int i) {
        return (long) Math.ceil((((double) (j + 1)) / Math.pow(2.0d, (double) i)) - 1.0d);
    }
}
