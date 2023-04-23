package mil.nga.geopackage.p009db.metadata;

/* renamed from: mil.nga.geopackage.db.metadata.GeometryMetadata */
public class GeometryMetadata {
    public static final String[] COLUMNS = {"geopackage_id", "table_name", "geom_id", "min_x", "max_x", "min_y", "max_y", "min_z", "max_z", "min_m", "max_m"};
    public static final String COLUMN_GEOPACKAGE_ID = "geopackage_id";
    public static final String COLUMN_ID = "geom_id";
    public static final String COLUMN_MAX_M = "max_m";
    public static final String COLUMN_MAX_X = "max_x";
    public static final String COLUMN_MAX_Y = "max_y";
    public static final String COLUMN_MAX_Z = "max_z";
    public static final String COLUMN_MIN_M = "min_m";
    public static final String COLUMN_MIN_X = "min_x";
    public static final String COLUMN_MIN_Y = "min_y";
    public static final String COLUMN_MIN_Z = "min_z";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String CREATE_SQL = "CREATE TABLE geom_metadata(geopackage_id INTEGER NOT NULL, table_name TEXT NOT NULL, geom_id INTEGER NOT NULL, min_x DOUBLE NOT NULL, max_x DOUBLE NOT NULL, min_y DOUBLE NOT NULL, max_y DOUBLE NOT NULL, min_z DOUBLE, max_z DOUBLE, min_m DOUBLE, max_m DOUBLE, CONSTRAINT pk_geom_metadata PRIMARY KEY (geopackage_id, table_name, geom_id), CONSTRAINT fk_gm_tm_gp FOREIGN KEY (geopackage_id) REFERENCES geopackage_table(geopackage_id), CONSTRAINT fk_gm_tm FOREIGN KEY (table_name) REFERENCES geopackage_table(table_name));";
    public static final String TABLE_NAME = "geom_metadata";
    public long geoPackageId;

    /* renamed from: id */
    public long f342id;
    public Double maxM;
    public double maxX;
    public double maxY;
    public Double maxZ;
    public Double minM;
    public double minX;
    public double minY;
    public Double minZ;
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

    public long getId() {
        return this.f342id;
    }

    public void setId(long j) {
        this.f342id = j;
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
