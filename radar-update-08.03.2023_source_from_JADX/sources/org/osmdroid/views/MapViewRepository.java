package org.osmdroid.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import java.util.HashSet;
import java.util.Set;
import org.osmdroid.library.C1340R;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class MapViewRepository {
    private Drawable mDefaultMarkerIcon;
    private MarkerInfoWindow mDefaultMarkerInfoWindow;
    private BasicInfoWindow mDefaultPolygonInfoWindow;
    private BasicInfoWindow mDefaultPolylineInfoWindow;
    private final Set<InfoWindow> mInfoWindowList = new HashSet();
    private MapView mMapView;

    public MapViewRepository(MapView mapView) {
        this.mMapView = mapView;
    }

    public void add(InfoWindow infoWindow) {
        this.mInfoWindowList.add(infoWindow);
    }

    public void onDetach() {
        synchronized (this.mInfoWindowList) {
            for (InfoWindow onDetach : this.mInfoWindowList) {
                onDetach.onDetach();
            }
            this.mInfoWindowList.clear();
        }
        this.mMapView = null;
        this.mDefaultMarkerInfoWindow = null;
        this.mDefaultPolylineInfoWindow = null;
        this.mDefaultPolygonInfoWindow = null;
        this.mDefaultMarkerIcon = null;
    }

    public MarkerInfoWindow getDefaultMarkerInfoWindow() {
        if (this.mDefaultMarkerInfoWindow == null) {
            this.mDefaultMarkerInfoWindow = new MarkerInfoWindow(C1340R.layout.bonuspack_bubble, this.mMapView);
        }
        return this.mDefaultMarkerInfoWindow;
    }

    public BasicInfoWindow getDefaultPolylineInfoWindow() {
        if (this.mDefaultPolylineInfoWindow == null) {
            this.mDefaultPolylineInfoWindow = new BasicInfoWindow(C1340R.layout.bonuspack_bubble, this.mMapView);
        }
        return this.mDefaultPolylineInfoWindow;
    }

    public BasicInfoWindow getDefaultPolygonInfoWindow() {
        if (this.mDefaultPolygonInfoWindow == null) {
            this.mDefaultPolygonInfoWindow = new BasicInfoWindow(C1340R.layout.bonuspack_bubble, this.mMapView);
        }
        return this.mDefaultPolygonInfoWindow;
    }

    public Drawable getDefaultMarkerIcon() {
        MapView mapView;
        Context context;
        if (!(this.mDefaultMarkerIcon != null || (mapView = this.mMapView) == null || (context = mapView.getContext()) == null)) {
            this.mDefaultMarkerIcon = context.getResources().getDrawable(C1340R.C1341drawable.marker_default);
        }
        return this.mDefaultMarkerIcon;
    }
}
