package mil.nga.geopackage.extension.index;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import java.sql.SQLException;
import java.util.Date;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.ExtensionsDao;
import mil.nga.geopackage.geom.GeoPackageGeometryData;
import mil.nga.geopackage.p010io.GeoPackageProgress;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryEnvelope;
import mil.nga.wkb.util.GeometryEnvelopeBuilder;

public abstract class FeatureTableCoreIndex extends BaseExtension {
    public static final String EXTENSION_AUTHOR = "nga";
    public static final String EXTENSION_DEFINITION = GeoPackageProperties.getProperty(PropertyConstants.EXTENSIONS, EXTENSION_NAME_NO_AUTHOR);
    public static final String EXTENSION_NAME = Extensions.buildExtensionName("nga", EXTENSION_NAME_NO_AUTHOR);
    public static final String EXTENSION_NAME_NO_AUTHOR = "geometry_index";
    private final String columnName;
    private final GeometryIndexDao geometryIndexDao;
    protected GeoPackageProgress progress;
    private final TableIndexDao tableIndexDao;
    private final String tableName;

    /* access modifiers changed from: protected */
    public abstract int indexTable(TableIndex tableIndex);

    protected FeatureTableCoreIndex(GeoPackageCore geoPackageCore, String str, String str2) {
        super(geoPackageCore);
        this.tableName = str;
        this.columnName = str2;
        this.tableIndexDao = geoPackageCore.getTableIndexDao();
        this.geometryIndexDao = geoPackageCore.getGeometryIndexDao();
    }

    public GeoPackageCore getGeoPackage() {
        return this.geoPackage;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setProgress(GeoPackageProgress geoPackageProgress) {
        this.progress = geoPackageProgress;
    }

    public int index() {
        return index(false);
    }

    public int index(boolean z) {
        if (!z && isIndexed()) {
            return 0;
        }
        getOrCreateExtension();
        TableIndex orCreateTableIndex = getOrCreateTableIndex();
        createOrClearGeometryIndices();
        return indexTable(orCreateTableIndex);
    }

    /* access modifiers changed from: protected */
    public boolean index(TableIndex tableIndex, long j, GeoPackageGeometryData geoPackageGeometryData) {
        Geometry geometry;
        if (geoPackageGeometryData != null) {
            GeometryEnvelope envelope = geoPackageGeometryData.getEnvelope();
            if (envelope == null && (geometry = geoPackageGeometryData.getGeometry()) != null) {
                envelope = GeometryEnvelopeBuilder.buildEnvelope(geometry);
            }
            if (envelope != null) {
                try {
                    this.geometryIndexDao.createOrUpdate(this.geometryIndexDao.populate(tableIndex, j, envelope));
                    return true;
                } catch (SQLException e) {
                    throw new GeoPackageException("Failed to create or update Geometry Index. GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName + ", Geom Id: " + j, e);
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void updateLastIndexed() {
        TableIndex tableIndex = new TableIndex();
        tableIndex.setTableName(this.tableName);
        tableIndex.setLastIndexed(new Date());
        try {
            this.tableIndexDao.createOrUpdate(tableIndex);
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to update last indexed date. GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName, e);
        }
    }

    public boolean deleteIndex() {
        ExtensionsDao extensionsDao = this.geoPackage.getExtensionsDao();
        try {
            boolean z = this.geoPackage.getTableIndexDao().deleteByIdCascade(this.tableName) > 0;
            if (extensionsDao.deleteByExtension(EXTENSION_NAME, this.tableName) > 0 || z) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete Table Index. GeoPackage: " + this.geoPackage.getName() + ", Table: " + this.tableName, e);
        }
    }

    public int deleteIndex(long j) {
        try {
            return this.geometryIndexDao.deleteById(new GeometryIndexKey(this.tableName, j));
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete index, GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName + ", Geometry Id: " + j, e);
        }
    }

    public boolean isIndexed() {
        Date lastIndexed;
        if (getExtension() == null) {
            return false;
        }
        try {
            Contents contents = (Contents) this.geoPackage.getContentsDao().queryForId(this.tableName);
            if (contents == null) {
                return false;
            }
            Date lastChange = contents.getLastChange();
            TableIndex tableIndex = (TableIndex) this.geoPackage.getTableIndexDao().queryForId(this.tableName);
            if (tableIndex == null || (lastIndexed = tableIndex.getLastIndexed()) == null || lastIndexed.getTime() < lastChange.getTime()) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to check if table is indexed, GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName, e);
        }
    }

    private TableIndex getOrCreateTableIndex() {
        TableIndex tableIndex = getTableIndex();
        if (tableIndex != null) {
            return tableIndex;
        }
        try {
            if (!this.tableIndexDao.isTableExists()) {
                this.geoPackage.createTableIndexTable();
            }
            TableIndex tableIndex2 = new TableIndex();
            tableIndex2.setTableName(this.tableName);
            tableIndex2.setLastIndexed((Date) null);
            this.tableIndexDao.create(tableIndex2);
            return tableIndex2;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to create Table Index for GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName + ", Column Name: " + this.columnName, e);
        }
    }

    public TableIndex getTableIndex() {
        try {
            if (this.tableIndexDao.isTableExists()) {
                return (TableIndex) this.tableIndexDao.queryForId(this.tableName);
            }
            return null;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to query for Table Index for GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName + ", Column Name: " + this.columnName, e);
        }
    }

    public Date getLastIndexed() {
        TableIndex tableIndex = getTableIndex();
        if (tableIndex != null) {
            return tableIndex.getLastIndexed();
        }
        return null;
    }

    private void createOrClearGeometryIndices() {
        if (!createGeometryIndexTable()) {
            clearGeometryIndices();
        }
    }

    private int clearGeometryIndices() {
        DeleteBuilder deleteBuilder = this.geometryIndexDao.deleteBuilder();
        try {
            deleteBuilder.where().mo19710eq("table_name", this.tableName);
            return this.geometryIndexDao.delete(deleteBuilder.prepare());
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to clear Geometry Index rows for GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName + ", Column Name: " + this.columnName, e);
        }
    }

