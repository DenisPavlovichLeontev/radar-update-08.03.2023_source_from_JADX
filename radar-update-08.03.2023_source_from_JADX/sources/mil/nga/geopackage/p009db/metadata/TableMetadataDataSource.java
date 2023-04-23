package mil.nga.geopackage.p009db.metadata;

import android.content.ContentValues;
import android.database.Cursor;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p009db.GeoPackageDatabase;

/* renamed from: mil.nga.geopackage.db.metadata.TableMetadataDataSource */
public class TableMetadataDataSource {

    /* renamed from: db */
    private GeoPackageDatabase f344db;

    public TableMetadataDataSource(GeoPackageMetadataDb geoPackageMetadataDb) {
        this.f344db = geoPackageMetadataDb.getDb();
    }

    TableMetadataDataSource(GeoPackageDatabase geoPackageDatabase) {
        this.f344db = geoPackageDatabase;
    }

    public void create(TableMetadata tableMetadata) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("geopackage_id", Long.valueOf(tableMetadata.getGeoPackageId()));
        contentValues.put("table_name", tableMetadata.getTableName());
        contentValues.put("last_indexed", tableMetadata.getLastIndexed());
        if (this.f344db.insert(TableMetadata.TABLE_NAME, (String) null, contentValues) == -1) {
            throw new GeoPackageException("Failed to insert table metadata. GeoPackage Id: " + tableMetadata.getGeoPackageId() + ", Table Name: " + tableMetadata.getTableName() + ", Last Indexed: " + tableMetadata.getLastIndexed());
        }
    }

    public boolean delete(TableMetadata tableMetadata) {
        return delete(tableMetadata.getGeoPackageId(), tableMetadata.getTableName());
    }

    public int delete(String str) {
        return delete(getGeoPackageId(str));
    }

    public int delete(long j) {
        new GeometryMetadataDataSource(this.f344db).delete(j);
        return this.f344db.delete(TableMetadata.TABLE_NAME, "geopackage_id = ?", new String[]{String.valueOf(j)});
    }

    public boolean delete(String str, String str2) {
        return delete(getGeoPackageId(str), str2);
    }

    public boolean delete(long j, String str) {
        new GeometryMetadataDataSource(this.f344db).delete(j, str);
        if (this.f344db.delete(TableMetadata.TABLE_NAME, "geopackage_id = ? AND table_name = ?", new String[]{String.valueOf(j), str}) > 0) {
            return true;
        }
        return false;
    }

    public boolean updateLastIndexed(TableMetadata tableMetadata, long j) {
        boolean updateLastIndexed = updateLastIndexed(tableMetadata.getGeoPackageId(), tableMetadata.getTableName(), j);
        if (updateLastIndexed) {
            tableMetadata.setLastIndexed(Long.valueOf(j));
        }
        return updateLastIndexed;
    }

    public boolean updateLastIndexed(String str, String str2, long j) {
        return updateLastIndexed(getGeoPackageId(str), str2, j);
    }

    public boolean updateLastIndexed(long j, String str, long j2) {
        String[] strArr = {String.valueOf(j), str};
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_indexed", Long.valueOf(j2));
        if (this.f344db.update(TableMetadata.TABLE_NAME, contentValues, "geopackage_id = ? AND table_name = ?", strArr) > 0) {
            return true;
        }
        return false;
    }

    public TableMetadata get(String str, String str2) {
        return get(getGeoPackageId(str), str2);
    }

    public TableMetadata get(long j, String str) {
        Cursor query = this.f344db.query(TableMetadata.TABLE_NAME, TableMetadata.COLUMNS, "geopackage_id = ? AND table_name = ?", new String[]{String.valueOf(j), str}, (String) null, (String) null, (String) null);
        try {
            return query.moveToNext() ? createTableMetadata(query) : null;
        } finally {
            query.close();
        }
    }

    public TableMetadata getOrCreate(String str, String str2) {
        GeoPackageMetadata orCreate = new GeoPackageMetadataDataSource(this.f344db).getOrCreate(str);
        TableMetadata tableMetadata = get(orCreate.getId(), str2);
        if (tableMetadata != null) {
            return tableMetadata;
        }
        TableMetadata tableMetadata2 = new TableMetadata();
        tableMetadata2.setGeoPackageId(orCreate.getId());
        tableMetadata2.setTableName(str2);
        create(tableMetadata2);
        return tableMetadata2;
    }

    public long getGeoPackageId(String str) {
        GeoPackageMetadata geoPackageMetadata = new GeoPackageMetadataDataSource(this.f344db).get(str);
        if (geoPackageMetadata != null) {
            return geoPackageMetadata.getId();
        }
        return -1;
    }

    private TableMetadata createTableMetadata(Cursor cursor) {
        TableMetadata tableMetadata = new TableMetadata();
        tableMetadata.setGeoPackageId(cursor.getLong(0));
        tableMetadata.setTableName(cursor.getString(1));
        if (!cursor.isNull(2)) {
            tableMetadata.setLastIndexed(Long.valueOf(cursor.getLong(2)));
        }
        return tableMetadata;
    }
}
