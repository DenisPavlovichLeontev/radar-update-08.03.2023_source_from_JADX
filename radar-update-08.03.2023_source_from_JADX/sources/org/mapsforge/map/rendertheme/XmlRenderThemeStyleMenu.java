package org.mapsforge.map.rendertheme;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class XmlRenderThemeStyleMenu implements Serializable {
    private static final long serialVersionUID = 1;
    private final String defaultLanguage;
    private final String defaultValue;

    /* renamed from: id */
    private final String f388id;
    private final Map<String, XmlRenderThemeStyleLayer> layers = new LinkedHashMap();

    public XmlRenderThemeStyleMenu(String str, String str2, String str3) {
        this.defaultLanguage = str2;
        this.defaultValue = str3;
        this.f388id = str;
    }

    public XmlRenderThemeStyleLayer createLayer(String str, boolean z, boolean z2) {
        XmlRenderThemeStyleLayer xmlRenderThemeStyleLayer = new XmlRenderThemeStyleLayer(str, z, z2, this.defaultLanguage);
        this.layers.put(str, xmlRenderThemeStyleLayer);
        return xmlRenderThemeStyleLayer;
    }

    public XmlRenderThemeStyleLayer getLayer(String str) {
        return this.layers.get(str);
    }

    public Map<String, XmlRenderThemeStyleLayer> getLayers() {
        return this.layers;
    }

    public String getDefaultLanguage() {
        return this.defaultLanguage;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public String getId() {
        return this.f388id;
    }
}
