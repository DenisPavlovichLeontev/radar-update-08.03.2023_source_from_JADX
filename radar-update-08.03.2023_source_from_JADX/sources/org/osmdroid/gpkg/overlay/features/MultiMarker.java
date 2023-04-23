package org.osmdroid.gpkg.overlay.features;

import java.util.ArrayList;
import java.util.List;
import org.osmdroid.views.overlay.Marker;

public class MultiMarker implements ShapeMarkers {
    private List<Marker> markers = new ArrayList();

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
        for (Marker visible : this.markers) {
            visible.setVisible(z);
        }
    }

    public void addNew(Marker marker) {
        add(marker);
    }
}
