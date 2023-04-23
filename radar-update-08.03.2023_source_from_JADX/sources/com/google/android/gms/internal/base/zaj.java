package com.google.android.gms.internal.base;

import android.graphics.drawable.Drawable;

/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
final class zaj extends Drawable.ConstantState {
    int zaa;
    int zab;

    zaj(zaj zaj) {
        if (zaj != null) {
            this.zaa = zaj.zaa;
            this.zab = zaj.zab;
        }
    }

    public final int getChangingConfigurations() {
        return this.zaa;
    }

    public final Drawable newDrawable() {
        return new zak(this);
    }
}
