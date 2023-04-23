package mil.nga.geopackage.metadata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(daoClass = MetadataDao.class, tableName = "gpkg_metadata")
public class Metadata {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_METADATA = "metadata";
    public static final String COLUMN_MIME_TYPE = "mime_type";
    public static final String COLUMN_SCOPE = "md_scope";
    public static final String COLUMN_STANDARD_URI = "md_standard_uri";
    public static final String TABLE_NAME = "gpkg_metadata";
    @DatabaseField(canBeNull = false, columnName = "id", mo19322id = true)

    /* renamed from: id */
    private long f352id;
    @DatabaseField(canBeNull = false, columnName = "metadata", defaultValue = "")
    private String metadata;
    @DatabaseField(canBeNull = false, columnName = "mime_type", defaultValue = "text/xml")
    private String mimeType;
    @DatabaseField(canBeNull = false, columnName = "md_scope", defaultValue = "dataset")
    private String scope;
    @DatabaseField(canBeNull = false, columnName = "md_standard_uri")
    private String standardUri;

    public Metadata() {
    }

    public Metadata(Metadata metadata2) {
        this.f352id = metadata2.f352id;
        this.scope = metadata2.scope;
        this.standardUri = metadata2.standardUri;
        this.mimeType = metadata2.mimeType;
        this.metadata = metadata2.metadata;
    }

    public long getId() {
        return this.f352id;
    }

    public void setId(long j) {
        this.f352id = j;
    }

    public MetadataScopeType getMetadataScope() {
        return MetadataScopeType.fromName(this.scope);
    }

    public void setMetadataScope(MetadataScopeType metadataScopeType) {
        this.scope = metadataScopeType.getName();
    }

    public String getStandardUri() {
        return this.standardUri;
    }

    public void setStandardUri(String str) {
        this.standardUri = str;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String str) {
        this.mimeType = str;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public void setMetadata(String str) {
        this.metadata = str;
    }
}
