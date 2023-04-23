package org.mapsforge.map.datastore;

import java.util.ArrayList;
import java.util.List;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Tile;

public class MultiMapDataStore extends MapDataStore {
    private BoundingBox boundingBox;
    private final DataPolicy dataPolicy;
    private final List<MapDataStore> mapDatabases = new ArrayList();
    private LatLong startPosition;
    private byte startZoomLevel;

    public enum DataPolicy {
        RETURN_FIRST,
        RETURN_ALL,
        DEDUPLICATE
    }

    public MultiMapDataStore(DataPolicy dataPolicy2) {
        this.dataPolicy = dataPolicy2;
    }

    public void addMapDataStore(MapDataStore mapDataStore, boolean z, boolean z2) {
        if (!this.mapDatabases.contains(mapDataStore)) {
            this.mapDatabases.add(mapDataStore);
            if (z) {
                this.startZoomLevel = mapDataStore.startZoomLevel().byteValue();
            }
            if (z2) {
                this.startPosition = mapDataStore.startPosition();
            }
            BoundingBox boundingBox2 = this.boundingBox;
            if (boundingBox2 == null) {
                this.boundingBox = mapDataStore.boundingBox();
            } else {
                this.boundingBox = boundingBox2.extendBoundingBox(mapDataStore.boundingBox());
            }
        } else {
            throw new IllegalArgumentException("Duplicate map database");
        }
    }

    public BoundingBox boundingBox() {
        return this.boundingBox;
    }

    public void close() {
        for (MapDataStore close : this.mapDatabases) {
            close.close();
        }
    }

