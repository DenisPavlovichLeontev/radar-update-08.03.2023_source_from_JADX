package org.mapsforge.map.layer.queue;

import java.io.File;
import org.mapsforge.core.model.Tile;

public class Job {
    public final boolean hasAlpha;
    private final String key;
    public final Tile tile;

    private static String composeKey(byte b, long j, long j2) {
        return String.valueOf(b) + File.separatorChar + j + File.separatorChar + j2;
    }

    public static String composeKey(String str, String str2, String str3) {
        return str + File.separatorChar + str2 + File.separatorChar + str3;
    }

    public Job(Tile tile2, boolean z) {
        if (tile2 != null) {
            this.tile = tile2;
            this.hasAlpha = z;
            this.key = composeKey(tile2.zoomLevel, (long) tile2.tileX, (long) tile2.tileY);
            return;
        }
        throw new IllegalArgumentException("tile must not be null");
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Job)) {
            return false;
        }
        Job job = (Job) obj;
        if (this.hasAlpha != job.hasAlpha || !this.tile.equals(job.tile)) {
            return false;
        }
        return true;
    }

    public String getKey() {
        return this.key;
    }

    public int hashCode() {
        return this.tile.hashCode();
    }
}
