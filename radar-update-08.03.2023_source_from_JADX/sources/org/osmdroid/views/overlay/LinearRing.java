package org.osmdroid.views.overlay;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.Distance;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.IntegerAccepter;
import org.osmdroid.util.LineBuilder;
import org.osmdroid.util.ListPointAccepter;
import org.osmdroid.util.ListPointL;
import org.osmdroid.util.PathBuilder;
import org.osmdroid.util.PointAccepter;
import org.osmdroid.util.PointL;
import org.osmdroid.util.SegmentClipper;
import org.osmdroid.util.SideOptimizationPointAccepter;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

public class LinearRing {
    private boolean isHorizontalRepeating;
    private boolean isVerticalRepeating;
    private final BoundingBox mBoundingBox;
    private final boolean mClosed;
    private double[] mDistances;
    private boolean mDistancesPrecomputed;
    private int mDowngradePixelSize;
    private float[] mDowngradePointList;
    private boolean mGeodesic;
    private final IntegerAccepter mIntegerAccepter;
    private final ArrayList<GeoPoint> mOriginalPoints;
    private final Path mPath;
    private final PointAccepter mPointAccepter;
    private final ListPointL mPointsForMilestones;
    private final PointL mProjectedCenter;
    private long mProjectedHeight;
    private long[] mProjectedPoints;
    private boolean mProjectedPrecomputed;
    private long mProjectedWidth;
    private final SegmentClipper mSegmentClipper;

    public LinearRing(Path path) {
        this(path, true);
    }

    public LinearRing(LineBuilder lineBuilder, boolean z) {
        this.mOriginalPoints = new ArrayList<>();
        this.mProjectedCenter = new PointL();
        this.mSegmentClipper = new SegmentClipper();
        this.mBoundingBox = new BoundingBox();
        this.isHorizontalRepeating = true;
        this.isVerticalRepeating = true;
        this.mPointsForMilestones = new ListPointL();
        this.mGeodesic = false;
        this.mPath = null;
        this.mPointAccepter = lineBuilder;
        if (lineBuilder instanceof LineDrawer) {
            IntegerAccepter integerAccepter = new IntegerAccepter(lineBuilder.getLines().length / 2);
            this.mIntegerAccepter = integerAccepter;
            ((LineDrawer) lineBuilder).setIntegerAccepter(integerAccepter);
        } else {
            this.mIntegerAccepter = null;
        }
        this.mClosed = z;
    }

    public LinearRing(LineBuilder lineBuilder) {
        this(lineBuilder, false);
    }

    public LinearRing(Path path, boolean z) {
        this.mOriginalPoints = new ArrayList<>();
        this.mProjectedCenter = new PointL();
        this.mSegmentClipper = new SegmentClipper();
        this.mBoundingBox = new BoundingBox();
        this.isHorizontalRepeating = true;
        this.isVerticalRepeating = true;
        this.mPointsForMilestones = new ListPointL();
        this.mGeodesic = false;
        this.mPath = path;
        this.mPointAccepter = new SideOptimizationPointAccepter(new PathBuilder(path));
        this.mIntegerAccepter = null;
        this.mClosed = z;
    }

    /* access modifiers changed from: package-private */
    public void clearPath() {
        this.mOriginalPoints.clear();
        this.mProjectedPoints = null;
        this.mDistances = null;
        resetPrecomputations();
        this.mPointAccepter.init();
    }

