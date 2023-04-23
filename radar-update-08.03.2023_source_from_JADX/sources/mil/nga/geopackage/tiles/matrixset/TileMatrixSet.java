package mil.nga.geopackage.tiles.matrixset;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;

@DatabaseTable(daoClass = TileMatrixSetDao.class, tableName = "gpkg_tile_matrix_set")
public class TileMatrixSet {
    public static final String COLUMN_ID = "table_name";
    public static final String COLUMN_MAX_X = "max_x";
    public static final String COLUMN_MAX_Y = "max_y";
    public static final String COLUMN_MIN_X = "min_x";
    public static final String COLUMN_MIN_Y = "min_y";
    public static final String COLUMN_SRS_ID = "srs_id";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String TABLE_NAME = "gpkg_tile_matrix_set";
    @DatabaseField(canBeNull = false, columnName = "table_name", foreign = true, foreignAutoRefresh = true)
    private Contents contents;
    @DatabaseField(canBeNull = false, columnName = "max_x")
    private double maxX;
    @DatabaseField(canBeNull = false, columnName = "max_y")
    private double maxY;
    @DatabaseField(canBeNull = false, columnName = "min_x")
    private double minX;
    @DatabaseField(canBeNull = false, columnName = "min_y")
    private double minY;
    @DatabaseField(canBeNull = false, columnName = "srs_id", foreign = true, foreignAutoRefresh = true)
    private SpatialReferenceSystem srs;
    @DatabaseField(canBeNull = false, columnName = "srs_id")
    private long srsId;
    @DatabaseField(canBeNull = false, columnName = "table_name", mo19322id = true)
    private String tableName;

    public TileMatrixSet() {
    }

    public TileMatrixSet(TileMatrixSet tileMatrixSet) {
        this.contents = tileMatrixSet.contents;
        this.tableName = tileMatrixSet.tableName;
        this.srs = tileMatrixSet.srs;
        this.srsId = tileMatrixSet.srsId;
        this.minX = tileMatrixSet.minX;
        this.minY = tileMatrixSet.minY;
        this.maxX = tileMatrixSet.maxX;
        this.maxY = tileMatrixSet.maxY;
    }

    public String getId() {
        return this.tableName;
    }

    public void setId(String str) {
        this.tableName = str;
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
                sb.append("TileMatrixSet");
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

    public SpatialReferenceSystem getSrs() {
        return this.srs;
    }

    public void setSrs(SpatialReferenceSystem spatialReferenceSystem) {
        this.srs = spatialReferenceSystem;
        this.srsId = spatialReferenceSystem != null ? spatialReferenceSystem.getId() : -1;
    }

    public long getSrsId() {
        return this.srsId;
    }

    public double getMinX() {
        return this.minX;
    }

    public void setMinX(double d) {
        this.minX = d;
    }

    public double getMinY() {
        return this.minY;
    }

    public void setMinY(double d) {
        this.minY = d;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public void setMaxX(double d) {
        this.maxX = d;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public void setMaxY(double d) {
        this.maxY = d;
    }

    public BoundingBox getBoundingBox() {
        return new BoundingBox(getMinX(), getMinY(), getMaxX(), getMaxY());
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        setMinX(boundingBox.getMinLongitude());
        setMaxX(boundingBox.getMaxLongitude());
        setMinY(boundingBox.getMinLatitude());
        setMaxY(boundingBox.getMaxLatitude());
    }
}
