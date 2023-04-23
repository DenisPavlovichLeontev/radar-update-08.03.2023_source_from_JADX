package org.osmdroid.gpkg.overlay.features;

import android.graphics.drawable.Drawable;

public class MarkerOptions {
    float alpha;
    Drawable icon;
    String subdescription;
    String title;

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIcon(Drawable drawable) {
        this.icon = drawable;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getSubdescription() {
        return this.subdescription;
    }

    public void setSubdescription(String str) {
        this.subdescription = str;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(float f) {
        this.alpha = f;
    }
}
