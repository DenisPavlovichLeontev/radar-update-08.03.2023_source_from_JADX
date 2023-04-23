package mil.nga.geopackage.tiles.user;

import java.util.List;
import mil.nga.geopackage.p009db.GeoPackageDataType;
import mil.nga.geopackage.user.UserTableReader;

public class TileTableReader extends UserTableReader<TileColumn, TileTable, TileRow, TileCursor> {
    public TileTableReader(String str) {
        super(str);
    }

    /* access modifiers changed from: protected */
    public TileTable createTable(String str, List<TileColumn> list) {
        return new TileTable(str, list);
    }

    /* access modifiers changed from: protected */
    public TileColumn createColumn(TileCursor tileCursor, int i, String str, String str2, Long l, boolean z, int i2, boolean z2) {
        GeoPackageDataType fromName = GeoPackageDataType.fromName(str2);
        TileCursor tileCursor2 = tileCursor;
        return new TileColumn(i, str, fromName, l, z, tileCursor.getValue(i2, fromName), z2);
    }
}
