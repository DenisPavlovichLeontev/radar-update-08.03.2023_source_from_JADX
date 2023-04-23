package mil.nga.geopackage.extension.index;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(daoClass = GeometryIndexDao.class, tableName = "nga_geometry_index")
public class GeometryIndex {
    public static final String COLUMN_GEOM_ID = "geom_id";
    public static final String COLUMN_MAX_M = "max_m";
    public static final String COLUMN_MAX_X = "max_x";
    public static final String COLUMN_MAX_Y = "max_y";
    public static final String COLUMN_MAX_Z = "max_z";
    public static final String COLUMN_MIN_M = "min_m";
    public static final String COLUMN_MIN_X = "min_x";
    public static final String COLUMN_MIN_Y = "min_y";
    public static final String COLUMN_MIN_Z = "min_z";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String TABLE_NAME = "nga_geometry_index";
    @DatabaseField(canBeNull = false, columnName = "geom_id", uniqueCombo = true)
    private long geomId;
    @DatabaseField(columnName = "max_m")
    private Double maxM;
    @DatabaseField(canBeNull = false, columnName = "max_x")
    private double maxX;
    @DatabaseField(canBeNull = false, columnName = "max_y")
    private double maxY;
    @DatabaseField(columnName = "max_z")
    private Double maxZ;
    @DatabaseField(columnName = "min_m")
    private Double minM;
    @DatabaseField(canBeNull = false, columnName = "min_x")
    private double minX;
    @DatabaseField(canBeNull = false, columnName = "min_y")
    private double minY;
    @DatabaseField(columnName = "min_z")
    private Double minZ;
    @DatabaseField(canBeNull = false, columnName = "table_name", foreign = true, foreignAutoRefresh = true, unique = true)
    private TableIndex tableIndex;
    @DatabaseField(canBeNull = false, columnName = "table_name", mo19322id = true, uniqueCombo = true)
    private String tableName;

    public GeometryIndex() {
    }

    public GeometryIndex(GeometryIndex geometryIndex) {
        this.tableIndex = geometryIndex.tableIndex;
        this.tableName = geometryIndex.tableName;
        this.geomId = geometryIndex.geomId;
        this.minX = geometryIndex.minX;
        this.maxX = geometryIndex.maxX;
        this.minY = geometryIndex.minY;
        this.maxY = geometryIndex.maxY;
        this.minZ = geometryIndex.minZ;
        this.maxZ = geometryIndex.maxZ;
        this.minM = geometryIndex.minM;
        this.maxM = geometryIndex.maxM;
    }

    public GeometryIndexKey getId() {
        return new GeometryIndexKey(this.tableName, this.geomId);
    }

    public void setId(GeometryIndexKey geometryIndexKey) {
        this.tableName = geometryIndexKey.getTableName();
        this.geomId = geometryIndexKey.getGeomId();
    }

    public TableIndex getTableIndex() {
        return this.tableIndex;
    }

    public void setTableIndex(TableIndex tableIndex2) {
        this.tableIndex = tableIndex2;
        if (tableIndex2 != null) {
            this.tableName = tableIndex2.getTableName();
        } else {
            this.tableName = null;
        }
    }

    public String getTableName() {
        return this.tableName;
    }

    public long getGeomId() {
        return this.geomId;
    }

    public void setGeomId(long j) {
        this.geomId = j;
    }

    public double getMinX() {
        return this.minX;
    }

    public void setMinX(double d) {
        this.minX = d;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public void setMaxX(double d) {
        this.maxX = d;
    }

    public double getMinY() {
        return this.minY;
    }

    public void setMinY(double d) {
        this.minY = d;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public void setMaxY(double d) {
        this.maxY = d;
    }

    public Double getMinZ() {
        return this.minZ;
    }

    public void setMinZ(Double d) {
        this.minZ = d;
    }

    public Double getMaxZ() {
        return this.maxZ;
    }

    public void setMaxZ(Double d) {
        this.maxZ = d;
    }

    public Double getMinM() {
        return this.minM;
    }

    public void setMinM(Double d) {
        this.minM = d;
    }

    public Double getMaxM() {
        return this.maxM;
    }

    public void setMaxM(Double d) {
        this.maxM = d;
    }
}
