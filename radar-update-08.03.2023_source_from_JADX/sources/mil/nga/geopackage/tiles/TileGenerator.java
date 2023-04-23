package mil.nga.geopackage.tiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.p010io.GeoPackageProgress;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.projection.ProjectionConstants;
import mil.nga.geopackage.projection.ProjectionFactory;
import mil.nga.geopackage.projection.ProjectionTransform;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrix.TileMatrixDao;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSetDao;
import mil.nga.geopackage.tiles.user.TileCursor;
import mil.nga.geopackage.tiles.user.TileDao;
import mil.nga.geopackage.tiles.user.TileRow;
import org.osgeo.proj4j.units.DegreeUnit;

public abstract class TileGenerator {
    protected BoundingBox boundingBox;
    private Bitmap.CompressFormat compressFormat = null;
    private int compressQuality = 100;
    protected final Context context;
    private final GeoPackage geoPackage;
    private boolean googleTiles = false;
    private long matrixHeight = 0;
    private long matrixWidth = 0;
    private final int maxZoom;
    private final int minZoom;
    private BitmapFactory.Options options = null;
    private GeoPackageProgress progress;
    protected Projection projection;
    private final String tableName;
    private Integer tileCount;
    private BoundingBox tileGridBoundingBox;
    private final SparseArray<TileGrid> tileGrids = new SparseArray<>();

    /* access modifiers changed from: protected */
    public abstract byte[] createTile(int i, long j, long j2);

    /* access modifiers changed from: protected */
    public abstract void preTileGeneration();

    public TileGenerator(Context context2, GeoPackage geoPackage2, String str, int i, int i2, BoundingBox boundingBox2, Projection projection2) {
        this.context = context2;
        geoPackage2.verifyWritable();
        this.geoPackage = geoPackage2;
        this.tableName = str;
        this.minZoom = i;
        this.maxZoom = i2;
        this.boundingBox = boundingBox2;
        this.projection = projection2;
    }

    public GeoPackage getGeoPackage() {
        return this.geoPackage;
    }

    public String getTableName() {
        return this.tableName;
    }

    public int getMinZoom() {
        return this.minZoom;
    }

    public int getMaxZoom() {
        return this.maxZoom;
    }

    public void setCompressFormat(Bitmap.CompressFormat compressFormat2) {
        this.compressFormat = compressFormat2;
    }

    public Bitmap.CompressFormat getCompressFormat() {
        return this.compressFormat;
    }

    public void setCompressQuality(Integer num) {
        if (num != null) {
            this.compressQuality = num.intValue();
        }
    }

    public Integer getCompressQuality() {
        return Integer.valueOf(this.compressQuality);
    }

    public void setProgress(GeoPackageProgress geoPackageProgress) {
        this.progress = geoPackageProgress;
    }

    public GeoPackageProgress getProgress() {
        return this.progress;
    }

    public void setBitmapCompressionConfig(Bitmap.Config config) {
        if (this.options == null) {
            this.options = new BitmapFactory.Options();
        }
        this.options.inPreferredConfig = config;
    }

    public void setGoogleTiles(boolean z) {
        this.googleTiles = z;
    }

    public boolean isGoogleTiles() {
        return this.googleTiles;
    }

    public int getTileCount() {
        BoundingBox boundingBox2;
        TileGrid tileGrid;
        if (this.tileCount == null) {
            long j = 0;
            if (this.projection.getUnit() instanceof DegreeUnit) {
                boundingBox2 = this.boundingBox;
            } else {
                boundingBox2 = this.projection.getTransformation(3857).transform(this.boundingBox);
            }
            for (int i = this.minZoom; i <= this.maxZoom; i++) {
                if (this.projection.getUnit() instanceof DegreeUnit) {
                    tileGrid = TileBoundingBoxUtils.getTileGridWGS84(boundingBox2, i);
                } else {
                    tileGrid = TileBoundingBoxUtils.getTileGrid(boundingBox2, i);
                }
                j += tileGrid.count();
                this.tileGrids.put(i, tileGrid);
            }
            this.tileCount = Integer.valueOf((int) Math.min(j, 2147483647L));
        }
        return this.tileCount.intValue();
    }