    private boolean createGeometryIndexTable() {
        try {
            if (!this.geometryIndexDao.isTableExists()) {
                return this.geoPackage.createGeometryIndexTable();
            }
            return false;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to create Geometry Index table for GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName + ", Column Name: " + this.columnName, e);
        }
    }

    private Extensions getOrCreateExtension() {
        return getOrCreate(EXTENSION_NAME, this.tableName, this.columnName, EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);
    }

    public Extensions getExtension() {
        return get(EXTENSION_NAME, this.tableName, this.columnName);
    }

    public CloseableIterator<GeometryIndex> query() {
        try {
            return queryBuilder().iterator();
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to query for all Geometry Indices. GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName + ", Column Name: " + this.columnName, e);
        }
    }

    public long count() {
        try {
            return queryBuilder().countOf();
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to query for Geometry Index count. GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName + ", Column Name: " + this.columnName, e);
        }
    }

    public QueryBuilder<GeometryIndex, GeometryIndexKey> queryBuilder() {
        QueryBuilder<GeometryIndex, GeometryIndexKey> queryBuilder = this.geometryIndexDao.queryBuilder();
        try {
            queryBuilder.where().mo19710eq("table_name", this.tableName);
            return queryBuilder;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to build query for all Geometry Indices. GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName + ", Column Name: " + this.columnName, e);
        }
    }

    public CloseableIterator<GeometryIndex> query(BoundingBox boundingBox) {
        return query(boundingBox.buildEnvelope());
    }

    public long count(BoundingBox boundingBox) {
        return count(boundingBox.buildEnvelope());
    }

    public CloseableIterator<GeometryIndex> query(GeometryEnvelope geometryEnvelope) {
        try {
            return queryBuilder(geometryEnvelope).iterator();
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to query for Geometry Indices. GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName + ", Column Name: " + this.columnName, e);
        }
    }

    public long count(GeometryEnvelope geometryEnvelope) {
        try {
            return queryBuilder(geometryEnvelope).countOf();
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to query for Geometry Index count. GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName + ", Column Name: " + this.columnName, e);
        }
    }

    public QueryBuilder<GeometryIndex, GeometryIndexKey> queryBuilder(GeometryEnvelope geometryEnvelope) {
        QueryBuilder<GeometryIndex, GeometryIndexKey> queryBuilder = this.geometryIndexDao.queryBuilder();
        try {
            Where<GeometryIndex, GeometryIndexKey> where = queryBuilder.where();
            where.mo19710eq("table_name", this.tableName).and().mo19723le("min_x", Double.valueOf(geometryEnvelope.getMaxX())).and().mo19712ge("max_x", Double.valueOf(geometryEnvelope.getMinX())).and().mo19723le("min_y", Double.valueOf(geometryEnvelope.getMaxY())).and().mo19712ge("max_y", Double.valueOf(geometryEnvelope.getMinY()));
            if (geometryEnvelope.hasZ()) {
                where.and().mo19723le("min_z", geometryEnvelope.getMaxZ()).and().mo19712ge("max_z", geometryEnvelope.getMinZ());
            }
            if (geometryEnvelope.hasM()) {
                where.and().mo19723le("min_m", geometryEnvelope.getMaxM()).and().mo19712ge("max_m", geometryEnvelope.getMinM());
            }
            return queryBuilder;
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to build query for Geometry Indices. GeoPackage: " + this.geoPackage.getName() + ", Table Name: " + this.tableName + ", Column Name: " + this.columnName, e);
        }
    }
}
