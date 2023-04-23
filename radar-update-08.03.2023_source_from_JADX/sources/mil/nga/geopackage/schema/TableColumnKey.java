package mil.nga.geopackage.schema;

public class TableColumnKey {
    private String columnName;
    private String tableName;

    public TableColumnKey(String str, String str2) {
        this.tableName = str;
        this.columnName = str2;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String str) {
        this.tableName = str;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String str) {
        this.columnName = str;
    }

    public String toString() {
        return this.tableName + ":" + this.columnName;
    }

    public int hashCode() {
        int i;
        String str = this.columnName;
        int i2 = 0;
        if (str == null) {
            i = 0;
        } else {
            i = str.hashCode();
        }
        int i3 = (i + 31) * 31;
        String str2 = this.tableName;
        if (str2 != null) {
            i2 = str2.hashCode();
        }
        return i3 + i2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TableColumnKey tableColumnKey = (TableColumnKey) obj;
        return this.columnName.equals(tableColumnKey.columnName) && this.tableName.equals(tableColumnKey.tableName);
    }
}
