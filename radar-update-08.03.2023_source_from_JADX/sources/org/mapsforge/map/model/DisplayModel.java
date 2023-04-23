package org.mapsforge.map.model;

import org.mapsforge.core.graphics.Filter;
import org.mapsforge.map.model.common.Observable;
import org.mapsforge.map.rendertheme.ThemeCallback;

public class DisplayModel extends Observable {
    private static final int DEFAULT_BACKGROUND_COLOR = -1118482;
    private static final int DEFAULT_MAX_TEXT_WIDTH = 179;
    private static final float DEFAULT_MAX_TEXT_WIDTH_FACTOR = 0.7f;
    private static final int DEFAULT_TILE_SIZE = 256;
    private static float defaultUserScaleFactor = 1.0f;
    private static float deviceScaleFactor = 1.0f;
    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private Filter filter = Filter.NONE;
    private int fixedTileSize;
    private int maxTextWidth = DEFAULT_MAX_TEXT_WIDTH;
    private float maxTextWidthFactor = DEFAULT_MAX_TEXT_WIDTH_FACTOR;
    private ThemeCallback themeCallback;
    private int tileSize = 256;
    private int tileSizeMultiple = 64;
    private float userScaleFactor = defaultUserScaleFactor;

    public static synchronized float getDefaultUserScaleFactor() {
        float f;
        synchronized (DisplayModel.class) {
            f = defaultUserScaleFactor;
        }
        return f;
    }

    public static synchronized float getDeviceScaleFactor() {
        float f;
        synchronized (DisplayModel.class) {
            f = deviceScaleFactor;
        }
        return f;
    }

    public static synchronized void setDefaultUserScaleFactor(float f) {
        synchronized (DisplayModel.class) {
            defaultUserScaleFactor = f;
        }
    }

    public static synchronized void setDeviceScaleFactor(float f) {
        synchronized (DisplayModel.class) {
            deviceScaleFactor = f;
        }
    }

    public DisplayModel() {
        setTileSize();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DisplayModel)) {
            return false;
        }
        DisplayModel displayModel = (DisplayModel) obj;
        return this.backgroundColor == displayModel.backgroundColor && this.filter == displayModel.filter && this.fixedTileSize == displayModel.fixedTileSize && this.maxTextWidth == displayModel.maxTextWidth && Float.floatToIntBits(this.maxTextWidthFactor) == Float.floatToIntBits(displayModel.maxTextWidthFactor) && this.tileSize == displayModel.tileSize && this.tileSizeMultiple == displayModel.tileSizeMultiple && Float.floatToIntBits(this.userScaleFactor) == Float.floatToIntBits(displayModel.userScaleFactor);
    }

    public synchronized int getBackgroundColor() {
        return this.backgroundColor;
    }

    public synchronized Filter getFilter() {
        return this.filter;
    }

    public int getMaxTextWidth() {
        return this.maxTextWidth;
    }

    public synchronized float getScaleFactor() {
        return deviceScaleFactor * this.userScaleFactor;
    }

    public synchronized ThemeCallback getThemeCallback() {
        return this.themeCallback;
    }

    public synchronized int getTileSize() {
        return this.tileSize;
    }

    public synchronized int getTileSizeMultiple() {
        return this.tileSizeMultiple;
    }

    public synchronized float getUserScaleFactor() {
        return this.userScaleFactor;
    }

    public int hashCode() {
        return ((((((((((((((this.backgroundColor + 31) * 31) + this.filter.hashCode()) * 31) + this.fixedTileSize) * 31) + this.maxTextWidth) * 31) + Float.floatToIntBits(this.maxTextWidthFactor)) * 31) + this.tileSize) * 31) + this.tileSizeMultiple) * 31) + Float.floatToIntBits(this.userScaleFactor);
    }

    public synchronized void setBackgroundColor(int i) {
        this.backgroundColor = i;
    }

    public synchronized void setFilter(Filter filter2) {
        this.filter = filter2;
    }

    public void setFixedTileSize(int i) {
        this.fixedTileSize = i;
        setTileSize();
    }

    public void setMaxTextWidthFactor(float f) {
        this.maxTextWidthFactor = f;
        setMaxTextWidth();
    }

    public synchronized void setThemeCallback(ThemeCallback themeCallback2) {
        this.themeCallback = themeCallback2;
    }

    public synchronized void setTileSizeMultiple(int i) {
        this.tileSizeMultiple = i;
        setTileSize();
    }

    public synchronized void setUserScaleFactor(float f) {
        this.userScaleFactor = f;
        setTileSize();
    }

    private void setMaxTextWidth() {
        this.maxTextWidth = (int) (((float) this.tileSize) * this.maxTextWidthFactor);
    }

    private void setTileSize() {
        int i = this.fixedTileSize;
        if (i == 0) {
            float f = deviceScaleFactor * 256.0f * this.userScaleFactor;
            int i2 = this.tileSizeMultiple;
            this.tileSize = Math.max(i2, Math.round(f / ((float) i2)) * this.tileSizeMultiple);
        } else {
            this.tileSize = i;
        }
        setMaxTextWidth();
    }
}
