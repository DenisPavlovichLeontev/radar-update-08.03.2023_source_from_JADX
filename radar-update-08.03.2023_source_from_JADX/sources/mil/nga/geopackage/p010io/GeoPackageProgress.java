package mil.nga.geopackage.p010io;

/* renamed from: mil.nga.geopackage.io.GeoPackageProgress */
public interface GeoPackageProgress {
    void addProgress(int i);

    boolean cleanupOnCancel();

    boolean isActive();

    void setMax(int i);
}
