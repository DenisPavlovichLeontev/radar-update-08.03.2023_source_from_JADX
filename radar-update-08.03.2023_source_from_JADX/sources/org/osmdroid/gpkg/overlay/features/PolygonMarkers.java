package org.osmdroid.gpkg.overlay.features;

import java.util.ArrayList;
import java.util.List;
import org.osmdroid.gpkg.overlay.OsmMapShapeConverter;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;

public class PolygonMarkers implements ShapeWithChildrenMarkers {
    private final OsmMapShapeConverter converter;
    private List<PolygonHoleMarkers> holes = new ArrayList();
    private List<Marker> markers = new ArrayList();
    private Polygon polygon;

    public PolygonMarkers(OsmMapShapeConverter osmMapShapeConverter) {
        this.converter = osmMapShapeConverter;
    }

    public Polygon getPolygon() {
        return this.polygon;
    }

    public void setPolygon(Polygon polygon2) {
        this.polygon = polygon2;
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

    public void addHole(PolygonHoleMarkers polygonHoleMarkers) {
        this.holes.add(polygonHoleMarkers);
    }

    public List<PolygonHoleMarkers> getHoles() {
        return this.holes;
    }

    public void setHoles(List<PolygonHoleMarkers> list) {
        this.holes = list;
    }

    public void setVisible(boolean z) {
        Polygon polygon2 = this.polygon;
        if (polygon2 != null) {
            polygon2.setVisible(z);
        }
        for (Marker visible : this.markers) {
            visible.setVisible(z);
        }
        for (PolygonHoleMarkers visible2 : this.holes) {
            visible2.setVisible(z);
        }
    }

    public void setVisibleMarkers(boolean z) {
        for (Marker visible : this.markers) {
            visible.setVisible(z);
        }
        for (PolygonHoleMarkers visibleMarkers : this.holes) {
            visibleMarkers.setVisibleMarkers(z);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x001d A[LOOP:0: B:8:0x001d->B:11:0x002d, LOOP_START, PHI: r0 
      PHI: (r0v4 boolean) = (r0v2 boolean), (r0v7 boolean) binds: [B:7:0x0017, B:11:0x002d] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isValid() {
        /*
            r3 = this;
            java.util.List<org.osmdroid.views.overlay.Marker> r0 = r3.markers
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L_0x0014
            java.util.List<org.osmdroid.views.overlay.Marker> r0 = r3.markers
            int r0 = r0.size()
            r1 = 3
            if (r0 < r1) goto L_0x0012
            goto L_0x0014
        L_0x0012:
            r0 = 0
            goto L_0x0015
        L_0x0014:
            r0 = 1
        L_0x0015:
            if (r0 == 0) goto L_0x002f
            java.util.List<org.osmdroid.gpkg.overlay.features.PolygonHoleMarkers> r1 = r3.holes
            java.util.Iterator r1 = r1.iterator()
        L_0x001d:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x002f
            java.lang.Object r0 = r1.next()
            org.osmdroid.gpkg.overlay.features.PolygonHoleMarkers r0 = (org.osmdroid.gpkg.overlay.features.PolygonHoleMarkers) r0
            boolean r0 = r0.isValid()
            if (r0 != 0) goto L_0x001d
        L_0x002f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.gpkg.overlay.features.PolygonMarkers.isValid():boolean");
    }

    public boolean isDeleted() {
        return this.markers.isEmpty();
    }

    public void addNew(Marker marker) {
        OsmdroidShapeMarkers.addMarkerAsPolygon(marker, this.markers);
    }

    public ShapeMarkers createChild() {
        PolygonHoleMarkers polygonHoleMarkers = new PolygonHoleMarkers(this);
        this.holes.add(polygonHoleMarkers);
        return polygonHoleMarkers;
    }
}
