package mil.nga.geopackage;

public class GeoPackageException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public GeoPackageException() {
    }

    public GeoPackageException(String str) {
        super(str);
    }

    public GeoPackageException(String str, Throwable th) {
        super(str, th);
    }

    public GeoPackageException(Throwable th) {
        super(th);
    }
}
