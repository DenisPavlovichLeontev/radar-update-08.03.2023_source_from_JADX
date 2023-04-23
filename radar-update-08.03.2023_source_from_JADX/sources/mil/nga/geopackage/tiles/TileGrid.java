package mil.nga.geopackage.tiles;

public class TileGrid {
    private long maxX;
    private long maxY;
    private long minX;
    private long minY;

    public TileGrid(long j, long j2, long j3, long j4) {
        this.minX = j;
        this.minY = j2;
        this.maxX = j3;
        this.maxY = j4;
    }

    public long getMinX() {
        return this.minX;
    }

    public void setMinX(long j) {
        this.minX = j;
    }

    public long getMaxX() {
        return this.maxX;
    }

    public void setMaxX(long j) {
        this.maxX = j;
    }

    public long getMinY() {
        return this.minY;
    }

    public void setMinY(long j) {
        this.minY = j;
    }

    public long getMaxY() {
        return this.maxY;
    }

    public void setMaxY(long j) {
        this.maxY = j;
    }

    public long count() {
        return ((this.maxX + 1) - this.minX) * ((this.maxY + 1) - this.minY);
    }

    public int hashCode() {
        long j = this.maxX;
        long j2 = this.maxY;
        long j3 = this.minX;
        long j4 = this.minY;
        return ((((((((int) (j ^ (j >>> 32))) + 31) * 31) + ((int) (j2 ^ (j2 >>> 32)))) * 31) + ((int) (j3 ^ (j3 >>> 32)))) * 31) + ((int) ((j4 >>> 32) ^ j4));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TileGrid tileGrid = (TileGrid) obj;
        return this.maxX == tileGrid.maxX && this.maxY == tileGrid.maxY && this.minX == tileGrid.minX && this.minY == tileGrid.minY;
    }
}
