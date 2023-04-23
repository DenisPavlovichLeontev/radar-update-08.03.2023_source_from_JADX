package mil.nga.geopackage.extension.link;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

public abstract class FeatureTileTableCoreLinker extends BaseExtension {
    public static final String EXTENSION_AUTHOR = "nga";
    public static final String EXTENSION_DEFINITION = GeoPackageProperties.getProperty(PropertyConstants.EXTENSIONS, EXTENSION_NAME_NO_AUTHOR);
    public static final String EXTENSION_NAME = Extensions.buildExtensionName("nga", EXTENSION_NAME_NO_AUTHOR);
    public static final String EXTENSION_NAME_NO_AUTHOR = "feature_tile_link";
    private final FeatureTileLinkDao featureTileLinkDao;

    protected FeatureTileTableCoreLinker(GeoPackageCore geoPackageCore) {
        super(geoPackageCore);
        this.featureTileLinkDao = geoPackageCore.getFeatureTileLinkDao();
    }

    public GeoPackageCore getGeoPackage() {
        return this.geoPackage;
    }

    public FeatureTileLinkDao getDao() {
        return this.featureTileLinkDao;
    }

    public void link(String str, String str2) {
        if (!isLinked(str, str2)) {
            getOrCreateExtension();
            try {
                if (!this.featureTileLinkDao.isTableExists()) {
                    this.geoPackage.createFeatureTileLinkTable();
                }
                FeatureTileLink featureTileLink = new FeatureTileLink();
                featureTileLink.setFeatureTableName(str);
                featureTileLink.setTileTableName(str2);
                this.featureTileLinkDao.create(featureTileLink);
            } catch (SQLException e) {
                throw new GeoPackageException("Failed to create feature tile link for GeoPackage: " + this.geoPackage.getName() + ", Feature Table: " + str + ", Tile Table: " + str2, e);
            }
        }
    }

    public boolean isLinked(String str, String str2) {
        return getLink(str, str2) != null;
    }

    public FeatureTileLink getLink(String str, String str2) {
        if (!featureTileLinksActive()) {
            return null;
        }
        try {
            return this.featureTileLinkDao.queryForId(new FeatureTileLinkKey(str, str2));
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to get feature tile link for GeoPackage: " + this.geoPackage.getName() + ", Feature Table: " + str + ", Tile Table: " + str2, e);
        }
    }

    public List<FeatureTileLink> queryForFeatureTable(String str) {
        if (featureTileLinksActive()) {
            return this.featureTileLinkDao.queryForFeatureTableName(str);
        }
        return new ArrayList();
    }

    public List<FeatureTileLink> queryForTileTable(String str) {
        if (featureTileLinksActive()) {
            return this.featureTileLinkDao.queryForTileTableName(str);
        }
        return new ArrayList();
    }

    public boolean deleteLink(String str, String str2) {
        try {
            if (!this.featureTileLinkDao.isTableExists()) {
                return false;
            }
            if (this.featureTileLinkDao.deleteById(new FeatureTileLinkKey(str, str2)) > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete feature tile link for GeoPackage: " + this.geoPackage.getName() + ", Feature Table: " + str + ", Tile Table: " + str2, e);
        }
    }

    public int deleteLinks(String str) {
        try {
            if (this.featureTileLinkDao.isTableExists()) {
                return this.featureTileLinkDao.deleteByTableName(str);
            }
            return 0;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete feature tile links for GeoPackage: " + this.geoPackage.getName() + ", Table: " + str, e);
        }
    }

    private Extensions getOrCreateExtension() {
        return getOrCreate(EXTENSION_NAME, (String) null, (String) null, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);
    }

    public Extensions getExtension() {
        return get(EXTENSION_NAME, (String) null, (String) null);
    }

    private boolean featureTileLinksActive() {
        if (getExtension() == null) {
            return false;
        }
        try {
            return this.featureTileLinkDao.isTableExists();
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to check if the feature tile link table exists for GeoPackage: " + this.geoPackage.getName(), e);
        }
    }

    public List<String> getTileTablesForFeatureTable(String str) {
        ArrayList arrayList = new ArrayList();
        for (FeatureTileLink tileTableName : queryForFeatureTable(str)) {
            arrayList.add(tileTableName.getTileTableName());
        }
        return arrayList;
    }

    public List<String> getFeatureTablesForTileTable(String str) {
        ArrayList arrayList = new ArrayList();
        for (FeatureTileLink featureTableName : queryForTileTable(str)) {
            arrayList.add(featureTableName.getFeatureTableName());
        }
        return arrayList;
    }
}
