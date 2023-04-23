package org.osmdroid.mapsforge;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.renderer.DirectRenderer;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.IFilesystemCache;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileFilesystemProvider;
import org.osmdroid.tileprovider.modules.SqlTileWriter;
import org.osmdroid.util.MapTileIndex;

public class MapsForgeTileProvider extends MapTileProviderArray {
    IFilesystemCache tileWriter;

    public MapsForgeTileProvider(IRegisterReceiver iRegisterReceiver, MapsForgeTileSource mapsForgeTileSource, IFilesystemCache iFilesystemCache) {
        super(mapsForgeTileSource, iRegisterReceiver);
        this.mTileProviderList.add(new MapTileFilesystemProvider(iRegisterReceiver, mapsForgeTileSource));
        this.mTileProviderList.add(new MapTileFileArchiveProvider(iRegisterReceiver, mapsForgeTileSource));
        if (iFilesystemCache != null) {
            this.tileWriter = iFilesystemCache;
        } else {
            this.tileWriter = new SqlTileWriter();
        }
        this.mTileProviderList.add(new MapsForgeTileModuleProvider(iRegisterReceiver, (MapsForgeTileSource) getTileSource(), this.tileWriter));
        mapsForgeTileSource.addTileRefresher(new DirectRenderer.TileRefresher() {
            public void refresh(Tile tile) {
                MapsForgeTileProvider.this.expireInMemoryCache(MapTileIndex.getTileIndex(tile.zoomLevel, tile.tileX, tile.tileY));
            }
        });
    }

    public void detach() {
        IFilesystemCache iFilesystemCache = this.tileWriter;
        if (iFilesystemCache != null) {
            iFilesystemCache.onDetach();
        }
        this.tileWriter = null;
        super.detach();
    }
}
