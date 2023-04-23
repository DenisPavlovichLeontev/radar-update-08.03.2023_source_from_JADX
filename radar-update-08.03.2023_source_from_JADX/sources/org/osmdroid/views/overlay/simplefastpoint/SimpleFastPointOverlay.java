package org.osmdroid.views.overlay.simplefastpoint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;

public class SimpleFastPointOverlay extends Overlay {
    private OnClickListener clickListener;
    private boolean[][] gridBool;
    private int gridHei;
    private List<StyledLabelledPoint> gridIndex;
    private int gridWid;
    private boolean hasMoved;
    private final BoundingBox mBoundingBox;
    private final PointAdapter mPointList;
    private Integer mSelectedPoint;
    private final SimpleFastPointOverlayOptions mStyle;
    private int numLabels;
    private BoundingBox prevBoundingBox;
    private BoundingBox startBoundingBox;
    private Projection startProjection;
    private int viewHei;
    private int viewWid;

    public interface OnClickListener {
        void onClick(PointAdapter pointAdapter, Integer num);
    }

    public interface PointAdapter extends Iterable<IGeoPoint> {
        IGeoPoint get(int i);

        boolean isLabelled();

        boolean isStyled();

        int size();
    }

    public class StyledLabelledPoint extends Point {
        /* access modifiers changed from: private */
        public Paint mPointStyle;
        /* access modifiers changed from: private */
        public Paint mTextStyle;
        /* access modifiers changed from: private */
        public String mlabel;

        public StyledLabelledPoint(Point point, String str, Paint paint, Paint paint2) {
            super(point);
            this.mlabel = str;
            this.mPointStyle = paint;
            this.mTextStyle = paint2;
        }
    }

    public SimpleFastPointOverlayOptions getStyle() {
        return this.mStyle;
    }

    public SimpleFastPointOverlay(PointAdapter pointAdapter, SimpleFastPointOverlayOptions simpleFastPointOverlayOptions) {
        this.hasMoved = false;
        this.prevBoundingBox = new BoundingBox();
        this.mStyle = simpleFastPointOverlayOptions;
        this.mPointList = pointAdapter;
        Iterator it = pointAdapter.iterator();
        Double d = null;
        Double d2 = null;
        Double d3 = null;
        Double d4 = null;
        while (it.hasNext()) {
            IGeoPoint iGeoPoint = (IGeoPoint) it.next();
            if (iGeoPoint != null) {
                d = (d == null || iGeoPoint.getLongitude() > d.doubleValue()) ? Double.valueOf(iGeoPoint.getLongitude()) : d;
                d4 = (d4 == null || iGeoPoint.getLongitude() < d4.doubleValue()) ? Double.valueOf(iGeoPoint.getLongitude()) : d4;
                d2 = (d2 == null || iGeoPoint.getLatitude() > d2.doubleValue()) ? Double.valueOf(iGeoPoint.getLatitude()) : d2;
                if (d3 == null || iGeoPoint.getLatitude() < d3.doubleValue()) {
                    d3 = Double.valueOf(iGeoPoint.getLatitude());
                }
            }
        }
        if (d != null) {
            this.mBoundingBox = new BoundingBox(d2.doubleValue(), d.doubleValue(), d3.doubleValue(), d4.doubleValue());
        } else {
            this.mBoundingBox = null;
        }
    }

    public SimpleFastPointOverlay(PointAdapter pointAdapter) {
        this(pointAdapter, SimpleFastPointOverlayOptions.getDefaultStyle());
    }

    private void updateGrid(MapView mapView) {
        this.viewWid = mapView.getWidth();
        this.viewHei = mapView.getHeight();
        this.gridWid = ((int) Math.floor((double) (((float) this.viewWid) / ((float) this.mStyle.mCellSize)))) + 1;
        int floor = ((int) Math.floor((double) (((float) this.viewHei) / ((float) this.mStyle.mCellSize)))) + 1;
        this.gridHei = floor;
        int i = this.gridWid;
        int[] iArr = new int[2];
        iArr[1] = floor;
        iArr[0] = i;
        this.gridBool = (boolean[][]) Array.newInstance(Boolean.TYPE, iArr);
    }

