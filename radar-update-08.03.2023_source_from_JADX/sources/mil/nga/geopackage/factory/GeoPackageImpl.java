package mil.nga.geopackage.factory;

import android.database.Cursor;
import com.j256.ormlite.stmt.QueryBuilder;
import java.sql.SQLException;
import java.util.List;
import mil.nga.geopackage.GeoPackage;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.attributes.AttributesConnection;
import mil.nga.geopackage.attributes.AttributesCursor;
import mil.nga.geopackage.attributes.AttributesDao;
import mil.nga.geopackage.attributes.AttributesTable;
import mil.nga.geopackage.attributes.AttributesTableReader;
import mil.nga.geopackage.attributes.AttributesWrapperConnection;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.extension.RTreeIndexExtension;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.user.FeatureConnection;
import mil.nga.geopackage.features.user.FeatureCursor;
import mil.nga.geopackage.features.user.FeatureDao;
import mil.nga.geopackage.features.user.FeatureTable;
import mil.nga.geopackage.features.user.FeatureTableReader;
import mil.nga.geopackage.features.user.FeatureWrapperConnection;
import mil.nga.geopackage.p009db.GeoPackageConnection;
import mil.nga.geopackage.p009db.GeoPackageTableCreator;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrix.TileMatrixDao;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.user.TileConnection;
import mil.nga.geopackage.tiles.user.TileCursor;
import mil.nga.geopackage.tiles.user.TileDao;
import mil.nga.geopackage.tiles.user.TileTable;
import mil.nga.geopackage.tiles.user.TileTableReader;
import mil.nga.geopackage.tiles.user.TileWrapperConnection;

class GeoPackageImpl extends GeoPackageCoreImpl implements GeoPackage {
    private final GeoPackageCursorFactory cursorFactory;
    private final GeoPackageConnection database;

    GeoPackageImpl(String str, String str2, GeoPackageConnection geoPackageConnection, GeoPackageCursorFactory geoPackageCursorFactory, GeoPackageTableCreator geoPackageTableCreator, boolean z) {
        super(str, str2, geoPackageConnection, geoPackageTableCreator, z);
        this.database = geoPackageConnection;
        this.cursorFactory = geoPackageCursorFactory;
    }

