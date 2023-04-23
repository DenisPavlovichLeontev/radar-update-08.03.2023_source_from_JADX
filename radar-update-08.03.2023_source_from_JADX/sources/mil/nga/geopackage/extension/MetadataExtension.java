package mil.nga.geopackage.extension;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

public class MetadataExtension extends BaseExtension {
    public static final String DEFINITION = GeoPackageProperties.getProperty(PropertyConstants.EXTENSIONS, "metadata");
    public static final String EXTENSION_NAME = "gpkg_metadata";
    public static final String NAME = "metadata";

    public MetadataExtension(GeoPackageCore geoPackageCore) {
        super(geoPackageCore);
    }

    public Extensions getOrCreate() {
        return getOrCreate("gpkg_metadata", (String) null, (String) null, DEFINITION, ExtensionScopeType.READ_WRITE);
    }

    public boolean has() {
        return has("gpkg_metadata", (String) null, (String) null);
    }
}
