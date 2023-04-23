package org.osmdroid.events;

import org.osmdroid.views.MapView;

public class ScrollEvent implements MapEvent {
    protected MapView source;

    /* renamed from: x */
    protected int f557x;

    /* renamed from: y */
    protected int f558y;

    public ScrollEvent(MapView mapView, int i, int i2) {
        this.source = mapView;
        this.f557x = i;
        this.f558y = i2;
    }

    public MapView getSource() {
        return this.source;
    }

    public int getX() {
        return this.f557x;
    }

    public int getY() {
        return this.f558y;
    }

    public String toString() {
        return "ScrollEvent [source=" + this.source + ", x=" + this.f557x + ", y=" + this.f558y + "]";
    }
}
