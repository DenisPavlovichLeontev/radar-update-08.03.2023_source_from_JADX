package org.osmdroid.views.overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.Distance;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.LineBuilder;
import org.osmdroid.util.PointL;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.milestones.MilestoneManager;

public abstract class PolyOverlayWithIW extends OverlayWithIW {
    private final boolean mClosePath;
    protected float mDensity = 1.0f;
    private float mDensityMultiplier = 1.0f;
    private final Point mDowngradeBottomRight = new Point();
    private final PointL mDowngradeCenter = new PointL();
    private boolean mDowngradeDisplay;
    private int mDowngradeMaximumPixelSize;
    private int mDowngradeMaximumRectanglePixelSize;
    private final PointL mDowngradeOffset = new PointL();
    private float[] mDowngradeSegments;
    private final Point mDowngradeTopLeft = new Point();
    protected Paint mFillPaint;
    protected List<LinearRing> mHoles = new ArrayList();
    private GeoPoint mInfoWindowLocation;
    private boolean mIsPaintOrPaintList = true;
    private LineDrawer mLineDrawer;
    private List<MilestoneManager> mMilestoneManagers = new ArrayList();
    protected LinearRing mOutline;
    protected Paint mOutlinePaint = new Paint();
    private final List<PaintList> mOutlinePaintLists = new ArrayList();
    protected Path mPath;
    private final PointL mVisibilityProjectedCenter = new PointL();
    private final PointL mVisibilityProjectedCorner = new PointL();
    private final PointL mVisibilityRectangleCenter = new PointL();
    private final PointL mVisibilityRectangleCorner = new PointL();

    /* access modifiers changed from: protected */
    public abstract boolean click(MapView mapView, GeoPoint geoPoint);

    protected PolyOverlayWithIW(MapView mapView, boolean z, boolean z2) {
        this.mClosePath = z2;
        if (mapView != null) {
            setInfoWindow(mapView.getRepository().getDefaultPolylineInfoWindow());
            this.mDensity = mapView.getContext().getResources().getDisplayMetrics().density;
        }
        usePath(z);
    }

    public void usePath(boolean z) {
        LinearRing linearRing = this.mOutline;
        ArrayList<GeoPoint> points = linearRing == null ? null : linearRing.getPoints();
        if (z) {
            Path path = new Path();
            this.mPath = path;
            this.mLineDrawer = null;
            this.mOutline = new LinearRing(path, this.mClosePath);
        } else {
            this.mPath = null;
            LineDrawer lineDrawer = new LineDrawer(256);
            this.mLineDrawer = lineDrawer;
            this.mOutline = new LinearRing((LineBuilder) lineDrawer, this.mClosePath);
            this.mLineDrawer.setPaint(this.mOutlinePaint);
        }
        if (points != null) {
            setPoints(points);
        }
    }

    public void setVisible(boolean z) {
        setEnabled(z);
    }

    public boolean isVisible() {
        return isEnabled();
    }

    public Paint getOutlinePaint() {
        this.mIsPaintOrPaintList = true;
        return this.mOutlinePaint;
    }

    public List<PaintList> getOutlinePaintLists() {
        this.mIsPaintOrPaintList = false;
        return this.mOutlinePaintLists;
    }

    /* access modifiers changed from: protected */
    public Paint getFillPaint() {
        return this.mFillPaint;
    }

    public void setGeodesic(boolean z) {
        this.mOutline.setGeodesic(z);
    }

    public boolean isGeodesic() {
        return this.mOutline.isGeodesic();
    }

    public void setInfoWindow(InfoWindow infoWindow) {
        if (this.mInfoWindow != null && this.mInfoWindow.getRelatedObject() == this) {
            this.mInfoWindow.setRelatedObject((Object) null);
        }
        this.mInfoWindow = infoWindow;
    }

    public void showInfoWindow() {
        if (this.mInfoWindow != null && this.mInfoWindowLocation != null) {
            this.mInfoWindow.open(this, this.mInfoWindowLocation, 0, 0);
        }
    }

    public void setInfoWindowLocation(GeoPoint geoPoint) {
        this.mInfoWindowLocation = geoPoint;
    }

    public GeoPoint getInfoWindowLocation() {
        return this.mInfoWindowLocation;
    }

    public void setMilestoneManagers(List<MilestoneManager> list) {
        if (list != null) {
            this.mMilestoneManagers = list;
        } else if (this.mMilestoneManagers.size() > 0) {
            this.mMilestoneManagers.clear();
        }
    }

