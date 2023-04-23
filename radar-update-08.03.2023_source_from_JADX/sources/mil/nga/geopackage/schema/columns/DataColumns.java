package mil.nga.geopackage.schema.columns;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.sql.SQLException;
import java.util.List;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.schema.TableColumnKey;
import mil.nga.geopackage.schema.constraints.DataColumnConstraints;
import mil.nga.geopackage.schema.constraints.DataColumnConstraintsDao;

@DatabaseTable(daoClass = DataColumnsDao.class, tableName = "gpkg_data_columns")
public class DataColumns {
    public static final String COLUMN_COLUMN_NAME = "column_name";
    public static final String COLUMN_CONSTRAINT_NAME = "constraint_name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ID_1 = "table_name";
    public static final String COLUMN_ID_2 = "column_name";
    public static final String COLUMN_MIME_TYPE = "mime_type";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String COLUMN_TITLE = "title";
    public static final String TABLE_NAME = "gpkg_data_columns";
    @DatabaseField(canBeNull = false, columnName = "column_name", uniqueCombo = true)
    private String columnName;
    @DatabaseField(columnName = "constraint_name")
    private String constraintName;
    @DatabaseField(canBeNull = false, columnName = "table_name", foreign = true, foreignAutoRefresh = true, unique = true)
    private Contents contents;
    @DatabaseField(columnName = "description")
    private String description;
    @DatabaseField(columnName = "mime_type")
    private String mimeType;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(canBeNull = false, columnName = "table_name", mo19322id = true, uniqueCombo = true)
    private String tableName;
    @DatabaseField(columnName = "title")
    private String title;

    public DataColumns() {
    }

    public DataColumns(DataColumns dataColumns) {
        this.contents = dataColumns.contents;
        this.tableName = dataColumns.tableName;
        this.columnName = dataColumns.columnName;
        this.name = dataColumns.name;
        this.title = dataColumns.title;
        this.description = dataColumns.description;
        this.mimeType = dataColumns.mimeType;
        this.constraintName = dataColumns.constraintName;
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
        if (contents2 == null) {
            this.tableName = null;
        } else if (contents2.getDataType() != null) {
            this.tableName = contents2.getId();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("The ");
            Class<Contents> cls = Contents.class;
            sb.append("Contents");
            sb.append(" of a ");
            sb.append("DataColumns");
            sb.append(" must have a data type");
            throw new GeoPackageException(sb.toString());
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

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String str) {
        this.mimeType = str;
    }

    public String getConstraintName() {
        return this.constraintName;
    }

    public void setConstraint(DataColumnConstraints dataColumnConstraints) {
        setConstraintName(dataColumnConstraints != null ? dataColumnConstraints.getConstraintName() : null);
    }

    public void setConstraintName(String str) {
        this.constraintName = str;
    }

    public List<DataColumnConstraints> getConstraints(DataColumnConstraintsDao dataColumnConstraintsDao) throws SQLException {
        String str = this.constraintName;
        if (str != null) {
            return dataColumnConstraintsDao.queryByConstraintName(str);
        }
        return null;
    }
}