    public FeatureDao getFeatureDao(GeometryColumns geometryColumns) {
        if (geometryColumns != null) {
            final FeatureTable featureTable = (FeatureTable) new FeatureTableReader(geometryColumns).readTable(new FeatureWrapperConnection(this.database));
            FeatureDao featureDao = new FeatureDao(getName(), this.database, new FeatureConnection(this.database), geometryColumns, featureTable);
            this.cursorFactory.registerTable(geometryColumns.getTableName(), new GeoPackageCursorWrapper() {
                public Cursor wrapCursor(Cursor cursor) {
                    return new FeatureCursor(featureTable, cursor);
                }
            });
            if (this.writable) {
                new RTreeIndexExtension(this).dropTriggers(featureTable);
            }
            return featureDao;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Non null ");
        Class<GeometryColumns> cls = GeometryColumns.class;
        sb.append("GeometryColumns");
        sb.append(" is required to create ");
        Class<FeatureDao> cls2 = FeatureDao.class;
        sb.append("FeatureDao");
        throw new GeoPackageException(sb.toString());
    }

    public FeatureDao getFeatureDao(Contents contents) {
        if (contents != null) {
            GeometryColumns geometryColumns = contents.getGeometryColumns();
            if (geometryColumns != null) {
                return getFeatureDao(geometryColumns);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No ");
            Class<GeometryColumns> cls = GeometryColumns.class;
            sb.append("GeometryColumns");
            sb.append(" exists for ");
            Class<Contents> cls2 = Contents.class;
            sb.append("Contents");
            sb.append(" ");
            sb.append(contents.getId());
            throw new GeoPackageException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Non null ");
        Class<Contents> cls3 = Contents.class;
        sb2.append("Contents");
        sb2.append(" is required to create ");
        Class<FeatureDao> cls4 = FeatureDao.class;
        sb2.append("FeatureDao");
        throw new GeoPackageException(sb2.toString());
    }

    public FeatureDao getFeatureDao(String str) {
        try {
            List queryForEq = getGeometryColumnsDao().queryForEq("table_name", str);
            if (queryForEq.isEmpty()) {
                throw new GeoPackageException("No Feature Table exists for table name: " + str);
            } else if (queryForEq.size() <= 1) {
                return getFeatureDao((GeometryColumns) queryForEq.get(0));
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected state. More than one ");
                Class<GeometryColumns> cls = GeometryColumns.class;
                sb.append("GeometryColumns");
                sb.append(" matched for table name: ");
                sb.append(str);
                sb.append(", count: ");
                sb.append(queryForEq.size());
                throw new GeoPackageException(sb.toString());
            }
        } catch (SQLException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to retrieve ");
            Class<FeatureDao> cls2 = FeatureDao.class;
            sb2.append("FeatureDao");
            sb2.append(" for table name: ");
            sb2.append(str);
            sb2.append(". Exception retrieving ");
            Class<GeometryColumns> cls3 = GeometryColumns.class;
            sb2.append("GeometryColumns");
            sb2.append(PropertyConstants.PROPERTY_DIVIDER);
            throw new GeoPackageException(sb2.toString(), e);
        }
    }

    public TileDao getTileDao(TileMatrixSet tileMatrixSet) {
        if (tileMatrixSet != null) {
            try {
                TileMatrixDao tileMatrixDao = getTileMatrixDao();
                QueryBuilder queryBuilder = tileMatrixDao.queryBuilder();
                queryBuilder.where().mo19710eq("table_name", tileMatrixSet.getTableName());
                queryBuilder.orderBy("zoom_level", true);
                queryBuilder.orderBy(TileMatrix.COLUMN_PIXEL_X_SIZE, false);
                queryBuilder.orderBy(TileMatrix.COLUMN_PIXEL_Y_SIZE, false);
                List query = tileMatrixDao.query(queryBuilder.prepare());
                final TileTable tileTable = (TileTable) new TileTableReader(tileMatrixSet.getTableName()).readTable(new TileWrapperConnection(this.database));
                TileDao tileDao = new TileDao(getName(), this.database, new TileConnection(this.database), tileMatrixSet, query, tileTable);
                this.cursorFactory.registerTable(tileMatrixSet.getTableName(), new GeoPackageCursorWrapper() {
                    public Cursor wrapCursor(Cursor cursor) {
                        return new TileCursor(tileTable, cursor);
                    }
                });
                return tileDao;
            } catch (SQLException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to retrieve ");
                Class<TileDao> cls = TileDao.class;
                sb.append("TileDao");
                sb.append(" for table name: ");
                sb.append(tileMatrixSet.getTableName());
                sb.append(". Exception retrieving ");
                Class<TileMatrix> cls2 = TileMatrix.class;
                sb.append("TileMatrix");
                sb.append(" collection.");
                throw new GeoPackageException(sb.toString(), e);
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Non null ");
            Class<TileMatrixSet> cls3 = TileMatrixSet.class;
            sb2.append("TileMatrixSet");
            sb2.append(" is required to create ");
            Class<TileDao> cls4 = TileDao.class;
            sb2.append("TileDao");
            throw new GeoPackageException(sb2.toString());
        }
    }

    public TileDao getTileDao(Contents contents) {
        if (contents != null) {
            TileMatrixSet tileMatrixSet = contents.getTileMatrixSet();
            if (tileMatrixSet != null) {
                return getTileDao(tileMatrixSet);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No ");
            Class<TileMatrixSet> cls = TileMatrixSet.class;
            sb.append("TileMatrixSet");
            sb.append(" exists for ");
            Class<Contents> cls2 = Contents.class;
            sb.append("Contents");
            sb.append(" ");
            sb.append(contents.getId());
            throw new GeoPackageException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Non null ");
        Class<Contents> cls3 = Contents.class;
        sb2.append("Contents");
        sb2.append(" is required to create ");
        Class<TileDao> cls4 = TileDao.class;
        sb2.append("TileDao");
        throw new GeoPackageException(sb2.toString());
    }

    public TileDao getTileDao(String str) {
        try {
            List queryForEq = getTileMatrixSetDao().queryForEq("table_name", str);
            if (queryForEq.isEmpty()) {
                throw new GeoPackageException("No Tile Table exists for table name: " + str);
            } else if (queryForEq.size() <= 1) {
                return getTileDao((TileMatrixSet) queryForEq.get(0));
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected state. More than one ");
                Class<TileMatrixSet> cls = TileMatrixSet.class;
                sb.append("TileMatrixSet");
                sb.append(" matched for table name: ");
                sb.append(str);
                sb.append(", count: ");
                sb.append(queryForEq.size());
                throw new GeoPackageException(sb.toString());
            }
        } catch (SQLException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to retrieve ");
            Class<TileDao> cls2 = TileDao.class;
            sb2.append("TileDao");
            sb2.append(" for table name: ");
            sb2.append(str);
            sb2.append(". Exception retrieving ");
            Class<TileMatrixSet> cls3 = TileMatrixSet.class;
            sb2.append("TileMatrixSet");
            sb2.append(PropertyConstants.PROPERTY_DIVIDER);
            throw new GeoPackageException(sb2.toString(), e);
        }
    }

    public AttributesDao getAttributesDao(Contents contents) {
        if (contents == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Non null ");
            Class<Contents> cls = Contents.class;
            sb.append("Contents");
            sb.append(" is required to create ");
            Class<AttributesDao> cls2 = AttributesDao.class;
            sb.append("AttributesDao");
            throw new GeoPackageException(sb.toString());
        } else if (contents.getDataType() == ContentsDataType.ATTRIBUTES) {
            final AttributesTable attributesTable = (AttributesTable) new AttributesTableReader(contents.getTableName()).readTable(new AttributesWrapperConnection(this.database));
            attributesTable.setContents(contents);
            AttributesDao attributesDao = new AttributesDao(getName(), this.database, new AttributesConnection(this.database), attributesTable);
            this.cursorFactory.registerTable(attributesTable.getTableName(), new GeoPackageCursorWrapper() {
                public Cursor wrapCursor(Cursor cursor) {
                    return new AttributesCursor(attributesTable, cursor);
                }
            });
            return attributesDao;
        } else {
            StringBuilder sb2 = new StringBuilder();
            Class<Contents> cls3 = Contents.class;
            sb2.append("Contents");
            sb2.append(" is required to be of type ");
            sb2.append(ContentsDataType.ATTRIBUTES);
            sb2.append(". Actual: ");
            sb2.append(contents.getDataTypeString());
            throw new GeoPackageException(sb2.toString());
        }
    }

    public AttributesDao getAttributesDao(String str) {
        try {
            Contents contents = (Contents) getContentsDao().queryForId(str);
            if (contents != null) {
                return getAttributesDao(contents);
            }
            throw new GeoPackageException("No Contents Table exists for table name: " + str);
        } catch (SQLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to retrieve ");
            Class<Contents> cls = Contents.class;
            sb.append("Contents");
            sb.append(" for table name: ");
            sb.append(str);
            throw new GeoPackageException(sb.toString(), e);
        }
    }

    public void execSQL(String str) {
        this.database.execSQL(str);
    }

    public Cursor rawQuery(String str, String[] strArr) {
        return this.database.rawQuery(str, strArr);
    }

    public GeoPackageConnection getConnection() {
        return this.database;
    }

    public Cursor foreignKeyCheck() {
        Cursor rawQuery = rawQuery("PRAGMA foreign_key_check", (String[]) null);
        if (rawQuery.moveToNext()) {
            return rawQuery;
        }
        rawQuery.close();
        return null;
    }

    public Cursor integrityCheck() {
        return integrityCheck(rawQuery("PRAGMA integrity_check", (String[]) null));
    }

    public Cursor quickCheck() {
        return integrityCheck(rawQuery("PRAGMA quick_check", (String[]) null));
    }

    private Cursor integrityCheck(Cursor cursor) {
        if (!cursor.moveToNext() || !cursor.getString(0).equals("ok")) {
            return cursor;
        }
        cursor.close();
        return null;
    }
}
