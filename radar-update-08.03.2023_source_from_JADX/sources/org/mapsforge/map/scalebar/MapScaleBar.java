package org.mapsforge.map.scalebar;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.GraphicContext;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.IMapViewPosition;
import org.mapsforge.map.model.MapViewDimension;

public abstract class MapScaleBar {
    private static final ScaleBarPosition DEFAULT_SCALE_BAR_POSITION = ScaleBarPosition.BOTTOM_LEFT;
    private static final double LATITUDE_REDRAW_THRESHOLD = 0.2d;
    protected final DisplayModel displayModel;
    protected DistanceUnitAdapter distanceUnitAdapter;
    protected final GraphicFactory graphicFactory;
    protected final Bitmap mapScaleBitmap;
    protected final Canvas mapScaleCanvas;
    private final MapViewDimension mapViewDimension;
    private final IMapViewPosition mapViewPosition;
    private int marginHorizontal;
    private int marginVertical;
    private MapPosition prevMapPosition;
    protected boolean redrawNeeded;
    protected ScaleBarPosition scaleBarPosition = DEFAULT_SCALE_BAR_POSITION;
    private boolean visible;

    public enum ScaleBarPosition {
        BOTTOM_CENTER,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP_CENTER,
        TOP_LEFT,
        TOP_RIGHT
    }

    /* access modifiers changed from: protected */
    public abstract void redraw(Canvas canvas);

    protected static class ScaleBarLengthAndValue {
        public int scaleBarLength;
        public int scaleBarValue;

        public ScaleBarLengthAndValue(int i, int i2) {
            this.scaleBarLength = i;
            this.scaleBarValue = i2;
        }
    }

    public MapScaleBar(IMapViewPosition iMapViewPosition, MapViewDimension mapViewDimension2, DisplayModel displayModel2, GraphicFactory graphicFactory2, int i, int i2) {
        this.mapViewPosition = iMapViewPosition;
        this.mapViewDimension = mapViewDimension2;
        this.displayModel = displayModel2;
        this.graphicFactory = graphicFactory2;
        Bitmap createBitmap = graphicFactory2.createBitmap(i, i2);
        this.mapScaleBitmap = createBitmap;
        Canvas createCanvas = graphicFactory2.createCanvas();
        this.mapScaleCanvas = createCanvas;
        createCanvas.setBitmap(createBitmap);
        this.distanceUnitAdapter = MetricUnitAdapter.INSTANCE;
        this.visible = true;
        this.redrawNeeded = true;
    }

    public void destroy() {
        this.mapScaleBitmap.decrementRefCount();
        this.mapScaleCanvas.destroy();
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean z) {
        this.visible = z;
    }

    public DistanceUnitAdapter getDistanceUnitAdapter() {
        return this.distanceUnitAdapter;
    }

    public void setDistanceUnitAdapter(DistanceUnitAdapter distanceUnitAdapter2) {
        if (distanceUnitAdapter2 != null) {
            this.distanceUnitAdapter = distanceUnitAdapter2;
            this.redrawNeeded = true;
            return;
        }
        throw new IllegalArgumentException("adapter must not be null");
    }

    public int getMarginHorizontal() {
        return this.marginHorizontal;
    }

    public void setMarginHorizontal(int i) {
        if (this.marginHorizontal != i) {
            this.marginHorizontal = i;
            this.redrawNeeded = true;
        }
    }

    public int getMarginVertical() {
        return this.marginVertical;
    }

    public void setMarginVertical(int i) {
        if (this.marginVertical != i) {
            this.marginVertical = i;
            this.redrawNeeded = true;
        }
    }

    public ScaleBarPosition getScaleBarPosition() {
        return this.scaleBarPosition;
    }

    public void setScaleBarPosition(ScaleBarPosition scaleBarPosition2) {
        if (this.scaleBarPosition != scaleBarPosition2) {
            this.scaleBarPosition = scaleBarPosition2;
            this.redrawNeeded = true;
        }
    }

