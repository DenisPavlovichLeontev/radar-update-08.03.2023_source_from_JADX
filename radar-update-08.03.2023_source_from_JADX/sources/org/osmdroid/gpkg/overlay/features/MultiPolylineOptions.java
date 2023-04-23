package org.osmdroid.gpkg.overlay.features;

import java.util.ArrayList;
import java.util.List;
import org.osmdroid.views.overlay.Polyline;

public class MultiPolylineOptions {
    private PolylineOptions options;
    private List<Polyline> polylineOptions = new ArrayList();

    public void add(Polyline polyline) {
        this.polylineOptions.add(polyline);
    }

    public List<Polyline> getPolylineOptions() {
        return this.polylineOptions;
    }

    public PolylineOptions getOptions() {
        return this.options;
    }

    public void setOptions(PolylineOptions polylineOptions2) {
        this.options = polylineOptions2;
    }

    public void setPolylineOptions(List<Polyline> list) {
        this.polylineOptions = list;
    }
}
