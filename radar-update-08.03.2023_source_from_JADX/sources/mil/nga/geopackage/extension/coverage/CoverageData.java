package mil.nga.geopackage.extension.coverage;

import android.graphics.Rect;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.coverage.CoverageDataImage;
import mil.nga.geopackage.projection.Projection;
import mil.nga.geopackage.projection.ProjectionTransform;
import mil.nga.geopackage.tiles.TileBoundingBoxAndroidUtils;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.user.TileCursor;
import mil.nga.geopackage.tiles.user.TileDao;
import mil.nga.geopackage.tiles.user.TileRow;

public abstract class CoverageData<TImage extends CoverageDataImage> extends CoverageDataCore<TImage> {
    protected final TileDao tileDao;

    public abstract TImage createImage(TileRow tileRow);

    public abstract byte[] drawTileData(GriddedTile griddedTile, Double[] dArr, int i, int i2);

    public abstract byte[] drawTileData(GriddedTile griddedTile, Double[][] dArr);

    public abstract double getValue(GriddedTile griddedTile, TileRow tileRow, int i, int i2);

    public abstract Double getValue(GriddedTile griddedTile, byte[] bArr, int i, int i2);

    public abstract Double[] getValues(GriddedTile griddedTile, byte[] bArr);

    public static CoverageData<?> getCoverageData(GeoPackage geoPackage, TileDao tileDao2, Integer num, Integer num2, Projection projection) {
        TileMatrixSet tileMatrixSet = tileDao2.getTileMatrixSet();
        GriddedCoverageDao griddedCoverageDao = geoPackage.getGriddedCoverageDao();
        try {
            GriddedCoverageDataType dataType = (griddedCoverageDao.isTableExists() ? griddedCoverageDao.query(tileMatrixSet) : null).getDataType();
            int i = C11631.f346x849fec31[dataType.ordinal()];
            if (i == 1) {
                return new CoverageDataPng(geoPackage, tileDao2, num, num2, projection);
            }
            if (i == 2) {
                return new CoverageDataTiff(geoPackage, tileDao2, num, num2, projection);
            }
            throw new GeoPackageException("Unsupported Gridded Coverage Data Type: " + dataType);
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to get Gridded Coverage for table name: " + tileMatrixSet.getTableName(), e);
        }
    }

    public static CoverageData<?> getCoverageData(GeoPackage geoPackage, TileDao tileDao2) {
        return getCoverageData(geoPackage, tileDao2, (Integer) null, (Integer) null, tileDao2.getProjection());
    }

    public static CoverageData<?> getCoverageData(GeoPackage geoPackage, TileDao tileDao2, Projection projection) {
        return getCoverageData(geoPackage, tileDao2, (Integer) null, (Integer) null, projection);
    }

    public static CoverageData<?> createTileTableWithMetadata(GeoPackage geoPackage, String str, BoundingBox boundingBox, long j, BoundingBox boundingBox2, long j2, GriddedCoverageDataType griddedCoverageDataType) {
        CoverageData<?> coverageData;
        TileDao tileDao2 = geoPackage.getTileDao(CoverageDataCore.createTileTableWithMetadata(geoPackage, str, boundingBox, j, boundingBox2, j2));
        int i = C11631.f346x849fec31[griddedCoverageDataType.ordinal()];
        if (i == 1) {
            coverageData = new CoverageDataPng(geoPackage, tileDao2);
        } else if (i == 2) {
            coverageData = new CoverageDataTiff(geoPackage, tileDao2);
        } else {
            throw new GeoPackageException("Unsupported Gridded Coverage Data Type: " + griddedCoverageDataType);
        }
        coverageData.getOrCreate();
        return coverageData;
    }

    public CoverageData(GeoPackage geoPackage, TileDao tileDao2, Integer num, Integer num2, Projection projection) {
        super(geoPackage, tileDao2.getTileMatrixSet(), num, num2, projection);
        this.tileDao = tileDao2;
    }

