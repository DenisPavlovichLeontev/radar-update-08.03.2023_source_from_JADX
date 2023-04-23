package org.osmdroid.wms;

import java.util.ArrayList;
import java.util.List;

public class WMSEndpoint {
    private String baseurl;
    private String description;
    private List<WMSLayer> layers = new ArrayList();
    private String name;
    private String title;
    private String wmsVersion = "1.1.0";

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getWmsVersion() {
        return this.wmsVersion;
    }

    public void setWmsVersion(String str) {
        this.wmsVersion = str;
    }

    public String getBaseurl() {
        return this.baseurl;
    }

    public void setBaseurl(String str) {
        this.baseurl = str;
    }

    public List<WMSLayer> getLayers() {
        return this.layers;
    }

    public void setLayers(List<WMSLayer> list) {
        this.layers = list;
    }
}
