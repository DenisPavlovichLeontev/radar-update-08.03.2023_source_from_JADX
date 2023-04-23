package mil.nga.geopackage.tiles.retriever;

public class GeoPackageTile {
    public final byte[] data;
    public final int height;
    public final int width;

    public GeoPackageTile(int i, int i2, byte[] bArr) {
        this.width = i;
        this.height = i2;
        this.data = bArr;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public byte[] getData() {
        return this.data;
    }
}
