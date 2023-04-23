package org.osmdroid.views.overlay.mylocation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import java.util.Iterator;
import java.util.LinkedList;
import org.osmdroid.api.IMapController;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.C1340R;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.IOverlayMenuProvider;
import org.osmdroid.views.overlay.Overlay;

public class MyLocationNewOverlay extends Overlay implements IMyLocationConsumer, IOverlayMenuProvider, Overlay.Snappable {
    public static final int MENU_MY_LOCATION = getSafeMenuId();
    protected boolean enableAutoStop;
    protected Paint mCirclePaint;
    protected Bitmap mDirectionArrowBitmap;
    protected float mDirectionArrowCenterX;
    protected float mDirectionArrowCenterY;
    protected boolean mDrawAccuracyEnabled;
    private final Point mDrawPixel;
    private final GeoPoint mGeoPoint;
    private Handler mHandler;
    private Object mHandlerToken;
    protected boolean mIsFollowing;
    private boolean mIsLocationEnabled;
    private Location mLocation;
    private IMapController mMapController;
    protected MapView mMapView;
    public IMyLocationProvider mMyLocationProvider;
    private boolean mOptionsMenuEnabled;
    protected Paint mPaint;
    protected Bitmap mPersonBitmap;
    protected final PointF mPersonHotspot;
    /* access modifiers changed from: private */
    public final LinkedList<Runnable> mRunOnFirstFix;
    private final Point mSnapPixel;
    private boolean wasEnabledOnPause;

    public MyLocationNewOverlay(MapView mapView) {
        this(new GpsMyLocationProvider(mapView.getContext()), mapView);
    }

    public MyLocationNewOverlay(IMyLocationProvider iMyLocationProvider, MapView mapView) {
        this.mPaint = new Paint();
        this.mCirclePaint = new Paint();
        this.mRunOnFirstFix = new LinkedList<>();
        this.mDrawPixel = new Point();
        this.mSnapPixel = new Point();
        this.mHandlerToken = new Object();
        this.enableAutoStop = true;
        this.mGeoPoint = new GeoPoint(0, 0);
        this.mIsLocationEnabled = false;
        this.mIsFollowing = false;
        this.mDrawAccuracyEnabled = true;
        this.mOptionsMenuEnabled = true;
        this.wasEnabledOnPause = false;
        this.mMapView = mapView;
        this.mMapController = mapView.getController();
        this.mCirclePaint.setARGB(0, 100, 100, 255);
        this.mCirclePaint.setAntiAlias(true);
        this.mPaint.setFilterBitmap(true);
        setPersonIcon(((BitmapDrawable) mapView.getContext().getResources().getDrawable(C1340R.C1341drawable.person)).getBitmap());
        setDirectionIcon(((BitmapDrawable) mapView.getContext().getResources().getDrawable(C1340R.C1341drawable.round_navigation_white_48)).getBitmap());
        this.mPersonHotspot = new PointF();
        setPersonAnchor(0.5f, 0.8125f);
        setDirectionAnchor(0.5f, 0.5f);
        this.mHandler = new Handler(Looper.getMainLooper());
        setMyLocationProvider(iMyLocationProvider);
    }

    @Deprecated
    public void setDirectionArrow(Bitmap bitmap, Bitmap bitmap2) {
        setPersonIcon(bitmap);
        setDirectionIcon(bitmap2);
        setDirectionAnchor(0.5f, 0.5f);
    }

    public void setDirectionIcon(Bitmap bitmap) {
        this.mDirectionArrowBitmap = bitmap;
    }

    public void onResume() {
        super.onResume();
        if (this.wasEnabledOnPause) {
            enableFollowLocation();
        }
        enableMyLocation();
    }

    public void onPause() {
        this.wasEnabledOnPause = this.mIsFollowing;
        disableMyLocation();
        super.onPause();
    }

    public void onDetach(MapView mapView) {
        disableMyLocation();
        this.mMapView = null;
        this.mHandler = null;
        this.mCirclePaint = null;
        this.mHandlerToken = null;
        this.mLocation = null;
        this.mMapController = null;
        IMyLocationProvider iMyLocationProvider = this.mMyLocationProvider;
        if (iMyLocationProvider != null) {
            iMyLocationProvider.destroy();
        }
        this.mMyLocationProvider = null;
        super.onDetach(mapView);
    }

    public void setDrawAccuracyEnabled(boolean z) {
        this.mDrawAccuracyEnabled = z;
    }

    public boolean isDrawAccuracyEnabled() {
        return this.mDrawAccuracyEnabled;
    }

    public IMyLocationProvider getMyLocationProvider() {
        return this.mMyLocationProvider;
    }

