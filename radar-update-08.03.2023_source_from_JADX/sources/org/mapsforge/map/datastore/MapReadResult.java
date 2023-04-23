package org.mapsforge.map.datastore;

import java.util.ArrayList;
import java.util.List;

public class MapReadResult {
    public boolean isWater;
    public List<PointOfInterest> pointOfInterests = new ArrayList();
    public List<Way> ways = new ArrayList();

    public void add(PoiWayBundle poiWayBundle) {
        this.pointOfInterests.addAll(poiWayBundle.pois);
        this.ways.addAll(poiWayBundle.ways);
    }

    public void add(MapReadResult mapReadResult, boolean z) {
        if (z) {
            for (PointOfInterest next : mapReadResult.pointOfInterests) {
                if (!this.pointOfInterests.contains(next)) {
                    this.pointOfInterests.add(next);
                }
            }
            for (Way next2 : mapReadResult.ways) {
                if (!this.ways.contains(next2)) {
                    this.ways.add(next2);
                }
            }
            return;
        }
        this.pointOfInterests.addAll(mapReadResult.pointOfInterests);
        this.ways.addAll(mapReadResult.ways);
    }
}
