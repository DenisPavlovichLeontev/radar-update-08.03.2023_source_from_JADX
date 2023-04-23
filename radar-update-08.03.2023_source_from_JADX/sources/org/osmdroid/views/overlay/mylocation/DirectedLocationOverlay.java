package org.osmdroid.views.overlay.mylocation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import org.osmdroid.library.C1340R;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;

public class DirectedLocationOverlay extends Overlay {
    protected Bitmap DIRECTION_ARROW;
    private float DIRECTION_ARROW_CENTER_X;
    private float DIRECTION_ARROW_CENTER_Y;
    private int DIRECTION_ARROW_HEIGHT;
    private int DIRECTION_ARROW_WIDTH;
    private final Matrix directionRotater = new Matrix();
    private int mAccuracy = 0;
    protected Paint mAccuracyPaint = new Paint();
    protected float mBearing;
    protected GeoPoint mLocation;
    protected Paint mPaint = new Paint();
    private boolean mShowAccuracy = true;
    private final Point screenCoords = new Point();

    public DirectedLocationOverlay(Context context) {
        setDirectionArrow(((BitmapDrawable) context.getResources().getDrawable(C1340R.C1341drawable.twotone_navigation_black_48)).getBitmap());
        this.mAccuracyPaint.setStrokeWidth(2.0f);
        this.mAccuracyPaint.setColor(-16776961);
        this.mAccuracyPaint.setAntiAlias(true);
    }

    public void setDirectionArrow(Bitmap bitmap) {
        this.DIRECTION_ARROW = bitmap;
        this.DIRECTION_ARROW_CENTER_X = (((float) bitmap.getWidth()) / 2.0f) - 0.5f;
        this.DIRECTION_ARROW_CENTER_Y = (((float) this.DIRECTION_ARROW.getHeight()) / 2.0f) - 0.5f;
        this.DIRECTION_ARROW_HEIGHT = this.DIRECTION_ARROW.getHeight();
        this.DIRECTION_ARROW_WIDTH = this.DIRECTION_ARROW.getWidth();
    }

    public void setShowAccuracy(boolean z) {
        this.mShowAccuracy = z;
    }

    public void setLocation(GeoPoint geoPoint) {
        this.mLocation = geoPoint;
    }

    public GeoPoint getLocation() {
        return this.mLocation;
    }

    public void setAccuracy(int i) {
        this.mAccuracy = i;
    }

    public void setBearing(float f) {
        this.mBearing = f;
    }

    public void onDetach(MapView mapView) {
        this.mPaint = null;
        this.mAccuracyPaint = null;
    }

    public void draw(Canvas canvas, Projection projection) {
        int i;
        GeoPoint geoPoint = this.mLocation;
        if (geoPoint != null) {
            projection.toPixels(geoPoint, this.screenCoords);
            if (this.mShowAccuracy && (i = this.mAccuracy) > 10) {
                float metersToPixels = projection.metersToPixels((float) i, this.mLocation.getLatitude(), projection.getZoomLevel());
                if (metersToPixels > 8.0f) {
                    this.mAccuracyPaint.setAntiAlias(false);
                    this.mAccuracyPaint.setAlpha(30);
                    this.mAccuracyPaint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle((float) this.screenCoords.x, (float) this.screenCoords.y, metersToPixels, this.mAccuracyPaint);
                    this.mAccuracyPaint.setAntiAlias(true);
                    this.mAccuracyPaint.setAlpha(150);
                    this.mAccuracyPaint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle((float) this.screenCoords.x, (float) this.screenCoords.y, metersToPixels, this.mAccuracyPaint);
                }
            }
            this.directionRotater.setRotate(this.mBearing, this.DIRECTION_ARROW_CENTER_X, this.DIRECTION_ARROW_CENTER_Y);
            Bitmap createBitmap = Bitmap.createBitmap(this.DIRECTION_ARROW, 0, 0, this.DIRECTION_ARROW_WIDTH, this.DIRECTION_ARROW_HEIGHT, this.directionRotater, false);
            canvas.drawBitmap(createBitmap, (float) (this.screenCoords.x - (createBitmap.getWidth() / 2)), (float) (this.screenCoords.y - (createBitmap.getHeight() / 2)), this.mPaint);
        }
    }
}
