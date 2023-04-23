package org.mapsforge.map.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.input.MapZoomControls;
import org.mapsforge.map.android.input.TouchGestureHandler;
import org.mapsforge.map.controller.FrameBufferController;
import org.mapsforge.map.controller.LayerManagerController;
import org.mapsforge.map.controller.MapViewController;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.TileLayer;
import org.mapsforge.map.layer.labels.LabelStore;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.model.common.Observer;
import org.mapsforge.map.scalebar.DefaultMapScaleBar;
import org.mapsforge.map.scalebar.MapScaleBar;
import org.mapsforge.map.util.MapPositionUtil;
import org.mapsforge.map.util.MapViewProjection;
import org.mapsforge.map.view.FpsCounter;
import org.mapsforge.map.view.FrameBuffer;
import org.mapsforge.map.view.FrameBufferHA2;
import org.mapsforge.map.view.InputListener;

public class MapView extends ViewGroup implements org.mapsforge.map.view.MapView, Observer {
    private static final GraphicFactory GRAPHIC_FACTORY = AndroidGraphicFactory.INSTANCE;
    private final FpsCounter fpsCounter;
    private final FrameBuffer frameBuffer;
    private final FrameBufferController frameBufferController;
    private final GestureDetector gestureDetector;
    private GestureDetector gestureDetectorExternal;
    private final List<InputListener> inputListeners;
    private final LayerManager layerManager;
    private final Handler layoutHandler;
    private MapScaleBar mapScaleBar;
    private final MapViewProjection mapViewProjection;
    private final MapZoomControls mapZoomControls;
    private final Model model;
    private final ScaleGestureDetector scaleGestureDetector;
    private final TouchGestureHandler touchGestureHandler;

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public Alignment alignment;
        public LatLong latLong;

        public enum Alignment {
            TOP_LEFT,
            TOP_CENTER,
            TOP_RIGHT,
            CENTER_LEFT,
            CENTER,
            CENTER_RIGHT,
            BOTTOM_LEFT,
            BOTTOM_CENTER,
            BOTTOM_RIGHT
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.alignment = Alignment.BOTTOM_CENTER;
        }

