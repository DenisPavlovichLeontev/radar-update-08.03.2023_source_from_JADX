package org.osmdroid.views.overlay.advancedpolyline;

import java.util.Map;
import java.util.SortedMap;

public class ColorMappingRanges extends ColorMappingForScalar {
    private final SortedMap<Float, Integer> mColorRanges;
    private final boolean mStrictComparison;

    public ColorMappingRanges(SortedMap<Float, Integer> sortedMap, boolean z) {
        this.mColorRanges = sortedMap;
        this.mStrictComparison = z;
    }

    /* access modifiers changed from: protected */
    public int computeColor(float f) {
        int i = 0;
        for (Map.Entry next : this.mColorRanges.entrySet()) {
            if (this.mStrictComparison) {
                if (f < ((Float) next.getKey()).floatValue()) {
                    return ((Integer) next.getValue()).intValue();
                }
            } else if (f <= ((Float) next.getKey()).floatValue()) {
                return ((Integer) next.getValue()).intValue();
            }
            i++;
        }
        if (i != this.mColorRanges.size()) {
            return 0;
        }
        SortedMap<Float, Integer> sortedMap = this.mColorRanges;
        return ((Integer) sortedMap.get(sortedMap.lastKey())).intValue();
    }
}
