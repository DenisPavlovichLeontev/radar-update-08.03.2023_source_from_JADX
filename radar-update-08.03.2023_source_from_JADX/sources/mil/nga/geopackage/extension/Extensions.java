package mil.nga.geopackage.extension;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(daoClass = ExtensionsDao.class, tableName = "gpkg_extensions")
public class Extensions {
    public static final String COLUMN_COLUMN_NAME = "column_name";
    public static final String COLUMN_DEFINITION = "definition";
    public static final String COLUMN_EXTENSION_NAME = "extension_name";
    public static final String COLUMN_SCOPE = "scope";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String EXTENSION_NAME_DIVIDER = "_";
    public static final String TABLE_NAME = "gpkg_extensions";
    @DatabaseField(columnName = "column_name", uniqueCombo = true)
    private String columnName;
    @DatabaseField(canBeNull = false, columnName = "definition")
    private String definition;
    @DatabaseField(canBeNull = false, columnName = "extension_name", uniqueCombo = true)
    private String extensionName;
    @DatabaseField(canBeNull = false, columnName = "scope")
    private String scope;
    @DatabaseField(columnName = "table_name", uniqueCombo = true)
    private String tableName;

    public Extensions() {
    }

    public Extensions(Extensions extensions) {
        this.tableName = extensions.tableName;
        this.columnName = extensions.columnName;
        this.extensionName = extensions.extensionName;
        this.definition = extensions.definition;
        this.scope = extensions.scope;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String str) {
        this.tableName = str;
        if (str == null) {
            this.columnName = null;
        }
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String str) {
        this.columnName = str;
    }

    public String getExtensionName() {
        return this.extensionName;
    }

    public void setExtensionName(String str) {
        this.extensionName = str;
    }

    public void setExtensionName(String str, String str2) {
        setExtensionName(buildExtensionName(str, str2));
    }

    public String getAuthor() {
        return getAuthor(this.extensionName);
    }

    public String getExtensionNameNoAuthor() {
        return getExtensionNameNoAuthor(this.extensionName);
    }

    public String getDefinition() {
        return this.definition;
    }

    public void setDefinition(String str) {
        this.definition = str;
    }

    public ExtensionScopeType getScope() {
        return ExtensionScopeType.fromValue(this.scope);
    }

    public void setScope(ExtensionScopeType extensionScopeType) {
        this.scope = extensionScopeType.getValue();
    }

    public static String buildExtensionName(String str, String str2) {
        return str + EXTENSION_NAME_DIVIDER + str2;
    }

    public static String getAuthor(String str) {
        if (str != null) {
            return str.substring(0, str.indexOf(EXTENSION_NAME_DIVIDER));
        }
        return null;
    }

    public static String getExtensionNameNoAuthor(String str) {
        if (str != null) {
            return str.substring(str.indexOf(EXTENSION_NAME_DIVIDER) + 1, str.length());
        }
        return null;
    }
}
