package org.mapsforge.core.model;

public class MapPosition {
    public final LatLong latLong;
    public final byte zoomLevel;

    public MapPosition(LatLong latLong2, byte b) {
        if (latLong2 == null) {
            throw new IllegalArgumentException("latLong must not be null");
        } else if (b >= 0) {
            this.latLong = latLong2;
            this.zoomLevel = b;
        } else {
            throw new IllegalArgumentException("zoomLevel must not be negative: " + b);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MapPosition)) {
            return false;
        }
        MapPosition mapPosition = (MapPosition) obj;
        return this.latLong.equals(mapPosition.latLong) && this.zoomLevel == mapPosition.zoomLevel;
    }

    public int hashCode() {
        return ((this.latLong.hashCode() + 31) * 31) + this.zoomLevel;
    }

    public String toString() {
        return "latLong=" + this.latLong + ", zoomLevel=" + this.zoomLevel;
    }
}