    /* access modifiers changed from: protected */
    public void setMyLocationProvider(IMyLocationProvider iMyLocationProvider) {
        if (iMyLocationProvider != null) {
            if (isMyLocationEnabled()) {
                stopLocationProvider();
            }
            this.mMyLocationProvider = iMyLocationProvider;
            return;
        }
        throw new RuntimeException("You must pass an IMyLocationProvider to setMyLocationProvider()");
    }

    @Deprecated
    public void setPersonHotspot(float f, float f2) {
        this.mPersonHotspot.set(f, f2);
    }

    /* access modifiers changed from: protected */
    public void drawMyLocation(Canvas canvas, Projection projection, Location location) {
        projection.toPixels(this.mGeoPoint, this.mDrawPixel);
        if (this.mDrawAccuracyEnabled) {
            float accuracy = location.getAccuracy() / ((float) TileSystem.GroundResolution(location.getLatitude(), projection.getZoomLevel()));
            this.mCirclePaint.setAlpha(50);
            this.mCirclePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle((float) this.mDrawPixel.x, (float) this.mDrawPixel.y, accuracy, this.mCirclePaint);
            this.mCirclePaint.setAlpha(150);
            this.mCirclePaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle((float) this.mDrawPixel.x, (float) this.mDrawPixel.y, accuracy, this.mCirclePaint);
        }
        if (location.hasBearing()) {
            canvas.save();
            float bearing = location.getBearing();
            if (bearing >= 360.0f) {
                bearing -= 360.0f;
            }
            canvas.rotate(bearing, (float) this.mDrawPixel.x, (float) this.mDrawPixel.y);
            canvas.drawBitmap(this.mDirectionArrowBitmap, ((float) this.mDrawPixel.x) - this.mDirectionArrowCenterX, ((float) this.mDrawPixel.y) - this.mDirectionArrowCenterY, this.mPaint);
            canvas.restore();
            return;
        }
        canvas.save();
        canvas.rotate(-this.mMapView.getMapOrientation(), (float) this.mDrawPixel.x, (float) this.mDrawPixel.y);
        canvas.drawBitmap(this.mPersonBitmap, ((float) this.mDrawPixel.x) - this.mPersonHotspot.x, ((float) this.mDrawPixel.y) - this.mPersonHotspot.y, this.mPaint);
        canvas.restore();
    }

    public void draw(Canvas canvas, Projection projection) {
        if (this.mLocation != null && isMyLocationEnabled()) {
            drawMyLocation(canvas, projection, this.mLocation);
        }
    }

    public boolean onSnapToItem(int i, int i2, Point point, IMapView iMapView) {
        boolean z = false;
        if (this.mLocation != null) {
            this.mMapView.getProjection().toPixels(this.mGeoPoint, this.mSnapPixel);
            point.x = this.mSnapPixel.x;
            point.y = this.mSnapPixel.y;
            double d = (double) (i - this.mSnapPixel.x);
            double d2 = (double) (i2 - this.mSnapPixel.y);
            if ((d * d) + (d2 * d2) < 64.0d) {
                z = true;
            }
            if (Configuration.getInstance().isDebugMode()) {
                Log.d(IMapView.LOGTAG, "snap=" + z);
            }
        }
        return z;
    }

    public void setEnableAutoStop(boolean z) {
        this.enableAutoStop = z;
    }

    public boolean getEnableAutoStop() {
        return this.enableAutoStop;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        boolean z = motionEvent.getAction() == 2 && motionEvent.getPointerCount() == 1;
        if (motionEvent.getAction() == 0 && this.enableAutoStop) {
            disableFollowLocation();
        } else if (z && isFollowLocationEnabled()) {
            return true;
        }
        return super.onTouchEvent(motionEvent, mapView);
    }

    public void setOptionsMenuEnabled(boolean z) {
        this.mOptionsMenuEnabled = z;
    }

    public boolean isOptionsMenuEnabled() {
        return this.mOptionsMenuEnabled;
    }

    public boolean onCreateOptionsMenu(Menu menu, int i, MapView mapView) {
        menu.add(0, MENU_MY_LOCATION + i, 0, mapView.getContext().getResources().getString(C1340R.string.my_location)).setIcon(mapView.getContext().getResources().getDrawable(C1340R.C1341drawable.ic_menu_mylocation)).setCheckable(true);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu, int i, MapView mapView) {
        menu.findItem(MENU_MY_LOCATION + i).setChecked(isMyLocationEnabled());
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem, int i, MapView mapView) {
        if (menuItem.getItemId() - i != MENU_MY_LOCATION) {
            return false;
        }
        if (isMyLocationEnabled()) {
            disableFollowLocation();
            disableMyLocation();
            return true;
        }
        enableFollowLocation();
        enableMyLocation();
        return true;
    }

