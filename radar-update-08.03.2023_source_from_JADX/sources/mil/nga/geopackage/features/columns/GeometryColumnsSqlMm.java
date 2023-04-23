package mil.nga.geopackage.features.columns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.schema.TableColumnKey;
import mil.nga.wkb.geom.GeometryType;

@DatabaseTable(daoClass = GeometryColumnsSqlMmDao.class, tableName = "st_geometry_columns")
public class GeometryColumnsSqlMm {
    public static final String COLUMN_COLUMN_NAME = "column_name";
    public static final String COLUMN_GEOMETRY_TYPE_NAME = "geometry_type_name";
    public static final String COLUMN_GEOMETRY_TYPE_NAME_PREFIX = "ST_";
    public static final String COLUMN_ID_1 = "table_name";
    public static final String COLUMN_ID_2 = "column_name";
    public static final String COLUMN_SRS_ID = "srs_id";
    public static final String COLUMN_SRS_NAME = "srs_name";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String TABLE_NAME = "st_geometry_columns";
    @DatabaseField(canBeNull = false, columnName = "column_name", uniqueCombo = true)
    private String columnName;
    @DatabaseField(canBeNull = false, columnName = "table_name", foreign = true, foreignAutoRefresh = true, unique = true)
    private Contents contents;
    @DatabaseField(canBeNull = false, columnName = "geometry_type_name")
    private String geometryTypeName;
    @DatabaseField(canBeNull = false, columnName = "srs_id", foreign = true, foreignAutoRefresh = true)
    private SpatialReferenceSystem srs;
    @DatabaseField(canBeNull = false, columnName = "srs_id")
    private long srsId;
    @DatabaseField(canBeNull = false, columnName = "srs_name")
    private String srsName;
    @DatabaseField(canBeNull = false, columnName = "table_name", mo19322id = true, uniqueCombo = true)
    private String tableName;

    public GeometryColumnsSqlMm() {
    }

    public GeometryColumnsSqlMm(GeometryColumnsSqlMm geometryColumnsSqlMm) {
        this.contents = geometryColumnsSqlMm.contents;
        this.tableName = geometryColumnsSqlMm.tableName;
        this.columnName = geometryColumnsSqlMm.columnName;
        this.geometryTypeName = geometryColumnsSqlMm.geometryTypeName;
        this.srs = geometryColumnsSqlMm.srs;
        this.srsId = geometryColumnsSqlMm.srsId;
        this.srsName = geometryColumnsSqlMm.srsName;
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
                sb.append("GeometryColumnsSqlMm");
                sb.append(" must have a data type of ");
                sb.append(ContentsDataType.FEATURES.getName());
                throw new GeoPackageException(sb.toString());
            }
            this.tableName = contents2.getId();
        }
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
        String str = this.geometryTypeName;
        return GeometryType.fromName(str.substring(3, str.length()));
    }

    public void setGeometryType(GeometryType geometryType) {
        this.geometryTypeName = COLUMN_GEOMETRY_TYPE_NAME_PREFIX + geometryType.getName();
    }

    public String getGeometryTypeName() {
        return this.geometryTypeName;
    }

    public SpatialReferenceSystem getSrs() {
        return this.srs;
    }

    public void setSrs(SpatialReferenceSystem spatialReferenceSystem) {
        this.srs = spatialReferenceSystem;
        if (spatialReferenceSystem != null) {
            this.srsId = spatialReferenceSystem.getId();
            this.srsName = spatialReferenceSystem.getSrsName();
        }
    }

    public long getSrsId() {
        return this.srsId;
    }

    public String getSrsName() {
        return this.srsName;
    }
}