    /* renamed from: org.mapsforge.map.scalebar.MapScaleBar$1 */
    static /* synthetic */ class C13331 {

        /* renamed from: $SwitchMap$org$mapsforge$map$scalebar$MapScaleBar$ScaleBarPosition */
        static final /* synthetic */ int[] f404x399227b0;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition[] r0 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f404x399227b0 = r0
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition r1 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.BOTTOM_LEFT     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f404x399227b0     // Catch:{ NoSuchFieldError -> 0x001d }
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition r1 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.TOP_LEFT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f404x399227b0     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition r1 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.BOTTOM_CENTER     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f404x399227b0     // Catch:{ NoSuchFieldError -> 0x0033 }
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition r1 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.TOP_CENTER     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = f404x399227b0     // Catch:{ NoSuchFieldError -> 0x003e }
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition r1 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.BOTTOM_RIGHT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = f404x399227b0     // Catch:{ NoSuchFieldError -> 0x0049 }
                org.mapsforge.map.scalebar.MapScaleBar$ScaleBarPosition r1 = org.mapsforge.map.scalebar.MapScaleBar.ScaleBarPosition.TOP_RIGHT     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.scalebar.MapScaleBar.C13331.<clinit>():void");
        }
    }

    private int calculatePositionLeft(int i, int i2, int i3) {
        switch (C13331.f404x399227b0[this.scaleBarPosition.ordinal()]) {
            case 1:
            case 2:
                return this.marginHorizontal;
            case 3:
            case 4:
                return ((i2 - i) - i3) / 2;
            case 5:
            case 6:
                return ((i2 - i) - i3) - this.marginHorizontal;
            default:
                throw new IllegalArgumentException("unknown horizontal position: " + this.scaleBarPosition);
        }
    }

    private int calculatePositionTop(int i, int i2, int i3) {
        switch (C13331.f404x399227b0[this.scaleBarPosition.ordinal()]) {
            case 1:
            case 3:
            case 5:
                return ((i2 - i) - i3) - this.marginVertical;
            case 2:
            case 4:
            case 6:
                return this.marginVertical;
            default:
                throw new IllegalArgumentException("unknown vertical position: " + this.scaleBarPosition);
        }
    }

    /* access modifiers changed from: protected */
    public ScaleBarLengthAndValue calculateScaleBarLengthAndValue(DistanceUnitAdapter distanceUnitAdapter2) {
        MapPosition mapPosition = this.mapViewPosition.getMapPosition();
        this.prevMapPosition = mapPosition;
        double calculateGroundResolution = MercatorProjection.calculateGroundResolution(mapPosition.latLong.latitude, MercatorProjection.getMapSize(this.prevMapPosition.zoomLevel, this.displayModel.getTileSize())) / distanceUnitAdapter2.getMeterRatio();
        int[] scaleBarValues = distanceUnitAdapter2.getScaleBarValues();
        int length = scaleBarValues.length;
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            i2 = scaleBarValues[i3];
            i = (int) (((double) i2) / calculateGroundResolution);
            if (i < this.mapScaleBitmap.getWidth() - 10) {
                break;
            }
        }
        return new ScaleBarLengthAndValue(i, i2);
    }

    /* access modifiers changed from: protected */
    public ScaleBarLengthAndValue calculateScaleBarLengthAndValue() {
        return calculateScaleBarLengthAndValue(this.distanceUnitAdapter);
    }

    public void draw(GraphicContext graphicContext) {
        if (this.visible && this.mapViewDimension.getDimension() != null) {
            if (isRedrawNecessary()) {
                redraw(this.mapScaleCanvas);
                this.redrawNeeded = false;
            }
            graphicContext.drawBitmap(this.mapScaleBitmap, calculatePositionLeft(0, this.mapViewDimension.getDimension().width, this.mapScaleBitmap.getWidth()), calculatePositionTop(0, this.mapViewDimension.getDimension().height, this.mapScaleBitmap.getHeight()));
        }
    }

    public void drawScaleBar() {
        draw(this.mapScaleCanvas);
    }

    public void redrawScaleBar() {
        this.redrawNeeded = true;
    }

    /* access modifiers changed from: protected */
    public boolean isRedrawNecessary() {
        if (this.redrawNeeded || this.prevMapPosition == null) {
            return true;
        }
        MapPosition mapPosition = this.mapViewPosition.getMapPosition();
        if (mapPosition.zoomLevel == this.prevMapPosition.zoomLevel && Math.abs(mapPosition.latLong.latitude - this.prevMapPosition.latLong.latitude) <= LATITUDE_REDRAW_THRESHOLD) {
            return false;
        }
        return true;
    }
}
