package org.osmdroid.views;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IProjection;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.GeometryMath;
import org.osmdroid.util.PointL;
import org.osmdroid.util.RectL;
import org.osmdroid.util.TileSystem;

public class Projection implements IProjection {
    public static final double mProjectedMapSize = 1.15292150460684698E18d;
    private boolean horizontalWrapEnabled;
    private final BoundingBox mBoundingBoxProjection;
    private final GeoPoint mCurrentCenter;
    private final Rect mIntrinsicScreenRectProjection;
    private final int mMapCenterOffsetX;
    private final int mMapCenterOffsetY;
    private final double mMercatorMapSize;
    private long mOffsetX;
    private long mOffsetY;
    private final float mOrientation;
    private final Matrix mRotateAndScaleMatrix;
    private final float[] mRotateScalePoints;
    private final Rect mScreenRectProjection;
    private long mScrollX;
    private long mScrollY;
    private final double mTileSize;
    private final TileSystem mTileSystem;
    private final Matrix mUnrotateAndScaleMatrix;
    private final double mZoomLevelProjection;
    private boolean verticalWrapEnabled;

    public void detach() {
    }

    Projection(MapView mapView) {
        this(mapView.getZoomLevelDouble(), mapView.getIntrinsicScreenRect((Rect) null), mapView.getExpectedCenter(), mapView.getMapScrollX(), mapView.getMapScrollY(), mapView.getMapOrientation(), mapView.isHorizontalMapRepetitionEnabled(), mapView.isVerticalMapRepetitionEnabled(), MapView.getTileSystem(), mapView.getMapCenterOffsetX(), mapView.getMapCenterOffsetY());
    }

    public Projection(double d, Rect rect, GeoPoint geoPoint, long j, long j2, float f, boolean z, boolean z2, TileSystem tileSystem, int i, int i2) {
        GeoPoint geoPoint2;
        float f2 = f;
        Matrix matrix = new Matrix();
        this.mRotateAndScaleMatrix = matrix;
        Matrix matrix2 = new Matrix();
        this.mUnrotateAndScaleMatrix = matrix2;
        this.mRotateScalePoints = new float[2];
        this.mBoundingBoxProjection = new BoundingBox();
        this.mScreenRectProjection = new Rect();
        this.mCurrentCenter = new GeoPoint(0.0d, 0.0d);
        this.mMapCenterOffsetX = i;
        this.mMapCenterOffsetY = i2;
        this.mZoomLevelProjection = d;
        this.horizontalWrapEnabled = z;
        this.verticalWrapEnabled = z2;
        this.mTileSystem = tileSystem;
        double MapSize = TileSystem.MapSize(d);
        this.mMercatorMapSize = MapSize;
        this.mTileSize = TileSystem.getTileSize(d);
        this.mIntrinsicScreenRectProjection = rect;
        if (geoPoint != null) {
            geoPoint2 = geoPoint;
        } else {
            geoPoint2 = new GeoPoint(0.0d, 0.0d);
        }
        this.mScrollX = j;
        this.mScrollY = j2;
        TileSystem tileSystem2 = tileSystem;
        double d2 = MapSize;
        this.mOffsetX = (((long) getScreenCenterX()) - this.mScrollX) - tileSystem2.getMercatorXFromLongitude(geoPoint2.getLongitude(), d2, this.horizontalWrapEnabled);
        this.mOffsetY = (((long) getScreenCenterY()) - this.mScrollY) - tileSystem2.getMercatorYFromLatitude(geoPoint2.getLatitude(), d2, this.verticalWrapEnabled);
        this.mOrientation = f2;
        matrix.preRotate(f2, (float) getScreenCenterX(), (float) getScreenCenterY());
        matrix.invert(matrix2);
        refresh();
    }

    public Projection(double d, int i, int i2, GeoPoint geoPoint, float f, boolean z, boolean z2, int i3, int i4) {
        this(d, new Rect(0, 0, i, i2), geoPoint, 0, 0, f, z, z2, MapView.getTileSystem(), i3, i4);
    }

    public Projection getOffspring(double d, Rect rect) {
        return new Projection(d, rect, this.mCurrentCenter, 0, 0, this.mOrientation, this.horizontalWrapEnabled, this.verticalWrapEnabled, this.mTileSystem, 0, 0);
    }

    public double getZoomLevel() {
        return this.mZoomLevelProjection;
    }

    public BoundingBox getBoundingBox() {
        return this.mBoundingBoxProjection;
    }

    public Rect getScreenRect() {
        return this.mScreenRectProjection;
    }

    public Rect getIntrinsicScreenRect() {
        return this.mIntrinsicScreenRectProjection;
    }

