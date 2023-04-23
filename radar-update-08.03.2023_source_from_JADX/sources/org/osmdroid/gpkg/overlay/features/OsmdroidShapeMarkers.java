package org.osmdroid.gpkg.overlay.features;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class OsmdroidShapeMarkers {
    private OsmDroidMapShape shape;
    private Map<String, ShapeMarkers> shapeMarkersMap = new HashMap();

    public void add(Marker marker, ShapeMarkers shapeMarkers) {
        add(marker.getId(), shapeMarkers);
    }

    public void add(String str, ShapeMarkers shapeMarkers) {
        this.shapeMarkersMap.put(str, shapeMarkers);
    }

    public void add(ShapeMarkers shapeMarkers) {
        for (Marker add : shapeMarkers.getMarkers()) {
            add(add, shapeMarkers);
        }
    }

    public void add(Marker marker) {
        add(marker, (ShapeMarkers) null);
    }

    public void add(List<Marker> list) {
        for (Marker add : list) {
            add(add);
        }
    }

    public void add(OsmdroidShapeMarkers osmdroidShapeMarkers) {
        this.shapeMarkersMap.putAll(osmdroidShapeMarkers.shapeMarkersMap);
    }

    public OsmDroidMapShape getShape() {
        return this.shape;
    }

    public void setShape(OsmDroidMapShape osmDroidMapShape) {
        this.shape = osmDroidMapShape;
    }

    public Map<String, ShapeMarkers> getShapeMarkersMap() {
        return this.shapeMarkersMap;
    }

    public boolean contains(Marker marker) {
        return contains(marker.getId());
    }

    public boolean contains(String str) {
        return this.shapeMarkersMap.containsKey(str);
    }

    public ShapeMarkers getShapeMarkers(Marker marker) {
        return getShapeMarkers(marker.getId());
    }

    public ShapeMarkers getShapeMarkers(String str) {
        return this.shapeMarkersMap.get(str);
    }

    public boolean isValid() {
        OsmDroidMapShape osmDroidMapShape = this.shape;
        if (osmDroidMapShape != null) {
            return osmDroidMapShape.isValid();
        }
        return true;
    }

    public static void addMarkerAsPolygon(Marker marker, List<Marker> list) {
        GeoPoint position = marker.getPosition();
        int size = list.size();
        if (list.size() > 2) {
            int size2 = list.size();
            double[] dArr = new double[size2];
            dArr[0] = SphericalUtil.computeDistanceBetween(position, list.get(0).getPosition());
            int i = 0;
            for (int i2 = 1; i2 < list.size(); i2++) {
                double computeDistanceBetween = SphericalUtil.computeDistanceBetween(position, list.get(i2).getPosition());
                dArr[i2] = computeDistanceBetween;
                if (computeDistanceBetween < dArr[i]) {
                    i = i2;
                }
            }
            int i3 = i > 0 ? i - 1 : size2 - 1;
            size = i < size2 - 1 ? i + 1 : 0;
            if (dArr[i3] <= dArr[size]) {
                size = i;
            }
        }
        list.add(size, marker);
    }

    public static void addMarkerAsPolyline(Marker marker, List<Marker> list) {
        GeoPoint position = marker.getPosition();
        int size = list.size();
        if (list.size() > 1) {
            int size2 = list.size();
            double[] dArr = new double[size2];
            int i = 0;
            dArr[0] = SphericalUtil.computeDistanceBetween(position, list.get(0).getPosition());
            for (int i2 = 1; i2 < list.size(); i2++) {
                double computeDistanceBetween = SphericalUtil.computeDistanceBetween(position, list.get(i2).getPosition());
                dArr[i2] = computeDistanceBetween;
                if (computeDistanceBetween < dArr[i]) {
                    i = i2;
                }
            }
            Integer num = null;
            Integer valueOf = i > 0 ? Integer.valueOf(i - 1) : null;
            if (i < size2 - 1) {
                num = Integer.valueOf(i + 1);
            }
            if (valueOf == null || num == null) {
                if (valueOf == null ? dArr[num.intValue()] < SphericalUtil.computeDistanceBetween(list.get(num.intValue()).getPosition(), list.get(i).getPosition()) : dArr[valueOf.intValue()] >= SphericalUtil.computeDistanceBetween(list.get(valueOf.intValue()).getPosition(), list.get(i).getPosition())) {
                    size = i + 1;
                }
            } else if (dArr[valueOf.intValue()] > dArr[num.intValue()]) {
                size = num.intValue();
            }
            size = i;
        }
        list.add(size, marker);
    }

    public void setVisible(boolean z) {
        setVisibleMarkers(z);
    }

    public void setVisibleMarkers(boolean z) {
        for (ShapeMarkers visibleMarkers : this.shapeMarkersMap.values()) {
            visibleMarkers.setVisibleMarkers(z);
        }
    }

    public int size() {
        return this.shapeMarkersMap.size();
    }

    public boolean isEmpty() {
        return this.shapeMarkersMap.isEmpty();
    }
}
