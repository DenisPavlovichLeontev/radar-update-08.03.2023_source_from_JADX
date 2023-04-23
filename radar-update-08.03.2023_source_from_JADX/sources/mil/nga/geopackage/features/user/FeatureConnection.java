package mil.nga.geopackage.features.user;

import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.user.UserConnection;

public class FeatureConnection extends UserConnection<FeatureColumn, FeatureTable, FeatureRow, FeatureCursor> {
    public FeatureConnection(GeoPackageConnection geoPackageConnection) {
        super(geoPackageConnection);
    }
}