    /* access modifiers changed from: protected */
    public void addGreatCircle(GeoPoint geoPoint, GeoPoint geoPoint2, int i) {
        int i2 = i;
        double latitude = geoPoint.getLatitude() * 0.017453292519943295d;
        double longitude = geoPoint.getLongitude() * 0.017453292519943295d;
        double latitude2 = geoPoint2.getLatitude() * 0.017453292519943295d;
        double longitude2 = geoPoint2.getLongitude() * 0.017453292519943295d;
        double d = longitude2;
        double asin = Math.asin(Math.sqrt(Math.pow(Math.sin((latitude - latitude2) / 2.0d), 2.0d) + (Math.cos(latitude) * Math.cos(latitude2) * Math.pow(Math.sin((longitude - longitude2) / 2.0d), 2.0d)))) * 2.0d;
        int i3 = 1;
        while (i3 <= i2) {
            double d2 = (((double) i3) * 1.0d) / ((double) (i2 + 1));
            double sin = Math.sin((1.0d - d2) * asin) / Math.sin(asin);
            double sin2 = Math.sin(d2 * asin) / Math.sin(asin);
            double cos = (Math.cos(latitude) * sin * Math.cos(longitude)) + (Math.cos(latitude2) * sin2 * Math.cos(d));
            double d3 = asin;
            double cos2 = (Math.cos(latitude) * sin * Math.sin(longitude)) + (Math.cos(latitude2) * sin2 * Math.sin(d));
            this.mOriginalPoints.add(new GeoPoint(Math.atan2((sin * Math.sin(latitude)) + (sin2 * Math.sin(latitude2)), Math.sqrt(Math.pow(cos, 2.0d) + Math.pow(cos2, 2.0d))) * 57.29577951308232d, Math.atan2(cos2, cos) * 57.29577951308232d));
            i3++;
            asin = d3;
        }
    }

    public void addPoint(GeoPoint geoPoint) {
        if (this.mGeodesic && this.mOriginalPoints.size() > 0) {
            ArrayList<GeoPoint> arrayList = this.mOriginalPoints;
            GeoPoint geoPoint2 = arrayList.get(arrayList.size() - 1);
            addGreatCircle(geoPoint2, geoPoint, ((int) geoPoint2.distanceToAsDouble(geoPoint)) / 100000);
        }
        this.mOriginalPoints.add(geoPoint);
        resetPrecomputations();
    }

    private void resetPrecomputations() {
        this.mProjectedPrecomputed = false;
        this.mDistancesPrecomputed = false;
        this.mDowngradePixelSize = 0;
        this.mDowngradePointList = null;
    }

    public void setPoints(List<GeoPoint> list) {
        clearPath();
        for (GeoPoint addPoint : list) {
            addPoint(addPoint);
        }
    }

    public ArrayList<GeoPoint> getPoints() {
        return this.mOriginalPoints;
    }

    /* access modifiers changed from: package-private */
    public double[] getDistances() {
        computeDistances();
        return this.mDistances;
    }

    public double getDistance() {
        double d = 0.0d;
        for (double d2 : getDistances()) {
            d += d2;
        }
        return d;
    }

    public void setGeodesic(boolean z) {
        this.mGeodesic = z;
    }

    public boolean isGeodesic() {
        return this.mGeodesic;
    }

    /* access modifiers changed from: package-private */
    public PointL buildPathPortion(Projection projection, PointL pointL, boolean z) {
        if (this.mOriginalPoints.size() < 2) {
            return pointL;
        }
        computeProjected();
        computeDistances();
        if (pointL == null) {
            pointL = new PointL();
            getBestOffset(projection, pointL);
        }
        this.mSegmentClipper.init();
        clipAndStore(projection, pointL, this.mClosed, z, this.mSegmentClipper);
        this.mSegmentClipper.end();
        if (this.mClosed) {
            this.mPath.close();
        }
        return pointL;
    }

    /* access modifiers changed from: package-private */
    public void buildLinePortion(Projection projection, boolean z) {
        if (this.mOriginalPoints.size() >= 2) {
            computeProjected();
            computeDistances();
            PointL pointL = new PointL();
            getBestOffset(projection, pointL);
            this.mSegmentClipper.init();
            clipAndStore(projection, pointL, this.mClosed, z, this.mSegmentClipper);
            this.mSegmentClipper.end();
        }
    }

    public ListPointL getPointsForMilestones() {
        return this.mPointsForMilestones;
    }