    public double getDistance() {
        return this.mOutline.getDistance();
    }

    /* access modifiers changed from: protected */
    public void setDefaultInfoWindowLocation() {
        if (this.mOutline.getPoints().size() == 0) {
            this.mInfoWindowLocation = new GeoPoint(0.0d, 0.0d);
            return;
        }
        if (this.mInfoWindowLocation == null) {
            this.mInfoWindowLocation = new GeoPoint(0.0d, 0.0d);
        }
        this.mOutline.getCenter(this.mInfoWindowLocation);
    }

    public void draw(Canvas canvas, Projection projection) {
        if (isVisible(projection)) {
            if (this.mDowngradeMaximumPixelSize <= 0 || isWorthDisplaying(projection)) {
                if (this.mPath != null) {
                    drawWithPath(canvas, projection);
                } else {
                    drawWithLines(canvas, projection);
                }
            } else if (this.mDowngradeDisplay) {
                displayDowngrade(canvas, projection);
            }
        }
    }

    private boolean isVisible(Projection projection) {
        BoundingBox bounds = getBounds();
        projection.toProjectedPixels(bounds.getCenterLatitude(), bounds.getCenterLongitude(), this.mVisibilityProjectedCenter);
        projection.toProjectedPixels(bounds.getLatNorth(), bounds.getLonEast(), this.mVisibilityProjectedCorner);
        projection.getLongPixelsFromProjected(this.mVisibilityProjectedCenter, projection.getProjectedPowerDifference(), true, this.mVisibilityRectangleCenter);
        projection.getLongPixelsFromProjected(this.mVisibilityProjectedCorner, projection.getProjectedPowerDifference(), true, this.mVisibilityRectangleCorner);
        double sqrt = Math.sqrt(Distance.getSquaredDistanceToPoint((double) this.mVisibilityRectangleCenter.f559x, (double) this.mVisibilityRectangleCenter.f560y, (double) this.mVisibilityRectangleCorner.f559x, (double) this.mVisibilityRectangleCorner.f560y));
        double d = (double) this.mVisibilityRectangleCenter.f559x;
        double d2 = (double) this.mVisibilityRectangleCenter.f560y;
        double width = (double) (projection.getWidth() / 2);
        double height = (double) (projection.getHeight() / 2);
        return Math.sqrt(Distance.getSquaredDistanceToPoint(d, d2, width, height)) <= sqrt + Math.sqrt(Distance.getSquaredDistanceToPoint(0.0d, 0.0d, width, height));
    }

    private void drawWithPath(Canvas canvas, Projection projection) {
        this.mPath.rewind();
        this.mOutline.setClipArea(projection);
        PointL buildPathPortion = this.mOutline.buildPathPortion(projection, (PointL) null, this.mMilestoneManagers.size() > 0);
        for (MilestoneManager next : this.mMilestoneManagers) {
            next.init();
            next.setDistances(this.mOutline.getDistances());
            Iterator<PointL> it = this.mOutline.getPointsForMilestones().iterator();
            while (it.hasNext()) {
                PointL next2 = it.next();
                next.add(next2.f559x, next2.f560y);
            }
            next.end();
        }
        List<LinearRing> list = this.mHoles;
        if (list != null) {
            for (LinearRing next3 : list) {
                next3.setClipArea(projection);
                next3.buildPathPortion(projection, buildPathPortion, this.mMilestoneManagers.size() > 0);
            }
            this.mPath.setFillType(Path.FillType.EVEN_ODD);
        }
        if (isVisible(this.mFillPaint)) {
            canvas.drawPath(this.mPath, this.mFillPaint);
        }
        if (isVisible(this.mOutlinePaint)) {
            canvas.drawPath(this.mPath, this.mOutlinePaint);
        }
        for (MilestoneManager draw : this.mMilestoneManagers) {
            draw.draw(canvas);
        }
        if (isInfoWindowOpen() && this.mInfoWindow != null && this.mInfoWindow.getRelatedObject() == this) {
            this.mInfoWindow.draw();
        }
    }

