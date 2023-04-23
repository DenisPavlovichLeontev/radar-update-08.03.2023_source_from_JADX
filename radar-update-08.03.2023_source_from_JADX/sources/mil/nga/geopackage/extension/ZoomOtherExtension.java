package mil.nga.geopackage.extension;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

public class ZoomOtherExtension extends BaseExtension {
    public static final String DEFINITION = GeoPackageProperties.getProperty(PropertyConstants.EXTENSIONS, NAME);
    public static final String EXTENSION_NAME = "gpkg_zoom_other";
    public static final String NAME = "zoom_other";

    public ZoomOtherExtension(GeoPackageCore geoPackageCore) {
        super(geoPackageCore);
    }

    public Extensions getOrCreate(String str) {
        return getOrCreate(EXTENSION_NAME, str, "tile_data", DEFINITION, ExtensionScopeType.READ_WRITE);
    }

    public boolean has(String str) {
        return has(EXTENSION_NAME, str, "tile_data");
    }
}
