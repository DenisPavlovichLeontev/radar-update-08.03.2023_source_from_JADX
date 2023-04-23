package mil.nga.geopackage.validate;

import java.io.File;
import java.sql.SQLException;
import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p010io.GeoPackageIOUtils;

public class GeoPackageValidate {
    public static boolean hasGeoPackageExtension(File file) {
        String fileExtension = GeoPackageIOUtils.getFileExtension(file);
        return fileExtension != null && (fileExtension.equalsIgnoreCase("gpkg") || fileExtension.equalsIgnoreCase(GeoPackageConstants.GEOPACKAGE_EXTENDED_EXTENSION));
    }

    public static void validateGeoPackageExtension(File file) {
        if (!hasGeoPackageExtension(file)) {
            throw new GeoPackageException("GeoPackage database file '" + file + "' does not have a valid extension of '" + "gpkg" + "' or '" + GeoPackageConstants.GEOPACKAGE_EXTENDED_EXTENSION + "'");
        }
    }

    public static boolean hasMinimumTables(GeoPackageCore geoPackageCore) {
        try {
            return geoPackageCore.getSpatialReferenceSystemDao().isTableExists() && geoPackageCore.getContentsDao().isTableExists();
        } catch (SQLException unused) {
            throw new GeoPackageException("Failed to check for required minimum GeoPackage tables. GeoPackage Name: " + geoPackageCore.getName());
        }
    }

    public static void validateMinimumTables(GeoPackageCore geoPackageCore) {
        if (!hasMinimumTables(geoPackageCore)) {
            throw new GeoPackageException("Invalid GeoPackage. Does not contain required tables: gpkg_spatial_ref_sys & gpkg_contents, GeoPackage Name: " + geoPackageCore.getName());
        }
    }
}
