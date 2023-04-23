package org.osmdroid.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import java.util.Iterator;
import java.util.LinkedList;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.MyMath;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;

public class MapController implements IMapController, MapView.OnFirstLayoutListener {
    private Animator mCurrentAnimator;
    protected final MapView mMapView;
    private ReplayController mReplayController;
    private double mTargetZoomLevel = 0.0d;
    private ScaleAnimation mZoomInAnimationOld;
    private ScaleAnimation mZoomOutAnimationOld;

    private enum ReplayType {
        ZoomToSpanPoint,
        AnimateToPoint,
        AnimateToGeoPoint,
        SetCenterPoint
    }

    public MapController(MapView mapView) {
        MapView mapView2 = mapView;
        this.mMapView = mapView2;
        this.mReplayController = new ReplayController(this, (C13781) null);
        if (!mapView.isLayoutOccurred()) {
            mapView2.addOnFirstLayoutListener(this);
        }
        if (Build.VERSION.SDK_INT < 11) {
            ZoomAnimationListener zoomAnimationListener = new ZoomAnimationListener(this);
            this.mZoomInAnimationOld = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, 1, 0.5f, 1, 0.5f);
            this.mZoomOutAnimationOld = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, 1, 0.5f, 1, 0.5f);
            this.mZoomInAnimationOld.setDuration((long) Configuration.getInstance().getAnimationSpeedShort());
            this.mZoomOutAnimationOld.setDuration((long) Configuration.getInstance().getAnimationSpeedShort());
            this.mZoomInAnimationOld.setAnimationListener(zoomAnimationListener);
            this.mZoomOutAnimationOld.setAnimationListener(zoomAnimationListener);
        }
    }

    public void onFirstLayout(View view, int i, int i2, int i3, int i4) {
        this.mReplayController.replayCalls();
    }

    public void zoomToSpan(double d, double d2) {
        if (d > 0.0d && d2 > 0.0d) {
            if (!this.mMapView.isLayoutOccurred()) {
                this.mReplayController.zoomToSpan(d, d2);
                return;
            }
            BoundingBox boundingBox = this.mMapView.getProjection().getBoundingBox();
            double zoomLevel = this.mMapView.getProjection().getZoomLevel();
            double max = Math.max(d / boundingBox.getLatitudeSpan(), d2 / boundingBox.getLongitudeSpan());
            if (max > 1.0d) {
                this.mMapView.setZoomLevel(zoomLevel - ((double) MyMath.getNextSquareNumberAbove((float) max)));
            } else if (max < 0.5d) {
                this.mMapView.setZoomLevel((zoomLevel + ((double) MyMath.getNextSquareNumberAbove(1.0f / ((float) max)))) - 1.0d);
            }
        }
    }

    public void zoomToSpan(int i, int i2) {
        zoomToSpan(((double) i) * 1.0E-6d, ((double) i2) * 1.0E-6d);
    }

    public void animateTo(IGeoPoint iGeoPoint) {
        animateTo(iGeoPoint, (Double) null, (Long) null);
    }

    public void animateTo(IGeoPoint iGeoPoint, Double d, Long l, Float f) {
        animateTo(iGeoPoint, d, l, f, (Boolean) null);
    }

    public void animateTo(IGeoPoint iGeoPoint, Double d, Long l, Float f, Boolean bool) {
        if (!this.mMapView.isLayoutOccurred()) {
            this.mReplayController.animateTo(iGeoPoint, d, l, f, bool);
        } else if (Build.VERSION.SDK_INT >= 11) {
            Double d2 = d;
            MapAnimatorListener mapAnimatorListener = new MapAnimatorListener(this, Double.valueOf(this.mMapView.getZoomLevelDouble()), d2, new GeoPoint(this.mMapView.getProjection().getCurrentCenter()), iGeoPoint, Float.valueOf(this.mMapView.getMapOrientation()), f, bool);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            ofFloat.addListener(mapAnimatorListener);
            ofFloat.addUpdateListener(mapAnimatorListener);
            if (l == null) {
                ofFloat.setDuration((long) Configuration.getInstance().getAnimationSpeedDefault());
            } else {
                ofFloat.setDuration(l.longValue());
            }
            Animator animator = this.mCurrentAnimator;
            if (animator != null) {
                mapAnimatorListener.onAnimationCancel(animator);
            }
            this.mCurrentAnimator = ofFloat;
            ofFloat.start();
        } else {
            IGeoPoint iGeoPoint2 = iGeoPoint;
            Point pixels = this.mMapView.getProjection().toPixels(iGeoPoint, (Point) null);
            animateTo(pixels.x, pixels.y);
        }
    }

    public void animateTo(IGeoPoint iGeoPoint, Double d, Long l) {
        animateTo(iGeoPoint, d, l, (Float) null);
    }

    public void animateTo(int i, int i2) {
        if (!this.mMapView.isLayoutOccurred()) {
            this.mReplayController.animateTo(i, i2);
        } else if (!this.mMapView.isAnimating()) {
            this.mMapView.mIsFlinging = false;
            int mapScrollX = (int) this.mMapView.getMapScrollX();
            int mapScrollY = (int) this.mMapView.getMapScrollY();
            int width = i - (this.mMapView.getWidth() / 2);
            int height = i2 - (this.mMapView.getHeight() / 2);
            if (width != mapScrollX || height != mapScrollY) {
                this.mMapView.getScroller().startScroll(mapScrollX, mapScrollY, width, height, Configuration.getInstance().getAnimationSpeedDefault());
                this.mMapView.postInvalidate();
            }
        }
    }

    public void scrollBy(int i, int i2) {
        this.mMapView.scrollBy(i, i2);
    }

    public void setCenter(IGeoPoint iGeoPoint) {
        if (!this.mMapView.isLayoutOccurred()) {
            this.mReplayController.setCenter(iGeoPoint);
        } else {
            this.mMapView.setExpectedCenter(iGeoPoint);
        }
    }

    public void stopPanning() {
        this.mMapView.mIsFlinging = false;
        this.mMapView.getScroller().forceFinished(true);
    }

    public void stopAnimation(boolean z) {
        if (!this.mMapView.getScroller().isFinished()) {
            if (z) {
                this.mMapView.mIsFlinging = false;
                this.mMapView.getScroller().abortAnimation();
            } else {
                stopPanning();
            }
        }
        if (Build.VERSION.SDK_INT >= 11) {
            Animator animator = this.mCurrentAnimator;
            if (!this.mMapView.mIsAnimating.get()) {
                return;
            }
            if (z) {
                animator.end();
            } else {
                animator.cancel();
            }
        } else if (this.mMapView.mIsAnimating.get()) {
            this.mMapView.clearAnimation();
        }
    }

    public int setZoom(int i) {
        return (int) setZoom((double) i);
    }

    public double setZoom(double d) {
        return this.mMapView.setZoomLevel(d);
    }

    public boolean zoomIn() {
        return zoomIn((Long) null);
    }

    public boolean zoomIn(Long l) {
        return zoomTo(this.mMapView.getZoomLevelDouble() + 1.0d, l);
    }

    public boolean zoomInFixing(int i, int i2, Long l) {
        return zoomToFixing(this.mMapView.getZoomLevelDouble() + 1.0d, i, i2, l);
    }

    public boolean zoomInFixing(int i, int i2) {
        return zoomInFixing(i, i2, (Long) null);
    }

    public boolean zoomOut(Long l) {
        return zoomTo(this.mMapView.getZoomLevelDouble() - 1.0d, l);
    }

    public boolean zoomOut() {
        return zoomOut((Long) null);
    }

    @Deprecated
    public boolean zoomOutFixing(int i, int i2) {
        return zoomToFixing(this.mMapView.getZoomLevelDouble() - 1.0d, i, i2, (Long) null);
    }

    public boolean zoomTo(int i) {
        return zoomTo(i, (Long) null);
    }

    public boolean zoomTo(int i, Long l) {
        return zoomTo((double) i, l);
    }

    public boolean zoomToFixing(int i, int i2, int i3, Long l) {
        return zoomToFixing((double) i, i2, i3, l);
    }

    public boolean zoomTo(double d, Long l) {
        return zoomToFixing(d, this.mMapView.getWidth() / 2, this.mMapView.getHeight() / 2, l);
    }

    public boolean zoomTo(double d) {
        return zoomTo(d, (Long) null);
    }

    public boolean zoomToFixing(double d, int i, int i2, Long l) {
        double maxZoomLevel = d > this.mMapView.getMaxZoomLevel() ? this.mMapView.getMaxZoomLevel() : d;
        if (maxZoomLevel < this.mMapView.getMinZoomLevel()) {
            maxZoomLevel = this.mMapView.getMinZoomLevel();
        }
        double zoomLevelDouble = this.mMapView.getZoomLevelDouble();
        if (!((maxZoomLevel < zoomLevelDouble && this.mMapView.canZoomOut()) || (maxZoomLevel > zoomLevelDouble && this.mMapView.canZoomIn())) || this.mMapView.mIsAnimating.getAndSet(true)) {
            return false;
        }
        ZoomEvent zoomEvent = null;
        for (MapListener next : this.mMapView.mListners) {
            if (zoomEvent == null) {
                zoomEvent = new ZoomEvent(this.mMapView, maxZoomLevel);
            }
            next.onZoom(zoomEvent);
        }
        this.mMapView.setMultiTouchScaleInitPoint((float) i, (float) i2);
        this.mMapView.startAnimation();
        float pow = (float) Math.pow(2.0d, maxZoomLevel - zoomLevelDouble);
        if (Build.VERSION.SDK_INT >= 11) {
            MapAnimatorListener mapAnimatorListener = new MapAnimatorListener(this, Double.valueOf(zoomLevelDouble), Double.valueOf(maxZoomLevel), (IGeoPoint) null, (IGeoPoint) null, (Float) null, (Float) null, (Boolean) null);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            ofFloat.addListener(mapAnimatorListener);
            ofFloat.addUpdateListener(mapAnimatorListener);
            if (l == null) {
                ofFloat.setDuration((long) Configuration.getInstance().getAnimationSpeedShort());
            } else {
                ofFloat.setDuration(l.longValue());
            }
            this.mCurrentAnimator = ofFloat;
            ofFloat.start();
            return true;
        }
        this.mTargetZoomLevel = maxZoomLevel;
        if (maxZoomLevel > zoomLevelDouble) {
            this.mMapView.startAnimation(this.mZoomInAnimationOld);
        } else {
            this.mMapView.startAnimation(this.mZoomOutAnimationOld);
        }
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, pow, 1.0f, pow, 1, 0.5f, 1, 0.5f);
        if (l == null) {
            scaleAnimation.setDuration((long) Configuration.getInstance().getAnimationSpeedShort());
        } else {
            scaleAnimation.setDuration(l.longValue());
        }
        scaleAnimation.setAnimationListener(new ZoomAnimationListener(this));
        return true;
    }

    public boolean zoomToFixing(double d, int i, int i2) {
        return zoomToFixing(d, i, i2, (Long) null);
    }

    public boolean zoomToFixing(int i, int i2, int i3) {
        return zoomToFixing(i, i2, i3, (Long) null);
    }

    /* access modifiers changed from: protected */
    public void onAnimationStart() {
        this.mMapView.mIsAnimating.set(true);
    }

    /* access modifiers changed from: protected */
    public void onAnimationEnd() {
        this.mMapView.mIsAnimating.set(false);
        this.mMapView.resetMultiTouchScale();
        if (Build.VERSION.SDK_INT >= 11) {
            this.mCurrentAnimator = null;
        } else {
            this.mMapView.clearAnimation();
            this.mZoomInAnimationOld.reset();
            this.mZoomOutAnimationOld.reset();
            setZoom(this.mTargetZoomLevel);
        }
        this.mMapView.invalidate();
    }

    private static class MapAnimatorListener implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
        private final GeoPoint mCenter = new GeoPoint(0.0d, 0.0d);
        private final IGeoPoint mCenterEnd;
        private final IGeoPoint mCenterStart;
        private final MapController mMapController;
        private final Float mOrientationSpan;
        private final Float mOrientationStart;
        private final Double mZoomEnd;
        private final Double mZoomStart;

        public void onAnimationRepeat(Animator animator) {
        }

        public MapAnimatorListener(MapController mapController, Double d, Double d2, IGeoPoint iGeoPoint, IGeoPoint iGeoPoint2, Float f, Float f2, Boolean bool) {
            this.mMapController = mapController;
            this.mZoomStart = d;
            this.mZoomEnd = d2;
            this.mCenterStart = iGeoPoint;
            this.mCenterEnd = iGeoPoint2;
            if (f2 == null) {
                this.mOrientationStart = null;
                this.mOrientationSpan = null;
                return;
            }
            this.mOrientationStart = f;
            this.mOrientationSpan = Float.valueOf((float) MyMath.getAngleDifference((double) f.floatValue(), (double) f2.floatValue(), bool));
        }

        public void onAnimationStart(Animator animator) {
            this.mMapController.onAnimationStart();
        }

        public void onAnimationEnd(Animator animator) {
            this.mMapController.onAnimationEnd();
        }

        public void onAnimationCancel(Animator animator) {
            this.mMapController.onAnimationEnd();
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (this.mZoomEnd != null) {
                this.mMapController.mMapView.setZoomLevel(this.mZoomStart.doubleValue() + ((this.mZoomEnd.doubleValue() - this.mZoomStart.doubleValue()) * ((double) floatValue)));
            }
            if (this.mOrientationSpan != null) {
                this.mMapController.mMapView.setMapOrientation(this.mOrientationStart.floatValue() + (this.mOrientationSpan.floatValue() * floatValue));
            }
            if (this.mCenterEnd != null) {
                MapView mapView = this.mMapController.mMapView;
                TileSystem tileSystem = MapView.getTileSystem();
                double cleanLongitude = tileSystem.cleanLongitude(this.mCenterStart.getLongitude());
                double d = (double) floatValue;
                double cleanLongitude2 = tileSystem.cleanLongitude(cleanLongitude + ((tileSystem.cleanLongitude(this.mCenterEnd.getLongitude()) - cleanLongitude) * d));
                double cleanLatitude = tileSystem.cleanLatitude(this.mCenterStart.getLatitude());
                this.mCenter.setCoords(tileSystem.cleanLatitude(cleanLatitude + ((tileSystem.cleanLatitude(this.mCenterEnd.getLatitude()) - cleanLatitude) * d)), cleanLongitude2);
                this.mMapController.mMapView.setExpectedCenter(this.mCenter);
            }
            this.mMapController.mMapView.invalidate();
        }
    }

    protected static class ZoomAnimationListener implements Animation.AnimationListener {
        private MapController mMapController;

        public void onAnimationRepeat(Animation animation) {
        }

        public ZoomAnimationListener(MapController mapController) {
            this.mMapController = mapController;
        }

        public void onAnimationStart(Animation animation) {
            this.mMapController.onAnimationStart();
        }

        public void onAnimationEnd(Animation animation) {
            this.mMapController.onAnimationEnd();
        }
    }

    private class ReplayController {
        private LinkedList<ReplayClass> mReplayList;

        private ReplayController() {
            this.mReplayList = new LinkedList<>();
        }

        /* synthetic */ ReplayController(MapController mapController, C13781 r2) {
            this();
        }

        public void animateTo(IGeoPoint iGeoPoint, Double d, Long l, Float f, Boolean bool) {
            this.mReplayList.add(new ReplayClass(ReplayType.AnimateToGeoPoint, (Point) null, iGeoPoint, d, l, f, bool));
        }

        public void animateTo(int i, int i2) {
            this.mReplayList.add(new ReplayClass(this, ReplayType.AnimateToPoint, new Point(i, i2), (IGeoPoint) null));
        }

        public void setCenter(IGeoPoint iGeoPoint) {
            this.mReplayList.add(new ReplayClass(this, ReplayType.SetCenterPoint, (Point) null, iGeoPoint));
        }

        public void zoomToSpan(int i, int i2) {
            this.mReplayList.add(new ReplayClass(this, ReplayType.ZoomToSpanPoint, new Point(i, i2), (IGeoPoint) null));
        }

        public void zoomToSpan(double d, double d2) {
            this.mReplayList.add(new ReplayClass(this, ReplayType.ZoomToSpanPoint, new Point((int) (d * 1000000.0d), (int) (d2 * 1000000.0d)), (IGeoPoint) null));
        }

        public void replayCalls() {
            Iterator it = this.mReplayList.iterator();
            while (it.hasNext()) {
                ReplayClass replayClass = (ReplayClass) it.next();
                int i = C13781.$SwitchMap$org$osmdroid$views$MapController$ReplayType[replayClass.mReplayType.ordinal()];
                if (i != 1) {
                    if (i != 2) {
                        if (i != 3) {
                            if (i == 4 && replayClass.mPoint != null) {
                                MapController.this.zoomToSpan(replayClass.mPoint.x, replayClass.mPoint.y);
                            }
                        } else if (replayClass.mGeoPoint != null) {
                            MapController.this.setCenter(replayClass.mGeoPoint);
                        }
                    } else if (replayClass.mPoint != null) {
                        MapController.this.animateTo(replayClass.mPoint.x, replayClass.mPoint.y);
                    }
                } else if (replayClass.mGeoPoint != null) {
                    MapController.this.animateTo(replayClass.mGeoPoint, replayClass.mZoom, replayClass.mSpeed, replayClass.mOrientation, replayClass.mClockwise);
                }
            }
            this.mReplayList.clear();
        }

        private class ReplayClass {
            /* access modifiers changed from: private */
            public final Boolean mClockwise;
            /* access modifiers changed from: private */
            public IGeoPoint mGeoPoint;
            /* access modifiers changed from: private */
            public final Float mOrientation;
            /* access modifiers changed from: private */
            public Point mPoint;
            /* access modifiers changed from: private */
            public ReplayType mReplayType;
            /* access modifiers changed from: private */
            public final Long mSpeed;
            /* access modifiers changed from: private */
            public final Double mZoom;

            public ReplayClass(ReplayController replayController, ReplayType replayType, Point point, IGeoPoint iGeoPoint) {
                this(replayType, point, iGeoPoint, (Double) null, (Long) null, (Float) null, (Boolean) null);
            }

            public ReplayClass(ReplayType replayType, Point point, IGeoPoint iGeoPoint, Double d, Long l, Float f, Boolean bool) {
                this.mReplayType = replayType;
                this.mPoint = point;
                this.mGeoPoint = iGeoPoint;
                this.mSpeed = l;
                this.mZoom = d;
                this.mOrientation = f;
                this.mClockwise = bool;
            }
        }
    }

    /* renamed from: org.osmdroid.views.MapController$1 */
    static /* synthetic */ class C13781 {
        static final /* synthetic */ int[] $SwitchMap$org$osmdroid$views$MapController$ReplayType;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                org.osmdroid.views.MapController$ReplayType[] r0 = org.osmdroid.views.MapController.ReplayType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$org$osmdroid$views$MapController$ReplayType = r0
                org.osmdroid.views.MapController$ReplayType r1 = org.osmdroid.views.MapController.ReplayType.AnimateToGeoPoint     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$org$osmdroid$views$MapController$ReplayType     // Catch:{ NoSuchFieldError -> 0x001d }
                org.osmdroid.views.MapController$ReplayType r1 = org.osmdroid.views.MapController.ReplayType.AnimateToPoint     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$org$osmdroid$views$MapController$ReplayType     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.osmdroid.views.MapController$ReplayType r1 = org.osmdroid.views.MapController.ReplayType.SetCenterPoint     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$org$osmdroid$views$MapController$ReplayType     // Catch:{ NoSuchFieldError -> 0x0033 }
                org.osmdroid.views.MapController$ReplayType r1 = org.osmdroid.views.MapController.ReplayType.ZoomToSpanPoint     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.MapController.C13781.<clinit>():void");
        }
    }
}
