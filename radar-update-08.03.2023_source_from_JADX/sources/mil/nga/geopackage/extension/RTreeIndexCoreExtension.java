package mil.nga.geopackage.extension;

import java.sql.SQLException;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.features.user.FeatureColumn;
import mil.nga.geopackage.features.user.FeatureTable;
import mil.nga.geopackage.p009db.GeoPackageCoreConnection;
import mil.nga.geopackage.p010io.ResourceIOUtils;
import mil.nga.geopackage.property.GeoPackageProperties;

public abstract class RTreeIndexCoreExtension extends BaseExtension {
    public static final String CREATE_PROPERTY = "create";
    public static final String DEFINITION = GeoPackageProperties.getProperty(EXTENSION_PROPERTY);
    public static final String DROP_PROPERTY = "drop";
    public static final String EXTENSION_NAME = "gpkg_rtree_index";
    private static final String EXTENSION_PROPERTY = "geopackage.extensions.rtree_index";
    public static final String GEOMETRY_COLUMN_SUBSTITUTE = GeoPackageProperties.getProperty(SUBSTITUTE_PROPERTY, "geometry_column");
    public static final String IS_EMPTY_FUNCTION = "ST_IsEmpty";
    public static final String LOAD_PROPERTY = "load";
    public static final String MAX_X_FUNCTION = "ST_MaxX";
    public static final String MAX_Y_FUNCTION = "ST_MaxY";
    public static final String MIN_X_FUNCTION = "ST_MinX";
    public static final String MIN_Y_FUNCTION = "ST_MinY";
    public static final String NAME = "rtree_index";
    public static final String PK_COLUMN_SUBSTITUTE = GeoPackageProperties.getProperty(SUBSTITUTE_PROPERTY, "pk_column");
    public static final String SQL_DIRECTORY = GeoPackageProperties.getProperty(SQL_PROPERTY, "directory");
    private static final String SQL_PROPERTY = "geopackage.extensions.rtree_index.sql";
    private static final String SUBSTITUTE_PROPERTY = "geopackage.extensions.rtree_index.sql.substitute";
    public static final String TABLE_SUBSTITUTE = GeoPackageProperties.getProperty(SUBSTITUTE_PROPERTY, "table");
    public static final String TRIGGER_DELETE_NAME = "delete";
    public static final String TRIGGER_DROP_PROPERTY = "drop";
    public static final String TRIGGER_INSERT_NAME = "insert";
    private static final String TRIGGER_PROPERTY = "geopackage.extensions.rtree_index.sql.trigger";
    public static final String TRIGGER_SUBSTITUTE = GeoPackageProperties.getProperty(SUBSTITUTE_PROPERTY, "trigger");
    public static final String TRIGGER_UPDATE1_NAME = "update1";
    public static final String TRIGGER_UPDATE2_NAME = "update2";
    public static final String TRIGGER_UPDATE3_NAME = "update3";
    public static final String TRIGGER_UPDATE4_NAME = "update4";
    private GeoPackageCoreConnection connection = null;

    public abstract void createIsEmptyFunction();

    public abstract void createMaxXFunction();

    public abstract void createMaxYFunction();

    public abstract void createMinXFunction();

    public abstract void createMinYFunction();

    protected RTreeIndexCoreExtension(GeoPackageCore geoPackageCore) {
        super(geoPackageCore);
        this.connection = geoPackageCore.getDatabase();
    }

    public Extensions getOrCreate(FeatureTable featureTable) {
        return getOrCreate(featureTable.getTableName(), featureTable.getGeometryColumn().getName());
    }

    public Extensions getOrCreate(String str, String str2) {
        return getOrCreate(EXTENSION_NAME, str, str2, DEFINITION, ExtensionScopeType.WRITE_ONLY);
    }

    public boolean has(FeatureTable featureTable) {
        return has(featureTable.getTableName(), featureTable.getGeometryColumn().getName());
    }

    public boolean has(String str, String str2) {
        return has(EXTENSION_NAME, str, str2);
    }

