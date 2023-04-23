package mil.nga.geopackage.tiles.user;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p009db.GeoPackageDataType;
import mil.nga.geopackage.user.UserColumn;

public class TileColumn extends UserColumn {
    public static TileColumn createIdColumn(int i) {
        return new TileColumn(i, "id", GeoPackageDataType.INTEGER, (Long) null, false, (Object) null, true);
    }

    public static TileColumn createZoomLevelColumn(int i) {
        return new TileColumn(i, "zoom_level", GeoPackageDataType.INTEGER, (Long) null, true, 0, false);
    }

    public static TileColumn createTileColumnColumn(int i) {
        return new TileColumn(i, "tile_column", GeoPackageDataType.INTEGER, (Long) null, true, 0, false);
    }

    public static TileColumn createTileRowColumn(int i) {
        return new TileColumn(i, "tile_row", GeoPackageDataType.INTEGER, (Long) null, true, 0, false);
    }

    public static TileColumn createTileDataColumn(int i) {
        return new TileColumn(i, "tile_data", GeoPackageDataType.BLOB, (Long) null, true, (Object) null, false);
    }

    public static TileColumn createColumn(int i, String str, GeoPackageDataType geoPackageDataType, boolean z, Object obj) {
        return createColumn(i, str, geoPackageDataType, (Long) null, z, obj);
    }

    public static TileColumn createColumn(int i, String str, GeoPackageDataType geoPackageDataType, Long l, boolean z, Object obj) {
        return new TileColumn(i, str, geoPackageDataType, l, z, obj, false);
    }

    TileColumn(int i, String str, GeoPackageDataType geoPackageDataType, Long l, boolean z, Object obj, boolean z2) {
        super(i, str, geoPackageDataType, l, z, obj, z2);
        if (geoPackageDataType == null) {
            throw new GeoPackageException("Data Type is required to create column: " + str);
        }
    }
}
