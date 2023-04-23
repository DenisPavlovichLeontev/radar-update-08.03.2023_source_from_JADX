package mil.nga.geopackage.core.srs;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(daoClass = SpatialReferenceSystemSfSqlDao.class, tableName = "spatial_ref_sys")
public class SpatialReferenceSystemSfSql {
    public static final String COLUMN_AUTH_NAME = "auth_name";
    public static final String COLUMN_AUTH_SRID = "auth_srid";
    public static final String COLUMN_ID = "srid";
    public static final String COLUMN_SRID = "srid";
    public static final String COLUMN_SRTEXT = "srtext";
    public static final String TABLE_NAME = "spatial_ref_sys";
    @DatabaseField(canBeNull = false, columnName = "auth_name")
    private String authName;
    @DatabaseField(canBeNull = false, columnName = "auth_srid")
    private int authSrid;
    @DatabaseField(canBeNull = false, columnName = "srid", mo19322id = true)
    private int srid;
    @DatabaseField(canBeNull = false, columnName = "srtext")
    private String srtext;

    public SpatialReferenceSystemSfSql() {
    }

    public SpatialReferenceSystemSfSql(SpatialReferenceSystemSfSql spatialReferenceSystemSfSql) {
        this.srid = spatialReferenceSystemSfSql.srid;
        this.authName = spatialReferenceSystemSfSql.authName;
        this.authSrid = spatialReferenceSystemSfSql.authSrid;
        this.srtext = spatialReferenceSystemSfSql.srtext;
    }

    public int getId() {
        return this.srid;
    }

    public void setId(int i) {
        this.srid = i;
    }

    public int getSrid() {
        return this.srid;
    }

    public void setSrid(int i) {
        this.srid = i;
    }

    public String getAuthName() {
        return this.authName;
    }

    public void setAuthName(String str) {
        this.authName = str;
    }

    public int getAuthSrid() {
        return this.authSrid;
    }

    public void setAuthSrid(int i) {
        this.authSrid = i;
    }

    public String getSrtext() {
        return this.srtext;
    }

    public void setSrtext(String str) {
        this.srtext = str;
    }
}
