package mil.nga.geopackage.extension.coverage;

public class CoverageDataSourcePixel {
    private int max;
    private int min;
    private float offset;
    private float pixel;

    public CoverageDataSourcePixel(float f, int i, int i2, float f2) {
        this.pixel = f;
        this.min = i;
        this.max = i2;
        this.offset = f2;
    }

    public float getPixel() {
        return this.pixel;
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }

    public float getOffset() {
        return this.offset;
    }

    public void setPixel(float f) {
        this.pixel = f;
    }

    public void setMin(int i) {
        this.min = i;
    }

    public void setMax(int i) {
        this.max = i;
    }

    public void setOffset(float f) {
        this.offset = f;
    }
}
