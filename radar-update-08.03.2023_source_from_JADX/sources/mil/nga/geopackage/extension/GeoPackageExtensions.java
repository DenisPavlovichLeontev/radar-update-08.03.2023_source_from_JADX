package mil.nga.geopackage.extension;

import java.sql.SQLException;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;

public class GeoPackageExtensions {
    public static void deleteTableExtensions(GeoPackageCore geoPackageCore, String str) {
        NGAExtensions.deleteTableExtensions(geoPackageCore, str);
        delete(geoPackageCore, str);
    }

    public static void deleteExtensions(GeoPackageCore geoPackageCore) {
        deleteExtensions(geoPackageCore, false);
    }

    public static void deleteExtensions(GeoPackageCore geoPackageCore, boolean z) {
        NGAExtensions.deleteExtensions(geoPackageCore, z);
        delete(geoPackageCore, z);
    }

    private static void delete(GeoPackageCore geoPackageCore, String str) {
        ExtensionsDao extensionsDao = geoPackageCore.getExtensionsDao();
        try {
            if (extensionsDao.isTableExists()) {
                extensionsDao.deleteByTableName(str);
            }
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete Table extensions. GeoPackage: " + geoPackageCore.getName() + ", Table: " + str, e);
        }
    }

    private static void delete(GeoPackageCore geoPackageCore, boolean z) {
        ExtensionsDao extensionsDao = geoPackageCore.getExtensionsDao();
        try {
            if (extensionsDao.isTableExists()) {
                extensionsDao.deleteAll();
            }
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete all extensions. GeoPackage: " + geoPackageCore.getName(), e);
        }
    }
}
