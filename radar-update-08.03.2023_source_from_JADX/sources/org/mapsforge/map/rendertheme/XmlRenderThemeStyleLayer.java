package org.mapsforge.map.rendertheme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XmlRenderThemeStyleLayer implements Serializable {
    private static final long serialVersionUID = 1;
    private final Set<String> categories = new LinkedHashSet();
    private final String defaultLanguage;
    private final boolean enabled;

    /* renamed from: id */
    private final String f387id;
    private final List<XmlRenderThemeStyleLayer> overlays;
    private final Map<String, String> titles = new HashMap();
    private final boolean visible;

    XmlRenderThemeStyleLayer(String str, boolean z, boolean z2, String str2) {
        this.f387id = str;
        this.visible = z;
        this.defaultLanguage = str2;
        this.enabled = z2;
        this.overlays = new ArrayList();
    }

    public void addCategory(String str) {
        this.categories.add(str);
    }

    public void addOverlay(XmlRenderThemeStyleLayer xmlRenderThemeStyleLayer) {
        this.overlays.add(xmlRenderThemeStyleLayer);
    }

    public void addTranslation(String str, String str2) {
        this.titles.put(str, str2);
    }

    public Set<String> getCategories() {
        return this.categories;
    }

    public String getId() {
        return this.f387id;
    }

    public List<XmlRenderThemeStyleLayer> getOverlays() {
        return this.overlays;
    }

    public String getTitle(String str) {
        String str2 = this.titles.get(str);
        return str2 == null ? this.titles.get(this.defaultLanguage) : str2;
    }

    public Map<String, String> getTitles() {
        return this.titles;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isVisible() {
        return this.visible;
    }
}
