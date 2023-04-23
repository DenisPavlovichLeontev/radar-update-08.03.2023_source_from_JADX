package mil.nga.geopackage.p009db.metadata;

/* renamed from: mil.nga.geopackage.db.metadata.GeoPackageMetadata */
public class GeoPackageMetadata {
    public static final String[] COLUMNS = {"geopackage_id", "name", COLUMN_EXTERNAL_PATH};
    public static final String COLUMN_EXTERNAL_PATH = "external_path";
    public static final String COLUMN_ID = "geopackage_id";
    public static final String COLUMN_NAME = "name";
    public static final String CREATE_SQL = "CREATE TABLE geopackage(geopackage_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE, external_path TEXT);";
    public static final String TABLE_NAME = "geopackage";
    public String externalPath;

    /* renamed from: id */
    public long f339id;
    public String name;

    public long getId() {
        return this.f339id;
    }

    public void setId(long j) {
        this.f339id = j;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getExternalPath() {
        return this.externalPath;
    }

    public void setExternalPath(String str) {
        this.externalPath = str;
    }

    public boolean isExternal() {
        return this.externalPath != null;
    }
}
