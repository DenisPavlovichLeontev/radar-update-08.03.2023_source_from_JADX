package mil.nga.geopackage.tiles.retriever;

public interface TileRetriever {
    GeoPackageTile getTile(int i, int i2, int i3);

    boolean hasTile(int i, int i2, int i3);
}
