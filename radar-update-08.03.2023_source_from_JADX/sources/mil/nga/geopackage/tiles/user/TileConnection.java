package mil.nga.geopackage.tiles.user;

import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.user.UserConnection;

public class TileConnection extends UserConnection<TileColumn, TileTable, TileRow, TileCursor> {
    public TileConnection(GeoPackageConnection geoPackageConnection) {
        super(geoPackageConnection);
    }
}
