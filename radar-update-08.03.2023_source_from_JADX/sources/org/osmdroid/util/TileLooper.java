package org.osmdroid.util;

import android.graphics.Rect;

public abstract class TileLooper {
    private boolean horizontalWrapEnabled;
    protected int mTileZoomLevel;
    protected final Rect mTiles;
    private boolean verticalWrapEnabled;

    public void finaliseLoop() {
    }

    public abstract void handleTile(long j, int i, int i2);

    public void initialiseLoop() {
    }

    public TileLooper() {
        this(false, false);
    }

    public TileLooper(boolean z, boolean z2) {
        this.mTiles = new Rect();
        this.horizontalWrapEnabled = z;
        this.verticalWrapEnabled = z2;
    }

    /* access modifiers changed from: protected */
    public void loop(double d, RectL rectL) {
        TileSystem.getTileFromMercator(rectL, TileSystem.getTileSize(d), this.mTiles);
        this.mTileZoomLevel = TileSystem.getInputTileZoomLevel(d);
        initialiseLoop();
        int i = 1 << this.mTileZoomLevel;
        for (int i2 = this.mTiles.left; i2 <= this.mTiles.right; i2++) {
            for (int i3 = this.mTiles.top; i3 <= this.mTiles.bottom; i3++) {
                if ((this.horizontalWrapEnabled || (i2 >= 0 && i2 < i)) && (this.verticalWrapEnabled || (i3 >= 0 && i3 < i))) {
                    handleTile(MapTileIndex.getTileIndex(this.mTileZoomLevel, MyMath.mod(i2, i), MyMath.mod(i3, i)), i2, i3);
                }
            }
        }
        finaliseLoop();
    }

    public boolean isHorizontalWrapEnabled() {
        return this.horizontalWrapEnabled;
    }

    public void setHorizontalWrapEnabled(boolean z) {
        this.horizontalWrapEnabled = z;
    }

    public boolean isVerticalWrapEnabled() {
        return this.verticalWrapEnabled;
    }

    public void setVerticalWrapEnabled(boolean z) {
        this.verticalWrapEnabled = z;
    }
}
