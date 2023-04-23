package org.osmdroid.tileprovider.tilesource;

import java.util.ArrayList;
import java.util.List;
import org.osmdroid.util.MapTileIndex;

public class TileSourceFactory {
    public static final OnlineTileSourceBase BASE_OVERLAY_NL = new XYTileSource("BaseNL", 0, 18, 256, ".png", new String[]{"https://overlay.openstreetmap.nl/basemap/"});
    public static final OnlineTileSourceBase CLOUDMADESMALLTILES = new CloudmadeTileSource("CloudMadeSmallTiles", 0, 21, 64, ".png", new String[]{"http://a.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s", "http://b.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s", "http://c.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s"});
    public static final OnlineTileSourceBase CLOUDMADESTANDARDTILES = new CloudmadeTileSource("CloudMadeStandardTiles", 0, 18, 256, ".png", new String[]{"http://a.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s", "http://b.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s", "http://c.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s"});
    public static final OnlineTileSourceBase ChartbundleENRH;
    public static final OnlineTileSourceBase ChartbundleENRL;
    public static final OnlineTileSourceBase ChartbundleWAC;
    public static final OnlineTileSourceBase DEFAULT_TILE_SOURCE;
    public static final OnlineTileSourceBase FIETS_OVERLAY_NL = new XYTileSource("Fiets", 3, 18, 256, ".png", new String[]{"https://overlay.openstreetmap.nl/openfietskaart-overlay/"}, "© OpenStreetMap contributors");
    public static final OnlineTileSourceBase HIKEBIKEMAP;
    public static final OnlineTileSourceBase MAPNIK;
    public static final OnlineTileSourceBase OPEN_SEAMAP = new XYTileSource("OpenSeaMap", 3, 18, 256, ".png", new String[]{"https://tiles.openseamap.org/seamark/"}, "OpenSeaMap");
    public static final OnlineTileSourceBase OpenTopo;
    public static final OnlineTileSourceBase PUBLIC_TRANSPORT;
    public static final OnlineTileSourceBase ROADS_OVERLAY_NL = new XYTileSource("RoadsNL", 0, 18, 256, ".png", new String[]{"https://overlay.openstreetmap.nl/roads/"}, "© OpenStreetMap contributors");
    public static final OnlineTileSourceBase USGS_SAT;
    public static final OnlineTileSourceBase USGS_TOPO;
    public static final OnlineTileSourceBase WIKIMEDIA;
    private static List<ITileSource> mTileSources;

    public static ITileSource getTileSource(String str) throws IllegalArgumentException {
        for (ITileSource next : mTileSources) {
            if (next.name().equals(str)) {
                return next;
            }
        }
        throw new IllegalArgumentException("No such tile source: " + str);
    }

    public static boolean containsTileSource(String str) {
        for (ITileSource name : mTileSources) {
            if (name.name().equals(str)) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public static ITileSource getTileSource(int i) throws IllegalArgumentException {
        for (ITileSource next : mTileSources) {
            if (next.ordinal() == i) {
                return next;
            }
        }
        throw new IllegalArgumentException("No tile source at position: " + i);
    }

    public static List<ITileSource> getTileSources() {
        return mTileSources;
    }

    public static void addTileSource(ITileSource iTileSource) {
        mTileSources.add(iTileSource);
    }

    public static int removeTileSources(String str) {
        int i = 0;
        for (int size = mTileSources.size() - 1; size >= 0; size--) {
            if (mTileSources.get(size).name().matches(str)) {
                mTileSources.remove(size);
                i++;
            }
        }
        return i;
    }

    static {
        XYTileSource xYTileSource = new XYTileSource("Mapnik", 0, 19, 256, ".png", new String[]{"https://a.tile.openstreetmap.org/", "https://b.tile.openstreetmap.org/", "https://c.tile.openstreetmap.org/"}, "© OpenStreetMap contributors", new TileSourcePolicy(2, 15));
        MAPNIK = xYTileSource;
        XYTileSource xYTileSource2 = new XYTileSource("Wikimedia", 1, 19, 256, ".png", new String[]{"https://maps.wikimedia.org/osm-intl/"}, "Wikimedia maps | Map data © OpenStreetMap contributors", new TileSourcePolicy(1, 15));
        WIKIMEDIA = xYTileSource2;
        XYTileSource xYTileSource3 = new XYTileSource("OSMPublicTransport", 0, 17, 256, ".png", new String[]{"http://openptmap.org/tiles/"}, "© OpenStreetMap contributors");
        PUBLIC_TRANSPORT = xYTileSource3;
        DEFAULT_TILE_SOURCE = xYTileSource;
        XYTileSource xYTileSource4 = new XYTileSource("HikeBikeMap", 0, 18, 256, ".png", new String[]{"https://tiles.wmflabs.org/hikebike/"});
        HIKEBIKEMAP = xYTileSource4;
        C13641 r10 = new OnlineTileSourceBase("USGS National Map Topo", 0, 15, 256, "", new String[]{"https://basemap.nationalmap.gov/arcgis/rest/services/USGSTopo/MapServer/tile/"}, "USGS") {
            public String getTileURLString(long j) {
                return getBaseUrl() + MapTileIndex.getZoom(j) + "/" + MapTileIndex.getY(j) + "/" + MapTileIndex.getX(j);
            }
        };
        USGS_TOPO = r10;
        C13652 r18 = new OnlineTileSourceBase("USGS National Map Sat", 0, 15, 256, "", new String[]{"https://basemap.nationalmap.gov/arcgis/rest/services/USGSImageryTopo/MapServer/tile/"}, "USGS") {
            public String getTileURLString(long j) {
                return getBaseUrl() + MapTileIndex.getZoom(j) + "/" + MapTileIndex.getY(j) + "/" + MapTileIndex.getX(j);
            }
        };
        USGS_SAT = r18;
        XYTileSource xYTileSource5 = new XYTileSource("ChartbundleWAC", 4, 12, 256, ".png?type=google", new String[]{"https://wms.chartbundle.com/tms/v1.0/wac/"}, "chartbundle.com");
        ChartbundleWAC = xYTileSource5;
        XYTileSource xYTileSource6 = new XYTileSource("ChartbundleENRH", 4, 12, 256, ".png?type=google", new String[]{"https://wms.chartbundle.com/tms/v1.0/enrh/", "chartbundle.com"});
        ChartbundleENRH = xYTileSource6;
        XYTileSource xYTileSource7 = new XYTileSource("ChartbundleENRL", 4, 12, 256, ".png?type=google", new String[]{"https://wms.chartbundle.com/tms/v1.0/enrl/", "chartbundle.com"});
        ChartbundleENRL = xYTileSource7;
        XYTileSource xYTileSource8 = new XYTileSource("OpenTopoMap", 0, 17, 256, ".png", new String[]{"https://a.tile.opentopomap.org/", "https://b.tile.opentopomap.org/", "https://c.tile.opentopomap.org/"}, "Kartendaten: © OpenStreetMap-Mitwirkende, SRTM | Kartendarstellung: © OpenTopoMap (CC-BY-SA)");
        OpenTopo = xYTileSource8;
        ArrayList arrayList = new ArrayList();
        mTileSources = arrayList;
        arrayList.add(xYTileSource);
        mTileSources.add(xYTileSource2);
        mTileSources.add(xYTileSource3);
        mTileSources.add(xYTileSource4);
        mTileSources.add(r10);
        mTileSources.add(r18);
        mTileSources.add(xYTileSource5);
        mTileSources.add(xYTileSource6);
        mTileSources.add(xYTileSource7);
        mTileSources.add(xYTileSource8);
    }
}
