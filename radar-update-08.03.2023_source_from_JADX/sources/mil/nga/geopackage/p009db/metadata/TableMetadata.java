package mil.nga.geopackage.p009db.metadata;

/* renamed from: mil.nga.geopackage.db.metadata.TableMetadata */
public class TableMetadata {
    public static final String[] COLUMNS = {"geopackage_id", "table_name", "last_indexed"};
    public static final String COLUMN_GEOPACKAGE_ID = "geopackage_id";
    public static final String COLUMN_LAST_INDEXED = "last_indexed";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String CREATE_SQL = "CREATE TABLE geopackage_table(geopackage_id INTEGER NOT NULL, table_name TEXT NOT NULL, last_indexed INTEGER, CONSTRAINT pk_table_metadata PRIMARY KEY (geopackage_id, table_name), CONSTRAINT fk_tm_gp FOREIGN KEY (geopackage_id) REFERENCES geopackage(geopackage_id));";
    public static final String TABLE_NAME = "geopackage_table";
    public long geoPackageId;
    public Long lastIndexed;
    public String tableName;

    public long getGeoPackageId() {
        return this.geoPackageId;
    }

    public void setGeoPackageId(long j) {
        this.geoPackageId = j;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String str) {
        this.tableName = str;
    }

    public Long getLastIndexed() {
        return this.lastIndexed;
    }

    public void setLastIndexed(Long l) {
        this.lastIndexed = l;
    }
}
