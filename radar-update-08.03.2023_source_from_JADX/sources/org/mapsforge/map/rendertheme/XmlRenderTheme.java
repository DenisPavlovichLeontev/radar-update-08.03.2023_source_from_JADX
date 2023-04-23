package org.mapsforge.map.rendertheme;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface XmlRenderTheme {
    XmlRenderThemeMenuCallback getMenuCallback();

    String getRelativePathPrefix();

    InputStream getRenderThemeAsStream() throws FileNotFoundException;

    void setMenuCallback(XmlRenderThemeMenuCallback xmlRenderThemeMenuCallback);
}