    public IGeoPoint fromPixels(int i, int i2) {
        return fromPixels(i, i2, (GeoPoint) null, false);
    }

    public IGeoPoint fromPixels(int i, int i2, GeoPoint geoPoint) {
        return fromPixels(i, i2, geoPoint, false);
    }

    public IGeoPoint fromPixels(int i, int i2, GeoPoint geoPoint, boolean z) {
        return this.mTileSystem.getGeoFromMercator(getCleanMercator(getMercatorXFromPixel(i), this.horizontalWrapEnabled), getCleanMercator(getMercatorYFromPixel(i2), this.verticalWrapEnabled), this.mMercatorMapSize, geoPoint, this.horizontalWrapEnabled || z, this.verticalWrapEnabled || z);
    }

    public Point toPixels(IGeoPoint iGeoPoint, Point point) {
        return toPixels(iGeoPoint, point, false);
    }

    public Point toPixels(IGeoPoint iGeoPoint, Point point, boolean z) {
        if (point == null) {
            point = new Point();
        }
        point.x = TileSystem.truncateToInt(getLongPixelXFromLongitude(iGeoPoint.getLongitude(), z));
        point.y = TileSystem.truncateToInt(getLongPixelYFromLatitude(iGeoPoint.getLatitude(), z));
        return point;
    }

    public long getLongPixelXFromLongitude(double d, boolean z) {
        return getLongPixelXFromMercator(this.mTileSystem.getMercatorXFromLongitude(d, this.mMercatorMapSize, this.horizontalWrapEnabled || z), this.horizontalWrapEnabled);
    }

    public long getLongPixelXFromLongitude(double d) {
        return getLongPixelXFromMercator(this.mTileSystem.getMercatorXFromLongitude(d, this.mMercatorMapSize, false), false);
    }

    public long getLongPixelYFromLatitude(double d, boolean z) {
        return getLongPixelYFromMercator(this.mTileSystem.getMercatorYFromLatitude(d, this.mMercatorMapSize, this.verticalWrapEnabled || z), this.verticalWrapEnabled);
    }

    public long getLongPixelYFromLatitude(double d) {
        return getLongPixelYFromMercator(this.mTileSystem.getMercatorYFromLatitude(d, this.mMercatorMapSize, false), false);
    }

    public PointL toProjectedPixels(GeoPoint geoPoint, PointL pointL) {
        return toProjectedPixels(geoPoint.getLatitude(), geoPoint.getLongitude(), pointL);
    }

    @Deprecated
    public PointL toProjectedPixels(long j, long j2, PointL pointL) {
        return toProjectedPixels(((double) j) * 1.0E-6d, ((double) j2) * 1.0E-6d, pointL);
    }

    public PointL toProjectedPixels(double d, double d2, PointL pointL) {
        return toProjectedPixels(d, d2, true, pointL);
    }

    public PointL toProjectedPixels(double d, double d2, boolean z, PointL pointL) {
        return this.mTileSystem.getMercatorFromGeo(d, d2, 1.15292150460684698E18d, pointL, z);
    }

    @Deprecated
    public Point toPixelsFromProjected(PointL pointL, Point point) {
        if (point == null) {
            point = new Point();
        }
        double projectedPowerDifference = getProjectedPowerDifference();
        PointL pointL2 = new PointL();
        getLongPixelsFromProjected(pointL, projectedPowerDifference, true, pointL2);
        point.x = TileSystem.truncateToInt(pointL2.f559x);
        point.y = TileSystem.truncateToInt(pointL2.f560y);
        return point;
    }

    @Deprecated
    public Point toPixelsFromMercator(long j, long j2, Point point) {
        if (point == null) {
            point = new Point();
        }
        point.x = TileSystem.truncateToInt(getLongPixelXFromMercator(j, true));
        point.y = TileSystem.truncateToInt(getLongPixelYFromMercator(j2, true));
        return point;
    }

    public PointL toMercatorPixels(int i, int i2, PointL pointL) {
        if (pointL == null) {
            pointL = new PointL();
        }
        pointL.f559x = getCleanMercator(getMercatorXFromPixel(i), this.horizontalWrapEnabled);
        pointL.f560y = getCleanMercator(getMercatorYFromPixel(i2), this.verticalWrapEnabled);
        return pointL;
    }

    public float metersToEquatorPixels(float f) {
        return metersToPixels(f, 0.0d, this.mZoomLevelProjection);
    }

    public float metersToPixels(float f) {
        return metersToPixels(f, getBoundingBox().getCenterWithDateLine().getLatitude(), this.mZoomLevelProjection);
    }

