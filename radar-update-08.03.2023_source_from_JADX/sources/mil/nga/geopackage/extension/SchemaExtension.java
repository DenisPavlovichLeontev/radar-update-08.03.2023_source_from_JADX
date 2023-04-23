package mil.nga.geopackage.extension;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

public class SchemaExtension extends BaseExtension {
    public static final String DEFINITION = GeoPackageProperties.getProperty(PropertyConstants.EXTENSIONS, NAME);
    public static final String EXTENSION_NAME = "gpkg_schema";
    public static final String NAME = "schema";

    public SchemaExtension(GeoPackageCore geoPackageCore) {
        super(geoPackageCore);
    }

    public Extensions getOrCreate() {
        return getOrCreate(EXTENSION_NAME, (String) null, (String) null, DEFINITION, ExtensionScopeType.READ_WRITE);
    }

    public boolean has() {
        return has(EXTENSION_NAME, (String) null, (String) null);
    }
}
