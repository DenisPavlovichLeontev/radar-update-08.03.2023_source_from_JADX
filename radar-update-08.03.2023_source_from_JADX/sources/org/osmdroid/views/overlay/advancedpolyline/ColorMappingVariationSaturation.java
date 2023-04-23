package org.osmdroid.views.overlay.advancedpolyline;

public class ColorMappingVariationSaturation extends ColorMappingVariation {
    private float mHue;
    private float mLuminance;

    public ColorMappingVariationSaturation(float f, float f2, float f3, float f4, float f5, float f6) {
        float constrain = ColorHelper.constrain(f3, 0.0f, 1.0f);
        float constrain2 = ColorHelper.constrain(f4, 0.0f, 1.0f);
        this.mHue = ColorHelper.constrain(f5, 0.0f, 360.0f);
        this.mLuminance = ColorHelper.constrain(f6, 0.0f, 1.0f);
        init(f, f2, constrain, constrain2);
    }

    /* access modifiers changed from: protected */
    public float getHue(float f) {
        return this.mHue;
    }

    /* access modifiers changed from: protected */
    public float getSaturation(float f) {
        return mapScalar(f);
    }

    /* access modifiers changed from: protected */
    public float getLuminance(float f) {
        return this.mLuminance;
    }
}
