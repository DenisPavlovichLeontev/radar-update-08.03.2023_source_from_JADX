package org.mapsforge.map.layer.cache;

import java.io.File;
import java.io.FilenameFilter;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;

final class ImageFileNameFilter implements FilenameFilter {
    static final FilenameFilter INSTANCE = new ImageFileNameFilter();

    private ImageFileNameFilter() {
    }

    public boolean accept(File file, String str) {
        return str.endsWith(OpenStreetMapTileProviderConstants.TILE_PATH_EXTENSION);
    }
}
