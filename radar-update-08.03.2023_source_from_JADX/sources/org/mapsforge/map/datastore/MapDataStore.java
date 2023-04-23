package org.mapsforge.map.datastore;

import java.util.List;
import java.util.Locale;
import mil.nga.geopackage.extension.Extensions;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Tag;
import org.mapsforge.core.model.Tile;

public abstract class MapDataStore {
    protected String preferredLanguage;

    public abstract BoundingBox boundingBox();

    public abstract void close();

    public abstract long getDataTimestamp(Tile tile);

    public abstract MapReadResult readMapData(Tile tile);

    public abstract MapReadResult readPoiData(Tile tile);

    public abstract LatLong startPosition();

    public abstract Byte startZoomLevel();

    public abstract boolean supportsTile(Tile tile);

    public boolean wayAsLabelTagFilter(List<Tag> list) {
        return false;
    }

    public static String extract(String str, String str2) {
        String str3 = null;
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        String[] split = str.split("\r");
        if (str2 == null || str2.trim().isEmpty()) {
            return split[0];
        }
        for (int i = 1; i < split.length; i++) {
            String[] split2 = split[i].split("\b");
            if (split2.length == 2) {
                if (split2[0].equalsIgnoreCase(str2)) {
                    return split2[1];
                }
                if (str3 == null && !split2[0].contains("-") && ((str2.contains("-") || str2.contains(Extensions.EXTENSION_NAME_DIVIDER)) && str2.toLowerCase(Locale.ENGLISH).startsWith(split2[0].toLowerCase(Locale.ENGLISH)))) {
                    str3 = split2[1];
                }
            }
        }
        if (str3 != null) {
            return str3;
        }
        return split[0];
    }

    public MapDataStore() {
        this((String) null);
    }

    public MapDataStore(String str) {
        this.preferredLanguage = str;
    }

    /* access modifiers changed from: protected */
    public String extractLocalized(String str) {
        return extract(str, this.preferredLanguage);
    }

    public MapReadResult readLabels(Tile tile) {
        return readMapData(tile);
    }

    public MapReadResult readLabels(Tile tile, Tile tile2) {
        if (tile.tileX > tile2.tileX || tile.tileY > tile2.tileY) {
            new IllegalArgumentException("upperLeft tile must be above and left of lowerRight tile");
        }
        MapReadResult mapReadResult = new MapReadResult();
        for (int i = tile.tileX; i <= tile2.tileX; i++) {
            for (int i2 = tile.tileY; i2 <= tile2.tileY; i2++) {
                mapReadResult.add(readLabels(new Tile(i, i2, tile.zoomLevel, tile.tileSize)), false);
            }
        }
        return mapReadResult;
    }

    public MapReadResult readMapData(Tile tile, Tile tile2) {
        if (tile.tileX > tile2.tileX || tile.tileY > tile2.tileY) {
            new IllegalArgumentException("upperLeft tile must be above and left of lowerRight tile");
        }
        MapReadResult mapReadResult = new MapReadResult();
        for (int i = tile.tileX; i <= tile2.tileX; i++) {
            for (int i2 = tile.tileY; i2 <= tile2.tileY; i2++) {
                mapReadResult.add(readMapData(new Tile(i, i2, tile.zoomLevel, tile.tileSize)), false);
            }
        }
        return mapReadResult;
    }

    public MapReadResult readPoiData(Tile tile, Tile tile2) {
        if (tile.tileX > tile2.tileX || tile.tileY > tile2.tileY) {
            new IllegalArgumentException("upperLeft tile must be above and left of lowerRight tile");
        }
        MapReadResult mapReadResult = new MapReadResult();
        for (int i = tile.tileX; i <= tile2.tileX; i++) {
            for (int i2 = tile.tileY; i2 <= tile2.tileY; i2++) {
                mapReadResult.add(readPoiData(new Tile(i, i2, tile.zoomLevel, tile.tileSize)), false);
            }
        }
        return mapReadResult;
    }
}
