package org.mapsforge.map.android.util;

import java.io.File;
import java.io.FileNotFoundException;
import org.mapsforge.map.rendertheme.ExternalRenderTheme;

public class ExternalRenderThemeUsingJarResources extends ExternalRenderTheme {
    public String getRelativePathPrefix() {
        return "/assets/";
    }

    public ExternalRenderThemeUsingJarResources(File file) throws FileNotFoundException {
        super(file);
    }
}