    public float metersToPixels(float f, double d, double d2) {
        return (float) (((double) f) / TileSystem.GroundResolution(d, d2));
    }

    public IGeoPoint getNorthEast() {
        return fromPixels(this.mIntrinsicScreenRectProjection.right, this.mIntrinsicScreenRectProjection.top, (GeoPoint) null, true);
    }

    public IGeoPoint getSouthWest() {
        return fromPixels(this.mIntrinsicScreenRectProjection.left, this.mIntrinsicScreenRectProjection.bottom, (GeoPoint) null, true);
    }

    public Matrix getInvertedScaleRotateCanvasMatrix() {
        return this.mUnrotateAndScaleMatrix;
    }

    public Point unrotateAndScalePoint(int i, int i2, Point point) {
        return applyMatrixToPoint(i, i2, point, this.mUnrotateAndScaleMatrix, this.mOrientation != 0.0f);
    }

    public Point rotateAndScalePoint(int i, int i2, Point point) {
        return applyMatrixToPoint(i, i2, point, this.mRotateAndScaleMatrix, this.mOrientation != 0.0f);
    }

    private Point applyMatrixToPoint(int i, int i2, Point point, Matrix matrix, boolean z) {
        if (point == null) {
            point = new Point();
        }
        if (z) {
            float[] fArr = this.mRotateScalePoints;
            fArr[0] = (float) i;
            fArr[1] = (float) i2;
            matrix.mapPoints(fArr);
            point.x = (int) this.mRotateScalePoints[0];
            point.y = (int) this.mRotateScalePoints[1];
        } else {
            point.x = i;
            point.y = i2;
        }
        return point;
    }

    public Rect getPixelFromTile(int i, int i2, Rect rect) {
        if (rect == null) {
            rect = new Rect();
        }
        rect.left = TileSystem.truncateToInt(getLongPixelXFromMercator(getMercatorFromTile(i), false));
        rect.top = TileSystem.truncateToInt(getLongPixelYFromMercator(getMercatorFromTile(i2), false));
        rect.right = TileSystem.truncateToInt(getLongPixelXFromMercator(getMercatorFromTile(i + 1), false));
        rect.bottom = TileSystem.truncateToInt(getLongPixelYFromMercator(getMercatorFromTile(i2 + 1), false));
        return rect;
    }

    public long getMercatorFromTile(int i) {
        return TileSystem.getMercatorFromTile(i, this.mTileSize);
    }

    public Matrix getScaleRotateCanvasMatrix() {
        return this.mRotateAndScaleMatrix;
    }

    public double getProjectedPowerDifference() {
        return 1.15292150460684698E18d / getWorldMapSize();
    }

    @Deprecated
    public Point getPixelsFromProjected(PointL pointL, double d, Point point) {
        if (point == null) {
            point = new Point();
        }
        PointL pointL2 = new PointL();
        getLongPixelsFromProjected(pointL, d, true, pointL2);
        point.x = TileSystem.truncateToInt(pointL2.f559x);
        point.y = TileSystem.truncateToInt(pointL2.f560y);
        return point;
    }

    public PointL getLongPixelsFromProjected(PointL pointL, double d, boolean z, PointL pointL2) {
        if (pointL2 == null) {
            pointL2 = new PointL();
        }
        pointL2.f559x = getLongPixelXFromMercator((long) (((double) pointL.f559x) / d), z);
        pointL2.f560y = getLongPixelYFromMercator((long) (((double) pointL.f560y) / d), z);
        return pointL2;
    }

    private long getCloserPixel(long j, int i, int i2, double d) {
        long j2 = (long) ((i + i2) / 2);
        long j3 = (long) i;
        long j4 = 0;
        if (j < j3) {
            while (j < j3) {
                long j5 = j;
                j = (long) (((double) j) + d);
                j4 = j5;
            }
            return (j >= ((long) i2) && Math.abs(j2 - j) >= Math.abs(j2 - j4)) ? j4 : j;
        }
        while (j >= j3) {
            long j6 = j;
            j = (long) (((double) j) - d);
            j4 = j6;
        }
        return (j4 >= ((long) i2) && Math.abs(j2 - j) < Math.abs(j2 - j4)) ? j : j4;
    }

    private long getLongPixelXFromMercator(long j, boolean z) {
        return getLongPixelFromMercator(j, z, this.mOffsetX, this.mIntrinsicScreenRectProjection.left, this.mIntrinsicScreenRectProjection.right);
    }

    private long getLongPixelYFromMercator(long j, boolean z) {
        return getLongPixelFromMercator(j, z, this.mOffsetY, this.mIntrinsicScreenRectProjection.top, this.mIntrinsicScreenRectProjection.bottom);
    }

