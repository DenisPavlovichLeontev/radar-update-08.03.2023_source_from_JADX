package mil.nga.geopackage.tiles.user;

import android.database.Cursor;
import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.user.UserWrapperConnection;

public class TileWrapperConnection extends UserWrapperConnection<TileColumn, TileTable, TileRow, TileCursor> {
    public TileWrapperConnection(GeoPackageConnection geoPackageConnection) {
        super(geoPackageConnection);
    }

    /* access modifiers changed from: protected */
    public TileCursor wrapCursor(Cursor cursor) {
        return new TileCursor((TileTable) null, cursor);
    }
}
