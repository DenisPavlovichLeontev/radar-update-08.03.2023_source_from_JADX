package mil.nga.geopackage.core.srs;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDao;
import mil.nga.geopackage.extension.CrsWktExtension;
import mil.nga.geopackage.features.columns.GeometryColumns;
import mil.nga.geopackage.features.columns.GeometryColumnsDao;
import mil.nga.geopackage.projection.ProjectionConstants;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSet;
import mil.nga.geopackage.tiles.matrixset.TileMatrixSetDao;

public class SpatialReferenceSystemDao extends BaseDaoImpl<SpatialReferenceSystem, Long> {
    private ContentsDao contentsDao;
    private CrsWktExtension crsWktExtension;
    private GeometryColumnsDao geometryColumnsDao;
    private TileMatrixSetDao tileMatrixSetDao;

    public SpatialReferenceSystemDao(ConnectionSource connectionSource, Class<SpatialReferenceSystem> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public void setCrsWktExtension(CrsWktExtension crsWktExtension2) {
        this.crsWktExtension = crsWktExtension2;
    }

    public boolean hasDefinition_12_063() {
        CrsWktExtension crsWktExtension2 = this.crsWktExtension;
        return crsWktExtension2 != null && crsWktExtension2.has();
    }

    public SpatialReferenceSystem createWgs84() throws SQLException {
        SpatialReferenceSystem spatialReferenceSystem = new SpatialReferenceSystem();
        spatialReferenceSystem.setSrsName(GeoPackageProperties.getProperty(PropertyConstants.WGS_84, "srs_name"));
        spatialReferenceSystem.setSrsId((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.WGS_84, "srs_id"));
        spatialReferenceSystem.setOrganization(GeoPackageProperties.getProperty(PropertyConstants.WGS_84, "organization"));
        spatialReferenceSystem.setOrganizationCoordsysId((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.WGS_84, "organization_coordsys_id"));
        spatialReferenceSystem.setDefinition(GeoPackageProperties.getProperty(PropertyConstants.WGS_84, "definition"));
        spatialReferenceSystem.setDescription(GeoPackageProperties.getProperty(PropertyConstants.WGS_84, "description"));
        create(spatialReferenceSystem);
        if (hasDefinition_12_063()) {
            spatialReferenceSystem.setDefinition_12_063(GeoPackageProperties.getProperty(PropertyConstants.WGS_84, PropertyConstants.DEFINITION_12_063));
            this.crsWktExtension.updateDefinition(spatialReferenceSystem.getSrsId(), spatialReferenceSystem.getDefinition_12_063());
        }
        return spatialReferenceSystem;
    }

    public SpatialReferenceSystem createUndefinedCartesian() throws SQLException {
        SpatialReferenceSystem spatialReferenceSystem = new SpatialReferenceSystem();
        spatialReferenceSystem.setSrsName(GeoPackageProperties.getProperty(PropertyConstants.UNDEFINED_CARTESIAN, "srs_name"));
        spatialReferenceSystem.setSrsId((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.UNDEFINED_CARTESIAN, "srs_id"));
        spatialReferenceSystem.setOrganization(GeoPackageProperties.getProperty(PropertyConstants.UNDEFINED_CARTESIAN, "organization"));
        spatialReferenceSystem.setOrganizationCoordsysId((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.UNDEFINED_CARTESIAN, "organization_coordsys_id"));
        spatialReferenceSystem.setDefinition(GeoPackageProperties.getProperty(PropertyConstants.UNDEFINED_CARTESIAN, "definition"));
        spatialReferenceSystem.setDescription(GeoPackageProperties.getProperty(PropertyConstants.UNDEFINED_CARTESIAN, "description"));
        create(spatialReferenceSystem);
        if (hasDefinition_12_063()) {
            spatialReferenceSystem.setDefinition_12_063(GeoPackageProperties.getProperty(PropertyConstants.UNDEFINED_CARTESIAN, PropertyConstants.DEFINITION_12_063));
            this.crsWktExtension.updateDefinition(spatialReferenceSystem.getSrsId(), spatialReferenceSystem.getDefinition_12_063());
        }
        return spatialReferenceSystem;
    }

    public SpatialReferenceSystem createUndefinedGeographic() throws SQLException {
        SpatialReferenceSystem spatialReferenceSystem = new SpatialReferenceSystem();
        spatialReferenceSystem.setSrsName(GeoPackageProperties.getProperty(PropertyConstants.UNDEFINED_GEOGRAPHIC, "srs_name"));
        spatialReferenceSystem.setSrsId((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.UNDEFINED_GEOGRAPHIC, "srs_id"));
        spatialReferenceSystem.setOrganization(GeoPackageProperties.getProperty(PropertyConstants.UNDEFINED_GEOGRAPHIC, "organization"));
        spatialReferenceSystem.setOrganizationCoordsysId((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.UNDEFINED_GEOGRAPHIC, "organization_coordsys_id"));
        spatialReferenceSystem.setDefinition(GeoPackageProperties.getProperty(PropertyConstants.UNDEFINED_GEOGRAPHIC, "definition"));
        spatialReferenceSystem.setDescription(GeoPackageProperties.getProperty(PropertyConstants.UNDEFINED_GEOGRAPHIC, "description"));
        create(spatialReferenceSystem);
        if (hasDefinition_12_063()) {
            spatialReferenceSystem.setDefinition_12_063(GeoPackageProperties.getProperty(PropertyConstants.UNDEFINED_GEOGRAPHIC, PropertyConstants.DEFINITION_12_063));
            this.crsWktExtension.updateDefinition(spatialReferenceSystem.getSrsId(), spatialReferenceSystem.getDefinition_12_063());
        }
        return spatialReferenceSystem;
    }

    public SpatialReferenceSystem createWebMercator() throws SQLException {
        SpatialReferenceSystem spatialReferenceSystem = new SpatialReferenceSystem();
        spatialReferenceSystem.setSrsName(GeoPackageProperties.getProperty(PropertyConstants.WEB_MERCATOR, "srs_name"));
        spatialReferenceSystem.setSrsId((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.WEB_MERCATOR, "srs_id"));
        spatialReferenceSystem.setOrganization(GeoPackageProperties.getProperty(PropertyConstants.WEB_MERCATOR, "organization"));
        spatialReferenceSystem.setOrganizationCoordsysId((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.WEB_MERCATOR, "organization_coordsys_id"));
        spatialReferenceSystem.setDefinition(GeoPackageProperties.getProperty(PropertyConstants.WEB_MERCATOR, "definition"));
        spatialReferenceSystem.setDescription(GeoPackageProperties.getProperty(PropertyConstants.WEB_MERCATOR, "description"));
        create(spatialReferenceSystem);
        if (hasDefinition_12_063()) {
            spatialReferenceSystem.setDefinition_12_063(GeoPackageProperties.getProperty(PropertyConstants.WEB_MERCATOR, PropertyConstants.DEFINITION_12_063));
            this.crsWktExtension.updateDefinition(spatialReferenceSystem.getSrsId(), spatialReferenceSystem.getDefinition_12_063());
        }
        return spatialReferenceSystem;
    }

    public SpatialReferenceSystem createWgs84Geographical3D() throws SQLException {
        SpatialReferenceSystem spatialReferenceSystem = new SpatialReferenceSystem();
        spatialReferenceSystem.setSrsName(GeoPackageProperties.getProperty(PropertyConstants.WGS_84_3D, "srs_name"));
        spatialReferenceSystem.setSrsId((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.WGS_84_3D, "srs_id"));
        spatialReferenceSystem.setOrganization(GeoPackageProperties.getProperty(PropertyConstants.WGS_84_3D, "organization"));
        spatialReferenceSystem.setOrganizationCoordsysId((long) GeoPackageProperties.getIntegerProperty(PropertyConstants.WGS_84_3D, "organization_coordsys_id"));
        spatialReferenceSystem.setDefinition(GeoPackageProperties.getProperty(PropertyConstants.WGS_84_3D, "definition"));
        spatialReferenceSystem.setDescription(GeoPackageProperties.getProperty(PropertyConstants.WGS_84_3D, "description"));
        create(spatialReferenceSystem);
        if (hasDefinition_12_063()) {
            spatialReferenceSystem.setDefinition_12_063(GeoPackageProperties.getProperty(PropertyConstants.WGS_84_3D, PropertyConstants.DEFINITION_12_063));
            this.crsWktExtension.updateDefinition(spatialReferenceSystem.getSrsId(), spatialReferenceSystem.getDefinition_12_063());
        }
        return spatialReferenceSystem;
    }

    public String getDefinition_12_063(long j) {
        if (hasDefinition_12_063()) {
            return this.crsWktExtension.getDefinition(j);
        }
        return null;
    }

    public void setDefinition_12_063(SpatialReferenceSystem spatialReferenceSystem) {
        String definition_12_063;
        if (spatialReferenceSystem != null && (definition_12_063 = getDefinition_12_063(spatialReferenceSystem.getSrsId())) != null) {
            spatialReferenceSystem.setDefinition_12_063(definition_12_063);
        }
    }

    public void setDefinition_12_063(Collection<SpatialReferenceSystem> collection) {
        for (SpatialReferenceSystem definition_12_063 : collection) {
            setDefinition_12_063(definition_12_063);
        }
    }

    public void updateDefinition_12_063(long j, String str) {
        if (hasDefinition_12_063()) {
            this.crsWktExtension.updateDefinition(j, str);
        }
    }

    public void updateDefinition_12_063(SpatialReferenceSystem spatialReferenceSystem) {
        String definition_12_063;
        if (spatialReferenceSystem != null && (definition_12_063 = spatialReferenceSystem.getDefinition_12_063()) != null) {
            updateDefinition_12_063(spatialReferenceSystem.getSrsId(), definition_12_063);
        }
    }

    public SpatialReferenceSystem queryForId(Long l) throws SQLException {
        SpatialReferenceSystem spatialReferenceSystem = (SpatialReferenceSystem) super.queryForId(l);
        setDefinition_12_063(spatialReferenceSystem);
        return spatialReferenceSystem;
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [com.j256.ormlite.stmt.PreparedQuery<mil.nga.geopackage.core.srs.SpatialReferenceSystem>, com.j256.ormlite.stmt.PreparedQuery] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public mil.nga.geopackage.core.srs.SpatialReferenceSystem queryForFirst(com.j256.ormlite.stmt.PreparedQuery<mil.nga.geopackage.core.srs.SpatialReferenceSystem> r1) throws java.sql.SQLException {
        /*
            r0 = this;
            java.lang.Object r1 = super.queryForFirst(r1)
            mil.nga.geopackage.core.srs.SpatialReferenceSystem r1 = (mil.nga.geopackage.core.srs.SpatialReferenceSystem) r1
            r0.setDefinition_12_063((mil.nga.geopackage.core.srs.SpatialReferenceSystem) r1)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.core.srs.SpatialReferenceSystemDao.queryForFirst(com.j256.ormlite.stmt.PreparedQuery):mil.nga.geopackage.core.srs.SpatialReferenceSystem");
    }

    public List<SpatialReferenceSystem> queryForAll() throws SQLException {
        List<SpatialReferenceSystem> queryForAll = super.queryForAll();
        setDefinition_12_063((Collection<SpatialReferenceSystem>) queryForAll);
        return queryForAll;
    }

    public List<SpatialReferenceSystem> queryForEq(String str, Object obj) throws SQLException {
        List<SpatialReferenceSystem> queryForEq = super.queryForEq(str, obj);
        setDefinition_12_063((Collection<SpatialReferenceSystem>) queryForEq);
        return queryForEq;
    }

    public List<SpatialReferenceSystem> query(PreparedQuery<SpatialReferenceSystem> preparedQuery) throws SQLException {
        List<SpatialReferenceSystem> query = super.query(preparedQuery);
        setDefinition_12_063((Collection<SpatialReferenceSystem>) query);
        return query;
    }

    public List<SpatialReferenceSystem> queryForMatching(SpatialReferenceSystem spatialReferenceSystem) throws SQLException {
        List<SpatialReferenceSystem> queryForMatching = super.queryForMatching(spatialReferenceSystem);
        setDefinition_12_063((Collection<SpatialReferenceSystem>) queryForMatching);
        return queryForMatching;
    }

    public List<SpatialReferenceSystem> queryForMatchingArgs(SpatialReferenceSystem spatialReferenceSystem) throws SQLException {
        List<SpatialReferenceSystem> queryForMatchingArgs = super.queryForMatchingArgs(spatialReferenceSystem);
        setDefinition_12_063((Collection<SpatialReferenceSystem>) queryForMatchingArgs);
        return queryForMatchingArgs;
    }

    public List<SpatialReferenceSystem> queryForFieldValues(Map<String, Object> map) throws SQLException {
        List<SpatialReferenceSystem> queryForFieldValues = super.queryForFieldValues(map);
        setDefinition_12_063((Collection<SpatialReferenceSystem>) queryForFieldValues);
        return queryForFieldValues;
    }

    public List<SpatialReferenceSystem> queryForFieldValuesArgs(Map<String, Object> map) throws SQLException {
        List<SpatialReferenceSystem> queryForFieldValuesArgs = super.queryForFieldValuesArgs(map);
        setDefinition_12_063((Collection<SpatialReferenceSystem>) queryForFieldValuesArgs);
        return queryForFieldValuesArgs;
    }

    public SpatialReferenceSystem queryForSameId(SpatialReferenceSystem spatialReferenceSystem) throws SQLException {
        SpatialReferenceSystem spatialReferenceSystem2 = (SpatialReferenceSystem) super.queryForSameId(spatialReferenceSystem);
        setDefinition_12_063(spatialReferenceSystem2);
        return spatialReferenceSystem2;
    }

    public int create(SpatialReferenceSystem spatialReferenceSystem) throws SQLException {
        int create = super.create(spatialReferenceSystem);
        updateDefinition_12_063(spatialReferenceSystem);
        return create;
    }

    public SpatialReferenceSystem createIfNotExists(SpatialReferenceSystem spatialReferenceSystem) throws SQLException {
        SpatialReferenceSystem spatialReferenceSystem2 = (SpatialReferenceSystem) super.createIfNotExists(spatialReferenceSystem);
        updateDefinition_12_063(spatialReferenceSystem2);
        return spatialReferenceSystem2;
    }

    public Dao.CreateOrUpdateStatus createOrUpdate(SpatialReferenceSystem spatialReferenceSystem) throws SQLException {
        Dao.CreateOrUpdateStatus createOrUpdate = super.createOrUpdate(spatialReferenceSystem);
        updateDefinition_12_063(spatialReferenceSystem);
        return createOrUpdate;
    }

    public int update(SpatialReferenceSystem spatialReferenceSystem) throws SQLException {
        int update = super.update(spatialReferenceSystem);
        updateDefinition_12_063(spatialReferenceSystem);
        return update;
    }

    public SpatialReferenceSystem getOrCreateFromEpsg(long j) throws SQLException {
        return getOrCreateCode(ProjectionConstants.AUTHORITY_EPSG, j);
    }

    public SpatialReferenceSystem getOrCreateCode(String str, long j) throws SQLException {
        return createIfNeeded(queryForOrganizationCoordsysId(str, j), str, j);
    }

    public SpatialReferenceSystem queryForOrganizationCoordsysId(String str, long j) throws SQLException {
        QueryBuilder queryBuilder = queryBuilder();
        queryBuilder.where().like("organization", str);
        queryBuilder.where().mo19710eq("organization_coordsys_id", Long.valueOf(j));
        List<SpatialReferenceSystem> query = query(queryBuilder.prepare());
        if (query.isEmpty()) {
            return null;
        }
        if (query.size() <= 1) {
            return query.get(0);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("More than one ");
        Class<SpatialReferenceSystem> cls = SpatialReferenceSystem.class;
        sb.append("SpatialReferenceSystem");
        sb.append(" returned for Organization: ");
        sb.append(str);
        sb.append(", Organization Coordsys Id: ");
        sb.append(j);
        throw new SQLException(sb.toString());
    }

    private SpatialReferenceSystem createIfNeeded(SpatialReferenceSystem spatialReferenceSystem, String str, long j) throws SQLException {
        if (spatialReferenceSystem != null) {
            setDefinition_12_063(spatialReferenceSystem);
            return spatialReferenceSystem;
        } else if (str.equalsIgnoreCase(ProjectionConstants.AUTHORITY_EPSG)) {
            int i = (int) j;
            if (i == 3857) {
                return createWebMercator();
            }
            if (i == 4326) {
                return createWgs84();
            }
            if (i == 4979) {
                return createWgs84Geographical3D();
            }
            throw new GeoPackageException("Spatial Reference System not supported for metadata creation. Organization: " + str + ", id: " + j);
        } else if (str.equalsIgnoreCase(ProjectionConstants.AUTHORITY_NONE)) {
            int i2 = (int) j;
            if (i2 == -1) {
                return createUndefinedCartesian();
            }
            if (i2 == 0) {
                return createUndefinedGeographic();
            }
            throw new GeoPackageException("Spatial Reference System not supported for metadata creation. Organization: " + str + ", id: " + j);
        } else {
            throw new GeoPackageException("Spatial Reference System not supported for metadata creation. Organization: " + str + ", id: " + j);
        }
    }

    public int deleteCascade(SpatialReferenceSystem spatialReferenceSystem) throws SQLException {
        if (spatialReferenceSystem == null) {
            return 0;
        }
        ForeignCollection<Contents> contents = spatialReferenceSystem.getContents();
        if (!contents.isEmpty()) {
            getContentsDao().deleteCascade((Collection<Contents>) contents);
        }
        GeometryColumnsDao geometryColumnsDao2 = getGeometryColumnsDao();
        if (geometryColumnsDao2.isTableExists()) {
            ForeignCollection<GeometryColumns> geometryColumns = spatialReferenceSystem.getGeometryColumns();
            if (!geometryColumns.isEmpty()) {
                geometryColumnsDao2.delete(geometryColumns);
            }
        }
        TileMatrixSetDao tileMatrixSetDao2 = getTileMatrixSetDao();
        if (tileMatrixSetDao2.isTableExists()) {
            ForeignCollection<TileMatrixSet> tileMatrixSet = spatialReferenceSystem.getTileMatrixSet();
            if (!tileMatrixSet.isEmpty()) {
                tileMatrixSetDao2.delete(tileMatrixSet);
            }
        }
        return delete(spatialReferenceSystem);
    }

    public int deleteCascade(Collection<SpatialReferenceSystem> collection) throws SQLException {
        int i = 0;
        if (collection != null) {
            for (SpatialReferenceSystem deleteCascade : collection) {
                i += deleteCascade(deleteCascade);
            }
        }
        return i;
    }

    public int deleteCascade(PreparedQuery<SpatialReferenceSystem> preparedQuery) throws SQLException {
        if (preparedQuery != null) {
            return deleteCascade((Collection<SpatialReferenceSystem>) query(preparedQuery));
        }
        return 0;
    }

    public int deleteByIdCascade(Long l) throws SQLException {
        SpatialReferenceSystem queryForId;
        if (l == null || (queryForId = queryForId(l)) == null) {
            return 0;
        }
        return deleteCascade(queryForId);
    }

    public int deleteIdsCascade(Collection<Long> collection) throws SQLException {
        int i = 0;
        if (collection != null) {
            for (Long deleteByIdCascade : collection) {
                i += deleteByIdCascade(deleteByIdCascade);
            }
        }
        return i;
    }

    private ContentsDao getContentsDao() throws SQLException {
        if (this.contentsDao == null) {
            this.contentsDao = (ContentsDao) DaoManager.createDao(this.connectionSource, Contents.class);
        }
        return this.contentsDao;
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
}
