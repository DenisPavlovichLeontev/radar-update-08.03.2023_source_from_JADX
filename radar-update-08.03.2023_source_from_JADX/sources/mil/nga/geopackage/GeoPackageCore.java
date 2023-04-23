package mil.nga.geopackage;

import com.j256.ormlite.dao.BaseDaoImpl;
import java.io.Closeable;
import java.util.List;
import mil.nga.geopackage.attributes.AttributesColumn;
import mil.nga.geopackage.attributes.AttributesTable;
import mil.nga.geopackage.core.contents.ContentsDao;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSfSqlDao;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSqlMmDao;
import mil.nga.geopackage.extension.ExtensionsDao;
import mil.nga.geopackage.extension.coverage.GriddedCoverageDao;
import mil.nga.geopackage.extension.coverage.GriddedTileDao;
import mil.nga.geopackage.extension.index.GeometryIndexDao;
import mil.nga.geopackage.extension.index.TableIndexDao;
import mil.nga.geopackage.extension.link.FeatureTileLinkDao;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.geopackage.features.columns.GeometryColumnsSfSqlDao;
import mil.nga.geopackage.features.columns.GeometryColumnsSqlMmDao;
import mil.nga.geopackage.features.user.FeatureColumn;
import mil.nga.geopackage.features.user.FeatureTable;
import mil.nga.geopackage.metadata.MetadataDao;
import mil.nga.geopackage.metadata.reference.MetadataReferenceDao;
import mil.nga.geopackage.p009db.GeoPackageCoreConnection;
import mil.nga.geopackage.schema.columns.DataColumnsDao;
import mil.nga.geopackage.schema.constraints.DataColumnConstraintsDao;
import mil.nga.geopackage.tiles.matrix.TileMatrixDao;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSetDao;
import mil.nga.geopackage.tiles.user.TileTable;

public interface GeoPackageCore extends Closeable {
    void close();

    AttributesTable createAttributesTable(String str, String str2, List<AttributesColumn> list);

    AttributesTable createAttributesTable(String str, List<AttributesColumn> list);

    void createAttributesTable(AttributesTable attributesTable);

    AttributesTable createAttributesTableWithId(String str, List<AttributesColumn> list);

    <T, S extends BaseDaoImpl<T, ?>> S createDao(Class<T> cls);

    boolean createDataColumnConstraintsTable();

    boolean createDataColumnsTable();

    boolean createExtensionsTable();

    void createFeatureTable(FeatureTable featureTable);

    GeometryColumns createFeatureTableWithMetadata(GeometryColumns geometryColumns, String str, List<FeatureColumn> list, BoundingBox boundingBox, long j);

    GeometryColumns createFeatureTableWithMetadata(GeometryColumns geometryColumns, String str, BoundingBox boundingBox, long j);

    GeometryColumns createFeatureTableWithMetadata(GeometryColumns geometryColumns, List<FeatureColumn> list, BoundingBox boundingBox, long j);

    GeometryColumns createFeatureTableWithMetadata(GeometryColumns geometryColumns, BoundingBox boundingBox, long j);

    GeometryColumns createFeatureTableWithMetadata(GeometryColumns geometryColumns, BoundingBox boundingBox, long j, List<FeatureColumn> list);

    boolean createFeatureTileLinkTable();

    boolean createGeometryColumnsTable();

    boolean createGeometryIndexTable();

    boolean createGriddedCoverageTable();

    boolean createGriddedTileTable();

    boolean createMetadataReferenceTable();

    boolean createMetadataTable();

    boolean createTableIndexTable();

    boolean createTileMatrixSetTable();

    boolean createTileMatrixTable();

    void createTileTable(TileTable tileTable);

    TileMatrixSet createTileTableWithMetadata(String str, BoundingBox boundingBox, long j, BoundingBox boundingBox2, long j2);

    TileMatrixSet createTileTableWithMetadata(ContentsDataType contentsDataType, String str, BoundingBox boundingBox, long j, BoundingBox boundingBox2, long j2);

    void deleteTable(String str);

    void deleteTableQuietly(String str);

    void dropTable(String str);

    void execSQL(String str);

    String getApplicationId();

    List<String> getAttributesTables();

    ContentsDao getContentsDao();

    DataColumnConstraintsDao getDataColumnConstraintsDao();

    DataColumnsDao getDataColumnsDao();

    GeoPackageCoreConnection getDatabase();

    ExtensionsDao getExtensionsDao();

    List<String> getFeatureAndTileTables();

    List<String> getFeatureTables();

    FeatureTileLinkDao getFeatureTileLinkDao();

    GeometryColumnsDao getGeometryColumnsDao();

    GeometryColumnsSfSqlDao getGeometryColumnsSfSqlDao();

    GeometryColumnsSqlMmDao getGeometryColumnsSqlMmDao();

    GeometryIndexDao getGeometryIndexDao();

    GriddedCoverageDao getGriddedCoverageDao();

    GriddedTileDao getGriddedTileDao();

    MetadataDao getMetadataDao();

    MetadataReferenceDao getMetadataReferenceDao();

    String getName();

    String getPath();

    SpatialReferenceSystemDao getSpatialReferenceSystemDao();

    SpatialReferenceSystemSfSqlDao getSpatialReferenceSystemSfSqlDao();

    SpatialReferenceSystemSqlMmDao getSpatialReferenceSystemSqlMmDao();

    TableIndexDao getTableIndexDao();

    List<String> getTables();

    List<String> getTables(ContentsDataType contentsDataType);

    TileMatrixDao getTileMatrixDao();

    TileMatrixSetDao getTileMatrixSetDao();

    List<String> getTileTables();

    int getUserVersion();

    int getUserVersionMajor();

    int getUserVersionMinor();

    int getUserVersionPatch();

    boolean isFeatureOrTileTable(String str);

    boolean isFeatureTable(String str);

    boolean isTable(String str);

    boolean isTableType(ContentsDataType contentsDataType, String str);

    boolean isTileTable(String str);

    boolean isWritable();

    void verifyWritable();
}
