package org.osmdroid.views.overlay;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.Projection;

@Deprecated
public class GroundOverlay4 extends Overlay {
    protected float mBearing = 0.0f;
    private GeoPoint mBottomLeft;
    private GeoPoint mBottomRight;
    private Bitmap mImage;
    private Matrix mMatrix = new Matrix();
    private final float[] mMatrixDst = new float[8];
    private final float[] mMatrixSrc = new float[8];
    private final Paint mPaint = new Paint();
    private GeoPoint mTopLeft;
    private GeoPoint mTopRight;
    protected float mTransparency;

    public GroundOverlay4() {
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

    public void draw(Canvas canvas, Projection projection) {
        if (this.mImage != null) {
            computeMatrix(projection);
            canvas.drawBitmap(getImage(), getMatrix(), getPaint());
        }
    }

    public void setImage(Bitmap bitmap) {
        this.mImage = bitmap;
        if (getImage() != null) {
            int width = getImage().getWidth();
            int height = getImage().getHeight();
            float[] fArr = this.mMatrixSrc;
            fArr[0] = 0.0f;
            fArr[1] = 0.0f;
            float f = (float) width;
            fArr[2] = f;
            fArr[3] = 0.0f;
            fArr[4] = f;
            float f2 = (float) height;
            fArr[5] = f2;
            fArr[6] = 0.0f;
            fArr[7] = f2;
        }
    }

    public void setPosition(GeoPoint geoPoint, GeoPoint geoPoint2, GeoPoint geoPoint3, GeoPoint geoPoint4) {
        this.mTopLeft = new GeoPoint(geoPoint);
        this.mTopRight = new GeoPoint(geoPoint2);
        this.mBottomRight = new GeoPoint(geoPoint3);
        this.mBottomLeft = new GeoPoint(geoPoint4);
    }

    /* access modifiers changed from: protected */
    public void computeMatrix(Projection projection) {
        Projection projection2 = projection;
        long longPixelXFromLongitude = projection2.getLongPixelXFromLongitude(this.mTopLeft.getLongitude());
        long longPixelYFromLatitude = projection2.getLongPixelYFromLatitude(this.mTopLeft.getLatitude());
        long longPixelXFromLongitude2 = projection2.getLongPixelXFromLongitude(this.mTopRight.getLongitude());
        long longPixelYFromLatitude2 = projection2.getLongPixelYFromLatitude(this.mTopRight.getLatitude());
        long longPixelXFromLongitude3 = projection2.getLongPixelXFromLongitude(this.mBottomRight.getLongitude());
        long longPixelYFromLatitude3 = projection2.getLongPixelYFromLatitude(this.mBottomRight.getLatitude());
        long longPixelXFromLongitude4 = projection2.getLongPixelXFromLongitude(this.mBottomLeft.getLongitude());
        long longPixelYFromLatitude4 = projection2.getLongPixelYFromLatitude(this.mBottomLeft.getLatitude());
        float[] fArr = this.mMatrixDst;
        fArr[0] = (float) longPixelXFromLongitude;
        fArr[1] = (float) longPixelYFromLatitude;
        fArr[2] = (float) longPixelXFromLongitude2;
        fArr[3] = (float) longPixelYFromLatitude2;
        fArr[4] = (float) longPixelXFromLongitude3;
        fArr[5] = (float) longPixelYFromLatitude3;
        fArr[6] = (float) longPixelXFromLongitude4;
        fArr[7] = (float) longPixelYFromLatitude4;
        getMatrix().setPolyToPoly(this.mMatrixSrc, 0, this.mMatrixDst, 0, 4);
    }
}
