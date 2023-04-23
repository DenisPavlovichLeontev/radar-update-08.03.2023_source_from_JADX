package org.mapsforge.map.datastore;

import java.util.List;

public class PoiWayBundle {
    final List<PointOfInterest> pois;
    final List<Way> ways;

    public PoiWayBundle(List<PointOfInterest> list, List<Way> list2) {
        this.pois = list;
        this.ways = list2;
    }
}
