package mil.nga.tiff;

public class ImageWindow {
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;

    public ImageWindow(int i, int i2, int i3, int i4) {
        this.minX = i;
        this.minY = i2;
        this.maxX = i3;
        this.maxY = i4;
    }

    public ImageWindow(int i, int i2) {
        this(i, i2, i + 1, i2 + 1);
    }

    public ImageWindow(FileDirectory fileDirectory) {
        this.minX = 0;
        this.minY = 0;
        this.maxX = fileDirectory.getImageWidth().intValue();
        this.maxY = fileDirectory.getImageHeight().intValue();
    }

    public int getMinX() {
        return this.minX;
    }

    public void setMinX(int i) {
        this.minX = i;
    }

    public int getMinY() {
        return this.minY;
    }

    public void setMinY(int i) {
        this.minY = i;
    }

    public int getMaxX() {
        return this.maxX;
    }

    public void setMaxX(int i) {
        this.maxX = i;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public void setMaxY(int i) {
        this.maxY = i;
    }

    public String toString() {
        return "ImageWindow [minX=" + this.minX + ", minY=" + this.minY + ", maxX=" + this.maxX + ", maxY=" + this.maxY + "]";
    }
}
