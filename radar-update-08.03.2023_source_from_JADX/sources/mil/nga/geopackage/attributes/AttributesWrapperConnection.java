package mil.nga.geopackage.attributes;

import android.database.Cursor;
import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.user.UserWrapperConnection;

public class AttributesWrapperConnection extends UserWrapperConnection<AttributesColumn, AttributesTable, AttributesRow, AttributesCursor> {
    public AttributesWrapperConnection(GeoPackageConnection geoPackageConnection) {
        super(geoPackageConnection);
    }

    /* access modifiers changed from: protected */
    public AttributesCursor wrapCursor(Cursor cursor) {
        return new AttributesCursor((AttributesTable) null, cursor);
    }
}
