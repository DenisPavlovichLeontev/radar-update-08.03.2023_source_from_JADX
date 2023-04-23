package mil.nga.geopackage.tiles.user;

import androidx.collection.LongSparseArray;
import com.j256.ormlite.stmt.query.SimpleComparison;
import java.util.HashMap;
import java.util.List;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.projection.ProjectionFactory;
import mil.nga.geopackage.tiles.TileBoundingBoxUtils;
import mil.nga.geopackage.tiles.TileGrid;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.user.UserDao;

public class TileDao extends UserDao<TileColumn, TileTable, TileRow, TileCursor> {
    private final double[] heights;
    private final long maxZoom;
    private final long minZoom;
    private final TileConnection tileDb;
    private final List<TileMatrix> tileMatrices;
    private final TileMatrixSet tileMatrixSet;
    private final double[] widths;
    private final LongSparseArray<TileMatrix> zoomLevelToTileMatrix = new LongSparseArray<>();

    public TileDao(String str, GeoPackageConnection geoPackageConnection, TileConnection tileConnection, TileMatrixSet tileMatrixSet2, List<TileMatrix> list, TileTable tileTable) {
        super(str, geoPackageConnection, tileConnection, tileTable);
        this.tileDb = tileConnection;
        this.tileMatrixSet = tileMatrixSet2;
        this.tileMatrices = list;
        this.widths = new double[list.size()];
        this.heights = new double[list.size()];
        this.projection = ProjectionFactory.getProjection(tileMatrixSet2.getSrs());
        if (!list.isEmpty()) {
            this.minZoom = list.get(0).getZoomLevel();
            this.maxZoom = list.get(list.size() - 1).getZoomLevel();
        } else {
            this.minZoom = 0;
            this.maxZoom = 0;
        }
        for (int i = 0; i < list.size(); i++) {
            TileMatrix tileMatrix = list.get(i);
            this.zoomLevelToTileMatrix.put(tileMatrix.getZoomLevel(), tileMatrix);
            this.widths[(list.size() - i) - 1] = tileMatrix.getPixelXSize() * ((double) tileMatrix.getTileWidth());
            this.heights[(list.size() - i) - 1] = tileMatrix.getPixelYSize() * ((double) tileMatrix.getTileHeight());
        }
        if (tileMatrixSet2.getContents() == null) {
            StringBuilder sb = new StringBuilder();
            Class<TileMatrixSet> cls = TileMatrixSet.class;
            sb.append("TileMatrixSet");
            sb.append(" ");
            sb.append(tileMatrixSet2.getId());
            sb.append(" has null ");
            Class<Contents> cls2 = Contents.class;
            sb.append("Contents");
            throw new GeoPackageException(sb.toString());
        } else if (tileMatrixSet2.getSrs() == null) {
            StringBuilder sb2 = new StringBuilder();
            Class<TileMatrixSet> cls3 = TileMatrixSet.class;
            sb2.append("TileMatrixSet");
            sb2.append(" ");
            sb2.append(tileMatrixSet2.getId());
            sb2.append(" has null ");
            Class<SpatialReferenceSystem> cls4 = SpatialReferenceSystem.class;
            sb2.append("SpatialReferenceSystem");
            throw new GeoPackageException(sb2.toString());
        }
    }

    public BoundingBox getBoundingBox() {
        return this.tileMatrixSet.getBoundingBox();
    }

    public BoundingBox getBoundingBox(long j) {
        TileGrid queryForTileGrid;
        TileMatrix tileMatrix = getTileMatrix(j);
        if (tileMatrix == null || (queryForTileGrid = queryForTileGrid(j)) == null) {
            return null;
        }
        return TileBoundingBoxUtils.getBoundingBox(getBoundingBox(), tileMatrix, queryForTileGrid);
    }

    public TileGrid getTileGrid(long j) {
        TileMatrix tileMatrix = getTileMatrix(j);
        if (tileMatrix != null) {
            return new TileGrid(0, 0, tileMatrix.getMatrixWidth() - 1, tileMatrix.getMatrixHeight() - 1);
        }
        return null;
    }

    public void adjustTileMatrixLengths() {
        TileDaoUtils.adjustTileMatrixLengths(this.tileMatrixSet, this.tileMatrices);
    }

