package mil.nga.geopackage.tiles.features;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import java.util.Iterator;
import java.util.List;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.features.index.FeatureIndexResults;
import mil.nga.geopackage.features.user.FeatureCursor;
import mil.nga.geopackage.features.user.FeatureDao;
import mil.nga.geopackage.features.user.FeatureRow;
import mil.nga.geopackage.geom.GeoPackageGeometryData;
import mil.nga.geopackage.projection.ProjectionTransform;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.wkb.geom.CompoundCurve;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.MultiLineString;
import mil.nga.wkb.geom.MultiPoint;
import mil.nga.wkb.geom.MultiPolygon;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;
import mil.nga.wkb.geom.PolyhedralSurface;

public class DefaultFeatureTiles extends FeatureTiles {
    public DefaultFeatureTiles(Context context, FeatureDao featureDao) {
        super(context, featureDao);
    }

    public DefaultFeatureTiles(Context context) {
        this(context, (FeatureDao) null);
    }

    public Bitmap drawTile(int i, BoundingBox boundingBox, FeatureIndexResults featureIndexResults) {
        Bitmap createNewBitmap = createNewBitmap();
        Canvas canvas = new Canvas(createNewBitmap);
        ProjectionTransform projectionToWebMercatorTransform = getProjectionToWebMercatorTransform(this.featureDao.getProjection());
        Iterator it = featureIndexResults.iterator();
        while (it.hasNext()) {
            drawFeature(i, boundingBox, projectionToWebMercatorTransform, canvas, (FeatureRow) it.next());
        }
        return createNewBitmap;
    }

    public Bitmap drawTile(int i, BoundingBox boundingBox, FeatureCursor featureCursor) {
        Bitmap createNewBitmap = createNewBitmap();
        Canvas canvas = new Canvas(createNewBitmap);
        ProjectionTransform projectionToWebMercatorTransform = getProjectionToWebMercatorTransform(this.featureDao.getProjection());
        while (featureCursor.moveToNext()) {
            drawFeature(i, boundingBox, projectionToWebMercatorTransform, canvas, (FeatureRow) featureCursor.getRow());
        }
        featureCursor.close();
        return createNewBitmap;
    }

    public Bitmap drawTile(int i, BoundingBox boundingBox, List<FeatureRow> list) {
        Bitmap createNewBitmap = createNewBitmap();
        Canvas canvas = new Canvas(createNewBitmap);
        ProjectionTransform projectionToWebMercatorTransform = getProjectionToWebMercatorTransform(this.featureDao.getProjection());
        for (FeatureRow drawFeature : list) {
            drawFeature(i, boundingBox, projectionToWebMercatorTransform, canvas, drawFeature);
        }
        return createNewBitmap;
    }

    private void drawFeature(int i, BoundingBox boundingBox, ProjectionTransform projectionTransform, Canvas canvas, FeatureRow featureRow) {
        try {
            GeoPackageGeometryData geometry = featureRow.getGeometry();
            if (geometry != null) {
                Geometry geometry2 = geometry.getGeometry();
                drawShape(TileBoundingBoxUtils.toleranceDistance(i, this.tileWidth, this.tileHeight), boundingBox, projectionTransform, canvas, geometry2);
            }
        } catch (Exception e) {
            Log.e("DefaultFeatureTiles", "Failed to draw feature in tile. Table: " + this.featureDao.getTableName(), e);
        }
    }

