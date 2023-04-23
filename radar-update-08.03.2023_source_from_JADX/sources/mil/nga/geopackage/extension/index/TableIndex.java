package mil.nga.geopackage.extension.index;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;
import mil.nga.geopackage.persister.DatePersister;

@DatabaseTable(daoClass = TableIndexDao.class, tableName = "nga_table_index")
public class TableIndex {
    public static final String COLUMN_LAST_INDEXED = "last_indexed";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String TABLE_NAME = "nga_table_index";
    @ForeignCollectionField(eager = false)
    private ForeignCollection<GeometryIndex> geometryIndices;
    @DatabaseField(columnName = "last_indexed", persisterClass = DatePersister.class)
    private Date lastIndexed;
    @DatabaseField(canBeNull = false, columnName = "table_name", mo19322id = true)
    private String tableName;

    public TableIndex() {
    }

    public TableIndex(TableIndex tableIndex) {
        this.tableName = tableIndex.tableName;
        this.lastIndexed = new Date(tableIndex.lastIndexed.getTime());
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String str) {
        this.tableName = str;
    }

    public Date getLastIndexed() {
        return this.lastIndexed;
    }

    public void setLastIndexed(Date date) {
        this.lastIndexed = date;
    }

    public ForeignCollection<GeometryIndex> getGeometryIndices() {
        return this.geometryIndices;
    }
}
