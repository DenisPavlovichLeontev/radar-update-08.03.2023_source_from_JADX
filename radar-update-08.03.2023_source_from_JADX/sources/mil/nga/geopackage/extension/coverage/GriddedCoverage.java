package mil.nga.geopackage.extension.coverage;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

@DatabaseTable(daoClass = GriddedCoverageDao.class, tableName = "gpkg_2d_gridded_coverage_ancillary")
public class GriddedCoverage {
    public static final String COLUMN_DATATYPE = "datatype";
    public static final String COLUMN_DATA_NULL = "data_null";
    public static final String COLUMN_FIELD_NAME = "field_name";
    public static final String COLUMN_GRID_CELL_ENCODING = "grid_cell_encoding";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_OFFSET = "offset";
    public static final String COLUMN_PRECISION = "precision";
    public static final String COLUMN_QUANTITY_DEFINITION = "quantity_definition";
    public static final String COLUMN_SCALE = "scale";
    public static final String COLUMN_TILE_MATRIX_SET_NAME = "tile_matrix_set_name";
    public static final String COLUMN_UOM = "uom";
    public static final String TABLE_NAME = "gpkg_2d_gridded_coverage_ancillary";
    @DatabaseField(columnName = "data_null")
    private Double dataNull;
    @DatabaseField(canBeNull = false, columnName = "datatype", defaultValue = "integer")
    private String datatype;
    @DatabaseField(columnName = "field_name", defaultValue = "Height")
    private String fieldName;
    @DatabaseField(columnName = "grid_cell_encoding", defaultValue = "grid-value-is-center")
    private String gridCellEncoding;
    @DatabaseField(canBeNull = false, columnName = "id", generatedId = true)

    /* renamed from: id */
    private long f348id;
    @DatabaseField(canBeNull = false, columnName = "offset", defaultValue = "0.0")
    private double offset = 0.0d;
    @DatabaseField(columnName = "precision", defaultValue = "1.0")
    private Double precision = Double.valueOf(1.0d);
    @DatabaseField(columnName = "quantity_definition", defaultValue = "Height")
    private String quantityDefinition;
    @DatabaseField(canBeNull = false, columnName = "scale", defaultValue = "1.0")
    private double scale = 1.0d;
    @DatabaseField(canBeNull = false, columnName = "tile_matrix_set_name", foreign = true, foreignAutoRefresh = true)
    private TileMatrixSet tileMatrixSet;
    @DatabaseField(canBeNull = false, columnName = "tile_matrix_set_name")
    private String tileMatrixSetName;
    @DatabaseField(columnName = "uom")
    private String uom;

    public GriddedCoverage() {
    }

    public GriddedCoverage(GriddedCoverage griddedCoverage) {
        this.f348id = griddedCoverage.f348id;
        this.tileMatrixSet = griddedCoverage.tileMatrixSet;
        this.tileMatrixSetName = griddedCoverage.tileMatrixSetName;
        this.datatype = griddedCoverage.datatype;
        this.scale = griddedCoverage.scale;
        this.offset = griddedCoverage.offset;
        this.precision = griddedCoverage.precision;
        this.dataNull = griddedCoverage.dataNull;
        this.gridCellEncoding = griddedCoverage.gridCellEncoding;
        this.uom = griddedCoverage.uom;
        this.fieldName = griddedCoverage.fieldName;
        this.quantityDefinition = griddedCoverage.quantityDefinition;
    }

    public long getId() {
        return this.f348id;
    }

    public TileMatrixSet getTileMatrixSet() {
        return this.tileMatrixSet;
    }

    public void setTileMatrixSet(TileMatrixSet tileMatrixSet2) {
        this.tileMatrixSet = tileMatrixSet2;
        if (tileMatrixSet2 != null) {
            this.tileMatrixSetName = tileMatrixSet2.getTableName();
        } else {
            this.tileMatrixSetName = null;
        }
    }

    public String getTileMatrixSetName() {
        return this.tileMatrixSetName;
    }

    public GriddedCoverageDataType getDataType() {
        return GriddedCoverageDataType.fromName(this.datatype);
    }

    public void setDataType(GriddedCoverageDataType griddedCoverageDataType) {
        this.datatype = griddedCoverageDataType.getName();
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

    public double getPrecision() {
        Double d = this.precision;
        if (d != null) {
            return d.doubleValue();
        }
        return 1.0d;
    }

    public void setPrecision(Double d) {
        this.precision = d;
    }

    public Double getDataNull() {
        return this.dataNull;
    }

    public void setDataNull(Double d) {
        this.dataNull = d;
    }

    public GriddedCoverageEncodingType getGridCellEncodingType() {
        return GriddedCoverageEncodingType.fromName(this.gridCellEncoding);
    }

    public void setGridCellEncodingType(GriddedCoverageEncodingType griddedCoverageEncodingType) {
        setGridCellEncoding(griddedCoverageEncodingType.getName());
    }

    public String getGridCellEncoding() {
        return this.gridCellEncoding;
    }

    public void setGridCellEncoding(String str) {
        this.gridCellEncoding = str;
    }

    public String getUom() {
        return this.uom;
    }

    public void setUom(String str) {
        this.uom = str;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String str) {
        this.fieldName = str;
    }

    public String getQuantityDefinition() {
        return this.quantityDefinition;
    }

    public void setQuantityDefinition(String str) {
        this.quantityDefinition = str;
    }
}
