package org.osmdroid.tileprovider.modules;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public class DatabaseFileArchive implements IArchiveFile {
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_PROVIDER = "provider";
    public static final String COLUMN_TILE = "tile";
    public static final String TABLE = "tiles";
    static final String[] tile_column = {COLUMN_TILE};
    private SQLiteDatabase mDatabase;
    private boolean mIgnoreTileSource = false;

    public DatabaseFileArchive() {
    }

    private DatabaseFileArchive(SQLiteDatabase sQLiteDatabase) {
        this.mDatabase = sQLiteDatabase;
    }

    public static DatabaseFileArchive getDatabaseFileArchive(File file) throws SQLiteException {
        return new DatabaseFileArchive(SQLiteDatabase.openDatabase(file.getAbsolutePath(), (SQLiteDatabase.CursorFactory) null, 0));
    }

    public void setIgnoreTileSource(boolean z) {
        this.mIgnoreTileSource = z;
    }

    public Set<String> getTileSources() {
        HashSet hashSet = new HashSet();
        try {
            Cursor rawQuery = this.mDatabase.rawQuery("SELECT distinct provider FROM tiles", (String[]) null);
            while (rawQuery.moveToNext()) {
                hashSet.add(rawQuery.getString(0));
            }
            rawQuery.close();
        } catch (Exception e) {
            Log.w(IMapView.LOGTAG, "Error getting tile sources: ", e);
        }
        return hashSet;
    }

    public void init(File file) throws Exception {
        this.mDatabase = SQLiteDatabase.openDatabase(file.getAbsolutePath(), (SQLiteDatabase.CursorFactory) null, 17);
    }

    public byte[] getImage(ITileSource iTileSource, long j) {
        Cursor cursor;
        byte[] bArr;
        SQLiteDatabase sQLiteDatabase = this.mDatabase;
        if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                Log.d(IMapView.LOGTAG, "Skipping DatabaseFileArchive lookup, database is closed");
            }
            return null;
        }
        try {
            String[] strArr = {COLUMN_TILE};
            long x = (long) MapTileIndex.getX(j);
            long y = (long) MapTileIndex.getY(j);
            long zoom = (long) MapTileIndex.getZoom(j);
            int i = (int) zoom;
            long j2 = (((zoom << i) + x) << i) + y;
            if (!this.mIgnoreTileSource) {
                cursor = this.mDatabase.query("tiles", strArr, "key = " + j2 + " and " + COLUMN_PROVIDER + " = ?", new String[]{iTileSource.name()}, (String) null, (String) null, (String) null);
            } else {
                cursor = this.mDatabase.query("tiles", strArr, "key = " + j2, (String[]) null, (String) null, (String) null, (String) null);
            }
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                bArr = cursor.getBlob(0);
            } else {
                bArr = null;
            }
            cursor.close();
            if (bArr != null) {
                return bArr;
            }
            return null;
        } catch (Throwable th) {
            Log.w(IMapView.LOGTAG, "Error getting db stream: " + MapTileIndex.toString(j), th);
        }
    }

    public InputStream getInputStream(ITileSource iTileSource, long j) {
        try {
            byte[] image = getImage(iTileSource, j);
            ByteArrayInputStream byteArrayInputStream = image != null ? new ByteArrayInputStream(image) : null;
            if (byteArrayInputStream != null) {
                return byteArrayInputStream;
            }
            return null;
        } catch (Throwable th) {
            Log.w(IMapView.LOGTAG, "Error getting db stream: " + MapTileIndex.toString(j), th);
        }
    }

    public void close() {
        this.mDatabase.close();
    }

    public String toString() {
        return "DatabaseFileArchive [mDatabase=" + this.mDatabase.getPath() + "]";
    }
}
