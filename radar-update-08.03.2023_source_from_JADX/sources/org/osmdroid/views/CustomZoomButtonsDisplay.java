package org.osmdroid.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import org.osmdroid.library.C1340R;

public class CustomZoomButtonsDisplay {
    private float mAdditionalPixelMarginBottom;
    private float mAdditionalPixelMarginLeft;
    private float mAdditionalPixelMarginRight;
    private float mAdditionalPixelMarginTop;
    private Paint mAlphaPaint;
    private int mBitmapSize;
    private boolean mHorizontalOrVertical;
    private HorizontalPosition mHorizontalPosition;
    private final MapView mMapView;
    private float mMargin;
    private float mPadding;
    private float mPixelMarginBottom;
    private float mPixelMarginLeft;
    private float mPixelMarginRight;
    private float mPixelMarginTop;
    private final Point mUnrotatedPoint = new Point();
    private VerticalPosition mVerticalPosition;
    private Bitmap mZoomInBitmapDisabled;
    private Bitmap mZoomInBitmapEnabled;
    private Bitmap mZoomOutBitmapDisabled;
    private Bitmap mZoomOutBitmapEnabled;

    public enum HorizontalPosition {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum VerticalPosition {
        TOP,
        CENTER,
        BOTTOM
    }

    public CustomZoomButtonsDisplay(MapView mapView) {
        this.mMapView = mapView;
        setPositions(true, HorizontalPosition.CENTER, VerticalPosition.BOTTOM);
        setMarginPadding(0.5f, 0.5f);
    }

    public void setPositions(boolean z, HorizontalPosition horizontalPosition, VerticalPosition verticalPosition) {
        this.mHorizontalOrVertical = z;
        this.mHorizontalPosition = horizontalPosition;
        this.mVerticalPosition = verticalPosition;
    }

    public void setMarginPadding(float f, float f2) {
        this.mMargin = f;
        this.mPadding = f2;
        refreshPixelMargins();
    }

    public void setAdditionalPixelMargins(float f, float f2, float f3, float f4) {
        this.mAdditionalPixelMarginLeft = f;
        this.mAdditionalPixelMarginTop = f2;
        this.mAdditionalPixelMarginRight = f3;
        this.mAdditionalPixelMarginBottom = f4;
        refreshPixelMargins();
    }

    private void refreshPixelMargins() {
        float f = this.mMargin * ((float) this.mBitmapSize);
        this.mPixelMarginLeft = this.mAdditionalPixelMarginLeft + f;
        this.mPixelMarginTop = this.mAdditionalPixelMarginTop + f;
        this.mPixelMarginRight = this.mAdditionalPixelMarginRight + f;
        this.mPixelMarginBottom = f + this.mAdditionalPixelMarginBottom;
    }

    public void setBitmaps(Bitmap bitmap, Bitmap bitmap2, Bitmap bitmap3, Bitmap bitmap4) {
        this.mZoomInBitmapEnabled = bitmap;
        this.mZoomInBitmapDisabled = bitmap2;
        this.mZoomOutBitmapEnabled = bitmap3;
        this.mZoomOutBitmapDisabled = bitmap4;
        this.mBitmapSize = bitmap.getWidth();
        refreshPixelMargins();
    }

    /* access modifiers changed from: protected */
    public Bitmap getZoomBitmap(boolean z, boolean z2) {
        Bitmap icon = getIcon(z);
        this.mBitmapSize = icon.getWidth();
        refreshPixelMargins();
        int i = this.mBitmapSize;
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setColor(z2 ? -1 : -3355444);
        paint.setStyle(Paint.Style.FILL);
        int i2 = this.mBitmapSize;
        canvas.drawRect(0.0f, 0.0f, (float) (i2 - 1), (float) (i2 - 1), paint);
        canvas.drawBitmap(icon, 0.0f, 0.0f, (Paint) null);
        return createBitmap;
    }

    /* access modifiers changed from: protected */
    public Bitmap getIcon(boolean z) {
        return ((BitmapDrawable) this.mMapView.getResources().getDrawable(z ? C1340R.C1341drawable.sharp_add_black_36 : C1340R.C1341drawable.sharp_remove_black_36)).getBitmap();
    }

