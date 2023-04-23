package mil.nga.geopackage.features.columns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.schema.TableColumnKey;
import mil.nga.wkb.geom.GeometryType;

@DatabaseTable(daoClass = GeometryColumnsDao.class, tableName = "gpkg_geometry_columns")
public class GeometryColumns {
    public static final String COLUMN_COLUMN_NAME = "column_name";
    public static final String COLUMN_GEOMETRY_TYPE_NAME = "geometry_type_name";
    public static final String COLUMN_ID_1 = "table_name";
    public static final String COLUMN_ID_2 = "column_name";
    public static final String COLUMN_M = "m";
    public static final String COLUMN_SRS_ID = "srs_id";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String COLUMN_Z = "z";
    public static final String TABLE_NAME = "gpkg_geometry_columns";
    @DatabaseField(canBeNull = false, columnName = "column_name", uniqueCombo = true)
    private String columnName;
    @DatabaseField(canBeNull = false, columnName = "table_name", foreign = true, foreignAutoRefresh = true, unique = true)
    private Contents contents;
    @DatabaseField(canBeNull = false, columnName = "geometry_type_name")
    private String geometryTypeName;
    @DatabaseField(canBeNull = false, columnName = "m")

    /* renamed from: m */
    private byte f350m;
    @DatabaseField(canBeNull = false, columnName = "srs_id", foreign = true, foreignAutoRefresh = true)
    private SpatialReferenceSystem srs;
    @DatabaseField(canBeNull = false, columnName = "srs_id")
    private long srsId;
    @DatabaseField(canBeNull = false, columnName = "table_name", mo19322id = true, uniqueCombo = true)
    private String tableName;
    @DatabaseField(canBeNull = false, columnName = "z")

    /* renamed from: z */
    private byte f351z;

    public GeometryColumns() {
    }

    public GeometryColumns(GeometryColumns geometryColumns) {
        this.contents = geometryColumns.contents;
        this.tableName = geometryColumns.tableName;
        this.columnName = geometryColumns.columnName;
        this.geometryTypeName = geometryColumns.geometryTypeName;
        this.srs = geometryColumns.srs;
        this.srsId = geometryColumns.srsId;
        this.f351z = geometryColumns.f351z;
        this.f350m = geometryColumns.f350m;
    }

    public TableColumnKey getId() {
        return new TableColumnKey(this.tableName, this.columnName);
    }

    public void setId(TableColumnKey tableColumnKey) {
        this.tableName = tableColumnKey.getTableName();
        this.columnName = tableColumnKey.getColumnName();
    }

    public Contents getContents() {
        return this.contents;
    }

    public void setContents(Contents contents2) {
        this.contents = contents2;
        if (contents2 != null) {
            ContentsDataType dataType = contents2.getDataType();
            if (dataType == null || dataType != ContentsDataType.FEATURES) {
                StringBuilder sb = new StringBuilder();
                sb.append("The ");
                Class<Contents> cls = Contents.class;
                sb.append("Contents");
                sb.append(" of a ");
                sb.append("GeometryColumns");
                sb.append(" must have a data type of ");
                sb.append(ContentsDataType.FEATURES.getName());
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

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String str) {
        this.columnName = str;
    }

    public GeometryType getGeometryType() {
        return GeometryType.fromName(this.geometryTypeName);
    }

    public void setGeometryType(GeometryType geometryType) {
        this.geometryTypeName = geometryType.getName();
    }

    public String getGeometryTypeName() {
        return this.geometryTypeName;
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

    public byte getZ() {
        return this.f351z;
    }

    public void setZ(byte b) {
        validateValues(COLUMN_Z, b);
        this.f351z = b;
    }

    public byte getM() {
        return this.f350m;
    }

    public void setM(byte b) {
        validateValues(COLUMN_M, b);
        this.f350m = b;
    }

    private void validateValues(String str, byte b) {
        if (b < 0 || b > 2) {
            throw new GeoPackageException(str + " value must be 0 for prohibited, 1 for mandatory, or 2 for optional");
        }
    }
}