    public GeoPoint getMyLocation() {
        if (this.mLocation == null) {
            return null;
        }
        return new GeoPoint(this.mLocation);
    }

    public Location getLastFix() {
        return this.mLocation;
    }

    public void enableFollowLocation() {
        Location lastKnownLocation;
        this.mIsFollowing = true;
        if (isMyLocationEnabled() && (lastKnownLocation = this.mMyLocationProvider.getLastKnownLocation()) != null) {
            setLocation(lastKnownLocation);
        }
        MapView mapView = this.mMapView;
        if (mapView != null) {
            mapView.postInvalidate();
        }
    }

    public void disableFollowLocation() {
        IMapController iMapController = this.mMapController;
        if (iMapController != null) {
            iMapController.stopAnimation(false);
        }
        this.mIsFollowing = false;
    }

    public boolean isFollowLocationEnabled() {
        return this.mIsFollowing;
    }

    public void onLocationChanged(final Location location, IMyLocationProvider iMyLocationProvider) {
        Handler handler;
        if (location != null && (handler = this.mHandler) != null) {
            handler.postAtTime(new Runnable() {
                public void run() {
                    MyLocationNewOverlay.this.setLocation(location);
                    Iterator it = MyLocationNewOverlay.this.mRunOnFirstFix.iterator();
                    while (it.hasNext()) {
                        Thread thread = new Thread((Runnable) it.next());
                        thread.setName(getClass().getName() + "#onLocationChanged");
                        thread.start();
                    }
                    MyLocationNewOverlay.this.mRunOnFirstFix.clear();
                }
            }, this.mHandlerToken, 0);
        }
    }

    /* access modifiers changed from: protected */
    public void setLocation(Location location) {
        this.mLocation = location;
        this.mGeoPoint.setCoords(location.getLatitude(), this.mLocation.getLongitude());
        if (this.mIsFollowing) {
            this.mMapController.animateTo(this.mGeoPoint);
            return;
        }
        MapView mapView = this.mMapView;
        if (mapView != null) {
            mapView.postInvalidate();
        }
    }

    public boolean enableMyLocation(IMyLocationProvider iMyLocationProvider) {
        Location lastKnownLocation;
        setMyLocationProvider(iMyLocationProvider);
        boolean startLocationProvider = this.mMyLocationProvider.startLocationProvider(this);
        this.mIsLocationEnabled = startLocationProvider;
        if (startLocationProvider && (lastKnownLocation = this.mMyLocationProvider.getLastKnownLocation()) != null) {
            setLocation(lastKnownLocation);
        }
        MapView mapView = this.mMapView;
        if (mapView != null) {
            mapView.postInvalidate();
        }
        return startLocationProvider;
    }

    public boolean enableMyLocation() {
        return enableMyLocation(this.mMyLocationProvider);
    }

    public void disableMyLocation() {
        this.mIsLocationEnabled = false;
        stopLocationProvider();
        MapView mapView = this.mMapView;
        if (mapView != null) {
            mapView.postInvalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void stopLocationProvider() {
        Object obj;
        IMyLocationProvider iMyLocationProvider = this.mMyLocationProvider;
        if (iMyLocationProvider != null) {
            iMyLocationProvider.stopLocationProvider();
        }
        Handler handler = this.mHandler;
        if (handler != null && (obj = this.mHandlerToken) != null) {
            handler.removeCallbacksAndMessages(obj);
        }
    }

    public boolean isMyLocationEnabled() {
        return this.mIsLocationEnabled;
    }

    public boolean runOnFirstFix(Runnable runnable) {
        if (this.mMyLocationProvider == null || this.mLocation == null) {
            this.mRunOnFirstFix.addLast(runnable);
            return false;
        }
        Thread thread = new Thread(runnable);
        thread.setName(getClass().getName() + "#runOnFirstFix");
        thread.start();
        return true;
    }

    public void setPersonIcon(Bitmap bitmap) {
        this.mPersonBitmap = bitmap;
    }

    public void setPersonAnchor(float f, float f2) {
        this.mPersonHotspot.set(((float) this.mPersonBitmap.getWidth()) * f, ((float) this.mPersonBitmap.getHeight()) * f2);
    }

    public void setDirectionAnchor(float f, float f2) {
        this.mDirectionArrowCenterX = ((float) this.mDirectionArrowBitmap.getWidth()) * f;
        this.mDirectionArrowCenterY = ((float) this.mDirectionArrowBitmap.getHeight()) * f2;
    }
}
