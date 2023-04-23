package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.osmdroid.tileprovider.modules.CantContinueException;
import org.osmdroid.tileprovider.modules.MapTileDownloader;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.GarbageCollector;
import org.osmdroid.util.MapTileArea;
import org.osmdroid.util.MapTileAreaList;

public class MapTilePreCache {
    private final MapTileCache mCache;
    private final GarbageCollector mGC = new GarbageCollector(new Runnable() {
        public void run() {
            while (true) {
                long access$000 = MapTilePreCache.this.next();
                if (access$000 != -1) {
                    MapTilePreCache.this.search(access$000);
                } else {
                    return;
                }
            }
        }
    });
    private final List<MapTileModuleProviderBase> mProviders = new ArrayList();
    private final MapTileAreaList mTileAreas = new MapTileAreaList();
    private Iterator<Long> mTileIndices;

    public MapTilePreCache(MapTileCache mapTileCache) {
        this.mCache = mapTileCache;
    }

    public void addProvider(MapTileModuleProviderBase mapTileModuleProviderBase) {
        this.mProviders.add(mapTileModuleProviderBase);
    }

    public void fill() {
        if (!this.mGC.isRunning()) {
            refresh();
            this.mGC.mo29215gc();
        }
    }

    private void refresh() {
        MapTileArea mapTileArea;
        synchronized (this.mTileAreas) {
            int i = 0;
            for (MapTileArea next : this.mCache.getAdditionalMapTileList().getList()) {
                if (i < this.mTileAreas.getList().size()) {
                    mapTileArea = this.mTileAreas.getList().get(i);
                } else {
                    mapTileArea = new MapTileArea();
                    this.mTileAreas.getList().add(mapTileArea);
                }
                mapTileArea.set(next);
                i++;
            }
            while (i < this.mTileAreas.getList().size()) {
                this.mTileAreas.getList().remove(this.mTileAreas.getList().size() - 1);
            }
            this.mTileIndices = this.mTileAreas.iterator();
        }
    }

    /* access modifiers changed from: private */
    public long next() {
        long longValue;
        do {
            synchronized (this.mTileAreas) {
                if (!this.mTileIndices.hasNext()) {
                    return -1;
                }
                longValue = this.mTileIndices.next().longValue();
            }
        } while (this.mCache.getMapTile(longValue) != null);
        return longValue;
    }

    /* access modifiers changed from: private */
    public void search(long j) {
        for (MapTileModuleProviderBase next : this.mProviders) {
            try {
                if (next instanceof MapTileDownloader) {
                    ITileSource tileSource = ((MapTileDownloader) next).getTileSource();
                    if ((tileSource instanceof OnlineTileSourceBase) && !((OnlineTileSourceBase) tileSource).getTileSourcePolicy().acceptsPreventive()) {
                    }
                }
                Drawable loadTileIfReachable = next.getTileLoader().loadTileIfReachable(j);
                if (loadTileIfReachable != null) {
                    this.mCache.putTile(j, loadTileIfReachable);
                    return;
                }
            } catch (CantContinueException unused) {
            }
        }
    }
}
