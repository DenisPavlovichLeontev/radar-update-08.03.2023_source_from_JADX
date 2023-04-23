package mil.nga.geopackage.core.srs;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(daoClass = SpatialReferenceSystemSqlMmDao.class, tableName = "st_spatial_ref_sys")
public class SpatialReferenceSystemSqlMm {
    public static final String COLUMN_DEFINITION = "definition";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ID = "srs_id";
    public static final String COLUMN_ORGANIZATION = "organization";
    public static final String COLUMN_ORGANIZATION_COORDSYS_ID = "organization_coordsys_id";
    public static final String COLUMN_SRS_ID = "srs_id";
    public static final String COLUMN_SRS_NAME = "srs_name";
    public static final String TABLE_NAME = "st_spatial_ref_sys";
    @DatabaseField(canBeNull = false, columnName = "definition")
    private String definition;
    @DatabaseField(columnName = "description")
    private String description;
    @DatabaseField(canBeNull = false, columnName = "organization")
    private String organization;
    @DatabaseField(canBeNull = false, columnName = "organization_coordsys_id")
    private int organizationCoordsysId;
    @DatabaseField(canBeNull = false, columnName = "srs_id", mo19322id = true)
    private int srsId;
    @DatabaseField(canBeNull = false, columnName = "srs_name")
    private String srsName;

    public SpatialReferenceSystemSqlMm() {
    }

    public SpatialReferenceSystemSqlMm(SpatialReferenceSystemSqlMm spatialReferenceSystemSqlMm) {
        this.srsName = spatialReferenceSystemSqlMm.srsName;
        this.srsId = spatialReferenceSystemSqlMm.srsId;
        this.organization = spatialReferenceSystemSqlMm.organization;
        this.organizationCoordsysId = spatialReferenceSystemSqlMm.organizationCoordsysId;
        this.definition = spatialReferenceSystemSqlMm.definition;
        this.description = spatialReferenceSystemSqlMm.description;
    }

    public int getId() {
        return this.srsId;
    }

    public void setId(int i) {
        this.srsId = i;
    }

    public String getSrsName() {
        return this.srsName;
    }

    public void setSrsName(String str) {
        this.srsName = str;
    }

    public int getSrsId() {
        return this.srsId;
    }

    public void setSrsId(int i) {
        this.srsId = i;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String str) {
        this.organization = str;
    }

    public int getOrganizationCoordsysId() {
        return this.organizationCoordsysId;
    }

    public void setOrganizationCoordsysId(int i) {
        this.organizationCoordsysId = i;
    }

    public String getDefinition() {
        return this.definition;
    }

    public void setDefinition(String str) {
        this.definition = str;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }
}