    public TileRow newRow() {
        return new TileRow((TileTable) getTable());
    }

    public TileConnection getTileDb() {
        return this.tileDb;
    }

    public TileMatrixSet getTileMatrixSet() {
        return this.tileMatrixSet;
    }

    public List<TileMatrix> getTileMatrices() {
        return this.tileMatrices;
    }

    public TileMatrix getTileMatrix(long j) {
        return this.zoomLevelToTileMatrix.get(j);
    }

    public long getMinZoom() {
        return this.minZoom;
    }

    public long getMaxZoom() {
        return this.maxZoom;
    }

    public TileRow queryForTile(long j, long j2, long j3) {
        HashMap hashMap = new HashMap();
        hashMap.put("tile_column", Long.valueOf(j));
        hashMap.put("tile_row", Long.valueOf(j2));
        hashMap.put("zoom_level", Long.valueOf(j3));
        TileCursor tileCursor = (TileCursor) queryForFieldValues(hashMap);
        try {
            return tileCursor.moveToNext() ? (TileRow) tileCursor.getRow() : null;
        } finally {
            tileCursor.close();
        }
    }

    public TileCursor queryForTile(long j) {
        return (TileCursor) queryForEq("zoom_level", (Object) Long.valueOf(j));
    }

    public TileCursor queryForTileDescending(long j) {
        return (TileCursor) queryForEq("zoom_level", Long.valueOf(j), (String) null, (String) null, "tile_row DESC, tile_column DESC");
    }

    public TileCursor queryForTilesInColumn(long j, long j2) {
        HashMap hashMap = new HashMap();
        hashMap.put("tile_column", Long.valueOf(j));
        hashMap.put("zoom_level", Long.valueOf(j2));
        return (TileCursor) queryForFieldValues(hashMap);
    }

    public TileCursor queryForTilesInRow(long j, long j2) {
        HashMap hashMap = new HashMap();
        hashMap.put("tile_row", Long.valueOf(j));
        hashMap.put("zoom_level", Long.valueOf(j2));
        return (TileCursor) queryForFieldValues(hashMap);
    }

    public Long getZoomLevel(double d) {
        return TileDaoUtils.getZoomLevel(this.widths, this.heights, this.tileMatrices, d);
    }

    public Long getZoomLevel(double d, double d2) {
        return TileDaoUtils.getZoomLevel(this.widths, this.heights, this.tileMatrices, d, d2);
    }

    public Long getClosestZoomLevel(double d) {
        return TileDaoUtils.getClosestZoomLevel(this.widths, this.heights, this.tileMatrices, d);
    }

    public Long getClosestZoomLevel(double d, double d2) {
        return TileDaoUtils.getClosestZoomLevel(this.widths, this.heights, this.tileMatrices, d, d2);
    }

    public TileCursor queryByTileGrid(TileGrid tileGrid, long j) {
        return queryByTileGrid(tileGrid, j, (String) null);
    }

    public TileCursor queryByTileGrid(TileGrid tileGrid, long j, String str) {
        if (tileGrid == null) {
            return null;
        }
        return (TileCursor) query(buildWhere("zoom_level", (Object) Long.valueOf(j)) + " AND " + buildWhere("tile_column", Long.valueOf(tileGrid.getMinX()), SimpleComparison.GREATER_THAN_EQUAL_TO_OPERATION) + " AND " + buildWhere("tile_column", Long.valueOf(tileGrid.getMaxX()), SimpleComparison.LESS_THAN_EQUAL_TO_OPERATION) + " AND " + buildWhere("tile_row", Long.valueOf(tileGrid.getMinY()), SimpleComparison.GREATER_THAN_EQUAL_TO_OPERATION) + " AND " + buildWhere("tile_row", Long.valueOf(tileGrid.getMaxY()), SimpleComparison.LESS_THAN_EQUAL_TO_OPERATION), buildWhereArgs(new Object[]{Long.valueOf(j), Long.valueOf(tileGrid.getMinX()), Long.valueOf(tileGrid.getMaxX()), Long.valueOf(tileGrid.getMinY()), Long.valueOf(tileGrid.getMaxY())}), (String) null, (String) null, str);
    }