    public void draw(Canvas canvas, float f, boolean z, boolean z2) {
        Paint paint;
        if (f != 0.0f) {
            if (f == 1.0f) {
                paint = null;
            } else {
                if (this.mAlphaPaint == null) {
                    this.mAlphaPaint = new Paint();
                }
                this.mAlphaPaint.setAlpha((int) (f * 255.0f));
                paint = this.mAlphaPaint;
            }
            canvas.drawBitmap(getBitmap(true, z), getTopLeft(true, true), getTopLeft(true, false), paint);
            canvas.drawBitmap(getBitmap(false, z2), getTopLeft(false, true), getTopLeft(false, false), paint);
        }
    }

    private float getTopLeft(boolean z, boolean z2) {
        int i;
        float f;
        float f2;
        if (z2) {
            float firstLeft = getFirstLeft(this.mMapView.getWidth());
            if (!this.mHorizontalOrVertical || !z) {
                return firstLeft;
            }
            i = this.mBitmapSize;
            f = firstLeft + ((float) i);
            f2 = this.mPadding;
        } else {
            float firstTop = getFirstTop(this.mMapView.getHeight());
            if (this.mHorizontalOrVertical || z) {
                return firstTop;
            }
            i = this.mBitmapSize;
            f = firstTop + ((float) i);
            f2 = this.mPadding;
        }
        return f + (f2 * ((float) i));
    }