    /* renamed from: mil.nga.geopackage.tiles.features.DefaultFeatureTiles$1 */
    static /* synthetic */ class C11771 {
        static final /* synthetic */ int[] $SwitchMap$mil$nga$wkb$geom$GeometryType;

        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|26) */
        /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0084 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                mil.nga.wkb.geom.GeometryType[] r0 = mil.nga.wkb.geom.GeometryType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$mil$nga$wkb$geom$GeometryType = r0
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POINT     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.LINESTRING     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.CIRCULARSTRING     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0033 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POLYGON     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x003e }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.TRIANGLE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0049 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTIPOINT     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0054 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTILINESTRING     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0060 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.MULTIPOLYGON     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x006c }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.COMPOUNDCURVE     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0078 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.POLYHEDRALSURFACE     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0084 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.TIN     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                int[] r0 = $SwitchMap$mil$nga$wkb$geom$GeometryType     // Catch:{ NoSuchFieldError -> 0x0090 }
                mil.nga.wkb.geom.GeometryType r1 = mil.nga.wkb.geom.GeometryType.GEOMETRYCOLLECTION     // Catch:{ NoSuchFieldError -> 0x0090 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0090 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0090 }
            L_0x0090:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.tiles.features.DefaultFeatureTiles.C11771.<clinit>():void");
        }
    }

    private void drawShape(double d, BoundingBox boundingBox, ProjectionTransform projectionTransform, Canvas canvas, Geometry geometry) {
        Canvas canvas2 = canvas;
        switch (C11771.$SwitchMap$mil$nga$wkb$geom$GeometryType[geometry.getGeometryType().ordinal()]) {
            case 1:
                BoundingBox boundingBox2 = boundingBox;
                ProjectionTransform projectionTransform2 = projectionTransform;
                Canvas canvas3 = canvas;
                drawPoint(boundingBox2, projectionTransform2, canvas3, this.pointPaint, (Point) geometry);
                return;
            case 2:
            case 3:
                Path path = new Path();
                addLineString(d, boundingBox, projectionTransform, path, (LineString) geometry);
                drawLinePath(canvas2, path);
                return;
            case 4:
            case 5:
                Path path2 = new Path();
                addPolygon(d, boundingBox, projectionTransform, path2, (Polygon) geometry);
                drawPolygonPath(canvas2, path2);
                return;
            case 6:
                for (Point drawPoint : ((MultiPoint) geometry).getPoints()) {
                    BoundingBox boundingBox3 = boundingBox;
                    ProjectionTransform projectionTransform3 = projectionTransform;
                    Canvas canvas4 = canvas;
                    drawPoint(boundingBox3, projectionTransform3, canvas4, this.pointPaint, drawPoint);
                }
                return;
            case 7:
                Path path3 = new Path();
                for (LineString addLineString : ((MultiLineString) geometry).getLineStrings()) {
                    addLineString(d, boundingBox, projectionTransform, path3, addLineString);
                }
                drawLinePath(canvas2, path3);
                return;
            case 8:
                Path path4 = new Path();
                for (Polygon addPolygon : ((MultiPolygon) geometry).getPolygons()) {
                    addPolygon(d, boundingBox, projectionTransform, path4, addPolygon);
                }
                drawPolygonPath(canvas2, path4);
                return;
            case 9:
                Path path5 = new Path();
                for (LineString addLineString2 : ((CompoundCurve) geometry).getLineStrings()) {
                    addLineString(d, boundingBox, projectionTransform, path5, addLineString2);
                }
                drawLinePath(canvas2, path5);
                return;
            case 10:
            case 11:
                Path path6 = new Path();
                for (Polygon addPolygon2 : ((PolyhedralSurface) geometry).getPolygons()) {
                    addPolygon(d, boundingBox, projectionTransform, path6, addPolygon2);
                }
                drawPolygonPath(canvas2, path6);
                return;
            case 12:
                for (Geometry drawShape : ((GeometryCollection) geometry).getGeometries()) {
                    drawShape(d, boundingBox, projectionTransform, canvas, drawShape);
                }
                return;
            default:
                throw new GeoPackageException("Unsupported Geometry Type: " + geometry.getGeometryType().getName());
        }
    }

    private void drawLinePath(Canvas canvas, Path path) {
        canvas.drawPath(path, this.linePaint);
    }

    private void drawPolygonPath(Canvas canvas, Path path) {
        canvas.drawPath(path, this.polygonPaint);
        if (this.fillPolygon) {
            path.setFillType(Path.FillType.EVEN_ODD);
            canvas.drawPath(path, this.polygonFillPaint);
        }
    }

    private void addLineString(double d, BoundingBox boundingBox, ProjectionTransform projectionTransform, Path path, LineString lineString) {
        List<Point> points = lineString.getPoints();
        if (points.size() >= 2) {
            List<Point> simplifyPoints = simplifyPoints(d, points);
            for (int i = 0; i < simplifyPoints.size(); i++) {
                Point point = getPoint(projectionTransform, simplifyPoints.get(i));
                float xPixel = TileBoundingBoxUtils.getXPixel((long) this.tileWidth, boundingBox, point.getX());
                float yPixel = TileBoundingBoxUtils.getYPixel((long) this.tileHeight, boundingBox, point.getY());
                if (i == 0) {
                    path.moveTo(xPixel, yPixel);
                } else {
                    path.lineTo(xPixel, yPixel);
                }
            }
        }
    }

    private void addPolygon(double d, BoundingBox boundingBox, ProjectionTransform projectionTransform, Path path, Polygon polygon) {
        List rings = polygon.getRings();
        if (!rings.isEmpty()) {
            List<Point> points = ((LineString) rings.get(0)).getPoints();
            if (points.size() >= 2) {
                addRing(d, boundingBox, projectionTransform, path, points);
                for (int i = 1; i < rings.size(); i++) {
                    List<Point> points2 = ((LineString) rings.get(i)).getPoints();
                    if (points2.size() >= 2) {
                        addRing(d, boundingBox, projectionTransform, path, points2);
                    }
                }
            }
        }
    }

    private void addRing(double d, BoundingBox boundingBox, ProjectionTransform projectionTransform, Path path, List<Point> list) {
        List<Point> simplifyPoints = simplifyPoints(d, list);
        for (int i = 0; i < simplifyPoints.size(); i++) {
            Point point = getPoint(projectionTransform, simplifyPoints.get(i));
            float xPixel = TileBoundingBoxUtils.getXPixel((long) this.tileWidth, boundingBox, point.getX());
            float yPixel = TileBoundingBoxUtils.getYPixel((long) this.tileHeight, boundingBox, point.getY());
            if (i == 0) {
                path.moveTo(xPixel, yPixel);
            } else {
                path.lineTo(xPixel, yPixel);
            }
        }
        path.close();
    }

    private void drawPoint(BoundingBox boundingBox, ProjectionTransform projectionTransform, Canvas canvas, Paint paint, Point point) {
        Point point2 = getPoint(projectionTransform, point);
        float xPixel = TileBoundingBoxUtils.getXPixel((long) this.tileWidth, boundingBox, point2.getX());
        float yPixel = TileBoundingBoxUtils.getYPixel((long) this.tileHeight, boundingBox, point2.getY());
        if (this.pointIcon != null) {
            if (xPixel >= ((float) (0 - this.pointIcon.getWidth())) && xPixel <= ((float) (this.tileWidth + this.pointIcon.getWidth())) && yPixel >= ((float) (0 - this.pointIcon.getHeight())) && yPixel <= ((float) (this.tileHeight + this.pointIcon.getHeight()))) {
                canvas.drawBitmap(this.pointIcon.getIcon(), xPixel - this.pointIcon.getXOffset(), yPixel - this.pointIcon.getYOffset(), paint);
            }
        } else if (xPixel >= 0.0f - this.pointRadius && xPixel <= ((float) this.tileWidth) + this.pointRadius && yPixel >= 0.0f - this.pointRadius && yPixel <= ((float) this.tileHeight) + this.pointRadius) {
            canvas.drawCircle(xPixel, yPixel, this.pointRadius, paint);
        }
    }

    private Point getPoint(ProjectionTransform projectionTransform, Point point) {
        return projectionTransform.transform(point);
    }
}
