package org.osmdroid.util;

public abstract class LineBuilder implements PointAccepter {
    private int mIndex;
    private final float[] mLines;

    public abstract void flush();

    public LineBuilder(int i) {
        this.mLines = new float[i];
    }

    public void init() {
        this.mIndex = 0;
    }

    public void add(long j, long j2) {
        float[] fArr = this.mLines;
        int i = this.mIndex;
        int i2 = i + 1;
        fArr[i] = (float) j;
        int i3 = i2 + 1;
        this.mIndex = i3;
        fArr[i2] = (float) j2;
        if (i3 >= fArr.length) {
            innerFlush();
        }
    }

    public void end() {
        innerFlush();
    }

    public float[] getLines() {
        return this.mLines;
    }

    public int getSize() {
        return this.mIndex;
    }

    private void innerFlush() {
        if (this.mIndex > 0) {
            flush();
        }
        this.mIndex = 0;
    }
}
