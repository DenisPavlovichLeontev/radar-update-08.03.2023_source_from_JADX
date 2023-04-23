package org.osmdroid.views.overlay.advancedpolyline;

import java.util.List;

public class ColorMappingCycle implements ColorMapping {
    private final int[] mColorArray;
    private final List<Integer> mColorList;
    private int mGeoPointNumber;

    public ColorMappingCycle(List<Integer> list) {
        this.mColorList = list;
        this.mColorArray = null;
    }

    public ColorMappingCycle(int[] iArr) {
        this.mColorList = null;
        this.mColorArray = iArr;
    }

    public void setGeoPointNumber(int i) {
        this.mGeoPointNumber = i;
    }

    public int getColorForIndex(int i) {
        int i2 = this.mGeoPointNumber;
        if (i2 > 0 && i >= i2) {
            i = 0;
        }
        int[] iArr = this.mColorArray;
        if (iArr != null) {
            return iArr[i % iArr.length];
        }
        List<Integer> list = this.mColorList;
        if (list != null) {
            return list.get(i % list.size()).intValue();
        }
        throw new IllegalArgumentException();
    }
}
