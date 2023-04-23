package org.osmdroid.tileprovider;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ExpirableBitmapDrawable extends BitmapDrawable {
    public static final int EXPIRED = -2;
    public static final int NOT_FOUND = -4;
    public static final int SCALED = -3;
    public static final int UP_TO_DATE = -1;
    private static final int defaultStatus = -1;
    private static final int[] settableStatuses = {-2, -3, -4};
    private int[] mState = new int[0];

    public ExpirableBitmapDrawable(Bitmap bitmap) {
        super(bitmap);
    }

    public int[] getState() {
        return this.mState;
    }

    public boolean isStateful() {
        return this.mState.length > 0;
    }

    public boolean setState(int[] iArr) {
        this.mState = iArr;
        return true;
    }

    @Deprecated
    public static boolean isDrawableExpired(Drawable drawable) {
        return getState(drawable) == -2;
    }

    public static int getState(Drawable drawable) {
        for (int i : drawable.getState()) {
            for (int i2 : settableStatuses) {
                if (i == i2) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Deprecated
    public static void setDrawableExpired(Drawable drawable) {
        setState(drawable, -2);
    }

    public static void setState(Drawable drawable, int i) {
        drawable.setState(new int[]{i});
    }
}
