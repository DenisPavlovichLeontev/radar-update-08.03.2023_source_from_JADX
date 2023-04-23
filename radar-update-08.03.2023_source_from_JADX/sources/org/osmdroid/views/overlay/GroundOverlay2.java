package org.osmdroid.views.overlay;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.Projection;

@Deprecated
public class GroundOverlay2 extends Overlay {
    protected float mBearing = 0.0f;
    private Bitmap mImage;
    private float mLatD;
    private float mLatU;
    private float mLonL;
    private float mLonR;
    private Matrix mMatrix = new Matrix();
    private final Paint mPaint = new Paint();
    protected float mTransparency;

    public GroundOverlay2() {
        setTransparency(0.0f);
    }

    /* access modifiers changed from: protected */
    public Paint getPaint() {
        return this.mPaint;
    }

    /* access modifiers changed from: protected */
    public Matrix getMatrix() {
        return this.mMatrix;
    }

    public Bitmap getImage() {
        return this.mImage;
    }

    public float getBearing() {
        return this.mBearing;
    }

    public void setBearing(float f) {
        this.mBearing = f;
    }

    public void setTransparency(float f) {
        this.mTransparency = f;
        this.mPaint.setAlpha(255 - ((int) (f * 255.0f)));
    }

    public float getTransparency() {
        return this.mTransparency;
    }

    public void setImage(Bitmap bitmap) {
        this.mImage = bitmap;
    }

    public void draw(Canvas canvas, Projection projection) {
        if (this.mImage != null) {
            computeMatrix(projection);
            canvas.drawBitmap(getImage(), getMatrix(), getPaint());
        }
    }

    public void setPosition(GeoPoint geoPoint, GeoPoint geoPoint2) {
        this.mLatU = (float) geoPoint.getLatitude();
        this.mLonL = (float) geoPoint.getLongitude();
        this.mLatD = (float) geoPoint2.getLatitude();
        this.mLonR = (float) geoPoint2.getLongitude();
    }

    /* access modifiers changed from: protected */
    public void computeMatrix(Projection projection) {
        long longPixelXFromLongitude = projection.getLongPixelXFromLongitude((double) this.mLonL);
        long longPixelYFromLatitude = projection.getLongPixelYFromLatitude((double) this.mLatU);
        long longPixelXFromLongitude2 = projection.getLongPixelXFromLongitude((double) this.mLonR);
        long longPixelYFromLatitude2 = projection.getLongPixelYFromLatitude((double) this.mLatD);
        getMatrix().setScale(((float) (longPixelXFromLongitude2 - longPixelXFromLongitude)) / ((float) getImage().getWidth()), ((float) (longPixelYFromLatitude2 - longPixelYFromLatitude)) / ((float) getImage().getHeight()));
        getMatrix().postTranslate((float) longPixelXFromLongitude, (float) longPixelYFromLatitude);
    }
}
