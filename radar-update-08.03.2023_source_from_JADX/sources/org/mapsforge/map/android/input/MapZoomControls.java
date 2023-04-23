package org.mapsforge.map.android.input;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.ZoomButton;
import android.widget.ZoomControls;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.model.common.Observer;

public class MapZoomControls extends LinearLayout implements Observer {
    private static final int DEFAULT_HORIZONTAL_MARGIN = 5;
    private static final int DEFAULT_VERTICAL_MARGIN = 0;
    private static final int DEFAULT_ZOOM_CONTROLS_GRAVITY = 85;
    private static final byte DEFAULT_ZOOM_LEVEL_MAX = 22;
    private static final byte DEFAULT_ZOOM_LEVEL_MIN = 0;
    private static final long DEFAULT_ZOOM_SPEED = 500;
    private static final int MSG_ZOOM_CONTROLS_HIDE = 0;
    private static final long ZOOM_CONTROLS_TIMEOUT = ViewConfiguration.getZoomControlsTimeout();
    private boolean autoHide = true;
    private final ZoomButton buttonZoomIn;
    private final ZoomButton buttonZoomOut;
    /* access modifiers changed from: private */
    public final MapView mapView;
    private boolean showMapZoomControls;
    private int zoomControlsGravity;
    private final Handler zoomControlsHideHandler;
    private byte zoomLevelMax;
    private byte zoomLevelMin;

    public enum Orientation {
        HORIZONTAL_IN_OUT(0, true),
        HORIZONTAL_OUT_IN(0, false),
        VERTICAL_IN_OUT(1, true),
        VERTICAL_OUT_IN(1, false);
        
        public final int layoutOrientation;
        public final boolean zoomInFirst;

        private Orientation(int i, boolean z) {
            this.layoutOrientation = i;
            this.zoomInFirst = z;
        }
    }

    public MapZoomControls(Context context, final MapView mapView2) {
        super(context);
        this.mapView = mapView2;
        setMarginHorizontal(5);
        setMarginVertical(0);
        this.showMapZoomControls = true;
        this.zoomLevelMax = DEFAULT_ZOOM_LEVEL_MAX;
        this.zoomLevelMin = 0;
        setVisibility(8);
        this.zoomControlsGravity = 85;
        this.zoomControlsHideHandler = new Handler() {
            public void handleMessage(Message message) {
                MapZoomControls.this.hide();
            }
        };
        ZoomControls zoomControls = new ZoomControls(context);
        ZoomButton zoomButton = (ZoomButton) zoomControls.getChildAt(1);
        this.buttonZoomIn = zoomButton;
        ZoomButton zoomButton2 = (ZoomButton) zoomControls.getChildAt(0);
        this.buttonZoomOut = zoomButton2;
        zoomControls.removeAllViews();
        setOrientation(zoomControls.getOrientation());
        setZoomInFirst(false);
        setZoomSpeed(DEFAULT_ZOOM_SPEED);
        zoomButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mapView2.onZoomEvent();
                MapZoomControls.this.mapView.getModel().mapViewPosition.zoomIn();
            }
        });
        zoomButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mapView2.onZoomEvent();
                MapZoomControls.this.mapView.getModel().mapViewPosition.zoomOut();
            }
        });
        mapView2.getModel().mapViewPosition.addObserver(this);
    }

    /* access modifiers changed from: private */
    public void changeZoomControls(int i) {
        boolean z = true;
        this.buttonZoomIn.setEnabled(i < this.zoomLevelMax);
        ZoomButton zoomButton = this.buttonZoomOut;
        if (i <= this.zoomLevelMin) {
            z = false;
        }
        zoomButton.setEnabled(z);
    }

    public void destroy() {
        this.mapView.getModel().mapViewPosition.removeObserver(this);
    }

    private void fade(int i, float f, float f2) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(f, f2);
        alphaAnimation.setDuration(DEFAULT_ZOOM_SPEED);
        startAnimation(alphaAnimation);
        setVisibility(i);
    }

    public int getZoomControlsGravity() {
        return this.zoomControlsGravity;
    }

    public byte getZoomLevelMax() {
        return this.zoomLevelMax;
    }

    public byte getZoomLevelMin() {
        return this.zoomLevelMin;
    }

    public void hide() {
        fade(8, 1.0f, 0.0f);
    }

    public boolean isAutoHide() {
        return this.autoHide;
    }

    public boolean isShowMapZoomControls() {
        return this.showMapZoomControls;
    }

    public void onChange() {
        onZoomLevelChange(this.mapView.getModel().mapViewPosition.getZoomLevel());
    }

    public void onMapViewTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() <= 1 && this.showMapZoomControls && this.autoHide) {
            int action = motionEvent.getAction();
            if (action == 0) {
                showZoomControls();
            } else if (action == 1) {
                showZoomControlsWithTimeout();
            } else if (action == 3) {
                showZoomControlsWithTimeout();
            }
        }
    }

    public void onZoomLevelChange(final int i) {
        if (AndroidUtil.currentThreadIsUiThread()) {
            changeZoomControls(i);
        } else {
            this.mapView.post(new Runnable() {
                public void run() {
                    MapZoomControls.this.changeZoomControls(i);
                }
            });
        }
    }

    public void setAutoHide(boolean z) {
        this.autoHide = z;
        if (!z) {
            showZoomControls();
        }
    }

    public void setMarginHorizontal(int i) {
        setPadding(i, getPaddingTop(), i, getPaddingBottom());
        this.mapView.requestLayout();
    }

    public void setMarginVertical(int i) {
        setPadding(getPaddingLeft(), i, getPaddingRight(), i);
        this.mapView.requestLayout();
    }

    public void setShowMapZoomControls(boolean z) {
        this.showMapZoomControls = z;
    }

    public void setZoomControlsGravity(int i) {
        this.zoomControlsGravity = i;
        this.mapView.requestLayout();
    }

    public void setZoomControlsOrientation(Orientation orientation) {
        setOrientation(orientation.layoutOrientation);
        setZoomInFirst(orientation.zoomInFirst);
    }

    public void setZoomInFirst(boolean z) {
        removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        if (z) {
            addView(this.buttonZoomIn, layoutParams);
            addView(this.buttonZoomOut, layoutParams);
            return;
        }
        addView(this.buttonZoomOut, layoutParams);
        addView(this.buttonZoomIn, layoutParams);
    }

    public void setZoomInResource(int i) {
        this.buttonZoomIn.setBackgroundResource(i);
    }

    public void setZoomLevelMax(byte b) {
        if (b >= this.zoomLevelMin) {
            this.zoomLevelMax = b;
            return;
        }
        throw new IllegalArgumentException();
    }

    public void setZoomLevelMin(byte b) {
        if (b <= this.zoomLevelMax) {
            this.zoomLevelMin = b;
            return;
        }
        throw new IllegalArgumentException();
    }

    public void setZoomOutResource(int i) {
        this.buttonZoomOut.setBackgroundResource(i);
    }

    public void setZoomSpeed(long j) {
        this.buttonZoomIn.setZoomSpeed(j);
        this.buttonZoomOut.setZoomSpeed(j);
    }

    public void show() {
        fade(0, 0.0f, 1.0f);
    }

    private void showZoomControls() {
        this.zoomControlsHideHandler.removeMessages(0);
        if (getVisibility() != 0) {
            show();
        }
    }

    private void showZoomControlsWithTimeout() {
        showZoomControls();
        this.zoomControlsHideHandler.sendEmptyMessageDelayed(0, ZOOM_CONTROLS_TIMEOUT);
    }
}
