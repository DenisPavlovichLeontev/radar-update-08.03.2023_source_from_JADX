package mil.nga.geopackage.extension.index;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.wkb.geom.GeometryEnvelope;

public class GeometryIndexDao extends BaseDaoImpl<GeometryIndex, GeometryIndexKey> {
    public GeometryIndexDao(ConnectionSource connectionSource, Class<GeometryIndex> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public GeometryIndex queryForId(GeometryIndexKey geometryIndexKey) throws SQLException {
        if (geometryIndexKey != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("table_name", geometryIndexKey.getTableName());
            hashMap.put("geom_id", Long.valueOf(geometryIndexKey.getGeomId()));
            List queryForFieldValues = queryForFieldValues(hashMap);
            if (!queryForFieldValues.isEmpty()) {
                if (queryForFieldValues.size() <= 1) {
                    return (GeometryIndex) queryForFieldValues.get(0);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("More than one ");
                Class<GeometryIndex> cls = GeometryIndex.class;
                sb.append("GeometryIndex");
                sb.append(" returned for key. Table Name: ");
                sb.append(geometryIndexKey.getTableName());
                sb.append(", Geom Id: ");
                sb.append(geometryIndexKey.getGeomId());
                throw new SQLException(sb.toString());
            }
        }
        return null;
    }

    public GeometryIndexKey extractId(GeometryIndex geometryIndex) throws SQLException {
        return geometryIndex.getId();
    }

    public boolean idExists(GeometryIndexKey geometryIndexKey) throws SQLException {
        return queryForId(geometryIndexKey) != null;
    }

    public GeometryIndex queryForSameId(GeometryIndex geometryIndex) throws SQLException {
        return queryForId(geometryIndex.getId());
    }

    public int updateId(GeometryIndex geometryIndex, GeometryIndexKey geometryIndexKey) throws SQLException {
        GeometryIndex queryForId = queryForId(geometryIndex.getId());
        if (queryForId == null || geometryIndexKey == null) {
            return 0;
        }
        queryForId.setId(geometryIndexKey);
        return update(queryForId);
    }

    public int delete(GeometryIndex geometryIndex) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        deleteBuilder.where().mo19710eq("table_name", geometryIndex.getTableName()).and().mo19710eq("geom_id", Long.valueOf(geometryIndex.getGeomId()));
        return delete(deleteBuilder.prepare());
    }

    public int deleteById(GeometryIndexKey geometryIndexKey) throws SQLException {
        GeometryIndex queryForId;
        if (geometryIndexKey == null || (queryForId = queryForId(geometryIndexKey)) == null) {
            return 0;
        }
        return delete(queryForId);
    }

    public int deleteIds(Collection<GeometryIndexKey> collection) throws SQLException {
        int i = 0;
        if (collection != null) {
            for (GeometryIndexKey deleteById : collection) {
                i += deleteById(deleteById);
            }
        }
        return i;
    }

    public int update(GeometryIndex geometryIndex) throws SQLException {
        UpdateBuilder updateBuilder = updateBuilder();
        updateBuilder.updateColumnValue("min_x", Double.valueOf(geometryIndex.getMinX()));
        updateBuilder.updateColumnValue("max_x", Double.valueOf(geometryIndex.getMaxX()));
        updateBuilder.updateColumnValue("min_y", Double.valueOf(geometryIndex.getMinY()));
        updateBuilder.updateColumnValue("max_y", Double.valueOf(geometryIndex.getMaxY()));
        updateBuilder.updateColumnValue("min_z", geometryIndex.getMinZ());
        updateBuilder.updateColumnValue("max_z", geometryIndex.getMaxZ());
        updateBuilder.updateColumnValue("min_m", geometryIndex.getMinM());
        updateBuilder.updateColumnValue("max_m", geometryIndex.getMaxM());
        updateBuilder.where().mo19710eq("table_name", geometryIndex.getTableName()).and().mo19710eq("geom_id", Long.valueOf(geometryIndex.getGeomId()));
        return update(updateBuilder.prepare());
    }

    public List<GeometryIndex> queryForTableName(String str) {
        try {
            return queryForEq("table_name", str);
        } catch (SQLException unused) {
            throw new GeoPackageException("Failed to query for Geometry Index objects by Table Name: " + str);
        }
    }

    public GeometryIndex populate(TableIndex tableIndex, long j, GeometryEnvelope geometryEnvelope) {
        GeometryIndex geometryIndex = new GeometryIndex();
        geometryIndex.setTableIndex(tableIndex);
        geometryIndex.setGeomId(j);
        geometryIndex.setMinX(geometryEnvelope.getMinX());
        geometryIndex.setMaxX(geometryEnvelope.getMaxX());
        geometryIndex.setMinY(geometryEnvelope.getMinY());
        geometryIndex.setMaxY(geometryEnvelope.getMaxY());
        if (geometryEnvelope.hasZ()) {
            geometryIndex.setMinZ(geometryEnvelope.getMinZ());
            geometryIndex.setMaxZ(geometryEnvelope.getMaxZ());
        }
        if (geometryEnvelope.hasM()) {
            geometryIndex.setMinM(geometryEnvelope.getMinM());
            geometryIndex.setMaxM(geometryEnvelope.getMaxM());
        }
        return geometryIndex;
    }

    public int deleteAll() throws SQLException {
        if (isTableExists()) {
            return delete(deleteBuilder().prepare());
        }
        return 0;
    }
}