    public TileDao getTileDao() {
        return this.tileDao;
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b5 A[Catch:{ all -> 0x00bf }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public mil.nga.geopackage.extension.coverage.CoverageDataResults getValues(mil.nga.geopackage.extension.coverage.CoverageDataRequest r17, java.lang.Integer r18, java.lang.Integer r19) {
        /*
            r16 = this;
            r8 = r16
            r0 = r17
            mil.nga.geopackage.BoundingBox r1 = r17.getBoundingBox()
            boolean r2 = r8.sameProjection
            r9 = 0
            if (r2 != 0) goto L_0x001c
            mil.nga.geopackage.projection.Projection r2 = r8.requestProjection
            mil.nga.geopackage.projection.Projection r3 = r8.coverageProjection
            mil.nga.geopackage.projection.ProjectionTransform r2 = r2.getTransformation((mil.nga.geopackage.projection.Projection) r3)
            mil.nga.geopackage.BoundingBox r1 = r2.transform((mil.nga.geopackage.BoundingBox) r1)
            r11 = r1
            r10 = r2
            goto L_0x001e
        L_0x001c:
            r11 = r1
            r10 = r9
        L_0x001e:
            r0.setProjectedBoundingBox(r11)
            int[] r1 = mil.nga.geopackage.extension.coverage.CoverageData.C11631.f345x69146fe5
            mil.nga.geopackage.extension.coverage.CoverageDataAlgorithm r2 = r8.algorithm
            int r2 = r2.ordinal()
            r1 = r1[r2]
            r2 = 1
            if (r1 == r2) goto L_0x0030
            r7 = r2
            goto L_0x0032
        L_0x0030:
            r1 = 3
            r7 = r1
        L_0x0032:
            mil.nga.geopackage.extension.coverage.CoverageDataTileMatrixResults r1 = r8.getResults((mil.nga.geopackage.extension.coverage.CoverageDataRequest) r0, (mil.nga.geopackage.BoundingBox) r11, (int) r7)
            if (r1 == 0) goto L_0x00c4
            mil.nga.geopackage.tiles.matrix.TileMatrix r12 = r1.getTileMatrix()
            mil.nga.geopackage.tiles.user.TileCursor r13 = r1.getTileResults()
            if (r18 == 0) goto L_0x0047
            int r1 = r18.intValue()     // Catch:{ all -> 0x00bf }
            goto L_0x004c
        L_0x0047:
            long r1 = r12.getTileWidth()     // Catch:{ all -> 0x00bf }
            int r1 = (int) r1     // Catch:{ all -> 0x00bf }
        L_0x004c:
            r14 = r1
            if (r19 == 0) goto L_0x0054
            int r1 = r19.intValue()     // Catch:{ all -> 0x00bf }
            goto L_0x0059
        L_0x0054:
            long r1 = r12.getTileHeight()     // Catch:{ all -> 0x00bf }
            int r1 = (int) r1     // Catch:{ all -> 0x00bf }
        L_0x0059:
            r15 = r1
            boolean r1 = r8.sameProjection     // Catch:{ all -> 0x00bf }
            if (r1 != 0) goto L_0x008d
            double r1 = r11.getMaxLongitude()     // Catch:{ all -> 0x00bf }
            double r3 = r11.getMinLongitude()     // Catch:{ all -> 0x00bf }
            double r1 = r1 - r3
            double r3 = r12.getPixelXSize()     // Catch:{ all -> 0x00bf }
            double r1 = r1 / r3
            long r1 = java.lang.Math.round(r1)     // Catch:{ all -> 0x00bf }
            int r1 = (int) r1     // Catch:{ all -> 0x00bf }
            if (r1 <= 0) goto L_0x0074
            goto L_0x0075
        L_0x0074:
            r1 = r14
        L_0x0075:
            double r2 = r11.getMaxLatitude()     // Catch:{ all -> 0x00bf }
            double r4 = r11.getMinLatitude()     // Catch:{ all -> 0x00bf }
            double r2 = r2 - r4
            double r4 = r12.getPixelYSize()     // Catch:{ all -> 0x00bf }
            double r2 = r2 / r4
            long r2 = java.lang.Math.round(r2)     // Catch:{ all -> 0x00bf }
            int r2 = (int) r2     // Catch:{ all -> 0x00bf }
            r5 = r1
            if (r2 <= 0) goto L_0x008e
            r6 = r2
            goto L_0x008f
        L_0x008d:
            r5 = r14
        L_0x008e:
            r6 = r15
        L_0x008f:
            r1 = r16
            r2 = r12
            r3 = r13
            r4 = r17
            java.lang.Double[][] r2 = r1.getValues(r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x00bf }
            if (r2 == 0) goto L_0x00b3
            boolean r1 = r8.sameProjection     // Catch:{ all -> 0x00bf }
            if (r1 != 0) goto L_0x00b3
            boolean r1 = r17.isPoint()     // Catch:{ all -> 0x00bf }
            if (r1 != 0) goto L_0x00b3
            mil.nga.geopackage.BoundingBox r5 = r17.getBoundingBox()     // Catch:{ all -> 0x00bf }
            r1 = r16
            r3 = r14
            r4 = r15
            r6 = r10
            r7 = r11
            java.lang.Double[][] r2 = r1.reprojectCoverageData(r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x00bf }
        L_0x00b3:
            if (r2 == 0) goto L_0x00bb
            mil.nga.geopackage.extension.coverage.CoverageDataResults r0 = new mil.nga.geopackage.extension.coverage.CoverageDataResults     // Catch:{ all -> 0x00bf }
            r0.<init>(r2, r12)     // Catch:{ all -> 0x00bf }
            r9 = r0
        L_0x00bb:
            r13.close()
            goto L_0x00c4
        L_0x00bf:
            r0 = move-exception
            r13.close()
            throw r0
        L_0x00c4:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.extension.coverage.CoverageData.getValues(mil.nga.geopackage.extension.coverage.CoverageDataRequest, java.lang.Integer, java.lang.Integer):mil.nga.geopackage.extension.coverage.CoverageDataResults");
    }

    /* renamed from: mil.nga.geopackage.extension.coverage.CoverageData$1 */
    static /* synthetic */ class C11631 {

        /* renamed from: $SwitchMap$mil$nga$geopackage$extension$coverage$CoverageDataAlgorithm */
        static final /* synthetic */ int[] f345x69146fe5;

        /* renamed from: $SwitchMap$mil$nga$geopackage$extension$coverage$GriddedCoverageDataType */
        static final /* synthetic */ int[] f346x849fec31;

        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0039 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x001d */
        static {
            /*
                mil.nga.geopackage.extension.coverage.CoverageDataAlgorithm[] r0 = mil.nga.geopackage.extension.coverage.CoverageDataAlgorithm.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f345x69146fe5 = r0
                r1 = 1
                mil.nga.geopackage.extension.coverage.CoverageDataAlgorithm r2 = mil.nga.geopackage.extension.coverage.CoverageDataAlgorithm.BICUBIC     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = f345x69146fe5     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.geopackage.extension.coverage.CoverageDataAlgorithm r3 = mil.nga.geopackage.extension.coverage.CoverageDataAlgorithm.NEAREST_NEIGHBOR     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r2 = f345x69146fe5     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.geopackage.extension.coverage.CoverageDataAlgorithm r3 = mil.nga.geopackage.extension.coverage.CoverageDataAlgorithm.BILINEAR     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r4 = 3
                r2[r3] = r4     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                mil.nga.geopackage.extension.coverage.GriddedCoverageDataType[] r2 = mil.nga.geopackage.extension.coverage.GriddedCoverageDataType.values()
                int r2 = r2.length
                int[] r2 = new int[r2]
                f346x849fec31 = r2
                mil.nga.geopackage.extension.coverage.GriddedCoverageDataType r3 = mil.nga.geopackage.extension.coverage.GriddedCoverageDataType.INTEGER     // Catch:{ NoSuchFieldError -> 0x0039 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0039 }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x0039 }
            L_0x0039:
                int[] r1 = f346x849fec31     // Catch:{ NoSuchFieldError -> 0x0043 }
                mil.nga.geopackage.extension.coverage.GriddedCoverageDataType r2 = mil.nga.geopackage.extension.coverage.GriddedCoverageDataType.FLOAT     // Catch:{ NoSuchFieldError -> 0x0043 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0043 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0043 }
            L_0x0043:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.extension.coverage.CoverageData.C11631.<clinit>():void");
        }
    }

    public CoverageDataResults getValuesUnbounded(CoverageDataRequest coverageDataRequest) {
        BoundingBox boundingBox;
        ProjectionTransform projectionTransform;
        BoundingBox boundingBox2 = coverageDataRequest.getBoundingBox();
        CoverageDataResults coverageDataResults = null;
        if (!this.sameProjection) {
            ProjectionTransform transformation = this.requestProjection.getTransformation(this.coverageProjection);
            boundingBox = transformation.transform(boundingBox2);
            projectionTransform = transformation;
        } else {
            boundingBox = boundingBox2;
            projectionTransform = null;
        }
        coverageDataRequest.setProjectedBoundingBox(boundingBox);
        CoverageDataTileMatrixResults results = getResults(coverageDataRequest, boundingBox);
        if (results != null) {
            TileMatrix tileMatrix = results.getTileMatrix();
            TileCursor tileResults = results.getTileResults();
            try {
                Double[][] valuesUnbounded = getValuesUnbounded(tileMatrix, tileResults, coverageDataRequest);
                if (valuesUnbounded != null && !this.sameProjection && !coverageDataRequest.isPoint()) {
                    valuesUnbounded = reprojectCoverageData(valuesUnbounded, valuesUnbounded[0].length, valuesUnbounded.length, coverageDataRequest.getBoundingBox(), projectionTransform, boundingBox);
                }
                if (valuesUnbounded != null) {
                    coverageDataResults = new CoverageDataResults(valuesUnbounded, tileMatrix);
                }
            } finally {
                tileResults.close();
            }
        }
        return coverageDataResults;
    }

    private CoverageDataTileMatrixResults getResults(CoverageDataRequest coverageDataRequest, BoundingBox boundingBox) {
        return getResults(coverageDataRequest, boundingBox, 0);
    }

    private CoverageDataTileMatrixResults getResults(CoverageDataRequest coverageDataRequest, BoundingBox boundingBox, int i) {
        TileMatrix tileMatrix = getTileMatrix(coverageDataRequest);
        if (tileMatrix == null) {
            return null;
        }
        CoverageDataTileMatrixResults results = getResults(boundingBox, tileMatrix, i);
        return results == null ? getResultsZoom(boundingBox, tileMatrix, i) : results;
    }

    private CoverageDataTileMatrixResults getResults(BoundingBox boundingBox, TileMatrix tileMatrix, int i) {
        TileCursor retrieveSortedTileResults = retrieveSortedTileResults(padBoundingBox(tileMatrix, boundingBox, i), tileMatrix);
        if (retrieveSortedTileResults != null) {
            if (retrieveSortedTileResults.getCount() > 0) {
                return new CoverageDataTileMatrixResults(tileMatrix, retrieveSortedTileResults);
            }
            retrieveSortedTileResults.close();
        }
        return null;
    }

    private CoverageDataTileMatrixResults getResultsZoom(BoundingBox boundingBox, TileMatrix tileMatrix, int i) {
        CoverageDataTileMatrixResults resultsZoomIn = (!this.zoomIn || !this.zoomInBeforeOut) ? null : getResultsZoomIn(boundingBox, tileMatrix, i);
        if (resultsZoomIn == null && this.zoomOut) {
            resultsZoomIn = getResultsZoomOut(boundingBox, tileMatrix, i);
        }
        return (resultsZoomIn != null || !this.zoomIn || this.zoomInBeforeOut) ? resultsZoomIn : getResultsZoomIn(boundingBox, tileMatrix, i);
    }

    private CoverageDataTileMatrixResults getResultsZoomIn(BoundingBox boundingBox, TileMatrix tileMatrix, int i) {
        long zoomLevel = tileMatrix.getZoomLevel() + 1;
        CoverageDataTileMatrixResults coverageDataTileMatrixResults = null;
        while (zoomLevel <= this.tileDao.getMaxZoom() && ((r4 = this.tileDao.getTileMatrix(zoomLevel)) == null || (coverageDataTileMatrixResults = getResults(boundingBox, r4, i)) == null)) {
            zoomLevel++;
        }
        return coverageDataTileMatrixResults;
    }

    private CoverageDataTileMatrixResults getResultsZoomOut(BoundingBox boundingBox, TileMatrix tileMatrix, int i) {
        long zoomLevel = tileMatrix.getZoomLevel() - 1;
        CoverageDataTileMatrixResults coverageDataTileMatrixResults = null;
        while (zoomLevel >= this.tileDao.getMinZoom() && ((r4 = this.tileDao.getTileMatrix(zoomLevel)) == null || (coverageDataTileMatrixResults = getResults(boundingBox, r4, i)) == null)) {
            zoomLevel--;
        }
        return coverageDataTileMatrixResults;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v4, resolved type: java.lang.Double[][]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x02e3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.Double[][] getValues(mil.nga.geopackage.tiles.matrix.TileMatrix r42, mil.nga.geopackage.tiles.user.TileCursor r43, mil.nga.geopackage.extension.coverage.CoverageDataRequest r44, int r45, int r46, int r47) {
        /*
            r41 = this;
            r14 = r41
            r15 = r45
            r13 = r46
            r12 = r47
            r16 = 0
            r0 = r16
            java.lang.Double[][] r0 = (java.lang.Double[][]) r0
            r0 = -1
            r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r4 = r16
            r5 = r4
            r6 = r5
            r7 = r6
        L_0x001a:
            boolean r8 = r43.moveToNext()
            if (r8 == 0) goto L_0x0341
            mil.nga.geopackage.user.UserRow r8 = r43.getRow()
            mil.nga.geopackage.tiles.user.TileRow r8 = (mil.nga.geopackage.tiles.user.TileRow) r8
            long r23 = r8.getTileRow()
            long r25 = r8.getTileColumn()
            int r0 = (r23 > r0 ? 1 : (r23 == r0 ? 0 : -1))
            if (r0 <= 0) goto L_0x003c
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r11 = r0
            r10 = r5
            r7 = r16
            goto L_0x003e
        L_0x003c:
            r11 = r5
            r10 = r6
        L_0x003e:
            r0 = 1
            if (r10 == 0) goto L_0x005d
            long r5 = r25 - r0
            java.lang.Long r5 = java.lang.Long.valueOf(r5)
            java.lang.Object r5 = r10.get(r5)
            java.lang.Double[][] r5 = (java.lang.Double[][]) r5
            java.lang.Long r6 = java.lang.Long.valueOf(r25)
            java.lang.Object r6 = r10.get(r6)
            java.lang.Double[][] r6 = (java.lang.Double[][]) r6
            r27 = r5
            r28 = r6
            goto L_0x0061
        L_0x005d:
            r27 = r16
            r28 = r27
        L_0x0061:
            int r5 = (r25 > r2 ? 1 : (r25 == r2 ? 0 : -1))
            if (r5 < 0) goto L_0x006e
            long r2 = r2 + r0
            int r0 = (r25 > r2 ? 1 : (r25 == r2 ? 0 : -1))
            if (r0 == 0) goto L_0x006b
            goto L_0x006e
        L_0x006b:
            r29 = r7
            goto L_0x0070
        L_0x006e:
            r29 = r16
        L_0x0070:
            mil.nga.geopackage.BoundingBox r0 = r14.coverageBoundingBox
            r17 = r0
            r18 = r42
            r19 = r25
            r21 = r23
            mil.nga.geopackage.BoundingBox r0 = mil.nga.geopackage.tiles.TileBoundingBoxUtils.getBoundingBox((mil.nga.geopackage.BoundingBox) r17, (mil.nga.geopackage.tiles.matrix.TileMatrix) r18, (long) r19, (long) r21)
            r9 = r44
            mil.nga.geopackage.BoundingBox r1 = r9.overlap(r0)
            long r2 = r8.getId()
            mil.nga.geopackage.extension.coverage.GriddedTile r7 = r14.getGriddedTile(r2)
            mil.nga.geopackage.extension.coverage.CoverageDataImage r8 = r14.createImage(r8)
            if (r1 == 0) goto L_0x02a0
            long r17 = r42.getTileWidth()
            long r19 = r42.getTileHeight()
            r21 = r0
            r22 = r1
            android.graphics.RectF r2 = mil.nga.geopackage.tiles.TileBoundingBoxAndroidUtils.getFloatRectangle(r17, r19, r21, r22)
            mil.nga.geopackage.BoundingBox r0 = r44.getProjectedBoundingBox()
            boolean r0 = r0.equals(r1)
            r5 = 0
            if (r0 == 0) goto L_0x00c1
            boolean r0 = r44.isPoint()
            if (r0 == 0) goto L_0x00b9
            android.graphics.RectF r0 = new android.graphics.RectF
            r0.<init>(r5, r5, r5, r5)
            goto L_0x00d2
        L_0x00b9:
            android.graphics.RectF r0 = new android.graphics.RectF
            float r1 = (float) r15
            float r3 = (float) r13
            r0.<init>(r5, r5, r1, r3)
            goto L_0x00d2
        L_0x00c1:
            long r5 = (long) r15
            r3 = r1
            long r0 = (long) r13
            mil.nga.geopackage.BoundingBox r21 = r44.getProjectedBoundingBox()
            r17 = r5
            r19 = r0
            r22 = r3
            android.graphics.RectF r0 = mil.nga.geopackage.tiles.TileBoundingBoxAndroidUtils.getFloatRectangle(r17, r19, r21, r22)
        L_0x00d2:
            r6 = r0
            boolean r0 = mil.nga.geopackage.tiles.TileBoundingBoxAndroidUtils.isValidAllowEmpty((android.graphics.RectF) r2)
            if (r0 == 0) goto L_0x02a0
            boolean r0 = mil.nga.geopackage.tiles.TileBoundingBoxAndroidUtils.isValidAllowEmpty((android.graphics.RectF) r6)
            if (r0 == 0) goto L_0x02a0
            if (r4 != 0) goto L_0x00f5
            r0 = 2
            int[] r1 = new int[r0]
            r0 = 1
            r1[r0] = r15
            r0 = 0
            r1[r0] = r13
            java.lang.Class<java.lang.Double> r0 = java.lang.Double.class
            java.lang.Object r0 = java.lang.reflect.Array.newInstance(r0, r1)
            java.lang.Double[][] r0 = (java.lang.Double[][]) r0
            r17 = r0
            goto L_0x00f7
        L_0x00f5:
            r17 = r4
        L_0x00f7:
            float r0 = r6.right
            float r1 = r6.left
            float r0 = r0 - r1
            float r1 = r6.bottom
            float r3 = r6.top
            float r1 = r1 - r3
            float r3 = r2.right
            float r4 = r2.left
            float r3 = r3 - r4
            float r4 = r2.bottom
            float r5 = r2.top
            float r4 = r4 - r5
            r33 = 0
            int r5 = (r0 > r33 ? 1 : (r0 == r33 ? 0 : -1))
            r18 = 1056964608(0x3f000000, float:0.5)
            if (r5 != 0) goto L_0x0118
            r3 = r33
            r19 = r3
            goto L_0x011e
        L_0x0118:
            float r0 = r3 / r0
            float r3 = r18 / r0
            r19 = r0
        L_0x011e:
            int r0 = (r1 > r33 ? 1 : (r1 == r33 ? 0 : -1))
            if (r0 != 0) goto L_0x0125
            r5 = r33
            goto L_0x012c
        L_0x0125:
            float r5 = r4 / r1
            float r0 = r18 / r5
            r33 = r5
            r5 = r0
        L_0x012c:
            float r0 = (float) r12
            float r3 = r3 * r0
            float r5 = r5 * r0
            float r0 = r6.top
            float r0 = r0 - r5
            double r0 = (double) r0
            double r0 = java.lang.Math.floor(r0)
            int r0 = (int) r0
            float r1 = r6.bottom
            float r1 = r1 + r5
            double r4 = (double) r1
            double r4 = java.lang.Math.ceil(r4)
            int r1 = (int) r4
            float r4 = r6.left
            float r4 = r4 - r3
            double r4 = (double) r4
            double r4 = java.lang.Math.floor(r4)
            int r4 = (int) r4
            float r5 = r6.right
            float r5 = r5 + r3
            r18 = r10
            double r9 = (double) r5
            double r9 = java.lang.Math.ceil(r9)
            int r3 = (int) r9
            r5 = 0
            int r0 = java.lang.Math.max(r0, r5)
            int r20 = java.lang.Math.max(r4, r5)
            int r4 = r13 + -1
            int r10 = java.lang.Math.min(r1, r4)
            int r1 = r15 + -1
            int r9 = java.lang.Math.min(r3, r1)
            r4 = r0
        L_0x016b:
            if (r4 > r10) goto L_0x0293
            r3 = r20
        L_0x016f:
            if (r3 > r9) goto L_0x0275
            int[] r0 = mil.nga.geopackage.extension.coverage.CoverageData.C11631.f345x69146fe5
            mil.nga.geopackage.extension.coverage.CoverageDataAlgorithm r1 = r14.algorithm
            int r1 = r1.ordinal()
            r0 = r0[r1]
            r1 = 1
            if (r0 == r1) goto L_0x021f
            r21 = r9
            r9 = 2
            if (r0 == r9) goto L_0x01e2
            r1 = 3
            if (r0 != r1) goto L_0x01c9
            float r1 = r6.top
            float r0 = r6.left
            float r12 = r2.top
            float r13 = r2.left
            r22 = r0
            r0 = r41
            r30 = r1
            r31 = 1
            r1 = r7
            r15 = r2
            r2 = r8
            r32 = r31
            r31 = r3
            r3 = r29
            r34 = r4
            r4 = r27
            r35 = r5
            r5 = r28
            r9 = r6
            r6 = r34
            r36 = r7
            r7 = r31
            r37 = r8
            r8 = r19
            r38 = r21
            r21 = r15
            r15 = r9
            r9 = r33
            r39 = r10
            r10 = r30
            r40 = r11
            r11 = r22
            java.lang.Double r0 = r0.getBilinearInterpolationValue(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)
            r14 = r21
            goto L_0x0253
        L_0x01c9:
            java.lang.UnsupportedOperationException r0 = new java.lang.UnsupportedOperationException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Algorithm is not supported: "
            r1.append(r2)
            mil.nga.geopackage.extension.coverage.CoverageDataAlgorithm r2 = r14.algorithm
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x01e2:
            r32 = r1
            r31 = r3
            r34 = r4
            r35 = r5
            r15 = r6
            r36 = r7
            r37 = r8
            r39 = r10
            r40 = r11
            r38 = r21
            r21 = r2
            float r10 = r15.top
            float r11 = r15.left
            r13 = r21
            float r12 = r13.top
            float r9 = r13.left
            r0 = r41
            r1 = r36
            r2 = r37
            r3 = r29
            r4 = r27
            r5 = r28
            r6 = r34
            r7 = r31
            r8 = r19
            r21 = r9
            r9 = r33
            r14 = r13
            r13 = r21
            java.lang.Double r0 = r0.getNearestNeighborValue(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)
            goto L_0x0253
        L_0x021f:
            r32 = r1
            r14 = r2
            r31 = r3
            r34 = r4
            r35 = r5
            r15 = r6
            r36 = r7
            r37 = r8
            r38 = r9
            r39 = r10
            r40 = r11
            float r10 = r15.top
            float r11 = r15.left
            float r12 = r14.top
            float r13 = r14.left
            r0 = r41
            r1 = r36
            r2 = r37
            r3 = r29
            r4 = r27
            r5 = r28
            r6 = r34
            r7 = r31
            r8 = r19
            r9 = r33
            java.lang.Double r0 = r0.getBicubicInterpolationValue(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)
        L_0x0253:
            if (r0 == 0) goto L_0x0259
            r1 = r17[r34]
            r1[r31] = r0
        L_0x0259:
            int r3 = r31 + 1
            r13 = r46
            r12 = r47
            r2 = r14
            r6 = r15
            r4 = r34
            r5 = r35
            r7 = r36
            r8 = r37
            r9 = r38
            r10 = r39
            r11 = r40
            r14 = r41
            r15 = r45
            goto L_0x016f
        L_0x0275:
            r14 = r2
            r34 = r4
            r35 = r5
            r15 = r6
            r36 = r7
            r37 = r8
            r38 = r9
            r39 = r10
            r40 = r11
            r32 = 1
            int r4 = r34 + 1
            r13 = r46
            r12 = r47
            r14 = r41
            r15 = r45
            goto L_0x016b
        L_0x0293:
            r35 = r5
            r36 = r7
            r37 = r8
            r40 = r11
            r32 = 1
            r4 = r17
            goto L_0x02ac
        L_0x02a0:
            r36 = r7
            r37 = r8
            r18 = r10
            r40 = r11
            r32 = 1
            r35 = 0
        L_0x02ac:
            long r0 = r42.getTileHeight()
            int r0 = (int) r0
            r1 = 2
            int[] r2 = new int[r1]
            r2[r32] = r0
            r0 = r47
            r2[r35] = r0
            java.lang.Class<java.lang.Double> r3 = java.lang.Double.class
            java.lang.Object r2 = java.lang.reflect.Array.newInstance(r3, r2)
            r7 = r2
            java.lang.Double[][] r7 = (java.lang.Double[][]) r7
            long r2 = r42.getTileWidth()
            int r2 = (int) r2
            int[] r1 = new int[r1]
            r1[r32] = r2
            r1[r35] = r0
            java.lang.Class<java.lang.Double> r2 = java.lang.Double.class
            java.lang.Object r1 = java.lang.reflect.Array.newInstance(r2, r1)
            java.lang.Double[][] r1 = (java.lang.Double[][]) r1
            java.lang.Long r2 = java.lang.Long.valueOf(r25)
            r5 = r40
            r5.put(r2, r1)
            r2 = r35
        L_0x02e1:
            if (r2 >= r0) goto L_0x0332
            long r8 = r42.getTileWidth()
            int r3 = (int) r8
            int r3 = r3 - r2
            int r3 = r3 + -1
            r6 = r35
        L_0x02ed:
            long r8 = (long) r6
            long r10 = r42.getTileHeight()
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 >= 0) goto L_0x0307
            r8 = r41
            r9 = r36
            r10 = r37
            java.lang.Double r11 = r8.getValue(r9, r10, r3, r6)
            r12 = r7[r2]
            r12[r6] = r11
            int r6 = r6 + 1
            goto L_0x02ed
        L_0x0307:
            r8 = r41
            r9 = r36
            r10 = r37
            long r11 = r42.getTileHeight()
            int r3 = (int) r11
            int r3 = r3 - r2
            int r3 = r3 + -1
            r6 = r35
        L_0x0317:
            long r11 = (long) r6
            long r13 = r42.getTileWidth()
            int r11 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r11 >= 0) goto L_0x032b
            java.lang.Double r11 = r8.getValue(r9, r10, r6, r3)
            r12 = r1[r2]
            r12[r6] = r11
            int r6 = r6 + 1
            goto L_0x0317
        L_0x032b:
            int r2 = r2 + 1
            r36 = r9
            r37 = r10
            goto L_0x02e1
        L_0x0332:
            r14 = r41
            r15 = r45
            r13 = r46
            r12 = r0
            r6 = r18
            r0 = r23
            r2 = r25
            goto L_0x001a
        L_0x0341:
            r8 = r14
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.extension.coverage.CoverageData.getValues(mil.nga.geopackage.tiles.matrix.TileMatrix, mil.nga.geopackage.tiles.user.TileCursor, mil.nga.geopackage.extension.coverage.CoverageDataRequest, int, int, int):java.lang.Double[][]");
    }

    private Double[][] getValuesUnbounded(TileMatrix tileMatrix, TileCursor tileCursor, CoverageDataRequest coverageDataRequest) {
        long j;
        long j2;
        TreeMap treeMap = new TreeMap();
        Long l = null;
        Long l2 = null;
        Long l3 = null;
        Long l4 = null;
        int i = 0;
        while (tileCursor.moveToNext()) {
            TileRow tileRow = (TileRow) tileCursor.getRow();
            BoundingBox boundingBox = TileBoundingBoxUtils.getBoundingBox(this.coverageBoundingBox, tileMatrix, tileRow.getTileColumn(), tileRow.getTileRow());
            BoundingBox overlap = coverageDataRequest.overlap(boundingBox);
            if (overlap != null) {
                Rect rectangle = TileBoundingBoxAndroidUtils.getRectangle(tileMatrix.getTileWidth(), tileMatrix.getTileHeight(), boundingBox, overlap);
                if (TileBoundingBoxAndroidUtils.isValidAllowEmpty(rectangle)) {
                    int min = Math.min(rectangle.top, ((int) tileMatrix.getTileHeight()) - 1);
                    int min2 = Math.min(rectangle.bottom, ((int) tileMatrix.getTileHeight()) - 1);
                    Long l5 = l;
                    int min3 = Math.min(rectangle.left, ((int) tileMatrix.getTileWidth()) - 1);
                    int min4 = Math.min(rectangle.right, ((int) tileMatrix.getTileWidth()) - 1);
                    GriddedTile griddedTile = getGriddedTile(tileRow.getId());
                    CoverageDataImage createImage = createImage(tileRow);
                    int[] iArr = new int[2];
                    iArr[1] = (min4 - min3) + 1;
                    iArr[0] = (min2 - min) + 1;
                    Double[][] dArr = (Double[][]) Array.newInstance(Double.class, iArr);
                    Map map = (Map) treeMap.get(Long.valueOf(tileRow.getTileRow()));
                    if (map == null) {
                        map = new TreeMap();
                        treeMap.put(Long.valueOf(tileRow.getTileRow()), map);
                    }
                    int i2 = min;
                    while (i2 <= min2) {
                        int i3 = min2;
                        for (int i4 = min3; i4 <= min4; i4++) {
                            dArr[i2 - min][i4 - min3] = getValue(griddedTile, createImage, i4, i2);
                        }
                        i2++;
                        min2 = i3;
                    }
                    map.put(Long.valueOf(tileRow.getTileColumn()), dArr);
                    i++;
                    Long valueOf = Long.valueOf(l5 == null ? tileRow.getTileRow() : Math.min(l5.longValue(), tileRow.getTileRow()));
                    Long valueOf2 = Long.valueOf(l2 == null ? tileRow.getTileRow() : Math.max(l2.longValue(), tileRow.getTileRow()));
                    if (l3 == null) {
                        j = tileRow.getTileColumn();
                    } else {
                        j = Math.min(l3.longValue(), tileRow.getTileColumn());
                    }
                    Long valueOf3 = Long.valueOf(j);
                    if (l4 == null) {
                        j2 = tileRow.getTileColumn();
                    } else {
                        j2 = Math.max(l4.longValue(), tileRow.getTileColumn());
                    }
                    l4 = Long.valueOf(j2);
                    l3 = valueOf3;
                    l2 = valueOf2;
                    l = valueOf;
                }
            }
            l = l;
        }
        return formatUnboundedResults(tileMatrix, treeMap, i, l.longValue(), l2.longValue(), l3.longValue(), l4.longValue());
    }

    private TileMatrix getTileMatrix(CoverageDataRequest coverageDataRequest) {
        if (coverageDataRequest.overlap(this.coverageBoundingBox) != null) {
            BoundingBox projectedBoundingBox = coverageDataRequest.getProjectedBoundingBox();
            Long closestZoomLevel = this.tileDao.getClosestZoomLevel(projectedBoundingBox.getMaxLongitude() - projectedBoundingBox.getMinLongitude(), projectedBoundingBox.getMaxLatitude() - projectedBoundingBox.getMinLatitude());
            if (closestZoomLevel != null) {
                return this.tileDao.getTileMatrix(closestZoomLevel.longValue());
            }
        }
        return null;
    }

    private TileCursor retrieveSortedTileResults(BoundingBox boundingBox, TileMatrix tileMatrix) {
        if (tileMatrix == null) {
            return null;
        }
        return this.tileDao.queryByTileGrid(TileBoundingBoxUtils.getTileGrid(this.coverageBoundingBox, tileMatrix.getMatrixWidth(), tileMatrix.getMatrixHeight(), boundingBox), tileMatrix.getZoomLevel(), "tile_row,tile_column");
    }

    public double getValue(TileRow tileRow, int i, int i2) {
        return getValue(getGriddedTile(tileRow.getId()), tileRow, i, i2);
    }
}
