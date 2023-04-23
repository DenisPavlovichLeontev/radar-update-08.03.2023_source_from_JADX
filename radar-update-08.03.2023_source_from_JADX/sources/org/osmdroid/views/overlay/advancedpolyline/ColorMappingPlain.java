package org.osmdroid.views.overlay.advancedpolyline;

public class ColorMappingPlain implements ColorMapping {
    private final int mColorPlain;

    public ColorMappingPlain(int i) {
        this.mColorPlain = i;
    }

    public int getColorForIndex(int i) {
        return this.mColorPlain;
    }
}
