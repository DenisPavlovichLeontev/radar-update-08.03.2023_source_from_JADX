package org.osmdroid.gpkg.overlay.features;

import java.util.ArrayList;
import java.util.List;
import org.osmdroid.views.overlay.Polygon;

public class MultiPolygon {
    private List<Polygon> polygons = new ArrayList();

    public void add(Polygon polygon) {
        this.polygons.add(polygon);
    }

    public List<Polygon> getPolygons() {
        return this.polygons;
    }

    public void setPolygons(List<Polygon> list) {
        this.polygons = list;
    }

    public void setVisible(boolean z) {
        for (Polygon visible : this.polygons) {
            visible.setVisible(z);
        }
    }
}
