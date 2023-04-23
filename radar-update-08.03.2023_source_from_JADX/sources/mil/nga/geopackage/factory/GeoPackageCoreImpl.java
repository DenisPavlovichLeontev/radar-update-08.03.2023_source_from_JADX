package mil.nga.geopackage.factory;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.DaoManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.attributes.AttributesColumn;
import mil.nga.geopackage.attributes.AttributesTable;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDao;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSfSql;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSfSqlDao;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSqlMm;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemSqlMmDao;
import mil.nga.geopackage.extension.CrsWktExtension;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.ExtensionsDao;
import mil.nga.geopackage.extension.GeoPackageExtensions;
import mil.nga.geopackage.extension.MetadataExtension;
import mil.nga.geopackage.extension.SchemaExtension;
import mil.nga.geopackage.extension.coverage.GriddedCoverage;
import mil.nga.geopackage.extension.coverage.GriddedCoverageDao;
import mil.nga.geopackage.extension.coverage.GriddedTile;
import mil.nga.geopackage.extension.coverage.GriddedTileDao;
import mil.nga.geopackage.extension.index.GeometryIndex;
import mil.nga.geopackage.extension.index.GeometryIndexDao;
import mil.nga.geopackage.extension.index.TableIndex;
import mil.nga.geopackage.extension.index.TableIndexDao;
import mil.nga.geopackage.extension.link.FeatureTileLink;
import mil.nga.geopackage.extension.link.FeatureTileLinkDao;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.geopackage.features.columns.GeometryColumnsSfSql;
import mil.nga.geopackage.features.columns.GeometryColumnsSfSqlDao;
import mil.nga.geopackage.features.columns.GeometryColumnsSqlMm;
import mil.nga.geopackage.features.columns.GeometryColumnsSqlMmDao;
import mil.nga.geopackage.features.user.FeatureColumn;
import mil.nga.geopackage.features.user.FeatureTable;
import mil.nga.geopackage.metadata.Metadata;
import mil.nga.geopackage.metadata.MetadataDao;
import mil.nga.geopackage.metadata.reference.MetadataReference;
import mil.nga.geopackage.metadata.reference.MetadataReferenceDao;
import mil.nga.geopackage.p009db.GeoPackageCoreConnection;
import mil.nga.geopackage.p009db.GeoPackageTableCreator;
import mil.nga.geopackage.schema.columns.DataColumns;
import mil.nga.geopackage.schema.columns.DataColumnsDao;
import mil.nga.geopackage.schema.constraints.DataColumnConstraints;
import mil.nga.geopackage.schema.constraints.DataColumnConstraintsDao;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrix.TileMatrixDao;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSetDao;
import mil.nga.geopackage.tiles.user.TileTable;

public abstract class GeoPackageCoreImpl implements GeoPackageCore {
    private final GeoPackageCoreConnection database;
    private final String name;
    private final String path;
    private final GeoPackageTableCreator tableCreator;
    protected final boolean writable;

    protected GeoPackageCoreImpl(String str, String str2, GeoPackageCoreConnection geoPackageCoreConnection, GeoPackageTableCreator geoPackageTableCreator, boolean z) {
        this.name = str;
        this.path = str2;
        this.database = geoPackageCoreConnection;
        this.tableCreator = geoPackageTableCreator;
        this.writable = z;
    }

    public void close() {
        this.database.close();
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public GeoPackageCoreConnection getDatabase() {
        return this.database;
    }

    public boolean isWritable() {
        return this.writable;
    }

    public String getApplicationId() {
        return this.database.getApplicationId();
    }

    public int getUserVersion() {
        return this.database.getUserVersion();
    }

    public int getUserVersionMajor() {
        return getUserVersion() / 10000;
    }

    public int getUserVersionMinor() {
        return (getUserVersion() % 10000) / 100;
    }

    public int getUserVersionPatch() {
        return getUserVersion() % 100;
    }

    public List<String> getFeatureTables() {
        return getTables(ContentsDataType.FEATURES);
    }

    public List<String> getTileTables() {
        return getTables(ContentsDataType.TILES);
    }

    public List<String> getAttributesTables() {
        return getTables(ContentsDataType.ATTRIBUTES);
    }

    public List<String> getTables(ContentsDataType contentsDataType) {
        try {
            return getContentsDao().getTables(contentsDataType);
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to retrieve " + contentsDataType.getName() + " tables", e);
        }
    }

    public List<String> getFeatureAndTileTables() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(getFeatureTables());
        arrayList.addAll(getTileTables());
        return arrayList;
    }

