package org.osmdroid.util;

public class IntegerAccepter {
    private int mIndex;
    private final int[] mValues;

    public void end() {
    }

    public IntegerAccepter(int i) {
        this.mValues = new int[i];
    }

    public void init() {
        this.mIndex = 0;
    }

    public void add(int i) {
        int[] iArr = this.mValues;
        int i2 = this.mIndex;
        this.mIndex = i2 + 1;
        iArr[i2] = i;
    }

    public int getValue(int i) {
        return this.mValues[i];
    }

    public void flush() {
        this.mIndex = 0;
    }
}
