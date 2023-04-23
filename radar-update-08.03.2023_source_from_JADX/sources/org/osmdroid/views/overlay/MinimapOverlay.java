package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

public class MinimapOverlay extends TilesOverlay {
    private int mHeight;
    private int mPadding;
    private final Paint mPaint;
    private int mWidth;
    private int mZoomDifference;

    public boolean isOptionsMenuEnabled() {
        return false;
    }

    public MinimapOverlay(Context context, Handler handler, MapTileProviderBase mapTileProviderBase, int i) {
        super(mapTileProviderBase, context);
        this.mWidth = 100;
        this.mHeight = 100;
        this.mPadding = 10;
        setZoomDifference(i);
        this.mTileProvider.getTileRequestCompleteHandlers().add(handler);
        setLoadingLineColor(getLoadingBackgroundColor());
        float f = context.getResources().getDisplayMetrics().density;
        this.mWidth = (int) (((float) this.mWidth) * f);
        this.mHeight = (int) (((float) this.mHeight) * f);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setColor(-7829368);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2.0f);
    }

    public MinimapOverlay(Context context, Handler handler, MapTileProviderBase mapTileProviderBase) {
        this(context, handler, mapTileProviderBase, 3);
    }

    public MinimapOverlay(Context context, Handler handler) {
        this(context, handler, new MapTileProviderBasic(context));
    }

    public void setTileSource(ITileSource iTileSource) {
        this.mTileProvider.setTileSource(iTileSource);
    }

    public int getZoomDifference() {
        return this.mZoomDifference;
    }

    public void setZoomDifference(int i) {
        this.mZoomDifference = i;
    }

    public void draw(Canvas canvas, Projection projection) {
        if (setViewPort(canvas, projection)) {
            projection.save(canvas, false, true);
            canvas.drawRect((float) (getCanvasRect().left - 2), (float) (getCanvasRect().top - 2), (float) (getCanvasRect().right + 2), (float) (getCanvasRect().bottom + 2), this.mPaint);
            super.drawTiles(canvas, getProjection(), getProjection().getZoomLevel(), this.mViewPort);
            projection.restore(canvas, true);
        }
    }

    public boolean onSingleTapUp(MotionEvent motionEvent, MapView mapView) {
        return contains(motionEvent);
    }

    public boolean onDoubleTap(MotionEvent motionEvent, MapView mapView) {
        return contains(motionEvent);
    }

    public boolean onLongPress(MotionEvent motionEvent, MapView mapView) {
        return contains(motionEvent);
    }

    public void setWidth(int i) {
        this.mWidth = i;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setHeight(int i) {
        this.mHeight = i;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void setPadding(int i) {
        this.mPadding = i;
    }

    public int getPadding() {
        return this.mPadding;
    }

    private boolean contains(MotionEvent motionEvent) {
        Rect canvasRect = getCanvasRect();
        return canvasRect != null && canvasRect.contains((int) motionEvent.getX(), (int) motionEvent.getY());
    }

    /* access modifiers changed from: protected */
    public boolean setViewPort(Canvas canvas, Projection projection) {
        double zoomLevel = projection.getZoomLevel() - ((double) getZoomDifference());
        if (zoomLevel < ((double) this.mTileProvider.getMinimumZoomLevel())) {
            return false;
        }
        int width = (canvas.getWidth() - getPadding()) - getWidth();
        int height = (canvas.getHeight() - getPadding()) - getHeight();
        setCanvasRect(new Rect(width, height, getWidth() + width, getHeight() + height));
        setProjection(projection.getOffspring(zoomLevel, getCanvasRect()));
        getProjection().getMercatorViewPort(this.mViewPort);
        return true;
    }
}
