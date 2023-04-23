package org.osmdroid.gpkg.overlay.features;

import java.util.List;
import org.osmdroid.views.overlay.Marker;

public interface ShapeMarkers {
    void addNew(Marker marker);

    List<Marker> getMarkers();

    void setVisible(boolean z);

    void setVisibleMarkers(boolean z);
}
