package org.osmdroid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.ZoomButtonsController;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.metalev.multitouch.controller.MultiTouchController;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.util.SimpleInvalidationHandler;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.GeometryMath;
import org.osmdroid.util.TileSystem;
import org.osmdroid.util.TileSystemWebMercator;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.overlay.DefaultOverlayManager;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayManager;
import org.osmdroid.views.overlay.TilesOverlay;

public class MapView extends ViewGroup implements IMapView, MultiTouchController.MultiTouchObjectCanvas<Object> {
    private static TileSystem mTileSystem = new TileSystemWebMercator();
    /* access modifiers changed from: private */
    public boolean enableFling;
    private boolean horizontalMapRepetitionEnabled;
    private GeoPoint mCenter;
    private final MapController mController;
    private boolean mDestroyModeOnDetach;
    private final GestureDetector mGestureDetector;
    /* access modifiers changed from: private */
    public boolean mImpossibleFlinging;
    private final Rect mInvalidateRect;
    protected final AtomicBoolean mIsAnimating;
    protected boolean mIsFlinging;
    private boolean mLayoutOccurred;
    private final Point mLayoutPoint;
    protected List<MapListener> mListners;
    private int mMapCenterOffsetX;
    private int mMapCenterOffsetY;
    private TilesOverlay mMapOverlay;
    private long mMapScrollX;
    private long mMapScrollY;
    protected Double mMaximumZoomLevel;
    protected Double mMinimumZoomLevel;
    /* access modifiers changed from: private */
    public MultiTouchController<Object> mMultiTouchController;
    private PointF mMultiTouchScaleCurrentPoint;
    private final GeoPoint mMultiTouchScaleGeoPoint;
    private final PointF mMultiTouchScaleInitPoint;
    private final LinkedList<OnFirstLayoutListener> mOnFirstLayoutListeners;
    private OverlayManager mOverlayManager;
    protected Projection mProjection;
    private final MapViewRepository mRepository;
    private final Rect mRescaleScreenRect;
    final Point mRotateScalePoint;
    private double mScrollableAreaLimitEast;
    private int mScrollableAreaLimitExtraPixelHeight;
    private int mScrollableAreaLimitExtraPixelWidth;
    private boolean mScrollableAreaLimitLatitude;
    private boolean mScrollableAreaLimitLongitude;
    private double mScrollableAreaLimitNorth;
    private double mScrollableAreaLimitSouth;
    private double mScrollableAreaLimitWest;
    /* access modifiers changed from: private */
    public final Scroller mScroller;
    private double mStartAnimationZoom;
    private MapTileProviderBase mTileProvider;
    private Handler mTileRequestCompleteHandler;
    private float mTilesScaleFactor;
    private boolean mTilesScaledToDpi;
    /* access modifiers changed from: private */
    public final CustomZoomButtonsController mZoomController;
    private double mZoomLevel;
    private boolean mZoomRounding;
    private float mapOrientation;
    /* access modifiers changed from: private */
    public boolean pauseFling;
    private boolean verticalMapRepetitionEnabled;

    public interface OnFirstLayoutListener {
        void onFirstLayout(View view, int i, int i2, int i3, int i4);
    }

    @Deprecated
    public float getMapScale() {
        return 1.0f;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    public MapView(Context context, MapTileProviderBase mapTileProviderBase, Handler handler, AttributeSet attributeSet) {
        this(context, mapTileProviderBase, handler, attributeSet, Configuration.getInstance().isMapViewHardwareAccelerated());
    }

    public MapView(Context context, MapTileProviderBase mapTileProviderBase, Handler handler, AttributeSet attributeSet, boolean z) {
        super(context, attributeSet);
        this.mZoomLevel = 0.0d;
        this.mIsAnimating = new AtomicBoolean(false);
        this.mMultiTouchScaleInitPoint = new PointF();
        this.mMultiTouchScaleGeoPoint = new GeoPoint(0.0d, 0.0d);
        this.mapOrientation = 0.0f;
        this.mInvalidateRect = new Rect();
        this.mTilesScaledToDpi = false;
        this.mTilesScaleFactor = 1.0f;
        this.mRotateScalePoint = new Point();
        this.mLayoutPoint = new Point();
        this.mOnFirstLayoutListeners = new LinkedList<>();
        this.mLayoutOccurred = false;
        this.horizontalMapRepetitionEnabled = true;
        this.verticalMapRepetitionEnabled = true;
        this.mListners = new ArrayList();
        this.mRepository = new MapViewRepository(this);
        this.mRescaleScreenRect = new Rect();
        this.mDestroyModeOnDetach = true;
        this.enableFling = true;
        this.pauseFling = false;
        Configuration.getInstance().getOsmdroidTileCache(context);
        if (isInEditMode()) {
            this.mTileRequestCompleteHandler = null;
            this.mController = null;
            this.mZoomController = null;
            this.mScroller = null;
            this.mGestureDetector = null;
            return;
        }
        if (!z && Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, (Paint) null);
        }
        this.mController = new MapController(this);
        this.mScroller = new Scroller(context);
        if (mapTileProviderBase == null) {
            mapTileProviderBase = new MapTileProviderBasic(context.getApplicationContext(), getTileSourceFromAttributes(attributeSet));
        }
        this.mTileRequestCompleteHandler = handler == null ? new SimpleInvalidationHandler(this) : handler;
        this.mTileProvider = mapTileProviderBase;
        mapTileProviderBase.getTileRequestCompleteHandlers().add(this.mTileRequestCompleteHandler);
        updateTileSizeForDensity(this.mTileProvider.getTileSource());
        this.mMapOverlay = new TilesOverlay(this.mTileProvider, context, this.horizontalMapRepetitionEnabled, this.verticalMapRepetitionEnabled);
        this.mOverlayManager = new DefaultOverlayManager(this.mMapOverlay);
        CustomZoomButtonsController customZoomButtonsController = new CustomZoomButtonsController(this);
        this.mZoomController = customZoomButtonsController;
        customZoomButtonsController.setOnZoomListener(new MapViewZoomListener());
        checkZoomButtons();
        GestureDetector gestureDetector = new GestureDetector(context, new MapViewGestureDetectorListener());
        this.mGestureDetector = gestureDetector;
        gestureDetector.setOnDoubleTapListener(new MapViewDoubleClickListener());
        if (Configuration.getInstance().isMapViewRecyclerFriendly() && Build.VERSION.SDK_INT >= 16) {
            setHasTransientState(true);
        }
        customZoomButtonsController.setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        this(context, (MapTileProviderBase) null, (Handler) null, attributeSet);
    }