    private long getLongPixelFromMercator(long j, boolean z, long j2, int i, int i2) {
        long j3 = j + j2;
        if (!z) {
            return j3;
        }
        return getCloserPixel(j3, i, i2, this.mMercatorMapSize);
    }

    public int getTileFromMercator(long j) {
        return TileSystem.getTileFromMercator(j, this.mTileSize);
    }

    public RectL getMercatorViewPort(RectL rectL) {
        if (rectL == null) {
            rectL = new RectL();
        }
        float f = (float) this.mIntrinsicScreenRectProjection.left;
        float f2 = (float) this.mIntrinsicScreenRectProjection.right;
        float f3 = (float) this.mIntrinsicScreenRectProjection.top;
        float f4 = (float) this.mIntrinsicScreenRectProjection.bottom;
        if (this.mOrientation != 0.0f) {
            float[] fArr = {(float) this.mIntrinsicScreenRectProjection.left, (float) this.mIntrinsicScreenRectProjection.top, (float) this.mIntrinsicScreenRectProjection.right, (float) this.mIntrinsicScreenRectProjection.bottom, (float) this.mIntrinsicScreenRectProjection.left, (float) this.mIntrinsicScreenRectProjection.bottom, (float) this.mIntrinsicScreenRectProjection.right, (float) this.mIntrinsicScreenRectProjection.top};
            this.mUnrotateAndScaleMatrix.mapPoints(fArr);
            for (int i = 0; i < 8; i += 2) {
                float f5 = fArr[i];
                if (f > f5) {
                    f = f5;
                }
                if (f2 < f5) {
                    f2 = f5;
                }
                float f6 = fArr[i + 1];
                if (f3 > f6) {
                    f3 = f6;
                }
                if (f4 < f6) {
                    f4 = f6;
                }
            }
        }
        rectL.left = getMercatorXFromPixel((int) f);
        rectL.top = getMercatorYFromPixel((int) f3);
        rectL.right = getMercatorXFromPixel((int) f2);
        rectL.bottom = getMercatorYFromPixel((int) f4);
        return rectL;
    }

    public int getScreenCenterX() {
        return ((this.mIntrinsicScreenRectProjection.right + this.mIntrinsicScreenRectProjection.left) / 2) + this.mMapCenterOffsetX;
    }

    public int getScreenCenterY() {
        return ((this.mIntrinsicScreenRectProjection.bottom + this.mIntrinsicScreenRectProjection.top) / 2) + this.mMapCenterOffsetY;
    }

    public long getMercatorXFromPixel(int i) {
        return ((long) i) - this.mOffsetX;
    }

    public long getMercatorYFromPixel(int i) {
        return ((long) i) - this.mOffsetY;
    }

    public long getCleanMercator(long j, boolean z) {
        return this.mTileSystem.getCleanMercator(j, this.mMercatorMapSize, z);
    }

    public GeoPoint getCurrentCenter() {
        return this.mCurrentCenter;
    }

    public long getOffsetX() {
        return this.mOffsetX;
    }

    public long getOffsetY() {
        return this.mOffsetY;
    }

    public void save(Canvas canvas, boolean z, boolean z2) {
        if (this.mOrientation != 0.0f || z2) {
            canvas.save();
            canvas.concat(z ? this.mRotateAndScaleMatrix : this.mUnrotateAndScaleMatrix);
        }
    }

    public void restore(Canvas canvas, boolean z) {
        if (this.mOrientation != 0.0f || z) {
            canvas.restore();
        }
    }

