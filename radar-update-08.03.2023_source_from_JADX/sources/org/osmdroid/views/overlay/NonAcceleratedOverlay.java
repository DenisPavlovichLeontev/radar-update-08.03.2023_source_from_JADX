package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.Log;
import org.osmdroid.api.IMapView;
import org.osmdroid.views.MapView;

public abstract class NonAcceleratedOverlay extends Overlay {
    private Bitmap mBackingBitmap;
    private Canvas mBackingCanvas;
    private final Matrix mBackingMatrix = new Matrix();
    private final Matrix mCanvasIdentityMatrix = new Matrix();

    public boolean isUsingBackingBitmap() {
        return true;
    }

    /* access modifiers changed from: protected */
    public abstract void onDraw(Canvas canvas, MapView mapView, boolean z);

    @Deprecated
    public NonAcceleratedOverlay(Context context) {
        super(context);
    }

    public NonAcceleratedOverlay() {
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas, Canvas canvas2, MapView mapView, boolean z) {
        onDraw(canvas, mapView, z);
    }

    public void onDetach(MapView mapView) {
        this.mBackingBitmap = null;
        this.mBackingCanvas = null;
        super.onDetach(mapView);
    }

    public final void draw(Canvas canvas, MapView mapView, boolean z) {
        boolean z2 = Build.VERSION.SDK_INT >= 11;
        if (!isUsingBackingBitmap() || !z2 || !canvas.isHardwareAccelerated()) {
            onDraw(canvas, canvas, mapView, z);
        } else if (!z && canvas.getWidth() != 0 && canvas.getHeight() != 0) {
            Bitmap bitmap = this.mBackingBitmap;
            if (!(bitmap != null && bitmap.getWidth() == canvas.getWidth() && this.mBackingBitmap.getHeight() == canvas.getHeight())) {
                this.mBackingBitmap = null;
                this.mBackingCanvas = null;
                try {
                    this.mBackingBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
                    this.mBackingCanvas = new Canvas(this.mBackingBitmap);
                } catch (OutOfMemoryError unused) {
                    Log.e(IMapView.LOGTAG, "OutOfMemoryError creating backing bitmap in NonAcceleratedOverlay.");
                    System.gc();
                    return;
                }
            }
            this.mBackingCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            canvas.getMatrix(this.mBackingMatrix);
            this.mBackingCanvas.setMatrix(this.mBackingMatrix);
            onDraw(this.mBackingCanvas, canvas, mapView, z);
            canvas.save();
            canvas.getMatrix(this.mCanvasIdentityMatrix);
            Matrix matrix = this.mCanvasIdentityMatrix;
            matrix.invert(matrix);
            canvas.concat(this.mCanvasIdentityMatrix);
            canvas.drawBitmap(this.mBackingBitmap, 0.0f, 0.0f, (Paint) null);
            canvas.restore();
        }
    }
}
