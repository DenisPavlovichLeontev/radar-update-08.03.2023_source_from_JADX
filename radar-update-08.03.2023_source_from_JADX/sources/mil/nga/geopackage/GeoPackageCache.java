package mil.nga.geopackage;

public class GeoPackageCache extends GeoPackageCoreCache<GeoPackage> {
    private GeoPackageManager manager;

    public GeoPackageCache(GeoPackageManager geoPackageManager) {
        this.manager = geoPackageManager;
    }

    public GeoPackage getOrOpen(String str) {
        GeoPackage geoPackage = (GeoPackage) get(str);
        if (geoPackage != null) {
            return geoPackage;
        }
        GeoPackage open = this.manager.open(str);
        add(open);
        return open;
    }
}