    public int generateTiles() throws SQLException, IOException {
        boolean z;
        TileMatrixSet tileMatrixSet;
        int tileCount2 = getTileCount();
        GeoPackageProgress geoPackageProgress = this.progress;
        if (geoPackageProgress != null) {
            geoPackageProgress.setMax(tileCount2);
        }
        adjustBounds(this.boundingBox, this.minZoom);
        TileMatrixSetDao tileMatrixSetDao = this.geoPackage.getTileMatrixSetDao();
        if (!tileMatrixSetDao.isTableExists() || !tileMatrixSetDao.idExists(this.tableName)) {
            SpatialReferenceSystem orCreateCode = this.geoPackage.getSpatialReferenceSystemDao().getOrCreateCode(this.projection.getAuthority(), Long.parseLong(this.projection.getCode()));
            tileMatrixSet = this.geoPackage.createTileTableWithMetadata(this.tableName, this.boundingBox, orCreateCode.getSrsId(), this.tileGridBoundingBox, orCreateCode.getSrsId());
            z = false;
        } else {
            tileMatrixSet = (TileMatrixSet) tileMatrixSetDao.queryForId(this.tableName);
            updateTileBounds(tileMatrixSet);
            z = true;
        }
        preTileGeneration();
        try {
            Contents contents = tileMatrixSet.getContents();
            TileMatrixDao tileMatrixDao = this.geoPackage.getTileMatrixDao();
            TileDao tileDao = this.geoPackage.getTileDao(tileMatrixSet);
            int i = this.minZoom;
            int i2 = 0;
            while (i <= this.maxZoom && ((r1 = this.progress) == null || r1.isActive())) {
                TileGrid tileGrid = null;
                if (this.googleTiles) {
                    long tilesPerSide = (long) TileBoundingBoxUtils.tilesPerSide(i);
                    this.matrixWidth = tilesPerSide;
                    this.matrixHeight = tilesPerSide;
                } else {
                    tileGrid = TileBoundingBoxUtils.getTileGrid(this.tileGridBoundingBox, this.matrixWidth, this.matrixHeight, this.boundingBox);
                }
                TileGrid tileGrid2 = tileGrid;
                int i3 = i;
                Contents contents2 = contents;
                i2 += generateTiles(tileMatrixDao, tileDao, contents, i, this.tileGrids.get(i), tileGrid2, this.matrixWidth, this.matrixHeight, z);
                if (!this.googleTiles) {
                    this.matrixWidth *= 2;
                    this.matrixHeight *= 2;
                }
                i = i3 + 1;
                contents = contents2;
            }
            Contents contents3 = contents;
            GeoPackageProgress geoPackageProgress2 = this.progress;
            if (geoPackageProgress2 == null || geoPackageProgress2.isActive() || !this.progress.cleanupOnCancel()) {
                contents3.setLastChange(new Date());
                this.geoPackage.getContentsDao().update(contents3);
                return i2;
            }
            this.geoPackage.deleteTableQuietly(this.tableName);
            return 0;
        } catch (RuntimeException e) {
            this.geoPackage.deleteTableQuietly(this.tableName);
            throw e;
        } catch (SQLException e2) {
            this.geoPackage.deleteTableQuietly(this.tableName);
            throw e2;
        } catch (IOException e3) {
            this.geoPackage.deleteTableQuietly(this.tableName);
            throw e3;
        }
    }

    private void adjustBounds(BoundingBox boundingBox2, int i) {
        if (this.googleTiles) {
            adjustGoogleBounds();
        } else if (this.projection.getUnit() instanceof DegreeUnit) {
            adjustGeoPackageBoundsWGS84(boundingBox2, i);
        } else {
            adjustGeoPackageBounds(boundingBox2, i);
        }
    }

