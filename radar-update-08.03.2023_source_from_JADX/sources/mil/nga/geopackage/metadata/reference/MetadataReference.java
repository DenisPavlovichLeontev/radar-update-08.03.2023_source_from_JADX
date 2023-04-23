package mil.nga.geopackage.metadata.reference;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.metadata.Metadata;
import mil.nga.geopackage.persister.DatePersister;

@DatabaseTable(daoClass = MetadataReferenceDao.class, tableName = "gpkg_metadata_reference")
public class MetadataReference {
    public static final String COLUMN_COLUMN_NAME = "column_name";
    public static final String COLUMN_FILE_ID = "md_file_id";
    public static final String COLUMN_PARENT_ID = "md_parent_id";
    public static final String COLUMN_REFERENCE_SCOPE = "reference_scope";
    public static final String COLUMN_ROW_ID_VALUE = "row_id_value";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String TABLE_NAME = "gpkg_metadata_reference";
    @DatabaseField(columnName = "column_name")
    private String columnName;
    @DatabaseField(canBeNull = false, columnName = "md_file_id")
    private long fileId;
    @DatabaseField(canBeNull = false, columnName = "md_file_id", foreign = true, foreignAutoRefresh = true)
    private Metadata metadata;
    @DatabaseField(columnName = "md_parent_id")
    private Long parentId;
    @DatabaseField(columnName = "md_parent_id", foreign = true, foreignAutoRefresh = true)
    private Metadata parentMetadata;
    @DatabaseField(canBeNull = false, columnName = "reference_scope")
    private String referenceScope;
    @DatabaseField(columnName = "row_id_value")
    private Long rowIdValue;
    @DatabaseField(columnName = "table_name")
    private String tableName;
    @DatabaseField(canBeNull = false, columnName = "timestamp", defaultValue = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", persisterClass = DatePersister.class)
    private Date timestamp;

    public MetadataReference() {
    }

    public MetadataReference(MetadataReference metadataReference) {
        this.referenceScope = metadataReference.referenceScope;
        this.tableName = metadataReference.tableName;
        this.columnName = metadataReference.columnName;
        this.rowIdValue = metadataReference.rowIdValue;
        this.timestamp = new Date(metadataReference.timestamp.getTime());
        this.metadata = metadataReference.metadata;
        this.fileId = metadataReference.fileId;
        this.parentMetadata = metadataReference.parentMetadata;
        this.parentId = metadataReference.parentId;
    }

    public ReferenceScopeType getReferenceScope() {
        return ReferenceScopeType.fromValue(this.referenceScope);
    }

    /* renamed from: mil.nga.geopackage.metadata.reference.MetadataReference$1 */
    static /* synthetic */ class C11741 {

        /* renamed from: $SwitchMap$mil$nga$geopackage$metadata$reference$ReferenceScopeType */
        static final /* synthetic */ int[] f353xaa4fe692;

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                mil.nga.geopackage.metadata.reference.ReferenceScopeType[] r0 = mil.nga.geopackage.metadata.reference.ReferenceScopeType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f353xaa4fe692 = r0
                mil.nga.geopackage.metadata.reference.ReferenceScopeType r1 = mil.nga.geopackage.metadata.reference.ReferenceScopeType.GEOPACKAGE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f353xaa4fe692     // Catch:{ NoSuchFieldError -> 0x001d }
                mil.nga.geopackage.metadata.reference.ReferenceScopeType r1 = mil.nga.geopackage.metadata.reference.ReferenceScopeType.TABLE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f353xaa4fe692     // Catch:{ NoSuchFieldError -> 0x0028 }
                mil.nga.geopackage.metadata.reference.ReferenceScopeType r1 = mil.nga.geopackage.metadata.reference.ReferenceScopeType.ROW     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f353xaa4fe692     // Catch:{ NoSuchFieldError -> 0x0033 }
                mil.nga.geopackage.metadata.reference.ReferenceScopeType r1 = mil.nga.geopackage.metadata.reference.ReferenceScopeType.COLUMN     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = f353xaa4fe692     // Catch:{ NoSuchFieldError -> 0x003e }
                mil.nga.geopackage.metadata.reference.ReferenceScopeType r1 = mil.nga.geopackage.metadata.reference.ReferenceScopeType.ROW_COL     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: mil.nga.geopackage.metadata.reference.MetadataReference.C11741.<clinit>():void");
        }
    }

    public void setReferenceScope(ReferenceScopeType referenceScopeType) {
        this.referenceScope = referenceScopeType.getValue();
        int i = C11741.f353xaa4fe692[referenceScopeType.ordinal()];
        if (i == 1) {
            setTableName((String) null);
            setColumnName((String) null);
            setRowIdValue((Long) null);
        } else if (i == 2) {
            setColumnName((String) null);
            setRowIdValue((Long) null);
        } else if (i == 3) {
            setColumnName((String) null);
        } else if (i == 4) {
            setRowIdValue((Long) null);
        }
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String str) {
        if (this.referenceScope == null || str == null || !getReferenceScope().equals(ReferenceScopeType.GEOPACKAGE)) {
            this.tableName = str;
            return;
        }
        throw new GeoPackageException("The table name must be null for " + ReferenceScopeType.GEOPACKAGE + " reference scope");
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String str) {
        if (!(this.referenceScope == null || str == null)) {
            ReferenceScopeType referenceScope2 = getReferenceScope();
            if (referenceScope2.equals(ReferenceScopeType.GEOPACKAGE) || referenceScope2.equals(ReferenceScopeType.TABLE) || referenceScope2.equals(ReferenceScopeType.ROW)) {
                throw new GeoPackageException("The column name must be null for " + referenceScope2 + " reference scope");
            }
        }
        this.columnName = str;
    }

    public Long getRowIdValue() {
        return this.rowIdValue;
    }

    public void setRowIdValue(Long l) {
        if (!(this.referenceScope == null || l == null)) {
            ReferenceScopeType referenceScope2 = getReferenceScope();
            if (referenceScope2.equals(ReferenceScopeType.GEOPACKAGE) || referenceScope2.equals(ReferenceScopeType.TABLE) || referenceScope2.equals(ReferenceScopeType.COLUMN)) {
                throw new GeoPackageException("The row id value must be null for " + referenceScope2 + " reference scope");
            }
        }
        this.rowIdValue = l;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date date) {
        this.timestamp = date;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(Metadata metadata2) {
        this.metadata = metadata2;
        this.fileId = metadata2 != null ? metadata2.getId() : -1;
    }

    public long getFileId() {
        return this.fileId;
    }

    public Metadata getParentMetadata() {
        return this.parentMetadata;
    }

    public void setParentMetadata(Metadata metadata2) {
        this.parentMetadata = metadata2;
        this.parentId = Long.valueOf(metadata2 != null ? metadata2.getId() : -1);
    }

    public Long getParentId() {
        return this.parentId;
    }
}
