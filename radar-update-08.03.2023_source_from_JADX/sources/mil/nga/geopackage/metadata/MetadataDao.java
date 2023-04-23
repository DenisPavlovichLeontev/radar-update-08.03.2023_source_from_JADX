package mil.nga.geopackage.metadata;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.Collection;
import mil.nga.geopackage.metadata.reference.MetadataReference;
import mil.nga.geopackage.metadata.reference.MetadataReferenceDao;

public class MetadataDao extends BaseDaoImpl<Metadata, Long> {
    private MetadataReferenceDao metadataReferenceDao;

    public MetadataDao(ConnectionSource connectionSource, Class<Metadata> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public int deleteCascade(Metadata metadata) throws SQLException {
        if (metadata == null) {
            return 0;
        }
        MetadataReferenceDao metadataReferenceDao2 = getMetadataReferenceDao();
        metadataReferenceDao2.deleteByMetadata(metadata.getId());
        metadataReferenceDao2.removeMetadataParent(metadata.getId());
        return delete(metadata);
    }

    public int deleteCascade(Collection<Metadata> collection) throws SQLException {
        int i = 0;
        if (collection != null) {
            for (Metadata deleteCascade : collection) {
                i += deleteCascade(deleteCascade);
            }
        }
        return i;
    }

    public int deleteCascade(PreparedQuery<Metadata> preparedQuery) throws SQLException {
        if (preparedQuery != null) {
            return deleteCascade((Collection<Metadata>) query(preparedQuery));
        }
        return 0;
    }

    public int deleteByIdCascade(Long l) throws SQLException {
        Metadata metadata;
        if (l == null || (metadata = (Metadata) queryForId(l)) == null) {
            return 0;
        }
        return deleteCascade(metadata);
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

    private MetadataReferenceDao getMetadataReferenceDao() throws SQLException {
        if (this.metadataReferenceDao == null) {
            this.metadataReferenceDao = (MetadataReferenceDao) DaoManager.createDao(this.connectionSource, MetadataReference.class);
        }
        return this.metadataReferenceDao;
    }
}
