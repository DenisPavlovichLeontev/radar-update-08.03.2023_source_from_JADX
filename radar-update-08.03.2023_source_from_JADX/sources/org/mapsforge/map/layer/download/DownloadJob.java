package org.mapsforge.map.layer.download;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.download.tilesource.TileSource;
import org.mapsforge.map.layer.queue.Job;

public class DownloadJob extends Job {
    public final TileSource tileSource;

    public DownloadJob(Tile tile, TileSource tileSource2) {
        super(tile, tileSource2.hasAlpha());
        this.tileSource = tileSource2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (super.equals(obj) && (obj instanceof DownloadJob)) {
            return this.tileSource.equals(((DownloadJob) obj).tileSource);
        }
        return false;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + this.tileSource.hashCode();
    }
}
