package mil.nga.geopackage.extension;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.index.FeatureTableCoreIndex;
import mil.nga.geopackage.extension.index.GeometryIndex;
import mil.nga.geopackage.extension.index.GeometryIndexDao;
import mil.nga.geopackage.extension.index.TableIndex;
import mil.nga.geopackage.extension.index.TableIndexDao;
import mil.nga.geopackage.extension.link.FeatureTileLink;
import mil.nga.geopackage.extension.link.FeatureTileLinkDao;
import mil.nga.geopackage.extension.link.FeatureTileTableCoreLinker;

public class NGAExtensions {
    public static void deleteTableExtensions(GeoPackageCore geoPackageCore, String str) {
        deleteGeometryIndex(geoPackageCore, str);
        deleteFeatureTileLink(geoPackageCore, str);
    }

    public static void deleteExtensions(GeoPackageCore geoPackageCore) {
        deleteExtensions(geoPackageCore, false);
    }

    public static void deleteExtensions(GeoPackageCore geoPackageCore, boolean z) {
        deleteGeometryIndexExtension(geoPackageCore, z);
        deleteFeatureTileLinkExtension(geoPackageCore, z);
    }

    public static void deleteGeometryIndex(GeoPackageCore geoPackageCore, String str) {
        TableIndexDao tableIndexDao = geoPackageCore.getTableIndexDao();
        ExtensionsDao extensionsDao = geoPackageCore.getExtensionsDao();
        try {
            if (tableIndexDao.isTableExists()) {
                tableIndexDao.deleteByIdCascade(str);
            }
            if (extensionsDao.isTableExists()) {
                extensionsDao.deleteByExtension(FeatureTableCoreIndex.EXTENSION_NAME, str);
            }
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete Table Index. GeoPackage: " + geoPackageCore.getName() + ", Table: " + str, e);
        }
    }

    public static void deleteGeometryIndexExtension(GeoPackageCore geoPackageCore, boolean z) {
        GeometryIndexDao geometryIndexDao = geoPackageCore.getGeometryIndexDao();
        TableIndexDao tableIndexDao = geoPackageCore.getTableIndexDao();
        ExtensionsDao extensionsDao = geoPackageCore.getExtensionsDao();
        ConnectionSource connectionSource = geoPackageCore.getDatabase().getConnectionSource();
        try {
            if (geometryIndexDao.isTableExists()) {
                TableUtils.dropTable(connectionSource, GeometryIndex.class, z);
            }
            if (tableIndexDao.isTableExists()) {
                TableUtils.dropTable(connectionSource, TableIndex.class, z);
            }
            if (extensionsDao.isTableExists()) {
                extensionsDao.deleteByExtension(FeatureTableCoreIndex.EXTENSION_NAME);
            }
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete Table Index extension and tables. GeoPackage: " + geoPackageCore.getName(), e);
        }
    }

    public static void deleteFeatureTileLink(GeoPackageCore geoPackageCore, String str) {
        FeatureTileLinkDao featureTileLinkDao = geoPackageCore.getFeatureTileLinkDao();
        try {
            if (featureTileLinkDao.isTableExists()) {
                featureTileLinkDao.deleteByTableName(str);
            }
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete Feature Tile Link. GeoPackage: " + geoPackageCore.getName() + ", Table: " + str, e);
        }
    }

    public static void deleteFeatureTileLinkExtension(GeoPackageCore geoPackageCore, boolean z) {
        FeatureTileLinkDao featureTileLinkDao = geoPackageCore.getFeatureTileLinkDao();
        ExtensionsDao extensionsDao = geoPackageCore.getExtensionsDao();
        ConnectionSource connectionSource = geoPackageCore.getDatabase().getConnectionSource();
        try {
            if (featureTileLinkDao.isTableExists()) {
                TableUtils.dropTable(connectionSource, FeatureTileLink.class, z);
            }
            if (extensionsDao.isTableExists()) {
                extensionsDao.deleteByExtension(FeatureTileTableCoreLinker.EXTENSION_NAME);
            }
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete Feature Tile Link extension and table. GeoPackage: " + geoPackageCore.getName(), e);
        }
    }
}
