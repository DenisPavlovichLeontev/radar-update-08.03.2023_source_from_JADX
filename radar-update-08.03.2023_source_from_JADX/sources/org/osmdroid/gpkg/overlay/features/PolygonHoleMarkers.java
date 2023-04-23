package org.osmdroid.gpkg.overlay.features;

import java.util.ArrayList;
import java.util.List;
import org.osmdroid.views.overlay.Marker;

public class PolygonHoleMarkers implements ShapeMarkers {
    private List<Marker> markers = new ArrayList();
    private final PolygonMarkers parentPolygon;

    public PolygonHoleMarkers(PolygonMarkers polygonMarkers) {
        this.parentPolygon = polygonMarkers;
    }

    public void add(Marker marker) {
        this.markers.add(marker);
    }

    public List<Marker> getMarkers() {
        return this.markers;
    }

    public void setMarkers(List<Marker> list) {
        this.markers = list;
    }

    public void setVisible(boolean z) {
        setVisibleMarkers(z);
    }

    public void setVisibleMarkers(boolean z) {
        for (Marker next : this.markers) {
            if (z) {
                next.setAlpha(1.0f);
            } else {
                next.setAlpha(0.0f);
            }
        }
    }

    public boolean isValid() {
        return this.markers.isEmpty() || this.markers.size() >= 3;
    }

    public boolean isDeleted() {
        return this.markers.isEmpty();
    }

    public void addNew(Marker marker) {
        OsmdroidShapeMarkers.addMarkerAsPolygon(marker, this.markers);
    }
}
