package mil.nga.geopackage.extension;

import java.sql.SQLException;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;

public abstract class BaseExtension {
    protected final ExtensionsDao extensionsDao;
    protected final GeoPackageCore geoPackage;

    protected BaseExtension(GeoPackageCore geoPackageCore) {
        this.geoPackage = geoPackageCore;
        this.extensionsDao = geoPackageCore.getExtensionsDao();
    }

    public GeoPackageCore getGeoPackage() {
        return this.geoPackage;
    }

    public ExtensionsDao getExtensionsDao() {
        return this.extensionsDao;
    }

    /* access modifiers changed from: protected */
    public Extensions getOrCreate(String str, String str2, String str3, String str4, ExtensionScopeType extensionScopeType) {
        Extensions extensions = get(str, str2, str3);
        if (extensions != null) {
            return extensions;
        }
        try {
            if (!this.extensionsDao.isTableExists()) {
                this.geoPackage.createExtensionsTable();
            }
            Extensions extensions2 = new Extensions();
            extensions2.setTableName(str2);
            extensions2.setColumnName(str3);
            extensions2.setExtensionName(str);
            extensions2.setDefinition(str4);
            extensions2.setScope(extensionScopeType);
            this.extensionsDao.create(extensions2);
            return extensions2;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to create '" + str + "' extension for GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + str2 + ", Column Name: " + str3, e);
        }
    }

    /* access modifiers changed from: protected */
    public Extensions get(String str, String str2, String str3) {
        try {
            if (this.extensionsDao.isTableExists()) {
                return this.extensionsDao.queryByExtension(str, str2, str3);
            }
            return null;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to query for '" + str + "' extension for GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + str2 + ", Column Name: " + str3, e);
        }
    }

    /* access modifiers changed from: protected */
    public boolean has(String str, String str2, String str3) {
        return get(str, str2, str3) != null;
    }
}