    public boolean has() {
        return has(EXTENSION_NAME, (String) null, (String) null);
    }

    public boolean createFunctions(FeatureTable featureTable) {
        return createFunctions(featureTable.getTableName(), featureTable.getGeometryColumn().getName());
    }

    public boolean createFunctions(String str, String str2) {
        boolean has = has(str, str2);
        if (has) {
            createAllFunctions();
        }
        return has;
    }

    public boolean createFunctions() {
        boolean has = has();
        if (has) {
            createAllFunctions();
        }
        return has;
    }

    public Extensions create(FeatureTable featureTable) {
        return create(featureTable.getTableName(), featureTable.getGeometryColumn().getName(), ((FeatureColumn) featureTable.getPkColumn()).getName());
    }

    public Extensions create(String str, String str2, String str3) {
        Extensions orCreate = getOrCreate(str, str2);
        createAllFunctions();
        createRTreeIndex(str, str2);
        loadRTreeIndex(str, str2, str3);
        createAllTriggers(str, str2, str3);
        return orCreate;
    }

    public void createRTreeIndex(FeatureTable featureTable) {
        createRTreeIndex(featureTable.getTableName(), featureTable.getGeometryColumn().getName());
    }

    public void createRTreeIndex(String str, String str2) {
        executeSQL(GeoPackageProperties.getProperty(SQL_PROPERTY, CREATE_PROPERTY), str, str2);
    }

    public void createAllFunctions() {
        createMinXFunction();
        createMaxXFunction();
        createMinYFunction();
        createMaxYFunction();
        createIsEmptyFunction();
    }

    public void loadRTreeIndex(FeatureTable featureTable) {
        loadRTreeIndex(featureTable.getTableName(), featureTable.getGeometryColumn().getName(), ((FeatureColumn) featureTable.getPkColumn()).getName());
    }

    public void loadRTreeIndex(String str, String str2, String str3) {
        executeSQL(GeoPackageProperties.getProperty(SQL_PROPERTY, LOAD_PROPERTY), str, str2, str3);
    }

    public void createAllTriggers(FeatureTable featureTable) {
        createAllTriggers(featureTable.getTableName(), featureTable.getGeometryColumn().getName(), ((FeatureColumn) featureTable.getPkColumn()).getName());
    }

    public void createAllTriggers(String str, String str2, String str3) {
        createInsertTrigger(str, str2, str3);
        createUpdate1Trigger(str, str2, str3);
        createUpdate2Trigger(str, str2, str3);
        createUpdate3Trigger(str, str2, str3);
        createUpdate4Trigger(str, str2, str3);
        createDeleteTrigger(str, str2, str3);
    }

    public void createInsertTrigger(String str, String str2, String str3) {
        executeSQL(GeoPackageProperties.getProperty(TRIGGER_PROPERTY, TRIGGER_INSERT_NAME), str, str2, str3);
    }

    public void createUpdate1Trigger(String str, String str2, String str3) {
        executeSQL(GeoPackageProperties.getProperty(TRIGGER_PROPERTY, TRIGGER_UPDATE1_NAME), str, str2, str3);
    }

    public void createUpdate2Trigger(String str, String str2, String str3) {
        executeSQL(GeoPackageProperties.getProperty(TRIGGER_PROPERTY, TRIGGER_UPDATE2_NAME), str, str2, str3);
    }

    public void createUpdate3Trigger(String str, String str2, String str3) {
        executeSQL(GeoPackageProperties.getProperty(TRIGGER_PROPERTY, TRIGGER_UPDATE3_NAME), str, str2, str3);
    }

    public void createUpdate4Trigger(String str, String str2, String str3) {
        executeSQL(GeoPackageProperties.getProperty(TRIGGER_PROPERTY, TRIGGER_UPDATE4_NAME), str, str2, str3);
    }

    public void createDeleteTrigger(String str, String str2, String str3) {
        executeSQL(GeoPackageProperties.getProperty(TRIGGER_PROPERTY, TRIGGER_DELETE_NAME), str, str2, str3);
    }