    private void refresh() {
        fromPixels(getScreenCenterX(), getScreenCenterY(), this.mCurrentCenter);
        float f = this.mOrientation;
        if (f == 0.0f || f == 180.0f) {
            this.mScreenRectProjection.left = this.mIntrinsicScreenRectProjection.left;
            this.mScreenRectProjection.top = this.mIntrinsicScreenRectProjection.top;
            this.mScreenRectProjection.right = this.mIntrinsicScreenRectProjection.right;
            this.mScreenRectProjection.bottom = this.mIntrinsicScreenRectProjection.bottom;
        } else {
            GeometryMath.getBoundingBoxForRotatatedRectangle(this.mIntrinsicScreenRectProjection, getScreenCenterX(), getScreenCenterY(), this.mOrientation, this.mScreenRectProjection);
        }
        GeoPoint fromPixels = fromPixels(this.mScreenRectProjection.right, this.mScreenRectProjection.top, (GeoPoint) null, true);
        TileSystem tileSystem = MapView.getTileSystem();
        if (fromPixels.getLatitude() > tileSystem.getMaxLatitude()) {
            fromPixels = new GeoPoint(tileSystem.getMaxLatitude(), fromPixels.getLongitude());
        }
        if (fromPixels.getLatitude() < tileSystem.getMinLatitude()) {
            fromPixels = new GeoPoint(tileSystem.getMinLatitude(), fromPixels.getLongitude());
        }
        GeoPoint fromPixels2 = fromPixels(this.mScreenRectProjection.left, this.mScreenRectProjection.bottom, (GeoPoint) null, true);
        if (fromPixels2.getLatitude() > tileSystem.getMaxLatitude()) {
            fromPixels2 = new GeoPoint(tileSystem.getMaxLatitude(), fromPixels2.getLongitude());
        }
        if (fromPixels2.getLatitude() < tileSystem.getMinLatitude()) {
            fromPixels2 = new GeoPoint(tileSystem.getMinLatitude(), fromPixels2.getLongitude());
        }
        this.mBoundingBoxProjection.set(fromPixels.getLatitude(), fromPixels.getLongitude(), fromPixels2.getLatitude(), fromPixels2.getLongitude());
    }

    public void adjustOffsets(IGeoPoint iGeoPoint, PointF pointF) {
        if (pointF != null && iGeoPoint != null) {
            Point unrotateAndScalePoint = unrotateAndScalePoint((int) pointF.x, (int) pointF.y, (Point) null);
            Point pixels = toPixels(iGeoPoint, (Point) null);
            adjustOffsets((long) (unrotateAndScalePoint.x - pixels.x), (long) (unrotateAndScalePoint.y - pixels.y));
        }
    }

    @Deprecated
    public void adjustOffsets(BoundingBox boundingBox) {
        if (boundingBox != null) {
            adjustOffsets(boundingBox.getLonWest(), boundingBox.getLonEast(), false, 0);
            adjustOffsets(boundingBox.getActualNorth(), boundingBox.getActualSouth(), true, 0);
        }
    }

    /* access modifiers changed from: package-private */
    public void adjustOffsets(double d, double d2, boolean z, int i) {
        long j;
        double d3 = d2;
        long j2 = 0;
        if (z) {
            j = getScrollableOffset(getLongPixelYFromLatitude(d), getLongPixelYFromLatitude(d3), this.mMercatorMapSize, this.mIntrinsicScreenRectProjection.height(), i);
        } else {
            long scrollableOffset = getScrollableOffset(getLongPixelXFromLongitude(d), getLongPixelXFromLongitude(d3), this.mMercatorMapSize, this.mIntrinsicScreenRectProjection.width(), i);
            j = 0;
            j2 = scrollableOffset;
        }
        adjustOffsets(j2, j);
    }

    /* access modifiers changed from: package-private */
    public void adjustOffsets(long j, long j2) {
        if (j != 0 || j2 != 0) {
            this.mOffsetX += j;
            this.mOffsetY += j2;
            this.mScrollX -= j;
            this.mScrollY -= j2;
            refresh();
        }
    }

    public static long getScrollableOffset(long j, long j2, double d, int i, int i2) {
        long j3;
        while (true) {
            j3 = j2 - j;
            if (j3 >= 0) {
                break;
            }
            j2 = (long) (((double) j2) + d);
        }
        if (j3 < ((long) (i - (i2 * 2)))) {
            long j4 = j3 / 2;
            long j5 = (long) (i / 2);
            long j6 = (j5 - j4) - j;
            if (j6 > 0) {
                return j6;
            }
            long j7 = (j5 + j4) - j2;
            if (j7 < 0) {
                return j7;
            }
            return 0;
        }
        long j8 = ((long) i2) - j;
        if (j8 < 0) {
            return j8;
        }
        long j9 = ((long) (i - i2)) - j2;
        if (j9 > 0) {
            return j9;
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public boolean setMapScroll(MapView mapView) {
        if (mapView.getMapScrollX() == this.mScrollX && mapView.getMapScrollY() == this.mScrollY) {
            return false;
        }
        mapView.setMapScroll(this.mScrollX, this.mScrollY);
        return true;
    }

    public boolean isHorizontalWrapEnabled() {
        return this.horizontalWrapEnabled;
    }

    public boolean isVerticalWrapEnabled() {
        return this.verticalWrapEnabled;
    }

    public float getOrientation() {
        return this.mOrientation;
    }

    public int getWidth() {
        return this.mIntrinsicScreenRectProjection.width();
    }

    public int getHeight() {
        return this.mIntrinsicScreenRectProjection.height();
    }

    public double getWorldMapSize() {
        return this.mMercatorMapSize;
    }
}
