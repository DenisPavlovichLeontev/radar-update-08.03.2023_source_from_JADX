package mil.nga.geopackage.features.user;

import android.database.Cursor;
import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.user.UserWrapperConnection;

public class FeatureWrapperConnection extends UserWrapperConnection<FeatureColumn, FeatureTable, FeatureRow, FeatureCursor> {
    public FeatureWrapperConnection(GeoPackageConnection geoPackageConnection) {
        super(geoPackageConnection);
    }

    /* access modifiers changed from: protected */
    public FeatureCursor wrapCursor(Cursor cursor) {
        return new FeatureCursor((FeatureTable) null, cursor);
    }
}