    private void drawWithLines(Canvas canvas, Projection projection) {
        this.mLineDrawer.setCanvas(canvas);
        this.mOutline.setClipArea(projection);
        boolean z = this.mMilestoneManagers.size() > 0;
        if (this.mIsPaintOrPaintList) {
            this.mLineDrawer.setPaint(getOutlinePaint());
            this.mOutline.buildLinePortion(projection, z);
        } else {
            for (PaintList paint : getOutlinePaintLists()) {
                this.mLineDrawer.setPaint(paint);
                this.mOutline.buildLinePortion(projection, z);
                z = false;
            }
        }
        for (MilestoneManager next : this.mMilestoneManagers) {
            next.init();
            next.setDistances(this.mOutline.getDistances());
            Iterator<PointL> it = this.mOutline.getPointsForMilestones().iterator();
            while (it.hasNext()) {
                PointL next2 = it.next();
                next.add(next2.f559x, next2.f560y);
            }
            next.end();
        }
        for (MilestoneManager draw : this.mMilestoneManagers) {
            draw.draw(canvas);
        }
        if (isInfoWindowOpen() && this.mInfoWindow != null && this.mInfoWindow.getRelatedObject() == this) {
            this.mInfoWindow.draw();
        }
    }

    public void onDetach(MapView mapView) {
        LinearRing linearRing = this.mOutline;
        if (linearRing != null) {
            linearRing.clear();
            this.mOutline = null;
        }
        this.mHoles.clear();
        this.mMilestoneManagers.clear();
        onDestroy();
    }

    public BoundingBox getBounds() {
        return this.mOutline.getBoundingBox();
    }

    public void setPoints(List<GeoPoint> list) {
        this.mOutline.setPoints(list);
        setDefaultInfoWindowLocation();
    }

    public void addPoint(GeoPoint geoPoint) {
        this.mOutline.addPoint(geoPoint);
    }

    public List<GeoPoint> getActualPoints() {
        return this.mOutline.getPoints();
    }

    public void setDowngradeDisplay(boolean z) {
        this.mDowngradeDisplay = z;
    }

    public void setDowngradePixelSizes(int i, int i2) {
        this.mDowngradeMaximumRectanglePixelSize = i2;
        this.mDowngradeMaximumPixelSize = Math.max(i, i2);
    }

    private boolean isWorthDisplaying(Projection projection) {
        BoundingBox bounds = getBounds();
        projection.toPixels(new GeoPoint(bounds.getLatNorth(), bounds.getLonEast()), this.mDowngradeTopLeft);
        projection.toPixels(new GeoPoint(bounds.getLatSouth(), bounds.getLonWest()), this.mDowngradeBottomRight);
        double worldMapSize = projection.getWorldMapSize();
        long round = Math.round(LinearRing.getCloserValue((double) this.mDowngradeTopLeft.x, (double) this.mDowngradeBottomRight.x, worldMapSize));
        long round2 = Math.round(LinearRing.getCloserValue((double) this.mDowngradeTopLeft.y, (double) this.mDowngradeBottomRight.y, worldMapSize));
        if (Math.abs(this.mDowngradeTopLeft.x - this.mDowngradeBottomRight.x) >= this.mDowngradeMaximumPixelSize && Math.abs(((long) this.mDowngradeTopLeft.x) - round) >= ((long) this.mDowngradeMaximumPixelSize) && Math.abs(this.mDowngradeTopLeft.y - this.mDowngradeBottomRight.y) >= this.mDowngradeMaximumPixelSize && Math.abs(((long) this.mDowngradeTopLeft.y) - round2) >= ((long) this.mDowngradeMaximumPixelSize)) {
            return true;
        }
        return false;
    }