    private void getBestOffset(Projection projection, PointL pointL) {
        Projection projection2 = projection;
        getBestOffset(projection, pointL, projection2.getLongPixelsFromProjected(this.mProjectedCenter, projection.getProjectedPowerDifference(), false, (PointL) null));
    }

    public void getBestOffset(Projection projection, PointL pointL, PointL pointL2) {
        PointL pointL3 = pointL2;
        Rect intrinsicScreenRect = projection.getIntrinsicScreenRect();
        getBestOffset((double) pointL3.f559x, (double) pointL3.f560y, ((double) (intrinsicScreenRect.left + intrinsicScreenRect.right)) / 2.0d, ((double) (intrinsicScreenRect.top + intrinsicScreenRect.bottom)) / 2.0d, projection.getWorldMapSize(), pointL);
    }

    private void getBestOffset(double d, double d2, double d3, double d4, double d5, PointL pointL) {
        long j;
        int i;
        int i2;
        long j2;
        int i3;
        PointL pointL2 = pointL;
        long round = Math.round(d5);
        int i4 = 0;
        if (!this.isVerticalRepeating) {
            j = round;
            i2 = 0;
            i = 0;
        } else {
            double d6 = d;
            double d7 = d2;
            double d8 = d3;
            long j3 = round;
            int bestOffset = getBestOffset(d6, d7, d8, d4, 0, round);
            j = round;
            i2 = getBestOffset(d6, d7, d8, d4, 0, -round);
            i = bestOffset;
        }
        if (i <= i2) {
            i = -i2;
        }
        long j4 = j * ((long) i);
        long j5 = j;
        PointL pointL3 = pointL;
        pointL3.f560y = j4;
        if (!this.isHorizontalRepeating) {
            j2 = j5;
            i3 = 0;
        } else {
            double d9 = d;
            double d10 = d2;
            double d11 = d3;
            long j6 = j5;
            double d12 = d4;
            i4 = getBestOffset(d9, d10, d11, d12, j6, 0);
            long j7 = j6;
            j2 = j7;
            i3 = getBestOffset(d9, d10, d11, d12, -j7, 0);
        }
        if (i4 <= i3) {
            i4 = -i3;
        }
        pointL3.f559x = j2 * ((long) i4);
    }

    private int getBestOffset(double d, double d2, double d3, double d4, long j, long j2) {
        double d5 = 0.0d;
        int i = 0;
        while (true) {
            long j3 = (long) i;
            double squaredDistanceToPoint = Distance.getSquaredDistanceToPoint(d + ((double) (j3 * j)), d2 + ((double) (j3 * j2)), d3, d4);
            if (i != 0 && d5 <= squaredDistanceToPoint) {
                return i - 1;
            }
            i++;
            d5 = squaredDistanceToPoint;
        }
    }

    private void clipAndStore(Projection projection, PointL pointL, boolean z, boolean z2, SegmentClipper segmentClipper) {
        PointL pointL2 = pointL;
        SegmentClipper segmentClipper2 = segmentClipper;
        this.mPointsForMilestones.clear();
        double projectedPowerDifference = projection.getProjectedPowerDifference();
        PointL pointL3 = new PointL();
        PointL pointL4 = new PointL();
        PointL pointL5 = new PointL();
        int i = 0;
        while (true) {
            long[] jArr = this.mProjectedPoints;
            if (i >= jArr.length) {
                break;
            }
            pointL3.set(jArr[i], jArr[i + 1]);
            projection.getLongPixelsFromProjected(pointL3, projectedPowerDifference, false, pointL4);
            long j = pointL4.f559x + pointL2.f559x;
            long j2 = pointL4.f560y + pointL2.f560y;
            if (z2) {
                this.mPointsForMilestones.add(j, j2);
            }
            if (segmentClipper2 != null) {
                segmentClipper2.add(j, j2);
            }
            if (i == 0) {
                pointL5.set(j, j2);
            }
            i += 2;
        }
        if (z) {
            if (segmentClipper2 != null) {
                segmentClipper2.add(pointL5.f559x, pointL5.f560y);
            }
            if (z2) {
                this.mPointsForMilestones.add(pointL5.f559x, pointL5.f560y);
            }
        }
    }