    /* renamed from: org.mapsforge.map.datastore.MultiMapDataStore$1 */
    static /* synthetic */ class C13201 {

        /* renamed from: $SwitchMap$org$mapsforge$map$datastore$MultiMapDataStore$DataPolicy */
        static final /* synthetic */ int[] f384xc5b59815;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                org.mapsforge.map.datastore.MultiMapDataStore$DataPolicy[] r0 = org.mapsforge.map.datastore.MultiMapDataStore.DataPolicy.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f384xc5b59815 = r0
                org.mapsforge.map.datastore.MultiMapDataStore$DataPolicy r1 = org.mapsforge.map.datastore.MultiMapDataStore.DataPolicy.RETURN_FIRST     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f384xc5b59815     // Catch:{ NoSuchFieldError -> 0x001d }
                org.mapsforge.map.datastore.MultiMapDataStore$DataPolicy r1 = org.mapsforge.map.datastore.MultiMapDataStore.DataPolicy.RETURN_ALL     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f384xc5b59815     // Catch:{ NoSuchFieldError -> 0x0028 }
                org.mapsforge.map.datastore.MultiMapDataStore$DataPolicy r1 = org.mapsforge.map.datastore.MultiMapDataStore.DataPolicy.DEDUPLICATE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mapsforge.map.datastore.MultiMapDataStore.C13201.<clinit>():void");
        }
    }

    public long getDataTimestamp(Tile tile) {
        int i = C13201.f384xc5b59815[this.dataPolicy.ordinal()];
        long j = 0;
        if (i == 1) {
            for (MapDataStore next : this.mapDatabases) {
                if (next.supportsTile(tile)) {
                    return next.getDataTimestamp(tile);
                }
            }
            return 0;
        } else if (i == 2 || i == 3) {
            for (MapDataStore next2 : this.mapDatabases) {
                if (next2.supportsTile(tile)) {
                    j = Math.max(j, next2.getDataTimestamp(tile));
                }
            }
            return j;
        } else {
            throw new IllegalStateException("Invalid data policy for multi map database");
        }
    }

    public MapReadResult readLabels(Tile tile) {
        int i = C13201.f384xc5b59815[this.dataPolicy.ordinal()];
        if (i == 1) {
            for (MapDataStore next : this.mapDatabases) {
                if (next.supportsTile(tile)) {
                    return next.readLabels(tile);
                }
            }
            return null;
        } else if (i == 2) {
            return readLabels(tile, false);
        } else {
            if (i == 3) {
                return readLabels(tile, true);
            }
            throw new IllegalStateException("Invalid data policy for multi map database");
        }
    }

    private MapReadResult readLabels(Tile tile, boolean z) {
        MapReadResult readLabels;
        MapReadResult mapReadResult = new MapReadResult();
        for (MapDataStore next : this.mapDatabases) {
            if (next.supportsTile(tile) && (readLabels = next.readLabels(tile)) != null) {
                mapReadResult.isWater &= readLabels.isWater;
                mapReadResult.add(readLabels, z);
            }
        }
        return mapReadResult;
    }

    public MapReadResult readLabels(Tile tile, Tile tile2) {
        int i = C13201.f384xc5b59815[this.dataPolicy.ordinal()];
        if (i == 1) {
            for (MapDataStore next : this.mapDatabases) {
                if (next.supportsTile(tile)) {
                    return next.readLabels(tile, tile2);
                }
            }
            return null;
        } else if (i == 2) {
            return readLabels(tile, tile2, false);
        } else {
            if (i == 3) {
                return readLabels(tile, tile2, true);
            }
            throw new IllegalStateException("Invalid data policy for multi map database");
        }
    }

    private MapReadResult readLabels(Tile tile, Tile tile2, boolean z) {
        MapReadResult readLabels;
        MapReadResult mapReadResult = new MapReadResult();
        for (MapDataStore next : this.mapDatabases) {
            if (next.supportsTile(tile) && (readLabels = next.readLabels(tile, tile2)) != null) {
                mapReadResult.isWater &= readLabels.isWater;
                mapReadResult.add(readLabels, z);
            }
        }
        return mapReadResult;
    }

    public MapReadResult readMapData(Tile tile) {
        int i = C13201.f384xc5b59815[this.dataPolicy.ordinal()];
        if (i == 1) {
            for (MapDataStore next : this.mapDatabases) {
                if (next.supportsTile(tile)) {
                    return next.readMapData(tile);
                }
            }
            return null;
        } else if (i == 2) {
            return readMapData(tile, false);
        } else {
            if (i == 3) {
                return readMapData(tile, true);
            }
            throw new IllegalStateException("Invalid data policy for multi map database");
        }
    }

    private MapReadResult readMapData(Tile tile, boolean z) {
        MapReadResult readMapData;
        MapReadResult mapReadResult = new MapReadResult();
        for (MapDataStore next : this.mapDatabases) {
            if (next.supportsTile(tile) && (readMapData = next.readMapData(tile)) != null) {
                mapReadResult.isWater &= readMapData.isWater;
                mapReadResult.add(readMapData, z);
            }
        }
        return mapReadResult;
    }

    public MapReadResult readMapData(Tile tile, Tile tile2) {
        int i = C13201.f384xc5b59815[this.dataPolicy.ordinal()];
        if (i == 1) {
            for (MapDataStore next : this.mapDatabases) {
                if (next.supportsTile(tile)) {
                    return next.readMapData(tile, tile2);
                }
            }
            return null;
        } else if (i == 2) {
            return readMapData(tile, tile2, false);
        } else {
            if (i == 3) {
                return readMapData(tile, tile2, true);
            }
            throw new IllegalStateException("Invalid data policy for multi map database");
        }
    }

    private MapReadResult readMapData(Tile tile, Tile tile2, boolean z) {
        MapReadResult readMapData;
        MapReadResult mapReadResult = new MapReadResult();
        for (MapDataStore next : this.mapDatabases) {
            if (next.supportsTile(tile) && (readMapData = next.readMapData(tile, tile2)) != null) {
                mapReadResult.isWater &= readMapData.isWater;
                mapReadResult.add(readMapData, z);
            }
        }
        return mapReadResult;
    }

    public MapReadResult readPoiData(Tile tile) {
        int i = C13201.f384xc5b59815[this.dataPolicy.ordinal()];
        if (i == 1) {
            for (MapDataStore next : this.mapDatabases) {
                if (next.supportsTile(tile)) {
                    return next.readPoiData(tile);
                }
            }
            return null;
        } else if (i == 2) {
            return readPoiData(tile, false);
        } else {
            if (i == 3) {
                return readPoiData(tile, true);
            }
            throw new IllegalStateException("Invalid data policy for multi map database");
        }
    }

    private MapReadResult readPoiData(Tile tile, boolean z) {
        MapReadResult readPoiData;
        MapReadResult mapReadResult = new MapReadResult();
        for (MapDataStore next : this.mapDatabases) {
            if (next.supportsTile(tile) && (readPoiData = next.readPoiData(tile)) != null) {
                mapReadResult.isWater &= readPoiData.isWater;
                mapReadResult.add(readPoiData, z);
            }
        }
        return mapReadResult;
    }

    public MapReadResult readPoiData(Tile tile, Tile tile2) {
        int i = C13201.f384xc5b59815[this.dataPolicy.ordinal()];
        if (i == 1) {
            for (MapDataStore next : this.mapDatabases) {
                if (next.supportsTile(tile)) {
                    return next.readPoiData(tile, tile2);
                }
            }
            return null;
        } else if (i == 2) {
            return readPoiData(tile, tile2, false);
        } else {
            if (i == 3) {
                return readPoiData(tile, tile2, true);
            }
            throw new IllegalStateException("Invalid data policy for multi map database");
        }
    }

    private MapReadResult readPoiData(Tile tile, Tile tile2, boolean z) {
        MapReadResult readPoiData;
        MapReadResult mapReadResult = new MapReadResult();
        for (MapDataStore next : this.mapDatabases) {
            if (next.supportsTile(tile) && (readPoiData = next.readPoiData(tile, tile2)) != null) {
                mapReadResult.isWater &= readPoiData.isWater;
                mapReadResult.add(readPoiData, z);
            }
        }
        return mapReadResult;
    }

    public void setStartPosition(LatLong latLong) {
        this.startPosition = latLong;
    }

    public void setStartZoomLevel(byte b) {
        this.startZoomLevel = b;
    }

    public LatLong startPosition() {
        LatLong latLong = this.startPosition;
        if (latLong != null) {
            return latLong;
        }
        BoundingBox boundingBox2 = this.boundingBox;
        if (boundingBox2 != null) {
            return boundingBox2.getCenterPoint();
        }
        return null;
    }

    public Byte startZoomLevel() {
        return Byte.valueOf(this.startZoomLevel);
    }

    public boolean supportsTile(Tile tile) {
        for (MapDataStore supportsTile : this.mapDatabases) {
            if (supportsTile.supportsTile(tile)) {
                return true;
            }
        }
        return false;
    }
}