    private void adjustGoogleBounds() {
        this.tileGridBoundingBox = ProjectionFactory.getProjection(4326).getTransformation(3857).transform(new BoundingBox(-ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH, -85.05112877980659d, ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH, 85.0511287798066d));
    }

    private void adjustGeoPackageBoundsWGS84(BoundingBox boundingBox2, int i) {
        TileGrid tileGridWGS84 = TileBoundingBoxUtils.getTileGridWGS84(boundingBox2, i);
        this.tileGridBoundingBox = TileBoundingBoxUtils.getWGS84BoundingBox(tileGridWGS84, i);
        this.matrixWidth = (tileGridWGS84.getMaxX() + 1) - tileGridWGS84.getMinX();
        this.matrixHeight = (tileGridWGS84.getMaxY() + 1) - tileGridWGS84.getMinY();
    }

    private void adjustGeoPackageBounds(BoundingBox boundingBox2, int i) {
        TileGrid tileGrid = TileBoundingBoxUtils.getTileGrid(boundingBox2, i);
        this.tileGridBoundingBox = TileBoundingBoxUtils.getWebMercatorBoundingBox(tileGrid, i);
        this.matrixWidth = (tileGrid.getMaxX() + 1) - tileGrid.getMinX();
        this.matrixHeight = (tileGrid.getMaxY() + 1) - tileGrid.getMinY();
    }

