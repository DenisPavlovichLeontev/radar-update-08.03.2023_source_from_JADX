package mil.nga.geopackage.tiles.features;

import android.graphics.Bitmap;

public class FeatureTilePointIcon {
    private final int height;
    private final Bitmap icon;
    private final int width;
    private float xOffset = 0.0f;
    private float yOffset = 0.0f;

    public FeatureTilePointIcon(Bitmap bitmap) {
        this.icon = bitmap;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        pinIcon();
    }

    public void pinIcon() {
        this.xOffset = ((float) this.width) / 2.0f;
        this.yOffset = (float) this.height;
    }

    public void centerIcon() {
        this.xOffset = ((float) this.width) / 2.0f;
        this.yOffset = ((float) this.height) / 2.0f;
    }

    public Bitmap getIcon() {
        return this.icon;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float getXOffset() {
        return this.xOffset;
    }

    public void setXOffset(float f) {
        this.xOffset = f;
    }

    public float getYOffset() {
        return this.yOffset;
    }

    public void setYOffset(float f) {
        this.yOffset = f;
    }
}
