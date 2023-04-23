package mil.nga.geopackage.tiles.user;

import java.util.ArrayList;
import java.util.List;
import mil.nga.geopackage.p009db.GeoPackageDataType;
import mil.nga.geopackage.user.UserTable;
import mil.nga.geopackage.user.UserUniqueConstraint;

public class TileTable extends UserTable<TileColumn> {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TILE_COLUMN = "tile_column";
    public static final String COLUMN_TILE_DATA = "tile_data";
    public static final String COLUMN_TILE_ROW = "tile_row";
    public static final String COLUMN_ZOOM_LEVEL = "zoom_level";
    private final int tileColumnIndex;
    private final int tileDataIndex;
    private final int tileRowIndex;
    private final int zoomLevelIndex;

    public TileTable(String str, List<TileColumn> list) {
        super(str, list);
        UserUniqueConstraint userUniqueConstraint = new UserUniqueConstraint();
        Integer num = null;
        Integer num2 = null;
        Integer num3 = null;
        Integer num4 = null;
        for (TileColumn next : list) {
            String name = next.getName();
            int index = next.getIndex();
            if (name.equals("zoom_level")) {
                duplicateCheck(index, num, "zoom_level");
                typeCheck(GeoPackageDataType.INTEGER, next);
                num = Integer.valueOf(index);
                userUniqueConstraint.add(next);
            } else if (name.equals("tile_column")) {
                duplicateCheck(index, num2, "tile_column");
                typeCheck(GeoPackageDataType.INTEGER, next);
                num2 = Integer.valueOf(index);
                userUniqueConstraint.add(next);
            } else if (name.equals("tile_row")) {
                duplicateCheck(index, num3, "tile_row");
                typeCheck(GeoPackageDataType.INTEGER, next);
                num3 = Integer.valueOf(index);
                userUniqueConstraint.add(next);
            } else if (name.equals("tile_data")) {
                duplicateCheck(index, num4, "tile_data");
                typeCheck(GeoPackageDataType.BLOB, next);
                num4 = Integer.valueOf(index);
            }
        }
        missingCheck(num, "zoom_level");
        this.zoomLevelIndex = num.intValue();
        missingCheck(num2, "tile_column");
        this.tileColumnIndex = num2.intValue();
        missingCheck(num3, "tile_row");
        this.tileRowIndex = num3.intValue();
        missingCheck(num4, "tile_data");
        this.tileDataIndex = num4.intValue();
        addUniqueConstraint(userUniqueConstraint);
    }

    public int getZoomLevelColumnIndex() {
        return this.zoomLevelIndex;
    }

    public TileColumn getZoomLevelColumn() {
        return (TileColumn) getColumn(this.zoomLevelIndex);
    }

    public int getTileColumnColumnIndex() {
        return this.tileColumnIndex;
    }

    public TileColumn getTileColumnColumn() {
        return (TileColumn) getColumn(this.tileColumnIndex);
    }

    public int getTileRowColumnIndex() {
        return this.tileRowIndex;
    }

    public TileColumn getTileRowColumn() {
        return (TileColumn) getColumn(this.tileRowIndex);
    }

    public int getTileDataColumnIndex() {
        return this.tileDataIndex;
    }

    public TileColumn getTileDataColumn() {
        return (TileColumn) getColumn(this.tileDataIndex);
    }

    public static List<TileColumn> createRequiredColumns() {
        return createRequiredColumns(0);
    }

    public static List<TileColumn> createRequiredColumns(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = i + 1;
        arrayList.add(TileColumn.createIdColumn(i));
        int i3 = i2 + 1;
        arrayList.add(TileColumn.createZoomLevelColumn(i2));
        int i4 = i3 + 1;
        arrayList.add(TileColumn.createTileColumnColumn(i3));
        arrayList.add(TileColumn.createTileRowColumn(i4));
        arrayList.add(TileColumn.createTileDataColumn(i4 + 1));
        return arrayList;
    }
}
