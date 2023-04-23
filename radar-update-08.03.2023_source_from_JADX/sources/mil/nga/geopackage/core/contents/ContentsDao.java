package mil.nga.geopackage.core.contents;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.geopackage.p009db.CoreSQLUtils;
import mil.nga.geopackage.p009db.GeoPackageCoreConnection;
import mil.nga.geopackage.tiles.matrix.TileMatrix;
import mil.nga.geopackage.tiles.matrix.TileMatrixDao;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSetDao;

public class ContentsDao extends BaseDaoImpl<Contents, String> {

    /* renamed from: db */
    private GeoPackageCoreConnection f334db;
    private GeometryColumnsDao geometryColumnsDao;
    private TileMatrixDao tileMatrixDao;
    private TileMatrixSetDao tileMatrixSetDao;

    public ContentsDao(ConnectionSource connectionSource, Class<Contents> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public void setDatabase(GeoPackageCoreConnection geoPackageCoreConnection) {
        this.f334db = geoPackageCoreConnection;
    }

    public int create(Contents contents) throws SQLException {
        verifyCreate(contents);
        return super.create(contents);
    }

    public Contents createIfNotExists(Contents contents) throws SQLException {
        verifyCreate(contents);
        return (Contents) super.createIfNotExists(contents);
    }

    public Dao.CreateOrUpdateStatus createOrUpdate(Contents contents) throws SQLException {
        verifyCreate(contents);
        return super.createOrUpdate(contents);
    }

    public List<String> getTables(ContentsDataType contentsDataType) throws SQLException {
        List<Contents> contents = getContents(contentsDataType);
        ArrayList arrayList = new ArrayList();
        for (Contents tableName : contents) {
            arrayList.add(tableName.getTableName());
        }
        return arrayList;
    }

    public List<Contents> getContents(ContentsDataType contentsDataType) throws SQLException {
        return queryForEq(Contents.COLUMN_DATA_TYPE, contentsDataType.getName());
    }

    public List<String> getTables() throws SQLException {
        List<Contents> queryForAll = queryForAll();
        ArrayList arrayList = new ArrayList();
        for (Contents tableName : queryForAll) {
            arrayList.add(tableName.getTableName());
        }
        return arrayList;
    }

    public int deleteCascade(Contents contents) throws SQLException {
        TileMatrixSet tileMatrixSet;
        GeometryColumns geometryColumns;
        if (contents == null) {
            return 0;
        }
        GeometryColumnsDao geometryColumnsDao2 = getGeometryColumnsDao();
        if (geometryColumnsDao2.isTableExists() && (geometryColumns = contents.getGeometryColumns()) != null) {
            geometryColumnsDao2.delete(geometryColumns);
        }
        TileMatrixDao tileMatrixDao2 = getTileMatrixDao();
        if (tileMatrixDao2.isTableExists()) {
            ForeignCollection<TileMatrix> tileMatrix = contents.getTileMatrix();
            if (!tileMatrix.isEmpty()) {
                tileMatrixDao2.delete(tileMatrix);
            }
        }
        TileMatrixSetDao tileMatrixSetDao2 = getTileMatrixSetDao();
        if (tileMatrixSetDao2.isTableExists() && (tileMatrixSet = contents.getTileMatrixSet()) != null) {
            tileMatrixSetDao2.delete(tileMatrixSet);
        }
        return delete(contents);
    }

    public int deleteCascade(Contents contents, boolean z) throws SQLException {
        int deleteCascade = deleteCascade(contents);
        if (z) {
            GeoPackageCoreConnection geoPackageCoreConnection = this.f334db;
            geoPackageCoreConnection.execSQL("DROP TABLE IF EXISTS " + CoreSQLUtils.quoteWrap(contents.getTableName()));
        }
        return deleteCascade;
    }

    public int deleteCascade(Collection<Contents> collection) throws SQLException {
        return deleteCascade(collection, false);
    }

    public int deleteCascade(Collection<Contents> collection, boolean z) throws SQLException {
        int i = 0;
        if (collection != null) {
            for (Contents deleteCascade : collection) {
                i += deleteCascade(deleteCascade, z);
            }
        }
        return i;
    }

    public int deleteCascade(PreparedQuery<Contents> preparedQuery) throws SQLException {
        return deleteCascade(preparedQuery, false);
    }

    public int deleteCascade(PreparedQuery<Contents> preparedQuery, boolean z) throws SQLException {
        if (preparedQuery != null) {
            return deleteCascade((Collection<Contents>) query(preparedQuery), z);
        }
        return 0;
    }

    public int deleteByIdCascade(String str) throws SQLException {
        return deleteByIdCascade(str, false);
    }

    public int deleteByIdCascade(String str, boolean z) throws SQLException {
        if (str != null) {
            Contents contents = (Contents) queryForId(str);
            if (contents != null) {
                return deleteCascade(contents, z);
            }
            if (z) {
                GeoPackageCoreConnection geoPackageCoreConnection = this.f334db;
                geoPackageCoreConnection.execSQL("DROP TABLE IF EXISTS " + CoreSQLUtils.quoteWrap(str));
            }
        }
        return 0;
    }

    public int deleteIdsCascade(Collection<String> collection) throws SQLException {
        return deleteIdsCascade(collection, false);
    }

    public int deleteIdsCascade(Collection<String> collection, boolean z) throws SQLException {
        int i = 0;
        if (collection != null) {
            for (String deleteByIdCascade : collection) {
                i += deleteByIdCascade(deleteByIdCascade, z);
            }
        }
        return i;
    }

    public void deleteTable(String str) {
        try {
            deleteByIdCascade(str, true);
        } catch (SQLException e) {
            throw new GeoPackageException("Failed to delete table: " + str, e);
        }
    }

    private void verifyCreate(Contents contents) throws SQLException {
        ContentsDataType dataType = contents.getDataType();
        if (dataType != null) {
            int i = C11611.$SwitchMap$mil$nga$geopackage$core$contents$ContentsDataType[dataType.ordinal()];
            if (i != 1) {
                if (i == 2) {
                    verifyTiles(dataType);
                } else if (i == 3) {
                    verifyTiles(dataType);
                } else if (i != 4) {
                    throw new GeoPackageException("Unsupported data type: " + dataType);
                }
            } else if (!getGeometryColumnsDao().isTableExists()) {
                StringBuilder sb = new StringBuilder();
                sb.append("A data type of ");
                sb.append(dataType.getName());
                sb.append(" requires the ");
                Class<GeometryColumns> cls = GeometryColumns.class;
                sb.append("GeometryColumns");
                sb.append(" table to first be created using the GeoPackage.");
                throw new GeoPackageException(sb.toString());
            }
        }
        if (!this.f334db.tableExists(contents.getTableName())) {
            throw new GeoPackageException("No table exists for Content Table Name: " + contents.getTableName() + ". Table must first be created.");
        }
    }

    /* renamed from: mil.nga.geopackage.core.contents.ContentsDao$1 */
    static /* synthetic */ class C11611 {
        static final /* synthetic */ int[] $SwitchMap$mil$nga$geopackage$core$contents$ContentsDataType;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                mil.nga.geopackage.core.contents.ContentsDataType[] r0 = mil.nga.geopackage.core.contents.ContentsDataType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$mil$nga$geopackage$core$contents$ContentsDataType = r0
                mil.nga.geopackage.core.contents.ContentsDataType r1 = mil.nga.geopackage.core.contents.ContentsDataType.FEATURES     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$mil$nga$geopackage$core$contents$ContentsDataType     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.geopackage.core.contents.ContentsDataType r1 = mil.nga.geopackage.core.contents.ContentsDataType.TILES     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$mil$nga$geopackage$core$contents$ContentsDataType     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.geopackage.core.contents.ContentsDataType r1 = mil.nga.geopackage.core.contents.ContentsDataType.GRIDDED_COVERAGE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$mil$nga$geopackage$core$contents$ContentsDataType     // Catch:{ NoSuchFieldError -> 0x0033 }
                mil.nga.geopackage.core.contents.ContentsDataType r1 = mil.nga.geopackage.core.contents.ContentsDataType.ATTRIBUTES     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.core.contents.ContentsDao.C11611.<clinit>():void");
        }
    }

