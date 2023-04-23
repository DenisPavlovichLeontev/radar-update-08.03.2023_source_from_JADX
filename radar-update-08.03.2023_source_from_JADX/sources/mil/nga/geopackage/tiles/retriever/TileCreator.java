package mil.nga.geopackage.tiles.retriever;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import java.io.IOException;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.p010io.BitmapConverter;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.projection.ProjectionFactory;
import mil.nga.geopackage.projection.ProjectionTransform;
import mil.nga.geopackage.tiles.TileBoundingBoxAndroidUtils;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.user.TileCursor;
import mil.nga.geopackage.tiles.user.TileDao;
import mil.nga.geopackage.tiles.user.TileRow;
import org.osgeo.proj4j.ProjCoordinate;

public class TileCreator {
    private static final Bitmap.CompressFormat COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    private final Integer height;
    private final Projection requestProjection;
    private final boolean sameProjection;
    private final TileDao tileDao;
    private final TileMatrixSet tileMatrixSet;
    private final BoundingBox tileSetBoundingBox;
    private final Projection tilesProjection;
    private final Integer width;

    public TileCreator(TileDao tileDao2, Integer num, Integer num2, Projection projection) {
        this.tileDao = tileDao2;
        this.width = num;
        this.height = num2;
        this.requestProjection = projection;
        TileMatrixSet tileMatrixSet2 = tileDao2.getTileMatrixSet();
        this.tileMatrixSet = tileMatrixSet2;
        Projection projection2 = ProjectionFactory.getProjection(tileDao2.getTileMatrixSet().getSrs());
        this.tilesProjection = projection2;
        this.tileSetBoundingBox = tileMatrixSet2.getBoundingBox();
        this.sameProjection = projection.getUnit().name.equals(projection2.getUnit().name);
    }

    public TileCreator(TileDao tileDao2) {
        this(tileDao2, (Integer) null, (Integer) null, tileDao2.getProjection());
    }

    public TileCreator(TileDao tileDao2, Integer num, Integer num2) {
        this(tileDao2, num, num2, tileDao2.getProjection());
    }

    public TileCreator(TileDao tileDao2, Projection projection) {
        this(tileDao2, (Integer) null, (Integer) null, projection);
    }

