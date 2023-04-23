package org.osmdroid.views.overlay;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.Projection;

public class GroundOverlay extends Overlay {
    private float mBearing = 0.0f;
    private GeoPoint mBottomLeft;
    private GeoPoint mBottomRight;
    private Bitmap mImage;
    private final Matrix mMatrix = new Matrix();
    private float[] mMatrixDst;
    private float[] mMatrixSrc;
    private final Paint mPaint = new Paint();
    private GeoPoint mTopLeft;
    private GeoPoint mTopRight;
    private float mTransparency;

    public GroundOverlay() {
        setTransparency(0.0f);
    }

    public void setImage(Bitmap bitmap) {
        this.mImage = bitmap;
        this.mMatrixSrc = null;
    }

    public Bitmap getImage() {
        return this.mImage;
    }

    public void setBearing(float f) {
        this.mBearing = f;
    }

    public float getBearing() {
        return this.mBearing;
    }

    public void setTransparency(float f) {
        this.mTransparency = f;
        this.mPaint.setAlpha(255 - ((int) (f * 255.0f)));
    }

    public float getTransparency() {
        return this.mTransparency;
    }

    public GeoPoint getTopLeft() {
        return this.mTopLeft;
    }

    public GeoPoint getTopRight() {
        return this.mTopRight;
    }

    public GeoPoint getBottomRight() {
        return this.mBottomRight;
    }

    public GeoPoint getBottomLeft() {
        return this.mBottomLeft;
    }

    public void draw(Canvas canvas, Projection projection) {
        if (this.mImage != null) {
            computeMatrix(projection);
            canvas.drawBitmap(this.mImage, this.mMatrix, this.mPaint);
        }
    }

    public void setPosition(GeoPoint geoPoint, GeoPoint geoPoint2, GeoPoint geoPoint3, GeoPoint geoPoint4) {
        this.mMatrix.reset();
        this.mTopLeft = new GeoPoint(geoPoint);
        this.mTopRight = new GeoPoint(geoPoint2);
        this.mBottomRight = new GeoPoint(geoPoint3);
        this.mBottomLeft = new GeoPoint(geoPoint4);
        this.mBounds = new BoundingBox(geoPoint.getLatitude(), geoPoint2.getLongitude(), geoPoint3.getLatitude(), geoPoint.getLongitude());
    }

    public void setPosition(GeoPoint geoPoint, GeoPoint geoPoint2) {
        this.mMatrix.reset();
        this.mMatrixSrc = null;
        this.mMatrixDst = null;
        this.mTopLeft = new GeoPoint(geoPoint);
        this.mTopRight = null;
        this.mBottomRight = new GeoPoint(geoPoint2);
        this.mBottomLeft = null;
        this.mBounds = new BoundingBox(geoPoint.getLatitude(), geoPoint2.getLongitude(), geoPoint2.getLatitude(), geoPoint.getLongitude());
    }

    private void computeMatrix(Projection projection) {
        Projection projection2 = projection;
        if (this.mTopRight == null) {
            long longPixelXFromLongitude = projection2.getLongPixelXFromLongitude(this.mTopLeft.getLongitude());
            long longPixelYFromLatitude = projection2.getLongPixelYFromLatitude(this.mTopLeft.getLatitude());
            long longPixelXFromLongitude2 = projection2.getLongPixelXFromLongitude(this.mBottomRight.getLongitude());
            long longPixelYFromLatitude2 = projection2.getLongPixelYFromLatitude(this.mBottomRight.getLatitude());
            this.mMatrix.setScale(((float) (longPixelXFromLongitude2 - longPixelXFromLongitude)) / ((float) this.mImage.getWidth()), ((float) (longPixelYFromLatitude2 - longPixelYFromLatitude)) / ((float) this.mImage.getHeight()));
            this.mMatrix.postTranslate((float) longPixelXFromLongitude, (float) longPixelYFromLatitude);
            return;
        }
        if (this.mMatrixSrc == null) {
            this.mMatrixSrc = new float[8];
            int width = this.mImage.getWidth();
            int height = this.mImage.getHeight();
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
        if (this.mMatrixDst == null) {
            this.mMatrixDst = new float[8];
        }
        long longPixelXFromLongitude3 = projection2.getLongPixelXFromLongitude(this.mTopLeft.getLongitude());
        long longPixelYFromLatitude3 = projection2.getLongPixelYFromLatitude(this.mTopLeft.getLatitude());
        long longPixelXFromLongitude4 = projection2.getLongPixelXFromLongitude(this.mTopRight.getLongitude());
        long longPixelYFromLatitude4 = projection2.getLongPixelYFromLatitude(this.mTopRight.getLatitude());
        long longPixelXFromLongitude5 = projection2.getLongPixelXFromLongitude(this.mBottomRight.getLongitude());
        long longPixelYFromLatitude5 = projection2.getLongPixelYFromLatitude(this.mBottomRight.getLatitude());
        long longPixelXFromLongitude6 = projection2.getLongPixelXFromLongitude(this.mBottomLeft.getLongitude());
        long longPixelYFromLatitude6 = projection2.getLongPixelYFromLatitude(this.mBottomLeft.getLatitude());
        float[] fArr2 = this.mMatrixDst;
        fArr2[0] = (float) longPixelXFromLongitude3;
        fArr2[1] = (float) longPixelYFromLatitude3;
        fArr2[2] = (float) longPixelXFromLongitude4;
        fArr2[3] = (float) longPixelYFromLatitude4;
        fArr2[4] = (float) longPixelXFromLongitude5;
        fArr2[5] = (float) longPixelYFromLatitude5;
        fArr2[6] = (float) longPixelXFromLongitude6;
        fArr2[7] = (float) longPixelYFromLatitude6;
        this.mMatrix.setPolyToPoly(this.mMatrixSrc, 0, fArr2, 0, 4);
    }
}
