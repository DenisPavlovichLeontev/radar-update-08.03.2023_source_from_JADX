package mil.nga.geopackage.tiles.user;

import java.util.List;
import mil.nga.geopackage.user.UserInvalidCursor;

public class TileInvalidCursor extends UserInvalidCursor<TileColumn, TileTable, TileRow, TileCursor, TileDao> {
    public TileInvalidCursor(TileDao tileDao, TileCursor tileCursor, List<Integer> list, List<TileColumn> list2) {
        super(tileDao, tileCursor, list, list2);
    }
}