    public void delete(FeatureTable featureTable) {
        delete(featureTable.getTableName(), featureTable.getGeometryColumn().getName());
    }

    public void delete(String str, String str2) {
        drop(str, str2);
        try {
            this.extensionsDao.deleteByExtension(EXTENSION_NAME, str, str2);
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete RTree Index extension. GeoPackage: " + this.geoPackage.getName() + ", Table: " + str + ", Geometry Column: " + str2, e);
        }
    }

    public void drop(FeatureTable featureTable) {
        drop(featureTable.getTableName(), featureTable.getGeometryColumn().getName());
    }

    public void drop(String str, String str2) {
        dropAllTriggers(str, str2);
        dropRTreeIndex(str, str2);
    }

    public void dropRTreeIndex(FeatureTable featureTable) {
        dropRTreeIndex(featureTable.getTableName(), featureTable.getGeometryColumn().getName());
    }

    public void dropRTreeIndex(String str, String str2) {
        executeSQL(GeoPackageProperties.getProperty(SQL_PROPERTY, "drop"), str, str2);
    }

    public void dropTriggers(FeatureTable featureTable) {
        dropTriggers(featureTable.getTableName(), featureTable.getGeometryColumn().getName());
    }

    public boolean dropTriggers(String str, String str2) {
        boolean has = has(str, str2);
        if (has) {
            dropAllTriggers(str, str2);
        }
        return has;
    }

    public void dropAllTriggers(FeatureTable featureTable) {
        dropAllTriggers(featureTable.getTableName(), featureTable.getGeometryColumn().getName());
    }

    public void dropAllTriggers(String str, String str2) {
        dropInsertTrigger(str, str2);
        dropUpdate1Trigger(str, str2);
        dropUpdate2Trigger(str, str2);
        dropUpdate3Trigger(str, str2);
        dropUpdate4Trigger(str, str2);
        dropDeleteTrigger(str, str2);
    }

    public void dropInsertTrigger(String str, String str2) {
        dropTrigger(str, str2, TRIGGER_INSERT_NAME);
    }

    public void dropUpdate1Trigger(String str, String str2) {
        dropTrigger(str, str2, TRIGGER_UPDATE1_NAME);
    }

    public void dropUpdate2Trigger(String str, String str2) {
        dropTrigger(str, str2, TRIGGER_UPDATE2_NAME);
    }

    public void dropUpdate3Trigger(String str, String str2) {
        dropTrigger(str, str2, TRIGGER_UPDATE3_NAME);
    }

    public void dropUpdate4Trigger(String str, String str2) {
        dropTrigger(str, str2, TRIGGER_UPDATE4_NAME);
    }

    public void dropDeleteTrigger(String str, String str2) {
        dropTrigger(str, str2, TRIGGER_DELETE_NAME);
    }

    public void dropTrigger(String str, String str2, String str3) {
        executeSQL(GeoPackageProperties.getProperty(TRIGGER_PROPERTY, "drop"), str, str2, (String) null, str3);
    }

    private void executeSQL(String str, String str2, String str3) {
        executeSQL(str, str2, str3, (String) null);
    }

    private void executeSQL(String str, String str2, String str3, String str4) {
        executeSQL(str, str2, str3, str4, (String) null);
    }

    private void executeSQL(String str, String str2, String str3, String str4, String str5) {
        for (String substituteSqlArguments : ResourceIOUtils.parseSQLStatements(SQL_DIRECTORY, str)) {
            this.connection.execSQL(substituteSqlArguments(substituteSqlArguments, str2, str3, str4, str5));
        }
    }

    private String substituteSqlArguments(String str, String str2, String str3, String str4, String str5) {
        String replaceAll = str.replaceAll(TABLE_SUBSTITUTE, str2).replaceAll(GEOMETRY_COLUMN_SUBSTITUTE, str3);
        if (str4 != null) {
            replaceAll = replaceAll.replaceAll(PK_COLUMN_SUBSTITUTE, str4);
        }
        return str5 != null ? replaceAll.replaceAll(TRIGGER_SUBSTITUTE, str5) : replaceAll;
    }
}
