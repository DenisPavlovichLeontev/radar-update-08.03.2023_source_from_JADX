package org.osmdroid.views.overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.PointL;
import org.osmdroid.util.RectL;
import org.osmdroid.util.SpeechBalloonHelper;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

public class SpeechBalloonOverlay extends Overlay {
    private Paint mBackground;
    private Paint mDragBackground;
    private float mDragDeltaX;
    private float mDragDeltaY;
    private Paint mDragForeground;
    private float mDragStartX;
    private float mDragStartY;
    private boolean mDraggable = true;
    private Paint mForeground;
    private GeoPoint mGeoPoint;
    private final SpeechBalloonHelper mHelper = new SpeechBalloonHelper();
    private final PointL mIntersection1 = new PointL();
    private final PointL mIntersection2 = new PointL();
    private boolean mIsDragged;
    private int mMargin;
    private int mOffsetX;
    private int mOffsetY;
    private final Path mPath = new Path();
    private final Point mPixel = new Point();
    private final PointL mPoint = new PointL();
    private double mRadius;
    private final RectL mRect = new RectL();
    private final Rect mTextRect = new Rect();
    private String mTitle;

    public void setTitle(String str) {
        this.mTitle = str;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.mGeoPoint = geoPoint;
    }

    public void setBackground(Paint paint) {
        this.mBackground = paint;
    }

    public void setForeground(Paint paint) {
        this.mForeground = paint;
    }

    public void setDragBackground(Paint paint) {
        this.mDragBackground = paint;
    }

    public void setDragForeground(Paint paint) {
        this.mDragForeground = paint;
    }

    public void setMargin(int i) {
        this.mMargin = i;
    }

    public void setRadius(long j) {
        this.mRadius = (double) j;
    }

    public void setOffset(int i, int i2) {
        this.mOffsetX = i;
        this.mOffsetY = i2;
    }

    public void draw(Canvas canvas, Projection projection) {
        Paint paint;
        Paint paint2;
        String str;
        Canvas canvas2 = canvas;
        if (this.mIsDragged) {
            paint2 = this.mDragBackground;
            if (paint2 == null) {
                paint2 = this.mBackground;
            }
            paint = this.mDragForeground;
            if (paint == null) {
                paint = this.mForeground;
            }
        } else {
            paint2 = this.mBackground;
            paint = this.mForeground;
        }
        Paint paint3 = paint2;
        Paint paint4 = paint;
        if (this.mGeoPoint != null && (str = this.mTitle) != null && str.trim().length() != 0 && paint4 != null && paint3 != null) {
            projection.toPixels(this.mGeoPoint, this.mPixel);
            String str2 = this.mTitle;
            paint4.getTextBounds(str2, 0, str2.length(), this.mTextRect);
            this.mPoint.set((long) this.mPixel.x, (long) this.mPixel.y);
            this.mTextRect.offset((int) (((float) (this.mPoint.f559x + ((long) this.mOffsetX))) + this.mDragDeltaX), (int) (((float) (this.mPoint.f560y + ((long) this.mOffsetY))) + this.mDragDeltaY));
            this.mTextRect.top -= this.mMargin;
            this.mTextRect.left -= this.mMargin;
            this.mTextRect.right += this.mMargin;
            this.mTextRect.bottom += this.mMargin;
            this.mRect.set((long) this.mTextRect.left, (long) this.mTextRect.top, (long) this.mTextRect.right, (long) this.mTextRect.bottom);
            int compute = this.mHelper.compute(this.mRect, this.mPoint, this.mRadius, this.mIntersection1, this.mIntersection2);
            canvas.drawRect((float) this.mTextRect.left, (float) this.mTextRect.top, (float) this.mTextRect.right, (float) this.mTextRect.bottom, paint3);
            if (compute != -1) {
                this.mPath.reset();
                this.mPath.moveTo((float) this.mPoint.f559x, (float) this.mPoint.f560y);
                this.mPath.lineTo((float) this.mIntersection1.f559x, (float) this.mIntersection1.f560y);
                this.mPath.lineTo((float) this.mIntersection2.f559x, (float) this.mIntersection2.f560y);
                this.mPath.close();
                canvas2.drawPath(this.mPath, paint3);
            }
            canvas2.drawText(str2, (float) (this.mTextRect.left + this.mMargin), (float) (this.mTextRect.bottom - this.mMargin), paint4);
        }
    }

    public boolean onLongPress(MotionEvent motionEvent, MapView mapView) {
        boolean hitTest = hitTest(motionEvent, mapView);
        if (hitTest && this.mDraggable) {
            this.mIsDragged = true;
            this.mDragStartX = motionEvent.getX();
            this.mDragStartY = motionEvent.getY();
            this.mDragDeltaX = 0.0f;
            this.mDragDeltaY = 0.0f;
            mapView.invalidate();
        }
        return hitTest;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.mDraggable && this.mIsDragged) {
            if (motionEvent.getAction() == 1) {
                this.mDragDeltaX = motionEvent.getX() - this.mDragStartX;
                float y = motionEvent.getY() - this.mDragStartY;
                this.mOffsetX = (int) (((float) this.mOffsetX) + this.mDragDeltaX);
                this.mOffsetY = (int) (((float) this.mOffsetY) + y);
                this.mDragDeltaX = 0.0f;
                this.mDragDeltaY = 0.0f;
                this.mIsDragged = false;
                mapView.invalidate();
                return true;
            } else if (motionEvent.getAction() == 2) {
                this.mDragDeltaX = motionEvent.getX() - this.mDragStartX;
                this.mDragDeltaY = motionEvent.getY() - this.mDragStartY;
                mapView.invalidate();
                return true;
            }
        }
        return false;
    }

    private boolean hitTest(MotionEvent motionEvent, MapView mapView) {
        return this.mRect.contains((long) ((int) motionEvent.getX()), (long) ((int) motionEvent.getY()));
    }
}
