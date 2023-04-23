package org.osmdroid.views.overlay.infowindow;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.drawing.MapSnapshot;

public abstract class InfoWindow {
    protected boolean mIsVisible = false;
    protected MapView mMapView;
    private int mOffsetX;
    private int mOffsetY;
    private GeoPoint mPosition;
    protected Object mRelatedObject;
    protected View mView;

    public abstract void onClose();

    public abstract void onOpen(Object obj);

    public InfoWindow(int i, MapView mapView) {
        this.mMapView = mapView;
        mapView.getRepository().add(this);
        View inflate = ((LayoutInflater) mapView.getContext().getSystemService("layout_inflater")).inflate(i, (ViewGroup) mapView.getParent(), false);
        this.mView = inflate;
        inflate.setTag(this);
    }

    public InfoWindow(View view, MapView mapView) {
        this.mMapView = mapView;
        this.mView = view;
        view.setTag(this);
    }

    public void setRelatedObject(Object obj) {
        this.mRelatedObject = obj;
    }

    public Object getRelatedObject() {
        return this.mRelatedObject;
    }

    public MapView getMapView() {
        return this.mMapView;
    }

    public View getView() {
        return this.mView;
    }

    public void open(Object obj, GeoPoint geoPoint, int i, int i2) {
        View view;
        close();
        this.mRelatedObject = obj;
        this.mPosition = geoPoint;
        this.mOffsetX = i;
        this.mOffsetY = i2;
        onOpen(obj);
        MapView.LayoutParams layoutParams = new MapView.LayoutParams(-2, -2, this.mPosition, 8, this.mOffsetX, this.mOffsetY);
        MapView mapView = this.mMapView;
        if (mapView == null || (view = this.mView) == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Error trapped, InfoWindow.open mMapView: ");
            String str = "null";
            sb.append(this.mMapView == null ? str : "ok");
            sb.append(" mView: ");
            if (this.mView != null) {
                str = "ok";
            }
            sb.append(str);
            Log.w(IMapView.LOGTAG, sb.toString());
            return;
        }
        mapView.addView(view, layoutParams);
        this.mIsVisible = true;
    }

    public void draw() {
        if (this.mIsVisible) {
            try {
                this.mMapView.updateViewLayout(this.mView, new MapView.LayoutParams(-2, -2, this.mPosition, 8, this.mOffsetX, this.mOffsetY));
            } catch (Exception e) {
                if (MapSnapshot.isUIThread()) {
                    throw e;
                }
            }
        }
    }

    public void close() {
        if (this.mIsVisible) {
            this.mIsVisible = false;
            ((ViewGroup) this.mView.getParent()).removeView(this.mView);
            onClose();
        }
    }

    public void onDetach() {
        close();
        View view = this.mView;
        if (view != null) {
            view.setTag((Object) null);
        }
        this.mView = null;
        this.mMapView = null;
        if (Configuration.getInstance().isDebugMode()) {
            Log.d(IMapView.LOGTAG, "Marked detached");
        }
    }

    public boolean isOpen() {
        return this.mIsVisible;
    }

    public static void closeAllInfoWindowsOn(MapView mapView) {
        Iterator<InfoWindow> it = getOpenedInfoWindowsOn(mapView).iterator();
        while (it.hasNext()) {
            it.next().close();
        }
    }

    public static ArrayList<InfoWindow> getOpenedInfoWindowsOn(MapView mapView) {
        int childCount = mapView.getChildCount();
        ArrayList<InfoWindow> arrayList = new ArrayList<>(childCount);
        for (int i = 0; i < childCount; i++) {
            Object tag = mapView.getChildAt(i).getTag();
            if (tag != null && (tag instanceof InfoWindow)) {
                arrayList.add((InfoWindow) tag);
            }
        }
        return arrayList;
    }
}
