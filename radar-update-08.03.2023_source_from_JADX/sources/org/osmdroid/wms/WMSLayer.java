package org.osmdroid.wms;

import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.BoundingBox;

public class WMSLayer {
    private BoundingBox bbox;
    private String description;
    private String name;
    private int pixelSize = 256;
    private List<String> srs = new ArrayList();
    private List<String> styles = new ArrayList();
    private String title;

    public List<String> getStyles() {
        return this.styles;
    }

    public void setStyles(List<String> list) {
        this.styles = list;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public BoundingBox getBbox() {
        return this.bbox;
    }

    public void setBbox(BoundingBox boundingBox) {
        this.bbox = boundingBox;
    }

    public int getPixelSize() {
        return this.pixelSize;
    }

    public void setPixelSize(int i) {
        this.pixelSize = i;
    }

    public List<String> getSrs() {
        return this.srs;
    }
}
