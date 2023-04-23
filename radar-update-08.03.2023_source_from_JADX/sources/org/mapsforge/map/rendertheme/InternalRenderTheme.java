package org.mapsforge.map.rendertheme;

import java.io.InputStream;

public enum InternalRenderTheme implements XmlRenderTheme {
    DEFAULT("/assets/mapsforge/default.xml"),
    OSMARENDER("/assets/mapsforge/osmarender.xml");
    
    private final String path;

    public XmlRenderThemeMenuCallback getMenuCallback() {
        return null;
    }

    public String getRelativePathPrefix() {
        return "/assets/";
    }

    public void setMenuCallback(XmlRenderThemeMenuCallback xmlRenderThemeMenuCallback) {
    }

    private InternalRenderTheme(String str) {
        this.path = str;
    }

    public InputStream getRenderThemeAsStream() {
        return getClass().getResourceAsStream(this.path);
    }
}
