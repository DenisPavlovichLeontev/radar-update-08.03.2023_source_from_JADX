package mil.nga.geopackage.p009db.metadata;

import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p009db.GeoPackageDatabase;

/* renamed from: mil.nga.geopackage.db.metadata.GeoPackageMetadataDataSource */
public class GeoPackageMetadataDataSource {

    /* renamed from: db */
    private GeoPackageDatabase f340db;

    public GeoPackageMetadataDataSource(GeoPackageMetadataDb geoPackageMetadataDb) {
        this.f340db = geoPackageMetadataDb.getDb();
    }

    GeoPackageMetadataDataSource(GeoPackageDatabase geoPackageDatabase) {
        this.f340db = geoPackageDatabase;
    }

    public void create(GeoPackageMetadata geoPackageMetadata) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", geoPackageMetadata.getName());
        contentValues.put(GeoPackageMetadata.COLUMN_EXTERNAL_PATH, geoPackageMetadata.getExternalPath());
        long insert = this.f340db.insert("geopackage", (String) null, contentValues);
        if (insert != -1) {
            geoPackageMetadata.setId(insert);
            return;
        }
        throw new GeoPackageException("Failed to insert GeoPackage metadata. Name: " + geoPackageMetadata.getName() + ", External Path: " + geoPackageMetadata.getExternalPath());
    }

    public boolean delete(GeoPackageMetadata geoPackageMetadata) {
        return delete(geoPackageMetadata.getName());
    }

    public boolean delete(String str) {
        GeoPackageMetadata geoPackageMetadata = get(str);
        if (geoPackageMetadata != null) {
            new TableMetadataDataSource(this.f340db).delete(geoPackageMetadata.getId());
        }
        if (this.f340db.delete("geopackage", "name = ?", new String[]{str}) > 0) {
            return true;
        }
        return false;
    }

    public boolean rename(GeoPackageMetadata geoPackageMetadata, String str) {
        boolean rename = rename(geoPackageMetadata.getName(), str);
        if (rename) {
            geoPackageMetadata.setName(str);
        }
        return rename;
    }

    public boolean rename(String str, String str2) {
        String[] strArr = {str};
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", str2);
        if (this.f340db.update("geopackage", contentValues, "name = ?", strArr) > 0) {
            return true;
        }
        return false;
    }

    public List<GeoPackageMetadata> getAll() {
        ArrayList arrayList = new ArrayList();
        Cursor query = this.f340db.query("geopackage", GeoPackageMetadata.COLUMNS, (String) null, (String[]) null, (String) null, (String) null, (String) null);
        while (query.moveToNext()) {
            try {
                arrayList.add(createGeoPackageMetadata(query));
            } finally {
                query.close();
            }
        }
        return arrayList;
    }

    public List<GeoPackageMetadata> getAllExternal() {
        ArrayList arrayList = new ArrayList();
        Cursor query = this.f340db.query("geopackage", GeoPackageMetadata.COLUMNS, "external_path IS NOT NULL", (String[]) null, (String) null, (String) null, (String) null);
        while (query.moveToNext()) {
            try {
                arrayList.add(createGeoPackageMetadata(query));
            } finally {
                query.close();
            }
        }
        return arrayList;
    }

    public GeoPackageMetadata get(String str) {
        Cursor query = this.f340db.query("geopackage", GeoPackageMetadata.COLUMNS, "name = ?", new String[]{str}, (String) null, (String) null, (String) null);
        try {
            return query.moveToNext() ? createGeoPackageMetadata(query) : null;
        } finally {
            query.close();
        }
    }

    public GeoPackageMetadata get(long j) {
        Cursor query = this.f340db.query("geopackage", GeoPackageMetadata.COLUMNS, "geopackage_id = ?", new String[]{String.valueOf(j)}, (String) null, (String) null, (String) null);
        try {
            return query.moveToNext() ? createGeoPackageMetadata(query) : null;
        } finally {
            query.close();
        }
    }

    public GeoPackageMetadata getOrCreate(String str) {
        GeoPackageMetadata geoPackageMetadata = get(str);
        if (geoPackageMetadata != null) {
            return geoPackageMetadata;
        }
        GeoPackageMetadata geoPackageMetadata2 = new GeoPackageMetadata();
        geoPackageMetadata2.setName(str);
        create(geoPackageMetadata2);
        return geoPackageMetadata2;
    }

    public boolean exists(String str) {
        return get(str) != null;
    }

    public boolean isExternal(String str) {
        return (get(str) == null || get(str).getExternalPath() == null) ? false : true;
    }

    public GeoPackageMetadata getExternalAtPath(String str) {
        Cursor query = this.f340db.query("geopackage", GeoPackageMetadata.COLUMNS, "external_path = ?", new String[]{str}, (String) null, (String) null, (String) null);
        try {
            return query.moveToNext() ? createGeoPackageMetadata(query) : null;
        } finally {
            query.close();
        }
    }

    public List<String> getMetadataWhereNameLike(String str, String str2) {
        return getMetadataWhereNameLike(str, str2, false);
    }

    public List<String> getMetadataWhereNameNotLike(String str, String str2) {
        return getMetadataWhereNameLike(str, str2, true);
    }

    private List<String> getMetadataWhereNameLike(String str, String str2, boolean z) {
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder("name");
        if (z) {
            sb.append(" not");
        }
        sb.append(" like ?");
        Cursor query = this.f340db.query("geopackage", new String[]{"name"}, sb.toString(), new String[]{str}, (String) null, (String) null, str2);
        while (query.moveToNext()) {
            try {
                arrayList.add(query.getString(0));
            } finally {
                query.close();
            }
        }
        return arrayList;
    }

    private GeoPackageMetadata createGeoPackageMetadata(Cursor cursor) {
        GeoPackageMetadata geoPackageMetadata = new GeoPackageMetadata();
        geoPackageMetadata.setId(cursor.getLong(0));
        geoPackageMetadata.setName(cursor.getString(1));
        geoPackageMetadata.setExternalPath(cursor.getString(2));
        return geoPackageMetadata;
    }
}
