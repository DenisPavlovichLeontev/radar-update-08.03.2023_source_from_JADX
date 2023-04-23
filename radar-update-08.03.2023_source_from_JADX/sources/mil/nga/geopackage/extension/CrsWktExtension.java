package mil.nga.geopackage.extension;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.p009db.GeoPackageCoreConnection;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

public class CrsWktExtension extends BaseExtension {
    public static final String COLUMN_DEF = GeoPackageProperties.getProperty("geopackage.extensions.crs_wkt", "column_def");
    public static final String COLUMN_NAME = GeoPackageProperties.getProperty("geopackage.extensions.crs_wkt", "column_name");
    public static final String DEFINITION = GeoPackageProperties.getProperty(PropertyConstants.EXTENSIONS, NAME);
    public static final String EXTENSION_NAME = "gpkg_crs_wkt";
    public static final String NAME = "crs_wkt";
    private GeoPackageCoreConnection connection = null;

    public CrsWktExtension(GeoPackageCore geoPackageCore) {
        super(geoPackageCore);
        this.connection = geoPackageCore.getDatabase();
    }

    public Extensions getOrCreate() {
        Extensions orCreate = getOrCreate(EXTENSION_NAME, (String) null, (String) null, DEFINITION, ExtensionScopeType.READ_WRITE);
        if (!hasColumn()) {
            createColumn();
        }
        return orCreate;
    }

    public boolean has() {
        boolean has = has(EXTENSION_NAME, (String) null, (String) null);
        return has ? hasColumn() : has;
    }

    public void updateDefinition(long j, String str) {
        GeoPackageCoreConnection geoPackageCoreConnection = this.connection;
        geoPackageCoreConnection.execSQL("UPDATE gpkg_spatial_ref_sys SET " + COLUMN_NAME + " = '" + str + "' WHERE " + "srs_id" + " = " + j);
    }

    public String getDefinition(long j) {
        GeoPackageCoreConnection geoPackageCoreConnection = this.connection;
        return geoPackageCoreConnection.querySingleStringResult("SELECT " + COLUMN_NAME + " FROM " + SpatialReferenceSystem.TABLE_NAME + " WHERE " + "srs_id" + " = ?", new String[]{String.valueOf(j)});
    }

    private void createColumn() {
        this.connection.addColumn(SpatialReferenceSystem.TABLE_NAME, COLUMN_NAME, COLUMN_DEF);
        updateDefinition((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.WGS_84, "srs_id"), GeoPackageProperties.getProperty(PropertyConstants.WGS_84, PropertyConstants.DEFINITION_12_063));
        updateDefinition((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.UNDEFINED_CARTESIAN, "srs_id"), GeoPackageProperties.getProperty(PropertyConstants.UNDEFINED_CARTESIAN, PropertyConstants.DEFINITION_12_063));
        updateDefinition((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.UNDEFINED_GEOGRAPHIC, "srs_id"), GeoPackageProperties.getProperty(PropertyConstants.UNDEFINED_GEOGRAPHIC, PropertyConstants.DEFINITION_12_063));
        updateDefinition((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.WEB_MERCATOR, "srs_id"), GeoPackageProperties.getProperty(PropertyConstants.WEB_MERCATOR, PropertyConstants.DEFINITION_12_063));
    }

    private boolean hasColumn() {
        return this.connection.columnExists(SpatialReferenceSystem.TABLE_NAME, COLUMN_NAME);
    }
}