    public MapView(Context context) {
        this(context, (MapTileProviderBase) null, (Handler) null, (AttributeSet) null);
    }

    public MapView(Context context, MapTileProviderBase mapTileProviderBase) {
        this(context, mapTileProviderBase, (Handler) null);
    }

    public MapView(Context context, MapTileProviderBase mapTileProviderBase, Handler handler) {
        this(context, mapTileProviderBase, handler, (AttributeSet) null);
    }

    public IMapController getController() {
        return this.mController;
    }

    public List<Overlay> getOverlays() {
        return getOverlayManager().overlays();
    }

    public OverlayManager getOverlayManager() {
        return this.mOverlayManager;
    }

    public void setOverlayManager(OverlayManager overlayManager) {
        this.mOverlayManager = overlayManager;
    }

    public MapTileProviderBase getTileProvider() {
        return this.mTileProvider;
    }

    public Scroller getScroller() {
        return this.mScroller;
    }

    public Handler getTileRequestCompleteHandler() {
        return this.mTileRequestCompleteHandler;
    }

    public double getLatitudeSpanDouble() {
        return getBoundingBox().getLatitudeSpan();
    }

    public double getLongitudeSpanDouble() {
        return getBoundingBox().getLongitudeSpan();
    }

    public BoundingBox getBoundingBox() {
        return getProjection().getBoundingBox();
    }

    public Rect getScreenRect(Rect rect) {
        Rect intrinsicScreenRect = getIntrinsicScreenRect(rect);
        if (!(getMapOrientation() == 0.0f || getMapOrientation() == 180.0f)) {
            GeometryMath.getBoundingBoxForRotatatedRectangle(intrinsicScreenRect, intrinsicScreenRect.centerX(), intrinsicScreenRect.centerY(), getMapOrientation(), intrinsicScreenRect);
        }
        return intrinsicScreenRect;
    }

    public Rect getIntrinsicScreenRect(Rect rect) {
        if (rect == null) {
            rect = new Rect();
        }
        rect.set(0, 0, getWidth(), getHeight());
        return rect;
    }

