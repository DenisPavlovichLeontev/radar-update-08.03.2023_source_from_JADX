package mil.nga.geopackage;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Set;
import mil.nga.geopackage.p010io.GeoPackageProgress;

public interface GeoPackageManager {
    boolean copy(String str, String str2);

    int count();

    boolean create(String str);

    boolean createAtPath(String str, File file);

    boolean createFile(File file);

    boolean createFile(String str, File file);

    Set<String> databaseSet();

    List<String> databases();

    List<String> databasesLike(String str);

    List<String> databasesNotLike(String str);

    boolean delete(String str);

    boolean deleteAll();

    boolean deleteAllExternal();

    boolean deleteAllMissingExternal();

    boolean exists(String str);

    boolean existsAtExternalFile(File file);

    boolean existsAtExternalPath(String str);

    void exportGeoPackage(String str, File file);

    void exportGeoPackage(String str, String str2, File file);

    int externalCount();

    Set<String> externalDatabaseSet();

    List<String> externalDatabases();

    String getDatabaseAtExternalFile(File file);

    String getDatabaseAtExternalPath(String str);

    File getFile(String str);

    String getPath(String str);

    boolean importGeoPackage(File file);

    boolean importGeoPackage(File file, boolean z);

    boolean importGeoPackage(String str, File file);

    boolean importGeoPackage(String str, File file, boolean z);

    boolean importGeoPackage(String str, InputStream inputStream);

    boolean importGeoPackage(String str, InputStream inputStream, GeoPackageProgress geoPackageProgress);

    boolean importGeoPackage(String str, InputStream inputStream, boolean z);

    boolean importGeoPackage(String str, InputStream inputStream, boolean z, GeoPackageProgress geoPackageProgress);

    boolean importGeoPackage(String str, URL url);

    boolean importGeoPackage(String str, URL url, GeoPackageProgress geoPackageProgress);

    boolean importGeoPackage(String str, URL url, boolean z);

    boolean importGeoPackage(String str, URL url, boolean z, GeoPackageProgress geoPackageProgress);

    boolean importGeoPackageAsExternalLink(File file, String str);

    boolean importGeoPackageAsExternalLink(File file, String str, boolean z);

    boolean importGeoPackageAsExternalLink(String str, String str2);

    boolean importGeoPackageAsExternalLink(String str, String str2, boolean z);

    int internalCount();

    Set<String> internalDatabaseSet();

    List<String> internalDatabases();

    boolean isExternal(String str);

    boolean isImportHeaderValidation();

    boolean isImportIntegrityValidation();

    boolean isOpenHeaderValidation();

    boolean isOpenIntegrityValidation();

    GeoPackage open(String str);

    GeoPackage open(String str, boolean z);

    String readableSize(String str);

    boolean rename(String str, String str2);

    void setImportHeaderValidation(boolean z);

    void setImportIntegrityValidation(boolean z);

    void setOpenHeaderValidation(boolean z);

    void setOpenIntegrityValidation(boolean z);

    long size(String str);

    boolean validate(String str);

    boolean validateHeader(String str);

    boolean validateIntegrity(String str);
}
