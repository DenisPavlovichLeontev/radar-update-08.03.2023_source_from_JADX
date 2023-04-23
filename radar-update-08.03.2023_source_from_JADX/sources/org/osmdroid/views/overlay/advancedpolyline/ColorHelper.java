package org.osmdroid.views.overlay.advancedpolyline;

import android.graphics.Color;

public class ColorHelper {
    public static float constrain(float f, float f2, float f3) {
        return f < f2 ? f2 : f > f3 ? f3 : f;
    }

    private static int constrain(int i, int i2, int i3) {
        return i < i2 ? i2 : i > i3 ? i3 : i;
    }

    public static int HSLToColor(float f, float f2, float f3) {
        int i;
        int i2;
        int i3;
        int i4;
        float abs = (1.0f - Math.abs((f3 * 2.0f) - 1.0f)) * f2;
        float f4 = f3 - (0.5f * abs);
        float abs2 = (1.0f - Math.abs(((f / 60.0f) % 2.0f) - 1.0f)) * abs;
        switch (((int) f) / 60) {
            case 0:
                i2 = Math.round((abs + f4) * 255.0f);
                i3 = Math.round((abs2 + f4) * 255.0f);
                i = Math.round(f4 * 255.0f);
                break;
            case 1:
                i2 = Math.round((abs2 + f4) * 255.0f);
                i3 = Math.round((abs + f4) * 255.0f);
                i = Math.round(f4 * 255.0f);
                break;
            case 2:
                i2 = Math.round(f4 * 255.0f);
                i3 = Math.round((abs + f4) * 255.0f);
                i = Math.round((abs2 + f4) * 255.0f);
                break;
            case 3:
                i2 = Math.round(f4 * 255.0f);
                i4 = Math.round((abs2 + f4) * 255.0f);
                i = Math.round((abs + f4) * 255.0f);
                break;
            case 4:
                i2 = Math.round((abs2 + f4) * 255.0f);
                i4 = Math.round(f4 * 255.0f);
                i = Math.round((abs + f4) * 255.0f);
                break;
            case 5:
            case 6:
                i2 = Math.round((abs + f4) * 255.0f);
                i3 = Math.round(f4 * 255.0f);
                i = Math.round((abs2 + f4) * 255.0f);
                break;
            default:
                i2 = 0;
                i = 0;
                i3 = 0;
                break;
        }
        i3 = i4;
        return Color.rgb(constrain(i2, 0, 255), constrain(i3, 0, 255), constrain(i, 0, 255));
    }
}
