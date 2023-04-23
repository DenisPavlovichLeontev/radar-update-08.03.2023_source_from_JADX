package org.osmdroid.gpkg.overlay.features;

public class PolylineOptions {
    private int color;
    boolean geodesic = false;
    private String subtitle;
    private String title;
    private float width;

    public boolean isGeodesic() {
        return this.geodesic;
    }

    public void setGeodesic(boolean z) {
        this.geodesic = z;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(String str) {
        this.subtitle = str;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float f) {
        this.width = f;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i) {
        this.color = i;
    }
}