    public Projection getProjection() {
        if (this.mProjection == null) {
            Projection projection = new Projection(this);
            this.mProjection = projection;
            projection.adjustOffsets((IGeoPoint) this.mMultiTouchScaleGeoPoint, this.mMultiTouchScaleCurrentPoint);
            if (this.mScrollableAreaLimitLatitude) {
                projection.adjustOffsets(this.mScrollableAreaLimitNorth, this.mScrollableAreaLimitSouth, true, this.mScrollableAreaLimitExtraPixelHeight);
            }
            if (this.mScrollableAreaLimitLongitude) {
                projection.adjustOffsets(this.mScrollableAreaLimitWest, this.mScrollableAreaLimitEast, false, this.mScrollableAreaLimitExtraPixelWidth);
            }
            this.mImpossibleFlinging = projection.setMapScroll(this);
        }
        return this.mProjection;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void setProjection(Projection projection) {
        this.mProjection = projection;
    }

    private void resetProjection() {
        this.mProjection = null;
    }

    /* access modifiers changed from: package-private */
    @Deprecated
    public void setMapCenter(IGeoPoint iGeoPoint) {
        getController().animateTo(iGeoPoint);
    }

    /* access modifiers changed from: package-private */
    @Deprecated
    public void setMapCenter(int i, int i2) {
        setMapCenter(new GeoPoint(i, i2));
    }

    /* access modifiers changed from: package-private */
    @Deprecated
    public void setMapCenter(double d, double d2) {
        setMapCenter(new GeoPoint(d, d2));
    }

    public boolean isTilesScaledToDpi() {
        return this.mTilesScaledToDpi;
    }

    public void setTilesScaledToDpi(boolean z) {
        this.mTilesScaledToDpi = z;
        updateTileSizeForDensity(getTileProvider().getTileSource());
    }

    public float getTilesScaleFactor() {
        return this.mTilesScaleFactor;
    }

    public void setTilesScaleFactor(float f) {
        this.mTilesScaleFactor = f;
        updateTileSizeForDensity(getTileProvider().getTileSource());
    }

    public void resetTilesScaleFactor() {
        this.mTilesScaleFactor = 1.0f;
        updateTileSizeForDensity(getTileProvider().getTileSource());
    }

    private void updateTileSizeForDensity(ITileSource iTileSource) {
        float tileSizePixels = (float) iTileSource.getTileSizePixels();
        int i = (int) (tileSizePixels * (isTilesScaledToDpi() ? ((getResources().getDisplayMetrics().density * 256.0f) / tileSizePixels) * this.mTilesScaleFactor : this.mTilesScaleFactor));
        if (Configuration.getInstance().isDebugMapView()) {
            Log.d(IMapView.LOGTAG, "Scaling tiles to " + i);
        }
        TileSystem.setTileSize(i);
    }

    public void setTileSource(ITileSource iTileSource) {
        this.mTileProvider.setTileSource(iTileSource);
        updateTileSizeForDensity(iTileSource);
        checkZoomButtons();
        setZoomLevel(this.mZoomLevel);
        postInvalidate();
    }

    /* access modifiers changed from: package-private */
    public double setZoomLevel(double d) {
        double max = Math.max(getMinZoomLevel(), Math.min(getMaxZoomLevel(), d));
        double d2 = this.mZoomLevel;
        int i = (max > d2 ? 1 : (max == d2 ? 0 : -1));
        if (i != 0) {
            Scroller scroller = this.mScroller;
            if (scroller != null) {
                scroller.forceFinished(true);
            }
            this.mIsFlinging = false;
        }
        GeoPoint currentCenter = getProjection().getCurrentCenter();
        this.mZoomLevel = max;
        setExpectedCenter(currentCenter);
        checkZoomButtons();
        ZoomEvent zoomEvent = null;
        if (isLayoutOccurred()) {
            getController().setCenter(currentCenter);
            Point point = new Point();
            Projection projection = getProjection();
            if (getOverlayManager().onSnapToItem((int) this.mMultiTouchScaleInitPoint.x, (int) this.mMultiTouchScaleInitPoint.y, point, this)) {
                getController().animateTo(projection.fromPixels(point.x, point.y, (GeoPoint) null, false));
            }
            this.mTileProvider.rescaleCache(projection, max, d2, getScreenRect(this.mRescaleScreenRect));
            this.pauseFling = true;
        }
        if (i != 0) {
            for (MapListener next : this.mListners) {
                if (zoomEvent == null) {
                    zoomEvent = new ZoomEvent(this, max);
                }
                next.onZoom(zoomEvent);
            }
        }
        requestLayout();
        invalidate();
        return this.mZoomLevel;
    }

    public void zoomToBoundingBox(BoundingBox boundingBox, boolean z) {
        zoomToBoundingBox(boundingBox, z, 0);
    }

    public double zoomToBoundingBox(BoundingBox boundingBox, boolean z, int i, double d, Long l) {
        int i2 = i * 2;
        double boundingBoxZoom = mTileSystem.getBoundingBoxZoom(boundingBox, getWidth() - i2, getHeight() - i2);
        if (boundingBoxZoom == Double.MIN_VALUE || boundingBoxZoom > d) {
            boundingBoxZoom = d;
        }
        double min = Math.min(getMaxZoomLevel(), Math.max(boundingBoxZoom, getMinZoomLevel()));
        GeoPoint centerWithDateLine = boundingBox.getCenterWithDateLine();
        Projection projection = new Projection(min, getWidth(), getHeight(), centerWithDateLine, getMapOrientation(), isHorizontalMapRepetitionEnabled(), isVerticalMapRepetitionEnabled(), getMapCenterOffsetX(), getMapCenterOffsetY());
        Point point = new Point();
        double centerLongitude = boundingBox.getCenterLongitude();
        projection.toPixels(new GeoPoint(boundingBox.getActualNorth(), centerLongitude), point);
        int i3 = point.y;
        projection.toPixels(new GeoPoint(boundingBox.getActualSouth(), centerLongitude), point);
        int height = ((getHeight() - point.y) - i3) / 2;
        if (height != 0) {
            projection.adjustOffsets(0, (long) height);
            projection.fromPixels(getWidth() / 2, getHeight() / 2, centerWithDateLine);
        }
        if (z) {
            getController().animateTo(centerWithDateLine, Double.valueOf(min), l);
        } else {
            getController().setZoom(min);
            getController().setCenter(centerWithDateLine);
        }
        return min;
    }

    public void zoomToBoundingBox(BoundingBox boundingBox, boolean z, int i) {
        zoomToBoundingBox(boundingBox, z, i, getMaxZoomLevel(), (Long) null);
    }

    @Deprecated
    public int getZoomLevel() {
        return (int) getZoomLevelDouble();
    }

    public double getZoomLevelDouble() {
        return this.mZoomLevel;
    }

    @Deprecated
    public double getZoomLevel(boolean z) {
        return getZoomLevelDouble();
    }

    public double getMinZoomLevel() {
        Double d = this.mMinimumZoomLevel;
        return d == null ? (double) this.mMapOverlay.getMinimumZoomLevel() : d.doubleValue();
    }

    public double getMaxZoomLevel() {
        Double d = this.mMaximumZoomLevel;
        return d == null ? (double) this.mMapOverlay.getMaximumZoomLevel() : d.doubleValue();
    }

    public void setMinZoomLevel(Double d) {
        this.mMinimumZoomLevel = d;
    }

    public void setMaxZoomLevel(Double d) {
        this.mMaximumZoomLevel = d;
    }

    public boolean canZoomIn() {
        return this.mZoomLevel < getMaxZoomLevel();
    }

    public boolean canZoomOut() {
        return this.mZoomLevel > getMinZoomLevel();
    }

    /* access modifiers changed from: package-private */
    @Deprecated
    public boolean zoomIn() {
        return getController().zoomIn();
    }

    /* access modifiers changed from: package-private */
    @Deprecated
    public boolean zoomInFixing(IGeoPoint iGeoPoint) {
        Point pixels = getProjection().toPixels(iGeoPoint, (Point) null);
        return getController().zoomInFixing(pixels.x, pixels.y);
    }

    /* access modifiers changed from: package-private */
    @Deprecated
    public boolean zoomInFixing(int i, int i2) {
        return getController().zoomInFixing(i, i2);
    }

    /* access modifiers changed from: package-private */
    @Deprecated
    public boolean zoomOut() {
        return getController().zoomOut();
    }

    /* access modifiers changed from: package-private */
    @Deprecated
    public boolean zoomOutFixing(IGeoPoint iGeoPoint) {
        Point pixels = getProjection().toPixels(iGeoPoint, (Point) null);
        return zoomOutFixing(pixels.x, pixels.y);
    }

    /* access modifiers changed from: package-private */
    @Deprecated
    public boolean zoomOutFixing(int i, int i2) {
        return getController().zoomOutFixing(i, i2);
    }

    public IGeoPoint getMapCenter() {
        return getMapCenter((GeoPoint) null);
    }

    public IGeoPoint getMapCenter(GeoPoint geoPoint) {
        return getProjection().fromPixels(getWidth() / 2, getHeight() / 2, geoPoint, false);
    }

    public void setMapOrientation(float f) {
        setMapOrientation(f, true);
    }

    public void setMapOrientation(float f, boolean z) {
        this.mapOrientation = f % 360.0f;
        if (z) {
            requestLayout();
            invalidate();
        }
    }

    public float getMapOrientation() {
        return this.mapOrientation;
    }

    public boolean useDataConnection() {
        return this.mMapOverlay.useDataConnection();
    }

    public void setUseDataConnection(boolean z) {
        this.mMapOverlay.setUseDataConnection(z);
    }

    public void setScrollableAreaLimitDouble(BoundingBox boundingBox) {
        if (boundingBox == null) {
            resetScrollableAreaLimitLatitude();
            resetScrollableAreaLimitLongitude();
            return;
        }
        setScrollableAreaLimitLatitude(boundingBox.getActualNorth(), boundingBox.getActualSouth(), 0);
        setScrollableAreaLimitLongitude(boundingBox.getLonWest(), boundingBox.getLonEast(), 0);
    }

    public void resetScrollableAreaLimitLatitude() {
        this.mScrollableAreaLimitLatitude = false;
    }

    public void resetScrollableAreaLimitLongitude() {
        this.mScrollableAreaLimitLongitude = false;
    }

    public void setScrollableAreaLimitLatitude(double d, double d2, int i) {
        this.mScrollableAreaLimitLatitude = true;
        this.mScrollableAreaLimitNorth = d;
        this.mScrollableAreaLimitSouth = d2;
        this.mScrollableAreaLimitExtraPixelHeight = i;
    }

    public void setScrollableAreaLimitLongitude(double d, double d2, int i) {
        this.mScrollableAreaLimitLongitude = true;
        this.mScrollableAreaLimitWest = d;
        this.mScrollableAreaLimitEast = d2;
        this.mScrollableAreaLimitExtraPixelWidth = i;
    }

    public boolean isScrollableAreaLimitLatitude() {
        return this.mScrollableAreaLimitLatitude;
    }

    public boolean isScrollableAreaLimitLongitude() {
        return this.mScrollableAreaLimitLongitude;
    }

    public void invalidateMapCoordinates(Rect rect) {
        invalidateMapCoordinates(rect.left, rect.top, rect.right, rect.bottom, false);
    }

    public void invalidateMapCoordinates(int i, int i2, int i3, int i4) {
        invalidateMapCoordinates(i, i2, i3, i4, false);
    }

    public void postInvalidateMapCoordinates(int i, int i2, int i3, int i4) {
        invalidateMapCoordinates(i, i2, i3, i4, true);
    }

    private void invalidateMapCoordinates(int i, int i2, int i3, int i4, boolean z) {
        this.mInvalidateRect.set(i, i2, i3, i4);
        int width = getWidth() / 2;
        int height = getHeight() / 2;
        if (getMapOrientation() != 0.0f) {
            GeometryMath.getBoundingBoxForRotatatedRectangle(this.mInvalidateRect, width, height, getMapOrientation() + 180.0f, this.mInvalidateRect);
        }
        if (z) {
            super.postInvalidate(this.mInvalidateRect.left, this.mInvalidateRect.top, this.mInvalidateRect.right, this.mInvalidateRect.bottom);
        } else {
            super.invalidate(this.mInvalidateRect);
        }
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2, (IGeoPoint) null, 8, 0, 0);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        measureChildren(i, i2);
        super.onMeasure(i, i2);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        myOnLayout(z, i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x008f, code lost:
        r11 = ((long) r11) + r9;
        r9 = (long) r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x00c7, code lost:
        r9 = (long) r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x00c8, code lost:
        r9 = r11 - r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00f3, code lost:
        r9 = r9 + ((long) r11);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void myOnLayout(boolean r15, int r16, int r17, int r18, int r19) {
        /*
            r14 = this;
            r6 = r14
            r14.resetProjection()
            int r0 = r14.getChildCount()
            r1 = 0
        L_0x0009:
            if (r1 >= r0) goto L_0x0118
            android.view.View r2 = r14.getChildAt(r1)
            int r3 = r2.getVisibility()
            r4 = 8
            if (r3 == r4) goto L_0x0114
            android.view.ViewGroup$LayoutParams r3 = r2.getLayoutParams()
            org.osmdroid.views.MapView$LayoutParams r3 = (org.osmdroid.views.MapView.LayoutParams) r3
            int r4 = r2.getMeasuredHeight()
            int r5 = r2.getMeasuredWidth()
            org.osmdroid.views.Projection r7 = r14.getProjection()
            org.osmdroid.api.IGeoPoint r8 = r3.geoPoint
            android.graphics.Point r9 = r6.mLayoutPoint
            r7.toPixels(r8, r9)
            float r7 = r14.getMapOrientation()
            r8 = 0
            int r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1))
            if (r7 == 0) goto L_0x0056
            org.osmdroid.views.Projection r7 = r14.getProjection()
            android.graphics.Point r8 = r6.mLayoutPoint
            int r8 = r8.x
            android.graphics.Point r9 = r6.mLayoutPoint
            int r9 = r9.y
            r10 = 0
            android.graphics.Point r7 = r7.rotateAndScalePoint(r8, r9, r10)
            android.graphics.Point r8 = r6.mLayoutPoint
            int r9 = r7.x
            r8.x = r9
            android.graphics.Point r8 = r6.mLayoutPoint
            int r7 = r7.y
            r8.y = r7
        L_0x0056:
            android.graphics.Point r7 = r6.mLayoutPoint
            int r7 = r7.x
            long r7 = (long) r7
            android.graphics.Point r9 = r6.mLayoutPoint
            int r9 = r9.y
            long r9 = (long) r9
            int r11 = r3.alignment
            switch(r11) {
                case 1: goto L_0x00e9;
                case 2: goto L_0x00d9;
                case 3: goto L_0x00cb;
                case 4: goto L_0x00b9;
                case 5: goto L_0x00a5;
                case 6: goto L_0x0093;
                case 7: goto L_0x0085;
                case 8: goto L_0x0075;
                case 9: goto L_0x0067;
                default: goto L_0x0065;
            }
        L_0x0065:
            goto L_0x00f5
        L_0x0067:
            int r11 = r14.getPaddingLeft()
            long r11 = (long) r11
            long r11 = r11 + r7
            long r7 = (long) r5
            long r7 = r11 - r7
            int r11 = r14.getPaddingTop()
            goto L_0x008f
        L_0x0075:
            int r11 = r14.getPaddingLeft()
            long r11 = (long) r11
            long r11 = r11 + r7
            int r7 = r5 / 2
            long r7 = (long) r7
            long r7 = r11 - r7
            int r11 = r14.getPaddingTop()
            goto L_0x008f
        L_0x0085:
            int r11 = r14.getPaddingLeft()
            long r11 = (long) r11
            long r7 = r7 + r11
            int r11 = r14.getPaddingTop()
        L_0x008f:
            long r11 = (long) r11
            long r11 = r11 + r9
            long r9 = (long) r4
            goto L_0x00c8
        L_0x0093:
            int r11 = r14.getPaddingLeft()
            long r11 = (long) r11
            long r11 = r11 + r7
            long r7 = (long) r5
            long r7 = r11 - r7
            int r11 = r14.getPaddingTop()
            long r11 = (long) r11
            long r11 = r11 + r9
            int r9 = r4 / 2
            goto L_0x00c7
        L_0x00a5:
            int r11 = r14.getPaddingLeft()
            long r11 = (long) r11
            long r11 = r11 + r7
            int r7 = r5 / 2
            long r7 = (long) r7
            long r7 = r11 - r7
            int r11 = r14.getPaddingTop()
            long r11 = (long) r11
            long r11 = r11 + r9
            int r9 = r4 / 2
            goto L_0x00c7
        L_0x00b9:
            int r11 = r14.getPaddingLeft()
            long r11 = (long) r11
            long r7 = r7 + r11
            int r11 = r14.getPaddingTop()
            long r11 = (long) r11
            long r11 = r11 + r9
            int r9 = r4 / 2
        L_0x00c7:
            long r9 = (long) r9
        L_0x00c8:
            long r9 = r11 - r9
            goto L_0x00f5
        L_0x00cb:
            int r11 = r14.getPaddingLeft()
            long r11 = (long) r11
            long r11 = r11 + r7
            long r7 = (long) r5
            long r7 = r11 - r7
            int r11 = r14.getPaddingTop()
            goto L_0x00f3
        L_0x00d9:
            int r11 = r14.getPaddingLeft()
            long r11 = (long) r11
            long r11 = r11 + r7
            int r7 = r5 / 2
            long r7 = (long) r7
            long r7 = r11 - r7
            int r11 = r14.getPaddingTop()
            goto L_0x00f3
        L_0x00e9:
            int r11 = r14.getPaddingLeft()
            long r11 = (long) r11
            long r7 = r7 + r11
            int r11 = r14.getPaddingTop()
        L_0x00f3:
            long r11 = (long) r11
            long r9 = r9 + r11
        L_0x00f5:
            int r11 = r3.offsetX
            long r11 = (long) r11
            long r7 = r7 + r11
            int r3 = r3.offsetY
            long r11 = (long) r3
            long r9 = r9 + r11
            int r3 = org.osmdroid.util.TileSystem.truncateToInt(r7)
            int r11 = org.osmdroid.util.TileSystem.truncateToInt(r9)
            long r12 = (long) r5
            long r7 = r7 + r12
            int r5 = org.osmdroid.util.TileSystem.truncateToInt(r7)
            long r7 = (long) r4
            long r9 = r9 + r7
            int r4 = org.osmdroid.util.TileSystem.truncateToInt(r9)
            r2.layout(r3, r11, r5, r4)
        L_0x0114:
            int r1 = r1 + 1
            goto L_0x0009
        L_0x0118:
            boolean r0 = r14.isLayoutOccurred()
            if (r0 != 0) goto L_0x0145
            r0 = 1
            r6.mLayoutOccurred = r0
            java.util.LinkedList<org.osmdroid.views.MapView$OnFirstLayoutListener> r0 = r6.mOnFirstLayoutListeners
            java.util.Iterator r7 = r0.iterator()
        L_0x0127:
            boolean r0 = r7.hasNext()
            if (r0 == 0) goto L_0x0140
            java.lang.Object r0 = r7.next()
            org.osmdroid.views.MapView$OnFirstLayoutListener r0 = (org.osmdroid.views.MapView.OnFirstLayoutListener) r0
            r1 = r14
            r2 = r16
            r3 = r17
            r4 = r18
            r5 = r19
            r0.onFirstLayout(r1, r2, r3, r4, r5)
            goto L_0x0127
        L_0x0140:
            java.util.LinkedList<org.osmdroid.views.MapView$OnFirstLayoutListener> r0 = r6.mOnFirstLayoutListeners
            r0.clear()
        L_0x0145:
            r14.resetProjection()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.MapView.myOnLayout(boolean, int, int, int, int):void");
    }

    public void addOnFirstLayoutListener(OnFirstLayoutListener onFirstLayoutListener) {
        if (!isLayoutOccurred()) {
            this.mOnFirstLayoutListeners.add(onFirstLayoutListener);
        }
    }

    public void removeOnFirstLayoutListener(OnFirstLayoutListener onFirstLayoutListener) {
        this.mOnFirstLayoutListeners.remove(onFirstLayoutListener);
    }

    public boolean isLayoutOccurred() {
        return this.mLayoutOccurred;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void onPause() {
        getOverlayManager().onPause();
    }

    public void onResume() {
        getOverlayManager().onResume();
    }

    public void onDetach() {
        getOverlayManager().onDetach(this);
        this.mTileProvider.detach();
        CustomZoomButtonsController customZoomButtonsController = this.mZoomController;
        if (customZoomButtonsController != null) {
            customZoomButtonsController.onDetach();
        }
        Handler handler = this.mTileRequestCompleteHandler;
        if (handler instanceof SimpleInvalidationHandler) {
            ((SimpleInvalidationHandler) handler).destroy();
        }
        this.mTileRequestCompleteHandler = null;
        Projection projection = this.mProjection;
        if (projection != null) {
            projection.detach();
        }
        this.mProjection = null;
        this.mRepository.onDetach();
        this.mListners.clear();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return getOverlayManager().onKeyDown(i, keyEvent, this) || super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return getOverlayManager().onKeyUp(i, keyEvent, this) || super.onKeyUp(i, keyEvent);
    }

    public boolean onTrackballEvent(MotionEvent motionEvent) {
        if (getOverlayManager().onTrackballEvent(motionEvent, this)) {
            return true;
        }
        scrollBy((int) (motionEvent.getX() * 25.0f), (int) (motionEvent.getY() * 25.0f));
        return super.onTrackballEvent(motionEvent);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean z;
        if (Configuration.getInstance().isDebugMapView()) {
            Log.d(IMapView.LOGTAG, "dispatchTouchEvent(" + motionEvent + ")");
        }
        if (this.mZoomController.isTouched(motionEvent)) {
            this.mZoomController.activate();
            return true;
        }
        MotionEvent rotateTouchEvent = rotateTouchEvent(motionEvent);
        try {
            if (super.dispatchTouchEvent(motionEvent)) {
                if (Configuration.getInstance().isDebugMapView()) {
                    Log.d(IMapView.LOGTAG, "super handled onTouchEvent");
                }
                return true;
            } else if (getOverlayManager().onTouchEvent(rotateTouchEvent, this)) {
                if (rotateTouchEvent != motionEvent) {
                    rotateTouchEvent.recycle();
                }
                return true;
            } else {
                MultiTouchController<Object> multiTouchController = this.mMultiTouchController;
                if (multiTouchController == null || !multiTouchController.onTouchEvent(motionEvent)) {
                    z = false;
                } else {
                    if (Configuration.getInstance().isDebugMapView()) {
                        Log.d(IMapView.LOGTAG, "mMultiTouchController handled onTouchEvent");
                    }
                    z = true;
                }
                if (this.mGestureDetector.onTouchEvent(rotateTouchEvent)) {
                    if (Configuration.getInstance().isDebugMapView()) {
                        Log.d(IMapView.LOGTAG, "mGestureDetector handled onTouchEvent");
                    }
                    z = true;
                }
                if (z) {
                    if (rotateTouchEvent != motionEvent) {
                        rotateTouchEvent.recycle();
                    }
                    return true;
                }
                if (rotateTouchEvent != motionEvent) {
                    rotateTouchEvent.recycle();
                }
                if (Configuration.getInstance().isDebugMapView()) {
                    Log.d(IMapView.LOGTAG, "no-one handled onTouchEvent");
                }
                return false;
            }
        } finally {
            if (rotateTouchEvent != motionEvent) {
                rotateTouchEvent.recycle();
            }
        }
    }

    private MotionEvent rotateTouchEvent(MotionEvent motionEvent) {
        if (getMapOrientation() == 0.0f) {
            return motionEvent;
        }
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        if (Build.VERSION.SDK_INT < 11) {
            getProjection().unrotateAndScalePoint((int) motionEvent.getX(), (int) motionEvent.getY(), this.mRotateScalePoint);
            obtain.setLocation((float) this.mRotateScalePoint.x, (float) this.mRotateScalePoint.y);
        } else {
            obtain.transform(getProjection().getInvertedScaleRotateCanvasMatrix());
        }
        return obtain;
    }

    public void computeScroll() {
        Scroller scroller = this.mScroller;
        if (scroller == null || !this.mIsFlinging || !scroller.computeScrollOffset()) {
            return;
        }
        if (this.mScroller.isFinished()) {
            this.mIsFlinging = false;
            return;
        }
        scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
        postInvalidate();
    }

    public void scrollTo(int i, int i2) {
        setMapScroll((long) i, (long) i2);
        resetProjection();
        invalidate();
        if (getMapOrientation() != 0.0f) {
            myOnLayout(true, getLeft(), getTop(), getRight(), getBottom());
        }
        ScrollEvent scrollEvent = null;
        for (MapListener next : this.mListners) {
            if (scrollEvent == null) {
                scrollEvent = new ScrollEvent(this, i, i2);
            }
            next.onScroll(scrollEvent);
        }
    }

    public void scrollBy(int i, int i2) {
        scrollTo((int) (getMapScrollX() + ((long) i)), (int) (getMapScrollY() + ((long) i2)));
    }

    public void setBackgroundColor(int i) {
        this.mMapOverlay.setLoadingBackgroundColor(i);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        long currentTimeMillis = System.currentTimeMillis();
        resetProjection();
        getProjection().save(canvas, true, false);
        try {
            getOverlayManager().onDraw(canvas, this);
            getProjection().restore(canvas, false);
            CustomZoomButtonsController customZoomButtonsController = this.mZoomController;
            if (customZoomButtonsController != null) {
                customZoomButtonsController.draw(canvas);
            }
            super.dispatchDraw(canvas);
        } catch (Exception e) {
            Log.e(IMapView.LOGTAG, "error dispatchDraw, probably in edit mode", e);
        }
        if (Configuration.getInstance().isDebugMapView()) {
            long currentTimeMillis2 = System.currentTimeMillis();
            Log.d(IMapView.LOGTAG, "Rendering overall: " + (currentTimeMillis2 - currentTimeMillis) + "ms");
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        if (this.mDestroyModeOnDetach) {
            onDetach();
        }
        super.onDetachedFromWindow();
    }

    public boolean isAnimating() {
        return this.mIsAnimating.get();
    }

    public Object getDraggableObjectAtPoint(MultiTouchController.PointInfo pointInfo) {
        if (isAnimating()) {
            return null;
        }
        setMultiTouchScaleInitPoint(pointInfo.getX(), pointInfo.getY());
        return this;
    }

    public void getPositionAndScale(Object obj, MultiTouchController.PositionAndScale positionAndScale) {
        startAnimation();
        positionAndScale.set(this.mMultiTouchScaleInitPoint.x, this.mMultiTouchScaleInitPoint.y, true, 1.0f, false, 0.0f, 0.0f, false, 0.0f);
    }

    public void selectObject(Object obj, MultiTouchController.PointInfo pointInfo) {
        if (this.mZoomRounding) {
            this.mZoomLevel = (double) Math.round(this.mZoomLevel);
            invalidate();
        }
        resetMultiTouchScale();
    }

    public boolean setPositionAndScale(Object obj, MultiTouchController.PositionAndScale positionAndScale, MultiTouchController.PointInfo pointInfo) {
        setMultiTouchScaleCurrentPoint(positionAndScale.getXOff(), positionAndScale.getYOff());
        setMultiTouchScale(positionAndScale.getScale());
        requestLayout();
        invalidate();
        return true;
    }

    public void resetMultiTouchScale() {
        this.mMultiTouchScaleCurrentPoint = null;
    }

    /* access modifiers changed from: protected */
    public void setMultiTouchScaleInitPoint(float f, float f2) {
        this.mMultiTouchScaleInitPoint.set(f, f2);
        Point unrotateAndScalePoint = getProjection().unrotateAndScalePoint((int) f, (int) f2, (Point) null);
        getProjection().fromPixels(unrotateAndScalePoint.x, unrotateAndScalePoint.y, this.mMultiTouchScaleGeoPoint);
        setMultiTouchScaleCurrentPoint(f, f2);
    }

    /* access modifiers changed from: protected */
    public void setMultiTouchScaleCurrentPoint(float f, float f2) {
        this.mMultiTouchScaleCurrentPoint = new PointF(f, f2);
    }

    /* access modifiers changed from: protected */
    public void setMultiTouchScale(float f) {
        setZoomLevel((Math.log((double) f) / Math.log(2.0d)) + this.mStartAnimationZoom);
    }

    /* access modifiers changed from: protected */
    public void startAnimation() {
        this.mStartAnimationZoom = getZoomLevelDouble();
    }

    @Deprecated
    public void setMapListener(MapListener mapListener) {
        this.mListners.add(mapListener);
    }

    public void addMapListener(MapListener mapListener) {
        this.mListners.add(mapListener);
    }

    public void removeMapListener(MapListener mapListener) {
        this.mListners.remove(mapListener);
    }

    private void checkZoomButtons() {
        this.mZoomController.setZoomInEnabled(canZoomIn());
        this.mZoomController.setZoomOutEnabled(canZoomOut());
    }

    @Deprecated
    public void setBuiltInZoomControls(boolean z) {
        CustomZoomButtonsController.Visibility visibility;
        CustomZoomButtonsController customZoomButtonsController = this.mZoomController;
        if (z) {
            visibility = CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT;
        } else {
            visibility = CustomZoomButtonsController.Visibility.NEVER;
        }
        customZoomButtonsController.setVisibility(visibility);
    }

    public void setMultiTouchControls(boolean z) {
        this.mMultiTouchController = z ? new MultiTouchController<>(this, false) : null;
    }

    public boolean isHorizontalMapRepetitionEnabled() {
        return this.horizontalMapRepetitionEnabled;
    }

    public void setHorizontalMapRepetitionEnabled(boolean z) {
        this.horizontalMapRepetitionEnabled = z;
        this.mMapOverlay.setHorizontalWrapEnabled(z);
        resetProjection();
        invalidate();
    }

    public boolean isVerticalMapRepetitionEnabled() {
        return this.verticalMapRepetitionEnabled;
    }

    public void setVerticalMapRepetitionEnabled(boolean z) {
        this.verticalMapRepetitionEnabled = z;
        this.mMapOverlay.setVerticalWrapEnabled(z);
        resetProjection();
        invalidate();
    }

    /* JADX WARNING: type inference failed for: r3v7, types: [org.osmdroid.tileprovider.tilesource.ITileSource, java.lang.Object] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.osmdroid.tileprovider.tilesource.ITileSource getTileSourceFromAttributes(android.util.AttributeSet r7) {
        /*
            r6 = this;
            org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase r0 = org.osmdroid.tileprovider.tilesource.TileSourceFactory.DEFAULT_TILE_SOURCE
            r1 = 0
            java.lang.String r2 = "OsmDroid"
            if (r7 == 0) goto L_0x003d
            java.lang.String r3 = "tilesource"
            java.lang.String r3 = r7.getAttributeValue(r1, r3)
            if (r3 == 0) goto L_0x003d
            org.osmdroid.tileprovider.tilesource.ITileSource r3 = org.osmdroid.tileprovider.tilesource.TileSourceFactory.getTileSource((java.lang.String) r3)     // Catch:{ IllegalArgumentException -> 0x0029 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IllegalArgumentException -> 0x0029 }
            r4.<init>()     // Catch:{ IllegalArgumentException -> 0x0029 }
            java.lang.String r5 = "Using tile source specified in layout attributes: "
            r4.append(r5)     // Catch:{ IllegalArgumentException -> 0x0029 }
            r4.append(r3)     // Catch:{ IllegalArgumentException -> 0x0029 }
            java.lang.String r4 = r4.toString()     // Catch:{ IllegalArgumentException -> 0x0029 }
            android.util.Log.i(r2, r4)     // Catch:{ IllegalArgumentException -> 0x0029 }
            r0 = r3
            goto L_0x003d
        L_0x0029:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Invalid tile source specified in layout attributes: "
            r3.append(r4)
            r3.append(r0)
            java.lang.String r3 = r3.toString()
            android.util.Log.w(r2, r3)
        L_0x003d:
            if (r7 == 0) goto L_0x006b
            boolean r3 = r0 instanceof org.osmdroid.tileprovider.tilesource.IStyledTileSource
            if (r3 == 0) goto L_0x006b
            java.lang.String r3 = "style"
            java.lang.String r7 = r7.getAttributeValue(r1, r3)
            if (r7 != 0) goto L_0x0051
            java.lang.String r7 = "Using default style: 1"
            android.util.Log.i(r2, r7)
            goto L_0x006b
        L_0x0051:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "Using style specified in layout attributes: "
            r1.append(r3)
            r1.append(r7)
            java.lang.String r1 = r1.toString()
            android.util.Log.i(r2, r1)
            r1 = r0
            org.osmdroid.tileprovider.tilesource.IStyledTileSource r1 = (org.osmdroid.tileprovider.tilesource.IStyledTileSource) r1
            r1.setStyle((java.lang.String) r7)
        L_0x006b:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r1 = "Using tile source: "
            r7.append(r1)
            java.lang.String r1 = r0.name()
            r7.append(r1)
            java.lang.String r7 = r7.toString()
            android.util.Log.i(r2, r7)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.MapView.getTileSourceFromAttributes(android.util.AttributeSet):org.osmdroid.tileprovider.tilesource.ITileSource");
    }

    public void setFlingEnabled(boolean z) {
        this.enableFling = z;
    }

    public boolean isFlingEnabled() {
        return this.enableFling;
    }

    private class MapViewGestureDetectorListener implements GestureDetector.OnGestureListener {
        private MapViewGestureDetectorListener() {
        }

        public boolean onDown(MotionEvent motionEvent) {
            if (MapView.this.mIsFlinging) {
                if (MapView.this.mScroller != null) {
                    MapView.this.mScroller.abortAnimation();
                }
                MapView.this.mIsFlinging = false;
            }
            if (!MapView.this.getOverlayManager().onDown(motionEvent, MapView.this) && MapView.this.mZoomController != null) {
                MapView.this.mZoomController.activate();
            }
            return true;
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!MapView.this.enableFling || MapView.this.pauseFling) {
                boolean unused = MapView.this.pauseFling = false;
                return false;
            }
            if (MapView.this.getOverlayManager().onFling(motionEvent, motionEvent2, f, f2, MapView.this)) {
                return true;
            }
            if (MapView.this.mImpossibleFlinging) {
                boolean unused2 = MapView.this.mImpossibleFlinging = false;
                return false;
            }
            MapView.this.mIsFlinging = true;
            if (MapView.this.mScroller != null) {
                MapView.this.mScroller.fling((int) MapView.this.getMapScrollX(), (int) MapView.this.getMapScrollY(), -((int) f), -((int) f2), Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
            return true;
        }

        public void onLongPress(MotionEvent motionEvent) {
            if (MapView.this.mMultiTouchController == null || !MapView.this.mMultiTouchController.isPinching()) {
                MapView.this.getOverlayManager().onLongPress(motionEvent, MapView.this);
            }
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (MapView.this.getOverlayManager().onScroll(motionEvent, motionEvent2, f, f2, MapView.this)) {
                return true;
            }
            MapView.this.scrollBy((int) f, (int) f2);
            return true;
        }

        public void onShowPress(MotionEvent motionEvent) {
            MapView.this.getOverlayManager().onShowPress(motionEvent, MapView.this);
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return MapView.this.getOverlayManager().onSingleTapUp(motionEvent, MapView.this);
        }
    }

    private class MapViewDoubleClickListener implements GestureDetector.OnDoubleTapListener {
        private MapViewDoubleClickListener() {
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (MapView.this.getOverlayManager().onDoubleTap(motionEvent, MapView.this)) {
                return true;
            }
            MapView.this.getProjection().rotateAndScalePoint((int) motionEvent.getX(), (int) motionEvent.getY(), MapView.this.mRotateScalePoint);
            return MapView.this.getController().zoomInFixing(MapView.this.mRotateScalePoint.x, MapView.this.mRotateScalePoint.y);
        }

        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            return MapView.this.getOverlayManager().onDoubleTapEvent(motionEvent, MapView.this);
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            return MapView.this.getOverlayManager().onSingleTapConfirmed(motionEvent, MapView.this);
        }
    }

    private class MapViewZoomListener implements CustomZoomButtonsController.OnZoomListener, ZoomButtonsController.OnZoomListener {
        public void onVisibilityChanged(boolean z) {
        }

        private MapViewZoomListener() {
        }

        public void onZoom(boolean z) {
            if (z) {
                MapView.this.getController().zoomIn();
            } else {
                MapView.this.getController().zoomOut();
            }
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public static final int BOTTOM_CENTER = 8;
        public static final int BOTTOM_LEFT = 7;
        public static final int BOTTOM_RIGHT = 9;
        public static final int CENTER = 5;
        public static final int CENTER_LEFT = 4;
        public static final int CENTER_RIGHT = 6;
        public static final int TOP_CENTER = 2;
        public static final int TOP_LEFT = 1;
        public static final int TOP_RIGHT = 3;
        public int alignment;
        public IGeoPoint geoPoint;
        public int offsetX;
        public int offsetY;

        public LayoutParams(int i, int i2, IGeoPoint iGeoPoint, int i3, int i4, int i5) {
            super(i, i2);
            if (iGeoPoint != null) {
                this.geoPoint = iGeoPoint;
            } else {
                this.geoPoint = new GeoPoint(0.0d, 0.0d);
            }
            this.alignment = i3;
            this.offsetX = i4;
            this.offsetY = i5;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.geoPoint = new GeoPoint(0.0d, 0.0d);
            this.alignment = 8;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    public void setTileProvider(MapTileProviderBase mapTileProviderBase) {
        this.mTileProvider.detach();
        this.mTileProvider.clearTileCache();
        this.mTileProvider = mapTileProviderBase;
        mapTileProviderBase.getTileRequestCompleteHandlers().add(this.mTileRequestCompleteHandler);
        updateTileSizeForDensity(this.mTileProvider.getTileSource());
        TilesOverlay tilesOverlay = new TilesOverlay(this.mTileProvider, getContext(), this.horizontalMapRepetitionEnabled, this.verticalMapRepetitionEnabled);
        this.mMapOverlay = tilesOverlay;
        this.mOverlayManager.setTilesOverlay(tilesOverlay);
        invalidate();
    }

    @Deprecated
    public void setInitCenter(IGeoPoint iGeoPoint) {
        setExpectedCenter(iGeoPoint);
    }

    public long getMapScrollX() {
        return this.mMapScrollX;
    }

    public long getMapScrollY() {
        return this.mMapScrollY;
    }

    /* access modifiers changed from: package-private */
    public void setMapScroll(long j, long j2) {
        this.mMapScrollX = j;
        this.mMapScrollY = j2;
        requestLayout();
    }

    /* access modifiers changed from: package-private */
    public GeoPoint getExpectedCenter() {
        return this.mCenter;
    }

    public void setExpectedCenter(IGeoPoint iGeoPoint, long j, long j2) {
        GeoPoint currentCenter = getProjection().getCurrentCenter();
        this.mCenter = (GeoPoint) iGeoPoint;
        setMapScroll(-j, -j2);
        resetProjection();
        if (!getProjection().getCurrentCenter().equals(currentCenter)) {
            ScrollEvent scrollEvent = null;
            for (MapListener next : this.mListners) {
                if (scrollEvent == null) {
                    scrollEvent = new ScrollEvent(this, 0, 0);
                }
                next.onScroll(scrollEvent);
            }
        }
        invalidate();
    }

    public void setExpectedCenter(IGeoPoint iGeoPoint) {
        setExpectedCenter(iGeoPoint, 0, 0);
    }

    public void setZoomRounding(boolean z) {
        this.mZoomRounding = z;
    }

    public static TileSystem getTileSystem() {
        return mTileSystem;
    }

    public static void setTileSystem(TileSystem tileSystem) {
        mTileSystem = tileSystem;
    }

    public MapViewRepository getRepository() {
        return this.mRepository;
    }

    public CustomZoomButtonsController getZoomController() {
        return this.mZoomController;
    }

    public TilesOverlay getMapOverlay() {
        return this.mMapOverlay;
    }

    public void setDestroyMode(boolean z) {
        this.mDestroyModeOnDetach = z;
    }

    public int getMapCenterOffsetX() {
        return this.mMapCenterOffsetX;
    }

    public int getMapCenterOffsetY() {
        return this.mMapCenterOffsetY;
    }

    public void setMapCenterOffset(int i, int i2) {
        this.mMapCenterOffsetX = i;
        this.mMapCenterOffsetY = i2;
    }
}
