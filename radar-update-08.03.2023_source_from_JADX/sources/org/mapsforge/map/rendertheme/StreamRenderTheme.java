package org.mapsforge.map.rendertheme;

import java.io.InputStream;
import org.mapsforge.core.util.Utils;

public class StreamRenderTheme implements XmlRenderTheme {
    private final InputStream inputStream;
    private XmlRenderThemeMenuCallback menuCallback;
    private final String relativePathPrefix;

    public StreamRenderTheme(String str, InputStream inputStream2) {
        this(str, inputStream2, (XmlRenderThemeMenuCallback) null);
    }

    public StreamRenderTheme(String str, InputStream inputStream2, XmlRenderThemeMenuCallback xmlRenderThemeMenuCallback) {
        this.relativePathPrefix = str;
        this.inputStream = inputStream2;
        this.menuCallback = xmlRenderThemeMenuCallback;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StreamRenderTheme)) {
            return false;
        }
        StreamRenderTheme streamRenderTheme = (StreamRenderTheme) obj;
        return this.inputStream == streamRenderTheme.inputStream && Utils.equals(this.relativePathPrefix, streamRenderTheme.relativePathPrefix);
    }

    public XmlRenderThemeMenuCallback getMenuCallback() {
        return this.menuCallback;
    }

    public String getRelativePathPrefix() {
        return this.relativePathPrefix;
    }

    public InputStream getRenderThemeAsStream() {
        return this.inputStream;
    }

    public int hashCode() {
        InputStream inputStream2 = this.inputStream;
        int i = 0;
        int hashCode = ((inputStream2 == null ? 0 : inputStream2.hashCode()) + 31) * 31;
        String str = this.relativePathPrefix;
        if (str != null) {
            i = str.hashCode();
        }
        return hashCode + i;
    }

    public void setMenuCallback(XmlRenderThemeMenuCallback xmlRenderThemeMenuCallback) {
        this.menuCallback = xmlRenderThemeMenuCallback;
    }
}