    public TileGrid queryForTileGrid(long j) {
        String buildWhere = buildWhere("zoom_level", (Object) Long.valueOf(j));
        String[] buildWhereArgs = buildWhereArgs(new Object[]{Long.valueOf(j)});
        Integer min = min("tile_column", buildWhere, buildWhereArgs);
        Integer max = max("tile_column", buildWhere, buildWhereArgs);
        Integer min2 = min("tile_row", buildWhere, buildWhereArgs);
        Integer max2 = max("tile_row", buildWhere, buildWhereArgs);
        if (min == null || max == null || min2 == null || max2 == null) {
            return null;
        }
        return new TileGrid((long) min.intValue(), (long) min2.intValue(), (long) max.intValue(), (long) max2.intValue());
    }

    public int deleteTile(long j, long j2, long j3) {
        return delete(buildWhere("zoom_level", (Object) Long.valueOf(j3)) + " AND " + buildWhere("tile_column", (Object) Long.valueOf(j)) + " AND " + buildWhere("tile_row", (Object) Long.valueOf(j2)), buildWhereArgs(new Object[]{Long.valueOf(j3), Long.valueOf(j), Long.valueOf(j2)}));
    }

    public int count(long j) {
        return count(buildWhere("zoom_level", (Object) Long.valueOf(j)), buildWhereArgs((Object) Long.valueOf(j)));
    }

    public double getMaxLength() {
        return TileDaoUtils.getMaxLength(this.widths, this.heights);
    }

    public double getMinLength() {
        return TileDaoUtils.getMinLength(this.widths, this.heights);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x004f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isGoogleTiles() {
        /*
            r8 = this;
            mil.nga.geopackage.tiles.matrixset.TileMatrixSet r0 = r8.tileMatrixSet
            mil.nga.geopackage.BoundingBox r0 = r0.getBoundingBox()
            mil.nga.geopackage.projection.Projection r1 = r8.projection
            r2 = 4326(0x10e6, double:2.1373E-320)
            mil.nga.geopackage.projection.ProjectionTransform r1 = r1.getTransformation((long) r2)
            mil.nga.geopackage.BoundingBox r0 = r1.transform((mil.nga.geopackage.BoundingBox) r0)
            double r1 = r0.getMinLatitude()
            r3 = -4587686678794778154(0xc0554345b1a549d6, double:-85.05112877980659)
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            r2 = 0
            if (r1 > 0) goto L_0x0071
            double r3 = r0.getMaxLatitude()
            r5 = 4635685358059997655(0x40554345b1a549d7, double:85.0511287798066)
            int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r1 < 0) goto L_0x0071
            double r3 = r0.getMinLongitude()
            double r5 = mil.nga.geopackage.projection.ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH
            double r5 = -r5
            int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r1 > 0) goto L_0x0071
            double r0 = r0.getMaxLongitude()
            double r3 = mil.nga.geopackage.projection.ProjectionConstants.WGS84_HALF_WORLD_LON_WIDTH
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 < 0) goto L_0x0071
            r0 = 1
            java.util.List<mil.nga.geopackage.tiles.matrix.TileMatrix> r1 = r8.tileMatrices
            java.util.Iterator r1 = r1.iterator()
        L_0x0049:
            boolean r3 = r1.hasNext()
            if (r3 == 0) goto L_0x0070
            java.lang.Object r3 = r1.next()
            mil.nga.geopackage.tiles.matrix.TileMatrix r3 = (mil.nga.geopackage.tiles.matrix.TileMatrix) r3
            long r4 = r3.getZoomLevel()
            int r4 = (int) r4
            int r4 = mil.nga.geopackage.tiles.TileBoundingBoxUtils.tilesPerSide(r4)
            long r4 = (long) r4
            long r6 = r3.getMatrixWidth()
            int r6 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r6 != 0) goto L_0x0071
            long r6 = r3.getMatrixHeight()
            int r3 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r3 == 0) goto L_0x0049
            goto L_0x0071
        L_0x0070:
            r2 = r0
        L_0x0071:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.tiles.user.TileDao.isGoogleTiles():boolean");
    }
}