    private void computeGrid(MapView mapView) {
        BoundingBox boundingBox = mapView.getBoundingBox();
        this.startBoundingBox = boundingBox;
        this.startProjection = mapView.getProjection();
        if (boundingBox.getLatNorth() != this.prevBoundingBox.getLatNorth() || boundingBox.getLatSouth() != this.prevBoundingBox.getLatSouth() || boundingBox.getLonWest() != this.prevBoundingBox.getLonWest() || boundingBox.getLonEast() != this.prevBoundingBox.getLonEast()) {
            this.prevBoundingBox = new BoundingBox(boundingBox.getLatNorth(), boundingBox.getLonEast(), boundingBox.getLatSouth(), boundingBox.getLonWest());
            if (this.gridBool != null && this.viewHei == mapView.getHeight() && this.viewWid == mapView.getWidth()) {
                for (boolean[] fill : this.gridBool) {
                    Arrays.fill(fill, false);
                }
            } else {
                updateGrid(mapView);
            }
            Point point = new Point();
            Projection projection = mapView.getProjection();
            this.gridIndex = new ArrayList();
            this.numLabels = 0;
            for (IGeoPoint iGeoPoint : this.mPointList) {
                if (iGeoPoint != null && iGeoPoint.getLatitude() > boundingBox.getLatSouth() && iGeoPoint.getLatitude() < boundingBox.getLatNorth() && iGeoPoint.getLongitude() > boundingBox.getLonWest() && iGeoPoint.getLongitude() < boundingBox.getLonEast()) {
                    projection.toPixels(iGeoPoint, point);
                    int floor = (int) Math.floor((double) (((float) point.x) / ((float) this.mStyle.mCellSize)));
                    int floor2 = (int) Math.floor((double) (((float) point.y) / ((float) this.mStyle.mCellSize)));
                    if (floor < this.gridWid && floor2 < this.gridHei && floor >= 0 && floor2 >= 0) {
                        boolean[] zArr = this.gridBool[floor];
                        if (!zArr[floor2]) {
                            zArr[floor2] = true;
                            this.gridIndex.add(new StyledLabelledPoint(point, this.mPointList.isLabelled() ? ((LabelledGeoPoint) iGeoPoint).getLabel() : null, this.mPointList.isStyled() ? ((StyledLabelledGeoPoint) iGeoPoint).getPointStyle() : null, this.mPointList.isStyled() ? ((StyledLabelledGeoPoint) iGeoPoint).getTextStyle() : null));
                            this.numLabels++;
                        }
                    }
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.mStyle.mAlgorithm != SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            this.startBoundingBox = mapView.getBoundingBox();
            this.startProjection = mapView.getProjection();
        } else if (action == 1) {
            this.hasMoved = false;
            this.startBoundingBox = mapView.getBoundingBox();
            this.startProjection = mapView.getProjection();
            mapView.invalidate();
        } else if (action == 2) {
            this.hasMoved = true;
        }
        return false;
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent, MapView mapView) {
        if (!this.mStyle.mClickable) {
            return false;
        }
        Float f = null;
        int i = -1;
        Point point = new Point();
        Projection projection = mapView.getProjection();
        for (int i2 = 0; i2 < this.mPointList.size(); i2++) {
            if (this.mPointList.get(i2) != null) {
                projection.toPixels(this.mPointList.get(i2), point);
                if (Math.abs(motionEvent.getX() - ((float) point.x)) <= 50.0f && Math.abs(motionEvent.getY() - ((float) point.y)) <= 50.0f) {
                    float x = ((motionEvent.getX() - ((float) point.x)) * (motionEvent.getX() - ((float) point.x))) + ((motionEvent.getY() - ((float) point.y)) * (motionEvent.getY() - ((float) point.y)));
                    if (f == null || x < f.floatValue()) {
                        f = Float.valueOf(x);
                        i = i2;
                    }
                }
            }
        }
        if (f == null) {
            return false;
        }
        setSelectedPoint(Integer.valueOf(i));
        mapView.invalidate();
        OnClickListener onClickListener = this.clickListener;
        if (onClickListener == null) {
            return true;
        }
        onClickListener.onClick(this.mPointList, Integer.valueOf(i));
        return true;
    }

    public void setSelectedPoint(Integer num) {
        if (num == null || num.intValue() < 0 || num.intValue() >= this.mPointList.size()) {
            this.mSelectedPoint = null;
        } else {
            this.mSelectedPoint = num;
        }
    }

    public Integer getSelectedPoint() {
        return this.mSelectedPoint;
    }

    public BoundingBox getBoundingBox() {
        return this.mBoundingBox;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.clickListener = onClickListener;
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
        Paint paint;
        Paint paint2;
        Paint paint3;
        MapView mapView2 = mapView;
        if (!z) {
            Point point = new Point();
            Projection projection = mapView.getProjection();
            if (this.mStyle.mPointStyle != null || this.mPointList.isStyled()) {
                int i = C13951.f569x97395854[this.mStyle.mAlgorithm.ordinal()];
                if (i == 1) {
                    if (this.gridBool == null || (!this.hasMoved && !mapView.isAnimating())) {
                        computeGrid(mapView2);
                    }
                    GeoPoint geoPoint = new GeoPoint(this.startBoundingBox.getLatNorth(), this.startBoundingBox.getLonWest());
                    GeoPoint geoPoint2 = new GeoPoint(this.startBoundingBox.getLatSouth(), this.startBoundingBox.getLonEast());
                    Point pixels = projection.toPixels(geoPoint, (Point) null);
                    Point pixels2 = projection.toPixels(geoPoint2, (Point) null);
                    Point pixels3 = this.startProjection.toPixels(geoPoint2, (Point) null);
                    Point point2 = new Point(pixels2.x - pixels3.x, pixels2.y - pixels3.y);
                    Point point3 = new Point(point2.x - pixels.x, point2.y - pixels.y);
                    boolean z2 = (this.mStyle.mLabelPolicy == SimpleFastPointOverlayOptions.LabelPolicy.DENSITY_THRESHOLD && this.numLabels <= this.mStyle.mMaxNShownLabels) || (this.mStyle.mLabelPolicy == SimpleFastPointOverlayOptions.LabelPolicy.ZOOM_THRESHOLD && mapView.getZoomLevelDouble() >= ((double) this.mStyle.mMinZoomShowLabels));
                    for (StyledLabelledPoint next : this.gridIndex) {
                        float f = ((float) (next.x + pixels.x)) + ((float) ((next.x * point3.x) / pixels3.x));
                        float f2 = ((float) (next.y + pixels.y)) + ((float) ((next.y * point3.y) / pixels3.y));
                        boolean z3 = this.mPointList.isLabelled() && z2;
                        String access$000 = next.mlabel;
                        Paint access$100 = (!this.mPointList.isStyled() || next.mPointStyle == null) ? this.mStyle.mPointStyle : next.mPointStyle;
                        if (!this.mPointList.isStyled() || (paint = next.mTextStyle) == null) {
                            paint = this.mStyle.mTextStyle;
                        }
                        drawPointAt(canvas, f, f2, z3, access$000, access$100, paint, mapView);
                        point3 = point3;
                        pixels = pixels;
                    }
                } else if (i == 2) {
                    if (this.gridBool != null && this.viewHei == mapView.getHeight() && this.viewWid == mapView.getWidth()) {
                        for (boolean[] fill : this.gridBool) {
                            Arrays.fill(fill, false);
                        }
                    } else {
                        updateGrid(mapView2);
                    }
                    boolean z4 = this.mStyle.mLabelPolicy == SimpleFastPointOverlayOptions.LabelPolicy.ZOOM_THRESHOLD && mapView.getZoomLevelDouble() >= ((double) this.mStyle.mMinZoomShowLabels);
                    BoundingBox boundingBox = mapView.getBoundingBox();
                    for (IGeoPoint iGeoPoint : this.mPointList) {
                        if (iGeoPoint != null && iGeoPoint.getLatitude() > boundingBox.getLatSouth() && iGeoPoint.getLatitude() < boundingBox.getLatNorth() && iGeoPoint.getLongitude() > boundingBox.getLonWest() && iGeoPoint.getLongitude() < boundingBox.getLonEast()) {
                            projection.toPixels(iGeoPoint, point);
                            int floor = (int) Math.floor((double) (((float) point.x) / ((float) this.mStyle.mCellSize)));
                            int floor2 = (int) Math.floor((double) (((float) point.y) / ((float) this.mStyle.mCellSize)));
                            if (floor < this.gridWid && floor2 < this.gridHei && floor >= 0 && floor2 >= 0) {
                                boolean[] zArr = this.gridBool[floor];
                                if (!zArr[floor2]) {
                                    zArr[floor2] = true;
                                    float f3 = (float) point.x;
                                    float f4 = (float) point.y;
                                    boolean z5 = this.mPointList.isLabelled() && z4;
                                    String label = this.mPointList.isLabelled() ? ((LabelledGeoPoint) iGeoPoint).getLabel() : null;
                                    if (this.mPointList.isStyled()) {
                                        StyledLabelledGeoPoint styledLabelledGeoPoint = (StyledLabelledGeoPoint) iGeoPoint;
                                        if (styledLabelledGeoPoint.getPointStyle() != null) {
                                            paint2 = styledLabelledGeoPoint.getPointStyle();
                                            Paint paint4 = paint2;
                                            if (!this.mPointList.isStyled() || (r0 = ((StyledLabelledGeoPoint) iGeoPoint).getTextStyle()) == null) {
                                                Paint paint5 = this.mStyle.mTextStyle;
                                            }
                                            drawPointAt(canvas, f3, f4, z5, label, paint4, paint5, mapView);
                                        }
                                    }
                                    paint2 = this.mStyle.mPointStyle;
                                    Paint paint42 = paint2;
                                    Paint paint52 = this.mStyle.mTextStyle;
                                    drawPointAt(canvas, f3, f4, z5, label, paint42, paint52, mapView);
                                }
                            }
                        }
                    }
                } else if (i == 3) {
                    boolean z6 = this.mStyle.mLabelPolicy == SimpleFastPointOverlayOptions.LabelPolicy.ZOOM_THRESHOLD && mapView.getZoomLevelDouble() >= ((double) this.mStyle.mMinZoomShowLabels);
                    BoundingBox boundingBox2 = mapView.getBoundingBox();
                    for (IGeoPoint iGeoPoint2 : this.mPointList) {
                        if (iGeoPoint2 != null && iGeoPoint2.getLatitude() > boundingBox2.getLatSouth() && iGeoPoint2.getLatitude() < boundingBox2.getLatNorth() && iGeoPoint2.getLongitude() > boundingBox2.getLonWest() && iGeoPoint2.getLongitude() < boundingBox2.getLonEast()) {
                            projection.toPixels(iGeoPoint2, point);
                            float f5 = (float) point.x;
                            float f6 = (float) point.y;
                            boolean z7 = this.mPointList.isLabelled() && z6;
                            String label2 = this.mPointList.isLabelled() ? ((LabelledGeoPoint) iGeoPoint2).getLabel() : null;
                            if (this.mPointList.isStyled()) {
                                StyledLabelledGeoPoint styledLabelledGeoPoint2 = (StyledLabelledGeoPoint) iGeoPoint2;
                                if (styledLabelledGeoPoint2.getPointStyle() != null) {
                                    paint3 = styledLabelledGeoPoint2.getPointStyle();
                                    Paint paint6 = paint3;
                                    if (!this.mPointList.isStyled() || (r0 = ((StyledLabelledGeoPoint) iGeoPoint2).getTextStyle()) == null) {
                                        Paint paint7 = this.mStyle.mTextStyle;
                                    }
                                    drawPointAt(canvas, f5, f6, z7, label2, paint6, paint7, mapView);
                                }
                            }
                            paint3 = this.mStyle.mPointStyle;
                            Paint paint62 = paint3;
                            Paint paint72 = this.mStyle.mTextStyle;
                            drawPointAt(canvas, f5, f6, z7, label2, paint62, paint72, mapView);
                        }
                    }
                }
            }
            Integer num = this.mSelectedPoint;
            if (num != null && num.intValue() < this.mPointList.size() && this.mPointList.get(this.mSelectedPoint.intValue()) != null && this.mStyle.mSelectedPointStyle != null) {
                projection.toPixels(this.mPointList.get(this.mSelectedPoint.intValue()), point);
                if (this.mStyle.mSymbol == SimpleFastPointOverlayOptions.Shape.CIRCLE) {
                    canvas.drawCircle((float) point.x, (float) point.y, this.mStyle.mSelectedCircleRadius, this.mStyle.mSelectedPointStyle);
                    return;
                }
                Canvas canvas2 = canvas;
                canvas.drawRect(((float) point.x) - this.mStyle.mSelectedCircleRadius, ((float) point.y) - this.mStyle.mSelectedCircleRadius, ((float) point.x) + this.mStyle.mSelectedCircleRadius, ((float) point.y) + this.mStyle.mSelectedCircleRadius, this.mStyle.mSelectedPointStyle);
            }
        }
    }

    /* renamed from: org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay$1 */
    static /* synthetic */ class C13951 {

        /* renamed from: $SwitchMap$org$osmdroid$views$overlay$simplefastpoint$SimpleFastPointOverlayOptions$RenderingAlgorithm */
        static final /* synthetic */ int[] f569x97395854;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions$RenderingAlgorithm[] r0 = org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions.RenderingAlgorithm.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f569x97395854 = r0
                org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions$RenderingAlgorithm r1 = org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f569x97395854     // Catch:{ NoSuchFieldError -> 0x001d }
                org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions$RenderingAlgorithm r1 = org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions.RenderingAlgorithm.MEDIUM_OPTIMIZATION     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f569x97395854     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions$RenderingAlgorithm r1 = org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions.RenderingAlgorithm.NO_OPTIMIZATION     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay.C13951.<clinit>():void");
        }
    }

    /* access modifiers changed from: protected */
    public void drawPointAt(Canvas canvas, float f, float f2, boolean z, String str, Paint paint, Paint paint2, MapView mapView) {
        canvas.save();
        canvas.rotate(-mapView.getMapOrientation(), f, f2);
        if (this.mStyle.mSymbol == SimpleFastPointOverlayOptions.Shape.CIRCLE) {
            canvas.drawCircle(f, f2, this.mStyle.mCircleRadius, paint);
        } else {
            canvas.drawRect(f - this.mStyle.mCircleRadius, f2 - this.mStyle.mCircleRadius, f + this.mStyle.mCircleRadius, f2 + this.mStyle.mCircleRadius, paint);
        }
        if (z && str != null) {
            canvas.drawText(str, f, (f2 - this.mStyle.mCircleRadius) - 5.0f, paint2);
        }
        canvas.restore();
    }
}