    public List<String> getTables() {
        try {
            return getContentsDao().getTables();
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to retrieve tables", e);
        }
    }

    public boolean isFeatureTable(String str) {
        return new HashSet(getFeatureTables()).contains(str);
    }

    public boolean isTileTable(String str) {
        return new HashSet(getTileTables()).contains(str);
    }

    public boolean isTableType(ContentsDataType contentsDataType, String str) {
        return new HashSet(getTables(contentsDataType)).contains(str);
    }

    public boolean isFeatureOrTileTable(String str) {
        return new HashSet(getFeatureAndTileTables()).contains(str);
    }

    public boolean isTable(String str) {
        return new HashSet(getTables()).contains(str);
    }

    public SpatialReferenceSystemDao getSpatialReferenceSystemDao() {
        SpatialReferenceSystemDao spatialReferenceSystemDao = (SpatialReferenceSystemDao) createDao(SpatialReferenceSystem.class);
        spatialReferenceSystemDao.setCrsWktExtension(new CrsWktExtension(this));
        return spatialReferenceSystemDao;
    }

    public SpatialReferenceSystemSqlMmDao getSpatialReferenceSystemSqlMmDao() {
        SpatialReferenceSystemSqlMmDao spatialReferenceSystemSqlMmDao = (SpatialReferenceSystemSqlMmDao) createDao(SpatialReferenceSystemSqlMm.class);
        verifyTableExists(spatialReferenceSystemSqlMmDao);
        return spatialReferenceSystemSqlMmDao;
    }

    public SpatialReferenceSystemSfSqlDao getSpatialReferenceSystemSfSqlDao() {
        SpatialReferenceSystemSfSqlDao spatialReferenceSystemSfSqlDao = (SpatialReferenceSystemSfSqlDao) createDao(SpatialReferenceSystemSfSql.class);
        verifyTableExists(spatialReferenceSystemSfSqlDao);
        return spatialReferenceSystemSfSqlDao;
    }

    public ContentsDao getContentsDao() {
        ContentsDao contentsDao = (ContentsDao) createDao(Contents.class);
        contentsDao.setDatabase(this.database);
        return contentsDao;
    }

    public GeometryColumnsDao getGeometryColumnsDao() {
        return (GeometryColumnsDao) createDao(GeometryColumns.class);
    }

    public GeometryColumnsSqlMmDao getGeometryColumnsSqlMmDao() {
        GeometryColumnsSqlMmDao geometryColumnsSqlMmDao = (GeometryColumnsSqlMmDao) createDao(GeometryColumnsSqlMm.class);
        verifyTableExists(geometryColumnsSqlMmDao);
        return geometryColumnsSqlMmDao;
    }

    public GeometryColumnsSfSqlDao getGeometryColumnsSfSqlDao() {
        GeometryColumnsSfSqlDao geometryColumnsSfSqlDao = (GeometryColumnsSfSqlDao) createDao(GeometryColumnsSfSql.class);
        verifyTableExists(geometryColumnsSfSqlDao);
        return geometryColumnsSfSqlDao;
    }