    private float getFirstLeft(int i) {
        float f;
        int i2 = C13771.f562xee29485[this.mHorizontalPosition.ordinal()];
        if (i2 == 1) {
            return this.mPixelMarginLeft;
        }
        if (i2 == 2) {
            float f2 = ((float) i) - this.mPixelMarginRight;
            int i3 = this.mBitmapSize;
            return (f2 - ((float) i3)) - (this.mHorizontalOrVertical ? (this.mPadding * ((float) i3)) + ((float) i3) : 0.0f);
        } else if (i2 == 3) {
            float f3 = ((float) i) / 2.0f;
            if (this.mHorizontalOrVertical) {
                float f4 = this.mPadding;
                int i4 = this.mBitmapSize;
                f = ((f4 * ((float) i4)) / 2.0f) + ((float) i4);
            } else {
                f = ((float) this.mBitmapSize) / 2.0f;
            }
            return f3 - f;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /* renamed from: org.osmdroid.views.CustomZoomButtonsDisplay$1 */
    static /* synthetic */ class C13771 {

        /* renamed from: $SwitchMap$org$osmdroid$views$CustomZoomButtonsDisplay$HorizontalPosition */
        static final /* synthetic */ int[] f562xee29485;

        /* renamed from: $SwitchMap$org$osmdroid$views$CustomZoomButtonsDisplay$VerticalPosition */
        static final /* synthetic */ int[] f563x4ecae3d7;

        /* JADX WARNING: Can't wrap try/catch for region: R(15:0|(2:1|2)|3|(2:5|6)|7|9|10|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Can't wrap try/catch for region: R(17:0|1|2|3|5|6|7|9|10|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0039 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0043 */
        static {
            /*
                org.osmdroid.views.CustomZoomButtonsDisplay$VerticalPosition[] r0 = org.osmdroid.views.CustomZoomButtonsDisplay.VerticalPosition.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f563x4ecae3d7 = r0
                r1 = 1
                org.osmdroid.views.CustomZoomButtonsDisplay$VerticalPosition r2 = org.osmdroid.views.CustomZoomButtonsDisplay.VerticalPosition.TOP     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = f563x4ecae3d7     // Catch:{ NoSuchFieldError -> 0x001d }
                org.osmdroid.views.CustomZoomButtonsDisplay$VerticalPosition r3 = org.osmdroid.views.CustomZoomButtonsDisplay.VerticalPosition.BOTTOM     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = f563x4ecae3d7     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.osmdroid.views.CustomZoomButtonsDisplay$VerticalPosition r4 = org.osmdroid.views.CustomZoomButtonsDisplay.VerticalPosition.CENTER     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                org.osmdroid.views.CustomZoomButtonsDisplay$HorizontalPosition[] r3 = org.osmdroid.views.CustomZoomButtonsDisplay.HorizontalPosition.values()
                int r3 = r3.length
                int[] r3 = new int[r3]
                f562xee29485 = r3
                org.osmdroid.views.CustomZoomButtonsDisplay$HorizontalPosition r4 = org.osmdroid.views.CustomZoomButtonsDisplay.HorizontalPosition.LEFT     // Catch:{ NoSuchFieldError -> 0x0039 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0039 }
                r3[r4] = r1     // Catch:{ NoSuchFieldError -> 0x0039 }
            L_0x0039:
                int[] r1 = f562xee29485     // Catch:{ NoSuchFieldError -> 0x0043 }
                org.osmdroid.views.CustomZoomButtonsDisplay$HorizontalPosition r3 = org.osmdroid.views.CustomZoomButtonsDisplay.HorizontalPosition.RIGHT     // Catch:{ NoSuchFieldError -> 0x0043 }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x0043 }
                r1[r3] = r0     // Catch:{ NoSuchFieldError -> 0x0043 }
            L_0x0043:
                int[] r0 = f562xee29485     // Catch:{ NoSuchFieldError -> 0x004d }
                org.osmdroid.views.CustomZoomButtonsDisplay$HorizontalPosition r1 = org.osmdroid.views.CustomZoomButtonsDisplay.HorizontalPosition.CENTER     // Catch:{ NoSuchFieldError -> 0x004d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x004d }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x004d }
            L_0x004d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.CustomZoomButtonsDisplay.C13771.<clinit>():void");
        }
    }

    private float getFirstTop(int i) {
        float f;
        float f2;
        int i2 = C13771.f563x4ecae3d7[this.mVerticalPosition.ordinal()];
        if (i2 == 1) {
            return this.mPixelMarginTop;
        }
        if (i2 == 2) {
            float f3 = ((float) i) - this.mPixelMarginBottom;
            int i3 = this.mBitmapSize;
            float f4 = f3 - ((float) i3);
            if (this.mHorizontalOrVertical) {
                f = 0.0f;
            } else {
                f = ((float) i3) + (this.mPadding * ((float) i3));
            }
            return f4 - f;
        } else if (i2 == 3) {
            float f5 = ((float) i) / 2.0f;
            if (this.mHorizontalOrVertical) {
                f2 = ((float) this.mBitmapSize) / 2.0f;
            } else {
                float f6 = this.mPadding;
                int i4 = this.mBitmapSize;
                f2 = ((f6 * ((float) i4)) / 2.0f) + ((float) i4);
            }
            return f5 - f2;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private Bitmap getBitmap(boolean z, boolean z2) {
        if (this.mZoomInBitmapEnabled == null) {
            setBitmaps(getZoomBitmap(true, true), getZoomBitmap(true, false), getZoomBitmap(false, true), getZoomBitmap(false, false));
        }
        return z ? z2 ? this.mZoomInBitmapEnabled : this.mZoomInBitmapDisabled : z2 ? this.mZoomOutBitmapEnabled : this.mZoomOutBitmapDisabled;
    }

    @Deprecated
    public boolean isTouchedRotated(MotionEvent motionEvent, boolean z) {
        if (this.mMapView.getMapOrientation() == 0.0f) {
            this.mUnrotatedPoint.set((int) motionEvent.getX(), (int) motionEvent.getY());
        } else {
            this.mMapView.getProjection().rotateAndScalePoint((int) motionEvent.getX(), (int) motionEvent.getY(), this.mUnrotatedPoint);
        }
        return isTouched(this.mUnrotatedPoint.x, this.mUnrotatedPoint.y, z);
    }

    public boolean isTouched(MotionEvent motionEvent, boolean z) {
        if (motionEvent.getAction() == 1) {
            return isTouched((int) motionEvent.getX(), (int) motionEvent.getY(), z);
        }
        return false;
    }

    private boolean isTouched(int i, int i2, boolean z) {
        if (!isTouched(z, true, (float) i) || !isTouched(z, false, (float) i2)) {
            return false;
        }
        return true;
    }

    private boolean isTouched(boolean z, boolean z2, float f) {
        float topLeft = getTopLeft(z, z2);
        return f >= topLeft && f <= topLeft + ((float) this.mBitmapSize);
    }
}
