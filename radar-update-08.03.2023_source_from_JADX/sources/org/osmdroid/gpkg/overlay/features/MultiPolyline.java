package org.osmdroid.gpkg.overlay.features;

import java.util.ArrayList;
import java.util.List;
import org.osmdroid.views.overlay.Polyline;

public class MultiPolyline {
    private List<Polyline> polylines = new ArrayList();

    public void add(Polyline polyline) {
        this.polylines.add(polyline);
    }

    public List<Polyline> getPolylines() {
        return this.polylines;
    }

    public void setPolylines(List<Polyline> list) {
        this.polylines = list;
    }

    public void setVisible(boolean z) {
        for (Polyline visible : this.polylines) {
            visible.setVisible(z);
        }
    }
}