    public boolean createGeometryColumnsTable() {
        verifyWritable();
        try {
            if (getGeometryColumnsDao().isTableExists() || this.tableCreator.createGeometryColumns() <= 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check if ");
            Class<GeometryColumns> cls = GeometryColumns.class;
            sb.append("GeometryColumns");
            sb.append(" table exists and create it");
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public void createFeatureTable(FeatureTable featureTable) {
        verifyWritable();
        this.tableCreator.createTable(featureTable);
    }

    public GeometryColumns createFeatureTableWithMetadata(GeometryColumns geometryColumns, BoundingBox boundingBox, long j) {
        return createFeatureTableWithMetadata(geometryColumns, (String) null, (List<FeatureColumn>) null, boundingBox, j);
    }

    public GeometryColumns createFeatureTableWithMetadata(GeometryColumns geometryColumns, String str, BoundingBox boundingBox, long j) {
        return createFeatureTableWithMetadata(geometryColumns, str, (List<FeatureColumn>) null, boundingBox, j);
    }

    public GeometryColumns createFeatureTableWithMetadata(GeometryColumns geometryColumns, List<FeatureColumn> list, BoundingBox boundingBox, long j) {
        return createFeatureTableWithMetadata(geometryColumns, (String) null, list, boundingBox, j);
    }

    public GeometryColumns createFeatureTableWithMetadata(GeometryColumns geometryColumns, String str, List<FeatureColumn> list, BoundingBox boundingBox, long j) {
        if (str == null) {
            str = "id";
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(FeatureColumn.createPrimaryKeyColumn(0, str));
        arrayList.add(FeatureColumn.createGeometryColumn(1, geometryColumns.getColumnName(), geometryColumns.getGeometryType(), false, (Object) null));
        if (list != null) {
            arrayList.addAll(list);
        }
        return createFeatureTableWithMetadata(geometryColumns, boundingBox, j, (List<FeatureColumn>) arrayList);
    }

    public GeometryColumns createFeatureTableWithMetadata(GeometryColumns geometryColumns, BoundingBox boundingBox, long j, List<FeatureColumn> list) {
        SpatialReferenceSystem srs = getSrs(j);
        createGeometryColumnsTable();
        createFeatureTable(new FeatureTable(geometryColumns.getTableName(), list));
        try {
            Contents contents = new Contents();
            contents.setTableName(geometryColumns.getTableName());
            contents.setDataType(ContentsDataType.FEATURES);
            contents.setIdentifier(geometryColumns.getTableName());
            contents.setMinX(Double.valueOf(boundingBox.getMinLongitude()));
            contents.setMinY(Double.valueOf(boundingBox.getMinLatitude()));
            contents.setMaxX(Double.valueOf(boundingBox.getMaxLongitude()));
            contents.setMaxY(Double.valueOf(boundingBox.getMaxLatitude()));
            contents.setSrs(srs);
            getContentsDao().create(contents);
            geometryColumns.setContents(contents);
            geometryColumns.setSrs(contents.getSrs());
            getGeometryColumnsDao().create(geometryColumns);
            return geometryColumns;
        } catch (RuntimeException e) {
            deleteTableQuietly(geometryColumns.getTableName());
            throw e;
        } catch (SQLException e2) {
            deleteTableQuietly(geometryColumns.getTableName());
            throw new GeoPackageException("Failed to create table and metadata: " + geometryColumns.getTableName(), e2);
        }
    }

    public TileMatrixSetDao getTileMatrixSetDao() {
        return (TileMatrixSetDao) createDao(TileMatrixSet.class);
    }

    public boolean createTileMatrixSetTable() {
        verifyWritable();
        try {
            if (getTileMatrixSetDao().isTableExists() || this.tableCreator.createTileMatrixSet() <= 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check if ");
            Class<TileMatrixSet> cls = TileMatrixSet.class;
            sb.append("TileMatrixSet");
            sb.append(" table exists and create it");
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public TileMatrixDao getTileMatrixDao() {
        return (TileMatrixDao) createDao(TileMatrix.class);
    }

    public boolean createTileMatrixTable() {
        verifyWritable();
        try {
            if (getTileMatrixDao().isTableExists() || this.tableCreator.createTileMatrix() <= 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check if ");
            Class<TileMatrix> cls = TileMatrix.class;
            sb.append("TileMatrix");
            sb.append(" table exists and create it");
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public void createTileTable(TileTable tileTable) {
        verifyWritable();
        this.tableCreator.createTable(tileTable);
    }

    public TileMatrixSet createTileTableWithMetadata(String str, BoundingBox boundingBox, long j, BoundingBox boundingBox2, long j2) {
        return createTileTableWithMetadata(ContentsDataType.TILES, str, boundingBox, j, boundingBox2, j2);
    }

    public TileMatrixSet createTileTableWithMetadata(ContentsDataType contentsDataType, String str, BoundingBox boundingBox, long j, BoundingBox boundingBox2, long j2) {
        SpatialReferenceSystem srs = getSrs(j);
        SpatialReferenceSystem srs2 = getSrs(j2);
        createTileMatrixSetTable();
        createTileMatrixTable();
        createTileTable(new TileTable(str, TileTable.createRequiredColumns()));
        try {
            Contents contents = new Contents();
            contents.setTableName(str);
            contents.setDataType(contentsDataType);
            contents.setIdentifier(str);
            contents.setMinX(Double.valueOf(boundingBox.getMinLongitude()));
            contents.setMinY(Double.valueOf(boundingBox.getMinLatitude()));
            contents.setMaxX(Double.valueOf(boundingBox.getMaxLongitude()));
            contents.setMaxY(Double.valueOf(boundingBox.getMaxLatitude()));
            contents.setSrs(srs);
            getContentsDao().create(contents);
            TileMatrixSet tileMatrixSet = new TileMatrixSet();
            tileMatrixSet.setContents(contents);
            tileMatrixSet.setSrs(srs2);
            tileMatrixSet.setMinX(boundingBox2.getMinLongitude());
            tileMatrixSet.setMinY(boundingBox2.getMinLatitude());
            tileMatrixSet.setMaxX(boundingBox2.getMaxLongitude());
            tileMatrixSet.setMaxY(boundingBox2.getMaxLatitude());
            getTileMatrixSetDao().create(tileMatrixSet);
            return tileMatrixSet;
        } catch (RuntimeException e) {
            deleteTableQuietly(str);
            throw e;
        } catch (SQLException e2) {
            deleteTableQuietly(str);
            throw new GeoPackageException("Failed to create table and metadata: " + str, e2);
        }
    }

    private SpatialReferenceSystem getSrs(long j) {
        try {
            SpatialReferenceSystem queryForId = getSpatialReferenceSystemDao().queryForId(Long.valueOf(j));
            if (queryForId != null) {
                return queryForId;
            }
            throw new GeoPackageException("Spatial Reference System could not be found. SRS ID: " + j);
        } catch (SQLException unused) {
            throw new GeoPackageException("Failed to retrieve Spatial Reference System. SRS ID: " + j);
        }
    }

    public DataColumnsDao getDataColumnsDao() {
        return (DataColumnsDao) createDao(DataColumns.class);
    }

    public boolean createDataColumnsTable() {
        verifyWritable();
        try {
            if (getDataColumnsDao().isTableExists() || this.tableCreator.createDataColumns() <= 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check if ");
            Class<DataColumns> cls = DataColumns.class;
            sb.append("DataColumns");
            sb.append(" table exists and create it");
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public DataColumnConstraintsDao getDataColumnConstraintsDao() {
        return (DataColumnConstraintsDao) createDao(DataColumnConstraints.class);
    }

    public boolean createDataColumnConstraintsTable() {
        verifyWritable();
        try {
            boolean z = false;
            if (!getDataColumnConstraintsDao().isTableExists()) {
                if (this.tableCreator.createDataColumnConstraints() > 0) {
                    z = true;
                }
                if (z) {
                    new SchemaExtension(this).getOrCreate();
                }
            }
            return z;
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check if ");
            Class<DataColumnConstraints> cls = DataColumnConstraints.class;
            sb.append("DataColumnConstraints");
            sb.append(" table exists and create it");
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public MetadataDao getMetadataDao() {
        return (MetadataDao) createDao(Metadata.class);
    }

    public boolean createMetadataTable() {
        verifyWritable();
        try {
            boolean z = false;
            if (!getMetadataDao().isTableExists()) {
                if (this.tableCreator.createMetadata() > 0) {
                    z = true;
                }
                if (z) {
                    new MetadataExtension(this).getOrCreate();
                }
            }
            return z;
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check if ");
            Class<Metadata> cls = Metadata.class;
            sb.append("Metadata");
            sb.append(" table exists and create it");
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public MetadataReferenceDao getMetadataReferenceDao() {
        return (MetadataReferenceDao) createDao(MetadataReference.class);
    }

    public boolean createMetadataReferenceTable() {
        verifyWritable();
        try {
            if (getMetadataReferenceDao().isTableExists() || this.tableCreator.createMetadataReference() <= 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check if ");
            Class<MetadataReference> cls = MetadataReference.class;
            sb.append("MetadataReference");
            sb.append(" table exists and create it");
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public ExtensionsDao getExtensionsDao() {
        return (ExtensionsDao) createDao(Extensions.class);
    }

    public boolean createExtensionsTable() {
        verifyWritable();
        try {
            if (getExtensionsDao().isTableExists() || this.tableCreator.createExtensions() <= 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check if ");
            Class<Extensions> cls = Extensions.class;
            sb.append("Extensions");
            sb.append(" table exists and create it");
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public void deleteTable(String str) {
        verifyWritable();
        GeoPackageExtensions.deleteTableExtensions(this, str);
        getContentsDao().deleteTable(str);
    }

    public void deleteTableQuietly(String str) {
        verifyWritable();
        try {
            deleteTable(str);
        } catch (Exception unused) {
        }
    }

    public <T, S extends BaseDaoImpl<T, ?>> S createDao(Class<T> cls) {
        try {
            return (BaseDaoImpl) DaoManager.createDao(this.database.getConnectionSource(), cls);
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to create " + cls.getSimpleName() + " dao", e);
        }
    }

    private void verifyTableExists(BaseDaoImpl<?, ?> baseDaoImpl) {
        try {
            if (!baseDaoImpl.isTableExists()) {
                throw new GeoPackageException("Table or view does not exist for: " + baseDaoImpl.getDataClass().getSimpleName());
            }
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to detect if table or view exists for dao: " + baseDaoImpl.getDataClass().getSimpleName(), e);
        }
    }

    public void verifyWritable() {
        String str;
        if (!this.writable) {
            StringBuilder sb = new StringBuilder();
            sb.append("GeoPackage file is not writable. Name: ");
            sb.append(this.name);
            if (this.path != null) {
                str = ", Path: " + this.path;
            } else {
                str = "";
            }
            sb.append(str);
            throw new GeoPackageException(sb.toString());
        }
    }

    public void dropTable(String str) {
        this.tableCreator.dropTable(str);
    }

    public GriddedCoverageDao getGriddedCoverageDao() {
        return (GriddedCoverageDao) createDao(GriddedCoverage.class);
    }

    public boolean createGriddedCoverageTable() {
        verifyWritable();
        try {
            if (getGriddedCoverageDao().isTableExists() || this.tableCreator.createGriddedCoverage() <= 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check if ");
            Class<GriddedCoverage> cls = GriddedCoverage.class;
            sb.append("GriddedCoverage");
            sb.append(" table exists and create it");
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public GriddedTileDao getGriddedTileDao() {
        return (GriddedTileDao) createDao(GriddedTile.class);
    }

    public boolean createGriddedTileTable() {
        verifyWritable();
        try {
            if (getGriddedTileDao().isTableExists() || this.tableCreator.createGriddedTile() <= 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check if ");
            Class<GriddedTile> cls = GriddedTile.class;
            sb.append("GriddedTile");
            sb.append(" table exists and create it");
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public TableIndexDao getTableIndexDao() {
        return (TableIndexDao) createDao(TableIndex.class);
    }

    public boolean createTableIndexTable() {
        verifyWritable();
        try {
            if (getTableIndexDao().isTableExists() || this.tableCreator.createTableIndex() <= 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check if ");
            Class<TableIndex> cls = TableIndex.class;
            sb.append("TableIndex");
            sb.append(" table exists and create it");
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public GeometryIndexDao getGeometryIndexDao() {
        return (GeometryIndexDao) createDao(GeometryIndex.class);
    }

    public boolean createGeometryIndexTable() {
        verifyWritable();
        try {
            if (getGeometryIndexDao().isTableExists() || this.tableCreator.createGeometryIndex() <= 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check if ");
            Class<GeometryIndex> cls = GeometryIndex.class;
            sb.append("GeometryIndex");
            sb.append(" table exists and create it");
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public FeatureTileLinkDao getFeatureTileLinkDao() {
        return (FeatureTileLinkDao) createDao(FeatureTileLink.class);
    }

    public boolean createFeatureTileLinkTable() {
        verifyWritable();
        try {
            if (getFeatureTileLinkDao().isTableExists() || this.tableCreator.createFeatureTileLink() <= 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check if ");
            Class<FeatureTileLink> cls = FeatureTileLink.class;
            sb.append("FeatureTileLink");
            sb.append(" table exists and create it");
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public void createAttributesTable(AttributesTable attributesTable) {
        verifyWritable();
        this.tableCreator.createTable(attributesTable);
    }

    public AttributesTable createAttributesTableWithId(String str, List<AttributesColumn> list) {
        return createAttributesTable(str, (String) null, list);
    }

    public AttributesTable createAttributesTable(String str, String str2, List<AttributesColumn> list) {
        if (str2 == null) {
            str2 = "id";
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(AttributesColumn.createPrimaryKeyColumn(0, str2));
        if (list != null) {
            arrayList.addAll(list);
        }
        return createAttributesTable(str, arrayList);
    }

    public AttributesTable createAttributesTable(String str, List<AttributesColumn> list) {
        AttributesTable attributesTable = new AttributesTable(str, list);
        createAttributesTable(attributesTable);
        try {
            Contents contents = new Contents();
            contents.setTableName(str);
            contents.setDataType(ContentsDataType.ATTRIBUTES);
            contents.setIdentifier(str);
            getContentsDao().create(contents);
            attributesTable.setContents(contents);
            return attributesTable;
        } catch (RuntimeException e) {
            deleteTableQuietly(str);
            throw e;
        } catch (SQLException e2) {
            deleteTableQuietly(str);
            throw new GeoPackageException("Failed to create table and metadata: " + str, e2);
        }
    }
}
