package org.osmdroid.views.overlay.advancedpolyline;

import java.util.ArrayList;
import java.util.List;

public class ColorMappingForScalarContainer {
    private final ColorMappingForScalar mInnerMapping;
    private float mScalarMax = Float.MIN_VALUE;
    private float mScalarMin = Float.MAX_VALUE;
    private final List<Float> mScalars = new ArrayList();

    public ColorMappingForScalarContainer(ColorMappingForScalar colorMappingForScalar) {
        this.mInnerMapping = colorMappingForScalar;
    }

    public ColorMappingForScalar getMappingForScalar() {
        return this.mInnerMapping;
    }

    public int size() {
        return this.mScalars.size();
    }

    public float getScalarMin() {
        return this.mScalarMin;
    }

    public float getScalarMax() {
        return this.mScalarMax;
    }

    public void add(float f) {
        this.mInnerMapping.add(f);
        this.mScalars.add(Float.valueOf(f));
        if (this.mScalarMin > f) {
            this.mScalarMin = f;
        }
        if (this.mScalarMax < f) {
            this.mScalarMax = f;
        }
    }

    public void refresh() {
        int i = 0;
        for (Float floatValue : this.mScalars) {
            this.mInnerMapping.set(i, floatValue.floatValue());
            i++;
        }
    }
}
