package mil.nga.geopackage.extension;

import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.p009db.GeoPackageConnection;

public class RTreeIndexExtension extends RTreeIndexCoreExtension {
    private GeoPackageConnection connection;

    public RTreeIndexExtension(GeoPackage geoPackage) {
        super(geoPackage);
        this.connection = geoPackage.getConnection();
    }

    public void createMinXFunction() {
        createFunction(RTreeIndexCoreExtension.MIN_X_FUNCTION);
    }

    public void createMaxXFunction() {
        createFunction(RTreeIndexCoreExtension.MAX_X_FUNCTION);
    }

    public void createMinYFunction() {
        createFunction(RTreeIndexCoreExtension.MIN_Y_FUNCTION);
    }

    public void createMaxYFunction() {
        createFunction(RTreeIndexCoreExtension.MAX_Y_FUNCTION);
    }

    public void createIsEmptyFunction() {
        createFunction(RTreeIndexCoreExtension.IS_EMPTY_FUNCTION);
    }

    private void createFunction(String str) {
        throw new UnsupportedOperationException("User defined SQL functions are not supported. name: " + str);
    }
}
