package mil.nga.geopackage.core.srs;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

@DatabaseTable(daoClass = SpatialReferenceSystemDao.class, tableName = "gpkg_spatial_ref_sys")
public class SpatialReferenceSystem {
    public static final String COLUMN_DEFINITION = "definition";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ID = "srs_id";
    public static final String COLUMN_ORGANIZATION = "organization";
    public static final String COLUMN_ORGANIZATION_COORDSYS_ID = "organization_coordsys_id";
    public static final String COLUMN_SRS_ID = "srs_id";
    public static final String COLUMN_SRS_NAME = "srs_name";
    public static final String TABLE_NAME = "gpkg_spatial_ref_sys";
    @ForeignCollectionField(eager = false)
    private ForeignCollection<Contents> contents;
    @DatabaseField(canBeNull = false, columnName = "definition")
    private String definition;
    private String definition_12_063;
    @DatabaseField(columnName = "description")
    private String description;
    @ForeignCollectionField(eager = false)
    private ForeignCollection<GeometryColumns> geometryColumns;
    @DatabaseField(canBeNull = false, columnName = "organization")
    private String organization;
    @DatabaseField(canBeNull = false, columnName = "organization_coordsys_id")
    private long organizationCoordsysId;
    @DatabaseField(canBeNull = false, columnName = "srs_id", mo19322id = true)
    private long srsId;
    @DatabaseField(canBeNull = false, columnName = "srs_name")
    private String srsName;
    @ForeignCollectionField(eager = false)
    private ForeignCollection<TileMatrixSet> tileMatrixSet;

    public SpatialReferenceSystem() {
    }

    public SpatialReferenceSystem(SpatialReferenceSystem spatialReferenceSystem) {
        this.srsName = spatialReferenceSystem.srsName;
        this.srsId = spatialReferenceSystem.srsId;
        this.organization = spatialReferenceSystem.organization;
        this.organizationCoordsysId = spatialReferenceSystem.organizationCoordsysId;
        this.definition = spatialReferenceSystem.definition;
        this.description = spatialReferenceSystem.description;
        this.definition_12_063 = spatialReferenceSystem.definition_12_063;
    }

    public long getId() {
        return this.srsId;
    }

    public void setId(long j) {
        this.srsId = j;
    }

    public String getSrsName() {
        return this.srsName;
    }

    public void setSrsName(String str) {
        this.srsName = str;
    }

    public long getSrsId() {
        return this.srsId;
    }

    public void setSrsId(long j) {
        this.srsId = j;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String str) {
        this.organization = str;
    }

    public long getOrganizationCoordsysId() {
        return this.organizationCoordsysId;
    }

    public void setOrganizationCoordsysId(long j) {
        this.organizationCoordsysId = j;
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

    public String getDefinition_12_063() {
        return this.definition_12_063;
    }

    public void setDefinition_12_063(String str) {
        this.definition_12_063 = str;
    }

    public ForeignCollection<Contents> getContents() {
        return this.contents;
    }

    public ForeignCollection<GeometryColumns> getGeometryColumns() {
        return this.geometryColumns;
    }

    public ForeignCollection<TileMatrixSet> getTileMatrixSet() {
        return this.tileMatrixSet;
    }
}
