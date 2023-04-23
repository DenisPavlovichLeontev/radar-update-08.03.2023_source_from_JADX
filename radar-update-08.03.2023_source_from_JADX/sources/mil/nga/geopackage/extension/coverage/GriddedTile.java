package mil.nga.geopackage.extension.coverage;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import mil.nga.geopackage.core.contents.Contents;

@DatabaseTable(daoClass = GriddedTileDao.class, tableName = "gpkg_2d_gridded_tile_ancillary")
public class GriddedTile {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MAX = "max";
    public static final String COLUMN_MEAN = "mean";
    public static final String COLUMN_MIN = "min";
    public static final String COLUMN_OFFSET = "offset";
    public static final String COLUMN_SCALE = "scale";
    public static final String COLUMN_STANDARD_DEVIATION = "std_dev";
    public static final String COLUMN_TABLE_ID = "tpudt_id";
    public static final String COLUMN_TABLE_NAME = "tpudt_name";
    public static final String TABLE_NAME = "gpkg_2d_gridded_tile_ancillary";
    @DatabaseField(canBeNull = false, columnName = "tpudt_name", foreign = true, foreignAutoRefresh = true)
    private Contents contents;
    @DatabaseField(canBeNull = false, columnName = "id", generatedId = true)

    /* renamed from: id */
    private long f349id;
    @DatabaseField(columnName = "max")
    private Double max;
    @DatabaseField(columnName = "mean")
    private Double mean;
    @DatabaseField(columnName = "min")
    private Double min;
    @DatabaseField(canBeNull = false, columnName = "offset")
    private double offset = 0.0d;
    @DatabaseField(canBeNull = false, columnName = "scale")
    private double scale = 1.0d;
    @DatabaseField(columnName = "std_dev")
    private Double standardDeviation;
    @DatabaseField(canBeNull = false, columnName = "tpudt_id")
    private long tableId;
    @DatabaseField(canBeNull = false, columnName = "tpudt_name")
    private String tableName;

    public GriddedTile() {
    }

    public GriddedTile(GriddedTile griddedTile) {
        this.f349id = griddedTile.f349id;
        this.contents = griddedTile.contents;
        this.tableName = griddedTile.tableName;
        this.tableId = griddedTile.tableId;
        this.scale = griddedTile.scale;
        this.offset = griddedTile.offset;
        this.min = griddedTile.min;
        this.max = griddedTile.max;
        this.mean = griddedTile.mean;
        this.standardDeviation = griddedTile.standardDeviation;
    }

    public long getId() {
        return this.f349id;
    }

    public Contents getContents() {
        return this.contents;
    }

    public void setContents(Contents contents2) {
        this.contents = contents2;
        if (contents2 != null) {
            this.tableName = contents2.getTableName();
        } else {
            this.tableName = null;
        }
    }

    public String getTableName() {
        return this.tableName;
    }

    public long getTableId() {
        return this.tableId;
    }

    public void setTableId(long j) {
        this.tableId = j;
    }

    public double getScale() {
        return this.scale;
    }

    public void setScale(double d) {
        this.scale = d;
    }

    public double getOffset() {
        return this.offset;
    }

    public void setOffset(double d) {
        this.offset = d;
    }

    public Double getMin() {
        return this.min;
    }

    public void setMin(Double d) {
        this.min = d;
    }

    public Double getMax() {
        return this.max;
    }

    public void setMax(Double d) {
        this.max = d;
    }

    public Double getMean() {
        return this.mean;
    }

    public void setMean(Double d) {
        this.mean = d;
    }

    public Double getStandardDeviation() {
        return this.standardDeviation;
    }

    public void setStandardDeviation(Double d) {
        this.standardDeviation = d;
    }
}