    public TileDao getTileDao() {
        return this.tileDao;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public TileMatrixSet getTileMatrixSet() {
        return this.tileMatrixSet;
    }

    public Projection getRequestProjection() {
        return this.requestProjection;
    }

    public Projection getTilesProjection() {
        return this.tilesProjection;
    }

    public BoundingBox getTileSetBoundingBox() {
        return this.tileSetBoundingBox;
    }

    public boolean isSameProjection() {
        return this.sameProjection;
    }

    public boolean hasTile(BoundingBox boundingBox) {
        BoundingBox transform = this.requestProjection.getTransformation(this.tilesProjection).transform(boundingBox);
        TileCursor retrieveTileResults = retrieveTileResults(transform, getTileMatrix(transform));
        boolean z = false;
        if (retrieveTileResults != null) {
            try {
                if (retrieveTileResults.getCount() > 0) {
                    z = true;
                }
            } finally {
                retrieveTileResults.close();
            }
        }
        return z;
    }

    public GeoPackageTile getTile(BoundingBox boundingBox) {
        int i;
        int i2;
        int i3;
        int i4;
        BoundingBox boundingBox2 = boundingBox;
        ProjectionTransform transformation = this.requestProjection.getTransformation(this.tilesProjection);
        BoundingBox transform = transformation.transform(boundingBox2);
        TileMatrix tileMatrix = getTileMatrix(transform);
        TileCursor retrieveTileResults = retrieveTileResults(transform, tileMatrix);
        GeoPackageTile geoPackageTile = null;
        if (retrieveTileResults != null) {
            try {
                if (retrieveTileResults.getCount() > 0) {
                    BoundingBox transform2 = transformation.transform(boundingBox2);
                    Integer num = this.width;
                    if (num != null) {
                        i = num.intValue();
                    } else {
                        i = (int) tileMatrix.getTileWidth();
                    }
                    int i5 = i;
                    Integer num2 = this.height;
                    if (num2 != null) {
                        i2 = num2.intValue();
                    } else {
                        i2 = (int) tileMatrix.getTileHeight();
                    }
                    int i6 = i2;
                    if (!this.sameProjection) {
                        int round = (int) Math.round((transform2.getMaxLongitude() - transform2.getMinLongitude()) / tileMatrix.getPixelXSize());
                        i4 = round;
                        i3 = (int) Math.round((transform2.getMaxLatitude() - transform2.getMinLatitude()) / tileMatrix.getPixelYSize());
                    } else {
                        i4 = i5;
                        i3 = i6;
                    }
                    Bitmap drawTile = drawTile(tileMatrix, retrieveTileResults, transform2, i4, i3);
                    if (drawTile != null) {
                        if (!this.sameProjection) {
                            Bitmap reprojectTile = reprojectTile(drawTile, i5, i6, boundingBox, transformation, transform);
                            drawTile.recycle();
                            drawTile = reprojectTile;
                        }
                        byte[] bytes = BitmapConverter.toBytes(drawTile, COMPRESS_FORMAT);
                        drawTile.recycle();
                        geoPackageTile = new GeoPackageTile(i5, i6, bytes);
                    }
                }
            } catch (IOException e) {
                Log.e("TileCreator", "Failed to create tile. min lat: " + boundingBox.getMinLatitude() + ", max lat: " + boundingBox.getMaxLatitude() + ", min lon: " + boundingBox.getMinLongitude() + ", max lon: " + boundingBox.getMaxLongitude(), e);
            } catch (Throwable th) {
                retrieveTileResults.close();
                throw th;
            }
            retrieveTileResults.close();
        }
        return geoPackageTile;
    }

    private Bitmap drawTile(TileMatrix tileMatrix, TileCursor tileCursor, BoundingBox boundingBox, int i, int i2) {
        int i3 = i;
        int i4 = i2;
        Bitmap bitmap = null;
        Canvas canvas = null;
        Paint paint = null;
        while (tileCursor.moveToNext()) {
            TileRow tileRow = (TileRow) tileCursor.getRow();
            Bitmap tileDataBitmap = tileRow.getTileDataBitmap();
            BoundingBox boundingBox2 = TileBoundingBoxUtils.getBoundingBox(this.tileSetBoundingBox, tileMatrix, tileRow.getTileColumn(), tileRow.getTileRow());
            BoundingBox overlap = TileBoundingBoxUtils.overlap(boundingBox, boundingBox2);
            if (overlap != null) {
                Rect rectangle = TileBoundingBoxAndroidUtils.getRectangle(tileMatrix.getTileWidth(), tileMatrix.getTileHeight(), boundingBox2, overlap);
                RectF roundedFloatRectangle = TileBoundingBoxAndroidUtils.getRoundedFloatRectangle((long) i3, (long) i4, boundingBox, overlap);
                if (bitmap == null) {
                    bitmap = Bitmap.createBitmap(i3, i4, Bitmap.Config.ARGB_8888);
                    canvas = new Canvas(bitmap);
                    paint = new Paint(1);
                }
                canvas.drawBitmap(tileDataBitmap, rectangle, roundedFloatRectangle, paint);
            }
        }
        return bitmap;
    }

    private Bitmap reprojectTile(Bitmap bitmap, int i, int i2, BoundingBox boundingBox, ProjectionTransform projectionTransform, BoundingBox boundingBox2) {
        int i3 = i;
        int i4 = i2;
        double maxLongitude = (boundingBox.getMaxLongitude() - boundingBox.getMinLongitude()) / ((double) i3);
        double maxLatitude = (boundingBox.getMaxLatitude() - boundingBox.getMinLatitude()) / ((double) i4);
        double maxLongitude2 = boundingBox2.getMaxLongitude() - boundingBox2.getMinLongitude();
        double maxLatitude2 = boundingBox2.getMaxLatitude() - boundingBox2.getMinLatitude();
        int width2 = bitmap.getWidth();
        int height2 = bitmap.getHeight();
        int[] iArr = new int[(width2 * height2)];
        int[] iArr2 = iArr;
        int i5 = height2;
        int i6 = width2;
        bitmap.getPixels(iArr, 0, width2, 0, 0, i6, i5);
        int[] iArr3 = new int[(i3 * i4)];
        int i7 = 0;
        while (i7 < i4) {
            int i8 = 0;
            while (i8 < i3) {
                double d = maxLongitude;
                int i9 = i7;
                ProjCoordinate transform = projectionTransform.transform(new ProjCoordinate(boundingBox.getMinLongitude() + (((double) i8) * maxLongitude), boundingBox.getMaxLatitude() - (((double) i9) * maxLatitude)));
                double d2 = transform.f409x;
                double d3 = transform.f410y;
                double d4 = maxLatitude;
                int i10 = i6;
                int i11 = i5;
                int i12 = i;
                iArr3[(i9 * i12) + i8] = iArr2[(Math.min(i11 - 1, Math.max(0, (int) Math.round(((boundingBox2.getMaxLatitude() - d3) / maxLatitude2) * ((double) i11)))) * i10) + Math.min(i10 - 1, Math.max(0, (int) Math.round(((d2 - boundingBox2.getMinLongitude()) / maxLongitude2) * ((double) i10))))];
                i8++;
                i7 = i9;
                i3 = i12;
                maxLongitude = d;
                maxLatitude = d4;
                int i13 = i2;
            }
            int i14 = i3;
            int i15 = i5;
            int i16 = i6;
            ProjectionTransform projectionTransform2 = projectionTransform;
            i7++;
            i3 = i14;
            maxLongitude = maxLongitude;
            maxLatitude = maxLatitude;
            i4 = i2;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i3, i2, bitmap.getConfig());
        createBitmap.setPixels(iArr3, 0, i, 0, 0, i, i2);
        return createBitmap;
    }

    private TileMatrix getTileMatrix(BoundingBox boundingBox) {
        Long zoomLevel;
        if (TileBoundingBoxUtils.overlap(boundingBox, this.tileSetBoundingBox) == null || (zoomLevel = this.tileDao.getZoomLevel(boundingBox.getMaxLongitude() - boundingBox.getMinLongitude(), boundingBox.getMaxLatitude() - boundingBox.getMinLatitude())) == null) {
            return null;
        }
        return this.tileDao.getTileMatrix(zoomLevel.longValue());
    }

    private TileCursor retrieveTileResults(BoundingBox boundingBox, TileMatrix tileMatrix) {
        if (tileMatrix == null) {
            return null;
        }
        return this.tileDao.queryByTileGrid(TileBoundingBoxUtils.getTileGrid(this.tileSetBoundingBox, tileMatrix.getMatrixWidth(), tileMatrix.getMatrixHeight(), boundingBox), tileMatrix.getZoomLevel());
    }
}
