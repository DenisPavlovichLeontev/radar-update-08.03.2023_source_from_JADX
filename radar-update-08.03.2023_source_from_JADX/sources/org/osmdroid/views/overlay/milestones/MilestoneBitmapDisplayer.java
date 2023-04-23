package org.osmdroid.views.overlay.milestones;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class MilestoneBitmapDisplayer extends MilestoneDisplayer {
    private final Bitmap mBitmap;
    private final int mOffsetX;
    private final int mOffsetY;

    public MilestoneBitmapDisplayer(double d, boolean z, Bitmap bitmap, int i, int i2) {
        super(d, z);
        this.mBitmap = bitmap;
        this.mOffsetX = i;
        this.mOffsetY = i2;
    }

    /* access modifiers changed from: protected */
    public void draw(Canvas canvas, Object obj) {
        canvas.drawBitmap(this.mBitmap, (float) (-this.mOffsetX), (float) (-this.mOffsetY), (Paint) null);
    }
}
