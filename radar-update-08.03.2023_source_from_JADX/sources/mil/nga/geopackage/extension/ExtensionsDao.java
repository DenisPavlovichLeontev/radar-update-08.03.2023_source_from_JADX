package mil.nga.geopackage.extension;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;
import mil.nga.geopackage.GeoPackageException;

public class ExtensionsDao extends BaseDaoImpl<Extensions, Void> {
    public ExtensionsDao(ConnectionSource connectionSource, Class<Extensions> cls) throws SQLException {
        super(connectionSource, cls);
    }

    public int update(Extensions extensions) throws SQLException {
        UpdateBuilder updateBuilder = updateBuilder();
        updateBuilder.updateColumnValue("definition", extensions.getDefinition());
        updateBuilder.updateColumnValue(Extensions.COLUMN_SCOPE, extensions.getScope().getValue());
        setUniqueWhere(updateBuilder.where(), extensions.getExtensionName(), true, extensions.getTableName(), true, extensions.getColumnName());
        return update(updateBuilder.prepare());
    }

    public int delete(Extensions extensions) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        setUniqueWhere(deleteBuilder.where(), extensions.getExtensionName(), true, extensions.getTableName(), true, extensions.getColumnName());
        return deleteBuilder.delete();
    }

    public int deleteByExtension(String str) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        deleteBuilder.where().mo19710eq(Extensions.COLUMN_EXTENSION_NAME, str);
        return deleteBuilder.delete();
    }

    public int deleteByExtension(String str, String str2) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        setUniqueWhere(deleteBuilder.where(), str, true, str2, false, (String) null);
        return deleteBuilder.delete();
    }

    public int deleteByExtension(String str, String str2, String str3) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        setUniqueWhere(deleteBuilder.where(), str, true, str2, true, str3);
        return deleteBuilder.delete();
    }

    public int deleteByTableName(String str) throws SQLException {
        DeleteBuilder deleteBuilder = deleteBuilder();
        deleteBuilder.where().mo19710eq("table_name", str);
        return deleteBuilder.delete();
    }

    public int deleteAll() throws SQLException {
        return deleteBuilder().delete();
    }

    public List<Extensions> queryByExtension(String str) throws SQLException {
        QueryBuilder queryBuilder = queryBuilder();
        setUniqueWhere(queryBuilder.where(), str, false, (String) null, false, (String) null);
        return queryBuilder.query();
    }

    public List<Extensions> queryByExtension(String str, String str2) throws SQLException {
        QueryBuilder queryBuilder = queryBuilder();
        setUniqueWhere(queryBuilder.where(), str, true, str2, false, (String) null);
        return queryBuilder.query();
    }

    public Extensions queryByExtension(String str, String str2, String str3) throws SQLException {
        QueryBuilder queryBuilder = queryBuilder();
        setUniqueWhere(queryBuilder.where(), str, true, str2, true, str3);
        List query = queryBuilder.query();
        if (query.size() > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("More than one ");
            Class<Extensions> cls = Extensions.class;
            sb.append("Extensions");
            sb.append(" existed for unique combination of Extension Name: ");
            sb.append(str);
            sb.append(", Table Name: ");
            sb.append(str2);
            sb.append(", Column Name: ");
            sb.append(str3);
            throw new GeoPackageException(sb.toString());
        } else if (query.size() == 1) {
            return (Extensions) query.get(0);
        } else {
            return null;
        }
    }

    private void setUniqueWhere(Where<Extensions, Void> where, String str, boolean z, String str2, boolean z2, String str3) throws SQLException {
        where.mo19710eq(Extensions.COLUMN_EXTENSION_NAME, str);
        if (z) {
            if (str2 == null) {
                where.and().isNull("table_name");
            } else {
                where.and().mo19710eq("table_name", str2);
            }
        }
        if (!z2) {
            return;
        }
        if (str3 == null) {
            where.and().isNull("column_name");
        } else {
            where.and().mo19710eq("column_name", str3);
        }
    }
}
