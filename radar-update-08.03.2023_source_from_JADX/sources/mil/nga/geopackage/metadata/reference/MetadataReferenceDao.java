package mil.nga.geopackage.metadata.reference;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;

public class MetadataReferenceDao extends BaseDaoImpl<MetadataReference, Void> {
    public MetadataReferenceDao(ConnectionSource connectionSource, Class<MetadataReference> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public int update(MetadataReference metadataReference) throws SQLException {
        UpdateBuilder updateBuilder = updateBuilder();
        updateBuilder.updateColumnValue(MetadataReference.COLUMN_REFERENCE_SCOPE, metadataReference.getReferenceScope().getValue());
        updateBuilder.updateColumnValue("table_name", metadataReference.getTableName());
        updateBuilder.updateColumnValue("column_name", metadataReference.getColumnName());
        updateBuilder.updateColumnValue(MetadataReference.COLUMN_ROW_ID_VALUE, metadataReference.getRowIdValue());
        updateBuilder.updateColumnValue(MetadataReference.COLUMN_TIMESTAMP, metadataReference.getTimestamp());
        setFkWhere(updateBuilder.where(), metadataReference.getFileId(), metadataReference.getParentId());
        return update(updateBuilder.prepare());
    }

    public int delete(MetadataReference metadataReference) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        setFkWhere(deleteBuilder.where(), metadataReference.getFileId(), metadataReference.getParentId());
        return deleteBuilder.delete();
    }

    public int deleteByMetadata(long j) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        deleteBuilder.where().mo19710eq(MetadataReference.COLUMN_FILE_ID, Long.valueOf(j));
        return deleteBuilder.delete();
    }

    public int removeMetadataParent(long j) throws SQLException {
        UpdateBuilder updateBuilder = updateBuilder();
        updateBuilder.updateColumnValue(MetadataReference.COLUMN_PARENT_ID, (Object) null);
        updateBuilder.where().mo19710eq(MetadataReference.COLUMN_PARENT_ID, Long.valueOf(j));
        return update(updateBuilder.prepare());
    }

    public List<MetadataReference> queryByMetadata(long j, Long l) throws SQLException {
        QueryBuilder queryBuilder = queryBuilder();
        setFkWhere(queryBuilder.where(), j, l);
        return queryBuilder.query();
    }

    public List<MetadataReference> queryByMetadata(long j) throws SQLException {
        QueryBuilder queryBuilder = queryBuilder();
        queryBuilder.where().mo19710eq(MetadataReference.COLUMN_FILE_ID, Long.valueOf(j));
        return queryBuilder.query();
    }

    public List<MetadataReference> queryByMetadataParent(long j) throws SQLException {
        QueryBuilder queryBuilder = queryBuilder();
        queryBuilder.where().mo19710eq(MetadataReference.COLUMN_PARENT_ID, Long.valueOf(j));
        return queryBuilder.query();
    }

    private void setFkWhere(Where<MetadataReference, Void> where, long j, Long l) throws SQLException {
        where.mo19710eq(MetadataReference.COLUMN_FILE_ID, Long.valueOf(j));
        if (l == null) {
            where.and().isNull(MetadataReference.COLUMN_PARENT_ID);
        } else {
            where.and().mo19710eq(MetadataReference.COLUMN_PARENT_ID, l);
        }
    }
}
