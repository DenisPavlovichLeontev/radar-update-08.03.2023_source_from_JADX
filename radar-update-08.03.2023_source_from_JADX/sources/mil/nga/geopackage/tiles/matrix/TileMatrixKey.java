package mil.nga.geopackage.tiles.matrix;

public class TileMatrixKey {
    private String tableName;
    private long zoomLevel;

    public TileMatrixKey(String str, long j) {
        this.tableName = str;
        this.zoomLevel = j;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String str) {
        this.tableName = str;
    }

    public long getZoomLevel() {
        return this.zoomLevel;
    }

    public void setZoomLevel(long j) {
        this.zoomLevel = j;
    }

    public String toString() {
        return this.tableName + ":" + this.zoomLevel;
    }

    public int hashCode() {
        int i;
        String str = this.tableName;
        if (str == null) {
            i = 0;
        } else {
            i = str.hashCode();
        }
        long j = this.zoomLevel;
        return ((i + 31) * 31) + ((int) (j ^ (j >>> 32)));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TileMatrixKey tileMatrixKey = (TileMatrixKey) obj;
        String str = this.tableName;
        if (str == null) {
            if (tileMatrixKey.tableName != null) {
                return false;
            }
        } else if (!str.equals(tileMatrixKey.tableName)) {
            return false;
        }
        return this.zoomLevel == tileMatrixKey.zoomLevel;
    }
}