    /* JADX INFO: finally extract failed */
    private void updateTileBounds(TileMatrixSet tileMatrixSet) throws SQLException {
        int i;
        TileDao tileDao;
        TileGenerator tileGenerator = this;
        TileMatrixSet tileMatrixSet2 = tileMatrixSet;
        TileDao tileDao2 = tileGenerator.geoPackage.getTileDao(tileMatrixSet2);
        if (tileDao2.isGoogleTiles()) {
            if (!tileGenerator.googleTiles) {
                tileGenerator.googleTiles = true;
                adjustGoogleBounds();
            }
        } else if (tileGenerator.googleTiles) {
            throw new GeoPackageException("Can not add Google formatted tiles to " + tileGenerator.tableName + " which already contains GeoPackage formatted tiles");
        }
        Projection projection2 = ProjectionFactory.getProjection(tileMatrixSet.getSrs());
        if (projection2.equals(tileGenerator.projection)) {
            Contents contents = tileMatrixSet.getContents();
            BoundingBox boundingBox2 = contents.getBoundingBox();
            if (boundingBox2 != null) {
                BoundingBox union = TileBoundingBoxUtils.union(tileGenerator.projection.getTransformation(ProjectionFactory.getProjection(contents.getSrs())).transform(tileGenerator.boundingBox), boundingBox2);
                if (!union.equals(boundingBox2)) {
                    contents.setBoundingBox(union);
                    tileGenerator.geoPackage.getContentsDao().update(contents);
                }
            }
            if (!tileGenerator.googleTiles) {
                BoundingBox boundingBox3 = tileMatrixSet.getBoundingBox();
                ProjectionTransform transformation = tileGenerator.projection.getTransformation(projection2);
                BoundingBox transform = transformation.transform(tileGenerator.boundingBox);
                int min = Math.min(tileGenerator.minZoom, (int) tileDao2.getMinZoom());
                tileGenerator.adjustBounds(transform, min);
                BoundingBox transform2 = transformation.transform(tileGenerator.tileGridBoundingBox);
                if (!boundingBox3.equals(transform2)) {
                    BoundingBox union2 = TileBoundingBoxUtils.union(transform2, boundingBox3);
                    tileMatrixSet2.setBoundingBox(union2);
                    tileGenerator.geoPackage.getTileMatrixSetDao().update(tileMatrixSet2);
                    tileGenerator.adjustBounds(union2, min);
                }
                TileMatrixDao tileMatrixDao = tileGenerator.geoPackage.getTileMatrixDao();
                long minZoom2 = tileDao2.getMinZoom();
                while (true) {
                    double d = 2.0d;
                    if (minZoom2 > tileDao2.getMaxZoom()) {
                        break;
                    }
                    TileMatrix tileMatrix = tileDao2.getTileMatrix(minZoom2);
                    if (tileMatrix != null) {
                        long pow = (long) Math.pow(2.0d, (double) (minZoom2 - ((long) min)));
                        long j = tileGenerator.matrixWidth * pow;
                        long j2 = tileGenerator.matrixHeight * pow;
                        TileCursor queryForTileDescending = tileDao2.queryForTileDescending(minZoom2);
                        while (queryForTileDescending.moveToNext()) {
                            try {
                                TileRow tileRow = (TileRow) queryForTileDescending.getRow();
                                TileRow tileRow2 = tileRow;
                                BoundingBox boundingBox4 = TileBoundingBoxUtils.getBoundingBox(boundingBox3, tileMatrix, tileRow.getTileColumn(), tileRow.getTileRow());
                                double minLatitude = boundingBox4.getMinLatitude() + ((boundingBox4.getMaxLatitude() - boundingBox4.getMinLatitude()) / d);
                                long j3 = j2;
                                long tileRow3 = TileBoundingBoxUtils.getTileRow(tileGenerator.tileGridBoundingBox, j3, minLatitude);
                                int i2 = min;
                                long j4 = j;
                                long tileColumn = TileBoundingBoxUtils.getTileColumn(tileGenerator.tileGridBoundingBox, j4, boundingBox4.getMinLongitude() + ((boundingBox4.getMaxLongitude() - boundingBox4.getMinLongitude()) / d));
                                TileRow tileRow4 = tileRow2;
                                tileRow4.setTileRow(tileRow3);
                                tileRow4.setTileColumn(tileColumn);
                                tileDao2.update(tileRow4);
                                j = j4;
                                j2 = j3;
                                min = i2;
                                d = 2.0d;
                            } catch (Throwable th) {
                                queryForTileDescending.close();
                                throw th;
                            }
                        }
                        long j5 = j2;
                        i = min;
                        long j6 = j;
                        queryForTileDescending.close();
                        double maxLongitude = ((tileGenerator.tileGridBoundingBox.getMaxLongitude() - tileGenerator.tileGridBoundingBox.getMinLongitude()) / ((double) j6)) / ((double) tileMatrix.getTileWidth());
                        tileDao = tileDao2;
                        tileMatrix.setMatrixWidth(j6);
                        tileMatrix.setMatrixHeight(j5);
                        tileMatrix.setPixelXSize(maxLongitude);
                        tileMatrix.setPixelYSize(((tileGenerator.tileGridBoundingBox.getMaxLatitude() - tileGenerator.tileGridBoundingBox.getMinLatitude()) / ((double) j5)) / ((double) tileMatrix.getTileHeight()));
                        tileMatrixDao.update(tileMatrix);
                    } else {
                        tileDao = tileDao2;
                        i = min;
                    }
                    minZoom2++;
                    tileGenerator = this;
                    tileDao2 = tileDao;
                    min = i;
                }
                int i3 = tileGenerator.minZoom;
                int i4 = min;
                if (i4 < i3) {
                    long pow2 = (long) Math.pow(2.0d, (double) (i3 - i4));
                    tileGenerator.matrixWidth *= pow2;
                    tileGenerator.matrixHeight *= pow2;
                    return;
                }
                return;
            }
            return;
        }
        throw new GeoPackageException("Can not update tiles projected at " + projection2.getCode() + " with tiles projected at " + tileGenerator.projection.getCode());
    }