        public LayoutParams(int i, int i2, LatLong latLong2, Alignment alignment2) {
            super(i, i2);
            this.latLong = latLong2;
            this.alignment = alignment2;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    public MapView(Context context) {
        this(context, (AttributeSet) null);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.inputListeners = new CopyOnWriteArrayList();
        this.layoutHandler = new Handler();
        setDescendantFocusability(393216);
        setWillNotDraw(false);
        Model model2 = new Model();
        this.model = model2;
        GraphicFactory graphicFactory = GRAPHIC_FACTORY;
        this.fpsCounter = new FpsCounter(graphicFactory, model2.displayModel);
        FrameBufferHA2 frameBufferHA2 = new FrameBufferHA2(model2.frameBufferModel, model2.displayModel, graphicFactory);
        this.frameBuffer = frameBufferHA2;
        this.frameBufferController = FrameBufferController.create(frameBufferHA2, model2);
        LayerManager layerManager2 = new LayerManager(this, model2.mapViewPosition, graphicFactory);
        this.layerManager = layerManager2;
        layerManager2.start();
        LayerManagerController.create(layerManager2, model2);
        MapViewController.create(this, model2);
        TouchGestureHandler touchGestureHandler2 = new TouchGestureHandler(this);
        this.touchGestureHandler = touchGestureHandler2;
        this.gestureDetector = new GestureDetector(context, touchGestureHandler2);
        this.scaleGestureDetector = new ScaleGestureDetector(context, touchGestureHandler2);
        MapZoomControls mapZoomControls2 = new MapZoomControls(context, this);
        this.mapZoomControls = mapZoomControls2;
        addView(mapZoomControls2, new ViewGroup.LayoutParams(-2, -2));
        this.mapScaleBar = new DefaultMapScaleBar(model2.mapViewPosition, model2.mapViewDimension, graphicFactory, model2.displayModel);
        this.mapViewProjection = new MapViewProjection(this);
        model2.mapViewPosition.addObserver(this);
    }

    public void addInputListener(InputListener inputListener) {
        if (inputListener == null) {
            throw new IllegalArgumentException("listener must not be null");
        } else if (!this.inputListeners.contains(inputListener)) {
            this.inputListeners.add(inputListener);
        } else {
            throw new IllegalArgumentException("listener is already registered: " + inputListener);
        }
    }

    public void addLayer(Layer layer) {
        this.layerManager.getLayers().add(layer);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void destroy() {
        this.touchGestureHandler.destroy();
        this.layoutHandler.removeCallbacksAndMessages((Object) null);
        this.layerManager.finish();
        this.frameBufferController.destroy();
        this.frameBuffer.destroy();
        MapScaleBar mapScaleBar2 = this.mapScaleBar;
        if (mapScaleBar2 != null) {
            mapScaleBar2.destroy();
        }
        this.mapZoomControls.destroy();
        getModel().mapViewPosition.destroy();
    }

    public void destroyAll() {
        LabelStore labelStore;
        Iterator<Layer> it = this.layerManager.getLayers().iterator();
        while (it.hasNext()) {
            Layer next = it.next();
            this.layerManager.getLayers().remove(next);
            next.onDestroy();
            if (next instanceof TileLayer) {
                ((TileLayer) next).getTileCache().destroy();
            }
            if ((next instanceof TileRendererLayer) && (labelStore = ((TileRendererLayer) next).getLabelStore()) != null) {
                labelStore.clear();
            }
        }
        destroy();
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2, (LatLong) null, LayoutParams.Alignment.BOTTOM_CENTER);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public BoundingBox getBoundingBox() {
        return MapPositionUtil.getBoundingBox(this.model.mapViewPosition.getMapPosition(), getDimension(), this.model.displayModel.getTileSize());
    }

    public Dimension getDimension() {
        return new Dimension(getWidth(), getHeight());
    }

    public FpsCounter getFpsCounter() {
        return this.fpsCounter;
    }

    public FrameBuffer getFrameBuffer() {
        return this.frameBuffer;
    }

    public LayerManager getLayerManager() {
        return this.layerManager;
    }

    public MapScaleBar getMapScaleBar() {
        return this.mapScaleBar;
    }

    public MapViewProjection getMapViewProjection() {
        return this.mapViewProjection;
    }

    public MapZoomControls getMapZoomControls() {
        return this.mapZoomControls;
    }

    public Model getModel() {
        return this.model;
    }

    public TouchGestureHandler getTouchGestureHandler() {
        return this.touchGestureHandler;
    }

    public void onChange() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (!getChildAt(i).equals(this.mapZoomControls)) {
                this.layoutHandler.post(new Runnable() {
                    public void run() {
                        MapView.this.requestLayout();
                    }
                });
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        org.mapsforge.core.graphics.Canvas createGraphicContext = AndroidGraphicFactory.createGraphicContext(canvas);
        this.frameBuffer.draw(createGraphicContext);
        MapScaleBar mapScaleBar2 = this.mapScaleBar;
        if (mapScaleBar2 != null) {
            mapScaleBar2.draw(createGraphicContext);
        }
        this.fpsCounter.draw(createGraphicContext);
        createGraphicContext.destroy();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00ad, code lost:
        r4 = r4 - r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00bb, code lost:
        r4 = r4 - r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00c2, code lost:
        r10.layout(r3, r4, r12 + r3, r1 + r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onLayout(boolean r8, int r9, int r10, int r11, int r12) {
        /*
            r7 = this;
            org.mapsforge.map.android.input.MapZoomControls r8 = r7.mapZoomControls
            int r8 = r8.getVisibility()
            r0 = 8
            if (r8 == r0) goto L_0x0045
            org.mapsforge.map.android.input.MapZoomControls r8 = r7.mapZoomControls
            int r8 = r8.getZoomControlsGravity()
            org.mapsforge.map.android.input.MapZoomControls r1 = r7.mapZoomControls
            int r1 = r1.getMeasuredWidth()
            org.mapsforge.map.android.input.MapZoomControls r2 = r7.mapZoomControls
            int r2 = r2.getMeasuredHeight()
            r3 = r8 & 7
            r4 = 1
            if (r3 == r4) goto L_0x0027
            r4 = 3
            if (r3 == r4) goto L_0x002c
            int r9 = r11 - r1
            goto L_0x002c
        L_0x0027:
            int r11 = r11 - r9
            int r11 = r11 - r1
            int r11 = r11 / 2
            int r9 = r9 + r11
        L_0x002c:
            r8 = r8 & 112(0x70, float:1.57E-43)
            r11 = 16
            if (r8 == r11) goto L_0x0039
            r11 = 48
            if (r8 == r11) goto L_0x003e
            int r10 = r12 - r2
            goto L_0x003e
        L_0x0039:
            int r12 = r12 - r10
            int r12 = r12 - r2
            int r12 = r12 / 2
            int r10 = r10 + r12
        L_0x003e:
            org.mapsforge.map.android.input.MapZoomControls r8 = r7.mapZoomControls
            int r1 = r1 + r9
            int r2 = r2 + r10
            r8.layout(r9, r10, r1, r2)
        L_0x0045:
            int r8 = r7.getChildCount()
            r9 = 0
        L_0x004a:
            if (r9 >= r8) goto L_0x00ca
            android.view.View r10 = r7.getChildAt(r9)
            org.mapsforge.map.android.input.MapZoomControls r11 = r7.mapZoomControls
            boolean r11 = r10.equals(r11)
            if (r11 == 0) goto L_0x005a
            goto L_0x00c7
        L_0x005a:
            int r11 = r10.getVisibility()
            if (r11 == r0) goto L_0x00c7
            android.view.ViewGroup$LayoutParams r11 = r10.getLayoutParams()
            boolean r11 = r7.checkLayoutParams(r11)
            if (r11 == 0) goto L_0x00c7
            android.view.ViewGroup$LayoutParams r11 = r10.getLayoutParams()
            org.mapsforge.map.android.view.MapView$LayoutParams r11 = (org.mapsforge.map.android.view.MapView.LayoutParams) r11
            int r12 = r10.getMeasuredWidth()
            int r1 = r10.getMeasuredHeight()
            org.mapsforge.map.util.MapViewProjection r2 = r7.mapViewProjection
            org.mapsforge.core.model.LatLong r3 = r11.latLong
            org.mapsforge.core.model.Point r2 = r2.toPixels(r3)
            if (r2 == 0) goto L_0x00c7
            int r3 = r7.getPaddingLeft()
            double r4 = r2.f381x
            long r4 = java.lang.Math.round(r4)
            int r4 = (int) r4
            int r3 = r3 + r4
            int r4 = r7.getPaddingTop()
            double r5 = r2.f382y
            long r5 = java.lang.Math.round(r5)
            int r2 = (int) r5
            int r4 = r4 + r2
            int[] r2 = org.mapsforge.map.android.view.MapView.C13192.f383xc9c660
            org.mapsforge.map.android.view.MapView$LayoutParams$Alignment r11 = r11.alignment
            int r11 = r11.ordinal()
            r11 = r2[r11]
            switch(r11) {
                case 2: goto L_0x00bf;
                case 3: goto L_0x00bd;
                case 4: goto L_0x00b9;
                case 5: goto L_0x00b3;
                case 6: goto L_0x00af;
                case 7: goto L_0x00ad;
                case 8: goto L_0x00aa;
                case 9: goto L_0x00a8;
                default: goto L_0x00a7;
            }
        L_0x00a7:
            goto L_0x00c2
        L_0x00a8:
            int r3 = r3 - r12
            goto L_0x00ad
        L_0x00aa:
            int r11 = r12 / 2
            int r3 = r3 - r11
        L_0x00ad:
            int r4 = r4 - r1
            goto L_0x00c2
        L_0x00af:
            int r3 = r3 - r12
            int r11 = r1 / 2
            goto L_0x00bb
        L_0x00b3:
            int r11 = r12 / 2
            int r3 = r3 - r11
            int r11 = r1 / 2
            goto L_0x00bb
        L_0x00b9:
            int r11 = r1 / 2
        L_0x00bb:
            int r4 = r4 - r11
            goto L_0x00c2
        L_0x00bd:
            int r3 = r3 - r12
            goto L_0x00c2
        L_0x00bf:
            int r11 = r12 / 2
            int r3 = r3 - r11
        L_0x00c2:
            int r12 = r12 + r3
            int r1 = r1 + r4
            r10.layout(r3, r4, r12, r1)
        L_0x00c7:
            int r9 = r9 + 1
            goto L_0x004a
        L_0x00ca:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.android.view.MapView.onLayout(boolean, int, int, int, int):void");
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        measureChildren(i, i2);
        super.onMeasure(i, i2);
    }

    public void onMoveEvent() {
        for (InputListener onMoveEvent : this.inputListeners) {
            onMoveEvent.onMoveEvent();
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        this.model.mapViewDimension.setDimension(new Dimension(i, i2));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isClickable()) {
            return false;
        }
        this.mapZoomControls.onMapViewTouchEvent(motionEvent);
        GestureDetector gestureDetector2 = this.gestureDetectorExternal;
        if (gestureDetector2 != null && gestureDetector2.onTouchEvent(motionEvent)) {
            return true;
        }
        return !this.scaleGestureDetector.isInProgress() ? this.gestureDetector.onTouchEvent(motionEvent) : this.scaleGestureDetector.onTouchEvent(motionEvent);
    }

    public void onZoomEvent() {
        for (InputListener onZoomEvent : this.inputListeners) {
            onZoomEvent.onZoomEvent();
        }
    }

    public void removeInputListener(InputListener inputListener) {
        if (inputListener == null) {
            throw new IllegalArgumentException("listener must not be null");
        } else if (this.inputListeners.contains(inputListener)) {
            this.inputListeners.remove(inputListener);
        } else {
            throw new IllegalArgumentException("listener is not registered: " + inputListener);
        }
    }

    public void repaint() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void setBuiltInZoomControls(boolean z) {
        this.mapZoomControls.setShowMapZoomControls(z);
    }

    public void setCenter(LatLong latLong) {
        this.model.mapViewPosition.setCenter(latLong);
    }

    public void setGestureDetector(GestureDetector gestureDetector2) {
        this.gestureDetectorExternal = gestureDetector2;
    }

    public void setMapScaleBar(MapScaleBar mapScaleBar2) {
        MapScaleBar mapScaleBar3 = this.mapScaleBar;
        if (mapScaleBar3 != null) {
            mapScaleBar3.destroy();
        }
        this.mapScaleBar = mapScaleBar2;
    }

    public void setZoomLevel(byte b) {
        this.model.mapViewPosition.setZoomLevel(b);
    }

    public void setZoomLevelMax(byte b) {
        this.model.mapViewPosition.setZoomLevelMax(b);
        this.mapZoomControls.setZoomLevelMax(b);
    }

    public void setZoomLevelMin(byte b) {
        this.model.mapViewPosition.setZoomLevelMin(b);
        this.mapZoomControls.setZoomLevelMin(b);
    }
}