    public static double getCloserValue(double d, double d2, double d3) {
        while (true) {
            double d4 = d2 - d3;
            if (Math.abs(d4 - d) >= Math.abs(d2 - d)) {
                break;
            }
            d2 = d4;
        }
        while (true) {
            double d5 = d2 + d3;
            if (Math.abs(d5 - d) >= Math.abs(d2 - d)) {
                return d2;
            }
            d2 = d5;
        }
    }

    private void setCloserPoint(PointL pointL, PointL pointL2, double d) {
        if (this.isHorizontalRepeating) {
            pointL2.f559x = Math.round(getCloserValue((double) pointL.f559x, (double) pointL2.f559x, d));
        }
        if (this.isVerticalRepeating) {
            pointL2.f560y = Math.round(getCloserValue((double) pointL.f560y, (double) pointL2.f560y, d));
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isCloseTo(GeoPoint geoPoint, double d, Projection projection, boolean z) {
        return getCloseTo(geoPoint, d, projection, z) != null;
    }

    /* access modifiers changed from: package-private */
    public GeoPoint getCloseTo(GeoPoint geoPoint, double d, Projection projection, boolean z) {
        double d2;
        double d3;
        double d4;
        double d5;
        Iterator<PointL> it;
        LinearRing linearRing = this;
        Projection projection2 = projection;
        computeProjected();
        GeoPoint geoPoint2 = null;
        Point pixels = projection2.toPixels(geoPoint, (Point) null);
        PointL pointL = new PointL();
        linearRing.getBestOffset(projection2, pointL);
        clipAndStore(projection, pointL, z, true, (SegmentClipper) null);
        double worldMapSize = projection.getWorldMapSize();
        Rect intrinsicScreenRect = projection.getIntrinsicScreenRect();
        int width = intrinsicScreenRect.width();
        int height = intrinsicScreenRect.height();
        double d6 = (double) pixels.x;
        while (true) {
            double d7 = d6 - worldMapSize;
            if (d7 < 0.0d) {
                break;
            }
            d6 = d7;
        }
        double d8 = (double) pixels.y;
        while (true) {
            double d9 = d8 - worldMapSize;
            if (d9 < 0.0d) {
                break;
            }
            d8 = d9;
        }
        double d10 = d * d;
        PointL pointL2 = new PointL();
        PointL pointL3 = new PointL();
        Iterator<PointL> it2 = linearRing.mPointsForMilestones.iterator();
        boolean z2 = true;
        int i = 0;
        while (it2.hasNext()) {
            pointL3.set(it2.next());
            if (z2) {
                d3 = worldMapSize;
                d4 = d6;
                d2 = d8;
                d5 = d10;
                it = it2;
                z2 = false;
            } else {
                double d11 = d6;
                d2 = d8;
                while (d11 < ((double) width)) {
                    double d12 = d2;
                    int i2 = width;
                    double d13 = d6;
                    while (d12 < ((double) height)) {
                        Iterator<PointL> it3 = it2;
                        double d14 = worldMapSize;
                        double d15 = d11;
                        double d16 = d12;
                        double projectionFactorToSegment = Distance.getProjectionFactorToSegment(d15, d16, (double) pointL2.f559x, (double) pointL2.f560y, (double) pointL3.f559x, (double) pointL3.f560y);
                        double d17 = d10;
                        int i3 = i2;
                        if (d17 > Distance.getSquaredDistanceToProjection(d15, d16, (double) pointL2.f559x, (double) pointL2.f560y, (double) pointL3.f559x, (double) pointL3.f560y, projectionFactorToSegment)) {
                            long[] jArr = this.mProjectedPoints;
                            int i4 = (i - 1) * 2;
                            long j = jArr[i4];
                            long j2 = jArr[i4 + 1];
                            int i5 = i * 2;
                            return MapView.getTileSystem().getGeoFromMercator((long) (((double) j) + (((double) (jArr[i5] - j)) * projectionFactorToSegment)), (long) (((double) j2) + (((double) (jArr[i5 + 1] - j2)) * projectionFactorToSegment)), 1.15292150460684698E18d, (GeoPoint) null, false, false);
                        }
                        d12 += d14;
                        it2 = it3;
                        linearRing = this;
                        i2 = i3;
                        worldMapSize = d14;
                        d10 = d17;
                    }
                    LinearRing linearRing2 = linearRing;
                    Iterator<PointL> it4 = it2;
                    d11 += worldMapSize;
                    width = i2;
                    d6 = d13;
                    d10 = d10;
                }
                d3 = worldMapSize;
                d4 = d6;
                d5 = d10;
                it = it2;
            }
            int i6 = width;
            LinearRing linearRing3 = linearRing;
            pointL2.set(pointL3);
            i++;
            it2 = it;
            d8 = d2;
            linearRing = linearRing3;
            width = i6;
            d6 = d4;
            worldMapSize = d3;
            d10 = d5;
            geoPoint2 = null;
        }
        LinearRing linearRing4 = linearRing;
        return geoPoint2;
    }

    public void setClipArea(long j, long j2, long j3, long j4) {
        this.mSegmentClipper.set(j, j2, j3, j4, this.mPointAccepter, this.mIntegerAccepter, this.mPath != null);
    }

    public void setClipArea(Projection projection) {
        Rect intrinsicScreenRect = projection.getIntrinsicScreenRect();
        int width = intrinsicScreenRect.width() / 2;
        int height = intrinsicScreenRect.height() / 2;
        int sqrt = (int) (Math.sqrt((double) ((width * width) + (height * height))) * 2.0d * 1.1d);
        setClipArea((long) (width - sqrt), (long) (height - sqrt), (long) (width + sqrt), (long) (height + sqrt));
        this.isHorizontalRepeating = projection.isHorizontalWrapEnabled();
        this.isVerticalRepeating = projection.isVerticalWrapEnabled();
    }

    public GeoPoint getCenter(GeoPoint geoPoint) {
        if (geoPoint == null) {
            geoPoint = new GeoPoint(0.0d, 0.0d);
        }
        BoundingBox boundingBox = getBoundingBox();
        geoPoint.setLatitude(boundingBox.getCenterLatitude());
        geoPoint.setLongitude(boundingBox.getCenterLongitude());
        return geoPoint;
    }

    private void computeProjected() {
        if (!this.mProjectedPrecomputed) {
            this.mProjectedPrecomputed = true;
            long[] jArr = this.mProjectedPoints;
            if (jArr == null || jArr.length != this.mOriginalPoints.size() * 2) {
                this.mProjectedPoints = new long[(this.mOriginalPoints.size() * 2)];
            }
            int i = 0;
            PointL pointL = new PointL();
            PointL pointL2 = new PointL();
            TileSystem tileSystem = MapView.getTileSystem();
            Iterator<GeoPoint> it = this.mOriginalPoints.iterator();
            double d = 0.0d;
            double d2 = 0.0d;
            double d3 = 0.0d;
            double d4 = 0.0d;
            long j = 0;
            long j2 = 0;
            long j3 = 0;
            long j4 = 0;
            while (it.hasNext()) {
                GeoPoint next = it.next();
                double latitude = next.getLatitude();
                double longitude = next.getLongitude();
                tileSystem.getMercatorFromGeo(latitude, longitude, 1.15292150460684698E18d, pointL2, false);
                if (i == 0) {
                    j = pointL2.f559x;
                    j2 = j;
                    j3 = pointL2.f560y;
                    j4 = j3;
                    d = latitude;
                    d3 = d;
                    d2 = longitude;
                    d4 = d2;
                } else {
                    setCloserPoint(pointL, pointL2, 1.15292150460684698E18d);
                    if (j2 > pointL2.f559x) {
                        j2 = pointL2.f559x;
                        d4 = longitude;
                    }
                    if (j < pointL2.f559x) {
                        j = pointL2.f559x;
                        d2 = longitude;
                    }
                    if (j4 > pointL2.f560y) {
                        j4 = pointL2.f560y;
                        d = latitude;
                    }
                    if (j3 < pointL2.f560y) {
                        j3 = pointL2.f560y;
                        d3 = latitude;
                    }
                }
                int i2 = i * 2;
                this.mProjectedPoints[i2] = pointL2.f559x;
                this.mProjectedPoints[i2 + 1] = pointL2.f560y;
                pointL.set(pointL2.f559x, pointL2.f560y);
                i++;
            }
            this.mProjectedWidth = j - j2;
            this.mProjectedHeight = j3 - j4;
            this.mProjectedCenter.set((j2 + j) / 2, (j4 + j3) / 2);
            this.mBoundingBox.set(d, d2, d3, d4);
        }
    }

    private void computeDistances() {
        if (!this.mDistancesPrecomputed) {
            this.mDistancesPrecomputed = true;
            double[] dArr = this.mDistances;
            if (dArr == null || dArr.length != this.mOriginalPoints.size()) {
                this.mDistances = new double[this.mOriginalPoints.size()];
            }
            int i = 0;
            GeoPoint geoPoint = new GeoPoint(0.0d, 0.0d);
            Iterator<GeoPoint> it = this.mOriginalPoints.iterator();
            while (it.hasNext()) {
                GeoPoint next = it.next();
                if (i == 0) {
                    this.mDistances[i] = 0.0d;
                } else {
                    this.mDistances[i] = next.distanceToAsDouble(geoPoint);
                }
                geoPoint.setCoords(next.getLatitude(), next.getLongitude());
                i++;
            }
        }
    }

    public BoundingBox getBoundingBox() {
        if (!this.mProjectedPrecomputed) {
            computeProjected();
        }
        return this.mBoundingBox;
    }

    public void clear() {
        this.mOriginalPoints.clear();
        Path path = this.mPath;
        if (path != null) {
            path.reset();
        }
        this.mPointsForMilestones.clear();
    }

    /* access modifiers changed from: package-private */
    public float[] computeDowngradePointList(int i) {
        if (i == 0) {
            return null;
        }
        if (this.mDowngradePixelSize == i) {
            return this.mDowngradePointList;
        }
        computeProjected();
        long j = this.mProjectedWidth;
        long j2 = this.mProjectedHeight;
        if (j <= j2) {
            j = j2;
        }
        if (j == 0) {
            return null;
        }
        ListPointAccepter listPointAccepter = new ListPointAccepter(true);
        SideOptimizationPointAccepter sideOptimizationPointAccepter = new SideOptimizationPointAccepter(listPointAccepter);
        double d = (((double) j) * 1.0d) / ((double) i);
        int i2 = 0;
        int i3 = 0;
        while (true) {
            long[] jArr = this.mProjectedPoints;
            if (i3 >= jArr.length) {
                break;
            }
            int i4 = i3 + 1;
            long j3 = jArr[i3];
            i3 = i4 + 1;
            sideOptimizationPointAccepter.add(Math.round(((double) (j3 - this.mProjectedCenter.f559x)) / d), Math.round(((double) (jArr[i4] - this.mProjectedCenter.f560y)) / d));
        }
        this.mDowngradePixelSize = i;
        this.mDowngradePointList = new float[listPointAccepter.getList().size()];
        while (true) {
            float[] fArr = this.mDowngradePointList;
            if (i2 >= fArr.length) {
                return fArr;
            }
            fArr[i2] = (float) listPointAccepter.getList().get(i2).longValue();
            i2++;
        }
    }
}