    private boolean isVisible(Paint paint) {
        return (paint == null || paint.getColor() == 0) ? false : true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x00dc A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00dd  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void displayDowngrade(android.graphics.Canvas r23, org.osmdroid.views.Projection r24) {
        /*
            r22 = this;
            r0 = r22
            r1 = r24
            org.osmdroid.views.overlay.LinearRing r2 = r0.mOutline
            org.osmdroid.util.BoundingBox r2 = r2.getBoundingBox()
            org.osmdroid.util.GeoPoint r3 = new org.osmdroid.util.GeoPoint
            double r4 = r2.getLatNorth()
            double r6 = r2.getLonEast()
            r3.<init>((double) r4, (double) r6)
            android.graphics.Point r4 = r0.mDowngradeTopLeft
            r1.toPixels(r3, r4)
            org.osmdroid.util.GeoPoint r3 = new org.osmdroid.util.GeoPoint
            double r4 = r2.getLatSouth()
            double r6 = r2.getLonWest()
            r3.<init>((double) r4, (double) r6)
            android.graphics.Point r2 = r0.mDowngradeBottomRight
            r1.toPixels(r3, r2)
            double r2 = r24.getWorldMapSize()
            android.graphics.Point r4 = r0.mDowngradeTopLeft
            int r4 = r4.x
            long r10 = (long) r4
            android.graphics.Point r4 = r0.mDowngradeTopLeft
            int r4 = r4.y
            long r12 = (long) r4
            double r4 = (double) r10
            android.graphics.Point r6 = r0.mDowngradeBottomRight
            int r6 = r6.x
            double r6 = (double) r6
            r8 = r2
            double r4 = org.osmdroid.views.overlay.LinearRing.getCloserValue(r4, r6, r8)
            long r14 = java.lang.Math.round(r4)
            double r4 = (double) r12
            android.graphics.Point r6 = r0.mDowngradeBottomRight
            int r6 = r6.y
            double r6 = (double) r6
            double r2 = org.osmdroid.views.overlay.LinearRing.getCloserValue(r4, r6, r8)
            long r2 = java.lang.Math.round(r2)
            int r4 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
            r5 = 1
            if (r4 != 0) goto L_0x0061
            r7 = r5
            goto L_0x0069
        L_0x0061:
            if (r4 <= 0) goto L_0x0067
            long r10 = r10 - r14
            r7 = r10
            r10 = r14
            goto L_0x0069
        L_0x0067:
            long r7 = r14 - r10
        L_0x0069:
            int r4 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
            if (r4 != 0) goto L_0x006e
            goto L_0x0076
        L_0x006e:
            if (r4 <= 0) goto L_0x0074
            long r5 = r12 - r2
            r12 = r2
            goto L_0x0076
        L_0x0074:
            long r5 = r2 - r12
        L_0x0076:
            org.osmdroid.util.PointL r2 = r0.mDowngradeCenter
            r3 = 2
            long r14 = r7 / r3
            long r14 = r14 + r10
            long r3 = r5 / r3
            long r3 = r3 + r12
            r2.set(r14, r3)
            org.osmdroid.views.overlay.LinearRing r2 = r0.mOutline
            org.osmdroid.util.PointL r3 = r0.mDowngradeOffset
            org.osmdroid.util.PointL r4 = r0.mDowngradeCenter
            r2.getBestOffset(r1, r3, r4)
            org.osmdroid.util.PointL r1 = r0.mDowngradeOffset
            long r1 = r1.f559x
            long r10 = r10 + r1
            org.osmdroid.util.PointL r1 = r0.mDowngradeOffset
            long r1 = r1.f560y
            long r12 = r12 + r1
            r1 = 0
            boolean r2 = r0.mIsPaintOrPaintList
            r3 = 0
            if (r2 == 0) goto L_0x00a1
            android.graphics.Paint r1 = r22.getOutlinePaint()
            goto L_0x00d4
        L_0x00a1:
            java.util.List r2 = r22.getOutlinePaintLists()
            int r2 = r2.size()
            if (r2 <= 0) goto L_0x00d4
            java.util.List r1 = r22.getOutlinePaintLists()
            java.lang.Object r1 = r1.get(r3)
            r14 = r1
            org.osmdroid.views.overlay.PaintList r14 = (org.osmdroid.views.overlay.PaintList) r14
            android.graphics.Paint r1 = r14.getPaint()
            if (r1 != 0) goto L_0x00d4
            r15 = 0
            float r1 = (float) r10
            float r2 = (float) r12
            long r3 = r10 + r7
            float r3 = (float) r3
            r20 = r10
            long r9 = r12 + r5
            float r4 = (float) r9
            r16 = r1
            r17 = r2
            r18 = r3
            r19 = r4
            android.graphics.Paint r1 = r14.getPaint(r15, r16, r17, r18, r19)
            goto L_0x00d6
        L_0x00d4:
            r20 = r10
        L_0x00d6:
            boolean r2 = r0.isVisible((android.graphics.Paint) r1)
            if (r2 != 0) goto L_0x00dd
            return
        L_0x00dd:
            int r2 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r2 <= 0) goto L_0x00e3
            r2 = r7
            goto L_0x00e4
        L_0x00e3:
            r2 = r5
        L_0x00e4:
            int r4 = r0.mDowngradeMaximumRectanglePixelSize
            long r9 = (long) r4
            int r4 = (r2 > r9 ? 1 : (r2 == r9 ? 0 : -1))
            if (r4 > 0) goto L_0x0101
            r10 = r20
            float r15 = (float) r10
            float r2 = (float) r12
            long r10 = r10 + r7
            float r3 = (float) r10
            long r12 = r12 + r5
            float r4 = (float) r12
            r14 = r23
            r16 = r2
            r17 = r3
            r18 = r4
            r19 = r1
            r14.drawRect(r15, r16, r17, r18, r19)
            return
        L_0x0101:
            org.osmdroid.views.overlay.LinearRing r4 = r0.mOutline
            int r5 = r0.mDowngradeMaximumPixelSize
            float[] r4 = r4.computeDowngradePointList(r5)
            if (r4 == 0) goto L_0x0171
            int r5 = r4.length
            if (r5 != 0) goto L_0x010f
            goto L_0x0171
        L_0x010f:
            int r5 = r4.length
            int r5 = r5 * 2
            float[] r6 = r0.mDowngradeSegments
            if (r6 == 0) goto L_0x0119
            int r6 = r6.length
            if (r6 >= r5) goto L_0x011d
        L_0x0119:
            float[] r5 = new float[r5]
            r0.mDowngradeSegments = r5
        L_0x011d:
            float r2 = (float) r2
            r3 = 1065353216(0x3f800000, float:1.0)
            float r2 = r2 * r3
            int r3 = r0.mDowngradeMaximumPixelSize
            float r3 = (float) r3
            float r2 = r2 / r3
            r3 = 0
            r5 = r3
            r6 = 0
            r7 = 0
        L_0x0129:
            int r8 = r4.length
            if (r6 >= r8) goto L_0x015d
            org.osmdroid.util.PointL r8 = r0.mDowngradeCenter
            long r8 = r8.f559x
            float r8 = (float) r8
            int r9 = r6 + 1
            r6 = r4[r6]
            float r6 = r6 * r2
            float r8 = r8 + r6
            org.osmdroid.util.PointL r6 = r0.mDowngradeCenter
            long r10 = r6.f560y
            float r6 = (float) r10
            int r10 = r9 + 1
            r9 = r4[r9]
            float r9 = r9 * r2
            float r6 = r6 + r9
            if (r7 != 0) goto L_0x0147
            r5 = r6
            r3 = r8
            goto L_0x0151
        L_0x0147:
            float[] r9 = r0.mDowngradeSegments
            int r11 = r7 + 1
            r9[r7] = r8
            int r7 = r11 + 1
            r9[r11] = r6
        L_0x0151:
            float[] r9 = r0.mDowngradeSegments
            int r11 = r7 + 1
            r9[r7] = r8
            int r7 = r11 + 1
            r9[r11] = r6
            r6 = r10
            goto L_0x0129
        L_0x015d:
            float[] r2 = r0.mDowngradeSegments
            int r4 = r7 + 1
            r2[r7] = r3
            int r3 = r4 + 1
            r2[r4] = r5
            r4 = 4
            if (r3 > r4) goto L_0x016b
            return
        L_0x016b:
            r4 = r23
            r5 = 0
            r4.drawLines(r2, r5, r3, r1)
        L_0x0171:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.overlay.PolyOverlayWithIW.displayDowngrade(android.graphics.Canvas, org.osmdroid.views.Projection):void");
    }

    public void setDensityMultiplier(float f) {
        this.mDensityMultiplier = f;
    }

    public boolean contains(MotionEvent motionEvent) {
        if (this.mPath.isEmpty()) {
            return false;
        }
        RectF rectF = new RectF();
        this.mPath.computeBounds(rectF, true);
        Region region = new Region();
        region.setPath(this.mPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        return region.contains((int) motionEvent.getX(), (int) motionEvent.getY());
    }

    public boolean isCloseTo(GeoPoint geoPoint, double d, MapView mapView) {
        return getCloseTo(geoPoint, d, mapView) != null;
    }

    public GeoPoint getCloseTo(GeoPoint geoPoint, double d, MapView mapView) {
        return this.mOutline.getCloseTo(geoPoint, d, mapView.getProjection(), this.mClosePath);
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent, MapView mapView) {
        GeoPoint geoPoint = (GeoPoint) mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY());
        if (this.mPath == null) {
            geoPoint = getCloseTo(geoPoint, (double) (this.mOutlinePaint.getStrokeWidth() * this.mDensity * this.mDensityMultiplier), mapView);
        } else if (!contains(motionEvent)) {
            geoPoint = null;
        }
        if (geoPoint != null) {
            return click(mapView, geoPoint);
        }
        return false;
    }
}
