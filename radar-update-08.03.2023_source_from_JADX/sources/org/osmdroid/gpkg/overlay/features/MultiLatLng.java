package org.osmdroid.gpkg.overlay.features;

import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.GeoPoint;

public class MultiLatLng {
    private List<GeoPoint> latLngs = new ArrayList();
    private MarkerOptions markerOptions;

    public void add(GeoPoint geoPoint) {
        this.latLngs.add(geoPoint);
    }

    public List<GeoPoint> getLatLngs() {
        return this.latLngs;
    }

    public MarkerOptions getMarkerOptions() {
        return this.markerOptions;
    }

    public void setMarkerOptions(MarkerOptions markerOptions2) {
        this.markerOptions = markerOptions2;
    }

    public void setLatLngs(List<GeoPoint> list) {
        this.latLngs = list;
    }
}
