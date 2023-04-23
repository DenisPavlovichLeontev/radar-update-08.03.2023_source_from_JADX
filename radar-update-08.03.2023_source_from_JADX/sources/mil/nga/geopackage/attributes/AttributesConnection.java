package mil.nga.geopackage.attributes;

import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.user.UserConnection;

public class AttributesConnection extends UserConnection<AttributesColumn, AttributesTable, AttributesRow, AttributesCursor> {
    public AttributesConnection(GeoPackageConnection geoPackageConnection) {
        super(geoPackageConnection);
    }
}