    private void verifyTiles(ContentsDataType contentsDataType) throws SQLException {
        if (!getTileMatrixSetDao().isTableExists()) {
            StringBuilder sb = new StringBuilder();
            sb.append("A data type of ");
            sb.append(contentsDataType.getName());
            sb.append(" requires the ");
            Class<TileMatrixSet> cls = TileMatrixSet.class;
            sb.append("TileMatrixSet");
            sb.append(" table to first be created using the GeoPackage.");
            throw new GeoPackageException(sb.toString());
        } else if (!getTileMatrixDao().isTableExists()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("A data type of ");
            sb2.append(contentsDataType.getName());
            sb2.append(" requires the ");
            Class<TileMatrix> cls2 = TileMatrix.class;
            sb2.append("TileMatrix");
            sb2.append(" table to first be created using the GeoPackage.");
            throw new GeoPackageException(sb2.toString());
        }
    }

    private GeometryColumnsDao getGeometryColumnsDao() throws SQLException {
        if (this.geometryColumnsDao == null) {
            this.geometryColumnsDao = (GeometryColumnsDao) DaoManager.createDao(this.connectionSource, GeometryColumns.class);
        }
        return this.geometryColumnsDao;
    }

    private TileMatrixSetDao getTileMatrixSetDao() throws SQLException {
        if (this.tileMatrixSetDao == null) {
            this.tileMatrixSetDao = (TileMatrixSetDao) DaoManager.createDao(this.connectionSource, TileMatrixSet.class);
        }
        return this.tileMatrixSetDao;
    }

    private TileMatrixDao getTileMatrixDao() throws SQLException {
        if (this.tileMatrixDao == null) {
            this.tileMatrixDao = (TileMatrixDao) DaoManager.createDao(this.connectionSource, TileMatrix.class);
        }
        return this.tileMatrixDao;
    }
}
