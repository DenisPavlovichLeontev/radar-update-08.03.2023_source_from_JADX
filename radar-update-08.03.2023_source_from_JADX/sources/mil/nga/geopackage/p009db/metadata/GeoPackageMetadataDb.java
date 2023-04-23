package mil.nga.geopackage.p009db.metadata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.p009db.GeoPackageDatabase;

/* renamed from: mil.nga.geopackage.db.metadata.GeoPackageMetadataDb */
public class GeoPackageMetadataDb extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "geopackage_metadata";
    public static final int DATABASE_VERSION = 1;

    /* renamed from: db */
    private GeoPackageDatabase f341db;

    public GeoPackageMetadataDb(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(GeoPackageMetadata.CREATE_SQL);
        sQLiteDatabase.execSQL(TableMetadata.CREATE_SQL);
        sQLiteDatabase.execSQL(GeometryMetadata.CREATE_SQL);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS geom_metadata");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS geopackage_table");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS geopackage");
        onCreate(sQLiteDatabase);
    }

    public void open() {
        this.f341db = new GeoPackageDatabase(getWritableDatabase());
    }

    /* access modifiers changed from: package-private */
    public GeoPackageDatabase getDb() {
        GeoPackageDatabase geoPackageDatabase = this.f341db;
        if (geoPackageDatabase != null) {
            return geoPackageDatabase;
        }
        throw new GeoPackageException("Database connection is not open");
    }

    public void close() {
        super.close();
        this.f341db = null;
    }
}
