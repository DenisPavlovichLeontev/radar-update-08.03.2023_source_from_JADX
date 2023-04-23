package mil.nga.geopackage.extension.link;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import mil.nga.geopackage.GeoPackageException;

public class FeatureTileLinkDao extends BaseDaoImpl<FeatureTileLink, FeatureTileLinkKey> {
    public FeatureTileLinkDao(ConnectionSource connectionSource, Class<FeatureTileLink> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public FeatureTileLink queryForId(FeatureTileLinkKey featureTileLinkKey) throws SQLException {
        if (featureTileLinkKey != null) {
            HashMap hashMap = new HashMap();
            hashMap.put(FeatureTileLink.COLUMN_FEATURE_TABLE_NAME, featureTileLinkKey.getFeatureTableName());
            hashMap.put(FeatureTileLink.COLUMN_TILE_TABLE_NAME, featureTileLinkKey.getTileTableName());
            List queryForFieldValues = queryForFieldValues(hashMap);
            if (!queryForFieldValues.isEmpty()) {
                if (queryForFieldValues.size() <= 1) {
                    return (FeatureTileLink) queryForFieldValues.get(0);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("More than one ");
                Class<FeatureTileLink> cls = FeatureTileLink.class;
                sb.append("FeatureTileLink");
                sb.append(" returned for key. Feature Table Name: ");
                sb.append(featureTileLinkKey.getFeatureTableName());
                sb.append(", Tile Table Name: ");
                sb.append(featureTileLinkKey.getTileTableName());
                throw new SQLException(sb.toString());
            }
        }
        return null;
    }

    public FeatureTileLinkKey extractId(FeatureTileLink featureTileLink) throws SQLException {
        return featureTileLink.getId();
    }

    public boolean idExists(FeatureTileLinkKey featureTileLinkKey) throws SQLException {
        return queryForId(featureTileLinkKey) != null;
    }

    public FeatureTileLink queryForSameId(FeatureTileLink featureTileLink) throws SQLException {
        return queryForId(featureTileLink.getId());
    }

    public int updateId(FeatureTileLink featureTileLink, FeatureTileLinkKey featureTileLinkKey) throws SQLException {
        FeatureTileLink queryForId = queryForId(featureTileLink.getId());
        if (queryForId == null || featureTileLinkKey == null) {
            return 0;
        }
        queryForId.setId(featureTileLinkKey);
        return update(queryForId);
    }

    public int delete(FeatureTileLink featureTileLink) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        deleteBuilder.where().mo19710eq(FeatureTileLink.COLUMN_FEATURE_TABLE_NAME, featureTileLink.getFeatureTableName()).and().mo19710eq(FeatureTileLink.COLUMN_TILE_TABLE_NAME, featureTileLink.getTileTableName());
        return delete(deleteBuilder.prepare());
    }

    public int deleteById(FeatureTileLinkKey featureTileLinkKey) throws SQLException {
        FeatureTileLink queryForId;
        if (featureTileLinkKey == null || (queryForId = queryForId(featureTileLinkKey)) == null) {
            return 0;
        }
        return delete(queryForId);
    }

    public int deleteIds(Collection<FeatureTileLinkKey> collection) throws SQLException {
        int i = 0;
        if (collection != null) {
            for (FeatureTileLinkKey deleteById : collection) {
                i += deleteById(deleteById);
            }
        }
        return i;
    }

    public int update(FeatureTileLink featureTileLink) throws SQLException {
        UpdateBuilder updateBuilder = updateBuilder();
        updateBuilder.where().mo19710eq(FeatureTileLink.COLUMN_FEATURE_TABLE_NAME, featureTileLink.getFeatureTableName()).and().mo19710eq(FeatureTileLink.COLUMN_TILE_TABLE_NAME, featureTileLink.getTileTableName());
        return update(updateBuilder.prepare());
    }

    public List<FeatureTileLink> queryForFeatureTableName(String str) {
        try {
            return queryForEq(FeatureTileLink.COLUMN_FEATURE_TABLE_NAME, str);
        } catch (SQLException unused) {
            throw new GeoPackageException("Failed to query for Feature Tile Link objects by Feature Table Name: " + str);
        }
    }

    public List<FeatureTileLink> queryForTileTableName(String str) {
        try {
            return queryForEq(FeatureTileLink.COLUMN_TILE_TABLE_NAME, str);
        } catch (SQLException unused) {
            throw new GeoPackageException("Failed to query for Feature Tile Link objects by Tile Table Name: " + str);
        }
    }

    public int deleteByTableName(String str) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        deleteBuilder.where().mo19710eq(FeatureTileLink.COLUMN_FEATURE_TABLE_NAME, str).mo19732or().mo19710eq(FeatureTileLink.COLUMN_TILE_TABLE_NAME, str);
        return delete(deleteBuilder.prepare());
    }

    public int deleteAll() throws SQLException {
        if (isTableExists()) {
            return delete(deleteBuilder().prepare());
        }
        return 0;
    }
}
