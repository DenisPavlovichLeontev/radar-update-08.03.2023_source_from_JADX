package mil.nga.geopackage.core.contents;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.IOException;
import java.util.Date;
import mil.nga.geopackage.BoundingBox;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.persister.DatePersister;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;

@DatabaseTable(daoClass = ContentsDao.class, tableName = "gpkg_contents")
public class Contents {
    public static final String COLUMN_DATA_TYPE = "data_type";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ID = "table_name";
    public static final String COLUMN_IDENTIFIER = "identifier";
    public static final String COLUMN_LAST_CHANGE = "last_change";
    public static final String COLUMN_MAX_X = "max_x";
    public static final String COLUMN_MAX_Y = "max_y";
    public static final String COLUMN_MIN_X = "min_x";
    public static final String COLUMN_MIN_Y = "min_y";
    public static final String COLUMN_SRS_ID = "srs_id";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String TABLE_NAME = "gpkg_contents";
    @DatabaseField(canBeNull = false, columnName = "data_type")
    private String dataType;
    @DatabaseField(columnName = "description", defaultValue = "")
    private String description;
    @ForeignCollectionField(eager = false)
    private ForeignCollection<GeometryColumns> geometryColumns;
    @DatabaseField(columnName = "identifier", unique = true)
    private String identifier;
    @DatabaseField(columnName = "last_change", defaultValue = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", persisterClass = DatePersister.class)
    private Date lastChange;
    @DatabaseField(columnName = "max_x")
    private Double maxX;
    @DatabaseField(columnName = "max_y")
    private Double maxY;
    @DatabaseField(columnName = "min_x")
    private Double minX;
    @DatabaseField(columnName = "min_y")
    private Double minY;
    @DatabaseField(columnName = "srs_id", foreign = true, foreignAutoRefresh = true)
    private SpatialReferenceSystem srs;
    @DatabaseField(columnName = "srs_id")
    private Long srsId;
    @DatabaseField(canBeNull = false, columnName = "table_name", mo19322id = true)
    private String tableName;
    @ForeignCollectionField(eager = false)
    private ForeignCollection<TileMatrix> tileMatrix;
    @ForeignCollectionField(eager = false)
    private ForeignCollection<TileMatrixSet> tileMatrixSet;

    public Contents() {
    }

    public Contents(Contents contents) {
        this.tableName = contents.tableName;
        this.dataType = contents.dataType;
        this.identifier = contents.identifier;
        this.description = contents.description;
        this.lastChange = new Date(contents.lastChange.getTime());
        this.minX = contents.minX;
        this.maxX = contents.maxX;
        this.minY = contents.minY;
        this.maxY = contents.maxY;
        this.srs = contents.srs;
        this.srsId = contents.srsId;
    }

    public String getId() {
        return this.tableName;
    }

    public void setId(String str) {
        this.tableName = str;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String str) {
        this.tableName = str;
    }

    public ContentsDataType getDataType() {
        return ContentsDataType.fromName(this.dataType);
    }

    public void setDataType(ContentsDataType contentsDataType) {
        this.dataType = contentsDataType.getName();
    }

    public String getDataTypeString() {
        return this.dataType;
    }

    public void setDataTypeString(String str) {
        this.dataType = str;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String str) {
        this.identifier = str;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public Date getLastChange() {
        return this.lastChange;
    }

    public void setLastChange(Date date) {
        this.lastChange = date;
    }

    public Double getMinX() {
        return this.minX;
    }

    public void setMinX(Double d) {
        this.minX = d;
    }

    public Double getMinY() {
        return this.minY;
    }

    public void setMinY(Double d) {
        this.minY = d;
    }

    public Double getMaxX() {
        return this.maxX;
    }

    public void setMaxX(Double d) {
        this.maxX = d;
    }

    public Double getMaxY() {
        return this.maxY;
    }

    public void setMaxY(Double d) {
        this.maxY = d;
    }

    public SpatialReferenceSystem getSrs() {
        return this.srs;
    }

    public void setSrs(SpatialReferenceSystem spatialReferenceSystem) {
        this.srs = spatialReferenceSystem;
        this.srsId = spatialReferenceSystem != null ? Long.valueOf(spatialReferenceSystem.getId()) : null;
    }

    public Long getSrsId() {
        return this.srsId;
    }

    public GeometryColumns getGeometryColumns() {
        if (this.geometryColumns.size() > 1) {
            throw new GeoPackageException("Unexpected state. More than one GeometryColumn has a foreign key to the Contents. Count: " + this.geometryColumns.size());
        } else if (this.geometryColumns.size() != 1) {
            return null;
        } else {
            CloseableIterator<GeometryColumns> closeableIterator = this.geometryColumns.closeableIterator();
            try {
                try {
                    return (GeometryColumns) closeableIterator.next();
                } catch (IOException e) {
                    throw new GeoPackageException("Failed to close the Geometry Columns iterator", e);
                }
            } finally {
                try {
                    closeableIterator.close();
                } catch (IOException e2) {
                    throw new GeoPackageException("Failed to close the Geometry Columns iterator", e2);
                }
            }
        }
    }

    public TileMatrixSet getTileMatrixSet() {
        if (this.tileMatrixSet.size() > 1) {
            throw new GeoPackageException("Unexpected state. More than one TileMatrixSet has a foreign key to the Contents. Count: " + this.tileMatrixSet.size());
        } else if (this.tileMatrixSet.size() != 1) {
            return null;
        } else {
            CloseableIterator<TileMatrixSet> closeableIterator = this.tileMatrixSet.closeableIterator();
            try {
                try {
                    return (TileMatrixSet) closeableIterator.next();
                } catch (IOException e) {
                    throw new GeoPackageException("Failed to close the Tile Matrix Set iterator", e);
                }
            } finally {
                try {
                    closeableIterator.close();
                } catch (IOException e2) {
                    throw new GeoPackageException("Failed to close the Tile Matrix Set iterator", e2);
                }
            }
        }
    }

    public ForeignCollection<TileMatrix> getTileMatrix() {
        return this.tileMatrix;
    }

    public BoundingBox getBoundingBox() {
        if (this.minX == null || this.maxX == null || this.minY == null || this.maxY == null) {
            return null;
        }
        return new BoundingBox(getMinX().doubleValue(), getMinY().doubleValue(), getMaxX().doubleValue(), getMaxY().doubleValue());
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        setMinX(Double.valueOf(boundingBox.getMinLongitude()));
        setMaxX(Double.valueOf(boundingBox.getMaxLongitude()));
        setMinY(Double.valueOf(boundingBox.getMinLatitude()));
        setMaxY(Double.valueOf(boundingBox.getMaxLatitude()));
    }
}
