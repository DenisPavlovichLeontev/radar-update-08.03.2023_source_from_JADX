package org.osmdroid.gpkg.overlay.features;

import java.util.ArrayList;
import java.util.List;
import org.osmdroid.gpkg.overlay.OsmMapShapeConverter;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

public class PolylineMarkers implements ShapeMarkers {
    private final OsmMapShapeConverter converter;
    private List<Marker> markers = new ArrayList();
    private Polyline polyline;

    public PolylineMarkers(OsmMapShapeConverter osmMapShapeConverter) {
        this.converter = osmMapShapeConverter;
    }

    public Polyline getPolyline() {
        return this.polyline;
    }

    public void setPolyline(Polyline polyline2) {
        this.polyline = polyline2;
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
        Polyline polyline2 = this.polyline;
        if (polyline2 != null) {
            polyline2.setVisible(z);
        }
        setVisibleMarkers(z);
    }

    public void setVisibleMarkers(boolean z) {
        for (Marker visible : this.markers) {
            visible.setVisible(z);
        }
    }

    public boolean isValid() {
        return this.markers.isEmpty() || this.markers.size() >= 2;
    }

    public boolean isDeleted() {
        return this.markers.isEmpty();
    }

    public void addNew(Marker marker) {
        OsmdroidShapeMarkers.addMarkerAsPolyline(marker, this.markers);
    }
}
