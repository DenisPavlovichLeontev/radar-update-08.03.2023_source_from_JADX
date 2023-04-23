package org.osmdroid.views.overlay.advancedpolyline;

public class ColorMappingVariationLuminance extends ColorMappingVariation {
    private float mHue;
    private float mSaturation;

    public ColorMappingVariationLuminance(float f, float f2, float f3, float f4, float f5, float f6) {
        float constrain = ColorHelper.constrain(f3, 0.0f, 1.0f);
        float constrain2 = ColorHelper.constrain(f4, 0.0f, 1.0f);
        this.mHue = ColorHelper.constrain(f5, 0.0f, 360.0f);
        this.mSaturation = ColorHelper.constrain(f6, 0.0f, 1.0f);
        init(f, f2, constrain, constrain2);
    }

    /* access modifiers changed from: protected */
    public float getHue(float f) {
        return this.mHue;
    }

    /* access modifiers changed from: protected */
    public float getSaturation(float f) {
        return this.mSaturation;
    }

    /* access modifiers changed from: protected */
    public float getLuminance(float f) {
        return mapScalar(f);
    }
}