    public void close() {
        GeoPackage geoPackage2 = this.geoPackage;
        if (geoPackage2 != null) {
            geoPackage2.close();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:45:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00e4 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int generateTiles(mil.nga.geopackage.tiles.matrix.TileMatrixDao r34, mil.nga.geopackage.tiles.user.TileDao r35, mil.nga.geopackage.core.contents.Contents r36, int r37, mil.nga.geopackage.tiles.TileGrid r38, mil.nga.geopackage.tiles.TileGrid r39, long r40, long r42, boolean r44) throws java.sql.SQLException, java.io.IOException {
        /*
            r33 = this;
            r6 = r33
            r7 = r34
            r15 = r35
            r13 = r37
            r11 = r40
            r9 = r42
            long r0 = r38.getMinX()
            r16 = 0
            r17 = 0
            r18 = r0
            r0 = r16
            r1 = r0
            r2 = r17
        L_0x001b:
            long r3 = r38.getMaxX()
            int r3 = (r18 > r3 ? 1 : (r18 == r3 ? 0 : -1))
            r14 = 1
            if (r3 > 0) goto L_0x00ff
            mil.nga.geopackage.io.GeoPackageProgress r3 = r6.progress
            if (r3 == 0) goto L_0x0030
            boolean r3 = r3.isActive()
            if (r3 != 0) goto L_0x0030
            goto L_0x00ff
        L_0x0030:
            long r3 = r38.getMinY()
            r20 = r0
            r21 = r1
            r22 = r2
            r23 = r3
        L_0x003c:
            long r0 = r38.getMaxY()
            int r0 = (r23 > r0 ? 1 : (r23 == r0 ? 0 : -1))
            r25 = 1
            if (r0 > 0) goto L_0x00ef
            mil.nga.geopackage.io.GeoPackageProgress r0 = r6.progress
            if (r0 == 0) goto L_0x0052
            boolean r0 = r0.isActive()
            if (r0 != 0) goto L_0x0052
            goto L_0x00ef
        L_0x0052:
            r0 = r33
            r1 = r37
            r2 = r18
            r4 = r23
            byte[] r0 = r0.createTile(r1, r2, r4)     // Catch:{ Exception -> 0x00dc }
            if (r0 == 0) goto L_0x00dc
            android.graphics.Bitmap$CompressFormat r1 = r6.compressFormat     // Catch:{ Exception -> 0x00dc }
            if (r1 == 0) goto L_0x0075
            android.graphics.BitmapFactory$Options r1 = r6.options     // Catch:{ Exception -> 0x00dc }
            android.graphics.Bitmap r1 = mil.nga.geopackage.p010io.BitmapConverter.toBitmap(r0, r1)     // Catch:{ Exception -> 0x00dc }
            if (r1 == 0) goto L_0x0077
            android.graphics.Bitmap$CompressFormat r0 = r6.compressFormat     // Catch:{ Exception -> 0x00dc }
            int r2 = r6.compressQuality     // Catch:{ Exception -> 0x00dc }
            byte[] r0 = mil.nga.geopackage.p010io.BitmapConverter.toBytes(r1, r0, r2)     // Catch:{ Exception -> 0x00dc }
            goto L_0x0077
        L_0x0075:
            r1 = r16
        L_0x0077:
            mil.nga.geopackage.tiles.user.TileRow r2 = r35.newRow()     // Catch:{ Exception -> 0x00dc }
            long r3 = (long) r13     // Catch:{ Exception -> 0x00dc }
            r2.setZoomLevel(r3)     // Catch:{ Exception -> 0x00dc }
            if (r39 == 0) goto L_0x009a
            long r27 = r38.getMinX()     // Catch:{ Exception -> 0x00dc }
            long r27 = r18 - r27
            long r29 = r39.getMinX()     // Catch:{ Exception -> 0x00dc }
            long r27 = r27 + r29
            long r29 = r38.getMinY()     // Catch:{ Exception -> 0x00dc }
            long r29 = r23 - r29
            long r31 = r39.getMinY()     // Catch:{ Exception -> 0x00dc }
            long r29 = r29 + r31
            goto L_0x009e
        L_0x009a:
            r27 = r18
            r29 = r23
        L_0x009e:
            if (r44 == 0) goto L_0x00ac
            r8 = r35
            r9 = r27
            r11 = r29
            r5 = r14
            r13 = r3
            r8.deleteTile(r9, r11, r13)     // Catch:{ Exception -> 0x00dd }
            goto L_0x00ad
        L_0x00ac:
            r5 = r14
        L_0x00ad:
            r3 = r27
            r2.setTileColumn(r3)     // Catch:{ Exception -> 0x00dd }
            r3 = r29
            r2.setTileRow(r3)     // Catch:{ Exception -> 0x00dd }
            r2.setTileData(r0)     // Catch:{ Exception -> 0x00dd }
            r15.create(r2)     // Catch:{ Exception -> 0x00dd }
            int r22 = r22 + 1
            if (r20 != 0) goto L_0x00dd
            if (r1 != 0) goto L_0x00c9
            android.graphics.BitmapFactory$Options r1 = r6.options     // Catch:{ Exception -> 0x00dd }
            android.graphics.Bitmap r1 = mil.nga.geopackage.p010io.BitmapConverter.toBitmap(r0, r1)     // Catch:{ Exception -> 0x00dd }
        L_0x00c9:
            if (r1 == 0) goto L_0x00dd
            int r0 = r1.getWidth()     // Catch:{ Exception -> 0x00dd }
            java.lang.Integer r20 = java.lang.Integer.valueOf(r0)     // Catch:{ Exception -> 0x00dd }
            int r0 = r1.getHeight()     // Catch:{ Exception -> 0x00dd }
            java.lang.Integer r21 = java.lang.Integer.valueOf(r0)     // Catch:{ Exception -> 0x00dd }
            goto L_0x00dd
        L_0x00dc:
            r5 = r14
        L_0x00dd:
            mil.nga.geopackage.io.GeoPackageProgress r0 = r6.progress
            if (r0 == 0) goto L_0x00e4
            r0.addProgress(r5)
        L_0x00e4:
            long r23 = r23 + r25
            r13 = r37
            r11 = r40
            r9 = r42
            r14 = r5
            goto L_0x003c
        L_0x00ef:
            long r18 = r18 + r25
            r13 = r37
            r11 = r40
            r9 = r42
            r0 = r20
            r1 = r21
            r2 = r22
            goto L_0x001b
        L_0x00ff:
            r5 = r14
            if (r0 == 0) goto L_0x017f
            if (r1 != 0) goto L_0x0106
            goto L_0x017f
        L_0x0106:
            if (r44 == 0) goto L_0x0119
            mil.nga.geopackage.tiles.matrix.TileMatrixKey r3 = new mil.nga.geopackage.tiles.matrix.TileMatrixKey
            java.lang.String r4 = r6.tableName
            r8 = r37
            long r9 = (long) r8
            r3.<init>(r4, r9)
            boolean r3 = r7.idExists((mil.nga.geopackage.tiles.matrix.TileMatrixKey) r3)
            r14 = r3 ^ 1
            goto L_0x011c
        L_0x0119:
            r8 = r37
            r14 = r5
        L_0x011c:
            if (r14 == 0) goto L_0x017b
            mil.nga.geopackage.BoundingBox r3 = r6.tileGridBoundingBox
            double r3 = r3.getMaxLongitude()
            mil.nga.geopackage.BoundingBox r5 = r6.tileGridBoundingBox
            double r9 = r5.getMinLongitude()
            double r3 = r3 - r9
            r9 = r40
            double r11 = (double) r9
            double r3 = r3 / r11
            int r5 = r0.intValue()
            double r11 = (double) r5
            double r3 = r3 / r11
            mil.nga.geopackage.BoundingBox r5 = r6.tileGridBoundingBox
            double r11 = r5.getMaxLatitude()
            mil.nga.geopackage.BoundingBox r5 = r6.tileGridBoundingBox
            double r13 = r5.getMinLatitude()
            double r11 = r11 - r13
            r13 = r42
            double r5 = (double) r13
            double r11 = r11 / r5
            int r5 = r1.intValue()
            double r5 = (double) r5
            double r11 = r11 / r5
            mil.nga.geopackage.tiles.matrix.TileMatrix r5 = new mil.nga.geopackage.tiles.matrix.TileMatrix
            r5.<init>()
            r6 = r36
            r5.setContents(r6)
            long r6 = (long) r8
            r5.setZoomLevel(r6)
            r5.setMatrixWidth(r9)
            r5.setMatrixHeight(r13)
            int r0 = r0.intValue()
            long r6 = (long) r0
            r5.setTileWidth(r6)
            int r0 = r1.intValue()
            long r0 = (long) r0
            r5.setTileHeight(r0)
            r5.setPixelXSize(r3)
            r5.setPixelYSize(r11)
            r0 = r34
            r0.create(r5)
        L_0x017b:
            r17 = r2
            goto L_0x0224
        L_0x017f:
            r8 = r37
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.Integer r1 = java.lang.Integer.valueOf(r37)
            java.lang.String r2 = "zoom_level"
            java.lang.String r1 = r15.buildWhere((java.lang.String) r2, (java.lang.Object) r1)
            r0.append(r1)
            java.lang.String r1 = " AND "
            r0.append(r1)
            long r2 = r38.getMinX()
            java.lang.Long r2 = java.lang.Long.valueOf(r2)
            java.lang.String r3 = "tile_column"
            java.lang.String r4 = ">="
            java.lang.String r2 = r15.buildWhere(r3, r2, r4)
            r0.append(r2)
            r0.append(r1)
            long r6 = r38.getMaxX()
            java.lang.Long r2 = java.lang.Long.valueOf(r6)
            java.lang.String r6 = "<="
            java.lang.String r2 = r15.buildWhere(r3, r2, r6)
            r0.append(r2)
            r0.append(r1)
            long r2 = r38.getMinY()
            java.lang.Long r2 = java.lang.Long.valueOf(r2)
            java.lang.String r3 = "tile_row"
            java.lang.String r2 = r15.buildWhere(r3, r2, r4)
            r0.append(r2)
            r0.append(r1)
            long r1 = r38.getMaxY()
            java.lang.Long r1 = java.lang.Long.valueOf(r1)
            java.lang.String r1 = r15.buildWhere(r3, r1, r6)
            r0.append(r1)
            r1 = 5
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.Integer r2 = java.lang.Integer.valueOf(r37)
            r1[r17] = r2
            long r2 = r38.getMinX()
            java.lang.Long r2 = java.lang.Long.valueOf(r2)
            r1[r5] = r2
            r2 = 2
            long r3 = r38.getMaxX()
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            r1[r2] = r3
            r2 = 3
            long r3 = r38.getMinY()
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            r1[r2] = r3
            r2 = 4
            long r3 = r38.getMaxY()
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            r1[r2] = r3
            java.lang.String[] r1 = r15.buildWhereArgs((java.lang.Object[]) r1)
            java.lang.String r0 = r0.toString()
            r15.delete(r0, r1)
        L_0x0224:
            return r17
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.tiles.TileGenerator.generateTiles(mil.nga.geopackage.tiles.matrix.TileMatrixDao, mil.nga.geopackage.tiles.user.TileDao, mil.nga.geopackage.core.contents.Contents, int, mil.nga.geopackage.tiles.TileGrid, mil.nga.geopackage.tiles.TileGrid, long, long, boolean):int");
    }
}
