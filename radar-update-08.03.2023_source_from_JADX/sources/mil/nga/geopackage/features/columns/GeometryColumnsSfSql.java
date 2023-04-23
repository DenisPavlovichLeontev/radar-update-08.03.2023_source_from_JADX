package mil.nga.geopackage.features.columns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.schema.TableColumnKey;
import mil.nga.wkb.geom.GeometryType;

@DatabaseTable(daoClass = GeometryColumnsSfSqlDao.class, tableName = "geometry_columns")
public class GeometryColumnsSfSql {
    public static final String COLUMN_COORD_DIMENSION = "coord_dimension";
    public static final String COLUMN_F_GEOMETRY_COLUMN = "f_geometry_column";
    public static final String COLUMN_F_TABLE_NAME = "f_table_name";
    public static final String COLUMN_GEOMETRY_TYPE = "geometry_type";
    public static final String COLUMN_ID_1 = "f_table_name";
    public static final String COLUMN_ID_2 = "f_geometry_column";
    public static final String COLUMN_SRID = "srid";
    public static final String TABLE_NAME = "geometry_columns";
    @DatabaseField(canBeNull = false, columnName = "f_table_name", foreign = true, foreignAutoRefresh = true, unique = true)
    private Contents contents;
    @DatabaseField(canBeNull = false, columnName = "coord_dimension")
    private byte coordDimension;
    @DatabaseField(canBeNull = false, columnName = "f_geometry_column", uniqueCombo = true)
    private String fGeometryColumn;
    @DatabaseField(canBeNull = false, columnName = "f_table_name", mo19322id = true, uniqueCombo = true)
    private String fTableName;
    @DatabaseField(canBeNull = false, columnName = "geometry_type")
    private int geometryType;
    @DatabaseField(canBeNull = false, columnName = "srid")
    private long srid;
    @DatabaseField(canBeNull = false, columnName = "srid", foreign = true, foreignAutoRefresh = true)
    private SpatialReferenceSystem srs;

    public GeometryColumnsSfSql() {
    }

    public GeometryColumnsSfSql(GeometryColumnsSfSql geometryColumnsSfSql) {
        this.contents = geometryColumnsSfSql.contents;
        this.fTableName = geometryColumnsSfSql.fTableName;
        this.fGeometryColumn = geometryColumnsSfSql.fGeometryColumn;
        this.geometryType = geometryColumnsSfSql.geometryType;
        this.coordDimension = geometryColumnsSfSql.coordDimension;
        this.srs = geometryColumnsSfSql.srs;
        this.srid = geometryColumnsSfSql.srid;
    }

    public TableColumnKey getId() {
        return new TableColumnKey(this.fTableName, this.fGeometryColumn);
    }

    public void setId(TableColumnKey tableColumnKey) {
        this.fTableName = tableColumnKey.getTableName();
        this.fGeometryColumn = tableColumnKey.getColumnName();
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
                sb.append("GeometryColumnsSfSql");
                sb.append(" must have a data type of ");
                sb.append(ContentsDataType.FEATURES.getName());
                throw new GeoPackageException(sb.toString());
            }
            this.fTableName = contents2.getId();
        }
    }

    public String getFTableName() {
        return this.fTableName;
    }

    public String getFGeometryColumn() {
        return this.fGeometryColumn;
    }

    public void setFGeometryColumn(String str) {
        this.fGeometryColumn = str;
    }

    public GeometryType getGeometryType() {
        return GeometryType.fromCode(this.geometryType);
    }

    public void setGeometryType(GeometryType geometryType2) {
        this.geometryType = geometryType2.getCode();
    }

    public int getGeometryTypeCode() {
        return this.geometryType;
    }

    public void setCoordDimension(byte b) {
        validateCoordDimension(COLUMN_COORD_DIMENSION, b);
        this.coordDimension = b;
    }

    public byte getCoordDimension() {
        return this.coordDimension;
    }

    public SpatialReferenceSystem getSrs() {
        return this.srs;
    }

    public void setSrs(SpatialReferenceSystem spatialReferenceSystem) {
        this.srs = spatialReferenceSystem;
        if (spatialReferenceSystem != null) {
            this.srid = spatialReferenceSystem.getId();
        }
    }

    public long getSrid() {
        return this.srid;
    }

    private void validateCoordDimension(String str, byte b) {
        if (b < 2 || b > 5) {
            throw new GeoPackageException(str + " value must be between 2 and 5");
        }
    }
}
