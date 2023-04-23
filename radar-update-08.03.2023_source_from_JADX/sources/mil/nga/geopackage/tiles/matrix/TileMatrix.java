package mil.nga.geopackage.tiles.matrix;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDataType;

@DatabaseTable(daoClass = TileMatrixDao.class, tableName = "gpkg_tile_matrix")
public class TileMatrix {
    public static final String COLUMN_ID_1 = "table_name";
    public static final String COLUMN_ID_2 = "zoom_level";
    public static final String COLUMN_MATRIX_HEIGHT = "matrix_height";
    public static final String COLUMN_MATRIX_WIDTH = "matrix_width";
    public static final String COLUMN_PIXEL_X_SIZE = "pixel_x_size";
    public static final String COLUMN_PIXEL_Y_SIZE = "pixel_y_size";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String COLUMN_TILE_HEIGHT = "tile_height";
    public static final String COLUMN_TILE_WIDTH = "tile_width";
    public static final String COLUMN_ZOOM_LEVEL = "zoom_level";
    public static final String TABLE_NAME = "gpkg_tile_matrix";
    @DatabaseField(canBeNull = false, columnName = "table_name", foreign = true, foreignAutoRefresh = true, unique = true)
    private Contents contents;
    @DatabaseField(canBeNull = false, columnName = "matrix_height")
    private long matrixHeight;
    @DatabaseField(canBeNull = false, columnName = "matrix_width")
    private long matrixWidth;
    @DatabaseField(canBeNull = false, columnName = "pixel_x_size")
    private double pixelXSize;
    @DatabaseField(canBeNull = false, columnName = "pixel_y_size")
    private double pixelYSize;
    @DatabaseField(canBeNull = false, columnName = "table_name", mo19322id = true, uniqueCombo = true)
    private String tableName;
    @DatabaseField(canBeNull = false, columnName = "tile_height")
    private long tileHeight;
    @DatabaseField(canBeNull = false, columnName = "tile_width")
    private long tileWidth;
    @DatabaseField(canBeNull = false, columnName = "zoom_level", uniqueCombo = true)
    private long zoomLevel;

    public TileMatrix() {
    }

    public TileMatrix(TileMatrix tileMatrix) {
        this.contents = tileMatrix.contents;
        this.tableName = tileMatrix.tableName;
        this.zoomLevel = tileMatrix.zoomLevel;
        this.matrixWidth = tileMatrix.matrixWidth;
        this.matrixHeight = tileMatrix.matrixHeight;
        this.tileWidth = tileMatrix.tileWidth;
        this.tileHeight = tileMatrix.tileHeight;
        this.pixelXSize = tileMatrix.pixelXSize;
        this.pixelYSize = tileMatrix.pixelYSize;
    }

    public TileMatrixKey getId() {
        return new TileMatrixKey(this.tableName, this.zoomLevel);
    }

    public void setId(TileMatrixKey tileMatrixKey) {
        this.tableName = tileMatrixKey.getTableName();
        this.zoomLevel = tileMatrixKey.getZoomLevel();
    }

    public Contents getContents() {
        return this.contents;
    }

    public void setContents(Contents contents2) {
        this.contents = contents2;
        if (contents2 != null) {
            ContentsDataType dataType = contents2.getDataType();
            if (dataType == null || !(dataType == ContentsDataType.TILES || dataType == ContentsDataType.GRIDDED_COVERAGE)) {
                StringBuilder sb = new StringBuilder();
                sb.append("The ");
                Class<Contents> cls = Contents.class;
                sb.append("Contents");
                sb.append(" of a ");
                sb.append("TileMatrix");
                sb.append(" must have a data type of ");
                sb.append(ContentsDataType.TILES.getName());
                sb.append(" or ");
                sb.append(ContentsDataType.GRIDDED_COVERAGE.getName());
                throw new GeoPackageException(sb.toString());
            }
            this.tableName = contents2.getId();
            return;
        }
        this.tableName = null;
    }

    public String getTableName() {
        return this.tableName;
    }

    public long getZoomLevel() {
        return this.zoomLevel;
    }

    public void setZoomLevel(long j) {
        validateValues("zoom_level", j, true);
        this.zoomLevel = j;
    }

    public long getMatrixWidth() {
        return this.matrixWidth;
    }

    public void setMatrixWidth(long j) {
        validateValues(COLUMN_MATRIX_WIDTH, j, false);
        this.matrixWidth = j;
    }

    public long getMatrixHeight() {
        return this.matrixHeight;
    }

    public void setMatrixHeight(long j) {
        validateValues(COLUMN_MATRIX_HEIGHT, j, false);
        this.matrixHeight = j;
    }

    public long getTileWidth() {
        return this.tileWidth;
    }

    public void setTileWidth(long j) {
        validateValues(COLUMN_TILE_WIDTH, j, false);
        this.tileWidth = j;
    }

    public long getTileHeight() {
        return this.tileHeight;
    }

    public void setTileHeight(long j) {
        validateValues(COLUMN_TILE_HEIGHT, j, false);
        this.tileHeight = j;
    }

    public double getPixelXSize() {
        return this.pixelXSize;
    }

    public void setPixelXSize(double d) {
        validateValues(COLUMN_PIXEL_X_SIZE, d);
        this.pixelXSize = d;
    }

    public double getPixelYSize() {
        return this.pixelYSize;
    }

    public void setPixelYSize(double d) {
        validateValues(COLUMN_PIXEL_Y_SIZE, d);
        this.pixelYSize = d;
    }

    public void setTableName(String str) {
        this.tableName = str;
    }

    private void validateValues(String str, long j, boolean z) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i < 0 || (i == 0 && !z)) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" value must be greater than ");
            sb.append(z ? "or equal to " : "");
            sb.append("0: ");
            sb.append(j);
            throw new GeoPackageException(sb.toString());
        }
    }

    private void validateValues(String str, double d) {
        if (d <= 0.0d) {
            throw new GeoPackageException(str + " value must be greater than 0: " + d);
        }
    }
}
