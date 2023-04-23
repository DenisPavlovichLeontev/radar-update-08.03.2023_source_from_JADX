package org.osmdroid.views.overlay.advancedpolyline;

import java.util.ArrayList;
import java.util.List;

public abstract class ColorMappingForScalar implements ColorMapping {
    private final List<Integer> mColors = new ArrayList();

    /* access modifiers changed from: protected */
    public abstract int computeColor(float f);

    public int getColorForIndex(int i) {
        return this.mColors.get(i).intValue();
    }

    public void add(float f) {
        this.mColors.add(Integer.valueOf(computeColor(f)));
    }

    /* access modifiers changed from: protected */
    public void set(int i, float f) {
        this.mColors.set(i, Integer.valueOf(computeColor(f)));
    }
}
