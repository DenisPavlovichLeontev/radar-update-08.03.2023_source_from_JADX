package mil.nga.geopackage.p009db;

import com.j256.ormlite.dao.DaoManager;
import java.sql.SQLException;
import java.util.List;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.extension.coverage.CoverageDataCore;
import mil.nga.geopackage.extension.index.FeatureTableCoreIndex;
import mil.nga.geopackage.extension.link.FeatureTileTableCoreLinker;
import mil.nga.geopackage.features.columns.GeometryColumnsSfSql;
import mil.nga.geopackage.p010io.ResourceIOUtils;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;
import mil.nga.geopackage.user.UserUniqueConstraint;

/* renamed from: mil.nga.geopackage.db.GeoPackageTableCreator */
public class GeoPackageTableCreator {

    /* renamed from: db */
    private final GeoPackageCoreConnection f338db;

    public GeoPackageTableCreator(GeoPackageCoreConnection geoPackageCoreConnection) {
        this.f338db = geoPackageCoreConnection;
    }

    public int createSpatialReferenceSystem() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, "spatial_reference_system"));
    }

    public int createContents() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, "contents"));
    }

    public int createGeometryColumns() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, GeometryColumnsSfSql.TABLE_NAME));
    }

    public int createTileMatrixSet() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, "tile_matrix_set"));
    }

    public int createTileMatrix() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, "tile_matrix"));
    }

    public int createDataColumns() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, "data_columns"));
    }

    public int createDataColumnConstraints() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, "data_column_constraints"));
    }

    public int createMetadata() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, "metadata"));
    }

    public int createMetadataReference() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, "metadata_reference"));
    }

    public int createExtensions() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, "extensions"));
    }

    public int createGriddedCoverage() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, CoverageDataCore.EXTENSION_NAME_NO_AUTHOR));
    }

    public int createGriddedTile() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, "2d_gridded_tile"));
    }

    public int createTableIndex() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, "table_index"));
    }

    public int createGeometryIndex() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, FeatureTableCoreIndex.EXTENSION_NAME_NO_AUTHOR));
    }

    public int createFeatureTileLink() {
        return createTable(GeoPackageProperties.getProperty(PropertyConstants.SQL, FeatureTileTableCoreLinker.EXTENSION_NAME_NO_AUTHOR));
    }

    private int createTable(String str) {
        List<String> parseSQLStatements = ResourceIOUtils.parseSQLStatements(GeoPackageProperties.getProperty(PropertyConstants.SQL, "directory"), str);
        for (String execSQL : parseSQLStatements) {
            this.f338db.execSQL(execSQL);
        }
        return parseSQLStatements.size();
    }

    public <TColumn extends UserColumn> void createTable(UserTable<TColumn> userTable) {
        if (!this.f338db.tableExists(userTable.getTableName())) {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ");
            sb.append(CoreSQLUtils.quoteWrap(userTable.getTableName()));
            sb.append(" (");
            List<TColumn> columns = userTable.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                UserColumn userColumn = (UserColumn) columns.get(i);
                if (i > 0) {
                    sb.append(",");
                }
                sb.append("\n  ");
                sb.append(CoreSQLUtils.quoteWrap(userColumn.getName()));
                sb.append(" ");
                sb.append(userColumn.getTypeName());
                if (userColumn.getMax() != null) {
                    sb.append("(");
                    sb.append(userColumn.getMax());
                    sb.append(")");
                }
                if (userColumn.isNotNull()) {
                    sb.append(" NOT NULL");
                }
                if (userColumn.isPrimaryKey()) {
                    sb.append(" PRIMARY KEY AUTOINCREMENT");
                }
            }
            List<UserUniqueConstraint<TColumn>> uniqueConstraints = userTable.getUniqueConstraints();
            for (int i2 = 0; i2 < uniqueConstraints.size(); i2++) {
                sb.append(",\n  UNIQUE (");
                List columns2 = uniqueConstraints.get(i2).getColumns();
                for (int i3 = 0; i3 < columns2.size(); i3++) {
                    UserColumn userColumn2 = (UserColumn) columns2.get(i3);
                    if (i3 > 0) {
                        sb.append(", ");
                    }
                    sb.append(userColumn2.getName());
                }
                sb.append(")");
            }
            sb.append("\n);");
            this.f338db.execSQL(sb.toString());
            return;
        }
        throw new GeoPackageException("Table already exists and can not be created: " + userTable.getTableName());
    }

    public void createRequired() {
        createSpatialReferenceSystem();
        createContents();
        try {
            SpatialReferenceSystemDao spatialReferenceSystemDao = (SpatialReferenceSystemDao) DaoManager.createDao(this.f338db.getConnectionSource(), SpatialReferenceSystem.class);
            spatialReferenceSystemDao.createWgs84();
            spatialReferenceSystemDao.createUndefinedCartesian();
            spatialReferenceSystemDao.createUndefinedGeographic();
        } catch (SQLException e) {
            throw new GeoPackageException("Error creating default required Spatial Reference Systems", e);
        }
    }

    public void dropTable(String str) {
        GeoPackageCoreConnection geoPackageCoreConnection = this.f338db;
        geoPackageCoreConnection.execSQL("DROP TABLE IF EXISTS " + CoreSQLUtils.quoteWrap(str));
    }
}
